package com.wxxx.gis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxxx.gis.entity.CityGrid;
import com.wxxx.gis.entity.CityGridVO;
import com.wxxx.gis.entity.GridVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-22 18:37
 **/
@Mapper
public interface GridMapper extends BaseMapper<CityGrid> {
    List<CityGridVO> findAll();

    List<GridVO> findAllByProvince(@Param("province") String province,@Param("city") String city);

    Map<String, CityGridVO> init();
}
