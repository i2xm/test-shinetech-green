package com.example.filedownload.controller;

import com.example.filedownload.model.Car;
import com.example.filedownload.service.CarService;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/25.
 */
@Controller
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    protected ProducerTemplate producerTemplate;


    // localhost:8081/exportCars
    @RequestMapping(value="/exportCars", method= RequestMethod.GET, produces="text/csv")
    public @ResponseBody String exportCars(HttpServletResponse response) {

        String fileName = "Cars.csv";
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        List<Car> cars = carService.findCars();
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("DecimalSeparator", ".");
        return producerTemplate.requestBodyAndHeaders("direct:carsToExcelCsv", cars, headers, String.class);
    }
}
