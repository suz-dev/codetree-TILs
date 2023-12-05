import java.io.*;
import java.util.*;

public class Main {

	static int N, M, P, C, D;
	static int[][] map;

	static int[][] drc = { { -1, 0, 1, 0, -1, 1, 1, -1 }, { 0, 1, 0, -1, 1, 1, -1, -1 } }; // 상우하좌 + 대각선
	
	static int rudolphR, rudolphC; // 루돌프 위치
	static Santa[] santaInfo;
	static int failedSanta = 0;	// if(failedSanta == P) game done

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());	
		M = Integer.parseInt(st.nextToken());
		P = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());
		
		map = new int[N+1][N+1];
		
		// 루돌프 초기 위치
		st = new StringTokenizer(br.readLine());
		rudolphR = Integer.parseInt(st.nextToken());
		rudolphC = Integer.parseInt(st.nextToken());
		map[rudolphR][rudolphC] = -1;
		
		// 산타 정보
		santaInfo = new Santa[P+1];
		for(int p = 1; p <= P; p++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			
			santaInfo[num] = new Santa(r,c,2,0);
			map[r][c] = num;
		}
		
		int idx = 0;
		while(idx++ < M) {			
			// game done
			if(failedSanta == P) break;
			
			// 턴 시작할때 산타 갱신
			for(int p = 1; p <= P; p++) {
				if(santaInfo[p].out == -1) continue;
				if(santaInfo[p].out < 2) {	
					++santaInfo[p].out;
					continue;
				}
			}
			
			// rudolph move
			rudolphMove();
			
			// update Map
			updateMap();	
			
			// santa move
			for(int p = 1; p <= P; p++) {				
				// except
				if(santaInfo[p].out < 2) continue;
				
				// santa move
				santaMove(p);
				
				// updateMap
				updateMap();
			}
			
			// saved Santa -> score++
			for(int p = 1; p <= P; p++) {
				if(santaInfo[p].out > -1) santaInfo[p].score++;
			}
		}
		
		// answer
		for(int p = 1; p <= P; p++) {
			System.out.print(santaInfo[p].score + " ");
		}
	}
	
	// santa move
	public static void santaMove(int num) {
		// check distance to Rudolph
		// distance, direction, nr, nc
		PriorityQueue<int[]> pq = new PriorityQueue<>((o1,o2)-> {
			if(o1[0] == o2[0]) return o1[1] - o2[1];
			return o1[0] - o2[0];
		});
		
		Santa nowSanta = santaInfo[num];
		
		// now location
		int distance = calDis(rudolphR,rudolphC, nowSanta.r, nowSanta.c);
		pq.add(new int[] {distance, -1, nowSanta.r, nowSanta.c});
		
		for(int d = 0; d < 4; d++) {
			int nr = nowSanta.r + drc[0][d];
			int nc = nowSanta.c + drc[1][d];
			
			// move impossible
			if(!rangeCheck(nr,nc) || map[nr][nc] > 0) continue;
			
			distance = calDis(rudolphR,rudolphC, nr, nc);
			pq.add(new int[] {distance, d, nr, nc});
		}
		
		int[] tmp = pq.poll();
		
		// not move
		if(tmp[1] == -1) return;
		
		// non crush -> just move
		nowSanta.r = tmp[2];
		nowSanta.c = tmp[3];
		
		// crush with rudolph
		if(map[tmp[2]][tmp[3]] == -1) {
			int dir = (tmp[1] + 2) % 4;
			crush(num, dir, D);
		}
	}
	
	// rudolph move 
	public static void rudolphMove() {
		// find santa
		// num, distance, r, c
		PriorityQueue<int[]> pq = new PriorityQueue<>((o1,o2)->{
			if(o1[1] == o2[1]) {
				if(o1[2] == o2[2]) return o2[3] - o1[3];
				return o2[2] - o1[2];
			}
			return o1[1] - o2[1];
		});
		
		for(int p = 1; p <= P; p++) {
			Santa santa = santaInfo[p];
			
			if(santa.out == -1) continue;	// failed Santa
						
			// 1. calculate distance
			int distance = calDis(rudolphR, rudolphC, santa.r, santa.c);
			pq.add(new int[] {p, distance, santa.r, santa.c});
		}
		
		if(pq.isEmpty()) return;	
		
		int[] tmpSanta = pq.poll();
		
		// find direction
		// direction, distance, nr, nc
		PriorityQueue<int[]> dirPQ = new PriorityQueue<>((o1,o2)->{
			return o1[1] - o2[1];
		});
			
		for(int d = 0; d < 8; d++) {
			int nr = rudolphR + drc[0][d];
			int nc = rudolphC + drc[1][d];
			
			// range out
			if(!rangeCheck(nr, nc)) continue;
			
			int distance = calDis(nr, nc, tmpSanta[2], tmpSanta[3]);
			dirPQ.add(new int[] {d, distance, nr, nc});
		}
		
		// move possible
		if(!dirPQ.isEmpty()) {
			int[] tmpDir = dirPQ.poll();
						
			// non crush -> just move
			rudolphR = tmpDir[2];
			rudolphC = tmpDir[3];
			
			// crush with santa
			if(map[rudolphR][rudolphC] > 0)
				crush(tmpSanta[0], tmpDir[0], C);
		}
	}
	
	// crush
	public static void crush(int num, int dir, int score) {
				
		// score, time 
		Santa nowSanta = santaInfo[num];
		nowSanta.score += score;
		nowSanta.out = 0;
		
		// move
		int nr = nowSanta.r + drc[0][dir] * score;
		int nc = nowSanta.c + drc[1][dir] * score;
		
		// out
		if(!rangeCheck(nr, nc)) {
			nowSanta.out = -1;
			failedSanta++;
			return;
		}
		
		// just move
		if(map[nr][nc] == 0) {
			santaInfo[num].r = nr;
			santaInfo[num].c = nc;
			return;
		}
			
		// relation
		if(map[nr][nc] != num && map[nr][nc] > 0) {
			relation(num, nr, nc, dir);
			return;
		}
	}
	
	// relation
	public static void relation(int num, int r, int c, int dir) {			
		int nr = santaInfo[num].r + drc[0][dir];
		int nc = santaInfo[num].c + drc[1][dir];
		
		// santa fail
		if(!rangeCheck(nr, nc)) {
			santaInfo[num].out = -1;
			failedSanta++;
			return;
		}
		
		santaInfo[num].r = nr;
		santaInfo[num].c = nc;
		
		if(map[nr][nc] > 0) relation(map[nr][nc], nr, nc, dir);
		else {	// just move
			santaInfo[num].r = nr;
			santaInfo[num].c = nc;
			return;
		}
	}
	
	public static int calDis(int r1, int c1, int r2, int c2) {
		return (int) (Math.pow(Math.abs(r1-r2), 2) + Math.pow(Math.abs(c1-c2), 2));
	}
	
	// update map info
	public static void updateMap() {
		map = new int[N+1][N+1];
		
		map[rudolphR][rudolphC] = -1;
		for(int p = 1; p <= P; p++) {
			Santa santa = santaInfo[p];
			if(santa.out > -1) map[santa.r][santa.c] = p;
		}
		
	}
	
	public static boolean rangeCheck(int r, int c) {
		if(r <= 0 || c > N || c <= 0 || r > N) return false;
		return true;
	}
	
	public static class Santa {
		int r, c, out, score;

		public Santa(int r, int c, int out, int score) {
			this.r = r;
			this.c = c;
			this.out = out;
			this.score = score;
		}

		@Override
		public String toString() {
			return "Santa [r=" + r + ", c=" + c + ", out=" + out + ", score=" + score + "]";
		}
	}
}