package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import combatiente.CultivadorDeLaEspada;
import pareja.Pareja;
import tecnicas.TecnicaOfensiva;

class ParejaTest {

	@Test
	void compañeroNoPuedeUsarTecnicaMientrasElPrimeroConserveUsosYEsteEnPie() {
		CultivadorDeLaEspada primero = new CultivadorDeLaEspada(1, "Primero");
		CultivadorDeLaEspada segundo = new CultivadorDeLaEspada(2, "Segundo");
		CultivadorDeLaEspada rival = new CultivadorDeLaEspada(3, "Rival");
		TecnicaOfensiva tecnica = new TecnicaOfensiva("Compartida", 0, 1);
		primero.aprenderTecnica(tecnica);
		segundo.aprenderTecnica(tecnica);
		Pareja pareja = new Pareja(1, primero, segundo);

		primero.ejecutarTecnica(tecnica, rival);
		pareja.registrarUso(primero, tecnica);
		assertFalse(pareja.puedeUsarTecnica(segundo, tecnica));

		for (int i = 1; i < 5; i++) {
			primero.ejecutarTecnica(tecnica, rival);
			pareja.registrarUso(primero, tecnica);
		}
		assertTrue(pareja.puedeUsarTecnica(segundo, tecnica));
	}

	@Test
	void compañeroPuedeUsarTecnicaCuandoElPrimeroCaeYSeLiberaAlRestaurar() {
		CultivadorDeLaEspada primero = new CultivadorDeLaEspada(1, "Primero");
		CultivadorDeLaEspada segundo = new CultivadorDeLaEspada(2, "Segundo");
		TecnicaOfensiva tecnica = new TecnicaOfensiva("Compartida", 0, 1);
		primero.aprenderTecnica(tecnica);
		segundo.aprenderTecnica(tecnica);
		Pareja pareja = new Pareja(1, primero, segundo);

		primero.ejecutarTecnica(tecnica, segundo);
		pareja.registrarUso(primero, tecnica);
		primero.recibirDanio(100);
		assertTrue(pareja.puedeUsarTecnica(segundo, tecnica));
		pareja.restaurar();
		assertTrue(pareja.puedeUsarTecnica(segundo, tecnica));
	}

	@Test
	void igualdadDeParejasDependeSoloDelIdentificador() {
		Pareja una = new Pareja(88, new CultivadorDeLaEspada(81, "A"), new CultivadorDeLaEspada(82, "B"));
		Pareja otra = new Pareja(88, new CultivadorDeLaEspada(83, "C"), new CultivadorDeLaEspada(84, "D"));
		assertEquals(una, otra);
		assertEquals(una.hashCode(), otra.hashCode());
	}

	@Test
	void noNotificaComoCaidoAUnCombatienteQueSigueEnPie() {
		CultivadorDeLaEspada uno = new CultivadorDeLaEspada(85, "Uno");
		CultivadorDeLaEspada dos = new CultivadorDeLaEspada(86, "Dos");
		Pareja pareja = new Pareja(89, uno, dos);
		assertThrows(IllegalStateException.class, () -> pareja.notificarCaida(uno));
	}

	@Test
	void rechazaIdsInvalidosCombatientesDuplicadosYUsosDeNoIntegrantes() {
		CultivadorDeLaEspada primero = new CultivadorDeLaEspada(1, "Primero");
		CultivadorDeLaEspada segundo = new CultivadorDeLaEspada(2, "Segundo");
		CultivadorDeLaEspada tercero = new CultivadorDeLaEspada(3, "Tercero");
		assertThrows(IllegalArgumentException.class, () -> new Pareja(0, primero, segundo));
		assertThrows(IllegalArgumentException.class, () -> new Pareja(1, primero, primero));
		Pareja pareja = new Pareja(1, primero, segundo);
		assertFalse(pareja.puedeUsarTecnica(tercero, new TecnicaOfensiva("T", 0, 1)));
		assertThrows(IllegalArgumentException.class,
				() -> pareja.registrarUso(tercero, new TecnicaOfensiva("T", 0, 1)));
	}
}