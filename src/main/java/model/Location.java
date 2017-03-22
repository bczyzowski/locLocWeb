package model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.time.LocalDateTime;


@Entity
@NamedQueries({
        @NamedQuery(name = "Location.getLocations", query = "SELECT l FROM Location l WHERE l.user.id = :id"),
        @NamedQuery(name = "Location.getLocationsByMonthAndYear", query = "SELECT l FROM Location l JOIN l.user WHERE l.user.id = :id AND YEAR(l.time) = :year AND MONTH(l.time) = :month AND DAY(l.time) = :day"),
        @NamedQuery(name = "Location.getLastLocation", query = "SELECT l FROM Location l JOIN l.user WHERE l.user.id = :id AND l.id = (SELECT MAX(l.id) FROM  Location l  JOIN l.user WHERE l.user.id = :id)")
})
@XmlRootElement
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;
    private double latitude, longitude;
    private float accuracy;
    private LocalDateTime time;
    @OneToOne
    private User user;

    public Location() {
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(double latitude, double longitude, LocalDateTime time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public Location(double latitude, double longitude, float accuracy, LocalDateTime time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
