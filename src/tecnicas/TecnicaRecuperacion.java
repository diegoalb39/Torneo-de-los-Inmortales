package tecnicas;

import combatiente.Combatiente;

public class TecnicaRecuperacion extends Tecnica {

	private final int vidaRecuperada;

	public TecnicaRecuperacion(String nombre, int costoEnergia, int vidaRecuperada) {
		super(nombre, costoEnergia);
		if (vidaRecuperada < 0) {
			throw new IllegalArgumentException("La recuperación no puede ser negativa");
		}
		this.vidaRecuperada = vidaRecuperada;
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		ejecutor.recuperarVida(vidaRecuperada);
	}

	@Override
	public TipoTecnica getTipo() {
		return TipoTecnica.RECUPERACION;
	}
}
