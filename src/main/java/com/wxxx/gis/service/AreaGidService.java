package com.wxxx.gis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxxx.gis.entity.AreaGid;
import com.wxxx.gis.mapper.AreaGidMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName:AreaGidService
 * Package:com.wxxx.gis.service
 * Description:
 *
 * @Date:2022/8/2821:39
 * @Author:yangyang
 */
@Service
public class AreaGidService extends ServiceImpl<AreaGidMapper, AreaGid> {
    @Resource
    private AreaGidMapper areaGidMapper;
    public List<AreaGid> findAll() {
        return areaGidMapper.findAll();
    }

    public List<AreaGid> findByProvinceAndCity(String province, String city) {
        return areaGidMapper.findByProvinceAndCity(province,city);
    }
}
