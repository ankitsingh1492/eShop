package com.eshop.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(
        name = "Catalog",
        indexes = {
                @Index(name = "IX_Catalog_CatalogBrandId", columnList = "CatalogBrandId"),
                @Index(name = "IX_Catalog_CatalogTypeId", columnList = "CatalogTypeId"),
                @Index(name = "IX_Catalog_Name", columnList = "Name")
        })
public class CatalogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "Price", nullable = false, columnDefinition = "numeric")
    private BigDecimal price;

    @Column(name = "PictureFileName")
    private String pictureFileName;

    @Column(name = "CatalogBrandId", nullable = false)
    private int catalogBrandId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CatalogBrandId", insertable = false, updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CatalogBrand catalogBrand;

    @Column(name = "CatalogTypeId", nullable = false)
    private int catalogTypeId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CatalogTypeId", insertable = false, updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CatalogType catalogType;

    @Column(name = "AvailableStock", nullable = false)
    private int availableStock;

    @Column(name = "RestockThreshold", nullable = false)
    private int restockThreshold;

    @Column(name = "MaxStockThreshold", nullable = false)
    private int maxStockThreshold;

    @Column(name = "OnReorder", nullable = false)
    private boolean onReorder;

    @JdbcTypeCode(SqlTypes.OTHER)
    @Column(name = "Embedding", columnDefinition = "vector(384)", insertable = false, updatable = false)
    private String embedding;


    protected CatalogItem() {
    }
    public CatalogItem(String name) {
        this.name = name;
    }
    // getters/setters for the scalar fields used in tests
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public String getPictureFileName() {
        return pictureFileName;
    }
    public int getCatalogBrandId() {
        return catalogBrandId;
    }
    public int getCatalogTypeId() {
        return catalogTypeId;
    }
    public int getAvailableStock() {
        return availableStock;
    }
    public int getRestockThreshold() {
        return restockThreshold;
    }
    public int getMaxStockThreshold() {
        return maxStockThreshold;
    }
    public boolean isOnReorder() {
        return onReorder;
    }
    public String getEmbedding() {
        return embedding;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }
    public void setCatalogBrandId(int catalogBrandId) {
        this.catalogBrandId = catalogBrandId;
    }

    public void setCatalogTypeId(int catalogTypeId) {
        this.catalogTypeId = catalogTypeId;
    }
}
