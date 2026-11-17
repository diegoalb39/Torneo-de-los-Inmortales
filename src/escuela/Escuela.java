package escuela;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import combatiente.Combatiente;
import equipamiento.Equipamiento;
import tecnicas.Tecnica;

public class Escuela {

	private final String nombre;
	private final List<Tecnica> tecnicas;
	private final List<Equipamiento> equipamientos;

	// Las técnicas deben conservar el orden de la escuela para resolver empates
	// durante el combate, por eso usamos una lista.

	public Escuela(String nombre) {
		if (null == nombre) {
			throw new IllegalArgumentException("El nombre de la escuela no puede ser nulo.\n");
		}
		this.nombre = nombre;
		this.tecnicas = new ArrayList<>();
		this.equipamientos = new ArrayList<>();
	}

	public void agregarTecnica(Tecnica tecnica) {
		if (null == tecnica) {
			throw new IllegalArgumentException("No se admiten técnicas nulas.\n");
		}
		if (!tecnicas.contains(tecnica)) {
			tecnicas.add(tecnica);
		}
	}

	// Agrega una opción de equipamiento que la escuela puede ofrecer a sus alumnos.
	public void agregarEquipamiento(Equipamiento equipo) {
		if (null == equipo) {
			throw new IllegalArgumentException("No se admite equipo nulo.\n");
		}
		if (!equipamientos.contains(equipo)) {
			equipamientos.add(equipo);
		}
	}

	// Enseña las técnicas y equipa hasta tres piezas, sin repetir categorías.
	public void prepararCombatiente(Combatiente combatiente) {
		if (null == combatiente) {
			throw new IllegalArgumentException("El combatiente no puede ser nulo.\n");
		}

		for (Tecnica tecnica : tecnicas) {
			combatiente.aprenderTecnica(tecnica);
		}

		for (Equipamiento equipo : equipamientos) {
			if (combatiente.getEquipamientos().size() >= 3) {
				break;
			}
			boolean categoriaOcupada = combatiente.tieneCategoriaOcupada(equipo.getCategoria());
			if (!categoriaOcupada) {
				combatiente.equipar(equipo);
			}
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
