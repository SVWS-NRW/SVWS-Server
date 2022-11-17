package de.nrw.schule.svws.db.schema;

import java.util.Vector;

import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.SQLiteUtils;
import de.nrw.schule.svws.db.converter.DBAttributeConverter;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt die Definition einer View in der SVWS-DB
 */
public class View {

	/** Der Name der View */
    public final @NotNull String name;
    
    /** Gibt den Namen des Java-Sub-Packages an, in dem der zugehörige Java-DTO generiert werden soll. */
    public final @NotNull String packageName;
    
    /** Gibt den Namen der zugehörigen Java-DTO-Klasse an. */
    public final @NotNull String dtoName;
    
    /** Eine Beschreibung, welchen Zweck die View erfüllt. */
    public final @NotNull String beschreibung;
	
	/** Die Revision, in welcher die View eingeführt wurde */
    final int revision;
	
	/** Die Revision, in welcher die View als veraltet definiert wurde - null, wenn sie noch nicht veraltet ist */
    final Integer veraltet;

	/** Der SQL-Code für den FROM-Teil des SELECT der View */
    final @NotNull String sql;
    
    /** Eine Liste der Spalten der View*/
    public final @NotNull Vector<@NotNull ViewSpalte> spalten = new Vector<>();
    
    /** Eine Liste der Spalten, welcher als Primärschlüssel der View geeignet ist */
    public final @NotNull Vector<@NotNull ViewSpalte> pkSpalten = new Vector<>();


    /**
     * Erstellt eine neue Schema-Definition einer Datenbank-View 
     * 
     * @param name           der Name der View
     * @param packageName    der Name des Java-Sub-Packages, in dem der zugehörige Java-DTO generiert werden soll
     * @param dtoName        der Name der zugehörigen Java-DTO-Klasse
     * @param beschreibung   eine Beschreibung, welchen Zweck die View erfüllt
     * @param revision       die Revision, in welcher die View eingeführt wurde
     * @param veraltet       die Revision, in welcher die View als veraltet definiert wurde - null, wenn sie noch nicht veraltet ist
     * @param sql            der SQL-Code für den FROM-Teil des SELECT der View
     */
	public View(final @NotNull String name, final @NotNull String packageName, @NotNull String dtoName, final @NotNull String beschreibung, 
				final Integer revision, final Integer veraltet, final @NotNull String sql) {
		this.name = name;
		this.packageName = packageName;
		this.dtoName = dtoName;
		this.beschreibung = beschreibung;
		this.revision = revision;
		this.veraltet = veraltet;
		this.sql = sql;
	}
    

	/**
	 * Fügt eine Spalte zu der View hinzu.
	 * 
	 * @param name            der Name der Spalte
	 * @param beschreibung    die Beschreibung der Spalte zur Dokumentation
	 * @param datentyp        der Java-Datentyp der Spalte
	 * @param sql             der SQL-Code für die Spaltendefinition
	 * @param converter       der Konverter zum automatischen Umwandeln des Datenbank-Wertes beim Einlesen in Java oder null
	 * @param istPrimaryKey   gibt an, ob die Spalte Bestandteil des Primärschlüssels der View ist
	 * 
	 * @return diese View-Definition 
	 */
	public View add(@NotNull String name, @NotNull String beschreibung, @NotNull String datentyp, @NotNull String sql, Class<? extends DBAttributeConverter<?, ?>> converter, boolean istPrimaryKey) {
		ViewSpalte spalte = new ViewSpalte(name, beschreibung, datentyp, sql, converter);
		spalten.add(spalte);
		if (istPrimaryKey)
			pkSpalten.add(spalte);
		return this;
	}

	
	/**
	 * Liefert den SQL-Code für das Erstellen der View
	 * 
	 * @param driver   der Datenbank-Treiber, für den der SQL-Code erzeugt wird 
	 * 
	 * @return der SQL-Code für das Erstellen der View
	 */
	public String getSQLCreate(DBDriver driver) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE VIEW IF NOT EXISTS ").append(name).append(" AS ");
		sb.append("SELECT ");
		for (int i = 0; i < spalten.size(); i++) {
			if (i > 0) 
				sb.append(", ");
			sb.append(spalten.get(i).getSQL());
		}
		sb.append(" FROM ").append(sql);
		if (driver == DBDriver.SQLITE)
			return SQLiteUtils.replaceConcat(sb.toString());
		return sb.toString();
	}


	/**
	 * Liefert den SQL-Code für das Entfernen der View
	 * 
	 * @return der SQL-Code für das Entfernen der View
	 */
	public String getSQLDrop() {
		return "DROP VIEW IF EXISTS " + name;
	}
	

	/**
	 * Gibt zurück, ob der Java-DTO einen einfachen Primary-Key hat oder nicht
	 * 
	 * @return true, falls der Java-DTO einen einfachen Primary-Key hat
	 */
	public boolean hasSimplePrimaryKey() {
		if (pkSpalten.size() <= 0)
			throw new IllegalStateException("Ein Java-DTO für die View " + name + " muss einen Primary-Key haben");
		return pkSpalten.size() == 1;
	}

}
