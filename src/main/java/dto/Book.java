package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {

    private Integer bookId;
    private String isbn;
    private String title;
    private String author;
    private Integer categoryId;
    private String availabilityStatus;


}
