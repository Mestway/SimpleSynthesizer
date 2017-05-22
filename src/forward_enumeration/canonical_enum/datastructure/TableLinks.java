package forward_enumeration.canonical_enum.datastructure;

import lang.table.Table;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The data structure recording the connection between tables in the value (table) net
 * Created by clwang on 4/6/16.
 */
public class TableLinks {

    public static final int CAPACITY = 20;

    // storing the edges backwardly, the key represents the destination
    // while the value set contains source nodes that can lead to the table.
    // The source nodes of the edges
    Map<Table, Set<Set<Table>>> edges = new HashMap<>();


    // insert an edge with one source node
    public void insertEdge(Table src, Table dst) {
        Set<Table> node = new HashSet<>();
        node.add(src);
        insertEdge(node, dst);
    }

    // insert an edge with two leading source nodes
    public void insertEdge(Table src1, Table src2, Table dst) {
        Set<Table> nodes = new HashSet<>();
        nodes.add(src1);
        nodes.add(src2);
        insertEdge(nodes, dst);
    }

    // the internal insertion method: insert a set of edges linked to dst
    private void insertEdge(Set<Table> src, Table dst) {

        if (dst == null)
            System.out.println("Oh no");

        if (edges.containsKey(dst)) {
            // insert only if capacity
            if (edges.get(dst).size() < CAPACITY)
                edges.get(dst).add(src);
        } else {
            edges.put(dst, new HashSet<>());
            edges.get(dst).add(src);
        }
    }

    public Map<Table, Set<Set<Table>>> edges() { return this.edges; }

    public List<TableTreeNode> findTableTrees(Table root, Set<Table> leafNodes, int depth) {
        List<TableTreeNode> result = new ArrayList<>();

        // if the target we want to construct is a leaf node, directly return it.
        if (leafNodes.contains(root)) {
            result.add(new TableTreeNode(root, new ArrayList<>()));
            return result;
        }

        // if we have to terminate now while the current node is not yet a leaf node, we abort the search
        if (depth == 0) { return new ArrayList<>(); }

        if (! (edges.containsKey(root)))
            return new ArrayList<>();

        for (Set<Table> src : edges.get(root)) {
            // each list in the list contains all candidate for the nodes in src
            List<List<TableTreeNode>> subTreeCandidates = horizontalDFS(
                    src.stream().collect(Collectors.toList()), leafNodes, depth -1);
            for (List<TableTreeNode> ttL : subTreeCandidates) {
                result.add(new TableTreeNode(root, ttL));
            }
        }

        return result;
    }

    // find a list such that each List in the list a a way to generate all tabletreenodes for the given tables.
    public List<List<TableTreeNode>> horizontalDFS(List<Table> tables, Set<Table> leafNodes, int depth) {
        List<List<TableTreeNode>> result = new ArrayList<>();

        // if the table list to expand is empty, we feed an empty list to keep the search going on
        if (tables.size() == 0) {
            result.add(new ArrayList<>());
            return result;
        }

        // candidates generated by the tails
        List<Table> subTables = tables.subList(1, tables.size());
        List<List<TableTreeNode>> prevCandidates = horizontalDFS(subTables, leafNodes, depth);

        List<TableTreeNode> nodes = findTableTrees(tables.get(0), leafNodes, depth);
        for (TableTreeNode n : nodes) {
            for (List<TableTreeNode> prevCand : prevCandidates) {
                List<TableTreeNode> newCand = new ArrayList<>();
                // append to head
                newCand.add(n);
                newCand.addAll(prevCand);
                result.add(newCand);
            }
        }
        return result;
    }

}
