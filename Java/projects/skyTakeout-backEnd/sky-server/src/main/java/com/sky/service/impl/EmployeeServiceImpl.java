package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.*;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeePageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对，使用md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void add(EmployeeDTO employeeDTO) {
        String username = employeeDTO.getUsername();
        String name = employeeDTO.getName();
        String phone = employeeDTO.getPhone();
        String sex = employeeDTO.getSex();
        String idNumber = employeeDTO.getIdNumber();

        // 数据校验
        // 用户名校验
        if (username == null || username.isEmpty())
            throw new FormValueIsNullException(MessageConstant.USERNAME_IS_NULL);
        else if (username.length() < 4 || username.length() > 32)
            throw new UsernameLengthWrongException(MessageConstant.USERNAME_LENGTH_WRONG);
        Employee employee = employeeMapper.getByUsername(username);
        // 已经注册
        if (employee != null) throw new UsernameExistsException(MessageConstant.USERNAME_EXISTS);

        // 姓名校验
        if (name == null || name.isEmpty())
            throw new FormValueIsNullException(MessageConstant.NAME_IS_NULL);
        else if (name.length() < 2 || name.length() > 32)
            throw new NameLengthWrongException(MessageConstant.NAME_LENGTH_WRONG);

        // 手机号校验
        if (phone == null || phone.isEmpty())
            throw new FormValueIsNullException(MessageConstant.PHONE_NUMBER_IS_NULL);
        else if (!phone.matches("^1[3-9]\\d{9}$"))
            throw new PhoneNumberIllegalException(MessageConstant.PHONE_NUMBER_ILLEGAL);

        // 性别校验
        if (!Objects.equals(sex, "0") && !Objects.equals(sex, "1")) throw new SexNotExistException(MessageConstant.SEX_NOT_EXIST);

        // 身份证号码校验
        if (idNumber == null || idNumber.isEmpty())
            throw new FormValueIsNullException(MessageConstant.ID_NUMBER_IS_NULL);
        else if (!idNumber.matches("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"))
            throw new IdNumberIllegalException(MessageConstant.ID_NUMBER_ILLEGAL);

        employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        // 设置默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 设置账号状态
        employee.setStatus(StatusConstant.ENABLE);
        // 设置创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 设置创建人ID和修改人ID
        Long id = BaseContext.getCurrentId();
        employee.setCreateUser(id);
        employee.setUpdateUser(id);

        // 插入数据
        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        try (Page<Employee> page = PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize())) {
            employeeMapper.pageQuery(employeePageQueryDTO);
            return new PageResult<>(page.getTotal(), page.getResult());
        }
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void changeStatus(Integer status, Long id) {
        if (status == null || !Objects.equals(status, StatusConstant.ENABLE) && !Objects.equals(status, StatusConstant.DISABLE))
            throw new FormValueIsNullException(MessageConstant.STATUS_IS_NOT_DEFINED);
        if (id == null || id <= 0)
            throw new FormValueIsNullException(MessageConstant.EMPLOYEE_NOT_FOUND);
        Integer count = employeeMapper.changeStatus(status, id);
        if (count != 1)
            throw new EmployeeStatusChangeFailedException(MessageConstant.EMPLOYEE_STATUS_CHANGE_FAILED);
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        if (id == null || id <= 0)
            throw new FormValueIsNullException(MessageConstant.EMPLOYEE_NOT_FOUND);
        Employee employee = employeeMapper.getById(id);
        if (employee == null)
            throw new EmployeeNotFoundException(MessageConstant.EMPLOYEE_NOT_FOUND);
        return employee;
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        String username = employeeDTO.getUsername();
        String name = employeeDTO.getName();
        String phone = employeeDTO.getPhone();
        String sex = employeeDTO.getSex();
        String idNumber = employeeDTO.getIdNumber();

        // 数据校验
        // 用户名校验
        if (username == null || username.isEmpty())
            throw new FormValueIsNullException(MessageConstant.USERNAME_IS_NULL);
        else if (username.length() < 4 || username.length() > 32)
            throw new UsernameLengthWrongException(MessageConstant.USERNAME_LENGTH_WRONG);
        Employee employee = employeeMapper.getByUsername(username);
        // 用户名已经被占用
        if (employee != null && !Objects.equals(employee.getId(), employeeDTO.getId())) throw new UsernameExistsException(MessageConstant.USERNAME_EXISTS);

        // 姓名校验
        if (name == null || name.isEmpty())
            throw new FormValueIsNullException(MessageConstant.NAME_IS_NULL);
        else if (name.length() < 2 || name.length() > 32)
            throw new NameLengthWrongException(MessageConstant.NAME_LENGTH_WRONG);

        // 手机号校验
        if (phone == null || phone.isEmpty())
            throw new FormValueIsNullException(MessageConstant.PHONE_NUMBER_IS_NULL);
        else if (!phone.matches("^1[3-9]\\d{9}$"))
            throw new PhoneNumberIllegalException(MessageConstant.PHONE_NUMBER_ILLEGAL);

        // 性别校验
        if (!Objects.equals(sex, "0") && !Objects.equals(sex, "1")) throw new SexNotExistException(MessageConstant.SEX_NOT_EXIST);

        // 身份证号码校验
        if (idNumber == null || idNumber.isEmpty())
            throw new FormValueIsNullException(MessageConstant.ID_NUMBER_IS_NULL);
        else if (!idNumber.matches("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"))
            throw new IdNumberIllegalException(MessageConstant.ID_NUMBER_ILLEGAL);

        // 封装对象
        employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        // 设置更新时间
        employee.setUpdateTime(LocalDateTime.now());
        // 设置更新人ID
        employee.setUpdateUser(BaseContext.getCurrentId());
        // 更新数据
        Integer count = employeeMapper.update(employee);
        if (count != 1) throw new EmplyeeUpdateFailedException(MessageConstant.EMPLOYEE_UPDATE_FAILED);
    }
}
