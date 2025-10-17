# User Management System
[![Maven CI](https://github.com/maulana78204/REST-API-User-Management-System-By-Maulana-Imran-Ravasia/actions/workflows/maven.yml/badge.svg)](https://github.com/maulana78204/REST-API-User-Management-System-By-Maulana-Imran-Ravasia/actions/workflows/maven.yml)

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


## Dokumentasi Swagger UI

	Registrasi & login user dengan autentikasi berbasis JWT dengan Integrasi Keycloak


○	Endpoint: POST /auth/keycloack/register

<img width="846" height="444" alt="image" src="https://github.com/user-attachments/assets/96e00a2d-6596-435e-bb51-957e4e66b09a" />



○	Endpoint: POST /auth/keycloack/login

<img width="918" height="108" alt="image" src="https://github.com/user-attachments/assets/baefefad-8d14-421e-aac0-730c6212f7f5" />

 

	Authentication & Authorization


○	Endpoint: POST /auth/register

 <img width="422" height="278" alt="image" src="https://github.com/user-attachments/assets/2f1250ab-21ad-41ea-bb3b-29399f36dbb6" />

 <img width="422" height="122" alt="image" src="https://github.com/user-attachments/assets/59e92fc9-5891-47c2-896e-18bafc7b0cb0" />


○	Endpoint: POST /auth/login

 <img width="439" height="166" alt="image" src="https://github.com/user-attachments/assets/854469bd-752f-4008-a7c3-450a88034abf" />

 <img width="881" height="112" alt="image" src="https://github.com/user-attachments/assets/d56f9229-e095-4500-8208-2fd9f748b78b" />
 

○	Endpoint: POST /auth/refresh

 <img width="875" height="90" alt="image" src="https://github.com/user-attachments/assets/2724163d-a0fa-4e2e-bb78-1f96e9a426c0" />

<img width="875" height="119" alt="image" src="https://github.com/user-attachments/assets/d401d38d-66ec-4b19-967c-97a93d39a787" />

○	Endpoint: POST /auth/logout

<img width="853" height="101" alt="image" src="https://github.com/user-attachments/assets/8cfa1cc2-6153-466e-9413-766f24027c75" />

<img width="464" height="111" alt="image" src="https://github.com/user-attachments/assets/80961956-1022-4c1d-ac3a-28950d7e30e5" />

	Profile Management (User)


○	Endpoint: GET /profiles/me

<img width="853" height="112" alt="image" src="https://github.com/user-attachments/assets/a86ed722-c092-4208-ae87-837d527cb17e" />

<img width="378" height="209" alt="image" src="https://github.com/user-attachments/assets/c81f5d67-d7ac-42a3-a4db-ea85aedfaf68" />

○	Endpoint: PUT /profiles/me

<img width="853" height="215" alt="image" src="https://github.com/user-attachments/assets/18b9d9e6-500b-4ef7-9834-64e4d5690862" />

<img width="389" height="213" alt="image" src="https://github.com/user-attachments/assets/4ea124a8-a794-4482-82ce-9d4a7ee62c97" />


○	Endpoint: DELETE /profiles/me

 <img width="738" height="117" alt="image" src="https://github.com/user-attachments/assets/4dcef898-e725-4085-8950-b5c8a8b50e37" />

 <img width="347" height="106" alt="image" src="https://github.com/user-attachments/assets/2b3c1cb0-48af-4904-854a-6436817e45c6" />


	Profile Management (Admin Only)

<img width="790" height="120" alt="image" src="https://github.com/user-attachments/assets/9ec581fa-44eb-4cf1-b639-928cc60e6ef5" />

<img width="425" height="211" alt="image" src="https://github.com/user-attachments/assets/af8abcb8-dc12-40e5-bbe7-e744630d87d3" />


○	Endpoint: GET /admin/users/{id}

 <img width="733" height="162" alt="image" src="https://github.com/user-attachments/assets/76a05de1-5ad0-412a-8e6d-942204c2c59c" />

<img width="469" height="220" alt="image" src="https://github.com/user-attachments/assets/7bdea8ff-83ff-4ece-8d8e-b7ac8ad652e1" />



○	Endpoint: PUT /admin/users/{id}/status

<img width="780" height="245" alt="image" src="https://github.com/user-attachments/assets/95b642d6-e88d-4935-859c-e28e03d4d6e0" />

<img width="508" height="233" alt="image" src="https://github.com/user-attachments/assets/21b768c4-e785-4824-ad3a-190e849976fb" />

○	Endpoint: PUT /admin/users/{id}

<img width="798" height="80" alt="image" src="https://github.com/user-attachments/assets/aa43da1a-d1cf-40c0-86e9-e1ecee384398" />

<img width="789" height="417" alt="image" src="https://github.com/user-attachments/assets/9cfd105d-0f7d-4303-835a-e0cbc77b3ddd" />


○	Endpoint: GET /admin/users

<img width="814" height="88" alt="image" src="https://github.com/user-attachments/assets/7a4af96e-3ba8-46a7-8818-0bc2e2f1263e" />

<img width="494" height="122" alt="image" src="https://github.com/user-attachments/assets/9c4b94d6-451e-49fb-9f60-5d6ae8948466" />

 


 




Endpoint: DELETE /admin/users {id}


 




 








