package jan.week5.cs;

import java.util.Scanner;

public class circularQueue_박재환 {
    /**
     * Queue
     * FIFO(First In First Out)자료구조입니다.
     * 시간 복잡도는 enque O(1), deque O(1) 입니다.
     *
     * 활용 예시로는 Cache 구현, Process 관리, BFS 등이 있습니다.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CircularQueue(sc);
        sc.close();
    }

    static void CircularQueue(Scanner sc) {
        System.out.print("Queue Size : ");
        int qSize = sc.nextInt();
        int[] q = new int[qSize];
        int front = 0, rear = 0;
        while(true) {
            System.out.println("enqueue : 1, dequeue : 0, exit : -1");
            System.out.print("enter command : ");
            int command = sc.nextInt();
            if(command == -1) break;

            switch(command) {
                case 1: {
                    System.out.print("enqueue value : ");
                    if ((rear + 1) % qSize == front) {
                        System.out.println("q is full");
                        return;
                    }
                    rear = (rear + 1) % qSize;
                    int num = sc.nextInt();
                    enqueue(q, rear, num);
                    break;
                } case 0: {
                    if (front == rear) {
                        System.out.println("q is empty");
                        return;
                    }
                    front = (front + 1) % qSize;
                    System.out.println(dequeue(q, front));
                    break;
                }
            }
        }
    }

    static void enqueue(int[] q, int insertIdx, int num) {
        q[insertIdx] = num;
    }

    static int dequeue(int[] q, int dequeIdx) {
        return q[dequeIdx];
    }
}
