package com.myvamp.vamp.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myvamp.vamp.common.BaseContext;
import com.myvamp.vamp.common.R;
import com.myvamp.vamp.entity.Employee;
import com.myvamp.vamp.entity.User;
import com.myvamp.vamp.service.EmployeeService;
import com.myvamp.vamp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.myvamp.vamp.common.R.success;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的密码md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据username查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lqw);
        //3.如果返回为空直接返回失败结果
        if(emp ==null){
            return R.error("登陆失败：用户名或密码错误");
        }
        //4.密码比对,不匹配返回失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("登陆失败：用户名或密码错误");
        }
        //5.密码匹配，查询员工状态status;
        if(emp.getStatus()==0){
            return R.error("登陆失败：账号已禁用");
        }
        //6.登陆成功，将用户id存入session,返回成功信息
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }


    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<Employee> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success(null);
    }


    /**
     * 新增员工
     * @param employee
     * @return
     */

    @PostMapping
    public R<User> addEmployee(@RequestBody Employee employee){

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        boolean save = employeeService.save(employee);
        if(save){
            log.info("保存成功：{}",employee.toString());
            return R.success(null);
        }
        return R.error("保存出错");
    }

    /**
     * 员工信息查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize,String name) {

        log.info("page={},pageSize={},name={}", page, pageSize, name);
        //分页构造器
        Page pageInfo = new Page(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        //判空并添加过滤条件
        lqw.like(name != null, Employee::getName, name);
        lqw.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, lqw);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<Employee> update(@RequestBody Employee employee){

//        Long empId = (Long) request.getSession().getAttribute("employee");
//        BaseContext.setCurrentId(empId);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);



//        log.info(employee.toString());
        return R.success(employee);

    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employeeById = employeeService.getById(id);
        log.info("employee by id:{}",employeeById);
        if(employeeById!=null){
            return R.success(employeeById);
        }
        return R.error("没有查询到对应信息");
    }



}
