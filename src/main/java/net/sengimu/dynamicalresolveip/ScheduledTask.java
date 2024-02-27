package net.sengimu.dynamicalresolveip;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ScheduledTask {

    @Autowired
    private InfoConfig infoConfig;

    @PostConstruct
    public void initConfig() {
        boolean exist = FileUtil.exist("../../application.yml");

        if (!exist) {
            FileUtil.copy("application.yml", "../../", false);
            System.exit(0);
        }
    }

    @Scheduled(cron = "0/300 * * * * ?")
    public void sendRea() {

        try {
            String ip = JSONObject.parseObject(HttpUtil.get("https://www.ipplus360.com/getIP")).getString("data");


            JSONObject zones = JSONObject.parseObject(HttpRequest.get("https://api.cloudflare.com/client/v4/zones")
                    .header("X-Auth-Email", infoConfig.getEmail())
                    .header("X-Auth-Key", infoConfig.getGKey())
                    .execute().body());

            String zoneId = null;

            for (JSONObject zone : ((List<JSONObject>) zones.get("result"))) {
                String domain = infoConfig.getName().substring(infoConfig.getName().indexOf('.') + 1);
                if (domain.equals(zone.getString("name"))) {
                    zoneId = zone.getString("id");
                    break;
                }
            }


            JSONObject dnsRecords = JSONObject.parseObject(HttpRequest.get("https://api.cloudflare.com/client/v4/zones/" + zoneId + "/dns_records")
                    .header("X-Auth-Email", infoConfig.getEmail())
                    .header("X-Auth-Key", infoConfig.getGKey())
                    .execute().body());

            String dnsRecordId = null;

            for (JSONObject dnsRecord : ((List<JSONObject>) dnsRecords.get("result"))) {
                if (infoConfig.getName().equals(dnsRecord.getString("name"))) {
                    dnsRecordId = dnsRecord.getString("id");
                    break;
                }
            }


            JSONObject params = JSONObject.of("content", ip, "name", infoConfig.getName(), "type", infoConfig.getType());

            JSONObject result = JSONObject.parseObject(HttpRequest.put("https://api.cloudflare.com/client/v4/zones/" + zoneId + "/dns_records/" + dnsRecordId)
                    .header("X-Auth-Email", infoConfig.getEmail())
                    .header("X-Auth-Key", infoConfig.getGKey())
                    .body(params.toString())
                    .execute().body());

            if (result.getString("success").equals("true")) {
                log.info("指定域名: " + infoConfig.getName() + " 已成功解析到当前IP: " + ip);
            } else {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            log.error("解析异常,请检查配置参数或配置域名是否存在.");
        }
    }
}
