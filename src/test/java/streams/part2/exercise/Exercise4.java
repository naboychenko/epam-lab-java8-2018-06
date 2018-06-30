package streams.part2.exercise;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.counting;
import static org.junit.Assert.assertEquals;

public class Exercise4 {

    /**
     * Выбирает из текста наиболее часто встречающиеся слова. Подсчет слов выполняется без учета их
     * регистра, т.е. "Привет", "привет", "пРиВеТ" - одно и то же слово. Если некоторые слова имеют
     * одинаковую частоту, то в выходном списке они упорядочиваются в лексикографическом порядке.
     *
     * @param text Исходный текст в котором слова (в смешанном регистре) разделены пробелами.
     * @param numberWords Количество наиболее часто встречающихся слов, которые необходимо
     * отобрать.
     * @return Список отобранных слов (в нижнем регистре).
     */
    private List<String> getFrequentlyOccurringWords(String text, int numberWords) {

        Comparator<Entry<String, Long>> comparingByValue = (entry1, entry2) -> Long
            .compare(entry2.getValue(), entry1.getValue());
        Comparator<Entry<String, Long>> comparingByKey = Entry.comparingByKey();

        Map<String, Long> frequencyCounted =
            Arrays.stream(text.split(" "))
                .collect(Collectors.groupingBy(String::toLowerCase, counting()));

        return frequencyCounted.entrySet()
            .stream()
            .sorted(comparingByValue.thenComparing(comparingByKey))
            .limit(numberWords)
            .map(Entry::getKey)
            .collect(Collectors.toList());
    }

    @Test
    public void test1() {
        String source = "Мама мыла мыла мыла раму";

        List<String> result = getFrequentlyOccurringWords(source, 5);

        assertEquals(Arrays.asList("мыла", "мама", "раму"), result);
    }

    @Test
    public void test2() {
        String source =
            "Lorem ipsum dolor sit amet consectetur adipiscing elit Sed sodales consectetur purus at "
                + "faucibus Donec mi quam tempor vel ipsum non faucibus suscipit massa Morbi lacinia velit "
                + "blandit tincidunt efficitur Vestibulum eget metus imperdiet sapien laoreet faucibus Nunc "
                + "eget vehicula mauris ac auctor lorem Lorem ipsum dolor sit amet consectetur adipiscing elit "
                + "Integer vel odio nec mi tempor dignissim";

        List<String> result = getFrequentlyOccurringWords(source, 10);

        assertEquals(Arrays
            .asList("consectetur", "faucibus", "ipsum", "lorem", "adipiscing", "amet", "dolor",
                "eget", "elit", "mi"), result);
    }
}
