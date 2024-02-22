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

            arr[0][i] = sb.toString();
            arr[1][i] = sb.reverse().toString();
        }

        int idx = 0;
        while(idx < n){
            String now = arr[0][idx];
            String reverseNow = arr[1][idx];

            PriorityQueue<String> pq = new PriorityQueue<>();
            for(int i = 0; i < n; i++){
                if(i != idx) pq.add(arr[1][i]);
                else pq.add(now);
            }

            int cnt1 = 0;
            String tmp = "";

            while(!tmp.equals(now)){
                tmp = pq.poll();
                cnt1++;
            }

            pq = new PriorityQueue<>();
            for(int i = 0; i < n; i++){
                if(i != idx) pq.add(arr[0][i]);
                else pq.add(reverseNow);
            }

            int cnt2 = 0;
            String tmp2 = "";

            while(!tmp2.equals(reverseNow)){
                cnt2++;
                tmp2 = pq.poll();
            }

            idx++;
            System.out.println(cnt1 + " " + cnt2);
        }
    }
}