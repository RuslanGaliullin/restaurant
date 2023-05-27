package kpo.restaurant.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("kpo.restaurant.domain")
@EnableJpaRepositories("kpo.restaurant.repos")
@EnableTransactionManagement
public class DomainConfig {
}
