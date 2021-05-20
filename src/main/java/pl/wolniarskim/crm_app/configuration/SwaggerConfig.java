package pl.wolniarskim.crm_app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${app.api.version}")
    String appVersion;

    @Value("${app.api.termsOfServiceUrl}")
    String termsOfServiceUrl;

    @Value("${app.api.apiDescription}")
    String apiDescription;

    @Value("${app.api.supportEmail}")
    String supportEmail;

    @Value("${app.api.licenseName}")
    String licenseName;

    @Value("${app.api.licenseUrl}")
    String licenseUrl;

    @Value("${app.api.contactName}")
    String contactName;

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(createApiInfo())
                ;
    }

    private ApiInfo createApiInfo(){
        return new ApiInfo("API docs",
                apiDescription,
                appVersion,
                termsOfServiceUrl,
                contactName,
                licenseName,
                licenseUrl);
    }
}
