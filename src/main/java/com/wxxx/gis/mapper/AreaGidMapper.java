package com.wxxx.gis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxxx.gis.entity.AreaGid;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName:AreaGidMapper
 * Package:com.wxxx.gis.mapper
 * Description:
 *
 * @Date:2022/8/2821:41
 * @Author:yangyang
 */
public interface AreaGidMapper extends BaseMapper<AreaGid> {
    List<AreaGid> findAll();

    List<AreaGid> findByProvinceAndCity(@Param("province") String province, @Param("city") String city);
}
