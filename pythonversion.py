import networkx as nx

INF = 9999999  # Valor infinito para representar la ausencia de conexión

def main():
    grafo = leer_grafo_desde_archivo("grafo.txt")
    num_vertices = obtener_numero_vertices(grafo)
    distancias = aplicar_algoritmo_floyd_warshall(grafo, num_vertices)

    # Imprimir el grafo
    print("Contenido del grafo:")
    imprimir_grafo(grafo)

    # Aquí puedes agregar tus opciones adicionales
    ciudad_origen = input("Ingrese el nombre de la ciudad origen: ")
    ciudad_destino = input("Ingrese el nombre de la ciudad destino: ")

    indice_origen = obtener_indice_ciudad(ciudad_origen, grafo)
    indice_destino = obtener_indice_ciudad(ciudad_destino, grafo)

    if indice_origen == -1 or indice_destino == -1:
        print("Una o ambas ciudades no existen en el grafo.")
    else:
        distancia_minima = distancias[indice_origen][indice_destino]
        if distancia_minima == INF:
            print("No hay ruta disponible entre las ciudades seleccionadas.")
        else:
            print("La ruta más corta entre", ciudad_origen, "y", ciudad_destino, "es:")
            print("Distancia:", distancia_minima)
            print("Ciudades intermedias:", end=" ")
            encontrar_ciudades_intermedias(indice_origen, indice_destino, distancias, grafo)

def leer_grafo_desde_archivo(nombre_archivo):
    grafo = nx.DiGraph()
    with open(nombre_archivo) as archivo:
        for linea in archivo:
            partes = linea.strip().split(",")
            ciudad_origen = partes[0]
            ciudad_destino = partes[1]
            tiempo_normal = int(partes[2])
            tiempo_lluvia = int(partes[3])
            tiempo_nieve = int(partes[4])
            tiempo_tormenta = int(partes[5])

            grafo.add_edge(ciudad_origen, ciudad_destino, tiempo_normal=tiempo_normal)
            grafo.add_edge(ciudad_origen, ciudad_destino, tiempo_lluvia=tiempo_lluvia)
            grafo.add_edge(ciudad_origen, ciudad_destino, tiempo_nieve=tiempo_nieve)
            grafo.add_edge(ciudad_origen, ciudad_destino, tiempo_tormenta=tiempo_tormenta)

    return grafo

def obtener_numero_vertices(grafo):
    return len(grafo.nodes())

def obtener_indice_ciudad(ciudad, grafo):
    for indice, nodo in enumerate(grafo.nodes()):
        if nodo == ciudad:
            return indice
    return -1

def aplicar_algoritmo_floyd_warshall(grafo, num_vertices):
    matriz_distancias = nx.floyd_warshall_numpy(grafo, weight='tiempo_normal')
    return matriz_distancias

def encontrar_ciudades_intermedias(indice_origen, indice_destino, distancias, grafo):
    camino_minimo = nx.shortest_path(grafo, source=indice_origen, target=indice_destino, weight='tiempo_normal')
    ciudades_intermedias = camino_minimo[1:-1]  # Excluir el origen y el destino

    for indice_ciudad in ciudades_intermedias:
        nombre_ciudad = grafo.nodes[indice_ciudad]
        print(nombre_ciudad, end=" ")
    print()

def imprimir_grafo(grafo):
    for u, v, datos in grafo.edges(data=True):
        print(f"Ciudad origen: {u}, Ciudad destino: {v}, Tiempo normal: {datos['tiempo_normal']}, Tiempo lluvia: {datos['tiempo_lluvia']}, Tiempo nieve: {datos['tiempo_nieve']}, Tiempo tormenta: {datos['tiempo_tormenta']}")

if __name__ == "__main__":
    main()
