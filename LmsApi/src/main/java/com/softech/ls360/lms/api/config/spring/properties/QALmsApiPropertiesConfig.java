package com.softech.ls360.lms.api.config.spring.properties;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.softech.ls360.util.config.spring.environment.QAEnvironment;

@Configuration
@Conditional(QAEnvironment.class)
@PropertySources({
	@PropertySource("classpath:lmsapi-webservice-qa.properties")
})
public class QALmsApiPropertiesConfig {

}
