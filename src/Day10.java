import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Day10
{
  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day10.dat"));

    var openings = Set.of('(', '[', '{', '<');
    var closings = Map.of(')', 3, ']', 57, '}', 1197, '>', 25137);

    // Part 1
    var illegals = new HashMap<Character, Integer>();
    var openStack = new ArrayDeque<Character>();
    var incomplete = new ArrayList<ArrayDeque<Character>>();
    for (var line : lines) {
      openStack.clear();
      for (char c : line.trim().toCharArray()) {
        if (closings.containsKey(c)) {
          if (openStack.isEmpty() || (c != openStack.getFirst() + 1 && c != openStack.getFirst() + 2)) {
            illegals.merge(c, 1, Integer::sum);
            openStack.clear();
            break;
          }
          else {
            openStack.pop();
          }
        }
        else if (openings.contains(c)) {
          openStack.push(c);
        }
      }
      if (!openStack.isEmpty()) {
        incomplete.add(openStack.clone());
      }
    }
    var points =
        illegals.entrySet().stream().map(e -> e.getValue() * closings.get(e.getKey())).mapToInt(Integer::intValue)
            .sum();
    System.out.printf("Part 1 score: %d %n", points);

    var scores = new ArrayList<Long>();
    for (var incompleteStack: incomplete) {
      long score = 0;
      while (!incompleteStack.isEmpty()) {
        char c = incompleteStack.pop();
        score *= 5;
        switch (c) {
          case '(' -> score += 1;
          case '[' -> score += 2;
          case '{' -> score += 3;
          case '<' -> score += 4;
        }
      }
      scores.add(score);
    }

    var middleScore = scores.stream().sorted().skip(scores.size()/2).limit(1).findFirst().get();
    System.out.printf("Part 2 score: %d %n", middleScore);
  }
}
