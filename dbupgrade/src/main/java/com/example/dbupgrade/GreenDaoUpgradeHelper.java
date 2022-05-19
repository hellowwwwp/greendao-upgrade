package com.example.dbupgrade;

import android.database.Cursor;
import android.util.Log;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;

/**
 * @author: wangpan
 * @email: p.wang0813@gmail.com
 * @date: 2020/5/19
 * <p>
 * greendao 数据库升级帮助类
 * 1. 获取旧表的所有字段名, 读取数据库获取
 * 2. 获取新表的所有字段名, 通过 DaoConfig 获取
 * 3. 通过执行 ALTER TABLE table_name ADD column_name datatype 添加新字段
 */
public class GreenDaoUpgradeHelper {

    /**
     * 数据库升级
     */
    public static void upgrade(@NonNull Database database, @NonNull Collection<Class<? extends AbstractDao<?, ?>>> daoClasses) {
        //生成新表如果表不存在
        createTables(database, daoClasses, true);
        //修改表结构
        alterTables(database, daoClasses);
    }

    /**
     * 创建表
     */
    private static void createTables(@NonNull Database database, @NonNull Collection<Class<? extends AbstractDao<?, ?>>> daoClasses, boolean ifNotExists) {
        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
            createTable(database, daoClass, ifNotExists);
        }
    }

    /**
     * 创建表
     */
    private static void createTable(@NonNull Database database, @NonNull Class<? extends AbstractDao<?, ?>> daoClass, boolean ifNotExists) {
        try {
            Method createTableMethod = daoClass.getDeclaredMethod("createTable", Database.class, boolean.class);
            createTableMethod.invoke(null, database, ifNotExists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除表
     */
    private static void dropTable(@NonNull Database database, @NonNull Class<? extends AbstractDao<?, ?>> daoClass, boolean ifExists) {
        try {
            Method createTableMethod = daoClass.getDeclaredMethod("dropTable", Database.class, boolean.class);
            createTableMethod.invoke(null, database, ifExists);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行更新表操作
     */
    private static void alterTables(@NonNull Database database, @NonNull Collection<Class<? extends AbstractDao<?, ?>>> daoClasses) {
        for (Class<? extends AbstractDao<?, ?>> daoClass : daoClasses) {
            DaoConfig daoConfig = new DaoConfig(database, daoClass);
            //获取旧表的所有字段
            Set<String> columnNames = getTableAllColumnNames(database, daoConfig.tablename);
            //检查表内字段是否存在, 不存在则新增
            for (Property property : daoConfig.properties) {
                String columnName = property.columnName;
                if (columnNames.contains(columnName.toUpperCase())
                        || columnNames.contains(columnName.toLowerCase())) {
                    //字段已经存在
                    continue;
                }
                //字段不存在
                String dataType = getDataTypeByProperty(property);
                if (!addTableColumn(database, daoConfig.tablename, columnName, dataType)) {
                    //兜底策略
                    dropTable(database, daoClass, true);
                    createTable(database, daoClass, true);
                }
            }
        }
    }

    /**
     * 添加表字段
     */
    private static boolean addTableColumn(@NonNull Database database, @NonNull String tableName, @NonNull String columnName, @NonNull String dataType) {
        try {
            String sql = "ALTER TABLE " + tableName + " ADD COLUMN '" + columnName + "' " + dataType;
            Log.d("tag", "alterTable: " + sql);
            database.execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取表的所有字段名
     */
    @NonNull
    private static Set<String> getTableAllColumnNames(@NonNull Database database, @NonNull String tableName) {
        Cursor cursor = null;
        String[] columnNames = null;
        try {
            String sql = "SELECT * FROM " + tableName;
            cursor = database.rawQuery(sql, null);
            columnNames = cursor.getColumnNames();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    //no op
                }
            }
        }
        if (columnNames == null) {
            columnNames = new String[0];
        }
        return new HashSet<>(Arrays.asList(columnNames));
    }

    /**
     * 根据字段属性类型返回对应的数据类型
     * 注意: 目前表结构仅支持 INTEGER 和 TEXT
     */
    @NonNull
    private static String getDataTypeByProperty(@NonNull Property property) {
        if (property.type.equals(byte.class) || property.type.equals(Byte.class)
                || property.type.equals(short.class) || property.type.equals(Short.class)
                || property.type.equals(int.class) || property.type.equals(Integer.class)
                || property.type.equals(long.class) || property.type.equals(Long.class)
                || property.type.equals(boolean.class) || property.type.equals(Boolean.class)) {
            return "INTEGER";
        } else {
            return "TEXT";
        }
    }

}
