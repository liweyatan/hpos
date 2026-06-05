package com.hpos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hpos.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 科室 Mapper 接口
 * <p>
 * 继承 MyBatis-Plus 的 BaseMapper，自动提供 CRUD 方法：
 * insert、deleteById、updateById、selectById、selectList、selectPage 等
 * 无需编写 XML 即可完成大部分数据库操作
 * </p>
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
