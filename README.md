# franchise-app

Aplicación Spring Boot con WebFlux, Gradle, Java 17, que se conecta a una base de datos MongoDB Atlas y construida con Arquitectura Hegaxonal.

## Descripción

Este proyecto es una aplicación web construida con Spring Boot, WebFlux, Gradle y Java 17 y Arquitectura Hegaxonal, diseñada para interactuar con una base de datos MongoDB Atlas. La aplicación no requiere una base de datos local ya que utiliza un servicio MongoDB Atlas en la nube.

## Requisitos Previos

Para ejecutar esta aplicación localmente, necesitarás tener instalado lo siguiente:

*   **Java 17:**  Verifica con `java -version` en tu terminal.
*   **Gradle:** Verifica con `gradle -v` en tu terminal.
*   **Docker (opcional):**  Necesario solo si quieres ejecutar la aplicación en un contenedor Docker. Verifica con `docker -v` en tu terminal.
*   **Cuenta de Docker Hub (opcional):**  Necesaria solo si quieres desplegar la aplicación en Docker y ya tienes la imagen publicada.

## Despliegue Local

Hay dos formas de desplegar la aplicación localmente: directamente con Gradle o usando Docker.

### Despliegue con Gradle

1.  **Clonar el repositorio:**

    ```bash
    git clone [https://github.com/AngelGiraldoS/franchise-app.git](https://github.com/AngelGiraldoS/franchise-app.git)
    cd franchise-app
    ```


2.  **Ejecutar la aplicación:**

    ```bash
    ./gradlew bootRun
    ```

    Esto iniciará la aplicación en el puerto 5000 (por defecto). Puedes acceder a la aplicación en tu navegador a través de la URL `http://localhost:5000`.

### Despliegue con Docker

1.  **Clonar el repositorio (si aún no lo has hecho):**

    ```bash
    git clone [https://github.com/AngelGiraldoS/franchise-app.git](https://github.com/AngelGiraldoS/franchise-app.git)
    cd franchise-app
    ```

2.  **Descargar la imagen Docker desde Docker Hub:**

    ```bash
    docker pull annonymousdo/franchise:latest
    ```
    También hay otras tags que puedes utilizar
    ```
    docker pull annonymousdo/franchise:latest
    ```

3.  **Ejecutar el contenedor Docker:**

    ```bash
    docker run -p 5000:5000 annonymousdo/franchise:latest
    ```

    Esto iniciará la aplicación dentro de un contenedor Docker y mapeará el puerto 5000 del contenedor al puerto 5000 de tu máquina local. Puedes acceder a la aplicación en tu navegador a través de la URL `http://localhost:5000`.

    **Nota:**  La configuración de la base de datos MongoDB Atlas ya está incluida en la imagen Docker, por lo que no es necesario configurarla manualmente cuando se utiliza este método. Si deseas cambiarla, considera usar variables de entorno al ejecutar el contenedor con `docker run -e "SPRING_DATA_MONGODB_URI=tu_nueva_uri" ...`.

## Repositorio

*   **Código fuente:** [https://github.com/AngelGiraldoS/franchise-app](https://github.com/AngelGiraldoS/franchise-app)
*   **Imagen Docker:** [https://hub.docker.com/r/annonymousdo/franchise/tags](https://hub.docker.com/r/annonymousdo/franchise/tags)

## Revisiones

La app está desplegada en una maquina ec2, se adjunta en la respuesta la collecion de postman donde estan expuestos todas los endpoint solicitados. 

