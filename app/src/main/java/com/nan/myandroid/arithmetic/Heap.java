package com.nan.myandroid.arithmetic;

/**
 * author：93289
 * date:2020/10/22
 * dsc:
 */
class Heap {

    /**
     * 升序堆排序
     * 1将所有元素构建大顶堆，堆顶的元素数组下标为0为最大值
     * 2将下标为0的元素和数组最后一个元素置换，最大的元素在数组尾部
     * 3将除了队尾的已排序元素以外剩下的元素再次构建大顶堆，重复1 2直到未排序元素为1（因为就剩一个元素，默认有序）
     */
    public static void heapSort() {
        for (int i = a.length - 1; i > 0; i--) {
            //遍历范围为最后一个元素的下标到第二个元素的下标，因为第一个元素最后默认有序
            max_heapify(a, i);
            //每构建一次大顶堆就将堆顶最大元素移到已排序元素的开始
            swap(a, 0, i);
        }

    }

    public static void max_heapify(int[] a, int n) {
        int child;
        //如果n是未排序元素中的长度，未排序元素中的最后一个非叶子结点的坐标为(n - 2) / 2
        //这里n等于未排序元素中的最大坐标
        //未排序元素中的最后一个非叶子结点的坐标为(n - 1) / 2
        for (int i = (n - 1) / 2; i >= 0; i--) {
            child = 2 * i + 1;
            //这里左子结点下标child的取值范围为<n
            //因为n是未排序元素的最大坐标，child肯定不能大于n，因为child最多等于允许的最大坐标n，child<=n
            //但是这里需要左节点和右节点比较，涉及到a[child + 1]，为保证child+1也在<=n范围内,child<=n-1即child<n
            if (child < n && a[child] < a[child + 1]) {
                child++;
            }
            if (a[i] < a[child]) {
                swap(a, i, child);
            }
        }
    }

    public static void buildHeapMax() {
        //开始位置时最后一个非叶子节点，即最后一个节点的父节点
        int start = (a.length - 2) / 2;
        //调整为大顶堆
        for (int i = start; i >= 0; i--) {
            maxHeap(a, a.length, i);
        }
    }

    /**
     * 根据大小关系，调整一个父节点与其子结点的位置
     *
     * @param arr   原数组
     * @param size  原数组长度
     * @param index 需要调整位置的父节点位于数组中的下标
     */
    public static void maxHeap(int[] arr, int size, int index) {
        //左子节点
        int leftNode = 2 * index + 1;
        //右子节点
        int rightNode = 2 * index + 2;
        int max = index;
        //和两个子节点对比，找出最大的一个节点
        if (leftNode < size && arr[leftNode] > arr[max]) {
            max = leftNode;
        }
        if (rightNode < size && arr[rightNode] > arr[max]) {
            max = rightNode;
        }

        //如果父节点不是最大的，交换位置
        if (max != index) {
            swap(a, max, index);
            //由于是从最后一个非叶子结点往前遍历
            //所以交换位置之后，之前原本已经堆化的子结点替换成了新值
            //有可能打破之前的堆化，所以这个交换过的子结点为父节点往下遍历，重新堆化一次
            maxHeap(arr, size, max);
        }
    }


    public static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }


    //打印节点
    public static void printS(int[] arr) {
        System.out.println();
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(i + "=" + arr[i]);
            if (i < arr.length - 1) {
                System.out.print(",");
            }
        }
        System.out.print("]");
        System.out.println();
    }

    public static void printHeap(int[] arr) {
        System.out.println();
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(i + "=" + arr[i]);
            if (i < arr.length - 1) {
                System.out.print(",");
            }
        }
        System.out.print("]");
        System.out.println();
    }


    static int[] a = {21, 12, 56, 1, 4,5,6,7,83,13,547,1223,122,2,23,35,567,8,1,5,312,35,63};

    //演示测试　
    public static void main(String[] args) {
        heapSort();

//        buildHeapMax();
//       [0=44,1=33,2=35,3=23,4=23,5=26,6=34,7=21,8=10,9=18,10=3,11=5,12=7,13=1,14=23]

        printS(a);

    }
}
