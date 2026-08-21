# Auth Client SDK (`client-sdk`)

SDK de cliente y biblioteca de integración para comunicación **Service-to-Service (M2M)** en arquitecturas de microservicios distribuidas con **Spring Boot 3** y **Spring Cloud OpenFeign**. 

Automatiza la autenticación máquina a máquina, el ciclo de vida de tokens de servicio (Service Bots) y la propagación transparente del header `Authorization: Bearer <token>` en todas las peticiones entre microservicios.

---

## Características Principales

* **Autenticación M2M Automatizada:** Gestión autónoma de credenciales de bots de servicio bajo roles RBAC de solo lectura para consumo inter-servicio seguro.
* **Auto-Renovación de Tokens en Memoria:** `BotAuthService` valida la vigencia del JWT con la clave pública antes de cada petición; si el token expiró o es nulo, solicita uno nuevo al servicio central de Auth de forma transparente.
* **Interceptor Feign Transparente:** `FeignInterceptorConfig` inyecta automáticamente la cabecera `Authorization: Bearer <token>` en todas las llamadas salientes, omitiendo rutas de login para evitar bucles.
* **Módulos Feign Declarativos:** Clientes predefinidos (`LoginClient`, `UsuarioClient`) listos para ser inyectados en lógica de negocio.
* **Zero-Config con Spring AutoConfiguration:** Integra `@AutoConfiguration`, `@ComponentScan` y `@EnableFeignClients` para que cualquier microservicio consumidor solo deba importar el SDK sin configuraciones manuales de beans.
* **DTOs Inmutables con Java Records:** Modelos de datos estructurados con `@Builder` y `record` (`LoginRequest`, `TokenResponse`, `UsuarioAuthDto`).

---

## Stack Tecnológico

* **Lenguaje:** Java 17+
* **Framework:** Spring Boot 3.x, Spring Cloud OpenFeign
* **Librerías Core:** `feign-core`, `lombok`, `com.ayuntamiento:security-lib`

---

##  Instalación & Configuración

### 1. Agregar la dependencia (Maven)

```xml
<dependency>
    <groupId>com.auth</groupId>
    <artifactId>client-sdk</artifactId>
    <version>1.0.0</version>
</dependency>

2. Configurar Propiedades del Consumidor (application.properties)
El microservicio consumidor únicamente debe declarar las credenciales del Bot de servicio y la URL del Gateway/Auth:

# Credenciales del Bot de Servicio (M2M)
bot.security.username=${BOT_SERVICE_USER:bot-gestion@ayuntamiento.gob.mx}
bot.security.password=${BOT_SERVICE_PASSWORD:secret-bot-password}

# Llave pública RSA para validación local del token
jwt.rsa.public-key=${JWT_PUBLIC_KEY}

# URL opcional si no se resuelve por Eureka directamente
app.feign.gateway.url=${GATEWAY_URL:}

Ejemplos de Uso
Consumo Declarativo de Usuarios entre Microservicios
Gracias a la autoconfiguración, basta con inyectar el cliente Feign directamente en cualquier @Service o @RestController:

package com.ayuntamiento.gestion.service;

import com.auth.client_sdk.client.UsuarioClient;
import com.auth.client_sdk.dto.UsuarioAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TramiteService {

    private final UsuarioClient usuarioClient;

    public void procesarTramite(Long usuarioId) {
        // La petición HTTP incluye automáticamente el Bearer Token del bot en los headers
        UsuarioAuthDto usuario = usuarioClient.obtenerUsuario(usuarioId);
        
        System.out.println("Procesando trámite para: " + usuario.nombre() + " (" + usuario.email() + ")");
    }

    public List<UsuarioAuthDto> listarPersonal() {
        return usuarioClient.obtenTodosEmpleados();
    }
}

Invalidación Manual de Token ante fallos

Si el microservicio recibe un 401 Unauthorized inesperado de un servicio remoto, puede solicitar la rotación inmediata del token:

@Autowired
private BotAuthService botAuthService;

public void reintentar() {
    botAuthService.forzarRenovacion();
}

Autor
Christopher Blanquet - Ingeniero en Sistemas Computacionales | Backend Developer

LinkedIn: linkedin.com/in/christopherblanquet/

GitHub: github.com/ChrisBlanquet



