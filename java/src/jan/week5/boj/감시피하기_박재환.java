package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 감시피하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * N x N 격자
     * 특정한 위치에는 선생님, 학생, 장애물이 위치할 수 있다.
     *
     * 선생님의 감시에 들키지 않는 것이 목표이다.
     * 선생님들은 (상 하 좌 우) 4가지 방향으로 감시한다.
     * 장애물 뒷 편의 학생은 볼 수 없다.
     * 
     * 3 개의 장애물을 설치할 수 있다.
     * 모든 학생들이 감시를 피할 수 있는지 계산해라
     */
    static List<int[]> teachers;
    static boolean isOk;
    static int n;
    static char[][] map;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        teachers = new ArrayList<>();
        isOk = false;
        n = Integer.parseInt(br.readLine().trim());
        map = new char[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) {
                map[x][y] = st.nextToken().charAt(0);
                if(map[x][y] == 'T') {
                    teachers.add(new int[] {x, y});
                }
            }
        }
        installObstacle(0, 0);
        System.out.println(isOk ? "YES" : "NO");
    }

    static void installObstacle(int idx, int installed) {
        if(isOk) return;
        if(installed == 3) {        // 모두 설치 완료
            if(!findStudents()) { // 학생을 발견하지 못했다면
                isOk = true;
            }
            return;
        }

        for (int i = idx; i < n * n; i++) {
            int x = i / n;
            int y = i % n;

            if (map[x][y] != 'X') continue;

            map[x][y] = 'O';
            installObstacle(i + 1, installed + 1);
            map[x][y] = 'X';
        }
    }

    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static boolean findStudents() {
        for(int[] teacher : teachers) {
            for(int dir=0; dir<4; dir++) {
                int tx = teacher[0] + dx[dir];
                int ty = teacher[1] + dy[dir];
                while(!isNotBoard(tx, ty)) {
                    if(map[tx][ty] == 'O') break;
                    if(map[tx][ty] == 'S') return true;
                    tx += dx[dir];
                    ty += dy[dir];
                }
            }
        }
        return false;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
