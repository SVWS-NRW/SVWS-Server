package de.svws_nrw.db.converter;

import java.util.HashMap;

import de.svws_nrw.db.converter.current.BenutzerKompetenzConverter;
import de.svws_nrw.db.converter.current.BenutzerTypConverter;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.Boolean01StringConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.DatumUhrzeitConverter;
import de.svws_nrw.db.converter.current.DavRessourceCollectionTypConverter;
import de.svws_nrw.db.converter.current.GeschlechtConverter;
import de.svws_nrw.db.converter.current.GeschlechtConverterFromString;
import de.svws_nrw.db.converter.current.KursFortschreibungsartConverter;
import de.svws_nrw.db.converter.current.NationalitaetenConverter;
import de.svws_nrw.db.converter.current.NoteConverterFromInteger;
import de.svws_nrw.db.converter.current.NoteConverterFromKuerzel;
import de.svws_nrw.db.converter.current.NoteConverterFromNotenpunkte;
import de.svws_nrw.db.converter.current.NoteConverterFromNotenpunkteString;
import de.svws_nrw.db.converter.current.PersonalTypConverter;
import de.svws_nrw.db.converter.current.SchuelerStatusConverter;
import de.svws_nrw.db.converter.current.SprachpruefungniveauConverter;
import de.svws_nrw.db.converter.current.SprachreferenzniveauConverter;
import de.svws_nrw.db.converter.current.StringToIntegerConverter;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
import de.svws_nrw.db.converter.current.VerkehrssprachenConverter;
import de.svws_nrw.db.converter.current.gost.AbiturBelegungsartConverter;
import de.svws_nrw.db.converter.current.gost.AbiturKursMarkierungConverter;
import de.svws_nrw.db.converter.current.gost.GOStAbiturFachConverter;
import de.svws_nrw.db.converter.current.gost.GOStBesondereLernleistungConverter;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;
import de.svws_nrw.db.converter.current.gost.GOStKursartConverter;
import de.svws_nrw.db.converter.current.gost.GostLaufbahnplanungFachkombinationTypConverter;
import de.svws_nrw.db.converter.current.kursblockung.GostKursblockungRegelTypConverter;
import de.svws_nrw.db.converter.current.statkue.SchulformKuerzelConverter;
import de.svws_nrw.db.converter.current.statkue.SchulgliederungKuerzelConverter;
import de.svws_nrw.db.converter.current.statkue.ZulaessigesFachKuerzelASDConverter;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;
import de.svws_nrw.db.converter.migration.MigrationBoolean01StringConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.migration.MigrationDatumConverter;
import de.svws_nrw.db.converter.migration.MigrationStringToIntegerConverter;
import jakarta.persistence.AttributeConverter;

/**
 * Diese Klasse dient als Basisklasse für Attribut-Converter, welche bei der Persistierung
 * von Objekt-Attributen in der SVWS-DB eingesetzt werden.
 *
 * @param <X>   der Datentyp, welcher im Java-DTO verwendet wird
 * @param <Y>   der Datentyp, welcher für die Persistierung in der Datenbank verwendet wird. 
 */
public abstract class DBAttributeConverter<X, Y> implements AttributeConverter<X, Y> {

	/** 
	 * Eine Map, welche einer Attribut-Konverter-Klasse eine Instanz der Klasse 
	 * zuordnet. Dies wird von dem Java-DTO-Creator benötigt.
	 */  
	@SuppressWarnings("rawtypes")
	private static HashMap<Class<? extends DBAttributeConverter>, DBAttributeConverter> converter = new HashMap<>();
	/** 
	 * Eine Map, welche dem Namen einer Attribut-Konverter-Klasse eine 
	 * Instanz der Klasse zuordnet. Dies wird von dem Java-DTO-Creator benötigt.
	 */  
	@SuppressWarnings("rawtypes")
	private static HashMap<String, DBAttributeConverter> converterByName = new HashMap<>();
	
	/**
	 * Fügt einen neuen Attribut-Konverter hinzu
	 * 
	 * @param <T>   der Typ des Attribut-Konverters 
	 * 
	 * @param conv   die Instanz des Konverters
	 */
	private static <T extends DBAttributeConverter<?,?>> void add(T conv) {
		converter.put(conv.getClass(), conv);
		converterByName.put(conv.getClass().getSimpleName(), conv);
	}
	
	
	/**
	 * Initialisiert die Attribut-Konverter
	 */
	private static void init() {
		// Revision 0 - Migration: Die Konverter befinden sich im Sub-Package migration und beginnen mit dem Präfix Migration
		add(new MigrationBoolean01Converter());
		add(new MigrationBoolean01StringConverter());
		add(new MigrationBooleanPlusMinusConverter());
		add(new MigrationBooleanPlusMinusDefaultMinusConverter());
		add(new MigrationBooleanPlusMinusDefaultPlusConverter());
		add(new MigrationDatumConverter());
		add(new MigrationStringToIntegerConverter());
		// Aktuelle Revision: Hier befinden sich die Konverter für die aktuelle Revision
		add(new BenutzerKompetenzConverter());
		add(new BenutzerTypConverter());
		add(new Boolean01Converter());
		add(new Boolean01StringConverter());
		add(new BooleanPlusMinusConverter());
		add(new BooleanPlusMinusDefaultMinusConverter());
		add(new BooleanPlusMinusDefaultPlusConverter());
		add(new DatumConverter());
		add(new DatumUhrzeitConverter());
		add(new DavRessourceCollectionTypConverter());
		add(new GeschlechtConverter());
		add(new GeschlechtConverterFromString());
		add(new KursFortschreibungsartConverter());
		add(new NationalitaetenConverter());
		add(new NoteConverterFromInteger());
		add(new NoteConverterFromKuerzel());
		add(new NoteConverterFromNotenpunkte());
		add(new NoteConverterFromNotenpunkteString());
		add(new PersonalTypConverter());
		add(new SchuelerStatusConverter());
		add(new StringToIntegerConverter());
		add(new AbiturBelegungsartConverter());
		add(new AbiturKursMarkierungConverter());
		add(new GOStAbiturFachConverter());
		add(new GOStBesondereLernleistungConverter());
		add(new GOStHalbjahrConverter());
		add(new GOStKursartConverter());
		add(new GostKursblockungRegelTypConverter());
		add(new GostLaufbahnplanungFachkombinationTypConverter());
		add(new SchulformKuerzelConverter());
		add(new SchulgliederungKuerzelConverter());
		add(new SprachreferenzniveauConverter());
		add(new SprachpruefungniveauConverter());
		add(new UhrzeitConverter());
		add(new VerkehrssprachenConverter());
		add(new ZulaessigesFachKuerzelASDConverter());
		
		// Zukünftige Revisionen: Konverter für zukünftige Revision liegen im Sub-Package revNNN und beginnen mit dem Präfix RevNNN		
	}
	
	
	/**
	 * Bestimmt den Attributkonverter anhand der Klasse.
	 * 
	 * @param <T>     der Typ des Attribut-Konverters 
	 * @param clazz   die Converter-Klasse
	 * 
	 * @return eine Instanz der Converter-Klasse
	 */
	@SuppressWarnings("unchecked")
	public static <T extends DBAttributeConverter<?,?>> T getByClass(Class<T> clazz) {
		if (converter.size() == 0)
			init();
		return (T) converter.get(clazz);
	}	
	
	
	
	/**
	 * Bestimmt den Attributkonverter anhand des Klassennamens.
	 * 
	 * @param <T>         der Typ des Attribut-Konverters 
	 * @param classname   der Name der Converter-Klasse
	 * 
	 * @return eine Instanz der Converter-Klasse
	 */
	@SuppressWarnings("unchecked")
	public static <T extends DBAttributeConverter<?,?>> T getByClassName(String classname) {
		if (converter.size() == 0)
			init();
		return (T) converterByName.get(classname);
	}	
	
	
	
	/**
	 * Gibt die Klasse des Datentyps zurück, welcher in der Java-DTO-Klasse verwendet wird.
	 * 
	 * @return die Klasse des Java-DTO-Datentyps. 
	 */
	public abstract Class<X> getResultType();
	
	
	/**
	 * Gibt die Klasse des Datentyps zurück, welcher für die Persistierung in der Datenbank
	 * verwendet wird.
	 *   
	 * @return der Datentyp für die Persistierung in der Datenbank
	 */
	public abstract Class<Y> getDBType();

	
	/**
	 * Wandelt die übergebenen Daten in den Typ X um. Hierbei wird geprüft,
	 * ob dbData ein Objekt des korrekten Typs ist.
	 * 
	 * @param dbData   die umzuwandelnden Daten
	 * 
	 * @return Die umgewandelten Daten oder null, falls dies nicht möglich ist 
	 */
	@SuppressWarnings("unchecked")
	public X convertToEntityAttributeFromObject(Object dbData) {
		if ((dbData == null) || (!getDBType().isInstance(dbData.getClass())))
			return null;
		return convertToEntityAttribute((Y)dbData);
	}
	
}
