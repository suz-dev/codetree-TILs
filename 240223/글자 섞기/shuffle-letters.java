import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        Map<String, String[]> map = new HashMap<>();

        List<String> list = new ArrayList<>();
        List<String> newList = new ArrayList<>();
        List<String> reverseList = new ArrayList<>();

        for(int i = 0; i < n; i++){
            String str = br.readLine();
            char[] chars = str.toCharArray();
            Arrays.sort(chars);

            String sortedStr = new String(chars);
            String reversedStr = new StringBuilder(sortedStr).reverse().toString();

            String[] vals = {sortedStr, reversedStr};
            map.put(str, vals);

            list.add(str);
            newList.add(sortedStr);
            reverseList.add(reversedStr);
        }

        Collections.sort(newList);
        Collections.sort(reverseList);

        for(int i = 0; i < n; i++){
            String now = list.get(i);
            String min = map.get(now)[0];
            String max = map.get(now)[1];

            int cnt1 = 1;
            int cnt2 = 1;
            for(int j = 0; j < n; j++){
                if(!newList.get(j).equals(min) && reverseList.get(j).compareTo(min) < 0) cnt1++;
                if(!reverseList.get(j).equals(max) && newList.get(j).compareTo(max) <= 0) cnt2++;
            }

            System.out.println(cnt1 + " " + cnt2);
        }
    }
}