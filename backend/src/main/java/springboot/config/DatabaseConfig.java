package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

/**
 * Configuração para popular o banco de dados com dados iniciais a partir de um
 * arquivo JSON localizado no classpath.
 */
@Configuration
public class DatabaseConfig {

    /**
     * Cria um bean que popula automaticamente o banco de dados com dados iniciais
     * do arquivo "data.json" localizado no classpath.
     *
     * @return Jackson2RepositoryPopulatorFactoryBean configurado com o recurso
     *         JSON.
     */
    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();

        Resource dataSource = new ClassPathResource("data.json");

        factory.setResources(new Resource[] { dataSource });
        return factory;
    }
}
