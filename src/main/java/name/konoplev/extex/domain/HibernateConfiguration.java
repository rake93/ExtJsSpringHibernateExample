package name.konoplev.extex.domain;

import java.util.Properties;

import javax.sql.DataSource;

import name.konoplev.extex.domain.entities.Letter;

import org.hibernate.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

@Configuration
public class HibernateConfiguration {
	@Value("#{dataSource}")
	private DataSource dataSource;

	@Bean
	public AnnotationSessionFactoryBean sessionFactoryBean() {
		Properties props = new Properties();
		props.put("hibernate.dialect", MySQLDialect.class.getName());
		props.put("hibernate.format_sql", "true");
		props.put("hibernate.hbm2ddl.auto", "create");
		props.put("hibernate.show_sql", "true");
		props.put("hibernate.connection.useUnicode", "true");
		props.put("hibernate.connection.characterEncoding", "UTF-8");
		props.put("hibernate.connection.charSet", "UTF-8");

		AnnotationSessionFactoryBean bean = new AnnotationSessionFactoryBean();
		bean.setAnnotatedClasses(new Class[] { Letter.class });
		bean.setHibernateProperties(props);
		bean.setDataSource(this.dataSource);
		bean.setSchemaUpdate(true);
		return bean;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactoryBean().getObject());
	}
}
