import java.util.Random;

public class RuletaCasinoSesgada {
    private final Random random = new Random();
    private int numeroGanador;
    private String colorGanador;

    // Probabilidad de que el casino gane (0.0 a 1.0)
    private double ventajaCasino = 0.6; // 60% de las veces el jugador perderá

    // Giro manipulado de la ruleta
    public void girar(String tipoApuesta, String valorApuesta) {
        double prob = random.nextDouble();

        // Si el número aleatorio está dentro de la ventaja del casino, forzamos un resultado perdedor
        if (prob < ventajaCasino) {
            generarResultadoPerdedor(tipoApuesta, valorApuesta);
        } else {
            generarResultadoAleatorio();
        }
    }

    // Genera cualquier número (para cuando el jugador puede ganar)
    private void generarResultadoAleatorio() {
        numeroGanador = random.nextInt(37);
        colorGanador = obtenerColor(numeroGanador);
    }

    // Genera un número con el que el jugador pierda
    private void generarResultadoPerdedor(String tipo, String valor) {
        while (true) {
            int candidato = random.nextInt(37);
            String color = obtenerColor(candidato);

            boolean ganaria = false;

            switch (tipo.toLowerCase()) {
                case "numero" -> ganaria = (Integer.parseInt(valor) == candidato);
                case "color" -> ganaria = valor.equalsIgnoreCase(color);
                case "parimpar" -> {
                    if (candidato != 0) {
                        ganaria = (valor.equals("par") && candidato % 2 == 0) ||
                                  (valor.equals("impar") && candidato % 2 != 0);
                    }
                }
                case "rangos" -> {
                    ganaria = (valor.equals("bajo") && candidato >= 1 && candidato <= 18) ||
                              (valor.equals("alto") && candidato >= 19 && candidato <= 36);
                }
            }

            // Si el jugador NO ganaría con este número, lo elegimos
            if (!ganaria) {
                numeroGanador = candidato;
                colorGanador = color;
                break;
            }
        }
    }

    public boolean verificarApuesta(String tipo, String valor) {
        tipo = tipo.toLowerCase();
        valor = valor.toLowerCase();

        switch (tipo) {
            case "numero":
                return Integer.parseInt(valor) == numeroGanador;

            case "color":
                return valor.equals(colorGanador);

            case "parimpar":
                if (numeroGanador == 0) return false;
                return (valor.equals("par") && numeroGanador % 2 == 0)
                        || (valor.equals("impar") && numeroGanador % 2 != 0);

            case "rangos":
                if (numeroGanador >= 1 && numeroGanador <= 18 && valor.equals("bajo")) return true;
                if (numeroGanador >= 19 && numeroGanador <= 36 && valor.equals("alto")) return true;
                return false;

            default:
                return false;
        }
    }

    public double calcularPago(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "numero" -> 35;
            case "color", "parimpar", "rangos" -> 1;
            default -> 0;
        };
    }

    public int getNumeroGanador() {
        return numeroGanador;
    }

    public String getColorGanador() {
        return colorGanador;
    }

    private String obtenerColor(int numero) {
        if (numero == 0) return "verde";
        int[] rojos = {1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36};
        for (int r : rojos) {
            if (numero == r) return "rojo";
        }
        return "negro";
    }
}
