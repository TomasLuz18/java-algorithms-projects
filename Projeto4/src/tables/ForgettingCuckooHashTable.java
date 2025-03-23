package aed.tables;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;

import aed.utils.TimeAnalysisUtils;
public class ForgettingCuckooHashTable<Key,Value> implements ISymbolTable<Key,Value> {

    private int primeIndex;
    private int m0; private int m1;
    private int size0; private int size1;
    private Key[] keys0; private Key keys1[];
    private Value[] values0; private Value values1[];
    private ArrayList<Integer> swapList;
    private boolean swapLogging;
    private boolean advanceTimeStatus;
    private long [] timestamps0;
    private long [] timestamps1;
    private long currentTimeMillis;
    private boolean isResizing;

    private static int[] primesTable0 = {
            7, 17, 37, 79, 163, 331,
            673, 1361, 2729, 5471, 10949,
            21911, 43853, 87719, 175447, 350899,
            701819, 1403641, 2807303, 5614657,
            11229331, 22458671, 44917381, 89834777, 179669557
    };

    private static int[] primesTable1 = {
            11, 19, 41, 83, 167, 337,
            677, 1367, 2731, 5477, 10957,
            21929, 43867, 87721, 175453, 350941,
            701837, 1403651, 2807323, 5614673,
            11229341, 22458677, 44917399, 89834821, 179669563
    };

    @SuppressWarnings("unchecked")
    public ForgettingCuckooHashTable(int primeIndex)
    {
        this.primeIndex = primeIndex;
        this.m0 = primesTable0[primeIndex]; this.m1 = primesTable1[primeIndex];
        this.size0 = 0; this.size1 = 0;
        this.keys0 = (Key[]) new Object[this.m0];
        this.keys1 = (Key[]) new Object[this.m1];
        this.values0 = (Value[]) new Object[this.m0];
        this.values1 = (Value[]) new Object[this.m1];
        this.swapList = new ArrayList<>();
        this.swapLogging = false;
        this.advanceTimeStatus = false;
        this.timestamps0 = new long[this.m0];
        this.timestamps1 = new long[this.m1];
        this.currentTimeMillis = System.currentTimeMillis();
        this.advanceTimeStatus = false;
        this.isResizing = false;
    }

    public ForgettingCuckooHashTable()
    {
        this(0);
    }

    public int size()
    {
        return this.size0 + this.size1;
    }

    @Override
    public boolean isEmpty()
    {
        return this.size0 == 0 && this.size1 ==0;
    }

    public int getCapacity()
    {
        return this.m0 + this.m1;
    }

    public float getLoadFactor()
    {

        return (float) size() / getCapacity();
    }

    public boolean containsKey(Key k)
    {
        int count0 = 0;
        for(int i = hash0(k); keys0[i] != null; i = (i + 1) % m0){

            if(keys0[i].equals(k)){

                return true;
            }
            if(count0 >= this.m0)
                break;
            count0++;
        }
        int count1 = 0;
        for(int i = hash1(k); keys1[i] != null; i = (i + 1) % m1){

            if(keys1[i].equals(k)){

                return true;
            }
            if(count1 >= this.m1)
                break;
            count1++;
        }
        return false;
    }

    public Value get(Key k)
    {
        int count = 0;
        for(int i = hash0(k); keys0[i] != null; i = (i + 1) % m0){

            if(!this.isResizing)
                this.timestamps0[i] = this.currentTimeMillis;
            if(this.keys0[i].equals(k)){

                return values0[i];
            }
            if(count >= this.m0)
                break;
            count++;
        }

        count = 0;
        for(int i = hash1(k); keys1[i] != null; i = (i + 1) % m1){

            if(!this.isResizing)
                this.timestamps1[i] = this.currentTimeMillis;
            if(keys1[i].equals(k)){

                return values1[i];
            }
            if(count >= this.m1)
                break;
            count++;
        }

        return null;
    }

    private int hash0(Key k) {
        return ((k.hashCode() & 0x7fffffff)) % this.m0;
    }

    private int hash1(Key k) {
        int hash = k.hashCode() & 0x7fffffff;
        int prime1 = 31;
        int prime2 = 37;
        return ((hash % this.m1) * prime1 + (hash % this.m1) * prime2) % this.m1;
    }

    public void put(Key k, Value v) {

        if (k == null) {
            throw new IllegalArgumentException();
        }
        if (v == null) {

            delete(k);
            return;
        }

        if (getLoadFactor() >= 0.5) {
            resize(primeIndex + 1);
        }

        int i0 = hash0(k);
        int i1 = hash1(k);

        if (keys0[i0] != null && keys0[i0].equals(k)) {

            if(!this.isResizing)
                this.timestamps0[i0] = this.currentTimeMillis;
            values0[i0] = v;

        } else if (keys1[i1] != null && keys1[i1].equals(k)) {
            if(!this.isResizing)
                this.timestamps1[i1] = this.currentTimeMillis;
            values1[i1] = v;

        } else {
            insertCucKoo(k, v);
        }
    }

    private void insertCucKoo(Key k, Value v) {

        int Swap_limit = 1000;
        int currentTable = 0;
        int swaps = 0;

        while (swaps < Swap_limit ) {
            int i0 = hash0(k);
            int i1 = hash1(k);

            Key tempKey;
            Value tempVal;

            if(currentTable == 0)
            {
                tempKey = keys0[i0];
                tempVal = values0[i0];
            }
            else
            {
                tempKey = keys1[i1];
                tempVal = values1[i1];
            }

            if(tempKey != null){
                if (keys0[i0] != null && keys1[i1] != null && k.hashCode() == keys0[i0].hashCode() && k.hashCode() == keys1[i1].hashCode()) {
                    throw new IllegalArgumentException();
                }
                if (currentTable == 0) {
                    if(KeyExpired(currentTable, i0) && this.advanceTimeStatus && !this.isResizing)
                    {
                        delete(tempKey);
                        this.keys0[i0] = k;
                        this.values0[i0] = v;
                        this.size0++;
                        break;
                    }
                    keys0[i0] = k;
                    values0[i0] = v;
                    k = tempKey;
                    v = tempVal;
                    currentTable = 1;

                } else {
                    if(KeyExpired(currentTable, i1) && this.advanceTimeStatus && !this.isResizing)
                    {
                        delete(tempKey);
                        this.keys1[i1] = k;
                        this.values1[i1] = v;
                        this.size1++;
                        break;
                    }
                    keys1[i1] = k;
                    values1[i1] = v;
                    k = tempKey;
                    v = tempVal;
                    currentTable = 0;
                }
                swaps++;
            }
            else{
                if (keys0[i0] == null) {

                    keys0[i0] = k;
                    values0[i0] = v;
                    size0++;
                    break;
                } else if (keys1[i1] == null) {

                    keys1[i1] = k;
                    values1[i1] = v;
                    size1++;
                    break;
                }
            }
        }

        if(swaps >= Swap_limit){
            resize(primeIndex + 1);
            put(k, v);
            return;
        }

        if(this.swapLogging)
        {
            this.swapList.add(swaps);
        }
    }

    private boolean KeyExpired(int currentTable, int index) {
        long timeStamp;

        if (currentTable == 0) {
            timeStamp = timestamps0[index];
        } else {
            timeStamp = timestamps1[index];
        }

        long expirationTime = 3600000 * 24;

        if (this.currentTimeMillis - timeStamp >= expirationTime) {
            return true;
        } else {
            return false;
        }
    }

    private void resize(int primeIndex) {
        if (primeIndex < 0 || primeIndex >= primesTable0.length + primesTable1.length) {
            return;
        }

        this.primeIndex = primeIndex;
        this.isResizing = true;

        ForgettingCuckooHashTable<Key, Value> aux = new ForgettingCuckooHashTable<>(this.primeIndex);

        int i = 0;
        while (i < m0 + m1){
            if (i < m0 && keys0[i] != null) {
                aux.put(keys0[i], values0[i]);
            }

            if (i < m1 && keys1[i] != null) {
                aux.put(keys1[i], values1[i]);
            }

            i++;
        }

        this.isResizing = false;
        this.advanceTimeStatus = false;
        this.keys0 = aux.keys0;
        this.values0 = aux.values0;
        this.m0 = aux.m0;
        this.timestamps0 = new long[this.m0];

        this.keys1 = aux.keys1;
        this.values1 = aux.values1;
        this.m1 = aux.m1;
        this.timestamps1 = new long[this.m1];
    }

    public void delete(Key k) {
        int i0 = hash0(k);
        int i1 = hash1(k);

        while (true) {
            if (this.keys0[i0] == null && this.keys1[i1] == null)
                return;

            if (this.keys0[i0] != null) {
                if (this.keys0[i0].equals(k)) {
                    this.keys0[i0] = null;
                    this.values0[i0] = null;
                    this.size0--;
                    break;
                }
            }
            if(this.keys1[i1] != null){
                if(this.keys1[i1].equals(k))
                {
                    this.keys1[i1] = null;
                    this.values1[i1] = null;
                    this.size1--;
                    break;
                }

            }
            i0 = (i0 + 1) % this.m0;
            i1 = (i1 + 1) % this.m1;
        }

        if(getLoadFactor() < 0.125f  && (getCapacity() > primesTable0[0] && getCapacity() > primesTable1[0]))
            resize(this.primeIndex-1 );
    }

    public Iterable<Key> keys() {
        return new KeyIterator();
    }

    private class KeyIterator implements Iterator<Key>, Iterable<Key> {

        private ArrayList<Key> allKeys;

        private int index;

        KeyIterator() {

            allKeys = new ArrayList<>();
            index = 0;
            for (Key key : keys0) {
                if (key != null) {
                    allKeys.add(key);
                }
            }

            for (Key key : keys1) {
                if (key != null) {
                    allKeys.add(key);
                }
            }

        }

        public boolean hasNext() {
            return this.index < allKeys.size();
        }

        public Key next() {
            if (hasNext()) {
                return allKeys.get(this.index++);
            }
            return null;
        }

        public void remove() {
            throw new UnsupportedOperationException("Iterator doesn't support removal");
        }

        @Override
        public Iterator<Key> iterator() {
            return this;
        }
    }

    public void setSwapLogging(boolean state)
    {
        this.swapLogging = state;

    }
    public float getSwapAverage() {
        if (!swapLogging || swapList.isEmpty()) {
            return 0.0f;
        }

        int timesSwapped = Math.min(swapList.size(), 100);
        int sum = 0;

        for (int i = 0; i < timesSwapped && i < swapList.size(); i++) {
            sum += swapList.get(i);

        }
        return (float) sum / timesSwapped;
    }

    public float getSwapVariation() {
        if (swapList.isEmpty() || !swapLogging)
            return 0.0f;

        int exchanges = Math.min(swapList.size(), 100);
        float sum = 0;
        float average = getSwapAverage();

        for (int i = 0; i < exchanges && i < swapList.size(); i++) {
            float difference = swapList.get(i) - average;
            sum += Math.pow(difference, 2);
        }
        return (float) sum / exchanges;
    }

    public void advanceTime(int hours)
    {
        this.currentTimeMillis += (hours * 3600000);
        this.advanceTimeStatus = true;
    }

    static int iterations = 15;

    public static void main(String[] args) {

        ForgettingCuckooHashTable<String, Integer> hashTable = new ForgettingCuckooHashTable<>();

        Function<Integer, Pair<String, Integer>> entryGenerator = capacity -> {
            String key = generateRandomString();
            int value = new Random().nextInt(1500);
            return new Pair<>(key, value);
        };

        System.out.println("Testes para o método put\n");


        TimeAnalysisUtils.runDoublingRatioTest(
                entryGenerator,
                entry -> {
                    for (int i = 0; i < hashTable.getCapacity(); i++) {
                        hashTable.put(entry.getKey(), entry.getValue());
                    }
                },
                iterations
        );

        System.out.println("-----------------------------------------");

        System.out.println("Testes para o método get\n");

        TimeAnalysisUtils.runDoublingRatioTest(
                entryGenerator,
                entry -> {
                    hashTable.get(entry.getKey());
                },
                iterations
        );
    }

    private static String generateRandomString() {
        int length = new Random().nextInt(10) + 1;
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) (new Random().nextInt(26) + 'A');
            randomString.append(randomChar);
        }
        return randomString.toString();
    }

    static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}


