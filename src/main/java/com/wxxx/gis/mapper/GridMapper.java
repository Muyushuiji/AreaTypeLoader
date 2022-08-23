package com.wxxx.gis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxxx.gis.entity.Grid;
import com.wxxx.gis.entity.GridVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-22 18:37
 **/
@Mapper
public interface GridMapper extends BaseMapper<Grid> {
    List<GridVO> findAll();
}
