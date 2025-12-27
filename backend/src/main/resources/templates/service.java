package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.Map;

<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    <#if table.havePK>
    ${entity} getByIdWithDetails(${table.keyPropertyType} id);
    </#if>

    List<${entity}> getByCondition(Map<String, Object> params);

    int countByCondition(Map<String, Object> params);

    Page<${entity}> pageByCondition(Page<${entity}> page, Map<String, Object> params);

    boolean create(${entity} entity);

    boolean update(${entity} entity);

    boolean delete(${table.keyPropertyType} id);

    boolean batchCreate(List<${entity}> entities);

    boolean batchUpdate(List<${entity}> entities);
}
</#if>
