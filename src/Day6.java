import java.nio.file.Path;
import java.util.Arrays;

public class Day6
{
  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day6.dat"));

    var fishAges = Arrays.stream(lines.get(0).trim().split(",")).mapToInt(Integer::parseInt).toArray();
    var fish = new long[9];
    for (Integer age : fishAges) {
      fish[age]++;
    }

    // Part 1
    for (int day = 0; day < 80; day++) {
      growFish(fish);
    }

    var fishCount = Arrays.stream(fish).sum();
    System.out.printf("After 80 days there are %d fish%n", fishCount);

    // Part 2
    for (int day = 80; day < 256; day++) {
      growFish(fish);
    }

    fishCount = Arrays.stream(fish).sum();
    System.out.printf("After 256 days there are %d fish%n", fishCount);
  }

  static void growFish(long[] fish) {
    var currentFish = fish.clone();
    System.arraycopy(currentFish, 1, fish, 0, currentFish.length - 1);
    fish[6] += currentFish[0];
    fish[8] = currentFish[0];
  }
}
