package com.bangladeshpolice.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * User model class representing a police officer or administrator
 */
public class User {
    private StringProperty id = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty username = new SimpleStringProperty();
    private StringProperty role = new SimpleStringProperty();
    private StringProperty policeId = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty phone = new SimpleStringProperty();
    private StringProperty department = new SimpleStringProperty();
    private StringProperty rank = new SimpleStringProperty();
    private StringProperty profilePhoto = new SimpleStringProperty();
    private BooleanProperty isActive = new SimpleBooleanProperty(true);
    private ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> lastLogin = new SimpleObjectProperty<>();

    // Constructors
    public User() {}

    public User(String id, String name, String username, String role) {
        this.id.set(id);
        this.name.set(name);
        this.username.set(username);
        this.role.set(role);
    }

    // Property getters for JavaFX
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty roleProperty() { return role; }
    public StringProperty policeIdProperty() { return policeId; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty departmentProperty() { return department; }
    public StringProperty rankProperty() { return rank; }
    public StringProperty profilePhotoProperty() { return profilePhoto; }
    public BooleanProperty isActiveProperty() { return isActive; }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
    public ObjectProperty<LocalDateTime> lastLoginProperty() { return lastLogin; }

    // Getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getUsername() { return username.get(); }
    public void setUsername(String username) { this.username.set(username); }

    public String getRole() { return role.get(); }
    public void setRole(String role) { this.role.set(role); }

    public String getPoliceId() { return policeId.get(); }
    public void setPoliceId(String policeId) { this.policeId.set(policeId); }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }

    public String getPhone() { return phone.get(); }
    public void setPhone(String phone) { this.phone.set(phone); }

    public String getDepartment() { return department.get(); }
    public void setDepartment(String department) { this.department.set(department); }

    public String getRank() { return rank.get(); }
    public void setRank(String rank) { this.rank.set(rank); }

    public String getProfilePhoto() { return profilePhoto.get(); }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto.set(profilePhoto); }

    public boolean isActive() { return isActive.get(); }
    public void setActive(boolean active) { this.isActive.set(active); }

    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }

    public LocalDateTime getLastLogin() { return lastLogin.get(); }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin.set(lastLogin); }

    // Utility methods
    public boolean isAdmin() {
        return "admin".equals(getRole());
    }

    public boolean isOfficer() {
        return "officer".equals(getRole());
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', role='%s'}",
                           getId(), getName(), getRole());
    }
}