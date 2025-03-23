import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Submission {

    public int numero;
    public LocalDateTime tempo;
    public int pontos;
    public String grupo;
    public String id_equipa;
    public String nome_equipa;
    public String problema;
    public String linguagem;
    public Result resultado;
    public State estado;
    

    //métodos da classe
 
    public Submission(int numero, LocalDateTime tempo, int pontos, String grupo, String idEquipa, String nomeEquipa, String problema, String linguagem, Result resultado, State estado) {
        this.numero = numero;
        this.tempo = tempo;
        this.pontos = pontos;
        this.grupo = grupo;
        this.id_equipa = idEquipa;
        this.nome_equipa = nomeEquipa;
        this.problema = problema;
        this.linguagem = linguagem;
        this.resultado = resultado;
        this.estado = estado;
    }

    
    public int getNumero()
    {
		return this.numero;
    }

    public LocalDateTime getTempo()
    {
		return this.tempo;
    }

    public int getPontos() 
	{
		return this.pontos;
	}

    public String getGrupo()
	{
		return this.grupo;
	}

    public String getIdEquipa()
    {
		return this.id_equipa;
    }

    public String getNomeEquipa() 
	{
		return this.nome_equipa;
	}

    public String getProblema() 
	{
		return this.problema;
	}

    public String getLinguagem() 
	{
		return this.linguagem;
	}

    public Result getResultado() 
	{
		return this.resultado;
	};

    public State getEstado() 
	{
		return this.estado;
	};



    //atualiza o número de pontos obtido numa determinada submissão.
    //O membro resultado deverá ser alterado para Accepted, e o membro estado para Final.
    public void update(int pontos)
    {
        this.pontos += pontos;
        this.resultado = Result.ACCEPTED;
        this.estado = State.FINAL;


    }

    public boolean equals(Submission that)
    {
        if(this.numero == that.numero){

           return true;

       } else{

            return false;

        }

    }

    public int compareTo(Submission that)
    {
		if(this.numero == that.numero){

            return 0;

        } else if(this.numero > that.numero){

            return this.numero - that.numero;

        } else{

            return that.numero - this.numero;


        }

    }

    @Override
    //Escreve alguma da informação de uma submissão numa única string, e retorna essa string
    //O formato a usar deve ser:
    //<número submissão>,<tempo>,<pontos>,<id da equipa>,<problema>,<resultado>,<estado>
    public String toString()
    {
        //dica: usar este formatador para imprimir o tempo para o ecrâ, ver método format da classe LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

		return String.format("%d,%s,%d,%s,%s,%s,%s", this.numero,this.tempo.format(formatter),this.pontos, this.id_equipa, this.problema, this.resultado, this.estado);
    }

    //Escreve a informação completa de uma submissão numa única string, e retorna essa string
    //os campos da submissão estão separados por tab ('\t')
    //<número submissão><tempo><pontos><grupo><id da equipa><nome da equipa><problema><linguagem><resultado><estado>
    public String toTabString()
    {
        //dica: usar este formatador para imprimir o tempo para o ecrâ, ver método format da classe LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

		return String.format("%d\t%s\t%d\t%s\t%s\t%s\t%s\t%s\t%s\t%s", this.numero,this.tempo.format(formatter),this.pontos,this.grupo, this.id_equipa, this.nome_equipa, this.problema, this.linguagem, this.resultado, this.estado);
    }
}

