# Keycloak Setup Guide

## 1. Start Services

```bash
docker-compose up -d
```

## 2. Access Keycloak Admin Console

- URL: http://localhost:8081
- Username: admin
- Password: admin

## 3. Create Realm

1. Login ke Keycloak Admin Console
2. Klik "Create Realm"
3. Nama realm: `user-management`
4. Klik "Create"

## 4. Create Client

1. Di realm `user-management`, pilih "Clients"
2. Klik "Create"
3. Client ID: `user-management-api`
4. Client Protocol: `openid-connect`
5. Root URL: `http://localhost:8080`
6. Klik "Save"

### Configure Client Settings

1. Di halaman client `user-management-api`:
   - Access Type: `confidential`
   - Service Accounts Enabled: `ON`
   - Authorization Enabled: `ON`
   - Valid Redirect URIs: `http://localhost:8080/*`
   - Web Origins: `http://localhost:8080`
   - Klik "Save"

2. Di tab "Credentials":
   - Copy "Secret" value
   - Update `keycloak.credentials.secret` di `application.properties`

## 5. Create Roles

1. Di realm `user-management`, pilih "Realm Roles"
2. Klik "Create Role"
3. Buat role berikut:
   - `CUSTOMER`
   - `MITRA`
   - `ADMIN`

## 6. Create Test Users

1. Di realm `user-management`, pilih "Users"
2. Klik "Add user"
3. Buat user dengan role yang sesuai

## 7. Test API

### Register User
```bash
curl -X POST http://localhost:8080/auth/keycloak/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "role": "CUSTOMER"
  }'
```

### Login User
```bash
curl -X POST http://localhost:8080/auth/keycloak/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## 8. Access Protected Endpoints

Gunakan token yang didapat dari login untuk mengakses endpoint yang dilindungi:

```bash
curl -X GET http://localhost:8080/profiles/me \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```
