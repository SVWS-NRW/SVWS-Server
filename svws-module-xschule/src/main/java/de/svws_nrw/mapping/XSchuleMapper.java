package de.svws_nrw.mapping;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import de.xbildung.def.xbildung._1_1.xsd.XBildungCodeLanguage;
import de.xbildung.def.xbildung._1_1.xsd.XBildungDokument;
import de.xbildung.def.xbildung._1_1.xsd.XBildungOrtsangabe;
import de.xbildung.def.xbildung._1_1.xsd.XBildungStringLocalized;
import de.xbildung.def.xbildung._1_1.xsd.XBildungTeilbekanntesDatum;
import digital.xschule.def.xschule._1_1.xsd.XSchuleAllgemeinerName;
import digital.xschule.def.xschule._1_1.xsd.XSchuleGeburt;
import digital.xschule.def.xschule._1_1.xsd.XSchuleNameNatuerlichePerson;
import digital.xschule.def.xschule._1_1.xsd.XSchuleNameOrganisation;
import digital.xschule.def.xschule._1_1.xsd.XSchuleSchueler;
import digital.xschule.def.xschule._1_1.xsd.XSchuleSchuelerSchulbescheinigung0004;
import digital.xschule.def.xschule._1_1.xsd.XSchuleSchule;
import digital.xschule.def.xschule._1_1.xsd.XSchuleZeitraum;

public final class XSchuleMapper {

	private static final DatatypeFactory DATATYPE_FACTORY;

	static {
		try {
			DATATYPE_FACTORY = DatatypeFactory.newInstance();
		} catch (final DatatypeConfigurationException e) {
			throw new IllegalStateException("DatatypeFactory nicht verfügbar", e);
		}
	}

	private XSchuleMapper() {
		// Utility-Klasse – keine Instanziierung erlaubt
	}

	/**
	 * Erstellt ein {@link XSchuleSchueler}-Objekt
	 *
	 * @param schuelerName zusammengesetztes Namensobjekt
	 * @param schuelerGeburt zusammengesetztes Geburtsobjekt
	 * @return zusammengesetztes Schüler-Objekt
	 */
	public static XSchuleSchueler toXSchuleSchueler(final XSchuleNameNatuerlichePerson schuelerName, final XSchuleGeburt schuelerGeburt) {
		final var schueler = new XSchuleSchueler();
		schueler.setNameNatuerlichePerson(schuelerName);
		schueler.setGeburt(schuelerGeburt);
		return schueler;
	}

	/**
	 * Ersetellt ein {@link XSchuleGeburt}-Objekt
	 *
	 * @param geburtsdatum Geburtsdatum
	 * @return zusammengesetztes Geburts-Objekt
	 */
	public static XSchuleGeburt toXSchuleGeburt(final String geburtsdatum) {
		final var geburt = new XSchuleGeburt();
		geburt.setDatum(toXBildungTeilbekanntesDatum(geburtsdatum));
		return geburt;
	}

	/**
	 * Erstellt ein {@link XSchuleNameNatuerlichePerson}-Objekt
	 *
	 * @param nachname Nachname
	 * @param vorname Vorname
	 * @return zusammengesetztes Namens-Objekt
	 */
	public static XSchuleNameNatuerlichePerson toXSchuleNameNatuerlichePerson(final String nachname, final String vorname) {
		final var nameNatuerlichePerson = new XSchuleNameNatuerlichePerson();
		nameNatuerlichePerson.setFamilienname(toXSchuleAllgemeinerName(nachname));
		nameNatuerlichePerson.setVorname(toXSchuleAllgemeinerName(vorname));
		nameNatuerlichePerson.setRufname(toXSchuleAllgemeinerName(vorname));
		return nameNatuerlichePerson;
	}

	/**
	 * Erstellt ein {@link XSchuleAllgemeinerName}-Objekt
	 *
	 * @param name Name
	 * @return zusammengesetztes Namens-Objekt
	 */
	private static XSchuleAllgemeinerName toXSchuleAllgemeinerName(final String name) {
		final var allgemeinerName = new XSchuleAllgemeinerName();
		allgemeinerName.setName(name);
		return allgemeinerName;
	}

	/**
	 * Erstellt ein {@link XBildungTeilbekanntesDatum}-Objekt
	 *
	 * @param datum Datum
	 * @return zusammengesetztes Datums-Objekt
	 */
	private static XBildungTeilbekanntesDatum toXBildungTeilbekanntesDatum(final String datum) {
		final var teilbekanntesDatum = new XBildungTeilbekanntesDatum();
		teilbekanntesDatum.setJahrMonatTag(toXMLGregorianCalendar(datum));
		return teilbekanntesDatum;
	}

	/**
	 * Erstellt ein {@link XMLGregorianCalendar}-Objekt
	 *
	 * @param datum Datum
	 * @return umgewandeltes Datums-Objekt
	 */
	private static XMLGregorianCalendar toXMLGregorianCalendar(final String datum) {
		return (datum != null) ? DATATYPE_FACTORY.newXMLGregorianCalendar(datum) : null;
	}

	/**
	 * Erstellt ein {@link XBildungStringLocalized}-Objekt
	 *
	 * @param string Basis-String
	 * @return umgewandeltes String-Objekt
	 */
	public static XBildungStringLocalized toXBildungStringLocalized(final String string) {
		final var stringLocalized = new XBildungStringLocalized();
		stringLocalized.setValue(string);
		return stringLocalized;
	}

	/**
	 * Erstellt eine {@link XBildungOrtsangabe}-Objekt
	 *
	 * @param ortName Name des Orts
	 * @return zusammengesetztes Orts-Objekt
	 */
	private static XBildungOrtsangabe toXBildungOrtsangabe(final String ortName) {
		final var ortsangabe = new XBildungOrtsangabe();
		ortsangabe.setOrt(ortName);
		return ortsangabe;
	}

	/**
	 * Erstellt eine {@link XBildungDokument.Ausstellung}-Objekt
	 *
	 * @param datum Ausstellungsdatum
	 * @param ort Ausstellungsort
	 * @return zusammengesetztes Ausstellungs-Objekt
	 */
	public static XBildungDokument.Ausstellung toXBildungAusstellung(final String datum, final String ort) {
		final var ausstellung = new XBildungDokument.Ausstellung();
		ausstellung.setDatum(toXMLGregorianCalendar(datum));
		ausstellung.setOrt(toXBildungOrtsangabe(ort));
		return ausstellung;
	}

	/**
	 * Erstellt ein {@link XSchuleNameOrganisation}-Objekt
	 *
	 * @param organisationName Name der Organisation
	 * @return zusammengesetztes Namens-Objekt
	 */
	public static XSchuleNameOrganisation toXSchuleNameOrganisation(final String organisationName) {
		final var nameOrganisation = new XSchuleNameOrganisation();
		nameOrganisation.setName(organisationName);
		return nameOrganisation;
	}

	/**
	 * Erstellt ein {@link XSchuleSchule}-Objekt
	 *
	 * @param nameOrganisation Name der Schule
	 * @return zusammengesetztes Schul-Objekt
	 */
	public static XSchuleSchule toXSchuleSchule(final XSchuleNameOrganisation nameOrganisation) {
		final var schule = new XSchuleSchule();
		schule.setName(nameOrganisation);
		return schule;
	}

	/**
	 * Erstellt ein {@link XSchuleZeitraum}-Objekt
	 *
	 * @param ende Ende des Zeitraums
	 * @return zusammengesetztes Zeitraum-Objekt
	 */
	public static XSchuleZeitraum toXSchuleZeitraum(final String ende) {
		final  var zeitraum = new XSchuleZeitraum();
		zeitraum.setEnde(toXMLGregorianCalendar(ende));
		return zeitraum;
	}

	/**
	 * Erstellt ein {@link XSchuleSchuelerSchulbescheinigung0004.Schulbesuch}-Objekt
	 *
	 * @param zeitraum Zeitraum des Schulbesuchs
	 * @return zusammengesetztes Schulbesuch-Objekt
	 */
	public static XSchuleSchuelerSchulbescheinigung0004.Schulbesuch toXSchuleSchulbesuch(final XSchuleZeitraum zeitraum) {
		final var schulbesuch = new XSchuleSchuelerSchulbescheinigung0004.Schulbesuch();
		schulbesuch.setZeitraum(zeitraum);
		return schulbesuch;
	}

	/**
	 * Erstellt ein {@link XBildungCodeLanguage}-Objekt mit dem gegebenen Sprachcode.
	 *
	 * @param sprachcode Sprachcode gemäß Codeliste
	 * @return befülltes Sprachcode-Objekt
	 */
	public static XBildungCodeLanguage toXBildungCodeLanguage(final String sprachcode) {
		final var codeLanguage = new XBildungCodeLanguage();
		codeLanguage.setCode(sprachcode);
		return codeLanguage;
	}

}
