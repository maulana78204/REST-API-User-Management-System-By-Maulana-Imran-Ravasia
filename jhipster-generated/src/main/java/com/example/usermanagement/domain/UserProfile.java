package com.example.usermanagement.domain;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @NotNull
    @Size(min = 10, max = 15)
    @Column(name = "phone", length = 15, nullable = false)
    private String phone;

    @NotNull
    @Size(min = 10, max = 255)
    @Column(name = "address", length = 255, nullable = false)
    private String address;

    @Column(name = "avatar")
    private String avatar;



    @JsonIgnoreProperties(value = {
        "userProfile",
    }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AppUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

        public void setId(Long id) {
        this.id = id;
    }
  
    public String getFullName() {
        return this.fullName;
    }

    public UserProfile fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

        public void setFullName(String fullName) {
        this.fullName = fullName;
    }
  
    public String getPhone() {
        return this.phone;
    }

    public UserProfile phone(String phone) {
        this.setPhone(phone);
        return this;
    }

        public void setPhone(String phone) {
        this.phone = phone;
    }
  
    public String getAddress() {
        return this.address;
    }

    public UserProfile address(String address) {
        this.setAddress(address);
        return this;
    }

        public void setAddress(String address) {
        this.address = address;
    }
  
    public String getAvatar() {
        return this.avatar;
    }

    public UserProfile avatar(String avatar) {
        this.setAvatar(avatar);
        return this;
    }

        public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
  
    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public UserProfile user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", avatar='" + getAvatar() + "'" +
            "}";
    }
}
