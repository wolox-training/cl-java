package wolox.training.models;

import static wolox.training.contants.ConstantsMain.ATTRIBUTE_CONFLICT_MSG;
import static wolox.training.contants.ConstantsMain.AUTHOR_EMPTY_MSG;
import static wolox.training.contants.ConstantsMain.AUTHOR_INVALID_CHARACTERS_MSG;
import static wolox.training.contants.ConstantsMain.AUTHOR_NULL_MSG;
import static wolox.training.contants.ConstantsMain.IMAGE_EMPTY_MSG;
import static wolox.training.contants.ConstantsMain.IMAGE_MSG;
import static wolox.training.contants.ConstantsMain.IMAGE_NULL_MSG;
import static wolox.training.contants.ConstantsMain.ISBN_EMPTY_MSG;
import static wolox.training.contants.ConstantsMain.ISBN_NULL_MSG;
import static wolox.training.contants.ConstantsMain.PAGES_NULL_MSG;
import static wolox.training.contants.ConstantsMain.PAGES_ZERO_MSG;
import static wolox.training.contants.ConstantsMain.PUBLISHER_EMPTY_MSG;
import static wolox.training.contants.ConstantsMain.PUBLISHER_NULL_MSG;
import static wolox.training.contants.ConstantsMain.SUBTITLE_EMPTY_MSG;
import static wolox.training.contants.ConstantsMain.SUBTITLE_NULL_MSG;
import static wolox.training.contants.ConstantsMain.TITLE_EMPTY_MSG;
import static wolox.training.contants.ConstantsMain.TITLE_NULL_MSG;
import static wolox.training.contants.ConstantsMain.YEAR_BEFORE_MSG;
import static wolox.training.contants.ConstantsMain.YEAR_EMPTY_MSG;
import static wolox.training.contants.ConstantsMain.YEAR_NULL_MSG;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import wolox.training.dtos.BookDTO;
import wolox.training.exceptions.responses.BookAttributeConflict;

/**
 * This class is used to have the model of a Book
 */
@Entity
@ApiModel(description = "Books from the OpenLibraryAPI")
public class Book {

    /**
     * Id of the book
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @ApiModelProperty(notes = "The ID of the book", example = "11")
    private Long id;

    /**
     * Genre of the book
     */
    @ApiModelProperty(notes = "The genre of the book", example = "Review")
    private String genre;

    /**
     * Author of the book
     */
    @NotNull
    @ApiModelProperty(notes = "The author of the book", required = true, example = "David")
    private String author;

    /**
     * Image of the book
     */
    @NotNull
    @ApiModelProperty(notes = "The image of the book", required = true, example = "147852369")
    private String image;

    /**
     * Title of the book
     */
    @NotNull
    @ApiModelProperty(notes = "The title of the book", required = true, example = "PROMPT")
    private String title;

    /**
     * Subtitle of the book
     */
    @ApiModelProperty(notes = "The subtitle of the book", required = true, example = "Real-Time Commit Protocol")
    @NotNull
    private String subtitle;

    /**
     * Publisher of the book
     */
    @NotNull
    @ApiModelProperty(notes = "The publisher of the book", required = true, example = "IEEExplorer")
    private String publisher;

    /**
     * Year of the book
     */
    @NotNull
    @ApiModelProperty(notes = "The year of the book", required = true, example = "2018")
    private String year;

    /**
     * Pages of the book
     */
    @NotNull
    @ApiModelProperty(notes = "The number of pages of the book", required = true, example = "350")
    private Integer pages;

    /**
     * International Standard Book Number of the book
     */
    @NotNull
    @ApiModelProperty(notes = "The ISBN of the book", required = true, example = "1234567898")
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

    public Book(BookDTO bookToMap) {
        try{
            this.author = bookToMap.getAuthors().get(0);
            this.image = IMAGE_MSG;
            this.title = bookToMap.getTitle();
            this.subtitle = bookToMap.getSubtitle();
            this.publisher = bookToMap.getPublishers().get(0);
            this.year = bookToMap.getYear();
            this.pages = bookToMap.getPages();
            this.isbn = bookToMap.getIsbn();
        } catch (RuntimeException ex) {
            throw new BookAttributeConflict(ATTRIBUTE_CONFLICT_MSG);
        }
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
        Preconditions.checkArgument(!author.isEmpty(), AUTHOR_EMPTY_MSG);
        Preconditions.checkNotNull(author,AUTHOR_NULL_MSG);
        Preconditions.checkArgument(author.matches("[a-zA-Z. ]*"), AUTHOR_INVALID_CHARACTERS_MSG);
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        Preconditions.checkNotNull(image, IMAGE_NULL_MSG);
        Preconditions.checkArgument(!image.isEmpty(), IMAGE_EMPTY_MSG);
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Preconditions.checkNotNull(title, TITLE_NULL_MSG);
        Preconditions.checkArgument(!title.isEmpty(), TITLE_EMPTY_MSG);
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        Preconditions.checkNotNull(subtitle, SUBTITLE_NULL_MSG);
        Preconditions.checkArgument(!subtitle.isEmpty(), SUBTITLE_EMPTY_MSG);
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        Preconditions.checkNotNull(publisher, PUBLISHER_NULL_MSG);
        Preconditions.checkArgument(!publisher.isEmpty(), PUBLISHER_EMPTY_MSG);
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {

        Preconditions.checkNotNull(year,YEAR_NULL_MSG);
        Preconditions.checkArgument(!year.isEmpty(), YEAR_EMPTY_MSG);
        Preconditions.checkArgument(Integer.parseInt(year)<= LocalDate.now().getYear(), YEAR_BEFORE_MSG);
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        Preconditions.checkNotNull(pages, PAGES_NULL_MSG);
        Preconditions.checkArgument(pages>0,PAGES_ZERO_MSG);
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        Preconditions.checkNotNull(isbn, ISBN_NULL_MSG);
        Preconditions.checkArgument(!isbn.isEmpty(), ISBN_EMPTY_MSG);
        this.isbn = isbn;
    }

}
