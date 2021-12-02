import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Day1
{
  public static void main(String[] args) {
    long lastVal = -1;
    long incCount = 0;

    // Part 1
    List<String> lines = Util.readFile(Path.of("resources/day1.dat"));
    for (String line : lines) {
      long val = Long.parseLong(line);
      if (lastVal >= 0 && val > lastVal) {
        incCount++;
      }
      lastVal = val;
    }
    System.out.println(incCount);

    // Part 2
    lastVal = -1;
    incCount = 0;
    Deque<Long> window = new ArrayDeque<>();
    long windowAccumulator = 0;
    for (String line : lines) {
      long val = Long.parseLong(line);
      window.addFirst(val);
      windowAccumulator += val;
      if (window.size() == 4) {
        windowAccumulator -= window.removeLast();
      }
      if (window.size() == 3) {
        if (lastVal >= 0 && windowAccumulator > lastVal) {
          incCount++;
        }
        lastVal = windowAccumulator;
      }
    }
    System.out.println(incCount);
  }
}
