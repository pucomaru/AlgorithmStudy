package feb.week3.boj;

import java.util.*;
import java.io.*;

public class 사탕상자_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    /**
     * 사탕의 맛 1 ~ 1,000,000 (1이 가장 맛있음)
     * 몇 번째로 맛있는 사탕을 꺼내주곤 한다.
     */
    static final int OUT = 1;
    static final int IN = 2;

    static class Command {
        int cmd;
        int b;
        int c;

        Command(int cmd, int b) {
            /**
             * 사탕을 빼는 경우
             */
            this.cmd = cmd;
            this.b = b;
        }
        Command(int cmd, int b, int c) {
            /**
             * 사탕을 넣는 경우
             */
            this.cmd = cmd;
            this.b = b;
            this.c = c;
        }
    }
    static StringTokenizer st;
    static Queue<Command> commands;
    static TreeSet<Integer> values;
    static void init() throws IOException {
        commands = new ArrayDeque<>();
        values = new TreeSet<>(Integer::compare);

        int n = Integer.parseInt(br.readLine().trim());         // 사탕 상자에 손을 댄 횟수
        while(n-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            Command command = null;
            if(cmd == IN) {
                int b = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                command = new Command(IN, b, c);
                values.add(b);           // c가 음수가 될 수도 있으니, 중복되지 않게
            } else if(cmd == OUT) {
                int b = Integer.parseInt(st.nextToken());
                command = new Command(OUT, b);
            }
            commands.offer(command);
        }

        processCommands();
    }
    static int[] tree;
    static int[] candies;
    static void processCommands() {
        compress();
        tree = new int[4*size];
        candies = new int[size+1];
        while(!commands.isEmpty()) {
            Command command = commands.poll();

            if(command.cmd == IN) {
                int candy = command.b;      // 넣고자 하는 사탕의 맛
                int cnt = command.c;       // 넣고자 하는 개수
                int id = valueToId.get(candy);
                int cur = candies[id];
                int next = cur + cnt;
                update(1, 1, size, id, id, next);
                candies[id] = next;
            } else if(command.cmd == OUT) {
                int targetRank = command.b;     // 찾고자하는 rank 값
                int id = query(1, 1, size, targetRank);     // 해당 rank의 candy id
                int cur = candies[id];
                int next = cur - 1;
                update(1, 1, size, id, id, next);
                candies[id] = next;
                sb.append(idToValue.get(id)).append('\n');
            }
        }
    }
    static int size;
    static Map<Integer, Integer> valueToId;
    static Map<Integer, Integer> idToValue;
    static void compress() {
        valueToId = new HashMap<>();
        idToValue = new HashMap<>();

        int id = 1;
        for(int i : values) {
            valueToId.put(i, id);
            idToValue.put(id, i);
            id++;
        }

        size = id-1;
    }
    static void update(int id, int l, int r, int s, int e, int v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            tree[id] = v;
            return;
        }
        int mid = l + (r - l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);
        tree[id] = tree[2*id] + tree[2*id+1];
    }
    static int query(int id, int l, int r, int targetRank) {
        if(l == r) return l;

        int mid = l + (r - l)/2;
        if(tree[2*id] >= targetRank) return query(2*id, l, mid, targetRank);
        else return query(2*id+1, mid+1, r, targetRank - tree[2*id]);
    }
}
