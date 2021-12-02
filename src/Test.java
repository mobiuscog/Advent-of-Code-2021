import java.nio.file.Paths;
import java.util.List;

public class Test
{
  public static void main(String[] args) {
    List<String> lines = Util.readFile(Paths.get("resources/day0.dat"));
    System.out.printf("Found %s lines\n", lines.size());
  }
}
