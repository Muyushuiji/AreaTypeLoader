package com.wxxx.gis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxxx.gis.entity.CityGrid;
import com.wxxx.gis.entity.CityGridVO;
import com.wxxx.gis.entity.GridVO;
import com.wxxx.gis.mapper.GridMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-22 18:37
 **/
@Service
public class GridService extends ServiceImpl<GridMapper, CityGrid> {
    @Resource
    private GridMapper gridMapper;


    public List<CityGridVO> findAll() {
        return gridMapper.findAll();
    }

    public List<GridVO> findAllByProvince(String province,String city) {
        return gridMapper.findAllByProvince(province,city);
    }

    public Map<String, CityGridVO> init() {
        return gridMapper.init();
    }
}
