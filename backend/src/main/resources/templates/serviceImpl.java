package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Map;

<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
@Service
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    <#if table.havePK>
    @Override
    public ${entity} getByIdWithDetails(${table.keyPropertyType} id) {
        return baseMapper.selectByIdWithDetails(id);
    }
    </#if>

    @Override
    public List<${entity}> getByCondition(Map<String, Object> params) {
        return baseMapper.selectByCondition(params);
    }

    @Override
    public int countByCondition(Map<String, Object> params) {
        return baseMapper.countByCondition(params);
    }

    @Override
    public Page<${entity}> pageByCondition(Page<${entity}> page, Map<String, Object> params) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
<#list table.fields as field>
    <#if !field.keyFlag>
        if (params.get("${field.propertyName}") != null) {
            queryWrapper.eq("${field.name}", params.get("${field.propertyName}"));
        }
    </#if>
</#list>
        return page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(${entity} entity) {
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(${entity} entity) {
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(${table.keyPropertyType} id) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCreate(List<${entity}> entities) {
        if (entities == null || entities.isEmpty()) {
            return false;
        }
        return saveBatch(entities, 500);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<${entity}> entities) {
        if (entities == null || entities.isEmpty()) {
            return false;
        }
        return updateBatchById(entities, 500);
    }
}
</#if>
