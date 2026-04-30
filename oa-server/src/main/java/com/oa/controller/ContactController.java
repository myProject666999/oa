package com.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oa.common.Result;
import com.oa.entity.Contact;
import com.oa.entity.Department;
import com.oa.entity.User;
import com.oa.service.ContactService;
import com.oa.service.DepartmentService;
import com.oa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/page")
    public Result<Page<Contact>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            Contact contact) {
        Page<Contact> page = new Page<>(current, size);
        LambdaQueryWrapper<Contact> wrapper = new LambdaQueryWrapper<>();
        if (contact != null) {
            if (StringUtils.hasText(contact.getRealName())) {
                wrapper.like(Contact::getRealName, contact.getRealName());
            }
            if (StringUtils.hasText(contact.getPhone())) {
                wrapper.like(Contact::getPhone, contact.getPhone());
            }
            if (contact.getDeptId() != null) {
                wrapper.eq(Contact::getDeptId, contact.getDeptId());
            }
        }
        wrapper.eq(Contact::getDeleted, "0");
        wrapper.orderByAsc(Contact::getDeptId);
        Page<Contact> result = contactService.page(page, wrapper);
        return Result.success(result);
    }

    @GetMapping("/list")
    public Result<List<Contact>> list(Contact contact) {
        LambdaQueryWrapper<Contact> wrapper = new LambdaQueryWrapper<>();
        if (contact != null) {
            if (StringUtils.hasText(contact.getRealName())) {
                wrapper.like(Contact::getRealName, contact.getRealName());
            }
            if (contact.getDeptId() != null) {
                wrapper.eq(Contact::getDeptId, contact.getDeptId());
            }
        }
        wrapper.eq(Contact::getDeleted, "0");
        wrapper.orderByAsc(Contact::getDeptId);
        List<Contact> list = contactService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Contact> getById(@PathVariable Long id) {
        Contact contact = contactService.getById(id);
        return Result.success(contact);
    }

    @PostMapping("/sync")
    public Result<Boolean> sync() {
        List<User> users = userService.list(new LambdaQueryWrapper<User>()
                .eq(User::getDeleted, "0"));
        
        for (User user : users) {
            LambdaQueryWrapper<Contact> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Contact::getUserId, user.getId());
            Contact exist = contactService.getOne(wrapper);
            
            Contact contact = new Contact();
            contact.setUserId(user.getId());
            contact.setRealName(user.getRealName());
            contact.setPhone(user.getPhone());
            contact.setEmail(user.getEmail());
            contact.setDeptId(user.getDeptId());
            contact.setPostId(user.getPostId());
            contact.setDeleted("0");
            
            if (exist != null) {
                contact.setId(exist.getId());
                contactService.updateById(contact);
            } else {
                contactService.save(contact);
            }
        }
        return Result.success(true);
    }
}