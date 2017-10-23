package com.stala.grzegorz.controller;

import com.stala.grzegorz.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@RestController
public class ResourceController {

    @Autowired
    private ProductService productService;


    @RequestMapping(value = "/admin/removeList", method = RequestMethod.POST)
    public String removeList(@RequestBody ArrayList<String> productIdList) {

        for (String id : productIdList) {
            String productId = id.substring(8);

            String name = productId + ".png";
            File fileImage = new File("src/main/resources/static/image/product/" + name);
            if (fileImage.isFile()) {
                try {
                    Files.delete(Paths.get("src/main/resources/static/image/product/" + name));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            productService.removeOne(Long.parseLong(productId));
        }

        return "delete success";
    }

}
