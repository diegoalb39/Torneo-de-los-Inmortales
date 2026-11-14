package combatiente;

public class MonjeDelCuerpoCelestial extends Combatiente {

    private static final double MULTIPLICADOR_DANIO = 0.65;

    public MonjeDelCuerpoCelestial(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    protected int aplicarReduccionDanioTipo(int danio) {
        return (int) Math.round(danio * MULTIPLICADOR_DANIO);
    }
}
