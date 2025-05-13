package DmData.cookie.dto;

import java.time.LocalDateTime;

public class CookieConsentDTO {
    private boolean analyticsAccepted;
    private boolean marketingAccepted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CookieConsentDTO() {}

    public boolean isAnalyticsAccepted() {
        return analyticsAccepted;
    }

    public void setAnalyticsAccepted(boolean analyticsAccepted) {
        this.analyticsAccepted = analyticsAccepted;
    }

    public boolean isMarketingAccepted() {
        return marketingAccepted;
    }

    public void setMarketingAccepted(boolean marketingAccepted) {
        this.marketingAccepted = marketingAccepted;
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
}
