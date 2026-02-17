# Backdoor Game
Développé par IsotopeStudio (Doryan Bessière)

Backdoor est un MOBA se déroulant dans un environnement de bureau simulé. L'objectif principal est de capturer des zones appelées Proxies en les piratant. Le jeu mise sur une expérience compétitive, avec un système de classement et des parties classées, tout en proposant une campagne solo et plusieurs modes multijoueur.

## Sommaire
- Aperçu
- Visuels
- Gameplay et objectifs
- Modes de jeu
- Social et coopération
- Personnalisation du bureau
- Tech stack et prérequis
- Lancer le projet
- Structure du dépôt

## Aperçu
Backdoor vise un large public, du joueur occasionnel au joueur compétitif. Le bureau simulé est totalement personnalisable et éditable, ce qui sert à la fois l'immersion et la stratégie.

## Visuels
![Lancement](docs/Launcher.webp)
![Profil](docs/Base_Profile_Screenshot_2020.03.26_-_03.16.09.35.png)
![Interface](docs/Base_Profile_Screenshot_2020.03.30_-_09.11.25.44.webp)

![Gameplay 1](docs/exemple.gif)
![Gameplay 2](docs/preuve5.gif)
![Gameplay 3](docs/spoil1.gif)

## Gameplay et objectifs
- Capturer des Proxies en piratant des zones du bureau.
- Contrôler la carte et gérer la pression sur plusieurs fronts.
- Miser sur la coordination et le timing pour prendre l'avantage.

## Modes de jeu
- Multijoueur compétitif avec matchmaking et classement.
- Campagne solo scénarisée.
- Variantes compétitives selon les objectifs de match.

## Social et coopération
- Création d'équipes et d'alliances.
- Raids coordonnés pour des attaques groupées.
- Dimension communautaire centrale à l'expérience.

## Personnalisation du bureau
- Bureau simulé personnalisable et éditable.
- Interface modulable pour adapter la stratégie et le confort visuel.

## Tech stack et prérequis
- Java 8 (source et target 1.8).
- Maven pour la compilation et le packaging.
- Dépendances graphiques et math (voir [pom.xml](pom.xml)).

## Lancer le projet
- Build Maven: `mvn package`
- Point d'entrée principal: [src/isotopestudio/backdoor/game/BackdoorGame.java](src/isotopestudio/backdoor/game/BackdoorGame.java)
- Exécution conseillée via un IDE Java en définissant la classe principale.

## Structure du dépôt
- [src](src) code source principal du jeu.
- [resources](resources) assets et ressources.
- [docs](docs) médias et visuels utilisés dans ce README.
- [target](target) sorties de build (si générées).
