package com.logistics.service.impl;

import com.logistics.entity.Product;
import com.logistics.mapper.ProductMapper;
import com.logistics.service.ProductService;
import com.logistics.vo.ProductRequestVO;
import com.logistics.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl extends BaseEntityServiceImpl<ProductMapper, Product> implements ProductService {
    @Override
    public String getEntityName() {
        return "产品";
    }

    @Override
    public Class<?> getVOClass() {
        return ProductVO.class;
    }

    @Override
    public Class<?> getRequestVOClass() {
        return ProductRequestVO.class;
    }

    @Override
    public List<Product> getAllProducts() {
        return getAllEntities();
    }

    @Override
    public List<ProductVO> getAllProductVOs() {
        return getAllEntityVOs(this::convertToProductVO);
    }

    @Override
    public Product getProductById(Long id) {
        return getById(id);
    }

    @Override
    public ProductVO getProductVOById(Long id) {
        return getEntityById(id, this::convertToProductVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "product", allEntries = true)
    public boolean createProduct(ProductRequestVO productRequestVO) {
        return createEntity(productRequestVO, Product.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "product", allEntries = true)
    public boolean updateProduct(Long id, ProductRequestVO productRequestVO) {
        return updateEntity(id, productRequestVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "product", allEntries = true)
    public boolean deleteProduct(Long id) {
        return deleteEntity(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "product", allEntries = true)
    public boolean batchDeleteProducts(List<Long> ids) {
        return batchDeleteEntities(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "product", allEntries = true)
    public boolean batchCreateProducts(List<ProductRequestVO> productRequestVOs) {
        return batchCreateEntities(productRequestVOs, Product.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "product", allEntries = true)
    public boolean batchUpdateProducts(List<Product> products) {
        return batchUpdateEntities(products);
    }

    @Override
    public List<ProductVO> getProductsByName(String name) {
        List<Product> products = baseMapper.selectProductsByName(name);
        return products.stream().map(this::convertToProductVO).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<ProductVO> getProductsByCategory(String category) {
        List<Product> products = baseMapper.selectProductsByCategory(category);
        return products.stream().map(this::convertToProductVO).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<ProductVO> getProductsBySupplier(Long supplierId) {
        List<Product> products = baseMapper.selectProductsBySupplier(supplierId);
        return products.stream().map(this::convertToProductVO).collect(java.util.stream.Collectors.toList());
    }

    private ProductVO convertToProductVO(Product product) {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        return productVO;
    }
}