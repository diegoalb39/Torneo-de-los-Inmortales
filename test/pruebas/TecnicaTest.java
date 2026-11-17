package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import combatiente.CultivadorDeLaEspada;
import tecnicas.TecnicaDefensiva;
import tecnicas.TecnicaOfensiva;
import tecnicas.TecnicaRecuperacion;

class TecnicaTest {

	@Test
	void tecnicaDefensivaPermaneceActivaHastaQueSeRestauraElCombatiente() {
		CultivadorDeLaEspada actor = new CultivadorDeLaEspada(1, "Actor");
		CultivadorDeLaEspada rival = new CultivadorDeLaEspada(2, "Rival");
		TecnicaDefensiva muro = new TecnicaDefensiva("Muro", 0, 10);
		actor.aprenderTecnica(muro);
		actor.ejecutarTecnica(muro, actor);

		rival.atacar(actor);
		assertEquals(95, actor.getVidaActual());
		actor.restaurar();
		rival.atacar(actor);
		assertEquals(85, actor.getVidaActual());
	}

	@Test
	void tecnicaDeRecuperacionNoSuperaLaVidaMaxima() {
		CultivadorDeLaEspada actor = new CultivadorDeLaEspada(1, "Actor");
		actor.recibirDanio(20);
		TecnicaRecuperacion meditacion = new TecnicaRecuperacion("Meditación", 0, 50);
		actor.aprenderTecnica(meditacion);
		actor.ejecutarTecnica(meditacion, actor);
		assertEquals(100, actor.getVidaActual());
	}

	@Test
	void constructoresRechazanEfectosNegativos() {
		assertThrows(IllegalArgumentException.class, () -> new TecnicaOfensiva("X", 0, -1));
		assertThrows(IllegalArgumentException.class, () -> new TecnicaDefensiva("X", 0, -1));
		assertThrows(IllegalArgumentException.class, () -> new TecnicaRecuperacion("X", 0, -1));
	}
}
