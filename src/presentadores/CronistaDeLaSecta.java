package presentadores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pareja.Pareja;

public class CronistaDeLaSecta implements InteresadoAcontecimiento {

	private final List<Acontecimiento> historial = new ArrayList<>();
	private final Map<Pareja, PosicionPareja> estadisticasPorPareja = new HashMap<>();
	private Pareja campeona;

	@Override
	public void recibirAcontecimiento(Acontecimiento acontecimiento) {
		historial.add(acontecimiento);

		if (acontecimiento.getTipo() == TipoAcontecimiento.RESULTADO_COMBATE) {
			int ronda = acontecimiento.getRonda();
			Pareja ganadora = acontecimiento.getParejaGanadora();
			Pareja perdedora = acontecimiento.getParejaPerdedora();

			estadisticasPorPareja.put(ganadora,
					new PosicionPareja(ganadora, ronda, acontecimiento.getVidaTotalGanadora(), false));
			estadisticasPorPareja.put(perdedora,
					new PosicionPareja(perdedora, ronda, acontecimiento.getVidaTotalPerdedora(), false));
		} else if (acontecimiento.getTipo() == TipoAcontecimiento.PROCLAMACION) {
			this.campeona = acontecimiento.getParejaGanadora();
			PosicionPareja previa = estadisticasPorPareja.get(campeona);
			if (previa != null) {
				estadisticasPorPareja.put(campeona, new PosicionPareja(campeona, previa.getUltimaRondaJugada(),
						previa.getVidaTotalUltimoCombate(), true));
			}
		}
	}

	public List<Acontecimiento> getHistorial() {
		return Collections.unmodifiableList(historial);
	}

	public Pareja getCampeona() {
		return campeona;
	}

	public List<PosicionPareja> obtenerTablaPosiciones() {
		List<PosicionPareja> tabla = new ArrayList<>(estadisticasPorPareja.values());
		Collections.sort(tabla);
		return tabla;
	}
}