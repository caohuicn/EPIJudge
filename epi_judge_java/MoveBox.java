import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoveBox {
    static class Solution {
        public int minPushBox(char[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            //4 position of S relative to box (l,t,r,d) * box position
            boolean[][] v = new boolean[4][m*n];
            Queue<int[]> q = new LinkedList<>();
            int[] tbs = getTargetBoxPersonPositions(grid);
            int t = tbs[0];
            int b = tbs[1];
            int s = tbs[2];
            boolean[] ns = getSPositionNextToBox(grid, s, b);
            for (int i = 0; i < ns.length; i++) {
                if (ns[i]) {
                    q.add(new int[]{i, b});
                    v[i][b] = true;
                }
            }

            int moves = 0;
            while(!q.isEmpty()) {
                int size = q.size();
                for (int k = 0; k < size; k++) {
                    int[] state = q.poll();
                    if (state[1] == t) {
                        return moves;
                    }
                    int[] newState = move(grid, state);
                    if (newState != null) {
                        ns = getSPositionNextToBox(grid, newState[0], newState[1]);
                        for (int i = 0; i < ns.length; i++) {
                            if (ns[i] && !v[i][newState[1]]) {
                                q.add(new int[]{i, newState[1]});
                                v[i][newState[1]] = true;
                            }
                        }

                    }
                }
                moves++;
            }

            return -1;
        }

        int[] getTargetBoxPersonPositions(char[][] grid) {
            int t = 0;
            int b = 0;
            int s = 0;
            int m = grid.length;
            int n = grid[0].length;

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == 'T') {
                        t = i * n + j;
                    } else if (grid[i][j] == 'S') {
                        s = i * n + j;
                    } else if (grid[i][j] == 'B') {
                        b = i * n + j;
                    }
                }
            }
            return new int[]{t, b, s};
        }

        static final int[][] DIR = new int[][]{{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

        boolean[] getSPositionNextToBox(char[][] grid, int s, int b) {
            int m = grid.length;
            int n = grid[0].length;
            int bi = b / n;
            int bj = b % n;
            int si = s / n;
            int sj = s % n;
            int count = 0;
            for (int[] d : DIR) {
                int nr = bi + d[0];
                int nc = bj + d[1];
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] != '#') {
                    count++;
                }
            }

            boolean[] result = new boolean[4];//l,t,r,d
            Queue<int[]> q = new LinkedList<>();
            boolean[][] v = new boolean[m][n];
            q.add(new int[]{si, sj});
            v[si][sj] = true;
            while (count > 0 && !q.isEmpty()) {
                int[] p = q.poll();
                if (Math.abs(p[0] - bi) + Math.abs(p[1] - bj) <= 1  ) {
                    v[p[0]][p[1]] = true;
                    if (p[0] < bi) {
                        result[1] = true;
                    } else if (p[0] > bi) {
                        result[3] = true;
                    } else if (p[1] < bj) {
                        result[0] = true;
                    } else {
                        result[2] = true;
                    }
                    count--;
                }
                for (int[] d : DIR) {
                    int nr = p[0] + d[0];
                    int nc = p[1] + d[1];
                    if (nr >= 0 && nr < m && nc >= 0 && nc < n && (nr != bi || nc != bj) && grid[nr][nc] != '#' && !v[nr][nc]) {
                        q.add(new int[]{nr, nc});
                        v[nr][nc] = true;
                    }
                }
            }
            return result;
        }


        int[] move(char[][] grid, int[] state) {
            int s = state[0];
            int b = state[1];
            int m = grid.length;
            int n = grid[0].length;
            int bi = b / n;
            int bj = b % n;
            if (s == 0 && bj < n - 1 && grid[bi][bj + 1] != '#') {
                return new int[]{b, b + 1};
            } else if (s == 2 && bj > 0 && grid[bi][bj - 1] != '#') {
                return new int[]{b, b - 1};
            } else if (s == 1 && bi < m - 1 && grid[bi + 1][bj] != '#') {
                return new int[]{b, (bi + 1) * n + bj};
            } else if (s == 3 && bi > 0 && grid[bi - 1][bj] != '#') {
                return new int[]{b, (bi - 1) * n + bj};
            }
            return null;

        }

        public static void main(String[] args) {
            //char[][] grid = {{'#','#','#','#','#','#'},{'#','T','#','#','#','#'},{'#','.','.','B','.','#'},{'#','.','#','#','.','#'},{'#','.','.','.','S','#'},{'#','#','#','#','#','#'}};
            //char[][] grid = {{'#','.','.','#','#','#','#','#'},{'#','.','.','T','#','.','.','#'},{'#','.','.','.','#','B','.','#'},{'#','.','.','.','.','.','.','#'},{'#','.','.','.','#','.','S','#'},{'#','.','.','#','#','#','#','#'}};
            //System.out.println(new Solution().minPushBox(grid));
            Set<List<Integer>> set = new HashSet<>();
            set.add(Arrays.asList(1,2,3));
            System.out.println(set.contains(Arrays.asList(1,2,3)));
        }
    }

    static class Solution2 {
        public boolean isPossibleDivide(int[] nums, int k) {
            if (nums.length % k != 0) return false;
            TreeMap<Integer, Integer> m = new TreeMap<>();
            for (int i = 0; i < nums.length; i++) {
                m.computeIfPresent(nums[i], (key,v) -> v + 1);
                m.putIfAbsent(nums[i], 1);
            }

            for (int i = 0; i < nums.length / k; i++) {
                int s = m.firstKey();
                for(int j = 0; j < k; j++) {
                    Integer c = m.get(s);
                    if (c == null) return false;
                    c = c - 1;
                    m.put(s, c);
                    if (c == 0) {
                        m.remove(s);
                    }
                    s++;
                }
            }
            return true;
        }

        public static void main(String[] args) {
            Pattern p = Pattern.compile("aaa");
            Matcher m = p.matcher("aaaa");
            m.matches();
            System.out.println(m.groupCount());
        }
    }

    static class Solution3 {
        public int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
            char[] chars = s.toCharArray();
            int max = 0;

            if (minSize == 1) {
                Map<Character, Integer> map = new HashMap<>();
                for (int i = 0; i < chars.length; i++) {
                    map.computeIfPresent(chars[i], (k, v) -> v + 1);
                    map.putIfAbsent(chars[i], 1);
                }
                for(Integer c : map.values()){
                    max = Math.max(max, c);
                }
                return max;
            }

            //int[][] hashes = hashes(chars, minSize, maxSize);
            Map<String, Integer> counts = new HashMap<>();
            for (int size = minSize; size <= minSize; size++) {
                Map<Character, Integer> map = new HashMap<>();
                for (int j = 0; j < size; j++) {
                    map.computeIfPresent(chars[j], (k, v) -> v + 1);
                    map.putIfAbsent(chars[j], 1);
                }
                for (int i = size; i <= chars.length; i++) {
                    if (map.size() <= maxLetters) {
                        String sub = String.valueOf(chars, i - size, size);
                        if (counts.get(sub) == null) {
                            counts.put(sub, 1);
                        } else {
                            counts.put(sub, counts.get(sub) + 1);
                        }
                    }
                    if (i < chars.length) {
                        map.computeIfPresent(chars[i], (k, v) -> v + 1);
                        map.putIfAbsent(chars[i], 1);
                    }
                    map.put(chars[i - size], map.get(chars[i - size]) - 1);
                    if (map.get(chars[i - size]) == 0) {
                        map.remove(chars[i - size]);
                    }
                }
            }

            for(Integer count : counts.values()) {
                max = Math.max(max, count);
            }
            return max;
        }

        int[][] hashes(char[] chars, int minSize, int maxSize) {
            int[][] h = new int[maxSize - minSize + 1][chars.length];
            int[] power = new int[maxSize - minSize + 1];
            int p = 1;
            power[0] = 1;
            for (int l = 1; l < maxSize; l++) {
                p = 31 * p % 101;
                if (l >= minSize - 1) {
                    power[l - minSize + 1] = p;
                }
            }

            for (int i = 0; i < chars.length; i++) {
                for (int l = minSize - 1; l < maxSize; l++) {
                    if (i + l < chars.length) {
                        if (i == 0) {
                            h[l - minSize + 1][i] = getHash(chars, i, l + 1);
                        } else {
                            h[l - minSize + 1][i] = ((h[l - minSize + 1][i - 1] - chars[i - 1] * power[l - minSize + 1] % 101 + 101 ) % 101 * 31 + chars[i + l]) % 101;
                        }
                    }
                }
            }
            return h;
        }

        int getHash(char[] chars, int start, int l) {
            int h = 0;
            for (int i = 0; i < l && start + i < chars.length; i++) {
                h = (h * 31 + chars[start + i]) % 101;
            }
            return h;
        }

        int matches(char[] chars, int start, int l, int[][] hashes, int min) {
            int m = 1;
            int h = hashes[l-min][start];
            for (int i = start + 1; i <= chars.length - l; i++) {
                boolean match = true;
                if (h != hashes[l-min][i]) continue;
                for (int j = 0; j < l; j++) {
                    if (chars[i + j] != chars[start + j]) {
                        match = false;
                        break;
                    }
                }
                if (match) m++;
            }
            return m;
        }

        public static void main(String[] args) {
            System.out.println(new Solution3().maxFreq("ddcbdbefcfdeecbebfdfddebeabedfaabbfbecbafeefffadadfdcdeeaefbdfdfddcbdafafafbaebdfaabccafafeaaccaecbdfdeedcebccdebafadaeebfbeafecefebbeafbdebfdfcfedecedfcedecebafcefdadafeffdbaccbaafbdeaecdeabbfebfbbcacafbadbdeaddfbebfddbadeddcaacecdbddffbeffccabfebdbdbacfbbefdcbcccadfdabdaacceeeaedfafcbafcfeeafdaaebdfabacbfbddaeaebdecedbefcebfeaaaeaabdaefaceecefdcaefabffbedebfefadeccfbadcadefaaadbbbaeecbecbfceebfceaefcceeefbaaffacaacafbadbccabffafecadadacddaffeafffbeedfacefcbacbaecdfbceeabcbcfaefedcafecdaedeacefddccfbbadeabeafecbaacfbaccddecdecfbbbfbccdfaceeaccffffbaadcafdbfbafeddbcafacabcfbeedbfceebecababebcbdcfecfacefbbddfbcdbbdbafedbafddaabbedecffeeebdcafdfaafcabbafacbbaacfabaecffffbfdcceaaccdafbefedfbebaccabbfbdafcaafecadbcddedccbcfdabbccbadeadbaebadbdfdedaffbaebfcccadaaabfdfccadbedaadabdcfadbebcfdcdcdfdfcffbfacdadcadfdaebfbbecaecbadecabaaeabddecddafbcdbecbecdcadaedecedbbbcfdbdbffdbccabedefbffffbbfadcecfddcadadaebddafafdcbabababbadfadbfbfbbaddafbdebcfffcacbfbdadbfaeabbbedcabacfbfeffcfbeaceacfbecbcdfcedcaefbfdefbbbcfafcbedfdabaeebebbcbdacfdcadedfffabbaeabebceabccddfeecbefbccdfcafbcbedaeabafeecdaefcaccfcfdedeeebcacddfecdacabffaaccddbeffabadfafbdcddcfbccdfbcdccffcfffcbccbbacffdcdafddcccbcfdfceaddacadebbcbbebcebddcfcfecfeadcccfdcecacbcfccbefbcaaddedadfaabecadbaafafcadbeedcafeabdceecaeffccdcbbcdfafeabffeccddecbaeaefbdbdacffeabdfdfbaecfbaafbcecbecafbfddaddeffbddbaeabddfeecdefacbedeadcefefccfebbdceaaacbdbcaebefbeebfccccedefbfeafefbdddbdcceadcecedcdcbbcaafbfbedfcebebfdbfadfbfeddfdfdadfdeaffdcdddfedfbebafaeabfeadedeadfdcaceeaacadadefbbceacdadbdacdfefcfcedbccfaafccafcffdcebbcfcbfdfcfbbdbfadcefdbbcbccbcfddacecacabdcecffabbbbffeacbfecabddeffdbdacfbdeaccffdbdbaeeafccbcbdfedfccafebbdccbdfabdfadaadeaadfddcefddcebdfebfadbafcabeabfdbbbeddafaddbebffebacfafcdbaafdaddafebdeebabdfafddececcdcdbffdafbbacfdbcafcbfeabaaedccccdcdaeccefcccddbacbfaefedeadbdfcbabbecafffecbfebafcdafecabedfdcbfaedbecbedeafebeaaebafbfddeeceabaafcaddadbfabafdadbaeccabbeeecefcdcbcfefaadadbbfaeafcdabddadfbbaacdbababbacdbabafcbbaeacaddaeadbdacffabfdaeeabcddadcebadbedaccfbcaddeabeeeefeaaeddebfeedffcbcfacdbffeeaeedafdacdcbcecfbdedacafebffdefbbbeffafcfeaeffddbdadcaaaebebdcedafaaffcdcfaaefdaffeadeeceadbceaabcdfffebebeaedcdffdffcefcbfebafddadbdbcbaadaafaecbcbbddecebbbfbcaabfffcaaaeeaacdcdddebadeafdcececcbadaaeedfabcfaaefefbaecceffcccebdcecdcbeefdbaeeaebaadafeedfeddeefefbbfeefbcebafbccebacdeacafadecbccbdbeeecfcbacbfefbbfedfbfaecdabdbdfceaeaafbaadacdffbceecfdbabeccdcfddbeaecfcabfcdeeffcaabfbfbedeadfbcaabdecccacdafcbaedecbbeedbbbfffdeceeacffebebcbccdecbbfbdfbcdacdeadbadfacdcffefcfbffeebeadbdacecdbecfdfefbaafeefbfebffbcdafefeeecfbecbffabccebffbdeddbaaecdcbcedaacbcbcffdaebbebaeccaffbfefeacdbddcbcddccadcecdfdcadfffefffffbfbbccbaacbeacedefbdeabfdecdababfffcbefbebdcfedcefbeaecacbdecccfefafcbbbcfeceeecaafdffbbcbbaaefbfacbdbcdbadafcdcabbefdfddbeadbdccdadedabcaddeffcfaeabefbabcdabbebeedebbcdffbeafefebbecfbabbedaeceffdfdbccfdadcabdbfdceaedddddcddfaaecdecceffdeebabdefdcefdeafaaedcebecdbfccaadedacbdbcbddcabbddabfccffabdaefeeeecdacfafbfbfdfaeeecaebcaeeffbbeececfdecadcadacbeeabdcabdaffcceeeceadefcfdcdfdedacfaafbfffdcfdebddbcaffdbeffdbbacbfdfdeccfffddbfbbcadeaeacaafceecbdcbcdafbdeeceeadcccaacfbdaaceafacaafeacaeefdbffcdcaccbdcdfddbccfcebaceecafdedddfdaadfdcfdadcfceececcbbfedbcfdfadcbffaaffadeffaaeabeebeeafdeccfddbffdeceecbeeccddcdecefbdcaeebbbfccbadaaeefcfbaebeecafafabdffbbcdebccffcbfdacabcbbdaaedfaabcaceadadabedebdffcabdafbdfcabecfbefdeeedbfedfdedffeffcdbcbcefdbadafdfdbafebccffeccabbfadebaafcfdbaadabbfaadfcfcedbaabdecdebeecbbccddbdcabafbbfaaddcedabdcadebceffececbbcfeccedcbdadaabecafafbbaabceafabaffcccffdbadfeccaacdcebbadcccabeabddcbedbcddcfcbbdbcaaecdbceafaeafadbdcabfbbbeeaedefdcbfcfdacfadbcecefdcebdbbedfacebccdbaaafbfecebffdbbcbbbbbacaaabfdadfcafbeeedfadedafecdaeeddeccaaebcefcafbcbaeacbfddefbafdeebecbeeacdacfcbbaabbadecbabbbdceddffaecdffafbefdfdaaeddbbaecbdcbdfdebdcfcedcbaabdabeacfecfadbdcdeccfbcccfdfdddbdfbebafccdefafaecdafbbccefbdabfecaeabefccdccfdbdacacbbaedefdaffcadfacaedbffbdeddfaddbbfeaaddfedbffcbaecabbcccecdbbadedaedcfbfceebedaaaaaeffaedeaaccebfecfedeffcdbdbecafdbefbcaefcaabbdcecdbbdaabcecbdccbdcecaebfbefeecaadebdecaaeedfdaafaacebeecfddfaebdedeeefddbeefdebadeeedfdcefffcecacafeacedebedecfbfaedadaeeddddcfebcdeeeceecabdcfdcbffdbcddbcdecdaabacacaacadcfcdfbbfaffaebaadcabffeccfeadaddacadfbaaaafdbdacaecababbfbedabaadbbaeffdfdebaecbbebffdffadfbbfcbefeffecefedefccfdabbcecefbdaebcfeacfbfacfefffcfbafaadfadfaabcaecaadcbdfbcbdedddfdcabdfcebaeafecbbcabdbfbecebbaadbbabaedbffabcbcadbeacddeaeeedfeaedcaeeaecabbaebddbdbedbfebaeddabaceeedffcefefdcbcdfaabdaebdfceccdbbffbeceaddafafbaafedacfafefdbbbbabefffeebcaafdcfccfdaffcdcbfdfafffeeeddbbbfdbadabaffaaeaacbddccfbdeddffdeafdebcfcfbaffdfdaeefdfcbbeffcdccdccccebddcfcacdeeefdbcadafbdaffddcbabddabefdcefabfcfaaaaddadebefaafacbdaaccbbbcddbdaaedbcefcdfbaaaddfbddbecffdcbfbffdbeacfccebeeccbbadccebeabafbbfdbfbaddfacaacdabadfebdcbcadceadebffabbaebfcecafcdffddafaccbbfddfbfbdbdadcebdccffacdccedffedccbeebecfbedcbfeefabddebedaebcafbebdddafaacfbbfbbbeeafdebccfadcdbfaeffcfeefefbfacbabdcdfdbbfcbdfaddedfeaeafbebafbecabfdaecafdebfcdbfbecefafcbbbefeeadaabcbcafaabeffbbedeeedbfdcddbdbcefeeecaeaecdbcfefdfdbaafeceeefaedeabfadbffedabebdefdbedbebefdfcaccbbbebdbcfbecbfaccbdbdadacfcdabeebfdafdbcbedaabdbcadcbccaecdfdbaebeedbedfffbbdaeacdcdffededfedbbecebacebaccdeeaccfbefecbdeccebfadcecbcaffdccbfefbbdadafcacecdcfacefaeefacebebbfccbabafadafcdffdedaacdcbbcefcafabebccddfbdfbadcaddbabfaeebdaeffeccfbddcabefbbfaeddceeabcecbfbcffaafaaeeaacefaacddceebefbcfdfeafbecfdcbeedcfbdebebfdcfbafcecbabbcaedebdbfbdbfaabafccaaefdefeebffbefdffecceeedbccdafedddbafcacaacdbeceadfffffacffdbefcebbbafdadfbcefdedbafbceeffdbaadcebfabfbafededaccdeadaffecebdaaddbdddfaebbbbdfcfaeeefbbcfafeaddddebdeecfeaffdfcceaefbacbcdfabadfeebbfdebafcefedcbdfbfdbffacebbfbaceccaeccdbadadebbecceaecaecbafafabeeedcbadbeaccabedadbadbfaebdabfeebbccfccfbbfdebbaaefcdcfebcbdcaffaadabcaccbefaedeefeefcdedefbbabfbccdfdeaeeaedaddbbcaccefaddaccbeecfafbdecbbbebfbdebaadecaabbfaaecbbfaefaffdffdebabebebecfebeebbcaeabdedeafaafbbffdcebaeadededefdceefbccdcdddfdbcbeeaecbbaeeccadebddebbabfaedafbbbdfbbebebafecbbeaecddefffbafdfbcafecfdcebfdaabfdfdbabbfadbfcddbfcadbdeafadcdfbbbaeeaeadbeafbdfafabdfdcfbfeccbbdaaaafccbbdfdaeacdaafdaeebeeaeabdbcaddadbfeeedeaacbedabecadbddedddfdaabbbddfbbccebdfcabebacaaffffbcefbceaacaefaeccdaaadbbeecedadddbbbfefacfcefdcebcdbcafbbdefafcecbeaddadcbacdfcfcecbafbeaecbadaebbbfdbaaafcffbcbbfeabdedeecceccadbedbaaebbedacaabfebebfdaebbcfdffbeebacffcdcceaabfcadcecdefdfbeeffaeffeddadffeafcaafcecbffcdddebfdefebfcabeedfcccebfccecabbeebebedcdfaffacddddefdaaeffecdcadebcefacafddddfefbdbfdbacabaaffaefbedbbfeadfaaecfcffcbadcacecdceacadcbcbdcddcdfceedbccccdccffeeddbcdbdbdbeeeeaddcbaadecaceafcdceaaacdaddeeabfaacdafefabbabfdefbfbaddcecfdebebedeeaddbbbcaafdfdadfdbaffacfeddcbbbbcaeeaefeefdcadafbecfdcbeebfbabebcacbdbdfbfcebedcfbaafacbcabbdecddeddbacabdeccebddbbadbfafaceeefcfadcaaffddcfabcefdcabeccaccdbccddaabdadfaccdbdafeefdbfbdaedeaaddddffcdceabcfefbdecdfafabebaeccecfefafdfcebcdeaaabccddaddefefeafcabccfcdbcabaefefceabaebcafdcdbfbbcfbdcceafeffbaedeeecbbfaafccfdbfdbaafecbfebcfcfedacdafaffeaacdddfdcfeacfcaadaaaeddceeadbabbeecedbebfdfacecbfbedfebcdcaeaaebebdddebdbebfeecbabceacfaddeebfbfeaabcdbfdcbeaffdaadccccbfadeceaeecfcdeadfceeeafeccafcfaaddecbbfceebddddefbccdedcaebbccaeabadadcaafccfffcedadcffaacdebdcdecefbbdddbdaadbadcdcdfecbefeedbdfbbcecadeaacdbeedccdeccdbeebeabdfecdbdfbabefacbffddebfdfefceebeccecebaeaceeabcebadfcecbbccaebcfafbabaaaabfbfcbdeafcacedecebaafabeeffaddcdfdcafddeefbbfeccdceddbacedcabcfcbadfdbebeabafcfabbabaeaefbefabffcfcbdecefadfebececaecccfefaecfdbabffefdcceeabbfccdbacabebaffdfacdccfdceeefecaddddbbdcdebfafebcfcccceadaaafafeeebafeeccbeebbbacaeeebadfbacbddeefdabfcfdfdabfbbabcfbbcffcfbdaacbffbddbfbbbeaadeedfcdebacebdcefdcbaceedaaafddbbcbdffcebddbafccfefdacbafedffaadbbebbbcabfaabdadabedbdcfaddbcddadafeeffbbdaaecddaeeefffffadaafeebbbfbbceddefcefbaccdbcfdddaeadaefcdfabfedafadbcddbefababbdadbddfccfafdaedcfeedbebccebcfbcaffcadfaeccafcedcffbffdccfbcefcfeadeaecaafbfebafdbadbedaecbdedebddccefacefadbcddeeebecaedfcbadedccbaadfefcfdeeeebcbdecfaeaffabedcebeebfbdebdfddeadcaaebeabfafececbdcdfdaeafbaedabbbfcabcfbaaacecdfbeddfaadbebfaebcedeeefaefbcadaafadaeecccfacbdcaadecdaebaefdbeccfabaabffdeaebccddfaeeabdbccbadccceedefbedcbcbbddaddaccafeacbdcabeeafddabfdafdbeafddeedccebdeabecbaddbafbabaadcaffedcfcdedbaacccebffcdaecaaadddfbefebcadfaebfeddbfebdcbebfccdcedbdfdbbbcebcdbadffaabfedbdcedfbcaaffdeecbaddacbcfabbccbefdffeedbdafdebebdefdfedfefecfcacaeabeffbfaaacecffefacaeadeeaecbaedbcfdcddfeeacdceedffbacffbcdededfcabbadfefdfeffeeebabcfdbacaafdcabbddebbeacedefbdcffceefcabefccacddaebaedcfadeaedebdcaeabcabfbaacfbdceecfffbecbbeafecdbecbfddefffdaecaccbedcdccfaefedcecbbceeacfceffafffddfabeceddbcbedbbaecaebbbcffddcddfcffacdcdacfbdcebaaddccbdfcfcdbedbcaeaeacfbacbdcdeaaaabcfbdcdabbfbaeebbefdddccefaeeccceddedbdecfdadeeebbaacbafcfabcefdcbcfcfbbffcfefcbceecddcaecbfefaeeadfaadecdbfabedcecfafecaddadddbefaabfefafcadfcccdfdabcecebdceaeecebfecebedbcfefebeeeeadafbedaacfcfeebcccfacedfbaeaceaadceaeaddacacddeeefbeccebeceededbeffbcdfcebbdfcffacdaafcdbfdbfbfbbcadedebfedcdaadfcbbfccabebaebfafcbaedfebcdcedacbdbdcdfcbaffdbcfedafceaceeefecfacdebdcacffecfaceceafdaaffaecfdecfbfbbecbebcbdcaafeffcecffcadffdbeecbefdaebbdcfedededcbafbbfeaecdbfbefebededabbdfeaabccdecebcedcbffbaaefbfbbddafebcaaaabfdbebcbfabdaeaacaffafdedfcdbbcfbafaaecefcacdbaddbddbbdeecfdebacfafcccebaffcaacbefbaaddeddceceedcfafebdfaaeadfffdfbbbcbbceeadbefaaecdccebdfebfbabbadeadfbbcebfdacffafcafccafffddceeaffbaaebfcbcbddbddbbdbffceaefaeeeadabbbbfcefbcbaadbaaebcfebadcbfebcecdeeccfdadedaaebddfffafcdeffbcbeaebeabecbcbcbbaebccaadccdfacaceddeccaeeefabecdcaefbfbfbdededaeedafcbdfbcafadfbfefbebddafdbefcdaaccffbbbceccdfeccdafafeafceddcebaefdadbfededaecddedaefbcfaafdadeeacceebceaafddabebabdcfeeebcdacfdeeaaccdceebaaafafeedcdcdafbbcfbcfaefcfadbaefdafcbadbedaeebdbfbfcedefbbccbbcadbeacccdeacffccdcaecedacffbfaebbcacddebeecfabdebacceeabdeddbdedacbdeededafefccfefebdcfeeeeffacaedacfecaecacdbaafbdcfbaddfdfcafcceefbceebbadeafdddcbeacedebadffffdbfffbefabfcdeacfcaaebcdfbbaafbddcedfaaddefddfbacbddebcdefcbededbbfbefbcccbbcefdbdeeedcddacceddffaccddadebfdfaedcbdbcdcbfeecdfdfdecdfcfcbabccebdcdbdcfbabbbeabfafbacbccfcafdbccefdfbfbdcfaacbfabdccaafccebbebddffefcfadaaeeceadefdadedebcebafdebcceaccabceccbbcecdbbbceeaadcbcaffdbaddddcbddfddcfaabbdceabfbaeeacbceffecdccccabfdfcffcceccffabddcfdacebeebeebfcdedfbbaecacfcbeaecafcabdbbfafafccfefdadbbdebaabdbbadaeecdabfafecfaddadaeffbdbccbfeaffbdbcedfdbbcbffaedbffbdcbabeceefebbefdedfadabeffeebeeccfefbcddeafdeecfdeefafdeafaccfcbcfbacadbbbbccbceabcbfdfdccfaaaffdfdddebecacbbecedcdbdbfaebbefbedbacabceffaaaffdbaddcfdeecdbbdbdbcfcccebddbbabdcaeefeaeebeecaeedfefdfcfadbdffcfbdfdddcdeafafbaadaabdaebbfeacbfcbfdcacbdccacbabdaaffddadcddcedbdbdedafbdeffddcdaedefafcacabceccadbefcfcaffcdfcceddfaedecfebbdeefbbcfbbeefbaebcdfadfcdadecbeafdaaeddefbffccdddeffcecccbedcdddbedcdbfffdebcbdfdbdbbbdefeeaceaaadcdefebcadaedddeabbcdedbeefddafecefebaeebdaadbeffdfcecafcbaddddbeafbfbadedaeccabfcabeaadbfadfafbaaeabbcbbedaacecedeaceceaaafafddeffdcbcfbccafacbeccefbccfdefaaadadedeefaafbbcdddbbcfebecadfecbccbbdebcefdaffafdfddeaecfbaabdccdcaefabcaddbdbbeebafdbceddcffafbafbcaabecbeccadfecafeabfaffdcfbddeabdecaeadefeaecdacbbfedcefdebcffecdbcbfccfbabfbbedfedbdfaaeafeccebadbfdacadefdbebaecceecdbfcdcbfaadddbebcdcfeecdacfebacadabedfccaeecafafcdddfedadfefbffcdbdbaacadafaebdefadfbcdaccaacdebebaaaaedccaaddaaebaafffabdfdeeedbadeadebbebefeeeedfdbbbcccbfbdaecbccaaafedffccaacccfbfdeabfebffedacaabfcaacfeaafdfabbfeaefacbbfccacfbdcfadcccbefbafbfddaceefdcdcdfbcfbbbbbfdacfefffcecfffcadfddcadbedcbdfffabdeacdedeeaecddfcafdbedecbfebcacecacbdddfefeeaebcffadbaadcabdcdfbdddafdecafbbefdddbbafcfceebacaddcfdabcacaaddfbabfaaddccaaaaebecfdcaafbcfaafdeffadbdebddfeebafbeeccbceeaecaeacfbebbedbffefbdcdbdfcefbeaebaeabababdddbafcaccbcafcaefeccefeafbbfcaeabaebcaebfbfeddfebbcfedcadefcfbedbcdcbfdaecdecdacadcebdcfbcafbcfbadabdbaccbbefaabcadbbbcbecdbdcbeeaeacccacbeaeccbedfdffcecaabdeabefcbbffbbcdadaaddbaabbedbfeeaaacdcbeddcafaacbabbecffcfbcadcafbedccafccccefbeebffbeceaacfaeaebeabcdbcbeefcdadbafebaecbecbffafeebdcadbafbbaedfcdcfdfebdaadcdcbdeccfaddeacabffceceafeffecdcddfbbeaaeafdcdadaafebaffafbdccdaccfddaccfcfccbfefbcafdcedfcfccfbacfdecaffefbaeddcafdbaabfdeacbeccbddddabfdbaabfcfcdcfdecfcbfbeccdcebfefaeccfedceafbbfccffbcdedebbbbeafaefcbcfddddfccebeecfcccadbadbbfbcfedccddabeeabfafdabdcfbaecdcfacbecdbdddfcdabefbaaadefbbbeaaabbafbbdbecacacfcdbedffaffeeaebddeecbaacffcbfdaebcabacdadcfeaaaffdcfabeffdabdefdcbdccabcacebeafbcdebfffdaccbaebcedeccdabacfbaafdcffbcffafdcabeaddafaefceadcebcbbcedfdeffafadaeaabcfefefbbeaececceaeabeeeecbccaeddedbcdfdbcccbdfcccaeeddcdfbaccefdebfedfcccfedcdffeefffeaefdbbebbeaaebcebedabaeedefaeacabfadcdacbafeaebdffecfbdbafedcedbeccbececefddcfafbabccefadfcceddaebebeccfcafadbbffdcabfafbeefaaddecfdbbbdabfdfafedcbdfbeecdebddcbfbeccbebfddcffdfbbbbfaeeeaabcdafafcceaceccbcfcebeeaadbdfbfcaeaefceddabeadaacceecbcfcbedcaaabcadfcdadffdfabcdeceadddddadadbbefaaceecfcedeeabaadbadeedbeceaefdbbfdbabdbdeedbeabcbcefefdeaeadfafbebbefedcafdfeedcbfdbbebbbeeecbeecfadddcaaadfcffcbfcbacbcdcfdebdbdfedfeecabfbbbaebffebdedfeefeaebfccfeebfcafbcaefeafdabeddaedbaeaacedbbfceedbedaaedcdaeddfeddcfaabaedafeebcdcfffecfeccbfaacfbcaaeabfafaafceefccbceeebeadeecbececffffcaaeaebabfaeebfefcbdceabdcddfcdecfadffcfaddabcaabdeecacaacacdbddeecfcdaddebaacdffaddbdeccbebdcecfedddeabfcfedbdfdecbffbebacbecddfdbfabbabfcbafbafcbfacdbdcafeaaecffeaacdbcceedbabacbefbdddbaedbedccbcebddbdcaadbedeadfefcdbeeaaefdbaffafcedcfebeaadceacecddbffbfcbeebedcacaffeddacdcbfcafbdccbbbbafbcfcbbbbeabaeabaececabacbdfcdcceeaecabddabfefcbcbfbeeaccabcdeffbbfebffdeffefeffafaebaafceffaafabfdbdbdafdbdfdeefbffafcfdbcaeadbaddbeafbfafbcddcdfbbecffcddfeadaeeacaefecaeebbfcbedbacddbbcddffcffaabbfccbfdedeaffebccbeeebaecadffcffcedeceeadedeaceecdbbdbbcecbcfbbfbbfdddfbecbfaffebefeeeceecfbdfadcdfbfecefddabfafdfcabdaefbafebcecbdfbcdcfaebdcdbbbcfaccddbfbffbbfcefecabbbcacceaaaabbdedeefaafdcddabafbfabcfcabdfbdcfdbcbecdadbdffecdcadddbacfeeedcadecbdebfcdbbeefbcecfafefccafefbfeedcaffdccafbdfeadcbbffcadeeeddbacbfdeceeaabaecfbfcbfdbcdffcbabfbfdfeddebcbafdccecfbbafeedcfbbceedecdbeabbacfccecbdffcfbdbefeccfbeaefeabbfacecfedfcfddfcadefccfaeabaddcaddaccaecceeebcceeefcddfccaffdcdeaabdddadaedbffdebaeaafdedddfdececefbbccfccabcacdddcbaeccafadccadffdefabcfdeebbdadbeeeedaddadadbaaaefbcfaddafdabfefeefeabefbebbabddcebecaebcafdceceecbebbddbbafdacaebfeeaebfdffcaabdabcbcfebdcbebcbebfbebbcaabdffdefeedcffaedbbcbcbbcecefbcddabfdfcefacadeeadbceaeabadeecbfbfaaecaedbbdcfffecafecbbcafedfbaeeeacdafbccdaabebefbefdadbaeccfcddfecaeffdbeefbfcbbdefaeaebccbdfbaefadbefaeebeacfbbaacafadbeccbdebdacafaebbfdcbdcbceecceebfffacbddbdddfefeabeddffacadbbefeeeeddcbfecfbcbdcbfeeebeaefcdbaadfadeedebcfbfbfdbebfecacebbffcddfdbfaaaeeedcbfccbccbacaddadadbeabdffbcfccbcecdecddadadcddebbbeaebefdababdddabdbcadacffffabddfceeceafdefacbdbabacabeaceebaffaadbdfdddbcecdfbdbceeabebdccbdaeeaacefddeaeaeefdcfcacbedceedfabbfaeafaebeccbadefffdebfffbebbceadbdefdcdbedaffeffceeddcecbdededeccdecafbdbadcdabacbfcddcdaccfcfdfbbcacabbcaeeaceceffceefdebafacebbfefdfebaebbaccbaeeccafedfecabebbfdccdcdabccaadaffecbabdaccfdabffdbeecdfffebabacabbebbbfffcabcddfeadeecdfbeeffcffcfdfaeffcaeeeaacabecdebcbabefbbabdabcdceebabaedefdfcfeaceddcbadfaecaadeabbfcfadaeeddcdbfddafcceecebacbabddbacccdbfbaedbeffafcffbeebeabcddbcefbffaebeabcfcdbbafcbaadddcafafceddfecfddecceadbfafdcaefcbcfbaafacbcbcdbadcbbbdfbadcdffcabdbaadfebafdfffebddabfcdddfdeebaafcbefbdddcdeccadbebacbfeeeadafadbeecaaefaccdfebbeddffcebfdbaefaabdbcacfebdcdfbccdffafdafebabadfbcdaccbcdffbeedbccbaecedebffbeafbfcadcdcfcedbebacaaacaddedeffbbebbfdeffacffafaeffefebadfcefadcadffeddbebabbefbcafbeecafafafefdefdefdbbfbccdeadebfcfabcfafdeaebbdcccfdddbebaddffbefecacdeabdfafbcedfccccabcedddfdfdeafcafebdccddeecddbbcfbceffafafccabfddacdbdaeffafbebdafecfaddaebaccacdffbeddbeadddbefdfcabcaddfcaccdcffffcbfdbcedffddacaabacffcfccbeffcfdfcefebfbdcbdfafcdaaefdebdcbcbcdeedebadebeabbbbcbbcafadedcbdaaeddbdbbdbaadfcbaaacefebfecbdebdbcbdccffaecdfdfffcaadccbdbfdabcacbacfcecbaefcddffebaffbceeaedefaecfefcddceaefaeadedffcccaddacedcccfdbeedcbaacefcccdffeaddfaabfcebeccacaeafcaadafcfadbdeadcbbbecfddafdbbaeaaaaffcbdcbefcbfdceeefbbefdfecdccbafdabfaccbfabaedaaeebeffffabecbffabdfdabfcbcbbddcdfbcdddefbbbfcbbedaeedebfdbbebedccacfdbabebabcbfdcbcfacfbcbffaffdefcbfcaadabbecfcbbadeaafbedeeddcdfabfacecaefcecafdcdabfebeabddbefcaadcbcbbacebbafbbbabafefbfdfadbbeedfecbcaffeebfdadafbfffababbdfebecfbaefecbdadfccdfcfaadfbfefddbfecdfedadfcfdddadddccafebdfdeadcbafecdcfcabbafaeabbafcfcebecadacbfdcbebdbaacebfaddedcfeefbdedcfeacffdbaaecdcebecdbecffdeadeacebeacabdaeeedebdbbbbaaefdffdddffeccdbdfcfadbdbebbcefdbcdbfceadddcbfcffcdbffbcbfaddaebfcabecffdfbaeabffafdfcaabcecbddcebfbfdadedbcacdfbdfddeefdafeaafafdeebcaacbafbdfedbbcaadaddbbcebcabcdbccbeabbaeecdadfcbfedaacdddcfeeeaddacabfcebaeffecfcfceefabfcddcfbbbfdbcbeaebdaaecbfadbbafacfbbbcdcdaccdeafabcbbeeefcccfeaddbcdaacbcecdaadbedbdfffdedabffceccfccddbbbecacecbdedafefcbbabebbbcdcdacaeaeddcceafdabacdbaabfafefadbcdcdddcddcbcaaabaaffadaeedacdbcedcefbbbcaacfbbecdceabcebacccccbefbafcfadfaaeebaeebfcbfcfbdafbbebeaeddbecdcbccefcbbcfaebfabfbcfabaecdbbedccdeacdfedbfcdaffbdafdbcdcdefecbfcaebbaaabfeadecaefcbaaeeddebaaafffbddcfbafddaeabcdceaedfbaadbeeaadcbcfbabcfdbbcdeddbefbdcacdaeaefeffbbfdffafcdfcddffcaabeedabbcaedaeedbffdcecefdcbcfbedaaffaafeecbefdcecaceedadaddcbbdaffcdbdefefcbfaeceaabcffafcaffadcadbeebbddddcfcefebbbaadfbedafbfacaffdffdcdccfedafebbbbbcbdbfcedfeecaafdbfdabadeddfeadbaeccfcaaadfacfbeffdffdcbfcfdfbdaecddbaeedeebfedaffcdebeacabaccddaaddcbdedabeeecbabbcabedcccabbddacdcaffbcddaddaabacbbdddfdbefdaaecedcbbbfcbfcffeaefbbbcebecaaafaedcdacfaddcddaecceefdfdcffacceaafebefadfdcaadbbbffddceeedccadaedebedacfbfbdefefadeeadfddabccedafeecafffafdcdafafdadfffdceaafefaecdccbbceaeaeebacdaefebfadbefcbdbdddfcddebbfaefbcbdababbbccaecddbadddefaffaccccbbbcdbebafefeabcaacacfaddfcbfefbecfdccebcccffefdeffcbdeaedcccbdaebcebbfeeffbdcafaebacefaebbecaecdbcfdaadeeedbdadaafaebecfdcdfcaeaafedbbebfedeafbfafdffeacfacaebeefbcbdebebfeaecfcbafccfccfbffdeffdcdefecadbeebbcbffadafbdeeaadadaeeebfaadaeecaeedffbcddddabcccfaacaedececdaeceffeaccbdaabbeeabfbbfcbcabbcffbdadffffdcfaeacadeffffafefabfbbecedeccabdcacecadbbbdcefadedcdeeefabedbcdadfdcadcfcbeeffabaeacdfaecacbcbfebdcdbfbfefccdffbffacfcacbbbbecfeeccacaceaaccfdbefaeceedecdacbceebecffbabfdcdddfcddeccafebcacfddedebdcdedacaddebceeccccfbaebfaadfccecabeafffabddaafbfdeefddbdbbbcbdfaeecdbfdeedcfedcaefedecbcfabdcbaeafcdefeaafcacfbfbeaebccacedfffbebfecfcfbcfcbfdbfafcefeeeecdbabfebaafaeecfdcedabedfadadddccdfaebdbeefcfaaadbbafcdeebabdacaaddbcedcdedeaedabcfecffabdfcdfdafecaaceecbafbccefedbedbcafbdefbdafdebaedfbdacdffeeaabbecbfcdbbfedefffafdfcfaaeafecbababdcfbadbefddcdfdaabaabbbaecebcfdcbbcdbbccdfedacfbebcfecdcdaeeebfdcffffbbdbfeeedfbcbeefdcaecfcfffadacacdbbacdfcebadadaeddfbfceedceecbafecdaaddbccacbcfcceafbdafcfdcbccdbbebbbddddddbaabdfdfdfebbdcaceaafddbbcedbaacbfcaeafffbdccfafcdcfeafcaadfeecddefaafdbbeecebedbccfacddadcbadfdaacefbddaaecddacecdbffedbffffdfdbeddfecbaebbaefcfccfbeddcbadffdaedaeebcdafbedcbbafcdefeaedeafcafbcefeacbbebcaeaeecfdedbfcfcabdffafeeaebccffbfeaafeebdaacbfffaffaeaeffbfaacecdbdebbebccdefabeeeacaeabbbedeaaddecfbdafedfebfceabbeacfaaacafccbaaffafcfbcdaffaaaadaebbcdffffbdfaaddfbcacaeedcdfaffafbddcbeceaebeeadccfdcabfbedaceceedcdabaccffddbadfeeffcfdefeedcbddecbbaefcbfdeaefececcfabebaeaaecbcfdffbdeafedffdcbabafbffddccafedfecafabbbfecebccbabdcfabbbbfcbccdabebcbacdaeccadcbddeadbcebcadfaeadaceaadeeabfeacdbffedcaaeeccdcbadffbecefeccefbddababbbdacafbccecbbafadcfebaeffcbdbdfddbcaddddededbabbaeaaacccdafefdddbccddffecaebbadbfdffaeffdadbdebbdfdccacbdeadbbcbcecbeebeedccdeebeceecbddebafeecbccfbaacaeceebccfcaaacabfdbbafbafaadfbacdbaeaeadbcdeedbdcedfbbfdaabfaeffaeafbafddfbcafedddaffdedaeaabedcecbbeebfeaadddbafcfedaeaaeafaefcebbabdebfdecdadaefdbcbaffccdafbffedfacfcacfcccbcacfcceebfecfecfacfabfeefabfddbfaacfdcabdccfbbedcacfdcbeffdebfcacbdfdebfdbccdebfddfabaceeedbddffffeebcfcededfcecedaeecfbaedfdfdafbcdadebccedcaaeadbfbdcfbdefecbaaeacfbbebecdddfcfaadddffedadacadebceaffecbfecdcaadeefccaafbbafafecdeaeedaefebbbbaadbeacbeccebdeefccccffdeafebacbefadffaecacabcfbddfdabcfebccfdabadadeebbbcdedcfadccafaadeacbdbaacaeccbafbebacdfbadebddaffbefabdefadffcfdefddefbcbfabbbbfeadfaccffacadbcdaefadccbbdeacdffcfcddadbaabefffacdfbfdeacbdecbbbeebdfdeebfedadaedfecbdbdfcfbfdbadaddfbbabccbdefcdfefceaaefdacbeaacecabfeacfebdeccdcdcbcebaddfabdffbbeebeacbddfccafeaadcdbedfdbddacbcbaedfccdbfffbffdcfaaecfcbecfccdadeefaefefdcfbbeeeacccdedbbddabbcbfefefacafedceeceecedcffbecfdbfdbbbabfdbcebfbbccfaabdabaacbbfecdddfdfbaabfcebcbffeadcacffdfaeafcdcdaecedaadebeccfdcaedeecfdbdabbebefaafecccecdeefeddaddaebbbfdafffbdedaacfcbbccdfbaddfceefaaeeadfacdfebadbcccfefafdfefdcaaebdcdfddcfbceeccbdecaafbbddabcaaecdffcbdcbffccebdeabedeccdebdefbfbeafebaedcfacfdffedaabdecdbbaadbcfcdcadfadbccbecaabcaddbcfddafcbafafaceafbceccbbabeeffefbcbdbeeffceaaceddebdffbddbbffcdcffbbfeaeebadacecbecbdebdbaabdfacefabcfbcaaaafcfeccfcbfbbcafaefecebaecafdabecefdcbccfaaacfdbfbfbbfdeadcabbafffdaddbbbbfdbbefbfeacaffeeaaffadeedfbbaecddddedceecafebdaddbadaccdffedcccdbccafedceddccfbcdaafeaafabeadfaebfeadccbabfefcadcdcaffcbfdabaacccaefccbdbefeacbaffdeadbbeeaaaacfeedededbafabdddbdaacffcacabecffbdfefeebeeabeadcefbffbeaadeaedeaccbcebdebdbfcdcefdeaddffecddfdbebeffaccbbdfebffcbeaffccdedacddcddcebaaaccfcdcbcdeddbbbbfbaccaabecfdbceebeddaffcebcdfcdfcabfdceffddbbebebcaadadeefbbafafaacfbdfcdceeefbecffbcdabfbcbcccedadacedcfbabddcecfbfddcbcdafbdceffdccdfbddfaecefbceadebfdfccbdeaefcfacfaeecbefebbffdfcecbffdabfabcadcfabacfddcfdbbaacbdcebfbaadeedbecedafedfaacdebfdddedabbfedccdfaeabfadadbabfabecbfcabebbddaebdbecfbaecacbbecaaaaebadefadcfddbefdaafbafdbfcfafffefbacebbfdceccbcfdbfffeaafcbddbaaeefaecafcabecfcaaceabecfcceafbfcefecdbfbcdeeebcfbeadbdaedbbcbfdbdeecfecbfcedabdcabafaabfecdfccdffebbedeecffebcebbeceffdcdfefcecbafcdfebdfdffefccedaacefcacdfcbdafedbedceaeccdacfedefdbdbcaabaaacaaabecdecacdbbafbeebbbbabcbffdffafaebcdcafcccceedbeacfeaefddefbfecbceddafeedbdaeefeeddfbbbcffeffacdebebdcefcdcdbefcafbfcfccafcbecfbdcdcdabfcfedfeacbffedefabfaabeeddbeccdfcdcbabeabdebdcfabcabedcbdcdfbfcdcabacacbfcadadeeaacfabfbeecefafebbaeecdaaddbddccddadeedcfcbecbecaaabccefbcdebbbacdfcceaeceaadbefebdaadecbedafaefaadbaadecaaeefaeddfcdeefabdafceffebadfdfaaecfecbbaeebdffafaacfbabdeebeaabcbdbfbebdfadbdfdbcbafccddbbaaaabbeefdaaadebbcedabcfffdbaeceefdedaaafebdfdecefeddffaeeadaecaccefeadcbbfcaacaedcedfffcfadaeefdbdaaaffeffdcddbbefeaacfadcdcbbfddcbdcafdbcdffbdfdadefaebfbfefecfcbeffeefabbabcbdafdecdbeebfffefbfedceaacaddcefefdccbcfafaabbccefabcafdbcdaccfefbceebdbfdecbbfaeafbddfbdedeecdcbdfaafdecaddbebafaddfdccfaedbadacafffdfbfadccbcdfaefbeeaddfccceecdffedfcbedccaeeabcfdedacabfadaeecdccddcbbddadacebfadbfcfacbeeccaebcecadbedcbebfcaadccabeecbaceeddcaabdbacdbdfcecacffafebafabebfcdabbfceabeebbaaaadfbbcdcdffffefdadaabacdceffbfeaeefaddfbacdafcfeddeddacafaccafacaeecbeceddbfbbbfbbfceddbfcaddcbebeaedbbbdebaecaacfabafdcbeaffccbcdaaeabeeebccbfdbafafdffcacaffaffffcaecebfcaefcaadabeaceafafefdedffbcabaeafffaffcdfdedbaabffddfffaffbbfaedefdecfefbfddcffcbaafebccbcbffdeedaeaedfccdcffbcdebffcdfdcccafbcbddeedcbfbffaddccbeeebfdebaadeabfaaaafedadaaebeddacececafddfdcccdceeffaaedadfedaefbffffadfcacbcdebfbefffcdcddcddffaebeefedbfcebededbccbbbaffddffbfafbdbddcacafbabcdfecebadfeedbffeeadaaaecaabefcddfcffdfaefdfacdcbeabbbbeccceffbdefbeecbcfadefbabbdfddcaaecbacbfaadbfbecedcfdefdededecdabcddfbecbbebfdeabcdddfefacaadfabbadfbddfafdeafcfefccbcbcdaeefcbfbcabdfccbbffdccaebdeaaabaebbbfecabbeaaaeeccbefedbdceccedcdacddffaaafafdccafcefffbdaffccdeeeefdfccdfaeeecfcefdcbdbbbbaeddddbaefcedcecedeaecacbbeddbeeaabfacbecbccdabcdcdecfeceedbbeabaccdfacefdeedbfcdebfcafbceaefedcdbabadfbaccccaefededfeafcdfebadaaaafafbdddfddfeffacaeaaafbbacceebcafabfcbefabdfabeaedbbbbdadfbaeefbbddfcfceebabcfaaaeacadfcfadaeeeacfeacfbfcfbdffffedadbbaabccdbcfbefbbfcbfdadbfdffbfaeaacabeadfcebcffebcdbafbbfbcabfcfcfffccfacafdffbbeeabddfafcefbdaffbfbcebdcfaabccefebeaabbedaecaabbeffbaabffdcfbaeebcdfecbaabbfadcfdaefebbefbaeeeecdbdaafacbcaadfeefdfafebbabcbaaaabbbcedbabbdfdeffdcabdbbefbbdcaaaaaafebebbeacaabbacdcacddaafdeebfecbffedceddddfcedbadaabcacabecfbbfdabebadbcfadcdfffeebfccccdbbceffbdebccaebfcdbdfcdeabecceeebfecfaeabadaaeacecdbadbfffcabcefdddbaabaceeacfacacebebeaceecddfbdccfaefcdfdfccaacebcebabfabffaaebedaddfadcebcdbcfacacafdfdccdabedddebdafcbcbbdeaacfefefdefecfbdaedaceccdbaefdfdfedebdecceeeddcfcfebdabbfdcdebbcccceadebffbbcaccfdeacdbbacaeadecbeebddcadddccaddcbfeeafcaabebeaebdbaedccbaeaebadaefababcbffaafeaeeaecdffecdaeccedbbbbabfeececefcdfbaefacbcfefedefceddcadadeeaadbddbdedfdffdbcececbdffdeaceebefbdbeaccaecebecfaeaabcbfabdcafddadadafebdaacbefbddfbfcfefaffacccdeebcafcaaaabdcbfdeddbeffbccdcdaddfdcaaddfddefaafcafacadedfaabefddddfcbfbbafadcecbdddefeefaaecaebfccdbfedecccabfacbccdfebcfaebebabdfbaceceeffdfcbebcbbceddfafababbddecafdaaebffeccadefafeddefefbdfffebcfedcddafdedbfffbfddcbaacfccafacccdfabababadefcbbbfcacaeeedabdecfccbedbbdbdeceeecdfdafcfbeffcbbbeedfcecbbdeebabcbebffeadfccbcceafdadffffcbacedcbbcbebebbddfebbebcdacfcbeaabbaecbbcedbaafaecfecbbfecedcfbfdbdbfdddbceabccaeafbcfeeeeaebfbfcbfcdcedefadeabadceeeaebedadcdddcaedfdcecfffaedccadeedcfdbbefaffcefccfcdbbaedbaceedeefbdecafbfaabfabcbdfbebeacaccddfbbbacabebedaeedefefbfbecfebefbdeaebbedfbedcdacbebfcafabfeffeaaeddacbaceffffdbfadcfbdadacedffbfefffbcccabdccabbeabceeddfddfbcfcfaffcdaeaaddeafdacafdaacdacceddfabfeeadcbfafddeaeaabebcaacbfdcccdfebfaccefacdefbafdabeadaacdfeceecabccdecedaadfbfaddcfceebfacbefbfbafeeeeaeeeacdfabfdfdbaceecbeaebcaaffaceacedbdbcafbafccafbefaefbcbbdbbbeeafecefcececacaafbaabbfbfcccbdcfcbebcaacfddcfdcddeabecdeddcdbbdabaddaecafeffacbcbccdfcfcbfcfdafcfcdcbadadcadedbfbcdddacfccdfaabcdcefbdbddbbaafcfefcdcecccccadeeefbeeffcbdcaaadaedeaaadccdbcecddecdafaddabfdcdacbefdfbfcabacdfdfcdbbcdfecdbdededcabfaaaeddadccaaabddcacfacbbaccadbebeecadcfcdfabaaedddcbdccfbddddffacfdfeefeacacceddfbbdbcefdcceaabdfbeeadaedffdbbafbfcaeebceffecffacfddeadeeafdedbccddabdbbccfdbffafcccdbdcfbaeceadcfecacfadbcafadabaddceaedbccebeeafbffbcaacdadceafacdcffbbcffbaadeabefaabbafebfccddbcbccadfefafadadebcfabcaedffaecdefdbddacccbbfabbdaeaddcaeeeefdcbbabddedffdbcbcdffaaeadbfbdffceaecbecaafbdcabefafbffebbbabcfcafeccbbdfbbebcfdfacfaacedbbdaeaabebdcafbeceadbbefdcbccfbeafcbcedcbddaffadeafadeefbadbccfdecdcfbbebfffdddcfdffbafebcaaecfcbbcabebbafabeecfeadaecddebaddedbdcedfccfcdeaefbeaafabbaaaadafdadccbfcffdfdabcffcfdcafafdabadcbdbaccfeabccdaeeaeeaafbbcfaecebbfceedcdcbeaabefffedbbeedfffdfefcfccbeddbdafdaceabadefadbeaeafeaeafebcefabafdeddecddaaecdefbfabdfebcfacecadcadbaecadbbecaffcbdffbaeedbefaaeeebeaaaaeaceadcbebbbadedffccefecfcdfdaadbdbcffaddcfdafcaecffbddafffcfecfbfcbefbbbffedfbeacfcaeebabdefccedcdbdbeddafbbdabadbaaccecebfabadebdeebcaabdfbcaeefacbfbccabbfceadaaadafeeecdaccededfecdcfddcaaaceefffaefecadccdfeffacfdabeeaccdcdecfdefecedbdaedacdccdcaefafcbbeeadcddaeabdbffbecacbdafddcfcfacfddceecabfbebbbbdebcafcfbccabfcdbdbfadecddcfadcbcfececbccabeefefdbefadadaebaabfbfabebcdcebfbdcdebcbaccecfcffcdfbcaaecafbbebaaedcddedbefafcfaebedaeeefddccbfdabbdfbfaedfedeaccbcdbbaaefdaecfcbedeeaeaaaefefaabafcdecaccafbfbcceeffafabefedabfdedcaebeaeecfdbccddcdcefbdacbfbdeefefcbdedfefbbbaaadbaeafaebccbcfcefbffaedbbbeaedacfaaaeafeebafcfeadcfedabfcabaffbbdafceebaaffefbcdcbdcdadebabdbeffcccfabcebeafaecccecbbadacfcacefababfabaafeecafcfddfdbdaacbdedbafebfecafcdfeccdfafeeadbbacafddeefbfbfeaabedcfbcbfdfefebfaceeaafddaeecfdfdfcaacbcbacaddbbafbeaaebdeeedabecbddfcdcdddecdafafefebfbcfdbfbffedbbfeaacabaabebabefaafaebacefbcbceeebecbdbfeeadcafdceadedeaedaacccfefcdbbcffdcddbaaeacfddddbabefefdbcffaeabcbebddccdfffdadbaadfdeedfacfedfbebecaaaecdabdacbfdfcfcdbecaedfcbfdcaaefebeaaaddaafdbedccebfbbeffbdcaffcdacebaebafcfeddccbdfbafcabceccddcefaafebaecbfbdfefdcdaddbebadbdacdafadeabbaabceecbbabfdcbecdeccdacbffdaabdcabbfeaacebbedbbdcfbaadecdfddedefddfebdfdbcffebbfdabccafcfadaceccfbaedcdfacdafeddcbdafeabeecfbfcdcafcafadceaeecbbefbbadaeeddbfebaacfbeafeabdafbafaddacbedddefaccfeaccabcdbccfbcaadffacdddddafddbbedeaeaaaafbdddfffcdaafdfdfeadbbfcbecccefcbfbfbdbacefdafedbafaeabeecfbcdbfcdfcaadfeefebdcbbcbeddfbaddecfcddaddddffbfcfcbbcdeafdddbceacfadcacbbbffeeffeeeffaaabafbfacfaefdceadafcbfcedcaffcfdabfdabfedefdbeedcedfffafecfbbbccffaebbdffccfaaefdebdeafccedebbdfedacddfaefdedbbdaddccfaabfcaaccbafcafdffcfbabbcecccdbdcabedbcfeeeeebbfcfcdeefabbaaccebeeaccaeafcdfcaecdcacffffbededfdaefccfeedbbdbfdfbeefbcabeadbeaeefccdcaaddbbdbdeeabfdeadcedfddeaffcbdceccaaaebeecadddebdbbabeabcebbdcdebafaffffafcdfbdebaaccfcfcabbeabecdfdffcbcdfbbcfadedbacfccbfabdacfcebddacfcedfcdafddfaddbeddbbafeadfdfbbeebeccedbdefebbdfddfedcaaaffdcfdefebfabefdedfeaefeeebfcfabcbdadceebdcfeadbfeeffccdbdbccadeacccebcfbcfdbefdbafcebbcacaacfbfefafcfcfeeedaaabdefcacefeaccafcffdddabfbcebbfbaeaabbfcdfdfbbbefffeeaabfbafafbdfcefedcfbaddafdddfbbebfdaeebcbeebcccecfabbedcedfcedcdfbfcaeadfabdfbeddfeffbdddcbdfcdbceafdaadbbadbecaccecdebbbbeecfdddffdeaabccedeafebfbebdbecfdeceddaeafafefcbbeaeefbcfdcfddefedffdbfbfcdaedcedbdceeaeafcefdbafcdfbaacdabbbbeebbdfabbccddefccdddcfdedbbcefbadbbdaaadcdfddeebaceacdefeefabfcacfbeffcbddfadfeefcdbcfabefffadefabcaaaadffbdecbeaddccfcadadbbdedaeeaccdcdaefadfdfecdadcedffdfcaffccdcdfbbcbccfbdbafbebcaeeaedcbedbdabafdcfbffbfdeeacfaaddabbbbcbffbeecebcacbddedfbcfaeffccdeeaabdcecfbfeecfdafcbdfebabfffebddcfceaccfdbafbcdbeebadeffedecfefeaabbcfedceafcfbcebbddcbbbeecffbecaefcdefafdaadcceffbbadbabbbbdacfefecaafacccaeacdcdbbdcbddbfaefeeefabcddcbdeacfaecdebeecefacbcdadbfbfdcbaefbcdabfdeabeffdaeccfffaadacbfafddecdebddcfcfcacdccbccdfecbfeecefbabaaaedfaeccfdefafccaaadbdebaebaaffbdfcfbfcaceebedceffbedbabcdceabbeeffaeddacbecabefcfdbacafafbafbeacdedfbfdceffbaccabbbbefcacdbdedfcabfbdcfbeafdeacbbadeccaabbdecaabaecacbdacfdfdedcadfafaeffbdddffcccfdadeadebefdfbbadfeefadabffbfeabfcefbccbdcfabcddecfcccadedababaefdddcfdfebfbeaadcfefbfacaabcedacfccfcbdacbbbeabaebecbabdeaccadeddfdebccbfcebafcbbffccdebfeafececdefbbdedadfdcdbdbfbefffcdddefafcdcfdabbbefaadfbacfbffcebaccabefbbceeeedebdbceecadfddcdfebdaacfaabcbccfbffdddadeaadbafdadffbedfaedaefebedeecdececbcfdceddcefaeecabbcebcfbafccddbdecbacccabacdffbefcebdededeafabcdefbeffadcfeecbcdadabbbbfaefefaadfbedcfecdfedfafdabdeccbbcebbdbcdefabadaaababcebabffbacadffdacaecacbfdabcadbfcfebddcfccbdebeababaebfbafdfbacbdfddbaaefdefdadedceaffcabbbeaaaaadbfaafcafdccacfcbdeabaebcdbdbaafdaedafdadabdbecbfcecabffaffceabaccdddedbadedcbcccaeeeaebfbbbacaabfbeaaabaccdaaddbdfbddfbadbafbdfafbacffddbabcfebedbdeddbdacbdacfabbeccfcfedcbbcfeeabccddbffdfdaacbbfccccdfcbbfcdfebbcfccaabedaadfaefefdcaacacdfaebceaadfbefacdeeaedbdfffdadbcdcbebbeabafaeaccecfddeceaccfaafeaeaadfcceaddefbcbcacaaeadfcadaaeddbfcaccaffcdecdbbdbcbdbedfeeadedaefccafaefceabdaabefaddeaecffebaecbefdbbfaffbbdffddfbceeddfcbbfcceefcddaadfdffdbcedbcfdbfaadfbaedfdecdacfedcdfbbfffdfecdcabdabbbfddcddaeceffaadcbbcaeedbecfdbabffbabfddbecbaebabbabedbfbeebefcecffbbdcedeefccdfabceefcabfffecbefecdfbedcafcbdbdedfbddaadbdcedcaacddbaeedeffbcacfebedcfebbecbbdbfebaeaddeeeddbeceabfdceecfbcbbecddabebcdbcdfecfdddbbfddfbffbadcbbbaddbdcaebcfbcaaebabbffedebdbdceeaaffdaefdcfbcdcdfddebfacdcadcbeadcacbfffbfedecfcdcbebdeaceeadfeddcdadcfcfabbebeadcbcdacffecbacfaaeabacfdaddccceffefddcdefefedbcceeecfbfecabcffeddcadabfbcfcbdeabbecafdabadaabdbdaaacccdcaccafecaacddbedfecdbdfbbbffdfcafadacbdeadbdfdedbbfccecabbaaecddcaafabffcdcdadacdaeaeabfdfdafaabcaaacedefdfdabbcddcfdbcffccdfadbaafcaeecefaccbdebdeeabfdabcfadcfccdcffcebdbcedcdadeecfadcaaadbccbecfbbeccbeeebccabbffbdefabebbacececaebacdcbafefdfabfcbfeddcbbaebfbdabacfcbcbeebcfafaebfdbcddfaacfadaeaccafabcbadeaccaefbedbbfaedabcececcbbedafacebaddbeceedacdabefaeccbcbcdebdaebffccebbfeadffbeeffacbfedaaadfdeefbcaebbefaafcfabdbfaddfeeebbecabcbfaeeaafbadefcedebdfccfffafadfbdcceeefdbaaaaaaedadfecebafefbcfebccdbaaeebfadabcffbeacefafccedaaecdeafbabafafbbbdbeeccfcbbfcbbacefcccccadfdeddcceaecdadfdbeaaacfdfbbfbeaedfbbafcbcfadccabbcdcfbafdeefbadfadfcfafcecbeafedcedfddeceafcfeeffcffdedfdebfffabdacdcdbfdccefadfbeedaadfacbbaedefbaadfadcbaadfebcefaddecbecfadcfddfebebaffdcebaccfdcddbccccfcfddefafbbecbeddbabbfecdcdedabaaffbbdddfddafcabadbabccfffcaadefcedebfcabbbacbadddbcfdfebdfebecadcceaaadbcebbbfafbfacfbbabecdcffedaecbcbdbccccdaadfeffbcaacaddcbcbcddabfcfffcebbebcefebeeeccbdfeaacaaceffcffbfbaecbeebfdecebafadfadfffbfabcaaacadbaceceecdacccccaffdfeafcaeaedbafeecccfeebeabdeedcebabfceaefdfcaaacbedfcbfaefdedebcbbdaabacbbdeaffcfceaefadeccbcbbaebfedffeabfaabaffdcffcdbaadffadaccbedadbfceebbedccbfebccabaaafedfabbcbebeeaedecffdebabecbdabcbabffbdfbdedccfbcbeebddedefaafaabceeeebfdbfcddedcffdffbaccccebfbcfbbaecbcdafbdbcfffddedddabcfdbcaadbedbcdbdfadacafafcaeefeaabaffedbaaafdefeacffaacadacaeaddaebafcfdfeaebeccfcfccecbeececccebdbddfdbecffbcacafebbaedffaeccacdddecfdcbcdccbfebbbefbbfafddcfbedcffcfffeecfdffcfedfbebdecdfaedafcdcadfbdfabaeeadfcfddaccedfefcbacdcbfacbcbccaaddfabdfafedfebeaabbadedadeaeeccbcdbfefcfafbedeabcfbfcebcaeabfedfcbeeceedcefbadcacbdadbefdfbbacdfceaffffdbcdabfdefabfcbddeadadbceebabeabbcbaaafbecabbbeefabfbbadaeafbeffffabaecbbcbdcafabbdbbcdfbebffafaecaacfecdaedadccddfdbcedffffcadaffbbddaddcecacfddfababccaacedacadedffdbebdcdfaeeccdcbbeacdffbabdbbcbddbebbdadfbebccafceacdbbeaecededdccaeaebaaadbfbcbcaafabdcdfeddbdacaecdccfcdeabdaabcdddcfdebdecdeeaaaaedcdbaaaaabaebdddbcadbfadcafdebedcfacabbadffbebffcfceefedfebecfaeebfffcbbffbadaebdcebbfbccabeccfcdbfeddeaaeadbbcffabeecebfdaeebadfdecebeeeadeadffbbefbbdacdcbbdcecaaebceeaeecdaddbafeddeadfbcefabcdccddcaaecbdededcbfdfecabcfbdfcafcddeddbcabbcecdecfbddfbcffaafadbfeeedefabfbbafadbecedcbbaaadccbeaaeeebfecbbbfdbcabfefeffadcafcbeadaadccbbababcedeedcbefbdbfaeeadcebebfcfcbbfffacbfdeaccbafebefccdfaadcaaceaadaefebcfdecbfaaaaecaffceebcedabafbdabffdecccebddccfefebfdcbcabbdfaebacfcaefcdeaafecfbdefafafbdfbcecabcdfdcdbaddadbfeadbfddeccdeebebddceceadbbededbafbbdedcdebbcfdaecdddaacecacaccdfbbbfaabbccfdeeacdfdcdedddaffeefefdaaddbeafdfbabffaafdfadffbadefdfeedbcfabfaffbaeceefbcceddaddcaacabfedcfcfeaeacaffbbddfddbefcddadddeebfdbfebadabfafadfbadfbdcabbaecdaaeeccaaafceeaefbcbacaafbaccddbbfbedccdcaffabdfecfcafcaeaccfcecefcccaeabbacddabbcbadfacffffffccaffdbfcfefafddacfaabeedafedeaeddaeffabedbcdeccaefddafdbbaaacabdedffdacfdeefcabbcddafdfacfdabdcbcaebafddbfbceeedbcbbbbddffaaaeaddcafcaaebbdbdbecefebbaaafefaacabbcdddcacaaecccdececeeebbeffbfdecafacfffdbdfdeaeffdeabbaafbacdbfbceaffeeeadccebdadebffacbaceeacbaaddeabcbbcccdfebeafabbdcaeeffedaecceddfbdbbedbcdcacaefcebbcfdbefccfcdafefadfaefaeebebffeaefceacafdffacffedaabafeaedbfccfadafabfbcfcacdcefeadaefeccbbdaaeeafbbacfdbfeaebcaacddeeaeeddfcaefedceecbbafecbedccfeddbbdcfcfedfebbaefabecdbcebfdfffaabbbbdaffbbddbeadbaedfebdccebeefdaccfaafcbdcbeefdcaebedfcefdcccecddafeeffdcecebfebeffceaefddfafcbfcefbeddefffaedfaddecbdbbbefbeffeaebfcebdafafaabcaceaeaadaebfaaaafffabbbdecadddefdddeaedcfffdbdadcacfcdedbcdceaaffffaefbfbbaecedabbabdadfdfcaadfcfcbbcbcebbdddddbddcffdddddfbdccddebfeeaacaacabffcfdffbeaaaadcddedadbefcdadabbcedfafeccfbebfecffcdcbafecaaedfcefcfeecefadcfcccddbdffdfefaababbdeccbeecdedfacadbabcddeeedddbfdcacffbfbbdaccabafcadeecfccbdceaebfeaccdcbbcbebfafeccdfebffceecddcfaadbeddeddadddfdceafbdfacaccabcddaeddcecdaffacdfbeadcecfbfdabeacbaccfbcdbbaededdeadadefeccacdebaaffbeffeceeeebbdeeecfbbaffcbddedefdaabcbcfdfbfeaadbbbbbeecfeecefebeebfbabbebfebebcfaacbfcaebcfddfecfdeefeebdbafcbaebdcbfbebdfeefcabbbeaabfababaffddbabdfbeeaeadecbdbddcefbbbbccbbcadbcddcfcbffffbfddedeedffcdfbdcaaccfcabeefdbedcfffceafdbcfecefeefabeabeddaceaccfadebbeeacffcbfadbeebeebbfafddbededacaaeafcbdbbcdbdefdbabfddfbeaaefaafefaebdacacefdefababdcdbbeefffcbcabdaaaecbaccbbfadcecbffbcdafceeecfadccbefbdbcddedeedfbdccceaabbcfdcedccccceaccbadebbfeeabebeafecbbdacdcadddcfdffbdcbefcfddefdefdadeabbacbceefbabadaeebdebbbffedacefdaedabebafeffbcfbeaeeefefdceefdbcecafcbdbdbbbfbcdddaecabffafdbcdcdabcaefebbabedaaafacadddeafdccdcfcceccdadefebbefedfcfdfaaceeebccbaefecdfacdafdeeeabaebddcdeafdacdbefefcdaffcbaafbddcfcadbcfccfdebfbddebfadcdbfeefbdfabeedfebccaffbdcbfebafeebcadaaaeebfbebedbaeadeffeeadcfefeeaeecadbfdbebadcabdceabfeecefdeeeccecadcfedfcaebffbfafbceffcefedfafceaabaceeaeafdadeaffbaefeefccddfacfddadecfcadcddeecbeccaddecdfacfaafcbaeffcbfcfcabadbebcaeabebbbfcccefcdcbabafbbafcbbcebaabcfbdbaccedccedcefceefebdabcdaafdfbadbfeacdfdceddcbecbebfbfafbeebfbfcddadbddecbbccbbcaedcfcebacffebfcaedfbecfabccbfabacbcbcdefdafbbeaeebdfaccbdbddbcddafbaeeeababcffbeddafbdadeefeddbceadafbfeffaaaedebdffdcbcbaaaeffbbfdaeacddababfeaccabcedfedebbdfabbfcbccdedffbfeeecffdbdccfeebcfadcdfaefafaecbaacaaddfefeeabbbecccafbfbdceecabbeebccccddedfeadfffdfcfdadadacfcadfeffbbafcdfccffdffbbfbcfeceddfccdcdbdbdddfebfddaeccabecffcddccecdfdfeebeaeffecebbcdcbefaabbcabcacdbebccbfaacfeecffaacfdaaefbecaecbccbdbbdacfefbaedceafddcafeeebbfacfddcffcffaaadebacacfafdaecefdadecfacfaafeddcddfeeedccdafdcfdacabaadadaabbaeabbbebeeabfcbfbffcdfdceedbedadffbcaedabfbadebfdafcecedecdaaedfcfcaecfeefebfbcabcaebbbedccefaafedaeecfffcfddcaacbdefccfcdfbadccbefadccbcabacefdbebadadbfcfeecadfadaaefdccabeadebaacdbfefdcfabdbfdaadffdbfdeadfadeaafdecbceaaaabebfeaabbfbdfbfddaccbcadffbcfeddcabecaacfeafddbbbeadeaadafabeeecaeccdccbccccffebebcedbeefddedcfadabffbcabfdcfcbcdefddeeefebdbcccbcccfeafcffcececbdbbdbbafadbfffbcdcafaaffdbaabfaadfcffacfbfdccfaadaefcabeadadcfcaffaedcaecfccaedebfcfcefaddafcabfcdbfdbfcacbbfddcfbaaefaefacadcfbbefddebbcbeaaddcbbcdefefdddfffefddeafefaeefdddccbaeeffcebcbafefbcefcdbadecbaeaafdfaaeecadbfbfbdecbbbbcbcdcfaeacadbdaeddbdeddedfeefebcadeedbbeadddefcfabcaddddcfbeacdcdabdccbdfacacafeaecceddcddedbfbebebadeaeeafeebfacccdedebaccbeeabcdabebadbcbdbcafcdeaccdacfbfaaafeecfdcdffbdaebeacddcbadbfadaccfdcbeeabedfcfacaaafdebdeebbccfbadabacdefbbcbabbdddecbbfbdefdcbffdfeffdceafaaadccceedfcdedfcfefdcbfeceabbeddcefcfefddddfcebfebeebbdcaabdaeefbccaafabaefccbbccbeddccdfffffcbcdeeccdaabdbebfffececfdcaffbafffecabdbcabdefeefacaacdeedaddfcffdaafcebafdabbfabfcdfdaabbdacebeaaaaffadfaffbecabffedfddebaecafadeffbdfebfffafaddbadfecebdabccbcfaeebbcbeccdfccbfcdcebbcbcfbdadbffeddfbcacafdccedebeefacbdfaaedfdabfefbdaabcffdaccdcfcfecddeebcbcdfdebbaebaacaedbadfbeecebffcddedecabaaeedacbbacdcfddbedfecbbceaccacadfcfbefbdaecaccaadcddedbfddbdfbacbcadfedccaafdfefcdeeecdcaeacccfbdbadadacccbcddeffeddadbeeccbbffeecfaefaaababacdcbfeceefafdfebacacbbdcdaeaffccddebfbbdebbaafeacbbbdcbfefcfceacedffefbeccfedfdddebabfdedecddcecdcdcfaebebcceaedfaecfedaafcaddfceeedefdceeddfaeffacadfeaecabbaafcbcabcceeeacdfbbebacfffecbeefcdaefbefefcceecefddcffdfffadedefeafdaefcaffabdccaaabcbcfbcacbbcfcffbbdaeeacbafeeecfdefdebecfaccbeaffcebcfceeeebcdfabdecdedceccdedbdbccbddfdbecfccbfaacafefeedaffcbebcdddedacfcafdcfdeffcefefabaefcddbdebbdddaeaedfecedfbdabefaebaadbaaefcbbfbcbfcdbadebaceddfbeeeaeadffccfcbcbcbafadafcbefbaaffcfabceebccdcfbffdcfdfbfcaecfadbdccacfafaacecdbabfdbbcdbcaacdcacbdddeeecebbeaabfcecadeaeaacaeffdebebebbaeeecaeeeabbdaebacaeccdfacebeaefdfaebddfeeeefececeeaaafceaabadadbeaecccbfcaffbaecdfbfafabdcbaaeebeeefcdffdbbdccdafeccfaabdbeccdafadcabbfffdeedcebcadffbbbbbaccabcdbebbcfabbbefaacbbfaafdbfaefdeefdaebcfbecbacdfdcadeabeaadeedefcfbacceeacbccaacbbeeeeeeddbefbddcddcbcebfaaafaeafbbacddafbceaaccdebdefefcbccbbcfacefabecdaacebdcbfeeedeecbcbfabaaebdbadfdbbbcdffdcbfbeeedefeecbfbcaacbfcafccaadbffddfbadcecbdbccbcffabfccaaeadebbdbbbfddcedbaeeaebedddfcffdddddfefaeafaecbedececdcedbcfcdcfeedcdfebfcfaefcacbacfcfccdaefcfbddddeeebfbbafcbbcdaebfccedcbbaedecbdaadeeececacdaaaefafafbbaefccabbbadfbdbfcbcfaceeecbadebfccdddecaedccbaedccdfaafbdbdebbbabdeddedddbdafcfebebadbceeaccddedbfaafdcaacfdfcbcfbecffcbdbfaeefdceffcefdccbcdcdfbdbacbdfedaafecddbbccdffefeeabeaeddeeccdadddbfaedfadfcefccbdcddacaabcaffaacbefeccfddefdaadcfefcabbfefeaffacdcdefcebaabfedcfaafeccfbbcedfcaefafadcfddbefbddaffbcfaddabedccfaaafafdaaddccdfebcceadaeaddcbaffcfbbbbabdbbeecbdccbfdccaaffeafefcdaedfbcbebbacabfbcddbfdefebccbdffbffaceedfaeafbcdadecebcdeebcdcdcbbafdafafbfeafbddaabffadbbbfcaabcafdfdefbaeaaaceccbbaefaeebafcadddbacebefcbfafdbbbfbfecdbafcaaafdffbecdbbfbcbcadebbfbcdbddcaadfffdbbabecfdaddeebeecefdddfecdbfccabefdebeaccccccedeebcbafabcdfbbdcddebbcdefbefbbefbeaebdbfbfceffaddebadfcbfedafdadfebcadfdecfadbbeaedaedecccebcdffcddacbceaefadccbcfcdedfabdabeadebfefbacbddadfcbcfebbfcafcaefabddccefdebbecddaffebefcbdbadacdbecafadfedcceebbdbbfdcfcfcbbbfacdbbefdfffcacdbdbebbadfbbbcadfbbbaffccdcedcdaabfafabaebaafdbfebdaddadbbedcbacadcaaffdbeddabeaeffaaaccdebdffafbdcbfebfeebacaadaeefbebcdcfbcbfcfeefeaadaebdccaaefdabaffbdebffbcebdeeabbbddefbecbfffdedfdbbbcbccddbdecacbbddffacefbcfbbbebebfeaecaddacccfcfbccafdbfdbfdeacecbbadeadfaacfaffdafcdbcafdaadedddbccffbfcaadbfbfadbcffcffbdcdbfbeabbbbccbbeeaedebfddfcfebedfebbbcfeccacedbfceceaaaeccacbdeeaedfccdffbcaadbdfbfaacbbfdafddfccafbbceffefbaadfbacbcbbbfcfecacecfecbecceccfbadaddebeebcfffddeefdadedbbfccfceddebeebddeacaecdacbeabcfbeadedcbfecfefaabfbdccdfaeabaeddeddeefcdeacbfdcdecefadecadebbcdabcecffeeeadbeffebbfdfdaeefcefbceefffaacccbfbaaffbfffbedffcddedebefdbedafddddfddcfedadcfcaadeefffffbeabbadfcadeedacfbdffabfeaeacefecdbdebffebedcdefdadfdefcadadeafffefcedadacdbdeafdbccddefbaccaddceaceedcbadcdbefffbcfbfddadfbfddbbafcafdfdcaeadcfacbfabcbdcdfdfbeabfcbaafbbededbfbbbcbcdaddbfebbbbbccabfcabbbdcbadacdbccaefaffdaddcbfdfddbaafeceebefdacfbddddfadacfdaeabcddcbeabffcdebdddcaacbeaacbbafabceddbbabbbddafbdfacbdfebcafcedccccbbdfaedacedabefcafaadedebdcbdbafafabaeececebccaefffdffeffadcdbfbbaafebcbddddbcdccbfbffadbafcaedcbbeaecefbaedcfeaeafacfdeccfbcaabebfafbefecdcdeacefaedcbdacfcfbfafeaaecefbfdaecdabefedcaddaceabcfcddefddceebfdfabedbceadeefdfcefefedfcbdddcebddaadbecbedeacaccaadfbdfcebdedfbdbdffffcbbabffbeccfbaeecdffafdfaabbdaaeccefeefbbfebdaeedbcedabbfedcbedddfdbfdfcdacdbfaababfdccadddcdddedbffcbeccbbcefbffddcfeedcbccecadedfabcffbadccdcfefaccdfaaeffebdcabeaadebcedacbeccedbecdbcbbccbaebeabcdcaddbcfeaffccfbefcbfdaeaebcbefdccdefabcaaabaefcbfaccbefbeceecdebfddddcccefddbaefadbcaffffadbbdceafbdcaeadccbbeafddafafdfbccbdafbedaedbfeafbbabdfcdcbdbebafffccccdcbdfcbdfafcbecbfacccbebaffddbcebeaafacfbeeccdadbadcfedbafdadbbdcabdbddaeeaacafbdbedefdefbcbafbfddeabbbbbfebbacdafeebcdaadcecbfcbdcaafbffaccecbeefceacabbdeeacfabaeaabfeebddbecbedefeedebacbbacafecbfffcbceebccdacfddabfffffacdddfeeebaecfcaaccefcadfddcabfffecbcefbabbcaeeadeceedbffdfcdfbcdbacedcbaafabcbadfcedfcccfaddadeebbdebabbdfdeaedbbbbaccfefdfcdacbeeadcafabdceaedafffedafafaaaacfaebaceabafdbdecccfeeedafcbcdabcfbeacddafafdedcfeefbdcfeebcabbacbabebbdacadadcbbcfedecddabcfebbefeebbfdacffbffbfbcdfbcedcfeebcaedbccefaefacbbfcfdedbcecbeccbeddbcacebcecacbbbffbdbeaadeaeacbdfdcadbeefeadedefecbdcadbadadadcdfccacfcdfbaccecdcdadcffebbcffaadfffbfbefafafeccefaefddacfdabdebcfeeecaecffffecacecacddcddefafcaeedaccdcaeabbbfbaffbbdddccdbafebbcdeffeddecbddceddabdecdedfbdcbbbbdbddeaaeeaebcddbacfbdbbabaafddedaccaecfebbcfdceedeaffcdbdfdecfbbdedcfbbdcfeccfcccbbcbbfddccacfedcefdadfdbbdebaddaacbdeeccdaedecdaffbcaabdeccdfffcbcdeeacccdebcdffcbcccafeeadbefadefdedebfdfffbecedceaaebebcfddffabedfbffbaafebccdbfdcadcaaedfdabafaaacececcfdcbfebbdbdedcbcddfadbfcaefbbdcfbbbadaabcbbdfdfabcbcffcfacbafbaadadaaffeaecbcefecefaffeeafbbaabaacfdbebfffbcdefbefbbdceeddfaaafedeacedbffaeadcbbbabfbeeaedbeafdeefadbbecbdefbbddaeffadfeefbeadddfddfdcebdebedfadfcedbefaebbcdbedeefdaefedffdddbfbebbdabcfdeffaefbeacdcbfafdfcbfcbbdccacaadfeabaedabfcddcbdaadeaeddabfffecaafcabffafeadffaecbabadfcdbdebdafceebdceceacaafbbbebcabeebeefacbccedecbcdfcceacabafbebbdfcceefecabdfbdabbebbacaccfffceadfffedcdafdfaebdbdfdfadecedcebaffdfecefbbfacfdfbffcceaedddbadfdddabefcfffaccefcbbacabfbcfcfbfcafefdddcefaeedccdedaeedfebaeeebbcacfeeacabdeeccbfdfcdedaefcceddfdcdffadbaddefaebcbcdbacddbcbcecddfdadffdebabedddbcdbcaddeadcdddbdbacbdefbcdbcaeaaefbeccaaecacaeecbcdaeefbeacfbeeacfcbccfcfbfabaeddffcfeececcdffbafcbbdefdbfdebbaccacdcdcdcaafefffaaadfafcdabeeabbffefcfdebcfddeaeecffedaaedcafadaffdedabcfbbcfceccafbacdbefeeaedaddabbfdaeefbaebfcbfdcbdaeabbdffabfbccfbfabbabeeebcfffdccdefeeeabecccdbfbbdadbecfebbccaaaeabfeefbdffeaccebbbdbeadbfebcbddefeaaffcafbdbcceadafabdcedfbadabeabcaacdbeedcaffcedfebeccbcecceadaefeffeaaaabaffaceacddcaacaafcecdafbbafedaefedddebebccacedabfbaccbceaddcaaafabfcaaacfbeaefffdebeccfadaecbefaddceacabbeceaadcebcefdfbbeabdcdfdefaebeefbcdbccacdfcfbdbfaaedffefbbdbeabebbfeebeebbcfbbafdbeddafecbaebbdadafbebbededcacdcbfffdbcacbaebdaaeeeaefcefceedffbeecaacdcbbffcafebbacbeffcaabfbfaabfcbfbcceefbbeeaafbaeeecdedebfdedbffccafafcdcffbecadcfedcbacddbdfbeebccecfbcaabfecdadbbbdecbdcdeebceeaaeaeceffbacabddddabafeeeaddfaaaeacfeaafdceaacffffeeedfeffafacffbfbbbbcdebecedcedcdbaccaccbacfeeaeacdddabaecabdfabffcbbecbaffbcbaafddbfeaaacffcdcaebceefbcecffcaeebaafcbfaeaedfbcaaafeedfebaeeeaecaaefddcdfacedbfcdfcafbcfaafaabdcbfeebfeecceabdbfaedcededdafddbfabcbcaaacfededecceccfabafdbfcdddefbcaecbafbffbfcbfeadcddcefafbddbebabdbcaacbfdfcfabdfcbeaeaadfcfbeeadbceebeafdcfadeaedbdadeacbaafefebaccfaaaaafdbfebfcdffbcabcecfbdddfdacbdfcdecbacbcfcffadcbefaccabcfbafbacddabbbddbcddeecaabfadeedbfbbccaccabafcceecabafaefefeabafbcfcbedffbeffdcdedfaedeeebaccbdebcffeeaebbaddebdbbcdebcabdafbcadbdbabdbbbaafabefecbcfafdbcfdceafeebadbedeaffbcebacecdcaafbedaeafdbeffceebfefcfbbdafbfdbabceccafddebdfcccdfadbeaaebffaedeeebadeceabcebeecdfccfaccbcebeafcaeebadadefebfbaeeeccbeaafbbbddfdfaaeedfdacdedaffcadaeeeffddcfdfedfcbadccdbeaaaadaddfdaedfdcbbeecddabeaedbcecccdfcafefbffbaccfafadfcfecdffaddbebbffafceaedfabfeeeaeeffcfcebedddbabafbbaafaaadffaeabbbfbcdbabbafbefeeefaabcaaeebcdeefdbedacccfedbfaecacaeffbfbbfcbeddcabfedeaaeeacabecdbebddbbfcbdceaddeefeecfaedeeedfdffdeaccfabbaffabdeadabaaeadabbeeeefecbcddafefdaccdeebcecabbebabaabadbcccccbbdeafefffedfafaeedcdbfcdeefdfaebabfaceeebdcddccaeefeddaffeaaedbfddddbfdceafddbbceddffddaddfeaafbfefccfeedbeaabddfbbcabfaefabdadfaeecdfbccdfdbebbdfdcccadfaaccedcddbcaedfceedfadbebafecfaecacaedcbfffbbacddccebafcaefffefdfdebcabfbcadaebdbcabcacbaebcdfcbebdbfbffacafbfebdddebccaaefdfacbdbdacecaeedcabfafeeebeafabddaadeabdabadcaedcfadabfebefeadbffebcdedadaaaccbfdfaceedbaedfcceabdfedaedffaafadaeecafbfbaebeeafaebcfcefedcfcddaafacebefeffddcadacddcabaedafbaecaeaccbfbcdaeaeceabfcfeffcdbdbacbdecbeebecbeadfaaddeffebdccdbcfcacfddadbafdadbbbbaafeceadbfccaeedfeededadfdcfebcbfaecbacbbdeeaffbfceebdfaeaefdddfddfcfbfbafcdbbaccdfaeafbfecfbabfbdffebaccedaccddbacdeceefbcaddbcdbcaadeccfbcdadabefcabdeaeedcabdacbccbfcbdbdcddfdeebedcdfacfbdaadaeebadbbfbeacbdffffefaefbceceabdeafedeffbfcacefeebeedaddaaacccedbedfcfffeedaaaceebafdfddafacbacabeccafcfceeefeedcbffbebcaceadaeaeceaacaaaebdfaaccdfbddcdcffcafeebfdbeaafeffbfbbabeaaeffacecbeedbbafcfcecacdabadbdeabefccbbaabbbbfcdbcbaaddebbfddbabebbebfbeebfdaaeadbcebebdddffcefefdafcccbcbaeaaabdbdebdddabdeaaacdcaadcdddefcddecbcdacdbaddbfcefaecaffceceabcccefcddefbbdaebffbadeeaddabcccaabcafebacdcefbcaebbcdbccbdfbcefeaedcadbdedfdeffddbeccccafabfefcbbcaadcbfadefbfdadcaabdefbecdcbaadbddfaffaaeefefecbfefdfebfefdfaeadfacfcdebbcabddabbbaedfdffbbdafeaaaaafcabaecacaeddabfaabdadbedcaadebbaefdaabcfddcdefbcbfbacdcafaabfededdccbbacbffeddabcbeacddbcfedbaeebcccdfecebacddebaddafeabdbedcceabcbafcbceecedbecccadcfadfbdbeadfbabfcdcfefbfefffdfebdcabbeaedfafebfedaeafceabbfdedababefacfffbfdceafdecdabdabebbfdbfbaacdaecefcfddddbacdddbeeecaadbdaeabbacffcaabacecddfbaeabaffabdaecdfccafcfedbdaabceaeaebfcbfdeecaddcdfdcbeffdfdacdabacfeabbaccffaffdafcdafeefebcfebcadfecbaafffbcebbbbedcbddbbffbffdedecffbddcddcfaadbefdfabaadebcdfcdfbafcddbcbacaeccbdbcdaebdbccccdcfcebdeebadaabedbcddccdcddcdfffdcceeeabfedbcedeeecbbacfdcfdcefcefdfbfebfbdbbfabceeafcaaecceebeebecdaccfbdadaaccabceacbcdefaebcaaffadfddcfeddcaffebfdeebafdcfcacbecacbbccedbbcbcefaceeccfadefddddbbdffcaeedbdddaffbccabceeeccfeffdbeedadebebebfefedaeeaddaeceddaafcffaeccbcefdcfdaabbbacedfbbbffcfaeedbaeffbdddcccaadbdcecdfebcefbfdcdbaaafeacecaaabaabbddadfedfcebcffeacefadabefbacbdabfdeffffacaebefdedececfdbeedddeedaaaefcdffcfeaadaeafdfdfeabfcadbdfdfaeeecbddbbdcfdadffffdfbaebeafdfbeeaaafbfadadcadefdfdecabfadbfeecbcffcdbaecccfdfcdcaabbbafbfaeefcafdffbadfbcafaeffcdeefdabcceffceeaaffbecfeadadffacebbbedeacffdfccbfeceaafcffceaaeafcbbdaacedafaacffcedfdbbcdcaedbcfaacebffddbbfdcaaeffaecedbcccaeccaddddfddbebedcbfbcdababffbacceeabbcdabfebffbfaeaadbadbecccfccbdbdddddccbbafcecfaefbefacbebfbfdadbbebfdceabeedefdadebccffbbbcfaceebddcaaebcacbaebffcacecbeeeffadadccafebcfadccadfcbbfaaacacbebcdcbcebbdeaaaeceecbfccdbafbceddbdfafcadfdfbaacfaedbfbbbadeedebccbdadbaabcfeedfbccbdffacddcffbfbdabacddebafebcdacebbafbdcebddcfbddcadbdcecabdcbcbccaeceadcacedfbdebdffeaadcadadccfeddfdddcbbaccbcbfacebddbdcefefefecceaafdeeaebbdcaecfbeedfdffffecbacfffcddbffedebfadacfcaccabcffcaefecaadafbcdcaebeeabcafcaafbaeacfeffbaaaefdfcedabcaffcffdcfaeccacfdfabaddeafeadfcccbfeecdbdceeebbecfedcfacceaeaeadfccfcfbcecdeeeccddfadadfabdddcdbdcdcbffcdfabffecddafddaaedbbecbcfdddadaadecfcbeabcddcdfedddceffcadefcbddcebccfceafafafededeebbdbebeafaabfdabbfebceedfcbdcefeeeebaefbefcffcebfedcdbaabfbadbcdfadeaceaeebeabdbdbbdcfeecfbdccddcffacedbbedfbecbfbabedbaaadfaffbddbccbeeacaccdeeafecbfccbadeabbcafdaacbadeecfaefdfbaafeefcaecafbecafffdcfebcaacddeaadeadeccbcdbabfbefbafcceebbceeacdffbfbdddaabadeabfeedbddfabafcaacfbeefbdabcefcbddeadcaecfeecdfaebaeffbddecbcfdeaacbbacafdfbbbfaecfbfbfdadbedfcadcedceedcdbdebaceaaffaccfcbdffbbccffaadfcbadccaceaccdcbcaddacdfdfacbcfcecbacfbbeeededcbbdaaffcfbecabbbcacfcbdbfdacabcaeaadaeadbdaeddeedebfaceedbcbddebacaafdcdbcddeadefdabcdccfefaedbaddbdfcdddddcbeeeddcdcddcecbaeefeecdcaccfabafcdfdcdaceebedffddddfdabddfadbbfdeccaefffacdceafdeddeeefcdbcbddebdbafbadfdcfccbbfdfabfdcafdfcaaacfbecafbceabcdcacadaddcefeddceeaafeebdefdbcebadeebfcfbeedcfebdceadcdebbeffeebfdceebdccaaddfbcddacfafbbcecabdbfadeececedaefedfafcbfeafcafcecaecfdecffbbbdceacfbeedabbefccabadadffddeeacbaeafbdaeedfceaadbafedaaefeccaeccccaabdadcadcbddcdcffdcffdcfdffcdbcedadfabebfddaaabeffcdafdfafaacbeaaebdfeeddfbecadedaccffadbcdafcdaccffabecbebfefaccafadcdefbcdbdecbbddcfabcedddeceabcfffaacfbebaeeafdcfadbedaceecaaacedcdafdaafccfddfebdabcacceedcabdbbbeeffddcbfbdbefefdddaffabdcafdefeefdcfbbebffdbdcecbafafcaceccbffedcccdbddcfffbbccdffdfafefbbbeecaddffefaacfcaedefcbfbebcdbfeeacaffcbbefbbfcdbdfedccbccdeeabdabbdacafbaeafddeffbbeaeefafbecdafccabbadbffffabcafabaecbbddeefaccfcaffccfffcfcbfdbcdbccabbdccafefeaaaddbcbdfcfdffeeacfecceaadecbdfccebfbbbeeafcafaefacddcfadcfdadfbefecdfdaecefabceceabfabddcedcdadadbeaeececfbbdbddedcdcecdeabbcceffcaecdcabcabeccfceeefbcbadacafddbfcbffdcfedeebfedbacacbdbbaecffaaecceafbadaedefeccbebbeeedfaddbcdafeebfefbeddfebaadddbfeffdcedadfbecbaddccaabfefebdbfcafccbecdbbaffdceaffcaeaccfddeaddabaaeacfabafbabfccfbccfccbbaacfbcbecdededcbfbdeaaadffeeccbfffbadebdeeadcdcafcfdfbadafecfbcdafdfdaceaefcdacaeefefceadeedfcdffdaefabcbeebcfdfaedaecdedccfadebeddefaeadeccedfffecacfadfdacedbdacbefdccffcacfbbadecdeadadccebfeacfcbaecbffadd",
                    3,
                    1,
                    10));
//            System.out.println(new Solution3().maxFreq("aababcaab", 2,3,4));
        }
    }

    static class Solution4 {
        public List<List<Integer>> reconstructMatrix(int upper, int lower, int[] colsum) {
            int sum = 0;
            for (int i = 0; i < colsum.length; i++) {
                sum += colsum[i];
            }
            if (sum != upper + lower) return new ArrayList<>();

            List<List<Integer>> matrix = new ArrayList<>(2);
            List<Integer> u = new ArrayList<>(colsum.length);
            List<Integer> l = new ArrayList<>(colsum.length);
            matrix.add(u);
            matrix.add(l);
            Set<List<Integer>> seen = new HashSet<>();
            boolean done = reconstructMatrixHelper(upper, lower, colsum, 0, matrix, seen);
            if (done) return matrix;
            return new  ArrayList<>();
        }

        public boolean reconstructMatrixHelper(int upper, int lower, int[] colsum, int index, List<List<Integer>> matrix, Set<List<Integer>> seen) {
            if (index == colsum.length - 1) {
                if (upper + lower == colsum[index] && upper <= 1 && upper >= 0 && lower >= 0 && lower <= 1) {
                    matrix.get(0).add(upper);
                    matrix.get(1).add(lower);
                    return true;
                } else {
                    seen.add(Arrays.asList(upper, lower, index));
                    return false;
                }
            }
            if (seen.contains(Arrays.asList(upper, lower, index))) return false;

            if (colsum[index] == 0) {
                matrix.get(0).add(0);
                matrix.get(1).add(0);
                if (reconstructMatrixHelper(upper, lower, colsum, index + 1, matrix, seen)) {
                    return true;
                }
                matrix.get(0).remove(index);
                matrix.get(1).remove(index);

            } else if (colsum[index] == 2) {
                if (upper >= 1 && lower >= 1) {
                    matrix.get(0).add(1);
                    matrix.get(1).add(1);
                    if (reconstructMatrixHelper(upper - 1, lower - 1, colsum, index + 1, matrix, seen)) {
                        return true;
                    }
                    matrix.get(0).remove(index);
                    matrix.get(1).remove(index);
                }

            } else {
                if (upper >=1) {
                    matrix.get(0).add(1);
                    matrix.get(1).add(0);
                    if (reconstructMatrixHelper(upper - 1, lower, colsum, index + 1, matrix, seen)) {
                        return true;
                    }
                    matrix.get(0).remove(index);
                    matrix.get(1).remove(index);
                }
                if (lower >=1) {
                    matrix.get(0).add(0);
                    matrix.get(1).add(1);
                    if (reconstructMatrixHelper(upper, lower - 1, colsum, index + 1, matrix, seen)) {
                        return true;
                    }
                    matrix.get(0).remove(index);
                    matrix.get(1).remove(index);
                }
            }

            seen.add(Arrays.asList(upper, lower, index));
            return false;
        }

        public static void main(String[] args) {
            System.out.println(new Solution4().reconstructMatrix(4972,
                    4983,
            new int[]{0,1,1,2,0,2,1,1,1,1,1,0,1,0,1,2,1,1,1,1,0,2,1,2,0,0,1,1,1,1,1,2,2,1,1,1,2,1,0,2,1,0,0,1,1,1,1,0,2,0,1,2,1,1,1,1,0,2,1,1,1,1,1,0,1,1,1,2,1,0,1,0,1,1,1,1,1,2,2,1,0,1,1,2,0,1,0,1,1,1,1,2,2,0,1,1,2,2,0,1,1,2,1,0,1,1,1,1,2,1,2,1,0,0,1,2,1,1,2,2,1,0,1,1,1,1,1,1,2,0,1,1,1,2,1,0,1,0,1,1,0,1,1,2,1,0,1,1,1,0,1,2,2,0,1,0,1,2,0,2,1,2,1,2,2,2,2,2,1,1,0,2,2,0,0,1,0,2,0,2,1,1,1,2,2,1,0,1,1,1,2,1,1,1,0,1,1,0,0,1,1,1,0,2,2,1,1,1,0,0,2,2,1,0,0,2,1,1,2,2,2,0,0,2,1,0,2,2,1,0,1,0,2,1,1,1,2,1,0,1,1,1,0,0,2,1,1,1,2,1,1,1,2,0,0,0,0,0,0,1,2,1,1,2,1,0,0,0,2,0,1,0,1,0,1,0,0,1,1,1,1,1,1,1,1,1,1,2,0,1,1,0,2,2,2,1,2,0,0,1,2,1,2,1,2,1,2,2,1,0,1,1,2,1,1,1,2,0,1,1,1,1,1,2,0,0,2,1,2,1,0,0,2,0,0,0,1,2,1,0,1,0,0,1,2,1,1,0,0,0,1,1,0,0,1,0,0,1,1,0,1,0,1,1,0,0,0,1,1,1,1,0,0,1,0,1,1,1,0,1,2,1,1,0,1,0,0,1,2,2,0,2,2,2,2,1,2,0,1,1,1,1,1,1,2,0,2,1,1,0,2,0,1,2,1,1,0,1,2,1,1,1,1,2,0,2,0,1,1,0,1,0,1,1,1,1,0,1,2,0,1,0,2,2,1,0,2,2,1,0,0,1,2,0,1,1,2,1,1,0,0,1,1,2,1,0,1,0,2,2,0,1,0,1,1,0,1,0,1,2,1,2,2,2,2,1,0,1,0,2,2,2,1,0,1,0,1,1,1,0,1,0,0,0,2,0,1,2,1,1,0,2,1,0,1,0,2,1,0,0,0,0,1,1,1,0,2,1,1,2,1,1,2,2,2,1,1,2,0,1,1,1,1,0,1,0,1,1,2,0,1,0,1,1,1,0,0,1,0,0,0,2,2,1,2,1,0,2,1,2,2,0,1,2,0,1,1,1,1,1,0,2,2,2,0,0,1,1,0,1,1,2,0,1,0,2,0,0,2,0,1,2,1,1,1,1,1,0,2,2,1,0,1,0,2,1,0,1,1,1,1,0,0,0,2,0,1,2,0,1,1,1,1,1,2,2,1,2,1,1,0,1,2,2,1,1,0,0,0,1,1,1,1,2,1,2,0,2,1,0,1,1,1,2,1,1,2,1,1,1,1,2,2,1,1,1,1,2,1,1,2,1,1,1,0,1,0,1,1,2,1,1,1,1,0,0,1,0,0,2,2,0,2,2,2,1,0,0,0,0,0,1,1,2,1,1,1,2,2,2,0,1,1,1,1,1,1,2,1,0,1,1,1,1,0,1,2,0,1,1,1,1,2,0,1,0,1,1,2,1,0,0,0,0,2,1,2,2,1,1,1,1,0,1,1,2,1,2,1,1,0,1,1,2,1,2,2,1,1,0,2,2,2,2,1,1,1,2,2,1,0,0,1,2,2,1,0,1,0,2,1,2,2,0,1,1,2,0,1,1,1,1,1,1,1,1,0,0,1,0,1,0,1,1,1,2,1,1,1,1,1,1,1,1,1,0,1,0,0,0,1,1,0,1,1,2,1,1,0,2,1,1,1,1,1,0,2,1,1,1,1,0,1,2,1,1,1,1,1,2,0,0,0,1,0,1,1,1,1,1,2,1,2,1,2,0,0,1,1,2,1,2,1,1,0,1,2,1,1,2,1,1,2,0,2,1,0,0,2,1,0,1,0,1,0,1,1,2,1,2,2,1,1,2,1,0,1,1,0,1,0,2,0,1,2,1,1,2,1,1,2,0,2,0,0,1,1,0,0,0,0,2,1,1,0,1,1,0,2,0,1,1,2,2,0,0,1,1,1,1,1,0,1,2,0,0,1,2,2,1,1,0,1,2,0,2,1,0,2,2,0,2,2,2,1,0,0,1,1,0,0,2,1,0,1,0,2,1,2,1,1,2,0,0,1,0,1,1,1,1,0,2,1,1,1,1,1,1,0,1,1,1,0,1,1,1,0,1,0,1,1,0,2,2,0,2,1,2,2,2,1,2,1,1,1,2,0,1,0,2,0,0,1,0,1,0,1,0,0,0,1,1,2,0,1,0,1,0,2,2,1,2,2,2,0,1,0,2,1,1,0,0,2,1,1,2,2,2,1,1,1,1,2,1,1,0,0,2,1,1,2,0,1,1,1,1,2,1,1,2,0,2,1,0,1,1,1,1,2,1,1,1,2,1,2,2,1,2,1,1,1,2,1,1,1,2,2,1,0,2,2,0,0,0,1,0,2,2,2,1,1,1,1,1,2,1,0,1,1,1,1,1,1,1,2,0,1,1,1,2,1,1,1,0,0,0,2,1,0,2,1,0,1,2,0,0,1,1,0,0,0,1,1,1,2,2,0,1,2,0,1,1,2,2,0,1,1,2,1,1,0,1,0,1,0,1,0,1,2,2,2,2,1,1,1,0,2,2,1,1,0,1,2,0,1,0,2,0,1,0,2,0,2,1,1,0,1,1,1,0,1,2,1,1,0,1,1,1,1,1,1,2,2,2,1,2,1,0,1,2,1,2,2,1,2,2,1,2,1,1,1,1,1,1,0,2,1,0,1,2,1,2,0,0,1,2,1,2,1,0,1,0,1,0,1,2,2,2,2,0,2,2,1,0,1,0,0,2,0,0,2,1,1,2,1,1,2,2,1,1,1,1,0,0,1,2,0,1,0,2,0,0,2,2,1,1,1,1,2,0,2,1,1,1,1,0,2,0,2,2,1,0,1,1,1,2,1,1,1,2,2,1,1,1,1,2,1,0,1,0,0,0,1,0,0,1,1,1,1,1,1,1,2,2,1,2,1,1,2,0,2,1,0,0,1,1,0,1,1,2,1,0,1,1,1,0,1,1,2,0,1,1,0,0,1,1,2,1,2,0,0,0,1,1,1,1,2,1,0,1,0,0,1,1,1,1,1,1,2,1,1,0,0,1,1,0,0,0,0,2,1,1,1,1,0,2,2,1,1,1,1,2,1,1,1,2,1,1,1,1,1,1,0,2,1,1,1,0,1,1,1,1,0,0,0,1,1,1,1,0,2,1,0,0,0,0,1,1,0,0,1,0,1,1,1,1,0,2,2,2,0,1,2,1,1,2,0,1,1,1,2,1,0,2,1,0,0,1,0,1,2,1,1,1,2,2,1,1,1,1,2,0,1,2,0,1,2,0,1,0,2,1,0,2,1,2,1,2,2,1,2,2,2,2,1,2,1,1,2,1,1,0,1,1,0,0,1,1,1,1,1,1,1,2,0,1,1,2,0,1,2,0,1,2,0,2,2,1,2,2,1,1,1,1,1,1,2,1,0,2,1,1,2,0,1,2,1,1,1,0,1,1,0,0,1,1,2,0,0,0,2,1,1,0,2,2,1,1,0,1,1,2,1,2,2,2,1,1,1,1,1,2,2,1,1,2,2,1,0,1,1,2,1,1,2,1,0,0,1,2,2,1,2,1,2,0,0,1,1,1,2,0,1,2,2,0,1,0,2,0,2,1,1,2,1,1,2,2,2,1,1,0,0,1,1,2,2,1,0,1,0,1,1,1,2,1,0,0,2,0,0,1,0,1,1,1,1,0,1,1,0,1,1,1,1,2,1,0,1,0,1,2,0,1,1,1,1,1,2,2,1,0,0,1,2,0,2,1,1,2,0,0,2,2,1,0,0,1,0,0,2,2,2,0,2,1,0,1,1,1,1,1,0,1,2,0,1,1,0,0,1,0,0,2,2,2,1,0,1,1,1,0,1,1,1,1,0,1,0,2,0,0,1,1,2,2,1,1,1,2,2,1,1,1,1,0,1,1,0,0,0,2,2,1,2,1,1,1,0,1,1,0,2,1,0,1,1,1,0,0,0,1,1,1,1,1,2,1,0,1,1,2,1,0,1,0,1,1,1,1,1,1,1,0,1,1,1,0,0,1,0,1,1,1,2,0,1,1,0,1,1,2,1,1,0,0,0,1,1,0,0,1,1,2,0,1,2,1,0,1,1,1,1,1,0,1,1,1,1,2,1,0,1,1,1,1,1,2,1,0,1,2,0,0,2,0,0,2,1,1,2,2,1,1,1,2,1,0,2,2,1,2,2,0,2,2,1,0,2,1,1,1,2,1,1,0,1,1,2,1,1,1,1,1,1,1,2,0,1,1,1,0,1,1,0,1,0,1,1,0,1,0,0,2,1,1,1,2,0,1,2,0,0,2,0,2,0,0,1,1,2,0,1,1,2,2,2,2,1,0,0,1,1,1,0,1,1,0,1,0,1,0,1,1,1,1,1,2,2,1,0,0,1,2,1,0,1,0,0,1,1,2,0,1,2,0,1,2,1,0,0,0,1,1,1,1,0,1,1,2,1,2,0,0,1,0,1,1,2,0,1,0,2,2,0,1,0,1,1,0,2,2,1,1,1,0,0,2,1,0,1,2,0,2,1,2,2,1,1,1,1,1,1,0,1,1,1,1,1,0,2,1,1,0,1,1,1,0,1,2,1,0,0,0,1,1,1,2,0,0,1,2,1,0,2,2,1,1,0,0,2,2,2,1,1,1,1,1,0,0,1,1,1,2,1,1,0,1,1,0,1,1,2,0,1,0,2,1,1,2,0,1,1,2,0,0,1,0,1,1,0,1,1,0,2,2,1,2,1,1,0,0,1,0,0,0,1,2,1,0,1,1,1,1,0,0,1,1,1,1,1,1,1,0,0,1,0,2,1,1,1,2,0,0,2,1,0,2,1,1,0,1,2,1,0,0,1,0,2,1,2,2,1,1,1,1,1,2,2,1,0,1,1,1,1,1,1,1,2,2,1,1,0,2,1,1,1,2,1,2,2,1,1,1,0,0,1,1,1,2,1,0,2,1,1,1,0,0,0,1,2,0,1,1,0,0,0,1,0,2,1,1,0,0,2,0,0,2,1,0,1,1,2,1,1,0,0,2,0,1,0,2,1,0,0,0,2,1,1,1,1,1,1,1,1,0,2,1,2,1,1,2,1,0,2,1,1,2,1,0,1,2,0,2,0,1,2,1,1,0,1,0,1,1,1,2,0,0,1,2,2,1,1,0,2,0,1,1,1,0,0,1,1,1,1,1,2,1,2,1,1,1,0,0,0,1,0,0,0,0,1,0,2,1,1,2,0,1,2,1,2,2,1,1,2,1,0,0,1,0,1,1,0,2,0,1,1,1,2,1,1,1,0,1,0,0,1,1,1,2,0,1,0,1,0,1,2,2,2,1,2,1,1,2,0,2,1,1,0,1,2,2,1,1,1,2,1,1,1,1,1,1,1,1,2,1,1,1,1,2,1,1,2,0,2,0,1,1,1,0,2,1,1,1,0,1,2,0,1,1,0,2,1,1,1,2,2,1,0,1,2,2,0,1,1,1,1,0,0,1,0,1,0,1,2,1,2,0,1,0,2,2,1,1,0,0,1,2,0,0,1,0,1,0,1,2,2,1,0,1,1,2,1,0,1,2,2,2,0,1,0,1,1,0,2,1,1,0,0,0,2,2,1,0,1,1,2,1,0,2,2,0,1,0,1,2,1,1,2,0,1,1,2,1,0,1,1,1,2,2,1,0,1,2,0,1,2,1,1,0,1,2,0,1,0,0,1,1,2,1,1,1,1,0,2,0,1,1,0,1,1,2,1,0,0,2,2,1,0,2,0,0,2,0,1,1,1,0,2,0,2,0,2,1,0,1,1,2,2,0,1,1,1,2,1,0,1,2,0,0,2,1,1,0,1,2,1,0,2,2,2,0,1,1,1,2,0,0,1,2,1,1,1,1,0,0,1,1,1,2,1,0,2,1,2,1,2,1,1,1,1,2,1,0,0,2,2,0,1,1,2,1,2,1,1,2,0,1,1,1,1,0,1,0,1,2,0,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,0,1,2,2,2,1,1,1,1,0,1,2,1,0,1,1,1,1,2,2,0,0,1,2,1,2,1,1,0,1,1,1,1,2,0,1,0,1,1,2,2,2,1,0,1,1,0,0,0,2,1,2,0,1,1,2,1,1,1,1,1,0,0,1,0,1,1,0,0,1,0,2,2,2,2,1,2,2,1,2,2,1,1,0,0,1,0,2,2,0,1,2,1,1,1,1,1,1,1,1,2,1,0,1,2,1,0,2,1,1,0,1,0,1,1,1,2,2,1,1,0,2,2,0,2,1,1,1,1,2,1,1,1,0,1,1,1,1,2,1,1,1,1,2,2,0,1,0,1,0,0,2,1,0,0,1,2,1,0,2,1,1,1,1,0,1,0,0,2,2,0,1,1,1,0,0,0,1,0,2,2,1,0,2,2,2,2,0,0,2,1,0,1,1,2,0,1,2,0,0,0,0,2,1,2,1,1,2,1,0,1,0,1,0,1,0,1,1,1,1,1,1,1,1,1,1,0,2,2,1,1,1,1,1,1,0,2,1,0,2,1,1,0,1,1,1,1,1,2,1,2,2,1,1,1,1,0,1,1,1,1,1,1,1,2,2,0,1,2,0,1,0,0,2,1,0,1,1,2,1,2,0,2,0,1,1,1,1,0,1,1,1,0,1,0,0,1,1,1,0,0,0,2,1,1,0,0,1,2,0,0,2,0,0,1,0,2,1,1,2,0,2,2,2,1,1,1,1,2,0,1,2,0,2,2,0,1,1,2,2,1,0,1,0,1,1,2,1,2,1,1,0,1,2,0,0,1,2,1,2,1,1,2,2,1,1,1,1,1,0,1,2,0,2,2,1,1,1,1,1,0,1,0,0,0,1,1,1,1,1,2,2,1,0,1,2,1,2,0,1,1,1,1,1,1,0,1,2,1,0,0,1,1,1,1,1,2,1,1,2,2,1,2,1,1,1,1,2,0,0,1,1,1,1,0,0,2,1,1,0,2,1,2,0,0,1,2,2,2,2,2,2,0,1,2,1,1,0,2,2,1,2,1,1,2,1,1,1,1,1,0,1,1,1,1,2,1,2,1,0,2,2,0,0,1,2,1,0,1,1,1,2,1,1,0,0,0,2,2,1,0,1,1,2,1,2,1,1,2,1,0,1,1,0,1,0,1,0,1,2,1,1,2,0,0,1,1,2,0,1,2,1,1,1,1,0,1,0,2,1,1,1,0,0,2,1,1,1,0,2,1,1,2,2,2,1,0,2,0,0,2,1,0,2,1,1,1,1,1,1,2,0,0,2,0,0,2,1,1,2,2,0,1,1,2,1,0,1,1,0,1,0,0,2,0,1,2,1,0,1,1,1,1,1,2,1,1,1,1,2,1,1,0,0,0,2,1,0,1,2,2,1,1,0,1,1,2,2,2,0,0,1,0,0,2,2,1,1,0,2,0,1,2,1,1,2,2,1,1,1,2,1,1,1,2,0,1,2,0,2,2,2,1,0,0,1,1,0,1,2,1,0,1,1,1,1,0,0,2,0,1,1,2,1,1,1,2,2,2,1,0,1,0,2,0,1,2,2,1,2,1,1,0,1,2,2,1,0,0,1,2,1,2,0,1,2,1,1,1,2,1,1,1,1,1,0,2,1,0,1,1,2,1,2,2,1,1,1,1,2,1,1,2,1,1,1,1,0,1,1,2,1,1,1,1,1,1,2,2,1,0,1,1,0,2,0,0,0,1,0,0,1,0,1,0,1,0,1,1,1,1,2,0,1,1,1,2,1,0,2,0,1,1,1,1,1,0,2,1,1,1,1,1,1,0,0,1,0,1,1,0,0,0,2,0,1,1,2,1,1,1,2,2,0,2,1,1,1,1,1,2,1,1,0,0,2,1,1,1,2,0,0,1,2,1,0,1,1,1,1,1,0,0,1,0,1,1,1,1,0,1,1,1,1,1,1,1,0,1,0,2,1,2,1,1,0,2,0,0,0,2,0,1,2,1,2,0,1,2,1,0,0,0,0,2,2,1,1,2,1,1,1,1,0,0,2,0,0,2,1,2,2,0,2,1,1,0,2,2,2,1,1,1,1,2,1,2,1,0,0,0,1,2,2,0,1,1,1,0,1,1,1,1,0,1,1,2,2,1,0,1,1,2,1,1,1,2,1,2,1,1,1,0,2,2,1,1,2,2,1,1,0,2,1,2,1,0,2,2,0,1,1,0,0,0,0,1,1,2,1,1,0,2,0,0,1,0,2,0,2,2,0,1,2,0,1,1,2,2,1,0,2,1,1,0,2,2,0,1,0,2,1,0,0,1,1,0,1,1,1,2,1,2,2,0,1,2,0,2,1,1,2,1,1,2,0,1,1,1,1,1,2,2,1,0,1,1,2,1,2,0,0,1,2,1,1,1,0,1,2,1,2,1,0,2,1,1,1,1,1,2,1,1,1,0,1,2,1,0,0,1,1,0,2,1,1,0,1,0,0,1,1,1,0,2,0,2,2,2,1,1,1,1,1,1,2,2,1,1,1,1,0,2,0,0,2,1,1,0,1,1,0,0,1,2,2,2,1,0,1,2,1,0,2,1,1,1,0,2,1,0,1,1,2,1,1,1,1,1,0,2,1,2,0,2,1,1,0,0,0,0,0,2,0,1,1,2,1,1,1,1,2,2,2,1,0,0,1,1,1,1,1,1,2,1,2,1,1,1,0,1,1,1,1,0,1,1,2,1,0,0,2,0,0,0,1,1,2,0,2,1,2,2,2,1,2,2,0,2,1,2,2,1,0,0,1,1,1,1,2,1,2,1,1,1,1,0,1,1,2,0,2,1,0,1,2,1,1,1,1,2,0,0,1,2,2,0,1,1,1,1,1,1,1,1,0,0,1,1,1,0,0,1,0,2,1,2,1,1,0,1,1,2,1,0,1,2,0,2,1,2,1,2,2,0,1,1,1,1,0,1,2,2,0,0,1,2,1,1,2,0,1,2,1,1,0,1,1,1,2,2,1,2,0,1,0,2,1,0,0,1,1,1,0,1,1,1,1,1,1,0,0,1,0,1,1,1,2,2,1,0,0,1,1,1,0,2,2,1,1,1,1,1,1,0,1,1,0,2,2,2,1,0,1,1,1,2,2,1,1,0,1,1,1,2,1,1,0,1,1,1,1,1,1,1,1,1,1,2,1,0,2,1,1,2,1,1,2,1,1,0,1,1,1,2,1,2,2,2,2,2,1,0,2,1,2,1,2,1,1,1,2,2,2,1,2,0,2,1,2,1,2,2,1,1,2,2,1,0,1,2,1,1,1,1,1,2,1,2,0,1,0,2,2,2,1,1,2,2,1,0,2,0,0,2,0,1,0,1,2,1,2,2,2,1,1,2,0,1,1,1,0,1,1,2,0,1,1,2,0,1,2,0,1,0,1,1,1,1,0,2,1,1,2,2,1,0,1,0,1,2,0,2,0,1,2,2,1,0,1,1,1,1,2,1,2,1,2,2,2,2,1,1,2,0,1,2,1,0,1,0,0,2,0,2,0,2,1,2,1,0,2,0,2,2,1,1,1,0,2,1,1,2,1,1,2,1,0,0,1,1,0,1,1,0,1,1,0,1,2,1,1,0,0,2,1,2,0,2,1,0,1,1,0,0,1,1,0,1,0,1,1,0,0,1,1,1,1,1,2,1,1,2,2,1,1,1,1,2,2,1,0,1,1,2,1,2,1,2,1,1,0,1,0,2,2,2,0,0,0,1,0,1,1,1,2,1,2,1,2,0,1,1,0,0,2,0,1,0,2,0,2,2,0,2,1,0,1,1,1,1,1,1,1,0,2,0,2,2,1,2,2,2,0,0,2,2,0,0,1,2,1,0,2,1,2,1,2,1,0,1,2,0,1,1,2,2,0,1,1,2,1,0,2,1,2,1,2,1,1,1,0,2,1,1,0,1,0,1,1,1,0,1,2,0,1,2,0,2,1,2,1,2,2,2,2,0,1,2,1,2,2,1,1,1,1,1,2,2,1,0,2,1,1,0,1,0,0,1,1,0,1,1,0,1,2,1,2,0,1,1,2,1,2,2,1,1,1,2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,2,1,2,0,1,2,1,0,2,1,1,2,2,1,0,1,1,1,2,0,1,2,2,1,1,2,0,0,1,1,0,2,0,1,2,0,1,0,0,2,1,2,0,0,2,1,2,1,1,2,1,1,0,1,2,1,1,0,1,1,2,1,0,2,0,2,0,2,1,0,2,2,1,0,0,0,0,0,1,0,1,1,2,1,1,1,1,1,1,0,1,1,1,1,2,1,1,1,1,1,0,1,2,1,2,1,2,2,0,0,1,1,0,1,2,1,1,1,1,2,1,1,0,2,1,1,2,2,1,1,0,2,1,1,1,1,1,2,2,2,0,0,2,1,0,0,1,0,1,1,2,1,1,0,2,1,2,1,2,2,0,0,2,2,2,0,2,1,2,0,2,2,2,2,1,1,1,1,2,1,0,2,2,0,1,2,0,2,0,1,1,1,2,1,1,1,2,1,1,2,2,1,2,0,1,0,0,1,2,2,2,1,1,0,1,1,1,0,0,0,1,1,2,0,1,1,1,1,0,0,1,2,2,1,2,1,2,1,1,2,2,2,1,1,2,0,2,2,2,2,2,1,0,2,2,1,2,0,2,2,1,0,1,1,1,1,1,1,2,1,2,2,1,1,0,0,1,0,2,0,2,0,0,1,1,1,1,1,2,2,0,2,2,0,0,0,0,2,0,2,2,2,1,1,1,2,2,1,2,0,2,0,1,0,0,2,1,1,1,1,1,1,0,1,0,0,2,1,0,1,1,1,1,2,2,1,2,1,0,1,1,2,2,0,2,0,1,0,0,1,1,1,2,1,0,2,0,1,1,2,1,0,2,0,0,1,2,1,0,1,1,1,1,1,1,2,0,1,0,0,1,0,1,1,2,1,1,1,2,1,0,1,1,2,0,1,1,1,1,2,2,1,1,0,0,0,1,0,0,1,1,2,1,0,0,2,2,1,1,1,2,1,2,1,2,0,2,1,1,1,1,0,2,1,1,2,1,2,1,1,1,1,1,2,2,0,2,1,0,1,0,2,1,0,1,2,2,1,1,2,1,1,1,0,0,2,2,1,2,2,1,2,1,1,1,1,0,2,2,0,0,1,1,1,1,2,0,1,2,1,1,0,0,1,2,1,0,1,1,2,0,1,0,2,2,1,1,2,1,1,0,1,0,0,1,1,0,0,1,0,1,1,1,1,2,1,2,0,2,2,1,1,1,1,1,1,1,0,0,1,1,1,2,2,1,1,1,2,1,1,1,1,1,1,0,0,2,0,1,0,0,0,1,0,2,1,1,0,1,0,2,2,1,1,1,0,1,0,1,1,1,1,0,0,1,2,1,1,1,1,1,1,2,1,0,1,0,2,1,0,1,1,1,0,0,1,0,2,2,1,2,0,0,0,0,1,1,1,1,1,1,1,1,0,0,1,2,1,1,1,2,2,1,1,0,0,1,0,1,2,2,0,0,0,1,0,1,1,0,2,0,1,1,1,1,1,2,1,0,1,1,1,2,2,1,1,0,0,0,1,1,1,2,2,1,0,0,1,1,0,2,0,2,0,2,1,1,1,1,0,1,1,2,1,2,1,1,0,1,1,1,0,1,0,1,2,1,0,1,1,2,1,1,1,2,0,0,1,1,2,1,1,0,0,1,2,1,1,0,1,1,2,2,2,1,1,1,2,1,1,0,1,0,0,1,1,2,2,1,0,2,1,1,1,1,1,1,0,2,1,1,1,2,0,2,2,1,2,2,2,1,1,2,2,0,1,0,1,0,1,0,1,0,1,2,1,1,1,0,1,1,1,2,0,0,0,0,2,0,1,2,1,1,1,1,0,1,0,1,1,1,1,1,2,1,0,0,2,0,0,0,1,1,1,1,0,1,1,0,0,1,2,2,1,1,1,1,0,2,1,1,0,0,1,1,0,2,1,1,0,2,0,2,2,2,2,2,2,2,1,0,1,1,1,2,0,1,1,0,1,2,1,2,2,2,2,2,2,1,0,0,1,0,1,1,0,1,1,0,1,2,2,0,2,0,0,0,0,1,0,0,1,0,1,0,2,1,1,2,1,1,1,0,2,1,2,1,0,1,1,0,1,1,2,0,1,1,1,0,1,1,1,1,1,1,1,2,1,2,1,0,2,0,2,0,2,2,1,2,2,0,0,1,1,0,2,1,2,1,0,2,1,0,1,1,0,1,1,2,2,1,1,1,1,1,0,1,1,2,0,0,0,1,0,2,2,1,1,0,2,1,2,1,1,1,2,1,2,1,1,0,1,0,2,0,2,1,2,0,1,2,1,0,2,1,1,2,1,2,0,1,0,0,2,1,1,1,0,1,0,1,0,0,0,1,1,1,2,0,1,0,1,0,1,0,0,1,2,2,2,1,0,1,1,1,0,1,0,1,1,0,1,0,2,1,1,1,2,1,0,1,1,0,1,1,0,2,1,0,1,1,1,0,1,0,0,1,2,0,0,1,0,0,1,2,0,2,0,2,1,0,1,2,1,1,0,1,1,1,2,1,1,1,1,2,1,1,0,1,0,0,1,2,0,1,1,2,1,1,2,1,0,2,1,1,1,0,1,1,1,2,0,2,2,1,1,2,1,1,2,1,1,0,1,1,0,2,0,2,1,0,2,1,0,1,2,1,0,1,1,1,1,1,2,2,1,1,2,1,2,1,1,2,1,1,2,0,1,1,1,1,1,2,1,1,1,1,1,1,1,0,2,2,1,0,1,2,1,1,2,0,1,0,2,2,0,2,0,1,0,1,1,1,2,1,2,1,1,0,0,1,1,0,1,1,1,2,1,1,1,1,2,1,1,1,1,1,2,1,1,0,0,0,0,0,2,2,1,1,1,0,2,1,1,1,0,1,0,1,2,2,0,1,0,2,1,1,1,1,0,1,2,0,1,1,1,1,2,2,1,1,1,1,0,2,0,1,2,1,1,1,1,1,2,2,1,0,1,2,1,2,2,1,0,0,2,2,1,1,0,1,1,1,1,1,1,0,1,2,1,1,2,1,0,1,1,1,1,0,2,0,1,0,0,1,0,2,1,2,0,1,0,1,2,1,0,2,2,1,1,2,1,1,1,2,1,2,2,1,1,1,0,1,1,1,1,0,2,0,0,0,1,0,1,1,1,1,1,2,1,0,1,1,1,2,1,1,1,1,1,1,0,2,1,1,2,2,2,1,0,2,1,1,0,1,2,1,1,2,0,2,1,1,2,2,1,2,2,1,1,2,1,0,1,2,1,0,1,1,1,1,1,1,1,2,2,1,2,2,2,1,2,1,1,1,0,1,2,1,0,1,2,1,2,1,2,2,2,2,0,2,2,2,1,1,1,1,1,1,2,1,2,1,1,1,2,1,2,1,1,2,1,0,0,2,1,1,0,0,1,0,1,0,2,1,2,0,1,1,1,1,1,0,2,1,2,1,1,1,1,1,1,2,0,1,1,1,1,2,2,0,2,1,2,1,1,0,1,1,1,2,0,1,1,1,2,2,1,1,1,2,1,1,2,1,0,2,1,0,0,2,1,1,0,2,2,1,1,1,1,2,1,2,1,1,2,1,1,1,1,1,0,0,0,1,1,2,2,0,1,1,1,1,2,1,1,1,1,2,1,1,1,2,1,1,1,1,1,0,2,0,1,1,2,1,1,0,2,1,1,0,2,0,1,1,1,2,1,1,0,2,0,2,1,2,1,1,2,0,1,0,1,0,0,1,0,0,1,2,1,1,1,2,1,1,1,0,1,1,1,1,0,1,2,0,2,2,0,1,1,1,2,1,0,1,2,2,1,1,1,1,1,0,1,1,1,2,1,1,2,1,2,1,2,0,0,2,0,2,2,2,2,2,0,0,2,1,2,1,2,1,1,1,1,2,1,2,2,1,1,0,1,1,1,1,2,1,0,2,0,0,0,2,1,0,2,1,2,1,0,1,0,0,2,1,2,2,1,1,2,2,0,0,2,0,1,2,1,1,0,2,2,1,0,2,1,0,1,1,0,1,1,1,1,0,0,0,2,1,1,1,1,1,0,0,0,2,1,0,1,1,1,1,0,0,2,2,1,2,0,1,1,0,1,2,2,0,2,1,1,2,0,1,1,0,1,1,0,2,1,1,1,1,1,0,2,2,2,0,1,1,1,2,0,1,2,2,2,2,2,1,0,1,1,1,0,2,2,2,1,1,1,1,1,2,1,1,1,0,0,1,2,1,0,1,1,2,1,1,0,1,0,2,1,0,2,0,1,1,0,1,1,2,1,2,2,2,1,1,1,0,2,1,2,1,2,1,1,1,1,1,0,1,2,0,1,1,0,1,2,1,1,0,2,2,1,2,2,0,0,0,2,0,2,1,1,1,1,1,1,2,2,2,1,1,2,2,1,2,2,2,0,0,1,2,1,0,0,0,1,0,1,1,2,1,0,1,1,0,1,1,1,2,0,2,0,1,0,1,1,1,0,1,1,1,1,1,2,1,0,1,2,2,1,1,1,1,2,1,0,2,2,0,2,1,0,2,1,0,1,1,2,1,1,0,1,1,1,2,0,2,0,0,1,1,1,1,0,1,2,1,2,1,1,2,1,2,2,1,1,1,2,2,0,1,2,2,1,2,1,0,0,1,1,1,1,1,0,2,1,1,1,2,2,1,1,2,2,0,1,1,0,0,1,1,1,1,1,1,1,1,0,0,2,0,1,1,0,1,1,2,1,1,0,1,2,1,1,1,1,1,1,2,2,1,1,1,0,1,2,0,1,0,0,2,1,1,1,2,2,0,0,2,0,0,0,0,0,2,1,2,0,2,1,2,1,0,1,2,1,1,1,2,2,1,1,0,1,0,1,1,1,1,1,0,1,1,1,0,2,1,1,0,2,2,1,2,1,0,0,2,0,1,2,1,1,0,2,1,0,2,2,1,1,2,0,1,1,2,0,2,1,1,2,2,1,2,2,1,2,1,1,1,0,0,1,1,1,1,1,1,0,1,0,2,1,1,2,1,1,0,0,0,1,2,0,1,0,2,1,1,0,1,2,1,2,1,0,2,1,1,2,0,2,0,1,0,1,1,2,0,1,1,0,1,1,2,1,1,1,0,1,1,1,2,1,0,1,1,1,0,1,0,2,1,2,2,1,1,1,1,1,2,1,1,0,1,1,2,2,0,1,2,1,1,0,1,1,2,1,0,0,1,1,1,1,2,1,1,1,1,1,1,1,1,0,1,0,1,1,1,2,0,1,0,0,1,0,0,1,2,0,2,1,0,1,2,1,2,1,0,2,1,1,0,1,2,2,0,1,1,1,0,1,0,1,1,1,1,1,0,1,1,1,1,2,2,0,1,1,0,1,1,1,2,1,2,1,1,1,1,0,2,0,1,1,0,1,2,0,1,1,1,1,1,1,2,1,2,1,0,1,2,2,1,1,2,1,1,2,1,1,0,0,1,0,1,1,1,0,1,0,1,1,1,1,0,1,0,2,1,2,1,1,1,1,0,1,1,1,0,0,1,2,1,1,1,0,2,2,1,1,1,0,0,0,1,1,1,2,1,0,1,1,1,1,0,1,1,1,0,0,0,1,1,1,0,0,1,1,0,2,1,2,1,2,1,1,1,2,0,2,2,1,1,2,1,2,1,2,1,1,1,2,0,2,1,0,0,0,0,1,0,2,1,0,2,1,2,1,1,1,2,0,1,1,1,1,2,0,2,2,1,0,1,1,0,2,2,0,2,1,0,1,0,2,2,0,1,2,0,2,0,1,0,1,2,2,1,0,0,1,1,2,1,0,0,1,1,1,2,1,1,1,2,0,1,0,0,0,1,0,1,0,0,2,0,0,1,0,0,1,1,1,0,2,0,2,2,2,1,1,0,2,0,1,1,1,1,2,1,1,0,2,2,2,1,1,2,0,1,0,1,0,2,2,0,2,2,0,1,1,1,1,0,1,0,1,1,1,0,2,1,1,0,0,0,0,1,2,0,0,1,1,1,1,1,2,1,0,2,2,1,2,1,1,1,1,0,2,2,0,1,1,0,0,2,0,1,1,0,0,1,0,2,1,1,2,1,2,2,2,2,0,1,1,1,2,1,1,0,1,0,1,0,1,1,0,1,0,2,2,2,1,1,2,0,0,1,0,2,2,0,1,2,0,2,2,1,1,2,2,1,2,0,0,2,2,0,1,1,2,1,1,1,0,0,1,1,0,0,1,1,1,2,1,1,0,1,1,2,0,2,0,2,1,0,1,0,1,2,1,1,0,1,1,2,1,1,0,1,1,1,1,0,1,0,0,2,0,0,1,0,2,0,0,2,0,2,2,1,2,2,2,2,0,1,0,0,2,0,1,1,2,0,1,1,1,2,2,2,1,1,2,1,1,1,0,2,0,1,2,0,1,2,0,1,0,0,2,0,1,1,0,2,1,2,0,2,1,2,1,0,2,1,0,2,1,1,0,1,0,2,1,0,2,0,1,2,2,1,0,0,1,0,1,0,2,0,2,1,1,1,1,1,1,0,1,1,2,2,2,1,2,2,1,2,1,1,0,0,1,0,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1,2,1,1,1,1,1,2,0,2,2,1,1,2,0,1,2,0,2,1,1,1,2,1,0,0,1,2,0,0,1,0,1,0,1,0,1,1,2,1,1,0,1,2,1,1,1,2,2,1,0,2,1,1,0,2,2,0,1,0,1,2,2,0,1,1,2,1,0,1,1,1,1,1,1,2,1,1,2,1,0,1,0,0,0,1,0,0,1,0,1,1,0,1,2,0,1,2,1,1,0,1,1,2,2,1,2,2,1,1,2,1,1,2,2,2,0,1,0,0,2,2,0,2,0,1,0,0,1,1,0,2,1,0,2,0,1,1,1,1,0,0,0,1,1,1,0,1,0,1,0,1,1,1,0,1,0,0,1,0,1,1,1,0,0,1,0,2,1,1,2,2,1,2,0,1,1,0,2,0,1,2,0,0,1,1,1,2,1,0,1,0,0,2,1,0,1,2,2,0,0,2,0,2,2,2,0,1,1,1,0,0,2,1,2,0,1,1,1,1,1,1,1,1,2,2,1,2,1,0,1,2,1,1,1,1,0,2,1,2,0,1,1,0,1,0,1,0,1,2,1,1,1,1,0,1,0,2,2,1,0,1,0,0,0,0,0,1,2,2,0,2,2,1,0,1,1,2,1,1,1,1,2,2,2,0,0,1,1,1,2,1,1,0,1,0,1,0,2,2,1,2,2,0,2,1,1,2,0,2,0,2,0,1,1,0,1,0,0,1,1,1,1,1,1,1,0,0,2,0,2,1,2,0,1,0,1,1,2,2,1,1,2,2,1,1,0,1,0,2,0,1,2,2,1,0,1,0,1,1,1,0,1,0,0,1,1,0,1,0,1,0,1,2,1,0,2,1,0,1,1,2,2,2,0,1,1,1,1,0,2,1,1,2,2,1,2,1,1,2,0,0,0,2,1,0,1,1,0,1,1,0,0,1,1,2,0,1,0,2,2,2,2,1,2,0,1,2,1,2,1,1,0,1,1,0,1,0,2,1,0,1,1,1,1,1,1,0,1,2,0,1,2,2,2,1,1,2,1,2,1,0,1,1,0,1,0,1,0,1,1,0,1,1,1,2,1,2,1,0,0,0,1,1,1,1,1,2,2,2,1,0,2,1,1,0,1,2,1,2,1,1,0,1,2,1,2,2,0,1,1,1,2,1,1,2,0,1,0,2,2,1,1,0,1,1,1,0,0,2,2,1,1,2,1,1,2,0,1,2,1,1,1,1,0,1,1,2,0,2,0,1,1,0,1,1,1,1,0,0,1,1,1,0,0,1,0,1,1,2,0,2,1,2,1,1,1,1,1,2,0,1,2,1,1,1,2,0,1,2,2,1,1,1,1,2,0,1,2,2,2,1,1,1,2,1,0,2,2,1,1,0,2,2,0,2,2,1,2,2,2,1,1,1,1,2,0,1,1,0,0,0,1,1,1,1,2,2,0,2,2,0,2,1,0,0,0,2,1,2,0,1,1,0,1,0,2,1,0,1,1,1,2,1,1,1,2,0,2,2,1,1,1,1,0,2,0,1,2,0,2,1,0,0,0,1,1,1,1,0,0,1,1,0,2,2,0,1,1,0,1,0,1,2,1,1,2,0,2,2,1,1,0,1,1,1,1,1,2,2,2,1,2,1,0,0,1,1,1,2,1,1,2,0,1,2,0,1,2,1,1,0,1,2,1,1,0,0,0,0,0,0,2,2,1,1,2,1,1,1,1,1,0,2,0,0,1,1,0,1,1,1,1,1,1,0,2,1,1,1,0,0,0,1,0,2,0,2,0,2,0,1,0,1,2,2,0,0,2,1,0,1,0,1,1,2,1,0,1,2,1,1,2,0,2,0,2,0,0,1,0,1,2,1,0,1,2,1,1,1,0,0,1,1,0,1,2,2,0,1,0,1,1,1,2,0,1,1,2,2,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,2,1,0,1,1,1,0,1,2,2,1,2,1,1,1,0,0,2,2,1,1,0,2,1,0,0,1,2,1,1,0,1,0,2,1,1,2,2,1,1,2,1,2,0,2,1,1,2,1,1,0,0,1,0,2,1,2,0,1,0,1,0,2,1,2,1,0,2,2,1,1,1,0,1,1,1,1,0,2,1,0,1,0,2,1,0,0,1,1,1,0,1,0,0,0,1,1,1,2,1,1,1,1,1,2,1,2,2,0,2,2,1,0,2,0,1,1,2,0,1,1,0,0,0,0,1,1,1,0,0,1,1,1,1,1,1,0,1,2,0,2,0,1,1,1,2,1,2,1,0,2,2,2,0,2,1,1,0,0,0,0,0,1,1,0,1,1,1,0,0,1,2,1,2,0,2,2,2,2,0,0,2,1,1,1,2,0,0,2,1,2,0,0,1,1,1,1,0,2,1,1,2,1,2,1,0,0,0,1,1,1,1,2,0,0,1,1,1,2,1,0,0,2,0,1,0,0,1,1,0,2,2,1,0,1,1,1,2,1,1,1,1,1,2,1,1,0,0,1,0,2,1,1,2,1,2,0,1,0,1,2,1,0,2,0,1,0,1,1,0,1,1,0,0,2,2,2,2,1,2,2,1,1,1,2,0,2,1,1,1,2,1,1,1,2,1,2,2,1,0,1,2,2,1,0,1,1,0,1,1,1,1,1,1,2,2,1,2,2,1,1,1,1,0,1,1,0,2,1,0,0,0,1,0,2,0,1,1,1,0,0,0,0,1,1,0,1,2,1,0,0,1,2,1,1,0,2,1,0,1,1,0,1,2,0,0,1,1,2,1,1,2,0,1,1,0,1,1,1,2,1,2,1,1,0,1,1,1,1,1,1,2,0,0,0,1,2,2,1,2,1,1,1,1,1,1,1,1,1,1,0,1,1,1,2,2,0,1,2,2,1,0,2,1,2,2,1,0,2,2,2,1,2,1,1,1,1,1,2,1,2,1,2,2,1,0,0,1,1,0,2,2,1,1,1,1,2,2,0,0,1,1,0,2,0,1,0,2,1,1,1,2,2,1,1,0,1,1,2,2,1,1,1,1,1,1,1,2,0,1,1,1,0,2,2,2,1,2,0,2,2,1,1,0,1,1,0,1,0,1,1,1,1,2,1,1,1,0,1,1,1,0,0,1,1,0,0,1,1,1,1,1,2,1,1,0,1,1,0,1,2,1,0,2,0,1,0,0,1,1,1,1,0,2,0,0,2,0,0,1,1,2,1,2,0,1,1,2,0,2,1,1,2,1,1,2,1,0,1,1,2,1,0,1,2,1,1,2,0,2,2,1,1,1,0,2,1,1,0,2,1,1,0,1,2,0,2,2,0,2,2,1,1,2,1,2,0,2,1,1,2,1,1,0,2,2,1,1,2,1,1,1,1,2,2,1,1,1,1,0,0,0,2,0,2,1,1,1,0,1,0,2,1,0,2,1,0,0,2,2,2,1,1,0,1,1,0,1,1,1,1,1,1,1,1,2,1,0,2,1,1,2,2,1,1,0,0,1,0,1,1,1,1,1,1,1,0,0,0,2,1,2,2,1,0,2,1,1,1,0,1,1,1,0,0,0,2,1,2,0,2,0,1,2,1,2,2,1,1,0,2,2,1,0,2,1,0,2,2,2,2,0,2,1,2,1,1,0,0,1,1,1,0,1,2,2,1,1,0,2,1,1,0,1,1,2,0,1,0,0,0,1,1,1,1,0,0,2,0,0,1,1,0,2,1,2,0,1,0,1,0,1,2,1,0,1,1,0,2,0,0,1,1,1,2,1,0,1,0,0,1,2,2,1,1,2,2,2,2,1,2,0,1,2,0,1,1,1,1,0,2,2,0,0,1,1,1,0,1,1,0,2,2,1,0,2,2,1,1,1,1,1,0,0,1,0,2,2,2,0,0,0,2,2,1,1,1,1,0,1,0,1,1,1,1,2,1,2,1,0,0,1,0,1,2,1,0,2,1,2,2,2,1,2,0,1,1,0,2,0,1,0,1,1,0,0,2,1,2,1,2,0,1,1,2,0,0,1,2,2,0,2,2,2,2,0,2,1,1,2,0,1,0,2,1,0,1,2,0,1,0,0,1,1,1,1,2,1,2,1,2,0,0,1,0,0,0,1,1,0,1,2,1,2,0,1,1,1,0,1,0,1,1,0,1,1,1,1,0,1,1,1,1,2,2,0,2,0,2,1,2,1,1,1,1,2,2,1,0,0,0,1,1,2,1,1,1,1,1,1,1,2,0,2,1,0,0,0,1,1,1,2,2,0,0,1,0,1,0,1,2,1,0,1,1,2,1,2,1,1,1,2,0,1,2,2,0,0,0,0,0,1,2,2,1,1,2,1,0,2,2,2,2,0,1,1,2,1,2,0,1,1,0,1,2,2,0,0,1,2,2,2,2,1,0,2,1,1,1,2,0,0,1,0,1,2,2,1,1,0,1,1,2,1,2,1,1,2,1,2,1,1,0,2,1,1,1,1,0,0,0,1,1,0,0,2,1,2,1,1,1,2,0,0,1,1,2,0,1,0,2,0,0,1,0,0,0,1,1,0,1,2,0,0,2,1,2,1,0,0,1,0,2,1,0,1,0,1,0,1,1,1,1,1,1,1,1,1,2,2,1,1,1,2,2,0,0,0,1,2,2,1,1,1,1,0,2,0,1,0,1,1,1,2,1,1,2,1,0,0,1,1,1,2,1,2,1,1,2,1,0,1,1,1,2,1,1,0,1,0,1,1,1,0,0,0,1,0,1,0,0,0,0,2,0,0,1,2,1,0,1,2,1,1,1,0,2,1,2,2,2,1,1,1,0,0,0,1,1,1,2,2,1,2,2,2,0,0,0,1,1,1,1,1,1,2,0,1,0,2,2,2,1,1,0,1,2,1,0,1,1,1,2,2,2,0,2,1,0,1,2,1,0,0,1,2,1,1,1,0,0,1,1,1,2,1,2,2,1,0,0,1,2,0,2,1,1,1,0,1,2,1,2,1,1,1,2,1,1,1,1,1,1,0,1,1,1,1,2,0,2,1,2,1,2,1,1,1,1,2,1,2,1,1,1,1,1,0,1,1,0,2,0,1,2,1,1,0,2,2,1,1,1,2,1,2,1,1,2,2,1,2,2,0,2,1,2,1,1,2,1,2,1,1,2,0,2,1,2,1,1,1,1,1,1,0,1,2,1,1,1,1,1,0,1,1,0,0,0,0,2,0,1,0,1,1,2,2,1,0,1,1,1,1,1,2,0,1,2,1,1,1,1,1,1,0,0,1,2,1,1,1,2,1,1,2,1,2,1,1,1,0,1,1,1,1,2,1,2,1,1,1,0,1,0,2,1,0,0,1,1,0,2,0,1,0,1,1,2,1,1,0,2,1,0,0,1,0,1,2,1,1,0,0,2,0,1,0,2,0,1,0,1,2,0,0,2,1,1,0,1,1,0,0,1,2,1,1,1,1,0,1,1,1,1,0,1,1,0,1,0,1,1,1,2,0,1,1,1,0,1,1,1,0,2,1,1,2,0,1,1,0,2,1,2,0,2,1,1,0,2,0,1,2,0,2,2,0,1,1,2,0,0,1,2,0,1,1,1,1,2,1}));
        }
    }

    static class Solution5 {
        static final int MOD = 1000000007;
        public int[] pathsWithMaxScore(List<String> board) {
            int n = board.size();
            int m = board.get(0).length();
            int[][][] dp = new int[n][m][2];
            // for (int i = 0; i < n; i++) {
            //     for (int j = 0; j < m; j++) {
            //         dp[i][j][0] = -1;
            //         dp[i][j][1] = -1;
            //     }
            // }
            int[] res = findPath(board, n-1, m - 1, dp);
            if (res[0] == -1) return new int[]{0, 0};
            return res;
        }

        int[] findPath(List<String> board, int r, int c, int[][][] dp) {
            if (r == 0 && c == 0) return new int[]{0, 1};

            if (dp[r][c][0] != 0) {
                return dp[r][c];
            }

            if (board.get(r).charAt(c) != 'X') {
                int score = -1;
                int path = -1;
                int curr = board.get(r).charAt(c) == 'S' ? 0 : board.get(r).charAt(c) - '0';
                if (r > 0 && board.get(r-1).charAt(c) != 'X') {
                    int[] up = findPath(board, r - 1, c, dp);
                    if (up[0] != -1) {
                        score = curr + up[0];
                        path = up[1];
                    }
                }
                if (c > 0 && board.get(r).charAt(c - 1) != 'X') {
                    int[] left = findPath(board, r, c - 1, dp);
                    if (left[0] != -1) {
                        if (left[0] + curr > score) {
                            score = curr + left[0];
                            path = left[1];
                        } else if (left[0] + curr == score) {
                            path += left[1];
                            if (path > MOD) {
                                path -= MOD;
                            }
                        }
                    }
                }
                if (r > 0 && board.get(r-1).charAt(c) == 'X' && c > 0 && board.get(r).charAt(c - 1) == 'X' && board.get(r - 1).charAt(c - 1) != 'X') {
                    int[] ul = findPath(board, r - 1, c - 1, dp);
                    if (ul[0] != -1) {
                        score = curr + ul[0];
                        path = ul[1];
                    }
                }
                dp[r][c][0] = score;
                dp[r][c][1] = path;
            }

            return dp[r][c];
        }

        public static void main(String[] args) {
            //System.out.println(new Solution5().pathsWithMaxScore(Arrays.asList("E11345","X452XX","3X43X4","44X312","23452X","1342XS")));
            System.out.println(-4 % 10);
        }
    }

    static class Solution6 {
        public int distinctEchoSubstrings(String text) {
            int n = text.length();
            char[] s = text.toCharArray();
            int[][] lcp = new int[n][n];
            for(int i = n-1;i >= 0;i--){
                for(int j = i+1;j < n;j++){
                    if(s[i] == s[j]){
                        lcp[i][j] = (j+1 < n ? lcp[i+1][j+1] : 0) + 1;
                    }
                }
            }

            RollingHash rh = new RollingHash(9999, new RollingHashFactory(2, 10000, new Random()));
            for(int i =0;i < n;i++){
                rh.add(s[i]);
            }

            Set<Long> set = new HashSet<>();
            for(int i = 0;i < n;i++){
                for(int j = i+1;j < n;j++){
                    int len =j-i;
                    if(j+len <= n && lcp[i][j] >= len){
                        set.add(rh.queryTwin(i, j));
                    }
                }
            }
            return set.size();
        }

        public class RollingHash
        {
            public RollingHashFactory rhf;
            public long[][] buf;
            public int p;

            public RollingHash(int bufsize, RollingHashFactory rhf)
            {
                buf = new long[rhf.deg][bufsize+1];
                this.rhf = rhf;
                this.p = 1;
            }

            public void add(int c)
            {
                for(int i = 0;i < rhf.deg;i++)buf[i][p] = (buf[i][p-1]*rhf.muls[i]+c)%rhf.mods[i];
                p++;
            }

            public void addr(int c)
            {
                for(int i = 0;i < rhf.deg;i++)buf[i][p] = (buf[i][p-1]+rhf.powers[i][p-1]*c)%rhf.mods[i];
                p++;
            }

            public long queryTwin(int r)
            {
                return buf[0][r]<<32|buf[1][r];
            }

            public long queryTwin(int l, int r)
            {
                assert l <= r;
                assert rhf.deg == 2;
                long h = 0;
                for(int i = 0;i < rhf.deg;i++){
                    long v = (buf[i][r] - buf[i][l] * rhf.powers[i][r-l]) % rhf.mods[i];
                    if(v < 0)v += rhf.mods[i];
                    h = h<<32|v;
                }
                return h;
            }

            public long[] query(int l, int r)
            {
                assert l <= r;
                long[] h = new long[rhf.deg];
                for(int i = 0;i < rhf.deg;i++){
                    h[i] = (buf[i][r] - buf[i][l] * rhf.powers[i][r-l]) % rhf.mods[i];
                    if(h[i] < 0)h[i] += rhf.mods[i];
                }
                return h;
            }

            public long add(long a, long b, int w, RollingHashFactory rhf)
            {
                assert rhf.deg == 2;
                long high = ((a>>>32) * rhf.powers[0][w] + (b>>>32)) % rhf.mods[0];
                long low = ((long)(int)a * rhf.powers[1][w] + (int)b) % rhf.mods[1];
                return high<<32|low;
            }
        }

        public class RollingHashFactory
        {
            public int[] mods;
            public int[] muls;
            public long[][] powers;
            public int deg;

            public RollingHashFactory(int deg, int n, Random gen)
            {
                this.deg = deg;
                mods = new int[deg];
                muls = new int[deg];
                mods[0] = 1000000007; mods[1] = 1000000009;
                muls[0] = 31; muls[1] = 31;
                powers = new long[deg][n+1];
                for(int i = 0;i < deg;i++){
                    powers[i][0] = 1;
                    for(int j = 1;j <= n;j++){
                        powers[i][j] = powers[i][j-1] * muls[i] % mods[i];
                    }
                }
            }
        }

        public static void main(String[] args) {
            System.out.println(new Solution6().distinctEchoSubstrings("abcabcabc"));
        }

    }

    static 	class Solution7 {
        public int minimumDistance(String word) {
            char[] s = word.toCharArray();
            int[][] dp = new int[27][27];
            for(int i = 0;i < 27;i++)Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
            dp[26][26] = 0;
            for(char x : s){
                int[][] ndp = new int[27][27];
                for(int i = 0;i < 27;i++)Arrays.fill(ndp[i], Integer.MAX_VALUE / 2);
                int r = (x-'A')/6;
                int c = (x-'A')%6;
                for(int i = 0;i < 27;i++){
                    for(int j = 0;j < 27;j++){
                        if(i < 26){
                            int ir = i/6, ic = i%6;
                            ndp[x-'A'][j] = Math.min(ndp[x-'A'][j], dp[i][j] + Math.abs(ir-r) + Math.abs(ic-c));
                        }else{
                            ndp[x-'A'][j] = Math.min(ndp[x-'A'][j], dp[i][j]);
                        }
                        if(j < 26){
                            int ir = j/6, ic = j%6;
                            ndp[i][x-'A'] = Math.min(ndp[i][x-'A'], dp[i][j] + Math.abs(ir-r) + Math.abs(ic-c));
                        }else{
                            ndp[i][x-'A'] = Math.min(ndp[i][x-'A'], dp[i][j]);
                        }
                    }
                }
                dp = ndp;
            }
            int ans = Integer.MAX_VALUE;
            for(int i = 0;i < 27;i++){
                for(int j = 0;j < 27;j++){
                    ans= Math.min(ans, dp[i][j]);
                }
            }
            return ans;
        }
    }
}
