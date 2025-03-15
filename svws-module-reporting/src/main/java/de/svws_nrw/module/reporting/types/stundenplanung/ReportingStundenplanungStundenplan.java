package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.adt.map.ListMap2DLongKeys;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Stundenplan.
 */
public class ReportingStundenplanungStundenplan extends ReportingBaseType {

	/** Die Beschreibung des Stundenplans. */
	protected String beschreibung;

	/** Datum, ab dem der Stundenplan gültig ist. */
	protected String gueltigAb;

	/** Datum, bis zu dem der Stundenplan gültig ist. */
	protected String gueltigBis;

	/** Die ID des Stundenplans. */
	protected long id;

	/** Der Schuljahresabschnitt, dem dieser Stundenplan zugeordnet ist. */
	protected ReportingSchuljahresabschnitt schuljahresabschnitt;

	/** Die Periodizität des Stundenplanes in Wochen (1 = Jede Woche gleich, 2 = Nach zwei Wochen gleich usw.) */
	protected int wochenperiodizitaet = 1;

	/** Eine Map mit den Bezeichnungen der Wochen des Stundenplans zur Wochennummer. */
	protected final HashMap<Integer, String> mapWochenbezeichnungen = new HashMap<>();

	/** Eine interne Map mit allen Eintragungen des Pausenrasters, beginnend mit Position 0 vor der ersten Unterrichtsstunde: [Tag (1-7) | Pos. 0-n]. */
	private final ListMap2DLongKeys<ReportingStundenplanungRasterElement> listMapPausenrastereintragZuTagUndStunde = new ListMap2DLongKeys<>();

	/** Eine interne Map mit allen Stunden des Unterrichtsrasters des Stundenplanes zu deren Position im Unterrichtsraster: [Wochentag (1-7) | Stunde]. */
	private ListMap2DLongKeys<ReportingStundenplanungUnterrichtsrasterstunde> listMapUnterrichtsrasterstundenZuTagUndStunde = new ListMap2DLongKeys<>();

	/** Eine interne Map mit allen Pausenzeiten des Stundenplanes zur deren ID. */
	private final HashMap<Long, ReportingStundenplanungPausenzeit> mapPausenzeiten = new HashMap<>();

	/** Eine interne Map mit allen Räumen des Stundenplanes zur deren ID. */
	private final HashMap<Long, ReportingStundenplanungRaum> mapRaeume = new HashMap<>();

	/** Eine Liste aller Pausenzeiträume im Stundenplan. */
	private final List<ReportingStundenplanungPausenzeit> pausenzeiten = new ArrayList<>();

	/** Eine Liste aller Räume im Stundenplan. */
	private final List<ReportingStundenplanungRaum> raeume = new ArrayList<>();

	/** Eine Liste von Eintragungen des Pausenrasters und der enthaltenen Pausen, die pro Zeile eine Liste von Rasterelementen der Wochentage enthälte. Die
	 * äußere Liste enthält genau ein Element mehr als das Raster der Unterrichte, da dieses die Lücken dazwischen auffüllt.  */
	private final List<ReportingStundenplanungRasterZeile> rasterPausen = new ArrayList<>();

	/** Eine Liste von Stunden des Unterrichtsrasters, die pro Stunde eine Liste von Rasterelementen der Wochentage enthälte. */
	private final List<ReportingStundenplanungRasterZeile> rasterUnterrichte = new ArrayList<>();

	/** Eine Liste mit den Stunden, die im gesamten Unterrichtsraster enthalten sind. */
	private final List<Integer> rasterUnterrichteStundennummern = new ArrayList<>();

	/** Eine Liste mit den Wochentagen, die im gesamten Unterrichtsraster enthalten sind. */
	private final List<Wochentag> rasterUnterrichteWochentage = new ArrayList<>();

	/** Die Stunden des Unterrichtsrasters dieses Stundenplanes. */
	private final List<ReportingStundenplanungUnterrichtsrasterstunde> unterrichtsrasterstunden = new ArrayList<>();

	/** Eine interne Map mit allen Unterrichtsrasterstunden des Stundenplanes zur deren ID. */
	private final HashMap<Long, ReportingStundenplanungUnterrichtsrasterstunde> mapUnterrichtsrasterstunden = new HashMap<>();


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param beschreibung			    Die Beschreibung des Stundenplans
	 * @param gueltigAb				    Datum, ab dem der Stundenplan gültig ist.
	 * @param gueltigBis			    Datum, bis zu dem der Stundenplan gültig ist.
	 * @param id					    Die ID des Stundenplans.
	 * @param pausenzeiten 				Eine Liste aller Pausenzeiten im Stundenplan.
	 * @param raeume				    Eine Liste aller Räume im Stundenplan.
	 * @param schuljahresabschnitt	    Der Schuljahresabschnitt, dem dieser Stundenplan zugeordnet ist.
	 * @param wochenperiodizitaet       Die Periodizität des Stundenplanes in Wochen (1 = Jede Woche gleich, 2 = Nach zwei Wochen gleich usw.
	 * @param wochenbezeichnungen       Die Bezeichnungen der Wochen des Stundenplans als Map zur Wochennummer
	 * @param unterrichtsrasterstunden 	Die Stunden des Unterrichtsrasters dieses Stundenplanes.
	 */
	public ReportingStundenplanungStundenplan(final String beschreibung, final String gueltigAb, final String gueltigBis, final long id,
			final List<ReportingStundenplanungPausenzeit> pausenzeiten, final List<ReportingStundenplanungRaum> raeume,
			final ReportingSchuljahresabschnitt schuljahresabschnitt, final int wochenperiodizitaet, final Map<Integer, String> wochenbezeichnungen,
			final List<ReportingStundenplanungUnterrichtsrasterstunde> unterrichtsrasterstunden) {
		this.beschreibung = beschreibung;
		this.gueltigAb = gueltigAb;
		this.gueltigBis = gueltigBis;
		this.id = id;
		this.schuljahresabschnitt = schuljahresabschnitt;
		this.wochenperiodizitaet = wochenperiodizitaet;
		this.mapWochenbezeichnungen.putAll(wochenbezeichnungen);

		setRaeume(raeume);
		setRasterUnterrichteUndPausen(unterrichtsrasterstunden, pausenzeiten);
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingStundenplanungStundenplan other))
			return false;
		return (id == other.id);
	}


	// ##### Berechnete Methode #####

	/**
	 * Prüft, ob das übergebene Datum im Zeitbereich des Stundenplans liegt.
	 *
	 * @param datum Das zu prüfende Datum im Format yyyy-mm-dd
	 *
	 * @return true, wenn das Datum im Zeitbereich des Stundenplans liegt, sonst false. Ist nur eine Gültigkeitsangabe im Plan gesetzt, wird nur gegen diese geprüft.
	 */
	public boolean istDatumImStundenplan(final String datum) {
		if ((datum == null) || (datum.length() != 10))
			return false;
		if ((gueltigAb != null) && (gueltigBis != null))
			return (datum.compareTo(gueltigAb) >= 0) && (datum.compareTo(gueltigBis) <= 0);
		else if (gueltigAb != null)
			return (datum.compareTo(gueltigAb) >= 0);
		else if (gueltigBis != null)
			return (datum.compareTo(gueltigBis) <= 0);

		return false;
	}

	/**
	 * Gibt den Raum zur übergebenen ID aus dem Stundenplan zurück.
	 *
	 * @param id Die ID des Raumes im Stundenplan.
	 *
	 * @return Raum zur ID oder null, wenn es zur ID keinen Raum im Stundenplan gibt.
	 */
	public ReportingStundenplanungRaum raum(final Long id) {
		if (id == null)
			return null;
		return this.mapRaeume.get(id);
	}

	/**
	 * Gibt die Stunde aus dem Unterrichtsraster zur übergebenen ID zurück.
	 *
	 * @param id Die ID der Stunde aus dem Unterrichtsraster.
	 *
	 * @return Stunde aus dem Unterrichtsraster zur ID oder null, wenn es zu ID keine Stunde gibt.
	 */
	public ReportingStundenplanungUnterrichtsrasterstunde unterrichtsrasterstunde(final Long id) {
		if (id == null)
			return null;
		return this.mapUnterrichtsrasterstunden.get(id);
	}

	/**
	 * Gibt eine Liste von Rasterzeilen zurück, die die Pausenraster-Elemente nach Stunde und Tagen organisiert enthalten.
	 *
	 * @return Eine Liste von Rasterzeilen, deren Elemente die Pausenraster-Elemente für eine bestimmte Stunde und Tag repräsentiert.
	 */
	public List<ReportingStundenplanungRasterZeile> rasterPausenNachStundeUndTagen() {
		return rasterPausen;
	}

	/**
	 * Gibt eine Liste Rasterzeilen zurück, die die Rasterelemente des Unterrichtsrasters nach Stunden und Tagen organisiert enthalten.
	 *
	 * @return Eine Liste von Rasterzeilen, deren Elemente die Unterrichtsraster-Elemente für eine bestimmte Stunde und Tag repräsentiert.
	 */
	public List<ReportingStundenplanungRasterZeile> rasterUnterrichteNachStundeUndTagen() {
		return rasterUnterrichte;
	}

	/**
	 * Gibt die Stunde aus dem Unterrichtsraster zum übergebenen Tag und zur übergebenen Stunde zurück.
	 *
	 * @param wochentag Der Wochentag der Stunde aus dem Unterrichtsraster.
	 * @param stunde 	Die Stunde am übergebenen Wochentag.
	 *
	 * @return Stunde aus dem Unterrichtsraster oder null, wenn es zur ID keine Stunde gibt.
	 */
	public ReportingStundenplanungUnterrichtsrasterstunde unterrichtsrasterstunde(final Wochentag wochentag, final int stunde) {
		if ((wochentag == null) || (stunde < 0))
			return null;
		return this.listMapUnterrichtsrasterstundenZuTagUndStunde.getSingle12OrNull(wochentag.id, stunde);
	}

	/**
	 * Gibt die Bezeichnung der Woche zur übergebenen Wochennummer zurück.
	 *
	 * @param wochennummer Die Wochennummer.
	 *
	 * @return Die Bezeichnung der Woche oder ein leerer String, falls keine Bezeichnung existiert.
	 */
	public String wochenbezeichnung(final int wochennummer) {
		return mapWochenbezeichnungen.getOrDefault(wochennummer, "");
	}


	// ##### Getter #####

	/**
	 * Die Beschreibung des Stundenplans.
	 *
	 * @return Inhalt des Feldes beschreibung
	 */
	public String beschreibung() {
		return beschreibung;
	}

	/**
	 * Datum, ab dem der Stundenplan gültig ist.
	 *
	 * @return Inhalt des Feldes gueltigAb
	 */
	public String gueltigAb() {
		return gueltigAb;
	}

	/**
	 * Datum, bis zu dem der Stundenplan gültig ist.
	 *
	 * @return Inhalt des Feldes gueltigBis
	 */
	public String gueltigBis() {
		return gueltigBis;
	}

	/**
	 * Die ID des Stundenplans.
	 *
	 * @return Inhalt des Feldes id
	 */
	public Long id() {
		return id;
	}

	/**
	 * Eine Liste aller Räume im Stundenplan.
	 *
	 * @return Inhalt des Feldes raeume
	 */
	public List<ReportingStundenplanungRaum> raeume() {
		return Collections.unmodifiableList(raeume);
	}

	/**
	 * Der Schuljahresabschnitt, dem dieser Stundenplan zugeordnet ist.
	 *
	 * @return Inhalt des Feldes schuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt schuljahresabschnitt() {
		return schuljahresabschnitt;
	}

	/**
	 * Die Periodizität des Stundenplanes in Wochen.
	 *
	 * @return Inhalt des Feldes wochenperiodizitaet
	 */
	public int wochenperiodizitaet() {
		return wochenperiodizitaet;
	}

	/**
	 * Gibt eine Map zurück, die die Wochenbezeichnungen zur Wochennummer enthält.
	 *
	 * @return Eine Map mit den Wochenbezeichnungen.
	 */
	public Map<Integer, String> wochenbezeichnungen() {
		return Collections.unmodifiableMap(mapWochenbezeichnungen);
	}

	/**
	 * Die Stunden aus dem Unterrichtsraster dieses Stundenplanes.
	 *
	 * @return Inhalt des Feldes unterrichtsrasterstunden
	 */
	public List<ReportingStundenplanungUnterrichtsrasterstunde> unterrichtsrasterstunden() {
		return Collections.unmodifiableList(unterrichtsrasterstunden);
	}

	/**
	 * Gibt die Nummern der Stunden aus dem Unterrichtsraster zurück.
	 *
	 * @return Eine unveränderliche Liste der Nummern aus dem Unterrichtsraster.
	 */
	public List<Integer> unterrichtsrasterStundennummern() {
		return Collections.unmodifiableList(rasterUnterrichteStundennummern);
	}

	/**
	 * Gibt die Wochentage aus dem Unterrichtsraster zurück.
	 *+
	 * @return Eine unveränderliche Liste der Wochentage aus dem Unterrichtsraster.
	 */
	public List<Wochentag> unterrichtsrasterWochentage() {
		return Collections.unmodifiableList(rasterUnterrichteWochentage);
	}


	// ##### Setter #####

	/**
	 * Setzt die Liste der Räume im Stundenplan und aktualisiert die entsprechenden internen Zuordnungen.
	 *
	 * @param raeume Die Liste der Räume, die für den Stundenplan gesetzt werden sollen.
	 */
	protected void setRaeume(final List<ReportingStundenplanungRaum> raeume) {
		this.raeume.clear();
		this.raeume.addAll(raeume);
		this.raeume.sort(Comparator.comparing(ReportingStundenplanungRaum::kuerzel));

		this.mapRaeume.clear();
		this.mapRaeume.putAll(this.raeume.stream().collect(Collectors.toMap(r -> r.id, r -> r)));
	}

	/**
	 * Setzt die Liste der Stunden aus dem Unterrichtsraster für den Stundenplan und aktualisiert die entsprechenden internen Zuordnungen.
	 *
	 * @param unterrichtsrasterstunden Die Liste der Stunden aus dem Unterrichtsraster, die für den Stundenplan gesetzt werden sollen.
	 * @param pausenzeiten             Die Liste der Pausenzeiten in diesem Stundenplan.
	 */
	protected void setRasterUnterrichteUndPausen(final List<ReportingStundenplanungUnterrichtsrasterstunde> unterrichtsrasterstunden,
			final List<ReportingStundenplanungPausenzeit> pausenzeiten) {
		// Erzeuge zunächst alle Eintragungen zu den Unterrichtsrasterstunden.
		this.unterrichtsrasterstunden.clear();
		this.unterrichtsrasterstunden
				.addAll(unterrichtsrasterstunden.stream().sorted(Comparator.comparing((ReportingStundenplanungUnterrichtsrasterstunde zrs) -> zrs.wochentag.id)
						.thenComparing((ReportingStundenplanungUnterrichtsrasterstunde zrs) -> zrs.stundeImUnterrichtsraster)).toList());

		this.mapUnterrichtsrasterstunden.clear();
		this.mapUnterrichtsrasterstunden.putAll(this.unterrichtsrasterstunden.stream().collect(Collectors.toMap(zr -> zr.id, zr -> zr)));

		this.listMapUnterrichtsrasterstundenZuTagUndStunde = new ListMap2DLongKeys<>();
		this.unterrichtsrasterstunden.forEach(zr -> listMapUnterrichtsrasterstundenZuTagUndStunde.add(zr.wochentag.id, zr.stundeImUnterrichtsraster, zr));

		this.rasterUnterrichteStundennummern.clear();
		this.rasterUnterrichteStundennummern.addAll(this.unterrichtsrasterstunden.stream().map(zr -> zr.stundeImUnterrichtsraster).distinct().toList());

		this.rasterUnterrichteWochentage.clear();
		this.rasterUnterrichteWochentage.addAll(this.unterrichtsrasterstunden.stream().map(zr -> zr.wochentag).distinct().toList());

		// Erzeuge dann die Eintragungen zu den Pausenzeiten.
		this.pausenzeiten.clear();
		this.pausenzeiten.addAll(pausenzeiten);
		this.pausenzeiten.sort(null);

		this.mapPausenzeiten.clear();
		this.mapPausenzeiten.putAll(this.pausenzeiten.stream().collect(Collectors.toMap(p -> p.id, p -> p)));

		// Erzeuge die Raster für die spätere Ausgabe des Stundenplans.
		if (!this.unterrichtsrasterstunden.isEmpty() || !this.pausenzeiten.isEmpty())
			erzeugeRasterUnterrichteUndPausen();
	}

	/**
	 * Erstellt die Raster mit den Unterrichtsrasterelementen und den Pausenzeiten.
	 * Ein Raster ist auf die spätere Ausgabe im Stundenplan-Template optimiert und stellt eine Liste von Listen dar.
	 * Die äußere Liste enthält die Zeilen (in der Regel eine Stunde im Unterrichtsraster oder eine Pausenzeit) und die innere Liste die Elemente dieser
	 * Zeile zu den verschiedenen Wochentagen.
	 */
	private void erzeugeRasterUnterrichteUndPausen() {
		rasterUnterrichte.clear();
		rasterPausen.clear();
		erzeugeRasterUnterrichte();
		erzeugeRasterPausen();
	}

	/**
	 * Erstelle das Raster der Unterrichte aus den Unterrichtsrasterstunden.
	 */
	private void erzeugeRasterUnterrichte() {
		final List<ReportingStundenplanungRasterZeile> rasterUnterrichteNachStundeUndTagen = new ArrayList<>();
		// Durchlaufe alle Stundennummern des Zeitrasters der Unterrichte.
		for (final Integer stunde : this.unterrichtsrasterStundennummern()) {
			final List<ReportingStundenplanungRasterElement> listeStundeMitRasterelementProTag = new ArrayList<>();
			// Durchlaufe zur aktuellen Stunde die Wochentage und speichere dann die Unterrichte in der Ergebnisliste.
			for (final Wochentag wochentag : this.unterrichtsrasterWochentage()) {
				listeStundeMitRasterelementProTag.add(new ReportingStundenplanungRasterElement(this.unterrichtsrasterstunde(wochentag, stunde),
						new ArrayList<>()));
			}
			rasterUnterrichteNachStundeUndTagen.add(new ReportingStundenplanungRasterZeile(stunde, stunde.toString(), listeStundeMitRasterelementProTag));
		}
		rasterUnterrichte.addAll(rasterUnterrichteNachStundeUndTagen);
	}

	/**
	 * Erzeugt auf Basis des Rasters der Unterrichte ein leeres Raster der Pausen. Dieses Raster füllt die Lücken zwischen Tagesbeginn und Tagesende, die nicht
	 * von Unterrichtsrasterstunden abgedeckt werden und hat damit genau ein Element mehr als das Raster der Unterrichte.
	 * In dieses Raster werden dann die definierten Pausenzeiten gesetzt.
	 */
	private void erzeugeRasterPausen() {
		// Nach der Erzeugung des Rasters der Unterrichte erzeuge das der Pausen.
		final List<ReportingStundenplanungRasterZeile> rasterPausenNachStundeUndTagen = new ArrayList<>();

		// Beim ersten Element muss die Pausenzeile davor angelegt werden. Diese Elemente füllen die Zeit von 0 Uhr bis zum Beginn der ersten Stunde.
		final List<ReportingStundenplanungRasterElement> listeErstePausenzeileMitRasterelementProTag = new ArrayList<>();
		for (final ReportingStundenplanungRasterElement tageselement : rasterUnterrichte.getFirst().rasterElemente()) {
			final ReportingStundenplanungRasterElement element = new ReportingStundenplanungRasterElement(null, new ArrayList<>());
			element.beginn = 0;
			element.ende = tageselement.beginn;
			element.wochentag = tageselement.wochentag;
			element.zeilennummer = 0;
			listeErstePausenzeileMitRasterelementProTag.add(element);
			listMapPausenrastereintragZuTagUndStunde.add(element.wochentag.id, element.zeilennummer, element);
		}
		rasterPausenNachStundeUndTagen.add(new ReportingStundenplanungRasterZeile(0, "", listeErstePausenzeileMitRasterelementProTag));

		// Bestimme die Pausenzeilen zwischen den einzelnen Stunden der Unterrichtsrasterstunden.
		for (int i = 1; i < rasterUnterrichte.size(); i++) {
			final List<ReportingStundenplanungRasterElement> listePausenzeileMitRasterelementProTag = new ArrayList<>();
			for (int j = 0; j < rasterUnterrichte.get(i).rasterElemente().size(); j++) {
				final ReportingStundenplanungRasterElement element = new ReportingStundenplanungRasterElement(null, new ArrayList<>());
				element.beginn = rasterUnterrichte.get(i - 1).rasterElemente().get(j).ende;
				element.ende = rasterUnterrichte.get(i).rasterElemente().get(j).beginn;
				element.wochentag = rasterUnterrichte.get(i).rasterElemente().get(j).wochentag;
				element.zeilennummer = i;
				listePausenzeileMitRasterelementProTag.add(element);
				listMapPausenrastereintragZuTagUndStunde.add(element.wochentag.id, element.zeilennummer, element);
			}
			rasterPausenNachStundeUndTagen.add(new ReportingStundenplanungRasterZeile(i, "", listePausenzeileMitRasterelementProTag));
		}

		// Nach dem letzten Element muss die Pausenzeile danach angelegt werden. Diese Elemente füllen die Zeit nach dem Unterricht bis null Uhr (1440 Minuten).
		final List<ReportingStundenplanungRasterElement> listeLetztePausenzeileMitRasterelementProTag = new ArrayList<>();
		for (final ReportingStundenplanungRasterElement tageselement : rasterUnterrichte.getLast().rasterElemente()) {
			final ReportingStundenplanungRasterElement element = new ReportingStundenplanungRasterElement(null, new ArrayList<>());
			element.beginn = tageselement.ende;
			element.ende = 1440;
			element.wochentag = tageselement.wochentag;
			element.zeilennummer = rasterUnterrichte.size();
			listeLetztePausenzeileMitRasterelementProTag.add(element);
			listMapPausenrastereintragZuTagUndStunde.add(element.wochentag.id, element.zeilennummer, element);
		}
		rasterPausenNachStundeUndTagen.add(new ReportingStundenplanungRasterZeile(rasterUnterrichte.size(), "", listeLetztePausenzeileMitRasterelementProTag));

		rasterPausen.addAll(rasterPausenNachStundeUndTagen);

		setzePausenzeitenInRaster();
	}

	/**
	 * Ergänzt die Pausenzeiten aus der Liste der Pausenzeiten im Raster der Unterrichte und der Pausen.
	 */
	private void setzePausenzeitenInRaster() {
		for (final ReportingStundenplanungPausenzeit pausenzeit : pausenzeiten) {
			// Durchlaufe alle Pausenzeilen und ergänze die Pausenzeit an der richtigen Stelle, d. h. wenn die Pausenzeit sich (teilweise) mit dem
			// Pausenraster deckt.
			for (final ReportingStundenplanungRasterZeile pausenZeile : rasterPausen) {
				for (final ReportingStundenplanungRasterElement element : pausenZeile.rasterElemente()) {
					if (istPausenzeitInRasterelement(pausenzeit, element)) {
						element.addPausenzeit(pausenzeit);
					}
				}
			}
			// Durchlaufe alle Unterrichtszeilen und ergänze die Pausenzeit an der richtigen Stelle, d. h. wenn die Pause also parallel zu Unterricht liegt.
			for (final ReportingStundenplanungRasterZeile unterrichtsZeile : rasterUnterrichte) {
				for (final ReportingStundenplanungRasterElement element : unterrichtsZeile.rasterElemente()) {
					if (istPausenzeitInRasterelement(pausenzeit, element)) {
						element.addPausenzeit(pausenzeit);
					}
				}
			}
		}

		// Nach dem Setzen der Pausen passe Beginn der ersten Zeile des Pausenrasters und das Ende der letzten Zeile des letzten Pausenrasters an die
		// Pausenzeiten an.
		rasterPausen.getFirst().beginn = pausenzeiten.stream().mapToInt(p -> p.beginn).min().orElse(0);
		rasterPausen.getLast().ende = pausenzeiten.stream().mapToInt(p -> p.ende).max().orElse(1440);
	}

	private boolean istPausenzeitInRasterelement(final ReportingStundenplanungPausenzeit pausenzeit, final ReportingStundenplanungRasterElement element) {
		return (((pausenzeit.dauerInMinuten() > 0) && (element.dauerInMinuten() > 0) && (pausenzeit.wochentag == element.wochentag))
				&& (((pausenzeit.beginn >= element.beginn) && (pausenzeit.beginn < element.ende))
						|| ((pausenzeit.ende > element.beginn) && (pausenzeit.ende <= element.ende))
						|| ((pausenzeit.beginn <= element.beginn) && (pausenzeit.ende >= element.ende))));
	}
}
