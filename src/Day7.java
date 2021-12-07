import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Day7
{
  public static void main(String[] args) {
    var positions = Util.readFile(Path.of("resources/day7.dat"),
        s -> s.map(l -> l.trim().split(",")).flatMap(Arrays::stream).map(Integer::parseInt).sorted());
    assert positions != null;

    // Part 1
    // Calculate median as 'most common' centre
    var count = positions.size();
    var centrePosition = (count / 2) - 1;
    var median = positions.get(centrePosition);
    if (count % 2 == 0) {
      median = (median + positions.get(centrePosition + 1)) / 2;
    }

    // Calculate fuel
    Integer finalMedian = median;
    var fuel = positions.stream().map(p -> p - finalMedian).map(Math::abs).reduce(0, Integer::sum);
    System.out.printf("Part 1 Fuel consumed: %d %n", fuel);

    // Part 2
    // Calculate average - shortest overall distance to travel - not sure why difference in high/low - rounding error ?
    var average = positions.stream().mapToInt(Integer::intValue).average().getAsDouble();
    var low = (int) Math.floor(average);
    var high = low + 1;
    var lowFuel = positions.stream()
        .map(p -> p - low)
        .map(Math::abs).map(d -> IntStream.range(1, d + 1).sum())
        .reduce(0, Integer::sum);
    var highFuel = positions.stream()
        .map(p -> p - high)
        .map(Math::abs).map(d -> IntStream.range(1, d + 1).sum())
        .reduce(0, Integer::sum);
    System.out.printf("Part 2 Fuel consumed: %d %n", Math.min(highFuel, lowFuel));
  }
}
