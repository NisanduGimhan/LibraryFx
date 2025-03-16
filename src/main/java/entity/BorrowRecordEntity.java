package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "borrowrecords")
public class BorrowRecordEntity {

    private Integer borrowID;
    private Integer memberID;
    private Integer bookID;
    private String borrowDate;
    private String returnDate;
    private String dueDate;
}
