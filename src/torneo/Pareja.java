package torneo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import combatiente.Combatiente;
import tecnicas.Tecnica;

public class Pareja {

    private final int id;
    private final Combatiente primero; 
    private final Combatiente segundo; 

    // Registro por combate para la "Regla del compañero": Técnica -> Primer integrante que la usó
    private final Map<Tecnica, Combatiente> tecnicasReservadasEnCombate = new HashMap<>();

    public Pareja(int id, Combatiente c1, Combatiente c2) {
        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException("Los integrantes no pueden ser nulos");
        }
        if (c1.equals(c2)) {
            throw new IllegalArgumentException("Un combatiente no puede formar pareja consigo mismo");
        }
        this.id = id;
        // Ordenamos al construir para que siempre actúe primero el de menor ID
        if (c1.getId() < c2.getId()) {
            this.primero = c1;
            this.segundo = c2;
        } else {
            this.primero = c2;
            this.segundo = c1;
        }
    }

    public void prepararParaCombate() {
        tecnicasReservadasEnCombate.clear(); // La restricción del compañero termina entre combates
        primero.prepararParaCombate();
        segundo.prepararParaCombate();
    }

    public void recuperarEnergiaAsalto(int energiaPorAsalto) {
        primero.recuperarEnergiaAsalto(energiaPorAsalto);
        segundo.recuperarEnergiaAsalto(energiaPorAsalto);
    }

    /*
    	Regla del compañero: Cuando un integrante usa una técnica en un combate, su compañero no puede usarla 
     mientras el primero conserve usos de ella y siga en pie; si los agota o queda
     fuera de combate, el compañero puede usarla.
     */
    public boolean puedeUsarSegunReglaCompaniero(Combatiente ejecutor, Tecnica tecnica) {
        Combatiente duenioRestriccion = tecnicasReservadasEnCombate.get(tecnica);

        if (duenioRestriccion == null || duenioRestriccion.equals(ejecutor)) {
            return true;
        }

        // Si el compañero que la usó primero sigue en pie y aún tiene usos, está bloqueada
        boolean companieroLaRetiene = duenioRestriccion.estaEnPie()
                && duenioRestriccion.getUsosRestantes(tecnica) > 0;

        return !companieroLaRetiene;
    }

    public void registrarUsoTecnicaEnCombate(Combatiente ejecutor, Tecnica tecnica) {
        Combatiente actual = tecnicasReservadasEnCombate.get(tecnica);
        // Si nadie la había usado o el dueño anterior ya la liberó (cayó o agotó usos), se actualiza
        if (actual == null || !actual.estaEnPie() || actual.getUsosRestantes(tecnica) == 0) {
            tecnicasReservadasEnCombate.put(tecnica, ejecutor);
        }
    }

    /**
     Objetivo para ataques y técnicas ofensivas: El rival en pie con menos vida actual; en empate, el de menor identificador.
     */
    public Combatiente elegirObjetivoRivalEnPie() {
        boolean pEnPie = primero.estaEnPie();
        boolean sEnPie = segundo.estaEnPie();

        if (pEnPie && sEnPie) {
            return (primero.getVidaActual() <= segundo.getVidaActual()) ? primero : segundo;
        }
        if (pEnPie) return primero;
        if (sEnPie) return segundo;
        return null;
    }

    public boolean tieneIntegrantesEnPie() {
        return primero.estaEnPie() || segundo.estaEnPie();
    }

    public int getVidaTotal() {
        int vida1 = primero.estaEnPie() ? primero.getVidaActual() : 0;
        int vida2 = segundo.estaEnPie() ? segundo.getVidaActual() : 0;
        return vida1 + vida2;
    }

    public List<Combatiente> getIntegrantesOrdenados() {
        return List.of(primero, segundo);
    }

    public int getId() { return id; }
    public Combatiente getPrimero() { return primero; }
    public Combatiente getSegundo() { return segundo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pareja)) return false;
        Pareja that = (Pareja) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}