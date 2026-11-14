package escuela;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import combatiente.Combatiente;
import equipamiento.Equipamiento;
import tecnicas.Tecnica;

public class Escuela {

    private final String nombre;
    private final List<Tecnica> tecnicas;
    private final List<Equipamiento> equipamientos;
    
    //Las técnicas deben conservar el orden de la escuela para resolver empates durante el combate, por eso usamos una lista.

    public Escuela(String nombre) {
        this.nombre = Objects.requireNonNull(nombre); //Si nombre tiene un valor asignado, lo asigna. Si no devuelve un NullPointerException.
        this.tecnicas = new ArrayList<>();
        this.equipamientos = new ArrayList<>();
    }

    public void agregarTecnica(Tecnica tecnica) {
        Objects.requireNonNull(tecnica);
        if (!tecnicas.contains(tecnica)) {
            tecnicas.add(tecnica);
        }
    }

    public void agregarEquipamiento(Equipamiento equipo) {
        Objects.requireNonNull(equipo);
        equipamientos.add(equipo);
    }

    public void prepararCombatiente(Combatiente combatiente) {
        Objects.requireNonNull(combatiente);

        for (Tecnica tecnica : tecnicas) {
            combatiente.aprenderTecnica(tecnica);
        }

        for (Equipamiento equipo : equipamientos) {
            combatiente.equipar(equipo);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public List<Tecnica> getTecnicas() {
        return Collections.unmodifiableList(tecnicas);
    }

    public List<Equipamiento> getEquipamientos() {
        return Collections.unmodifiableList(equipamientos);
    }
}

//Ejemplo implementacion:
//
//Escuela palacioVacio = new Escuela("Palacio del Vacío");
//
//palacioVacio.agregarTecnica(new TecnicaDrenaje("Dominio de la Nada", 20, 20, 10));
//
//palacioVacio.agregarTecnica(new TecnicaOfensiva("Corte de los Mil Amaneceres", 10, 25));
