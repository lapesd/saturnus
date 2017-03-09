package lapesd.saturnus.webInterface;

import lapesd.saturnus.dataStructures.AttTimeComparator;
import lapesd.saturnus.simulator.SimulationController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class RequestsController {

    @PostMapping(value = "/start")
    public String startSimulation(Model model, @RequestBody MultiValueMap<String, String> formData) throws IOException{
        // TODO: Check if the map is not empty;
        SimulationController simulation = new SimulationController();
        ArrayList subRequestsInfo;
        subRequestsInfo = simulation.initSimulation(formData.toSingleValueMap());
        subRequestsInfo.sort(new AttTimeComparator());
        model.addAttribute("eventInfo", subRequestsInfo);
        return "simulation";
    }

    @RequestMapping("/about")
    public String aboutPage() {
        return "about";
    }
}
