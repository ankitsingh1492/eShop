package com.eshop.catalog.infrastructure.persistence;

import com.eshop.catalog.domain.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, Integer> {
}
