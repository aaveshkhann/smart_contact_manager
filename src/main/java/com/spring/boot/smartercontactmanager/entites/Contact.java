package com.spring.boot.smartercontactmanager.entites;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name="contact_table")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cId;
    @NotBlank(message="Name is Required")
    private String cName;
    private String secondName;
    private String work;
    @Column(unique = true)
    private String email;
    @NotBlank(message="Number is Required")
    private String phone;
    private String image;
    @Column(length = 500)
    private String description;
    
    @ManyToOne
    private User user;

    // Constructors
    public Contact() {}

    // Getters and Setters

    public int getcId() {
        return cId;
    }
    public void setcId(int cId) {
        this.cId = cId;
    }
    public String getcName() {
        return cName;
    }
    public void setcName(String cName) {
        this.cName = cName;
    }
    public String getSecondName() {
        return secondName;
    }
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    public String getWork() {
        return work;
    }
    public void setWork(String work) {
        this.work = work;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;   
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Contact [cId=" + cId + ", cName=" + cName + ", secondName=" + secondName + ", work=" + work + ", email="
                + email + ", phone=" + phone + ", image=" + image + ", description=" + description + ", user=" + user
                + "]";
    }
}
