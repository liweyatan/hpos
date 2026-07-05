package com.hospital.repository;

import com.hospital.entity.DoctorSchedule;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DoctorScheduleRepository {

    @Select("SELECT * FROM doctor_schedule WHERE doctor_id = #{doctorId} AND day_of_week = #{dayOfWeek} AND active = 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "doctorId", column = "doctor_id"),
        @Result(property = "dayOfWeek", column = "day_of_week"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "slotDuration", column = "slot_duration"),
        @Result(property = "active", column = "active")
    })
    List<DoctorSchedule> findByDoctorAndDay(Long doctorId, Integer dayOfWeek);
}
