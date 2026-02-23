package feb.week3.codetree;

import java.util.*;
import java.io.*;

public class 코드트리DB_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(init());
        br.close();
    }
    static final String INIT = "init";
    static final String DELETE = "delete";
    static final String INSERT = "insert";
    static final String RANK = "rank";
    static final String SUM = "sum";
    static class Command {
        String cmd;
        String name;
        int value;
        int k;

        // INSERT
        Command(String cmd, String name, int value) {
            this.cmd = cmd;
            this.name = name;
            this.value = value;
        }
        // DELETE
        Command(String cmd, String name) {
            this.cmd = cmd;
            this.name = name;
        }
        // RANK, SUM
        Command(String cmd, int k) {
            this.cmd = cmd;
            this.k = k;
        }
    }
    static StringTokenizer st;
    static StringBuilder sb;
    static Queue<Command> cmdQ;
    static List<Integer> values;
    static String init() throws IOException {
        sb = new StringBuilder();
        cmdQ = new ArrayDeque<>();
        values = new ArrayList<>();

        int queryTime = Integer.parseInt(br.readLine().trim());
        while(queryTime-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            String cmd = st.nextToken();
            if(cmd.equals(INIT)) {
                if(!cmdQ.isEmpty()) {       // 수행할 명령어가 쌓였다면 실행
                    processCommand();
                }
                cmdQ.clear();
                values.clear();
                continue;
            }
            Command command = null;
            if(cmd.equals(INSERT)){
                String name = st.nextToken();
                int value = Integer.parseInt(st.nextToken());
                values.add(value);
                command = new Command(INSERT, name, value);
            }
            else if(cmd.equals(DELETE)){
                String name = st.nextToken();
                command = new Command(DELETE, name);
            }
            else if(cmd.equals(RANK)){
                int k = Integer.parseInt(st.nextToken());
                command = new Command(RANK, k);
            }
            else if(cmd.equals(SUM)){
                int k = Integer.parseInt(st.nextToken());
                command = new Command(SUM, k);
            }
            cmdQ.offer(command);
        }
        if (!cmdQ.isEmpty()) {
            processCommand();
        }
        return sb.toString();
    }

    static Map<String, Integer> nameToValue;
    static Map<Integer, String> valueToName;
    static long[] sumTree;      // 누적 합을 기록
    static int[] rankTree;      // 순위를 기록
    static void processCommand() {
        compress();
        if (size == 0) {        // 처리할 INSERT 가 없을 때
            while (!cmdQ.isEmpty()) {
                Command cur = cmdQ.poll();

                if (cur.cmd.equals(RANK)) sb.append("None");
                else if (cur.cmd.equals(SUM)) sb.append(0);
                else if (cur.cmd.equals(INSERT)) sb.append(1);
                else if (cur.cmd.equals(DELETE)) sb.append(0);
                sb.append('\n');
            }
            return;
        }
        nameToValue = new HashMap<>();
        valueToName = new HashMap<>();
        sumTree = new long[4*size];
        rankTree = new int[4*size];

        while(!cmdQ.isEmpty()) {
            Command cur = cmdQ.poll();

            // UPDATE
            if(cur.cmd.equals(INSERT)) {
                String name = cur.name;
                int value = cur.value;
                /**
                 * name 과 value 는 각 PK
                 */
                if(nameToValue.get(name) != null) sb.append(0);
                else if(valueToName.get(value) != null) sb.append(0);
                else {      // PK 조건
                    int id = valueToId.get(value);

                    updateSum(1, 1, size, id, id, value);
                    updateRank(1, 1, size, id, id, 1);

                    nameToValue.put(name, value);
                    valueToName.put(value, name);
                    sb.append(1);
                }
            }
            else if(cur.cmd.equals(DELETE)) {
                String name = cur.name;
                if(nameToValue.get(name) == null) sb .append(0);
                else {
                    int value = nameToValue.get(name);
                    int id = valueToId.get(value);

                    updateSum(1, 1, size, id, id, 0);
                    updateRank(1, 1, size, id, id, 0);
                    sb.append(value);
                    nameToValue.remove(name);
                    valueToName.remove(value);
                }
            }
            // QUERY
            else if(cur.cmd.equals(RANK)) {
                int k = cur.k;
                if(rankTree[1] < k) sb.append("None");
                else {
                    int id = rankQuery(1, 1, size, k);
                    int value = idToValue[id];
                    sb.append(valueToName.get(value));
                }
            }
            else if(cur.cmd.equals(SUM)) {
                int k = cur.k;
                /**
                 * k 에 대응하는 원소가 없을 수 있음
                 */
                int id = upperBoundId(k);
                if(id <= 0) sb.append(0);
                else {
                    sb.append(sumQuery(1, 1, size, 1, id));
                }
            }
            sb.append('\n');
        }
    }
    static int size;
    static Map<Integer, Integer> valueToId;
    static int[] idToValue;
    static void compress() {
        Set<Integer> set = new HashSet<>(values);     // 중복 제거 (PK)
        List<Integer> sorted = new ArrayList<>(set);
        sorted.sort(Integer::compare);

        size = set.size();
        valueToId = new HashMap<>();
        idToValue = new int[size+1];        // 1-based

        for(int i=0; i<size; i++) {
            int value = sorted.get(i);
            valueToId.put(value, i+1);
            idToValue[i+1] = value;
        }
    }

    static void updateSum(int id, int l, int r, int s, int e, int v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            sumTree[id] = v;
            return;
        }
        int mid = l + (r-l)/2;
        updateSum(2*id, l, mid, s, e, v);
        updateSum(2*id+1, mid+1, r, s, e, v);
        sumTree[id] = sumTree[2*id] + sumTree[2*id+1];
    }
    static void updateRank(int id, int l, int r, int s, int e, int v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            rankTree[id] = v;
            return;
        }
        int mid = l + (r-l)/2;
        updateRank(2*id, l, mid, s, e, v);
        updateRank(2*id+1, mid+1, r, s, e, v);
        rankTree[id] = rankTree[2*id] + rankTree[2*id+1];
    }
    static long sumQuery(int id, int l, int r, int s, int e) {
       if(r < s || l > e) return 0;
       if(l >= s && r <= e) return sumTree[id];

       int mid = l + (r-l)/2;
       return sumQuery(2*id, l, mid, s, e) + sumQuery(2*id+1, mid+1, r, s, e);
    }
    static int rankQuery(int id, int l, int r, int k) {
        if(l == r) return l;        // 리프 노드에 도달

        int lNodeCnt = rankTree[2*id];
        int mid = l + (r-l)/2;

        if(lNodeCnt >= k) {     // 왼쪽에 찾고자하는 원소가 존재
            return rankQuery(2*id, l, mid, k);
        } else {
            return rankQuery(2*id+1, mid+1, r, k-lNodeCnt);
        }
    }
    static int upperBoundId(int target) {
        int l = 1, r = size+1;        // 1-based
        while(l < r) {
            int mid = l + (r-l)/2;
            if(target < idToValue[mid]) r = mid;
            else l = mid+1;
        }
        return l-1;     // k보다 큰 첫 id니까 1 감소
    }
}
