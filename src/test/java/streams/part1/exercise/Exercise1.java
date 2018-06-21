package streams.part1.exercise;

import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"ConstantConditions", "unused"})
public class Exercise1 {

    @Test
    public void findPersonsEverWorkedInEpam() {
        List<Employee> employees = getEmployees();

        // TODO реализация, использовать Collectors.toList()
        List<Person> personsEverWorkedInEpam = employees.stream()
                .filter(employee -> employee.getJobHistory().stream()
                        .anyMatch(entry -> "epam".equalsIgnoreCase(entry.getEmployer())))
                .map(Employee::getPerson)
                .collect(Collectors.toList());

        List<Person> expected = Arrays.asList(
                employees.get(0).getPerson(),
                employees.get(1).getPerson(),
                employees.get(4).getPerson(),
                employees.get(5).getPerson());
        assertEquals(expected, personsEverWorkedInEpam);
    }

    @Test
    public void findPersonsBeganCareerInEpam() {
        List<Employee> employees = getEmployees();

        // TODO реализация, использовать Collectors.toList()
        List<Person> startedFromEpam = employees.stream()
                .filter(employee -> employee.getJobHistory().stream()
                        .findFirst()
                        .get()
                        .getEmployer()
                        .equalsIgnoreCase("epam"))
                .map(Employee::getPerson)
                .collect(Collectors.toList());

        List<Person> expected = Arrays.asList(
                employees.get(0).getPerson(),
                employees.get(1).getPerson(),
                employees.get(4).getPerson());
        assertEquals(expected, startedFromEpam);
    }

    @Test
    public void findAllCompanies() {
        List<Employee> employees = getEmployees();

        // TODO реализация, использовать Collectors.toSet()
        Set<String> companies = employees.stream()
                .map(Employee::getJobHistory)
                .flatMap(Collection::stream)
                .map(JobHistoryEntry::getEmployer)
                .collect(Collectors.toSet());

        Set<String> expected = new HashSet<>();
        expected.add("EPAM");
        expected.add("google");
        expected.add("yandex");
        expected.add("mail.ru");
        expected.add("T-Systems");
        assertEquals(expected, companies);
    }

    @Test
    public void findMinimalAgeOfEmployees() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Integer minimalAge = employees.stream()
                .map(employee -> employee.getPerson().getAge())
                .min(Integer::compareTo)
                .get();

        assertEquals(21, minimalAge.intValue());
    }

    private static List<Employee> getEmployees() {
        return Arrays.asList(
                new Employee(
                        new Person("Иван", "Мельников", 30),
                        Arrays.asList(
                                new JobHistoryEntry(2, "dev", "EPAM"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Александр", "Дементьев", 28),
                        Arrays.asList(
                                new JobHistoryEntry(1, "tester", "EPAM"),
                                new JobHistoryEntry(1, "dev", "EPAM"),
                                new JobHistoryEntry(1, "dev", "google")
                        )),
                new Employee(
                        new Person("Дмитрий", "Осинов", 40),
                        Arrays.asList(
                                new JobHistoryEntry(3, "QA", "yandex"),
                                new JobHistoryEntry(1, "QA", "mail.ru"),
                                new JobHistoryEntry(1, "dev", "mail.ru")
                        )),
                new Employee(
                        new Person("Анна", "Светличная", 21),
                        Collections.singletonList(
                                new JobHistoryEntry(1, "tester", "T-Systems")
                        )),
                new Employee(
                        new Person("Игорь", "Толмачёв", 50),
                        Arrays.asList(
                                new JobHistoryEntry(5, "tester", "EPAM"),
                                new JobHistoryEntry(6, "QA", "EPAM")
                        )),
                new Employee(
                        new Person("Иван", "Александров", 33),
                        Arrays.asList(
                                new JobHistoryEntry(2, "QA", "T-Systems"),
                                new JobHistoryEntry(3, "QA", "EPAM"),
                                new JobHistoryEntry(1, "dev", "EPAM")
                        ))
        );
    }
}