package com.example.studentsystem.service;

import com.example.studentsystem.entity.Course;
import com.example.studentsystem.entity.Enrollment;
import com.example.studentsystem.mapper.CourseMapper;
import com.example.studentsystem.mapper.EnrollmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 学生选课
     * 业务规则 1：防止重复选课
     * 业务规则 2：检查课程人数是否已满
     */
    public void enroll(Long studentId, Long courseId) {
        //1.检查该学生是否已经选过这门课
        int count = enrollmentMapper.countByStudentAndCourse(studentId, courseId);
        if (count > 0) {
            throw new RuntimeException("您已经选过这门课了，不能重复选课！");//抛出错误信息
        }

        //2.检查这门课是否已经选满
        //先查信息
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在！");
        }

        //再查已选人数
        int currentCount = enrollmentMapper.countByCourseId(courseId);
        if (currentCount >= course.getMaxStudents()) {
            throw new RuntimeException("这门课人数已满，无法选课！");
        }

        //3.检查通过，插入选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollmentMapper.insert(enrollment);
    }

    /**
     * 学生退课
     */
    public void cancelEnroll(Long enrollmentId) {
        enrollmentMapper.deleteById(enrollmentId);
    }

    /**
     * 查看学生已选的课程列表
     */
    public List<Course> getMyCourses(Long studentId) {
        return enrollmentMapper.findCoursesByStudentId(studentId);
    }
}