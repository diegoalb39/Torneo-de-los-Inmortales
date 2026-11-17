package tecnicas;

import combatiente.Combatiente;

public class TecnicaOfensiva extends Tecnica {

	private final int danioBase;

	public TecnicaOfensiva(String nombre, int costoEnergia, int danioBase) {
		super(nombre, costoEnergia);
		if (danioBase < 0) {
			throw new IllegalArgumentException("El daño no puede ser negativo");
		}
		this.danioBase = danioBase;
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		int danio = ejecutor.calcularDanioSaliente(danioBase);
		objetivo.recibirDanio(danio);
	}

	@Override
	public TipoTecnica getTipo() {
		return TipoTecnica.OFENSIVA; // daña al rival, se elige como ofensiva
	}
}
