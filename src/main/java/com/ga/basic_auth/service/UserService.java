package com.ga.basic_auth.service;

import com.ga.basic_auth.dto.request.ChangePasswordRequestDto;
import com.ga.basic_auth.dto.request.LoginRequestDto;
import com.ga.basic_auth.dto.response.LoginResponseDto;
import com.ga.basic_auth.dto.response.ResponseTemplate;
import com.ga.basic_auth.dto.response.UserDto;
import com.ga.basic_auth.exception.InformationExistsException;
import com.ga.basic_auth.exception.InformationNotFoundException;
import com.ga.basic_auth.model.*;
import com.ga.basic_auth.repository.IUserImageRepository;
import com.ga.basic_auth.repository.IUserRepository;
import com.ga.basic_auth.util.EmailUtil;
import com.ga.basic_auth.util.FileUtil;
import com.ga.basic_auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService implements BaseService{

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    @Lazy
    private User currUser;

    @Autowired
    private IUserImageRepository userImageRepository;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    @Lazy
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder ;

    @Autowired
    @Lazy
    private EmailUtil emailUtil;


    public User saveUser(User user){
        user.setId(0);
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = this.userRepository.save(user);
        return createdUser;
    }

    public ResponseEntity<?> registerUser(User user) {
        removeUnactivatedUser(user.getEmail());

        user.setId(0);
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.UNVERIFIED);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User createdUser = this.userRepository.save(user);

        String token = jwtUtil.generateVerificationToken(createdUser);

        StringBuilder body = new StringBuilder();
        body.append("Please click on the following link to verify your account:\n\n");
        body.append("http://localhost:5000/auth/users/verify?token=" + token + "\n\n");
        emailUtil.sendEmail(user.getEmail(), "Verification", body.toString());

        return new ResponseTemplate(HttpStatus.OK,"verification email ");
    }

    private void removeUnactivatedUser(String email) {
        try {
            User user = findByEmail(email);

            if (user.getStatus().equals(UserStatus.UNVERIFIED)) {
                this.userRepository.deleteById(user.getId());
                return;
            }

            throw new InformationExistsException("a user with the same email is already registered");

        } catch (InformationNotFoundException e) {

        }
    }

    public ResponseEntity<?> verifyEmail(String token) {
        try {
            if (!jwtUtil.validateVerificationToken(token))
                return ResponseEntity.ok("<h2><center>The email verification link has expired</center></h2>");

            String name = jwtUtil.getUserNameFromVerificationToken(token);

            Optional<User> optionalUser = userRepository.findByUsername(name);

            if(optionalUser.isEmpty())
                throw new InformationNotFoundException("");

            User user=optionalUser.get();

            user.setStatus(UserStatus.ACTIVE);

            this.userRepository.save(user);

            return ResponseEntity.ok("<h2><center>Email has been verified</center></h2>");
        } catch (InformationNotFoundException e) {
            return ResponseEntity.ok("<h2><center>The email verification link has expired</center></h2>");
        }
    }

    public ResponseEntity<?> changePassword(ChangePasswordRequestDto requestDto) {
       currUser=getCurrUser();

        if (!passwordEncoder.matches(requestDto.getOldPassword(), currUser.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid password");

        currUser.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        this.userRepository.save(currUser);
        return new ResponseTemplate(HttpStatus.OK,"Password has been changed");
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        Optional<User> userOptional = this.userRepository.findByEmail(email);

        if (userOptional.isEmpty())
            throw new InformationNotFoundException("No user with the email " + email + " was found");

        return userOptional.get();
    }
    public User findByUsername(String email) {
        Optional<User> userOptional = this.userRepository.findByUsername(email);

        if (userOptional.isEmpty())
            throw new InformationNotFoundException("No user with the email " + email + " was found");

        return userOptional.get();
    }

    public void deleteUserById(int id){
        Optional<User> user= this.userRepository.findById(id);

        if(user.isEmpty())
            throw new InformationNotFoundException("No user with a matching id was found");

        user.get().setStatus(UserStatus.UNACTIVE);

        this.userRepository.save(user.get());
    }

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                (loginRequestDto.getEmail(), loginRequestDto.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            User myUserDetails = (User) authentication.getPrincipal();
            final String jwt = jwtUtil.generateToken(myUserDetails);
            return ResponseEntity.ok(new LoginResponseDto(jwt, "success"));

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body(new LoginResponseDto("", "invalid email or password"));
        }
    }

    public UserDto updateUser(UserDto user){
        Optional<User> userOptional=this.userRepository.findById(user.getId());

        if(userOptional.isEmpty())
            throw new InformationNotFoundException("no user with a matching id was found");

        User userToUpdate=userOptional.get();

        userToUpdate.setUsername(user.getPhoneNo());

        this.userRepository.save(userToUpdate);

        return UserDto.toUserDto(userToUpdate);
    }

    public ImageDetails saveImage(MultipartFile file) throws IOException {
        deleteUserImage();
        UserImage userImage=new UserImage();

        ImageDetails imageDetails=fileUtil.handleFileUpload(file);
        userImage.setImageDetails(imageDetails);
        userImage.setUser(getCurrUser());

        this.userImageRepository.save(userImage);

        return imageDetails;
    }

    public void deleteUserImage() throws IOException {
        User user=getCurrUser();

        Optional<UserImage> userImage=this.userImageRepository.findByUserId(user.getId());

        if(userImage.isEmpty())
            return;

        fileUtil.deleteImage(userImage.get().getImageName());

        this.userImageRepository.deleteById(userImage.get().getId());
    }

}
