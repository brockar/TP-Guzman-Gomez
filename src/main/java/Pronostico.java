//package tp;
public class Pronostico {
    private int numPartido;
    private int ronda;
    private int fase;
    private Equipo equipo;
    private String nombre;
    private ResultadoEnum resultado;
    private int puntos;


    public int fpuntos() {
        int r = 0;
        if (equipo != null && resultado != null) {
//            Gano el equipo que aposto
            if (resultado.ordinal() == 0) {
                r = 1;
            }
//            si equipo es null es porque aposto por empate
        } else if (equipo == null && resultado != null) {
            if (resultado.ordinal() == 2) r = 1;
        }
        return r;
    }

    public int getNumPartido() {
        return numPartido;
    }

    public void setNumPartido(int numPartido) {
        this.numPartido = numPartido;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void print() {
        System.out.println(
                "numP " +
                        this.getNumPartido() + " " +
//                this.getEquipo()+" "+
//                this.getResultado()+ " "+
//                this.getPuntos() + " " +
                        this.getNombre()
        );
    }

    public int getRonda() {
        return ronda;
    }

    public void setRonda(int ronda) {
        this.ronda = ronda;
    }

    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }
}
