package com.example.demo.repository;

import com.example.demo.dto.CameraAccessor;
import com.example.demo.dto.CustomerAccessor;
import com.example.demo.model.Camera;
import com.example.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {
    @Query("select c.id as camId, c.camName as camName from Camera c" +
            " left join Zone z on c.zone.id = z.id where z.id = :zoneId")
    List<CameraAccessor> getCameraByZoneId(Long zoneId);
}
