**_<h1 align="center">:vulcan_salute: Proyecto Realizado con Andoid Studio dividido en Etapas y concluye como NVP de App de Seguridad  Vecinal:computer:</h1>_**

**<h3>:blue_book: Descripción de la Problemática Abordada:</h3>**

<p><b>Contexto</b></p>
<p>En comunidades residenciales como villas y condominios, existe una necesidad crítica de sistemas de alerta temprana que permitan a los vecinos comunicar situaciones de emergencia de manera rápida y efectiva. Las soluciones comerciales actuales presentan limitaciones significativas:</p>
<p>Alto costo económico: Las aplicaciones profesionales de seguridad tienen tarifas mensuales elevadas que resultan inaccesibles para comunidades completas.</p>
<p><b>Capacidad limitada de usuarios:</b> Los planes disponibles restringen el número de cuentas, lo que imposibilita su implementación en villas grandes (como la nuestra, con más de 1,300 viviendas).
Falta de personalización: Las soluciones genéricas no se adaptan a las necesidades específicas de cada comunidad.</p>
<p><b>Tiempo de respuesta:</b> En emergencias médicas o delitos en curso, cada segundo cuenta. Se requiere un sistema que permita activación inmediata sin necesidad de desbloquear el teléfono o navegar por múltiples menús.</p>

<p><b>Problemática Específica</b></p>
<p>Los residentes de nuestra villa necesitan:</p>

1. Un método instantáneo para reportar emergencias médicas y delitos
2. Un sistema que geolocalice automáticamente al usuario en emergencia
3. Notificaciones inmedia tas a todos los vecinos de la comunidad
3. Una plataforma accesible económicamente para toda la villa
4. Un sistema que identifique quién y dónde está ocurriendo la emergencia

**<h3>:orange_book: Solución Propuesta:</h3>**

<p><b>Emergencia Vecinal</b> es una aplicación móvil Android desarrollada en Kotlin que permite a los miembros de una comunidad reportar y recibir alertas de emergencias en tiempo real, facilitando la ayuda mutua entre vecinos.</p>
<p>El proyecto se va a entregar en etapas y con el avance de cada una se documentara acá</p>

**<h3>📁 Estructura del Proyecto Android:</h3>**

```Android
📘 README.md
📁 app/src/main/
├── 🟧 AndroidManifest.xml
├── 📁 java
│   ├── 📁 com.example.holamundo
│   │   ├── 📁 ui
│   │   │   └── 📁 theme
│   │   │    ├── 🟦 Color.kt
│   │   │    ├── 🟦 Theme.kt
│   │   │    └── 🟦 Type.kt
|   |   ├── 🟦 DeveloperProfile.kt
|   |   ├── 🟦 KotlinFeaturesDemo.kt
│   │   └── 🟦 MainActivity.kt
│   ├── 📁 com.example.holamundo (android Test)
│   │   └── 🟦 ExampleInstrumentedTest.java
│   └── 📁 com.example.holamundo (test)
│       └── 🟦 ExampleUnitTest.java
├── 📁 java (generated)
├── 📁 res
│   ├── 📁 drawable
│   │   │    ├── 🖼️ hello_world_cellphone.png
│   │   │    ├── 🖼️ image_project.jpg
│   │   │    ├── 🖼️ world.jpg
│   │   │    ├── 🟧 ic_launcher_background.xml
│   │   │    └── 🟧 ic_launcher_foreground.xml
│   ├── 📁 layout
│   │   └── 🟧 activity_main.xml
│   ├── 📁 mipmap
│   │   ├── 📁 ic_launcher
│   │   └── 📁 ic_launcher_round
│   ├── 📁 values
│   │   ├── 📁themes
│   │   │   ├── 🟧 themes.xml
│   │   │   └── 🟧 themes.xml (night)
│   │   ├── 🟧 colors.xml
│   │   └── 🟧 strings.xml
│   └── 📁 xml
📁 Gradle Scripts
├── 🟦 build.gradle.kts (Project: HolaMundo)
├── 🟦 build.gradle.kts (Module: app)
├── 🟦 proguard-rules.pro (ProGuard rules for ":app")
├── 🟦 gradle.properties (Project properties)
├── 🟦 gradle-wrapper.properties (Gradle Version)
├── 🟦 libs.versions.toml (version Catalog "libs")
├── 🟦 local.properties (SDK Location)
└── 🟦 settings.gradle.kts (Project Settings)
```

**<h3>:book: Imagen general del proyecto:</h3>**

<img src="./app/src/main/res/drawable/emulador_etapa_1_img1.jpg" alt="Proyecto Vista General etapa 1" style="width: 80%;">

**<h3>:book: Capturas de pantalla de la interfaz de usuario final:</h3>**

<img src="./app/src/main/res/drawable/emulador_etapa_1_img2.jpg" alt="Vista Celular etapa 1" style="width: 40%;">