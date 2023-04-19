
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class PartidoTest {


    @Test
    public void testPartido() {
        //Procesar
        Partido Testpartido = new Partido();

        Equipo equipo1test = new Equipo();
        equipo1test.setNombre("Argentina");
        Testpartido.setEquipo1(equipo1test);

        Equipo equipo2test = new Equipo();
        equipo2test.setNombre("Arabia Saudita");
        Testpartido.setEquipo2(equipo2test);

        Testpartido.setGolesEquipo1(3);
        Testpartido.setGolesEquipo2(2);


        ResultadoEnum resultado1 = Testpartido.resultadoPart(equipo1test);
        ResultadoEnum resultado2 = Testpartido.resultadoPart(equipo2test);

        // Evaluar

        assertEquals(ResultadoEnum.GANADOR, resultado1);
        assertEquals(ResultadoEnum.PERDEDOR, resultado2);
    }
@Test
    public void Partidoempate(){
        //Procesar
        Partido Testpartido = new Partido();

        Equipo equipo1test = new Equipo();
        equipo1test.setNombre("Argentina");
        Testpartido.setEquipo1(equipo1test);

        Equipo equipo2test = new Equipo();
        equipo2test.setNombre("Arabia Saudita");
        Testpartido.setEquipo2(equipo2test);

        Testpartido.setGolesEquipo1(0);
        Testpartido.setGolesEquipo2(0);


        ResultadoEnum resultado1 = Testpartido.resultadoPart(equipo1test);
        ResultadoEnum resultado2 = Testpartido.resultadoPart(equipo2test);

        // Evaluar

        assertEquals(ResultadoEnum.EMPATE, resultado1);
        assertEquals(ResultadoEnum.EMPATE, resultado2);





}




}