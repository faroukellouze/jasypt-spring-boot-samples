package demo;


import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

/**
 * Sample Boot application that showcases easy integration of Jasypt encryption by
 * simply adding {@literal @EnableEncryptableProperties} to any Configuration class.
 * For decryption a password is required and is set through system properties in this example,
 * but it could be passed command line argument too like this: --jasypt.encryptor.password=password
 *
 * @author Ulises Bocchio
 */
@SpringBootApplication
@EncryptablePropertySources({@EncryptablePropertySource("classpath:encrypted.properties"),
                             @EncryptablePropertySource(name = "IgnoredResource_FileDoesNotExist", value = "classpath:does_not_exists.properties", ignoreResourceNotFound = true)})
@Import(TestConfig.class)
public class SimpleDemoApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleDemoApplication.class);

    @Autowired
    ApplicationContext appCtx;

    public static void main(String[] args) {
        //try commenting the following line out and run the app from the command line passing the password as
        //a command line argument: java -jar target/jasypt-spring-boot-demo-0.0.1-SNAPSHOT.jar --jasypt.encryptor.password=password
        //System.setProperty("jasypt.encryptor.password", "password");
        //Enable proxy mode for intercepting encrypted properties
        //System.setProperty("jasypt.encryptor.proxyPropertySources", "true");
        new SpringApplicationBuilder().sources(SimpleDemoApplication.class).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        MyService service = appCtx.getBean(MyService.class);
        Environment environment = appCtx.getBean(Environment.class);
        LOG.info("MyService's secret: {}", service.getSecret());
        LOG.info("MyService's secret2: {}", service.getSecret2());
        LOG.info("Environment's secret: {}", environment.getProperty("secret.property"));
        LOG.info("Environment's secret2: {}", environment.getProperty("secret2.property"));
        LOG.info("Done!");
    }
}
