//package tp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Archivo {
    public static void main(String[] args) {
        //Lectura de configuracion

        HashMap<String, String> configuracion = new HashMap<>();
        try {
            // Abre el archivo de configuración
            BufferedReader conf = new BufferedReader(new FileReader("src/main/java/config.txt"));
            String linea;
            // Lee cada línea del archivo
            while ((linea = conf.readLine()) != null) {
                // Separa la línea en dos partes usando el carácter =
                String[] partes = linea.split("=");
                // Añade la clave y el valor al HashMap
                configuracion.put(partes[0], partes[1]);
            }

            // Cierra el archivo
            conf.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // Imprime los datos de configuración
//        for (String clave : configuracion.keySet()) {
//            System.out.println(clave + " = " + configuracion.get(clave));
//        }

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
                boolean correcta = false;

                if (datos.length == 7) {
//                    \\d+ son los enteros en regex
                    if (datos[0].matches("\\d+") && datos[2].matches("\\d+") && datos[3].matches("\\d+") && datos[5].matches("\\d+") && datos[6].matches("\\d+")) {
                        if (datos[1] != null && datos[4] != null) {
                            correcta = true;
                        }
                    }
                }

                if (correcta) {
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
                    p.setRonda(Integer.parseInt(datos[5]));

                    aP.add(Integer.parseInt(datos[0]), p);

                } else {
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
                    pro.setRonda(Integer.parseInt(datos0[7]));
                    pro.setFase(Integer.parseInt(datos0[8]));

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


//      Recuento PUNTOS  --------------------------------------------
//      Lo hice con iterador para saber el primero y el ultimo, de al forma for each no tenia manera de comprobar el primero y el ultimo
        //!Se puede sacar la primer iteracion afuera haciendo un aPro.get(0) y empezando el for en 1.
        //!Tambien se puede sacar el ultimo si se lo busca en aPro.size

//        --------------------------------------------
//        ArrayList<puntosPersona> $ArrayListDePersonas = new ArrayList<>();
//        ArrayList<String> nombres= new ArrayList<String>();
//
//        ArrayList<Integer> rondaxfase = new ArrayList<Integer>();
//
//        int max=0;
//        for (int i = 0; i < aPro.size(); i++) {
//            if (i == 0) {
//                max = aPro.get(i).getFase();
//            } else {
//                if (max < aPro.get(i).getFase()) {
//                    max = aPro.get(i).getFase();
//                }
//            }
//        }
//
//        for (int i = 0; i < max; i++) {
//            rondaxfase.add(0);
//        }
//
//        for (int i = 0; i < aPro.size(); i++) {
//            if (false && rondaxfase.size() < aPro.get(i).getFase()) {
//                rondaxfase.add(1);
//            } else {
//                if (aPro.get(i).getRonda() > rondaxfase.get(aPro.get(i).getFase()-1)) {
//                    //rondaxfase.get();
//                    rondaxfase.set(aPro.get(i).getFase()-1, aPro.get(i).getRonda());
//                }
//            }
//        }
//
//        List<String> personas= nombres.stream().distinct().collect(Collectors.toList());
//
//        for(String persona:personas){
//            System.out.println(persona);
//        }
//
//        for(int i = 0; i < rondaxfase.size(); i++){
//            System.out.println("Fase: " + (i+1) + " Rondas: " + rondaxfase.get(i));
//        }
//
//        int[] puntosxpersona=new int[personas.size()];
//        ------------------------------------------------------------------------------------


//        for (int i = 0; i < aPro.size(); i++) {
//            for (int j = 0; j < $ArrayListDePersonas.size(); j++) {
//                int SumaDePuntosTemporal = 0;
//                if ((aPro.get(i)).getPuntos() == $ArrayListDePersonas.get(j).getPuntos()) {
//                    //sumar puntos
//                    SumaDePuntosTemporal += 0;
//                }
//                if (aPro.get(i).getRonda() == $ArrayListDePersonas.get(j).getRondas()) {
//                    //sumar puntos extra
//                    SumaDePuntosTemporal += 0;
//                }
//                if (aPro.get(i).getFase() == $ArrayListDePersonas.get(j).getFase()) {
//                    //sumar puntos extra
//                    SumaDePuntosTemporal += 0;
//                }
//                $ArrayListDePersonas.get(j).setPuntos($ArrayListDePersonas.get(j).getPuntos() + SumaDePuntosTemporal);
//            }
//        }

        //Puntos por partido viejo
//      M=4, 1 fase, 1 ronda
//      P=8, 2 fase, 3 ronda
        int puntosPersona = 0;
        String nombrePersona = "";

        int multip = Integer.parseInt(configuracion.get("PuntosPart"));
        int ronda = 0;
        int fase = 0;
        int cuentarondas = 0;
        int rondasbien = 0;
        int puntosar = 0;
        int cuentafase = 0;
        int puntosafase = 0;
        int fasesbien = 0;

        int masRonda = Integer.parseInt(configuracion.get("PuntosRonda"));
        int masFase = Integer.parseInt(configuracion.get("PuntosFase"));

        //#Tendria que guardarlos en un arraylist con todos los partidos que acertaron y ver si son de la misma ronda y fase para darle bien los puntos extras
        //ya que las fases pueden ser de dos rondas no contiguas
        for (int i = 0; i < aPro.size(); i++) {
            Pronostico pronostico = aPro.get(i);
//            Primer entrada
            if (i == 0) {
                nombrePersona = pronostico.getNombre();
                puntosPersona = pronostico.getPuntos() * multip;

                ronda = pronostico.getRonda();
                fase = pronostico.getFase();
                cuentarondas++;
                cuentafase++;
            }
//          resto de entradas excepto la ultima
            else if (nombrePersona.equals(pronostico.getNombre()) && i != aPro.size() - 1) {

                System.out.println("ronda:" +ronda+" cuentarondas: "+cuentarondas+" calc:"+((puntosPersona / multip) - puntosar - (fasesbien * masFase))
                + " puntosar: "+puntosar+" fb: "+fasesbien);

                if (ronda != pronostico.getRonda()) {

                    if (cuentarondas == (puntosPersona / multip) - puntosar - (fasesbien * masFase)) {
                        puntosPersona = puntosPersona + masRonda;
                        rondasbien++;
                    }
                    ronda = pronostico.getRonda();
                    puntosar = puntosPersona / multip;
                    cuentarondas = 0;
                }
                cuentarondas++;

                System.out.println("fase: "+fase+" cuentafase: "+cuentafase+" calc: "+((puntosPersona / multip) - puntosafase - (rondasbien * masRonda))
                + " puntosafase: "+puntosafase+" rb: "+rondasbien);
                System.out.println("\n");

                if (fase != pronostico.getFase()) {
                    if (cuentafase == (puntosPersona / multip) - puntosafase - (rondasbien * masRonda)) {
                        puntosPersona = puntosPersona + masFase;
                        fasesbien++;
                    }
                    fase = pronostico.getFase();
                    puntosafase = puntosPersona / multip;
                    cuentafase = 0;
                }
                cuentafase++;

                puntosPersona = puntosPersona + (pronostico.getPuntos() * multip);
            }

//          ultima entrada
            else if (i == aPro.size() - 1) {


                if (ronda != pronostico.getRonda()) {
                    ronda = pronostico.getRonda();
                    cuentarondas = 1;
                    puntosar = puntosPersona / multip;
                }

                if (fase != pronostico.getFase()) {
                    fase = pronostico.getFase();
                    cuentafase = 1;
                    puntosafase = puntosPersona / multip;
                }

                puntosPersona = puntosPersona + (pronostico.getPuntos() * multip);

                System.out.println("ronda:" +ronda+" cuentarondas: "+cuentarondas+" calc:"+((puntosPersona / multip) - puntosar - (fasesbien * masFase))
                        + " puntosar: "+puntosar+" fb: "+fasesbien);

                if (cuentarondas == (puntosPersona / multip) - puntosar - (fasesbien * masFase)) {
                    puntosPersona = puntosPersona + masRonda;
                }

                System.out.println("fase: "+fase+" cuentafase: "+cuentafase+" calc: "+((puntosPersona / multip) - puntosafase - (rondasbien * masRonda))
                        + " puntosafase: "+puntosafase+" rb: "+rondasbien);

                if (cuentafase == (puntosPersona / multip) - puntosafase - (rondasbien * masRonda)) {
                    puntosPersona = puntosPersona + masFase;
                }
                System.out.println(nombrePersona + " obtuvo " + puntosPersona + " puntos.");

                System.out.println("\n");
            }

//          Cambio de nombre
            else {

                System.out.println("ronda:" +ronda+" cuentarondas: "+cuentarondas+" calc:"+((puntosPersona / multip) - puntosar - (fasesbien * masFase))
                        + " puntosar: "+puntosar+" fb: "+fasesbien);

                if (cuentarondas == (puntosPersona / multip) - puntosar-(fasesbien*masFase)) {
                    puntosPersona = puntosPersona + masRonda;
                }
                ronda = pronostico.getRonda();
                cuentarondas = 1;

                System.out.println("fase: "+fase+" cuentafase: "+cuentafase+" calc: "+((puntosPersona / multip) - puntosafase - (rondasbien * masRonda))
                        + " puntosafase: "+puntosafase+" rb: "+rondasbien);

                if (cuentafase == (puntosPersona / multip) - puntosafase-(rondasbien*masRonda)) {
                    puntosPersona = puntosPersona + masFase;
                }

                System.out.println("\n");

                fase = pronostico.getFase();
                cuentafase = 1;

                System.out.println(nombrePersona + " obtuvo " + puntosPersona + " puntos.");
                nombrePersona = pronostico.getNombre();
                puntosPersona = pronostico.getPuntos() * multip;

                puntosar = 0;
                puntosafase = 0;
            }
        }

//      CONEXION DB---------------------------------------------------
        ///C:\xampp\phpMyAdmin\config.inc.php hay que cambiar la contraseña
//        String USERDB=configuracion.get("USERDB");
//        String PASSDB=configuracion.get("PASSDB");
//        String URLDB=configuracion.get("URLDB");
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
////                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/tpdatos","root","root");
//            Connection con=DriverManager.getConnection("jdbc:mysql://"+URLDB,USERDB,PASSDB);
//            Statement stmt=con.createStatement();
////                USO DE LA DBl
//            con.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }



    }
}
/*
Entrega 3
En esta entrega se deben poder leer los pronósticos desde una base de datos MySQL.
Por otro lado, debe poder ser configurable la cantidad de puntos que se otorgan cuando se acierta
un resultado (ganar, perder, empatar). !!LISTO
Finalmente, se agregan 2(dos) reglas para la asignación de puntajes de los participantes:
● Se suman puntos extra cuando se aciertan todos los resultados de una ronda. !!LISTO
● Se suman puntos extra cuando se aciertan todos los resultados de una fase !!Falta
(nuevamente, hace falta modificar los archivos para agregar este dato) sobre un
equipo. Se debe considerar que una fase es un conjunto de rondas.
Se recomienda analizar qué estrategia se puede aplicar para incluir otras nuevas reglas con el
menor impacto posible, de forma simple.
En esta entrega, el programa debe:
● Estar actualizado en el repositorio de Git.
● Recibir como argumento un archivo con los resultados y otro con configuración, por
ejemplo: conexión a la DB, puntaje por partido ganado, puntos extra, etc.

Si, vas a tener que modificar los archivos para validar ese comportamiento, igual que cuando lo tuvieron que hacer por las excepciones
Una fase es un conjunto de rondas, no importa cuántas, la idea es que el sistema soporte que haya fases y que las mismas estén compuestas por rondas, idealmente re recomiendo que pruebes con 2 porque sino pierde un poco el sentido.
Respecto del archivo de configuración lo que queremos es que utilizan una mecánica que se usa mucho en software que consiste en establecer configuraciones por medio de un archivo para darle versatilidad al programa que están construyendo.
Con que agreguen la base configuración de la base y los puntos.

En general los archivos de configuración se arman con un par clave valor, por ejemplo:
Puntos_Extra_Ronda=2
*/