package combatiente;

public class MonjeDelCuerpoCelestial extends Combatiente {

    private static final double MULTIPLICADOR_DANIO = 0.65;
    private static final int REDUCCION_PROXIMO_GOLPE = 20;
    
    private int escudoPendiente = 0;

	public MonjeDelCuerpoCelestial(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    protected int aplicarReduccionDanioTipo(int danio) {
        int danioReducido = (int) Math.round(danio * MULTIPLICADOR_DANIO);
        int resultado = Math.max(0, danioReducido - escudoPendiente);

        escudoPendiente = 0;
        return resultado;
    }
    
    // La caída del compañero activa un escudo para el próximo golpe.
    @Override
    protected boolean alCaerCompanero() {
        escudoPendiente = REDUCCION_PROXIMO_GOLPE;
        return true;
    }

    // Elimina el escudo pendiente al comenzar otro combate.
    @Override
    protected void reiniciarReaccion() {
        escudoPendiente = 0;
    }
}
