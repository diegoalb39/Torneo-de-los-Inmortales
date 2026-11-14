package fabrica;

import combatiente.Combatiente;
import combatiente.CultivadorDeLaEspada;
import combatiente.MonjeDelCuerpoCelestial;
import combatiente.HechiceroDelVacio;
import escuela.Escuela;

import java.util.Objects;

public class FabricaCombatientes {
	
	//Agregar un cuarto tipo de combatiente requiere modificar la fábrica, posible alternativa implementarlo con un Map.

    private int siguienteId = 1;

    public Combatiente crearCombatiente(
            TipoCombatiente tipo,
            String nombre,
            Escuela escuela) {

        Objects.requireNonNull(tipo);
        Objects.requireNonNull(escuela);

        int id = siguienteId;

        Combatiente combatiente;

        switch (tipo) {
            case CULTIVADOR_ESPADA:
                combatiente = new CultivadorDeLaEspada(id, nombre);
                break;

            case MONJE_CELESTIAL:
                combatiente = new MonjeDelCuerpoCelestial(id, nombre);
                break;

            case HECHICERO_VACIO:
                combatiente = new HechiceroDelVacio(id, nombre);
                break;

            default:
                throw new IllegalArgumentException(
                    "Tipo de combatiente desconocido"
                );
        }

        escuela.prepararCombatiente(combatiente);

        siguienteId++;

        return combatiente;
    }
}

//Ejemplo implementacion:

//FabricaCombatientes fabrica = new FabricaCombatientes();
//
//Combatiente combatiente1 = fabrica.crearCombatiente(
//TipoCombatiente.CULTIVADOR_ESPADA, "Li Jian", espadaCelestial);

