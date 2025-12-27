package com.logistics.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class EnhancedCodeGenerator {

    private static Properties properties = new Properties();

    static {
        try (InputStream input = EnhancedCodeGenerator.class.getClassLoader().getResourceAsStream("generator.properties")) {
            if (input == null) {
                System.out.println("未找到generator.properties配置文件，使用默认配置");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();
        
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/" + getProperty("generator.outputDir", "src/main/java"));
        globalConfig.setAuthor(getProperty("generator.author", "Logistics System"));
        globalConfig.setOpen(false);
        globalConfig.setDateType(DateType.valueOf(getProperty("generator.dateType", "TIME_PACK")));
        globalConfig.setIdType(IdType.valueOf(getProperty("generator.idType", "AUTO")));
        globalConfig.setBaseResultMap(Boolean.parseBoolean(getProperty("generator.baseResultMap", "true")));
        globalConfig.setBaseColumnList(Boolean.parseBoolean(getProperty("generator.baseColumnList", "true")));
        globalConfig.setSwagger2(Boolean.parseBoolean(getProperty("generator.swagger", "true")));
        globalConfig.setEntityName(getProperty("generator.entityName", "%s"));
        globalConfig.setMapperName(getProperty("generator.mapperName", "%sMapper"));
        globalConfig.setXmlName(getProperty("generator.xmlName", "%sMapper"));
        globalConfig.setServiceName(getProperty("generator.serviceName", "I%sService"));
        globalConfig.setServiceImplName(getProperty("generator.serviceImplName", "%sServiceImpl"));
        globalConfig.setControllerName(getProperty("generator.controllerName", "%sController"));
        generator.setGlobalConfig(globalConfig);
        
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                getProperty("database.url", "jdbc:mysql://localhost:3306/logistics_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai"),
                getProperty("database.username", "root"),
                getProperty("database.password", "root")
        )
                .build();
        generator.setDataSource(dataSourceConfig);
        
        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent(getProperty("generator.parentPackage", "com.logistics"))
                .moduleName(getProperty("generator.moduleName", ""))
                .entity(getProperty("generator.package.entity", "entity"))
                .service(getProperty("generator.package.service", "service"))
                .serviceImpl(getProperty("generator.package.serviceImpl", "service.impl"))
                .mapper(getProperty("generator.package.mapper", "mapper"))
                .xml(getProperty("generator.package.xml", "mapper.xml"))
                .controller(getProperty("generator.package.controller", "controller"))
                .build();
        generator.setPackageInfo(packageConfig);
        
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .addInclude(getProperty("generator.tables", "warehouse,order,inventory,customer,supplier,transport").split(","))
                .addTablePrefix(getProperty("generator.tablePrefix", "t_,sys_").split(","))
                .entityBuilder()
                .enableLombok(Boolean.parseBoolean(getProperty("generator.lombok", "true")))
                .enableTableFieldAnnotation()
                .naming(NamingStrategy.valueOf(getProperty("generator.namingStrategy", "underline_to_camel")))
                .columnNaming(NamingStrategy.valueOf(getProperty("generator.columnNamingStrategy", "underline_to_camel")))
                .idType(IdType.valueOf(getProperty("generator.idType", "AUTO")))
                .formatFileName(getProperty("generator.entityName", "%s"))
                .logicDeleteColumnName(getProperty("generator.logicDeleteField", "is_deleted"))
                .logicDeletePropertyName(getProperty("generator.logicDeletePropertyName", "deleted"))
                .versionColumnName(getProperty("generator.versionField", "version"))
                .versionPropertyName(getProperty("generator.versionPropertyName", "version"))
                .addTableFills(new Column(getProperty("generator.insertFill", "create_time"), FieldFill.INSERT))
                .addTableFills(new Column(getProperty("generator.updateFill", "update_time"), FieldFill.INSERT_UPDATE))
                .controllerBuilder()
                .enableRestStyle(Boolean.parseBoolean(getProperty("generator.restController", "true")))
                .formatFileName(getProperty("generator.controllerName", "%sController"))
                .serviceBuilder()
                .formatServiceFileName(getProperty("generator.serviceName", "I%sService"))
                .formatServiceImplFileName(getProperty("generator.serviceImplName", "%sServiceImpl"))
                .mapperBuilder()
                .enableMapperAnnotation()
                .formatMapperFileName(getProperty("generator.mapperName", "%sMapper"))
                .formatXmlFileName(getProperty("generator.xmlName", "%sMapper"))
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
        
        System.out.println("开始生成代码...");
        System.out.println("数据库URL: " + getProperty("database.url", "jdbc:mysql://localhost:3306/logistics_db"));
        System.out.println("生成表: " + getProperty("generator.tables", "warehouse,order,inventory,customer,supplier,transport"));
        
        generator.execute();
        
        System.out.println("代码生成完成！");
    }
}
