package torneo;

import combatiente.Combatiente;
import escuela.Escuela;
import fabrica.FabricaCombatientes;
import fabrica.TipoCombatiente;
import pareja.Pareja;
import tecnicas.TecnicaDefensiva;
import tecnicas.TecnicaDrenaje;
import tecnicas.TecnicaOfensiva;
import tecnicas.TecnicaRecuperacion;

public class TorneoDeLosInmortales {

	public static void main(String[] args) {
		FabricaCombatientes fabrica = new FabricaCombatientes();

		// Escuela: el orden de agregado es el orden de la escuela (desempates)
		Escuela escuela = new Escuela("Palacio del Vacío");
		escuela.agregarTecnica(new TecnicaOfensiva("Corte de los Mil Amaneceres", 10, 25));
		escuela.agregarTecnica(new TecnicaOfensiva("Rugido de la Bestia Primordial", 25, 60));
		escuela.agregarTecnica(new TecnicaDrenaje("Succión del Vacío", 15, 20, 10));
		escuela.agregarTecnica(new TecnicaDefensiva("Escudo Celestial", 10, 5));
		escuela.agregarTecnica(new TecnicaRecuperacion("Meditación del Inmortal", 15, 30));

		// 1) Crear 8 combatientes con la fábrica (ids 1 a 8, en orden de creación)
		Combatiente aurelio = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Aurelio", escuela);
		Combatiente nebula = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Nébula", escuela);
		Combatiente kael = fabrica.crearCombatiente(TipoCombatiente.MONJE_CELESTIAL, "Kael", escuela);
		Combatiente darian = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Darian", escuela);

		Combatiente seraph = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Seraph", escuela);
		Combatiente lyra = fabrica.crearCombatiente(TipoCombatiente.MONJE_CELESTIAL, "Lyra", escuela);
		Combatiente orion = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Orión", escuela);
		Combatiente vega = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Vega", escuela);

		// 2) Crear 4 parejas (ids únicos 1 a 4)
		Pareja p1 = new Pareja(1, aurelio, nebula);
		Pareja p2 = new Pareja(2, kael, darian);
		Pareja p3 = new Pareja(3, seraph, lyra);
		Pareja p4 = new Pareja(4, orion, vega);

		// 3) Inscribir en el torneo
		Torneo torneo = new Torneo();
		torneo.inscribir(p1);
		torneo.inscribir(p2);
		torneo.inscribir(p3);
		torneo.inscribir(p4);

		// 4) Pruebas de rechazo (cada una debería imprimir "OK, rechazada")
		probarRechazo("Pareja repetida (mismo id)", torneo, new Pareja(1, aurelio, nebula));
		probarRechazo("Combatiente ya inscripto (Aurelio con Vega)", torneo, new Pareja(5, aurelio, vega));
		Combatiente nuevo = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Nuevo", escuela);
		probarRechazo("Combatiente ya inscripto en segunda posición", torneo, new Pareja(6, nuevo, kael));

		// Cupo lleno (hoy solo imprime un mensaje, no lanza excepción)
		System.out.println("\nProbando cupo lleno:");
		Combatiente extra1 = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Extra1", escuela);
		Combatiente extra2 = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Extra2", escuela);
		torneo.inscribir(new Pareja(7, extra1, extra2));

		// 5) Iniciar (combate la primera ronda)
		System.out.println("\n=========== COMIENZA EL TORNEO ===========");
		torneo.iniciarTorneo();
	}

	private static void probarRechazo(String descripcion, Torneo torneo, Pareja pareja) {
		try {
			torneo.inscribir(pareja);
			System.out.println("FALLO: no se rechazó -> " + descripcion);
		} catch (IllegalArgumentException e) {
			System.out.println("OK, rechazada -> " + descripcion + " (" + e.getMessage() + ")");
		}
	}
}