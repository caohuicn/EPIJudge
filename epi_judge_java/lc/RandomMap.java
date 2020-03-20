package lc;

import java.util.*;

public class RandomMap<K, V>{
    private HashMap<K, V> map = new HashMap<>();
    private ArrayList<K> keys = new ArrayList<>();
    private HashMap<K, Integer> keyIndex = new HashMap<>();
    private ArrayList<V> values = new ArrayList<>();
    private HashMap<V, Integer> valueIndex = new HashMap<>();
    private HashMap<V, Integer> valueCounts = new HashMap<>();
    private Random r = new Random();


    public V put(K key, V value) {
        boolean exist = map.containsKey(key);
        V v = map.put(key, value);
        if (!exist) {
            keys.add(key);
            keyIndex.put(key, keys.size() - 1);
            increaseValue(value);
        } else {
            //replaced v with value
            if (!Objects.equals(v, value)) {
                decreaseValue(v);
                increaseValue(value);
            }
        }
        return v;
    }

    private void increaseValue(V value) {
        if (valueCounts.containsKey(value)) {
            valueCounts.put(value, valueCounts.get(value) + 1);
        } else {
            valueCounts.put(value, 1);
            values.add(value);
            valueIndex.put(value, values.size() - 1);
        }
    }

    private void decreaseValue(V v) {
        valueCounts.put(v, valueCounts.get(v) - 1);
        if (valueCounts.get(v) == 0) {
            valueCounts.remove(v);
            int ind = valueIndex.get(v);
            Collections.swap(values, ind, values.size() - 1);
            valueIndex.put(values.get(ind), ind);
            values.remove(values.size() - 1);
    }
    }

    public V remove(Object key) {
        boolean exist = map.containsKey(key);
        V v = map.remove(key);
        if (exist) {
            decreaseValue(v);
            int ind = keyIndex.get(key);
            Collections.swap(keys, ind, keys.size() - 1);
            keyIndex.put(keys.get(ind), ind);
            keys.remove(keys.size() - 1);
        }
        return v;
    }

    public V getRandomValue() {
        if (map.isEmpty()) return null;
        int i = r.nextInt(keys.size());
        return map.get(keys.get(i));
    }

    public V getUniformRandomValue() {
        if (map.isEmpty()) return null;
        int i = r.nextInt(values.size());
        return values.get(i);
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public static void main(String[] args) {
        RandomMap<String, String> rm = new RandomMap<>();
        assert rm.getRandomValue() == null;
        assert rm.getUniformRandomValue() == null;
        rm.put("a", "apple");
        assert rm.getRandomValue().equals("apple");
        assert rm.getUniformRandomValue().equals("apple");
        rm.put("b", "banana");
        String randomValue = rm.getRandomValue();
        assert randomValue.equals("apple") || randomValue.equals("banana");
        randomValue = rm.getUniformRandomValue();
        assert randomValue.equals("apple") || randomValue.equals("banana");
        int ca = 0;
        int cb = 0;
        for (int i = 0; i < 1000; i++) {
            randomValue = rm.getRandomValue();
            if (randomValue.equals("apple")) ca++;
            if (randomValue.equals("banana")) cb++;
        }
        System.out.println("ca: " + ca);
        System.out.println("cb: " + cb);
        assert Math.abs(ca - cb) <= 100;

        rm.put("c", "apple");
        ca = 0;
        cb = 0;
        for (int i = 0; i < 1000; i++) {
            randomValue = rm.getRandomValue();
            if (randomValue.equals("apple")) ca++;
            if (randomValue.equals("banana")) cb++;
        }
        System.out.println("when there are 2 apples" );
        System.out.println("ca: " + ca);
        System.out.println("cb: " + cb);
        assert (ca - cb) > 200;

        ca = 0;
        cb = 0;
        for (int i = 0; i < 1000; i++) {
            randomValue = rm.getUniformRandomValue();
            if (randomValue.equals("apple")) ca++;
            if (randomValue.equals("banana")) cb++;
        }
        System.out.println("when calling getUniformRandomValue" );
        System.out.println("ca: " + ca);
        System.out.println("cb: " + cb);
        assert Math.abs(ca - cb) <= 100;

        rm.remove("a");
        ca = 0;
        cb = 0;
        for (int i = 0; i < 1000; i++) {
            randomValue = rm.getRandomValue();
            if (randomValue.equals("apple")) ca++;
            if (randomValue.equals("banana")) cb++;
        }
        System.out.println("when one apple is removed" );
        System.out.println("ca: " + ca);
        System.out.println("cb: " + cb);
        assert Math.abs(ca - cb) <= 100;

        rm.remove("c");
        randomValue = rm.getRandomValue();
        assert randomValue.equals("banana");
        randomValue = rm.getUniformRandomValue();
        assert randomValue.equals("banana");

        rm.put("b", null);
        assert rm.containsKey("b");
        randomValue = rm.getRandomValue();
        assert randomValue == null;
        randomValue = rm.getUniformRandomValue();
        assert randomValue == null;

        rm.put(null, "banana");
        assert rm.containsKey(null);
        randomValue = rm.getRandomValue();
        assert randomValue == null || randomValue.equals("banana");
        randomValue = rm.getUniformRandomValue();
        assert randomValue == null || randomValue.equals("banana");

    }
}
