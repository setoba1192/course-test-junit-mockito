package net.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // Junit test for create employee method
    @DisplayName("Junit test for createEmployee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    // Junit test for getAllEmployees
    @DisplayName("Junit test for getAllEmployees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenEmployeesList() throws Exception {

        //given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder()
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build());
        listOfEmployees.add(Employee.builder()
                .firstName("Sebastian")
                .lastName("Sanchez")
                .email("setoba1192@hotmail.com")
                .build());

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    // positive scenario -valid employee id
    // Junit test for getEmployeeById Rest API
    @DisplayName("Junit test for getEmployeeById Rest API")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();

        long employeeId = 1L;

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    // negative scenario -valid employee id
    // Junit test for getEmployeeById Rest API that return not found
    @DisplayName("Junit test for getEmployeeById Rest API that return not found")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmty() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();

        long employeeId = 1L;

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // Junit test for updateEmployee REST API
    @DisplayName("Junit test for updateEmployee REST API")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Joan Sebastian")
                .lastName("Roa Sanchez")
                .email("setoba1192@hotmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class))).willReturn(updatedEmployee);

        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }
}
