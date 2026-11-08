package equipamiento;

public abstract class Equipamiento {
	private final String nombre;
    private final CategoriaEquipamiento categoria;

    public Equipamiento(String nombre, CategoriaEquipamiento categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public String getNombre() { return nombre; }
    public CategoriaEquipamiento getCategoria() { return categoria; }

    public int bonoDanio() { return 0; }
    public int bonoReduccionDanio() { return 0; }
    public int bonoVidaMaxima() { return 0; }
    public int bonoEnergiaMaxima() { return 0; }

}
