package DmData.cookie.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cookie_consent")
public class CookieConsent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 36)
    private String visitorId;

    @Column(nullable = false)
    private boolean analyticsAccepted = false;

    @Column(nullable = false)
    private boolean marketingAccepted = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public CookieConsent() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public boolean isAnalyticsAccepted() {
        return analyticsAccepted;
    }

    public void setAnalyticsAccepted(boolean analyticsAccepted) {
        this.analyticsAccepted = analyticsAccepted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isMarketingAccepted() {
        return marketingAccepted;
    }

    public void setMarketingAccepted(boolean marketingAccepted) {
        this.marketingAccepted = marketingAccepted;
    }
}
