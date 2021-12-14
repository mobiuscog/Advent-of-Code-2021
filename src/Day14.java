import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

public class Day14
{
  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day14.dat"));

    String template = lines.get(0);

    var insertionRules = new HashMap<String, Mapping>();
    for (var index = 2; index < lines.size(); index++) {
      var split = lines.get(index).split(" -> ");
      var prefixPair = String.valueOf(split[0].charAt(0)) + split[1].charAt(0);
      var suffixPair = String.valueOf(split[1].charAt(0)) + split[0].charAt(1);
      insertionRules.put(split[0], new Mapping(prefixPair, suffixPair, split[1].charAt(0)));
    }

    // Part 1
    var sortedCount = processSteps(10, template, insertionRules).values().stream().sorted().toArray(Long[]::new);
    System.out.printf("Part 1 result: %d %n", sortedCount[sortedCount.length - 1] - sortedCount[0]);

    // Part 2
    sortedCount = processSteps(40, template, insertionRules).values().stream().sorted().toArray(Long[]::new);
    System.out.printf("Part 2 result: %d %n", sortedCount[sortedCount.length - 1] - sortedCount[0]);
  }

  private static HashMap<Character, Long> processSteps(
      int stepCount,
      String template,
      HashMap<String, Mapping> insertionRules)
  {
    var pairCountMap = new HashMap<String, Long>();
    var charCount = new HashMap<Character, Long>();
    for (int i = 0; i < template.length(); i++) {
      charCount.merge(template.charAt(i), 1L, Long::sum);
      if (i < template.length() - 1) {
        pairCountMap.merge(template.substring(i, i + 2), 1L, Long::sum);
      }
    }

    for (int step = 0; step < stepCount; step++) {
      var currentCounts = new HashMap<>(pairCountMap);
      for (var pair : pairCountMap.keySet().stream().toList()) {
        var count = currentCounts.get(pair);
        pairCountMap.merge(insertionRules.get(pair).prefixPair(), count, Long::sum);
        pairCountMap.merge(insertionRules.get(pair).suffixPair(), count, Long::sum);
        pairCountMap.merge(pair, -count, Long::sum);
        charCount.merge(insertionRules.get(pair).insert(), count, Long::sum);
      }
      pairCountMap.entrySet().stream().filter(e -> e.getValue() <= 0).map(Entry::getKey).toList()
          .forEach(pairCountMap::remove);
    }
    return charCount;
  }

  record Mapping(String prefixPair, String suffixPair, char insert) { }
}
