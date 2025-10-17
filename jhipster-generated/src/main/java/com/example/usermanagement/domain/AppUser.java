package com.example.usermanagement.domain;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.Instant;

import com.example.usermanagement.domain.enumeration.RoleEnum;

import com.example.usermanagement.domain.enumeration.StatusEnum;


/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 5, max = 191)
    @Column(name = "email", length = 191, nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 8)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleEnum role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnum status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;



    @JsonIgnoreProperties(value = {
        "user",
    }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

        public void setId(Long id) {
        this.id = id;
    }
  
    public String getEmail() {
        return this.email;
    }

    public AppUser email(String email) {
        this.setEmail(email);
        return this;
    }

        public void setEmail(String email) {
        this.email = email;
    }
  
    public String getPassword() {
        return this.password;
    }

    public AppUser password(String password) {
        this.setPassword(password);
        return this;
    }

        public void setPassword(String password) {
        this.password = password;
    }
  
    public RoleEnum getRole() {
        return this.role;
    }

    public AppUser role(RoleEnum role) {
        this.setRole(role);
        return this;
    }

        public void setRole(RoleEnum role) {
        this.role = role;
    }
  
    public StatusEnum getStatus() {
        return this.status;
    }

    public AppUser status(StatusEnum status) {
        this.setStatus(status);
        return this;
    }

        public void setStatus(StatusEnum status) {
        this.status = status;
    }
  
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public AppUser createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

        public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
  
    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public AppUser updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

        public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
  
    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        if (this.userProfile != null) {
            this.userProfile.setUser(null);
        }
        if (userProfile != null) {
            userProfile.setUser(this);
        }
        this.userProfile = userProfile;
    }

    public AppUser userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return getId() != null && getId().equals(((AppUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", role='" + getRole() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
