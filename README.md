Pokedex App
Este proyecto es una aplicación móvil creada como parte de una prueba técnica utilizando la PokeAPI. La app permite consultar información sobre los Pokémon, aplicando principios de Clean Architecture y el patrón de diseño MVVM.

🛠️ Tecnologías utilizadas
Lenguaje: Kotlin
IDE: Android Studio
Arquitectura: Clean Architecture + MVVM
Base de datos local: Room
Backend: Consumo de la API pública PokeAPI
Notificaciones: Implementación de notificaciones locales y servicio en segundo plano.
🌟 Características principales
Consumo de la PokeAPI:
La app se conecta a la API para obtener información de los Pokémon, incluyendo detalles como nombre, imagen y URL.

Almacenamiento local con Room:
Los Pokémon descargados se guardan localmente en una base de datos Room para un acceso rápido y sin necesidad de conexión a internet.

Servicios en segundo plano:

Se ejecutan procesos en segundo plano para mantener actualizados los datos de los Pokémon.
Notificaciones locales para alertar al usuario sobre actualizaciones.
Repositorio centralizado:

El repositorio extrae los datos de la API o de la base de datos Room, según sea necesario.
Esto permite desacoplar las fuentes de datos y mejorar la escalabilidad del proyecto.
Diseño modular:

La app está organizada de manera modular y sigue los principios de Clean Architecture para mantener separación de responsabilidades.
Se hace uso del patrón MVVM para manejar la interacción entre UI, lógica de presentación y lógica de negocio.
