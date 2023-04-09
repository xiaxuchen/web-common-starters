package org.originit.et.executor;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import org.originit.et.config.ConvertConfig;
import org.originit.et.converter.DefaultTypeConverter;
import org.originit.et.converter.TypeConverter;
import org.originit.et.info.ColumnInfoAcquirer;
import org.originit.et.info.ModelTableInfoAcquirer;
import org.originit.et.info.impl.JPAColumnInfoAcquirer;
import org.originit.et.info.impl.JPAModelTableInfoAcquirer;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xxc
 */
public abstract class AbstractConvertExecutor implements ConvertExecutor{

    protected Map<Class<?>, TypeConverter> typeMap;

    protected List<org.originit.et.entity.Table> tables;

    /**
     * 最多只能存在两个相同的类型转换器
     */
    private static final int MAX_SAME_TYPE_COUNT = 2;

    protected final ConvertConfig config;

    protected ModelTableInfoAcquirer modelTableInfoAcquirer;

    public AbstractConvertExecutor(ConvertConfig config) {
        Assert.notNull(config, "配置不能为空");
        this.config = config;
        typeMap = new HashMap<>();
        tables = new ArrayList<>();
        // 加载类型转换器
        this.loadTypeConverters();
        // 加载信息获取器
        this.loadAcquirers();
        // 加载模型类的名称
        this.loadModelClasses();
    }

    private void loadAcquirers() {
        if (this.config.getColumnInfoAcquirerClassName() == null) {
            this.config.setColumnInfoAcquirerClassName(JPAColumnInfoAcquirer.class.getName());
        }
        if (this.config.getTableInfoAcquirerClassName() == null) {
            this.config.setTableInfoAcquirerClassName(JPAModelTableInfoAcquirer.class.getName());
        }
        try {
            final ColumnInfoAcquirer columnInfoAcquirer = (ColumnInfoAcquirer) ClassUtil
                    .loadClass(config.getColumnInfoAcquirerClassName()).getConstructor(Map.class)
                    .newInstance(this.typeMap);
            this.modelTableInfoAcquirer = (ModelTableInfoAcquirer) ClassUtil
                    .loadClass(config.getTableInfoAcquirerClassName()).getConstructor(ColumnInfoAcquirer.class).newInstance(columnInfoAcquirer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 整合加载包中所有的模型类和配置的模型类
     */
    private void loadModelClasses() {
        final List<String> packageNames = this.config.getPackageNames();
        List<String> tableClassNames = this.config.getTableClassNames();
        if (tableClassNames == null) {
            tableClassNames = Collections.EMPTY_LIST;
        }
        List<Class> modelClasses = new ArrayList<>();
        if (packageNames != null) {
            for (String packageName : packageNames) {
                Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
                Set<Class<?>> classes = reflections.getAllTypes().stream().map(cs -> ClassUtil.loadClass(cs))
                        .filter(objectClass -> {
                            try {
                                modelTableInfoAcquirer.isTable(objectClass);
                                return true;
                            }catch (Exception e) {
                                return false;
                            }
                        })
                        .collect(Collectors.toSet());
                modelClasses.addAll(classes);
            }
        }
        modelClasses.addAll(tableClassNames.stream().map(c -> ClassUtil.getClass(c)).collect(Collectors.toList()));
        for (Class c : modelClasses) {
            this.modelTableInfoAcquirer.isTable(c);
            tables.add(this.modelTableInfoAcquirer.getTable(c));
        }
    }

    /**
     * 通过SPI机制加载所有的类型转换器
     */
    private void loadTypeConverters() {
        Map<Class<?>, List<TypeConverter>> converterListMap = new HashMap<>();
        // SPI加载所有的TypeConverter
        ServiceLoader<TypeConverter> serviceLoader = ServiceLoader.load(TypeConverter.class);
        final Iterator<TypeConverter> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            final TypeConverter converter = iterator.next();
            List<TypeConverter> typeConverters = converterListMap.get(converter.javaType());
            if (typeConverters == null) {
                typeConverters = new ArrayList<>(MAX_SAME_TYPE_COUNT);
                converterListMap.put(converter.javaType(), typeConverters);
            }
            typeConverters.add(converter);
        }
        // 若有两个同javaType类型的Converter,则选择非默认的
        for (Map.Entry<Class<?>, List<TypeConverter>> classListEntry : converterListMap.entrySet()) {
            if (classListEntry.getValue().size() > MAX_SAME_TYPE_COUNT) {
                throw new IllegalStateException("There are more than 2 TypeConverter for javaType:" + classListEntry.getKey().getName() + ",they are:" + classListEntry.getValue().stream().map(typeConverter -> typeConverter.getClass()).collect(Collectors.toList()));
            }
            TypeConverter cur = null;
            boolean isDefault = true;
            for (TypeConverter typeConverter : classListEntry.getValue()) {
                if (typeConverter instanceof DefaultTypeConverter) {
                    if (isDefault) {
                        cur = typeConverter;
                    }
                } else {
                    if (isDefault) {
                        cur = typeConverter;
                        isDefault = false;
                    } else {
                        throw new IllegalStateException("There are 2 TypeConverter(none is default) for javaType:" + classListEntry.getKey().getName() + ",they are:" + classListEntry.getValue().stream().map(t -> t.getClass()).collect(Collectors.toList()));
                    }
                }
            }
            if (cur != null) {
                this.typeMap.put(classListEntry.getKey(), cur);
            }
        }
    }
}
