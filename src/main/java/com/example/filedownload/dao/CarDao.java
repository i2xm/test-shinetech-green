package com.example.filedownload.dao;

import com.example.filedownload.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2018/6/18.
 */
public interface CarDao extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car>{
}




