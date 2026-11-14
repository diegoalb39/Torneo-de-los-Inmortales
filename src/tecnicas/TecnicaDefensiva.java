package tecnicas;

import combatiente.Combatiente;

public class TecnicaDefensiva extends Tecnica {

    private final int reduccion;

    public TecnicaDefensiva(String nombre, int costoEnergia, int reduccion) {
        super(nombre, costoEnergia);
        this.reduccion = reduccion;
    }

    @Override
    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        ejecutor.agregarDefensaTemporal(reduccion);
    }
}

//Ej: Tecnica muro = new TecnicaDefensiva("Muro del Cuerpo Dorado", 15, 10);