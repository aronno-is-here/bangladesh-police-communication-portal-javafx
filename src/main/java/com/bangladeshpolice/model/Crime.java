package com.bangladeshpolice.model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Crime model class representing individual criminal activities
 */
public class Crime {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty recordId = new SimpleStringProperty();
    private StringProperty crimeType = new SimpleStringProperty();
    private ObjectProperty<LocalDate> crimeDate = new SimpleObjectProperty<>();
    private StringProperty location = new SimpleStringProperty();
    private StringProperty status = new SimpleStringProperty("active");
    private StringProperty description = new SimpleStringProperty();
    private ObjectProperty<java.time.LocalDateTime> createdAt = new SimpleObjectProperty<>();

    // Constructors
    public Crime() {}

    public Crime(String crimeType, LocalDate crimeDate, String location) {
        this.crimeType.set(crimeType);
        this.crimeDate.set(crimeDate);
        this.location.set(location);
        this.createdAt.set(java.time.LocalDateTime.now());
    }

    // Property getters for JavaFX
    public LongProperty idProperty() { return id; }
    public StringProperty recordIdProperty() { return recordId; }
    public StringProperty crimeTypeProperty() { return crimeType; }
    public ObjectProperty<LocalDate> crimeDateProperty() { return crimeDate; }
    public StringProperty locationProperty() { return location; }
    public StringProperty statusProperty() { return status; }
    public StringProperty descriptionProperty() { return description; }
    public ObjectProperty<java.time.LocalDateTime> createdAtProperty() { return createdAt; }

    // Getters and setters
    public Long getId() { return id.get(); }
    public void setId(Long id) { this.id.set(id); }

    public String getRecordId() { return recordId.get(); }
    public void setRecordId(String recordId) { this.recordId.set(recordId); }

    public String getCrimeType() { return crimeType.get(); }
    public void setCrimeType(String crimeType) { this.crimeType.set(crimeType); }

    public LocalDate getCrimeDate() { return crimeDate.get(); }
    public void setCrimeDate(LocalDate crimeDate) { this.crimeDate.set(crimeDate); }

    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public java.time.LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt.set(createdAt); }

    // Utility methods
    public boolean isActive() {
        return "active".equals(getStatus());
    }

    public boolean isResolved() {
        return "resolved".equals(getStatus());
    }

    public boolean isUnderInvestigation() {
        return "under_investigation".equals(getStatus());
    }

    public String getFormattedCrimeDate() {
        if (getCrimeDate() == null) return "";
        return getCrimeDate().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    @Override
    public String toString() {
        return String.format("Crime{id=%d, type='%s', location='%s', status='%s'}",
                           getId(), getCrimeType(), getLocation(), getStatus());
    }
}