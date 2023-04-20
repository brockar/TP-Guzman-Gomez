public class Ronda {
    private int numero;
    private int fase;
    private Partido partido[];
    private int partidosJugados;
    private int puntos;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Partido[] getPartido() {
        return partido;
    }

    public void setPartido(Partido[] partido) {
        this.partido = partido;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) { this.puntos = puntos;}
    public int getPartidosJugados () {return partidosJugados;}
    public void setPartidosJugados ( int partidosJugados){ this.partidosJugados = partidosJugados;}
    public int getFase() {return fase;}
    public void setFase(int fase) {this.fase = fase;}
}


