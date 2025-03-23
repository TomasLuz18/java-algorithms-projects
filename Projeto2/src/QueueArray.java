package aed.collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")

public class QueueArray<Item> implements Iterable<Item> {

    private int max;
    private Item[] queueArray;
    private int size;
    private int lastIndex;

    private int firstIndex;

    public QueueArray(int max) {

        this.max = max;
        this.queueArray = (Item[]) new Object[max];
        this.size = 0;
        this.lastIndex = 0;
        this.firstIndex = 0;


    }

    public void enqueue(Item item) {

        if (item == null) {

            throw new IllegalArgumentException();
        }
        if (size == max) {

            throw new OutOfMemoryError();
        }

        queueArray[lastIndex] = item;
        lastIndex = (lastIndex + 1) % max;
        size++;

    }

    public Item dequeue() {

        if (size == 0) {

            return null;

        }
        Item item = queueArray[firstIndex];
        queueArray[firstIndex] = null;
        firstIndex = (firstIndex + 1) % max;
        size--;

        return item;

    }


    public Item peek() {

        if (size == 0) {

            return null;
        }

        return queueArray[firstIndex];
    }

    public boolean isEmpty() {

        return size == 0;
    }

    public int size() {

        return size;
    }

    public QueueArray<Item> shallowCopy() {

        QueueArray<Item> copy = new QueueArray<>(this.max);
        copy.firstIndex = this.firstIndex;
        copy.lastIndex = this.lastIndex;
        copy.size = this.size;
        System.arraycopy(this.queueArray, 0, copy.queueArray, 0, this.max);
        return copy;
    }

    public Iterator<Item> iterator() {
        return new QueueArrayIterator();
    }

    private class QueueArrayIterator implements Iterator<Item> {

        private int currentIndex;
        private int count;

        public QueueArrayIterator() {

            currentIndex = firstIndex;
            count = 0;
        }

        public boolean hasNext() {

            return count < size;
        }

        public Item next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = queueArray[currentIndex];
            currentIndex = (currentIndex + 1) % max;
            count++;
            return item;
        }

    }

    public static void main(String[] args) {
        QueueArray<String> fila = new QueueArray<>(4);

        // Teste para o método enqueue
        fila.enqueue("A");
        fila.enqueue("B");
        fila.enqueue("C");
        fila.enqueue("D");

        // Teste para o método size
        System.out.println("Tamanho da fila: " + fila.size());

        // Teste para o método peek
        System.out.println("Primeiro elemento da fila: " + fila.peek());

        // Teste para o método dequeue
        String itemRemovido = fila.dequeue();
        System.out.println("Item removido: " + itemRemovido);

        System.out.println("Primeiro elemento da fila: " + fila.peek());

        // Teste para o método size
        System.out.println("Tamanho da fila: " + fila.size());

        // Teste para o método isEmpty
        System.out.println("Fila vazia? " + fila.isEmpty());


    }
}






