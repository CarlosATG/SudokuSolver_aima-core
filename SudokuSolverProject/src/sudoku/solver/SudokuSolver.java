package sudoku.solver;

import aima.core.search.csp.CSP;
import aima.core.search.csp.Domain;
import aima.core.search.csp.Variable;
import aima.core.search.csp.examples.NotEqualConstraint;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.Assignment;

import java.util.ArrayList;
import java.util.List;

public class  SudokuSolver {

    public static void main(String[] args) {
        int[][] tableroInicial = new int[9][9];
        // Poner valores Iniciales, hasta 15
        tableroInicial[0][0] = 5;
        tableroInicial[0][1] = 3;
        tableroInicial[0][4] = 7;
        
        tableroInicial[1][0] = 6;
        tableroInicial[1][3] = 1;
        tableroInicial[1][4] = 9;
        tableroInicial[1][5] = 5;

        tableroInicial[2][1] = 9;
        tableroInicial[2][2] = 8;
        tableroInicial[2][7] = 6;
        
        tableroInicial[3][0] = 8;
        tableroInicial[3][4] = 6;
        tableroInicial[3][8] = 3;
        
        tableroInicial[4][0] = 4;
        tableroInicial[4][8] = 1;
        

        SudokuCSP csp = new SudokuCSP(tableroInicial);

        // Usar el algoritmo de backtracking para resolver el CSP
        BacktrackingStrategy solucionador = new BacktrackingStrategy();
        Assignment solucion = solucionador.solve(csp);

        if (solucion != null) {
            imprimirSolucion(solucion);
        } else {
            System.out.println("No se encontró solución.");
        }
    }

    // Definir el CSP para Sudoku
    public static class SudokuCSP extends CSP {

        public SudokuCSP(int[][] tableroInicial) {
            // Crear variables y dominios
            List<Variable> variables = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Variable variable = new Variable("Celda" + i + j);
                    addVariable(variable);
                    variables.add(variable);
                    if (tableroInicial[i][j] != 0) {
                        setDomain(variable, new Domain(new Object[]{tableroInicial[i][j]}));
                    } else {
                        setDomain(variable, new Domain(new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));
                    }
                }
            }

            // Agregar restricciones para asegurar las reglas de Sudoku (filas, columnas y subcuadrículas 3x3)
            for (int i = 0; i < 9; i++) {
                agregarRestriccionDiferentes(getFila(i, variables));
                agregarRestriccionDiferentes(getColumna(i, variables));
                agregarRestriccionDiferentes(getSubcuadricula(i, variables));
            }
        }

        private List<Variable> getFila(int fila, List<Variable> variables) {
            List<Variable> variablesFila = new ArrayList<>();
            for (int col = 0; col < 9; col++) {
                variablesFila.add(variables.get(fila * 9 + col));
            }
            return variablesFila;
        }

        private List<Variable> getColumna(int col, List<Variable> variables) {
            List<Variable> variablesColumna = new ArrayList<>();
            for (int fila = 0; fila < 9; fila++) {
                variablesColumna.add(variables.get(fila * 9 + col));
            }
            return variablesColumna;
        }

        private List<Variable> getSubcuadricula(int indice, List<Variable> variables) {
            List<Variable> variablesSubcuadricula = new ArrayList<>();
            int filaInicio = (indice / 3) * 3;
            int colInicio = (indice % 3) * 3;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    variablesSubcuadricula.add(variables.get((filaInicio + i) * 9 + (colInicio + j)));
                }
            }
            return variablesSubcuadricula;
        }

        private void agregarRestriccionDiferentes(List<Variable> vars) {
            for (int i = 0; i < vars.size(); i++) {
                for (int j = i + 1; j < vars.size(); j++) {
                    addConstraint(new NotEqualConstraint(vars.get(i), vars.get(j)));
                }
            }
        }
    }

    // Imprimir la solución del Sudoku
    private static void imprimirSolucion(Assignment solucion) {
        int[][] tablero = new int[9][9];
        for (Variable variable : solucion.getVariables()) {
            String nombre = variable.getName();
            int fila = Character.getNumericValue(nombre.charAt(5));
            int col = Character.getNumericValue(nombre.charAt(6));
            tablero[fila][col] = (Integer) solucion.getAssignment(variable);
        }

        for (int r = 0; r < 9; r++) {
            for (int d = 0; d < 9; d++) {
                System.out.print(tablero[r][d] + " ");
            }
            System.out.println();
        }
    }
}