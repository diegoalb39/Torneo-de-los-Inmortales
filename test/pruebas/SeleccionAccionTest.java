package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import combatiente.CultivadorDeLaEspada;
import pareja.Pareja;
import presentadores.Acontecimiento;
import presentadores.TipoAcontecimiento;
import tecnicas.TecnicaDefensiva;
import tecnicas.TecnicaOfensiva;
import tecnicas.TecnicaRecuperacion;
import torneo.Torneo;

class SeleccionAccionTest {

    @Test
    void priorizaRecuperacionCuandoLeFaltaVida() {
        CultivadorDeLaEspada actor = new CultivadorDeLaEspada(3, "Actor");
        actor.aprenderTecnica(new TecnicaOfensiva("Ataque", 0, 50));
        actor.aprenderTecnica(new TecnicaRecuperacion("Meditación", 0, 30));
        Acontecimiento primeraAccion = ejecutarYObtenerPrimeraAccion(actor);

        assertTrue(primeraAccion.getDetalle().contains("Meditación"));
        assertTrue(primeraAccion.getDetalle().contains("Vida actual: 100"));
    }

    @Test
    void priorizaDefensaAntesQueAtaqueSiNoTieneUnaActiva() {
        CultivadorDeLaEspada actor = new CultivadorDeLaEspada(3, "Actor");
        actor.aprenderTecnica(new TecnicaOfensiva("Ataque", 0, 50));
        actor.aprenderTecnica(new TecnicaDefensiva("Muro", 0, 10));
        Acontecimiento primeraAccion = ejecutarYObtenerPrimeraAccion(actor);

        assertTrue(primeraAccion.getDetalle().contains("Muro"));
        assertTrue(actor.tieneDefensaActiva());
    }

    @Test
    void eligeLaOfensivaConMasUsosRestantesYDesempataPorOrdenDeAprendizaje() {
        CultivadorDeLaEspada actor = new CultivadorDeLaEspada(3, "Actor");
        TecnicaOfensiva primera = new TecnicaOfensiva("Primera", 0, 1);
        TecnicaOfensiva segunda = new TecnicaOfensiva("Segunda", 0, 1);
        actor.aprenderTecnica(primera);
        actor.aprenderTecnica(segunda);
        actor.ejecutarTecnica(primera, new CultivadorDeLaEspada(90, "Objetivo de prueba"));
        Acontecimiento primeraAccion = ejecutarYObtenerPrimeraAccion(actor);

        assertTrue(primeraAccion.getDetalle().contains("Segunda"));
    }

    @Test
    void enEmpateDeUsosEligeLaPrimeraTecnicaAprendida() {
        CultivadorDeLaEspada actor = new CultivadorDeLaEspada(3, "Actor");
        actor.aprenderTecnica(new TecnicaOfensiva("Primera", 0, 1));
        actor.aprenderTecnica(new TecnicaOfensiva("Segunda", 0, 1));
        Acontecimiento primeraAccion = ejecutarYObtenerPrimeraAccion(actor);

        assertTrue(primeraAccion.getDetalle().contains("Primera"));
    }

    private Acontecimiento ejecutarYObtenerPrimeraAccion(CultivadorDeLaEspada actor) {
        Torneo torneo = new Torneo(1);
        torneo.inscribir(new Pareja(2, actor, new CultivadorDeLaEspada(4, "Aliado")));
        torneo.inscribir(pareja(1, 1, 2));
        torneo.inscribir(pareja(3, 5, 6));
        torneo.inscribir(pareja(4, 7, 8));
        List<Acontecimiento> eventos = new ArrayList<>();
        torneo.registrarInteresado(eventos::add);
        torneo.iniciarTorneo();
        return eventos.stream()
                .filter(e -> e.getTipo() == TipoAcontecimiento.ACCION_REALIZADA && e.getRonda() == 1
                        && e.getDetalle().contains("Actor ejecuta"))
                .findFirst().orElseThrow();
    }

    private Pareja pareja(int idPareja, int id1, int id2) {
        return new Pareja(idPareja, new CultivadorDeLaEspada(id1, "C" + id1),
                new CultivadorDeLaEspada(id2, "C" + id2));
    }
}

