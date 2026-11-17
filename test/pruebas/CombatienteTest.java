package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import combatiente.Combatiente;
import combatiente.CultivadorDeLaEspada;
import combatiente.HechiceroDelVacio;
import combatiente.MonjeDelCuerpoCelestial;
import equipamiento.AnilloEspiritual;
import equipamiento.ArmaduraDorada;
import equipamiento.EspadaCelestial;
import equipamiento.SelloDelFenix;
import tecnicas.TecnicaOfensiva;

class CombatienteTest {

	@Test
	void mismaTecnicaProduceDanioDistintoSegunElEjecutor() {
		Combatiente espada = new CultivadorDeLaEspada(1, "Espada");
		Combatiente monje = new MonjeDelCuerpoCelestial(2, "Monje");

		assertEquals(35, espada.calcularDanioSaliente(30));
		assertEquals(30, monje.calcularDanioSaliente(30));
	}

	@Test
	void noPermiteEjecutarTecnicaSinEnergia() {
		Combatiente actor = new CultivadorDeLaEspada(1, "Actor");
		Combatiente objetivo = new CultivadorDeLaEspada(2, "Objetivo");
		TecnicaOfensiva tecnica = new TecnicaOfensiva("Costosa", 41, 10);
		actor.aprenderTecnica(tecnica);

		assertFalse(actor.puedeUsar(tecnica));
		assertThrows(IllegalStateException.class, () -> actor.ejecutarTecnica(tecnica, objetivo));
		assertEquals(40, actor.getEnergiaActual());
		assertEquals(100, objetivo.getVidaActual());
	}

	@Test
	void noPermiteEjecutarTecnicaSinUsosYLosUsosPersistenAlRestaurar() {
		Combatiente actor = new CultivadorDeLaEspada(1, "Actor");
		Combatiente objetivo = new CultivadorDeLaEspada(2, "Objetivo");
		TecnicaOfensiva tecnica = new TecnicaOfensiva("Golpe", 0, 1);
		actor.aprenderTecnica(tecnica);

		for (int i = 0; i < 5; i++)
			actor.ejecutarTecnica(tecnica, objetivo);

		assertEquals(0, actor.getUsosRestantes(tecnica));
		assertFalse(actor.puedeUsar(tecnica));
		assertThrows(IllegalStateException.class, () -> actor.ejecutarTecnica(tecnica, objetivo));
		actor.recibirDanio(10);
		actor.perderEnergia(10);
		actor.restaurar();

		assertEquals(100, actor.getVidaActual());
		assertEquals(40, actor.getEnergiaActual());
		assertEquals(0, actor.getUsosRestantes(tecnica));
	}

	@Test
	void equiparNoAumentaValoresActualesYAlQuitarRecortaElMaximo() {
		Combatiente combatiente = new CultivadorDeLaEspada(1, "Portador");
		combatiente.equipar(new SelloDelFenix());

		assertEquals(125, combatiente.getVidaMaxima());
		assertEquals(100, combatiente.getVidaActual());
		combatiente.recuperarVida(25);
		assertEquals(125, combatiente.getVidaActual());
		combatiente.quitarEquipamiento(equipamiento.CategoriaEquipamiento.SELLO);

		assertEquals(100, combatiente.getVidaMaxima());
		assertEquals(100, combatiente.getVidaActual());
	}

	@Test
	void efectosDeEquipamientoSeAcumulanYValoresActualesNoSuperanElMaximo() {
		Combatiente combatiente = new CultivadorDeLaEspada(1, "Portador");
		combatiente.equipar(new EspadaCelestial());
		combatiente.equipar(new ArmaduraDorada());
		combatiente.equipar(new AnilloEspiritual());

		assertEquals(25, combatiente.calcularDanioSaliente(10));
		assertEquals(60, combatiente.getEnergiaMaxima());
		combatiente.recuperarEnergia(1000);
		assertEquals(60, combatiente.getEnergiaActual());
	}

	@Test
	void identidadDependeSoloDelIdYNoDelNombreNiDelTipo() {
		Combatiente uno = new CultivadorDeLaEspada(77, "Nombre A");
		Combatiente otro = new MonjeDelCuerpoCelestial(77, "Nombre B");
		Combatiente distinto = new CultivadorDeLaEspada(78, "Nombre A");

		assertEquals(uno, otro);
		assertEquals(uno.hashCode(), otro.hashCode());
		assertNotEquals(uno, distinto);
	}

	@Test
	void rechazaDatosInvalidosYDanioNegativo() {
		assertThrows(IllegalArgumentException.class, () -> new CultivadorDeLaEspada(0, "Inválido"));
		assertThrows(IllegalArgumentException.class, () -> new CultivadorDeLaEspada(1, " "));
		Combatiente combatiente = new CultivadorDeLaEspada(2, "Válido");
		assertThrows(IllegalArgumentException.class, () -> combatiente.recibirDanio(-1));
		assertThrows(IllegalArgumentException.class, () -> combatiente.calcularDanioSaliente(-1));
	}

	@Test
	void quitarAnilloRecortaEnergiaActualAlNuevoMaximo() {
		Combatiente combatiente = new CultivadorDeLaEspada(1, "Portador");
		combatiente.equipar(new AnilloEspiritual());
		combatiente.recuperarEnergia(100);
		assertEquals(60, combatiente.getEnergiaActual());
		combatiente.quitarEquipamiento(equipamiento.CategoriaEquipamiento.ANILLO);
		assertEquals(40, combatiente.getEnergiaActual());
		assertEquals(40, combatiente.getEnergiaMaxima());
	}

	@Test
	void ataqueBasicoNoConsumeEnergiaNiUsosDeTecnicas() {
		Combatiente actor = new CultivadorDeLaEspada(1, "Actor");
		Combatiente objetivo = new CultivadorDeLaEspada(2, "Objetivo");
		int energiaInicial = actor.getEnergiaActual();
		actor.atacar(objetivo);
		actor.atacar(objetivo);
		assertEquals(energiaInicial, actor.getEnergiaActual());
		assertEquals(70, objetivo.getVidaActual());
	}

	@Test
	void hechiceroReduceCostoDeEnergia() {
		Combatiente hechicero = new HechiceroDelVacio(1, "Hechicero");
		assertEquals(5, hechicero.calcularCostoEnergia(10));
	}
}
