package de.svws_nrw.asd.export.aggregation;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import de.svws_nrw.asd.data.statistik.KlassenStatistikGesamt;
import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerLernabschnittStatistikGesamt;
import de.svws_nrw.asd.data.statistik.SchuelerStatistikGesamt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;


/**
 *Sortier und Vergleichsobjekt für die klassen
 */
public class TeilKlassenKey {
	/**
	 *
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
	 * @param s
	 * @param klassenStatistikGesamt
	 * @param lehrerStatistikGesamt
	 * @param idSchuljahresabschnitt
	 * @param jahrgangIds
	 */
	public TeilKlassenKey(final SchuelerStatistikGesamt s, final Map<Long, KlassenStatistikGesamt> klassenStatistikGesamt,
			final Map<Long, LehrerStatistikGesamt> lehrerStatistikGesamt, final long idSchuljahresabschnitt, final Map<Long, Long> jahrgangIds) {
		SchuelerLernabschnittStatistikGesamt lernabschnitt = new SchuelerLernabschnittStatistikGesamt();
		Optional<SchuelerLernabschnittStatistikGesamt> optional =
				s.lernabschnitte.stream().filter(e -> e.idSchuljahresabschnitt == idSchuljahresabschnitt).findFirst();

		if (optional.isPresent()) {
			lernabschnitt = optional.get();
		}

		//TODO: Kuerzel ist eigentlich die interne Bezeichnung; hier statt dessen Jahrgang und Paralellitaet einbauen
		this.klassenKuerzel = klassenStatistikGesamt.get(lernabschnitt.idKlasse).kuerzel;
		this.gliederung = lernabschnitt.schulgliederung;
		//TODO klassenart auf idKlassenart umstellen und aus coretype holen
		this.klassenart = lernabschnitt.Klassenart;
		//TODO organisationsform auf idorganisationsform umstellen und aus coretype holen
		this.orgForm = lernabschnitt.organisationsform;
		this.aktJahrgang = Jahrgaenge.data().getSchluesselByIDOrNull(jahrgangIds.get(lernabschnitt.idJahrgang));
		this.foerderschwerp = Foerderschwerpunkt.data().getSchluesselByIDOrNull(lernabschnitt.idFoerderschwerpunkt1);
		this.schwerstbeh = lernabschnitt.hatSchwerbehinderungsNachweis;
		this.labk = lehrerStatistikGesamt.get(klassenStatistikGesamt.get(lernabschnitt.idKlasse).klassenLeitungen.getFirst()).kuerzel;
		//TODO reformpdg auf Klassenebene in svws-server implementieren
		this.reformpdg = AggregationStatistikExport.EIN_LEERZEICHEN;
		this.foerderschwerp2 = Foerderschwerpunkt.data().getSchluesselByIDOrNull(lernabschnitt.idFoerderschwerpunkt2);
		this.adressmerkmal = klassenStatistikGesamt.get(lernabschnitt.idKlasse).teilstandort;
	}

	@Override
	public final String toString() {
		return "KlassenKey [klasseKuerzel=" + klassenKuerzel + ", gliederung=" + gliederung + ", klassenart=" + klassenart + ", orgForm=" + orgForm
				+ ", aktJahrgang=" + aktJahrgang + ", foerderschwerp=" + foerderschwerp + ", schwerstbeh=" + schwerstbeh + ", labk=" + labk + ", reformpdg="
				+ reformpdg + ", foerderschwerp2=" + foerderschwerp2 + ", adressmerkmal=" + adressmerkmal + "]";
	}

	/**
	 *
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TeilKlassenKey))
			return false;
		TeilKlassenKey that = (TeilKlassenKey) o;
		return Objects.equals(klassenKuerzel, that.klassenKuerzel)
				&& Objects.equals(gliederung, that.gliederung)
				&& Objects.equals(klassenart, that.klassenart)
				&& Objects.equals(orgForm, that.orgForm)
				&& Objects.equals(aktJahrgang, that.aktJahrgang)
				&& Objects.equals(foerderschwerp, that.foerderschwerp)
				&& Objects.equals(schwerstbeh, that.schwerstbeh)
				&& Objects.equals(labk, that.labk)
				&& Objects.equals(reformpdg, that.reformpdg)
				&& Objects.equals(foerderschwerp2, that.foerderschwerp2)
				&& Objects.equals(adressmerkmal, that.adressmerkmal);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(klassenKuerzel, gliederung, klassenart, orgForm,
				aktJahrgang, foerderschwerp, schwerstbeh,
				labk, reformpdg, foerderschwerp2,
				adressmerkmal);
	}
}
