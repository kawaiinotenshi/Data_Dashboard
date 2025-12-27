package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
@Mapper
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

    <#if table.havePK>
    ${entity} selectByIdWithDetails(@Param("id") ${table.keyPropertyType} id);
    </#if>

    List<${entity}> selectByCondition(@Param("params") Map<String, Object> params);

    int countByCondition(@Param("params") Map<String, Object> params);
}
</#if>
