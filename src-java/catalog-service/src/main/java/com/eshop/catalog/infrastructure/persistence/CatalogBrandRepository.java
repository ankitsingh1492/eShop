package com.eshop.catalog.infrastructure.persistence;

import com.eshop.catalog.domain.CatalogBrand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogBrandRepository extends JpaRepository<CatalogBrand, Integer> {
}
