import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

public class main {
    private static final int INF = 9999999; // Valor infinito para representar la ausencia de conexión

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Como esta el clima hoy? 1. Normal 2. Lluevioso 3. Nevado 4. Tormentoso");
        int a = teclado.nextInt();
        teclado.nextLine();
        int[][] grafo = leerGrafoDesdeArchivo("grafo.txt", a);
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

    private static int[][] leerGrafoDesdeArchivo(String nombreArchivo, int a) {
        int[][] grafo = null;
        int[][] grafonormal = null;
        int[][] grafolluvia = null;
        int[][] grafonieve = null;
        int[][] grafotormenta = null;
        int inf = 9999999;
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            List<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

            List<String> lineas2 = new ArrayList<>();

            String ciudadpartida = "";

            int lad = 1;

            for(String l : lineas){

                String s = l;
                

                int i = s.indexOf(",");

                if(ciudadpartida.equals(s.substring(0, i).trim()) == false){
                    lad += 1;
                }

                ciudadpartida = s.substring(0, i).trim();
            }

            grafonormal = new int[lad][lad];
            grafolluvia = new int[lad][lad];
            grafonieve = new int[lad][lad];
            grafotormenta = new int[lad][lad];

            grafonormal[lad-1][lad-1] = inf;
            grafolluvia[lad-1][lad-1] = inf;
            grafonieve[lad-1][lad-1] = inf;
            grafotormenta[lad-1][lad-1] = inf;

            int x = -1;
            int z = 0;
            String cpartida = "";

            for(String l : lineas){
                
                String s = l;

                int i = s.indexOf(",");

                if(cpartida.equals(s.substring(0, i).trim()) == false){

                    x += 1;
                    z = x+1;

                    if(z > lad-1){
                        z = 0;
                    }
                    if(z == x){
                        z += 1;
                    }

                } else {

                    z += 1;

                    if(z > lad-1){
                        z = 0;
                    }
                    if(z == x){
                        z += 1;
                    }

                }
                //Ciudud-partida
                cpartida = s.substring(0, i).trim();
                s = s.replaceFirst(s.substring(0, i+1).trim(), "");
                //Ciudad-destino
                i = s.indexOf(",");
                s = s.replaceFirst(s.substring(0, i+1).trim(), "");
                //Tiempo-normal
                i = s.indexOf(",");
                int tnormal = Integer.parseInt(s.substring(0, i).trim());
                s = s.replaceFirst(s.substring(0, i+1).trim(), "");
                grafonormal[x][x] = inf;
                grafonormal[x][z] = tnormal;
                //Tiempo-lluvia
                i = s.indexOf(",");
                int tlluvia = Integer.parseInt(s.substring(0, i).trim());
                s = s.replaceFirst(s.substring(0, i+1).trim(), "");
                grafolluvia[x][x] = inf;
                grafolluvia[x][z] = tlluvia;
                //Tiempo-nieve
                i = s.indexOf(",");
                int tnieve = Integer.parseInt(s.substring(0, i).trim());
                s = s.replaceFirst(s.substring(0, i+1).trim(), "");
                grafonieve[x][x] = inf;
                grafonieve[x][z] = tnieve;
                //Tiempo-tormenta
                grafotormenta[x][x] = inf;
                grafotormenta[x][z] = Integer.parseInt(s);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch(a){
            case 1:{
                grafo = grafonormal;
            }
            case 2:{
                grafo = grafolluvia;
            }
            case 3:{
                grafo = grafonieve;
            }
            case 4:{
                grafo = grafotormenta;
            }
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
