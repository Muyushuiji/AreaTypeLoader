package com.wxxx.gis.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxxx.gis.entity.Covered;
import com.wxxx.gis.listener.CoveredExcelListener;
import com.wxxx.gis.mapper.CoveredMapper;
import com.wxxx.gis.service.CoveredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 室外覆盖结果表
 *
 * @author xmmxjy
 * @date 2022-03-11 16:00:26
 */
@Service
public class CoveredServiceImpl extends ServiceImpl<CoveredMapper, Covered> implements CoveredService {
}
