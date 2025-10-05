Bangladesh Police - Station Portal (JavaFX + Spring Boot)
========================================================

Modules
 - shared: DTOs shared by client and server
 - server: Spring Boot 3, SQLite, REST + STOMP WebSocket, file uploads, message TTL cleanup
 - client: JavaFX 21, FXML screens, WebView map (Leaflet), mobile-friendly styling

Prerequisites
 - Java 17+
 - Maven 3.9+

Quick start
 1. Build all: `mvn -q -f pom.xml clean package`
 2. Run server: `mvn -q -f server/pom.xml spring-boot:run`
 3. Run client: `mvn -q -f client/pom.xml javafx:run`

Default endpoints
 - Auth: POST /api/auth/admin/login, /api/auth/officer/login
 - Rooms: GET/POST /api/rooms, POST /api/rooms/{id}/complete, DELETE /api/rooms/{id}
 - Chat: GET /api/chat/{roomId}, WS STOMP endpoint /ws (topic /topic/rooms/{roomId})
 - Files: GET /api/files?roomId=..., POST /api/files/upload (multipart), GET /api/files/{id}/download
 - Criminal Records: GET /api/criminal-records?name=&gender=&hometown=&nationalId=

Notes
 - Messages auto-delete after 10 min (scheduler)
 - File uploads stored under ${user.home}/bdpolice/uploads
 - Map uses Leaflet inside WebView
 - This skeleton focuses on structure; controllers should be connected to REST/WS services.
