package com.wxxx.gis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxxx.gis.entity.Covered;
import com.wxxx.gis.mapper.CoveredMapper;
import com.wxxx.gis.service.CoveredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 室外覆盖结果表
 *
 * @author xmmxjy
 * @date 2022-03-11 16:00:26
 */
@Service
public class CoveredServiceImpl extends ServiceImpl<CoveredMapper, Covered> implements CoveredService {

    @Autowired
    private CoveredMapper coveredMapper;

    @Override
    public void truncate() {

    }

    @Override
    public void insertBatchSomeColumn(List<Covered> list) {

    }

    @Override
    public List<Covered> getAllCoverd() {
        return coveredMapper.getAllCoverd();
    }

}
