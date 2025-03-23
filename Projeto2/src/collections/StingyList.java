package aed.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StingyList<T> implements Iterable<T> {

    //representamos null como o long 0L.
    private static final long NULL = 0L;

    private int size;
    private long first;
    private long last;

    public StingyList()
    {
        this.first = NULL;
        this.last = NULL;
        this.size = 0;
    }

    // Embora não seja obrigatório, aconselho-vos a implementar estes 3 métodos seguintes, pois o código da StingyList pode
    // ser implementado de forma simples com base nestes métodos.

    // Dado um long que representa o endereço do nó atual (node), e um segundo long que representa o endereço do nó
    // de onde viemos numa sequência antes de chegar ao nó atual, devolve uma referência para o nó onde queremos ir a seguir.
    // Este método funciona quer estejamos a "viajar" da esquerda para a direita, ou da direita para a esquerda,
    // como podemos ver no seguinte diagrama:
    //       from -- to --> Node -- to --> beyond
    //       beyond <-- to -- Node <-- to -- from
    //
    private static long getBeyond(long nodeAddr, long fromAddr)
    {
        if(nodeAddr == NULL || nodeAddr == fromAddr){

            return nodeAddr;

        }
        long prevNext = UNode.get_prev_next_addr(nodeAddr);
        return fromAddr^prevNext;
    }

    // Atualiza uma das referências do nó (pode ser usado para atualizar o previous ou o next).
    // Recebe como argumento um endereço para o nó, um endereço para a ligação que queremos atualizar (previous ou next),
    // e o novo endereço a usar. Se passármos o previous, este método atualiza apenas o ponteiro para o previous
    // mantendo o ponteiro para o next, e vice-versa.
    private static void updateNodeReference(long nodeAddr, long oldAddr, long newAddr)
    {
        long prevNext = UNode.get_prev_next_addr(nodeAddr);
        long updateAddr = oldAddr^prevNext;
        long updatedPrevNext = updateAddr^newAddr;
        UNode.set_prev_next_addr(nodeAddr, updatedPrevNext);
    }

    //Atualiza ambas as referências do nó em simultâneo (previous e next). Útil quando queremos atualizar ambas,
    //e já temos as referências para o novo previous e o novo next. Recebe como argumentos o novo nó previous para o
    // qual queremos apontar, e o novo nó next para o qual queremos apontar.
    private static void updateBothNodeReferences(long nodeAddr, long prevAddr, long nextAddr)
    {
        long updatedPrevNext = prevAddr^nextAddr;
        UNode.set_prev_next_addr(nodeAddr, updatedPrevNext);
    }


    //Stingy List Methods

    public void add(T item)
    {
        if(item == null){

            throw new IllegalArgumentException();
        }


        long newNode = UNode.create_node(item, this.last, NULL);
        if(this.last != NULL){

            long prevNode = getBeyond(this.last, NULL);
            updateBothNodeReferences(this.last, prevNode, newNode);
            this.last = newNode;
        } else{

            this.last = newNode;
            this.first = newNode;


        }
        this.size++;
    }

    public T remove()
    {
        if(size == 0)
        {
            throw new IndexOutOfBoundsException();
        }

        long prevNode = getBeyond(last, NULL);
        T item = UNode.get_item(last);
        UNode.free_node(last);
        if(prevNode == NULL){
            first = NULL;
            last = NULL;

        } else{

            long prevPrevNode = getBeyond(prevNode, last);
            updateBothNodeReferences(prevNode, prevPrevNode, NULL);
            last = prevNode;

        }

        size--;
        return item;
    }

    public T get()
    {
        if(size == 0){

            throw new IndexOutOfBoundsException();
        }
        return UNode.get_item(last);
    }

    public T get(int i)
    {
        if(i < 0 || i >= size)
        {
            throw new IndexOutOfBoundsException();
        }
        else
        {
            long current = first;
            long prev = NULL;

            if(i < size / 2)
            {
                for(int index = 0; index < i; index++)
                {
                    long next = UNode.get_prev_next_addr(current) ^ prev;
                    prev = current;
                    current = next;
                }
                return UNode.get_item(current);
            }
            else
            {
                current = last;
                long next = NULL;
                for (int index = size - 1; index > i; index--)
                {
                    prev = getBeyond(current, next);
                    next = current;
                    current = prev;
                }
                return UNode.get_item(current);

            }
        }
    }

    public T getSlow(int i)
    {
        if(i < 0 || i >= size)
        {
            throw new IndexOutOfBoundsException();
        }

        long current = this.first;
        long prev = NULL;
        for(int z = 0; z < i ; z++){


            long next = getBeyond(current, prev);
            prev = current;
            current = next;


        }
        return UNode.get_item(current);
    }

    public void addAt(int i, T item) {

        if (size == 0) {

            if (i < 0 || i > size) {
                throw new IndexOutOfBoundsException();
            }
            add(item);
        } else {
            if (i < 0 || i > size) {
                throw new IndexOutOfBoundsException();

            } else if(i == 0) {

                long newNode = UNode.create_node(item, first, NULL);
                updateNodeReference(first, NULL, newNode);
                first = newNode;


            } else if (i == size) {

                long newNode = UNode.create_node(item, last, NULL);
                updateNodeReference(last, NULL, newNode);
                last = newNode;


            } else {
                long current = NULL;
                long fromNode = NULL;
                if(i <= size/ 2){
                    current = first;
                    for (int index = 0; index < i; index++) {

                        long v_temp = current;
                        current = getBeyond(current, fromNode);
                        fromNode = v_temp;

                    }

                } else{

                    current = last;
                    for (int index = size; index > i; index--)
                    {
                        long v_temp = current;
                        current = getBeyond(current, fromNode);
                        fromNode = v_temp;
                    }

                }
                long newNode = UNode.create_node(item, fromNode, current);
                updateNodeReference(fromNode, current, newNode);
                updateNodeReference(current, fromNode, newNode);

            }
            size++;
        }


    }


    public T removeAt(int i)
    {
        if (i < 0 || i >= size)
        {
            throw new IndexOutOfBoundsException();
        }

        if(i == 0){

            long nextNode =  getBeyond(first, NULL);
            T item = UNode.get_item(first);
            long nextNextNode = getBeyond(nextNode, first);
            UNode.free_node(first);
            updateBothNodeReferences(nextNode, NULL, nextNextNode);
            first = nextNode;
            size--;

            if(size == 0){
                first = NULL;
                last = NULL;

            }
            return item;

        }
        else if(i == (size - 1)){

            return  remove();

        } else{


            if(i < size/ 2){

                long current = first;
                long prev = NULL;
                for(int index = 0; index < i; index++){

                    long next = getBeyond(current, prev);
                    prev = current;
                    current = next;

                }
                long nextNode =  getBeyond(current, prev);
                T item = UNode.get_item(current);
                updateNodeReference(prev, current, nextNode);
                updateNodeReference(nextNode, current, prev);
                UNode.free_node(current);
                size--;
                return item;
            } else{

                long current = last;
                long next = NULL;

                for (int index = size - 1; index > i; index--){

                    long prev = getBeyond(current, next);
                    next = current;
                    current = prev;

                }

                long prevNode =  getBeyond(current, next);
                T item = UNode.get_item(current);
                updateNodeReference(next, current, prevNode);
                updateNodeReference(prevNode, current, next);
                UNode.free_node(current);
                size--;
                return item;

            }




        }


    }

    public void reverse()
    {
        if (size <= 1)
        {
            return;
        }
        else
        {
            long node = first;
            first = last;
            last = node;
        }
    }

    public StingyList<T> reversed()
    {
        StingyList<T> reversedList = new StingyList<>();
        reversedList.first = last;
        reversedList.last = first;
        reversedList.size = size;
        return reversedList;
    }

    public void clear()
    {
        long current = first;
        long prev = NULL;

        while (current != NULL)
        {
            long next = getBeyond(current, prev);
            UNode.free_node(current);
            prev = current;
            current = next;
        }


        first = NULL;
        last = NULL;
        size = 0;

    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public int size()
    {
        return this.size;
    }

    public Object[] toArray()
    {
        Object array[] = new Object[size];
        long current = first;
        long prev = NULL;
        long next;
        int i = 0;

        while (current != NULL) {
            T item = UNode.get_item(current);
            array[i++] = item;
            next = getBeyond(current, prev);
            prev = current;
            current = next;


        }

        return array;
    }


    private class StingyListIterator implements Iterator<T>
    {
        private long current;
        private long prev;
        private long next;
        private int currentIndex;

        public StingyListIterator()
        {
            current = first;
            prev = NULL;
            currentIndex = 0;
            next = getBeyond(current, prev);
        }

        public boolean hasNext()
        {
            return currentIndex < size;
        }

        public T next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }

            T item = UNode.get_item(current);
            prev = current;
            current = next;
            currentIndex++;

            if (currentIndex < size)
            {
                next = getBeyond(current, prev);


            }
            return item;
        }
    }

    public Iterator<T> iterator() {
        return new StingyListIterator();
    }


    public static void main(String[] args)
    {

        StingyList<String> myList = new StingyList<String>();

        // Teste do método add()
        myList.add("A");
        myList.add("B");
        myList.add("C");
        System.out.print("list:");
        for (String item : myList) {
            System.out.print(item);
        }
        System.out.print("\n");

        // Teste do método remove()
        System.out.println("Removed item: " + myList.remove());

        // Teste do método get()
        System.out.println("Last item: " + myList.get());

        // Teste do método get(int i)
        System.out.println("Item at index 0: " + myList.get(0));
        System.out.println("Item at index 1: " + myList.get(1));

        // Teste do método addAt()
        myList.addAt(1, "D");
        System.out.println("Item at index 1 after addAt: " + myList.get(1));

        // Teste do método removeAt()
        System.out.println("Removed item at index 2: " + myList.removeAt(2));

        // Teste do método reverse()
        myList.reverse();
        System.out.print("Reverse List(utilizando o metodo reverse): ");
        for (String item : myList) {
            System.out.print(item);
        }
        System.out.println();

        // Teste do método reversed()
        StingyList<String> reversedList = myList.reversed();
        System.out.print("Reversed List(metodo reversed): ");
        for (String item : reversedList) {
            System.out.print(item);
        }
        System.out.println();

        // Teste do método clear()
        myList.clear();
        System.out.println("Is list empty after clear(): " + myList.isEmpty());

        // Teste do método toArray()

        // Teste do método toArray()
        myList.add("A");
        myList.add("B");
        myList.add("C");


        Object[] objectArray = myList.toArray();
        String[] stringArray = new String[objectArray.length];

        for (int i = 0; i < objectArray.length; i++) {
            stringArray[i] = (String) objectArray[i];
        }

        System.out.print("Array from list: ");
        for (String item : stringArray) {
            System.out.print(item);
        }
        System.out.println();

        // Teste do Iterator
        System.out.print("Using Iterator: ");
        for (String item : myList) {
            System.out.print(item);
        }
        System.out.println();

        // Verificação do tamanho da lista
        System.out.println("Size of the list: " + myList.size());

    }



}