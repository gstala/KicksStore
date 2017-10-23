package com.stala.grzegorz.model;

import javax.persistence.*;

@Entity
public class ProductSize {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_product_size")
    private Long id;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(targetEntity = Size.class)
    @JoinColumn(name = "size_id")
    private Size size;

    private int quantity;


    public ProductSize() {
    }

    public ProductSize(Product product, Size size, int quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
