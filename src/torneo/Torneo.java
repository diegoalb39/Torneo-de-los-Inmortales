package torneo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import combatiente.Combatiente;
import pareja.Pareja;
import tecnicas.Tecnica;
import tecnicas.TipoTecnica;

public class Torneo {
	private Set<Pareja> parejas = new TreeSet<>();
	public static final int CANTIDAD_PAREJAS_MAX = 4;
	public static final int LIMITE_ASALTOS = 3;
	public static final int ENERGIA_RECUPERADA_POR_ASALTO = 5;

	public void inscribir(Pareja pareja) {
		if (!validarPareja(pareja))
			throw new IllegalArgumentException("Uno/ambos combatientes ya tiene/n pareja");
		if (parejas.size() < CANTIDAD_PAREJAS_MAX)
			parejas.add(pareja);
		else
			throw new IllegalStateException("MAXIMA CANTIDAD DE PAREJAS ALCANZADA");// pal heraldo movelo, Chapuzero
	}

	private Boolean validarPareja(Pareja pareja) {
		for (Pareja p : parejas) {
			if ((p.contieneA(pareja.getC1()) || p.contieneA(pareja.getC2())) || p.equals(pareja)) {
				return false;
			}
		}
		return true;
	}

	public void iniciarTorneo() {
		if (parejas.size() != CANTIDAD_PAREJAS_MAX) {
			throw new IllegalStateException(
					"El torneo requiere exactamente " + CANTIDAD_PAREJAS_MAX + " parejas. Hay " + parejas.size());
		}
		emparejarEnCombates();
	}

	//porque el set no tiene indices. los obtengo mas rapido en una lista
	public void emparejarEnCombates() {
		List<Pareja> lista = new ArrayList<>(parejas);
		for (int i = 0; i + 1 < lista.size(); i += 2) {
			combatir(lista.get(i), lista.get(i + 1));
		}
	}

	private void combatir(Pareja parejaA, Pareja parejaB) {
		//por las dudas restauro la vida, energia al default
		parejaA.restaurar();
		parejaB.restaurar();
		//elijo la pareja que juega primero
		Pareja primera = parejaA.getId() <= parejaB.getId() ? parejaA : parejaB;
		Pareja segunda = (primera == parejaA) ? parejaB : parejaA;

		int asalto = 0;
		while (parejaEnPie(primera) && parejaEnPie(segunda) && asalto < LIMITE_ASALTOS) {
			asalto++;
			System.out.println();
			System.out.println("--- Asalto " + asalto + " ---");

			recuperarEnergia(primera);
			recuperarEnergia(segunda);

			jugarTurno(primera, segunda);
			if (!parejaEnPie(segunda))
				break;

			jugarTurno(segunda, primera);
		}
		Pareja ganadora = determinarGanadora(primera, segunda);
		System.out.println();
		System.out.println(">>> Gana la pareja " + ganadora.getId() + " <<<");
		System.out.println();
	}

	private void recuperarEnergia(Pareja p) {
		if (p.getC1().estaEnPie())
			p.getC1().recuperarEnergia(ENERGIA_RECUPERADA_POR_ASALTO);
		if (p.getC2().estaEnPie())
			p.getC2().recuperarEnergia(ENERGIA_RECUPERADA_POR_ASALTO);
	}

	private void jugarTurno(Pareja atacante, Pareja defensor) {
		System.out.println("  Turno de la Pareja " + atacante.getId() + ":");
		//elijo al atacante
		Combatiente a1 = atacante.getC1().getId() <= atacante.getC2().getId() ? atacante.getC1() : atacante.getC2();
		Combatiente a2 = (a1 == atacante.getC1()) ? atacante.getC2() : atacante.getC1();

		actuar(a1, atacante, defensor);
		actuar(a2, atacante, defensor);
	}

	private void actuar(Combatiente actor, Pareja propia, Pareja rival) {
		if (!actor.estaEnPie())
			return;

		// El objetivo se calcula en cada acción: la vida cambia entre ataques
		Combatiente objetivo = elegirObjetivo(rival);
		if (objetivo == null)
			return;

		Tecnica tecnica = elegirTecnica(actor, propia);
		//ataque basico
		if (tecnica == null) {
			actor.atacar(objetivo);
			System.out.println("    " + actor.getNombre() + " ataca a " + objetivo.getNombre()
					+ " (vida: " + objetivo.getVidaActual() + ")");
			return;
		}

		// Ofensivas al rival; defensivas y de recuperación al ejecutor
		boolean ofensiva = tecnica.getTipo() == TipoTecnica.OFENSIVA;
		Combatiente destino = ofensiva ? objetivo : actor;

		actor.ejecutarTecnica(tecnica, destino);
		propia.registrarUso(actor, tecnica);
		System.out.println("    " + actor.getNombre() + " usa " + tecnica.getNombre() + " sobre "
				+ destino.getNombre() + " (vida: " + destino.getVidaActual() + ")");
	}
	// Rival en pie con menos vida actual; empate → menor id
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
	// Criterio por defecto: recuperación si le falta vida 
	// >defensiva si no tiene una activa
	// >ofensiva con más usos restantes. 
	// En empate, la primera en el orden de aprendizaje. Devuelve null si ninguna es posible (→ ataque básico).
	private Tecnica elegirTecnica(Combatiente actor, Pareja propia) {
		if (actor.getVidaActual() <= actor.getVidaMaxima()*0.15) {//solo se cura cuando tenga poca vida
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
			if (t.getTipo() != TipoTecnica.OFENSIVA || !esUsable(actor, propia, t))
				continue;
			// ">" estricto: en empate se queda la que menos usaste
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

		int vidaPrimera = primera.getC1().getVidaActual() + primera.getC2().getVidaActual();
		int vidaSegunda = segunda.getC1().getVidaActual() + segunda.getC2().getVidaActual();
		System.out.println("    [Límite de asaltos alcanzado] Vida total: Pareja "
				+ primera.getId() + " = " + vidaPrimera
				+ ", Pareja " + segunda.getId() + " = " + vidaSegunda);
		if (vidaPrimera != vidaSegunda) {
			return vidaPrimera > vidaSegunda ? primera : segunda;
		}
		return primera; // primera es la de menor id
	}

	private boolean parejaEnPie(Pareja p) {
		return p.getC1().estaEnPie() || p.getC2().estaEnPie();
	}


}