package com.hospital.repository;

import com.hospital.entity.Doctor;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 医生信息数据访问接口
 * 处理医生表的CRUD操作，支持关联查询
 */
@Mapper
public interface DoctorRepository {

    /**
     * 查询所有医生（包含科室名称）
     */
    @Select("SELECT d.*, dep.name as department_name FROM doctor d " +
            "LEFT JOIN department dep ON d.department_id = dep.id " +
            "ORDER BY d.name")
    List<Doctor> findAll();

    /**
     * 查询所有可用医生（包含科室名称）
     */
    @Select("SELECT d.*, dep.name as department_name FROM doctor d " +
            "LEFT JOIN department dep ON d.department_id = dep.id " +
            "WHERE d.available = 1 ORDER BY d.name")
    List<Doctor> findAllActive();

    /**
     * 根据ID查询医生信息
     */
    @Select("SELECT d.*, dep.name as department_name FROM doctor d " +
            "LEFT JOIN department dep ON d.department_id = dep.id " +
            "WHERE d.id = #{id}")
    Doctor findById(Long id);

    /**
     * 根据科室ID查询医生
     */
    @Select("SELECT d.*, dep.name as department_name FROM doctor d " +
            "LEFT JOIN department dep ON d.department_id = dep.id " +
            "WHERE d.department_id = #{departmentId} AND d.available = 1 ORDER BY d.name")
    List<Doctor> findByDepartmentId(Long departmentId);

    /**
     * 新增医生
     */
    @Insert("INSERT INTO doctor(name, department_id, title, specialty, max_patients, current_patients, available) " +
            "VALUES(#{name}, #{departmentId}, #{title}, #{specialty}, #{maxPatients}, #{currentPatients}, #{available})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Doctor doctor);

    /**
     * 更新医生信息
     */
    @Update("UPDATE doctor SET name=#{name}, department_id=#{departmentId}, title=#{title}, " +
            "specialty=#{specialty}, max_patients=#{maxPatients}, current_patients=#{currentPatients}, " +
            "available=#{available}, update_time=CURRENT_TIMESTAMP WHERE id=#{id}")
    int update(Doctor doctor);

    /**
     * 更新医生当前挂号数
     */
    @Update("UPDATE doctor SET current_patients = #{currentPatients} WHERE id = #{id}")
    int updateCurrentPatients(Long id, Integer currentPatients);

    /**
     * 检查医生姓名是否存在（同一科室内）
     */
    @Select("SELECT COUNT(*) FROM doctor WHERE name = #{name} AND department_id = #{departmentId} AND id != #{excludeId}")
    int countByNameAndDepartment(String name, Long departmentId, Long excludeId);

    /**
     * 删除医生（逻辑删除）
     */
    @Update("UPDATE doctor SET available = 0, update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int deleteById(Long id);
}