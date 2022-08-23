package com.wxxx.gis.util;

import cn.hutool.core.util.CharsetUtil;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.wxxx.gis.entity.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @program: gis-web
 * @description:
 * @author: hxl
 * @create: 2022-08-22 18:12
 **/
public class CsvUtil {

    public  static List parseByName(MultipartFile file) throws IOException {
        InputStreamReader inputStream = new InputStreamReader(file.getInputStream(), CharsetUtil.CHARSET_GBK);
        // 设置解析策略，csv的头和POJO属性的名称对应，也可以使用@CsvBindByName注解来指定名称
        HeaderColumnNameMappingStrategy strategy = new HeaderColumnNameMappingStrategy();
        strategy.setType(Param.class);

        CsvToBean csvToBean = new CsvToBeanBuilder(inputStream)
                .withMappingStrategy(strategy)
                .build();
        List carCsvDTOList = csvToBean.parse();
        return carCsvDTOList;
    }
}
