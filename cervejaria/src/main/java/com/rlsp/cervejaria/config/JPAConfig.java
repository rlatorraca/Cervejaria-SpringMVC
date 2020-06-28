package com.rlsp.cervejaria.config;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.repository.CervejasRepository;

/**
 * Faz a configuracao do SPRING DATA JPA
 * @author rlatorraca
 *
 */
@Configuration
@ComponentScan(basePackageClasses = CervejasRepository.class)
@EnableJpaRepositories(basePackages = "com.rlsp.cervejaria.repository", enableDefaultTransactions = false) // Para encontrar os repositorios no PACOTE de CervejasRepository
@EnableTransactionManagement //Diz para o Spring que o PROGRAMADOR vai gerenciar as transacoes com o DB [enableDefaultTransactions = false, acima necessario para desabilitar o gerenciamento do Spring]
public class JPAConfig {
	
	/**
	 * Faz a configuracao para ligar ao context.xml[Data source] (src/main/webapp/context.xml)
	 * context.xml ==> serve pra configurar o Tomcat
	 */
	//@Profile("local")
	@Bean
	public DataSource dataSource() {
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup(); //recuper o arquivo context.xml
		dataSourceLookup.setResourceRef(true); //procura dentro do Conteiner = Tomcat ??? Configurando dentro do Tomcat
		return dataSourceLookup.getDataSource("jdbc/cervejariaDB");
	}
	
	/**
	 * Configuracao do HEROKU para conectar o DB 
	 * @return
	 * @throws URISyntaxException
	 */
	//@Profile("prod")
	//@Bean
	public DataSource dataSourceProd() throws URISyntaxException {
		URI jdbUri = new URI(System.getenv("JAWSDB_URL"));

	    String username = jdbUri.getUserInfo().split(":")[0];
	    String password = jdbUri.getUserInfo().split(":")[1];
	    String port = String.valueOf(jdbUri.getPort());
	    String jdbUrl = "jdbc:mysql://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();
	    
	    BasicDataSource dataSource = new BasicDataSource(); // da dependencia "Commons dbp2"
	    dataSource.setUrl(jdbUrl);
	    dataSource.setUsername(username);
	    dataSource.setPassword(password);
	    dataSource.setInitialSize(10); // tamanho do pool de Conexoes
	    return dataSource;
	}
	
	/**
	 * Configura o Hibernate 
	 */
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL); //Configura o tipo de DB
		adapter.setShowSql(false);
		adapter.setGenerateDdl(false); // Pois esto usando o Flyway
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect"); // traduz JPA, Criteria to MySQL
		return adapter;
	}
	
	
	/**
	 * Configura o EntityManager para GERENCIAR as Entidades 
	 * 
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource); // Pega as configuracoes do Data Source (metodo ACIMA)
		factory.setJpaVendorAdapter(jpaVendorAdapter); // Pegar as configuracoes do Hibernate (metodos ACIMA)
		factory.setPackagesToScan(Cerveja.class.getPackage().getName()); // Pega o nome do pacote da Classe Cerveja
		factory.setMappingResources("sql/consultas-nativas.xml"); // Faz o mapeamento para usar as consultas SQL
		factory.afterPropertiesSet(); // cria o FACTORY faz depois que tudo estiver pronto
		
		return factory.getObject();
	}
	

	/**
	 * Configura as transacoes com DB
	 * @param entityManagerFactory
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

}
