import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class  Testdatos {
    public static void main(String[] args)  {
        //C:/xampp/phpMyAdmin/config.inc.php hay que cambiar la contraseña
        Testdatos pro = new Testdatos();
        pro.createConnection();


    }
    void createConnection(){
        Partido p;
        String [] datos;
        List<String> itemList = new ArrayList<String>();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pronosticofinal","root","");
            Statement stmt= con.createStatement();
            ResultSet rs =stmt.executeQuery("SELECT * FROM tabla1");
            if (rs.next()){
                do {
                    String resString = rs.getString(2) + "--" + rs.getInt(1);
                itemList.add(resString);
                }while(rs.next());
            }





            else {
                System.out.println("Not Found");
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }



}



        //String USERDB = configuracion.get("root");
       // String PASSDB = configuracion.get("root");
       //String URLDB = configuracion.get("URLDB");
       // try {
      //      Class.forName("com.mysql.cj.jdbc.Driver");
      //      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pronostico", "root", "root");
     //       con = DriverManager.getConnection("jdbc:mysql://" + URLDB, USERDB, PASSDB);
      //      Statement stmt = con.createStatement();
       //     //USO DE LA DBl
     //       con.close();
     //   } catch (Exception e) {
      //      e.printStackTrace();
     //       void mostrarTabla () {
//
      //      }
      //      System.out.println();


