package de.svws_nrw.module.reporting.types.stundenplanung;


import de.svws_nrw.core.adt.map.ListMap4DLongKeys;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert ein Element eines Zeitrasters im Rahmen der Stundenplanung.
 */
public class ReportingStundenplanungRasterElement extends ReportingStundenplanungZeitelement {

	/** Eine Liste aller Pausenaufsichten, die in den Pausenzeiten enthalten sind, die zumindest teilweise in diesem Rasterelement enthalten sind. */
	protected final List<ReportingStundenplanungPausenaufsicht> pausenaufsichten = new ArrayList<>();

	/** Eine Liste von Pausenzeiten, die zumindest teilweise in diesem Rasterelement enthalten sind. */
	protected final List<ReportingStundenplanungPausenzeit> pausenzeiten = new ArrayList<>();

	/** Eine Liste von Unterrichten, die diesem Rasterelement zugeordnet sind. */
	protected final ReportingStundenplanungUnterrichtsrasterstunde unterrichtsrasterstunde;

	/** Die Nummer der Zeile des Elements im Raster. */
	protected int zeilennummer = -1;

	/** Eine ListMap der Pausenaufsichten nach den IDs zur Pausenaufsicht, zum Lehrer, zum Aufsichtsbereich und zum Wochentyp. */
	private final ListMap4DLongKeys<ReportingStundenplanungPausenaufsicht> listMapPausenaufsichten = new ListMap4DLongKeys<>();


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse auf Basis einer Stunde aus dem Raster der Unterrichtsstunden.
	 * Sofern eine Unterrichtsrasterstunde übergeben wird, sind deren Zeiten auch die dieses Rasterelements.
	 * Da mehrere Pausenzeiten in einem Rasterelement liegen können, werden dessen Zeiteintragungen nicht übernommen.
	 *
	 * @param unterrichtsrasterstunde Eine Stunde aus dem Raster der Unterrichtsstunden.
	 * @param pausenzeiten            Eine Liste von Pausenzeiten, die zumindest teilweise in diesem Rasterelement enthalten sind.
	 */
	public ReportingStundenplanungRasterElement(final ReportingStundenplanungUnterrichtsrasterstunde unterrichtsrasterstunde,
			final List<ReportingStundenplanungPausenzeit> pausenzeiten) {
		super(null, null, null);

		this.unterrichtsrasterstunde = unterrichtsrasterstunde;

		if (this.unterrichtsrasterstunde != null) {
			this.beginn = unterrichtsrasterstunde.beginn;
			this.ende = unterrichtsrasterstunde.ende;
			this.wochentag = unterrichtsrasterstunde.wochentag;
			this.zeilennummer = unterrichtsrasterstunde.stundeImUnterrichtsraster;
		}

		this.pausenzeiten.addAll(Objects.requireNonNullElseGet(pausenzeiten, ArrayList::new));

		this.pausenzeiten.forEach(pz -> this.pausenaufsichten.addAll(pz.pausenaufsichten().stream().sorted().toList()));
		this.pausenaufsichten.forEach(pa -> this.listMapPausenaufsichten.add(pa.id(), pa.lehrkraft().id(), pa.idAufsichtsbereich(), pa.wochentyp(), pa));
	}


	// ##### Hash und Equals und Compare-Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return 31 + Objects.hash(zeilennummer, wochentag.id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingStundenplanungRasterElement other))
			return false;
		return (Objects.equals(zeilennummer, other.zeilennummer)) && (Objects.equals(wochentag.id, other.wochentag.id));
	}

	/**
	 * Vergleicht dieses ReportingStundenplanungRasterelement mit einem anderen,
	 * um eine sortierte Ordnung zu bestimmen. Der Vergleich erfolgt basierend
	 * auf dem Wochentag sowie der Position.
	 *
	 * @param zeitelement Das zu vergleichende ReportingStundenplanungRasterelement.
	 * @return Einen negativen Wert, wenn dieses Element vor dem angegebenen Element liegt,
	 *         einen positiven Wert, wenn es danach liegt,
	 *         oder 0, wenn beide Elemente in ihrer Anordnung gleich sind.
	 */
	@Override
	public int compareTo(final @NotNull ReportingStundenplanungZeitelement zeitelement) {
		// Eine null beim Wochentag wird als "vor Montag" gewertet.
		final int thisWochentag = (this.wochentag == null) ? 0 : this.wochentag.id;
		final int zeitelementWochentag = (zeitelement.wochentag == null) ? 0 : zeitelement.wochentag.id;

		// Vergleiche die Wochentage.
		if (thisWochentag < zeitelementWochentag)
			return -1;
		if (thisWochentag > zeitelementWochentag)
			return 1;

		// Bei gleichem Wochentag vergleiche die Position, wenn es ein Rasterelement ist.
		if (zeitelement instanceof final ReportingStundenplanungRasterElement rasterelement)
			return Integer.compare(this.zeilennummer, rasterelement.zeilennummer);

		return super.compareTo(zeitelement);
	}


	// ##### Getter #####

	/**
	 * Gibt den frühesten Beginn des Unterrichts oder aller Pausenzeiten dieses Elements zurück.
	 * Dabei wird bei vorhandenem Unterricht dessen Beginn zurückgegeben, andernfalls der früheste Beginn der Pausenzeiten.
	 * Pausen mit Beginn null werden nicht berücksichtigt.
	 *
	 * @return der früheste Beginn aller Einträge, oder null, wenn keiner definiert wurde.
	 */
	public Integer beginnUnterrichtOderPausen() {
		if (this.unterrichtsrasterstunde != null)
			return this.unterrichtsrasterstunde.beginn();

		return pausenzeiten.stream()
				.map(ReportingStundenplanungPausenzeit::beginn)
				.filter(Objects::nonNull)
				.min(Integer::compareTo)
				.orElse(null);
	}

	/**
	 * Gibt das späteste Ende des Unterrichts oder aller Pausenzeiten dieses Elements zurück.
	 * Dabei wird bei vorhandenem Unterricht dessen Ende zurückgegeben, andernfalls das späteste Ende der Pausenzeiten.
	 * Pausen mit Ende null werden nicht berücksichtigt.
	 *
	 * @return das späteste Ende aller Einträge, oder null, wenn keines definiert wurde.
	 */
	public Integer endeUnterrichtOderPausen() {
		if (this.unterrichtsrasterstunde != null)
			return this.unterrichtsrasterstunde.ende();

		return pausenzeiten.stream()
				.map(ReportingStundenplanungPausenzeit::ende)
				.filter(Objects::nonNull)
				.max(Integer::compareTo)
				.orElse(null);
	}


	/**
	 * Gibt die Liste aller Pausenaufsichten, die in den Pausenzeiten enthalten sind, die zumindest teilweise in diesem Rasterelement enthalten sind.
	 *
	 * @return die Liste der Pausenaufsichten
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichten() {
		return Collections.unmodifiableList(pausenaufsichten);
	}

	/**
	 * Gibt die Liste der Pausenzeiten zurück, die zumindest teilweise in diesem Rasterelement enthalten sind.
	 *
	 * @return die Liste der Pausenzeiten
	 */
	public List<ReportingStundenplanungPausenzeit> pausenzeiten() {
		return Collections.unmodifiableList(pausenzeiten);
	}

	/**
	 * Gibt die Liste der Unterrichte zurück, die diesem Rasterelement zugeordnet sind.
	 *
	 * @return die Liste der Unterrichte
	 */
	public ReportingStundenplanungUnterrichtsrasterstunde unterrichtsrasterstunde() {
		return unterrichtsrasterstunde;
	}

	/**
	 * Gibt eine Liste von Pausenaufsichten basierend auf der angegebenen ID und dem Wochentyp zurück.
	 * Optional können zusätzlich Pausenaufsichten des Wochentyps 0 einbezogen werden.
	 *
	 * @param id                    Die ID, für die die Pausenaufsichten ermittelt werden sollen.
	 * @param wochentyp             Der Wochentyp, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param inklusiveWochentyp0   Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste der ermittelten {@link ReportingStundenplanungPausenaufsicht}-Objekte für die gegebene ID und den angegebenen Wochentyp.
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichtenByIdUndWochentyp(final long id, final int wochentyp, final boolean inklusiveWochentyp0) {
		final List<ReportingStundenplanungPausenaufsicht> result = new ArrayList<>();

		if (this.pausenaufsichten.isEmpty())
			return result;

		if (inklusiveWochentyp0) {
			result.addAll(listMapPausenaufsichten.get14(id, 0));
		}
		result.addAll(listMapPausenaufsichten.get14(id, wochentyp));

		return result;
	}

	/**
	 * Liefert eine Liste von Pausenaufsichten basierend auf der Lehrkraft-ID und dem Wochentyp.
	 * Zusätzlich können Pausenaufsichten des Wochentyps 0 einbezogen werden.
	 *
	 * @param idLehrkraft         Die ID der Lehrkraft, für die die Pausenaufsichten ermittelt werden sollen.
	 * @param wochentyp           Der Wochentyp, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param inklusiveWochentyp0 Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste von {@link ReportingStundenplanungPausenaufsicht}-Objekten, die der angegebenen Lehrkraft-ID und dem Wochentyp entsprechen.
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichtenByLehrkraftUndWochentyp(final long idLehrkraft, final int wochentyp,
			final boolean inklusiveWochentyp0) {
		return pausenaufsichtenByLehrkraeftenUndWochentyp(List.of(idLehrkraft), wochentyp, inklusiveWochentyp0);
	}

	/**
	 * Liefert eine Liste von Pausenaufsichten basierend auf den IDs der Lehrkräfte und dem Wochentyp.
	 * Zusätzlich können Pausenaufsichten des Wochentyps 0 einbezogen werden.
	 *
	 * @param idsLehrkraefte      Die IDs der Lehrkräfte, für die die Pausenaufsichten ermittelt werden sollen.
	 * @param wochentyp           Der Wochentyp, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param inklusiveWochentyp0 Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste von {@link ReportingStundenplanungPausenaufsicht}-Objekten, die der angegebenen Lehrkraft-ID und dem Wochentyp entsprechen.
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichtenByLehrkraeftenUndWochentyp(final List<Long> idsLehrkraefte, final int wochentyp,
			final boolean inklusiveWochentyp0) {
		final List<ReportingStundenplanungPausenaufsicht> result = new ArrayList<>();

		if ((idsLehrkraefte == null) || idsLehrkraefte.isEmpty())
			return result;

		if (this.pausenaufsichten.isEmpty())
			return result;

		for (final long idLehrkraft : idsLehrkraefte) {
			if (inklusiveWochentyp0) {
				result.addAll(listMapPausenaufsichten.get24(idLehrkraft, 0));
			}
			result.addAll(listMapPausenaufsichten.get24(idLehrkraft, wochentyp));
		}

		return result;
	}

	/**
	 * Gibt eine Liste von Pausenaufsichten basierend auf der ID des Aufsichtsbereichs und dem Wochentyp zurück.
	 * Optional können zusätzlich Pausenaufsichten des Wochentyps 0 einbezogen werden.
	 *
	 * @param idAufsichtsbereich   Die ID des Aufsichtsbereichs, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param wochentyp            Der Wochentyp, für den die Pausenaufsichten ermittelt werden sollen.
	 * @param inklusiveWochentyp0  Gibt an, ob auch Pausenaufsichten des Wochentyps 0 berücksichtigt werden sollen.
	 * @return Eine Liste von {@link ReportingStundenplanungPausenaufsicht}-Objekten, die dem angegebenen Aufsichtsbereich und Wochentyp entsprechen.
	 */
	public List<ReportingStundenplanungPausenaufsicht> pausenaufsichtenByAufsichtsbereichUndWochentyp(final long idAufsichtsbereich, final int wochentyp,
			final boolean inklusiveWochentyp0) {
		final List<ReportingStundenplanungPausenaufsicht> result = new ArrayList<>();

		if (this.pausenaufsichten.isEmpty())
			return result;

		if (inklusiveWochentyp0) {
			result.addAll(listMapPausenaufsichten.get34(idAufsichtsbereich, 0));
		}
		result.addAll(listMapPausenaufsichten.get24(idAufsichtsbereich, wochentyp));

		return result;
	}

	/**
	 * Gibt die Zeilennummer der Zeile des Elements im Zeitraster zurück.
	 *
	 * @return die Zeilennummer der Zeile im Zeitraster
	 */
	public int zeilennummer() {
		return zeilennummer;
	}

	// ##### Setter #####

	/**
	 * Fügt eine Pausenzeit zu diesem Rasterelement hinzu, sofern sie noch nicht
	 * in der Liste enthalten ist.
	 *
	 * @param pausenzeit Die hinzuzufügende {@link ReportingStundenplanungPausenzeit}.
	 *                   Wenn null, wird keine Aktion ausgeführt.
	 */
	public void addPausenzeit(final ReportingStundenplanungPausenzeit pausenzeit) {
		if ((pausenzeit == null) || this.pausenzeiten.contains(pausenzeit))
			return;

		this.pausenzeiten.add(pausenzeit);

		this.pausenaufsichten.addAll(pausenzeit.pausenaufsichten());
		pausenzeit.pausenaufsichten()
				.forEach(pa -> this.listMapPausenaufsichten.add(pa.id(), pa.lehrkraft().id(), pa.idAufsichtsbereich(), pa.wochentyp(), pa));
	}


}
