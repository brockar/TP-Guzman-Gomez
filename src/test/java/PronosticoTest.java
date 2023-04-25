import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PronosticoTest {

    @Test
    public void testAciertos() {
        Partido partidoPRO = new Partido();
        Equipo equipoPRO1= new Equipo();
        Equipo equipoPRO2 = new Equipo();

        equipoPRO1.setNombre("Argentina");
        partidoPRO.setEquipo1(equipoPRO1);

        equipoPRO2.setNombre("Arabia Saudita");
        partidoPRO.setEquipo2(equipoPRO2);


        // PRIMERA RONDA
        Partido partidoPRO1 = new Partido();
        partidoPRO1.setGolesEquipo1(3);
        partidoPRO1.setGolesEquipo2(2);


        Pronostico test = new Pronostico();
        test.setResultado(ResultadoEnum.GANADOR);
        test.setEquipo(equipoPRO1);


        int puntos= test.fpuntos();
        assertEquals(1,puntos);

        
    }




    }









