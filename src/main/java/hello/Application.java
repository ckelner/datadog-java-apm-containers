package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import datadog.trace.api.Trace;

@SpringBootApplication
@RestController
public class Application {

    @Trace
    @RequestMapping("/")
    public String home() {
        return "Hello Docker World";
    }

    @Trace
    @RequestMapping("/slow")
    public String slow() {
        try {
          Thread.sleep(5000);
        } catch (Exception e) {
          return "Kelnerhax: he ain't sleepy";
        }
        return "Hello Sleepy Docker World";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
