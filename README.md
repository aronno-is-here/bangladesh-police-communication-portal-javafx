# Bangladesh Police Station Portal (JavaFX + Spring Boot)

Multi-module project:
- `backend`: Spring Boot REST/WebSocket API with H2 database and file storage
- `client`: JavaFX desktop client (FXML via SceneBuilder), responsive and mobile-friendly tweaks

## Features (MVP)
- Admin/Officer login screens
- Admin: create rooms, list rooms, files, criminal records
- Officer: my rooms, chat room UI, Leaflet map in WebView
- WebSocket/STOMP scaffolding for real-time messages
- Files upload per room; 10-minute auto-deletion of chat messages (files persist)
- Criminal records search with filters

## Quick Start

### Prerequisites
- JDK 17+
- Maven 3.9+

### Run Backend
```bash
cd backend
mvn spring-boot:run
```
- API base: `http://localhost:8080/api`
- H2 Console: `http://localhost:8080/h2` (JDBC url: `jdbc:h2:file:./data/db`)

### Run Client (JavaFX)
```bash
cd client
mvn -Pdevelopment javafx:run
```
Or build a runnable app:
```bash
mvn -pl client -am clean package
```

## Client Screens (FXML)
- `Landing.fxml`: Admin/Officer login
- `AdminShell.fxml` with subviews:
  - `admin/Home.fxml`
  - `admin/Rooms.fxml`
  - `admin/Files.fxml`
  - `admin/Records.fxml`
  - `admin/Settings.fxml`
- `OfficerShell.fxml` with subviews:
  - `officer/Home.fxml`
  - `officer/Rooms.fxml`
  - `officer/ChatRoom.fxml`
- `MapDialog.fxml`: Leaflet map in WebView

Open FXML files in SceneBuilder to adjust visuals.

## Enhancements vs React Prototype
- Dark blue official theme, elevated cards, badges
- Smooth transitions and spacing; mobile-friendly layout (min sizes + scalable fonts)
- Leaflet map with OSM tiles, popups, and extensible markers

## Next Steps (Guided)
1. Wire controllers to backend endpoints in `client/.../api` (create `ApiClient`):
   - Login: `/api/public/login/admin|officer`
   - Rooms CRUD & assign: `/api/rooms`...
   - Messages: `/api/rooms/{id}/messages`, `/api/rooms/{id}/messages` (POST)
   - Uploads: `/api/rooms/{id}/upload`
   - Records search: `/api/records/search`
2. Enable STOMP over WebSocket from Java client or poll for MVP.
3. Enforce verification prompts before files/map access.
4. Add role-based UI disabling for completed rooms.

## Security Notes
- Replace basic demo auth with JWT / Session auth.
- Store passwords with BCrypt in DB.
- Validate file types and scan uploads.

## Mobile
- JavaFX scales layout; style in `css/app.css` uses larger hit targets and flexible cards.

## License
Internal use for Bangladesh Police.
