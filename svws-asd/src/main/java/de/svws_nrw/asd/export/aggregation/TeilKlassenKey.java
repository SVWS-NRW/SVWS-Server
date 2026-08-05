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
	 * @param foerderschwerpunktIdMap
	 * @param fehlermeldungen
	 */
	public TeilKlassenKey(final SchuelerStatistikGesamt schueler, final Map<Long, KlassenStatistikGesamt> klassenStatistikGesamt,
			final Map<Long, LehrerStatistikGesamt> lehrerStatistikGesamt, final long idSchuljahresabschnitt, final Map<Long, Long> jahrgangIds,
			final Schulform schulform, final Map<Long, Long> foerderschwerpunktIdMap, final List<String> fehlermeldungen) {
		final SchuelerLernabschnittStatistikGesamt lernabschnitt = AggregationUtils.ermittelnLernabschnitt(schueler, idSchuljahresabschnitt);
		aktJahrgang = Jahrgaenge.data().getSchluesselByIDOrNull(jahrgangIds.get(lernabschnitt.idJahrgang));

		if (aktJahrgang == null) {
			aktJahrgang = "";
			fehlermeldungen.add("Bei folgendem Schüler konnte kein Jahrgang ermittelt werden: " + schueler.id + " JahrgangsID: " + lernabschnitt.idJahrgang);
		}

		final KlassenStatistikGesamt klasse = klassenStatistikGesamt.get(lernabschnitt.idKlasse);

		if ((Schulform.BK.equals(schulform)) || (Schulform.SB.equals(schulform))) {
			if ((klasse != null) && (klasse.kuerzel != null)) {
				klassenKuerzel = klasse.kuerzel;
			} else {
				klassenKuerzel = "";
				fehlermeldungen
						.add("Beim Schüler mit der ID: " + schueler.id + " konnte zu folgender Klasse kein Kürzel ermittelt werden: " + lernabschnitt.idKlasse);
			}
		} else {
			final String parallelitaet = ((klasse != null) && (klasse.parallelitaet != null))
					? klasse.parallelitaet
					: "";
			klassenKuerzel = aktJahrgang.concat(parallelitaet);
		}

		final Schulgliederung schulgliederung = Schulgliederung.data().getWertByIDOrNull(lernabschnitt.idSchulgliederung);

		if (schulgliederung != null) {
			gliederung = schulgliederung.name();
		} else {
			gliederung = "";
			fehlermeldungen.add("Beim Schüler mit der ID: " + schueler.id + " ist die Schulgliederung NULL.");
		}
		klassenart = Klassenart.data().getSchluesselByIDOrNull(lernabschnitt.idKlassenart) == null ? ""
				: Klassenart.data().getSchluesselByIDOrNull(lernabschnitt.idKlassenart);
		if (Schulform.WB == schulform) {
			orgForm = WeiterbildungskollegOrganisationsformen.data().getSchluesselByIDOrNull(lernabschnitt.idOrganisationsform);
		} else if (Schulform.BK == schulform) {
			orgForm = BerufskollegOrganisationsformen.data().getSchluesselByIDOrNull(lernabschnitt.idOrganisationsform);
		} else {
			orgForm = AllgemeinbildendOrganisationsformen.data().getSchluesselByIDOrNull(lernabschnitt.idOrganisationsform);
		}

		if (orgForm == null) {
			orgForm = "";
			fehlermeldungen.add("Beim Schüler mit der ID: " + schueler.id + " konnte für die Organisationsform mit der ID: " + lernabschnitt.idOrganisationsform
					+ " kein passender Schlüssel gefunden werden.");
		}

		foerderschwerp = Foerderschwerpunkt.data().getSchluesselByIDOrNull(foerderschwerpunktIdMap.get(lernabschnitt.idFoerderschwerpunkt1)) == null ? ""
				: Foerderschwerpunkt.data().getSchluesselByIDOrNull(foerderschwerpunktIdMap.get(lernabschnitt.idFoerderschwerpunkt1));
		schwerstbeh = lernabschnitt.hatSchwerbehinderungsNachweis;
		labk = lehrerStatistikGesamt.get(klassenStatistikGesamt.get(lernabschnitt.idKlasse).klassenLeitungen.getFirst()).kuerzel == null ? ""
				: lehrerStatistikGesamt.get(klassenStatistikGesamt.get(lernabschnitt.idKlasse).klassenLeitungen.getFirst()).kuerzel;
		//TODO reformpdg auf Klassenebene in svws-server implementieren
		reformpdg = AggregationStatistikExport.EIN_LEERZEICHEN;
		foerderschwerp2 = Foerderschwerpunkt.data().getSchluesselByIDOrNull(foerderschwerpunktIdMap.get(lernabschnitt.idFoerderschwerpunkt2)) == null ? ""
				: Foerderschwerpunkt.data().getSchluesselByIDOrNull(foerderschwerpunktIdMap.get(lernabschnitt.idFoerderschwerpunkt2));
		adressmerkmal =
				klassenStatistikGesamt.get(lernabschnitt.idKlasse).teilstandort;

		if (adressmerkmal == null) {
			adressmerkmal = "";
			fehlermeldungen.add(
					"Beim Schüler mit der ID: " + schueler.id + " konnte zu folgender Klasse kein Adressmerkmal ermittelt werden: " + lernabschnitt.idKlasse);
		}

		istJva = schueler.istJvaSchueler;
		// TODO: Muss noch über einen CoreType in den Schlüssel umgesetzt werden
		fachklasse = String.valueOf(lernabschnitt.idFachklasse);
	}



	@Override
	public final int hashCode() {
		return Objects.hash(adressmerkmal, aktJahrgang, fachklasse, foerderschwerp, foerderschwerp2, gliederung, istJva,
				klassenKuerzel, klassenart, labk,
				orgForm, reformpdg, schwerstbeh);
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
		return Objects.equals(adressmerkmal, other.adressmerkmal) && Objects.equals(aktJahrgang, other.aktJahrgang)
				&& Objects.equals(fachklasse, other.fachklasse) && Objects.equals(foerderschwerp, other.foerderschwerp)
				&& Objects.equals(foerderschwerp2, other.foerderschwerp2) && Objects.equals(gliederung, other.gliederung)
				&& (istJva == other.istJva)
				&& Objects.equals(klassenKuerzel, other.klassenKuerzel) && Objects.equals(klassenart, other.klassenart)
				&& Objects.equals(labk, other.labk)
				&& Objects.equals(orgForm, other.orgForm) && Objects.equals(reformpdg, other.reformpdg) && (schwerstbeh == other.schwerstbeh);
	}



	@Override
	public final String toString() {
		return "TeilKlassenKey [klassenKuerzel=" + klassenKuerzel + ", gliederung=" + gliederung + ", klassenart=" + klassenart + ", orgForm="
				+ orgForm
				+ ", aktJahrgang=" + aktJahrgang + ", foerderschwerp=" + foerderschwerp + ", schwerstbeh=" + schwerstbeh + ", labk=" + labk
				+ ", reformpdg="
				+ reformpdg + ", foerderschwerp2=" + foerderschwerp2 + ", adressmerkmal=" + adressmerkmal + ", istJva=" + istJva
				+ ", fachklasse=" + fachklasse
				+ "]";
	}



}
