package tecnicas;

import java.util.Objects;

import combatiente.Combatiente;

public abstract class Tecnica {

    private final String nombre;
    private final int costoEnergia;

    protected Tecnica(String nombre, int costoEnergia) {
        if (nombre == null || nombre.isBlank() || costoEnergia < 0) {
            throw new IllegalArgumentException("Datos de técnica inválidos");
        }

        this.nombre = nombre;
        this.costoEnergia = costoEnergia;
    }
    

    public abstract void ejecutar(Combatiente ejecutor, Combatiente objetivo);
    
    public boolean esDeRecuperacion() { return false; }
    
    public boolean esDefensiva() { return false; }
    
    public boolean esOfensiva() { return false; }
 

    public String getNombre() {
        return nombre;
    }

    public int getCostoEnergia() {
        return costoEnergia;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tecnica)) return false;
        Tecnica that = (Tecnica) o;
        return Objects.equals(this.nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
