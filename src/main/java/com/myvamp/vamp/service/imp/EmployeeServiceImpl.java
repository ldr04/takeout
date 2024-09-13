package com.myvamp.vamp.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myvamp.vamp.entity.Employee;
import com.myvamp.vamp.mapper.EmployeeMapper;
import com.myvamp.vamp.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{

}
