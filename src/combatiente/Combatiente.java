package combatiente;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import equipamiento.CategoriaEquipamiento;
import equipamiento.Equipamiento;
import tecnicas.Tecnica;

public abstract class Combatiente {
    private static final int MAX_EQUIPAMIENTOS = 3;
    protected static final int VIDA_BASE = 100;
    protected static final int ENERGIA_BASE = 40;
    private static final int MAX_USOS_TECNICA = 5;
    

    private final int id;
    private final String nombre;
    private int vidaActual;
    private int energiaActual;
    private int reduccionDefensivaActiva = 0;

    // Usamos EnumMap para garantizar que no haya dos de la misma categoría
    private final Map<CategoriaEquipamiento, Equipamiento> equipamientos;
    
    // Usamos un LinkedHashMap porque permite asociar cada técnica con su cantidad de usos y conserva el orden en que se agregaron.
    private final Map<Tecnica, Integer> usosTecnicas = new LinkedHashMap<>();

    protected Combatiente(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.equipamientos = new EnumMap<>(CategoriaEquipamiento.class);
        this.vidaActual = VIDA_BASE;
        this.energiaActual = ENERGIA_BASE;
    }


    protected int aplicarBonificacionDanioTipo(int danio) { return danio; }
    protected int aplicarReduccionDanioTipo(int danio) { return danio; }
    protected int aplicarReduccionCostoEnergiaTipo(int costo) { return costo; }


    public int calcularDanioSaliente(int danioBase) {
        int danioConTipo = aplicarBonificacionDanioTipo(danioBase);
        int bonoEquipo = equipamientos.values().stream().mapToInt(Equipamiento::bonoDanio).sum();
        return Math.max(0, danioConTipo + bonoEquipo);
    }

    public void recibirDanio(int danioBruto) {
        int danioTrasTipo = aplicarReduccionDanioTipo(danioBruto);
        int reduccionEquipo = equipamientos.values().stream().mapToInt(Equipamiento::bonoReduccionDanio).sum();
        // Acá también restamos reducción por técnicas defensivas activas en el combate
        int danioFinal = Math.max(0, danioTrasTipo - reduccionEquipo - reduccionDefensivaActiva);//Se agrega redDefensiva al calculo.
        this.vidaActual = Math.max(0, this.vidaActual - danioFinal);
    }

    public int calcularCostoEnergia(int costoBase) {
        return Math.max(0, aplicarReduccionCostoEnergiaTipo(costoBase));
    }

    // Gestión de equipamiento
    public void equipar(Equipamiento nuevo) {
        if (nuevo == null) {
            throw new IllegalArgumentException("El equipamiento no puede ser nulo.");
        }
        if (equipamientos.size() >= MAX_EQUIPAMIENTOS) {
            throw new IllegalStateException("No se pueden llevar más de " + MAX_EQUIPAMIENTOS + " equipamientos.");
        }
        if (equipamientos.containsKey(nuevo.getCategoria())) {
            throw new IllegalStateException("Ya tiene un equipamiento de la categoría: " + nuevo.getCategoria());
        }
        // Equipar un objeto que aumenta un máximo NO aumenta el valor actual
        equipamientos.put(nuevo.getCategoria(), nuevo);
    }

    public void quitarEquipamiento(CategoriaEquipamiento categoria) {
        Equipamiento removido = equipamientos.remove(categoria);
        if (removido != null) {
            // Al quitarlo, el valor actual se recorta al nuevo máximo si lo supera
            this.vidaActual = Math.min(this.vidaActual, getVidaMaxima());
            this.energiaActual = Math.min(this.energiaActual, getEnergiaMaxima());
        }
    }
    
    //Aumento de defensa durante x cantidad de turnos.
    public void agregarDefensaTemporal(int reduccion) {
        if (reduccion < 0) {
            throw new IllegalArgumentException("Reducción inválida");
        }
        reduccionDefensivaActiva += reduccion;
    }
    
    //Aplica en tecnicas de drenaje
    public void perderEnergia(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }
        this.energiaActual = Math.max(0, energiaActual - cantidad);
    }
    
    //Aplica en tecnicas de curacion
    public void recuperarVida(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }

        this.vidaActual = Math.min(
            getVidaMaxima(),
            vidaActual + cantidad
        );
    }
    
    public void aprenderTecnica(Tecnica tecnica) {
        if (tecnica == null) {
            throw new IllegalArgumentException("Técnica nula");
        }

        usosTecnicas.putIfAbsent(tecnica, 0);//Si la tecnica no existe, la inicializa en 0.
    }
    
    public int getUsosRestantes(Tecnica tecnica) {
        Integer usados = usosTecnicas.get(tecnica);

        if (usados == null) {
            throw new IllegalArgumentException("Técnica no aprendida");
        }

        return MAX_USOS_TECNICA - usados;
    }
    

public void ejecutarTecnica(Tecnica tecnica, Combatiente objetivo) {

    if (!estaEnPie()) {
        throw new IllegalStateException("Combatiente fuera de combate");
    }

    if (objetivo == null) {
        throw new IllegalArgumentException("Objetivo nulo");
    }

    if (!usosTecnicas.containsKey(tecnica)) {
        throw new IllegalArgumentException("Técnica no aprendida");
    }

    if (getUsosRestantes(tecnica) <= 0) {
        throw new IllegalStateException("Sin usos disponibles");
    }

    int costo = calcularCostoEnergia(tecnica.getCostoEnergia());

    if (energiaActual < costo) {
        throw new IllegalStateException("Energía insuficiente");
    }

    energiaActual -= costo;

    tecnica.ejecutar(this, objetivo);

    usosTecnicas.put(tecnica, usosTecnicas.get(tecnica) + 1);
}


    public boolean tieneDefensaActiva() {
        return reduccionDefensivaActiva > 0;
    }

    public Collection<Equipamiento> getEquipamientos() {
        return Collections.unmodifiableCollection(equipamientos.values());
    }

    public int getVidaMaxima() {
        int bono = equipamientos.values().stream().mapToInt(Equipamiento::bonoVidaMaxima).sum();
        return VIDA_BASE + bono;
    }

    public int getEnergiaMaxima() {
        int bono = equipamientos.values().stream().mapToInt(Equipamiento::bonoEnergiaMaxima).sum();
        return ENERGIA_BASE + bono;
    }

    public boolean estaEnPie() {
        return this.vidaActual > 0;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getVidaActual() { return vidaActual; }
    public int getEnergiaActual() { return energiaActual; }

    //
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Combatiente)) return false;
        Combatiente that = (Combatiente) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}