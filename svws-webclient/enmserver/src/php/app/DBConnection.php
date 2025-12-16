<?php
namespace wenom;

use \PDO as PDO;
use \PDOException as PDOException;
use \PDOStatement as PDOStatement;

/**
 * Diese Klasse dient dem Zugriff auf eine SQLite-Datenbank.
 */
class DBConnection {

    // Der Dateiname der SQLite-Datenbank
    private string $filename;

    // Der Name der Datenquelle (Data Source Name)
    private string $dsn;

    // Die PDO-Instanz für den Zugriff auf die SQLite-Datenbank
    private PDO | null $pdo;

    /**
     * Erstellt eine neue Datenbankverbindung für den Zugriff auf die SQLite-Datenbank an der übergebene Stelle
     *
     * @param string $path       der Name der Datenquelle (Data Source Name)
     * @param string $filename   der Name der Datenquelle (Data Source Name)
     */
    public function __construct(string $path, string $filename) {
        $this->filename = $filename;
        $dbPath = $path."/".$filename;
        $this->dsn = "sqlite:".$dbPath;
        $this->pdo = $this->connectTo($this->dsn);
    }

    /**
     * Beendet die Datenbankverbindung.
     *
     * @return void
     */
    public function __destruct() {
        $this->pdo = null;
    }

    /**
     * Beendet die Anfrage mit einem internen Server-Error (Code 500) und gibt dabei die übergebene Fehlermeldung
     * hinter dem Datenbanknamen aus.
     *
     * @param string $msg   die Fehlermeldung
     */
    private function exit500(string $msg) {
        Http::exit500("Database ($this->filename) - ".$msg);
    }

    /**
     * Erstellt eine neue Verbindung zu der übergebenen Datenquelle
     *
     * @param string $dsn   der Name der Datenquelle
     *
     * @return PDO   das PHP Data Object für die Datenbank-Verbindung
     */
    private function connectTo(string $dsn): PDO | string {
        try {
            $pdo = new PDO($dsn);
            $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $e) {
            $this->exit500("Fehler beim Öffnen (".$e->getCode()."): ".$e->getMessage());
        }
        return $pdo;
    }


    /**
     * Beginnt eine Transaktion. Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
     */
    public function beginTransaction(): void {
        try {
            $this->pdo->beginTransaction();
        } catch (PDOException $e) {
            $this->exit500("Fehler beim Erstellen der Transaction (".$e->getCode()."): ".$e->getMessage());
        }
    }

    /**
     * Führt bei einer Transaktion einen Commit aus.
     * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
     */
    public function commitTransaction(): void {
        try {
            $this->pdo->commit();
        } catch (PDOException $e) {
            $this->exit500("Fehler beim Commit der Transaction (".$e->getCode()."): ".$e->getMessage());
        }
    }

    /**
     * Erstellt eine Tabelle mit dem übergebenen SQL-Befehl
     *
     * @param string $tablename   der Name der Tabelle
     * @param string $sql         der SQL-Befehl
     */
    public function createTable(string $tablename, string $sql): void {
        try {
            $this->pdo->exec($sql);
        } catch (PDOException $e) {
            $this->exit500("Fehler beim Erstellen der Tabelle $tablename (".$e->getCode()."): ".$e->getMessage());
        }
    }

    /**
     * Leert die Tabelle mit dem übergegebenen Namen.
     *
     * @param string $tablename   der Name der Tabelle
     */
    public function clearTable(string $tablename): void {
        try {
            $this->pdo->exec("DELETE FROM $tablename");
        } catch (PDOException $e) {
            $this->exit500("Fehler beim Leeren der Tabelle $tablename (".$e->getCode()."): ".$e->getMessage());
        }
    }

    /**
     * Fügt Daten mithilfe des übergebenen SQL-Strings in die Tabelle mit dem übergegebenen Namen ein.
     *
     * @param string $tablename   der Name der Tabelle
     * @param string $sql         der SQL-Befehl
     */
    public function insertInto(string $tablename, string $sql): void {
        try {
            $this->pdo->exec($sql);
        } catch (PDOException $e) {
            $this->exit500("Fehler beim Schreiben in die Tabelle $tablename (".$e->getCode()."): ".$e->getMessage()." - Befehl: '$sql'");
        }
    }

    /**
     * Entfernt Daten aus der Tabelle mit dem übergebenen Namen und der übergebenen Lösch-Bedingung
     *
     * @param string $tablename   der Tabellenname
     * @param string $bedingung   die Lösch-Bedingung
     */
    public function dropFrom(string $tablename, string $bedingung): void {
        try {
            $sql = "DELETE FROM $tablename WHERE $bedingung";
            $this->pdo->exec($sql);
        } catch (PDOException $e) {
            $this->exit500("Fehler beim Entfernen von Daten aus der Tabelle $tablename (".$e->getCode()."): ".$e->getMessage()." - Befehl: '$sql'");
        }
    }

    /**
     * Führt die übergebene SQL-Anfrage aus und gibt alle Ergebnisse als Objekte zurück.
     * Im Fehlerfall wird null zurückgegeben.
     *
     * @param string $sql   die SQL-Anfrage
     *
     * @return array | null   ein Array mit Objekten oder null
     */
    public function queryAllOrNull(string $sql): array | null {
        try {
            return $this->pdo->query($sql, PDO::FETCH_OBJ)->fetchAll(PDO::FETCH_OBJ);
        } catch (PDOException $e) {
            return null;
        }
    }

    /**
     * Führt die übergebene SQL-Anfrage aus und gibt alle Ergebnisse als Objekte zurück.
     * Im Fehlerfall wird ein 500er Response Code erzeugt
     *
     * @param string $sql     die SQL-Anfrage
     * @param string $error   ein
     *
     * @return array   ein Array mit Objekten
     */
    public function queryAllOrExit500(string $sql, string $error): array {
        try {
            return $this->pdo->query($sql, PDO::FETCH_OBJ)->fetchAll(PDO::FETCH_OBJ);
        } catch (PDOException $e) {
            $this->exit500($error." (".$e->getCode()."): ".$e->getMessage());
        }
    }

    /**
     * Führt die übergebene SQL-Anfrage aus und gibt das Ergebnis als Objekt zurück.
     * Im Fehlerfall wird  null zurückgegeben.
     *
     * @param string $sql   die sql-Anfrage
     *
     * @return object | null   das Objekt oder null
     */
    public function querySingleOrNull(string $sql): object | null {
        try {
            return $this->pdo->query($sql)->fetchObject();
        } catch (PDOException $e) {
            return null;
        }
    }


    /**
     * Führt den übergebenen SQL-Update-Befehl aus und gibt bei Erfolg true zurück.
     *
     * @return bool true, wenn das Update erfolgreich durchgeführt wurde, und ansonsten false
     */
    public function execUpdate(string $sql): bool {
        try {
            $this->pdo->exec($sql);
            return true;
        } catch (PDOException $e) {
            return false;
        }
    }

    /**
     * Bereitet ein Statement mit dem übergenenen SQL-Befehl vor und gibt dieses zurück.
     * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
     *
     * @return PDOStatement   das Statement
     */
    public function prepareStatement(string $sql): PDOStatement {
        try {
            return $this->pdo->prepare($sql);
        } catch (PDOException $e) {
            $this->exit500("Fehler beim Aufruf von prepare (".$e->getCode()."): ".$e->getMessage());
        }
    }

    /**
     * Bindet den Wert des Parameters an das Statement.
     * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
     *
     * @param PDOStatement $statement   das Statement
     * @param string $param             der Parameter, z.B. ':id'
     * @param mixed $value              der Wert
     * @param int $type                 der PDO-Datentyp des Parameters
     */
    public function bindStatementValue(PDOStatement $statement, string $param, mixed $value, int $type): void {
        try {
            $statement->bindValue($param, $value, $type);
        } catch (PDOException $e) {
            $this->exit500("Fehler bei bindValue mit '$param' (".$e->getCode()."): ".$e->getMessage());
        }
    }

    /**
     * Führt das übergebene Statement aus.
     * Ist dies nicht erfolgreich, so wird ein Fehlercode 500 ausgeführt.
     *
     * @param PDOStatement $statement   das Statement
     */
    public function executeStatement(PDOStatement $statement): void {
        try {
            $statement->execute();
        } catch (PDOException $e) {
            $this->exit500("Fehler beim Ausführen des Statements (".$e->getCode()."): ".$e->getMessage());
        }
    }

}
