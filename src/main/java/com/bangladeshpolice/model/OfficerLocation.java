package com.bangladeshpolice.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Officer Location model class for tracking officer positions
 */
public class OfficerLocation {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty userId = new SimpleStringProperty();
    private DoubleProperty latitude = new SimpleDoubleProperty();
    private DoubleProperty longitude = new SimpleDoubleProperty();
    private DoubleProperty accuracy = new SimpleDoubleProperty();
    private ObjectProperty<LocalDateTime> timestamp = new SimpleObjectProperty<>();
    private StringProperty roomId = new SimpleStringProperty();

    // Related objects (loaded on demand)
    private User officer;
    private ChatRoom room;

    // Constructors
    public OfficerLocation() {}

    public OfficerLocation(String userId, double latitude, double longitude) {
        this.userId.set(userId);
        this.latitude.set(latitude);
        this.longitude.set(longitude);
        this.timestamp.set(LocalDateTime.now());
    }

    public OfficerLocation(String userId, double latitude, double longitude, double accuracy) {
        this(userId, latitude, longitude);
        this.accuracy.set(accuracy);
    }

    // Property getters for JavaFX
    public LongProperty idProperty() { return id; }
    public StringProperty userIdProperty() { return userId; }
    public DoubleProperty latitudeProperty() { return latitude; }
    public DoubleProperty longitudeProperty() { return longitude; }
    public DoubleProperty accuracyProperty() { return accuracy; }
    public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }
    public StringProperty roomIdProperty() { return roomId; }

    // Getters and setters
    public Long getId() { return id.get(); }
    public void setId(Long id) { this.id.set(id); }

    public String getUserId() { return userId.get(); }
    public void setUserId(String userId) { this.userId.set(userId); }

    public double getLatitude() { return latitude.get(); }
    public void setLatitude(double latitude) { this.latitude.set(latitude); }

    public double getLongitude() { return longitude.get(); }
    public void setLongitude(double longitude) { this.longitude.set(longitude); }

    public Double getAccuracy() { return accuracy.get(); }
    public void setAccuracy(Double accuracy) { this.accuracy.set(accuracy); }

    public LocalDateTime getTimestamp() { return timestamp.get(); }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp.set(timestamp); }

    public String getRoomId() { return roomId.get(); }
    public void setRoomId(String roomId) { this.roomId.set(roomId); }

    // Related object getters/setters
    public User getOfficer() { return officer; }
    public void setOfficer(User officer) { this.officer = officer; }

    public ChatRoom getRoom() { return room; }
    public void setRoom(ChatRoom room) { this.room = room; }

    // Utility methods
    public boolean isRecent() {
        if (getTimestamp() == null) return false;
        return getTimestamp().isAfter(LocalDateTime.now().minusMinutes(5));
    }

    public boolean isAccurate() {
        return getAccuracy() != null && getAccuracy() < 10.0; // Less than 10 meters
    }

    public String getFormattedTimestamp() {
        if (getTimestamp() == null) return "";
        return getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, HH:mm:ss"));
    }

    public String getFormattedCoordinates() {
        return String.format("%.6f, %.6f", getLatitude(), getLongitude());
    }

    public double distanceTo(OfficerLocation other) {
        if (other == null) return 0.0;

        final int R = 6371; // Earth's radius in km

        double lat1Rad = Math.toRadians(getLatitude());
        double lat2Rad = Math.toRadians(other.getLatitude());
        double deltaLatRad = Math.toRadians(other.getLatitude() - getLatitude());
        double deltaLngRad = Math.toRadians(other.getLongitude() - getLongitude());

        double a = Math.sin(deltaLatRad/2) * Math.sin(deltaLatRad/2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(deltaLngRad/2) * Math.sin(deltaLngRad/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c * 1000; // Distance in meters
    }

    @Override
    public String toString() {
        return String.format("OfficerLocation{userId='%s', lat=%.6f, lng=%.6f, time='%s'}",
                           getUserId(), getLatitude(), getLongitude(), getFormattedTimestamp());
    }
}