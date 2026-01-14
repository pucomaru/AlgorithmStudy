package jan.week3.programmers;

import java.util.Iterator;
import java.util.function.Function;
import java.util.List;
import java.util.ArrayList;

public class 숫자야구_박재환 {
    public static void main(String[] args) {
        숫자야구_박재환 problem = new 숫자야구_박재환();
        problem.makeAllPassword(new StringBuilder(), new boolean[10]);
    }
    /**
     * 숫자가 비밀번호에 포함되어 있지 않다. : OUT
     * 숫자가 비밀번호에 포함되어 있지만, 위치가 틀리다. : BALL
     * 숫자가 비밀번호에 포함되어 있고, 위치까지 정확하다. : STRIKE
     */
    List<String> passwords;
    public int solution(int n, Function<Integer, String> submit) {
        passwords = new ArrayList<>();
        makeAllPassword(new StringBuilder(), new boolean[10]);
        while(passwords.size() > 1) {
            int passwordCandidate = Integer.parseInt(passwords.get(0));
            String matchResult = submit.apply(passwordCandidate);
            filterIncorrectPassword(matchResult, String.valueOf(passwordCandidate));
        }
        return Integer.parseInt(passwords.get(0));
    }
    /**
     * 서로 다른 숫자 4개로 이루어진 비밀번호를 맞춘다.
     * 1. 가장 먼저 서로 다른 숫자 4개로 만들 수 있는 비밀번호를 모두 만든다.
     */
    void makeAllPassword(StringBuilder password, boolean[] isUsed) {
        if(password.length() == 4) {
            passwords.add(password.toString());
            return;
        }

        for(int num=1; num<10; num++) {
            if(isUsed[num]) continue;

            isUsed[num] = true;
            password.append(num);
            makeAllPassword(password, isUsed);
            password.deleteCharAt(password.length()-1);
            isUsed[num] = false;
        }
    }

    void filterIncorrectPassword(String result, String submitPassword) {
        List<String> filteredPassword = new ArrayList<>();
        int strikeCount = result.charAt(0) - '0';
        int ballCount = result.charAt(3) - '0';

        for(String password : passwords) {
            if(compareTwoPassword(password, submitPassword, strikeCount, ballCount)) filteredPassword.add(password);
        }

        passwords = filteredPassword;
    }

    boolean compareTwoPassword(String candidatePassword, String validationPassword, int strikeCount, int ballCount) {
        int s=0, b=0;

        for(int i=0; i<4; i++){
            if(candidatePassword.charAt(i) == validationPassword.charAt(i)) s++;
            else if(candidatePassword.indexOf(validationPassword.charAt(i)) > -1) b++;
        }

        return strikeCount == s && ballCount == b;
    }
}
