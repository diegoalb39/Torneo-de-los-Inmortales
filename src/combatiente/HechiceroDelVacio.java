package combatiente;

public class HechiceroDelVacio extends Combatiente{
	
	private static final double MULTIPLICADOR_ENERGIA = 0.5;
	
	public HechiceroDelVacio(int id, String nombre) {
		super(id, nombre);
	}
	
	@Override
	protected int aplicarReduccionCostoEnergiaTipo(int costo) {
		return (int) Math.round(costo * MULTIPLICADOR_ENERGIA);
	}

}
