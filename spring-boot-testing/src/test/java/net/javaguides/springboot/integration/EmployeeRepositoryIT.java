package net.javaguides.springboot.integration;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for repository layer
 */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIT {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;


    @BeforeEach
    public void setup() {

        employee = Employee.builder()
                .firstName("Joan")
                .lastName("Roa")
                .email("setoba1192@gmail.com")
                .build();
    }

    @DisplayName(("Junit test for save employee operation"))
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        //given - precondition or setup (replaced by setup method)

        //when - action or the behavior that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);

    }

    // Junit test for get all employees operation
    @DisplayName("Junit test for get all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {

        //given - precondition or setup (replaced by setup method)

        Employee employee1 = Employee.builder()
                .firstName("Sebastian")
                .lastName("Sanchez")
                .email("sanchez@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        //when - action or the behavior that we are goint to test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    // Junit test for get employ by id operation
    @DisplayName("Junit test for get employ by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        //given - precondition or setup - replaced by setup method
        employeeRepository.save(employee);
        //when - action or the behavior that we are goint to test
        Employee retrievedEmployee = employeeRepository.findById(employee.getId()).get();

        //then - verify the output
        assertThat(retrievedEmployee).isNotNull();

    }

    // Junit test for get employee by email operation
    @DisplayName("Junit test for get employee by email operation")
    @Test
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {

        //given - precondition or setup (replaced by setup method)

        employeeRepository.save(employee);

        //when - action or the behavior that we are goint to test
        Employee retreivedEmployee = employeeRepository.findByEmail(employee.getEmail()).get();

        //then - verify the output
        assertThat(retreivedEmployee).isNotNull();
    }

    // Junit test for update employee operation
    @DisplayName("Junit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmploy_thenReturnUpdatedEmployee() {

        //given - precondition or setup (replaced by setup method)

        employeeRepository.save(employee);
        //when - action or the behavior that we are goint to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("setoba1192@hotmail.com");
        savedEmployee.setFirstName("Paula");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("setoba1192@hotmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Paula");
    }

    // Junit test for delete employee operation
    @DisplayName("Junit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemove() {

        //given - precondition or setup (replaced by setup method)

        employeeRepository.save(employee);

        //when - action or the behavior that we are goint to test
        employeeRepository.delete(employee);
        Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(deletedEmployee).isEmpty();

    }

    // Junit test for custom JPQL operation
    @DisplayName("Junit test for custom JPQL operation")
    @Test
    public void givenFirstAndLastName_whenFindByJPQL_thenEmployeeObject() {

        //given - precondition or setup (replaced by setup method)

        employeeRepository.save(employee);

        String firstName = "Joan";
        String lastName = "Roa";

        //when - action or the behavior that we are goint to test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // Junit test for custom JPQLNamed params operation
    @DisplayName("Junit test for custom JPQLNamed params operation")
    @Test
    public void givenFirstAndLastName_whenFindByJPQLNamedParams_thenEmployeeObject() {

        //given - precondition or setup (replaced by setup method)

        employeeRepository.save(employee);

        String firstName = "Joan";
        String lastName = "Roa";

        //when - action or the behavior that we are goint to test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // Junit test for custom query using native SQL with index params
    @DisplayName("Junit test for custom query using native SQL with index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {

        //given - precondition or setup (replaced by setup method)
        employeeRepository.save(employee);

        String firstName = "Joan";
        String lastName = "Roa";

        //when - action or the behavior that we are goint to test
        Employee savedEmployee = employeeRepository.findByNativeSQL(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // Junit test for custom query using native SQL with index params
    @DisplayName("Junit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {

        //given - precondition or setup (replaced by setup method)
        employeeRepository.save(employee);

        String firstName = "Joan";
        String lastName = "Roa";

        //when - action or the behavior that we are goint to test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(firstName, lastName);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
