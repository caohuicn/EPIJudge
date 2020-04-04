package lib;

public class UF {
    int[] id;
    int[] sz;
    int maxSize;

    UF(int n) {
        id = new int[n];
        sz = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
            sz[i] = 1;
        }
        maxSize = 1;
    }

    int find(int x) {
        if (id[x] == x) return x;
        id[x] = find(id[x]);
        return id[x];
    }

    void union(int p, int q) {
        int pid = find(p);
        int qid = find(q);
        if (pid == qid) return;

        if (sz[pid] < sz[qid]) {
            id[pid] = qid;
        } else {
            id[qid] = pid;
        }
        sz[pid] += sz[qid];
        sz[qid] = sz[pid];

        maxSize= Math.max(maxSize, sz[pid]);
    }

    int getMaxSize() {
        return maxSize;
    }

}
