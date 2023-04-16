//package tp;
public class Partido {
    private int numPartido;
    private Equipo equipo1;
    private Equipo equipo2;
    private int golesEquipo1;
    private int golesEquipo2;

    public ResultadoEnum resultadoPart(Equipo equipo) {
        ResultadoEnum ret = null;
        if(equipo!=null) {
            //comparar q equipo es primero
            if (equipo.getNombre().equals(equipo1.getNombre())) {
                if (golesEquipo1 > golesEquipo2) {
                    ret = ResultadoEnum.GANADOR;
                } else if (golesEquipo1 == (golesEquipo2)) {
                    ret = ResultadoEnum.EMPATE;
                } else {
                    ret = ResultadoEnum.PERDEDOR;
                }
            } else if (equipo.getNombre().equals(equipo2.getNombre())) {
                if (golesEquipo1 < golesEquipo2) {
                    ret = ResultadoEnum.GANADOR;
                } else if (golesEquipo1 == (golesEquipo2)) {
                    ret = ResultadoEnum.EMPATE;
                } else {
                    ret = ResultadoEnum.PERDEDOR;
                }
            }
        }
        else if(equipo==null){
            if (golesEquipo1 == golesEquipo2) ret=ResultadoEnum.EMPATE;
        }
        return ret;
    }

    public int getNumPartido() {
        return numPartido;
    }

    public void setNumPartido(int numPartido) {
        this.numPartido = numPartido;
    }

    public Equipo getEquipo1() {
        return equipo1;
    }

    public void setEquipo1(Equipo equipo1) {
        this.equipo1 = equipo1;
    }

    public Equipo getEquipo2() {
        return equipo2;
    }

    public void setEquipo2(Equipo equipo2) {
        this.equipo2 = equipo2;
    }

    public int getGolesEquipo1() {
        return golesEquipo1;
    }

    public void setGolesEquipo1(int golesEquipo1) {
        this.golesEquipo1 = golesEquipo1;
    }

    public int getGolesEquipo2() {
        return golesEquipo2;
    }

    public void setGolesEquipo2(int golesEquipo2) {
        this.golesEquipo2 = golesEquipo2;
    }

    public void print(){
        System.out.println(
                "numP "+
                this.getNumPartido()+ " "+
                equipo1.getNombre()+ " " +
                this.golesEquipo1 + " " +
//                equipo1.getDescripcion() + " " +
                equipo2.getNombre() + " " +
//                equipo2.getDescripcion() + " " +

                this.golesEquipo2
        );
    }

}