//package tp;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
public class Archivo {
    public static void main(String[] args) {
        // Lectura PARTIDOS ----------------
        // Explicacion
        // https://www.youtube.com/watch?v=WhpP6HWVOb8
        ArrayList<Partido> aP = new ArrayList<>();
        Partido p;
        String[] datos;
        try (Scanner scFile = new Scanner(new File("src/resultados.csv"))) {
            while (scFile.hasNextLine()) {
                //Separa los datos por ;
                datos = scFile.nextLine().split(";");
                p = new Partido();

                Equipo e = new Equipo();
                e.setNombre(datos[1]);
                p.setEquipo1(e);

                p.setGolesEquipo1(Integer.parseInt(datos[2]));
                p.setGolesEquipo2(Integer.parseInt(datos[3]));

                e = new Equipo();
                e.setNombre(datos[4]);
                p.setEquipo2(e);

//                System.out.println("APe= " + e);

                aP.add(p);
                p.setNumPartido(Integer.parseInt(datos[0]));
            }
        } catch (Exception e) {
            System.out.println("e");
        }

        //para mostrar por pantalla los partidos en aP
//        for (Partido partido : aP) {
//            partido.print();
//        }

        // Lectura PRONOSTICOS -------------------
        ArrayList<Pronostico> aPro = new ArrayList<>();
        Pronostico pro;
        String[] datos0;

        try (Scanner scFile = new Scanner(new File("src/pronostico.csv"))) {
            while (scFile.hasNextLine()) {
                datos0 = scFile.nextLine().split(";");
                pro = new Pronostico();

                pro.setNumPartido(Integer.parseInt(datos0[0]));

                // Mira que partido es y si existe.
                boolean existe = false;
                for(Partido pa : aP){
                    if (pa.getNumPartido() == pro.getNumPartido()) {
                        existe = true;
                        break;
                    }
                }

                if(existe) {
                    Equipo e = new Equipo();
                    e.setNombre(datos0[1]);
                    Equipo e1 = new Equipo();
                    e1.setNombre(datos0[5]);
                    //Se fija por quien aposto el del pronostico
                    //Si esta marcado la columna 1 es que apuesta por el equipo1 (e),
                    //Si esta marcada la columna 3 es que apuesta por el equipo2 (e1),
                    //Si esta marcada la columna del medio es empate, por lo tanto no se asigna equipo.
                    if (!datos0[2].isEmpty() && Integer.parseInt(datos0[2]) == 1) {
                        pro.setEquipo(e);
                    } else if (!datos0[4].isEmpty() && Integer.parseInt(datos0[4]) == 1) {
                        pro.setEquipo(e1);
                    }
                    //!Hasta aca esta bien
                    //!No funciona porque equipo es null porque apuesta por empate
                    //!To do: arreglar errores del recuento de puntos.

                    //busca el ResultadoEnum del equipo que aposto
                    System.out.println(aP.get(pro.getNumPartido()).resultadoPart(pro.getEquipo())+ "\n");
                    pro.setResultado(aP.get(pro.getNumPartido()).resultadoPart(pro.getEquipo()));
//                   hacer un if para comprobar si tiene o no equipo, si no tiene equipo manda la consulta sin equipo

                    //Agrega los puntos correspondientes a los que tenia.
                    pro.setPuntos(pro.getPuntos() + pro.fpuntos());

                    aPro.add(pro);
                }
            }
        } catch (Exception error1) {
            error1.printStackTrace();
        }

        //para mostrar por pantalla los partidos en aP
        System.out.println("Pornosticos");
        for (Pronostico pronostico : aPro) {
            pronostico.print();
        }

    }
}


