import java.io.*;
import java.util.*;

public class Main {

    static int n;
    static ArrayList<String> list, newList, reverseList;
    static Map<String, String[]> map;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());

        map = new HashMap<>();

        list = new ArrayList<>();
        newList = new ArrayList<>();
        reverseList = new ArrayList<>();

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

            int idx1 = Collections.binarySearch(newList, min);
            int idx2 = Collections.binarySearch(reverseList, max);

            newList.remove(idx1);
            reverseList.remove(idx2);

            int cnt1 = Collections.binarySearch(reverseList, min);
            int cnt2 = Collections.binarySearch(newList, max);

            cnt1 *= (-1);
            cnt2 *= (-1);

            newList.add(idx1, min);
            reverseList.add(idx2, max);
   
            System.out.println(cnt1 + " " + cnt2);
        }
    }
}