package tecnicas;

import combatiente.Combatiente;

public class TecnicaDrenaje extends Tecnica {

    private final int danioBase;
    private final int energiaDrenada;

    public TecnicaDrenaje(String nombre, int costoEnergia,
                         int danioBase, int energiaDrenada) {
        super(nombre, costoEnergia);
        this.danioBase = danioBase;
        this.energiaDrenada = energiaDrenada;
    }

    @Override
    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        int danio = ejecutor.calcularDanioSaliente(danioBase);

        objetivo.recibirDanio(danio);
        objetivo.perderEnergia(energiaDrenada);
    }
    @Override
    public TipoTecnica getTipo() {
        return TipoTecnica.OFENSIVA; // daña al rival, se elige como ofensiva
    }
}

//Provocan danio y quitan energia.
//Ej: Tecnica dominio = new TecnicaDrenaje("Dominio de la Nada", 20, 20, 10);