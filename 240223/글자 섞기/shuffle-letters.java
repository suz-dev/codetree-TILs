import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        String[][] arr = new String[2][n];

        for(int i = 0; i < n; i++){
            String str = br.readLine();
            ArrayList<Character> tmp = new ArrayList();
            for(int j = 0; j < str.length(); j++){
                tmp.add(str.charAt(j));
            }

            Collections.sort(tmp);
            StringBuilder sb = new StringBuilder();
            for(int j = 0; j < str.length(); j++){
                sb.append(tmp.get(j));
            }

            arr[0][i] = sb.toString();  // 알파벳 오름차순
            arr[1][i] = sb.reverse().toString();    // 알파벳 내림차순
        }

        int idx = 0;
        while(idx < n){
            String now = arr[0][idx];
            String reverseNow = arr[1][idx];

            ArrayList<String> list1 = new ArrayList<>();
            ArrayList<String> list2 = new ArrayList<>();

            int cnt1 = 1;
            for(int i = 0; i < n; i++){
                if(i != idx && now.compareTo(arr[1][i]) > 0) {
                    cnt1++;
                }
            }

            int cnt2 = 1;
            for(int i = 0; i < n; i++){
                if(i != idx && reverseNow.compareTo(arr[0][i]) > 0) {
                    cnt2++;
                }
            }

            System.out.println(cnt1 + " " + cnt2);
            idx++;
        }
    }
}