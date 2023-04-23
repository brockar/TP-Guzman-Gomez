//package tp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Archivo {
    public static void main(String[] args) {
        //Lectura de configuracion

        HashMap<String, String> configuracion = new HashMap<String, String>();
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
                boolean correcta=false;

                if(datos.length==7){
//                    \\d+ son los enteros en regex
                    if( datos[0].matches("\\d+") && datos[2].matches("\\d+") && datos[3].matches("\\d+") && datos[5].matches("\\d+") && datos[6].matches("\\d+") ){
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
                    p.setRonda(Integer.parseInt(datos[5]));

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
        Pronostico pro;
        List<String> aPro = new ArrayList<String>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pronosticofinal", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tabla1");
            while (rs.next()) {
                pro = new Pronostico();

                pro.setNumPartido(rs.getInt("NumPartido"));

                boolean existe = false;
                for (Partido pa : aP) {
                    if (pa.getNumPartido() == pro.getNumPartido()) {
                        existe = true;
                        break;
                    }
                }
                if (existe) {
                    Equipo e = new Equipo();
                    e.setNombre(rs.getString("Equipo1"));
                    Equipo e1 = new Equipo();
                    e1.setNombre(rs.getString("Equipo2"));


                    if (rs.getInt("GanaEquipo1") == 1) {
                        pro.setEquipo(e);
                    } else if (rs.getInt("GanaEquipo2") == 1) {
                        pro.setEquipo(e1);

                    }
                    pro.setNombre("Nombre");
                    pro.setRonda(Integer.parseInt("Ronda"));
                    pro.setFase(Integer.parseInt("Fase"));


                    //busca el ResultadoEnum del equipo que aposto y lo agrega
                    pro.setResultado(aP.get(pro.getNumPartido()).resultadoPart(pro.getEquipo()));
                    //Agrega los puntos correspondientes a los que tenia.
                    pro.setPuntos(pro.fpuntos());
                    aPro.add(String.valueOf(pro));

                }


            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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
//        //M=2 P=3
        int puntosPersona = 0;
        String nombrePersona = "";

        int[] partxronda = new int[10];
        int iter=0;
        int multip = Integer.parseInt(configuracion.get("PuntosPart"));

<<<<<<< HEAD
        int masRonda=Integer.parseInt(configuracion.get("PuntosRonda"));
        int masFase=Integer.parseInt(configuracion.get("PuntosFase"));
=======
//        HashMap<String, int[]> rondasg = new HashMap<String, int[]>();
//        int[] a = new int [10];
>>>>>>> parent of 5e902c5 (cuento rondas y falta fases)

        //#Tendria que guardarlos en un arraylist con todos los partidos que acertaron y ver si son de la misma ronda y fase para darle bien los puntos extras
        //ya que las fases pueden ser de dos rondas no contiguas
        for (int i = 0; i < aPro.size(); i++) {
            Pronostico pronostico = aPro.get(i);
//            Primer entrada
            if (i == 0) {
                nombrePersona = pronostico.getNombre();
                puntosPersona = pronostico.getPuntos() * multip ;
                partxronda[iter]=1;
            }
//            resto de entradas excepto la ultima
            else if (nombrePersona.equals(pronostico.getNombre()) && i != aPro.size() - 1) {
                puntosPersona = puntosPersona + (pronostico.getPuntos() * multip );
                partxronda[iter]++;
            }
//            ultima entrada
            else if (i == aPro.size() - 1) {
                puntosPersona = puntosPersona + (pronostico.getPuntos() * multip);

                partxronda[iter]++;

                if(partxronda[iter]==(puntosPersona/multip)){
                    puntosPersona= puntosPersona + Integer.parseInt(configuracion.get("PuntosRonda"));
//                  para saber las rondas que tienen ganadas
//                    if(rondasg.get(nombrePersona)!=null) {
//                        a = rondasg.get(nombrePersona);
//                    }
//                    a[pronostico.getRonda()-1]++;
//                    rondasg.put(nombrePersona,a);
                }

                System.out.println(nombrePersona + " obtuvo " + puntosPersona + " puntos.");
                }
            else {
                if(partxronda[iter]==(puntosPersona/multip)){
                    puntosPersona= puntosPersona + Integer.parseInt(configuracion.get("PuntosRonda"));
//                  para saber las rondas que tienen ganadas
//                    if(rondasg.get(nombrePersona)!=null) {
//                        a = rondasg.get(nombrePersona);
//                    }
//                    a[pronostico.getRonda()-1]++;
//                    rondasg.put(nombrePersona,a);
                }
                System.out.println(nombrePersona + " obtuvo " + puntosPersona + " puntos.");
                iter++;
                partxronda[iter]++;
                nombrePersona= pronostico.getNombre();
                puntosPersona= pronostico.getPuntos() * multip;
            }


//            int[] fases = new int[3];
//            fases[0]=1;
//
//            for (String clave : rondasg.keySet()) {
//                boolean fase = true;
//                int[] valores = rondasg.get(clave);
//                for(int f: fases){
//                    for(int z=0; z<f;z++){
//                        if(valores[z]==1)fase=true;
//                        else fase=false;
//                    }
//                    if(fase) puntosPersona=puntosPersona+Integer.parseInt(configuracion.get("PuntosFase"));
//                }
//            }

        }

//        for (String clave : rondasg.keySet()) {
//            int[] valores = rondasg.get(clave);
//            System.out.print("Clave: " + clave + ", Valores: ");
//            for (int valor : valores) {
//                System.out.print(valor + " ");
//            }
//            System.out.println();
//        }

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


    }}

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