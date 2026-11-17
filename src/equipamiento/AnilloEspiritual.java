package equipamiento;

public class AnilloEspiritual extends Equipamiento {
    private static final int BONO_ENERGIA = 20;

    // Inicializa el objeto con su nombre y categoría correspondiente.
	public AnilloEspiritual() {
        super("Anillo Espiritual", CategoriaEquipamiento.ANILLO);
    }

    // Incrementa la energía máxima del portador.
    @Override
    public int bonoEnergiaMaxima() {
        return BONO_ENERGIA;
    }
}