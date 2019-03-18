# GeoQuizz

### Installation

Build des deux APIs depuis la racine du projet : 
```sh
$ cd ./API_BACKOFFICE
$ mvn clean install --DskipTests
$ cd ../API_PLAYER
$ mvn clean install --DskipTests
```

Ensuite il faut lancer le docker : 
```sh
$ docker-compose up --build
```

Vos deux APIs sont fonctionnelles !!!

### Utilisation : 
### API PLAYER

| API_PLAYER | Routes |
| ------ | ------ |
| Series | 127.0.0.1:8080/series |

### API BACKOFFICE

| API_BACKOFFICE | Routes |
| ------ | ------ |
| Series | 127.0.0.1:8081/series |
