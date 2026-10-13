package com.example.studentsystem.controller;

import com.example.studentsystem.common.Result;
import com.example.studentsystem.entity.Course;
import com.example.studentsystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController{

    @Autowired
    private CourseService courseService;

    //1.查看所有课程(TEACHER)
    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")//明确只有老师能看
    public Result<List<Course>> getAllCourses(){
        return Result.success(courseService.findAll());
    }

    //2.创建课程
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Void> createCourse(@RequestBody Course course,
                                     org.springframework.security.core.Authentication authentication){
        //从 Spring Security 上下文中获取当前登录用户
        com.example.studentsystem.entity.User currentUser =
                (com.example.studentsystem.entity.User) authentication.getPrincipal();

        //把当前登录老师的ID设置进去
        course.setTeacherId(currentUser.getId());

        courseService.addCourse(course);
        return Result.success();
    }

    //3.修改课程
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Void> updateCourse(@PathVariable Long id, @RequestBody Course course){
        course.setId(id);//把路径里的 id 塞进对象
        courseService.updateCourse(course);
        return Result.success();
    }

    //4.删除课程
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Void> deleteCourse(@PathVariable Long id){
        courseService.deleteCourse(id);
        return Result.success();
    }

    //5.老师查看某门课程的学生列表
    @GetMapping("/{courseId}/students")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<List<com.example.studentsystem.entity.User>> getStudentsByCourseId(@PathVariable Long courseId){
        return Result.success(courseService.findStudentsByCourseId(courseId));
    }

    //6.学生查看可选课程列表（方法级权限覆盖类级权限）
    @GetMapping("/available")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<List<Course>> getAvailableCourses(){
        return Result.success(courseService.findAll());
    }
}