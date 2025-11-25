# Roadmap.

Tengo hasta el 3/12. El 1/12 tiene que estar todo terminado.

Tengo exactamente dos fines de semana, uno de ellos largo.


Plan para las slides.

Presentación del chapter. 
"Un ejemplo práctico integrando varios temas vistos recientemente."

Primero, la idea de desarrollo.

Quiero hacer un juego tipo asteroids.

Donde dos jugadores puedan jugar online un juego sencillo de naves.

Que necesitamos:

Descartado: Un frontend. pero eso no es lo importante este chapter.

Pero también: Un backend que se comunique con este frontend y vaya recompilando los eventos del frontend y sincronizando el juego entre los dos jugadores.

Este chapter trata sobre esa parte. Vamos a hacer el backend compilando tecnologías vistas en chapters anteriores:

Para comunicarnos con el front: Vamos a usar websockets.

Para modelar el backend: Vamos a usar programaciòn reactiva.

Para hacer la lógica: Vamos a usar Scala.

Vos a centrarme, por la naturaleza del chapter, en los websockets y la programaciòn reactiva.

Websockets. Update de HTTP para conseguir programación realmente asincŕonica con el backend. Tiene sus limitaciones.

Sobre websockets: 
Explicar:
  - Funcionamiento
  - Limitaciones (UDP y un websocket por pestaña)

Programación reactiva: Idea de programación en la que existen productores y consumidores de eventos sincronizados por publishres y consumers.

Sobre programación reactiva:
  - Sirve para sincronizar clientes (explicar otros casos de uso)
  - Explicar que parte necesitamos (para publicar keypresses y )

Lógica: Se escribe una librerìa en scala que pueda ser invocada independientemente del backend. En este chapter lo muestro por encima para pero son las mismas ideas que el chapter anterior.

