package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    /**
     * Define custom query using JPQL with index params
     *
     * @param firstName
     * @param lastName
     * @return
     */
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    @Query("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    /**
     * Define custom query using Native SQL with index params
     * @param firstName
     * @param lastName
     * @return
     */
    @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByNativeSQL(String firstName, String lastName);
}
