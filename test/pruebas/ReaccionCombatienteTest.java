
package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import combatiente.CultivadorDeLaEspada;
import combatiente.HechiceroDelVacio;
import combatiente.MonjeDelCuerpoCelestial;
import pareja.Pareja;

class ReaccionCombatienteTest {

    @Test
    void cultivadorPotenciaUnSoloAtaqueTrasLaCaidaDelCompanero() {
        CultivadorDeLaEspada cultivador = new CultivadorDeLaEspada(1, "Aurelio");
        MonjeDelCuerpoCelestial companero = new MonjeDelCuerpoCelestial(2, "Monje");
        Pareja pareja = new Pareja(1, cultivador, companero);

        // El compañero cae y activa la venganza del cultivador.
        companero.recibirDanio(200);
        assertSame(cultivador, pareja.notificarCaida(companero));

        // El primer ataque recibe +5 propios y +20 de venganza.
        assertEquals(35, cultivador.calcularDanioSaliente(10));

        // El segundo ataque conserva solamente el bono propio.
        assertEquals(15, cultivador.calcularDanioSaliente(10));
    }

    @Test
    void cultivadorPierdeLaVenganzaAlRestaurarse() {
        CultivadorDeLaEspada cultivador = new CultivadorDeLaEspada(1, "Aurelio");
        MonjeDelCuerpoCelestial companero = new MonjeDelCuerpoCelestial(2, "Monje");
        Pareja pareja = new Pareja(1, cultivador, companero);

        // Activa la venganza sin utilizar el ataque potenciado.
        companero.recibirDanio(200);
        pareja.notificarCaida(companero);

        // Al restaurarse debe perder cualquier bono pendiente.
        cultivador.restaurar();

        assertEquals(15, cultivador.calcularDanioSaliente(10));
    }

    @Test
    void monjeReduceUnSoloGolpeTrasLaCaidaDelCompanero() {
        MonjeDelCuerpoCelestial monje = new MonjeDelCuerpoCelestial(1, "Kael");
        CultivadorDeLaEspada companero = new CultivadorDeLaEspada(2, "Aurelio");
        Pareja pareja = new Pareja(1, monje, companero);

        // La caída del compañero activa el escudo adicional.
        companero.recibirDanio(200);
        assertSame(monje, pareja.notificarCaida(companero));

        // El primer golpe de 20 se reduce a 13 y el escudo absorbe todo.
        monje.recibirDanio(20);
        assertEquals(100, monje.getVidaActual());

        // El siguiente golpe ya no cuenta con el escudo adicional.
        monje.recibirDanio(20);
        assertEquals(87, monje.getVidaActual());
    }

    @Test
    void monjePierdeElEscudoAlRestaurarse() {
        MonjeDelCuerpoCelestial monje = new MonjeDelCuerpoCelestial(1, "Kael");
        CultivadorDeLaEspada companero = new CultivadorDeLaEspada(2, "Aurelio");
        Pareja pareja = new Pareja(1, monje, companero);

        // Activa el escudo y comienza un nuevo combate.
        companero.recibirDanio(200);
        pareja.notificarCaida(companero);
        monje.restaurar();

        // Sin escudo pendiente recibe el daño reducido habitual.
        monje.recibirDanio(20);

        assertEquals(87, monje.getVidaActual());
    }

    @Test
    void hechiceroRecuperaEnergiaCuandoCaeSuCompanero() {
        HechiceroDelVacio hechicero = new HechiceroDelVacio(1, "Nebula");
        CultivadorDeLaEspada companero = new CultivadorDeLaEspada(2, "Aurelio");
        Pareja pareja = new Pareja(1, hechicero, companero);

        // El hechicero consume 30 puntos de energía.
        hechicero.perderEnergia(30);
        assertEquals(10, hechicero.getEnergiaActual());

        // La caída del compañero recupera 15 puntos.
        companero.recibirDanio(200);
        assertSame(hechicero, pareja.notificarCaida(companero));

        assertEquals(25, hechicero.getEnergiaActual());
    }

    @Test
    void hechiceroNoSuperaSuEnergiaMaximaConLaReaccion() {
        HechiceroDelVacio hechicero = new HechiceroDelVacio(1, "Nebula");
        CultivadorDeLaEspada companero = new CultivadorDeLaEspada(2, "Aurelio");
        Pareja pareja = new Pareja(1, hechicero, companero);

        // Solo consume 5 puntos antes de activar la reacción.
        hechicero.perderEnergia(5);

        companero.recibirDanio(200);
        pareja.notificarCaida(companero);

        // Recupera energía sin superar el máximo permitido.
        assertEquals(hechicero.getEnergiaMaxima(), hechicero.getEnergiaActual());
    }

    @Test
    void combatienteNoReaccionaSiSuCompaneroSigueEnPie() {
        CultivadorDeLaEspada cultivador = new CultivadorDeLaEspada(1, "Aurelio");
        MonjeDelCuerpoCelestial companero = new MonjeDelCuerpoCelestial(2, "Kael");
        Pareja pareja = new Pareja(1, cultivador, companero);

        // El compañero recibe daño, pero continúa con vida.
        companero.recibirDanio(10);

        // No se permite notificar una caída inexistente.
        assertThrows(IllegalStateException.class,
                () -> pareja.notificarCaida(companero));

        // El cultivador conserva únicamente su daño habitual.
        assertEquals(15, cultivador.calcularDanioSaliente(10));
    }

    @Test
    void combatienteCaidoNoPuedeActivarSuReaccion() {
        CultivadorDeLaEspada cultivador = new CultivadorDeLaEspada(1, "Aurelio");
        MonjeDelCuerpoCelestial companero = new MonjeDelCuerpoCelestial(2, "Kael");
        Pareja pareja = new Pareja(1, cultivador, companero);

        // Ambos combatientes quedan fuera de combate.
        cultivador.recibirDanio(200);
        companero.recibirDanio(200);

        // El cultivador caído no puede activar su venganza.
        assertNull(pareja.notificarCaida(companero));
        assertEquals(15, cultivador.calcularDanioSaliente(10));
    }
}

