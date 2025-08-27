# 🏦 E-Banking Backend

[![Status](https://img.shields.io/badge/Status-En%20développement-orange)](https://github.com/votre-username/ebanking-backend)
[![Progress](https://img.shields.io/badge/Progress-60%25-yellow)](https://github.com/votre-username/ebanking-backend)
[![Java](https://img.shields.io/badge/Java-17+-red)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-green)](https://spring.io/projects/spring-boot)

## 📋 Description

Backend Spring Boot pour une application de banque en ligne moderne avec gestion complète des comptes bancaires, sécurité OAuth2 et API REST documentée.

## 🚀 Fonctionnalités

- **Gestion des comptes bancaires** : CRUD complet des comptes
- **API REST sécurisée** : Endpoints protégés avec OAuth2
- **Base de données MySQL** : Persistance des données avec JPA
- **Documentation API** : Interface Swagger/OpenAPI
- **Architecture en couches** : Services, Repositories, DTOs, Mappers
- **Gestion des exceptions** : Gestion centralisée des erreurs
- **Sécurité avancée** : Authentification et autorisation

## 🛠️ Technologies utilisées

- **Java 17+** - Langage principal
- **Spring Boot 3.4.2** - Framework principal
- **Spring Data JPA** - Persistance des données
- **Spring Security + OAuth2** - Sécurité et authentification
- **MySQL 8.2.0** - Base de données
- **Lombok** - Réduction du code boilerplate
- **Maven** - Gestion des dépendances
- **SpringDoc OpenAPI** - Documentation de l'API

## 📊 État du Projet

**Statut :** 🟡 En développement  
**Progression :** 60%  
**Dernière mise à jour :** Décembre 2024  

### ✅ Ce qui est terminé
- [x] Configuration de base Spring Boot
- [x] Structure du projet (packages, architecture)
- [x] Configuration de la base de données MySQL
- [x] Dépendances Maven configurées
- [x] Sécurité OAuth2 configurée
- [x] Documentation OpenAPI configurée
- [x] Architecture en couches (Services, Repositories, DTOs, Mappers)
- [x] Gestion des exceptions

### 🚧 En cours de développement
- [ ] Implémentation des services métier
- [ ] Logique de gestion des comptes bancaires
- [ ] Validation des données
- [ ] Tests unitaires

### 📋 À faire
- [ ] Tests d'intégration
- [ ] Tests de sécurité
- [ ] Optimisation des performances
- [ ] Documentation complète
- [ ] Déploiement
- [ ] Monitoring et logging

## 🚀 Installation et démarrage

### Prérequis
- Java 17 ou supérieur
- MySQL 8.0 ou supérieur
- Maven 3.6+

### Étapes d'installation

1. **Cloner le projet**
   ```bash
   git clone [URL_DE_VOTRE_DEPOT]
   cd ebanking-backend
   ```

2. **Configurer la base de données**
   - Créer une base de données MySQL
   - Modifier `src/main/resources/application.properties`

3. **Installer les dépendances**
   ```bash
   mvn clean install
   ```

4. **Lancer l'application**
   ```bash
   mvn spring-boot:run
   ```

5. **Accéder à l'application**
   - API : http://localhost:8080
   - Documentation Swagger : http://localhost:8080/swagger-ui.html

## 📚 Structure du Projet

```
src/main/java/com/mourad/ebankingbackend/
├── security/          # Configuration de la sécurité OAuth2
├── web/              # Contrôleurs REST
├── services/         # Logique métier
├── repositories/     # Accès aux données
├── dtos/            # Objets de transfert de données
├── mappers/         # Mappers entre entités et DTOs
├── exceptions/      # Gestion des exceptions
├── enums/           # Énumérations
├── enteties/        # Entités JPA
└── BankingBackendApplication.java
```

## 🔐 Sécurité

- **OAuth2 Resource Server** pour la sécurisation des endpoints
- **JWT** pour l'authentification
- **Spring Security** pour l'autorisation

## 📖 API Documentation

L'API est documentée avec **SpringDoc OpenAPI** et accessible via :
- **Swagger UI** : Interface interactive pour tester l'API
- **OpenAPI JSON** : Spécification OpenAPI 3.0

## 🧪 Tests

```bash
# Exécuter tous les tests
mvn test

# Exécuter les tests avec rapport de couverture
mvn test jacoco:report
```

## 📝 Contribution

1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence [MIT](LICENSE).

## 👨‍💻 Auteur

**Mourad** - Développeur Full Stack

## 📞 Support

Pour toute question ou suggestion, n'hésitez pas à :
- Ouvrir une issue sur GitHub
- Me contacter directement

---

**Note :** Ce projet est actuellement en développement actif. Les fonctionnalités peuvent évoluer et certaines parties peuvent ne pas être encore implémentées.
