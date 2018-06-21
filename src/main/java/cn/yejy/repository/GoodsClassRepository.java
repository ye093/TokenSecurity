package cn.yejy.repository;

import cn.yejy.jooq.domain.tables.GoodsClass;
import cn.yejy.jooq.domain.tables.GoodsClassRel;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class GoodsClassRepository {
    @Autowired
    DSLContext dsl;

    public boolean save(Integer parent_class_id, Byte level, Boolean is_end, Integer order, String goods_class_name, String goods_class_img_url,
                        LocalDateTime creation_time, String created_by) {
        GoodsClass gc = GoodsClass.GOODS_CLASS;
        // 保存时，要看看父类是否是最后一层
        boolean parIsEnd;
        if (parent_class_id == null || parent_class_id.intValue() == 0) {
            parIsEnd = false;
        } else {
            parIsEnd = dsl.select(gc.IS_END).from(gc).where(gc.GOODS_CLASS_ID.eq(parent_class_id)).fetchOne().component1();
        }
        if (parIsEnd) {
            // 最后一层不允许再建分类
            return false;
        }
        int row = dsl.insertInto(gc, gc.PARENT_CLASS_ID, gc.LEVEL, gc.IS_END, gc.ORDER, gc.GOODS_CLASS_NAME, gc.GOODS_CLASS_IMG_URL,
                gc.CREATION_TIME, gc.CREATED_BY).values(parent_class_id, level, is_end, order, goods_class_name,
                goods_class_img_url, creation_time, created_by).execute();
        return row == 1;
    }

    public boolean update(Integer goods_class_id, Boolean is_end, Integer order, String goods_class_name, String goods_class_img_url,
                          LocalDateTime update_time, String updated_by) {
        boolean canUpdate = true;
        if (is_end != null) {
            if (!is_end) {
                // 判断此分类是否有商品，有则不允许更新！
                GoodsClassRel gcr = GoodsClassRel.GOODS_CLASS_REL;
                Integer sum = dsl.selectCount().from(gcr).where(gcr.GOODS_CLASS_ID.eq(goods_class_id)).fetchOne().component1();
                if (sum != null && sum.intValue() > 0) {
                    canUpdate = false;
                }
            } else {
                // 判断此分类下是否有分类，有则不允许
                GoodsClass p = GoodsClass.GOODS_CLASS.as("p");
                GoodsClass sub = GoodsClass.GOODS_CLASS.as("sub");
                Integer sum = dsl.selectCount().from(sub).innerJoin(p).on(sub.PARENT_CLASS_ID.eq(p.GOODS_CLASS_ID)).where(p.GOODS_CLASS_ID.eq(goods_class_id)).fetchOne().component1();
                if (sum != null && sum.intValue() > 0) {
                    canUpdate = false;
                }
            }
        }
        if (!canUpdate) {
            return false;
        }
        GoodsClass gc = GoodsClass.GOODS_CLASS;
        UpdateSetMoreStep<Record> updateSetMoreStep = dsl.update(gc).set(gc.GOODS_CLASS_ID, goods_class_id);
        if (is_end != null) {
            updateSetMoreStep.set(gc.IS_END, is_end);
        }
        if (order != null) {
            updateSetMoreStep.set(gc.ORDER, order);
        }
        if (goods_class_name != null) {
            updateSetMoreStep.set(gc.GOODS_CLASS_NAME, goods_class_name);
        }
        if (goods_class_img_url != null) {
            updateSetMoreStep.set(gc.GOODS_CLASS_IMG_URL, goods_class_img_url);
        }
        if (update_time != null) {
            updateSetMoreStep.set(gc.UPDATE_TIME, update_time);
        }
        if (updated_by != null) {
            updateSetMoreStep.set(gc.UPDATED_BY, updated_by);
        }
        int row = updateSetMoreStep.where(gc.GOODS_CLASS_ID.eq(goods_class_id)).execute();
        return row == 1;
    }

    // 一次性添加多条,开启事务,要求前端先设置此目录为最后一层is_end=true
    public boolean saveGoodsClassRel(Integer[] goods_ids, Integer goods_class_id) throws SQLException {
        GoodsClassRel rel = GoodsClassRel.GOODS_CLASS_REL;
        InsertValuesStep2<Record, Integer, Integer> insertValuesStep2 = dsl.insertInto(rel, rel.GOODS_ID, rel.GOODS_CLASS_ID);
        for (Integer goods_id : goods_ids) {
            insertValuesStep2.values(goods_id, goods_class_id);
        }
        int len = goods_ids.length;
        int rows = insertValuesStep2.execute();
        if (rows != len) {
            throw new SQLException();
        }
        return true;
    }

    // 一次性添加多条,开启事务,要求前端先设置此目录为最后一层is_end=true
    public boolean deleteGoodsClassRel(Integer[] goods_ids, Integer goods_class_id) throws SQLException {
        GoodsClassRel rel = GoodsClassRel.GOODS_CLASS_REL;
        int row = dsl.deleteFrom(rel).where(rel.GOODS_CLASS_ID.eq(goods_class_id).and(rel.GOODS_ID.in(goods_ids))).execute();
        if (row != goods_ids.length) {
            throw new SQLException();
        }
        return true;
    }

    // 删除这一类，连同子类和子类关联商品一起干掉
    public boolean deleteGoodsClass(Integer goods_class_id, boolean is_end) throws SQLException {
        if (is_end) {
            GoodsClassRel gcr = GoodsClassRel.GOODS_CLASS_REL;
            dsl.deleteFrom(gcr).where(gcr.GOODS_CLASS_ID.eq(goods_class_id)).executeAsync();
        }
        GoodsClass gc = GoodsClass.GOODS_CLASS;
        int row = dsl.deleteFrom(gc).where(gc.GOODS_CLASS_ID.eq(goods_class_id)).execute();
        return row == 1;
    }
}
