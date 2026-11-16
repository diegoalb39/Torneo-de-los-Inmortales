package presentadores;

import pareja.Pareja;

public class Acontecimiento {

    private final TipoAcontecimiento tipo;
    private final int ronda;
    private final String detalle;

    // Datos congelados del momento para el cronista y la tabla de posiciones
    private final Pareja parejaGanadora;
    private final Pareja parejaPerdedora;
    private final int vidaTotalGanadora;
    private final int vidaTotalPerdedora;

    private Acontecimiento(TipoAcontecimiento tipo, int ronda, String detalle,
                           Pareja parejaGanadora, Pareja parejaPerdedora,
                           int vidaTotalGanadora, int vidaTotalPerdedora) {
        this.tipo = tipo;
        this.ronda = ronda;
        this.detalle = detalle;
        this.parejaGanadora = parejaGanadora;
        this.parejaPerdedora = parejaPerdedora;
        this.vidaTotalGanadora = vidaTotalGanadora;
        this.vidaTotalPerdedora = vidaTotalPerdedora;
    }

    public static Acontecimiento inicioCombate(int ronda, Pareja p1, Pareja p2) {
        String msg = String.format("[Ronda %d] Inicia combate: Pareja #%d (%s y %s) vs Pareja #%d (%s y %s)",
                ronda,
                p1.getId(), p1.getC1().getNombre(), p1.getC2().getNombre(),
                p2.getId(), p2.getC1().getNombre(), p2.getC2().getNombre());
        return new Acontecimiento(TipoAcontecimiento.INICIO_COMBATE, ronda, msg, null, null, 0, 0);
    }

    public static Acontecimiento accionRealizada(int ronda, String nombreEjecutor, String nombreAccion,
                                                 String nombreObjetivo, int vidaRestanteObjetivo) {
        String msg = String.format("  -> %s ejecuta %s sobre %s (Vida actual: %d)",
                nombreEjecutor, nombreAccion, nombreObjetivo, vidaRestanteObjetivo);
        return new Acontecimiento(TipoAcontecimiento.ACCION_REALIZADA, ronda, msg, null, null, 0, 0);
    }
    
    // Describe la reacción que se activa cuando un combatiente pierde a su compañero.
    public static Acontecimiento reaccionActivada(int ronda, String nombreCombatiente) {
        String msg = String.format("  [!] %s activa su reacción por la caída de su compañero", nombreCombatiente);
        return new Acontecimiento(TipoAcontecimiento.REACCION_ACTIVADA, ronda, msg, null, null, 0, 0);
    }

    public static Acontecimiento eliminacion(int ronda, String nombreCaido, String nombreEjecutor) {
        String msg = String.format("  [!] %s ha quedado fuera de combate ante el ataque de %s",
                nombreCaido, nombreEjecutor);
        return new Acontecimiento(TipoAcontecimiento.ELIMINACION, ronda, msg, null, null, 0, 0);
    }

    public static Acontecimiento resultadoCombate(int ronda, Pareja ganadora, Pareja perdedora,
                                                  int vidaGanadora, int vidaPerdedora) {
        String msg = String.format("[Ronda %d] Fin del combate -> Ganadora: Pareja #%d (Vida total: %d) | Perdedora: Pareja #%d (Vida total: %d)",
                ronda, ganadora.getId(), vidaGanadora, perdedora.getId(), vidaPerdedora);
        return new Acontecimiento(TipoAcontecimiento.RESULTADO_COMBATE, ronda, msg,
                ganadora, perdedora, vidaGanadora, vidaPerdedora);
    }

    public static Acontecimiento proclamacion(int rondaFinal, Pareja campeona) {
        String msg = String.format("=== PROCLAMACIÓN: La Pareja #%d (%s y %s) son los Dos Santos Supremos ===",
                campeona.getId(), campeona.getC1().getNombre(), campeona.getC2().getNombre());
        return new Acontecimiento(TipoAcontecimiento.PROCLAMACION, rondaFinal, msg, campeona, null, 0, 0);
    }

    public TipoAcontecimiento getTipo() { return tipo; }
    public int getRonda() { return ronda; }
    public String getDetalle() { return detalle; }
    public Pareja getParejaGanadora() { return parejaGanadora; }
    public Pareja getParejaPerdedora() { return parejaPerdedora; }
    public int getVidaTotalGanadora() { return vidaTotalGanadora; }
    public int getVidaTotalPerdedora() { return vidaTotalPerdedora; }
}