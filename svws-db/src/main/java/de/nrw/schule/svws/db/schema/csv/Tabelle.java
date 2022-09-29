package de.nrw.schule.svws.db.schema.csv;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.nrw.schule.svws.csv.converter.Boolean01ConverterDeserializer;
import de.nrw.schule.svws.csv.converter.Boolean01ConverterSerializer;
import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.schema.DBSchemaDefinition;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;

/**
 * Diese Klasse repräsentiert ein DTO-Objekt für den Zugriff auf die CSV-Datei, welche
 * die Tabellen in dem SVWS-Datenbank-Schema definiert.
 */
public class Tabelle {
	
	/** Der eindeutige Name der Tabelle */
    @JsonProperty public String Name;
	
	/** Die Revision, ab welcher die Tabelle im Schema existiert */
    @JsonProperty public Long Revision;
	
	/** 
	 * Eine Version, ab der die Tabelle veraltet ist und aus dem Schema entfernt werden soll 
     * oder: -1 falls die Tabelle auch bei der neuesten Version noch aktuell ist 
     */
    @JsonProperty public Long Veraltet;
	
	/** Gibt an, ob die Tabelle bei der Migration einer alten Schild NRW-Version (2.x) in die SVWS-DB (Revision 0) berücksichtigt werden soll */
    @JsonSerialize(using=Boolean01ConverterSerializer.class)
    @JsonDeserialize(using=Boolean01ConverterDeserializer.class)	
    @JsonProperty public Boolean Migration;
    
	/** Gibt an, ob die Tabelle bei dem Import oder Export nach SQLite berücksichtigt werden soll */
    @JsonSerialize(using=Boolean01ConverterSerializer.class)
    @JsonDeserialize(using=Boolean01ConverterDeserializer.class)	
    @JsonProperty public Boolean ImpExp;

	/** Gibt den Namen der Java-DTO-Klasse an, welche für den Zugriff auf die Tabelle erzeugt werden soll */
    @JsonProperty private String JavaKlasse;

	/** Gibt das Sub-Package von de.nrw.schule.svws.db.dto an oder NULL, falls die Klasse direkt in diesem Package liegen soll */
    @JsonProperty public String JavaPackage;

	/** Ein Kommentar, welcher bei dem Javadoc-Kommentar der DTO-Klasse integriert werden soll. */
    @JsonProperty public String JavaKommentar;

    
    /** Gibt den jeweiligen String an, der für die Sortierung der Tabellen bei der jeweiligen Revision relevant ist. */
    @JsonIgnore public HashMap<Long, String> sortierung = new HashMap<>();
    
    /** Die Revision, bei der die Tabelle erstellt wird */
    @JsonIgnore public SchemaRevisionen dbRevision;

    /** Die Revision, ab der die Tabelle veraltet ist, oder null */
    @JsonIgnore public SchemaRevisionen dbRevisionVeraltet;
    
    /** Die Primärschlüsseldefinition für diese Tabelle */
    @JsonIgnore public Primaerschluessel primaerschluessel = new Primaerschluessel();
    
    /** Eine HashMap mit allen der Tabelle zugeordneten Spalten */
    @JsonIgnore private final HashMap<String, TabelleSpalte> spalten = new HashMap<>();
    
    /** Eine Map mit den Indizes, die auf dieser Tabelle definiert sind. */ 
    @JsonIgnore public final Vector<TabelleIndex> indizes = new Vector<>();

    /** Eine Map mit den Unique-Constraints, die auf dieser Tabelle definiert sind. */ 
    @JsonIgnore public final HashMap<String, TabelleUnique> unique = new HashMap<>();

    /** Eine Map mit den Fremdschlüsseln, die auf dieser Tabelle definiert sind. */ 
    @JsonIgnore public final Vector<Fremdschluessel> fremdschluessel = new Vector<>();
    
    /** Enthält alle Trigger, die unabhängig von Auto-Inkrementen auf der Tabelle definiert wurden. */
    @JsonIgnore public final Vector<Trigger> trigger = new Vector<>();

    /** Enthält alle manuellen SQL-Befehle, die auf der Tabelle definiert wurden. */
    @JsonIgnore public final Vector<TabelleManualSQL> manualSQL = new Vector<>();
    

    /**
     * Prüft, ob die Tabelle in der angegebenen Revision definiert ist oder nicht.
     * 
     * @param rev   die zu prüfende Revision
     * 
     * @return true, falls die Tabelle in der Revision definiert ist und ansonsten false
     */
    @JsonIgnore
    public boolean isDefined(final long rev) {
    	final long revision = (rev < 0) ? SchemaRevisionen.maxRevision.revision : rev;
    	return (revision >= dbRevision.revision) && ((dbRevisionVeraltet.revision < 0) || (revision < dbRevisionVeraltet.revision));
    }
    
    
    /**
     * Liefert die Tabellenspalten in der durch das Feld Sortierung definierten Reihenfolge
     * 
     * @return die Tabellenspalten in der durch das Feld Sortierung definierten Reihenfolge
     */
    @JsonIgnore 
    public List<TabelleSpalte> getSpalten() {
    	return spalten.values().stream().sorted((a,b) -> { return a.Sortierung.compareTo(b.Sortierung); }).collect(Collectors.toList());
    }
    
    
    /**
     * Liefert die Tabellenspalten, die in der angegeben Revision definiert sind in der durch das 
     * Feld Sortierung definierten Reihenfolge.
     * 
     * @param rev   die Revision, in der die Tabellenspalte definiert sein muss
     * 
     * @return die Tabellenspalten in der durch das Feld Sortierung definierten Reihenfolge
     */
    @JsonIgnore
    public List<TabelleSpalte> getSpalten(final long rev) {
    	final long revision = (rev < 0) ? SchemaRevisionen.maxRevision.revision : rev;
    	return spalten.values().stream()
    			.filter(sp -> (revision >= sp.dbRevision.revision) && ((sp.dbRevisionVeraltet.revision < 0) || (revision < sp.dbRevisionVeraltet.revision)))
    			.sorted((a,b) -> { return a.Sortierung.compareTo(b.Sortierung); }).collect(Collectors.toList());
    }



    /**
     * Liefert den Namen der Java-Klasse, wie er in der angegebenn Revision genutzt werden soll.
     * 
     * @param rev   die Revision
     * 
     * @return der Name der Java-Klasse
     */
    @JsonIgnore
    public String getJavaKlasse(final long rev) {
    	if (rev > 0)
    		return "Rev" + rev + JavaKlasse;
    	if (rev == 0)
    		return "Migration" + JavaKlasse;
		return JavaKlasse;
	}


	/**
     * Liefert die Spalte mit dem angegebenen Spaltennamen
     * 
     * @param name   der Spaltenname
     * 
     * @return die Tabellenspalte
     */
    @JsonIgnore
    public TabelleSpalte getSpalte(String name) {
    	return spalten.get(name);
    }
    
    /**
     * Fügt die angegebenen Spalte zu der internen Liste hinzu.
     * 
     * @param ts   die Tabellenspalte
     */
    @JsonIgnore
    public void addSpalte(TabelleSpalte ts) {
    	spalten.put(ts.NameSpalte, ts);
    }
    
    
    
    /**
     * Liefert den CREATE TABLE-Befehl in der entsprechenden Version zu dieser Tabelle. Dabei
     * wird der Dialekt des angegebenen DBMS genutzt.
     *  
     * @param schema   die Definition des SVWS-DB-Schemas
     * @param dbms     das DBMS in dessen Dialekt der CREATE TABLE Befehl formuliert ist
     * @param rev      die Revision des Schemas, für welche der SQL-Befehl erzeugt wird 
     * 
     * @return der entsprechende SQL-Befehl
     */
    public String getSQL(DBSchemaDefinition schema, DBDriver dbms, long rev) {
		String newline = System.lineSeparator();
		String pk = this.primaerschluessel.getSQL();
		return "CREATE TABLE " + this.Name + " (" + newline
				+ "  " + getSQLSpalten(schema, dbms, rev)
				+ (((pk == null) || ("".equals(pk))) ? "" : "," + newline + "  " + pk)
				+ this.getSQLFremdschluessel(rev)
				+ this.getSQLUniqueContraints(rev)
				+ newline + ");";
    }
    
    
	/**
	 * Erzeugt den SQL-Drop-Befehl für diese Tabelle für den SQL-Dialekt des angegebenen DBMS
	 * 
	 * @param dbms   das DBMS
	 * 
	 * @return der SQL-Drop-Befehl
	 */
	@JsonIgnore
	public String getSQLDrop(DBDriver dbms) {
		return "DROP TABLE " + (dbms.supportsIfExists() ? "IF EXISTS " : "") + this.Name + ";";
	}
    
    
    /**
     * Liefert alle in der angegebenen Revision gültigen Fremdschlüssel
     * 
     * @param rev   die Revision
     * 
     * @return die in der angegebenen Revision gültigen Fremdschlüssel
     */
    @JsonIgnore
    public List<Fremdschluessel> getFremdschluessel(long rev) {
    	return fremdschluessel.stream()
			.filter(fk -> ((rev == -1) && (fk.dbRevisionVeraltet.revision == -1)) 
				|| ((rev != -1) && (rev >= fk.dbRevision.revision) && ((fk.dbRevisionVeraltet.revision == -1) || (rev < fk.dbRevisionVeraltet.revision))))
			.collect(Collectors.toList());
    }
    
    
    /**
     * Generiert den SQL-Code für die Fremdschlüssel-Constraints der Tabelle
     * 
     * @param rev   die Revision, für welche die Fremdschlüssel-Constraints der Tabelle erzeugt werden sollen
     * 
     * @return der SQL-Code für die Fremdschlüssel-Constraints der Tabelle
     */
    @JsonIgnore
    private String getSQLFremdschluessel(long rev) {
    	String result = fremdschluessel.stream()
    			.filter(fk -> ((rev == -1) && (fk.dbRevisionVeraltet.revision == -1)) 
					|| ((rev != -1) && (rev >= fk.dbRevision.revision) && ((fk.dbRevisionVeraltet.revision == -1) || (rev < fk.dbRevisionVeraltet.revision))))
    			.map(fk -> fk.getSQL())
    			.collect(Collectors.joining("," + System.lineSeparator() + "  "));
    	if ((result == null) || ("".equals(result)))
    		return "";
    	return "," + System.lineSeparator() + "  " + result;
    }

    
    /**
     * Erzeugt den SQL-Code für die Spalten der Tabelle.
     * 
     * @param schema   die Definition des SVWS-DB-Schemas
     * @param dbms     das DBMS in dessen SQL-Dialekt formuliert wird
     * @param rev      die Revision des Schemas, für welche der Spalten-SQL-Code erzeugt wird
     *  
     * @return der SQL-Code
     */
    @JsonIgnore 
    private String getSQLSpalten(DBSchemaDefinition schema, DBDriver dbms, long rev) {
    	return getSpalten().stream()
    			.filter(col -> ((rev == -1) && (col.dbRevisionVeraltet.revision == -1)) 
    					|| ((rev != -1) && (rev >= col.dbRevision.revision) && ((col.dbRevisionVeraltet.revision == -1) || (rev < col.dbRevisionVeraltet.revision))))
    			.map(col -> col.getSQL(schema, dbms))
    			.collect(Collectors.joining(", " + System.lineSeparator() + "  "));    	
    }
    
    
    
    /**
     * Generiert den SQL-Code für die Unique-Constraints der Tabelle
     * 
     * @param rev   die Revision, für welche die Unique-Constraints der Tabelle erzeugt werden sollen
     * 
     * @return der SQL-Code für die Unique-Constraints der Tabelle
     */
    @JsonIgnore
    private String getSQLUniqueContraints(long rev) {
    	String result = unique.values().stream()
        		.filter(uc -> ((rev == -1) && (uc.dbRevisionVeraltet.revision == -1)) 
        				|| ((rev != -1) && (rev >= uc.dbRevision.revision) && ((uc.dbRevisionVeraltet.revision == -1) || (rev < uc.dbRevisionVeraltet.revision))))
        		.map(uc -> uc.getSQL())
        		.collect(Collectors.joining("," + System.lineSeparator() + "  "));
    	if ((result == null) || ("".equals(result)))
    		return "";
    	return "," + System.lineSeparator() + "  " + result;
    }

    
    /**
     * Generiert den SQL-Code für das Erstellen der Indizes der Tabelle
     * 
     * @param rev   die Revision, für welche die Indizes der Tabelle erzeugt werden sollen
     * 
     * @return der SQL-Code für die Indizes der Tabelle
     */
    @JsonIgnore
    public String getSQLIndizes(long rev) {
    	return indizes.stream()
        		.filter(idx -> ((rev == -1) && (idx.dbRevisionVeraltet.revision == -1)) 
        				|| ((rev != -1) && (rev >= idx.dbRevision.revision) && ((idx.dbRevisionVeraltet.revision == -1) || (rev < idx.dbRevisionVeraltet.revision))))
        		.map(idx -> idx.getSQL())
        		.collect(Collectors.joining(System.lineSeparator()));
    }

    
    
    /**
     * Generiert den SQL-Code für das Erstellen oder Entfernen der Trigger der Tabelle
     * 
     * @param dbms     das DBMS, für welches der Trigger-SQL-Code generiert wird
     * @param rev      die Revision, für welche die Trigger der Tabelle erzeugt oder entfernt werden sollen
     * @param create   gibt an, ob der SQL-Code für das Erstellen oder das Entfernen von Triggern generiert wird. 
     * 
     * @return der SQL-Code für die Trigger der Tabelle
     */
    @JsonIgnore
    public String getSQLTrigger(DBDriver dbms, long rev, boolean create) {
    	// Lese die einzelnen Trigger aus
    	Vector<String> sqlTrigger = new Vector<>();
    	for (Trigger t : this.trigger) {
    		if (!dbms.equals(t.dbms))
    			continue;
    		// Prüfe, ab wann die Trigger gültig bzw. ungültig sind
    		if (create) {
        		if (((rev == -1) && (t.dbRevisionVeraltet.revision == -1))
    					|| ((rev != -1) && (rev >= t.dbRevision.revision) && ((t.dbRevisionVeraltet.revision == -1) || (rev < t.dbRevisionVeraltet.revision))))
        			sqlTrigger.add(t.getSQL(dbms, true));    			
    		} else {
    			if ((t.dbRevisionVeraltet.revision >= 0) && (rev >= t.dbRevisionVeraltet.revision))
    				sqlTrigger.add(t.getSQL(dbms, false));
    		}
    	}
    	sqlTrigger.addAll(this.primaerschluessel.getTriggerSQLList(dbms, rev, create));
    	// Füge die einzelnen SQL-Code-Abschnitte zu einem Skript zusammen
    	var newline = System.lineSeparator();
    	if (create) {
	    	if (DBDriver.MARIA_DB.equals(dbms) || DBDriver.MYSQL.equals(dbms)) {
	    		return sqlTrigger.stream().map(sql -> "delimiter $" + newline + sql + newline + "$" + newline + "delimiter ;" + newline)
	    				.collect(Collectors.joining(newline + newline));
	    	} else if (DBDriver.MSSQL.equals(dbms)) {
	    		return sqlTrigger.stream().map(sql -> sql + newline + "GO" + newline)
	    				.collect(Collectors.joining(newline + newline));    		
	    	} 
	    	// DBDriver.SQLITE.equals(dbms))
			return sqlTrigger.stream().collect(Collectors.joining(newline + newline));
    	}
    	return sqlTrigger.stream().collect(Collectors.joining(newline));
    }
    
    
    /**
     * Liefert die manuellen SQL-Befehle für das angegebene DBMS, die zur Initialisierung eines neuen Schemas dienen.
     * 
     * @param dbms   das DBMS
     * @param rev    die Revision des Schemas
     * 
     * @return die SQL-Befehle
     */
    @JsonIgnore
    public String getSQLInit(DBDriver dbms, long rev) {
    	String result = manualSQL.stream()
        		.filter(msql -> ((rev == -1) && (msql.dbRevisionVeraltet.revision == -1)) 
        				|| ((rev != -1) && (rev >= msql.dbRevision.revision) && ((msql.dbRevisionVeraltet.revision == -1) || (rev < msql.dbRevisionVeraltet.revision))))
        		.filter(msql -> !msql.UpdateOnly)
        		.map(msql -> msql.getSQL(dbms))
        		.filter(msql -> (msql != null) && (!"".equals(msql)))
        		.collect(Collectors.joining(";" + System.lineSeparator()));
    	return "".equals(result) ? "" : result + ";" + System.lineSeparator();
    }

    
    /**
     * Vergleicht diese und eine andere Tabelle in Bezug auf die Sortierreihenfolge bei Erstellen der Tabellen  
     * 
     * @param rev     die DB-Revision, für die der Vergleich stattfindet 
     * @param other   die andere Tabelle, mit der verglichen wird
     * 
     * @return -1, falls diese Tabelle zuvor einsortiert wird, 
     *          0, falls die Tabellen identisch einsortiert werden und 
     *          1, falls diese Tabelle weiter hinten einsortiert ist
     */
    @JsonIgnore
    public int compareTo(long rev, Tabelle other) {
    	var a = this.sortierung.get(rev);
    	var b = other.sortierung.get(rev);
    	if ((a == null) && (b == null))
    		return 0;
    	if (a == null)
    		return 1;
    	if (b == null)
    		return -1;
    	int result = a.compareTo(b);
    	return (result != 0) ? result : this.Name.compareTo(other.Name);
    }
    
}


