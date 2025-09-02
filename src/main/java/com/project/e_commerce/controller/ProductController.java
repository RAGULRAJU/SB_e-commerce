package com.project.e_commerce.controller;

import com.project.e_commerce.model.Product;
import com.project.e_commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService service;

    @RequestMapping("/")
    public String greet(){
        return "Let's get started";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct(){
       return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){

        Product product = service.getProductById(id);
        if(product != null)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile image){
        System.out.println(product);
        System.out.println(image);
        try{
            Product product1 = service.addProduct(product, image);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int productId){
        Product product = service.getProductById(productId);
        byte[] image = product.getImageData();

        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType()))
                .body(image);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product, @RequestPart MultipartFile image ){
        Product product1 = null;
        try {
            product1 = service.updateProduct(id, product, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(product1 != null)
            return new ResponseEntity<>("Product Updated Successfully",HttpStatus.OK);
        else
            return new ResponseEntity<>("Cannot Update the Product", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = service.getProductById(id);
        if(product != null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products = service.searchProducts(keyword);
        return  new ResponseEntity<>(products, HttpStatus.OK);
    }

}
