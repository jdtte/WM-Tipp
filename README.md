# WM-Tippspiel

## Ausführung

Starten Sie die Anwendung auf eine der folgenden Arten und gehen Sie im Browser auf [http://localhost:8000](http://localhost:8000)!

Sie können sich mit dem Administratorlogin zugehörigem Passwort anmelden. Die Zugangsdaten sind in der Datei
`src/main/resources/application.yml` konfiguriert (Standard: admin/geheim).

### In Eclipse

Um die Anwendung zu starten, die Klasse `WmtippApplication` starten!

### Mit Maven

Mithilfe des Maven-Plugins kann die Anwendung auch ohne vorherigen Build direkt gestartet werden:

`mvn spring-boot:run`

### Aus der Kommandozeile mit dem gepackten Modul

Sofern schon ein Maven-Build ausgeführt wurde, kann das Java-Archiv auch direkt ausgeführt werden:

`java -jar wmtipp-0.0.1-SNAPSHOT.jar` 