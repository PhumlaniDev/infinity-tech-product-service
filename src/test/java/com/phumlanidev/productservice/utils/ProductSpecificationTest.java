package com.phumlanidev.productservice.utils;

import com.phumlanidev.productservice.model.Product;
import com.phumlanidev.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ProductSpecificationTest.TestAuditingConfig.class)
class ProductSpecificationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Product laptop = createProduct("Gaming Laptop Pro", new BigDecimal("1499.99"));
        Product phone = createProduct("Smartphone X12", new BigDecimal("799.50"));
        Product tablet = createProduct("Tablet Air 2024", new BigDecimal("449.00"));
        Product desktop = createProduct("Office Desktop Basic", new BigDecimal("699.00"));

        entityManager.persist(laptop);
        entityManager.persist(phone);
        entityManager.persist(tablet);
        entityManager.persist(desktop);

        entityManager.flush();
        entityManager.clear();
    }

    private Product createProduct(String name, BigDecimal price) {
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        // p.setCategory("electronics"); // if you uncomment category later
        return p;
    }

    @Test
    @DisplayName("Should find products by partial name (case insensitive)")
    void shouldFilterByNameCaseInsensitive() {
        Specification<Product> spec = ProductSpecification.filterProducts("laptop", null, null);

        List<Product> result = productRepository.findAll(spec);

        assertThat(result)
                .hasSize(1)
                .extracting(Product::getName)
                .containsExactly("Gaming Laptop Pro");
    }

    @Test
    @DisplayName("Should find products within price range")
    void shouldFilterByPriceRange() {
        Specification<Product> spec = ProductSpecification.filterProducts(
                null,
                new BigDecimal("500.00"),
                new BigDecimal("1000.00")
        );
        List<Product> result = findAllWithSpec(spec);

        assertThat(result)
                .hasSize(2)
                .extracting(Product::getName)
                .containsExactlyInAnyOrder(
                        "Office Desktop Basic",
                        "Smartphone X12"
                );
    }

    @Test
    @DisplayName("Should combine name and price filters")
    void shouldCombineNameAndPriceFilter() {
        Specification<Product> spec = ProductSpecification.filterProducts(
                "tab",
                null,
                new BigDecimal("500.00")
        );

        List<Product> result = findAllWithSpec(spec);

        assertThat(result)
                .hasSize(1)
                .extracting(Product::getName)
                .containsExactly("Tablet Air 2024");
    }

    @Test
    @DisplayName("Should return all products when no filters are provided")
    void shouldReturnAllWhenFiltersAreNullOrEmpty() {
        Specification<Product> spec = ProductSpecification.filterProducts("", null, null);

        List<Product> result = findAllWithSpec(spec);

        assertThat(result).hasSize(4);
    }

    @Test
    @DisplayName("Should handle minPrice only")
    void shouldFilterOnlyByMinPrice() {
        Specification<Product> spec = ProductSpecification.filterProducts(
                null,
                new BigDecimal("1000.00"),
                null
        );

        List<Product> result = findAllWithSpec(spec);

        assertThat(result)
                .hasSize(1)
                .extracting(Product::getName)
                .containsExactly("Gaming Laptop Pro");
    }

    @Test
    @DisplayName("Should handle maxPrice only")
    void shouldFilterOnlyByMaxPrice() {
        Specification<Product> spec = ProductSpecification.filterProducts(
                null,
                null,
                new BigDecimal("500.00")
        );

        List<Product> result = findAllWithSpec(spec);

        assertThat(result)
                .hasSize(1)
                .extracting(Product::getName)
                .containsExactly("Tablet Air 2024");
    }

    private List<Product> findAllWithSpec(Specification<Product> spec) {
        return productRepository.findAll(spec);
    }

    static class TestAuditingConfig {
        @Bean
        AuditorAware<String> auditorAwareImpl() {
            return () -> Optional.of("test-auditor");
        }
    }
}