package sortbenchmark;

import java.util.Comparator;

public class EmployeeSalaryComparator implements Comparator<Employee> {
    @Override
    public int compare(Employee a, Employee b) {
        return Double.compare(a.getSalary(), b.getSalary());
    }
}

