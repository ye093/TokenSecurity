package cn.yejy.repository;

import cn.yejy.jooq.domain.tables.Goods;
import cn.yejy.jooq.domain.tables.GoodsImage;
import cn.yejy.util.TextUtil;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class GoodsRepository {

    @Autowired
    DSLContext dsl;

    /**
     * 根据商品条码返回商品信息
     */
    public Record getGoodsByBarCode(String barCode) {
        Goods g = Goods.GOODS.as("g"); // 别名
        GoodsImage img = GoodsImage.GOODS_IMAGE.as("img");
        Record record = dsl.select(g.BAR_CODE, g.GOODS_NAME, img.IMG_URL).from(g).leftJoin(img).on(g.GOODS_ID.eq(img.GOODS_ID)
                .and(img.TYPE.eq(Integer.valueOf(1).byteValue())))
                .where(g.BAR_CODE.eq(barCode)).fetchOne();
        return record;
    }

    public Map<String, Object> saveGoods(String spec, String img, String code, String manuName, String trademark, String manuAddress,
                            String goodsName, String createdBy) {
        Goods G = Goods.GOODS.as("g"); // 别名
         Record goodsRecord = dsl.insertInto(Goods.GOODS).columns(G.SPEC, G.BAR_CODE, G.MANU_NAME, G.TRADEMARK, G.MANU_ADDRESS,
                G.GOODS_NAME, G.CREATION_TIME, G.CREATED_BY).values(spec, code, manuName, trademark, manuAddress, goodsName,
                LocalDateTime.now(), createdBy).returning().fetchOne();
        Integer goodsId = goodsRecord.get(G.GOODS_ID);
        if (TextUtil.isNotEmpty(img) && goodsId != null) {
            GoodsImage IMG = GoodsImage.GOODS_IMAGE.as("img");
            dsl.insertInto(GoodsImage.GOODS_IMAGE).columns(IMG.GOODS_ID, IMG.IMG_URL, IMG.ORDER, IMG.TYPE, IMG.CREATION_TIME, IMG.CREATED_BY)
                    .values(goodsId, img, (byte) 1, (byte) 1, LocalDateTime.now(), createdBy).executeAsync();
        }
        Map<String, Object> goods = goodsRecord.intoMap();
        goods.put("img_url", img);
        return goods;
    }
}
