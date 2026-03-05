package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 자동차공장_박재환_TLE {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Employee {
        int id;
        long salary;
        int masterId;
        Employee master;
        List<Employee> slave;
        long lazy;

        Employee(int id, int salary, int masterId) {
            this.id = id;
            this.salary = salary;
            this.masterId = masterId;

            this.master = null;
            this.slave = new ArrayList<>();
            this.lazy = 0;
        }
    }
    static final String UPDATE = "p";
    static final String QUERY = "u";
    static StringTokenizer st;
    static int n, m;
    static Map<Integer, Employee> employees;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        employees = new HashMap<>();
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int id = i+1;
            int salary = Integer.parseInt(st.nextToken());
            int masterId = i != 0 ? Integer.parseInt(st.nextToken()) : -1;
            Employee employee = new Employee(id, salary, masterId);
            employees.put(id, employee);
        }
        updateMaster();

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            String cmd = st.nextToken();
            if(cmd.equals(UPDATE)) {
                int empId = Integer.parseInt(st.nextToken());
                int salary = Integer.parseInt(st.nextToken());
                update(empId, salary);
            }
            else if(cmd.equals(QUERY)) {
                int empId = Integer.parseInt(st.nextToken());
                sb.append(query(empId)).append('\n');
            }
        }
        System.out.println(sb);
    }
    static void updateMaster() {
        for(Employee e : employees.values()) {
            int masterId = e.masterId;
            Employee master = employees.get(masterId);
            e.master = master;
            if(master != null) master.slave.add(e);
        }
    }
    static void update(int empId, int salary) {
        Employee employee = employees.get(empId);
        employee.lazy += salary;
    }
    static long query(int empId) {
        /**
         * 현재 empId 의 제일 상사까지 올라가서 갱신 O(N) => 최악 500,000 ** 2 -> TLE
         */
        Employee cur = employees.get(empId);
        Employee master = cur.master;
        long acc = 0;
        while(master != null) {
            acc += master.lazy;
            master = master.master;
        }
        return cur.salary + acc;
    }
}
