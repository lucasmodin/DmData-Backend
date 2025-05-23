package DmData.cookie.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String visitorHash;

    private String country;
    private String city;

    @Column(nullable = false, updatable = false)
    private LocalDateTime visitedAt = LocalDateTime.now();

    public Visit() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVisitorHash() {
        return visitorHash;
    }

    public void setVisitorHash(String visitorHash) {
        this.visitorHash = visitorHash;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(LocalDateTime visitedAt) {
        this.visitedAt = visitedAt;
    }
}
