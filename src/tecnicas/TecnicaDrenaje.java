package tecnicas;

import combatiente.Combatiente;

public class TecnicaDrenaje extends Tecnica {

	private final int danioBase;
	private final int energiaDrenada;

	public TecnicaDrenaje(String nombre, int costoEnergia, int danioBase, int energiaDrenada) {
		super(nombre, costoEnergia);
		if (danioBase < 0 || energiaDrenada < 0) {
			throw new IllegalArgumentException("El daño y el drenaje no pueden ser negativos");
		}
		this.danioBase = danioBase;
		this.energiaDrenada = energiaDrenada;
	}

	@Override
	public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
		int danio = ejecutor.calcularDanioSaliente(danioBase);

		objetivo.recibirDanio(danio);
		objetivo.perderEnergia(energiaDrenada);
	}

	@Override
	public TipoTecnica getTipo() {
		return TipoTecnica.OFENSIVA; // daña al rival, se elige como ofensiva
	}
}
