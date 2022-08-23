package com.wxxx.gis.util;

import cn.hutool.core.util.CharsetUtil;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.wxxx.gis.entity.GenParam;
import com.wxxx.gis.entity.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
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

    public static String writeByEntity(List<GenParam> genParams, String path, String province) throws Exception {
        String csvPath = path + province + ".csv";
        File parentDir = new File(path);
        File csvFile = new File(csvPath);
        if(!parentDir.exists()){
            if (parentDir.mkdirs()){
                csvFile.createNewFile();
            }
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(csvFile.getAbsolutePath()), CharsetUtil.CHARSET_GBK);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();

        beanToCsv.write(genParams);
        return csvPath;
    }

//    @GetMapping("/genGrid")
//    public String loadWorkingParameter() {
//        String pathString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\5Gxin.csv";
//        String pathOutString = "C:\\Users\\14818\\Desktop\\Site-Plan\\merge20220415\\5Ggrid.csv";
//        String outBuffer = "";
//        // 锚定终端 TAC map
//        FileWriter fw = null;
//        BufferedWriter bw = null;
//        try {
//            BufferedReader in = new BufferedReader(new FileReader(pathString));
//            fw = new FileWriter(pathOutString);
//            bw = new BufferedWriter(fw);
//
//            String str;
//            int line = 0;
//            while ((str = in.readLine()) != null) {
//                line++;
//                String[] split = str.split(",");
//                String cgiString = split[WP4G_CGI_INDEX].trim();
//                String grididString = split[WP4G_GRIDID_INDEX].trim();
//                String gridid2String = split[WP4G_GRIDID2_INDEX].trim();
//                String id50mString = split[WP4G_ID50M_INDEX].trim();
//                取出省份、城市、经度、纬度
//                找网格的空间信息，通过省份、城市筛选
//                        得到网格列表
//                获取经度纬度对应的网格id
//                        拼接输出
//
//                String outString = cgiString + "," + grididString + "," + gridid2String + "," + id50mString + "\r\n";
//                bw.write(outString);
//                bw.newLine();
//                bw.flush();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (null != bw) {
//                try {
//                    bw.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        log.info("333333333333333 end");
//        return pathOutString;
//    }

}
