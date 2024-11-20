// package com.CRUD.Employee.controller;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.test.web.servlet.MockMvc;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(EmployeeController.class)
// public class EmployeeControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Test
//     void shouldReturnEmptyList() throws Exception {
//         mockMvc.perform(get("/api/employees"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("[]")); // Expecting an empty list
//     }
// }

