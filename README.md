### GreenDao database upgrade

- 获取旧表的所有字段名, 读取数据库获取
- 获取新表的所有字段名, 通过 `DaoConfig` 获取
- 通过执行 `ALTER TABLE table_name ADD column_name datatype` 添加新字段
- 使用注解处理器添加 `DAO class`