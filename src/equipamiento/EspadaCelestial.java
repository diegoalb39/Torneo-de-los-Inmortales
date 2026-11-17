package equipamiento;

public class EspadaCelestial extends Equipamiento {
	private static final int BONO_DANIO = 10;

	// Inicializa el objeto con su nombre y categoría correspondiente.
	public EspadaCelestial() {
		super("Espada Celestial", CategoriaEquipamiento.ARMA);
	}

	// Aumenta el daño de todos los ataques del portador.
	@Override
	public int bonoDanio() {
		return BONO_DANIO;
	}
}