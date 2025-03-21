package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Member {
    private Integer memberID;
    private String name;
    private String contactInfo;
    private LocalDate membershipDate;

}
