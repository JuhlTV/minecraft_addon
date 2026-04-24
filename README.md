# StatsRank

Ein Paper-Minecraft-Plugin, das Spieler-Stats speichert und daraus automatisch einen Rang berechnet.

## Funktionen

- Trackt Kills
- Trackt Deaths
- Trackt abgebaute Bloecke
- Trackt gesetzte Bloecke
- Zeigt die Werte mit `/stats` an
- Vergibt anhand der gesammelten Punkte einen Rang
- Aktualisiert den Rang direkt im Tab-Namen des Spielers

## Befehl

`/stats`

Zeigt deine eigenen Stats an.

`/stats <spieler>`

Zeigt die Stats eines anderen Spielers an.

## Punkte-System

Die Punkte und Rang-Grenzen kannst du in `src/main/resources/config.yml` anpassen.

Standardmaessig gilt:

- 1 Kill = 10 Punkte
- 1 Death = -3 Punkte
- 50 abgebaute Bloecke = 1 Punkt
- 50 gesetzte Bloecke = 1 Punkt

## Build

Das Plugin ist als Maven-Projekt angelegt.

Bauen:

```powershell
.\mvnw.cmd clean package
```

Die fertige Datei liegt danach unter:

`target/statsrank-1.0.0.jar`

## Installation

1. JAR bauen
2. Die Datei in den `plugins`-Ordner deines Paper-Servers legen
3. Server neu starten

## Hinweis

Java ist bei dir bereits vorhanden. Maven muss nicht global installiert sein, weil das Projekt jetzt einen eigenen Wrapper mitbringt.

## GitHub

Wenn du das Projekt auf GitHub hochlaedst, wird die Plugin-JAR automatisch per GitHub Actions gebaut.

Lokale Git-Vorbereitung:

```powershell
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin <DEIN_GITHUB_REPO_URL>
git push -u origin main
```

Danach findest du die gebaute Datei auf GitHub unter deinem Repository in `Actions` als Artifact `statsrank-jar`.