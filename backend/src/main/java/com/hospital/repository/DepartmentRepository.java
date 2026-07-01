package com.hospital.repository;

import com.hospital.entity.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 科室信息数据访问接口
 * 修正@Results映射，确保字段与数据库列正确对应
 */
@Mapper
public interface DepartmentRepository {

    /**
     * 查询所有启用的科室
     */
    @Select("SELECT id, name, description, director, phone, location, active, create_time, update_time " +
            "FROM department WHERE active = 1 ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),  // 修正：原映射dept_name错误
            @Result(property = "description", column = "description"),
            @Result(property = "director", column = "director"),  // 新增映射
            @Result(property = "phone", column = "phone"),        // 新增映射
            @Result(property = "location", column = "location"),  // 新增映射
            @Result(property = "active", column = "active"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<Department> findAllActive();

    /**
     * 根据ID查询科室
     */
    @Select("SELECT id, name, description, director, phone, location, active, create_time, update_time " +
            "FROM department WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "director", column = "director"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "location", column = "location"),
            @Result(property = "active", column = "active"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    Department findById(Long id);

    // 其他方法保持不变，但需确保SQL查询列与实体类字段匹配
    @Select("SELECT id, name, description, director, phone, location, active, create_time, update_time " +
            "FROM department ORDER BY active DESC, name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "director", column = "director"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "location", column = "location"),
            @Result(property = "active", column = "active"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<Department> findAll();

    /**
     * 新增科室 - 修正INSERT语句，匹配数据库列
     */
    @Insert("INSERT INTO department(name, description, director, phone, location, active) " +
            "VALUES(#{name}, #{description}, #{director}, #{phone}, #{location}, #{active})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Department department);

    /**
     * 更新科室信息 - 修正UPDATE语句
     */
    @Update("UPDATE department SET name=#{name}, description=#{description}, director=#{director}, " +
            "phone=#{phone}, location=#{location}, active=#{active}, update_time=CURRENT_TIMESTAMP WHERE id=#{id}")
    int update(Department department);

    // 以下方法保持不变
    @Update("UPDATE department SET active=0, update_time=CURRENT_TIMESTAMP WHERE id=#{id}")
    int deleteById(Long id);

    @Select("SELECT COUNT(*) FROM department WHERE name = #{name} AND id != #{excludeId}")
    int countByName(String name, Long excludeId);
}