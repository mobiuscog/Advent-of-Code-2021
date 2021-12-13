import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Util
{
  static List<String> readFile(Path filepath) {
    return readFile(filepath, Function.identity());
  }

  static <R> List<R> readFile(Path filepath, Function<Stream<String>, Stream<R>> lineFunction) {
    try (BufferedReader br = Files.newBufferedReader(filepath)) {
      return lineFunction.apply(br.lines().map(String::trim)).toList();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }
}