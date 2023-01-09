package net.javaguides.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeControllerITT {

    @Container
    private static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }

    // Junit integration test for create employee method
    @DisplayName("Junit integration test for createEmployee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();

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

    // Junit integration test for getAllEmployees
    @DisplayName("Junit integration test for getAllEmployees")
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
        employeeRepository.saveAll(listOfEmployees);

        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    // positive scenario -valid employee id
    // Junit integration test for getEmployeeById Rest API
    @DisplayName("Junit integration test for getEmployeeById Rest API")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();
        employeeRepository.save(employee);

        long employeeId = employee.getId();

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
    @DisplayName("Junit integration test for getEmployeeById Rest API that return not found")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmty() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();
        employeeRepository.save(employee);

        long employeeId = 1L;

        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // Junit integration test for updateEmployee REST API
    @DisplayName("Junit itegration test for updateEmployee REST API")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        //given - precondition or setup
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

        employeeRepository.save(savedEmployee);

        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Junit integration test for updateEmployee when employee not found REST API")
    @Test
    public void givenNotFoundEmployee_whenUpdateEmployee_thenReturnNotFound() throws Exception {

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

        employeeRepository.save(savedEmployee);

        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // Junit test for deleteEmployee REST API
    @DisplayName("Junit integration test for deleteEmployee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200Status() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or the behavior that we are goint to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("Employee deleted successfully"));

    }
}
