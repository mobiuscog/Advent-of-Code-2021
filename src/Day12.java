import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Day12
{
  private static final String START = "start";
  private static final String END = "end";

  public static void main(String[] args) {
    var lines = Util.readFile(Path.of("resources/day12.dat"));

    // Build nodes
    var nodes = new HashMap<String, Node>();
    for (var line : lines) {
      var names = line.trim().split("-");
      Node left = nodes.computeIfAbsent(names[0], Node::new);
      Node right = nodes.computeIfAbsent(names[1], Node::new);
      left.addConnection(right);
      right.addConnection(left);
    }

    // Part 1
    var startPath = new NodePath(nodes.get(START), false);
    var allPaths =
        startPath.follow().stream().filter(p -> p.pathSteps.contains(nodes.get(END))).collect(Collectors.toSet());
    System.out.printf("Part 1 Paths: %d%n", allPaths.size());

    // Part 2
    startPath = new NodePath(nodes.get(START), true);
    allPaths =
        startPath.follow().stream().filter(p -> p.pathSteps.contains(nodes.get(END))).collect(Collectors.toSet());
    System.out.printf("Part 2 Paths: %d%n", allPaths.size());
  }

  static class NodePath
  {
    private final Deque<Node> pathSteps;

    private final Set<Node> seenNodes = new HashSet<>();

    private final Set<NodePath> spawnedPaths = new HashSet<>();

    private final boolean allowTwice;

    NodePath(Node initialNode, boolean allowTwice) {
      this.pathSteps = new ArrayDeque<>();
      this.pathSteps.push(initialNode);
      this.allowTwice = allowTwice;
    }

    NodePath(Node nextNode, Deque<Node> existingPath, Set<Node> previouslySeen, boolean allowTwice) {
      this.pathSteps = new ArrayDeque<>(existingPath);
      this.pathSteps.push(nextNode);
      this.seenNodes.addAll(previouslySeen);
      this.spawnedPaths.add(this);
      this.allowTwice = allowTwice;
    }

    Set<NodePath> follow() {
      Node currentNode = this.pathSteps.peek();
      Set<Node> connections = currentNode.getConnections();
      if (!currentNode.isEnd()) {
        for (var node : connections) {
          if (node.isStart()) {
            continue;
          }
          var previouslySeen = new HashSet<>(seenNodes);
          if (node.isSmall()) {
            if (!this.seenNodes.contains(node)) {
              if (allowTwice) {
                var newPath = new NodePath(node, this.pathSteps, previouslySeen, false);
                spawnedPaths.addAll(newPath.follow());
                previouslySeen.add(node);
                newPath = new NodePath(node, this.pathSteps, previouslySeen, true);
                spawnedPaths.addAll(newPath.follow());
              }
              else {
                previouslySeen.add(node);
                var newPath = new NodePath(node, this.pathSteps, previouslySeen, false);
                spawnedPaths.addAll(newPath.follow());
              }
            }
          }
          else {
            var newPath = new NodePath(node, this.pathSteps, previouslySeen, allowTwice);
            spawnedPaths.addAll(newPath.follow());
          }
        }
      }
      return (spawnedPaths);
    }

    private String getHash() {
      return String.join(",", pathSteps.stream().map(Node::getName).toList());
    }

    @Override
    public boolean equals(final Object o) {
      return this == o || (o instanceof NodePath path) && getHash().equals(path.getHash());
    }

    @Override
    public int hashCode() {
      return getHash().hashCode();
    }
  }

  static class Node
  {
    private final String name;

    private final boolean isStart;

    private final boolean isEnd;

    private final boolean isSmall;

    private final Set<Node> connections = new HashSet<>();

    Node(String name) {
      this.name = name;
      this.isStart = this.name.equalsIgnoreCase(START);
      this.isEnd = this.name.equalsIgnoreCase(END);
      this.isSmall = this.name.toLowerCase().equals(this.name);
    }

    String getName() {
      return this.name;
    }

    boolean isSmall() {
      return this.isSmall;
    }

    void addConnection(Node node) {
      this.connections.add(node);
    }

    boolean isStart() {
      return this.isStart;
    }

    boolean isEnd() {
      return this.isEnd;
    }

    @Override
    public boolean equals(Object other) {
      return (other instanceof Node node) && node.name.equals(this.name);
    }

    public Set<Node> getConnections() {
      return Set.copyOf(connections);
    }

    @Override
    public int hashCode() {
      return this.name.hashCode();
    }
  }
}
