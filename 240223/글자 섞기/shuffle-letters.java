import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        String[] arr = new String[n];
        String[] reverseArr = new String[n];

        // 문자열 입력 및 정렬
        for(int i = 0; i < n; i++){
            String str = br.readLine();
            char[] chars = str.toCharArray();
            Arrays.sort(chars);

            arr[i] = new String(chars);  // 알파벳 오름차순
            reverseArr[i] = new StringBuilder(arr[i]).reverse().toString();    // 알파벳 내림차순
        }

        // 문자열 비교
        for(int i = 0; i < n; i++){
            String now = arr[i];
            String reverseNow = reverseArr[i];

            String[] tmpArr = Arrays.copyOf(arr, arr.length);
            String[] tmpReverseArr = Arrays.copyOf(reverseArr, reverseArr.length);

            tmpArr[i] = reverseNow;
            tmpReverseArr[i] = now;

            Arrays.sort(tmpArr);
            Arrays.sort(tmpReverseArr);

            int cnt1 = Arrays.binarySearch(tmpArr, reverseNow) + 1;
            int cnt2 = Arrays.binarySearch(tmpReverseArr, now) + 1;

            System.out.println(cnt2 + " " + cnt1);
        }
    }
}