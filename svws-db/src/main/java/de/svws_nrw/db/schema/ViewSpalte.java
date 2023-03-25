package de.svws_nrw.db.schema;

import de.svws_nrw.db.converter.DBAttributeConverter;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält die Definition einer Spalte einer View
 */
public class ViewSpalte {
	
	/** Der Name der Spalte in der View */
	public final @NotNull String name;
	
	/** Die Beschreibung der Spalte zur Dokumentation */
	public final @NotNull String beschreibung;
    
	/** Der Java-Datentyp der Spalte */
	public final @NotNull String datentyp;
	  
	/** Der SQL-Code für die Spaltendefinition */
	public final @NotNull String sql;
	
	/** Der Konverter zum automatischen Umwandeln des Datenbank-Wertes beim Einlesen in Java oder null */
	public final Class<? extends DBAttributeConverter<?, ?>> converter;


	/**
	 * Erstellt eine neue Spalte für die übergebene View.
	 * 
	 * @param name            der Name der Spalte
	 * @param beschreibung    die Beschreibung der Spalte zur Dokumentation
	 * @param datentyp        der Java-Datentyp der Spalte
	 * @param sql             der SQL-Code für die Spaltendefinition
	 * @param converter       der Konverter zum automatischen Umwandeln des Datenbank-Wertes beim Einlesen in Java oder null
	 */
	public ViewSpalte(@NotNull String name, @NotNull String beschreibung, @NotNull String datentyp, @NotNull String sql, Class<? extends DBAttributeConverter<?, ?>> converter) {
		this.name = name;
		this.beschreibung = beschreibung;
		this.datentyp = datentyp;
		this.sql = sql;
		this.converter = converter;
	}
    
	
	/**
	 * Liefert den SQL-Code für diese Spalte
	 * 
	 * @return der SQL-Code für diese Spalte
	 */
	public String getSQL() {
		return sql + " AS " + name;
	}

}
