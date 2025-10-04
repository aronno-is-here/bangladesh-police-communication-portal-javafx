package com.bangladeshpolice.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.List;

/**
 * Criminal Record model class representing a criminal's information and history
 */
public class CriminalRecord {
    private StringProperty id = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty age = new SimpleIntegerProperty();
    private StringProperty gender = new SimpleStringProperty();
    private StringProperty hometown = new SimpleStringProperty();
    private StringProperty nationalId = new SimpleStringProperty();
    private ObjectProperty<LocalDate> birthDate = new SimpleObjectProperty<>();
    private StringProperty photoUrl = new SimpleStringProperty();
    private StringProperty createdBy = new SimpleStringProperty();
    private ObjectProperty<java.time.LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private ObjectProperty<java.time.LocalDateTime> updatedAt = new SimpleObjectProperty<>();

    // Related collections
    private ObservableList<Crime> crimes = FXCollections.observableArrayList();

    // Constructors
    public CriminalRecord() {}

    public CriminalRecord(String id, String name, int age, String gender, String nationalId) {
        this.id.set(id);
        this.name.set(name);
        this.age.set(age);
        this.gender.set(gender);
        this.nationalId.set(nationalId);
    }

    // Property getters for JavaFX
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty hometownProperty() { return hometown; }
    public StringProperty nationalIdProperty() { return nationalId; }
    public ObjectProperty<LocalDate> birthDateProperty() { return birthDate; }
    public StringProperty photoUrlProperty() { return photoUrl; }
    public StringProperty createdByProperty() { return createdBy; }
    public ObjectProperty<java.time.LocalDateTime> createdAtProperty() { return createdAt; }
    public ObjectProperty<java.time.LocalDateTime> updatedAtProperty() { return updatedAt; }

    // Getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public int getAge() { return age.get(); }
    public void setAge(int age) { this.age.set(age); }

    public String getGender() { return gender.get(); }
    public void setGender(String gender) { this.gender.set(gender); }

    public String getHometown() { return hometown.get(); }
    public void setHometown(String hometown) { this.hometown.set(hometown); }

    public String getNationalId() { return nationalId.get(); }
    public void setNationalId(String nationalId) { this.nationalId.set(nationalId); }

    public LocalDate getBirthDate() { return birthDate.get(); }
    public void setBirthDate(LocalDate birthDate) { this.birthDate.set(birthDate); }

    public String getPhotoUrl() { return photoUrl.get(); }
    public void setPhotoUrl(String photoUrl) { this.photoUrl.set(photoUrl); }

    public String getCreatedBy() { return createdBy.get(); }
    public void setCreatedBy(String createdBy) { this.createdBy.set(createdBy); }

    public java.time.LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt.set(createdAt); }

    public java.time.LocalDateTime getUpdatedAt() { return updatedAt.get(); }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt.set(updatedAt); }

    // Collection methods
    public ObservableList<Crime> getCrimes() { return crimes; }
    public void setCrimes(List<Crime> crimes) {
        this.crimes.clear();
        if (crimes != null) {
            this.crimes.addAll(crimes);
        }
    }

    public void addCrime(Crime crime) {
        crimes.add(crime);
    }

    public void removeCrime(Crime crime) {
        crimes.remove(crime);
    }

    // Utility methods
    public int getCrimeCount() {
        return crimes.size();
    }

    public List<Crime> getActiveCrimes() {
        return crimes.filtered(crime -> "active".equals(crime.getStatus()));
    }

    public List<Crime> getResolvedCrimes() {
        return crimes.filtered(crime -> "resolved".equals(crime.getStatus()));
    }

    public List<Crime> getCrimesUnderInvestigation() {
        return crimes.filtered(crime -> "under_investigation".equals(crime.getStatus()));
    }

    public String getFormattedBirthDate() {
        if (getBirthDate() == null) return "";
        return getBirthDate().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public String getFormattedCreatedAt() {
        if (getCreatedAt() == null) return "";
        return getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("CriminalRecord{id='%s', name='%s', nationalId='%s'}",
                           getId(), getName(), getNationalId());
    }
}