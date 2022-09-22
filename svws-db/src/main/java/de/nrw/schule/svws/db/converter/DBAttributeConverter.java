package de.nrw.schule.svws.db.converter;

import java.util.HashMap;
import java.util.Map;
import de.nrw.schule.svws.db.converter.current.BenutzerKompetenzConverter;
import de.nrw.schule.svws.db.converter.current.BenutzerTypConverter;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;
import de.nrw.schule.svws.db.converter.current.Boolean01StringConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.current.DatumConverter;
import de.nrw.schule.svws.db.converter.current.GeschlechtConverter;
import de.nrw.schule.svws.db.converter.current.GeschlechtConverterFromString;
import de.nrw.schule.svws.db.converter.current.KursFortschreibungsartConverter;
import de.nrw.schule.svws.db.converter.current.NationalitaetenConverter;
import de.nrw.schule.svws.db.converter.current.NoteConverterFromInteger;
import de.nrw.schule.svws.db.converter.current.NoteConverterFromKuerzel;
import de.nrw.schule.svws.db.converter.current.NoteConverterFromNotenpunkte;
import de.nrw.schule.svws.db.converter.current.NoteConverterFromNotenpunkteString;
import de.nrw.schule.svws.db.converter.current.PersonalTypConverter;
import de.nrw.schule.svws.db.converter.current.SchuelerStatusConverter;
import de.nrw.schule.svws.db.converter.current.SprachpruefungniveauConverter;
import de.nrw.schule.svws.db.converter.current.SprachreferenzniveauConverter;
import de.nrw.schule.svws.db.converter.current.StringToIntegerConverter;
import de.nrw.schule.svws.db.converter.current.UhrzeitConverter;
import de.nrw.schule.svws.db.converter.current.gost.AbiturBelegungsartConverter;
import de.nrw.schule.svws.db.converter.current.gost.AbiturKursMarkierungConverter;
import de.nrw.schule.svws.db.converter.current.gost.GOStAbiturFachConverter;
import de.nrw.schule.svws.db.converter.current.gost.GOStBesondereLernleistungConverter;
import de.nrw.schule.svws.db.converter.current.gost.GOStHalbjahrConverter;
import de.nrw.schule.svws.db.converter.current.gost.GOStKursartConverter;
import de.nrw.schule.svws.db.converter.current.kursblockung.GostKursblockungRegelTypConverter;
import de.nrw.schule.svws.db.converter.current.statkue.SchulformKuerzelConverter;
import de.nrw.schule.svws.db.converter.current.statkue.SchulgliederungKuerzelConverter;
import de.nrw.schule.svws.db.converter.current.statkue.ZulaessigesFachKuerzelASDConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationBoolean01Converter;
import de.nrw.schule.svws.db.converter.migration.MigrationBoolean01StringConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationDatumConverter;
import de.nrw.schule.svws.db.converter.migration.MigrationStringToIntegerConverter;
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
	 * Eine Map, welche dem "SimpleName" der Java-Klasse des Attribut-Konverters eine 
	 * Instanz der Klasse des Attribut-Konverters zuordnet. Dies wird von
	 * dem Java-DTO-Creator benötigt.
	 */  
	@SuppressWarnings("rawtypes")
	private static Map<String, DBAttributeConverter> converter = null;
	
	
	/**
	 * Bestimmt den Attributkonverter anhand des Klassennamens.
	 * 
	 * @param classname   the Name der Converter-Klasse
	 * 
	 * @return eine Instanz der Converter-Klasse
	 */
	public static DBAttributeConverter<?,?> getByClassName(String classname) {
		if (converter == null) {
			converter = new HashMap<>();
			// Revision 0 - Migration: Die Konverter befinden sich im Sub-Package migration und beginnen mit dem Präfix Migration
			converter.put(MigrationBoolean01Converter.class.getSimpleName(), new MigrationBoolean01Converter());
			converter.put(MigrationBoolean01StringConverter.class.getSimpleName(), new MigrationBoolean01StringConverter());
			converter.put(MigrationBooleanPlusMinusConverter.class.getSimpleName(), new MigrationBooleanPlusMinusConverter());
			converter.put(MigrationBooleanPlusMinusDefaultMinusConverter.class.getSimpleName(), new MigrationBooleanPlusMinusDefaultMinusConverter());
			converter.put(MigrationBooleanPlusMinusDefaultPlusConverter.class.getSimpleName(), new MigrationBooleanPlusMinusDefaultPlusConverter());
			converter.put(MigrationDatumConverter.class.getSimpleName(), new MigrationDatumConverter());
			converter.put(MigrationStringToIntegerConverter.class.getSimpleName(), new MigrationStringToIntegerConverter());
			// Aktuelle Revision: Hier befinden sich die Konverter für die aktuelle Revision
			converter.put(BenutzerKompetenzConverter.class.getSimpleName(), new BenutzerKompetenzConverter());
			converter.put(BenutzerTypConverter.class.getSimpleName(), new BenutzerTypConverter());
			converter.put(Boolean01Converter.class.getSimpleName(), new Boolean01Converter());
			converter.put(Boolean01StringConverter.class.getSimpleName(), new Boolean01StringConverter());
			converter.put(BooleanPlusMinusConverter.class.getSimpleName(), new BooleanPlusMinusConverter());
			converter.put(BooleanPlusMinusDefaultMinusConverter.class.getSimpleName(), new BooleanPlusMinusDefaultMinusConverter());
			converter.put(BooleanPlusMinusDefaultPlusConverter.class.getSimpleName(), new BooleanPlusMinusDefaultPlusConverter());
			converter.put(DatumConverter.class.getSimpleName(), new DatumConverter());
			converter.put(GeschlechtConverter.class.getSimpleName(), new GeschlechtConverter());
			converter.put(GeschlechtConverterFromString.class.getSimpleName(), new GeschlechtConverterFromString());
			converter.put(KursFortschreibungsartConverter.class.getSimpleName(), new KursFortschreibungsartConverter());
			converter.put(NationalitaetenConverter.class.getSimpleName(), new NationalitaetenConverter());
			converter.put(NoteConverterFromInteger.class.getSimpleName(), new NoteConverterFromInteger());
			converter.put(NoteConverterFromKuerzel.class.getSimpleName(), new NoteConverterFromKuerzel());
			converter.put(NoteConverterFromNotenpunkte.class.getSimpleName(), new NoteConverterFromNotenpunkte());
			converter.put(NoteConverterFromNotenpunkteString.class.getSimpleName(), new NoteConverterFromNotenpunkteString());
			converter.put(PersonalTypConverter.class.getSimpleName(), new PersonalTypConverter());
			converter.put(SchuelerStatusConverter.class.getSimpleName(), new SchuelerStatusConverter());
			converter.put(StringToIntegerConverter.class.getSimpleName(), new StringToIntegerConverter());
			converter.put(AbiturBelegungsartConverter.class.getSimpleName(), new AbiturBelegungsartConverter());
			converter.put(AbiturKursMarkierungConverter.class.getSimpleName(), new AbiturKursMarkierungConverter());
			converter.put(GOStAbiturFachConverter.class.getSimpleName(), new GOStAbiturFachConverter());
			converter.put(GOStBesondereLernleistungConverter.class.getSimpleName(), new GOStBesondereLernleistungConverter());
			converter.put(GOStHalbjahrConverter.class.getSimpleName(), new GOStHalbjahrConverter());
			converter.put(GOStKursartConverter.class.getSimpleName(), new GOStKursartConverter());
			converter.put(GostKursblockungRegelTypConverter.class.getSimpleName(), new GostKursblockungRegelTypConverter());
			converter.put(SchulformKuerzelConverter.class.getSimpleName(), new SchulformKuerzelConverter());
			converter.put(SchulgliederungKuerzelConverter.class.getSimpleName(), new SchulgliederungKuerzelConverter());
			converter.put(SprachreferenzniveauConverter.class.getSimpleName(), new SprachreferenzniveauConverter());
			converter.put(SprachpruefungniveauConverter.class.getSimpleName(), new SprachpruefungniveauConverter());
			converter.put(UhrzeitConverter.class.getSimpleName(), new UhrzeitConverter());
			converter.put(ZulaessigesFachKuerzelASDConverter.class.getSimpleName(), new ZulaessigesFachKuerzelASDConverter());
			
			// Zukünftige Revisionen: Konverter für zukünftige Revision liegen im Sub-Package revNNN und beginnen mit dem Präfix RevNNN
		}
		DBAttributeConverter<?,?> tmp = converter.get(classname);
		if ((tmp == null) && (classname.startsWith("Rev")))
			return converter.get(classname.replaceFirst("Rev\\d+", ""));
		return tmp;
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
