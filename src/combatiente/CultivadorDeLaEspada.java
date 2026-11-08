package combatiente;

public class CultivadorDeLaEspada extends Combatiente {
    private static final int BONO_DANIO_ESPADA = 5; 

    public CultivadorDeLaEspada(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    protected int aplicarBonificacionDanioTipo(int danio) {
        return danio + BONO_DANIO_ESPADA;
    }
}