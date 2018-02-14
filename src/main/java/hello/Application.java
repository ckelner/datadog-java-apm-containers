package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import datadog.trace.api.Trace;

@SpringBootApplication
@RestController
public class Application {

    /* NOTE: @ckelner:
      When using the OpenJ9 JRE not all resources are reported. It appears
      to be related to https://gist.github.com/ckelner/ffbd6182bdff27929715a0d85ac991b4
      Hit the following urls
      localhost:<port>/slow
      localhost:<port>/sleepy
      And under OpenJ9 you'll only see one resource: https://cl.ly/1X0c131b0W0e
      However if you examine the trace metrics themselves it appears that they do
      exist: https://cl.ly/1c0G2j3h2Q0Z
      Under OpenJDK each of these resources will report as seen here: https://cl.ly/1i3N151Y3g0e
    */
    @RequestMapping("/")
    /* NOTE: @ckelner: @Trace Only required for OpenJ9 - can be removed when using OpenJDK */
    @Trace
    public String home() {
        return "Hello Docker World";
    }

    @RequestMapping("/slow")
    /* NOTE: @ckelner: @Trace Only required for OpenJ9 - can be removed when using OpenJDK */
    @Trace
    public String slow() {
        try {
          Thread.sleep(5000);
        } catch (Exception e) {
          return "Kelnerhax: he ain't sleepy";
        }
        return "Hello Sleepy Docker World";
    }

    @RequestMapping("/sleepy")
    /* NOTE: @ckelner: @Trace Only required for OpenJ9 - can be removed when using OpenJDK */
    @Trace
    public String sleepy() {
        return "Hello Sleepy Docker World";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
