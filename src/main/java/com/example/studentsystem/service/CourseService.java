package com.example.studentsystem.service;

import com.example.studentsystem.entity.Course;
import com.example.studentsystem.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    //1.查看所有课程(TEACHER)
    public List<Course> findAll() {
        return courseMapper.findAll();
    }

    //2.根据老师ID查询课程
    public List<Course> findByTeacherId(Long teacherId) {
        return courseMapper.findByTeacherId(teacherId);
    }

    //3.新增课程
    public void addCourse(Course course) {
        courseMapper.insert(course);
    }

    //4.修改课程信息
    public void updateCourse(Course course) {
        courseMapper.update(course);
    }

    //5.删除课程
    public void deleteCourse(Long id) {
        courseMapper.deleteById(id);
    }

    //6.查看选修某门课程的所有学生
    public List<com.example.studentsystem.entity.User> findStudentsByCourseId(Long courseId) {
        return courseMapper.findStudentsByCourseId(courseId);
    }
}