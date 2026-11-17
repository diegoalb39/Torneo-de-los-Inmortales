package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import combatiente.CultivadorDeLaEspada;
import combatiente.MonjeDelCuerpoCelestial;
import pareja.Pareja;
import presentadores.Acontecimiento;
import presentadores.CronistaDeLaSecta;
import presentadores.TipoAcontecimiento;
import tecnicas.TecnicaOfensiva;
import torneo.Torneo;

class TorneoTest {

	@Test
	void rechazaCantidadInvalidaInscripcionesDuplicadasYParejasConMismoId() {
		Torneo torneo = new Torneo(1);
		Pareja p1 = pareja(1, 1, 2);
		torneo.inscribir(p1);
		assertThrows(IllegalArgumentException.class, () -> torneo.inscribir(p1));
		assertThrows(IllegalArgumentException.class, () -> torneo.inscribir(pareja(1, 3, 4)));
		assertThrows(IllegalArgumentException.class, () -> torneo.inscribir(pareja(2, 2, 5)));
		assertThrows(IllegalStateException.class, torneo::iniciarTorneo);
	}

	@Test
	void desempataAlLimiteDeAsaltosPorVidaTotalYLuegoPorId() {
		Torneo torneo = new Torneo(1);
		Pareja primera = parejaMonjes(1, 1, 2);
		Pareja segunda = parejaMonjes(2, 3, 4);
		torneo.inscribir(primera);
		torneo.inscribir(segunda);
		torneo.inscribir(pareja(3, 5, 6));
		torneo.inscribir(pareja(4, 7, 8));
		List<Acontecimiento> eventos = new ArrayList<>();
		torneo.registrarInteresado(eventos::add);

		torneo.iniciarTorneo();

		Acontecimiento primerResultado = eventos.stream()
				.filter(e -> e.getTipo() == TipoAcontecimiento.RESULTADO_COMBATE && e.getRonda() == 1).findFirst()
				.orElseThrow();
		assertEquals(primera, primerResultado.getParejaGanadora());
		assertEquals(segunda, primerResultado.getParejaPerdedora());
		assertEquals(primerResultado.getVidaTotalGanadora(), primerResultado.getVidaTotalPerdedora());
	}

	@Test
	void elCaidoNoActuaYNoVuelveASerElegidoComoObjetivo() {
		Torneo torneo = new Torneo(1);
		CultivadorDeLaEspada ejecutor = new CultivadorDeLaEspada(1, "Ejecutor");
		CultivadorDeLaEspada companero = new CultivadorDeLaEspada(2, "Compañero");
		CultivadorDeLaEspada caido = new CultivadorDeLaEspada(3, "Rival caído");
		CultivadorDeLaEspada sobreviviente = new CultivadorDeLaEspada(4, "Rival vivo");
		TecnicaOfensiva letal = new TecnicaOfensiva("Golpe letal", 0, 200);
		ejecutor.aprenderTecnica(letal);
		companero.aprenderTecnica(letal);
		caido.recibirDanio(99);

		torneo.inscribir(new Pareja(1, ejecutor, companero));
		torneo.inscribir(new Pareja(2, caido, sobreviviente));
		torneo.inscribir(pareja(3, 5, 6));
		torneo.inscribir(pareja(4, 7, 8));
		List<Acontecimiento> eventos = new ArrayList<>();
		torneo.registrarInteresado(eventos::add);

		torneo.iniciarTorneo();

		assertTrue(eventos.stream().noneMatch(e -> e.getTipo() == TipoAcontecimiento.ACCION_REALIZADA
				&& e.getDetalle().contains("Rival caído ejecuta")));
		assertTrue(eventos.stream().anyMatch(e -> e.getTipo() == TipoAcontecimiento.ACCION_REALIZADA
				&& e.getDetalle().contains("Compañero ejecuta Ataque Básico sobre Rival vivo")));
		assertThrows(IllegalArgumentException.class, () -> sobreviviente.atacar(caido));
	}

	@Test
	void elObjetivoSeEligePorMenorVidaYEnEmpatePorMenorId() {
		Torneo torneo = new Torneo(1);
		CultivadorDeLaEspada actor = new CultivadorDeLaEspada(1, "Actor");
		CultivadorDeLaEspada aliado = new CultivadorDeLaEspada(2, "Aliado");
		CultivadorDeLaEspada objetivoMenorId = new CultivadorDeLaEspada(3, "Objetivo menor ID");
		CultivadorDeLaEspada objetivoMayorId = new CultivadorDeLaEspada(4, "Objetivo mayor ID");
		actor.aprenderTecnica(new TecnicaOfensiva("Prueba de objetivo", 0, 1));
		torneo.inscribir(new Pareja(1, actor, aliado));
		torneo.inscribir(new Pareja(2, objetivoMenorId, objetivoMayorId));
		torneo.inscribir(pareja(3, 5, 6));
		torneo.inscribir(pareja(4, 7, 8));
		List<Acontecimiento> eventos = new ArrayList<>();
		torneo.registrarInteresado(eventos::add);

		torneo.iniciarTorneo();

		Acontecimiento primeraAccion = eventos.stream()
				.filter(e -> e.getTipo() == TipoAcontecimiento.ACCION_REALIZADA && e.getRonda() == 1).findFirst()
				.orElseThrow();
		assertTrue(primeraAccion.getDetalle().contains("Objetivo menor ID"));
	}

	@Test
	void recuperaEnergiaAlInicioDeCadaAsalto() {
		Torneo torneo = new Torneo(2);
		CultivadorDeLaEspada actor = new CultivadorDeLaEspada(1, "Actor");
		TecnicaOfensiva costosa = new TecnicaOfensiva("Costosa", 40, 1);
		actor.aprenderTecnica(costosa);
		torneo.inscribir(new Pareja(1, actor, new CultivadorDeLaEspada(2, "Aliado")));
		torneo.inscribir(pareja(2, 3, 4));
		torneo.inscribir(pareja(3, 5, 6));
		torneo.inscribir(pareja(4, 7, 8));

		torneo.iniciarTorneo();

		assertEquals(5, actor.getEnergiaActual());
		assertEquals(4, actor.getUsosRestantes(costosa));
	}

	@Test
	void alComenzarElSiguienteCombateRecuperaVidaYEnergiaPeroConservaUsos() {
		Torneo torneo = new Torneo(1);
		CultivadorDeLaEspada vencedor = new CultivadorDeLaEspada(1, "Vencedor");
		TecnicaOfensiva fuerte = new TecnicaOfensiva("Fuerte", 0, 200);
		vencedor.aprenderTecnica(fuerte);
		Pareja primera = new Pareja(1, vencedor, new CultivadorDeLaEspada(2, "Aliado"));
		torneo.inscribir(primera);
		torneo.inscribir(pareja(2, 3, 4));
		torneo.inscribir(pareja(3, 5, 6));
		torneo.inscribir(pareja(4, 7, 8));
		List<String> estadoAlInicioDeFinal = new ArrayList<>();
		torneo.registrarInteresado(evento -> {
			if (evento.getTipo() == TipoAcontecimiento.INICIO_COMBATE && evento.getRonda() == 2
					&& (evento.getDetalle().contains("Pareja #1"))) {
				estadoAlInicioDeFinal.add(vencedor.getVidaActual() + ":" + vencedor.getEnergiaActual() + ":"
						+ vencedor.getUsosRestantes(fuerte));
			}
		});

		torneo.iniciarTorneo();

		assertEquals(List.of("100:40:4"), estadoAlInicioDeFinal);
	}

	@Test
	void torneoEsDeterministaYCronistaOrdenaPorRondaVidaEIdentificador() {
		Resultado primero = ejecutarTorneoDeterminista();
		Resultado segundo = ejecutarTorneoDeterminista();

		assertEquals(primero.campeon.getId(), segundo.campeon.getId());
		assertEquals(primero.historial, segundo.historial);
		assertEquals(2, primero.tabla.get(0).getUltimaRondaJugada());
		assertTrue(
				primero.tabla.get(0).getVidaTotalUltimoCombate() >= primero.tabla.get(1).getVidaTotalUltimoCombate());
		assertTrue(primero.tabla.get(2).getUltimaRondaJugada() == 1);
	}

	@Test
	void rechazaUnaNovenaPareja() {
		Torneo torneo = new Torneo(1);
		for (int i = 0; i < 8; i++)
			torneo.inscribir(pareja(i + 1, i * 2 + 1, i * 2 + 2));
		assertThrows(IllegalStateException.class, () -> torneo.inscribir(pareja(9, 17, 18)));
	}

	@Test
	void crucesConservanOrdenDeInscripcionPeroEmpiezaLaParejaDeMenorId() {
		Torneo torneo = new Torneo(1);
		torneo.inscribir(pareja(30, 1, 2));
		torneo.inscribir(pareja(10, 3, 4));
		torneo.inscribir(pareja(40, 5, 6));
		torneo.inscribir(pareja(20, 7, 8));
		List<Acontecimiento> eventos = new ArrayList<>();
		torneo.registrarInteresado(eventos::add);

		torneo.iniciarTorneo();

		List<String> inicios = eventos.stream()
				.filter(e -> e.getTipo() == TipoAcontecimiento.INICIO_COMBATE && e.getRonda() == 1)
				.map(Acontecimiento::getDetalle).toList();
		assertTrue(inicios.get(0).indexOf("Pareja #10") < inicios.get(0).indexOf("Pareja #30"));
		assertTrue(inicios.get(1).indexOf("Pareja #20") < inicios.get(1).indexOf("Pareja #40"));
	}

	@Test
	void admiteOchoParejasYRespetaLasTresRondas() {
		Torneo torneo = new Torneo(1);
		List<Acontecimiento> eventos = new ArrayList<>();
		torneo.registrarInteresado(eventos::add);
		for (int i = 0; i < 8; i++) {
			torneo.inscribir(pareja(i + 1, i * 2 + 1, i * 2 + 2));
		}

		torneo.iniciarTorneo();

		long resultados = eventos.stream().filter(e -> e.getTipo() == TipoAcontecimiento.RESULTADO_COMBATE).count();
		assertEquals(7, (int) resultados);
		assertTrue(eventos.stream()
				.anyMatch(e -> e.getTipo() == TipoAcontecimiento.RESULTADO_COMBATE && e.getRonda() == 3));
	}

	@Test
	void torneoNoPuedeIniciarseDosVecesNiAceptarInscripcionesLuegoDeComenzar() {
		Torneo torneo = new Torneo(1);
		torneo.inscribir(pareja(1, 1, 2));
		torneo.inscribir(pareja(2, 3, 4));
		torneo.inscribir(pareja(3, 5, 6));
		torneo.inscribir(pareja(4, 7, 8));
		torneo.iniciarTorneo();

		assertThrows(IllegalStateException.class, torneo::iniciarTorneo);
		assertThrows(IllegalStateException.class, () -> torneo.inscribir(pareja(5, 9, 10)));
	}

	private Resultado ejecutarTorneoDeterminista() {
		Torneo torneo = new Torneo(1);
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		torneo.registrarInteresado(cronista);
		torneo.inscribir(pareja(1, 1, 2));
		torneo.inscribir(pareja(2, 3, 4));
		torneo.inscribir(pareja(3, 5, 6));
		torneo.inscribir(pareja(4, 7, 8));
		Pareja campeon = torneo.iniciarTorneo();
		List<String> historial = cronista.getHistorial().stream()
				.map(e -> e.getTipo() + "|" + e.getRonda() + "|" + e.getDetalle()).toList();
		return new Resultado(campeon, historial, cronista.obtenerTablaPosiciones());
	}

	private Pareja pareja(int idPareja, int id1, int id2) {
		return new Pareja(idPareja, new CultivadorDeLaEspada(id1, "C" + id1), new CultivadorDeLaEspada(id2, "C" + id2));
	}

	private Pareja parejaMonjes(int idPareja, int id1, int id2) {
		return new Pareja(idPareja, new MonjeDelCuerpoCelestial(id1, "M" + id1),
				new MonjeDelCuerpoCelestial(id2, "M" + id2));
	}

	private static class Resultado {
		private final Pareja campeon;
		private final List<String> historial;
		private final List<presentadores.PosicionPareja> tabla;

		private Resultado(Pareja campeon, List<String> historial, List<presentadores.PosicionPareja> tabla) {
			this.campeon = campeon;
			this.historial = historial;
			this.tabla = tabla;
		}
	}
}