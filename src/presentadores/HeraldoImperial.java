package presentadores;

import pareja.Pareja;

public class HeraldoImperial {
	
	public void anunciarInicioTorneo() {
        System.out.println("=== COMIENZA EL TORNEO IMPERIAL ===");
    }
	   public void anunciarParejaInscripta(Pareja pareja) {
	        System.out.println(
	            "Pareja #" + pareja.getId() + " inscripta: "
	            + pareja.getC1().getNombre() + " vs " + pareja.getC2().getNombre()
	        );
	    }
}
