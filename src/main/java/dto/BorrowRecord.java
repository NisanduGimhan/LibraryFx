package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class BorrowRecord {

    private Integer borrowID;
    private Integer memberID;
    private Integer bookID;
    private String borrowDate;
    private String returnDate;
    private String dueDate;

}
