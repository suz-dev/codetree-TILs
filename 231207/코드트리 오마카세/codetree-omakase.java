import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class Main {
	
	static int L, Q;
	
	static Map<String, HashMap<Integer, Integer>> susiInfo;
	static Map<String, int[]> customerInfo;
	
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L = Integer.parseInt(st.nextToken());	// seats count
		Q = Integer.parseInt(st.nextToken());	// input count
		
		// order input	- code, time
		String[][] orders = new String[Q+1][];
		for(int i = 1; i <= Q; i++) {
			orders[i] = br.readLine().split(" ");
		}
		
		susiInfo = new HashMap<>();
		customerInfo = new HashMap<>();
		
		int idx = 1;
		for(int i = 1; i <= Integer.parseInt(orders[Q][1]); i++) {	// 전체 time 
						
			String[] order = orders[idx];
			int code = Integer.parseInt(order[0]);
			int time = Integer.parseInt(order[1]);
			
			// 초밥 회전
			susiRotation();
						
			// 초밥 먹기 -> whole customer
			eatSusi();
			
			// 초밥놓기, 손님등장, 사진촬영 O
			if(i == time) {			
				if(code == 100) {	// new susi
					
					String name = order[3];
					int location = Integer.parseInt(order[2]);
					
					// put susi
					putNewSusi(name, location);
//					System.out.println("put susi");
					
					// eat susi only me
					if(customerInfo.containsKey(name) && susiInfo.containsKey(name) && susiInfo.get(name).containsKey(location)) {
						int[] customer = customerInfo.get(name);
						int susiCnt = susiInfo.get(name).get(location);
												
						eatSusiOnlyMe(name, location, customer[1], susiCnt);
//						System.out.println("eat susi");
					}
				}
				
				if(code == 200) {	// customer in
					String name = order[3];
					int location = Integer.parseInt(order[2]);
					int n = Integer.parseInt(order[4]);
					
					// customer seat
					int[] tmp = {location, n};
					if(customerInfo.containsKey(name)) {
						tmp[1] += customerInfo.get(name)[1];
					}
					customerInfo.put(name, tmp);
//					System.out.println("customer seat");
					
					// eat susi only me (check)
					if(susiInfo.containsKey(name) && susiInfo.get(name).containsKey(location)) {
						int susiCnt = susiInfo.get(name).get(location);
						eatSusiOnlyMe(name, location, n, susiCnt);
			
//						System.out.println("eat susi");
					}
				}
				
				if(code == 300) {	// photo					
					int leftSusi = 0;
					if(!susiInfo.isEmpty()) {
						for(String name : susiInfo.keySet()) {
							leftSusi += susiInfo.get(name).size();
						}
					}
					
					System.out.println(customerInfo.size() + " " + leftSusi);
				}
				
				idx++;
			}	
			
		}
	}
	
	// put susi
	public static void putNewSusi(String name, int x) {		
		Map<Integer, Integer> susi;
		
		if(!susiInfo.containsKey(name)) {
			susi = new HashMap<>();
			susi.put(x, 1);
			
		}else {
			susi = susiInfo.get(name);
			
			if(!susi.containsKey(x)) {
				susi.put(x,1);
			}else {
				susi.put(x, susi.get(x) + 1);
			}		
		}
		
		susiInfo.put(name, (HashMap<Integer, Integer>) susi);
	}
	
	// rotation susi
	public static void susiRotation() {
		if(susiInfo.isEmpty()) return;
		
		for(String name : susiInfo.keySet()) {
			Map<Integer, Integer> tmp = new HashMap<>();
			Map<Integer, Integer> val = susiInfo.get(name);
			
			for(Entry<Integer, Integer> vals : val.entrySet()) {
				int loc = vals.getKey();
				int cnt = vals.getValue();
				
				tmp.put((loc+1) % L, cnt);		// location replace
			}
			
			susiInfo.put(name, (HashMap<Integer, Integer>) tmp);
		}
	}
			
	// eat susi
	public static void eatSusi() {
		if(customerInfo.isEmpty()) return;
		
		for(String name : customerInfo.keySet()) {
			
			if(!susiInfo.containsKey(name)) continue;
			
			Map<Integer, Integer> susiMap = susiInfo.get(name);
			int[] val = customerInfo.get(name); // x, n
			
			if(!susiMap.containsKey(val[0])) return;
			
			int susiCnt = susiMap.get(val[0]);
			
			val[1] -= susiCnt;

			// customer out
			if(val[1] <= 0) {
				customerInfo.remove(name);
				susiInfo.remove(name);
			}else {	// just eat susi (updateInfo)
				customerInfo.put(name, val);
				susiMap.remove(val[0]);
			}
		}
	}
	
	// eat susi only me
	public static void eatSusiOnlyMe(String name, int location, int n, int susiCnt) {
		int val = n - susiCnt;
		
		if(val <= 0) {
			customerInfo.remove(name);
			susiInfo.remove(name);
		}else {
			customerInfo.put(name, new int[] {location, val});
			susiInfo.get(name).remove(location);
		}
	}
	
	public static class Node{
		int x, n;

		public Node(int x, int n) {
			super();
			this.x = x;
			this.n = n;
		}
	}
}