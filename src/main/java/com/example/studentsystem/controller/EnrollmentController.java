package com.example.studentsystem.controller;

import com.example.studentsystem.common.Result;
import com.example.studentsystem.entity.Course;
import com.example.studentsystem.entity.User;
import com.example.studentsystem.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@PreAuthorize("hasRole('STUDENT')")//这个接口只有学生能访问
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    //1.学生选课
    @PostMapping
    public Result<Void> enroll(@RequestBody Map<String, Long> data, Authentication authentication) {
        //从 Spring Security 上下文里拿到当前登录学生的ID
        User currentUser = (User) authentication.getPrincipal();
        Long courseId = data.get("courseId"); //前端传过来的课程ID

        enrollmentService.enroll(currentUser.getId(), courseId);
        return Result.success();
    }

    //2.学生退课
    @DeleteMapping("/{id}")
    public Result<Void> cancelEnroll(@PathVariable Long id) {
        enrollmentService.cancelEnroll(id);
        return Result.success();
    }

    //3.学生查看已选课程列表
    @GetMapping("/student")
    public Result<List<Course>> getMyCourses(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return Result.success(enrollmentService.getMyCourses(currentUser.getId()));
    }
}