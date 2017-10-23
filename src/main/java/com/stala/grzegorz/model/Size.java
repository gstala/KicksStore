package com.stala.grzegorz.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_size")
    private Long id;
    @Column(name = "size_eur")
    private double sizeEUR;
    @Column(name = "length")
    private double lengthInCm;

    @OneToMany(mappedBy = "size")
    private List<ProductSize> productSizeList;

    public Size() {
    }

    public Size(double sizeEUR, double lengthInCm) {
        this.sizeEUR = sizeEUR;
        this.lengthInCm = lengthInCm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSizeEUR() {
        return sizeEUR;
    }

    public void setSizeEUR(double sizeEUR) {
        this.sizeEUR = sizeEUR;
    }

    public double getLengthInCm() {
        return lengthInCm;
    }

    public void setLengthInCm(double lengthInCm) {
        this.lengthInCm = lengthInCm;
    }

    public List<ProductSize> getProductSizeList() {
        return productSizeList;
    }

    public void setProductSizeList(List<ProductSize> productSizeList) {
        this.productSizeList = productSizeList;
    }

    @Override
    public String toString() {
        return "Size{" +
                "id=" + id +
                ", sizeEUR=" + sizeEUR +
                ", lengthInCm=" + lengthInCm +
                '}';
    }
}
