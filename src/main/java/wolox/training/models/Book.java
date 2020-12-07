package wolox.training.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class is used to have the model of a Book
 */
@Entity
public class Book {

    /**
     * Id of the book
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Genre of the book
     */
    private String genre;

    /**
     * Author of the book
     */
    @Column(nullable = false)
    private String author;

    /**
     * Image of the book
     */
    @Column(nullable = false)
    private String image;

    /**
     * Title of the book
     */
    @Column(nullable = false)
    private String title;

    /**
     * Subtitle of the book
     */
    @Column(nullable = false)
    private String subtitle;

    /**
     * Publisher of the book
     */
    @Column(nullable = false)
    private String publisher;

    /**
     * Year of the book
     */
    @Column(nullable = false)
    private String year;

    /**
     * Pages of the book
     */
    @Column(nullable = false)
    private Integer pages;

    /**
     * International Standard Book Number of the book
     */
    @Column(nullable = false)
    private String isbn;

    public Book() {
    }

    public Book(Long id, String genre, String author, String image, String title,
            String subtitle, String publisher, String year, Integer pages, String isbn) {
        this.id = id;
        this.genre = genre;
        this.author = author;
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
