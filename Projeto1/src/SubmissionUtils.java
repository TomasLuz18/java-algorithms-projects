import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubmissionUtils {


   
    public static Submission parseSubmission(String line)
    {
        
        //dica: usar este formatador para fazer a leitura do tempo. Ver método estático parse da classe LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        String[] tokens = line.split("\t");
        int numero = Integer.parseInt(tokens[0]);
        LocalDateTime tempo = LocalDateTime.parse(tokens[1], formatter);
        int pontos = Integer.parseInt(tokens[2]);
        String grupo = tokens[3];
        String id_equipa = tokens[4];
        String nome_equipa = tokens[5];
        String problema = tokens[6];
        String linguagem = tokens[7];
        Result resultado = Result.valueOf(tokens[8]);
        State estado = State.valueOf(tokens[9]);

        Submission s = new Submission(numero, tempo, pontos, grupo, id_equipa, nome_equipa, problema, linguagem, resultado, estado);

		return s;
    }

    public static List<Submission> readSubmissionsFromFile(String fileName) {

        File file = new File(fileName);
        List<Submission> submissions = new ArrayList<>();
            try {
                if (!file.exists()) {
                    return submissions;
                }
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                int interation = 0;
                while ((line = br.readLine()) != null) {
                    if (interation == 0) {
                        interation++;
                        continue;

                    }
                    submissions.add(parseSubmission(line));

                }

                fr.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        return submissions;
    }

    public static void writeSubmissionsToFile(String fileName, List<Submission> submissions)
    {
        //TODO: implement
    }

    public static void sortSubmissions(List<Submission> submissions)
    {
        //TODO: implement
    }

    public static void printSubmissions(List<Submission> submissions, int n)
    {
        //TODO: implement
    }

    public static List<Submission> filterByProblem(List<Submission> submissions, String problem)
    {
        //TODO: implement
		return null;
    }

    public static Submission getSubmissionWithNumber(List<Submission> submissions, int submissionNumber)
    {
        //TODO: implement
		return null;
    }

    public static void printProblemStats(List<Submission> submissions, String problem)
    {
        //TODO: implement
    }
	
	public static List<Submission> getBestSubmissions(List<Submission> submissions, String teamName)
	{
		//TODO: implement
		return null;
	}
	
	public static List<String> getTeams(List<Submission> submissions)
	{
		//TODO: implement
		return null;
	}

    public static void testUpdate()
    {
        //TODO: implement
    }
}
