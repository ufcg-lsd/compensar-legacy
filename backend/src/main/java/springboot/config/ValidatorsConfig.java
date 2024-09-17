package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuração de validadores para o MongoDB.
 * Define os beans necessários para habilitar a validação de documentos usando
 * anotações.
 */
@Configuration
public class ValidatorsConfig {

    /**
     * Bean que cria o Listener de Validação para eventos do MongoDB.
     * Este listener valida documentos antes de serem persistidos no MongoDB.
     *
     * @return uma instância de ValidatingMongoEventListener
     */
    @Bean
    public ValidatingMongoEventListener mongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    /**
     * Bean que fornece a fábrica de validadores para o Spring.
     * Utiliza a implementação padrão de Bean Validation (JSR-380).
     *
     * @return uma instância de LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
