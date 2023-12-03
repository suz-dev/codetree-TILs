import java.io.*;
import java.util.*;

public class Main {
    static int L, N, Q;
    static Knight[] knights;
    static int[][] map, knightMap;
    static int[][] drc = {{-1, 0, 1, 0}, {0, 1, 0, -1}};  // 북 동 남 서
    static Queue<Integer> moveQ;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());   // map 크기
        N = Integer.parseInt(st.nextToken());   // 기사 정보 수
        Q = Integer.parseInt(st.nextToken());   // 명령 수

        // 0: 빈칸, 1: 함정, 2: 벽
        map = new int[L + 1][L + 1];
        for (int i = 1; i < L + 1; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j < L + 1; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        knights = new Knight[31];
        knightMap = new int[L + 1][L + 1];
        for (int i = 1; i < N + 1; i++) {
            st = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            Knight knight = new Knight(r, c, (r + h) - 1, (c + w) - 1, k, 0);
            knights[i] = knight;    // 초기 정보

            fillKnightMap(i, knight);   // 기사들 위치 정보
        }

        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine());
            int i = Integer.parseInt(st.nextToken());   // 기사 번호
            int d = Integer.parseInt(st.nextToken());   // 이동 방향

            moveQ = new LinkedList<>(); // 움직일 기사

            // 이동 가능 check (boolean)
            if (knights[i] == null || !movePossible(i, d)) continue;

            // 이동
            // 기사별 데미지 check
            moveKnights(i, d);
        }

        int ans = 0;
        for (int i = 1; i <= 30; i++) {
            if (knights[i] != null) ans += knights[i].damages;
        }
        System.out.println(ans);
    }

    // bfs
    public static boolean movePossible(int i, int d) {
        Queue<Integer> q = new LinkedList<>();  // 기사 리트스
        Set<Integer> set = new HashSet<>();
        q.offer(i);

        while (!q.isEmpty()) {
            int knightNum = q.poll();
            Knight now = knights[knightNum];

            int nStartR = now.startR + drc[0][d];
            int nStartC = now.startC + drc[1][d];
            int nEndR = now.endR + drc[0][d];
            int nEndC = now.endC + drc[1][d];

            for (int r = nStartR; r <= nEndR; r++) {
                for (int c = nStartC; c <= nEndC; c++) {

                    // 이동 불가
                    if (r <= 0 || r >= L + 1 || c <= 0 || c >= L + 1 || map[r][c] == 2) return false;

                    // 이동 가능 && 밀어낼 대상 X
                    if (knightMap[r][c] == 0 || knightMap[r][c] == knightNum) continue;

                    // 이동 가능 && 밀어낼 대상 O (대상 큐에 넣기
                    q.offer(knightMap[r][c]);
                    set.add(knightMap[r][c]);
                }
            }
        }

        moveQ.offer(i);
        for (Integer s : set) {
            moveQ.add(s);
        }
        return true;
    }

    // 위치 갱신
    public static void moveKnights(int i, int d) {
        while (!moveQ.isEmpty()) {
            int knightNum = moveQ.poll();
            Knight now = knights[knightNum];

            int nStartR = now.startR + drc[0][d];
            int nStartC = now.startC + drc[1][d];
            int nEndR = now.endR + drc[0][d];
            int nEndC = now.endC + drc[1][d];

            if (knightNum != i) {
                // damage check
                for (int r = nStartR; r <= nEndR; r++) {
                    for (int c = nStartC; c <= nEndC; c++) {
                        if (map[r][c] == 1) now.damages++;
                    }
                }
            }

            // 기사 제거
            if (now.k - now.damages <= 0) {
                knights[knightNum] = null;
            } else { // 정보 갱신
                knights[knightNum] = new Knight(nStartR, nStartC, nEndR, nEndC, now.k, now.damages);
            }
        }

        knightMap = new int[L + 1][L + 1];

        // knightMap 갱신
        for (int k = 1; k <= 30; k++) {
            if (knights[k] != null) fillKnightMap(k, knights[k]);
        }
    }

    public static void fillKnightMap(int i, Knight knight) {
        for (int r = knight.startR; r <= knight.endR; r++) {
            for (int c = knight.startC; c <= knight.endC; c++) {
                knightMap[r][c] = i;
            }
        }
    }

    static class Knight {
        int startR, startC, endR, endC, k, damages;

        public Knight(int startR, int startC, int endR, int endC, int k, int damages) {
            this.startR = startR;
            this.startC = startC;
            this.endR = endR;
            this.endC = endC;
            this.k = k;
            this.damages = damages;
        }

        @Override
        public String toString() {
            return "Knight{" +
                    "startR=" + startR +
                    ", startC=" + startC +
                    ", endR=" + endR +
                    ", endC=" + endC +
                    ", k=" + k +
                    ", damages=" + damages +
                    '}';
        }
    }
}