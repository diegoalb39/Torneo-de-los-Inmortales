package torneo;

import combatiente.Combatiente;
import combatiente.CultivadorDeLaEspada;
import combatiente.HechiceroDelVacio;
import combatiente.MonjeDelCuerpoCelestial;
import pareja.Pareja;
import tecnicas.Tecnica;
import tecnicas.TecnicaDefensiva;
import tecnicas.TecnicaDrenaje;
import tecnicas.TecnicaOfensiva;
import tecnicas.TecnicaRecuperacion;

public class TorneoDeLosInmortales {

	public static void main(String[] args) {
		// 1) Técnicas compartidas (misma instancia para que la regla del compañero las reconozca)
		Tecnica corte = new TecnicaOfensiva("Corte de los Mil Amaneceres", 10, 25);
		Tecnica rugido = new TecnicaOfensiva("Rugido de la Bestia Primordial", 25, 60);
		Tecnica drenaje = new TecnicaDrenaje("Succion del Vacio", 15, 20, 10);
		Tecnica escudo = new TecnicaDefensiva("Escudo Celestial", 10, 5);
		Tecnica meditacion = new TecnicaRecuperacion("Meditación del Inmortal", 15, 30);

		// 2) Crear 8 combatientes con ids únicos (1 a 8)
		Combatiente aurelio = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente nebula = new HechiceroDelVacio(2, "Nébula");
		Combatiente kael = new MonjeDelCuerpoCelestial(3, "Kael");
		Combatiente darian = new CultivadorDeLaEspada(4, "Darian");

		Combatiente seraph = new HechiceroDelVacio(5, "Seraph");
		Combatiente lyra = new MonjeDelCuerpoCelestial(6, "Lyra");
		Combatiente orion = new CultivadorDeLaEspada(7, "Orión");
		Combatiente vega = new HechiceroDelVacio(8, "Vega");

		// 3) Enseñar técnicas (el orden de aprendizaje es el orden de la escuela)
		Combatiente[] todos = { aurelio, nebula, kael, darian, seraph, lyra, orion, vega };
		for (Combatiente c : todos) {
			c.aprenderTecnica(corte);
			c.aprenderTecnica(escudo);
			c.aprenderTecnica(meditacion);
		}
		// Algunos con técnicas extra para variar
		nebula.aprenderTecnica(drenaje);
		vega.aprenderTecnica(drenaje);
		aurelio.aprenderTecnica(rugido);
		orion.aprenderTecnica(rugido);

		// 4) Crear 4 parejas (ids únicos 1 a 4)
		Pareja p1 = new Pareja(1, aurelio, nebula);
		Pareja p2 = new Pareja(2, kael, darian);
		Pareja p3 = new Pareja(3, seraph, lyra);
		Pareja p4 = new Pareja(4, orion, vega);

		// 5) Inscribir en el torneo
		Torneo torneo = new Torneo();
		torneo.inscribir(p1);
		torneo.inscribir(p2);
		torneo.inscribir(p3);
		torneo.inscribir(p4);

		// 6) Pruebas de rechazo (cada una debería imprimir "OK, rechazada")
		probarRechazo("Pareja repetida (mismo id)", torneo, new Pareja(1, aurelio, nebula));
		probarRechazo("Combatiente ya inscripto (Aurelio con Vega)", torneo, new Pareja(5, aurelio, vega));
		probarRechazo("Combatiente ya inscripto en segunda posición", torneo, new Pareja(6,
				new CultivadorDeLaEspada(9, "Nuevo"), kael));

		// Cupo lleno con combatientes nuevos (hoy solo imprime un mensaje, no lanza excepción)
		System.out.println("\nProbando cupo lleno:");
		torneo.inscribir(new Pareja(7, new CultivadorDeLaEspada(10, "Extra1"),
				new HechiceroDelVacio(11, "Extra2")));

		// 7) Iniciar (combate la primera ronda)
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