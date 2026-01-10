package com.logistics.controller;

import com.logistics.common.Result;
import com.logistics.service.ProductService;
import com.logistics.vo.ProductVO;
import com.logistics.vo.ProductRequestVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public Result<List<ProductVO>> getAllProducts() {
        List<ProductVO> list = productService.getAllProductVOs();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<ProductVO> getProductById(@PathVariable Long id) {
        ProductVO productVO = productService.getProductVOById(id);
        return Result.success(productVO);
    }

    @PostMapping
    public Result<Void> createProduct(@RequestBody ProductRequestVO productRequestVO) {
        productService.createProduct(productRequestVO);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequestVO productRequestVO) {
        productService.updateProduct(id, productRequestVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        boolean success = productService.deleteProduct(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除产品失败");
        }
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDeleteProducts(@RequestBody List<Long> ids) {
        boolean success = productService.batchDeleteProducts(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error("批量删除产品失败");
        }
    }

    @GetMapping("/name/{name}")
    public Result<List<ProductVO>> getProductsByName(@PathVariable String name) {
        List<ProductVO> list = productService.getProductsByName(name);
        return Result.success(list);
    }

    @GetMapping("/category/{category}")
    public Result<List<ProductVO>> getProductsByCategory(@PathVariable String category) {
        List<ProductVO> list = productService.getProductsByCategory(category);
        return Result.success(list);
    }

    @GetMapping("/supplier/{supplierId}")
    public Result<List<ProductVO>> getProductsBySupplier(@PathVariable Long supplierId) {
        List<ProductVO> list = productService.getProductsBySupplier(supplierId);
        return Result.success(list);
    }
}