package com.hpos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpos.entity.Department;
import com.hpos.mapper.DepartmentMapper;
import com.hpos.service.DepartmentService;
import org.springframework.stereotype.Service;

/**
 * 科室 Service 实现类
 */
@Service // 标记为 Spring Bean
public class DepartmentServiceImpl
        extends ServiceImpl<DepartmentMapper, Department>
        implements DepartmentService {
    // ServiceImpl 已提供 IService 的所有基本 CRUD 实现
    // 你加一个测试方法看看 baseMapper 到底有没有值
    public void test() {
        // 这里的 baseMapper 是 ServiceImpl 自带的，不用你声明
        System.out.println(baseMapper); // 会打印出 DepartmentMapper 的代理对象
        System.out.println(baseMapper.getClass().getName()); // 是代理类，不是 null
}}
