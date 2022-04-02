package com.wxxx.gis.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxxx.gis.entity.Covered;
import com.wxxx.gis.service.CoveredService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * 解析站点数据
 */
@Slf4j
@Component
public class CoveredExcelListener extends AnalysisEventListener<Covered> {

    @Resource
    private CoveredService coveredService;

    /**
     * 批处理阈值
     */
    private static final int BATCH_COUNT = 10;
    List<Covered> list = new ArrayList<>(BATCH_COUNT);

    @Override
    public void invoke(Covered covered, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(covered));
        // 判断数据库是否存在该数据
        this.truncate(covered);
        if (list.size() >= BATCH_COUNT) {
            log.info("{}条数据，开始存储数据库！", list.size());
            coveredService.saveBatch(list);
            log.info("存储数据库成功，当前缓存数据：{}条", list.size());
            list.clear();
        }
    }

    private void truncate(Covered covered) {
        LambdaQueryWrapper<Covered> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Covered::getCity, covered.getCity())
                .eq(Covered::getDistance, covered.getDistance())
                .eq(Covered::getDistrict, covered.getDistrict())
                .eq(Covered::getLevel, covered.getLevel())
                .eq(Covered::getLat, covered.getLat())
                .eq(Covered::getLng, covered.getLng())
                .eq(Covered::getNum, covered.getNum())
                .eq(Covered::getProvince, covered.getProvince())
                .eq(Covered::getQuestion, covered.getQuestion())
                .eq(Covered::getReason, covered.getReason())
                .eq(Covered::getRegion, covered.getRegion())
                .eq(Covered::getScene, covered.getScene())
                .eq(Covered::getSuggest, covered.getSuggest());
        List<Object> coveredList = coveredService.listObjs(queryWrapper);
        if (coveredList.size() > 1) {
            log.info("该条数据{}已存在，无需重复插入！",coveredList);
            return;
        }
        list.add(covered);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据解析完成！");
    }

}
