package com.fortytwotalents.samples.pet_clinic.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.fortytwotalents.samples.pet_clinic")
@EnableJpaRepositories("com.fortytwotalents.samples.pet_clinic")
@EnableTransactionManagement
public class DomainConfig {
}
