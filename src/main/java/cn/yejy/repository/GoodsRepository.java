package cn.yejy.repository;

import cn.yejy.jooq.domain.tables.Goods;
import cn.yejy.jooq.domain.tables.GoodsCode;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GoodsRepository {

    @Autowired
    DSLContext dsl;

    /**
     * 保存商品基本资料
     *
     * @param spec        规格
     * @param img         图片
     * @param code        69码
     * @param manuName    生产商
     * @param trademark   品牌
     * @param manuAddress 生产地址
     * @param goodsName   商品名称
     * @param createdBy   创建人
     * @return
     */
    public Integer saveGoods(String spec, String img, String code, String manuName, String trademark, String manuAddress,
                             String goodsName, String createdBy) {
        return save(goodsName, code, null, spec, null, null, trademark, manuName,
                manuAddress, null, null, null, img, null,
                null, null, null, null, null, null,
                null, null, null, LocalDateTime.now(), createdBy, null,
                null);
    }

    public Record getGoodsById(Integer goodsId) {
        return select(goodsId, null, null);
    }

    public Record getGoodsByBarCode(String barCode) {
        return select(null, barCode, null);
    }

    public Record getGoodsByGoodsCode(String goodsCode) {
        return select(null, null, goodsCode);
    }

    private Record select(Integer goods_id, String bar_code, String goods_code) {
        Goods g = Goods.GOODS;
        GoodsCode gc = GoodsCode.GOODS_CODE;
        // SELECT FIELDS
        SelectSelectStep<Record> selectFields = dsl.select(g.GOODS_NAME, g.BAR_CODE, g.UNIT, g.SPEC, g.GOODS_SHORT_NAME, g.GOODS_SHORT_CODE, g.TRADEMARK, g.MANU_NAME,
                g.MANU_ADDRESS, g.HAS_EXPIRY_DATE, g.SHELF_LIFE_IN_DAYS, g.WARRANTY_PERIOD, g.THUMBNAIL, g.WEIGHT, g.CUBAGE,
                g.IS_INVENTED, g.IS_PACKING, g.IS_WEIGH, g.STORAGE, g.PACKING_QTY, g.COUNTRY_OF_ORIGIN, g.DESCRIPTION,
                g.REMARK, g.CREATION_TIME, g.CREATED_BY, g.UPDATE_TIME, g.UPDATED_BY, gc.GOODS_CODE_);
        Condition condition;
        if (goods_code != null) {
            condition = gc.GOODS_CODE_.eq(goods_code);
        } else if (goods_id != null) {
            condition = g.GOODS_ID.eq(goods_id);
        } else {
            condition = g.BAR_CODE.eq(bar_code);
        }
        SelectOnConditionStep<Record> selectOnConditionStep;
        if (goods_code != null) {
            selectOnConditionStep = selectFields.from(gc).innerJoin(g).on(gc.GOODS_ID.eq(g.GOODS_ID));
        } else {
            selectOnConditionStep = selectFields.from(g).leftJoin(gc).on(g.GOODS_ID.eq(gc.GOODS_ID));
        }
        return selectOnConditionStep.where(condition).fetchOne();
    }

    public Integer save(String goods_name, String bar_code, String unit, String spec, String goods_short_name, String goods_short_code, String trademark,
                        String manu_name, String manu_address, Boolean has_expiry_date, Integer shelf_life_in_days, String warranty_period, String thumbnail,
                        BigDecimal weight, BigDecimal cubage, Boolean is_invented, Boolean is_packing, Boolean is_weigh, String storage, Integer packing_qty,
                        String country_of_origin, String description, String remark, LocalDateTime creation_time, String created_by,
                        LocalDateTime update_time, String updated_by) {
        Goods g = Goods.GOODS;
        Record record = dsl.insertInto(g, g.GOODS_NAME, g.BAR_CODE, g.UNIT, g.SPEC, g.GOODS_SHORT_NAME, g.GOODS_SHORT_CODE, g.TRADEMARK, g.MANU_NAME,
                g.MANU_ADDRESS, g.HAS_EXPIRY_DATE, g.SHELF_LIFE_IN_DAYS, g.WARRANTY_PERIOD, g.THUMBNAIL, g.WEIGHT, g.CUBAGE,
                g.IS_INVENTED, g.IS_PACKING, g.IS_WEIGH, g.STORAGE, g.PACKING_QTY, g.COUNTRY_OF_ORIGIN, g.DESCRIPTION,
                g.REMARK, g.CREATION_TIME, g.CREATED_BY, g.UPDATE_TIME, g.UPDATED_BY).values(goods_name, bar_code, unit, spec, goods_short_name, goods_short_code, trademark,
                manu_name, manu_address, has_expiry_date, shelf_life_in_days, warranty_period, thumbnail, weight, cubage, is_invented, is_packing,
                is_weigh, storage, packing_qty, country_of_origin, description, remark, creation_time, created_by, update_time, updated_by).returning(g.GOODS_ID).fetchOne();
        return record.getValue(g.GOODS_ID);
    }

    public boolean update(Integer goods_id, String goods_name, String bar_code, String unit, String spec, String goods_short_name, String goods_short_code,
                          String trademark, String manu_name, String manu_address, Boolean has_expiry_date, Integer shelf_life_in_days, String warranty_period,
                          String thumbnail, BigDecimal weight, BigDecimal cubage, Boolean is_invented, Boolean is_packing, Boolean is_weigh, String storage,
                          Integer packing_qty, String country_of_origin, String description, String remark, LocalDateTime creation_time, String created_by,
                          LocalDateTime update_time, String updated_by) {
        Goods g = Goods.GOODS;
        UpdateSetMoreStep<Record> updateSetMoreStep = dsl.update(g).set(g.GOODS_ID, goods_id);
        if (goods_name != null) {
            updateSetMoreStep.set(g.GOODS_NAME, goods_name);
        }
        if (bar_code != null) {
            updateSetMoreStep.set(g.BAR_CODE, bar_code);
        }
        if (unit != null) {
            updateSetMoreStep.set(g.UNIT, unit);
        }
        if (spec != null) {
            updateSetMoreStep.set(g.SPEC, spec);
        }
        if (goods_short_name != null) {
            updateSetMoreStep.set(g.GOODS_SHORT_NAME, goods_short_name);
        }
        if (goods_short_code != null) {
            updateSetMoreStep.set(g.GOODS_SHORT_CODE, goods_short_code);
        }
        if (trademark != null) {
            updateSetMoreStep.set(g.TRADEMARK, trademark);
        }
        if (manu_name != null) {
            updateSetMoreStep.set(g.MANU_NAME, manu_name);
        }
        if (manu_address != null) {
            updateSetMoreStep.set(g.MANU_ADDRESS, manu_address);
        }
        if (has_expiry_date != null) {
            updateSetMoreStep.set(g.HAS_EXPIRY_DATE, has_expiry_date);
        }
        if (shelf_life_in_days != null) {
            updateSetMoreStep.set(g.SHELF_LIFE_IN_DAYS, shelf_life_in_days);
        }
        if (warranty_period != null) {
            updateSetMoreStep.set(g.WARRANTY_PERIOD, warranty_period);
        }
        if (thumbnail != null) {
            updateSetMoreStep.set(g.THUMBNAIL, thumbnail);
        }
        if (weight != null) {
            updateSetMoreStep.set(g.WEIGHT, weight);
        }
        if (cubage != null) {
            updateSetMoreStep.set(g.CUBAGE, cubage);
        }
        if (is_invented != null) {
            updateSetMoreStep.set(g.IS_INVENTED, is_invented);
        }
        if (is_packing != null) {
            updateSetMoreStep.set(g.IS_PACKING, is_packing);
        }
        if (is_weigh != null) {
            updateSetMoreStep.set(g.IS_WEIGH, is_weigh);
        }
        if (storage != null) {
            updateSetMoreStep.set(g.STORAGE, storage);
        }
        if (packing_qty != null) {
            updateSetMoreStep.set(g.PACKING_QTY, packing_qty);
        }
        if (country_of_origin != null) {
            updateSetMoreStep.set(g.COUNTRY_OF_ORIGIN, country_of_origin);
        }
        if (description != null) {
            updateSetMoreStep.set(g.DESCRIPTION, description);
        }
        if (remark != null) {
            updateSetMoreStep.set(g.REMARK, remark);
        }
        if (creation_time != null) {
            updateSetMoreStep.set(g.CREATION_TIME, creation_time);
        }
        if (created_by != null) {
            updateSetMoreStep.set(g.CREATED_BY, created_by);
        }
        if (update_time != null) {
            updateSetMoreStep.set(g.UPDATE_TIME, update_time);
        }
        if (updated_by != null) {
            updateSetMoreStep.set(g.UPDATED_BY, updated_by);
        }
        return updateSetMoreStep.where(g.GOODS_ID.eq(goods_id)).execute() == 1;
    }
}
