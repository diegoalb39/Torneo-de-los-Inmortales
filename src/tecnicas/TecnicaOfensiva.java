package tecnicas;

import combatiente.Combatiente;

public class TecnicaOfensiva extends Tecnica {

//	Para todas las tecnicas que se limitan a hacer danio. Ej:
//	Tecnica corte = new TecnicaOfensiva("Corte de los Mil Amaneceres", 10, 25);
//  Tecnica rugido = new TecnicaOfensiva("Rugido de la Bestia Primordial", 25, 60);

	private final int danioBase;

	public TecnicaOfensiva(String nombre, int costoEnergia, int danioBase) {
		super(nombre, costoEnergia);
		if (danioBase < 0) throw new IllegalArgumentException("El daño no puede ser negativo");
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
