package cn.yejy.repository;

import cn.yejy.jooq.domain.tables.GoodsImage;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.UpdateSetMoreStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class GoodsImageRepository {

    @Autowired
    DSLContext dsl;

    public boolean save(Integer goods_id, Byte type, Byte order, String img_url, LocalDateTime creation_time, String created_by) {

        GoodsImage img = GoodsImage.GOODS_IMAGE;
        int rows = dsl.insertInto(img, img.GOODS_ID, img.TYPE, img.ORDER, img.IMG_URL, img.CREATION_TIME, img.CREATED_BY)
                .values(goods_id, type, order, img_url, creation_time, created_by).execute();
        return rows == 1;
    }

    public boolean update(Long goods_img_id, Integer goods_id, Byte type, Byte order, String img_url, LocalDateTime update_time, String updated_by) {
        GoodsImage img = GoodsImage.GOODS_IMAGE;
        UpdateSetMoreStep updateSetMoreStep = dsl.update(img).set(img.GOODS_IMG_ID, goods_img_id);
        if (goods_img_id != null) {
            updateSetMoreStep.set(img.GOODS_IMG_ID, goods_img_id);
        }
        if (goods_id != null) {
            updateSetMoreStep.set(img.GOODS_ID, goods_id);
        }
        if (type != null) {
            updateSetMoreStep.set(img.TYPE, type);
        }
        if (order != null) {
            updateSetMoreStep.set(img.ORDER, order);
        }
        if (img_url != null) {
            updateSetMoreStep.set(img.IMG_URL, img_url);
        }
        if (update_time != null) {
            updateSetMoreStep.set(img.UPDATE_TIME, update_time);
        }
        if (updated_by != null) {
            updateSetMoreStep.set(img.UPDATED_BY, updated_by);
        }
        int row = updateSetMoreStep.where(img.GOODS_IMG_ID.eq(goods_img_id)).execute();
        return row == 1;
    }

    public Result getByGoodsId(Integer goods_id, Byte type) {
        GoodsImage img = GoodsImage.GOODS_IMAGE;
        Result records = dsl.select(img.GOODS_IMG_ID, img.GOODS_ID, img.TYPE, img.ORDER, img.IMG_URL, img.CREATION_TIME,
                img.UPDATE_TIME, img.CREATED_BY, img.UPDATED_BY)
                .from(img).where(img.GOODS_ID.eq(goods_id).and(img.TYPE.eq(type)))
                .orderBy(img.ORDER.asc()).fetch();
        return records;
    }

    public boolean deleteById(Long goods_img_id) {
        GoodsImage img = GoodsImage.GOODS_IMAGE;
        int res = dsl.deleteFrom(img).where(img.GOODS_IMG_ID.eq(goods_img_id)).execute();
        return res == 1;
    }

}
