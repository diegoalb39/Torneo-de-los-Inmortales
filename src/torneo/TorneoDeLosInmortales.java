package torneo;

import combatiente.Combatiente;
import escuela.Escuela;
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

        Escuela escuela = new Escuela("Palacio del Vacío");
        escuela.agregarTecnica(new TecnicaOfensiva("Corte de los Mil Amaneceres", 10, 25));
        escuela.agregarTecnica(new TecnicaOfensiva("Rugido de la Bestia Primordial", 25, 60));
        escuela.agregarTecnica(new TecnicaDrenaje("Dominio de la Nada", 15, 20, 10));
        escuela.agregarTecnica(new TecnicaDefensiva("Muro del Cuerpo Dorado", 10, 5));
        escuela.agregarTecnica(new TecnicaRecuperacion("Meditación del Inmortal", 15, 30));

        Combatiente aurelio = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Aurelio", escuela);
        Combatiente nebula = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Nébula", escuela);
        Combatiente kael = fabrica.crearCombatiente(TipoCombatiente.MONJE_CELESTIAL, "Kael", escuela);
        Combatiente darian = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Darian", escuela);

        Combatiente seraph = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Seraph", escuela);
        Combatiente lyra = fabrica.crearCombatiente(TipoCombatiente.MONJE_CELESTIAL, "Lyra", escuela);
        Combatiente orion = fabrica.crearCombatiente(TipoCombatiente.CULTIVADOR_ESPADA, "Orión", escuela);
        Combatiente vega = fabrica.crearCombatiente(TipoCombatiente.HECHICERO_VACIO, "Vega", escuela);

        Pareja p1 = new Pareja(1, aurelio, nebula);
        Pareja p2 = new Pareja(2, kael, darian);
        Pareja p3 = new Pareja(3, seraph, lyra);
        Pareja p4 = new Pareja(4, orion, vega);

        Torneo torneo = new Torneo();
        HeraldoImperial heraldo = new HeraldoImperial();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();

        // Registramos al Heraldo y al Cronista como observadores
        torneo.registrarInteresado(heraldo);
        torneo.registrarInteresado(cronista);

        torneo.inscribir(p1);
        torneo.inscribir(p2);
        torneo.inscribir(p3);
        torneo.inscribir(p4);

        // Ejecutamos el torneo completo (Semifinales + Final + Proclamación)
        torneo.iniciarTorneo();

        // Al terminar el torneo, el main le pide la tabla al Cronista y el Heraldo la presenta
        heraldo.presentarTablaPosiciones(cronista.obtenerTablaPosiciones());
    }
}