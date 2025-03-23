package aed.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class Cordel implements Iterable<String>{

    //Nesta implementação, iremos usar a própria classe Cordel como nó, em vez de termos uma classe separada para representar
    //os nós.
    //Há algumas razões para fazer isto, a mais importante é que pretendemos fazer uma implementação imutável (não destrutiva)
    //de cordeis, o que quer dizer que uma vez criado um cordel, os seus campos não vão mais ser alterados, e portanto
    //quer dizer que parte dos nossos métodos vão ter que retornar cordéis, pois é a única forma de conseguirmos construir
    //cordéis mais elaborados. Por exemplo, quando quero concatenar uma string a um cordel, vou ter que retornar um
    // novo cordel (não posso alterar o cordel original) que corresponde a um nó de concatenação entre o cordel original e um
    // novo cordel folha com a string recebida.

    //Devido a esta característica, e ao facto de toda a informação que precisamos estar guardada no nó, não
    //se justifica estar a criar uma classe interna Node, e fazer com que a Classe cordel tenha apenas um membro root
    // (iríamos gastar mais memória sem nenhuma vantagem).

    //Estes membros são declarados como final, para o compilador de Java garantir que uma vez definidos no construtor
    //nunca mais podem ser alterados.
    private final Cordel left;
    private final Cordel right;
    //Importante: normalmente, os cordéis implementam-se usando apenas informação sobre o tamanho da subárvore esquerda
    private final int leftLength;
    //Mas no nosso caso, queremos também guardar informação sobre o tamanho da subárvore direita para tornar a contatenação
    //ligeiramente mais eficiente (se não tivessemos este membro, teríamos de pagar O(log n) para calcular o tamanho do
    //lado direito)
    private final int rightLength;
    private final String string;

    //Cria e inicializa um cordel folha (LEAF) com a string s recebida
    //A string s não pode ser nula
    public Cordel(String s)
    {
        if(s == null)
            throw new IllegalArgumentException();

        this.string = s;
        this.left = null;
        this.right = null;
        this.leftLength = 0;
        this.rightLength = 0;
    }

    //Cria e inicializa um cordel que corresponde a um nó de concatenação (CONCAT)
    //e que concatena os dois cordéis recebidos
    public Cordel(Cordel left, Cordel right)
    {
        this.left = left;
        this.right = right;
        this.leftLength = left.length();
        this.rightLength = right.length();
        this.string = null;
    }

    //Retorna o tamanho de um cordel, ou seja, o número de caracteres total guardado no cordel.
    public int length()
    {
		if(string != null){

            return string.length();

        } else{

            return leftLength + rightLength;
        }
    }

    //Dado uma string, retorna um novo cordel que corresponde à concatenação à direita da string recebida com o cordel.
    public Cordel append(String s)
    {
        Cordel folha = new Cordel(s);
		return new Cordel(this, folha);
    }

    //Dado um Cordel c, retorna um novo cordel que corresponde à concatenação à direita do cordel c recebido com o cordel.
    public Cordel append(Cordel c)
    {
        return new Cordel(this, c);
    }

    //Dado uma string, retorna um novo cordel que corresponde à concatenação à esquerda da string recebida com o cordel.
    public Cordel prepend(String s)
    {
        Cordel folha = new Cordel(s);

		return new Cordel(folha, this);
    }

    //Dado um Cordel c, retorna um novo cordel que corresponde à concatenação à esquerda do cordel c recebido com o cordel.
    public Cordel prepend(Cordel c)
    {
        return new Cordel(c, this);
    }

    //Imprime as strings guardadas dentro deste cordel, pela ordem da string mais à esquerda para a mais à direita.
    //Não imprime mudanças de linha.
    public void print()
    {
            if(string != null){

                System.out.print(string);
            }
            if(left != null){

                left.print();
            }
            if(right != null){

                right.print();
            }


    }

    //Igual ao anterior, mas imprime uma nova linha no fim
    public void println()
    {
        print();
        System.out.println();
    }

    //Imprime informação sobre cada um dos nós da árvore.
    public void printInfo()
    {
        if(string != null){

            System.out.print("LEAF:\"" + string + "\"");

        } else{

            System.out.print("CONCAT[" + leftLength + "," + rightLength + "]:{" );
            left.printInfo();
            System.out.print(",");
            right.printInfo();
            System.out.print("}");
        }
    }

    //igual ao anterior, mas imprime uma nova linha no fim
    public void printInfoNL()
    {
       printInfo();
        System.out.println();

    }

    //Dado um índice, devolve o caracter correspondente ao índice i do cordel.
    public char charAt(int i)
    {
            if(string != null){

              return string.charAt(i);

            } else{

                if(i < leftLength){

                 return left.charAt(i);

            } else{

                  return right.charAt(i - leftLength);
                }

        }
    }

    //Dado um índice, parte o cordel em dois cordéis no índice i. É devolvido um array com 2 cordéis.
    // O primeiro cordel contém as strings com todos os caracteres desde o índice 0 até o índice i-1,
    // e o segundo cordel contém as strings com todos os caracteres desde o índice i até ao fim.
    public Cordel[] split(int i)
    {
        if(string != null){

            String substring1 = string.substring(0,i);
            String substring2 = string.substring(i);
            Cordel leftnode = new Cordel(substring1);
            Cordel rightnode = new Cordel(substring2);
            return new Cordel[]{leftnode, rightnode};
        } else if(i == leftLength) {

            return new Cordel[]{left, right};
        }else{

            if( i < leftLength){

                Cordel[] splitleft = left.split(i);
                Cordel newRight = splitleft[1].append(right);
                return new Cordel[]{splitleft[0], newRight};
            } else{

                Cordel[] splitright = right.split(i - leftLength);
                Cordel newLeft = splitright[0].prepend(left);
                return new Cordel[]{newLeft, splitright[1]};

            }
        }

    }

    //Dado um índice, e uma string que não pode ser nula, retorna um cordel que corresponde ao
    // resultado de inserirmos a string s, na posição i do cordel.
    public Cordel insertAt(int i, String s)
    {
        if(i == 0){

            return this.prepend(s);

        } else if(i == this.length()){

            return this.append(s);
        } else{

            Cordel[] splited = this.split(i);
            Cordel newRight = splited[0].append(s);
            return newRight.append(splited[1]);
        }

    }

    //Dado um índice i, e um tamanho, apaga a partir do índice i do cordel
    // um número de caracteres correspondentes ao tamanho recebido.
    public Cordel delete(int i, int length)
    {
       if(i == 0){

           Cordel[] splited = this.split(length);

           return splited[1];
       } else{

           Cordel[] splitByI = this.split(i);
           Cordel[] lastSplit = splitByI[1].split(length);
           Cordel result = splitByI[0].append(lastSplit[1]);
           return result;

       }

    }

    private class CordelIterator implements Iterator<String>{

        private final Stack<Cordel> stack;

    public CordelIterator(Cordel c){

        stack = new Stack<>();
        stack.push(c);
    }

        @Override
        public boolean hasNext() {

            return !(stack.isEmpty());
        }

        public String next() {
            if (!hasNext()) {

                throw new NoSuchElementException();
            }

            String result = null;
            while(result == null) {

                Cordel current = stack.pop();
                if (current.string != null) {

                    result = current.string;
                } else {

                    if (current.right != null) {

                        stack.push(current.right);
                    }
                    if (current.left != null) {
                        stack.push(current.left);
                    }

                }
            }
            return result;
        }
    }

    //Devolve um iterador que vai percorrer todas as strings guardadas em nós folha deste cordel,
    // pela ordem correta (i.e. da esquerda para a direita).
    @Override
    public Iterator<String> iterator() {

		return new CordelIterator(this);
    }

    //Dado um índice i, e um tamanho, imprime um número de caracteres do cordel igual ao tamanho recebido,
    // a partir do índice i (inclusive).
    public void print(int i, int length)
    {
        if (i < 0 || i >= length()) {
            throw new IllegalArgumentException("O indice encontra-se fora dos limites");
        }

        printRecursive(i, length);
    }

    private void printRecursive(int i, int length) {
        if (string != null)
        {
            int endIndex = Math.min(i + length, string.length());
            System.out.print(string.substring(i, endIndex));
        }
        else
        {
            if (i < leftLength)
            {

                left.printRecursive(i, length);
                if (i + length > leftLength)
                {
                    right.printRecursive(0, Math.max(0, i + length - leftLength));
                }
            }
            else
            {
                right.printRecursive(i - leftLength, length);
            }
        }
    }

    public void println(int i, int length)
    {
        print(i,length);
        System.out.println();
    }

    public static void main(String[] args)
    {
        Cordel cordel = new Cordel("ABCDEFGHIJ").append("KLMNOP");


        System.out.println("Printing from index 8, length 5:");
        cordel.print(8, 5);
        System.out.println();

        System.out.println("Printing from index 6, length 3:");
        cordel.print(6, 3);
        System.out.println();
    }
}
