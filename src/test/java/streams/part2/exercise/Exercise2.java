package streams.part2.exercise;

import java.util.stream.Collectors;
import lambda.data.Employee;
import lambda.data.JobHistoryEntry;
import lambda.data.Person;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ConstantConditions")
public class Exercise2 {

    class PersonEmployer {

        Person person;
        String employer;
        int duration;

        public PersonEmployer(Person person, String employer) {
            this.person = person;
            this.employer = employer;
        }

        public PersonEmployer(Person person, String employer, int duration) {
            this.person = person;
            this.employer = employer;
            this.duration = duration;
        }

        public String getEmployer() {
            return employer;
        }

        public Person getPerson() {
            return person;
        }

        public int getDuration() {
            return duration;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PersonEmployer that = (PersonEmployer) o;
            return Objects.equals(person, that.person) &&
                Objects.equals(employer, that.employer);
        }

        @Override
        public int hashCode() {

            return Objects.hash(person, employer);
        }
    }


    /**
     * Преобразовать список сотрудников в отображение [компания -> множество людей, когда-либо
     * работавших в этой компании].
     *
     * Входные данные: [ { {Иван Мельников 30}, [ {2, dev, "EPAM"}, {1, dev, "google"} ] }, {
     * {Александр Дементьев 28}, [ {2, tester, "EPAM"}, {1, dev, "EPAM"}, {1, dev, "google"} ] }, {
     * {Дмитрий Осинов 40}, [ {3, QA, "yandex"}, {1, QA, "EPAM"}, {1, dev, "mail.ru"} ] }, { {Анна
     * Светличная 21}, [ {1, tester, "T-Systems"} ] } ]
     *
     * Выходные данные: [ "EPAM" -> [ {Иван Мельников 30}, {Александр Дементьев 28}, {Дмитрий Осинов
     * 40} ], "google" -> [ {Иван Мельников 30}, {Александр Дементьев 28} ], "yandex" -> [ {Дмитрий
     * Осинов 40} ] "mail.ru" -> [ {Дмитрий Осинов 40} ] "T-Systems" -> [ {Анна Светличная 21} ] ]
     */
    @Test
    public void employersStuffList() {

        List<Employee> employees = getEmployees();

        Map<String, Set<Person>> result = employees.stream()
            .flatMap(employee -> employee.getJobHistory().stream()
                .map(entry -> new PersonEmployer(employee.getPerson(), entry.getEmployer())))
            .collect(Collectors.groupingBy(
                PersonEmployer::getEmployer,
                Collectors.mapping(PersonEmployer::getPerson, Collectors.toSet())));

        Map<String, Set<Person>> expected = new HashMap<>();
        expected
            .put("yandex", new HashSet<>(Collections.singletonList(employees.get(2).getPerson())));
        expected
            .put("mail.ru", new HashSet<>(Collections.singletonList(employees.get(2).getPerson())));
        expected.put("EPAM", new HashSet<>(Arrays.asList(
            employees.get(0).getPerson(),
            employees.get(1).getPerson(),
            employees.get(4).getPerson(),
            employees.get(5).getPerson()
        )));
        expected.put("google", new HashSet<>(Arrays.asList(
            employees.get(0).getPerson(),
            employees.get(1).getPerson()
        )));
        expected.put("T-Systems", new HashSet<>(Arrays.asList(
            employees.get(3).getPerson(),
            employees.get(5).getPerson()
        )));
        assertEquals(expected, result);
    }

    /**
     * Преобразовать список сотрудников в отображение [компания -> множество людей, начавших свою
     * карьеру в этой компании].
     *
     * Пример.
     *
     * Входные данные: [ { {Иван Мельников 30}, [ {2, dev, "EPAM"}, {1, dev, "google"} ] }, {
     * {Александр Дементьев 28}, [ {2, tester, "EPAM"}, {1, dev, "EPAM"}, {1, dev, "google"} ] }, {
     * {Дмитрий Осинов 40}, [ {3, QA, "yandex"}, {1, QA, "EPAM"}, {1, dev, "mail.ru"} ] }, { {Анна
     * Светличная 21}, [ {1, tester, "T-Systems"} ] } ]
     *
     * Выходные данные: [ "EPAM" -> [ {Иван Мельников 30}, {Александр Дементьев 28} ], "yandex" -> [
     * {Дмитрий Осинов 40} ] "T-Systems" -> [ {Анна Светличная 21} ] ]
     */
    @Test
    public void indexByFirstEmployer() {
        List<Employee> employees = getEmployees();

        Map<String, Set<Person>> result = employees.stream()
            .map(employee -> new PersonEmployer(employee.getPerson(),
                employee.getJobHistory().get(0).getEmployer()))
            .collect(Collectors.groupingBy(
                PersonEmployer::getEmployer,
                Collectors.mapping(PersonEmployer::getPerson, Collectors.toSet())));

        Map<String, Set<Person>> expected = new HashMap<>();
        expected
            .put("yandex", new HashSet<>(Collections.singletonList(employees.get(2).getPerson())));
        expected.put("EPAM", new HashSet<>(Arrays.asList(
            employees.get(0).getPerson(),
            employees.get(1).getPerson(),
            employees.get(4).getPerson()
        )));
        expected.put("T-Systems", new HashSet<>(Arrays.asList(
            employees.get(3).getPerson(),
            employees.get(5).getPerson()
        )));
        assertEquals(expected, result);
    }

    /**
     * Преобразовать список сотрудников в отображение [компания -> сотрудник, суммарно проработавший
     * в ней наибольшее время]. Гарантируется, что такой сотрудник будет один.
     */
    @Test
    public void greatestExperiencePerEmployer() {
        List<Employee> employees = getEmployees();

        Map<String, Person> collect = employees.stream()
            .flatMap(employee -> employee.getJobHistory().stream()
                .map(entry -> new PersonEmployer(employee.getPerson(), entry.getEmployer(),
                    entry.getDuration())))
            .collect(Collectors.toMap(personEmployer -> personEmployer, PersonEmployer::getDuration,
                (d1, d2) -> d1 + d2)).entrySet().stream().collect(Collectors
                .toMap(entry -> entry.getKey().getEmployer(), Map.Entry::getKey,
                    (e1, e2) -> e1.duration >= e2.duration ? e1 : e2)).entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().person));

        Map<String, Person> expected = new HashMap<>();
        expected.put("EPAM", employees.get(4).getPerson());
        expected.put("google", employees.get(1).getPerson());
        expected.put("yandex", employees.get(2).getPerson());
        expected.put("mail.ru", employees.get(2).getPerson());
        expected.put("T-Systems", employees.get(5).getPerson());
        assertEquals(expected, collect);
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
                    new JobHistoryEntry(2, "dev", "EPAM"),
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