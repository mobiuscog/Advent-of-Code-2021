import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day11
{
  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day11.dat"));

    var octopusLookup = new HashMap<Location, Octopus>();
    for (int y = 0; y < 10; y++) {
      var line = lines.get(y);
      for (int x = 0; x < 10; x++) {
        var energyLevel = Integer.parseInt(String.valueOf(line.charAt(x)));
        Location location = new Location(x, y);
        Octopus octopus = new Octopus(location, energyLevel);
        octopusLookup.put(location, octopus);
        if (x > 0) {
          Octopus newFriend = octopusLookup.get(new Location(x - 1, y));
          octopus.addFriend(newFriend);
        }
        if (y > 0) {
          Octopus newFriend = octopusLookup.get(new Location(x, y - 1));
          octopus.addFriend(newFriend);
          if (x > 0) {
            newFriend = octopusLookup.get(new Location(x - 1, y - 1));
            octopus.addFriend(newFriend);
          }
          if (x < 9) {
            newFriend = octopusLookup.get(new Location(x + 1, y - 1));
            octopus.addFriend(newFriend);
          }
        }
        if (x > 0 && y > 0) {
          Octopus newFriend = octopusLookup.get(new Location(x - 1, y - 1));
          octopus.addFriend(newFriend);
        }
      }
    }

    for (var step = 1; step <= 100; step++) {
      performStep(octopusLookup);
    }

    System.out.printf("%s %n", octopusLookup.values().stream().mapToInt(Octopus::getFlashCount).sum());

    octopusLookup.values().forEach(Octopus::resetEnergy);

    int step = 0;
    do {
      step++;
      performStep(octopusLookup);
    }
    while (octopusLookup.values().stream().mapToInt(Octopus::getEnergyLevel).sum() != 0);

    System.out.printf("Synchronized flash at step %d %n", step);
  }

  private static void performStep(Map<Location, Octopus> octopusMap) {
    octopusMap.values().forEach(Octopus::increaseEnergy);
    octopusMap.values().forEach(Octopus::flash);
    octopusMap.values().forEach(Octopus::resetFlash);
  }

  record Location(int x, int y) { }

  private static class Octopus
  {
    final Location location;

    final Set<Octopus> friends = new HashSet<>();

    int startEnergyLevel;

    int energyLevel;

    boolean hasFlashed;

    int flashCount;

    Octopus(Location location, int energyLevel) {
      this.location = location;
      this.startEnergyLevel = energyLevel;
      this.energyLevel = energyLevel;
      hasFlashed = false;
      flashCount = 0;
    }

    void addFriend(Octopus friend) {
      if (this.friends.add(friend)) {
        friend.addFriend(this);
      }
    }

    public int getEnergyLevel() {
      return energyLevel;
    }

    void increaseEnergy() {
      if (!hasFlashed) {
        energyLevel++;
      }
    }

    void flash() {
      if (energyLevel > 9 && !hasFlashed) {
        hasFlashed = true;
        energyLevel = 0;
        flashCount++;
        for (Octopus friend : friends) {
          friend.increaseEnergy();
          if (friend.energyLevel > 9) {
            friend.flash();
          }
        }
      }
    }

    int getFlashCount() {
      return flashCount;
    }

    void resetFlash() {
      hasFlashed = false;
    }

    void resetEnergy() {
      this.energyLevel = this.startEnergyLevel;
    }
  }
}
