import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseRobotAI {
	public static void main(String[] args) {
		int height, width;
		height = width = 10;
		Node start = new Node(0, 0, 2);
		Node end = new Node(9, 9, 3);
		List<Node> obstacles = new ArrayList<Node>();
		obstacles.add(new Node(9, 7, 0));
		obstacles.add(new Node(8, 7, 0));
		obstacles.add(new Node(6, 7, 0));
		obstacles.add(new Node(6, 8, 0));

		Grid grid = constructGrid(height, width, start, end, obstacles);
		List<Node> path = grid.shortestPath();
		grid.printPath(path);
	}

	public static Grid constructGrid(int height, int width, Node start, Node end, List<Node> obstacles) {
		// 0: path, 1: obstacle, 2: start, 3: Destination
		int[][] nodes = new int[height][width];
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				nodes[row][column] = 1;
			}
		}
		// start and end node position
		nodes[start.getX()][start.getY()] = start.getState();
		nodes[end.getX()][end.getY()] = end.getState();

		for (Node node : obstacles) {
			nodes[node.getX()][node.getY()] = node.getState();
		}

		printGrid(nodes);

		return new Grid(nodes, start, end);
	}

	public static Grid constructGrid(Node start, Node end, List<Node> obstacles) {
		return constructGrid(10, 10, start, end, obstacles);
	}

	private static void printGrid(int[][] nodes) {
		for (int row = 0; row < nodes.length; row++) {
			String rowString = "";
			for (int column = 0; column < nodes[row].length; column++) {
				rowString += nodes[row][column] + " - ";
			}
			System.out.println(rowString);
		}
	}
}

class Grid {
	private int[][] nodes;
	Node start;
	Node end;

	public Grid(int[][] nodes, Node start, Node end) {
		this.nodes = nodes;
		this.start = start;
		this.end = end;
	}

	public List<Node> shortestPath() {
		Map<Node, Node> parents = new HashMap<Node, Node>();
		// Use Breatdth first search to find path to end node
		List<Node> temp = new ArrayList<Node>();
		temp.add(start);
		parents.put(start, null);

		boolean reachDestination = false;
		while (temp.size() > 0 && !reachDestination) {
			Node currentNode = temp.remove(0);
			List<Node> children = getChildren(currentNode);
			for (Node child : children) {
				// Only visit node once
				if (!parents.containsKey(child)) {
					parents.put(child, currentNode);
					int state = child.getState();
					if (state == 1) {
						temp.add(child);
					} else if (state == 3) {
						temp.add(child);
						reachDestination = true;
						break;
					}
				}
			}
		}
		
		System.out.println("Temp path:");
		printPath(temp);

		// Find shortest path
		Node node = end;
		List<Node> path = new ArrayList<Node>();
		while (node != null) {
			path.add(0, node);
			node = parents.get(node);
		}
		printPath(path);
		return path;
	}

	private List<Node> getChildren(Node parent) {
		List<Node> children = new ArrayList<Node>();
		int x = parent.getX();
		int y = parent.getY();
		if (x - 1 >= 0) {
      		Node child = new Node(x - 1, y, nodes[x - 1][y]);
      		children.add(child);
    	}
	    if (y - 1 >= 0) {
	    	Node child = new Node(x, y - 1, nodes[x][y - 1]);
	    	children.add(child);
	    }
	    if (x + 1 < nodes.length) {
	    	Node child = new Node(x + 1, y, nodes[x + 1][y]);
	    	children.add(child);
	    }
	    if (y + 1 < nodes[0].length) {
	    	Node child = new Node(x, y + 1, nodes[x][y + 1]);
	    	children.add(child);
	    }
	    return children;
	}

	public void printPath(List<Node> path) {
		String resultPath = "Path: [";
		int stepCount = 0;
		for (Node node : path) {
			resultPath += "(" + node.getX() + "," + node.getY() + ")";
			stepCount++;
		}
		resultPath += "], in " + stepCount + " steps.";
		System.out.println(resultPath);
	}
}

class Node {
	private int x;
	private int y;
	private int state;

	public Node(int x, int y, int state) {
		this.x = x;
		this.y = y;
		this.state = state;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getState() {
		return state;
	}
}