# E-Banking Backend — API REST Spring Boot sécurisée par JWT

Backend d'une application de banque en ligne : gestion des clients, des comptes courants et épargne, et des opérations (débit, crédit, virement) avec historique paginé. L'API est stateless, sécurisée par **JWT** (Spring Security OAuth2 Resource Server) avec des rôles **USER** et **ADMIN**.

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green)
![Spring Security](https://img.shields.io/badge/Security-JWT-6DB33F)
![MySQL](https://img.shields.io/badge/DB-MySQL-4479A1)

## Fonctionnalités

- **Clients** : CRUD et recherche par mot-clé
- **Comptes bancaires** : comptes courants (avec découvert) et comptes épargne (avec taux d'intérêt), modélisés par héritage JPA (`SINGLE_TABLE`)
- **Opérations** : débit, crédit et virement entre comptes, avec contrôle du solde
- **Historique** des opérations d'un compte, paginé
- **Authentification JWT** : jeton signé en HS512, valable 10 minutes
- **Autorisations par rôle** avec `@PreAuthorize` : lecture pour USER, écriture pour ADMIN
- **Documentation** de l'API via Swagger UI (springdoc-openapi)

## Stack technique

| Couche | Technologies |
|---|---|
| Langage | Java 17 |
| Framework | Spring Boot 3.4 (Web, Data JPA, Security, OAuth2 Resource Server) |
| Sécurité | JWT (Nimbus JOSE), BCrypt |
| Base de données | MySQL 8 |
| Documentation | springdoc-openapi (Swagger UI) |
| Outils | Maven, Lombok |

## Architecture

```
src/main/java/com/mourad/ebankingbackend/
├── web/           → contrôleurs REST (comptes, clients)
├── security/      → configuration Spring Security + endpoint /auth
├── services/      → logique métier
├── repositories/  → accès aux données (Spring Data JPA)
├── enteties/      → entités JPA : Customer, BankAcount, CurrentAccount, SavingAccount, AccountOperation
├── dtos/          → objets de transfert exposés par l'API
├── mappers/       → conversion entités ⇄ DTO
├── exceptions/    → exceptions métier (compte introuvable, solde insuffisant…)
└── enums/         → statut de compte, type d'opération
```

## Lancer le projet

Prérequis : Java 17 ou plus et un serveur MySQL local. La base `E-BANK` est créée automatiquement au démarrage.

```bash
git clone https://github.com/erryhany/ebanking-backend.git
cd ebanking-backend
```

Définir les variables d'environnement. `JWT_SECRET` est obligatoire et doit faire au moins 64 caractères (HS512) :

```bash
# Linux / macOS
export JWT_SECRET=$(openssl rand -base64 64 | tr -d '\n')
export DB_USERNAME=root      # optionnel, "root" par défaut
export DB_PASSWORD=          # optionnel, vide par défaut
```

```powershell
# Windows PowerShell
$env:JWT_SECRET = [Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Max 256 }))
```

Puis démarrer :

```bash
./mvnw spring-boot:run       # Windows : mvnw.cmd spring-boot:run
```

| Ressource | URL |
|---|---|
| API | http://localhost:8085 |
| Swagger UI | http://localhost:8085/swagger-ui.html |

## Authentification

Comptes de démonstration, définis en mémoire et réservés au développement :

| Utilisateur | Rôles |
|---|---|
| `user1` | USER |
| `admin` | USER, ADMIN |

Le mot de passe de démonstration est défini dans `SecurityConfig`.

```bash
# 1. Obtenir un jeton
curl -X POST "http://localhost:8085/auth/login" -d "username=admin&password=<mot_de_passe>"
# → {"access_token":"eyJ..."}

# 2. Appeler l'API avec le jeton
curl "http://localhost:8085/customers" -H "Authorization: Bearer eyJ..."
```

## Endpoints principaux

| Méthode | Route | Rôle | Description |
|---|---|---|---|
| POST | `/auth/login` | public | Obtenir un jeton JWT |
| GET | `/auth/profile` | authentifié | Profil de l'utilisateur connecté |
| GET | `/customers` | USER | Liste des clients |
| GET | `/customers/search?keyword=` | USER | Recherche de clients |
| GET | `/customers/{id}` | USER | Détail d'un client |
| POST | `/customers` | ADMIN | Créer un client |
| PUT | `/customers/{id}` | ADMIN | Modifier un client |
| DELETE | `/customers/{id}` | ADMIN | Supprimer un client |
| GET | `/accounts` | authentifié | Liste des comptes |
| GET | `/accounts/{id}` | authentifié | Détail d'un compte |
| GET | `/accounts/{id}/PageOperations?page=&size=` | authentifié | Historique paginé |
| POST | `/accounts/debit` | authentifié | Débiter un compte |
| POST | `/accounts/credit` | authentifié | Créditer un compte |
| POST | `/accounts/transfer` | authentifié | Virement entre deux comptes |

## Pistes d'évolution

- Utilisateurs et rôles stockés en base au lieu d'être en mémoire
- Gestion globale des erreurs avec `@ControllerAdvice`
- Tests unitaires et d'intégration (JUnit 5, MockMvc, Testcontainers)
- Docker Compose (application et MySQL)

## Auteur

**Mourad Erryhany** — Développeur Java / Spring Boot · [GitHub](https://github.com/erryhany)
