package com.example.filedownload.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/6/27.
 */
@Component
public class CsvRouteBuilder extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        CsvDataFormat carsFormat = new CsvDataFormat(CSVFormat.EXCEL);
        CsvDataFormat carsSemicolonFormat = new CsvDataFormat(CSVFormat.EXCEL.withDelimiter(';'));
        String[] cars = new String[] {
                "id",
                "color",
                "size",
                "brand" };
        carsFormat.setHeader(cars);
        carsSemicolonFormat.setHeader(cars);

        from("direct:carsToExcelCsv").bean("carServiceImpl", "pojosToMap")
                .choice()
                .when(header("DecimalSeparator").isEqualTo(',')).marshal(carsSemicolonFormat)
                .otherwise().marshal(carsFormat);
    }
}
