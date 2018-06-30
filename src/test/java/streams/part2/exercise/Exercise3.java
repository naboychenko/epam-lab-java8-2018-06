package streams.part2.exercise;

import java.util.stream.IntStream;
import org.junit.Test;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

public class Exercise3 {

    @Test
    public void createLimitedStringWithOddNumbersSeparatedBySpaces() {
        int countNumbers = 10;

        String result = IntStream
            .iterate(1, i -> i + 2)
            .limit(countNumbers)
            .mapToObj(String::valueOf)
            .collect(joining(" "));

        assertEquals("1 3 5 7 9 11 13 15 17 19", result);
    }

    @Test
    public void extractEvenNumberedCharactersToNewString() {
        String source = "abcdefghijklm";

        String result = IntStream.range(0, source.length())
            .filter(i -> (i & 1) == 0)
            .collect(
                StringBuilder::new,
                (sb, i) -> sb.append(source.charAt(i)),
                StringBuilder::append).toString();

        assertEquals("acegikm", result);
    }
}
