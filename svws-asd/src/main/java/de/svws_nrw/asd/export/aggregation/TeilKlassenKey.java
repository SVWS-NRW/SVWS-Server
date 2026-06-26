package de.svws_nrw.asd.export.aggregation;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;


/**
 *Sortier und Vergleichsobjekt für die Klassen
 */
public class TeilKlassenKey {
	/**
	 * Bei A-Schulen bestehend aus Jahrgang und Parallelität und bei BK-Schulen das Kürzel.
	 */
	String klassenKuerzel;
	/**
	 *
	 */
	String gliederung;
	/**
	 *
	 */
	String klassenart;
	/**
	 *
	 */
	String orgForm;
	/**
	 *
	 */
	String aktJahrgang;
	/**
	 *
	 */
	String foerderschwerp;
	/**
	 *
	 */
	boolean schwerstbeh;
	/**
	 *
	 */
	String labk;
	/**
	 *
	 */
	String reformpdg;
	/**
	 *
	 */
	String foerderschwerp2;
	/**
	 *
	 */
	String adressmerkmal;

	/**
	 *
	 */
	boolean istJva;
	/**
	 *
	 */
	String fachklasse;



	/**
	 * @param schueler
	 * @param klassenStatistikGesamt
	 * @param lehrerStatistikGesamt
	 * @param idSchuljahresabschnitt
	 * @param jahrgangIds
	 * @param schulform
	 * @param fehlermeldungen
	 */
	public TeilKlassenKey(final SchuelerStatistikGesamt schueler, final Map<Long, KlassenStatistikGesamt> klassenStatistikGesamt,
			final Map<Long, LehrerStatistikGesamt> lehrerStatistikGesamt, final long idSchuljahresabschnitt, final Map<Long, Long> jahrgangIds,
			final Schulform schulform, final List<String> fehlermeldungen) {
		final SchuelerLernabschnittStatistikGesamt lernabschnitt = AggregationUtils.ermittelnLernabschnitt(schueler, idSchuljahresabschnitt);
		this.aktJahrgang = Jahrgaenge.data().getSchluesselByIDOrNull(jahrgangIds.get(lernabschnitt.idJahrgang));

		if (this.aktJahrgang == null) {
			this.aktJahrgang = "";
			fehlermeldungen.add("Bei folgendem Schüler konnte kein Jahrgang ermittelt werden: " + schueler.id + " JahrgangsID: " + lernabschnitt.idJahrgang);
		}

		final KlassenStatistikGesamt klasse = klassenStatistikGesamt.get(lernabschnitt.idKlasse);

		if ((Schulform.BK.equals(schulform)) || (Schulform.SB.equals(schulform))) {
			if ((klasse != null) && (klasse.kuerzel != null)) {
				this.klassenKuerzel = klasse.kuerzel;
			} else {
				this.klassenKuerzel = "";
				fehlermeldungen
						.add("Beim Schüler mit der ID: " + schueler.id + " konnte zu folgender Klasse kein Kürzel ermittelt werden: " + lernabschnitt.idKlasse);
			}
		} else {
			final String parallelitaet = ((klasse != null) && (klasse.parallelitaet != null))
					? klasse.parallelitaet
					: "";
			this.klassenKuerzel = this.aktJahrgang.concat(parallelitaet);
		}

		final Schulgliederung schulgliederung = Schulgliederung.data().getWertByIDOrNull(lernabschnitt.idSchulgliederung);

		if (schulgliederung != null) {
			this.gliederung = schulgliederung.name();
		} else {
			this.gliederung = "";
			fehlermeldungen.add("Beim Schüler mit der ID: " + schueler.id + " ist die Schulgliederung NULL.");
		}
		this.klassenart = Klassenart.data().getSchluesselByIDOrNull(lernabschnitt.idKlassenart) == null ? ""
				: Klassenart.data().getSchluesselByIDOrNull(lernabschnitt.idKlassenart);
		if (Schulform.WB == schulform) {
			this.orgForm = WeiterbildungskollegOrganisationsformen.data().getSchluesselByIDOrNull(lernabschnitt.idOrganisationsform);
		} else if (Schulform.BK == schulform) {
			this.orgForm = BerufskollegOrganisationsformen.data().getSchluesselByIDOrNull(lernabschnitt.idOrganisationsform);
		} else {
			this.orgForm = AllgemeinbildendOrganisationsformen.data().getSchluesselByIDOrNull(lernabschnitt.idOrganisationsform);
		}

		if (this.orgForm == null) {
			this.orgForm = "";
			fehlermeldungen.add("Beim Schüler mit der ID: " + schueler.id + " konnte für die Organisationsform mit der ID: " + lernabschnitt.idOrganisationsform
					+ " kein passender Schlüssel gefunden werden.");
		}

		this.foerderschwerp = Foerderschwerpunkt.data().getSchluesselByIDOrNull(lernabschnitt.idFoerderschwerpunkt1) == null ? ""
				: Foerderschwerpunkt.data().getSchluesselByIDOrNull(lernabschnitt.idFoerderschwerpunkt1);
		this.schwerstbeh = lernabschnitt.hatSchwerbehinderungsNachweis;
		this.labk = lehrerStatistikGesamt.get(klassenStatistikGesamt.get(lernabschnitt.idKlasse).klassenLeitungen.getFirst()).kuerzel == null ? ""
				: lehrerStatistikGesamt.get(klassenStatistikGesamt.get(lernabschnitt.idKlasse).klassenLeitungen.getFirst()).kuerzel;
		//TODO reformpdg auf Klassenebene in svws-server implementieren
		this.reformpdg = AggregationStatistikExport.EIN_LEERZEICHEN;
		this.foerderschwerp2 = Foerderschwerpunkt.data().getSchluesselByIDOrNull(lernabschnitt.idFoerderschwerpunkt2) == null ? ""
				: Foerderschwerpunkt.data().getSchluesselByIDOrNull(lernabschnitt.idFoerderschwerpunkt2);
		this.adressmerkmal =
				klassenStatistikGesamt.get(lernabschnitt.idKlasse).teilstandort;

		if (this.adressmerkmal == null) {
			this.adressmerkmal = "";
			fehlermeldungen.add(
					"Beim Schüler mit der ID: " + schueler.id + " konnte zu folgender Klasse kein Adressmerkmal ermittelt werden: " + lernabschnitt.idKlasse);
		}

		this.istJva = schueler.istJvaSchueler;
		// TODO: Muss noch über einen CoreType in den Schlüssel umgesetzt werden
		this.fachklasse = String.valueOf(lernabschnitt.idFachklasse);
	}



	@Override
	public final int hashCode() {
		return Objects.hash(this.adressmerkmal, this.aktJahrgang, this.fachklasse, this.foerderschwerp, this.foerderschwerp2, this.gliederung, this.istJva,
				this.klassenKuerzel, this.klassenart, this.labk,
				this.orgForm, this.reformpdg, this.schwerstbeh);
	}



	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TeilKlassenKey other = (TeilKlassenKey) obj;
		return Objects.equals(this.adressmerkmal, other.adressmerkmal) && Objects.equals(this.aktJahrgang, other.aktJahrgang)
				&& Objects.equals(this.fachklasse, other.fachklasse) && Objects.equals(this.foerderschwerp, other.foerderschwerp)
				&& Objects.equals(this.foerderschwerp2, other.foerderschwerp2) && Objects.equals(this.gliederung, other.gliederung)
				&& (this.istJva == other.istJva)
				&& Objects.equals(this.klassenKuerzel, other.klassenKuerzel) && Objects.equals(this.klassenart, other.klassenart)
				&& Objects.equals(this.labk, other.labk)
				&& Objects.equals(this.orgForm, other.orgForm) && Objects.equals(this.reformpdg, other.reformpdg) && (this.schwerstbeh == other.schwerstbeh);
	}



	@Override
	public final String toString() {
		return "TeilKlassenKey [klassenKuerzel=" + this.klassenKuerzel + ", gliederung=" + this.gliederung + ", klassenart=" + this.klassenart + ", orgForm="
				+ this.orgForm
				+ ", aktJahrgang=" + this.aktJahrgang + ", foerderschwerp=" + this.foerderschwerp + ", schwerstbeh=" + this.schwerstbeh + ", labk=" + this.labk
				+ ", reformpdg="
				+ this.reformpdg + ", foerderschwerp2=" + this.foerderschwerp2 + ", adressmerkmal=" + this.adressmerkmal + ", istJva=" + this.istJva
				+ ", fachklasse=" + this.fachklasse
				+ "]";
	}



}
