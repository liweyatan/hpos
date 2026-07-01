package com.hospital.repository;

import com.hospital.entity.Patient;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 患者信息数据访问接口
 * 处理患者表的CRUD操作
 */
@Mapper
public interface PatientRepository {

    /**
     * 查询所有患者
     */
    @Select("SELECT * FROM patient ORDER BY create_time DESC")
    List<Patient> findAll();

    /**
     * 根据ID查询患者
     */
    @Select("SELECT * FROM patient WHERE id = #{id}")
    Patient findById(Long id);

    /**
     * 根据身份证号查询患者
     */
    @Select("SELECT * FROM patient WHERE id_card = #{idCard}")
    Patient findByIdCard(String idCard);

    /**
     * 根据手机号查询患者
     */
    @Select("SELECT * FROM patient WHERE phone = #{phone}")
    Patient findByPhone(String phone);

    /**
     * 新增患者
     */
    @Insert("INSERT INTO patient(name, id_card, phone, gender, birth_date) " +
            "VALUES(#{name}, #{idCard}, #{phone}, #{gender}, #{birthDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Patient patient);

    /**
     * 更新患者信息
     */
    @Update("UPDATE patient SET name=#{name}, id_card=#{idCard}, phone=#{phone}, " +
            "gender=#{gender}, birth_date=#{birthDate}, update_time=CURRENT_TIMESTAMP WHERE id=#{id}")
    int update(Patient patient);

    /**
     * 删除患者
     */
    @Delete("DELETE FROM patient WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 检查身份证号是否存在
     */
    @Select("SELECT COUNT(*) FROM patient WHERE id_card = #{idCard} AND id != #{excludeId}")
    int countByIdCard(String idCard, Long excludeId);

    /**
     * 检查手机号是否存在
     */
    @Select("SELECT COUNT(*) FROM patient WHERE phone = #{phone} AND id != #{excludeId}")
    int countByPhone(String phone, Long excludeId);

    /**
     * 根据姓名模糊查询患者
     */
    @Select("SELECT * FROM patient WHERE name LIKE CONCAT('%', #{name}, '%') ORDER BY create_time DESC")
    List<Patient> findByNameContaining(String name);
}