package io.pivotal.pal.tracker;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

   private @Value("${welcome.message}") String str;


    public String getStr() {
        return str;
    }

    public void setStr(final String val) {
        this.str = val;
    }

    public WelcomeController() {
        this.setStr(str);
    }


    public WelcomeController(String message) {
        this.setStr(message);
    }

    @GetMapping("/")
    public String sayHello(){
        return this.getStr();
    }
}
