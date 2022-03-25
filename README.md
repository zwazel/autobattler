# Autobattler

## Attribute

- Witzgruppen
    <!--tolle webseite: https://www.watson.ch/spass/lifestyle/991759181-diese-9-humor-typen-lachen-ab-jedem-sch-->
    - Flachwitze
    - Schwarzer Humor
    - Schadensfreude
    - Meme-affin
    - Deutscher Humor
- Sprachen
    - Englisch
    - Deutsch
- Grösse
    - Klein
        - Wird selten von Fernkampfwaffen getroffen
        - Macht weniger schaden an grossen
    - Gross
        - Macht starken schaden an kleinen
        - Trifft selten kleine

## Unit ideas

- Ash
    - Humor
        - Schwarzer Humor
        - Schadensfreude
        - Meme-affin
    - Fähigkeiten
        - No fighting, motivating group with emotional support
- Loic
    - Humor
        - Schwarzer Humor
    - Fähigkeiten
        - Ice hockey, rammt immer gegner und schadet sich dabei selber. knockt sich teilweise selber aus für einen
          Moment
    - Eigenschaften
        - Gross
- Dennis
    - Humor
        - Schwarzer Humor
        - Meme-affin
    - Fähigkeiten
        - Verführt männliche gegner da er so gay ist. Verführte Gegner sind abgelenkt und können nicht kämpfen,
          verfolgen den verführer.
    - Schwächen
        - Hat angst vor Giulia
- Phillip
    - Humor
        - "Alles kickt amigs chli ine"
    - Fähigkeiten
        - Spielt basketball, trifft nie. Ball macht jedoch area damage
        - Lässt Musiklaufen bei jedem Instrument das er findet. dadurch tanzt er und kann nicht mehr kämpfen, die gegner
          werden jedoch abgelenkt und die freunde motiviert
    - Schwächen
        - Schwäche wird von autos und so abgelenkt
- Tim
    - Humor
        - Schwarzer Humor (aber nicht so krass)
        - Meme-affin
        - Flachwitze
    - Fähigkeiten
        - Wirft "deine mom" witze an gegner um sie agressiv zu machen
    - Schwächen
        - Wird von Computerspielen und Computerspiel Sales abgelenkt.
- Magnus
    - Humor
        - Deutscher Humor
    - Schwächen
        - hat schwäche gegenüber frauen, ist simp. wird abgelenkt
            - Wenn er Nadina sieht, egal auf welchem Team sie ist, geht er zu ihr und vergisst das er kämpfen sollte (
              das er eine Aufgabe hat)
- Moritz
    - Humor
        - Flachwitze (aber extrem stark, doppelter effekt)
        - Deutscher Humor
    - Fähigkeiten
        - Lacht ab seinen eigenen Witzen. Sein Lachen gibt moral boost an seine mates weil das lustig.
        - Leute mit selben Humor kriegen zusätzlichen Boost
        - Geht immer auf den nächst höchst gelevelten Gegner
    - Schwäche
        - Nervt Teammitglieder, die seinen Humor nicht teilen mit seinen schlechten witze.
        - Greift immer einen Gegner an, ohne den vorherigen ganz zu besiegen
- Marc
    - Humor
        - Schwarzer Humor
        - Schadensfreude
        - Meme-affin
    - Fähigkeiten
        - idk
    - Schwäche
        - 02 simp
    - Eigenschaften
        - Gross
- Nadina
    - Humor
        - Something
    - Fähigkeiten
        - Something else
    - Schwäche
        - anotherone
- Noel Schärer
    - Humor
        - idk
    - Fähigkeiten
        - Ist so klein das Fernkampfwaffen ihn nicht treffen
    - Schwäche
        - Ist klein
- Parwiz
    - Humor
        - idk
    - Fähigkeiten
        - idk
    - Schwäche
        - Kommt immer zu spät in den Kampf, manchmal vergisst er es komplett oder verwechselt wo er eigentlich sein
          sollte (geht ins falsche team? kommt in einem anderen kampf plötzlich unerwartet wo niemand ihn ausgewählt
          hat?)
- Leon
    - Humor
        - Deutscher Humor
    - Fähigkeiten
        - Entscheidet mit einer Partie Schach das Spiel. Schach wird richtig simuliert, am Anfang random entschieden wer
          schwarz und weiss ist (nicht rassistisch gemeint). Jeder Charakter hat ein "Schach kenntniss" Level mit
          welchem entschieden wird wie weit jeder vorandenken kann.
    - Catchphrases:
        - Kazusch

## Random ideas

### Events

- Während einer gewissen Zeit kann man seine "Units" in Events auf eine Mission schicken.
- Units sind nicht in allen Kombinationen gleich effektiv und lassen sich evtl von anderen ablenken.

### Spezielle Stadien

- Ablenkung
    - Der Charakter wird für eine gewisse Zeit vom Kampf abelenkt.
- Aggression / Aggressiv
    - Einzelne Buffs auf Angriffs-Werte aber Verteidigungs und Intelligents Werte sinken.

### Antike Programmieraufgaben

- Antike Programmieraufgaben verleien einen kurzzeitigen oder permanenten Boost.

### Booster-Gegenstände

- Booster-Gegenstände sind Gegenstände, welche man seinen Charakteren zuweisen kann. Diese werden von diesem Charakter
  verbraucht, dieser erhält dafür aber für die nächste Anzahl von Kämpfen (aktiv und oder passiv) oder aber für eine
  gewisse Zeit einen Boost je nach Booster-Gegenstand, denn dieser Charakter konsumiert hat.

### Auf-/Ausüsten

- Charaktere können mit Gegenständen ausgerüstet werden, welche ihre Werte verbessern
- Charaktere sammel während Kämpfen EP und können so nach einer gewissen Zeit verbessert werden. Dies verschafft ihnen
  neue Möglichkeiten und verbessert ihre Werte

## Wünsche

- Es werden auch für die Gegner "Charakter Karten" angezeigt und nicht wie in AFK Arena nur die eigenen, bei vielen
  Effekten kann man schnell den Überblick verlieren, wie viele Gegner noch leben & wieviel Leben sie noch haben.

## Setup

For this project ro run successfully, you need to create the following environment variables:

- SPRING_DATASOURCE_DRIVER-CLASS-NAME
    - The driver class name for the database.
    - Default: com.mysql.jdbc.Driver
- SPRING_DATASOURCE_USERNAME
    - The username for the database.
    - Default: root
- SPRING_DATASOURCE_PASSWORD
    - The password for the database.
    - Example: root
    - Default:
- SPRING_DATASOURCE_URL
    - The URL for the database.
    - Default: jdbc:mysql://localhost:3306/autobattler?createDatabaseIfNotExist=true
- SPRING_JPA_HIBERNATE_DDL-AUTO
    - The DDL auto value for the database.
    - Example: create, update, create-drop, validate, or none
    - Default: update
- SPRING_JPA_SHOW-SQL
    - The show sql value for the database, if you want to see the SQL queries in the console set this to true, otherwise
      set it to false.
    - Example: true or false
    - Default: false
- ZWAZEL_APP_JWT_COOKIE_NAME
    - The name of the cookie that is used to store the JWT token.
    - Default: zwazelAutobattler
- ZWAZEL_APP_JWT_SECRET
    - The secret for the JWT token.
    - Default: zwazelAutobattlerSecret
- ZWAZEL_APP_JWT_EXPIRATION_TIME
    - The expiration time for the JWT token in MS.
    - Default: 86400000
