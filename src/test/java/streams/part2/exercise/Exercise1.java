package streams.part2.exercise;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"ConstantConditions", "unused"})
public class Exercise1 {

    @Test
    public void calcTotalYearsSpentInEpam() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Long hours = employees.stream()
            .map(Employee::getJobHistory)
            .flatMap(Collection::stream)
            .filter(entry -> "epam".equalsIgnoreCase(entry.getEmployer()))
            .mapToLong(JobHistoryEntry::getDuration)
            .sum();

        assertEquals(19, hours.longValue());
    }

    @Test
    public void findPersonsWithQaExperience() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        Set<Person> workedAsQa = employees.stream()
            .filter(history -> history
                .getJobHistory().stream()
                .anyMatch(
                    entry -> "qa".equalsIgnoreCase(entry.getPosition())))
            .map(Employee::getPerson)
            .collect(Collectors.toSet());

        assertEquals(new HashSet<>(Arrays.asList(
            employees.get(2).getPerson(),
            employees.get(4).getPerson(),
            employees.get(5).getPerson()
        )), workedAsQa);
    }

    @Test
    public void composeFullNamesOfEmployeesUsingLineSeparatorAsDelimiter() {
        List<Employee> employees = getEmployees();

        // TODO реализация
        String result = employees.stream()
            .map(Employee::getPerson)
            .map(Person::getFullName)
            .collect(Collectors.joining("\n"));

        assertEquals("Иван Мельников\n"
            + "Александр Дементьев\n"
            + "Дмитрий Осинов\n"
            + "Анна Светличная\n"
            + "Игорь Толмачёв\n"
            + "Иван Александров", result);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void groupPersonsByFirstPositionUsingToMap() {
        List<Employee> employees = getEmployees();
        // TODO реализация

        Function<Employee, String> keyMapper = employee -> employee.getJobHistory().get(0)
            .getPosition();
        Function<Employee, Set<Person>> valueMapper = employee -> new HashSet<>(
            Collections.singletonList(employee.getPerson()));
        BinaryOperator<Set<Person>> mergeFunction = (s1, s2) -> {
            s1.addAll(s2);
            return s1;
        };
        Map<String, Set<Person>> result = employees.stream()
            .collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));

        Map<String, Set<Person>> expected = new HashMap<>();
        expected.put("QA", new HashSet<>(
            Arrays.asList(employees.get(2).getPerson(), employees.get(5).getPerson())));
        expected.put("dev", Collections.singleton(employees.get(0).getPerson()));
        expected.put("tester", new HashSet<>(Arrays.asList(
            employees.get(1).getPerson(),
            employees.get(3).getPerson(),
            employees.get(4).getPerson()))
        );
        assertEquals(expected, result);
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void groupPersonsByFirstPositionUsingGroupingByCollector() {
        List<Employee> employees = getEmployees();

        Function<Employee, String> classifier = employee -> employee.getJobHistory().get(0)
            .getPosition();
        // TODO реализация
        Map<String, Set<Person>> result = employees.stream()
            .collect(Collectors.groupingBy(classifier,
                Collectors.mapping(Employee::getPerson, Collectors.toSet())));

        Map<String, Set<Person>> expected = new HashMap<>();
        expected.put("QA", new HashSet<>(
            Arrays.asList(employees.get(2).getPerson(), employees.get(5).getPerson())));
        expected.put("dev", Collections.singleton(employees.get(0).getPerson()));
        expected.put("tester", new HashSet<>(Arrays.asList(
            employees.get(1).getPerson(),
            employees.get(3).getPerson(),
            employees.get(4).getPerson()))
        );
        assertEquals(expected, result);
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
