package presentadores;

import pareja.Pareja;

public class PosicionPareja implements Comparable<PosicionPareja> {

	private final Pareja pareja;
	private final int ultimaRondaJugada;
	private final int vidaTotalUltimoCombate;
	private final boolean esCampeona;

	public PosicionPareja(Pareja pareja, int ultimaRondaJugada, int vidaTotalUltimoCombate, boolean esCampeona) {
		this.pareja = pareja;
		this.ultimaRondaJugada = ultimaRondaJugada;
		this.vidaTotalUltimoCombate = vidaTotalUltimoCombate;
		this.esCampeona = esCampeona;
	}

	public Pareja getPareja() {
		return pareja;
	}

	public int getUltimaRondaJugada() {
		return ultimaRondaJugada;
	}

	public int getVidaTotalUltimoCombate() {
		return vidaTotalUltimoCombate;
	}

	public boolean isEsCampeona() {
		return esCampeona;
	}

	@Override
	public int compareTo(PosicionPareja otra) {
		int cmpRonda = Integer.compare(otra.ultimaRondaJugada, this.ultimaRondaJugada);
		if (cmpRonda != 0) {
			return cmpRonda;
		}
		int cmpVida = Integer.compare(otra.vidaTotalUltimoCombate, this.vidaTotalUltimoCombate);
		if (cmpVida != 0) {
			return cmpVida;
		}
		return Integer.compare(this.pareja.getId(), otra.pareja.getId());
	}
}