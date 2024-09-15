package spring.boot.fainalproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RecyclingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "  roduct Name cannot be null")
    @Size(min = 3, max = 20, message = " product Name must be between 2 and 20 characters")
    @Column(columnDefinition = "varchar(20) not null")
    private String productName;

    @NotNull(message = " quantity cannot be null" )
    @Column(columnDefinition = "int not null")
    private int quantity;

    @NotBlank(message = " description cannot be null")
    @Column(columnDefinition = "varchar(1000) not null")
    private String description;
//
//    @Column(columnDefinition = "varchar(10) default 'PENDING'")
//    @Pattern(regexp = "PENDING|APPROVED|REJECTED|CANCELED", message = "Status must be either PENDING, APPROVED, REJECTED, or CANCELED")
//    private String status = "PENDING";

    @ManyToOne
    @JsonIgnore
    private Facility facility_recycle;

    @ManyToOne
    @JsonIgnore
    private Supplier supplier_recycle;

    @OneToMany(mappedBy = "recyclingRequest", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<PriceOffer> priceOffers = new HashSet<>();

//    @ManyToOne
//    @JsonIgnore
//    private PriceOffer price_offer;

}