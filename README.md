# Insurance Policy Management System (Mini Project)

This mini project is built with core Java concepts:

- Class and objects
- Abstraction, polymorphism, inheritance, encapsulation
- Abstract class and interface
- Garbage collector usage (`System.gc()` request)
- Lambda expressions

## Features

- Add and manage `HealthPolicy` and `VehiclePolicy`
- View, search, sort and delete policies
- Persist policies in a JSON file (`data/policies.json`)
- Dual UI support:
  - Terminal UI
  - Swing GUI
- Toggle UI mode through a boolean value in `InsurancePolicyManagementApp`

## Project structure

- `src/com/insurance/model` - domain classes and interface
- `src/com/insurance/repository` - JSON persistence abstraction and implementation
- `src/com/insurance/service` - business logic
- `src/com/insurance/ui` - terminal and Swing user interfaces
- `src/com/insurance/InsurancePolicyManagementApp.java` - launcher with UI switch boolean

## Run

```bash
javac -d out $(find src -name "*.java")
java -cp out com.insurance.InsurancePolicyManagementApp
```

For GUI mode, set:

```java
private static final boolean USE_GUI = true;
```

inside `InsurancePolicyManagementApp` and run again.
