package com.i2s.flawlog.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig  {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());  
	
	// 精确到 目录
	static final String PACKAGE = "com.i2s.flawlog.dao";
	static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

	@Value("${mybatis.datasource.url}")
	private String url;

	@Value("${mybatis.datasource.username}")
	private String user;

	@Value("${mybatis.datasource.password}")
	private String password;

	@Value("${mybatis.datasource.driverClassName}")
	private String driverClass;

	@Value("${spring.datasource.initialSize}")  
	private int initialSize;  

	@Value("${spring.datasource.minIdle}")  
	private int minIdle;  

	@Value("${spring.datasource.maxActive}")  
	private int maxActive;  

	@Value("${spring.datasource.maxWait}")  
	private int maxWait;  

	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")  
	private int timeBetweenEvictionRunsMillis;  

	@Value("${spring.datasource.minEvictableIdleTimeMillis}")  
	private int minEvictableIdleTimeMillis;  

	@Value("${spring.datasource.validationQuery}")  
	private String validationQuery;  

	@Value("${spring.datasource.testWhileIdle}")  
	private boolean testWhileIdle;  

	@Value("${spring.datasource.testOnBorrow}")  
	private boolean testOnBorrow;  

	@Value("${spring.datasource.testOnReturn}")  
	private boolean testOnReturn;  

	@Value("${spring.datasource.poolPreparedStatements}")  
	private boolean poolPreparedStatements;  

	@Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")  
	private int maxPoolPreparedStatementPerConnectionSize;  

	@Value("${spring.datasource.filters}")  
	private String filters;  

	@Value("{spring.datasource.connectionProperties}")  
	private String connectionProperties;  


	
	
	@Bean(name = "dataSource")
	@Primary
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		//configuration  
		dataSource.setInitialSize(initialSize);  
		dataSource.setMinIdle(minIdle);  
		dataSource.setMaxActive(maxActive);  
		dataSource.setMaxWait(maxWait);  
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);  
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);  
		dataSource.setValidationQuery(validationQuery);  
		dataSource.setTestWhileIdle(testWhileIdle);  
		dataSource.setTestOnBorrow(testOnBorrow);  
		dataSource.setTestOnReturn(testOnReturn);  
		dataSource.setPoolPreparedStatements(poolPreparedStatements);  
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);  
		 try {  
			 dataSource.setFilters(filters);  
	        } catch (SQLException e) {  
	            logger.error("druid configuration initialization filter", e);  
	        }  
		 dataSource.setConnectionProperties(connectionProperties);  
		return dataSource;
	}

	@Bean(name = "transactionManager")
	@Primary
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean(name = "sqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
		.getResources(DataSourceConfig.MAPPER_LOCATION));
		return sessionFactory.getObject();
	}

}
