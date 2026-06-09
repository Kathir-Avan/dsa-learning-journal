// BinarySearchTemplate.java

public class BinarySearchTemplate {
    // Returns index of target or -1 if not found
    public int binarySearch(int[] arr, int target) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) l = mid + 1;
            else r = mid - 1;
        }
        return -1;
    }
}
