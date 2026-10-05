/**
 * Solver oficial para la maraton de la SlotMachine.
 * Cumple al 100% con los requisitos de Testing Tool:
 * Solo utiliza SlotMachine(n), spin(wheel, steps) y distinctSymbols().
 */
public class SlotMachineContest
{
    /**
     * Resuelve el juego manteniendo la maquina invisible.
     * 
     * @param n cantidad de ruedas y simbolos
     * @return cantidad total de movimientos realizados
     */
    public int solve(int n)
    {
        SlotMachine machine = new SlotMachine(n);
        machine.makeInvisible();

        return resolver(machine, n);
    }

    /**
     * Resuelve el juego mostrando la maquina durante el proceso.
     * 
     * @param n cantidad de ruedas y simbolos
     * @return cantidad total de movimientos realizados
     */
    public int simulate(int n)
    {
        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible();

        return resolver(machine, n);
    }

    /**
     * Algoritmo de optimizacion guiado por el gradiente de distinctSymbols().
     * Recorre rueda por rueda probando sus rotaciones y fijando la que reduzca los simbolos distintos.
     */
    private int resolver(SlotMachine machine, int n)
    {
        int movimientos = 0;

        // Si por azar inicio en Jackpot
        if (machine.distinctSymbols() == 1)
        {
            return movimientos;
        }

        int currentDistinct = machine.distinctSymbols();
        boolean huboMejora = true;

        // Itera mientras no haya jackpot y siga habiendo reducciones
        while (currentDistinct > 1 && huboMejora)
        {
            huboMejora = false;

            for (int rueda = 1; rueda <= n; rueda++)
            {
                if (machine.distinctSymbols() == 1)
                {
                    return movimientos;
                }

                int mejorPaso = 0;
                int menorDistinct = currentDistinct;

                // Evalua las rotaciones posibles de la rueda actual
                for (int paso = 1; paso < n; paso++)
                {
                    machine.spin(rueda, 1);
                    movimientos++;

                    int d = machine.distinctSymbols();
                    if (d < menorDistinct)
                    {
                        menorDistinct = d;
                        mejorPaso = paso;
                    }

                    if (d == 1)
                    {
                        return movimientos;
                    }
                }

                // Paso final para completar los n giros y regresar la rueda a su inicio
                machine.spin(rueda, 1);
                movimientos++;

                // Si alguna posicion redujo los simbolos distintos, se fija esa rotacion
                if (mejorPaso > 0)
                {
                    machine.spin(rueda, mejorPaso);
                    movimientos += mejorPaso;
                    currentDistinct = menorDistinct;
                    huboMejora = true;

                    if (currentDistinct == 1)
                    {
                        return movimientos;
                    }
                }
            }

            // En caso de estancamiento en un minimo local, perturba una rueda para continuar el descenso
            if (!huboMejora && currentDistinct > 1)
            {
                int ruedaRandom = (int)(Math.random() * n) + 1;
                int pasosRandom = (int)(Math.random() * (n - 1)) + 1;
                machine.spin(ruedaRandom, pasosRandom);
                movimientos += pasosRandom;
                currentDistinct = machine.distinctSymbols();
                huboMejora = true;
            }
        }

        return movimientos;
    }
}