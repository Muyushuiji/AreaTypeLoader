package com.wxxx.gis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxxx.gis.entity.Covered;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 室外覆盖结果表
 *
 * @author xmmxjy
 * @date 2022-03-11 16:00:26
 */
@Mapper
public interface CoveredMapper extends BaseMapper<Covered> {
}
