package tecnicas;

import combatiente.Combatiente;

public class TecnicaRecuperacion extends Tecnica {

    private final int vidaRecuperada;

    public TecnicaRecuperacion(String nombre, int costoEnergia,
                              int vidaRecuperada) {
        super(nombre, costoEnergia);
        this.vidaRecuperada = vidaRecuperada;
    }

    @Override
    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        ejecutor.recuperarVida(vidaRecuperada);
    }
}

//Ej: Tecnica meditacion = new TecnicaRecuperacion("Meditación del Inmortal", 15, 30);
