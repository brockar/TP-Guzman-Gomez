//package tp;
import java.io.File;
import java.sql.*;
import java.time.Year;
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
        try (Scanner scFile = new Scanner(new File("src/main/java/resultados.csv"))) {
            while (scFile.hasNextLine()) {
                //Separa los datos por ;
                datos = scFile.nextLine().split(";");
                boolean correcta=false;

                if(datos.length==6){
//                    \\d+ son los enteros en regex
                    if( datos[0].matches("\\d+") && datos[2].matches("\\d+") && datos[3].matches("\\d+") && datos[5].matches("\\d+")  ){
                        if(datos[1]!=null && datos[4]!=null){
                            correcta=true;
                        }
                    }
                }


                if(correcta){
                    p = new Partido();

                    Equipo e = new Equipo();
                    e.setNombre(datos[1]);
                    p.setEquipo1(e);

                    p.setGolesEquipo1(Integer.parseInt(datos[2]));
                    p.setGolesEquipo2(Integer.parseInt(datos[3]));

                    e = new Equipo();
                    e.setNombre(datos[4]);
                    p.setEquipo2(e);
                    p.setNumPartido(Integer.parseInt(datos[0]));

                    aP.add(Integer.parseInt(datos[0]),p);

                }
                else{
                    System.out.println("Algun dato esta mal.");
                }
            }
        } catch (Exception e) {
            System.out.println("e");
        }


        // Lectura PRONOSTICOS -------------------
        ArrayList<Pronostico> aPro = new ArrayList<>();
        Pronostico pro;
        String[] datos0;

        try (Scanner scFile = new Scanner(new File("src/main/java/pronostico.csv"))) {
            while (scFile.hasNextLine()) {
                datos0 = scFile.nextLine().split(";");
                pro = new Pronostico();

                pro.setNumPartido(Integer.parseInt(datos0[0]));

                // Mira que partido es y si existe.
                boolean existe = false;
                for (Partido pa : aP) {
                    if (pa.getNumPartido() == pro.getNumPartido()) {
                        existe = true;
                        break;
                    }
                }

                if (existe) {
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

                    pro.setNombre(datos0[6]);

                    //!To do: arreglar errores del recuento de puntos.
                    //!Probar bien los puntos, con todas las convinaciones posibles.
                    //busca el ResultadoEnum del equipo que aposto y lo agrega

                    pro.setResultado(aP.get(pro.getNumPartido()).resultadoPart(pro.getEquipo()));

                    //Agrega los puntos correspondientes a los que tenia.
                    pro.setPuntos(pro.fpuntos());

                    aPro.add(pro);
                }
            }
        } catch (Exception error1) {
            error1.printStackTrace();
        }

//      Recuento de puntos por persona
        //M=2 P=3
        int puntosPersona = 0;
        String nombrePersona = "";
//     Lo hice con iterador para saber el primero y el ultimo, de al forma for each no tenia manera de comprobar el primero y el ultimo
        //!Se puede sacar la primer iteracion afuera haciendo un aPro.get(0) y empezando el for en 1.
        //!Tambien se puede sacar el ultimo si se lo busca en aPro.size
        for (int i = 0; i < aPro.size(); i++) {
            Pronostico pronostico = aPro.get(i);
//            Primer entrada
            if (i == 0) {
                nombrePersona = pronostico.getNombre();
                puntosPersona = pronostico.getPuntos();
            }
//            resto de entradas excepto la ultima
            else if (nombrePersona.equals(pronostico.getNombre()) && i != aPro.size() - 1) {
                puntosPersona = puntosPersona + pronostico.getPuntos();
            }
//            ultima entrada
            else if (i == aPro.size() - 1) {
                puntosPersona = puntosPersona + pronostico.getPuntos();
                System.out.println(nombrePersona + " obtuvo " + puntosPersona + " puntos.");
            } else {
                System.out.println(nombrePersona + " obtuvo " + puntosPersona + " puntos.");
                nombrePersona = pronostico.getNombre();
                puntosPersona = pronostico.getPuntos();
            }

            //! Intentar tomar los datos de la config
            ///C:\xampp\phpMyAdmin\config.inc.php hay que cambéar la contraseña
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/tpdatos","root","root");
                Statement stmt=con.createStatement();
//                USO DE LA DB
                con.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}



/*
Entrega 3
        En esta entrega se deben poder leer los pronósticos desde una base de datos MySQL. Por
        otro lado, debe poder ser configurable la cantidad de puntos que se otorgan cuando se acierta
        un resultado (ganar, perder, empatar).
        Finalmente, se agregan 2(dos) reglas para la asignación de puntajes de los participantes:
        ● Se suman puntos extra cuando se aciertan todos los resultados de una ronda.
        ● Se suman puntos extra cuando se aciertan todos los resultados de una fase
        (nuevamente, hace falta modificar los archivos para agregar este dato) sobre un
        equipo. Se debe considerar que una fase es un conjunto de rondas.
        Se recomienda analizar qué estrategia se puede aplicar para incluir otras nuevas reglas con el
        menor impacto posible, de forma simple.
        En esta entrega, el programa debe:
        ● Estar actualizado en el repositorio de Git.
        ● Recibir como argumento un archivo con los resultados y otro con configuración, por
        ejemplo: conexión a la DB, puntaje por partido ganado, puntos extra, etc.
*/