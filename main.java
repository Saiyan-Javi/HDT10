import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class main {
    private static final int INF = 9999999; // Valor infinito para representar la ausencia de conexión

    public static void main(String[] args) {
        int[][] grafo = leerGrafoDesdeArchivo("grafo.txt");
        int numVertices = obtenerNumeroVertices(grafo);
        int[][] distancias = aplicarAlgoritmoFloydWarshall(grafo, numVertices);
        imprimirDistancias(distancias, numVertices);
    }

    private static int[][] leerGrafoDesdeArchivo(String nombreArchivo) {
        int[][] grafo = null;
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            List<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
    
            int numVertices = lineas.size();
            grafo = new int[numVertices][numVertices];
    
            // Crear un mapa para almacenar el índice de cada ciudad
            Map<String, Integer> indiceCiudad = new HashMap<>();
    
            for (int i = 0; i < numVertices; i++) {
                Arrays.fill(grafo[i], INF);
                String[] partes = lineas.get(i).split(",");
                String ciudadOrigen = partes[0];
                String ciudadDestino = partes[1];
                int tiempoNormal = Integer.parseInt(partes[2]);
                int tiempoLluvia = Integer.parseInt(partes[3]);
                int tiempoNieve = Integer.parseInt(partes[4]);
                int tiempoTormenta = Integer.parseInt(partes[5]);
    
                // Verificar si ya se ha asignado un índice a la ciudad de origen
                if (!indiceCiudad.containsKey(ciudadOrigen)) {
                    indiceCiudad.put(ciudadOrigen, indiceCiudad.size());
                }
                int indiceOrigen = indiceCiudad.get(ciudadOrigen);
    
                // Verificar si ya se ha asignado un índice a la ciudad de destino
                if (!indiceCiudad.containsKey(ciudadDestino)) {
                    indiceCiudad.put(ciudadDestino, indiceCiudad.size());
                }
                int indiceDestino = indiceCiudad.get(ciudadDestino);
    
                grafo[indiceOrigen][indiceDestino] = tiempoNormal;
                grafo[indiceOrigen][indiceDestino] = tiempoLluvia;
                grafo[indiceOrigen][indiceDestino] = tiempoNieve;
                grafo[indiceOrigen][indiceDestino] = tiempoTormenta;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grafo;
    }
    

    private static int obtenerNumeroVertices(int[][] grafo) {
        return grafo.length;
    }

    private static int obtenerIndiceCiudad(String ciudad, List<String> lineas) {
        for (int i = 0; i < lineas.size(); i++) {
            String[] partes = lineas.get(i).split(",");
            if (partes[0].equals(ciudad)) {
                return i;
            }
        }
        return -1;
    }

    private static int[][] aplicarAlgoritmoFloydWarshall(int[][] grafo, int numVertices) {
        int[][] distancias = Arrays.copyOf(grafo, numVertices);
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }
        return distancias;
    }

    private static void imprimirDistancias(int[][] distancias, int numVertices) {
        System.out.println("Distancias más cortas entre todas las ciudades:");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (distancias[i][j] == INF) {
                    System.out.print("INF ");
                } else {
                    System.out.print(distancias[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
