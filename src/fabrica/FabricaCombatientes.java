package fabrica;

import combatiente.Combatiente;
import combatiente.CultivadorDeLaEspada;
import combatiente.MonjeDelCuerpoCelestial;
import combatiente.HechiceroDelVacio;
import escuela.Escuela;

public class FabricaCombatientes {

	private static int siguienteId = 1;

	public Combatiente crearCombatiente(TipoCombatiente tipo, String nombre, Escuela escuela) {
		if (null == tipo) {
			throw new IllegalArgumentException("El tipo de combatiente no puede ser nulo.\n");
		}
		if (null == escuela) {
			throw new IllegalArgumentException("La escuela no puede ser nula.\n");
		}

		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre del combatiente es obligatorio");
		}

		int id = siguienteId;

		Combatiente combatiente;

		switch (tipo) {
		case CULTIVADOR_ESPADA:
			combatiente = new CultivadorDeLaEspada(id, nombre);
			break;

		case MONJE_CELESTIAL:
			combatiente = new MonjeDelCuerpoCelestial(id, nombre);
			break;

		case HECHICERO_VACIO:
			combatiente = new HechiceroDelVacio(id, nombre);
			break;

		default:
			throw new IllegalArgumentException("Tipo de combatiente desconocido");
		}

		escuela.prepararCombatiente(combatiente);

		siguienteId++;

		return combatiente;
	}
}
