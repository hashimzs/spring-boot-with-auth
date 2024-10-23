package com.ga.basic_auth.model;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ImageDetails {
    @Column
    private String imageUrl;
    @Column
    private String imageName;
    @Column
    private String contentType;
}
