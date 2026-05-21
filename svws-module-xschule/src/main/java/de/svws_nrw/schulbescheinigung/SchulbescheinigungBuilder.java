package de.svws_nrw.schulbescheinigung;

import java.util.Objects;

import de.svws_nrw.mapping.XSchuleMapper;
import de.xbildung.def.xbildung._1_1.xsd.XBildungDokument;
import digital.xschule.def.xschule._1_1.xsd.XSchuleGeburt;
import digital.xschule.def.xschule._1_1.xsd.XSchuleNameNatuerlichePerson;
import digital.xschule.def.xschule._1_1.xsd.XSchuleNameOrganisation;
import digital.xschule.def.xschule._1_1.xsd.XSchuleSchuelerSchulbescheinigung0004;
import digital.xschule.def.xschule._1_1.xsd.XSchuleZeitraum;

/**
 * Builder für {@link XSchuleSchuelerSchulbescheinigung0004}.
 * Felder ohne Wert (null) erzeugen keine leeren XML-Knoten.
 */
public final class SchulbescheinigungBuilder {

	private String titel;
	private String sprache;
	private String ausstellungOrt;
	private String ausstellungDatum;
	private String schuelerNachname;
	private String schuelerVorname;
	private String schuelerGeburtsdatum;
	private String schuleName;
	private String bildungsgangEnddatum;

	/**
	 * @param titel Titel der Schulbescheinigung
	 * @return Builder-Objekt
	 */
	public SchulbescheinigungBuilder titel(final String titel) {
		this.titel = titel;
		return this;
	}

	/**
	 * @param sprache Sprachcode des Dokuments gemäß Codeliste (z.B. "deu" für Deutsch)
	 * @return Builder-Objekt
	 */
	public SchulbescheinigungBuilder sprache(final String sprache) {
		this.sprache = sprache;
		return this;
	}

	/**
	 * @param ausstellungOrt Ort der Ausstellung
	 * @return Builder-Objekt
	 */
	public SchulbescheinigungBuilder ausstellungOrt(final String ausstellungOrt) {
		this.ausstellungOrt = ausstellungOrt;
		return this;
	}

	/**
	 * @param ausstellungDatum Datum der Ausstellung
	 * @return Builder-Objekt
	 */
	public SchulbescheinigungBuilder ausstellungDatum(final String ausstellungDatum) {
		this.ausstellungDatum = ausstellungDatum;
		return this;
	}

	/**
	 * @param schuelerNachname Nachname des Schülers
	 * @return Builder-Objekt
	 */
	public SchulbescheinigungBuilder schuelerNachname(final String schuelerNachname) {
		this.schuelerNachname = schuelerNachname;
		return this;
	}

	/**
	 * @param schuelerVorname Vorname des Schülers
	 * @return Builder-Objekt
	 */
	public SchulbescheinigungBuilder schuelerVorname(final String schuelerVorname) {
		this.schuelerVorname = schuelerVorname;
		return this;
	}

	/**
	 * @param schuelerGeburtsdatum Geburtsdatum des Schülers
	 * @return Builder-Objekt
	 */
	public SchulbescheinigungBuilder schuelerGeburtsdatum(final String schuelerGeburtsdatum) {
		this.schuelerGeburtsdatum = schuelerGeburtsdatum;
		return this;
	}

	/**
	 * @param schuleName Name der Schule
	 * @return Builder-Objekt
	 */
	public SchulbescheinigungBuilder schuleName(final String schuleName) {
		this.schuleName = schuleName;
		return this;
	}

	/**
	 * @param bildungsgangEnddatum Enddatum des Bildungsgangs
	 * @return Builder-Objekt
	 */
	public SchulbescheinigungBuilder bildungsgangEnddatum(final String bildungsgangEnddatum) {
		this.bildungsgangEnddatum = bildungsgangEnddatum;
		return this;
	}

	/**
	 * Erzeugt das zusammengesetzte Schulbescheinigungs-Objekt durch Aufruf der Methoden von {@link XSchuleMapper}
	 * mit den vorhandenen Daten. Felder ohne Wert werden nicht gesetzt und erzeugen keine leeren XML-Knoten.
	 *
	 * @return vollständig befüllte {@link XSchuleSchuelerSchulbescheinigung0004}
	 */
	public XSchuleSchuelerSchulbescheinigung0004 build() {
		checkRequiredFields();

		final var bescheinigung = new XSchuleSchuelerSchulbescheinigung0004();

		setSprache(bescheinigung);
		setSchueler(bescheinigung);
		setSchule(bescheinigung);
		setSchulbesuch(bescheinigung);

		setTitelIfPresent(bescheinigung);
		setAusstellungIfPresent(bescheinigung);

		return bescheinigung;
	}

	private void setSprache(final XSchuleSchuelerSchulbescheinigung0004 bescheinigung) {
		bescheinigung.setSprache(XSchuleMapper.toXBildungCodeLanguage(this.sprache));
	}

	private void setSchueler(final XSchuleSchuelerSchulbescheinigung0004 bescheinigung) {
		final XSchuleNameNatuerlichePerson name = XSchuleMapper.toXSchuleNameNatuerlichePerson(this.schuelerNachname, this.schuelerVorname);
		final XSchuleGeburt geburt = (this.schuelerGeburtsdatum != null) ? XSchuleMapper.toXSchuleGeburt(this.schuelerGeburtsdatum) : null;
		bescheinigung.setSchueler(XSchuleMapper.toXSchuleSchueler(name, geburt));
	}

	private void setSchule(final XSchuleSchuelerSchulbescheinigung0004 bescheinigung) {
		final XSchuleNameOrganisation nameOrganisation = XSchuleMapper.toXSchuleNameOrganisation(this.schuleName);
		bescheinigung.setSchule(XSchuleMapper.toXSchuleSchule(nameOrganisation));
	}

	private void setSchulbesuch(final XSchuleSchuelerSchulbescheinigung0004 bescheinigung) {
		if (this.bildungsgangEnddatum != null) {
			final XSchuleZeitraum zeitraum = XSchuleMapper.toXSchuleZeitraum(this.bildungsgangEnddatum);
			bescheinigung.setSchulbesuch(XSchuleMapper.toXSchuleSchulbesuch(zeitraum));
		}
	}

	private void setTitelIfPresent(final XSchuleSchuelerSchulbescheinigung0004 bescheinigung) {
		if (this.titel != null) {
			bescheinigung.setTitel(XSchuleMapper.toXBildungStringLocalized(this.titel));
		}
	}

	private void setAusstellungIfPresent(final XSchuleSchuelerSchulbescheinigung0004 bescheinigung) {
		if ((this.ausstellungDatum != null) || (this.ausstellungOrt != null)) {
			final XBildungDokument.Ausstellung ausstellung = XSchuleMapper.toXBildungAusstellung(this.ausstellungDatum, this.ausstellungOrt);
			bescheinigung.getAusstellung().add(ausstellung);
		}
	}

	private void checkRequiredFields() {
		Objects.requireNonNull(sprache, "sprache darf nicht null sein");
		Objects.requireNonNull(schuelerNachname, "schuelerNachname darf nicht null sein");
		Objects.requireNonNull(schuelerVorname, "schuelerVorname darf nicht null sein");
		Objects.requireNonNull(bildungsgangEnddatum, "bildungsgangEnddatum darf nicht null sein");
		Objects.requireNonNull(schuleName, "schuleName darf nicht null sein");
	}

}
