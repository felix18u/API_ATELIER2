# API_ATELIER2
##Commande a faire
• Dans chaque dossier, lancer la commande :
###mvn clean install -DskipTests
Il va compiler les projets en téléchargeant les dépendances. Le fichier .jar sera créé dans le dossier target

• Pour lancer les containers, executer la commande :
###docker-compose up --build -d
Tous les countainers seront lancés en même temps. -d vous permet de reprendre la main sur votre invite de commance. --build va construire les container avant de les executer

• Pour remplir la table d'exemple, executer ces 2 commandes :
###docker cp ./data.sql postgres:/docker-entrypoint-initdb.d/dump.sql
###docker exec -u postgres postgres psql postgres postgres -f docker-entrypoint-initdb.d/dump.sql
Ces commandes va copier le fichier data.sql dans le container postgres puis executer le script sql



