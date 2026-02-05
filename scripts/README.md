# Script Python pour Générer des Données de Test

Ce script crée automatiquement des données de test pour l'API POI.

## Prérequis

```bash
pip install requests
```

## Utilisation

1. Assurez-vous que l'API est en cours d'exécution sur `http://localhost:8080`
2. Exécutez le script:

```bash
python scripts/create_test_data.py
```

## Données Créées

- **3 Organisations** (MERCHANT, DISTRIBUTOR, SUPPLIER)
- **3 Utilisateurs** (1 ADMIN, 2 USER)
- **3 POIs** (Restaurant, Supermarché, Cinéma)
- **2 Blogs**
- **2 Podcasts**
- **3 Reviews** (1 pour POI, 1 pour Blog, 1 pour Podcast)

## Note Importante

Les POIs sont créés avec le statut `SUBMITTED` et `is_active=false` selon le nouveau workflow d'approbation.

Pour activer un POI, utilisez:
```bash
# Approuver le POI
PATCH /api/pois/{poi_id}/approve?approverId={admin_user_id}

# Activer le POI
PATCH /api/pois/{poi_id}/activate
```
