package feb.week1.boj;

import java.io.*;
import java.util.*;

public class 나무재테크_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int N, M, K;
    static List<Tree>[][] land ;
    static int[][] A;
    static int[][] nutrient;

    static int[] dy = {-1,-1,-1,0,0,1,1,1};
    static int[] dx = {-1,0,1,-1,1,-1,0,1};
    // 나무 재테크
    // 봄  (양분 먹기,죽음)
    // 1. 나무가 자신의 나이만큼 양분을 먹고, 나이가 1증가
    // 2. 각각 나무는 나무가 있는 1 x 1 크기의 칸에 있는 양분만 먹을 수 있음
    // 3. 하나의 칸에 여러 개의 나무가 있다면, 나이가 어린 나무부터 양분을 먹는다.
    // 4. 만약, 땅에 양분이 부족해 자신의 나이만큼 양분을 먹을 수 없는 나무는 양분을 먹지 못하고 죽음

    // 여름 ( 죽은 나무 양분 변함 , 추가 )
    // 1. 봄에 죽은 나무가 양분으로 변함
    // 2. 각각 죽은 나무마다 나이를 2로 나눈 값이 나무가 있떤 칸에 양분으로 추가. 소수점 아래 버림

    // 가을 (번식)
    // 1. 나무 번식
    // 2. 번식 나무 나이 5의 배수 / 인접한 8개의 칸에 나이가 1인 나무 생김
    // 3. 상도의 땅을 벗어나는 칸에는 나무가 생기지 않는다.

    // 겨울 (양분 추가)
    // 1. 로봇이 땅 돌아다니면서 양분 추가 .

    // K년이 지난 후 살아있는 나무의 개수를 구하는 프로그램 작성

    public static void main(String[] args) throws IOException {

        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        // N : 땅크기
        N = Integer.parseInt(st.nextToken());
        // M : 나무 갯수
        M = Integer.parseInt(st.nextToken());
        // K : K년이 지난 후 나무의 수 출력
        K = Integer.parseInt(st.nextToken());

        // N*N 크기의 땅
        land = new ArrayList[N+1][N+1];

        for (int i = 0 ; i < N + 1 ; i++){
            for(int j = 0; j< N + 1 ;j++){
                land[i][j] = new ArrayList<>();
            }
        }

        // 양분 배열
        A = new int[N+1][N+1];
        // 현재 양분 배열
        nutrient = new int[N+1][N+1];

        // 양분 초기값은 5
        for (int i = 1 ; i < N+1 ; i++){
            for (int j = 1; j < N+1 ; j++){
                nutrient[i][j] = 5;
            }
        }
        // A 배열 양분 저장
        for (int i = 1 ; i < N+1 ; i++){
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j < N + 1 ; j++){
                A[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 나무가 심겨져 있는 위치
        for(int i = 0 ; i < M ; i++){
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int age = Integer.parseInt(st.nextToken());

            Tree tree = new Tree(age,true);
            land[r][c].add(tree);
        }

        // 나무가 심겨져있는 배열이면 나무 나이를 기준으로 오름차순으로 정렬
        for(int i = 1 ; i < N + 1; i++){
            for (int j = 1 ; j < N + 1; j++){
                if(land[i][j].isEmpty()) continue;
                if(land[i][j].size() == 1) continue;
                Collections.sort(land[i][j], (a,b) -> a.age - b.age);
            }
        }

        for (int i = 0 ; i < K ;i++){
            spring();
            summer();
            fall();
            winter();
        }

        int result = 0;

        for(int i = 1 ; i < N + 1; i++){
            for (int j = 1; j < N + 1 ; j++){
                if(land[i][j].isEmpty()) continue;
                result += land[i][j].size();
            }
        }
        System.out.println(result);
    }

    static void spring(){
        for(int i = 1 ; i < N + 1 ; i++){
            for(int j = 1 ; j < N + 1; j++){
                if (land[i][j].isEmpty()) continue;
                for(int k = 0 ; k < land[i][j].size(); k++){
                    Tree nowTree = land[i][j].get(k);
                    if(nowTree.age <= nutrient[i][j]) {
                        nutrient[i][j] -= nowTree.age;
                        nowTree.age ++;
                    } else nowTree.state = false;
                }
            }

        }
    }


    static void summer(){
        for(int i = 1 ; i < N + 1 ; i++){
            for (int j = 1; j < N + 1 ; j++){
                if(land[i][j].isEmpty()) continue;
                for (int k = land[i][j].size() - 1 ; k >=0; k--){
                    Tree nowTree = land[i][j].get(k);
                    if (nowTree.state == false) {
                        nutrient[i][j] += (nowTree.age / 2);
                        land[i][j].remove(k);
                    };
                }
            }
        }
    }

    static void fall(){
        for(int i = 1; i < N + 1 ; i++){
            for (int j = 1; j < N + 1; j++){
                if(land[i][j].isEmpty()) continue;
                for (int k = 0 ; k < land[i][j].size();k++){
                    Tree nowTree = land[i][j].get(k);
                    if(nowTree.age%5 == 0){
                        int nowR = i;
                        int nowC = j;
                        for(int m = 0 ; m < 8 ; m++){
                            int ny = nowR + dy[m];
                            int nx = nowC + dx[m];
                            if(ny <= 0 || ny >= N+1 || nx <= 0 || nx >= N+1) continue;
                            land[ny][nx].add(0,new Tree(1,true));
                        }
                    }
                }
            }
        }

    }

    static void winter(){
        for(int i = 1 ; i < N+1 ; i++){
            for(int j = 1; j< N+1 ; j++){
                nutrient[i][j] += A[i][j];
            }
        }
    }

    static class Tree{
        int age;
        boolean state;

        Tree(int age, boolean state){
            this.age = age;
            this.state = state;
        }

    }

}
