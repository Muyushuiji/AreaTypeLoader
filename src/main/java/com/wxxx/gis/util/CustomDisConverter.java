package com.wxxx.gis.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author xmm
 */
public class CustomDisConverter implements Converter<Double> {
    @Override
    public Class supportJavaTypeKey() {
        return Double.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 这里读的时候会调用
     *
     * @param cellData
     *            NotNull
     * @param contentProperty
     *            Nullable
     * @param globalConfiguration
     *            NotNull
     * @return
     */
    @Override
    public Double convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) {
        String stringValue;
        switch (cellData.getType()) {
            case STRING:
                stringValue = cellData.getStringValue();
                break;
            case NUMBER:
                stringValue = cellData.getNumberValue().toString();
                break;
            default:
                stringValue = (String) cellData.getData();
        }
        if (StrUtil.isBlank(stringValue)) {
            return 0d;
        }
        if (stringValue.contains("km")) {
            stringValue= stringValue.replace("km", "");

        }

        double d = Double.parseDouble(stringValue);
        if (d > 100) {
            d = d/1000;
        }
        return d;
    }

    /**
     * 这里是写的时候会调用 不用管
     *
     * @param value
     *            NotNull
     * @param contentProperty
     *            Nullable
     * @param globalConfiguration
     *            NotNull
     * @return
     */
    @Override
    public CellData convertToExcelData(Double value, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) {
        return new CellData(value);
    }

}