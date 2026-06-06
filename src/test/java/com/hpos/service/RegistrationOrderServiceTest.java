package com.hpos.service;

import com.hpos.dto.RegistrationRequest;
import com.hpos.service.impl.RegistrationOrderServiceImpl;
import com.hpos.entity.*;
import com.hpos.mapper.RegistrationOrderMapper;
import com.hpos.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationOrderServiceTest {

    @Mock
    private PatientService patientService;
    @Mock
    private DoctorService doctorService;
    @Mock
    private RegistrationSourceService sourceService;
    @Mock
    private DepartmentService departmentService;
    @Mock
    private OrderNotificationService notificationService;
    @Mock
    private RegistrationOrderMapper orderMapper;

    private RegistrationOrderServiceImpl orderService;

    private RegistrationRequest validRequest;

    @BeforeEach
    void setUp() {
        orderService = new RegistrationOrderServiceImpl();
        ReflectionTestUtils.setField(orderService, "patientService", patientService);
        ReflectionTestUtils.setField(orderService, "doctorService", doctorService);
        ReflectionTestUtils.setField(orderService, "sourceService", sourceService);
        ReflectionTestUtils.setField(orderService, "departmentService", departmentService);
        ReflectionTestUtils.setField(orderService, "notificationService", notificationService);
        ReflectionTestUtils.setField(orderService, "baseMapper", orderMapper);

        validRequest = new RegistrationRequest();
        validRequest.setPatientName("张三");
        validRequest.setIdCard("110101199001011234");
        validRequest.setPhone("13800138001");
        validRequest.setGender(1);
        validRequest.setDeptId(1);
        validRequest.setDoctorId(1);
        validRequest.setSourceId(1);
        validRequest.setWorkDate("2026-06-07");
        validRequest.setPeriod(1);
    }

    @Test
    void createOrder_withNewPatient_shouldSucceed() {
        when(patientService.findByPhone("13800138001")).thenReturn(null);
        when(patientService.save(any(Patient.class))).thenReturn(true);
        when(sourceService.deductSource(1)).thenReturn(true);

        RegistrationSource source = new RegistrationSource();
        source.setId(1);
        source.setFee(BigDecimal.valueOf(15.00));
        when(sourceService.getById(1)).thenReturn(source);

        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setRealName("张明");
        when(doctorService.getById(1)).thenReturn(doctor);

        String orderNo = orderService.createOrder(validRequest);

        assertNotNull(orderNo);
        assertTrue(orderNo.startsWith("REG"));
        verify(patientService).save(any(Patient.class));
        verify(sourceService).deductSource(1);
        verify(notificationService).sendOrderSuccessNotification(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void createOrder_withExistingPatient_shouldNotCreateNew() {
        Patient existing = new Patient();
        existing.setId(1);
        existing.setRealName("张三");
        existing.setPhone("13800138001");
        when(patientService.findByPhone("13800138001")).thenReturn(existing);

        RegistrationSource source = new RegistrationSource();
        source.setId(1);
        source.setFee(BigDecimal.valueOf(15.00));
        when(sourceService.getById(1)).thenReturn(source);

        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setRealName("张明");
        when(doctorService.getById(1)).thenReturn(doctor);

        when(sourceService.deductSource(1)).thenReturn(true);

        String orderNo = orderService.createOrder(validRequest);

        assertNotNull(orderNo);
        verify(patientService, never()).save(any(Patient.class));
    }

    @Test
    void createOrder_whenSourceFull_shouldThrow() {
        when(patientService.findByPhone("13800138001")).thenReturn(null);
        when(patientService.save(any(Patient.class))).thenReturn(true);
        when(sourceService.deductSource(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(validRequest));
        assertTrue(ex.getMessage().contains("号源已满"));
    }

    @Test
    void createOrder_whenNotificationFails_shouldNotRollback() {
        when(patientService.findByPhone("13800138001")).thenReturn(null);
        when(patientService.save(any(Patient.class))).thenReturn(true);
        when(sourceService.deductSource(1)).thenReturn(true);

        RegistrationSource source = new RegistrationSource();
        source.setId(1);
        source.setFee(BigDecimal.valueOf(15.00));
        when(sourceService.getById(1)).thenReturn(source);

        Doctor doctor = new Doctor();
        doctor.setId(1);
        doctor.setRealName("张明");
        when(doctorService.getById(1)).thenReturn(doctor);

        doThrow(new RuntimeException("MQ down")).when(notificationService)
                .sendOrderSuccessNotification(anyString(), anyString(), anyString(), anyString(), anyString());

        String orderNo = orderService.createOrder(validRequest);
        assertNotNull(orderNo);
    }

    @Test
    void cancelOrder_shouldRestoreSource() {
        RegistrationOrder order = new RegistrationOrder();
        order.setId(1);
        order.setPatientId(1);
        order.setSourceId(1);
        order.setStatus(0);

        when(orderMapper.selectById(1)).thenReturn(order);

        orderService.cancelOrder(1, 1);

        verify(sourceService).restoreSource(1);
        verify(orderMapper).updateById(any(RegistrationOrder.class));
    }

    @Test
    void cancelOrder_wrongPatient_shouldThrow() {
        RegistrationOrder order = new RegistrationOrder();
        order.setId(1);
        order.setPatientId(2);
        order.setStatus(0);

        when(orderMapper.selectById(1)).thenReturn(order);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.cancelOrder(1, 1));
        assertTrue(ex.getMessage().contains("无权"));
    }

    @Test
    void cancelOrder_alreadyCancelled_shouldThrow() {
        RegistrationOrder order = new RegistrationOrder();
        order.setId(1);
        order.setPatientId(1);
        order.setStatus(2);

        when(orderMapper.selectById(1)).thenReturn(order);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.cancelOrder(1, 1));
        assertTrue(ex.getMessage().contains("已取消"));
    }
}
