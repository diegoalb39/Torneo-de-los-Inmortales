package torneo;

import combatiente.Combatiente;
import tecnicas.Tecnica;

public class Combate {

    public static final int LIMITE_ASALTOS_POR_DEFECTO = 20;
    public static final int ENERGIA_RECUPERADA_POR_ASALTO = 5;

    private final Pareja parejaMenorId;
    private final Pareja parejaMayorId;
    private final int limiteAsaltos;
    private Pareja ganadora;

    public Combate(Pareja p1, Pareja p2) {
        this(p1, p2, LIMITE_ASALTOS_POR_DEFECTO);
    }

    public Combate(Pareja p1, Pareja p2, int limiteAsaltos) {
        if (p1 == null || p2 == null || p1.equals(p2)) {
            throw new IllegalArgumentException("Parejas inválidas para el combate");
        }
        if (limiteAsaltos <= 0) {
            throw new IllegalArgumentException("El límite de asaltos debe ser positivo");
        }
        // Empieza la pareja de menor identificador
        if (p1.getId() < p2.getId()) {
            this.parejaMenorId = p1;
            this.parejaMayorId = p2;
        } else {
            this.parejaMenorId = p2;
            this.parejaMayorId = p1;
        }
        this.limiteAsaltos = limiteAsaltos;
    }

    public Pareja disputar() {
        parejaMenorId.prepararParaCombate();
        parejaMayorId.prepararParaCombate();
    
        int asaltoActual = 1;

        while (asaltoActual <= limiteAsaltos && ambasParejasEnPie()) {
            // Al comenzar cada asalto, cada combatiente en pie recupera energía fija
            parejaMenorId.recuperarEnergiaAsalto(ENERGIA_RECUPERADA_POR_ASALTO);
            parejaMayorId.recuperarEnergiaAsalto(ENERGIA_RECUPERADA_POR_ASALTO);

            // Turno de la pareja de menor ID
            jugarTurnoPareja(parejaMenorId, parejaMayorId);

            // Si la otra pareja sigue en pie, juega su turno
            if (ambasParejasEnPie()) {
                jugarTurnoPareja(parejaMayorId, parejaMenorId);
            }

            asaltoActual++;
        }

        this.ganadora = determinarGanadora();
        return this.ganadora;
    }

    private void jugarTurnoPareja(Pareja activa, Pareja rival) {
        for (Combatiente atacante : activa.getIntegrantesOrdenados()) {
            if (!ambasParejasEnPie()) {
                break; // Si en cualquier momento una pareja queda sin integrantes en pie, termina
            }
            if (atacante.estaEnPie()) {
                ejecutarAccionCombatiente(atacante, activa, rival);
            }
        }
    }

    private void ejecutarAccionCombatiente(Combatiente ejecutor, Pareja suPareja, Pareja parejaRival) {
        Tecnica elegida = ejecutor.elegirTecnicaParaTurno(
            t -> suPareja.puedeUsarSegunReglaCompaniero(ejecutor, t)
        );

        if (elegida != null) {
            Combatiente objetivo = elegida.esOfensiva()
                    ? parejaRival.elegirObjetivoRivalEnPie()
                    : ejecutor; // Defensivas y recuperación se aplican al ejecutor

            boolean rivalEstabaEnPie = objetivo.estaEnPie();
            ejecutor.ejecutarTecnica(elegida, objetivo);
            suPareja.registrarUsoTecnicaEnCombate(ejecutor, elegida);

            // TODO: Notificar acontecimiento de Acción realizada (Técnica)
            if (rivalEstabaEnPie && !objetivo.estaEnPie()) {
           
            }
        } else {
            Combatiente objetivoRival = parejaRival.elegirObjetivoRivalEnPie();
            boolean rivalEstabaEnPie = objetivoRival.estaEnPie();
            ejecutor.realizarAtaqueBasico(objetivoRival);

        
            if (rivalEstabaEnPie && !objetivoRival.estaEnPie()) {
           
            }
        }
    }

    private boolean ambasParejasEnPie() {
        return parejaMenorId.tieneIntegrantesEnPie() && parejaMayorId.tieneIntegrantesEnPie();
    }

    private Pareja determinarGanadora() {
        if (!parejaMayorId.tieneIntegrantesEnPie()) return parejaMenorId;
        if (!parejaMenorId.tieneIntegrantesEnPie()) return parejaMayorId;

        // Al alcanzar límite de asaltos: gana la pareja con más vida total; en empate, menor ID
        int vidaMenorId = parejaMenorId.getVidaTotal();
        int vidaMayorId = parejaMayorId.getVidaTotal();

        return (vidaMenorId >= vidaMayorId) ? parejaMenorId : parejaMayorId;
    }

    public Pareja getGanadora() {
        return ganadora;
    }
}