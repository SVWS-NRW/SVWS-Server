package de.svws_nrw.module.reporting.types.stundenplanung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * Diese Klasse repräsentiert eine Zeile im Pausenraster oder Unterrichtsraster mit den zugehörigen RasterElementen.
 */
public class ReportingStundenplanungRasterZeile extends ReportingStundenplanungZeitelement {

	/** Die Zeilennummer im Raster. */
	protected int zeilennummer;

	/** Die Beschriftung der Zeile im Raster. */
	protected String zeilenbeschriftung;

	/** Die Liste der RasterElemente in dieser Zeile. */
	protected final List<ReportingStundenplanungRasterElement> rasterElemente = new ArrayList<>();


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param zeilennummer       Die Zeilennummer im Raster.
	 * @param zeilenbeschriftung Die Beschriftung der Zeile im Raster.
	 * @param rasterElemente     Die Liste der RasterElemente, die dieser Zeile zugeordnet sind.
	 */
	public ReportingStundenplanungRasterZeile(final int zeilennummer, final String zeilenbeschriftung,
			final List<ReportingStundenplanungRasterElement> rasterElemente) {
		super(null, null, null);

		this.zeilennummer = zeilennummer;
		this.zeilenbeschriftung = zeilenbeschriftung;
		addRasterElemente(rasterElemente);
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return 31 + Objects.hash(zeilennummer, beginn, ende);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		final ReportingStundenplanungRasterZeile other = (ReportingStundenplanungRasterZeile) obj;
		return (zeilennummer == other.zeilennummer) && Objects.equals(beginn, other.beginn) && Objects.equals(ende, other.ende);
	}


	// ##### Berechnete Methoden #####

	/**
	 * Ermittelt den frühesten Beginn und spätesten Ende aus den RasterElementen
	 * und setzt die Attribute 'beginn' und 'ende' entsprechend.
	 */
	private void ermittleBeginnUndEndeAusRasterElementen() {
		beginn = rasterElemente.stream()
				.map(ReportingStundenplanungRasterElement::beginn)
				.filter(Objects::nonNull)
				.min(Integer::compareTo)
				.orElse(null);

		ende = rasterElemente.stream()
				.map(ReportingStundenplanungRasterElement::ende)
				.filter(Objects::nonNull)
				.max(Integer::compareTo)
				.orElse(null);
	}

	/**
	 * Prüft, ob eine Lehrkraft in dieser Zeile eine Pausenaufsicht hat.
	 *
	 * @param idLehrkraft         Die ID der Lehrkraft, für die die Pausenaufsichten geprüft werden sollen.
	 * @param wochentyp           Der Wochentyp, für den die Pausenaufsichten geprüft werden sollen.
	 * @param inklusiveWochentyp0 Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 *
	 * @return                    Gibt true zurück, wenn mindestens eine Aufsicht für die Lehrkraft in den Rasterelementen der Zeile gefunden wird, sonst false.
	 */
	public boolean hatLehrkraftPausenaufsichtInZeile(final long idLehrkraft, final int wochentyp, final boolean inklusiveWochentyp0) {
		return rasterElemente.stream()
				.anyMatch(element -> !element.pausenaufsichtenByLehrkraftUndWochentyp(idLehrkraft, wochentyp, inklusiveWochentyp0).isEmpty());
	}

	/**
	 * Prüft, ob eine Lehrkraft aus einer Liste von Lehrkräften in dieser Zeile eine Pausenaufsicht hat.
	 *
	 * @param idsLehrkraefte      Die IDs der Lehrkräfte, für die die Pausenaufsichten geprüft werden sollen.
	 * @param wochentyp           Der Wochentyp, für den die Pausenaufsichten geprüft werden sollen.
	 * @param inklusiveWochentyp0 Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 *
	 * @return                    Gibt true zurück, wenn mindestens eine Aufsicht für die Lehrkraft in den Rasterelementen der Zeile gefunden wird, sonst false.
	 */
	public boolean habenLehrkraeftePausenaufsichtInZeile(final List<Long> idsLehrkraefte, final int wochentyp, final boolean inklusiveWochentyp0) {
		return rasterElemente.stream()
				.anyMatch(element -> !element.pausenaufsichtenByLehrkraeftenUndWochentyp(idsLehrkraefte, wochentyp, inklusiveWochentyp0).isEmpty());
	}

	/**
	 * Setzt die Zeilennummer für alle RasterElemente dieser Zeile.
	 * Diese Methode iteriert über die Liste der RasterElemente und weist
	 * jedem Element die aktuelle Zeilennummer dieser Instanz zu.
	 */
	private void aktualisiereZeilennummerRasterElemente() {
		rasterElemente.forEach(element -> element.zeilennummer = this.zeilennummer);
	}

	// ##### Getter #####

	/**
	 * Gibt den frühesten Beginn des Unterrichts oder aller Pausenzeiten dieser Zeile zurück.
	 * Dabei wird bei vorhandenem Unterricht dessen Beginn zurückgegeben, andernfalls der früheste Beginn der Pausenzeiten.
	 * Pausen mit Beginn null werden nicht berücksichtigt.
	 *
	 * @return der früheste Beginn aller Einträge, oder null, wenn keiner definiert wurde.
	 */
	public Integer beginnUnterrichtOderPausen() {
		return rasterElemente.stream()
				.map(ReportingStundenplanungRasterElement::beginnUnterrichtOderPausen)
				.filter(Objects::nonNull)
				.min(Integer::compareTo)
				.orElse(null);
	}

	/**
	 * Bestimmt die max. Gesamtdauer dieser Zeile gemäß den enthaltenen Unterrichts- oder Pausenzeiten.
	 *
	 * @return die max. Gesamtdauer dieser Zeile
	 */
	public int dauerZeileInMinutenUnterrichtOderPausen() {
		if ((beginnUnterrichtOderPausen() == null) || (endeUnterrichtOderPausen() == null))
			return 0;
		return endeUnterrichtOderPausen() - beginnUnterrichtOderPausen();
	}

	/**
	 * Gibt das späteste Ende des Unterrichts oder aller Pausenzeiten dieser Zeile zurück.
	 * Dabei wird bei vorhandenem Unterricht dessen Ende zurückgegeben, andernfalls das späteste Ende der Pausenzeiten.
	 * Pausen mit Ende null werden nicht berücksichtigt.
	 *
	 * @return das späteste Ende aller Einträge, oder null, wenn keines definiert wurde.
	 */
	public Integer endeUnterrichtOderPausen() {
		return rasterElemente.stream()
				.map(ReportingStundenplanungRasterElement::endeUnterrichtOderPausen)
				.filter(Objects::nonNull)
				.max(Integer::compareTo)
				.orElse(null);
	}

	/**
	 * Gibt die Liste der RasterElemente in dieser Zeile zurück.
	 *
	 * @return die Liste der RasterElemente
	 */
	public List<ReportingStundenplanungRasterElement> rasterElemente() {
		return Collections.unmodifiableList(rasterElemente);
	}

	/**
	 * Gibt die Beschriftung der Zeile im Raster zurück.
	 *
	 * @return die Zeilenbeschriftung
	 */
	public String zeilenbeschriftung() {
		return zeilenbeschriftung;
	}

	/**
	 * Gibt die Zeilennummer im Raster zurück.
	 *
	 * @return die Zeilennummer
	 */
	public int zeilennummer() {
		return zeilennummer;
	}


	// ##### Setter #####

	/**
	 * Fügt ein neues RasterElement zu dieser Zeile hinzu.
	 *
	 * @param rasterElement das hinzuzufügende RasterElement
	 */
	public void addRasterElement(final ReportingStundenplanungRasterElement rasterElement) {
		if (rasterElement != null) {
			this.rasterElemente.add(rasterElement);
			aktualisiereZeilennummerRasterElemente();
			ermittleBeginnUndEndeAusRasterElementen();
		}
	}

	/**
	 * Fügt neue RasterElemente zu dieser Zeile hinzu.
	 *
	 * @param rasterElemente Liste mit hinzuzufügenden RasterElementen
	 */
	public void addRasterElemente(final List<ReportingStundenplanungRasterElement> rasterElemente) {
		if ((rasterElemente != null) && !rasterElemente.isEmpty()) {
			this.rasterElemente.addAll(rasterElemente);
			aktualisiereZeilennummerRasterElemente();
			ermittleBeginnUndEndeAusRasterElementen();
		}
	}

	/**
	 * Setzt die Liste der RasterElemente zu dieser Zeile neu.
	 *
	 * @param rasterElemente Liste mit zu setzenden RasterElementen
	 */
	public void setRasterElemente(final List<ReportingStundenplanungRasterElement> rasterElemente) {
		this.rasterElemente.clear();
		addRasterElemente(rasterElemente);
	}

}
