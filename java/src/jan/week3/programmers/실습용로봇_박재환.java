package jan.week3.programmers;

import java.util.Scanner;

public class 실습용로봇_박재환 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();

        실습용로봇_박재환 problem = new 실습용로봇_박재환();
        System.out.println(problem.solution(command));
    }

    /*
        로봇은 x, y 좌표 평면 위에서 이동한다.
        하나의 명령은 하나의 문자로 주어진다.
        - R : 오른쪽으로 90도 회전
        - L : 왼쪽으로 90도 회전
        - G : 한 칸 전진
        - B : 한 칸 후진

        로봇의 초기 위치는 (0,0) 에 있다.
        -> 이때 좌측 상단이 아닌, 격자의 정중앙이 (0,0) 이다.
        -> 초기에 로봇이 바라보고 있는 방향은 북쪽 방향이다.

        => 좌표는 생각하지 않고, 이동만 생각하면 될 듯
    */

    // 상 우 하 좌
    int[] dx = {0,1,0,-1};
    int[] dy = {1,0,-1,0};
    public int[] solution(String command) {
        Robot robot = new Robot();
        for(char c : command.toCharArray()) {
            switch(c) {
                case 'R':
                    robot.turnRight();
                    break;
                case 'L':
                    robot.turnLeft();
                    break;
                case 'G':
                    robot.moveForward();
                    break;
                case 'B':
                    robot.moveBackward();
                    break;
            }
            System.out.println(robot.currentState());
        }
        return robot.currentLocation();
    }

    class Robot {
        int dir;        // 로봇이 바라보고 있는 방향
        int x, y;    // 로봇의 현재 위치

        Robot() {
            this.dir = 0;
            this.x = 0;
            this.y = 0;
        }

        void turnRight() {
            this.dir = (this.dir + 1)%4;
        }

        void turnLeft() {
            this.dir = (4 + (this.dir - 1))%4;
        }

        void moveForward() {
            this.x += dx[dir];
            this.y += dy[dir];
        }

        void moveBackward() {
            this.x -= dx[dir];
            this.y -= dy[dir];
        }

        int[] currentLocation() { return new int[] {this.x, this.y}; }

        String currentState() {
            return String.format("dir : [%d], currentLocation : [%d, %d]", this.dir, this.x, this.y);
        }
    }
}
