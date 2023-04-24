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
        //Lectura de configuracion ------------------------------------
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

        // Lectura PARTIDOS ----------------
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

        // Lectura PRONOSTICOS -----------------------------
//        ArrayList<Pronostico> aPro = new ArrayList<>();
//        Pronostico pro;
//        String[] datos0;
//        try (Scanner scFile = new Scanner(new File("src/main/java/pronostico.csv"))) {
//            while (scFile.hasNextLine()) {
//                datos0 = scFile.nextLine().split(";");
//                pro = new Pronostico();
//                pro.setNumPartido(Integer.parseInt(datos0[0]));
//                // Mira que partido es y si existe.
//                boolean existe = false;
//                for (Partido pa : aP) {
//                    if (pa.getNumPartido() == pro.getNumPartido()) {
//                        existe = true;
//                        break;
//                    }
//                }
//                if (existe) {
//                    Equipo e = new Equipo();
//                    e.setNombre(datos0[1]);
//                    Equipo e1 = new Equipo();
//                    e1.setNombre(datos0[5]);
//                    //Se fija por quien aposto el del pronostico
//                    //Si esta marcado la columna 1 es que apuesta por el equipo1 (e),
//                    //Si esta marcada la columna 3 es que apuesta por el equipo2 (e1),
//                    //Si esta marcada la columna del medio es empate, por lo tanto no se asigna equipo.
//                    if (!datos0[2].isEmpty() && Integer.parseInt(datos0[2]) == 1) {
//                        pro.setEquipo(e);
//                    } else if (!datos0[4].isEmpty() && Integer.parseInt(datos0[4]) == 1) {
//                        pro.setEquipo(e1);
//                    }
//                    pro.setNombre(datos0[6]);
//                    pro.setRonda(Integer.parseInt(datos0[7]));
//                    pro.setFase(Integer.parseInt(datos0[8]));
//
//                    //busca el ResultadoEnum del equipo que aposto y lo agrega
//                    pro.setResultado(aP.get(pro.getNumPartido()).resultadoPart(pro.getEquipo()));
//                    //Agrega los puntos correspondientes a los que tenia.
//                    pro.setPuntos(pro.fpuntos());
//                    aPro.add(pro);
//                }
//            }
//        } catch (Exception error1) {
//            error1.printStackTrace();
//        }

//      TERMINA lectura de PRONOSTICOS

//      CONEXION DB---------------------------------------------------
        Pronostico pro;
        ArrayList<Pronostico> aPro = new ArrayList<>();

        //C:\xampp\phpMyAdmin\config.inc.php hay que cambiar la contraseña
        String USERDB=configuracion.get("USERDB");
        String PASSDB=configuracion.get("PASSDB");
        String URLDB=configuracion.get("URLDB");
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
//          Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/tpdatos","root","root");
            Connection con=DriverManager.getConnection("jdbc:mysql://"+URLDB,USERDB,PASSDB);
//            Connection con=DriverManager.getConnection("jdbc:mysql://"+URLDB,USERDB,"");
            Statement stmt=con.createStatement();
//             USO DE LA DBl

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

                    if (rs.getInt("GanaEquipo1") !=0  && rs.getInt("GanaEquipo1") ==1 ) {
                        pro.setEquipo(e);
                    } else if (rs.getInt("GanaEquipo2") != 0 && rs.getInt("GanaEquipo2")==1) {
                        pro.setEquipo(e1);
                    }
                    pro.setNombre(rs.getString("Nombre"));
                    pro.setRonda(rs.getInt("Ronda"));
                    pro.setFase(rs.getInt("Fase"));

                    //busca el ResultadoEnum del equipo que aposto y lo agrega
                    pro.setResultado(aP.get(pro.getNumPartido()).resultadoPart(pro.getEquipo()));
                    //Agrega los puntos correspondientes a los que tenia.
                    pro.setPuntos(pro.fpuntos());
                    aPro.add(pro);
                }
            }
//          TERMINA EL USO
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

//      TERMINA CONEXION DB

//      Recuento PUNTOS  --------------------------------------------
//      M=4, 1 fase, 1 ronda == 6
//      P=8, 2 fase, 3 ronda == 13
        int puntosPersona = 0;
        String nombrePersona = "";

        int multip = Integer.parseInt(configuracion.get("PuntosPart"));
        int masRonda = Integer.parseInt(configuracion.get("PuntosRonda"));
        int masFase = Integer.parseInt(configuracion.get("PuntosFase"));
        int ronda = 0;
        int fase = 0;
        int cuentarondas = 0;
        int rondasbien = 0;
        int cuentafase = 0;
        int fasesbien = 0;
        int paRonda=0;
        int paFase=0;



        for (int i = 0; i < aPro.size(); i++) {
            Pronostico pronostico = aPro.get(i);
//          Primer entrada
            if (i == 0) {
                nombrePersona = pronostico.getNombre();
                puntosPersona = pronostico.getPuntos();

                ronda = pronostico.getRonda();
                fase = pronostico.getFase();
                cuentarondas++;
                cuentafase++;
            }

//          entradas de una misma persona
            else if (nombrePersona.equals(pronostico.getNombre()) && i != aPro.size() - 1) {
//              si cambia de ronda en la misma persona
                if (ronda != pronostico.getRonda()) {
                    if (cuentarondas == (puntosPersona)-paRonda){
                        rondasbien++;
                    }
                    paRonda=puntosPersona;
                    ronda = pronostico.getRonda();
                    cuentarondas=0;
                }
                cuentarondas++;

//              si cambia de fase en la misma persona
                if (fase != pronostico.getFase()) {
                    if (cuentafase == (puntosPersona)-paFase) {
                        fasesbien++;
                    }
                    paFase=puntosPersona;
                    fase = pronostico.getFase();
                    cuentafase = 0;
                }
                cuentafase++;
                puntosPersona = puntosPersona + (pronostico.getPuntos());
            }

//          ultima entrada
            else if (i == aPro.size() - 1) {
                if (ronda != pronostico.getRonda()) {
                    ronda = pronostico.getRonda();
                    cuentarondas = 1;
                    paRonda=puntosPersona;
                }

                if (fase != pronostico.getFase()) {
                    fase = pronostico.getFase();
                    cuentafase = 1;
                    paFase=puntosPersona;
                }
                puntosPersona = puntosPersona + (pronostico.getPuntos());

                if (cuentarondas == (puntosPersona-paRonda)) {
                    rondasbien++;
                }

                if (cuentafase == (puntosPersona-paFase)) {
                    fasesbien++;
                }

//              agrega puntos de rondas y fases
                puntosPersona = (puntosPersona*multip)+(rondasbien*masRonda)+(fasesbien*masFase);
                System.out.println(nombrePersona + " obtuvo " + puntosPersona + " puntos.");
            }

//          Cambio de persona
            else {
//              se fija si la ultima entrada es una ronda aparte
                if (ronda != pronostico.getRonda()) {
                    cuentarondas = 1;
                    paRonda=puntosPersona-1;
                }

//              se fija si la ultima entrada es una fase aparte
                if (fase != pronostico.getFase()) {
                    cuentafase = 1;
                    paFase=puntosPersona-1;
                }

                if (cuentarondas == (puntosPersona-paRonda)) {
                    rondasbien++;
                }
//              para la proxima persona
                paRonda=0;
                ronda = pronostico.getRonda();
                cuentarondas = 1;

                if (cuentafase == (puntosPersona-paFase)) {
                    fasesbien++;
                }
//              para la proxima persona
                paFase=0;
                fase = pronostico.getFase();
                cuentafase = 1;

                puntosPersona= (puntosPersona*multip)+(rondasbien*masRonda)+(fasesbien*masFase);
                System.out.println(nombrePersona + " obtuvo " + puntosPersona + " puntos.");

//              cambio de persona
                nombrePersona = pronostico.getNombre();
                puntosPersona = pronostico.getPuntos();
                rondasbien=0;
                fasesbien=0;
            }
        }
//      TERMINA LECTURA DE PUNTOS

    }
}