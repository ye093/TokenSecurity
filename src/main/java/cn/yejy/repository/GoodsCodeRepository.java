package cn.yejy.repository;

import cn.yejy.jooq.domain.tables.GoodsCode;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsCodeRepository {
    @Autowired
    DSLContext dsl;

    public boolean save(
            String goods_code,
            Integer goods_id
    ) {
        GoodsCode gc = GoodsCode.GOODS_CODE;
        int row = dsl.insertInto(gc,
                gc.GOODS_CODE_,
                gc.GOODS_ID).values(goods_code, goods_id).execute();
        return row == 1;
    }

    public boolean updateGoodsCode(String goods_code,
                                   Integer goods_id) {
        GoodsCode gc = GoodsCode.GOODS_CODE;
        int row = dsl.update(gc).set(gc.GOODS_CODE_, goods_code).where(gc.GOODS_ID.eq(goods_id))
                .execute();
        return row == 1;
    }

}
