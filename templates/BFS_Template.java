// BFS_Template.java

import java.util.*;

public class BFS_Template {
    // Example BFS traversal for a tree/graph
    public void bfs(int start, Map<Integer, List<Integer>> graph) {
        Queue<Integer> q = new LinkedList<>();
        Set<Integer> seen = new HashSet<>();
        q.add(start); seen.add(start);
        while (!q.isEmpty()) {
            int cur = q.poll();
            // process cur
            for (int nei : graph.getOrDefault(cur, Collections.emptyList())) {
                if (!seen.contains(nei)) { seen.add(nei); q.add(nei); }
            }
        }
    }
}
