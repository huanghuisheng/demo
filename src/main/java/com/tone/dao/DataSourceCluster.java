package com.tone.dao;

import javax.sql.DataSource;

/**
*
* 多数据源集群下的数据源群. 目前只用单数据源,待做好数据读写分离,分割之后,再行扩展.
* 目前只实现读写分离
* 
* @author ybbk
* @date $Date: 2010-05-08 17:05:05 +0800 (星期六, 08 五月 2010) $
* @version $Revision: 9412 $
*/
public class DataSourceCluster {

   private DataSource readableDataSource;
   private DataSource writeableDataSource;

   public DataSourceCluster(){
   }
   public DataSourceCluster(DataSource writeableDataSource, DataSource readableDataSource){
   	setWriteableDataSource(writeableDataSource);
   	setReadableDataSource(readableDataSource);
   }
  
   /** 只读数据源 用于从库 */
   public DataSource getReadableDataSource() {
       return readableDataSource;
   }

   /** 只读数据源 用于从库 */
   public void setReadableDataSource(DataSource readableDataSource) {
       this.readableDataSource = readableDataSource;
   }

   /** 读写数据源, 用于主库 */
   public DataSource getWriteableDataSource() {
       return writeableDataSource;
   }

   /** 读写数据源, 用于主库 */
   public void setWriteableDataSource(DataSource writeableDataSource) {
       this.writeableDataSource = writeableDataSource;
   }
}