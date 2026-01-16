# les urls suivant posent des problemes

## organisation
1. curl -X 'GET' 'http://localhost:8080/api/organizations' -H 'accept: */*'
 le type d'organisation est deja defini mais n'est pas pris en compte lors de la creation d'une organization. d'ou le mappage du type d'organisation present en base de donnees avec les types predefini renvoir une erreur

2. curl -X 'PUT' \
  'http://localhost:8080/api/organizations/48eda37b-c5cd-4bef-831e-7855b2915050' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "organizationName": "GENERAL EXPRESS VOYAGE",
  "orgType": "MERCHANT",
  "isActive": true
}'

pourquoi le orgCode est obligatoire pour le update?
ainsi que le organizationName.

donnee minimal a fournir pour un update sur une organisation:
{
  "organizationName": "GENERAL EXPRESS VOYAGE",
  "orgCode": "GENEARAL_1234",
  "orgType": "MERCHANT"
}

## User app
1. 
gestion des erreurs dans le cas les elements n'existe pas.

## Point of Interest
1.