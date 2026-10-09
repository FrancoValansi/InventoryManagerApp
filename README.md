# Inventory Manager

Aplicación móvil de gestión de inventarios desarrollada con Kotlin Multiplatform, Jetpack Compose y Supabase, como parte de un challenge técnico de desarrollo de software.

El objetivo del proyecto es ofrecer una solución para administrar inventarios y sus productos, con persistencia de datos, autenticación de usuarios y una arquitectura modular que facilite el mantenimiento y la incorporación de nuevas funcionalidades.

## Funcionalidades

* **Autenticación:** registro, inicio y cierre de sesión.
* **Gestión de inventarios:** creación, listado y eliminación de inventarios.
* **Gestión de productos:** creación, consulta, edición y eliminación de productos.
* **Validaciones:** control de campos obligatorios, precios y cantidades de stock.
* **Navegación:** acceso a los productos de un inventario y regreso al listado.
* **Persistencia:** almacenamiento de datos mediante Supabase.

## Tecnologías utilizadas

* **Kotlin:** lenguaje principal.
* **Kotlin Multiplatform:** estructura del proyecto orientada al desarrollo multiplataforma.
* **Jetpack Compose y Material 3:** construcción declarativa de la interfaz.
* **Coroutines:** ejecución de operaciones asíncronas.
* **ViewModel:** administración del estado y la lógica de presentación.
* **Supabase Auth:** autenticación de usuarios.
* **Supabase PostgreSQL:** persistencia de los datos.
* **Git y GitHub:** control de versiones.

## Arquitectura

El proyecto utiliza una separación por capas y responsabilidades, buscando reducir el acoplamiento entre la interfaz, la lógica de presentación y el acceso a los datos.

### 1. Capa de presentación

Ubicada en `presentation/`, contiene las pantallas, los componentes visuales reutilizables y la lógica necesaria para representar e interactuar con los datos.

* `auth/`: registro e inicio de sesión.
* `inventory/`: listado, creación y eliminación de inventarios.
* `product/`: gestión de productos.
* `components/`: componentes de interfaz compartidos, como campos de formulario y diálogos de confirmación.

Las pantallas se construyen con Jetpack Compose y reaccionan a los cambios de estado para actualizar la interfaz.

### 2. Capa de lógica de presentación

Los `ViewModel` coordinan las acciones de las pantallas, ejecutan operaciones asíncronas y comunican los resultados a la interfaz.

Por ejemplo, `InventoryViewModel` administra las operaciones relacionadas con los inventarios, mientras que `AuthViewModel` coordina el registro y el inicio de sesión.

Esta separación evita concentrar toda la lógica dentro de los componentes visuales y facilita el mantenimiento y las pruebas.

### 3. Capa de acceso a datos

Ubicada en `data/`, se encarga de representar los datos y centralizar las operaciones de acceso a Supabase.

* `model/`: define las entidades principales, como `Inventory` y `Product`.
* `repository/`: centraliza las operaciones de autenticación y gestión de datos.
* `remote/`: contiene la configuración del cliente de Supabase.

Los repositorios actúan como intermediarios entre la lógica de presentación y el servicio de datos. De esta manera, las pantallas no necesitan implementar directamente las consultas a la base de datos.

### 4. Base de datos y servicios

Supabase proporciona autenticación y una base de datos PostgreSQL para persistir los perfiles, inventarios y productos.

Las relaciones entre las tablas permiten asociar cada inventario con su propietario y cada producto con su inventario correspondiente.

Las políticas de seguridad a nivel de fila (RLS) permiten definir qué registros puede consultar o modificar cada usuario autenticado.

## Componentes reutilizables

Se prioriza la reutilización de componentes y la centralización de responsabilidades para evitar duplicaciones y simplificar futuras modificaciones.

Por ejemplo:

* **`FormField`:** permite reutilizar campos de entrada en distintos formularios, manteniendo una presentación consistente.
* **`ConfirmationDialog`:** centraliza los diálogos de confirmación, como los utilizados antes de eliminar un inventario.
* **Repositorios:** agrupan las operaciones de acceso a datos y evitan repetir consultas en distintas pantallas.
* **Modelos de datos:** permiten representar y transportar la información mediante estructuras comunes.
* **ViewModel:** separa la gestión del estado y las operaciones de la interfaz visual.

Esta organización facilita agregar nuevos formularios, entidades y operaciones sin tener que reconstruir desde cero los componentes existentes.

## Modelo de datos

La aplicación utiliza tres tablas principales:

* **`profiles`:** almacena el perfil de cada usuario, identificado mediante el ID de autenticación.
* **`inventories`:** almacena los inventarios y su propietario mediante `owner_id`.
* **`products`:** almacena los productos asociados a cada inventario mediante `inventory_id`.

Las relaciones entre estas entidades permiten mantener la integridad de los datos y establecer quién es propietario de cada inventario.

## Soporte para múltiples usuarios y evolución futura

La estructura de datos contempla la incorporación de múltiples usuarios. Cada perfil se relaciona con un usuario de Supabase Auth, y cada inventario tiene un propietario identificado mediante `owner_id`.

Esto permite evolucionar desde una aplicación de uso individual hacia un sistema en el que diferentes usuarios administren sus propios inventarios de forma independiente.

Como evolución futura, se plantea:

* **Aislamiento de datos por usuario:** aplicar y verificar políticas RLS para garantizar que cada usuario solo pueda acceder a los inventarios que le corresponden y a los productos de esos inventarios.
* **Personalización del perfil:** utilizar el nombre o apodo almacenado en `profiles` para personalizar la experiencia.
* **Compartir inventarios:** permitir que varios usuarios accedan al mismo inventario mediante un modelo de permisos o una tabla de membresías.
* **Roles y permisos:** diferenciar acciones según el nivel de acceso, por ejemplo, administrador, editor o lector.
* **Ampliación del dominio:** incorporar categorías, proveedores, movimientos de stock o reportes sin concentrar toda la lógica en las pantallas.

El soporte para múltiples usuarios requiere no solo relaciones entre tablas, sino también políticas de autorización correctamente definidas y probadas en la base de datos.

## Configuración y ejecución

1. Clonar el repositorio:

   ```bash
   git clone URL_DEL_REPOSITORIO
   ```

2. Abrir el proyecto con Android Studio y sincronizar las dependencias de Gradle.

3. Configurar un proyecto de Supabase con las tablas, relaciones y políticas de seguridad necesarias.

4. Configurar la URL y la clave pública correspondiente en el cliente de la aplicación. No incluir claves secretas ni credenciales privadas en el repositorio.

5. Ejecutar la aplicación en un emulador Android o dispositivo compatible.

## Seguridad

La autenticación se gestiona mediante Supabase Auth. El control de acceso a los registros debe implementarse y verificarse mediante políticas RLS en PostgreSQL.

La aplicación no debe depender únicamente de las validaciones de la interfaz para proteger los datos: las restricciones de acceso deben aplicarse también en el servidor.

## Autor

**Franco Agustín Valansi Miraglia**

Estudiante de Ingeniería en Informática en la Universidad Nacional de La Matanza (UNLaM).
