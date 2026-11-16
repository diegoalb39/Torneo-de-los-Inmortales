package combatiente;

public class HechiceroDelVacio extends Combatiente{
	
	private static final double MULTIPLICADOR_ENERGIA = 0.5;
	private static final int ENERGIA_RECUPERADA_POR_CAIDA = 15;
	
	public HechiceroDelVacio(int id, String nombre) {
		super(id, nombre);
	}
	
	@Override
	protected int aplicarReduccionCostoEnergiaTipo(int costo) {
		return (int) Math.round(costo * MULTIPLICADOR_ENERGIA);
	}
	
    // Recupera energía inmediatamente cuando cae su compañero.
    @Override
    protected boolean alCaerCompanero() {
        recuperarEnergia(ENERGIA_RECUPERADA_POR_CAIDA);
        return true;
    }

}
