import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final int INF = 9999999; // Valor infinito para representar la ausencia de conexión

    public static void main(String[] args) {
        int[][] grafo = leerGrafoDesdeArchivo("grafo.txt");
        int numVertices = obtenerNumeroVertices(grafo);
        int[][] distancias = aplicarAlgoritmoFloydWarshall(grafo, numVertices);
        // imprimirDistancias(distancias, numVertices);

        // Aquí puedes agregar tus opciones adicionales
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre de la ciudad origen: ");
        String ciudadOrigen = scanner.nextLine();
        System.out.print("Ingrese el nombre de la ciudad destino: ");
        String ciudadDestino = scanner.nextLine();

        int indiceOrigen = obtenerIndiceCiudad(ciudadOrigen, grafo);
        int indiceDestino = obtenerIndiceCiudad(ciudadDestino, grafo);

        if (indiceOrigen == -1 || indiceDestino == -1) {
            System.out.println("Una o ambas ciudades no existen en el grafo.");
        } else {
            int distanciaMinima = distancias[indiceOrigen][indiceDestino];
            if (distanciaMinima == INF) {
                System.out.println("No hay ruta disponible entre las ciudades seleccionadas.");
            } else {
                System.out.println("La ruta más corta entre " + ciudadOrigen + " y " + ciudadDestino + " es: ");
                System.out.println("Distancia: " + distanciaMinima);
                System.out.print("Ciudades intermedias: ");
                encontrarCiudadesIntermedias(indiceOrigen, indiceDestino, distancias, grafo);
            }
        }
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

                if (!indiceCiudad.containsKey(ciudadOrigen)) {
                    indiceCiudad.put(ciudadOrigen, indiceCiudad.size());
                }
                int indiceOrigen = indiceCiudad.get(ciudadOrigen);

                if (!indiceCiudad.containsKey(ciudadDestino)) {
                    indiceCiudad.put(ciudadDestino, indiceCiudad.size());
                }
                int indiceDestino = indiceCiudad.get(ciudadDestino);

                grafo[indiceOrigen][indiceDestino] = tiempoNormal;
                grafo[indiceOrigen][indiceDestino] = tiempoLluvia;
                grafo[indiceOrigen][indiceDestino] = tiempoNieve;
                grafo[indiceOrigen][indiceDestino] = tiempoTormenta;
            }
            System.out.println("Contenido de la matriz grafo:");
        for (int i = 0; i < grafo.length; i++) {
            for (int j = 0; j < grafo.length; j++) {
                System.out.print(grafo[i][j] + " ");
            }
            System.out.println();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grafo;
    }

    private static int obtenerNumeroVertices(int[][] grafo) {
        return grafo.length;
    }

    private static int obtenerIndiceCiudad(String ciudad, int[][] grafo) {
        for (int i = 0; i < grafo.length; i++) {
            if (String.valueOf(grafo[i][0]).equals(ciudad)) {
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

    private static void encontrarCiudadesIntermedias(int indiceOrigen, int indiceDestino, int[][] distancias, int[][] grafo) {
        List<Integer> ciudadesIntermedias = new ArrayList<>();
        String ciudadIntermedia = obtenerNombreCiudad(distancias[indiceOrigen][indiceDestino], grafo);
        encontrarCiudadesIntermediasRecursivo(indiceOrigen, indiceDestino, distancias, ciudadesIntermedias);
        ciudadesIntermedias.add(Integer.parseInt(ciudadIntermedia));
        encontrarCiudadesIntermediasRecursivo(Integer.parseInt(ciudadIntermedia), indiceDestino, distancias, ciudadesIntermedias);

        for (int i = 0; i < ciudadesIntermedias.size(); i++) {
            String nombreCiudad = obtenerNombreCiudad(ciudadesIntermedias.get(i), grafo);
            System.out.print(nombreCiudad + " ");
        }
        System.out.println();
    }

    private static void encontrarCiudadesIntermediasRecursivo(int indiceOrigen, int indiceDestino, int[][] distancias, List<Integer> ciudadesIntermedias) {
        int ciudadIntermedia = distancias[indiceOrigen][indiceDestino];
        if (ciudadIntermedia != indiceDestino) {
            encontrarCiudadesIntermediasRecursivo(indiceOrigen, ciudadIntermedia, distancias, ciudadesIntermedias);
            ciudadesIntermedias.add(ciudadIntermedia);
            encontrarCiudadesIntermediasRecursivo(ciudadIntermedia, indiceDestino, distancias, ciudadesIntermedias);
        }
    }

    private static String obtenerNombreCiudad(int indiceCiudad, int[][] grafo) {
        for (int i = 0; i < grafo.length; i++) {
            if (grafo[i][0] == indiceCiudad) {
                return grafo[i][0] + "";
            }
        }
        return null;
    }
}
