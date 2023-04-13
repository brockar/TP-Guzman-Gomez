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
        //para mostrar por pantalla los partidos en aP
//        System.out.println("Pornosticos");
//        for (Pronostico pronostico : aPro) {
//            pronostico.print();
//        }

//      Recuento de puntos por persona
        //M=2 P=3
        int puntosPersona=0;
        String nombrePersona="";
//     Lo hice con iterador para saber el primero y el ultimo, de al forma for each no tenia manera de comprobar el primero y el ultimo
        //!Se puede sacar la primer iteracion afuera haciendo un aPro.get(0) y empezando el for en 1.
//        !Tambien se puede sacar el ultimo si se lo busca en aPro.size
        for (int i=0; i<aPro.size(); i++) {
            Pronostico pronostico = aPro.get(i);
//            Primer entrada
            if (i==0) {
                nombrePersona= pronostico.getNombre();
                puntosPersona= pronostico.getPuntos();
            }
//            resto de entradas excepto la ultima
            else if (nombrePersona.equals(pronostico.getNombre()) && i!=aPro.size()-1){
                puntosPersona= puntosPersona + pronostico.getPuntos();
            }
//            ultima entrada
            else if (i==aPro.size()-1) {
                puntosPersona= puntosPersona+pronostico.getPuntos();
                System.out.println(nombrePersona + ": "+puntosPersona);
            } else{
                System.out.println(nombrePersona + ": "+puntosPersona);
                nombrePersona=pronostico.getNombre();
                puntosPersona=pronostico.getPuntos();
            }

//            System.out.println("i: "+i+ " NP: "+nombrePersona +" NPronos: "+ pronostico.getNombre());
//            System.out.println((nombrePersona.equals(pronostico.getNombre())));

        }
        Pronostico pronostico = aPro.get(aPro.size()-1);
        System.out.println(pronostico.getNombre()+": "+puntosPersona);
//        for (Pronostico pronostico : aPro){
////            System.out.println(pronostico.getNombre());
//
//            if(nombrePersona.equals(pronostico.getNombre())) {
//                puntosPersona=puntosPersona+pronostico.getPuntos();
//
//            } else{
//                System.out.println(nombrePersona + ": "+puntosPersona);
//                nombrePersona=pronostico.getNombre();
//                puntosPersona=pronostico.getPuntos();
//            }
//        }

    }
}


