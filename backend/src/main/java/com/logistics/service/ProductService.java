package com.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.logistics.entity.Product;
import com.logistics.vo.ProductRequestVO;
import com.logistics.vo.ProductVO;

import java.util.List;

public interface ProductService extends IService<Product> {
    List<Product> getAllProducts();
    List<ProductVO> getAllProductVOs();
    Product getProductById(Long id);
    ProductVO getProductVOById(Long id);
    boolean createProduct(ProductRequestVO productRequestVO);
    boolean updateProduct(Long id, ProductRequestVO productRequestVO);
    boolean deleteProduct(Long id);
    boolean batchDeleteProducts(List<Long> ids);
    
    boolean batchCreateProducts(List<ProductRequestVO> productRequestVOs);
    boolean batchUpdateProducts(List<Product> products);
    
    List<ProductVO> getProductsByName(String name);
    List<ProductVO> getProductsByCategory(String category);
    List<ProductVO> getProductsBySupplier(Long supplierId);
}