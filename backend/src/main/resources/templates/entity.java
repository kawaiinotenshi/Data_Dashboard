package ${package.Entity};

<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
</#if>
<#if entitySerialVersionUID>
import java.io.Serializable;
</#if>
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;

<#if swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>

<#if entityLombokModel>
@Data
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
</#if>
<#if table.convert>
@TableName("${schemaName}${table.name}")
</#if>
<#if swagger>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
<#if entitySerialVersionUID>
public class ${entity} implements Serializable {
<#else>
public class ${entity} {
</#if>
<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>

<#-- 主键 -->
<#if table.havePK>
    <#if table.idConvert>
    @TableId(value = "${table.name}.${table.keyProperty}", type = IdType.AUTO)
    <#else>
    @TableId(value = "${table.keyProperty}", type = IdType.AUTO)
    </#if>
    @ApiModelProperty(value = "主键ID")
    private ${table.keyPropertyType} ${table.keyProperty};
</#if>

<#-- 普通字段 -->
<#list table.fields as field>
    <#if !field.keyFlag>
    <#if field.comment!?length gt 0>
    @ApiModelProperty(value = "${field.comment}")
    </#if>
    <#if field.convert>
    @TableField("${field.name}")
    </#if>
    <#if field.logicDelete>
    @TableLogic
    </#if>
    <#if field.versionField>
    @Version
    </#if>
    <#if field.fill??>
    @TableField(fill = FieldFill.${field.fill})
    </#if>
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>

<#if entityLombokModel>
    @Override
    public String toString() {
        return "${entity}{" +
        <#list table.fields as field>
            <#if field_index==0>
            "${field.propertyName}=" + ${field.propertyName} +
            <#else>
            ", ${field.propertyName}=" + ${field.propertyName} +
            </#if>
        </#list>
        "}";
    }
</#if>
}
