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

        Escuela espada = new Escuela("Escuela de la Espada Celestial");
        espada.agregarTecnica(new TecnicaOfensiva("Corte de los Mil Amaneceres", 10, 25));
        espada.agregarEquipamiento(new EspadaCelestial());
        espada.agregarEquipamiento(new ArmaduraInfernal());

        Escuela vacio = new Escuela("Palacio del Vacío");
        vacio.agregarTecnica(new TecnicaDrenaje("Dominio de la Nada", 15, 20, 10));
        vacio.agregarTecnica(new TecnicaRecuperacion("Meditación del Inmortal", 15, 30));
        vacio.agregarEquipamiento(new AnilloEspiritual());

        Escuela cuerpo = new Escuela("Monasterio del Cuerpo Dorado");
        cuerpo.agregarTecnica(new TecnicaDefensiva("Muro del Cuerpo Dorado", 10, 5));
        cuerpo.agregarTecnica(new TecnicaOfensiva("Rugido de la Bestia Primordial", 25, 60));
        cuerpo.agregarEquipamiento(new ArmaduraDorada());
        cuerpo.agregarEquipamiento(new SelloDelFenix());

        Combatiente aurelio = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Aurelio", espada);
        Combatiente nebula = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Nébula", vacio);
        Combatiente kael = fabrica.crearCombatiente(TipoCombatiente.MONJE_CELESTIAL, "Kael", cuerpo);
        Combatiente darian = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Darian", espada);

        Combatiente seraph = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Seraph", vacio);
        Combatiente lyra = fabrica.crearCombatiente(TipoCombatiente.MONJE_CELESTIAL, "Lyra", cuerpo);
        Combatiente orion = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Orión", espada);
        Combatiente vega = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Vega", vacio);

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