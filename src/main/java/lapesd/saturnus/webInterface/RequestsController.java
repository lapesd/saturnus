package lapesd.saturnus.webInterface;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RequestsController {

    @PostMapping("/start")
    public String startSimulation(@RequestBody MultiValueMap<String, String> formData) {

        return "simulation";
    }
}
