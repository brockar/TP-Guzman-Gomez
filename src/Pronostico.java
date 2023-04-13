//package tp;
public class Pronostico{
    //Esto podria estar de mas ya que el mismo arraylist te pone un indice
    private int numPartido;
    private Equipo equipo;
    private ResultadoEnum resultado;
    private int puntos;

    public int fpuntos(){
        int r=0;
        if(equipo!=null){
            if(resultado.ordinal()==0){r=1;}
        } else {
            if(resultado.ordinal()==2){r=1;}
        }
        return r;
    }

    public int getNumPartido() {
        return numPartido;
    }

    public void setNumPartido(int numPartido) {
        this.numPartido = numPartido;
    }

    public Equipo getEquipo() {return equipo;}

    public void setEquipo(Equipo equipo) {this.equipo = equipo;
    }

    public ResultadoEnum getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoEnum resultado) {
        this.resultado = resultado;
    }
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    // No se que otra cosa le falta en el print
    public void print(){
        System.out.println(
                "numP "+
                this.getNumPartido()+ " "+
                this.getEquipo()+" "+
                this.getResultado()+ " "+
                this.getPuntos()
        );
    }

}