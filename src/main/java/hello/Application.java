package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import datadog.trace.api.Trace;

@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/")
    public String home() {
        return "Hello Docker World";
    }

    /* NOTE: @ckelner:
      These can be uncommented and used with the OpenJDK relase, however
      when using the OpenJ9 JRE this causes traces not to be reported. It appears
      to be related to https://gist.github.com/ckelner/ffbd6182bdff27929715a0d85ac991b4
      Uncomment these lines when you build with OpenJDK and you can hit the following urls
      localhost:<port>/slow
      localhost:<port>/sleepy
      And you'll see them report into Datadog: https://cl.ly/1a1X0d0i3B40
    */
    /*
    @RequestMapping("/slow")
    public String slow() {
        try {
          Thread.sleep(5000);
        } catch (Exception e) {
          return "Kelnerhax: he ain't sleepy";
        }
        return "Hello Sleepy Docker World";
    }

    @RequestMapping("/sleepy")
    public String sleepy() {
        return "Hello Sleepy Docker World";
    }
    */

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
