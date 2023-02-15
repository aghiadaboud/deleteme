Die Terminplaner Webanwendung ist eine funktionelle Anmeldungs- und
Reservierungsapp, die Studierende bei ihrer Anmeldung zu Übungen unterstützen soll. Auch
Organisatoren sollen dadurch den ganzen Prozess einfacher managen.  
Um in der Anwendung anzumelden und die Services zu benutzen, Users müssen einen
Github Account haben und sie müssen in einer bestimmten Organisation Mitglied sein.
Zum Beispiel, eine Organisation, die ihre Universität/Professor gegründet hat.  
Wenn die Übungsreservierungen fertig sind und alle StudierendenGruppen feststehen,
verteilt das System die Tutoren auf die Übungsgruppen zufällig und erstellt für jede Gruppe
ein Github-Repository, anschließend fügt das System alle Studierenden einer Gruppe und den
zugeordneten Tutor zu der Repository hinzu.

## Stakeholder

Rollen:  
- Studenten  
- Tutoren  
- Organisatoren

| Rolle       | Erwartungshaltung                                                                                                |
|:------------|:-----------------------------------------------------------------------------------------------------------------|
| Student     | Einfache Terminbuchung und eine übersichtliche Anmeldeseite                                                      |
| Tutor       | Detailierte Übersicht der Tutorenzuteilung für Gruppen                                                           |
| Organisator | Änderungen leichter durchzuführen bei Verschieben, Löschen, Hinzufügen oder verteilen der Studenten und Terminen |

## Anleitung zur Benutzung dieser Anwendung

- GitHub Konto erstellen.  
- Bei GitHub anmelden.  
- Organisation auf GitHub erstellen.  
- Bei Developer settings GitHub App erstellen.  
- Bei Developer settings OAuth App erstellen.  
- Beide Apps auf der Organisation installieren.  
- Daten aus den beiden Apps entnehmen.  
- APP_ID, CLIENT_ID, CLIENT_SECRET, INSTALLATIONS_ID, ORGANISATION_NAME in einer `.env` Datei abspeichern.  
- Private key generieren.  
- Private key umwandeln mit dem
Befehl `openssl pkcs8 -topk8 -inform PEM -outform DER -in key.pem -out key.der -nocrypt` 
- Anwendung im Terminal mit den Befehl `docker-compose up` starten.

## Benutzte Frameworks/Tools/Programme/APIs/Libraries

- Docker und Docker-Compose
- Spring / Spring Boot / Spring Data JDBC
- Gradle
- MySQL
- Thymeleaf
- CSS und Bootstrap
- [Spring Security und OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)(Login über Github)
- GitHub App und [diese Java Bibliothek](https://github-api.kohsuke.org/index.html), um mit [Github REST API](https://docs.github.com/en/rest) zu kommunizieren, sodass die Anwendung sich gegenüber GitHub authentifizieren und innerhalb einer Github-Organisation neue Repositories anlegen kann

## Domain Storytelling

![](images/Individualanmeldung.png)
![](images/Gruppenanmeldung.png)

## Glossar

Das folgende Glossar erklärt die Begriffe, die für die Nutzung für den Terminplaner verwendet werden:

| Name        | Erwartungshaltung/Aufgabe                                                                                                                         |  
|:------------|:--------------------------------------------------------------------------------------------------------------------------------------------------|  
| Übung       | Wird von Tutoren gehalten und von Studenten besucht                                                                                               |  
| Zeitslot    | Zeitpunkte wann man die  verschiedenen Übungen reservieren kann                                                                                   |  
| Repository  | Wird ein Classroomlink für jede Gruppe bei Github erstellt                                                                                        |  
| GitHub      | Zuständiges System für die Authentifizierung, Autorisierung und Erstellung der Repos                                                              |  
| Gruppe      | Besteht aus mehreren Studenten, die gemeinsam die zugeordnete Übung besuchen                                                                      |  
| Student     | Gruppen erstellen oder einer beitreten. Zur Übungen anmelden                                                                                      |  
| Tutor       | Terminübersicht und Zuteilung für Gruppen einsehen                                                                                                |  
| Organisator | Erstellen, Verwalten und ändern von Gruppen,Übungen und Zeitslots. Organisatorn Können zusätzlich Studierende beliebig in den Gruppen verschieben |

## Design Decisions:

**Architektur**

N-layered architecture.  
MVC(Model-View-Controller).

**Datenbank**

Development: H2 Database.  
Production: MySQL Database running in a Docker container.

**Systemaufteilung**

Für die Systemaufteilung habe ich mich für einen Monolithen entschieden, da die Anwendung klein ist und deshalb die
Aufteilung in Microservices oder SCSs(Self-Contained-Systems) nicht erforderlich ist.
