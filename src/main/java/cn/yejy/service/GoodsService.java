package cn.yejy.service;

import cn.yejy.repository.GoodsRepository;
import cn.yejy.util.JsonUtil;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品服务
 */
@Service
public class GoodsService {
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    RestTemplate restTemplate;

    @Value("${ali-barcode.url}")
    String url;
    @Value("${ali-barcode.appcode}")
    String appCode;

    public Map<String, Object> getGoodsByBarCode(String barCode) {
        Record record = goodsRepository.getGoodsByBarCode(barCode);
        if (record == null) {
            Map aliMap = getGoodsFromAli(barCode);
            if (aliMap == null) {
                // 请联系管理员
                return null;
            }
            Map resBody = (Map) aliMap.get("showapi_res_body");
            if (resBody != null && resBody.get("flag") == null ? false : (Boolean) resBody.get("flag")) {
                // 返回成功
                String spec = (String) resBody.get("spec");
                String img = (String) resBody.get("img");
                String code = (String) resBody.get("code");
                String manuName = (String) resBody.get("manuName");
                String trademark = (String) resBody.get("trademark");
                String manuAddress = (String) resBody.get("manuAddress");
                String goodsName = (String) resBody.get("goodsName");
                return goodsRepository.saveGoods(spec, img, code, manuName, trademark, manuAddress, goodsName, "yejy");
            }
            return null;
        } else {
            return record.intoMap();
        }
    }

    private Map getGoodsFromAli(String code) {
        Map resEntity = null;
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            List<String> headerItem = new ArrayList<>();
            headerItem.add("APPCODE " + appCode);
            headers.put("Authorization", headerItem);
            HttpEntity httpEntity = new HttpEntity(headers);
            ResponseEntity<String> respEntity =
                    restTemplate.exchange(url + "?code=" + code, HttpMethod.GET, httpEntity, String.class);
            resEntity = JsonUtil.parse(respEntity.getBody());
        } catch (Exception e) {
            System.out.println(e);
        }
        return resEntity;
    }
}
