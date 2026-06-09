// DFS_Template.java

import java.util.*;

public class DFS_Template {
    public void dfs(int node, Map<Integer, List<Integer>> graph, Set<Integer> seen) {
        if (seen.contains(node)) return;
        seen.add(node);
        // process node
        for (int nei : graph.getOrDefault(node, Collections.emptyList())) dfs(nei, graph, seen);
    }
}
