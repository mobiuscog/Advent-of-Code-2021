import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Day6
{
  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day6.dat"));

    var fishAges = Arrays.stream(lines.get(0).trim().split(",")).mapToInt(Integer::parseInt).toArray();

    var fishes = new ArrayList<Fish>();
    for (Integer age : fishAges) {
      fishes.add(new Fish(age));
    }

    // Part 1
    for (int day = 0; day < 80; day++) {
      var newFish = new ArrayList<Fish>();
      for (Fish fish : fishes) {
        fish.grow().ifPresent(newFish::add);
      }
      fishes.addAll(newFish);
      System.out.println(newFish.size());
      newFish.clear();
    }

    System.out.printf("After 80 days there are %d fish%n", fishes.size());

    // Part 2 -- the memory killer (if this works, you have more RAM than I do)
    for (int day = 80; day < 256; day++) {
      var newFish = new ArrayList<Fish>();
      for (Fish fish : fishes) {
        fish.grow().ifPresent(newFish::add);
      }
      fishes.addAll(newFish);
      newFish.clear();
    }

    System.out.printf("After 256 days there are %d fish%n", fishes.size());

  }

  static class Fish
  {
    final int discoveryAge;

    int age;

    Fish(int age) {
      this.discoveryAge = age;
      this.age = age;
    }

    Optional<Fish> grow() {
      age--;
      if (age < 0) {
        age = 6;
        return Optional.of(new Fish(8));
      }
      return Optional.empty();
    }
  }
}
