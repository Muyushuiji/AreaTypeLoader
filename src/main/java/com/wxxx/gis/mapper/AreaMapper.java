package com.wxxx.gis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxxx.gis.entity.Area;
import com.wxxx.gis.entity.GridVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;



/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-24 16:20
 **/
public interface AreaMapper extends BaseMapper<Area> {
    List<Area> findAll();

    List<Area> findByProvinceAndCity(@Param("province") String province, @Param("city") String city);
}
