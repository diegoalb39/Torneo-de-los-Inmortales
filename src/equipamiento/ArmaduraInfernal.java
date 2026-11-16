package equipamiento;

public class ArmaduraInfernal extends Equipamiento {
    private static final int REDUCCION_DANIO = 8;

    // Inicializa el objeto con su nombre y categoría correspondiente.
    public ArmaduraInfernal() {
        super("Armadura Infernal", CategoriaEquipamiento.ARMADURA);
    }

    // Reduce el daño recibido por el combatiente.
    @Override
    public int bonoReduccionDanio() {
        return REDUCCION_DANIO;
    }
}
