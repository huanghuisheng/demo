package com.tone.dao;

import javax.sql.DataSource;

/**
 * @author ybbk
 * @Date 2012-6-7 下午5:06:16
 */
public class DataSourceBuilder {

    private static final String DATA_SOURCE_NAME_MASTER = "dbMaster";
    private static final String DATA_SOURCE_NAME_SLAVE = "dbSlave";
    private static final String DATA_SOURCE_CONFIG = "db.properties";

    private DataSourceCluster dataSourceCluster = null;
    private DataSourceParser builder = null;

    private DataSourceBuilder() {
        buildDataSources();
    }

    private void buildDataSources() {
        Configuration configure = new Configuration(DATA_SOURCE_CONFIG);
        //创建数据源
        builder = DataSourceParser.builder(configure);
    }

    private static class HolderSingletonHolder {
        static DataSourceBuilder instance = new DataSourceBuilder();
    }

    public static DataSourceBuilder getInstance() {
        return HolderSingletonHolder.instance;
    }

    public DataSourceCluster getDataSourceCluster() {
        return dataSourceCluster;
    }


    public DataSource getDataSource(String dsname) {
        if (builder == null) {
            Configuration configure = new Configuration(DATA_SOURCE_CONFIG);
            builder = DataSourceParser.builder(configure);
        }
        DataSource ds = builder.getDataSource(dsname);
        if (ds == null) {
            ds = builder.getDataSource();
        }
        return ds;
    }
}
