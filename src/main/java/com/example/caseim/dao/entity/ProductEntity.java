package com.example.caseim.dao.entity;

import com.example.caseim.dao.entity.enums.PriceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table(name = "products")
@Entity
@AllArgsConstructor
//@NoArgsConstructor
@Data
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Double price;
    private String caseCode;
    private Integer viewCount;
    private String description;
    private String opportunities;
    private Integer starNumber;
    private Integer voteCount;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PriceType priceType = PriceType.AZN;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private ModelEntity model;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private BrandEntity brand;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "product_colors",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id"))
    private Set<ColorEntity> colors;

    @OneToMany(mappedBy = "product")
    private List<ProductPictureEntity> picture;

    public ProductEntity() {
    }
}
