package com.hpos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpos.entity.Department;
import com.hpos.mapper.DepartmentMapper;
import com.hpos.service.DepartmentService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentServiceImpl
        extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {

    @Cacheable(value = "hpos:departments", key = "'all'", unless = "#result == null || #result.isEmpty()")
    @Override
    public List<Department> list() {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getStatus, 1)
               .orderByAsc(Department::getSortOrder);
        return baseMapper.selectList(wrapper);
    }
}
