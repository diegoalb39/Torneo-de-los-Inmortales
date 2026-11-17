package torneo;

import combatiente.Combatiente;
import escuela.Escuela;
import equipamiento.AnilloEspiritual;
import equipamiento.ArmaduraDorada;
import equipamiento.ArmaduraInfernal;
import equipamiento.EspadaCelestial;
import equipamiento.SelloDelFenix;
import fabrica.FabricaCombatientes;
import fabrica.TipoCombatiente;
import pareja.Pareja;
import presentadores.CronistaDeLaSecta;
import presentadores.HeraldoImperial;
import tecnicas.TecnicaDefensiva;
import tecnicas.TecnicaDrenaje;
import tecnicas.TecnicaOfensiva;
import tecnicas.TecnicaRecuperacion;

public class TorneoDeLosInmortales {

	public static void main(String[] args) {
		FabricaCombatientes fabrica = new FabricaCombatientes();

		Escuela escuelaDeLaEspada = new Escuela("Escuela de la Espada Celestial");
		escuelaDeLaEspada.agregarTecnica(new TecnicaOfensiva("Corte de los Mil Amaneceres", 10, 25));
		escuelaDeLaEspada.agregarEquipamiento(new EspadaCelestial());
		escuelaDeLaEspada.agregarEquipamiento(new ArmaduraInfernal());

		Escuela palacioDelVacio = new Escuela("Palacio del Vacío");
		palacioDelVacio.agregarTecnica(new TecnicaDrenaje("Dominio de la Nada", 15, 20, 10));
		palacioDelVacio.agregarTecnica(new TecnicaRecuperacion("Meditación del Inmortal", 15, 30));
		palacioDelVacio.agregarEquipamiento(new AnilloEspiritual());

		Escuela monasterioDelCuerpo = new Escuela("Monasterio del Cuerpo Dorado");
		monasterioDelCuerpo.agregarTecnica(new TecnicaDefensiva("Muro del Cuerpo Dorado", 10, 5));
		monasterioDelCuerpo.agregarTecnica(new TecnicaOfensiva("Rugido de la Bestia Primordial", 25, 60));
		monasterioDelCuerpo.agregarEquipamiento(new ArmaduraDorada());
		monasterioDelCuerpo.agregarEquipamiento(new SelloDelFenix());

		Combatiente aurelio = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Aurelio", escuelaDeLaEspada);
		Combatiente nebula = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Nébula", palacioDelVacio);
		Combatiente kael = fabrica.crearCombatiente(TipoCombatiente.MONJE_CELESTIAL, "Kael", monasterioDelCuerpo);
		Combatiente darian = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Darian", escuelaDeLaEspada);

		Combatiente seraph = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Seraph", palacioDelVacio);
		Combatiente lyra = fabrica.crearCombatiente(TipoCombatiente.MONJE_CELESTIAL, "Lyra", monasterioDelCuerpo);
		Combatiente orion = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Orión", escuelaDeLaEspada);
		Combatiente vega = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Vega", palacioDelVacio);

		Pareja p1 = new Pareja(1, aurelio, nebula);
		Pareja p2 = new Pareja(2, kael, darian);
		Pareja p3 = new Pareja(3, seraph, lyra);
		Pareja p4 = new Pareja(4, orion, vega);

		Torneo torneo = new Torneo();
		HeraldoImperial heraldo = new HeraldoImperial();
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();

		// Ambos observadores reciben acciones, eliminaciones y activaciones reactivas.
		torneo.registrarInteresado(heraldo);
		torneo.registrarInteresado(cronista);

		torneo.inscribir(p1);
		torneo.inscribir(p2);
		torneo.inscribir(p3);
		torneo.inscribir(p4);

		torneo.iniciarTorneo();
		heraldo.presentarTablaPosiciones(cronista.obtenerTablaPosiciones());
	}
}