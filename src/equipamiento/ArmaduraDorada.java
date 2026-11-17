package equipamiento;

public class ArmaduraDorada extends Equipamiento {
	private static final int REDUCCION_DANIO = 12;

	// Inicializa el objeto con su nombre y categoría correspondiente.
	public ArmaduraDorada() {
		super("Armadura Dorada", CategoriaEquipamiento.ARMADURA);
	}

	// Brinda una protección superior a la armadura infernal.
	@Override
	public int bonoReduccionDanio() {
		return REDUCCION_DANIO;
	}
}
