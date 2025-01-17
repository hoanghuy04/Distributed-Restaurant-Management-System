package model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street;
    private String ward;
    private String district;
    private String city;
}
