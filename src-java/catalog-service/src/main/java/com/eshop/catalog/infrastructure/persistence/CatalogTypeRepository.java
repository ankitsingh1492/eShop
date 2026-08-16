package com.eshop.catalog.infrastructure.persistence;

import com.eshop.catalog.domain.CatalogType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogTypeRepository extends JpaRepository<CatalogType, Integer> {
}
