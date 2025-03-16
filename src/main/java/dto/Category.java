package dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class Category {
    private Integer categoryID;
    private String categoryName;
}
