# Implementation

- ## Run:

```bash
./gradlew.bat run
```

- ## Build:

```bash
./gradlew build
```

- ## Test:

```bash
./gradlew test
```

- ## Clean:

```bash
./gradlew clean
```

# Project Structure

```bash
src/
└── main/
    ├── java/
    │   └── yourpackage/
    │       ├── Main.java                <-- จุดเริ่มรัน JavaFX
    │       │
    │       ├── jfx/                     <-- UI, Controller, Drawing
    │       │   ├── MainController.java
    │       │   ├── GridRenderer.java
    │       │   ├── AnimationRenderer.java
    │       │   └── ColorTheme.java
    │       │
    │       ├── algorithm/   
    │       │   ├── BFS.java
    │       │   ├── Dijkstra.java
    │       │   ├── AStar.java
    │       │   └── GA/
    │       │       ├── GeneticAlgorithm.java
    │       │       └── Chromosome.java
    │       │
    │       ├── model/
    │       │   ├── Grid.java
    │       │   ├── Node.java
    │       │   └── PathResult.java
    │       │
    │       ├── io/ 
    │       │   ├── MapLoader.java
    │       │   └── SaveManager.java
    │       │
    │       └── utils/
    │           ├── TimerUtil.java
    │           └── RandomUtil.java
    │
    └── resources/
        ├── yourpackage/
        │   └── main.fxml                 <-- UI layout
        └── application.css               <-- theme/style
```
