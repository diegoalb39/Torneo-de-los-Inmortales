package pareja;

import java.util.HashMap;
import java.util.Map;

import combatiente.Combatiente;
import tecnicas.Tecnica;

public class Pareja implements Comparable<Pareja> {
	private int id = 0;
	// uso 2 campos y no una coleccion por ser 2 combatientes fijos y por
	// simplicidad para buscarlos
	private final Combatiente c1;
	private final Combatiente c2;

	// Regla del compañero: qué integrante "tiene tomada" cada técnica en el combate actual
	private final Map<Tecnica, Combatiente> usadaPor = new HashMap<>();

	public Pareja(int id, Combatiente c1, Combatiente c2) {
		this.id = id;
		this.c1 = c1;
		this.c2 = c2;
	}

	public Combatiente getC1() {
		return c1;
	}

	public Combatiente getC2() {
		return c2;
	}

	public boolean contieneA(Combatiente c) {
		return c1.equals(c) || c2.equals(c);
	}

	// El compañero la bloquea mientras el que la usó siga en pie y le queden usos
	public boolean puedeUsarTecnica(Combatiente c, Tecnica t) {
		Combatiente duenio = usadaPor.get(t);//obtenes el que usa la tecnica y te fijas si la uso c, el compa
		if (duenio == null || duenio.equals(c)) {
			return true;
		}
		return !duenio.estaEnPie() || duenio.getUsosRestantes(t) <= 0;
	}

	public void registrarUso(Combatiente c, Tecnica t) {
		usadaPor.put(t, c);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Pareja otra = (Pareja) obj;
		return id == otra.id;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}

	public int getId() {
		return this.id;
	}

	@Override
	public int compareTo(Pareja o) {
		return Integer.compare(this.id, o.id);
	}

	// Al comenzar cada combate: vida/energía al máximo y se libera la regla del compañero
	public void restaurar() {
		c1.restaurar();
		c2.restaurar();
		usadaPor.clear();
	}
}
