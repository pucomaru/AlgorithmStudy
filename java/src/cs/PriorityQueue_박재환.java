package cs;

import java.util.*;

public class PriorityQueue_박재환 {
    static int[] heap;
    static int size;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        init();

        System.out.println("offer : 1, poll : 2, exit : enter");
        String input;
        while((input = sc.nextLine()) != null && !input.isEmpty()) {
            if(input.equals("1")) {         // offer
                System.out.print("num : ");
                int num = sc.nextInt();
                offer(num);
                System.out.println("offer : " + Arrays.toString(heap));
            } else if(input.equals("2")) {  // poll
                int num = poll();
                System.out.println("poll : " + num);
            }
        }

        sc.close();
    }
    //-------------------------------------------------------
    static void init() {
        heap = new int[16];
        size = 0;
    }
    //-------------------------------------------------------
    static void offer(int num) {
        ensureCapacity();       // 공간 확보, 자동 Resize
        heap[size] = num;
        shiftUp(size);
        size++;
    }
    static void shiftUp(int id) {
        while(id > 0) {
            int pId = (id-1)/2;
            // SWAP 조건 확인 (MIN HEAP)
            if(heap[pId] <= heap[id]) break;
            swap(pId, id);
            id = pId;
        }
    }
    //-------------------------------------------------------
    static int poll() {
        if(size == 0) throw new IllegalArgumentException("Empty PQ");
        
        int pollNum = heap[0];
        heap[0] = heap[size-1];         // 마지막 원소를 루트로 올려줌
        size--;
        shiftDown(0);
        return pollNum;
    }
    static void shiftDown(int id) {
        while(true) {
            int leftId = 2*id;
            int rightId = 2*id+1;
            int smallest = id;

            if(leftId < size && heap[leftId] < heap[smallest]) {
                smallest = leftId;
            }
            if(rightId < size && heap[rightId] < heap[smallest]) {
                smallest = rightId;
            }
            if(smallest == id) break;       // SWAP 하지 않아도 됨
            swap(smallest, id);
            id = smallest;
        }
    }
    //-------------------------------------------------------
    static int peek() {
        if(size == 0) throw new IllegalArgumentException("Empty PQ");

        int peekNum = heap[0];
        return peekNum;
    }
    //-------------------------------------------------------
    static void swap(int id1, int id2) {
        int tmp = heap[id1];
        heap[id1] = heap[id2];
        heap[id2] = tmp;
    }
    static void ensureCapacity() {
        if(size == heap.length) {
            int[] newHeap = new int[heap.length*2];
            for(int i=0; i<heap.length; i++) newHeap[i] = heap[i];
            heap = newHeap;
        }
    }
}
