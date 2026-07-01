package com.hospital.service;

import com.hospital.entity.Department;
import com.hospital.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 科室业务服务类
 * 调整字段访问（如department.getName()代替department.getDeptName()）
 */
@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 获取所有科室列表（包括非活跃的）
     *
     * @return 返回所有科室的列表
     */
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    /**
     * 获取所有活跃科室列表
     *
     * @return 返回所有活跃科室的列表
     */
    public List<Department> getAllActiveDepartments() {
        return departmentRepository.findAllActive();
    }

    /**
     * 根据ID获取科室信息
     * @param id 科室ID
     * @return 返回对应ID的科室对象
     */
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    /**
     * 新增科室 - 调整字段验证
     * @param department 要新增的科室对象
     * @return 返回是否新增成功
     * @throws RuntimeException 当科室名称已存在时抛出异常
     */
    public boolean addDepartment(Department department) {
        // 检查科室名称是否已存在（使用name字段）
        if (departmentRepository.countByName(department.getName(), 0L) > 0) {
            throw new RuntimeException("科室名称已存在");
        }
        return departmentRepository.insert(department) > 0;
    }

    /**
     * 创建科室（与addDepartment功能相同，提供兼容性）
     * @param department 要创建的科室对象
     * @return 返回是否创建成功
     */
    public boolean createDepartment(Department department) {
        return addDepartment(department);
    }

    /**
     * 更新科室信息 - 调整字段验证
     * @param department 要更新的科室对象
     * @return 返回是否更新成功
     * @throws RuntimeException 当科室名称已存在时抛出异常
     */
    public boolean updateDepartment(Department department) {
        // 检查科室名称是否已存在（使用name字段）
        if (departmentRepository.countByName(department.getName(), department.getId()) > 0) {
            throw new RuntimeException("科室名称已存在");
        }
        return departmentRepository.update(department) > 0;
    }

    /**
     * 删除科室
     * @param id 要删除的科室ID
     * @return 返回是否删除成功
     */
    public boolean deleteDepartment(Long id) {
        return departmentRepository.deleteById(id) > 0;
    }
}