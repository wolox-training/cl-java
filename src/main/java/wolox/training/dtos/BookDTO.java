package wolox.training.dtos;

import static wolox.training.contants.ConstantsMain.ATTRIBUTE_CONFLICT_MSG;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import wolox.training.exceptions.responses.BookAttributeConflict;

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

    public BookDTO(LinkedHashMap data, String isbn) {
        try {
            List<String> authorNames = new ArrayList<>();
            List<String> publisherNames = new ArrayList<>();
            this.isbn = isbn;
            this.title = data.get("title").toString();
            this.subtitle = data.get("subtitle").toString();
            this.year = data.get("publish_date").toString();
            this.pages = (Integer) data.get("number_of_pages");
            List<LinkedHashMap> publishers = new ArrayList<>(
                    (Collection<? extends LinkedHashMap>) data.get("publishers")
            );
            for (LinkedHashMap p : publishers) {
                publisherNames.add(p.get("name").toString());
            }
            this.publishers = publisherNames;

            List<LinkedHashMap> authors = new ArrayList<>(
                    (Collection<? extends LinkedHashMap>) data.get("authors")
            );
            for (LinkedHashMap a : authors) {
                authorNames.add(a.get("name").toString());
            }
            this.authors = authorNames;
        } catch (RuntimeException ex) {
            throw new BookAttributeConflict(ATTRIBUTE_CONFLICT_MSG);
        }
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
