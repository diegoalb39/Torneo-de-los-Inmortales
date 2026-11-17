package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import combatiente.Combatiente;
import escuela.Escuela;
import fabrica.FabricaCombatientes;
import fabrica.TipoCombatiente;

class FabricaCombatienteTest {

    @Test
    void distintasFabricasNoGeneranIdentificadoresRepetidos() {
        Escuela escuela = new Escuela("Escuela");
        Combatiente uno = new FabricaCombatientes().crearCombatiente(
                TipoCombatiente.CULTIVADOR_ESPADA, "Uno", escuela);
        Combatiente dos = new FabricaCombatientes().crearCombatiente(
                TipoCombatiente.HECHICERO_VACIO, "Dos", escuela);

        assertTrue(dos.getId() > uno.getId());
        assertNotEquals(uno, dos);
    }

    @Test
    void nombresIgualesNoHacenIgualesADosCombatientesConIdsDistintos() {
        Escuela escuela = new Escuela("Escuela");
        FabricaCombatientes fabrica = new FabricaCombatientes();
        Combatiente uno = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Mismo nombre", escuela);
        Combatiente dos = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Mismo nombre", escuela);

        assertNotEquals(uno, dos);
        assertEquals(uno.getNombre(), dos.getNombre());
    }
}
