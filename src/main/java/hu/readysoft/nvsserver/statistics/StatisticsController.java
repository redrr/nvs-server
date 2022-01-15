package hu.readysoft.nvsserver.statistics;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    public static final String BROWSER = "Chrome/79.0.3945.130";

    @GetMapping("/all/solo")
    public JSONObject test() {
        JSONObject jsonObject = new JSONObject();
        try {
            Document doc = Jsoup.connect("https://www.valasztas.hu/ogy2018").userAgent(BROWSER).get();
            jsonObject.put("size", doc.getAllElements().size());
        } catch (Exception e) {
            log.error("/statistics/test - parse error", e);
        }
        return jsonObject;
    }
}
