package combatiente;

public class CultivadorDeLaEspada extends Combatiente {
	private static final int BONO_DANIO_ESPADA = 5;
	private static final int BONO_VENGANZA = 20;

	private int bonoProximoAtaque = 0;

	public CultivadorDeLaEspada(int id, String nombre) {
		super(id, nombre);
	}

	// Aplica el bono propio y consume la venganza si está activa.
	@Override
	protected int aplicarBonificacionDanioTipo(int danio) {
		int resultado = danio + BONO_DANIO_ESPADA + bonoProximoAtaque;
		bonoProximoAtaque = 0;
		return resultado;
	}

	// La caída del compañero potencia el siguiente ataque.
	@Override
	protected boolean alCaerCompanero() {
		bonoProximoAtaque = BONO_VENGANZA;
		return true;
	}

	// Elimina la venganza pendiente al comenzar otro combate.
	@Override
	protected void reiniciarReaccion() {
		bonoProximoAtaque = 0;
	}
}