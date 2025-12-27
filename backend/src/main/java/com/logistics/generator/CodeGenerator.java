package com.logistics.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();
        
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor("Logistics System");
        globalConfig.setOpen(false);
        globalConfig.setDateType(DateType.TIME_PACK);
        globalConfig.setIdType(IdType.AUTO);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setSwagger2(true);
        globalConfig.setEntityName("%s");
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("I%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sController");
        generator.setGlobalConfig(globalConfig);
        
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                "jdbc:mysql://localhost:3306/logistics_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai",
                "root",
                "root"
        )
                .build();
        generator.setDataSource(dataSourceConfig);
        
        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent("com.logistics")
                .moduleName("")
                .entity("entity")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("mapper")
                .xml("mapper.xml")
                .controller("controller")
                .build();
        generator.setPackageInfo(packageConfig);
        
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .addInclude("warehouse", "order", "inventory", "customer", "supplier", "transport")
                .addTablePrefix("t_", "sys_")
                .entityBuilder()
                .enableLombok()
                .enableTableFieldAnnotation()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .idType(IdType.AUTO)
                .formatFileName("%s")
                .logicDeleteColumnName("is_deleted")
                .logicDeletePropertyName("deleted")
                .versionColumnName("version")
                .versionPropertyName("version")
                .addTableFills(new Column("create_time", FieldFill.INSERT))
                .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                .controllerBuilder()
                .enableRestStyle()
                .formatFileName("%sController")
                .serviceBuilder()
                .formatServiceFileName("I%sService")
                .formatServiceImplFileName("%sServiceImpl")
                .mapperBuilder()
                .enableMapperAnnotation()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")
                .build();
        generator.setStrategy(strategyConfig);
        
        InjectionConfig injectionConfig = new InjectionConfig.Builder()
                .beforeOutputFile((tableInfo, objectMap) -> {
                    System.out.println("准备生成文件: " + tableInfo.getName());
                })
                .build();
        generator.setCfg(injectionConfig);
        
        TemplateConfig templateConfig = new TemplateConfig.Builder()
                .disable(TemplateType.CONTROLLER)
                .entity("/templates/entity.java")
                .mapper("/templates/mapper.java")
                .service("/templates/service.java")
                .serviceImpl("/templates/serviceImpl.java")
                .xml("/templates/mapper.xml")
                .build();
        generator.setTemplate(templateConfig);
        
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        
        generator.execute();
    }
}
