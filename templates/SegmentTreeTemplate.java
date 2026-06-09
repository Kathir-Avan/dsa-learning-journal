// SegmentTreeTemplate.java

public class SegmentTreeTemplate {
    int[] tree; int n;
    public SegmentTreeTemplate(int[] arr) {
        n = arr.length; tree = new int[4 * n];
        build(arr, 1, 0, n - 1);
    }
    private void build(int[] arr, int node, int l, int r) {
        if (l == r) { tree[node] = arr[l]; return; }
        int m = (l + r) / 2;
        build(arr, node*2, l, m);
        build(arr, node*2+1, m+1, r);
        tree[node] = tree[node*2] + tree[node*2+1];
    }
    // add query/update helpers as needed
}
