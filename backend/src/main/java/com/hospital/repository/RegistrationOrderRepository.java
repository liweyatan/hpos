package com.hospital.repository;

import com.hospital.entity.RegistrationOrder;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 挂号订单数据访问接口
 * 严格按照数据库列名编写SQL语句，确保与数据库结构完全一致
 */
@Mapper
public interface RegistrationOrderRepository {

    /**
     * 查询所有挂号订单（包含患者、医生、科室信息）
     */
    @Select("SELECT ro.*, p.name as patient_name, d.name as doctor_name, dep.name as department_name " +
            "FROM registration_order ro " +
            "LEFT JOIN patient p ON ro.patient_id = p.id " +
            "LEFT JOIN doctor d ON ro.doctor_id = d.id " +
            "LEFT JOIN department dep ON d.department_id = dep.id " +
            "ORDER BY ro.register_time DESC")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "doctorId", column = "doctor_id"),
            @Result(property = "registerTime", column = "register_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "symptoms", column = "symptoms"),
            @Result(property = "notes", column = "notes"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "patientName", column = "patient_name"),
            @Result(property = "doctorName", column = "doctor_name"),
            @Result(property = "departmentName", column = "department_name")
    })
    List<RegistrationOrder> findAllWithDetails();

    /**
     * 根据ID查询挂号订单详情
     */
    @Select("SELECT ro.*, p.name as patient_name, d.name as doctor_name, dep.name as department_name " +
            "FROM registration_order ro " +
            "LEFT JOIN patient p ON ro.patient_id = p.id " +
            "LEFT JOIN doctor d ON ro.doctor_id = d.id " +
            "LEFT JOIN department dep ON d.department_id = dep.id " +
            "WHERE ro.id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "doctorId", column = "doctor_id"),
            @Result(property = "registerTime", column = "register_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "symptoms", column = "symptoms"),
            @Result(property = "notes", column = "notes"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "patientName", column = "patient_name"),
            @Result(property = "doctorName", column = "doctor_name"),
            @Result(property = "departmentName", column = "department_name")
    })
    RegistrationOrder findByIdWithDetails(Long id);

    /**
     * 根据患者ID查询挂号订单
     */
    @Select("SELECT ro.*, p.name as patient_name, d.name as doctor_name, dep.name as department_name " +
            "FROM registration_order ro " +
            "LEFT JOIN patient p ON ro.patient_id = p.id " +
            "LEFT JOIN doctor d ON ro.doctor_id = d.id " +
            "LEFT JOIN department dep ON d.department_id = dep.id " +
            "WHERE ro.patient_id = #{patientId} ORDER BY ro.register_time DESC")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "doctorId", column = "doctor_id"),
            @Result(property = "registerTime", column = "register_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "symptoms", column = "symptoms"),
            @Result(property = "notes", column = "notes"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "patientName", column = "patient_name"),
            @Result(property = "doctorName", column = "doctor_name"),
            @Result(property = "departmentName", column = "department_name")
    })
    List<RegistrationOrder> findByPatientId(Long patientId);

    /**
     * 根据医生ID查询挂号订单
     */
    @Select("SELECT ro.*, p.name as patient_name, d.name as doctor_name, dep.name as department_name " +
            "FROM registration_order ro " +
            "LEFT JOIN patient p ON ro.patient_id = p.id " +
            "LEFT JOIN doctor d ON ro.doctor_id = d.id " +
            "LEFT JOIN department dep ON d.department_id = dep.id " +
            "WHERE ro.doctor_id = #{doctorId} ORDER BY ro.register_time DESC")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "doctorId", column = "doctor_id"),
            @Result(property = "registerTime", column = "register_time"),
            @Result(property = "status", column = "status"),
            @Result(property = "symptoms", column = "symptoms"),
            @Result(property = "notes", column = "notes"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "patientName", column = "patient_name"),
            @Result(property = "doctorName", column = "doctor_name"),
            @Result(property = "departmentName", column = "department_name")
    })
    List<RegistrationOrder> findByDoctorId(Long doctorId);

    /**
     * 新增挂号订单
     */
    @Insert("INSERT INTO registration_order(patient_id, doctor_id, register_time, status, symptoms, notes) " +
            "VALUES(#{patientId}, #{doctorId}, #{registerTime}, #{status}, #{symptoms}, #{notes})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RegistrationOrder order);

    /**
     * 更新挂号订单状态
     */
    @Update("UPDATE registration_order SET status=#{status}, update_time=CURRENT_TIMESTAMP WHERE id=#{id}")
    int updateStatus(Long id, String status);

    /**
     * 更新挂号订单信息
     */
    @Update("UPDATE registration_order SET patient_id=#{patientId}, doctor_id=#{doctorId}, " +
            "register_time=#{registerTime}, status=#{status}, symptoms=#{symptoms}, " +
            "notes=#{notes}, update_time=CURRENT_TIMESTAMP WHERE id=#{id}")
    int update(RegistrationOrder order);

    /**
     * 删除挂号订单
     */
    @Delete("DELETE FROM registration_order WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 检查同一医生同一时间是否已有预约
     */
    @Select("SELECT COUNT(*) FROM registration_order WHERE doctor_id = #{doctorId} " +
            "AND register_time = #{registerTime} AND status IN ('pending', 'confirmed')")
    int countByDoctorAndTime(Long doctorId, LocalDateTime registerTime);

    /**
     * 统计医生同一小时内已预约数量
     */
    @Select("SELECT COUNT(*) FROM registration_order WHERE doctor_id = #{doctorId} " +
            "AND DATE(register_time) = DATE(#{registerTime}) " +
            "AND HOUR(register_time) = HOUR(#{registerTime}) " +
            "AND status IN ('PENDING', 'CONFIRMED')")
    int countByDoctorAndHour(Long doctorId, LocalDateTime registerTime);

    /**
     * 统计医生当天的预约数量
     */
    @Select("SELECT COUNT(*) FROM registration_order WHERE doctor_id = #{doctorId} " +
            "AND DATE(register_time) = DATE(#{date}) AND status IN ('pending', 'confirmed')")
    int countByDoctorAndDate(Long doctorId, LocalDateTime date);
}