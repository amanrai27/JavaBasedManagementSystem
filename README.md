# Management System

A full desktop management application built with **Java 17 + JavaFX 21**, covering
the core operations of a cargo/logistics company: transport, shipments, export,
import, fleet, customers, invoicing/transactions and reporting. All data is
generated **in-memory dummy data** (no database setup required) so the whole
application can be explored end-to-end immediately after launch.

## Modules

| Module | What it manages |
|---|---|
| **Dashboard** | KPI cards + live charts (shipment status, shipment type, monthly revenue trend, top customers) |
| **Shipments** | Every cargo shipment booked by a customer (domestic / export / import) |
| **Transport** | Vehicle + driver assignments that physically move each shipment |
| **Export** | Customs/export paperwork for shipments leaving the country |
| **Import** | Customs/import paperwork for shipments entering the country |
| **Customers** | Individual and business clients who book shipments |
| **Fleet** | Vehicles (trucks, container trailers, cargo ships, aircraft, rail wagons) and Drivers |
| **Transactions** | Payments received, refunds, expenses and customs duty movements |
| **Invoices** | Billing raised against customers with tax and payment-status tracking |
| **Reports** | Financial summary + tabular breakdowns (status counts, top customers, top export markets) |

Every module has a full **Add / Edit / Delete / Search** screen wired to a real
service layer sitting on an in-memory data store — this is a working
application, not a mockup.

## Tech stack & architecture

- **Java 17**, **JavaFX 21** (programmatic UI, no FXML)
- **Maven** build (`pom.xml`)
- Layered architecture:
  - `model` — plain entity classes + enums (`Customer`, `Shipment`, `Vehicle`, `Driver`, `TransportAssignment`, `ExportRecord`, `ImportRecord`, `Transaction`, `Invoice`, …)
  - `data` — `DataStore` (in-memory "database" as `ObservableList`s) + `DummyDataGenerator` (seeds ~30 cross-referenced shipments, 12 customers, 10 vehicles/drivers, invoices, transactions, export/import records)
  - `service` — one service per entity (`CustomerService`, `ShipmentService`, …) built on a shared generic `AbstractCrudService<T>`, plus `ReportService` for aggregated analytics and `ServiceRegistry` wiring it all together
  - `ui` — a generic reusable `CrudView<T>` (table + search + toolbar) that every module screen extends, `ui.dialogs` for the Add/Edit forms, `ui.util` for shared formatting/dialog helpers
  - `MainApp` — the JavaFX application shell (sidebar navigation + content area)

This means swapping the in-memory `DataStore` for a real database later (JDBC/JPA)
only touches the `data`/`service` layers — the UI does not need to change.

## How to run

### Option 1 — Maven (recommended)

```bash
mvn javafx:run
```

### Option 2 — IDE (IntelliJ IDEA / Eclipse)

1. Open the folder as a Maven project (`File → Open → select this folder`).
2. Let it download dependencies (JavaFX 21 modules from Maven Central).
3. Run the `com.folkcargo.MainApp` class directly (most IDEs run JavaFX apps
   fine this way once the `javafx-*` Maven dependencies are on the classpath).

### Option 3 — Runnable jar

```bash
mvn package
java -jar target/folk-cargo-management.jar
```

(This uses `com.folkcargo.Launcher` as the jar's entry point — a small class
that isn't itself a JavaFX `Application`, which avoids the "JavaFX runtime
components are missing" error you'd otherwise get running a self-contained jar.)

## Requirements

- JDK 17 or newer
- Maven 3.6+
- Internet access the first time you build, so Maven can download the
  `org.openjfx` JavaFX artifacts (~15 MB) from Maven Central

## Project structure

```
folk-cargo-management/
├── pom.xml
└── src/main/
    ├── java/com/folkcargo/
    │   ├── MainApp.java            # application shell / sidebar navigation
    │   ├── Launcher.java           # jar entry point
    │   ├── model/                  # entities + enums
    │   ├── data/                   # DataStore + DummyDataGenerator
    │   ├── service/                # CRUD services + ReportService
    │   └── ui/
    │       ├── CrudView.java       # generic list+toolbar+dialog base screen
    │       ├── *View.java          # one screen per module
    │       ├── dialogs/            # Add/Edit dialogs per entity
    │       └── util/               # UIUtils, FormGrid
    └── resources/com/folkcargo/css/styles.css
```

## Notes for extending this project

- **Swap in a real database**: replace `DataStore`'s `ObservableList`s with
  JDBC/JPA-backed repositories behind the same `service` interfaces — the
  `CrudView`/dialog UI layer works against services, not the store directly.
- **Add authentication**: add a login screen before `MainApp.start()` shows
  the main scene; a `User`/`Role` model would slot in alongside the existing
  entities.
- **Export reports**: the `Reports` screen's data (`ReportService`) is plain
  Java data (maps/records), so wiring it into a PDF/Excel export (e.g. with
  Apache POI or iText) is straightforward.
- **Regenerate dummy data differently**: all sample data comes from
  `data/DummyDataGenerator.java` — edit the arrays there to change customers,
  routes, fleet, etc.
