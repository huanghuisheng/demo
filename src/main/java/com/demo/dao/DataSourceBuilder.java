package com.demo.dao;

import javax.sql.DataSource;

//import com.tonetime.commons.database.Configuration;
//import com.tonetime.commons.database.DataSourceBuilder;
//import com.tonetime.commons.database.DataSourceCluster;
//import com.tonetime.commons.database.DataSourceParser;
//import com.tonetime.commons.database.DataSourceBuilder.HolderSingletonHolder;

/**
 * @author ybbk
 * @Date 2012-6-7 下午5:06:16
 */
public class DataSourceBuilder {

    private static final String DATA_SOURCE_NAME_MASTER = "dbMaster";
    private static final String DATA_SOURCE_NAME_SLAVE = "dbSlave";
    private static final String DATA_SOURCE_CONFIG = "db.properties";

    private DataSourceCluster dataSourceCluster = null;

    private DataSourceBuilder() {
        buildDataSources();
    }

    private void buildDataSources() {
        Configuration configure = new Configuration(DATA_SOURCE_CONFIG);
        //创建数据源
        DataSourceParser builder = DataSourceParser.builder(configure);
        DataSource dsMaster = builder.getDataSource(DATA_SOURCE_NAME_MASTER);
        if (dsMaster == null) {
            dsMaster = builder.getDataSource();
        }
        DataSource dbSlave = builder.getDataSource(DATA_SOURCE_NAME_SLAVE);
        if (dbSlave == null) {
            dbSlave = dsMaster;
        }
        dataSourceCluster = new DataSourceCluster(dsMaster, dbSlave);
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
}
