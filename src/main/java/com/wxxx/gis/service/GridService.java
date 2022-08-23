package com.wxxx.gis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxxx.gis.entity.Grid;
import com.wxxx.gis.entity.GridVO;
import com.wxxx.gis.mapper.GridMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-22 18:37
 **/
@Service
public class GridService extends ServiceImpl<GridMapper, Grid> {
    @Resource
    private GridMapper gridMapper;


    public List<GridVO> findAll() {
        return gridMapper.findAll();
    }
}
