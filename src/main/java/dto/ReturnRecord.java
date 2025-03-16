package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReturnRecord {
    private Integer borrowID;
    private Integer memberID;
    private Integer bookID;
    private String dueDate;
    private String memberName;
    private Double fineAmount;
}
