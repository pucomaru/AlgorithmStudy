package jan.week1.boj;

// 백준 21608 상어 초등학교
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;
public class 상어초등학교_이다예 {
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        // 교실의 크기 N * N / 학생의 수 N * N
        int N = Integer.parseInt(br.readLine());
        int classSize = N*N;
        int students = N*N;
        int[][] classroom = new int[N][N];

        // 동서남북 방향
        int[] dr = {1, -1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        // 각 학생마다 좋아하는 학생의 정보를 담은 2차원 배열 선언
        int[][] like = new int[students + 1][4];

        // 자리 선점 순서
        int[] sequence = new int[students];
        for(int i=0; i < students; i++){
            st = new StringTokenizer(br.readLine());
            int who = Integer.parseInt(st.nextToken());
            sequence[i] = who;
            like[who][0] = Integer.parseInt(st.nextToken());
            like[who][1] = Integer.parseInt(st.nextToken());
            like[who][2] = Integer.parseInt(st.nextToken());
            like[who][3] = Integer.parseInt(st.nextToken());
        }

        // 자리 선정 규칙
        // 1. 비어있는 칸 중 좋아하는 학생이 인접한 칸에 가장 많은 칸으로 자리 정함
        // 2. 1을 만족하는 칸 N개면, 인접한 칸 중에서 비어있는 칸이 가장 많은 칸으로 자리 정하기
        // 3. 2을 만족하는 칸 N개면, 행의 번호가 가장 작은칸.. 이러한 칸도 N개면 열의 번호가 가장 적은칸
        for (int i=0; i<students; i++){
            // 현재 차례 학생의 번호
            int now = sequence[i];

            int bestR = -1, bestC = -1;
            int bestLike = -1, bestEmpty = -1;

            for (int r=0; r < N;r++){
                for (int c=0; c<N;c++){

                    // 이 자리에 누가 앉아있으면 패스
                    if (classroom[r][c] != 0) continue;

                    // 인접 칸에 좋아하는 사람 카운트
                    int likeCount = 0;
                    int emptyCount = 0;

                    // 지금 보고있는 자리가 아무도 앉아있지 않을 경우에만
                    for ( int k=0; k < 4; k++){

                        int nr = r+dr[k];
                        int nc = c+dc[k];

                        // 배열 범위 넘어가면 패스
                        if (nr < 0 || nr >= N || nc < 0 || nc >= N) continue;

                        int neighbor = classroom[nr][nc];

                        if (neighbor == 0){
                            emptyCount++;
                        } else {
                            for (int d=0; d < 4; d++){
                                if (neighbor == like[now][d]){
                                    likeCount++;
                                }
                            }
                        }
                    }

                    // 조건 1,2,3 체크
                    if (likeCount > bestLike
                            || (likeCount == bestLike && emptyCount > bestEmpty)
                            || (likeCount == bestLike && emptyCount == bestEmpty
                            && (bestR == -1 || r < bestR || (r == bestR && c < bestC)))){
                        bestLike = likeCount;
                        bestEmpty = emptyCount;
                        bestR = r;
                        bestC = c;
                    }
                }
            }
            classroom[bestR][bestC] = now;
        }

        int result = 0;
        // 만족도 구하기
        for (int r = 0 ; r < N ; r++){
            for (int c = 0; c < N ; c++){
                int likeCount = 0;
                int now = classroom[r][c];
                for (int d = 0 ; d<4 ; d++){
                    int nr = r + dr[d];
                    int nc = c + dc[d];
                    if (nr < 0 || nr >= N || nc < 0 || nc >= N) continue;

                    if (classroom[nr][nc] == like[now][0]) likeCount++;
                    if (classroom[nr][nc] == like[now][1]) likeCount++;
                    if (classroom[nr][nc] == like[now][2]) likeCount++;
                    if (classroom[nr][nc] == like[now][3]) likeCount++;

                }

                if (likeCount == 1) result+=1;
                if (likeCount == 2) result+=10;
                if (likeCount == 3) result+=100;
                if (likeCount == 4) result+=1000;

            }
        }
        System.out.println(result);
    }

}
