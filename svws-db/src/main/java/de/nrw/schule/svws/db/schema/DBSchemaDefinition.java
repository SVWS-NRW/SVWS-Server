package de.nrw.schule.svws.db.schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.nrw.schule.svws.csv.CsvReader;
import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.schema.csv.Datenbanksysteme;
import de.nrw.schule.svws.db.schema.csv.Datentypen;
import de.nrw.schule.svws.db.schema.csv.Fremdschluessel;
import de.nrw.schule.svws.db.schema.csv.FremdschluesselSpalte;
import de.nrw.schule.svws.db.schema.csv.PrimaerschluesselSpalte;
import de.nrw.schule.svws.db.schema.csv.Tabelle;
import de.nrw.schule.svws.db.schema.csv.TabelleDefaultDaten;
import de.nrw.schule.svws.db.schema.csv.TabelleIndex;
import de.nrw.schule.svws.db.schema.csv.TabelleIndexSpalte;
import de.nrw.schule.svws.db.schema.csv.TabelleManualSQL;
import de.nrw.schule.svws.db.schema.csv.TabelleSpalte;
import de.nrw.schule.svws.db.schema.csv.TabelleUnique;
import de.nrw.schule.svws.db.schema.csv.TabelleUniqueSpalte;
import de.nrw.schule.svws.db.schema.csv.Trigger;
import de.nrw.schule.svws.db.schema.csv.Versionen;


/**
 * Diese Klasse beinhaltet die Definition der unterschiedlichen Revision des SVWS-Datenbank-Schemas. 
 */
public class DBSchemaDefinition {

	/** die Singleton-Instanz der SVWS-Datenbank-Schema-Definition.*/
	@JsonIgnore private static DBSchemaDefinition instance;
	
	
	/** Das Java-Paket, welches die Klassen für den Datenbankzugriff beinhaltet */
	@JsonIgnore public static final String javaPackage = "de.nrw.schule.svws.db";
	
	/** Das Java-Unterpaket, welches die Klassen für die generierten SVWS-DTOs beinhaltet */
	@JsonIgnore public static final String javaDTOPackage = "dto";
	
	
    /** Enthält alle Tabellen des Schemas, die in der dazugehörigen CSV-Datei definiert wurden */
    @JsonIgnore public final List<Tabelle> tabellen;

    /** Enthält alle Tabellen des Schemas, die in der dazugehörigen CSV-Datei definiert wurden */
    @JsonIgnore private final Map<String, Tabelle> tabellenMap;
    
    /** Gibt die Reihenfolge für das Erstellen der Tabellen für die jeweiligen Revisionen an. */
    @JsonIgnore public final Map<Integer, Tabelle> tabellenSortiert;
    
    /** Enthält alle Indizes des Schema, die in der dazugehörigen CSV-Datei definiert wurden */
    @JsonIgnore public final List<TabelleIndex> indizes;

    /** Enthält alle Indizes des Schema, die in der dazugehörigen CSV-Datei definiert wurden */
    @JsonIgnore public final Map<String, TabelleIndex> indizesMap;
    
    /** Enthält alle Unique-Constraints des Schemas, die in der dazugehörigen CSV-Datei definiert wurden */
    @JsonIgnore public final Map<String, TabelleUnique> unique;

    
    /** Enthält alle Fremdschlüssel, die in der dazugehörigen CSV-Datei definiert wurden */
    @JsonIgnore public final List<Fremdschluessel> fremdschluessel;
    
    /** Enthält alle Fremdschlüssel, die in der dazugehörigen CSV-Datei definiert wurden */
    @JsonIgnore public final Map<String, Fremdschluessel> fremdschluesselMap;

    
    /** Enthält alle Tabellen des Schemas, die in der dazugehörigen CSV-Datei definiert wurden */
    @JsonIgnore public final Map<String, TabelleDefaultDaten> tabellenMitDefaultDaten;
    
    /** Enthält alle in irgendeiner Form unterstützten - auch teilunterstützte - Datenbanksysteme */
    @JsonIgnore public final Map<String, Datenbanksysteme> dbms;

    /** Enthält alle im Projekt für die Datenbank verwendeten Datentypen in den DBMS- und Java-spezifischen Benennungen */
    @JsonIgnore public final Map<String, Datentypen> datentypen;
    
    
    /** Enthält alle Revisionen des SVWS-Datenbank-Schemas */
    @JsonIgnore public final Map<Integer, Versionen> revisionen;
    
    /** Die maximale Version (-1), die bei dem SVWS-DB-Schema existiert */
    @JsonIgnore private Versionen maxVersion;
    
    /** Die maximale Revision, die bei dem SVWS-DB-Schema existiert */
    @JsonIgnore public int maxRevision;
    
    /** Die maximale Version, die für ein SVWS-DB-Schema existiert, welches nicht im Produktivbetrieb eingesetzt wird, sondern nur im Bereich der Weiterentwicklung des Servers */
    @JsonIgnore private Versionen maxVersionDeveloper;

    /** Die maximale Revision, die für ein SVWS-DB-Schema existiert, welches nicht im Produktivbetrieb eingesetzt wird, sondern nur im Bereich der Weiterentwicklung des Servers */
    @JsonIgnore public int maxRevisionDeveloper;
    
    /** Enthält alle Trigger, die unabhängig von Auto-Inkrementen auf der SVWS-DB definiert wurden. */
    @JsonIgnore public final Map<DBDriver, Map<String, Trigger>> trigger;
    
    /** Enthält alle "von Hand" definierten SQL-Befehle die bei einem Update zu einer speziellen SVWS-DB-Revision ausgeführt werden müssen */
    @JsonIgnore public final Map<DBDriver, Map<Integer, List<TabelleManualSQL>>> manualSQL;



    /**
     * Erzeugt ein neues Objekt für die Schema-Definition und liest diese dabei aus den zugehörigen CSV-Dateien ein.  
     */
    @JsonIgnore
	private DBSchemaDefinition() {
    	// Lese die im Projekt für die Datenbank verwendeten Datentypen ein
    	datentypen = CsvReader.fromResource("schema/csv/Datentypen.csv", Datentypen.class).stream().collect(Collectors.toMap(d -> d.Name, d -> d));
    	
    	// Lese die Datenbank-Revisionen ein
    	revisionen = CsvReader.fromResource("schema/csv/Versionen.csv", Versionen.class).stream().collect(Collectors.toMap(v -> v.Revision, v -> v));
    	maxVersion = revisionen.get(-1);
    	if (maxVersion == null)
			throw new RuntimeException("FEHLER: Es wurde keine gültige Schema-Revision -1 gefunden. Eine maximale Revision ist somit nicht definiert.");
    	if (maxVersion.MaxRevision == null)
			throw new RuntimeException("FEHLER: Es wurde keine gültige maximale Schema-Revision bei der Revision -1 gefunden.");
    	maxRevision = maxVersion.MaxRevision;
    	maxVersionDeveloper = revisionen.values().stream().max((a,b) -> Integer.compare(a.Revision, b.Revision)).orElse(null);
    	if (maxVersionDeveloper == null)
			throw new RuntimeException("FEHLER: Es wurde keine gültige Schema-Revision gefunden, so dass keine maximale Entwickler-Revision bestimmt werden konnte.");
    	maxRevisionDeveloper = maxVersionDeveloper.Revision;
    	
    	// Lese die unterstützten DBMS ein
    	dbms = CsvReader.fromResource("schema/csv/Datenbanksysteme.csv", Datenbanksysteme.class).stream().collect(Collectors.toMap(d -> d.Name, d -> d));

    	// Lese die Tabellen ein
    	tabellen = CsvReader.fromResource("schema/csv/SchemaTabelle.csv", Tabelle.class).stream().collect(Collectors.toList());
    	tabellenMap = tabellen.stream().collect(Collectors.toMap(t -> t.Name, t -> t));
    	for (Tabelle tab : tabellen) {
    		tab.primaerschluessel.tabelle = tab;
    		if (tab.Revision == null)
    			throw new NullPointerException("FEHLER: Die Revision bei der Tabelle " + tab.Name + " darf nicht null sein.");
    		tab.dbRevision = revisionen.get(tab.Revision);
    		if (tab.dbRevision == null)
    			throw new RuntimeException("FEHLER: Die Revision bei der Tabelle " + tab.Name + " ist ungültig.");
    		if (tab.Veraltet == null)
    			throw new NullPointerException("FEHLER: Die Revision, welche angibt ob die Tabelle " + tab.Name + " veraltet ist, darf nicht null sein. -1 signalisiert, dass die Tabelle nicht veraltet ist.");
    		tab.dbRevisionVeraltet = revisionen.get(tab.Veraltet);
    		if (tab.dbRevisionVeraltet == null)
    			throw new RuntimeException("FEHLER: Die Revision, ab wann die Tabelle " + tab.Name + " veraltet ist, ist ungültig.");
    	}
    	
    	// Lese die Spalten ein und ordne diese der entsprechenden Tabelle zu - und umgekehrt
    	CsvReader.fromResource("schema/csv/SchemaTabelleSpalte.csv", TabelleSpalte.class).stream().forEach(ts -> {
    		ts.tabelle = tabellenMap.get(ts.NameTabelle);
    		if (ts.tabelle == null)
    			throw new NullPointerException("FEHLER: Kann die Tabellenspalte keiner Tabelle zuordnen: " + ts.NameTabelle + "." + ts.NameSpalte);
    		ts.tabelle.addSpalte(ts);
    		// Übertrage ggf. Revisionen von der Tabelle auf die Spalten
    		if (ts.Revision == null) {
    			ts.dbRevision = ts.tabelle.dbRevision;
    		} else {
    			ts.dbRevision = revisionen.get(ts.Revision);
    			if (ts.dbRevision == null)
    				throw new RuntimeException("FEHLER: Die Revision bei der Tabellenspalte " + ts.NameTabelle + "." + ts.NameSpalte + " ist ungültig.");
    		}
    		if (ts.Veraltet == null) {
    			ts.dbRevisionVeraltet = ts.tabelle.dbRevisionVeraltet;
    		} else {
    			ts.dbRevisionVeraltet = revisionen.get(ts.Veraltet);
    			if (ts.dbRevisionVeraltet == null)
    				throw new RuntimeException("FEHLER: Die Revision, ab der die Tabellenspalte " + ts.NameTabelle + "." + ts.NameSpalte + " veraltet ist, ist ungültig.");    			
    		}
    	});
    	
    	// Lese die Primärschlüssel der Tabellen ein
    	CsvReader.fromResource("schema/csv/SchemaTabellePrimaerschluessel.csv", PrimaerschluesselSpalte.class).stream().forEach(pks -> {
    		// Prüfe, ob die angegeben Tabelle überhaupt definiert ist
    		var tabelle = tabellenMap.get(pks.NameTabelle);
    		if (tabelle == null)
    			throw new NullPointerException("FEHLER: Kann die Tabelle " + pks.NameTabelle + "für die Primärschlüssel-Spalte " + pks.NameSpalte + " nicht ermitteln. Die Defintion des Primärschlüssels ist nicht korrekt.");
    		// Prüfe ob die Tabellenspalte bei der Tabelle überhaupt definiert ist
    		var spalte = tabelle.getSpalte(pks.NameSpalte);
    		if (spalte == null)
    			throw new NullPointerException("FEHLER: Kann die Spalte " + pks.NameSpalte + " bei der Tabelle " + pks.NameTabelle + " nicht finden. Die Defintion des Primärschlüssels ist nicht korrekt. ");
    		// Prüfe, ob die Autoinkrement-Definition zulässig ist, sofern sie gesetzt ist.
    		if (pks.hatAutoIncrement || tabelle.primaerschluessel.hatAutoIncrement) {
    			if (tabelle.primaerschluessel.spalten.size() > 0)
    				throw new RuntimeException("FEHLER: Die Primärschlüsseldefintion für die Tabelle " + pks.NameTabelle + " kann kein Autoinkrement haben, da dieser Tabelle mehr als eine Primärschlüsselspalte zugeorndet wurde.");
    			// TODO Prüfe den Datentyp bei spalte.Datentyp
    		}
    		// Füge die Spalte beim Primärschlüssel hinzu.
    		tabelle.primaerschluessel.hatAutoIncrement = pks.hatAutoIncrement;
    		tabelle.primaerschluessel.spalten.add(spalte);
    		spalte.istPrimaerschlusselAttribut = true;
    	});
    	
    	// TODO Prüfe, ob für jede Tabelle ein Primärschlüssel definiert wurde...
    	
    	// Lese die Indizes ein und ordne diese den einzelnen Tabellen bzw. den zugehörigen Spalten zu - und umgekehrt
    	indizes = new Vector<>();
    	indizesMap = new HashMap<>();
    	CsvReader.fromResource("schema/csv/SchemaTabelleIndizes.csv", TabelleIndexSpalte.class).stream().forEach(tis -> {
    		if ((tis.Name == null) || ("".equals(tis.Name)))
    			throw new NullPointerException("FEHLER: Der Name des Index darf nicht leer sein.");
    		// Prüfe, ob die angegeben Tabelle überhaupt definiert ist
    		tis.tabelle = tabellenMap.get(tis.NameTabelle);
    		if (tis.tabelle == null)
    			throw new NullPointerException("FEHLER: Kann die Tabellenspalte des Index " + tis.Name + " keiner Tabelle zuordnen: " + tis.NameTabelle + "." + tis.NameSpalte);
    		// Prüfe ob die Tabellenspalte bei der Tabelle überhaupt definiert ist
    		tis.spalte = tis.tabelle.getSpalte(tis.NameSpalte);
    		if (tis.spalte == null)
    			throw new NullPointerException("FEHLER: Kann die Tabellenspalte des Index " + tis.Name + " keiner Tabellenspalte zuordnen: " + tis.NameTabelle + "." + tis.NameSpalte);
    		// Übertrage ggf. Revisionen von der Tabelle auf die Indexspalte
    		if (tis.Revision == null) {
    			tis.dbRevision = tis.tabelle.dbRevision;
    		} else {
    			tis.dbRevision = revisionen.get(tis.Revision);
    			if (tis.dbRevision == null)
    				throw new RuntimeException("FEHLER: Die Revision bei dem index " + tis.Name + " ist ungültig.");
    		}
    		if (tis.Veraltet == null) {
    			tis.dbRevisionVeraltet = tis.tabelle.dbRevisionVeraltet;
    		} else {
    			tis.dbRevisionVeraltet = revisionen.get(tis.Veraltet);
    			if (tis.dbRevisionVeraltet == null)
    				throw new RuntimeException("FEHLER: Die Revision, ab der der Index " + tis.Name + " veraltet ist, ist ungültig.");    			
    		}
    		// TODO Bestimme Revisionen und Prüfe, ob ggf. gemachte Revisionsangaben zur Tabelle bzw. zur Spalte passen (ist die Tabelle z.B. schon ungültig ?)  
    		// Prüfe, ob ein Index mit dem Namen bereits definiert wurde...
    		TabelleIndex tabIndex = indizesMap.get(tis.Name);
    		if (tabIndex != null) {
    			// Prüfe, ob der existierende Index, die gleichen Informationen beinhaltet, wie Tabelle und Revisionen
    			if (!tis.NameTabelle.equals(tabIndex.tabelle.Name))
    				throw new NullPointerException("FEHLER: Kann die Tabellenspalte des Index " + tis.Name + " keiner Tabelle zuordnen: " + tis.NameTabelle + "." + tis.NameSpalte);
    			if (tis.dbRevision.compareTo(tabIndex.dbRevision) != 0)
    				throw new RuntimeException("FEHLER: Die Revision bei dem Index " + tis.Name + " wurde bei einzelnen Tabellenspalten nicht einheitlich definiert");
    			if (tis.dbRevisionVeraltet.compareTo(tabIndex.dbRevisionVeraltet) != 0)
    				throw new RuntimeException("FEHLER: Die Revision, wann der Index " + tis.Name + " veraltet ist, wurde bei einzelnen Tabellenspalten nicht einheitlich definiert");
    		} else {
    			// Füge den neuen Index ein - auch bei der zugehörigen Tabelle
    			tabIndex = new TabelleIndex();
    			tabIndex.Name = tis.Name;
    			tabIndex.tabelle = tis.tabelle;
        		tabIndex.dbRevision = tis.dbRevision;
        		tabIndex.dbRevisionVeraltet = tis.dbRevisionVeraltet;
        		indizes.add(tabIndex);
        		indizesMap.put(tis.Name, tabIndex);
        		tis.tabelle.indizes.add(tabIndex);
    		}
    		tis.spalte.indizes.put(tis.Name, tabIndex);
    		tabIndex.spalten.put(tis.spalte.NameSpalte, tis.spalte);
    		tabIndex.indexSpalten.put(tis.Name, tis);
    	});
    	
    	// Lese die Indizes ein und ordne diese den einzelnen Tabellen bzw. den zugehörigen Spalten zu - und umgekehrt
    	unique = new HashMap<>(); 
    	CsvReader.fromResource("schema/csv/SchemaTabelleUniqueConstraints.csv", TabelleUniqueSpalte.class).stream().forEach(tus -> {
    		if ((tus.Name == null) || ("".equals(tus.Name)))
    			throw new NullPointerException("FEHLER: Der Name der Unique-Constraint darf nicht leer sein.");
    		// Prüfe, ob die angegeben Tabelle überhaupt definiert ist
    		tus.tabelle = tabellenMap.get(tus.NameTabelle);
    		if (tus.tabelle == null)
    			throw new NullPointerException("FEHLER: Kann die Tabellenspalte der Unique-Constraint " + tus.Name + " keiner Tabelle zuordnen: " + tus.NameTabelle + "." + tus.NameSpalte);
    		// Prüfe ob die Tabellenspalte bei der Tabelle überhaupt definiert ist
    		tus.spalte = tus.tabelle.getSpalte(tus.NameSpalte);
    		if (tus.spalte == null)
    			throw new NullPointerException("FEHLER: Kann die Tabellenspalte der Unique-Constraint " + tus.Name + " keiner Tabellenspalte zuordnen: " + tus.NameTabelle + "." + tus.NameSpalte);
    		// Übertrage ggf. Revisionen von der Tabelle auf die der Unique-Constraint-Spalte
    		if (tus.Revision == null) {
    			tus.dbRevision = tus.tabelle.dbRevision;
    		} else {
    			tus.dbRevision = revisionen.get(tus.Revision);
    			if (tus.dbRevision == null)
    				throw new RuntimeException("FEHLER: Die Revision bei der Unique-Constraint " + tus.Name + " ist ungültig.");
    		}
    		if (tus.Veraltet == null) {
    			tus.dbRevisionVeraltet = tus.tabelle.dbRevisionVeraltet;
    		} else {
    			tus.dbRevisionVeraltet = revisionen.get(tus.Veraltet);
    			if (tus.dbRevisionVeraltet == null)
    				throw new RuntimeException("FEHLER: Die Revision, ab der die Unique-Constraint " + tus.Name + " veraltet ist, ist ungültig.");    			
    		}
    		// TODO Bestimme Revisionen und Prüfe, ob ggf. gemachte Revisionsangaben zur Tabelle bzw. zur Spalte passen (ist die Tabelle z.B. schon ungültig ?)  
    		// Prüfe, ob eine Unique-Constraint mit dem Namen bereits definiert wurde...
    		TabelleUnique tabUnique = unique.get(tus.Name);
    		if (tabUnique != null) {
    			// Prüfe, ob die existierende Unique-Constraint, die gleichen Informationen beinhaltet, wie Tabelle und Revisionen
    			if (!tus.NameTabelle.equals(tabUnique.tabelle.Name))
    				throw new NullPointerException("FEHLER: Kann die Tabellenspalte der Unique-Constraint " + tus.Name + " keiner Tabelle zuordnen: " + tus.NameTabelle + "." + tus.NameSpalte);
    			if (tus.dbRevision.compareTo(tabUnique.dbRevision) != 0)
    				throw new RuntimeException("FEHLER: Die Revision bei der Unique-Constraint " + tus.Name + " wurde bei einzelnen Tabellenspalten nicht einheitlich definiert");
    			if (tus.dbRevisionVeraltet.compareTo(tabUnique.dbRevisionVeraltet) != 0)
    				throw new RuntimeException("FEHLER: Die Revision, wann die Unique-Constraint " + tus.Name + " veraltet ist, wurde bei einzelnen Tabellenspalten nicht einheitlich definiert");
    		} else {
    			// Füge die neue Unique-Constraint ein - auch bei der zugehörigen Tabelle
    			tabUnique = new TabelleUnique();
    			tabUnique.Name = tus.Name;
    			tabUnique.tabelle = tus.tabelle;
    			tabUnique.dbRevision = tus.dbRevision;
    			tabUnique.dbRevisionVeraltet = tus.dbRevisionVeraltet;    			
    			unique.put(tus.Name, tabUnique);
        		tus.tabelle.unique.put(tus.Name, tabUnique);
    		}
    		tus.spalte.unique.put(tus.Name, tabUnique);
    		tabUnique.spalten.put(tus.spalte.NameSpalte, tus.spalte);
    		tabUnique.uniqueSpalten.put(tus.Name, tus);
    	});

    	// Lese die Indizes ein und ordne diese den einzelnen Tabellen bzw. den zugehörigen Spalten zu - und umgekehrt
    	fremdschluessel = new Vector<>();
    	fremdschluesselMap = new HashMap<>(); 
    	CsvReader.fromResource("schema/csv/SchemaTabelleFremdschluessel.csv", FremdschluesselSpalte.class).stream().forEach(tfs -> {
    		if ((tfs.Name == null) || ("".equals(tfs.Name)))
    			throw new NullPointerException("FEHLER: Der Name des Fremdschlüssels darf nicht leer sein.");
    		// Prüfe, ob die angegebe Tabelle überhaupt definiert ist
    		tfs.tabelle = tabellenMap.get(tfs.NameTabelle);
    		if (tfs.tabelle == null)
    			throw new NullPointerException("FEHLER: Kann die Tabelle " + tfs.NameTabelle + " zum Fremdschlüssel " + tfs.Name + " nicht finden.");
    		// Prüfe ob die Tabellenspalte bei der Tabelle überhaupt definiert ist
    		tfs.spalte = tfs.tabelle.getSpalte(tfs.NameSpalte);
    		if (tfs.spalte == null)
    			throw new NullPointerException("FEHLER: Kann die Tabellenspalte " + tfs.NameSpalte + " der Tabelle " + tfs.NameTabelle + " beim Fremdschlüssel " + tfs.Name + " nicht finden.");
    		// Prüfe, ob die angegebe referenzierte Tabelle überhaupt definiert ist
    		tfs.tabelleReferenziert = tabellenMap.get(tfs.NameTabelleReferenziert);
    		if (tfs.tabelleReferenziert == null)
    			throw new NullPointerException("FEHLER: Kann die referenzierte Tabelle " + tfs.NameTabelleReferenziert + " beim Fremdschlüssel " + tfs.Name + " nicht finden.");
    		// Prüfe ob die referenzierte Tabellenspalte bei der referenzierte Tabelle überhaupt definiert ist
    		tfs.spalteReferenziert = tfs.tabelleReferenziert.getSpalte(tfs.NameSpalteReferenziert);
    		if (tfs.spalteReferenziert == null)
    			throw new NullPointerException("FEHLER: Kann die referenzierte Tabellenspalte " + tfs.NameSpalteReferenziert + " der referenzierten Tabelle " + tfs.NameTabelleReferenziert + " beim Fremdschlüssel " + tfs.Name + " nicht finden.");
    		// Übertrage ggf. Revisionen von der Tabelle auf die Indexspalte
    		if (tfs.Revision == null) {
    			tfs.dbRevision = tfs.tabelle.dbRevision;
    		} else {
    			tfs.dbRevision = revisionen.get(tfs.Revision);
    			if (tfs.dbRevision == null)
    				throw new RuntimeException("FEHLER: Die Revision bei dem Fremdschlüssel " + tfs.Name + " ist ungültig.");
    		}
    		if (tfs.Veraltet == null) {
    			tfs.dbRevisionVeraltet = tfs.tabelle.dbRevisionVeraltet;
    		} else {
    			tfs.dbRevisionVeraltet = revisionen.get(tfs.Veraltet);
    			if (tfs.dbRevisionVeraltet == null)
    				throw new RuntimeException("FEHLER: Die Revision, ab der der Fremdschlüssel " + tfs.Name + " veraltet ist, ist ungültig.");    			
    		}
    		// TODO Bestimme Revisionen und Prüfe, ob ggf. gemachte Revisionsangaben zur Tabelle bzw. zur Spalte passen (ist die Tabelle z.B. schon ungültig ?)  
    		// Prüfe, ob ein Fremdschlüssel mit dem Namen bereits definiert wurde...
    		Fremdschluessel fk = fremdschluesselMap.get(tfs.Name);
    		if (fk != null) {
    			// Prüfe, ob der existierende Index, die gleichen Informationen beinhaltet, wie Tabelle und Revisionen
    			if (!tfs.NameTabelle.equals(fk.tabelle.Name))
    				throw new NullPointerException("FEHLER: Fehler bei der Definition des Fremdschlüssels " + tfs.Name + ": Der Name der zugehörigen Tabelle ist nicht eindeutig (" + tfs.NameTabelle + " bzw. " + fk.tabelle.Name + ")");
    			if (!tfs.NameTabelleReferenziert.equals(fk.tabelleReferenziert.Name))
    				throw new NullPointerException("FEHLER: Fehler bei der Definition des Fremdschlüssels " + tfs.Name + ": Der Name der referenzierten Tabelle ist nicht eindeutig (" + tfs.NameTabelleReferenziert + " bzw. " + fk.tabelleReferenziert.Name + ")");
    			if (tfs.dbRevision.compareTo(fk.dbRevision) != 0)
    				throw new RuntimeException("FEHLER: Die Revision bei dem Fremdschlüssel " + tfs.Name + " wurde nicht einheitlich definiert");
    			if (tfs.dbRevisionVeraltet.compareTo(fk.dbRevisionVeraltet) != 0)
    				throw new RuntimeException("FEHLER: Die Revision, ab wann der Fremdschlüssel " + tfs.Name + " veraltet ist, wurde nicht einheitlich definiert");
    			if (!tfs.OnUpdate.equals(fk.OnUpdate))
    				throw new NullPointerException("FEHLER: Fehler bei der Definition des Fremdschlüssels " + tfs.Name + ": Die Reaktion bei OnUpdate ist nicht eindeutig festgelegt.");
    			if (!tfs.OnDelete.equals(fk.OnDelete))
    				throw new NullPointerException("FEHLER: Fehler bei der Definition des Fremdschlüssels " + tfs.Name + ": Die Reaktion bei OnDelete ist nicht eindeutig festgelegt.");
    		} else {
    			// Füge den neuen Index ein - auch bei der zugehörigen Tabelle
    			fk = new Fremdschluessel();
    			fk.Name = tfs.Name;
    			fk.tabelle = tfs.tabelle;
    			fk.tabelleReferenziert = tfs.tabelleReferenziert;
        		fk.dbRevision = tfs.dbRevision;
        		fk.dbRevisionVeraltet = tfs.dbRevisionVeraltet;
        		fk.OnUpdate = tfs.OnUpdate;
        		fk.OnDelete = tfs.OnDelete;
        		fremdschluessel.add(fk);
        		fremdschluesselMap.put(fk.Name, fk);
        		fk.tabelle.fremdschluessel.add(fk);
    		}
    		fk.spalten.add(tfs.spalte);
    		fk.spaltenReferenziert.add(tfs.spalteReferenziert);
    	});

    	
    	// Lese ein, welche Tabellen Default-Daten beinhalten und ordne diesen anschließend das zugehörige Tabellen-Objekt zu
    	tabellenMitDefaultDaten = CsvReader.fromResource("schema/csv/SchemaDefaultDaten.csv", TabelleDefaultDaten.class).stream().collect(Collectors.toMap(t -> t.NameTabelle, t -> t));
    	for (TabelleDefaultDaten td : tabellenMitDefaultDaten.values()) {
    		td.tabelle = tabellenMap.get(td.NameTabelle);
    		if (td.tabelle == null)
    			throw new NullPointerException("FEHLER: Kann die Tabelle für die Default-Daten nicht finden: " + td.NameTabelle);
    		if (td.Revision == null)
    			throw new NullPointerException("FEHLER: Die Revision für die letzten Änderungen bei den Default-Daten der Tabelle " + td.NameTabelle + " darf nicht null sein.");
    		td.dbRevision = revisionen.get(td.Revision);
    		if (td.dbRevision == null)
    			throw new RuntimeException("FEHLER: Die Revision für die letzten Änderungen bei den Default-Daten der Tabelle " + td.NameTabelle + " ist ungültig.");
    	}
    	
    	
    	trigger = new HashMap<>();
    	for (DBDriver dbms : DBDriver.values()) {
    		trigger.put(dbms, new HashMap<String, Trigger>());
    	}
    	CsvReader.fromResource("schema/csv/SchemaTabelleTrigger.csv", Trigger.class).stream().forEach(tr -> {
    		if ((tr.Name == null) || ("".equals(tr.Name)))
    			throw new NullPointerException("FEHLER: Der Name des Triggers darf nicht leer sein.");
    		// Prüfe, ob die angegebe Tabelle überhaupt definiert ist
    		tr.tabelle = tabellenMap.get(tr.NameTabelle);
    		if (tr.tabelle == null)
    			throw new NullPointerException("FEHLER: Kann die Tabelle " + tr.NameTabelle + " zum Trigger " + tr.Name + " nicht finden.");
    		// Übertrage ggf. Revisionen von der Tabelle auf die der Unique-Constraint-Spalte
    		if (tr.Revision == null) {
    			tr.dbRevision = tr.tabelle.dbRevision;
    		} else {
    			tr.dbRevision = revisionen.get(tr.Revision);
    			if (tr.dbRevision == null)
    				throw new RuntimeException("FEHLER: Die Revision bei der Unique-Constraint " + tr.Name + " ist ungültig.");
    		}
    		if (tr.Veraltet == null) {
    			tr.dbRevisionVeraltet = tr.tabelle.dbRevisionVeraltet;
    		} else {
    			tr.dbRevisionVeraltet = revisionen.get(tr.Veraltet);
    			if (tr.dbRevisionVeraltet == null)
    				throw new RuntimeException("FEHLER: Die Revision, ab der die Unique-Constraint " + tr.Name + " veraltet ist, ist ungültig.");    			
    		}
    		tr.dbDriver = DBDriver.fromString(tr.dbms);
    		if (tr.dbDriver == null)
    			throw new NullPointerException("FEHLER: Das DBMS '" + tr.dbms + "' zum Trigger " + tr.Name + " ist nicht definiert.");
    		trigger.get(tr.dbDriver).put(tr.Name, tr);
    		tr.tabelle.trigger.add(tr);
    	}); 

    	
    	manualSQL = new HashMap<>();
    	for (DBDriver dbms : DBDriver.values()) {
    		var map = new HashMap<Integer, List<TabelleManualSQL>>();
    		manualSQL.put(dbms, map);
    		for (Integer rev : revisionen.keySet()) {
    			map.put(rev, new Vector<TabelleManualSQL>());
    		}
    	}
    	CsvReader.fromResource("schema/csv/SchemaTabelleManualSQL.csv", TabelleManualSQL.class).stream().forEach(msql -> {
    		// Prüfe, ob die angegebe Tabelle überhaupt definiert ist
    		msql.tabelle = tabellenMap.get(msql.NameTabelle);
    		if (msql.tabelle == null)
    			throw new NullPointerException("FEHLER: Kann die für den SQL-Befehl '" + msql.Kommentar + "' benötigte Tabelle " + msql.NameTabelle + " nicht finden.");
			msql.dbRevision = revisionen.get(msql.Revision);
			if (msql.dbRevision == null)
				throw new RuntimeException("FEHLER: Die Revision bei dem SQL-Befehl '" + msql.Kommentar + "' ist ungültig.");
    		if (msql.Veraltet == null) {
    			msql.dbRevisionVeraltet = msql.tabelle.dbRevisionVeraltet;
    		} else {
    			msql.dbRevisionVeraltet = revisionen.get(msql.Veraltet);
    			if (msql.dbRevisionVeraltet == null)
    				throw new RuntimeException("FEHLER: Die Revision, ab der der SQL-Befehl " + msql.Kommentar + " veraltet ist, ist ungültig.");    			
    		}
    		if ("ALL".equals(msql.dbms)) {
    			msql.dbDriver = null;
    			for (DBDriver dbDriver : DBDriver.values())
    				manualSQL.get(dbDriver).get(msql.Revision).add(msql);
    		} else {
	    		msql.dbDriver = DBDriver.fromString(msql.dbms);
	    		if (msql.dbDriver == null)
	    			throw new NullPointerException("FEHLER: Das DBMS '" + msql.dbms + "' für den SQL-Befehl " + msql.Kommentar + " ist nicht definiert.");
	    		manualSQL.get(msql.dbDriver).get(msql.Revision).add(msql);
    		}
    		msql.tabelle.manualSQL.add(msql);
    	});
    	
    	// Initialisiere die Tabellenreihenfolge für alle Revisionen
    	tabellenSortiert = new HashMap<>();
    	for (Versionen version : revisionen.values()) {
    		int rev = version.Revision;
    		for (Tabelle t : tabellen) {
    			t.sortierung.put(rev, getTabelleSortierKriterium(rev, 0, t));
    		}
    	}
	}
    
    
    /**
     * Diese Methode wird intern genutzt, um die Sortierreihenfolge beim Erstellen der Tabellen festzulegen.
     * Diese Methode dient zum Start der Rekursion. 
     * 
     * @param rev        die Revision, für die die Sortierreihenfolge festgelegt wird. 
     * @param recDepth   die Rekursionstiefe, welche für die Namenszuordnung genutzt wird.
     * @param t          die aktuell zu untersuchende Tabelle, deren Fremdschlüssel weiter untersucht werden. 
     * 
     * @return ein String, der den Namen für die Sortierung der Tabellen bei der entsprechenden Revision festlegt, 
     *         oder null, falls dieser Aufruf zu einer in der Revision ungültigen Tabelle führt 
     */
    private String getTabelleSortierKriterium(int rev, int recDepth, Tabelle t) {
    	List<Tabelle> tabellen = new Vector<>();
    	return getTabelleSortierKriterium(rev, recDepth, t, tabellen);
    }
    
    
    /**
     * Diese Methode wird intern genutzt, um die Sortierreihenfolge beim Erstellen der Tabellen festzulegen.
     * Diese Methode wird für die rekursiven Aufrufe genutzt.
     * 
     * @param rev        die Revision, für die die Sortierreihenfolge festgelegt wird. 
     * @param recDepth   die Rekursionstiefe, welche für die Namenszuordnung genutzt wird.
     * @param t          die aktuell zu untersuchende Tabelle, deren Fremdschlüssel weiter untersucht werden.
     * @param tabellen   die Tabellen, die bereits rekursiv besucht wurden. 
     * 
     * @return ein String, der den Namen für die Sortierung der Tabellen bei der entsprechenden Revision festlegt, 
     *         oder null, falls dieser Aufruf zu einer in der Revision ungültigen Tabelle führt 
     */
    private String getTabelleSortierKriterium(int rev, int recDepth, Tabelle t, List<Tabelle> tabellen) {
    	// Prüfe, ob die Revision der Tabelle in Ordnung ist
    	if (((rev == -1) && (t.dbRevisionVeraltet.Revision == -1))
			|| ((rev != -1) && (rev >= t.dbRevision.Revision) && ((t.dbRevisionVeraltet.Revision == -1) || (rev < t.dbRevisionVeraltet.Revision)))) {
    		// Revision in Ordnung. Prüfe, nun, ob es mit der neuen Tabelle zu einer Schleife gekommen ist
    		if (tabellen.contains(t)) {
    			String s = "Fehler beim Erstellen des Datenbankschemas: Es kann keine geeignete Sortierreihenfolge erstellt werden, da die Fremdschlüssel-Bedingungen Schleifen beinhalten. "
    					+ System.lineSeparator() + "Tabelle: " + t.Name
    					+ System.lineSeparator() + "vorige Tabellen: ";
    			for (Tabelle tab: tabellen)
    				s += System.lineSeparator() + "    -> " + tab.Name; 
    			s += System.lineSeparator() + "Umgekehrte Reihenfolge der Fremdschlüssel: ";
    			throw new RuntimeException(s);
    		}
    		// Keine Schleife... Prüfe nun alle in der Revision gültigen Fremdschlüssel der Tabelle
    		tabellen.add(t);
    		String result = t.Name + recDepth;
    		for (Fremdschluessel fk : t.getFremdschluessel(rev)) {
    			String tmp;
    			try {
    				tmp = getTabelleSortierKriterium(rev, recDepth+1, fk.tabelleReferenziert, tabellen);
    			} catch(RuntimeException e) {
    				String s = "    -> " + fk.Name + "[ " 
    			      + fk.tabelle.Name + fk.spalten.stream().map(sp -> sp.NameSpalte).collect(Collectors.joining(", ", "(", ")"))
    			      + " -> "
    			      + fk.tabelleReferenziert.Name + fk.spaltenReferenziert.stream().map(sp -> sp.NameSpalte).collect(Collectors.joining(", ", "(", ")"))
    				  + " ]";
    				throw new RuntimeException(e.getMessage() + System.lineSeparator() + s);
    			}
    			if (tmp == null)
    				continue;
    			if (result.compareTo(tmp) < 0)
    				result = tmp;
    		}
    		tabellen.remove(t);
    		return result;
    	}
    	return null;
    }

    
    /**
     * Liefert die Tabelle mit dem angegebenen Namen.
     * 
     * @param name   der Name der Tabelle
     * 
     * @return die Tabelle
     */
    public Tabelle getTabelleByName(String name) {
    	return tabellenMap.get(name);
    }
    
    /**
     * Liefert alle Tabellen, die irgendwann mal definiert waren, d.h. unabhängig von der aktuellen Revision.
     * 
     * @return eine Liste mit allen irgendwann definierten Tabellen
     */
    public List<Tabelle> getTabellen() {
    	return this.tabellen;
    }
    
    
    /**
     * Liefert alle Tabellen, welche in der angegebenen Revision definiert sind. 
     * 
     * @param rev   die SVWS-DB-Revision
     * 
     * @return eine Liste mit den definierten Tabellen
     */
    @JsonIgnore 
    public List<Tabelle> getTabellen(int rev) {
    	return this.tabellen.stream()
    			.filter(t -> ((rev == -1) && (t.dbRevisionVeraltet.Revision == -1))
    					|| ((rev != -1) && (rev >= t.dbRevision.Revision) && ((t.dbRevisionVeraltet.Revision == -1) || (rev < t.dbRevisionVeraltet.Revision))))
    			.collect(Collectors.toList());
    }

    

    /**
     * Liefert alle Tabellen, welche in der angegebenen Revision definiert sind in der Reihenfolge bei der Erstellung der Tabellen. 
     * 
     * @param rev   die SVWS-DB-Revision
     * 
     * @return eine Liste mit den in der Revision definierten Tabellen
     */
    @JsonIgnore 
    public List<Tabelle> getTabellenSortiert(int rev) {
    	return this.tabellen.stream()
    			.filter(t -> ((rev == -1) && (t.dbRevisionVeraltet.Revision == -1))
    					|| ((rev != -1) && (rev >= t.dbRevision.Revision) && ((t.dbRevisionVeraltet.Revision == -1) || (rev < t.dbRevisionVeraltet.Revision))))
    			.sorted((a,b) -> a.compareTo(rev, b))
    			.collect(Collectors.toList());
    }

    
    /**
     * Liefert alle Tabellen, welche in der angegebenen Revision definiert sind in der umgekehrten Reihenfolge wie bei der Erstellung der Tabellen. 
     * 
     * @param rev   die SVWS-DB-Revision
     * 
     * @return eine Liste mit den in der Revision definierten Tabellen
     */
    @JsonIgnore 
    public List<Tabelle> getTabellenSortiertAbsteigend(int rev) {
    	return this.tabellen.stream()
    			.filter(t -> ((rev == -1) && (t.dbRevisionVeraltet.Revision == -1))
    					|| ((rev != -1) && (rev >= t.dbRevision.Revision) && ((t.dbRevisionVeraltet.Revision == -1) || (rev < t.dbRevisionVeraltet.Revision))))
    			.sorted((a,b) -> b.compareTo(rev, a))
    			.collect(Collectors.toList());
    }
    
    
    /**
     * Liefert alle Tabellen mit Default-Daten, welche in der angegebenen Revision definiert sind in der Reihenfolge bei der Erstellung der Tabellen. 
     * 
     * @param rev   die SVWS-DB-Revision
     * 
     * @return eine Liste mit den in der Revision definierten Tabellen, die Default-Daten haben
     */
    @JsonIgnore 
    public List<Tabelle> getTabellenDefaultDatenSortiert(int rev) {
    	return this.tabellenMitDefaultDaten.values().stream()
    			.map(tdd -> tdd.tabelle)
    			.filter(t -> ((rev == -1) && (t.dbRevisionVeraltet.Revision == -1))
    					|| ((rev != -1) && (rev >= t.dbRevision.Revision) && ((t.dbRevisionVeraltet.Revision == -1) || (rev < t.dbRevisionVeraltet.Revision))))
    			.sorted((a,b) -> a.compareTo(rev, b))
    			.collect(Collectors.toList());
    }
    
    
	/**
	 * Erstellt eine Liste aller Tabellen mit Default-Daten,
	 * wo die Daten bei der angebenen Revision verändert wurden.
	 * 
	 * @param rev   die Revision, bei der die Default-Daten verändert wurden
	 * 
	 * @return die Liste mit den Tabellennamen in Erstell-Reihenfolge
	 */
    @JsonIgnore
    public List<Tabelle> getTabellenDefaultDatenUpdatesSortiert(int rev) {
    	return this.tabellenMitDefaultDaten.values().stream()
    			.filter(tdd -> tdd.dbRevision.Revision == rev)
    			.map(tdd -> tdd.tabelle)
    			.filter(t -> ((rev == -1) && (t.dbRevisionVeraltet.Revision == -1))
    					|| ((rev != -1) && (rev >= t.dbRevision.Revision) && ((t.dbRevisionVeraltet.Revision == -1) || (rev < t.dbRevisionVeraltet.Revision))))
    			.sorted((a,b) -> a.compareTo(rev, b))
    			.collect(Collectors.toList());    	
    }
    
    
    
    /**
     * Liefert die SQL-Befehle zum Anlegen von Default-SVWS-Benutzern
     * bei einem leeren Schema in Abhängigkeit von der übergebenen
     * Revision.
     * 
     * @param rev    die Revision
     * 
     * @return eine Liste mit den SQL-Befehlen
     */
    @JsonIgnore 
    public List<String> getCreateBenutzerSQL(int rev) {
    	Vector<String> result = new Vector<>();
    	if (rev == 0) {
    		result.add("INSERT INTO Users(ID,US_Name,US_LoginName,US_UserGroups,US_Privileges) VALUES "
    			 	 + "(1,'Administrator','Admin','1;2;3','$');");
    		result.add("INSERT INTO Usergroups(UG_ID, UG_Bezeichnung, UG_Kompetenzen, UG_Nr) VALUES "
    				 + "(1, 'Administrator', '$', 1),"
    				 + "(2, 'Lehrer', '11;21;22;81;31;61', 3),"
    				 + "(3, 'Sekretariat', '11;12;13;14;21;81;82;83;31;32;33;34;91;92;93;94;95;61;62;71', 4);");
    		return result;
    	}
    	result.add("INSERT INTO Credentials(ID, Benutzername, BenutzernamePseudonym, Initialkennwort, PasswordHash, RSAPublicKey, RSAPrivateKey, AES) VALUES "
    			 + "(1, 'Admin', NULL, NULL, NULL, NULL, NULL, NULL);");
		result.add("INSERT INTO BenutzerAllgemein(ID,AnzeigeName,CredentialID) VALUES "
				 + "(1,'Administrator',1);");
		result.add("INSERT INTO Benutzer(ID, Typ, Allgemein_ID, Lehrer_ID, Schueler_ID, Erzieher_ID, IstAdmin) VALUES "
				 + "(1, 0, 1, NULL, NULL, NULL, 1)");
		result.add("INSERT INTO Benutzergruppen(ID, Bezeichnung, IstAdmin) VALUES "
				 + "(1, 'Administrator', 1),"
				 + "(2, 'Lehrer', 0),"
				 + "(3, 'Sekretariat', 0);");
		result.add("INSERT INTO BenutzergruppenMitglieder(Gruppe_ID, Benutzer_ID) VALUES (1, 1);");
		result.add("INSERT INTO BenutzergruppenKompetenzen(Gruppe_ID, Kompetenz_ID) VALUES "
				 + "(2, 11), (2, 21), (2, 22), (2, 31), (2, 61), (2, 81),"
				 + "(3, 11),(3, 12),(3, 13),(3, 14),(3, 21),(3, 31),(3, 32),(3, 33),(3, 34),(3, 61),(3, 62),(3, 71),(3, 81),(3, 82),(3, 83),(3, 91),(3, 92),(3, 93),(3, 94),(3, 95);");
    	return result;
    }


	/**
	 * Gibt die Instanz der Klasse DBSchemaDefinition zurück. Ist keine Instanz vorhanden, so
	 * wird sie zunächst mit dem privaten Konstruktor erzeugt.
	 * 
	 * @return die Instanz der Klasse DBSchemaDefinition mit den Informationen des SVWS-Datenbank-Schemas
	 */
	public static DBSchemaDefinition getInstance() {
		if (instance == null) {
			instance = new DBSchemaDefinition(); 
		}
		return instance;
	}
    
	
	
	
	/**
	 * Bestimmt das Package für die Klasse der angegebenen Tabelle
	 * 
	 * @param tabellenname   der Name der Tabelle
	 * 
	 * @return das Package für die Klasse
	 */
	public String getPackage(String tabellenname) {
		Tabelle tabelle = this.tabellenMap.get(tabellenname);
		if (tabelle == null)
			return null;
		if ((tabelle.JavaPackage == null) || (tabelle.JavaPackage.isEmpty()))
			return javaPackage + "." + javaDTOPackage;		
		return javaPackage + "." + javaDTOPackage + "." + tabelle.JavaPackage;
	}
	
	

	/**
	 * Bestimmt den Pfad für ein Java-Package der Klasse für die angegebene Tabelle  
	 * 
	 * @param tabellenname   der Name der Tabelle
	 * 
	 * @return der Pfad des Java-Packages
	 */
	public String getPackagePath(String tabellenname) {
		String tmp = getPackage(tabellenname);
		if (tmp == null)
			return null;
		return tmp.replace(".", "/");
	}

	
	
	/**
	 * Bestimmt den Pfad für ein Java-Package der Klasse für die angegebene Tabelle
	 * und ergänzt diesen um der angegebenen Pfad zum Quell-Ordner, wo die Packages
	 * liegen.  
	 * 
	 * @param tabellenname   der name der Tabelle
	 * @param src_path       der Pfad zu den Java-Quellen
	 * 
	 * @return der vollständige Pfad des Java-Packages
	 */
	public String getPackagePath(String tabellenname, String src_path) {
		String tmp = getPackagePath(tabellenname);
		if (tmp == null)
			return null;
		if ((src_path == null) || src_path.isEmpty())
			return tmp;
		return src_path + ((src_path.endsWith("/") ? "" : "/") + tmp);
	}


	
	/**
	 * Bestimmt für den SQL-Datentyp der Spalte den zugehörigen Java-Datentyp
	 * 
	 * @param spalte   die Beschreibung der Spalte
	 * 
	 * @return der Java-Datentyp der Spalte
	 */
	public String getJavatype(TabelleSpalte spalte) {
		return datentypen.get(spalte.Datentyp).java;
	}
	
}
