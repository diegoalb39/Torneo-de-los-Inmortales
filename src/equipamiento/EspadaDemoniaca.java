package equipamiento;

public class EspadaDemoniaca extends Equipamiento {
    private static final int BONO_DANIO = 15;

    // Inicializa el objeto con su nombre y categoría correspondiente.
    public EspadaDemoniaca() {
        super("Espada Demoníaca", CategoriaEquipamiento.ARMA);
    }

    // Aporta más daño que la espada común, pero ocupa la misma categoría.
    @Override
    public int bonoDanio() {
        return BONO_DANIO;
    }
}