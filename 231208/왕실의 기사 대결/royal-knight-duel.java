import java.util.*;
import java.io.*;

public class Main {

	static int L, N, Q;
	static int[][] map;
	static int[][] knightMap;

	static Knight[] knightInfo;
	static int[][] drc = { { -1, 0, 1, 0 }, { 0, 1, 0, -1 } }; // 상우하좌
	
	static Set<Integer> set;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());

		map = new int[L + 1][L + 1];
		knightMap = new int[L + 1][L + 1];
		for (int i = 1; i <= L; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= L; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		knightInfo = new Knight[N + 1];
		for (int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());

			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());

			Knight knight = new Knight(r, c, r + h - 1, c + w - 1, k, 0, 1);

			knightInfo[i] = knight;
			
			for(int a = r; a < r + h; a++) {
				for(int b = c; b < c + w; b++) {
					knightMap[a][b] = i;
				}
			}
		}
	
		while (Q-- > 0) {
			st = new StringTokenizer(br.readLine());

			int i = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			
			if(movePossible(i,d)) updateKnightInfo(i, d);
			
			updateKnightMap();
		}
		
		int ans = 0;
		for(int i = 1; i <= N; i++) {
			Knight knight = knightInfo[i];
			
			if(knight.state == 1) ans += knight.damage;
		}
		
		System.out.println(ans);
	}
	
	// move possible check
	public static boolean movePossible(int i, int d) {
		Queue<Integer> q = new LinkedList<>();
		q.add(i);
		
		set = new HashSet<>();
		set.add(i);
		
		while(!q.isEmpty()) {
			int now = q.poll();
			
			Knight nowKnight = knightInfo[now];
			
			for(int r = nowKnight.startR; r <= nowKnight.endR; r++) {
				for(int c = nowKnight.startC; c <= nowKnight.endC; c++) {
					
					int nr = r + drc[0][d];
					int nc = c + drc[1][d];
					
					// out
					if(nr <= 0 || nr > L || nc <= 0 || nc > L || map[nr][nc] == 2) return false;
					
					if(knightMap[nr][nc] != now && knightMap[nr][nc] > 0) {
						q.add(knightMap[nr][nc]);
						set.add(knightMap[nr][nc]);
					}
				}
			}
		}

		return true;
	}
	
	public static void updateKnightInfo(int i, int d) {		
		for(Integer s : set) {
			Knight knight = knightInfo[s];
			
			int nStartR = knight.startR + drc[0][d];
			int nStartC = knight.startC + drc[1][d];
			int nEndR = knight.endR + drc[0][d];
			int nEndC = knight.endC + drc[1][d];
			
			int power = knight.power;
			int damage = knight.damage;
			int state = knight.state;
			
			if(s != i) {
				damage = knight.damage + damage(nStartR, nStartC, nEndR, nEndC);
				state = (power - damage <= 0 ? 0 : 1);
			}
			
			knightInfo[s] = new Knight(nStartR, nStartC, nEndR, nEndC, power, damage, state);
		}
	}
	
	// damage Check
	public static int damage(int startR, int startC, int endR, int endC) {
		int cnt = 0;
		
		for(int r = startR; r <= endR; r++) {
			for(int c = startC; c <= endC; c++) {
				if(map[r][c] == 1) cnt++;
			}
		}
		
		return cnt;
	}
	
	// update Map
	public static void updateKnightMap() {
		int[][] tmp = new int[L+1][L+1];
		
		for(int i = 1; i <= N; i++) {
			Knight knight = knightInfo[i];
			
			if(knight.state == 0) continue;
			
			for(int r = knight.startR; r <= knight.endR; r++) {
				for(int c = knight.startC; c <= knight.endC; c++) {
					tmp[r][c]= i;
				}
			}
		}
		
		for(int r = 1; r <= L; r++) {
			for(int c = 1; c <= L; c++) {
				knightMap[r][c] = tmp[r][c];
			}
		}
	}
	
	

	public static class Knight {
		int startR, startC, endR, endC, power, damage, state;

		public Knight(int startR, int startC, int endR, int endC, int power, int damage, int state) {
			this.startR = startR;
			this.startC = startC;
			this.endR = endR;
			this.endC = endC;
			this.power = power;
			this.damage = damage;
			this.state = state;
		}

		@Override
		public String toString() {
			return "Knight [startR=" + startR + ", startC=" + startC + ", endR=" + endR + ", endC=" + endC + ", power="
					+ power + ", damage=" + damage + ", state=" + state + "]";
		}
		
		
	}
}