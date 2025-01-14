# KDraw

A flexible drawing application built using Kotlin Multiplatform and Compose Multiplatform. This app provides advanced drawing capabilities and supports multiple platforms, including Android and Desktop.

## Features

- **Real-Time Drawing:** Smooth and responsive drawing experience with stylus and touch input support.
- **Zoom & Pan:** Pinch-to-zoom and drag gestures for precise navigation on the canvas.
- **Selection & Manipulation:** Select and move strokes or points individually or in groups.
- **Customizable Canvas:** Switch between grid, dotted, or blank canvas modes.
- **Local Storage:** Drawings are saved locally using SQLite (Room) for persistent storage.
- **Cross-Platform Compatibility:** Seamless functionality on both Android and Desktop platforms.

## Requirements

- **Android**: Android 7.0 (API 24) or higher.
- **Desktop**: Windows, macOS, or Linux with JBR (JetBrains Runtime) 17 installed.
- **Development Environment**:
  - Android Studio.
  - Kotlin version 2.1.0.

## Installation

### Clone the Repository

```bash
git clone https://github.com/RicardoDB96/KDraw.git
cd KDraw
```

### Android

1. Open the project in Android Studio.
2. Wait for the project to sync.
3. Connect an Android device or start an emulator.
4. Select the `composeApp` module and click **Run**.

### Desktop

1. Open the project in Android Studio.
2. Wait for the project to sync.
3. Run the desktop application target with following line:

```
./gradlew composeApp:run
```

## Key Features to Try

### 1. Drawing

- **How to Use**: Open the app, create a new drawing, switch to drawing mode, and use your finger, mouse, or stylus to draw on the canvas. Tap or click to create points, or drag to create continuous lines, you can change of color and width by clicking again on drawing mode when is selected.

### 2. Zoom & Pan

- **How to Use**: Use the zoom control on the bottom left or select drag mode on the toolbar. On Android, pinch to zoom in or out and drag to pan. On Desktop, hold the control key and scroll the mouse wheel to zoom, or click and drag to pan.

### 3. Selection Mode

- **How to Use**: Choose selection mode from the toolbar. Click and drag to create a selection box around the strokes or points you want to select. Once selected, you can move them by dragging within the selection box.

### 4. Local Storage

- **How to Use**: Your drawings are saved automatically to local storage using SQLite. Close and reopen the app to verify that your work is still available.

## Project Structure

The project follows the MVVM (Model-View-ViewModel) architecture to ensure a clear separation of concerns and maintainability:

- **Data Layer**: Handles local storage and data persistence using SQLite (Room).
- **Domain Layer**: Contains business logic and use cases for the app.
- **UI Layer**: Includes platform-specific implementations and Compose Multiplatform UI components.
- **ViewModel**: Manages the app's state and communicates between the UI and domain layers.
- **Dependency Injection**: Koin is used for dependency management, ensuring modularity and ease of testing.

## License

```
Copyright 2025 ribod (Ricardo DB)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
