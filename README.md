# User Management System

REST API untuk sistem manajemen user dengan autentikasi berbasis Keycloak/JWT, dibuat menggunakan Java Spring Boot dan PostgreSQL.

## Fitur

### Authentication & Authorization
- ✅ Registrasi user + profile dalam satu request
- ✅ Login dengan Keycloak (OpenID Connect)
- ✅ Logout dan refresh token
- ✅ Role-based access control (CUSTOMER, MITRA, ADMIN)
- ✅ Integrasi Keycloak (IAM)

### User Profile Management
- ✅ GET /profiles/me - Mendapatkan profile user yang sedang login
- ✅ PUT /profiles/me - Update profile user sendiri
- ✅ DELETE /profiles/me - Hapus profile user sendiri

### Admin Management
- ✅ GET /admin/users - Daftar semua user dengan pagination
- ✅ GET /admin/users/{id} - Detail user tertentu
- ✅ PUT /admin/users/{id} - Update data user
- ✅ PUT /admin/users/{id}/status - Ubah status user
- ✅ DELETE /admin/users/{id} - Hapus user

### Dokumentasi API
- ✅ Swagger/OpenAPI documentation
- ✅ Validation dan error handling
- ✅ Transactional operations

## Teknologi

- Java 17
- Spring Boot 2.7.18
- Spring Security + Keycloak
- PostgreSQL
- Docker (opsional)
- Swagger/OpenAPI
- Maven

## Struktur Proyek

```
src/
├── main/
│   ├── java/com/example/usermanagement/
│   │   ├── config/          # Konfigurasi (Security, OpenAPI)
│   │   ├── controller/      # REST Controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA Entities
│   │   ├── exception/       # Custom Exceptions
│   │   ├── repository/      # JPA Repositories
│   │   ├── service/         # Business Logic
│   │   └── util/            # Utilities (JWT)
│   └── resources/
│       └── application.properties
└── test/
    ├── java/               # Integration Tests
    └── resources/
        └── application-test.properties
```

## Setup dan Running

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL
- Docker (opsional, untuk menjalankan Keycloak cepat)

### 1. Clone Repository
```bash
git clone <repository-url>
cd REST API User Management System
```

### 2. Konfigurasi Database & Keycloak

#### Database (sesuaikan dengan application.properties)
- URL: `jdbc:postgresql://localhost:5433/usermanagement`
- Username: `postgres`
- Password: `newpassword`

#### Menjalankan Keycloak (Direkomendasikan via Docker)
```bash
docker run --name keycloak \
  -p 9080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -d quay.io/keycloak/keycloak:23.0.7 start-dev
```

Buka Keycloak Admin Console: `http://localhost:9080/`

Buat realm & client:
- Realm: `jhipster`
- Client: `web_app`
  - Client type: OpenID Connect
  - Client authentication: ON (confidential)
  - Standard Flow: ON
  - Direct Access Grants: ON
  - Valid redirect URIs: `http://localhost:10000/*`
  - Web origins: `*`
  - Credentials → salin Client Secret

Sesuaikan `src/main/resources/application.properties`:
```
keycloak.auth-server-url=http://localhost:9080
keycloak.realm=jhipster
keycloak.resource=web_app
keycloak.public-client=false
keycloak.credentials.secret=<ISI DENGAN CLIENT SECRET DARI KEYCLOAK>
```

### 3. Menjalankan Aplikasi
```bash
mvn spring-boot:run
```
Jika port 10000 sedang dipakai, hentikan prosesnya:
```powershell
netstat -ano | findstr :10000
taskkill /PID <PID> /F
```
Atau jalankan di port lain:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=10001"
```

### 4. Akses Aplikasi
- API Base URL: `http://localhost:10000`
- Swagger UI: `http://localhost:10000/swagger-ui/`
- Keycloak Admin Console: `http://localhost:9080`

## Cara Uji Keycloack Autentication (End-to-End)

1) Buat user di Keycloak
- Keycloak Admin → realm `jhipster` → Users → Add user
- Isi: username & email (contoh `user2@example.com`), Email verified: Yes → Create
- Tab Credentials → set password (contoh `Password123!`), Temporary: OFF → Set Password
- (Opsional) Assign roles sesuai kebutuhan endpoint (mis. ADMIN untuk endpoint admin)

2) Login via Swagger
- Buka Swagger → Authentication → `POST /auth/login` → Try it out
- Body contoh:
```json
{
  "email": "user2@example.com",
  "password": "Password123!"
}
```
- Execute → salin `accessToken` dari response

3) Akses endpoint yang dilindungi
- Contoh: `GET /admin/users?page=0&size=10&sort=fullName,asc`
- Tambahkan header: `Authorization: Bearer <accessToken>`
- Execute → pastikan mendapat 200 OK

Jika mendapat 401/403, cek:
- Email/password benar
- Token belum kedaluwarsa
- Role user sesuai dengan akses endpoint
- Konfigurasi Keycloak dan client secret sesuai

## API Endpoints (Ringkas)

### Authentication
- `POST /auth/register` - Registrasi user baru
- `POST /auth/login` - Login user (Keycloak)
- `POST /auth/logout` - Logout user
- `POST /auth/refresh` - Refresh token

### Profile Management (CUSTOMER, MITRA, ADMIN)
- `GET /profiles/me`
- `PUT /profiles/me`
- `DELETE /profiles/me`

### Admin Management (ADMIN only)
- `GET /admin/users`
- `GET /admin/users/{id}`
- `PUT /admin/users/{id}`
- `PUT /admin/users/{id}/status`
- `DELETE /admin/users/{id}`

## Testing
```bash
mvn test
```

## Docker Commands (opsional)
```bash
docker build -t user-management-api .
docker run -p 10000:10000 user-management-api
```

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5433/usermanagement` | Database URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `newpassword` | Database password |
| `JWT_SECRET` | `mySecretKey123456789012345678901234567890` | JWT secret key |
| `JWT_EXPIRATION` | `86400000` | JWT expiration (24 hours) |

## Security
- JWT-based authentication
- Password encryption dengan BCrypt
- Role-based authorization
- CORS configuration
- Input validation

## Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## License
Distributed under the MIT License. See `LICENSE` for more information.
