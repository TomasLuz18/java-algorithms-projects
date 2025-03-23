import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;


public class Main {

    public static void main(String[] args){

        Result resultado = Result.ACCEPTED;
        Result resultado1 = Result.EVALUATING;
        State estado = State.FINAL;
        LocalDate ld = LocalDate.of(2012, Month.JANUARY, 12);
        LocalTime lt = LocalTime.of(12,55);

        LocalDateTime ldt = LocalDateTime.of(ld, lt);



    }
}
