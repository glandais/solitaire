# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Klondike Solitaire game implementation in Java with both console and GUI interfaces, featuring an AI solver using recursive state exploration with multi-threading support.

## Build Commands

```bash
# Compile and package (creates executable JAR with dependencies)
mvn clean package

# Run without packaging
mvn compile exec:java -Dexec.mainClass="io.github.glandais.solitaire.Main" -Dexec.args="<args>"

# Build executable JAR only
mvn shade:shade
```

## Running the Application

```bash
# Run from JAR after packaging
java -jar target/solitaire-1.0-SNAPSHOT.jar klondike <command>

# Available commands:
# - play: Interactive GUI mode
# - solve: AI solver mode
# - print: Display board state
# - debug: Debug mode
```

## Architecture

### Core Package Structure

- **`io.github.glandais.solitaire.common`**: Shared abstractions for solitaire games
  - `board/`: Core game state management (`Board`, `Pile`, `State`)
  - `cards/`: Card representations and enums
  - `move/`: Movement logic and scoring
  - `solver/`: AI solver with recursive state exploration and multi-threading

- **`io.github.glandais.solitaire.klondike`**: Klondike-specific implementation
  - Main game logic in `Klondike.java`
  - Pile types: `StockPile`, `TableauPile`, `FoundationPile`
  - `printer/`: Console and JavaFX GUI renderers
  - `serde/`: Serialization for saving/loading game states

### Key Design Patterns

1. **Command Pattern**: PicoCLI-based command structure for different game modes
2. **State Management**: Immutable board states with copy-on-write for solver exploration
3. **Solver Architecture**: 
   - Multi-threaded recursive exploration with `RecursiveSolitaireSolver`
   - State caching to avoid redundant exploration
   - Scoring system for move prioritization

### Important Classes

- `Main.java`: Entry point with PicoCLI command routing
- `Klondike.java`: Core game rules and movement validation
- `RecursiveSolitaireSolver.java`: AI solver with parallel exploration
- `KlondikeGuiApplication.java`: JavaFX GUI implementation
- `States.java`: Efficient state storage using byte arrays for deduplication

## Development Guidelines

### Adding New Features

- New game variants should implement the `Solitaire<T>` interface
- Pile types must extend `PileType<T>` for type safety
- Movements are validated through `getOrderedMovements()` method

### Performance Considerations

- Solver uses Caffeine cache for state memoization
- Board states are compressed to byte arrays for efficient comparison
- Multi-threading via AtomicInteger/AtomicLong for thread-safe counters

### GUI Development

- JavaFX-based with drag-and-drop card interactions
- Card images in `src/main/resources/images/`
- Vector2 class for position calculations
- PrintableCard handles rendering logic

## Dependencies

- **Java 21** required
- **Lombok**: Reduce boilerplate (provided scope)
- **JavaFX 21**: GUI framework
- **PicoCLI**: Command-line interface
- **Jackson**: JSON serialization
- **Caffeine**: High-performance caching
- **Jansi**: Console color output