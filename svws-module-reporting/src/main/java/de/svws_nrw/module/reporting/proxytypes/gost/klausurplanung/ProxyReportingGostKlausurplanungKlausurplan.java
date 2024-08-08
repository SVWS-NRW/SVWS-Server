package de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraum;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurraumstunde;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausur;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostSchuelerklausurterminraumstunde;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenRaum;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenRaumstunde;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausurTermin;
import de.svws_nrw.data.gost.klausurplan.DataGostKlausurenSchuelerklausurraumstunde;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.module.reporting.proxytypes.kurs.ProxyReportingKurs;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurraum;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKlausurtermin;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungKursklausur;
import de.svws_nrw.module.reporting.types.gost.klausurplanung.ReportingGostKlausurplanungSchuelerklausur;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan und erweitert die Klasse
 *  {@link ReportingGostKlausurplanungKlausurplan}.</p>
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
 *  </ul>
 */
public class ProxyReportingGostKlausurplanungKlausurplan extends ReportingGostKlausurplanungKlausurplan {

	/** Repository für die Reporting. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Klausurmanager des GOSt-Klausurplans. */
	@JsonIgnore
	private final GostKlausurplanManager gostKlausurManager;

	/** Die Räume, in denen Klausuren geschrieben werden. */
	@JsonIgnore
	private final List<GostKlausurraum> gostKlausurraeume = new ArrayList<>();

	/** Die Räume mit den Unterrichtsstunden, in denen Klausur geschrieben wird. */
	@JsonIgnore
	private final List<GostKlausurraumstunde> gostKlausurraumstunden = new ArrayList<>();


	/**
	 * Erstellt ein neues Reporting-Objekt anhand des Abiturjahres und des Gost-Halbjahres.
	 *
	 * @param reportingRepository	Repository für die Reporting.
	 * @param gostKlausurManager 	Der Manager der Klausuren zu diesem Klausurplan
	 */
	public ProxyReportingGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository, final GostKlausurplanManager gostKlausurManager) {
		super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

		this.reportingRepository = reportingRepository;
		this.gostKlausurManager = gostKlausurManager;

		// 1. Schülerstammdaten der Schüler aus den Schülerklausuren ermitteln und in Listen und Maps einfügen.
		initSchueler();

		// 2. Kurs-Objekte anhand der Kursklausuren erzeugen.
		initKurse();

		// 3. Klausurtermine erstellen
		// HINWEIS: Termine werden ohne Klausuren erzeugt. Wenn Klausuren erzeugt werden, werden diese dem Termin zugewiesen.
		super.klausurtermine.addAll(this.gostKlausurManager.terminGetMengeAsList().stream()
				.map(t -> (ReportingGostKlausurplanungKlausurtermin) new ProxyReportingGostKlausurplanungKlausurtermin(t))
				.toList());

		// 4. Kursklausuren erstellen.
		// HINWEIS: Kursklausuren und Klausurtermine erhalten ihre Schülerklausuren erst bei der Erzeugung der Schülerklausuren.
		// HINWEIS: Die Klausurräume werden in einem folgenden Schritt zentral zugewiesen.
		super.kursklausuren.addAll(this.gostKlausurManager.kursklausurGetMengeAsList().stream()
				.map(k -> (ReportingGostKlausurplanungKursklausur) new ProxyReportingGostKlausurplanungKursklausur(
						k,
						this.gostKlausurManager.vorgabeByKursklausur(k),
						(this.gostKlausurManager.terminOrNullByKursklausur(k) == null)
								? null : klausurtermin(this.gostKlausurManager.terminOrNullByKursklausur(k).id),
						kurs(this.gostKlausurManager.kursdatenByKursklausur(k).id)))
				.toList());

		// 5. Klausurräume mit Aufsichten (sofern schon zugeteilt) erstellen.
		initKlausurraeume();

		// 6. Schülerklausuren erstellen.
		initSchuelerklausuren();

		// 7. Sortiere alle Schülerklausuren, sowohl Gesamtliste als auch bei den Kursklausuren.
		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		super.schuelerklausuren.sort(Comparator
				.comparing((ReportingGostKlausurplanungSchuelerklausur sk) -> sk.schueler().nachname(), colGerman)
				.thenComparing(sk -> sk.schueler().vorname(), colGerman)
				.thenComparing(sk -> sk.schueler().vornamen(), colGerman)
				.thenComparing(sk -> sk.schueler().id()));
		super.kursklausuren.forEach(kk -> kk.schuelerklausuren().sort(Comparator
				.comparing((ReportingGostKlausurplanungSchuelerklausur sk) -> sk.schueler().nachname(), colGerman)
				.thenComparing(sk -> sk.schueler().vorname(), colGerman)
				.thenComparing(sk -> sk.schueler().vornamen(), colGerman)
				.thenComparing(sk -> sk.schueler().id())));
	}

	/**
	 * Initialisiert die Schüler für die später zu erstellenden Schülerklausuren.
	 */
	private void initSchueler() {
		final List<SchuelerStammdaten> schuelerStammdaten = new ArrayList<>();
		final List<Long> fehlendeSchueler = new ArrayList<>();

		for (final Long idSchueler : this.gostKlausurManager.schuelerklausurGetMengeAsList().stream().map(s -> s.idSchueler).distinct().toList()) {
			if (this.reportingRepository.mapSchuelerStammdaten().containsKey(idSchueler))
				schuelerStammdaten.add(this.reportingRepository.mapSchuelerStammdaten().get(idSchueler));
			else
				fehlendeSchueler.add(idSchueler);
		}
		if (!fehlendeSchueler.isEmpty()) {
			final List<SchuelerStammdaten> fehlendeSchuelerStammdaten = DataSchuelerStammdaten.getListStammdaten(this.reportingRepository.conn(),
					fehlendeSchueler);
			schuelerStammdaten.addAll(fehlendeSchuelerStammdaten);
			fehlendeSchuelerStammdaten.forEach(s -> this.reportingRepository.mapSchuelerStammdaten().putIfAbsent(s.id, s));
		}

		final Collator colGerman = Collator.getInstance(Locale.GERMAN);
		super.schueler.addAll(schuelerStammdaten.stream().map(s -> (ReportingSchueler) new ProxyReportingSchueler(this.reportingRepository, s))
				.sorted(Comparator.comparing(ReportingSchueler::nachname, colGerman)
						.thenComparing(ReportingSchueler::vorname, colGerman)
						.thenComparing(ReportingSchueler::vornamen, colGerman)
						.thenComparing(ReportingSchueler::id))
				.toList());
	}

	/**
	 * Initialisiert die Kurse für die später zu erstellenden Kursklausuren.
	 */
	private void initKurse() {
		final List<ReportingKurs> gefundeneKurse = new ArrayList<>();
		final List<Long> gefundeneIdsKurse = new ArrayList<>();

		for (final GostKursklausur kursklausur : this.gostKlausurManager.kursklausurGetMengeAsList()) {
			final KursDaten kursDaten = this.gostKlausurManager.kursdatenByKursklausur(kursklausur);
			if (!gefundeneIdsKurse.contains(kursDaten.id)) {
				gefundeneKurse.add(new ProxyReportingKurs(this.reportingRepository, kursDaten));
				gefundeneIdsKurse.add(kursDaten.id);
			}
		}
		super.kurse.addAll(gefundeneKurse);
	}

	/**
	 * Initialisiert die Raumdaten und Unterrichtsstunden der Klausurräume. Das Ergebnis wir in den übergebenen Listen gespeichert.
	 */
	private void initKlausurraeume() {
		try {
			// Sammle zunächst die Klausurräume und Stunden alle Klausurtermine.
			this.gostKlausurraeume.addAll(DataGostKlausurenRaum.getKlausurraeumeZuTerminen(
					this.reportingRepository.conn(), super.klausurtermine.stream().map(t -> t.id).toList()));
			if (!this.gostKlausurraeume.isEmpty()) {
				this.gostKlausurraumstunden.addAll(DataGostKlausurenRaumstunde.getKlausurraumstundenZuRaeumen(
						this.reportingRepository.conn(), this.gostKlausurraeume));

				// Durchlaufe alle Klausurtermine und weise ihnen die ReportingKlausurräume zu, die aus den Daten erzeugt werden.
				for (final ReportingGostKlausurplanungKlausurtermin termin : super.klausurtermine) {
					// Einem Termin können mehrere Räume zugewiesen worden sein. Filtere sie gemäß TerminID.
					final List<GostKlausurraum> terminraeume = this.gostKlausurraeume.stream().filter(r -> r.idTermin == termin.id).toList();
					// Durchlaufe alle Räume, ermittle dabei die Klausurstunden und erzeuge damit die Klausurräume.
					for (final GostKlausurraum terminraum : terminraeume) {
						final List<GostKlausurraumstunde> terminraumstunden =
								this.gostKlausurraumstunden.stream().filter(s -> s.idRaum == terminraum.id).toList();
						termin.klausurraeume().add(
								new ProxyReportingGostKlausurplanungKlausurraum(this.reportingRepository, termin, terminraum, terminraumstunden));
					}
				}
			}
		} catch (final Exception ignore) {
			// Wenn ein Fehler beim Einlesen der Klausurräume auftritt, dann erhlaten die Termine keine Räume und Aufsichten.
		}
	}

	/**
	 * Initialsiert die Schülerklausuren mit allen Infromationen (auch individuelle Raumdaten, Zeit oder Klausurdaten).
	 */
	private void initSchuelerklausuren() {

		// Listen und Maps mit Daten aus den vorherigen Schritten, um nicht erneut auf die DB zugreifen zu müssen.
		final Map<Long, ReportingGostKlausurplanungKlausurtermin> mapKlausurtermine =
				super.klausurtermine.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKlausurtermin::id, t -> t));
		final Map<Long, ReportingGostKlausurplanungKursklausur> mapKursklausuren =
				super.kursklausuren.stream().collect(Collectors.toMap(ReportingGostKlausurplanungKursklausur::id, k -> k));
		final Map<Long, GostKlausurraum> mapGostKlausurraeume = new HashMap<>();
		mapGostKlausurraeume.putAll(this.gostKlausurraeume.stream().collect(Collectors.toMap(r -> r.id, r -> r)));
		final Map<Long, GostKlausurraumstunde> mapGostKlausurraumstunden = new HashMap<>();
		mapGostKlausurraumstunden.putAll(this.gostKlausurraumstunden.stream().collect(Collectors.toMap(s -> s.id, s -> s)));

		// Listen und Maps vorbereiten für die Schülerklaurtermine und -raumstunden.
		final List<GostSchuelerklausurTermin> gostSchuelerklausurtermine = new ArrayList<>();
		final List<GostSchuelerklausurterminraumstunde> gostSchuelerklausurtermineRaumstunden = new ArrayList<>();
		final Map<Long, GostSchuelerklausurTermin> mapGostSchuelerklausurtermine = new HashMap<>();
		final Map<Long, List<GostSchuelerklausurterminraumstunde>> mapGostSchuelerklausurtermineRaumstunden = new HashMap<>();

		// Fülle Listen und Maps vorbereiten für die Schülerklaurtermine und -raumstunden.
		try {
			// Termine der Schülerklausuren
			gostSchuelerklausurtermine.addAll(
					DataGostKlausurenSchuelerklausurTermin.getSchuelerklausurtermineZuSchuelerklausuren(this.reportingRepository.conn(),
							this.gostKlausurManager.schuelerklausurGetMengeAsList()));
			mapGostSchuelerklausurtermine.putAll(gostSchuelerklausurtermine.stream().collect(Collectors.toMap(t -> t.id, t -> t)));

			// Raumstunden zu den Terminen der Schülerklausuren
			gostSchuelerklausurtermineRaumstunden.addAll(
					DataGostKlausurenSchuelerklausurraumstunde.getSchuelerklausurterminraumstundenZuSchuelerklausurterminids(this.reportingRepository.conn(),
							gostSchuelerklausurtermine.stream().map(rs -> rs.id).toList()));

			final List<Long> tempIds = gostSchuelerklausurtermineRaumstunden.stream().map(rs -> rs.idSchuelerklausurtermin).distinct().toList();

			for (final Long i : tempIds) {
				mapGostSchuelerklausurtermineRaumstunden.put(i,
						gostSchuelerklausurtermineRaumstunden.stream().filter(r -> r.idSchuelerklausurtermin == i).toList());
			}
		} catch (final Exception e) {
			// Wenn ein Fehler beim Lesen der Schülertermine und -raumstunden aus der DB auftritt, dann bleiben diese Angaben leer.
			e.printStackTrace();
		}

		// Durchlaufe nun alle Schülerklausuren und erzeuge dafür deren Termine mit Klausurräumen usw.
		for (final GostSchuelerklausur sk : gostKlausurManager.schuelerklausurGetMengeAsList()) {
			// Zu einer Schülerklausur kann es mehrere Schülerklausurtermine geben, die sich in ihrer FolgeNr unterscheiden (z. B. bei Nachschrieb).
			// Filtere daher die Schülerklausurtermine zur Schülerklausur in eine Liste und werte diese aus.
			final List<GostSchuelerklausurTermin> skTermine = gostSchuelerklausurtermine.stream().filter(t -> t.idSchuelerklausur == sk.id).toList();

			for (final GostSchuelerklausurTermin skTermin : skTermine) {
				// 1. Den Klausurtermin für den Schülerklausurtermin erzeugen.
				ReportingGostKlausurplanungKlausurtermin klausurtermin = null;

				// Der Termin mit FolgeNr 0 ist der Termin der Kursklausur, wenn auch die TerminID null ist.
				if ((skTermin.folgeNr == 0) && (skTermin.idTermin == null)) {
					klausurtermin = mapKursklausuren.get(gostKlausurManager.kursklausurBySchuelerklausur(sk).id).klausurtermin();
				} else {
					klausurtermin =
							((mapGostSchuelerklausurtermine.get(skTermin.id) != null) && (mapGostSchuelerklausurtermine.get(skTermin.id).idTermin != null))
									? mapKlausurtermine.get(mapGostSchuelerklausurtermine.get(skTermin.id).idTermin) : null;
				}

				// 2. Den Klausurraum mit den Stunden zum Schülerklausurtermin erzeugen.
				ReportingGostKlausurplanungKlausurraum klausurraum = null;
				final List<GostSchuelerklausurterminraumstunde> gostSchuelerraumstunden = mapGostSchuelerklausurtermineRaumstunden.get(skTermin.id);

				if ((gostSchuelerraumstunden != null) && !gostSchuelerraumstunden.isEmpty()) {
					// Für einen Schüler kann es nur einen Raum zu einem Termin geben, nehme daher den ersten Eintrag der Liste aus den Raumstunden.
					gostKlausurManager.klausurraumGetBySchuelerklausurtermin(skTermin);

					// Änderung von ESR: Der auskommentierte Code gab bei mir einen Fehler. Stattdessen habe ich die Zeile darunter eingefügt
					// final GostKlausurraum gostKlausurraum =
					//	mapGostKlausurraeume.get(mapGostKlausurraumstunden.get(gostSchuelerraumstunden.getFirst().idRaumstunde).idRaum);
					final GostKlausurraum gostKlausurraum = gostKlausurManager.klausurraumGetBySchuelerklausurtermin(skTermin);

					if (gostKlausurraum != null) {
						// Ab hier gibt es einen Klausurraum mit Raumstunden, erzeuge daher den Klausurraum
						final List<GostKlausurraumstunde> gostKlausurraumstundenFuerSchueler = gostSchuelerraumstunden.stream()
								.map(skrs -> mapGostKlausurraumstunden.get(skrs.idRaumstunde)).toList();
						klausurraum = new ProxyReportingGostKlausurplanungKlausurraum(
								this.reportingRepository, klausurtermin, gostKlausurraum, gostKlausurraumstundenFuerSchueler);
					}
				}

				// 3. Schülerklausur erzeugen und der Gesamtliste der Schülerklausuren hinzufügen.
				super.schuelerklausuren.add(new ProxyReportingGostKlausurplanungSchuelerklausur(sk, skTermin, klausurraum, klausurtermin,
						mapKursklausuren.get(gostKlausurManager.kursklausurBySchuelerklausur(sk).id), schueler(sk.idSchueler)));
			}

		}
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	@JsonIgnore
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}

}
