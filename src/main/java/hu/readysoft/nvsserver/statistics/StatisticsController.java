package hu.readysoft.nvsserver.statistics;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    public static final String BROWSER = "Chrome/79.0.3945.130";

    @GetMapping("/all/solo")
    public String test() {
        JSONArray array = new JSONArray();
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL url = new URL("https://www.valasztas.hu/ogy2018");
            URLConnection con = url.openConnection();
            Reader reader = new InputStreamReader(con.getInputStream());
            StringBuilder srcDoc = new StringBuilder();
            while (true) {
                int ch = reader.read();
                if (ch==-1) {
                    break;
                }
                srcDoc.append((char)ch);
            }
            Document doc = Jsoup.parse(srcDoc.toString());
            doc.select("#p_p_id_ogykiemelteredmenyek_WAR_nvinvrportlet_ > div > div > div.ogy-kiemelt-eredmenyek-wrapper > div > div.span6.bar-charts-wrapper.with-horseshoe > div.oevk-chart-wrapper > div > div.row-fluid > div > div.row-fuild.nvi-graph-container.span12 > div.nvi-graph-layer.block-layer.main-layer span[data-text]").forEach(e -> {
                JSONObject jsonObject = new JSONObject();
                String[] data = e.attr("data-text").split(": ");
                jsonObject.put("label", data[0]);
                jsonObject.put("value", data[1]);
                Optional.ofNullable(e.selectFirst("div.nvi-graph-line")).ifPresent(element -> {
                    String color = element.attr("style").split("background-color: ")[1].replace(";", "");
                    jsonObject.put("svg", new JSONObject().put("fill", color));
                });
                array.put(jsonObject);
            });
        } catch (Exception e) {
            log.error("/statistics/test - parse error", e);
        }
        return array.toString();
    }
}
