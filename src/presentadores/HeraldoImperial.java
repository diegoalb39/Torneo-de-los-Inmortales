package presentadores;

import java.util.List;
import pareja.Pareja;

public class HeraldoImperial implements InteresadoAcontecimiento {

    @Override
    public void recibirAcontecimiento(Acontecimiento acontecimiento) {
        System.out.println(acontecimiento.getDetalle());
    }

    public void presentarTablaPosiciones(List<PosicionPareja> tabla) {
        System.out.println("\n=== TABLA DE POSICIONES DEL TORNEO ===");
        int puesto = 1;
        for (PosicionPareja pos : tabla) {
            Pareja p = pos.getPareja();
            System.out.printf("%d° Puesto - Pareja #%d (%s y %s) | Última ronda: %d | Vida final: %d%n",
                    puesto++,
                    p.getId(),
                    p.getC1().getNombre(),
                    p.getC2().getNombre(),
                    pos.getUltimaRondaJugada(),
                    pos.getVidaTotalUltimoCombate());
        }
    }

    public void anunciarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}