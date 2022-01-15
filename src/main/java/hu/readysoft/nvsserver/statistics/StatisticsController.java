package hu.readysoft.nvsserver.statistics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @GetMapping("/test")
    public String test() {
        return "Hello";
    }
}
