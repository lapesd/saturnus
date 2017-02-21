package lapesd.saturnus.webInterface;

import lapesd.saturnus.simulator.SimulationController;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Controller
public class RequestsController {

    @PostMapping("/start")
    public String startSimulation(@RequestBody MultiValueMap<String, String> formData) throws IOException{
        // TODO: Check if the map is not empty;
        SimulationController.initSimulation(formData.toSingleValueMap());
        return "simulation";
    }
}
