import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13
{
  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day13.dat"));

    var dots = parseDots(lines);
    var folds =
        lines.stream().filter(l -> l.startsWith("fold along")).map(l -> l.split(" ")[2]).map(Day13::parseFold).toList();

    // Part 1
    dots = performFold(dots, folds.get(0));
    System.out.printf("Part 1 dots: %d %n", dots.size());

    // Part 2
    for (int fold = 1; fold < folds.size(); fold++) {
      dots = performFold(dots, folds.get(fold));
    }

    int maxX = 0;
    int maxY = 0;
    for (Dot dot : dots) {
      maxX = Math.max(maxX, dot.x());
      maxY = Math.max(maxY, dot.y());
    }
    var output = new char[maxY + 1][maxX + 1];

    for (Dot dot : dots) {
      output[dot.y()][dot.x()] = '#';
    }

    System.out.printf("Part 2: %n");
    for (int y = 0; y <= maxY; y++) {
      for (int x = 0; x <= maxX; x++) {
        System.out.print(output[y][x]);
      }
      System.out.println();
    }
  }

  static Set<Dot> parseDots(List<String> lines) {
    return lines.stream().takeWhile(l -> !l.isBlank()).map(Day13::parseDot).collect(Collectors.toSet());
  }

  static Dot parseDot(String input) {
    var coords = input.split(",");
    return new Dot(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
  }

  static Fold parseFold(String input) {
    var instructions = input.split("=");
    return new Fold(Objects.equals(instructions[0], "y"), Integer.parseInt(instructions[1]));
  }

  static Set<Dot> performFold(Set<Dot> dots, Fold fold) {
    var foldedDots = new HashSet<Dot>();
    dots.forEach(dot -> foldDot(dot, fold).ifPresent(foldedDots::add));
    return foldedDots;
  }

  static Optional<Dot> foldDot(Dot dot, Fold fold) {
    var isVertical = fold.isVertical();
    var dotValue = isVertical ? dot.y() : dot.x();
    if (dotValue > fold.value()) {
      return Optional.of(
          new Dot(
              isVertical ? dot.x() : fold.value() - (dot.x() - fold.value()),
              isVertical ? fold.value() - (dot.y() - fold.value()) : dot.y()
          ));
    }
    else if (dotValue == fold.value()) {
      return Optional.empty();
    }
    else {
      return Optional.of(dot);
    }
  }

  record Dot(int x, int y) { }

  record Fold(boolean isVertical, int value) { }
}
