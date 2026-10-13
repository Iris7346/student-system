package com.example.studentsystem.mapper;

import com.example.studentsystem.entity.Course;
import com.example.studentsystem.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface CourseMapper {

    //1.查询所有课程
    @Select("SELECT id, name, teacher_id AS teacherId, max_students AS maxStudents, description FROM course")
    List<Course> findAll();

    //2.根据ID查询课程
    @Select("SELECT id, name, teacher_id AS teacherId, max_students AS maxStudents, description FROM course WHERE id = #{id}")
    Course findById(Long id);

    //3.根据老师ID查询所教的课程
    @Select("SELECT id, name, teacher_id AS teacherId, max_students AS maxStudents, description FROM course WHERE teacher_id = #{teacherId}")
    List<Course> findByTeacherId(Long teacherId);

    //4.新增课程
    @Insert("INSERT INTO course(name, teacher_id, max_students, description) " +
            "VALUES(#{name}, #{teacherId}, #{maxStudents}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")//插入后将数据库自动生成的id回填到Course对象中
    void insert(Course course);

    //5.修改课程信息
    @Update("UPDATE course SET name = #{name}, max_students = #{maxStudents}, " +
            "description = #{description} WHERE id = #{id}")
    void update(Course course);

    //6.删除课程
    @Delete("DELETE FROM course WHERE id = #{id}")
    void deleteById(Long id);

    //7.根据课程ID查询选了这门课的学生列表
    //需要返回学生信息，用 @Select 直接联表查
    @Select("SELECT u.id, u.username, u.real_name AS realName, u.role, u.created_at AS createdAt " +
            "FROM user u JOIN enrollment e ON u.id = e.student_id " +
            "WHERE e.course_id = #{courseId}")
    List<User> findStudentsByCourseId(Long courseId);
}