package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import combatiente.CultivadorDeLaEspada;
import equipamiento.AnilloEspiritual;
import equipamiento.ArmaduraDorada;
import equipamiento.EspadaCelestial;
import equipamiento.EspadaDemoniaca;
import equipamiento.SelloDelFenix;
import escuela.Escuela;

class EquipamientoEscuelaTest {

    @Test
    void rechazaCuartoEquipamientoYCategoriaRepetidaSinModificarElEstado() {
        CultivadorDeLaEspada combatiente = new CultivadorDeLaEspada(1, "Combatiente");
        combatiente.equipar(new EspadaCelestial());
        combatiente.equipar(new ArmaduraDorada());
        combatiente.equipar(new AnilloEspiritual());

        assertThrows(IllegalStateException.class, () -> combatiente.equipar(new SelloDelFenix()));
        assertThrows(IllegalStateException.class, () -> combatiente.equipar(new EspadaDemoniaca()));
        assertEquals(3, combatiente.getEquipamientos().size());
        assertEquals(25, combatiente.calcularDanioSaliente(10));
        assertEquals(60, combatiente.getEnergiaMaxima());
    }

    @Test
    void escuelaOfreceAlternativasYSeleccionaHastaTresCategoriasDistintas() {
        Escuela escuela = new Escuela("Escuela de prueba");
        EspadaCelestial espada = new EspadaCelestial();
        escuela.agregarEquipamiento(espada);
        escuela.agregarEquipamiento(new EspadaDemoniaca());
        escuela.agregarEquipamiento(new ArmaduraDorada());
        escuela.agregarEquipamiento(new AnilloEspiritual());
        escuela.agregarEquipamiento(new SelloDelFenix());

        CultivadorDeLaEspada alumno = new CultivadorDeLaEspada(1, "Alumno");
        escuela.prepararCombatiente(alumno);

        assertEquals(3, alumno.getEquipamientos().size());
        assertTrue(alumno.getEquipamientos().contains(espada));
        assertEquals(60, alumno.getEnergiaMaxima());
        assertEquals(100, alumno.getVidaMaxima());
    }
}

