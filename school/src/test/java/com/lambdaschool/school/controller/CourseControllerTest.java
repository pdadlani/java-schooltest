package com.lambdaschool.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.school.controller.CourseController;
import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.Instructor;
import com.lambdaschool.school.service.CourseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CourseController.class, secure = false)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private ArrayList<Course> courseList;

    @Before
    public void setUp() {
        courseList = new ArrayList<>();
        Instructor i1 = new Instructor("Sally");
        Instructor i2 = new Instructor("Lucy");
        Instructor i3 = new Instructor("Charlie");

        Course c1 = new Course("Data Science", i1);
        c1.setCourseid(1);
        courseList.add(c1);
        Course c2 = new Course("JavaScript", i1);
        c2.setCourseid(2);
        courseList.add(c2);
        Course c3 = new Course("Node.js", i1);
        c3.setCourseid(3);
        courseList.add(c3);
        Course c4 = new Course("Java Back End", i2);
        c4.setCourseid(4);
        courseList.add(c4);
        Course c5 = new Course("Mobile IOS", i2);
        c5.setCourseid(5);
        courseList.add(c5);
        Course c6 = new Course("Mobile Android", i3);
        c6.setCourseid(6);
        courseList.add(c6);

    }

    @Test
    public void listAllCourses() throws Exception {
        String apiUrl = "/courses/courses";

        Mockito.when(courseService.findAll()).thenReturn(courseList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);

        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(courseList);

        assertEquals("Rest API returns List", er, tr);
    }

    @Test
    public void addNewCourse() throws Exception {
        String apiUrl = "/courses/course/add";

        String courseName = "Blockchain";
        Course newCourse = new Course(courseName, null);
        newCourse.setCourseid(8);

        ObjectMapper mapper = new ObjectMapper();
        String courseString = mapper.writeValueAsString(newCourse);

        Mockito.when(courseService.save(any(Course.class))).thenReturn(newCourse);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(courseString);

        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

}
