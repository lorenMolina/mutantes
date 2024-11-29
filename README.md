# Parcial mutantes
## Descripción
Este proyecto implementa una API para detectar si una secuencia de DNA pertenece a un mutante o no. El sistema recibe una cadena de DNA y aplica reglas predefinidas para identificar patrones mutantes. 
Un ejemplo de mutante seria: { "dna": ["ATCCC","TTTAC","ACATG","CATTT","GGGGA"] } 
Un ejemplo de humano seria: { "dna": ["ATCCC","TTTAC","ACATG","CATTT","GAGGA"] }
## Tecnologías
- Java 22
- Spring Boot
- Gradle para la gestión de dependencias y automatización de compilaciones
- H2 Database (base de datos en memoria) para persistencia de datos
- JPA/Hibernate para manejo de la base de datos
- Mockito y JUnit para pruebas unitarias
- Docker Contenedores y despliegue
- Cloud Platform Render para el despliegue en la nube
## Instalación
Clona el repositorio:

```
git clone https://github.com/lorenMolina/mutantes.git
```

Posicionarse en el directorio del proyecto:

```
cd ./mutantes
```

Compilar el proyecto:

```
gradle build
```

Ejecuta el proyecto usando Gradle:

```
./gradlew bootRun
```
## Despliegue local o en Render

Puedes interactuar con la API en dos entornos:

#### Local
Desde `http://localhost:8080/`

JDBC URL: jdbc:h2:mem:testdb

Username: SA

Password:

#### Render:
Disponible en [https://mutantes-npwt.onrender.com](https://mutantes-npwt.onrender.com)
### Endpoints disponibles:

- **POST /mutant:** Verifica si una secuencia de ADN es mutante.[https://mutantes-npwt.onrender.com/mutant](https://mutantes-npwt.onrender.com/mutant)

```

{
  "dna": ["ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"]
}
```

- **GET /stats:** Devuelve estadísticas sobre las verificaciones de ADN (proporción de mutantes/humanos).[https://mutantes-npwt.onrender.com/stats](https://mutantes-npwt.onrender.com/stats)
#### Respuestas
- Si el ADN es de un mutante, se devuelve un código HTTP **200 OK**.
- Si el ADN pertenece a un humano, se devuelve un código HTTP **403 Forbidden**.