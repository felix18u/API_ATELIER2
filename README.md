# GeoQuizz

## Installation

Build des deux APIs depuis la racine du projet : 
```sh
$ cd ./API_BACKOFFICE
$ mvn clean install --DskipTests
$ cd ../API_PLAYER
$ mvn clean install --DskipTests
```

Ensuite il faut lancer le docker : 
```sh
$ docker-compose up --build -d
```

Tous les containers seront lancés en même temps. 


-d : Vous permet de reprendre la main sur votre invite de commande.
--build : Va construire les container avant de les executer

## Utilisation : 
### API PLAYER

| Nom | Description | Method | Nom |
| ------ | ------ | ------ | ------ |
| Créer une partie | Permet de créer une partie avec un token | Post | /partie/{serieid} |
| Envoyer le score | Permet d’envoyer le score afin que le résultat soit enregistré.  Il faut mettre le token dans le header | PUT | /partie/{id} |
| Récupérer toutes les parties | Permet de récupérer toutes les parties enregistrée | Get | /partie |
| Récupérer une partie | Permet de récupérer les informations de une partie. Il faut mettre le token dans le header | Get | /partie/{idpartie} |
| Supprimer une partie | Permet de supprimer une partie | DELETE | /partie/{id} |


### API BACKOFFICE

| Nom | Description | Method | Nom |
| ------ | ------ | ------ | ------ |
| Créer une série | Permet de créer une série | Post | /series |
| Modifier une série | Permet de modifier une série | PUT | /series/{id} |
| Supprimer une série | Permet de supprimer une série | DELETE | /series/{id} |
| Récupérer une série | Permet de prendre une série | Get | /series/{id} |
| Ajouter une photo | Permet d'ajouter une photos | Post | /photos/upload |
| Ajouter les infos d'une photo | Permet d'ajouter les informations photos comme la localisation ou la serie associée | Post | /photos/{serieid} |
