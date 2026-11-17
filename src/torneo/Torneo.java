package torneo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import combatiente.Combatiente;
import pareja.Pareja;
import presentadores.Acontecimiento;
import presentadores.InteresadoAcontecimiento;
import tecnicas.Tecnica;
import tecnicas.TipoTecnica;

public class Torneo {

	// LinkedHashSet evita duplicados y conserva el orden exacto de inscripción
	private final Set<Pareja> parejas = new LinkedHashSet<>();
	private final List<InteresadoAcontecimiento> interesados = new ArrayList<>();

	public static final int CANTIDAD_PAREJAS_MIN = 4;
	public static final int CANTIDAD_PAREJAS_MAX = 8;
	public static final int LIMITE_ASALTOS_POR_DEFECTO = 20;
	public static final int ENERGIA_RECUPERADA_POR_ASALTO = 5;

	private final int limiteAsaltos;
	private boolean iniciado;

	public Torneo() {
		this(LIMITE_ASALTOS_POR_DEFECTO);
	}

	public Torneo(int limiteAsaltos) {
		if (limiteAsaltos <= 0) {
			throw new IllegalArgumentException("El límite de asaltos debe ser mayor a 0");
		}
		this.limiteAsaltos = limiteAsaltos;
	}

	public void registrarInteresado(InteresadoAcontecimiento interesado) {
		if (interesado != null && !interesados.contains(interesado)) {
			interesados.add(interesado);
		}
	}

	private void notificar(Acontecimiento acontecimiento) {
		for (InteresadoAcontecimiento interesado : interesados) {
			interesado.recibirAcontecimiento(acontecimiento);
		}
	}

	public void inscribir(Pareja pareja) {
		if (iniciado) {
			throw new IllegalStateException("No se puede inscribir parejas una vez iniciado el torneo");
		}
		if (pareja == null) {
			throw new IllegalArgumentException("La pareja no puede ser nula");
		}
		if (!validarPareja(pareja)) {
			throw new IllegalArgumentException("Pareja duplicada o uno/ambos combatientes ya están inscriptos");
		}
		if (parejas.size() >= CANTIDAD_PAREJAS_MAX) {
			throw new IllegalStateException("Máxima cantidad de parejas alcanzada (" + CANTIDAD_PAREJAS_MAX + ")");
		}
		parejas.add(pareja);
	}

	private boolean validarPareja(Pareja pareja) {
		for (Pareja p : parejas) {
			if (p.equals(pareja) || p.contieneA(pareja.getC1()) || p.contieneA(pareja.getC2())) {
				return false;
			}
		}
		return true;
	}

	public Pareja iniciarTorneo() {
		if (iniciado) {
			throw new IllegalStateException("El torneo ya fue iniciado");
		}
		int inscriptas = parejas.size();
		if (inscriptas != CANTIDAD_PAREJAS_MIN && inscriptas != CANTIDAD_PAREJAS_MAX) {
			throw new IllegalStateException(
					"El torneo se debe jugar únicamente con 4 u 8 parejas. Inscriptas: " + inscriptas);
		}

		iniciado = true;
		List<Pareja> enCompetencia = new ArrayList<>(parejas);
		int numeroRonda = 1;

		// Se juegan las rondas eliminatorias hasta que queda una sola pareja en pie
		while (enCompetencia.size() > 1) {
			List<Pareja> ganadorasRonda = new ArrayList<>();
			for (int i = 0; i + 1 < enCompetencia.size(); i += 2) {
				Pareja ganadora = combatir(numeroRonda, enCompetencia.get(i), enCompetencia.get(i + 1));
				ganadorasRonda.add(ganadora);
			}
			enCompetencia = ganadorasRonda;
			numeroRonda++;
		}

		Pareja campeona = enCompetencia.get(0);
		notificar(Acontecimiento.proclamacion(numeroRonda - 1, campeona));
		return campeona;
	}

	private Pareja combatir(int ronda, Pareja parejaA, Pareja parejaB) {
		// Al comenzar cada combate, todos recuperan vida y energía al máximo y se
		// limpia la regla del compañero
		parejaA.restaurar();
		parejaB.restaurar();

		// Empieza la pareja de menor identificador
		Pareja primera = parejaA.getId() <= parejaB.getId() ? parejaA : parejaB;
		Pareja segunda = (primera == parejaA) ? parejaB : parejaA;

		notificar(Acontecimiento.inicioCombate(ronda, primera, segunda));

		int asalto = 0;
		while (parejaEnPie(primera) && parejaEnPie(segunda) && asalto < limiteAsaltos) {
			asalto++;

			recuperarEnergia(primera);
			recuperarEnergia(segunda);

			jugarTurno(ronda, primera, segunda);
			if (parejaEnPie(segunda)) {
				jugarTurno(ronda, segunda, primera);
			}

		}

		Pareja ganadora = determinarGanadora(primera, segunda);
		Pareja perdedora = (ganadora == primera) ? segunda : primera;

		notificar(Acontecimiento.resultadoCombate(ronda, ganadora, perdedora, calcularVidaTotalPareja(ganadora),
				calcularVidaTotalPareja(perdedora)));

		return ganadora;
	}

	private void recuperarEnergia(Pareja p) {
		if (p.getC1().estaEnPie()) {
			p.getC1().recuperarEnergia(ENERGIA_RECUPERADA_POR_ASALTO);
		}
		if (p.getC2().estaEnPie()) {
			p.getC2().recuperarEnergia(ENERGIA_RECUPERADA_POR_ASALTO);
		}
	}

	private void jugarTurno(int ronda, Pareja atacante, Pareja defensor) {
		// Dentro de la pareja actúa primero el de menor identificador
		Combatiente a1 = atacante.getC1().getId() <= atacante.getC2().getId() ? atacante.getC1() : atacante.getC2();
		Combatiente a2 = (a1 == atacante.getC1()) ? atacante.getC2() : atacante.getC1();

		actuar(ronda, a1, atacante, defensor);
		// Si en cualquier momento una pareja queda sin integrantes en pie, el combate
		// termina
		if (parejaEnPie(defensor) && parejaEnPie(atacante)) {
			actuar(ronda, a2, atacante, defensor);
		}
	}

	private void actuar(int ronda, Combatiente actor, Pareja propia, Pareja rival) {
		if (!actor.estaEnPie()) {
			return;
		}

		Combatiente objetivo = elegirObjetivo(rival);
		if (objetivo == null) {
			return;
		}

		Tecnica tecnica = elegirTecnica(actor, propia);

		if (tecnica == null) {
			boolean estabaEnPie = objetivo.estaEnPie();
			actor.atacar(objetivo);
			notificar(Acontecimiento.accionRealizada(ronda, actor.getNombre(), "Ataque Básico", objetivo.getNombre(),
					objetivo.getVidaActual()));

			if (estabaEnPie && !objetivo.estaEnPie()) {
				notificar(Acontecimiento.eliminacion(ronda, objetivo.getNombre(), actor.getNombre()));
				notificarReaccionCompanero(ronda, rival, objetivo);
			}
			return;
		}

		boolean ofensiva = tecnica.getTipo() == TipoTecnica.OFENSIVA;
		Combatiente destino = ofensiva ? objetivo : actor;
		boolean estabaEnPie = destino.estaEnPie();

		actor.ejecutarTecnica(tecnica, destino);
		propia.registrarUso(actor, tecnica);

		notificar(Acontecimiento.accionRealizada(ronda, actor.getNombre(), tecnica.getNombre(), destino.getNombre(),
				destino.getVidaActual()));

		if (estabaEnPie && !destino.estaEnPie()) {
			notificar(Acontecimiento.eliminacion(ronda, destino.getNombre(), actor.getNombre()));
			notificarReaccionCompanero(ronda, rival, destino);
		}
	}

	// Informa al compañero sobreviviente y registra si activó una reacción.
	private void notificarReaccionCompanero(int ronda, Pareja parejaDefensora, Combatiente caido) {
		Combatiente reactivo = parejaDefensora.notificarCaida(caido);
		if (reactivo != null) {
			notificar(Acontecimiento.reaccionActivada(ronda, reactivo.getNombre()));
		}
	}

	private Combatiente elegirObjetivo(Pareja rival) {
		Combatiente c1 = rival.getC1();
		Combatiente c2 = rival.getC2();

		if (!c1.estaEnPie() && !c2.estaEnPie())
			return null;
		if (!c1.estaEnPie())
			return c2;
		if (!c2.estaEnPie())
			return c1;

		if (c1.getVidaActual() != c2.getVidaActual()) {
			return c1.getVidaActual() < c2.getVidaActual() ? c1 : c2;
		}
		return c1.getId() <= c2.getId() ? c1 : c2;
	}

	/**
	 * Criterio por defecto (Sección 6): 1. Técnica de recuperación si le falta
	 * vida. 2. Técnica defensiva si no tiene una activa. 3. Técnica ofensiva con
	 * más usos restantes (en empate, primera en el orden de la escuela).
	 */
	private Tecnica elegirTecnica(Combatiente actor, Pareja propia) {
		if (actor.getVidaActual() < actor.getVidaMaxima()) {
			Tecnica t = primeraUsable(actor, propia, TipoTecnica.RECUPERACION);
			if (t != null)
				return t;
		}

		if (!actor.tieneDefensaActiva()) {
			Tecnica t = primeraUsable(actor, propia, TipoTecnica.DEFENSIVA);
			if (t != null)
				return t;
		}

		Tecnica mejor = null;
		for (Tecnica t : actor.getTecnicas()) {
			if (t.getTipo() != TipoTecnica.OFENSIVA || !esUsable(actor, propia, t)) {
				continue;
			}
			if (mejor == null || actor.getUsosRestantes(t) > actor.getUsosRestantes(mejor)) {
				mejor = t;
			}
		}
		return mejor;
	}

	private Tecnica primeraUsable(Combatiente actor, Pareja propia, TipoTecnica tipo) {
		for (Tecnica t : actor.getTecnicas()) {
			if (t.getTipo() == tipo && esUsable(actor, propia, t)) {
				return t;
			}
		}
		return null;
	}

	private boolean esUsable(Combatiente actor, Pareja propia, Tecnica t) {
		return actor.puedeUsar(t) && propia.puedeUsarTecnica(actor, t);
	}

	private Pareja determinarGanadora(Pareja primera, Pareja segunda) {
		if (!parejaEnPie(primera))
			return segunda;
		if (!parejaEnPie(segunda))
			return primera;

		int vidaPrimera = calcularVidaTotalPareja(primera);
		int vidaSegunda = calcularVidaTotalPareja(segunda);

		if (vidaPrimera != vidaSegunda) {
			return vidaPrimera > vidaSegunda ? primera : segunda;
		}
		return primera; // primera ya es la de menor identificador
	}

	private int calcularVidaTotalPareja(Pareja p) {
		int v1 = p.getC1().estaEnPie() ? p.getC1().getVidaActual() : 0;
		int v2 = p.getC2().estaEnPie() ? p.getC2().getVidaActual() : 0;
		return v1 + v2;
	}

	private boolean parejaEnPie(Pareja p) {
		return p.getC1().estaEnPie() || p.getC2().estaEnPie();
	}
}