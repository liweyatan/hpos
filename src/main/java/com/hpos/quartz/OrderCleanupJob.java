package com.hpos.quartz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hpos.entity.RegistrationOrder;
import com.hpos.entity.RegistrationSource;
import com.hpos.mapper.RegistrationOrderMapper;
import com.hpos.mapper.RegistrationSourceMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderCleanupJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(OrderCleanupJob.class);
    private static final int TIMEOUT_MINUTES = 30;

    @Autowired
    private RegistrationOrderMapper orderMapper;

    @Autowired
    private RegistrationSourceMapper sourceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(JobExecutionContext context) {
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(TIMEOUT_MINUTES);

        LambdaQueryWrapper<RegistrationOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegistrationOrder::getStatus, 0)
               .lt(RegistrationOrder::getCreateTime, deadline);

        List<RegistrationOrder> expiredOrders = orderMapper.selectList(wrapper);

        for (RegistrationOrder order : expiredOrders) {
            order.setStatus(2);
            orderMapper.updateById(order);

            RegistrationSource source = sourceMapper.selectById(order.getSourceId());
            if (source != null && source.getAvailableCount() < source.getTotalCount()) {
                source.setAvailableCount(source.getAvailableCount() + 1);
                sourceMapper.updateById(source);
            }

            log.info("已取消超时订单: {}, 号源已释放: {}", order.getOrderNo(), order.getSourceId());
        }

        if (!expiredOrders.isEmpty()) {
            log.info("本次清理超时订单 {} 笔", expiredOrders.size());
        }
    }
}
