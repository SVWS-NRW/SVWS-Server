package de.svws_nrw.module.pdf.proxytypes.gost.kursplanung;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.module.pdf.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.pdf.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.pdf.proxytypes.schueler.gost.kursplanung.ProxyReportingSchuelerGostKursplanungKursbelegung;
import de.svws_nrw.module.pdf.repositories.ReportingRepository;
import de.svws_nrw.module.pdf.types.gost.kursplanung.ReportingGostKursplanungBlockungsergebnis;
import de.svws_nrw.module.pdf.types.gost.kursplanung.ReportingGostKursplanungKurs;
import de.svws_nrw.module.pdf.types.gost.kursplanung.ReportingGostKursplanungSchiene;
import de.svws_nrw.module.pdf.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.pdf.types.schueler.ReportingSchueler;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKursplanungBlockungsergebnis und erweitert die Klasse
 *  {@link ReportingGostKursplanungBlockungsergebnis}.
 *  Zu der Erweiterung zählen unter anderem die Filtermöglichkeiten für die Hauptlisten Schüler und Kurse des Blockungsergebnisses.
 *  So können Reports, die auf diesen beruhen bei der Ausgabe eingeschränkt werden. Diese Filterungen greifen nicht bei den Listen,
 *  die untergeordnet sind, also zum Beispiel der Liste der Schüler eines Kurses.</p>
 *
 *  <p>In diesem Kontext besitzt die Proxy-Klasse ausschließlich die gleichen Methoden wie die zugehörige Reporting-Super-Klasse.
 *  Während die Super-Klasse aber als reiner Datentyp konzipiert ist, d. h. ohne Anbindung an die Datenbank,
 *  greift die Proxy-Klassen an verschiedenen Stellen auf die Datenbank zu.</p>
 *
 *  <ul>
 *      <li>Die Proxy-Klasse stellt in der Regel einen zusätzlichen Constructor zur Verfügung, um Reporting-Objekte
 *  		aus Stammdatenobjekten (aus dem Package core.data) erstellen zu können. Darin werden Felder, die Reporting-Objekte
 *  		zurückgegeben und nicht im Stammdatenobjekt enthalten sind, mit null initialisiert.</li>
 * 		<li>Die Proxy-Klasse überschreibt einzelne Getter der Super-Klasse (beispielsweise bei Felder, die mit null initialisiert wurden)
 *  		und lädt dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
 *  		halten.</li>
 *  	<li>Die Proxy-Klasse können zudem auf das Repository {@link ReportingRepository} zugreifen. Dieses
 *  		enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 *  		wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 *  		Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *
 *  </ul>
 */
public class ProxyReportingGostKursplanungBlockungsergebnis extends ReportingGostKursplanungBlockungsergebnis {

	/** Repository für die Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Legt fest, ob die Schülermenge gefiltert werden soll. */
	@JsonIgnore
	private boolean filterSchueler = false;

	/** Liste von Schüler-Ids, die als Filter für die Gesamtliste der Schüler genutzt werden soll. */
	@JsonIgnore
	private List<Long> filterIdsSchueler = new ArrayList<>();

	/** Liste mit Schülern des Blockungsergebnisses, die sich als Ergebnis der Filterung der Gesamtliste der Schüler mit einer ID-Liste ergibt. */
	 @JsonIgnore
	private List<ReportingSchueler> schuelerGefiltert = new ArrayList<>();

	/** Legt fest, ob die Kursmenge gefiltert werden soll. */
	@JsonIgnore
	private boolean filterKurse = false;

	/** Liste von Kurs-Ids, die als Filter für die Gesamtliste der Kurse genutzt werden soll. */
	@JsonIgnore
	private List<Long> filterIdsKurse = new ArrayList<>();

	/** Liste mit Kursen des Blockungsergebnisses, die sich als Ergebnis der Filterung der Gesamtliste der Kurse mit einer ID-Liste ergibt. */
	@JsonIgnore
	private List<ReportingGostKursplanungKurs> kurseGefiltert = new ArrayList<>();



	/**
	 * Erstellt ein neues Reporting-Objekt anhand der Blockungsergebnis-ID.
	 * @param reportingRepository	Repository für die Reporting.
	 * @param id 					Die ID des Blockungsergebnisses aus der Kursplanung der gymnasialen Oberstufe.
	*/
	public ProxyReportingGostKursplanungBlockungsergebnis(final ReportingRepository reportingRepository, final long id) {
		super(0, 0, 0, 0, 0, 0, "", "", id, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		this.reportingRepository = reportingRepository;

		// Initialisiere das Blockungsergebnis und dessen Manager.
		final GostBlockungsergebnis blockungsergebnis = (new DataGostBlockungsergebnisse(this.reportingRepository.conn())).getErgebnisFromID(id);
		final GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(this.reportingRepository.conn())).getBlockungsdatenManagerFromDB(blockungsergebnis.blockungID);
		final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, blockungsergebnis);

		// Sortierungen definieren.
		ergebnisManager.kursSetSortierungKursartFachNummer();
		final Collator colGerman = java.text.Collator.getInstance(Locale.GERMAN);

		// Maps für den weiteren Verlauf definieren.
		final HashMap<Long, ReportingGostKursplanungSchiene> mapBlockungsergebnisSchienenmenge = new HashMap<>();
		final HashMap<Long, ReportingSchueler> mapBlockungsergebnisSchuelermenge = new HashMap<>();

		// Lese alle Schüler-IDs der Blockung aus dem Datenmanager aus und lese damit die Stammdaten aller Schüler aus der DB.
		final List<SchuelerStammdaten> schuelerStammdaten = DataSchuelerStammdaten.getListStammdaten(this.reportingRepository.conn(), datenManager.schuelerGetListe().stream().map(s -> s.id).toList());

		// Lege die Stammdaten im Repository ab.
		schuelerStammdaten.forEach(s -> this.reportingRepository.mapSchuelerStammdaten().putIfAbsent(s.id, s));

		// Füge die neu erzeugten Reporting-Objekte der Schüler der internen Map hinzu, um auf die Schüler im Folgenden direkt zugreifen zu können.
		mapBlockungsergebnisSchuelermenge.putAll(schuelerStammdaten
			.stream()
			.map(s -> (ReportingSchueler) new ProxyReportingSchueler(
				reportingRepository,
				s))
			.toList()
			.stream()
			.collect(Collectors.toMap(ReportingSchueler::id, s -> s)));

		// Liste der Schienen aus der Blockung einlesen und diese einer internen Map hinzufügen. Dabei werden Schienen ohne Kurse nicht berücksichtigt.
		mapBlockungsergebnisSchienenmenge.putAll(datenManager.schieneGetListe()
			.stream()
			.filter(s -> !ergebnisManager.getOfSchieneKursmengeSortiert(s.id).isEmpty())
			.map(s -> (ReportingGostKursplanungSchiene) new ProxyReportingGostKursplanungSchiene(
				this,
				ergebnisManager.getOfSchieneAnzahlSchuelerDummy(s.id),
				ergebnisManager.getOfSchieneAnzahlSchuelerExterne(s.id),
				ergebnisManager.getOfSchieneAnzahlSchueler(s.id),
				s.bezeichnung,
				ergebnisManager.getOfSchieneHatKollision(s.id),
				s.id,
				ergebnisManager.getOfSchieneKursmengeMitKollisionen(s.id).stream().map(k -> k.id).toList(),
				ergebnisManager.getOfSchieneSchuelermengeMitKollisionen(s.id).stream().toList(),
				new ArrayList<>(),
				s.nummer))
			.toList()
			.stream()
			.collect(Collectors.toMap(ReportingGostKursplanungSchiene::id, s -> s)));


		// Liste der Kurse aus der Blockung einlesen.
		// Dabei werden auch die Kursbelegungen der Schüler und die Kurse bei den Schienen ergänzt.
		for (final GostBlockungKurs kurs : datenManager.kursGetListeSortiertNachKursartFachNummer()) {
			// Fach für Hintergrundfarbe ermitteln.
			final ZulaessigesFach fach = ZulaessigesFach.getByKuerzelASD(datenManager.faecherManager().get(datenManager.kursGet(kurs.id).fach_id).kuerzel);
			final String farbeClientRGB = (fach != null) ? fach.getHMTLFarbeRGB().replace("rgba", "rgb") : "";

			// Liste der Kurslehrer erzeugen.
			List<ReportingLehrer> kursLehrer = new ArrayList<>();
			if (!datenManager.kursGetLehrkraefteSortiert(kurs.id).isEmpty())
				 kursLehrer = datenManager.kursGetLehrkraefteSortiert(kurs.id)
					.stream()
					.map(l -> (ReportingLehrer) new ProxyReportingLehrer(
						reportingRepository,
						reportingRepository.mapLehrerStammdaten().computeIfAbsent(l.id, ls -> new DataLehrerStammdaten(reportingRepository.conn()).getFromID(l.id))))
					.toList();

			// Den Kurs der Gost-Kurplanung erzeugen.
			// Darin fehlen die Kurschüler. Diese werden später durch das ProxyKursobjekt nachgeladen (lazy-loading), in dem dort
			// alle Schüler durchlaufen werden und deren Kursbelegung geprüft wird.
			// Zudem fehlen in den Schienen der Schienenliste dieses Kurses die anderen Kurse der Schiene. Auch diese
			// werden später nachgeladen, in dem das ProxySchienenobjekt alle Kurse durchläuft und deren Schienenzugehörigkeit auswertet.
			final ReportingGostKursplanungKurs reportingGostKursplanungKurs = new ProxyReportingGostKursplanungKurs(
				this,
				ergebnisManager.getOfKursAnzahlSchuelerAbiturLK(kurs.id),
				ergebnisManager.getOfKursAnzahlSchuelerAbitur3(kurs.id),
				ergebnisManager.getOfKursAnzahlSchuelerAbitur4(kurs.id),
				ergebnisManager.getOfKursAnzahlSchuelerDummy(kurs.id),
				ergebnisManager.getOfKursAnzahlSchuelerExterne(kurs.id),
				ergebnisManager.getOfKursAnzahlSchuelerSchriftlich(kurs.id),
				ergebnisManager.getOfKursAnzahlSchueler(kurs.id),
				datenManager.kursGetName(kurs.id),
				farbeClientRGB,
				GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel,
				kurs.id,
				GostKursart.fromID(ergebnisManager.getKursE(kurs.id).kursart).kuerzel,
				kursLehrer,
				ergebnisManager.getOfKursSchienenmenge(kurs.id).stream().map(s -> mapBlockungsergebnisSchienenmenge.get(s.id)).toList(),
				new ArrayList<>());

			// Ergänze bei den Schülern die Kursbelegung mit dem neuen Kurs (ohne die Mitschüler des Kurses).
			for (final long idKursschueler : ergebnisManager.getOfKursSchuelermenge(kurs.id).stream().map(s -> s.id).toList()) {
				mapBlockungsergebnisSchuelermenge.get(idKursschueler).gostKursplanungKursbelegungen()
					.add(new ProxyReportingSchuelerGostKursplanungKursbelegung(
						Objects.toString(ergebnisManager.getOfSchuelerOfKursFachwahl(idKursschueler, kurs.id).abiturfach, ""),
						ergebnisManager.getOfSchuelerOfKursFachwahl(idKursschueler, kurs.id).istSchriftlich,
						reportingGostKursplanungKurs));
			}

			// Füge den neuen Kurs in die Liste der Kurse der entsprechenden Schienen ein.
			reportingGostKursplanungKurs.schienen().forEach(s -> mapBlockungsergebnisSchienenmenge.get(s.id()).kurse().add(reportingGostKursplanungKurs));

			// Füge den neuen Kurs in die Liste der Kurse ein und initialisiere damit schrittweise die Liste der Super-Klasse.
			super.kurse().add(reportingGostKursplanungKurs);
		}

		// Erstelle eine alphabetisch sortierte Liste der Schülermenge aus dem Blockungsergebnis und initialisiere damit die Liste der Super-Klasse.
		super.schueler().addAll(mapBlockungsergebnisSchuelermenge.values()
			.stream()
			.sorted(Comparator.comparing(ReportingSchueler::nachname, colGerman)
				.thenComparing(ReportingSchueler::vorname, colGerman)
				.thenComparing(ReportingSchueler::vornamen, colGerman)
				.thenComparing(ReportingSchueler::id))
			.toList());

		// Erstelle eine Liste von Schienen aus dem Blockungsergebnis und initialisiere damit die Liste der Super-Klasse.
		datenManager.schieneGetListe()
			.stream()
			.filter(s -> !ergebnisManager.getOfSchieneKursmengeSortiert(s.id).isEmpty())
			.toList()
			.forEach(s -> super.schienen().add(mapBlockungsergebnisSchienenmenge.get(s.id)));

		// Grundwerte des Blockungsergebnisses setzen.
		super.setAbiturjahr(datenManager.daten().abijahrgang);
		super.setAnzahlDummy(ergebnisManager.getAnzahlSchuelerDummy());
		super.setAnzahlExterne(ergebnisManager.getAnzahlSchuelerExterne());
		super.setAnzahlMaxKurseProSchiene(ergebnisManager.getOfSchieneMaxKursanzahl());
		super.setAnzahlSchienen(super.schienen().size());
		super.setAnzahlSchueler(datenManager.schuelerGetAnzahl());
		super.setBezeichnung(datenManager.daten().name);
		super.setGostHalbjahr(GostHalbjahr.fromID(datenManager.daten().gostHalbjahr).kuerzel);
	}



	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	@JsonIgnore
	public ReportingRepository reportingRepositorySchule() {
		return reportingRepository;
	}

	/**
	 * Legt fest, ob die Kursmenge gefiltert werden soll.
	 * @return Inhalt des Feldes filterKurse = false
	 */
	@JsonIgnore
	public boolean filterKurse() {
		return filterKurse;
	}

	/**
	 * Legt fest, ob die Kursmenge gefiltert werden soll wird gesetzt.
	 * @param filterKurse Neuer Wert für das Feld filterKurse
	 */
	@JsonIgnore
	public void setFilterKurse(final boolean filterKurse) {
		this.filterKurse = filterKurse;
	}

	/**
	 * Liste von Kurs-Ids, die als Filter für die Gesamtliste der Kurse genutzt werden soll.
	 * @return Inhalt des Feldes filterIdsKurse
	 */
	@JsonIgnore
	public List<Long> filterIdsKurse() {
		return filterIdsKurse;
	}

	/**
	 * Liste von Kurs-Ids, die als Filter für die Gesamtliste der Kurse genutzt werden soll wird gesetzt.
	 * Wenn die Liste gleich viele oder mehr IDs enthält, als es Kurse in der Blockung gibt, so wird die Filterliste
	 * ignoriert und eine Filterliste erstellt, die alle Kurs-IDs der Blockung enthält.
	 * @param filterIdsKurse Neuer Wert für das Feld filterIdsKurse
	 */
	@JsonIgnore
	public void setFilterIdsKurse(final List<Long> filterIdsKurse) {
		this.filterIdsKurse = new ArrayList<>();
		this.kurseGefiltert = new ArrayList<>();
		if (filterIdsKurse != null && !filterIdsKurse.isEmpty()) {
			if (filterIdsKurse.size() < super.kurse().size()) {
				this.filterIdsKurse = filterIdsKurse;
				this.kurseGefiltert = new ArrayList<>();
				this.kurseGefiltert.addAll(
					super.kurse()
						.stream()
						.filter(k -> this.filterIdsKurse.contains(k.id()))
						.toList());
			} else {
				this.filterIdsKurse.addAll(super.kurse().stream().map(ReportingGostKursplanungKurs::id).toList());
				this.kurseGefiltert.addAll(super.kurse());
			}
		}
	}

	/**
	 * Liste mit Kursen des Blockungsergebnisses, die sich als Ergebnis der Filterung der Gesamtliste der Kurse mit einer ID-Liste ergibt.
	 * Ist die Filterung deaktiviert, wird die Gesamtliste der Kurse zurückgegeben.
	 * @return Inhalt des Feldes kurseGefiltert
	 */
	@JsonIgnore
	public List<ReportingGostKursplanungKurs> kurseGefiltert() {
		return this.filterKurse ? this.kurseGefiltert : super.kurse();
	}

	/**
	 * Liste mit Kursen des Blockungsergebnisses, die sich als Ergebnis der Filterung der Gesamtliste der Kurse mit einer ID-Liste ergibt, wird gesetzt.
	 * @param kurseGefiltert Neuer Wert für das Feld kurseGefiltert
	 */
	@JsonIgnore
	public void setKurseGefiltert(final List<ReportingGostKursplanungKurs> kurseGefiltert) {
		this.kurseGefiltert = kurseGefiltert;
	}

	/**
	 * Legt fest, ob die Schülermenge gefiltert werden soll.
	 * @return Inhalt des Feldes filterSchueler = false
	 */
	@JsonIgnore
	public boolean filterSchueler() {
		return filterSchueler;
	}

	/**
	 * Legt fest, ob die Schülermenge gefiltert werden soll wird gesetzt.
	 * @param filterSchueler Neuer Wert für das Feld filterSchueler
	 */
	@JsonIgnore
	public void setFilterSchueler(final boolean filterSchueler) {
		this.filterSchueler = filterSchueler;
	}

	/**
	 * Liste von Schüler-Ids, die als Filter für die Gesamtliste der Schüler genutzt werden soll.
	 * @return Inhalt des Feldes filterIdsSchueler
	 */
	@JsonIgnore
	public List<Long> filterIdsSchueler() {
		return filterIdsSchueler;
	}

	/**
	 * Liste von Schüler-Ids, die als Filter für die Gesamtliste der Schüler genutzt werden soll, wird gesetzt.
	 * Wenn die Liste gleich viele oder mehr IDs enthält, als es Schüler in der Blockung gibt, so wird die Filterliste
	 * ignoriert und eine Filterliste erstellt, die alle Schüler-IDs der Blockung enthält.
	 * @param filterIdsSchueler Neuer Wert für das Feld filterIdsSchueler
	 */
	@JsonIgnore
	public void setFilterIdsSchueler(final List<Long> filterIdsSchueler) {
		this.filterIdsSchueler = new ArrayList<>();
		this.schuelerGefiltert = new ArrayList<>();
		if (filterIdsSchueler != null && !filterIdsSchueler.isEmpty()) {
			if (filterIdsSchueler.size() < super.schueler().size()) {
				this.filterIdsSchueler = filterIdsSchueler;
				this.schuelerGefiltert = new ArrayList<>();
				this.schuelerGefiltert.addAll(
					super.schueler()
						.stream()
						.filter(s -> this.filterIdsSchueler.contains(s.id()))
						.toList());
			} else {
				this.filterIdsSchueler.addAll(super.schueler().stream().map(ReportingSchueler::id).toList());
				this.schuelerGefiltert.addAll(super.schueler());
			}
		}
	}

	/**
	 * Liste mit Schülern des Blockungsergebnisses, die sich als Ergebnis der Filterung der Gesamtliste der Schüler mit einer ID-Liste ergibt.
	 * Ist die Filterung deaktiviert, wird die Gesamtliste der Schüler zurückgegeben.
	 * @return Inhalt des Feldes schuelerGefiltert
	 */
	@JsonIgnore
	public List<ReportingSchueler> schuelerGefiltert() {
		return this.filterSchueler ? this.schuelerGefiltert : super.schueler();
	}

	/**
	 * Liste mit Schülern des Blockungsergebnisses, die sich als Ergebnis der Filterung der Gesamtliste der Schüler mit einer ID-Liste ergibt, wird gesetzt.
	 * @param schuelerGefiltert Neuer Wert für das Feld schuelerGefiltert
	 */
	@JsonIgnore
	public void setSchuelerGefiltert(final List<ReportingSchueler> schuelerGefiltert) {
		this.schuelerGefiltert = schuelerGefiltert;
	}
}
