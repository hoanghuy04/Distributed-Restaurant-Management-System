package model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
public class Address {
    @Column(name = "street", columnDefinition = "nvarchar(255)")
    private String street;

    @Column(name = "ward", columnDefinition = "nvarchar(255)")
    private String ward;

    @Column(name = "district", columnDefinition = "nvarchar(255)")
    private String district;

    @Column(name = "city", columnDefinition = "nvarchar(255)")
    private String city;

}
