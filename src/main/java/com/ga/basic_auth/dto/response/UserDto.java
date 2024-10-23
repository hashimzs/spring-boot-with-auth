package com.ga.basic_auth.dto.response;


import com.ga.basic_auth.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserDto {
    private int id;
    private String username;
    private String email;
    private String status;
    private String phoneNo;

    public static UserDto toUserDto(User user){
        return new UserDto(user.getId(), user.getUsername(),user.getEmail(),user.getStatus().toString(), user.getPhoneNo());
    }
}
