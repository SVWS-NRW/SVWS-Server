package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerLeistungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.adt.map.ListMap4DLongKeys;
import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.data.schueler.SchuelerTelefon;
import de.svws_nrw.data.erzieher.DataErzieherStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerLeistungsdaten;
import de.svws_nrw.data.schueler.DataSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schueler.DataSchuelerSprachbelegung;
import de.svws_nrw.data.schueler.DataSchuelerTelefon;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOSchuelerZuweisung;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.signing.SchulbescheinigungQrDaten;
import de.svws_nrw.module.reporting.signing.SchulbescheinigungQrFactory;
import de.svws_nrw.module.reporting.signing.SchulbescheinigungSignaturzustand;
import de.svws_nrw.module.reporting.sortierung.ComparatorFactory;
import de.svws_nrw.module.reporting.types.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieher;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerAnkreuzkompetenz;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerZuweisung;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerAnkreuzkompetenz;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerZuweisung;
import de.svws_nrw.module.reporting.types.schueler.schulbesuch.ReportingSchuelerSchulbesuch;
import de.svws_nrw.module.reporting.types.schueler.sprachen.ReportingSchuelerSprachbelegung;
import de.svws_nrw.module.reporting.types.schueler.telefon.ProxyReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.types.schueler.telefon.ReportingSchuelerTelefonkontakt;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzRepositoryImpl;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;
import de.svws_nrw.service.schueler.foto.SchuelerFoto;

/**
 * Domänen-Repository für Schülerdaten (Stammdaten, Lernabschnitte, Leistungsdaten und Reporting-Objekte).
 * Die Schülerdaten werden bei Bedarf aus der Datenbank nachgeladen und im Cache gehalten.
 */
public class ReportingRepositorySchueler {

	private final ReportingContext reportingContext;

	private final Map<Long, SchuelerStammdaten> mapSchuelerStammdaten = new HashMap<>();

	/**
	 * Die Fehler gescheiterter Ladevorgänge je Schüler-ID. Sie leben so lange wie der Stammdaten-Cache, weil dessen Fehler-Marker jeden weiteren Ladeversuch
	 * verhindert; ohne diesen Speicher stünde in einer späteren Meldung nur noch, dass das Laden scheiterte, nicht mehr woran.
	 */
	private final Map<Long, Exception> ladefehlerSchuelerStammdaten = new HashMap<>();

	/** Die Fotos je Schüler-ID. Sie werden erst beim ersten Zugriff geladen, denn die Stammdaten werden auch für Ausgaben ohne Fotos gebraucht. */
	private final Map<Long, String> mapSchuelerFotos = new HashMap<>();

	/** Die Fehler gescheiterter Foto-Ladevorgänge je Schüler-ID. */
	private final Map<Long, Exception> ladefehlerSchuelerFotos = new HashMap<>();

	private final Map<Long, List<ErzieherStammdaten>> mapErzieherStammdaten = new HashMap<>();
	private final Map<Long, List<Sprachbelegung>> mapSchuelerSprachbelegungen = new HashMap<>();
	private final Map<Long, SchuelerSchulbesuchsdaten> mapSchuelerSchulbesuchsdaten = new HashMap<>();
	private final ListMap3DLongKeys<SchuelerLeistungsdaten> mapLeistungsdaten = new ListMap3DLongKeys<>();
	private final ListMap4DLongKeys<SchuelerLernabschnittsdaten> mapLernabschnittsdaten = new ListMap4DLongKeys<>();

	private final Map<Long, ReportingSchueler> mapSchueler = new HashMap<>();
	private final Map<Long, List<ReportingSchuelerTelefonkontakt>> mapSchuelerTelefonkontakte = new HashMap<>();
	private final Map<Long, List<ReportingSchuelerZuweisung>> mapSchuelerZuweisungen = new HashMap<>();
	private final Map<Long, List<DTOSchuelerAnkreuzfloskeln>> mapSchuelerAnkreuzkompetenzen = new HashMap<>();
	private final Map<Long, SchulbescheinigungQrDaten> mapSchulbescheinigungQrDaten = new HashMap<>();
	private final Set<Long> idsLernabschnitteZuLadenLeistungsdaten = new HashSet<>();
	private final Set<Long> idsLernabschnitteZuLadenAnkreuzkompetenzen = new HashSet<>();

	/**
	 * Die Fehler gescheiterter Ladevorgänge der Teildaten je Schüler-ID. Sie leben so lange wie der jeweilige Cache, weil dieser nach dem ersten Versuch auch
	 * das erfolglose Ergebnis hält. Bei den Listen-Caches sind diese Maps die einzige Spur des Fehlers: Dort steht anschließend die leere Liste, damit
	 * Konsumenten nie {@code null} sehen, und die ist nicht mehr von einem Schüler ohne solche Daten zu unterscheiden.
	 */
	private final Map<Long, Exception> ladefehlerErzieherStammdaten = new HashMap<>();
	private final Map<Long, Exception> ladefehlerSprachbelegungen = new HashMap<>();
	private final Map<Long, Exception> ladefehlerSchulbesuchsdaten = new HashMap<>();
	private final Map<Long, Exception> ladefehlerTelefonkontakte = new HashMap<>();

	/**
	 * Erstellt ein neues ReportingSchuelerRepository.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 */
	public ReportingRepositorySchueler(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}


	// ##### Schüler (Reporting-Objekte und Stammdaten) #####

	/**
	 * Gibt das ReportingSchueler-Objekt zur übergebenen ID zurück. Fehlt der Eintrag im Cache, wird er aus der Datenbank nachgeladen.
	 * Die Methode delegiert an {@link #schueler(List, boolean)}, damit auch die Map der Schülerstammdaten konsistent gefüllt wird.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Das ReportingSchueler-Objekt oder null, falls der Schüler nicht existiert.
	 */
	public ReportingSchueler schueler(final long idSchueler) {
		final List<ReportingSchueler> result = schueler(List.of(idSchueler), false);
		return result.isEmpty() ? null : result.getFirst();
	}

	/**
	 * Gibt das ReportingSchueler-Objekt zur übergebenen ID für die modulinterne Auflösung von Rückverweisen zurück. Fehlt der Eintrag im Cache,
	 * wird er aus der Datenbank nachgeladen.
	 * Anders als {@link #schueler(long)} wird der Benutzerfilter <b>nicht</b> angewendet: Der Rückverweis eines Objektes auf den Schüler, zu dem
	 * es fachlich gehört, darf nicht davon abhängen, ob dieser Schüler in der Ausgabe erscheint. Für Daten, die an die Vorlagen gehen, ist
	 * weiterhin {@link #schueler(long)} zu verwenden.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Das ReportingSchueler-Objekt oder null, falls der Schüler nicht existiert oder nicht geladen werden konnte.
	 */
	public ReportingSchueler schuelerOhneFilter(final long idSchueler) {
		final List<ReportingSchueler> result = schueler(List.of(idSchueler), false, false);
		return result.isEmpty() ? null : result.getFirst();
	}

	/**
	 * Gibt eine sortierte Liste von ReportingSchueler-Objekten zu den übergebenen IDs zurück.
	 *
	 * @param idsSchueler Liste der Schüler-IDs.
	 *
	 * @return Sortierte Liste von ReportingSchueler-Objekten.
	 */
	public List<ReportingSchueler> schueler(final List<Long> idsSchueler) {
		return schueler(idsSchueler, true);
	}

	/**
	 * Gibt eine Liste von ReportingSchueler-Objekten zu den übergebenen IDs zurück, optional sortiert.
	 *
	 * @param idsSchueler   Liste der Schüler-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 *
	 * @return Liste von ReportingSchueler-Objekten.
	 */
	public List<ReportingSchueler> schueler(final List<Long> idsSchueler, final boolean sortiereListe) {
		return schueler(idsSchueler, sortiereListe, true);
	}

	/**
	 * Gibt eine Liste von ReportingSchueler-Objekten zu den übergebenen IDs zurück, optional sortiert und optional gefiltert. Ein Schüler, dessen Laden
	 * endgültig scheitert, fehlt in der Liste; der Befund wird als Ausgabeproblem gemeldet, weil diese Methode anders als {@link #waehleAus(List)} nur die
	 * Objekte herausgibt.
	 *
	 * @param idsSchueler   Liste der Schüler-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 * @param mitFilter     Gibt an, ob der Benutzerfilter auf die Rückgabe angewendet werden soll. Die Caches werden unabhängig davon vollständig gefüllt.
	 *
	 * @return Liste von ReportingSchueler-Objekten.
	 */
	private List<ReportingSchueler> schueler(final List<Long> idsSchueler, final boolean sortiereListe, final boolean mitFilter) {
		final ReportingAuswahlergebnis<ReportingSchueler> auswahl = waehleAus(idsSchueler, sortiereListe, mitFilter);
		ReportingRepositoryUtils.meldeFehlgeschlageneAuslassungen(this.reportingContext, auswahl, ReportingSchueler.class, "des Schülers");
		return auswahl.objekte();
	}

	/**
	 * Wählt die Schüler zu den übergebenen IDs aus und gibt zusätzlich an, welche IDs nicht in die Ausgabe gelangen. Die Auswahl ist sortiert und gefiltert wie
	 * {@link #schueler(List)}; anders als dort überlebt hier die Angabe, welche IDs fehlen und warum - die Grundlage dafür, dass eine unbekannte ID ausgelassen
	 * statt der ganze Report abgebrochen wird. Ausgelassene und ausgefilterte IDs bleiben getrennt, damit ein ausgefilterter Schüler nicht als Ausgabeproblem
	 * gemeldet wird.
	 *
	 * @param idsSchueler Liste der Schüler-IDs.
	 *
	 * @return Das Auswahlergebnis.
	 */
	public ReportingAuswahlergebnis<ReportingSchueler> waehleAus(final List<Long> idsSchueler) {
		return waehleAus(idsSchueler, true, true);
	}

	/**
	 * Wählt die Schüler zu den übergebenen IDs aus, optional sortiert und optional gefiltert.
	 *
	 * @param idsSchueler   Liste der Schüler-IDs.
	 * @param sortiereListe Gibt an, ob die definierte Sortierung angewendet werden soll.
	 * @param mitFilter     Gibt an, ob der Benutzerfilter angewendet werden soll. Die Caches werden unabhängig davon vollständig gefüllt.
	 *
	 * @return Das Auswahlergebnis.
	 */
	private ReportingAuswahlergebnis<ReportingSchueler> waehleAus(final List<Long> idsSchueler, final boolean sortiereListe, final boolean mitFilter) {
		final Comparator<ReportingSchueler> comparator = ComparatorFactory.buildComparator(
				this.reportingContext.sortierungService(),
				this.reportingContext.logger(),
				ReportingSchueler.class.getSimpleName(),
				ReportingSchueler.SORTIERUNG,
				sortiereListe
		);
		final Predicate<ReportingSchueler> filter = mitFilter
				? ReportingSchueler.FILTER.bedingung(this.reportingContext.filterService().getFilter(ReportingSchueler.class.getSimpleName()), null)
				: null;

		return ReportingRepositoryUtils.waehleAus(
				idsSchueler,
				mapSchuelerStammdaten,
				mapSchueler,
				fehlendeIds -> SchuelerServiceFactory.getNewInstance().getSchuelerStammdatenService().getListOhneFotos(fehlendeIds),
				key -> new ProxyReportingSchueler(this.reportingContext, mapSchuelerStammdaten.get(key)),
				stammdaten -> stammdaten.id,
				comparator, filter,
				"Schüler",
				this.reportingContext.logger(),
				ladefehlerSchuelerStammdaten
		);
	}

	/**
	 * Gibt die IDs aller Schüler zurück, deren Stammdaten bereits im Cache dieses Repositories liegen. Die Methode lädt selbst nichts
	 * nach und wendet keinen Filter an; sie dient dazu, ohnehin benötigte Daten für den bereits bekannten Bestand gebündelt
	 * nachladen zu können, statt je Schüler eine eigene Abfrage abzusetzen.
	 *
	 * @return Unveränderliche Liste der IDs. Aufrufer, die die Liste erweitern wollen, legen eine eigene Kopie an.
	 */
	public List<Long> idsGeladenerSchueler() {
		return List.copyOf(mapSchuelerStammdaten.keySet());
	}

	/**
	 * Übernimmt die Stammdaten eines Schülers in den Cache dieses Repositories und ersetzt dabei einen bereits vorhandenen Eintrag.
	 * Die Methode ist für die Selbstregistrierung des {@link ProxyReportingSchueler} bei dessen Konstruktion vorgesehen; sie ersetzt
	 * den früheren direkten Schreibzugriff auf die Cache-Map.
	 *
	 * @param idSchueler Die ID des Schülers.
	 * @param stammdaten Die Stammdaten des Schülers.
	 */
	public void registriereStammdaten(final long idSchueler, final SchuelerStammdaten stammdaten) {
		mapSchuelerStammdaten.put(idSchueler, stammdaten);
	}

	/**
	 * Liefert das Foto des übergebenen Schülers im Base64-Format. Beim ersten Zugriff werden die Fotos aller bereits bekannten Schüler gesammelt
	 * nachgeladen, so dass eine Ausgabe mit Fotos nicht je Schüler eine eigene Abfrage absetzt.
	 * <p>
	 * Die Stammdaten kommen bewusst ohne Fotos; eine Ausgabe ohne Bilder überträgt sie damit gar nicht erst.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Das Foto im Base64-Format oder ein leerer String, wenn keines hinterlegt ist oder das Laden gescheitert ist.
	 */
	public String schuelerFoto(final long idSchueler) {
		final List<Long> ids = new ArrayList<>(idsGeladenerSchueler());
		ids.add(idSchueler);
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				ids,
				mapSchuelerFotos,
				this::ladeFotos,
				"Schülerfotos",
				this.reportingContext.logger(),
				ladefehlerSchuelerFotos);
		meldeTeildatenLadefehler(ladefehlerSchuelerFotos, idSchueler, ReportingSchueler.class, "Die Fotodaten");
		final String foto = mapSchuelerFotos.get(idSchueler);
		return (foto == null) ? "" : foto;
	}

	/**
	 * Lädt die Fotos zu den übergebenen Schüler-IDs. Schüler ohne hinterlegtes Foto erhalten einen leeren Eintrag: Ohne ihn gälten sie als noch nicht
	 * geladen, und jeder weitere Zugriff stieße eine erneute Abfrage an.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Fotos geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und dem Foto im Base64-Format als Wert.
	 */
	private Map<Long, String> ladeFotos(final List<Long> idsSchueler) {
		final Map<Long, String> gefundene = SchuelerServiceFactory.getNewInstance().getSchuelerFotoService().getBySchuelerIds(idsSchueler).stream()
				.filter(f -> f.fotoBase64() != null)
				.collect(Collectors.toMap(SchuelerFoto::idSchueler, SchuelerFoto::fotoBase64));
		final Map<Long, String> ergebnis = new HashMap<>();
		for (final Long id : idsSchueler) {
			ergebnis.put(id, gefundene.getOrDefault(id, ""));
		}
		return ergebnis;
	}


	// ##### Erzieherstammdaten #####

	/**
	 * Liefert die Erzieherstammdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Liste der Erzieherstammdaten; leere Liste, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public List<ErzieherStammdaten> erzieherStammdaten(final long idSchueler) {
		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(
				mapSchuelerStammdaten.keySet(),
				mapErzieherStammdaten,
				this::ladeErzieherStammdaten,
				"Erzieherstammdaten",
				this.reportingContext.logger(),
				ladefehlerErzieherStammdaten);
		meldeTeildatenLadefehler(ladefehlerErzieherStammdaten, idSchueler, ReportingErzieher.class, "Die Erzieherdaten");
		return mapErzieherStammdaten.getOrDefault(idSchueler, List.of());
	}

	private Map<Long, List<ErzieherStammdaten>> ladeErzieherStammdaten(final List<Long> idsSchueler) {
		return new DataErzieherStammdaten(this.reportingContext.conn()).getListBySchuelerIds(idsSchueler).stream()
				.collect(Collectors.groupingBy(e -> e.idSchueler));
	}


	// ##### Sprachbelegungen #####

	/**
	 * Liefert die Sprachbelegungen zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Liste der Sprachbelegungen; leere Liste, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public List<Sprachbelegung> sprachbelegungen(final long idSchueler) {
		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(
				mapSchuelerStammdaten.keySet(),
				mapSchuelerSprachbelegungen,
				this::ladeSprachbelegungen,
				"Sprachbelegungen",
				this.reportingContext.logger(),
				ladefehlerSprachbelegungen);
		meldeTeildatenLadefehler(ladefehlerSprachbelegungen, idSchueler, ReportingSchuelerSprachbelegung.class, "Die Sprachbelegungen");
		return mapSchuelerSprachbelegungen.getOrDefault(idSchueler, List.of());
	}

	private Map<Long, List<Sprachbelegung>> ladeSprachbelegungen(final List<Long> idsSchueler) {
		return DataSchuelerSprachbelegung.getMapBySchuelerIDs(this.reportingContext.conn(), idsSchueler);
	}


	// ##### Schulbesuchsdaten #####

	/**
	 * Liefert die Schulbesuchsdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die Schulbesuchsdaten oder {@code null}, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public SchuelerSchulbesuchsdaten schulbesuchsdaten(final long idSchueler) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				mapSchuelerStammdaten.keySet(),
				mapSchuelerSchulbesuchsdaten,
				this::ladeSchulbesuchsdaten,
				"Schulbesuchsdaten",
				this.reportingContext.logger(),
				ladefehlerSchulbesuchsdaten);
		meldeTeildatenLadefehler(ladefehlerSchulbesuchsdaten, idSchueler, ReportingSchuelerSchulbesuch.class, "Die Daten zum Schulbesuch");
		return mapSchuelerSchulbesuchsdaten.get(idSchueler);
	}

	private Map<Long, SchuelerSchulbesuchsdaten> ladeSchulbesuchsdaten(final List<Long> idsSchueler) {
		return SchuelerServiceFactory
				.getNewInstance()
				.getSchulbesuchService()
				.getByIds(idsSchueler)
				.stream()
				.collect(Collectors.toMap(sb -> sb.id, sb -> sb));
	}


	// ##### Signierte Schulbescheinigung (QR-Codes) #####

	/**
	 * Liefert die gerenderten QR-Codes der signierten Schulbescheinigung zum übergebenen Schüler. Beim ersten Zugriff
	 * werden für alle bekannten Schüler die XML-Dokumente erzeugt, in einem einzigen Batch signiert und die beiden
	 * QR-Codes (Inhalt und Signatur) als SVG gerendert; das Ergebnis wird im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die QR-Daten der Schulbescheinigung; im Fehlerfall ein Eintrag mit dem Zustand DATENFEHLER oder SIGNIERFEHLER (niemals {@code null}).
	 */
	public SchulbescheinigungQrDaten schulbescheinigungQrDaten(final long idSchueler) {
		// Bewusst nicht über das generische Ladeverfahren: Genau ein Batch-Aufruf für alle Schüler des Vorgangs. Ein Abbruch der Factory - Dienstausfall
		// oder Anmeldefehler - propagiert unverändert; der generische Einzel-ID-Fallback würde ihn verschlucken und den ausgefallenen Dienst je Schüler
		// erneut aufrufen.
		if (mapSchulbescheinigungQrDaten.isEmpty()) {
			// Fehler-Marker (Wert null) bleiben außen vor: Diese Schüler sind bereits von der Auswahl als ausgelassen gemeldet; im Batch erzeugten sie
			// eine zweite Meldung mit anderem Schlüssel, und der Sammler könnte nicht deduplizieren.
			final List<Long> idsGeladen = mapSchuelerStammdaten.entrySet().stream()
					.filter(eintrag -> eintrag.getValue() != null).map(Map.Entry::getKey).toList();
			mapSchulbescheinigungQrDaten.putAll(new SchulbescheinigungQrFactory(this.reportingContext).erzeuge(idsGeladen));
		}
		final SchulbescheinigungQrDaten daten = mapSchulbescheinigungQrDaten.get(idSchueler);
		if (daten != null) {
			return daten;
		}
		// Der Schüler war beim Batch nicht geladen - die Factory garantiert je übergebener ID einen Eintrag. Die Vorlage erhält den Datenfehler-Zustand,
		// ihr Vertrag "niemals null" hält; der Befund wird gemeldet.
		this.reportingContext.meldeAusgabeproblem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.TEILDATEN_FEHLEN,
				ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, idSchueler),
				"Für den Schüler %d liegen keine QR-Daten aus dem Signier-Batch vor; die Bescheinigung erscheint ohne digitale Signatur."
						.formatted(idSchueler), null);
		return new SchulbescheinigungQrDaten(null, null, SchulbescheinigungSignaturzustand.DATENFEHLER);
	}

	// ##### Lernabschnitts- und Leistungsdaten, Ankreuzkompetenzen #####

	/**
	 * Gibt die vierdimensionale Map aller Lernabschnittsdaten der Schüler zurück.
	 *
	 * @return Map der Lernabschnittsdaten.
	 */
	public ListMap4DLongKeys<SchuelerLernabschnittsdaten> lernabschnittsdaten() {
		return mapLernabschnittsdaten;
	}

	/**
	 * Liefert die Lernabschnittsdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt. Schüler-IDs, für die keine Lernabschnitte zurückgegeben wurden, werden
	 * negativ markiert, damit ein erneuter Bulk-Load vermieden wird.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Liste der Lernabschnittsdaten; leere Liste, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public List<SchuelerLernabschnittsdaten> lernabschnitte(final long idSchueler) {
		if (mapLernabschnittsdaten.containsKey1(idSchueler)) {
			return mapLernabschnittsdaten.get1(idSchueler);
		}

		final List<Long> idsSchuelerFehlend = mapSchuelerStammdaten.keySet().stream()
				.filter(id -> !mapLernabschnittsdaten.containsKey1(id))
				.toList();
		final List<Long> idsSchuelerZuLaden = idsSchuelerFehlend.contains(idSchueler) ? idsSchuelerFehlend : List.of(idSchueler);

		final List<SchuelerLernabschnittsdaten> schuelerLernabschnittsdaten;
		try {
			schuelerLernabschnittsdaten = ladeLernabschnitte(idsSchuelerZuLaden);
		} catch (final Exception e) {
			// Gefangen wird jeder Fehler, aber nur der des Datenzugriffs: Seine Ursache lässt sich an dieser Stelle nicht zuverlässig bestimmen und wird
			// deshalb als datensatzbezogen hingenommen - ein Pfad, der nur einen Fehlertyp abfängt, ließe jeden anderen die Ausgabe beenden. Die Verarbeitung
			// der geladenen Daten steht außerhalb des try: Ein Fehler dort ist ein Programmierfehler, kein Datenfehler, und beendet die Ausgabe.
			meldeTeildatenLadefehler(ReportingProblemSchluessel.fuer(ReportingSchuelerLernabschnitt.class, idSchueler),
					"Die Lernabschnitte des Schülers %d".formatted(idSchueler), e);
			return mapLernabschnittsdaten.get1(idSchueler);
		}

		for (final SchuelerLernabschnittsdaten la : schuelerLernabschnittsdaten) {
			mapLernabschnittsdaten.add(la.schuelerID, la.schuljahresabschnitt, la.wechselNr, la.id, la);
			// Speichere die IDs der geladenen Lernabschnitte für das Nachladen der Leistungsdaten und Ankreuzkompetenzen.
			idsLernabschnitteZuLadenLeistungsdaten.add(la.id);
			idsLernabschnitteZuLadenAnkreuzkompetenzen.add(la.id);
		}

		final Set<Long> idsSchuelerMitLernabschnittsdaten = schuelerLernabschnittsdaten.stream().map(la -> la.schuelerID).collect(Collectors.toSet());
		// Schüler ohne Lernabschnitte werden auch in der Lernabschnitt-Map hinterlegt, damit die Abfragen für diese Schüler nicht immer wieder neu gestartet werden müssen.
		for (final Long id : idsSchuelerZuLaden) {
			if (!idsSchuelerMitLernabschnittsdaten.contains(id)) {
				mapLernabschnittsdaten.addEmpty(id, -1, -1, -1);
			}
		}
		return mapLernabschnittsdaten.get1(idSchueler);
	}

	private List<SchuelerLernabschnittsdaten> ladeLernabschnitte(final List<Long> idsSchueler) throws ApiOperationException {
		return new DataSchuelerLernabschnittsdaten(this.reportingContext.conn()).getListFromSchuelerIDs(idsSchueler, false, false);
	}

	/**
	 * Gibt die dreidimensionale Map aller Leistungsdaten der Schüler zurück.
	 *
	 * @return Map der Leistungsdaten.
	 */
	public ListMap3DLongKeys<SchuelerLeistungsdaten> leistungsdaten() {
		return mapLeistungsdaten;
	}

	/**
	 * Liefert die Leistungsdaten für den übergebenen Lernabschnitt eines Schülers als Liste von Reporting-Objekten.
	 * Beim ersten Zugriff auf einen Schuljahresabschnitt werden in einer einzigen Abfrage alle Leistungsdaten für sämtliche
	 * bereits bekannten Lernabschnitte dieses Schuljahresabschnitts nachgeladen und im Cache abgelegt.
	 *
	 * @param reportingSchuelerLernabschnitt Der Lernabschnitt, dessen Leistungsdaten geliefert werden sollen.
	 *
	 * @return Liste der Leistungsdaten des Lernabschnitts. Leere Liste, falls keine vorhanden sind.
	 */
	public List<ReportingSchuelerLeistungsdaten> leistungsdatenZuLernabschnitt(final ReportingSchuelerLernabschnitt reportingSchuelerLernabschnitt) {
		leistungsdatenZuSchuljahresabschnitt(reportingSchuelerLernabschnitt.idSchuljahresabschnitt());
		return mapLeistungsdaten.get12(reportingSchuelerLernabschnitt.idSchueler(), reportingSchuelerLernabschnitt.id()).stream()
				.map(sld -> (ReportingSchuelerLeistungsdaten) new ProxyReportingSchuelerLeistungsdaten(this.reportingContext, reportingSchuelerLernabschnitt,
						sld))
				.toList();
	}

	/**
	 * Stellt sicher, dass die Leistungsdaten der Schüler für alle bekannten Lernabschnitte des angegebenen Schuljahresabschnitts geladen sind.
	 * Beim ersten Aufruf für einen Schuljahresabschnitt werden in einer einzigen Abfrage alle Leistungsdaten für sämtliche im Cache vorhandenen
	 * Lernabschnitte dieses Schuljahresabschnitts nachgeladen und in der 3D-Map nach Schüler-, Lernabschnitts- und Leistungsdaten-ID im Cache abgelegt.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts, für den die Leistungsdaten geladen werden sollen.
	 */
	private void leistungsdatenZuSchuljahresabschnitt(final long idSchuljahresabschnitt) {
		final List<Long> idsLernabschnitte = getLernabschnitteZuLaden(idsLernabschnitteZuLadenLeistungsdaten, idSchuljahresabschnitt);
		if (idsLernabschnitte.isEmpty()) {
			return;
		}
		final List<SchuelerLeistungsdaten> schuelerLeistungsdaten;
		try {
			schuelerLeistungsdaten = new DataSchuelerLeistungsdaten(this.reportingContext.conn()).getByLernabschnitten(idsLernabschnitte);
		} catch (final Exception e) {
			// Der Zugriff gilt allen Lernabschnitten des Schuljahresabschnitts und nicht einem einzelnen Datensatz; der Schlüssel führt deshalb keine ID.
			// Hingenommen wird nur der Fehler des Datenzugriffs; die Verarbeitung der geladenen Daten steht außerhalb des try.
			meldeTeildatenLadefehler(ReportingProblemSchluessel.fuer(ReportingSchuelerLeistungsdaten.class),
					"Die Leistungsdaten zum Schuljahresabschnitt %d".formatted(idSchuljahresabschnitt), e);
			return;
		}

		for (final SchuelerLeistungsdaten sld : schuelerLeistungsdaten) {
			final SchuelerLernabschnittsdaten lernabschnitt = mapLernabschnittsdaten.getSingle4OrNull(sld.lernabschnittID);
			if ((lernabschnitt != null) && !mapLeistungsdaten.containsKey123(lernabschnitt.schuelerID, lernabschnitt.id, sld.id)) {
				mapLeistungsdaten.add(lernabschnitt.schuelerID, lernabschnitt.id, sld.id, sld);
			}
		}
	}

	/**
	 * Liefert die Belegungen der Ankreuzkompetenzen für den übergebenen Lernabschnitt eines Schülers als Liste von Reporting-Objekten.
	 * Beim ersten Zugriff auf einen Schuljahresabschnitt werden in einer einzigen Abfrage alle Belegungen für sämtliche
	 * bereits bekannten Lernabschnitte dieses Schuljahresabschnitts nachgeladen und im Cache abgelegt.
	 *
	 * @param lernabschnitt Der Lernabschnitt, dessen Belegungen der Ankreuzkompetenzen geliefert werden sollen.
	 *
	 * @return Liste der Belegungen der Ankreuzkompetenzen des Lernabschnitts. Leere Liste, falls keine vorhanden sind.
	 */
	public List<ReportingSchuelerAnkreuzkompetenz> schuelerLernabschnittAnkreuzkompetenzen(final ReportingSchuelerLernabschnitt lernabschnitt) {
		schuelerAnkreuzkompetenzenZuSchuljahresabschnitt(lernabschnitt.idSchuljahresabschnitt());
		return mapSchuelerAnkreuzkompetenzen.getOrDefault(lernabschnitt.id(), new ArrayList<>()).stream()
				.map(dto -> (ReportingSchuelerAnkreuzkompetenz) new ProxyReportingSchuelerAnkreuzkompetenz(dto, lernabschnitt))
				.sorted(Comparator.comparingInt(sak -> sak.ankreuzkompetenz().sortierung()))
				.toList();
	}

	/**
	 * Stellt sicher, dass die Ankreuzkompetenzen der Schüler für alle bekannten Lernabschnitte des angegebenen Schuljahresabschnitts geladen sind.
	 * Beim ersten Aufruf für einen Schuljahresabschnitt werden in einer einzigen Abfrage alle Belegungen für sämtliche im Cache vorhandenen
	 * Lernabschnitte dieses Schuljahresabschnitts nachgeladen und nach Lernabschnitts-ID indiziert im Cache abgelegt.
	 *
	 * @param idSchuljahresabschnitt Die ID des Schuljahresabschnitts, für den die Belegungen geladen werden sollen.
	 */
	private void schuelerAnkreuzkompetenzenZuSchuljahresabschnitt(final long idSchuljahresabschnitt) {
		final List<Long> idsLernabschnitte = getLernabschnitteZuLaden(idsLernabschnitteZuLadenAnkreuzkompetenzen, idSchuljahresabschnitt);
		if (idsLernabschnitte.isEmpty()) {
			return;
		}
		final List<DTOSchuelerAnkreuzfloskeln> dtos;
		try {
			dtos = new SchuelerAnkreuzkompetenzRepositoryImpl(this.reportingContext.conn()).findListByLernabschnitt(idsLernabschnitte);
		} catch (final Exception e) {
			// Der Zugriff gilt allen Lernabschnitten des Schuljahresabschnitts und nicht einem einzelnen Datensatz; der Schlüssel führt deshalb keine ID.
			// Hingenommen wird nur der Fehler des Datenzugriffs; die Verarbeitung der geladenen Daten steht außerhalb des try.
			meldeTeildatenLadefehler(ReportingProblemSchluessel.fuer(ReportingSchuelerAnkreuzkompetenz.class),
					"Die Ankreuzkompetenzen zum Schuljahresabschnitt %d".formatted(idSchuljahresabschnitt), e);
			return;
		}

		for (final DTOSchuelerAnkreuzfloskeln dto : dtos) {
			mapSchuelerAnkreuzkompetenzen.computeIfAbsent(dto.Abschnitt_ID, k -> new ArrayList<>()).add(dto);
		}
	}


	// ##### Telefonkontakte #####

	/**
	 * Liefert die Telefonkontakte zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Sortierte Liste der Telefonkontakte; leere Liste, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public List<ReportingSchuelerTelefonkontakt> telefonkontakte(final long idSchueler) {
		ReportingRepositoryUtils.ladeFehlendeListenInRepositoryMap(
				mapSchuelerStammdaten.keySet(),
				mapSchuelerTelefonkontakte,
				this::ladeTelefonkontakte,
				"Telefonkontakte",
				this.reportingContext.logger(),
				ladefehlerTelefonkontakte);
		meldeTeildatenLadefehler(ladefehlerTelefonkontakte, idSchueler, ReportingSchuelerTelefonkontakt.class, "Die Telefonkontakte");
		return mapSchuelerTelefonkontakte.getOrDefault(idSchueler, List.of());
	}

	private Map<Long, List<ReportingSchuelerTelefonkontakt>> ladeTelefonkontakte(final List<Long> idsSchueler) {
		final List<SchuelerTelefon> schuelerTelefone = new DataSchuelerTelefon(this.reportingContext.conn()).getListFromSchuelerIDs(idsSchueler);
		return schuelerTelefone.stream()
				.collect(Collectors.groupingBy(
						dto -> dto.idSchueler,
						Collectors.collectingAndThen(
								Collectors.mapping(
										t -> (ReportingSchuelerTelefonkontakt) new ProxyReportingSchuelerTelefonkontakt(this.reportingContext, t),
										Collectors.toList()),
								list -> {
									list.sort(Comparator.comparing(ReportingSchuelerTelefonkontakt::sortierung));
									return list;
								})));
	}


	// ##### Schüler-Zuweisungen #####

	/**
	 * Lädt die Schüler-Zuweisungen zum übergebenen Lernabschnitt aus der Datenbank und cachet sie. Bei erneutem Aufruf wird der
	 * Cache zurückgegeben.
	 *
	 * @param idLernabschnitt Die ID des Lernabschnitts, zu dem die Zuweisungen geladen werden sollen.
	 * @param lernabschnitt   Der Lernabschnitt, der zur Erstellung der Reporting-Zuweisungen benötigt wird.
	 *
	 * @return Liste der Reporting-Zuweisungen. Leere Liste, falls keine Daten ermittelt werden konnten.
	 */
	public List<ReportingSchuelerZuweisung> zuweisungen(final long idLernabschnitt, final ReportingSchuelerLernabschnitt lernabschnitt) {
		if (mapSchuelerZuweisungen.containsKey(idLernabschnitt)) {
			return mapSchuelerZuweisungen.get(idLernabschnitt);
		}
		final List<ReportingSchuelerZuweisung> reportingZuweisungen = new ArrayList<>();
		final List<DTOSchuelerZuweisung> dtos;
		try {
			dtos = this.reportingContext.conn().queryList(DTOSchuelerZuweisung.QUERY_BY_ABSCHNITT_ID, DTOSchuelerZuweisung.class, idLernabschnitt);
		} catch (final Exception e) {
			// Hingenommen wird nur der Fehler des Datenzugriffs; der Aufbau der Reporting-Objekte steht außerhalb des try, ein Fehler dort ist ein
			// Programmierfehler und beendet die Ausgabe.
			meldeTeildatenLadefehler(ReportingProblemSchluessel.fuer(ReportingSchuelerZuweisung.class, idLernabschnitt),
					"Die Zuweisungen zum Lernabschnitt %d".formatted(idLernabschnitt), e);
			mapSchuelerZuweisungen.put(idLernabschnitt, reportingZuweisungen);
			return reportingZuweisungen;
		}

		if (dtos != null) {
			for (final DTOSchuelerZuweisung dto : dtos) {
				reportingZuweisungen.add(new ProxyReportingSchuelerZuweisung(this.reportingContext, dto, lernabschnitt));
			}
		}
		mapSchuelerZuweisungen.put(idLernabschnitt, reportingZuweisungen);
		return reportingZuweisungen;
	}


	// ##### Meldung ausgelassener Teildaten #####

	/**
	 * Meldet ein Ausgabeproblem, wenn das Laden der genannten Teildaten dieses Schülers gescheitert ist. Ohne festgehaltenen Fehler geschieht nichts.
	 * <p>Der Schlüssel führt die Objektart der Teildaten und die ID des Schülers; damit zählt jede Art von Teildaten je Schüler genau einmal. Diese Methode
	 * ergänzt die gemeinsame Meldestelle allein um die Wortwahl für Schüler.</p>
	 *
	 * @param ladefehler  Die Fehler gescheiterter Ladevorgänge dieser Teildaten je Schüler-ID.
	 * @param idSchueler  Die ID des Schülers, dessen Teildaten fehlen.
	 * @param objektart   Die Objektart der ausgelassenen Teildaten.
	 * @param bezeichnung Die Benennung der ausgelassenen Teildaten für den Logeintrag, etwa "Die Erzieherdaten".
	 */
	private void meldeTeildatenLadefehler(final Map<Long, Exception> ladefehler, final long idSchueler, final Class<?> objektart, final String bezeichnung) {
		ReportingRepositoryUtils.meldeTeildatenLadefehler(this.reportingContext, ladefehler, idSchueler, objektart,
				"%s des Schülers %d".formatted(bezeichnung, idSchueler));
	}

	/**
	 * Meldet ein Ausgabeproblem für Teildaten, deren Laden gescheitert ist. Der Befund läuft über
	 * {@link ReportingRepositoryUtils#meldeTeildatenLadefehler}, damit alle Repositories dieselbe Wortwahl verwenden; ein Abbruch bleibt der
	 * Infrastrukturstörung vorbehalten, die die gemeinsame Klassifikation aus dem Fehler erkennt.
	 *
	 * @param schluessel  Welche Teildaten welches Datensatzes betroffen sind.
	 * @param bezeichnung Die Benennung der ausgelassenen Teildaten für den Logeintrag.
	 * @param fehler      Der Fehler, an dem das Laden gescheitert ist.
	 */
	private void meldeTeildatenLadefehler(final ReportingProblemSchluessel schluessel, final String bezeichnung, final Exception fehler) {
		ReportingRepositoryUtils.meldeTeildatenLadefehler(this.reportingContext, schluessel, bezeichnung, fehler);
	}


	// ##### Hilfsmethoden #####

	/**
	 * Ermittelt die Lernabschnitt-IDs, für die noch Daten (wie Leistungsdaten oder Ankreuzkompetenzen) nachgeladen werden müssen.
	 * Dabei werden nur diejenigen Lernabschnitte berücksichtigt, die zum übergebenen Schuljahresabschnitt gehören.
	 * Die zurückgegebenen IDs werden aus dem übergebenen Set entfernt, sodass sie bei zukünftigen Aufrufen nicht erneut geladen werden.
	 *
	 * @param idsLernabschnitteZuLaden Das Set, welches die IDs der Lernabschnitte enthält, für die noch Daten fehlen.
	 * @param idSchuljahresabschnitt   Die ID des Schuljahresabschnitts, für den Daten nachgeladen werden sollen.
	 *
	 * @return Eine Liste mit Lernabschnitt-IDs, für die im angegebenen Schuljahresabschnitt Daten nachgeladen werden müssen.
	 */
	private List<Long> getLernabschnitteZuLaden(final Set<Long> idsLernabschnitteZuLaden, final long idSchuljahresabschnitt) {
		// Alle Lernabschnitte des Schuljahresabschnitts aus dem Cache holen
		final List<Long> idsLernabschnitteImCache = mapLernabschnittsdaten.get2(idSchuljahresabschnitt).stream()
				.map(la -> la.id)
				.distinct()
				.toList();

		// Herausfiltern, welche dieser Lernabschnitte noch im Set der zu ladenden stehen
		final List<Long> idsLernabschnitteZuLadenImCache = idsLernabschnitteImCache.stream()
				.filter(idsLernabschnitteZuLaden::contains)
				.toList();

		// Die IDs, die wir jetzt laden, aus dem Set entfernen (konsumieren)
		idsLernabschnitteZuLadenImCache.forEach(idsLernabschnitteZuLaden::remove);

		return idsLernabschnitteZuLadenImCache;
	}
}
