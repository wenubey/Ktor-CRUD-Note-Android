# CRUD Note App with Ktor and Android

This Android app demonstrates a simple CRUD (Create, Read, Update, Delete) operation for notes. It uses Ktor as the HTTP client for network operations and follows MVVM architecture for separating the business logic from the UI. The app showcases the use of Kotlin Coroutines for asynchronous tasks and LiveData for observing data changes.

## Backend Repository

The backend for this project is handled by a separate Ktor server. You can find the source code and setup instructions for the backend part at the following GitHub repository:

[CRUD Note App Backend Repository](https://github.com/wenubey/Ktor-CRUD-Note-Server)

Please ensure that the backend server is running before using the Android app to perform CRUD operations.

## Features

- View a list of notes
- Add a new note
- Update an existing note
- Delete a note

## Visualization

This section provides a visual representation of the app's architecture, flow, and UI design. Understanding the app's structure and user interface can help contributors and users grasp the functionality and design principles more effectively.

### Architecture Diagram

- **Android Application (UI Layer):** This includes Activities (MainActivity), Fragments, and Views that interact with the user.
- **ViewModel (ViewModel Layer):** Manages UI-related data, handles UI events (NoteViewModel), and communicates with the Service Layer.
- **Service Layer (Data Layer):** Interfaces and implementations that handle data operations, such as NoteServiceImpl, which interacts with the backend server using Ktor.
- **Data Source (Remote):** Ktor Client making HTTP requests to a backend server.
- **Backend Server:** The server that provides RESTful endpoints for CRUD operations on notes.

### UI Screenshots

Screenshots of the app's main screens can give a quick overview of its user interface and features.

<div style="display:flex">
    <img src="https://github.com/wenubey/Ktor-CRUD-Note-Android/blob/main/app/src/main/assets/screenshot-1.png" alt="not found" width="240" height="600">
    <img src="https://github.com/wenubey/Ktor-CRUD-Note-Android/blob/main/app/src/main/assets/screenshot-2.png" alt="not found" width="240" height="600">
    <img src="https://github.com/wenubey/Ktor-CRUD-Note-Android/blob/main/app/src/main/assets/screenshot-3.png" alt="not found" width="240" height="600">
</div>

### Flow Diagram

- **Start:** User opens the app.
- **View Notes List:** The main screen showing a list of notes fetched using the getAllNotes() function.
- **Add Note:** User initiates adding a note, which triggers addNote() in the ViewModel.
- **View Note Details:** User selects a note to view its details.
- **Update Note:** User updates the note, calling updateNote() in the ViewModel.
- **Delete Note:** User deletes a note, invoking deleteNote() in the ViewModel.
- **End:** User exits the app or navigates away from the current operation.

## Tech Stack

- Kotlin: Primary programming language
- Android SDK: For the UI and application components
- Ktor: For network operations
- LiveData and ViewModel: For UI data management
- Koin: For dependency injection

## Getting Started

### Prerequisites

- Android Studio Arctic Fox | 2020.3.1 or later
- Kotlin Plugin
- Gradle version 7.0.2 or higher

### Installation

1. Clone the repository:

```shell
git clone https://github.com/yourusername/crudnoteappviewui.git
```
2. Open the project in Android Studio:
   Open Android Studio and select "Open an Existing Project", then navigate to the directory where you cloned the repository.

### Build and Run the Application

1. **Sync the project with Gradle files**: Android Studio should automatically prompt you to sync the project with Gradle files. If it doesn't, you can trigger a sync manually by clicking on `File > Sync Project with Gradle Files`.

2. **Run the app**: To run the app, select a target Android device or emulator and click the run button (`Shift + F10` on Windows/Linux, `Control + R` on macOS).

### Structure

The project is structured as follows:

- **data**: Contains the data access layer, including the data transfer objects (DTOs) and the service implementation for making network requests using Ktor.

- **ui**: Contains the UI components, including activities, view models, and state management for the application.

- **domain**: Includes domain models that represent the core data of the app, separate from the data layer models.

### Key Components

- **Ktor Client**: Used for making network requests to a backend service.
- **ViewModel**: Manages UI-related data in a lifecycle-conscious way.
- **LiveData**: Used for observing changes in data and updating the UI accordingly.
- **Koin**: Provides dependency injection to supply the required objects in the application.

### Dependencies

This project uses the following main dependencies:

- Ktor for network operations
- Kotlin Coroutines for asynchronous programming
- AndroidX for MVVM architecture components (ViewModel, LiveData)
- Koin for dependency injection
- Android Navigation Component for managing UI navigation

For a full list of dependencies, refer to the `build.gradle` (app level) file in the project.

## Usage

The app allows users to perform CRUD operations on notes:

- **Create**: Add a new note by entering the title and description.
- **Read**: View a list of all notes or view details of a specific note.
- **Update**: Edit the title or description of an existing note.
- **Delete**: Remove a note from the list.

## Acknowledgements

- Ktor for providing an easy way to build asynchronous clients
- Android Architecture Components for a robust app architecture
- Kotlin for the concise and expressive syntax

