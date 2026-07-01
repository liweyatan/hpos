package com.hospital.controller;

import com.hospital.entity.User;
import com.hospital.entity.Patient;
import com.hospital.service.RegistrationOrderService;
import com.hospital.service.PatientService;
import com.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

/**
 * 首页控制器 - 修复版本
 * 处理通用页面路由映射，确保所有页面可访问
 */
@Controller
public class HomeController {

    @Autowired
    private RegistrationOrderService registrationOrderService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private UserService userService;

    /**
     * 处理根路径请求 - 转发到Vue SPA的index.html
     */
    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    /**
     * 处理登录页面请求
     * @return 返回登录页面视图名称 "login"
     */
    @GetMapping("/login")
    public String login() {
        // 直接返回登录页面视图名称，无需额外处理
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        // 直接返回登录页面视图名称，无需额外处理
        return "register";
    }

    /**
     * 处理预约挂号页面请求
     * @return 返回预约挂号页面视图名称 "appointment"
     */
    @GetMapping("/appointment")
    public String appointment() {
        // 直接返回预约挂号页面视图名称，无需额外处理
        return "appointment";
    }

    /**
     * 处理我的预约页面请求 - 修复：根据用户权限显示对应患者的预约
     * @param model 用于向视图传递数据的模型对象
     * @param session HTTP会话，用于获取当前登录用户信息
     * @return 返回我的预约页面视图名称 "appointments"
     */
    @GetMapping("/appointments")
    public String appointments(Model model, @RequestParam(required = false) String status, HttpSession session) {
        try {
            // 从session中获取当前登录用户信息
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                // 未登录，重定向到登录页面
                return "redirect:/login";
            }
            
            Long patientId;
            
            // 根据用户角色决定如何获取患者ID
            if ("ADMIN".equals(currentUser.getRole())) {
                // 管理员可以查看所有预约
                patientId = null; // null表示获取所有预约
                model.addAttribute("pageTitle", "所有预约管理");
            } else {
                // 普通用户只能查看自己的预约
                // 通过手机号查找对应的患者信息
                Patient patient = patientService.getPatientByPhone(currentUser.getPhone());
                if (patient == null) {
                    // 未找到对应的患者信息
                    model.addAttribute("errorMessage", "未找到您的患者信息，请先创建患者档案");
                    model.addAttribute("appointments", java.util.Collections.emptyList());
                    model.addAttribute("pendingCount", 0);
                    model.addAttribute("confirmedCount", 0);
                    model.addAttribute("completedCount", 0);
                    model.addAttribute("cancelledCount", 0);
                    model.addAttribute("pageTitle", "我的预约");
                    model.addAttribute("systemName", "智慧医院管理系统");
                    return "appointments";
                }
                patientId = patient.getId();
                model.addAttribute("pageTitle", "我的预约");
            }

            // 获取预约数据
            var appointments = registrationOrderService.getAppointmentsByPatientId(patientId, status);

            // 计算各状态数量（忽略大小写）
            long pendingCount = appointments.stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count();
            long confirmedCount = appointments.stream().filter(a -> "CONFIRMED".equalsIgnoreCase(a.getStatus())).count();
            long completedCount = appointments.stream().filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus())).count();
            long cancelledCount = appointments.stream().filter(a -> "CANCELLED".equalsIgnoreCase(a.getStatus())).count();

            // 添加模型属性
            model.addAttribute("appointments", appointments);
            model.addAttribute("pendingCount", pendingCount);
            model.addAttribute("confirmedCount", confirmedCount);
            model.addAttribute("completedCount", completedCount);
            model.addAttribute("cancelledCount", cancelledCount);
            model.addAttribute("systemName", "智慧医院管理系统");
            
            // 添加当前用户信息到模型
            model.addAttribute("currentUser", currentUser);

        } catch (Exception e) {
            // 发生异常时设置错误信息
            model.addAttribute("errorMessage", "加载预约数据失败：" + e.getMessage());
            model.addAttribute("appointments", java.util.Collections.emptyList());
            model.addAttribute("pendingCount", 0);
            model.addAttribute("confirmedCount", 0);
            model.addAttribute("completedCount", 0);
            model.addAttribute("cancelledCount", 0);
            model.addAttribute("pageTitle", "我的预约");
            model.addAttribute("systemName", "智慧医院管理系统");
        }
        
        return "appointments";
    }

    /**
     * 处理取消预约请求
     */
    @PostMapping("/appointments/cancel")
    public String cancelAppointment(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean result = registrationOrderService.cancelAppointment(id);
            if (result) {
                redirectAttributes.addFlashAttribute("successMessage", "预约取消成功");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "取消预约失败，预约不存在或已取消");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "取消预约失败：" + e.getMessage());
        }

        return "redirect:/appointments";
    }

    /**
     * 处理患者注册页面请求
     * @return 返回患者注册页面视图名称 "patients"
     */
    @GetMapping("/patients")
    public String patients() {
        // 直接返回患者注册页面视图名称，无需额外处理
        return "patients";
    }

    /**
     * 处理管理员后台页面请求
     * 添加权限验证，非管理员用户无法访问
     *
     * @return 返回管理员后台页面视图名称 "admin" 或重定向到首页
     */
    @GetMapping("/admin")
    public String admin(HttpSession session) {
        // 检查用户是否登录且为管理员
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || !"ADMIN".equals(currentUser.getRole())) {
            // 非管理员用户重定向到首页
            return "redirect:/";
        }
        
        // 管理员可以访问后台页面
        return "admin";
    }

}