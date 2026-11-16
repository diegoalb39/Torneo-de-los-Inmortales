package tecnicas;

import combatiente.Combatiente;

public class TecnicaDefensiva extends Tecnica {

    private final int reduccion;

    public TecnicaDefensiva(String nombre, int costoEnergia, int reduccion) {
        super(nombre, costoEnergia);
        if (reduccion < 0) throw new IllegalArgumentException("La reducción no puede ser negativa");
        this.reduccion = reduccion;
    }

    @Override
    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        ejecutor.agregarDefensaTemporal(reduccion);
   }
    @Override
    public TipoTecnica getTipo() {
        return TipoTecnica.DEFENSIVA;
    }
}

//Ej: Tecnica muro = new TecnicaDefensiva("Muro del Cuerpo Dorado", 15, 10);