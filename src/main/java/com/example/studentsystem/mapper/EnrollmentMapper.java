package com.example.studentsystem.mapper;

import com.example.studentsystem.entity.Enrollment;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface EnrollmentMapper{

    //1.学生选课(插入一条选课记录)
    @Insert("INSERT INTO enrollment(student_id, course_id) VALUES(#{studentId}, #{courseId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Enrollment enrollment);

    //2.学生退(删除记录)
    @Delete("DELETE FROM enrollment WHERE id = #{id}")
    void deleteById(Long id);

    //3.检查该学生是否已经选过这门课(防止重复选课)
    //返回查到的记录数,如果 >0 说明已经选过了
    @Select("SELECT COUNT(*) FROM enrollment WHERE student_id = #{studentId} AND course_id = #{courseId}")
    int countByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    //4.统计某门课程目前已经选了多少人(检查是否超过人数上限)
    @Select("SELECT COUNT(*) FROM enrollment WHERE course_id = #{courseId}")
    int countByCourseId(Long courseId);

    //5.查看某个学生已选的课程列表
    //使用JOIN，直接查出课程详细信息
    @Select("SELECT c.* FROM course c " +
            "JOIN enrollment e ON c.id = e.course_id " +
            "WHERE e.student_id = #{studentId}")
    List<com.example.studentsystem.entity.Course> findCoursesByStudentId(Long studentId);

    //6.老师查看某门课程的学生列表(需要连表查用户表)
    //返回User对象
    @Select("SELECT u.* FROM user u " +
            "JOIN enrollment e ON u.id = e.student_id " +
            "WHERE e.course_id = #{courseId}")
    List<com.example.studentsystem.entity.User> findStudentsByCourseId(Long courseId);
}