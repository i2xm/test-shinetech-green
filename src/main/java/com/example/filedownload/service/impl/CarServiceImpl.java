package com.example.filedownload.service.impl;

import com.example.filedownload.dao.CarDao;
import com.example.filedownload.model.Car;
import com.example.filedownload.service.CarService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DecimalNode;
import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/6/18.
 */
@Service
public class CarServiceImpl implements CarService {
    private final static DecimalFormat decimalFormat = new DecimalFormat("0.000");

    @Autowired
    private CarDao carDao;

    @Override
    public List<Car> findCars() {
        return carDao.findAll();
    }

    public List<Map<String, String>> pojosToMap(List<Serializable> pojos, @Header("DecimalSeparator") char decimalSeparator) throws JsonGenerationException, JsonMappingException, IOException {
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>(pojos.size());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setLocale(Locale.CHINA);
        objectMapper.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
        for (Serializable pojo : pojos) {
            JsonNode rootNode = objectMapper.valueToTree(pojo);
            Map<String, String> map = parseTree(rootNode, decimalSeparator);
            mapList.add(map);
        }

        return mapList;
    }

    private Map<String, String> parseTree(JsonNode rootNode, char decimalSeparator) {
        SortedMap<String, String> map = new TreeMap<String, String>();
        parseNode("", map, rootNode, decimalSeparator);
        return map;
    }

    private void parseNode(String prefix, Map<String, String> map, JsonNode node, char decimalSeparator) {
        Iterator<String> fieldNameIterator = node.fieldNames();
        while (fieldNameIterator.hasNext()) {
            String fieldName = fieldNameIterator.next();
            JsonNode childNode = node.findValue(fieldName);
            if (childNode.isValueNode()) {
                String text;
                if (childNode instanceof DecimalNode) {
                    text = decimalFormat.format(childNode.asDouble());
                    text = decimalSeparator == ','? text.replace('.', ','): text;
                } else {
                    text = childNode.asText();
                }
                map.put(prefix + fieldName, text);
            } else {
                if (childNode.isObject()) {
                    parseNode(prefix + fieldName + "/", map, childNode, decimalSeparator);
                }
            }
        }
    }

}
