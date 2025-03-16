package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "books")
public class BookEntity {

    private Integer bookId;
    @Id
    private String isbn;
    private String title;
    private String author;
    private Integer categoryId;
    private String availabilityStatus;
}
