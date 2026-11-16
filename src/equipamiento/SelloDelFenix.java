package equipamiento;

public class SelloDelFenix extends Equipamiento {
    private static final int BONO_VIDA = 25;

    // Inicializa el objeto con su nombre y categoría correspondiente.
    public SelloDelFenix() {
        super("Sello del Fénix", CategoriaEquipamiento.SELLO);
    }

    // Incrementa la vida máxima del portador.
    @Override
    public int bonoVidaMaxima() {
        return BONO_VIDA;
    }
}
