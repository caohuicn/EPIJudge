package lc;

import java.util.*;

public class WordSearch {
    // Word search

    // BRT
    // YEA
    // PWC

    // 'cat', 'dog', 'eat'

    // input: nxn grid of letters, list of words
    // output: print the grid, but only with the words' letters
    // Words can be in 8 different directions (can't change direction in the middle of word)

    // ..T
    // ..A
    // ..C

    static class TrieNode {
        Map<Character, TrieNode> map = new HashMap<>();
        String word;
    }
    static class Trie{
        TrieNode root = new TrieNode();
        void insertWord(String w) {
            TrieNode n = root;
            for (int i = 0; i < w.length(); i++) {
                char c = w.charAt(i);
                if (!n.map.containsKey(c)) {
                    n.map.put(c, new TrieNode());
                }
                n = n.map.get(c);
            }
            n.word = w;
        }
    }

    public char[][] findWords(char[][] board, String[] words) {
        Trie trie = buildTrie(words);
        int m = board.length;
        int n = board[0].length;
        char[][] ans = new char[m][n];
        for (char[] cs : ans) {
            Arrays.fill(cs, '.');
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < DIR.length; k++) {
                    search(board, i, j, trie.root, DIR[k], ans);
                }
            }
        }
        return ans;
    }

    Trie buildTrie(String[] words) {
        Trie trie = new Trie();
        for (String w : words) {
            trie.insertWord(w);
        }
        return trie;
    }

    static final int[][] DIR = new int[][]{{0, -1},{-1, -1},{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}};

    void search(char[][] board, int r, int c, TrieNode tn, int[] dir, char[][] ans) {
        char letter = board[r][c];
        String word = null;
        int nr = r;
        int nc = c;

        while (tn.map.containsKey(letter)) {
            tn = tn.map.get(letter);
            if (tn.word != null) {
                //don't break yet, to get the longest word
                word = tn.word;
            }
            nr = nr + dir[0];
            nc = nc + dir[1];
            if (nr < 0 || nr >= board.length || nc < 0 || nc >= board[0].length) break;
            letter = board[nr][nc];
        }
        if (word != null) {
            for (int i = 0; i < word.length(); i++) {
                ans[r + dir[0] * i][c + dir[1] * i] = word.charAt(i);
            }
        }
    }

    public static void main(String[] args) {
        WordSearch ws = new WordSearch();
        char[][] b = new char[][]{{'b','r','t'},{'y','e','a'},{'p','w','c'}};
        String[] words = new String[]{"ca", "cat", "dog", "eat"};
        char[][] nb = ws.findWords(b, words);
        for (int i = 0; i < nb.length; i++) {
            for (int j = 0; j < nb[i].length; j++) {
                System.out.print(nb[i][j] + ",");
            }
            System.out.println();
        }
    }
}