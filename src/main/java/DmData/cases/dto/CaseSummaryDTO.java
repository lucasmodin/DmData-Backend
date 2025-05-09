package DmData.cases.dto;

import java.util.Objects;

public class CaseSummaryDTO {

    private int id;
    private String title;
    private String imagePath;

    public CaseSummaryDTO() {

    }

    public CaseSummaryDTO(int id, String title, String imagePath) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseSummaryDTO)) return false;
        CaseSummaryDTO that = (CaseSummaryDTO) o;
        return id == that.id
                && Objects.equals(title, that.title)
                && Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, imagePath);
    }
}
