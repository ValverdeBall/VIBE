# VIBE

**VIBE — Valverde's Integrated Build Environment**

> Want to code on Android? Don't worry! Try VIBE!

VIBE is a modern, extensible development environment for Android.

The goal is simple: make serious software development on Android less painful.

No "Android IDE #482", no ancient-looking UI, no forcing every language into the same workflow.

VIBE is designed to be a flexible development environment with support for multiple programming languages, toolchains and language servers.

## Features

- Modern code editor
- Jetpack Compose + Material 3 UI
- LSP support
- Extensible language support
- Native Android/AArch64 toolchains
- External compiler and build-tool integration
- Designed specifically for development on Android
- Works with Android-native/Bionic toolchains (it's its purpose afterall, :/)
- Rust-friendly architecture
- C/C++ support through clang

## Language Server Support

VIBE uses the **Language Server Protocol (LSP)** to provide language intelligence.

Currently being tested with:

- C
- C++
- clangd

Example clangd environment:

```text
clangd 21.1.8
target: aarch64-unknown-linux-android24
```

Planned language support includes:

- C / C++ → clangd
- Rust → rust-analyzer
- Java → jdtls
- Kotlin → kotlin-language-server
- Python → pyright
- and more

The idea is to make language support modular instead of hardcoding every language into the editor.

## Toolchains

VIBE is intended to work with native Android/AArch64 toolchains.

Instead of reinventing every compiler and utility, VIBE can integrate with existing Android-compatible environments via Termux.

Possible toolchains include:

```text
Clang / LLVM
OpenJDK
Rust
Python
CMake
Ninja
Gradle
```

## Extensibility

VIBE is being designed with an extension/API system in mind.

A language integration could provide things such as:

```text
LanguageSupport
├── file extensions
├── syntax highlighting
├── LSP configuration
├── formatter
├── build configuration
├── commands
└── debugger support
```

This should allow VIBE to grow without turning the core project into an enormous collection of language-specific code.

## 📱 Why VIBE?

Android has plenty of coding applications.

The problem is that many of them are either:

- focused on one language;
- limited to simple editing;
- built around old Android UI patterns;
- dependent on remote/cloud environments;
- or simply not designed as a proper development environment.

VIBE aims for something different:

> **A real development environment, running directly on your Android device.**

## Project Status

VIBE is currently **in development**.

Things are being implemented incrementally.

Current work includes:

- [x] Basic text buffer
- [x] Multiline editing
- [x] Cursor movement
- [x] Basic editor rendering
- [x] LSP client prototype
- [x] clangd connection
- [x] LSP initialization
- [ ] Diagnostics UI
- [ ] Autocomplete UI
- [ ] Go-to-definition
- [ ] Hover information
- [ ] Syntax highlighting
- [ ] File explorer
- [ ] Project system
- [ ] Build system integration
- [ ] Extension API
- [ ] More language servers
- [ ] Debugger integration

This list will probably change because software development is apparently a ritual where the TODO list reproduces faster than the programmer can delete items.

## Building

Clone the repository:

```bash
git clone https://github.com/ValverdeBall/VIBE
cd VIBE
```

Open the project in CodeAssist and build. (why CodeAssist? i use it to make the app abd im lazy to write gradle files, yikes)

## Contributing

Contributions, ideas and experiments are welcome.

If you want to add a language, toolchain or integration, try to keep it modular and avoid coupling it directly to the editor UI.

Please open an issue before making large architectural changes.

## License

This project is licensed under the **MIT License**.

See [`LICENSE`](LICENSE) for details.

---

Made with Kotlin, Android, questionable amounts of caffeine, love, and the desire to make coding on Android less painful.

**VIBE — Code wherever you are.**
