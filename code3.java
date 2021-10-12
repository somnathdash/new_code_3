/* package codechef; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */

class FastReader {
    BufferedReader br;
    StringTokenizer st;

    public FastReader()
    {
        br = new BufferedReader(
            new InputStreamReader(System.in));
    }

    String next()
    {
        while (st == null || !st.hasMoreElements()) {
            try {
                st = new StringTokenizer(br.readLine());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return st.nextToken();
    }

    int nextInt() { return Integer.parseInt(next()); }

    long nextLong() { return Long.parseLong(next()); }

    double nextDouble()
    {
        return Double.parseDouble(next());
    }

    String nextLine()
    {
        String str = "";
        try {
            str = br.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}

class Pair implements Comparable<Pair> {
    private int a;
    private int b;
    
    public Pair(int a, int b) {
        this.a = a;
        this.b = b;
    }
    
    public int first() {
        return this.a;
    }
    
    public int second() {
        return this.b;
    }
    
    public int compareTo(Pair p) {

        if(this.first() == p.first())
            return -Integer.compare(this.second(),p.second());
        else
            return -Integer.compare(this.first(),p.first());
    }
}

class Codechef
{
    static FastReader sc;
    static int MAX_NUM;
    static long MOD;
    static ArrayList<Integer>[] tree;
    static PriorityQueue<Pair> nodesPath;
    static ArrayList<Integer> sSet;
    static int[] A;
    static int[] B;
    static int[] a;
    static int[] b;
    static int[] h;
    static int[] connected;
    static int[] parent;
    static int[] visited;
    static int n,s,dd;
    
    public static void initialize() {
        sc = new FastReader();
        MOD = 1000000007L;
        MAX_NUM = 1000001;
        A = new int[MAX_NUM];
        B = new int[MAX_NUM];
        a = new int[MAX_NUM];
        b = new int[MAX_NUM];
        h = new int[MAX_NUM];
        connected = new int[MAX_NUM];
        parent = new int[MAX_NUM];
        visited = new int[MAX_NUM];

        tree = new ArrayList[MAX_NUM];
        sSet = new ArrayList<>();
        nodesPath = new PriorityQueue<>();
        for(int i = 0 ; i < MAX_NUM ; i++) {
            tree[i] = new ArrayList<Integer>();
        }
    }
    
    public static void destructor() {
        for (int i = 0 ; i <= n ; i++) {
            tree[i].clear();
            a[i] = 0;
            b[i] = 0;
            parent[i] = 0;
            h[i] = 0;
        }
        while(nodesPath.size() > 0) {
            nodesPath.poll();
        }
    }
    
    public static void cleanNode(int item) {
        connected[a[item]] = 0;
        connected[b[item]] = 0;
        
        A[a[item]] = 0;
        A[b[item]] = 0;
        
        B[a[item]] = 0;
        B[b[item]] = 0;
    }
    
    public static int canMakeSet(int item) {
        B[b[item]]++;
        A[a[item]]++;
        if (A[a[item]] == B[a[item]] && connected[a[item]] != 0) {
            connected[a[item]]--;
            dd--;
        } else if (connected[a[item]] == 0) {
            connected[a[item]]++;
            dd++;
        }
        
        if (A[b[item]] == B[b[item]] && connected[b[item]] != 0) {
            connected[b[item]]--;
            dd--;
        } else if (connected[b[item]] == 0) {
            connected[b[item]]++;
            dd++;
        }
        
        visited[item]++;
        sSet.add(item);
        
        if (dd == 0) {
            if (visited[parent[item]] == 0 && item != 1) {
                nodesPath.add(new Pair(h[parent[item]], parent[item]));
            }
            cleanNode(item);
            return 1;
        }
        
        if (item == 1) {
            cleanNode(item);
            return 0;
        }
        
        if (visited[parent[item]] == 0) {
            if (canMakeSet(parent[item]) == 1) {
                cleanNode(item);
                return 1;
            }
        }
        
        cleanNode(item);
        return 0;
    }
    
    public static void printAnswer() {
        boolean flag = true;
        ArrayList<ArrayList<Integer>> sets = new ArrayList<>();
        while (nodesPath.size() > 0) {
            Pair leaf = nodesPath.poll();
            if (visited[leaf.second()] == 0) {
                dd = 0;
                sSet.clear();
                if (canMakeSet(leaf.second()) == 0) {
                    flag = false;
                    break;
                } else {
                    sets.add((ArrayList<Integer>)sSet.clone());
                }
            }
        }
        if (flag == false) {
            System.out.println("0");
            return;
        }
        
        if (s == 1) {
            System.out.println("1");
            return;
        }
        
        long ans = 1;
        int x = sets.size();
        
        for (int i = 0 ; i < x ; i++) {
            int u = sets.get(i).get(0);
            int l = sets.get(i).get(0);
            int xx = sets.get(i).size();
            for (int j = 1 ; j < xx ; j++) {
                if (h[sets.get(i).get(j)] > h[l]) {
                    l = sets.get(i).get(j);
                }
                if (h[sets.get(i).get(j)] < h[u]) {
                    u = sets.get(i).get(j);
                }
            }
            long count = 0;
            for (int ch: tree[l]) {
                if (ch != parent[l]) {
                    count += 1;
                }
            }
            ans = (ans*(count+1))%MOD;
        }
        System.out.println(ans);
    }
    
    public static void computation(int node, int d) {
        visited[node] += 1;
        h[node] = d;
        boolean flag = true;
        for (int ch: tree[node]) {
            if (visited[ch] == 0) {
                parent[ch] = node;
                computation(ch,d+1);
                flag = false;
            }
        }
        if (flag) {
            nodesPath.add(new Pair(d,node));
        }
    }
    
    public static void process() {
        for (int i = 1 ; i < n+1 ; i++) {
            visited[i] = 0;
            parent[i] = 0;
            h[i] = 0;
        }
        
        computation(1,1);
        
        for (int i = 1 ; i < n+1 ; i++) {
            visited[i] = 0;
        }
    }
    
    public static void takeInput() {
        n = sc.nextInt();
        s = sc.nextInt();
        for (int i = 0 ; i < n-1 ; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            tree[u].add(v);
            tree[v].add(u);
        }
        for (int i = 1 ; i < n+1 ; i++) {
            a[i] = sc.nextInt();
        }
        for (int i = 1 ; i < n+1 ; i++) {
            b[i] = sc.nextInt();
        }
    }
    
    public static void main (String[] args) throws java.lang.Exception
    {
        initialize();
        int t = sc.nextInt();
        while (t-- > 0) {
            takeInput();
            process();
            printAnswer();
            destructor();
        }
    }
}
