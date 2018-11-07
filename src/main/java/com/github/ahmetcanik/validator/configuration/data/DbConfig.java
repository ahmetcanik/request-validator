package com.github.ahmetcanik.validator.configuration.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(basePackages = {"com.github.ahmetcanik.validator.data"}, entityManagerFactoryRef = "validatorEntityManagerFactory",
		transactionManagerRef = "validatorTransactionManager")
public class DbConfig {

	private final Environment env;

	@Autowired
	public DbConfig(Environment env) {
		this.env = env;
	}

	@Bean
	public DataSource validatorDataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(env.getProperty("validator.jdbc.url", "jdbc:mysql://localhost:3306/validator"));
		dataSource.setDriverClassName(env.getProperty("validator.jdbc.driverClassName", "com.mysql.cj.jdbc.Driver"));
		dataSource.setUsername(env.getProperty("validator.jdbc.username", "validator"));
		dataSource.setPassword(env.getProperty("validator.jdbc.password", "validator"));

		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean validatorEntityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(validatorDataSource());
		em.setPackagesToScan("com.github.ahmetcanik.validator.data.entity");

		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		final HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.globally_quoted_identifiers", env.getProperty("hibernate.globally_quoted_identifiers"));
		properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		em.setJpaPropertyMap(properties);
		return em;
	}

	@Bean
	public PlatformTransactionManager validatorTransactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(validatorEntityManagerFactory().getObject());
		return transactionManager;
	}

}
