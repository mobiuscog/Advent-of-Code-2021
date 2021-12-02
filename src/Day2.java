import java.nio.file.Path;
import java.util.List;

public class Day2
{
  public static void main(String[] args) {

    List<String> lines = Util.readFile(Path.of("resources/day2.dat"));

    // Part 1
    long forward = 0;
    long down = 0;
    for (String line: lines) {
      String[] data = line.split(" ");
      switch (data[0]) {
        case "forward" -> forward += Long.parseLong(data[1]);
        case "up" -> down -= Long.parseLong(data[1]);
        case "down" -> down += Long.parseLong(data[1]);
      }
    }

    System.out.printf("forward: %d down: %d result: %d%n", forward, down, forward * down);

    // Part 2
    forward = 0;
    down = 0;
    long aim = 0;
    for (String line: lines) {
      String[] data = line.split(" ");
      long value = Long.parseLong(data[1]);
      switch (data[0]) {
        case "forward" -> { forward += value; down += aim * value; }
        case "up" -> { aim -= value; }
        case "down" -> { aim += value; }
      }
    }

    System.out.printf("forward: %d down: %d result: %d%n", forward, down, forward * down);

  }
}
