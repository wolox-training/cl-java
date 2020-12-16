package wolox.training.dtos;

import java.util.List;

public class BookDTO {

    private String isbn;
    private String title;
    private String subtitle;
    private List<String> publishers;
    private String year;
    private Integer pages;
    private List<String> authors;

    public BookDTO() {
    }

    public BookDTO(String isbn, String title, String subtitle,
            List<String> publishers, String year, Integer pages,
            List<String> authors) {
        this.isbn = isbn;
        this.title = title;
        this.subtitle = subtitle;
        this.publishers = publishers;
        this.year = year;
        this.pages = pages;
        this.authors = authors;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
}
