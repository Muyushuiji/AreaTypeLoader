package com.wxxx.gis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxxx.gis.entity.Covered;

import java.util.List;

/**
 * 室外覆盖结果表
 *
 * @author xmmxjy
 * @date 2022-03-11 16:00:26
 */
public interface CoveredService extends IService<Covered> {

    void truncate();

    void insertBatchSomeColumn(List<Covered> list);

    List<Covered> getAllCoverd();
}
