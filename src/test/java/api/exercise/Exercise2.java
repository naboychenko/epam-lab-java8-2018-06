package api.exercise;

import org.junit.Test;

import java.util.function.BinaryOperator;

import static org.junit.Assert.*;

public class Exercise2 {

    @Test
    public void sequentialPrefixTestSum() {
        Integer[] original = {1, 2, 3, 4, 5, 6, 7, 8};

        Integer[] result = sequentialPrefix(original, Integer::sum);

        assertNotSame(original, result);
        assertArrayEquals(new Integer[]{1, 3, 6, 10, 15, 21, 28, 36}, result);
    }

    @Test
    public void sequentialPrefixTestMult() {
        Integer[] original = {1, 2, 3, 4, 5, 6, 7, 8};

        Integer[] result = sequentialPrefix(original, (left, right) -> left * right);

        assertNotSame(original, result);
        assertArrayEquals(new Integer[]{1, 2, 6, 24, 120, 720, 5_040, 40_320}, result);
    }

    @Test
    public void sequentialPrefixTestLiningByMax() {
        Integer[] original = {1, 2, 3, 2, 1, 5, 7, 5};

        Integer[] result = sequentialPrefix(original, Integer::max);

        assertNotSame(original, result);
        assertArrayEquals(new Integer[]{1, 2, 3, 3, 3, 5, 7, 7}, result);
    }

    /**
     * Выполняет операцию сканирования в однопоточном режиме.
     * Не модифицирует исходный набор данных.
     *
     * @param source   Массив исходных элементов.
     * @param operator Оператор сканирования.
     * @return Результат сканирования.
     * @see <a href="https://habr.com/company/epam_systems/blog/247805">Сканирование</a>
     */
    private static <T> T[] sequentialPrefix(T[] source, BinaryOperator<T> operator) {
        T[] result = source.clone();
        for (int step = 0; step < log2(result.length); step++) {
            int dist = pow(2, step);
            for (int i = result.length - 1; i > dist - 1; i--) {
                result[i] = operator.apply(result[i], result[i - dist]);
            }
        }
        return result;
    }

    @Test
    public void log2TestNormalCases() {
        assertEquals(0, log2(1));
        assertEquals(1, log2(2));
        assertEquals(1, log2(3));
        assertEquals(2, log2(4));
        assertEquals(30, log2(Integer.MAX_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void log2WithZeroValueShouldFail() {
        log2(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void log2WithNegativeValuesShouldFail() {
        log2(Integer.MIN_VALUE);
    }

    /**
     * Вычисляет двоичный логарифм положительного числа.
     *
     * @param value Аргумент.
     * @return Логарифм по основанию 2 от аргумента.
     * @throws IllegalArgumentException Если {@code value <= 0}
     */
    private static int log2(int value) throws IllegalArgumentException {
        if (value <= 0)
            throw new IllegalArgumentException();

        return (int) (Math.log(value) / Math.log(2));
    }

    @Test
    public void powTestNormalCases() {
        assertEquals(1, pow(0, 0));
        assertEquals(1, pow(1, 0));
        assertEquals(1, pow(1, 1));
        assertEquals(5, pow(5, 1));
        assertEquals(25, pow(5, 2));
        assertEquals(1000, pow(10, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void powWithNegativeBaseShouldFail() {
        pow(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void powWithNegativeDegreeShouldFail() {
        pow(0, -1);
    }

    /**
     * Возводит неотрицательное число в неотрицательную степень.
     *
     * @param base   Основание степени.
     * @param degree Показатель степени.
     * @return Значение {@code base}<sup>{@code degree}</sup>
     * @throws IllegalArgumentException Если {@code base < 0} или {@code degree < 0}
     */
    private static int pow(int base, int degree) throws IllegalArgumentException {
        if(base < 0 || degree < 0)
            throw new IllegalArgumentException();
        int result = 1;
        for(int i = 0; i < degree; i++)
            result *= base;
        return result;
    }
}
