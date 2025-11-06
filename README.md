# Krono
## 🚀 Simple & Minimalist Task Tracker in Kotlin & Compose Multiplatform

This is a **Kotlin Multiplatform** project targeting **Android, iOS, and Desktop (JVM)**.

---

### ✨ Project Scope and Goals

The primary goal of this application is to provide a **very simple and minimal tool for tracking the time dedicated to each task (Time Tracking)**.

Concurrently, the project serves as a **"side project"** to offer a **solid base and a practical example** of a well-architected application using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)**.

---

### 🛠️ Architecture and Technology Stack

The project demonstrates the use of a **modern and robust stack** for KMP/CMP development, implementing the following tools and patterns:

#### 💻 Core Technologies

* **Kotlin:** The primary programming language for shared and platform-specific logic.
* **Compose Multiplatform (CMP):** Used to build a **single, declarative, and reactive user interface** across all target platforms.
* **Kotlin Multiplatform (KMP):** Enables **code sharing** of the business logic, data layer, and ViewModels between Android, iOS, and Desktop.

#### ⚙️ Data & State Management

* **MVI (Model-View-Intent) Pattern:** The architecture adopted for reactive and unidirectional state management.
* **Coroutines & Flow:** Used for asynchronous operation management and reactive data propagation.
* **Koin:** The dependency injection (DI) framework for modular and scalable component management.
* **Room:** For local data persistence.

---

### 🎯 Target Platforms

| Platform | Status |
| :--- | :--- |
| **Android** | ✅ Supported |
| **iOS** | ✅ Supported |
| **Desktop (JVM)** | ✅ Supported |

---

## TODO

### Core Implementation
- [x] Project setup and configuration
- [x] Database implementation
- [x] Business logic implementation
- [x] UI implementation
- [x] Save data on app close

### Platform Support & Improvements
- [ ] Android
- [ ] iOS
- [x] Desktop

### Distribution
- [ ] Add build instructions to README

### Features
- [ ] Activity log with timestamps for all task starts
- [ ] Add manual notes for each task session
- [ ] Pause all tasks when new has started
- [ ] Right-click on icon shows last 5 tasks
- [x] Confirmation dialog for deletion
- [ ] Global shortcuts for task control
- [ ] Intercept close shortcut to save database
- [ ] Auto-scroll to top when adding new task
- [ ] Localization/i18n support

### Improvements
- [ ] Improve UI/UX
- [ ] Implement tests

---

### 🤝 Contributing

If you find the project useful and would like to contribute, feel free to open an **Issue** for bugs or suggestions, or a **Pull Request** for changes and improvements!

<img width="912" height="712" alt="krono_desktop" src="https://github.com/user-attachments/assets/cd68ce5d-f6da-47e5-9dfc-478122ca70ea" />
