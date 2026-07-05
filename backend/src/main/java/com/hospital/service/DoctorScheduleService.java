package com.hospital.service;

import com.hospital.entity.DoctorSchedule;
import com.hospital.repository.DoctorScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorScheduleService {

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    public List<String> generateTimeSlots(Long doctorId, int dayOfWeek) {
        List<DoctorSchedule> schedules = doctorScheduleRepository.findByDoctorAndDay(doctorId, dayOfWeek);
        List<String> slots = new ArrayList<>();

        for (DoctorSchedule schedule : schedules) {
            LocalTime current = schedule.getStartTime();
            int duration = schedule.getSlotDuration() != null ? schedule.getSlotDuration() : 30;

            while (current.isBefore(schedule.getEndTime())) {
                LocalTime slotEnd = current.plusMinutes(duration);
                if (!slotEnd.isAfter(schedule.getEndTime())) {
                    slots.add(String.format("%02d:%02d", current.getHour(), current.getMinute()));
                }
                current = slotEnd;
            }
        }
        return slots;
    }
}
