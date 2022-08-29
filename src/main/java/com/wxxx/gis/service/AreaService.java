package com.wxxx.gis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxxx.gis.entity.Area;
import com.wxxx.gis.entity.GridVO;
import com.wxxx.gis.mapper.AreaMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-24 16:23
 **/
@Service
public class AreaService extends ServiceImpl<AreaMapper, Area> {
    @Resource
    private AreaMapper areaMapper;
    public List<Area> findAll() {
        return areaMapper.findAll();
    }

    public List<Area> findByProvinceAndCity(String province, String city) {
        return areaMapper.findByProvinceAndCity(province,city);
    }

}
