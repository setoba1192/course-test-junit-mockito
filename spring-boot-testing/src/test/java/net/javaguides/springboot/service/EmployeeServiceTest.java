package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {

        employee = Employee.builder()
                .id(1L)
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();

    }

    // Junit test for saveEmployee method
    @DisplayName("Junit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup (moved to setup method)

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee))
                .willReturn(employee);

        //when - action or the behavior that we are goint to test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // Junit test for saveEmployee method
    @DisplayName("Junit test for saveEmployee method wich throw exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // Junit test for method getAllEmployees
    @DisplayName("Junit test for method getAllEmployees")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Sebastian")
                .lastName("Sanchez")
                .email("setoba1192@hotmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action or the behavior that we are goint to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // Junit test for method getAllEmployees negative scenario
    @DisplayName("Junit test for method getAllEmployees (negative scenario")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {

        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behavior that we are goint to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    // Junit test for getEmployeeById
    @DisplayName("Junit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){

        //given - precondition or setup
        given(employeeRepository.findById(employee.getId()))
                .willReturn(Optional.of(employee));

        //when - action or the behavior that we are goint to test
        Employee foundEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then - verify the output
        assertThat(foundEmployee).isNotNull();
    }

    // Junit test for updateEmployee method
    @DisplayName("Junit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        //given - precondition or setup
        given(employeeRepository.save(employee))
                .willReturn(employee);

        employee.setFirstName("Sebas");
        employee.setEmail("setoba1192@hotmail.com");

        //when - action or the behavior that we are goint to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("setoba1192@hotmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Sebas");
    }

    // Junit test for deleteEmployee method
    @DisplayName("Junit test for deleteEmployee method")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenNothing(){

        //given - precondition or setup
        long employeeId = 1l;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //when - action or the behavior that we are goint to test
        employeeService.deleteEmployee(employeeId);

        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

}
