package com.ga.basic_auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "user_images")
@Entity
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String imageUrl;
    @Column
    private String imageName;
    @Column
    private String contentType;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public void setImageDetails(ImageDetails imageDetails){
        imageUrl=imageDetails.getImageUrl();
        contentType=imageDetails.getContentType();
        imageName=imageDetails.getImageName();
    }

}
