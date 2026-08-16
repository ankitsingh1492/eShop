package com.eshop.catalog;

import com.eshop.catalog.domain.CatalogItem;
import com.eshop.catalog.infrastructure.persistence.CatalogBrandRepository;
import com.eshop.catalog.infrastructure.persistence.CatalogItemRepository;
import com.eshop.catalog.infrastructure.persistence.CatalogTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PostgresTestConfiguration.class)
class CatalogRepositoryTest {
    @Autowired
    private CatalogBrandRepository brands;

    @Autowired
    private CatalogTypeRepository types;

    @Autowired
    private CatalogItemRepository items;

    @Test
    void readsSeededBrandsTypesAndItems() {
        assertEquals(1, brands.findAll().size());
        assertEquals(".NET", brands.findById(1).orElseThrow().getBrand());

        assertEquals(1, types.findAll().size());
        assertEquals("Mug", types.findById(1).orElseThrow().getType());

        CatalogItem item = items.findById(1).orElseThrow();
        assertEquals("Test Mug", item.getName());
        assertEquals(1, item.getCatalogBrandId());
        assertEquals(1, item.getCatalogTypeId());
        assertEquals(0, item.getPrice().compareTo(new BigDecimal("8.50")));
        assertEquals(1, items.findAll().size());
    }
}
