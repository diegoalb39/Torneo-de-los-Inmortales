package tecnicas;

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

    public String getNombre() {
        return nombre;
    }

    public int getCostoEnergia() {
        return costoEnergia;
    }
}
