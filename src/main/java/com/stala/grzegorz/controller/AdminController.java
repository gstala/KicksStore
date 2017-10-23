package com.stala.grzegorz.controller;

import com.stala.grzegorz.model.Product;
import com.stala.grzegorz.model.ProductSize;
import com.stala.grzegorz.model.Size;
import com.stala.grzegorz.service.ProductService;
import com.stala.grzegorz.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private ProductService productService;

    @Autowired
    private SizeService sizeService;

    @RequestMapping
    public String adminHome() {
        return "admin/adminHome";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addProduct(Model model) {
        Product product = new Product();

        List<ProductSize> productSizeList = new ArrayList<>();
        List<Size> sizeList = sizeService.findAll();
        for (Size size : sizeList) {
            ProductSize productSize = new ProductSize();
            productSize.setProduct(product);
            productSize.setQuantity(0);
            productSize.setSize(size);
            productSizeList.add(productSize);
        }
        product.setProductSizeList(productSizeList);

        model.addAttribute("product", product);

        return "admin/addProduct";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addProductPost(@ModelAttribute("product") Product product) {

        product.getProductSizeList().forEach(productSize -> productSize.setProduct(product));
        productService.save(product);

        MultipartFile productImage = product.getProductImage();

        try {
            byte[] bytes = productImage.getBytes();
            String name = product.getId() + ".png";
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
            stream.write(bytes);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:productList";
    }

    @RequestMapping("/productList")
    public String productList(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);

        return "admin/adminProductList";
    }

    @RequestMapping("/productInfo")
    public String productInfo(@RequestParam("id") Long id, Model model) {
        Product product = productService.findOne(id);
        model.addAttribute("product", product);

        return "admin/adminProductInfo";
    }

    @RequestMapping("/updateProduct")
    public String updateProduct(@RequestParam("id") Long id, Model model) {
        Product product = productService.findOne(id);
        model.addAttribute("product", product);

        return "admin/updateProduct";
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public String updateProductPost(@ModelAttribute("product") Product product, HttpServletRequest request) {
        productService.save(product);
        System.out.println("UPDATE " + product);
        MultipartFile productImage = product.getProductImage();

        if (!productImage.isEmpty()) {
            try {
                byte[] bytes = productImage.getBytes();
                String name = product.getId() + ".png";

                Files.delete(Paths.get("src/main/resources/static/image/product/" + name));

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(new File("src/main/resources/static/image/product/" + name)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:/admin/productInfo?id=" + product.getId();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(@ModelAttribute("id") String id, Model model) {
        Product product = productService.findOne(Long.parseLong(id.substring(11)));

        String name = product.getId() + ".png";
        File fileImage = new File("src/main/resources/static/image/product/" + name);
        if (fileImage.isFile()) {
            try {
                Files.delete(Paths.get("src/main/resources/static/image/product/" + name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        productService.removeOne(product.getId());
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);

        return "redirect:/admin/productList";
    }
}
