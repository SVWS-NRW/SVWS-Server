package de.svws_nrw.mapper.klassen;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongFunction;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.json.JsonDaten;
import de.svws_nrw.service.klassen.KlassenCreateRequest;
import de.svws_nrw.service.klassen.KlassenPatchRequest;
import jakarta.ws.rs.core.Response;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * MapStruct-Mapper für die Konvertierung einer {@link DTOKlassen} in das Core-DTO {@link KlassenDaten} (Lesen).
 * <p>
 * Entscheidung: Die Fachlogik des Mappings (Fallbacks bei Klassenart und Schulgliederung, Halbjahresbetrieb bei
 * Vorgänger- und Folgeklasse, Organisationsformen, Parallelität) liegt als benannte, einzeln testbare Methoden in
 * diesem Mapper. Der Mapper greift nicht auf Repositories, Services oder die Datenbank zu. Alles, was er benötigt
 * (Schulform, Schuljahresabschnitt, die Klassen der Nachbarabschnitte, Klassenleitungen und Schüler), bekommt er
 * über den {@link KlassenMappingContext}. Das Laden der Daten übernimmt der aufrufende Service.
 * <p>
 * Für das Anlegen und Ändern ({@link #toDomain} und {@link #patch}) übernimmt der Mapper alle Attribute, die sich ohne Zugriff auf die
 * Datenbank bestimmen lassen. Das sind die einfachen Attribute, die Organisationsformen, die Schulgliederung, die Klassenart, die Fachklasse,
 * die Parallelität und die Standardwerte einer neuen Klasse. Ungültige Katalogwerte führen zu einer {@link ApiOperationException}. Alles, was die
 * Datenbank braucht (Kürzel, Teilstandort, Jahrgang, Vorgänger- und Folgeklasse, Klassenleitungen), prüft und setzt der {@code KlassenService}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KlassenMapper {

	/** Der Wert für die Sortierung, wenn bei der Klasse keine Sortierung gesetzt ist. */
	int SORTIERUNG_DEFAULT = 32000;

	/** Die Nummer des ersten Schuljahresabschnitts im Halbjahresbetrieb. */
	int ERSTER_ABSCHNITT = 1;

	/** Die Nummer des zweiten Schuljahresabschnitts im Halbjahresbetrieb. */
	int ZWEITER_ABSCHNITT = 2;

	/** Die ID der Schulgliederung, wenn der Klasse keine Schulgliederung zugeordnet werden kann. */
	long KEINE_SCHULGLIEDERUNG = -1;

	/** Die Sortierung einer neu angelegten Klasse. */
	int SORTIERUNG_NEUE_KLASSE = 0;

	/** Die Länge des Jahrgangsteils im ASD-Kürzel der Klasse. Danach folgt die Parallelität. */
	int LAENGE_JAHRGANGSTEIL = 2;

	/** Der Jahrgangsteil im ASD-Kürzel einer jahrgangsübergreifenden Klasse. */
	String JAHRGANGSTEIL_UEBERGREIFEND = "JU";

	/** Der Jahrgangsteil im ASD-Kürzel für den Statistik-Jahrgang E1. */
	String JAHRGANGSTEIL_E1 = "1E";

	/** Der Jahrgangsteil im ASD-Kürzel für den Statistik-Jahrgang E2. */
	String JAHRGANGSTEIL_E2 = "2E";

	/** Die Art der allgemeinbildenden Organisationsformen in den Meldungen. */
	String ART_ALLGEMEINBILDEND = "allgemeinbildend";

	/** Die Art der berufsbildenden Organisationsformen in den Meldungen. */
	String ART_BERUFSBILDEND = "berufsbildend";

	/** Die Art der Organisationsformen des Weiterbildungskollegs in den Meldungen. */
	String ART_WEITERBILDUNG = "Weiterbildungskolleg";

	/** Die Meldung, wenn der Standardwert der Organisationsform nicht bestimmt werden kann. */
	String DEFAULT_ORGANISATIONSFORM_FEHLER = "Fehler beim Anlegen des Default-Wertes für die Organisationsform.";

	/** Die Meldung, wenn der Standardwert der Schulgliederung nicht bestimmt werden kann. */
	String DEFAULT_SCHULGLIEDERUNG_FEHLER = "Fehler beim Anlegen des Default-Wertes für die Schulgliederung.";

	/** Die Meldung, wenn der Standardwert der Klassenart nicht bestimmt werden kann. */
	String DEFAULT_KLASSENART_FEHLER = "Fehler beim Anlegen des Default-Wertes für die Klassenart.";

	/** Die Meldung, wenn eine allgemeinbildende Organisationsform an einer Schule ohne allgemeinbildende Schulform gesetzt wird. */
	String KEINE_ALLGEMEINBILDENDE_SCHULFORM = "Der Wert kann nicht gesetzt werden, da die Schule keine allgemeinbildende Schulform hat.";

	/** Die Meldung, wenn eine berufsbildende Organisationsform an einer Schule ohne berufsbildende Schulform gesetzt wird. */
	String KEINE_BERUFSBILDENDE_SCHULFORM = "Der Wert kann nicht gesetzt werden, da die Schule keine berufsbildende Schulform hat.";

	/** Die Meldung, wenn eine Organisationsform der Weiterbildung an einer Schule ohne Schulform der Weiterbildung gesetzt wird. */
	String KEINE_WEITERBILDUNG_SCHULFORM = "Der Wert kann nicht gesetzt werden, da die Schule keine Schulform für die Weiterbildung hat.";

	/** Die Meldung, wenn die ID keiner Organisationsform der Art entspricht. */
	String ORGANISATIONSFORM_UNGUELTIG = "Die ID %d für die Organisationsform (%s) ist ungültig.";

	/** Die Meldung, wenn die Organisationsform im Schuljahr der Klasse nicht gültig ist. */
	String ORGANISATIONSFORM_UNGUELTIG_IM_SCHULJAHR = "Die ID %d für die Organisationsform (%s) ist für das Schuljahr %d der Klasse ungültig.";

	/** Die Meldung, wenn die Organisationsform NICHT_ZUGEORDNET im Schuljahr der Klasse nicht gültig ist. */
	String NICHT_ZUGEORDNET_UNGUELTIG_IM_SCHULJAHR = "Die allgemeinbildende Organisationsform NICHT_ZUGEORDNET ist für das Schuljahr %d der Klasse ungültig.";

	/** Die Meldung, wenn die ID keiner Schulgliederung entspricht. */
	String SCHULGLIEDERUNG_UNBEKANNT = "Es konnte keine Schulgliederung für die ID %d gefunden werden.";

	/** Die Meldung, wenn die Schulform die Schulgliederung nicht unterstützt. */
	String SCHULGLIEDERUNG_NICHT_UNTERSTUETZT = "Die Schulgliederung wird von der angegeben Schulform nicht unterstützt.";

	/** Die Meldung, wenn die ID keiner Klassenart entspricht. */
	String KLASSENART_UNBEKANNT = "Es konnte keine Klassenart für die ID %d gefunden werden.";

	/** Die Meldung, wenn die Klassenart im Schuljahr der Klasse nicht gültig ist. */
	String KLASSENART_UNGUELTIG_IM_SCHULJAHR = "Die Klassenart mit der ID %d ist für das Schuljahr %d ungültig.";

	/** Die Meldung, wenn die ID keiner Fachklasse im Katalog entspricht. */
	String FACHKLASSE_UNBEKANNT = "Es konnte keine Fachklasse für die ID %d gefunden werden.";

	/** Instanz des Mappers */
	KlassenMapper INSTANCE = Mappers.getMapper(KlassenMapper.class);

	/**
	 * Konvertiert eine {@link DTOKlassen} in ein {@link KlassenDaten}.
	 * Die Vorgänger- und Folgeklasse werden via {@link #mapNachbarklassen} und die Klassenleitungen und Schüler
	 * via {@link #mapListen} nach dem Mapping aus dem {@link KlassenMappingContext} gesetzt.
	 *
	 * @param dto   die Klasse, welche gemappt werden soll
	 * @param ctx   der Mapping-Kontext
	 *
	 * @return die Klassendaten
	 */
	@Mapping(source = "ID",                       target = "id")
	@Mapping(source = "Schuljahresabschnitts_ID", target = "idSchuljahresabschnitt")
	@Mapping(source = "Klasse",                   target = "kuerzel")
	@Mapping(source = "Jahrgang_ID",              target = "idJahrgang")
	@Mapping(source = "ASDKlasse",                target = "parallelitaet",                      qualifiedByName = "mapParallelitaet")
	@Mapping(source = "Sortierung",               target = "sortierung",                         qualifiedByName = "mapSortierung")
	@Mapping(source = "AdrMerkmal",               target = "teilstandort",                       defaultValue = "")
	@Mapping(source = "Bezeichnung",              target = "beschreibung",                       defaultValue = "")
	@Mapping(source = "ASDSchulformNr",           target = "idSchulgliederung",                  qualifiedByName = "mapIdSchulgliederung")
	@Mapping(source = "OrgFormKrz",               target = "idAllgemeinbildendOrganisationsform", qualifiedByName = "mapIdAllgemeinbildendOrganisationsform")
	@Mapping(source = "OrgFormKrz",               target = "idBerufsbildendOrganisationsform",   qualifiedByName = "mapIdBerufsbildendOrganisationsform")
	@Mapping(source = "OrgFormKrz",               target = "idWeiterbildungOrganisationsform",   qualifiedByName = "mapIdWeiterbildungOrganisationsform")
	@Mapping(source = "PruefOrdnung",             target = "pruefungsordnung")
	@Mapping(source = "Klassenart",               target = "idKlassenart",                       qualifiedByName = "mapIdKlassenart")
	@Mapping(source = "NotenGesperrt",            target = "noteneingabeGesperrt")
	@Mapping(source = "Ankreuzzeugnisse",         target = "verwendungAnkreuzkompetenzen")
	@Mapping(source = "Fachklasse_ID",            target = "idFachklasse")
	@Mapping(source = "SommerSem",                target = "beginnSommersemester")
	@Mapping(source = "istJva",                   target = "istJva")
	@Mapping(source = "istReformpaedagogik",      target = "istReformpaedagogik")
	KlassenDaten toApi(DTOKlassen dto, @Context KlassenMappingContext ctx);

	/**
	 * Setzt nach dem Mapping die Kürzel und IDs der Vorgänger- und der Folgeklasse.
	 *
	 * @param dto      die Klasse mit den gespeicherten Kürzeln der Vorgänger- und der Folgeklasse
	 * @param ctx      der Mapping-Kontext mit den Klassen der Nachbarabschnitte
	 * @param target   das bereits gemappte Zielobjekt
	 */
	@AfterMapping
	default void mapNachbarklassen(final DTOKlassen dto, @Context final KlassenMappingContext ctx, @MappingTarget final KlassenDaten target) {
		final Schulform schulform = ctx.schulform();
		final int abschnitt = ctx.abschnitt();
		target.kuerzelVorgaengerklasse = mapKuerzelVorgaengerklasse(dto.Klasse, dto.VKlasse, schulform, abschnitt);
		target.idVorgaengerklasse = mapIdKlasse(ctx.klassenVorher(), target.kuerzelVorgaengerklasse);
		target.kuerzelFolgeklasse = mapKuerzelFolgeklasse(dto.Klasse, dto.FKlasse, schulform, abschnitt);
		target.idFolgeklasse = mapIdKlasse(ctx.klassenNachher(), target.kuerzelFolgeklasse);
	}

	/**
	 * Setzt nach dem Mapping die Klassenleitungen und die Schüler aus dem {@link KlassenMappingContext}.
	 *
	 * @param ctx      der Mapping-Kontext mit den Klassenleitungen und den Schülern
	 * @param target   das bereits gemappte Zielobjekt
	 */
	@AfterMapping
	default void mapListen(@Context final KlassenMappingContext ctx, @MappingTarget final KlassenDaten target) {
		target.klassenLeitungen.addAll(ctx.klassenLeitungen());
		target.schueler.addAll(ctx.schueler());
	}

	/**
	 * Bestimmt das Kürzel der Vorgängerklasse. Ist keine Vorgängerklasse gesetzt, so ist das Ergebnis null.
	 * Im Halbjahresbetrieb (Schulformen außer dem Weiterbildungskolleg) ist die Vorgängerklasse im 2. Abschnitt
	 * die Klasse mit dem eigenen Kürzel im 1. Abschnitt.
	 *
	 * @param kuerzel                  das Kürzel der Klasse
	 * @param kuerzelVorgaengerklasse  das in der Klasse gespeicherte Kürzel der Vorgängerklasse
	 * @param schulform                die Schulform der Schule
	 * @param abschnitt                die Nummer des Schuljahresabschnitts
	 *
	 * @return das Kürzel der Vorgängerklasse oder null
	 */
	default String mapKuerzelVorgaengerklasse(final String kuerzel, final String kuerzelVorgaengerklasse, final Schulform schulform, final int abschnitt) {
		if (kuerzelVorgaengerklasse == null) {
			return null;
		}
		return (istHalbjahresbetrieb(schulform) && (abschnitt == ZWEITER_ABSCHNITT)) ? kuerzel : kuerzelVorgaengerklasse;
	}

	/**
	 * Bestimmt das Kürzel der Folgeklasse. Ist keine Folgeklasse gesetzt, so ist das Ergebnis null.
	 * Im Halbjahresbetrieb (Schulformen außer dem Weiterbildungskolleg) ist die Folgeklasse im 1. Abschnitt
	 * die Klasse mit dem eigenen Kürzel im 2. Abschnitt.
	 *
	 * @param kuerzel                das Kürzel der Klasse
	 * @param kuerzelFolgeklasse     das in der Klasse gespeicherte Kürzel der Folgeklasse
	 * @param schulform              die Schulform der Schule
	 * @param abschnitt              die Nummer des Schuljahresabschnitts
	 *
	 * @return das Kürzel der Folgeklasse oder null
	 */
	default String mapKuerzelFolgeklasse(final String kuerzel, final String kuerzelFolgeklasse, final Schulform schulform, final int abschnitt) {
		if (kuerzelFolgeklasse == null) {
			return null;
		}
		return (istHalbjahresbetrieb(schulform) && (abschnitt == ERSTER_ABSCHNITT)) ? kuerzel : kuerzelFolgeklasse;
	}

	/**
	 * Gibt zurück, ob die Schulform im Halbjahresbetrieb arbeitet. Das ist bei allen Schulformen außer dem
	 * Weiterbildungskolleg (Semesterbetrieb) der Fall.
	 *
	 * @param schulform   die Schulform der Schule
	 *
	 * @return true, wenn die Schulform im Halbjahresbetrieb arbeitet, sonst false
	 */
	default boolean istHalbjahresbetrieb(final Schulform schulform) {
		return schulform != Schulform.WB;
	}

	/**
	 * Bestimmt die ID der Klasse mit dem angegebenen Kürzel aus den Klassen eines Schuljahresabschnitts.
	 *
	 * @param klassen   die Klassen des Schuljahresabschnitts mit dem Kürzel als Schlüssel, kann null sein
	 * @param kuerzel   das Kürzel der gesuchten Klasse, kann null sein
	 *
	 * @return die ID der Klasse oder null, wenn es keine solche Klasse gibt
	 */
	default Long mapIdKlasse(final Map<String, DTOKlassen> klassen, final String kuerzel) {
		if ((klassen == null) || (kuerzel == null)) {
			return null;
		}
		final DTOKlassen klasse = klassen.get(kuerzel);
		return (klasse == null) ? null : klasse.ID;
	}

	/**
	 * Bestimmt die ID der Schulgliederung anhand des Schlüssels aus der Klasse. Wird dazu keine Schulgliederung
	 * gefunden, so wird die Default-Schulgliederung der Schulform verwendet. Gibt es auch diese nicht,
	 * so ist das Ergebnis -1.
	 *
	 * @param schluessel   der Schlüssel der Schulgliederung (ASD-Schulform-Nr.) aus der Klasse, kann null sein
	 * @param ctx          der Mapping-Kontext mit der Schulform und dem Schuljahresabschnitt
	 *
	 * @return die ID der Schulgliederung oder -1
	 */
	@Named("mapIdSchulgliederung")
	default long mapIdSchulgliederung(final String schluessel, @Context final KlassenMappingContext ctx) {
		final int schuljahr = ctx.schuljahr();
		return Optional.ofNullable(Schulgliederung.getBySchuljahrAndSchulformAndSchluessel(schuljahr, ctx.schulform(), schluessel))
				.or(() -> Optional.ofNullable(Schulgliederung.getDefault(ctx.schulform())))
				.map(gliederung -> gliederung.daten(schuljahr).id)
				.orElse(KEINE_SCHULGLIEDERUNG);
	}

	/**
	 * Bestimmt die ID der Klassenart anhand des Kürzels. Ist die Klassenart unbekannt oder für die Schulform
	 * nicht zulässig, so wird die Klassenart "Regelklasse" (RK) verwendet.
	 *
	 * @param kuerzel   das Kürzel der Klassenart, kann null sein
	 * @param ctx       der Mapping-Kontext mit der Schulform und dem Schuljahresabschnitt
	 *
	 * @return die ID der Klassenart
	 */
	@Named("mapIdKlassenart")
	default Long mapIdKlassenart(final String kuerzel, @Context final KlassenMappingContext ctx) {
		final int schuljahr = ctx.schuljahr();
		final KlassenartKatalogEintrag eintrag = Optional.ofNullable(Klassenart.data().getWertByKuerzel(kuerzel))
				.filter(klassenart -> klassenart.hatSchulform(schuljahr, ctx.schulform()))
				.map(klassenart -> klassenart.daten(schuljahr))
				.orElseGet(() -> Klassenart.RK.daten(schuljahr));
		return eintrag.id;
	}

	/**
	 * Bestimmt die ID der allgemeinbildenden Organisationsform anhand des Kürzels.
	 *
	 * @param kuerzel   das Kürzel der Organisationsform, kann null sein
	 * @param ctx       der Mapping-Kontext mit dem Schuljahresabschnitt
	 *
	 * @return die ID der Organisationsform oder null, wenn das Kürzel keine allgemeinbildende Organisationsform bezeichnet
	 */
	@Named("mapIdAllgemeinbildendOrganisationsform")
	default Long mapIdAllgemeinbildendOrganisationsform(final String kuerzel, @Context final KlassenMappingContext ctx) {
		final AllgemeinbildendOrganisationsformen form = AllgemeinbildendOrganisationsformen.data().getWertByKuerzel(kuerzel);
		return (form == null) ? null : form.daten(ctx.schuljahr()).id;
	}

	/**
	 * Bestimmt die ID der berufsbildenden Organisationsform anhand des Kürzels.
	 *
	 * @param kuerzel   das Kürzel der Organisationsform, kann null sein
	 * @param ctx       der Mapping-Kontext mit dem Schuljahresabschnitt
	 *
	 * @return die ID der Organisationsform oder null, wenn das Kürzel keine berufsbildende Organisationsform bezeichnet
	 */
	@Named("mapIdBerufsbildendOrganisationsform")
	default Long mapIdBerufsbildendOrganisationsform(final String kuerzel, @Context final KlassenMappingContext ctx) {
		final BerufskollegOrganisationsformen form = BerufskollegOrganisationsformen.data().getWertByKuerzel(kuerzel);
		return (form == null) ? null : form.daten(ctx.schuljahr()).id;
	}

	/**
	 * Bestimmt die ID der Organisationsform des Weiterbildungskollegs anhand des Kürzels.
	 *
	 * @param kuerzel   das Kürzel der Organisationsform, kann null sein
	 * @param ctx       der Mapping-Kontext mit dem Schuljahresabschnitt
	 *
	 * @return die ID der Organisationsform oder null, wenn das Kürzel keine Organisationsform des Weiterbildungskollegs bezeichnet
	 */
	@Named("mapIdWeiterbildungOrganisationsform")
	default Long mapIdWeiterbildungOrganisationsform(final String kuerzel, @Context final KlassenMappingContext ctx) {
		final WeiterbildungskollegOrganisationsformen form = WeiterbildungskollegOrganisationsformen.data().getWertByKuerzel(kuerzel);
		return (form == null) ? null : form.daten(ctx.schuljahr()).id;
	}

	/**
	 * Bestimmt die Parallelität einer Klasse aus dem ASD-Kürzel der Klasse. Die Parallelität ist der Teil ab dem
	 * 3. Zeichen. Ist das ASD-Kürzel null oder kürzer als 3 Zeichen, so ist das Ergebnis null.
	 *
	 * @param asdKlasse   das ASD-Kürzel der Klasse, kann null sein
	 *
	 * @return die Parallelität oder null
	 */
	@Named("mapParallelitaet")
	default String mapParallelitaet(final String asdKlasse) {
		return ((asdKlasse == null) || (asdKlasse.length() <= LAENGE_JAHRGANGSTEIL)) ? null : asdKlasse.substring(LAENGE_JAHRGANGSTEIL);
	}

	/**
	 * Bestimmt die Sortierung einer Klasse. Ist keine Sortierung gesetzt, so wird {@link #SORTIERUNG_DEFAULT} verwendet.
	 *
	 * @param sortierung   die Sortierung der Klasse, kann null sein
	 *
	 * @return die Sortierung
	 */
	@Named("mapSortierung")
	default int mapSortierung(final Integer sortierung) {
		return (sortierung == null) ? SORTIERUNG_DEFAULT : sortierung;
	}

	/**
	 * Überträgt die angegebenen einfachen Attribute eines {@link KlassenCreateRequest} auf eine neue {@link DTOKlassen}.
	 * Attribute, die im Request null sind, bleiben unverändert, sodass die Standardwerte der Klasse erhalten bleiben.
	 *
	 * @param request   der Request mit den Attributen
	 * @param dto       die neue Klasse
	 */
	default void applySimpleAttributes(final KlassenCreateRequest request, final DTOKlassen dto) {
		if (request.sortierung != null) {
			dto.Sortierung = request.sortierung;
		}
		if (request.beschreibung != null) {
			dto.Bezeichnung = request.beschreibung;
		}
		if (request.noteneingabeGesperrt != null) {
			dto.NotenGesperrt = request.noteneingabeGesperrt;
		}
		if (request.verwendungAnkreuzkompetenzen != null) {
			dto.Ankreuzzeugnisse = request.verwendungAnkreuzkompetenzen;
		}
		if (request.beginnSommersemester != null) {
			dto.SommerSem = request.beginnSommersemester;
		}
		if (request.istJva != null) {
			dto.istJva = request.istJva;
		}
		if (request.istReformpaedagogik != null) {
			dto.istReformpaedagogik = request.istReformpaedagogik;
		}
	}

	/**
	 * Überträgt die gesetzten einfachen Attribute eines {@link KlassenPatchRequest} auf eine {@link DTOKlassen}.
	 * Nicht gesetzte Attribute ({@code undefined}) bleiben unverändert, explizit auf null gesetzte Attribute werden
	 * übernommen. Das gilt für die Beschreibung, bei den übrigen Attributen verhindert die Validierung des Requests null.
	 *
	 * @param request   der Request mit den Attributen
	 * @param dto       die zu ändernde Klasse
	 */
	default void applySimpleAttributes(final KlassenPatchRequest request, final DTOKlassen dto) {
		if (request.kuerzel.isPresent()) {
			dto.Klasse = request.kuerzel.get();
		}
		if (request.sortierung.isPresent()) {
			dto.Sortierung = request.sortierung.get();
		}
		if (request.beschreibung.isPresent()) {
			dto.Bezeichnung = request.beschreibung.get();
		}
		if (request.noteneingabeGesperrt.isPresent()) {
			dto.NotenGesperrt = request.noteneingabeGesperrt.get();
		}
		if (request.verwendungAnkreuzkompetenzen.isPresent()) {
			dto.Ankreuzzeugnisse = request.verwendungAnkreuzkompetenzen.get();
		}
		if (request.beginnSommersemester.isPresent()) {
			dto.SommerSem = request.beginnSommersemester.get();
		}
		if (request.istJva.isPresent()) {
			dto.istJva = request.istJva.get();
		}
		if (request.istReformpaedagogik.isPresent()) {
			dto.istReformpaedagogik = request.istReformpaedagogik.get();
		}
	}

	/**
	 * Erstellt aus dem Request eine neue, noch nicht gespeicherte {@link DTOKlassen}. Attribute, die im Request null sind, behalten den
	 * Standardwert der Schulform. Bei einem Jahrgang null entsteht eine jahrgangsübergreifende Klasse (JU). Die Prüfungen mit Datenbankbezug
	 * (Kürzel, Teilstandort, Jahrgang, Vorgänger- und Folgeklasse) hat der Aufrufer bereits durchgeführt und setzt die Vorgänger- und Folgeklasse danach.
	 *
	 * @param request      der Request mit den Attributen der neuen Klasse
	 * @param abschnitt    der Schuljahresabschnitt der neuen Klasse
	 * @param schulform    die Schulform der Schule
	 * @param jahrgang     der Jahrgang der Klasse oder null bei einer jahrgangsübergreifenden Klasse
	 * @param adrMerkmal   das Adressmerkmal des Teilstandortes, der als Standard verwendet wird
	 *
	 * @return die neue Klasse
	 */
	default DTOKlassen toDomain(final KlassenCreateRequest request, final Schuljahresabschnitt abschnitt, final Schulform schulform,
			final DTOJahrgang jahrgang, final String adrMerkmal) {
		final var dto = new DTOKlassen(0, abschnitt.id, request.kuerzel, false, false);
		applyDefaults(dto, abschnitt, schulform, adrMerkmal);
		applyJahrgang(dto, jahrgang);
		ifNotNull(request.parallelitaet, parallelitaet -> applyParallelitaet(dto, parallelitaet));
		applySimpleAttributes(request, dto);
		ifNotNull(request.teilstandort, teilstandort -> dto.AdrMerkmal = teilstandort);
		ifNotNull(request.idAllgemeinbildendOrganisationsform, id -> applyAllgemeinbildendOrganisationsform(dto, abschnitt, schulform, id));
		ifNotNull(request.idBerufsbildendOrganisationsform, id -> applyBerufsbildendOrganisationsform(dto, abschnitt, schulform, id));
		ifNotNull(request.idWeiterbildungOrganisationsform, id -> applyWeiterbildungOrganisationsform(dto, abschnitt, schulform, id));
		ifNotNull(request.idSchulgliederung, id -> applySchulgliederung(dto, abschnitt, schulform, id));
		ifNotNull(request.idKlassenart, id -> applyKlassenart(dto, abschnitt, id));
		ifNotNull(request.idFachklasse, id -> applyFachklasse(dto, id));
		return dto;
	}

	/**
	 * Überträgt die im Patch gesetzten Attribute, die sich ohne Zugriff auf die Datenbank bestimmen lassen, auf die Klasse. Die Prüfungen
	 * mit Datenbankbezug (Kürzel, Teilstandort) hat der Aufrufer bereits durchgeführt. Jahrgang, Vorgänger- und Folgeklasse und die
	 * Klassenleitungen setzt der Aufrufer danach.
	 *
	 * @param request     der Patch
	 * @param dto         die zu ändernde Klasse
	 * @param abschnitt   der Schuljahresabschnitt der Klasse
	 * @param schulform   die Schulform der Schule
	 */
	default void patch(final KlassenPatchRequest request, final DTOKlassen dto, final Schuljahresabschnitt abschnitt, final Schulform schulform) {
		ifPresent(request.parallelitaet, parallelitaet -> applyParallelitaet(dto, parallelitaet));
		applySimpleAttributes(request, dto);
		ifPresent(request.teilstandort, teilstandort -> dto.AdrMerkmal = teilstandort);
		ifPresent(request.idAllgemeinbildendOrganisationsform, id -> applyAllgemeinbildendOrganisationsform(dto, abschnitt, schulform, id));
		ifPresent(request.idBerufsbildendOrganisationsform, id -> applyBerufsbildendOrganisationsform(dto, abschnitt, schulform, id));
		ifPresent(request.idWeiterbildungOrganisationsform, id -> applyWeiterbildungOrganisationsform(dto, abschnitt, schulform, id));
		ifPresent(request.idSchulgliederung, id -> applySchulgliederung(dto, abschnitt, schulform, id));
		ifPresent(request.idKlassenart, id -> applyKlassenart(dto, abschnitt, id));
		ifPresent(request.idFachklasse, id -> applyFachklasse(dto, id));
	}

	/**
	 * Setzt die Standardwerte einer neuen Klasse: Sortierung 0, das Adressmerkmal sowie die Standardwerte für Organisationsform,
	 * Schulgliederung und Klassenart anhand der Schulform.
	 *
	 * @param dto          die neue Klasse
	 * @param abschnitt    der Schuljahresabschnitt der Klasse
	 * @param schulform    die Schulform der Schule
	 * @param adrMerkmal   das Adressmerkmal des Teilstandortes
	 */
	default void applyDefaults(final DTOKlassen dto, final Schuljahresabschnitt abschnitt, final Schulform schulform, final String adrMerkmal) {
		dto.Sortierung = SORTIERUNG_NEUE_KLASSE;
		dto.AdrMerkmal = adrMerkmal;
		dto.OrgFormKrz = getDefaultOrganisationsform(abschnitt.schuljahr, schulform);
		dto.ASDSchulformNr = getDefaultSchulgliederung(abschnitt.schuljahr, schulform);
		dto.Klassenart = getDefaultKlassenart(abschnitt.schuljahr, schulform);
	}

	/**
	 * Setzt den Jahrgang der Klasse und baut den Jahrgangsteil des ASD-Kürzels neu auf. Bei null handelt es sich um eine
	 * jahrgangsübergreifende Klasse (Jahrgangsteil JU). Die Jahrgänge E1 und E2 werden zu 1E und 2E. Die Parallelität im
	 * ASD-Kürzel bleibt erhalten.
	 *
	 * @param dto        die Klasse
	 * @param jahrgang   der Jahrgang oder null
	 */
	default void applyJahrgang(final DTOKlassen dto, final DTOJahrgang jahrgang) {
		if (jahrgang == null) {
			dto.Jahrgang_ID = null;
			dto.ASDKlasse = JAHRGANGSTEIL_UEBERGREIFEND + getParallelitaetAusAsdKlasse(dto.ASDKlasse);
			return;
		}
		dto.Jahrgang_ID = jahrgang.ID;
		dto.ASDKlasse = toKlassenjahrgang(jahrgang.ASDJahrgang) + getParallelitaetAusAsdKlasse(dto.ASDKlasse);
	}

	/**
	 * Setzt die Parallelität der Klasse und baut damit das ASD-Kürzel neu auf. Bei null wird die Parallelität entfernt.
	 *
	 * @param dto            die Klasse
	 * @param parallelitaet  ein Großbuchstabe oder null
	 */
	default void applyParallelitaet(final DTOKlassen dto, final String parallelitaet) {
		dto.ASDKlasse = getJahrgangsteil(dto.ASDKlasse) + ((parallelitaet == null) ? "" : parallelitaet);
	}

	/**
	 * Setzt die allgemeinbildende Organisationsform der Klasse. Bei null wird die Organisationsform NICHT_ZUGEORDNET gesetzt.
	 *
	 * @param dto         die Klasse
	 * @param abschnitt   der Schuljahresabschnitt der Klasse
	 * @param schulform   die Schulform der Schule, die allgemeinbildend sein muss
	 * @param idOrgform   die ID der Organisationsform oder null
	 */
	default void applyAllgemeinbildendOrganisationsform(final DTOKlassen dto, final Schuljahresabschnitt abschnitt, final Schulform schulform,
			final Long idOrgform) {
		validateAllgemeinbildendeSchulform(schulform);
		dto.OrgFormKrz = (idOrgform == null)
				? getKuerzelNichtZugeordnet(abschnitt.schuljahr)
				: getKuerzelOrganisationsform(idOrgform, abschnitt.schuljahr, ART_ALLGEMEINBILDEND,
						id -> AllgemeinbildendOrganisationsformen.data().getWertByIDOrNull(id), AllgemeinbildendOrganisationsformen::daten);
	}

	/**
	 * Setzt die berufsbildende Organisationsform der Klasse. Bei null wird die Organisationsform NICHT_ZUGEORDNET gesetzt.
	 *
	 * @param dto         die Klasse
	 * @param abschnitt   der Schuljahresabschnitt der Klasse
	 * @param schulform   die Schulform der Schule, die berufsbildend sein muss
	 * @param idOrgform   die ID der Organisationsform oder null
	 */
	default void applyBerufsbildendOrganisationsform(final DTOKlassen dto, final Schuljahresabschnitt abschnitt, final Schulform schulform,
			final Long idOrgform) {
		validateBerufsbildendeSchulform(schulform);
		dto.OrgFormKrz = (idOrgform == null)
				? getKuerzelNichtZugeordnet(abschnitt.schuljahr)
				: getKuerzelOrganisationsform(idOrgform, abschnitt.schuljahr, ART_BERUFSBILDEND,
						id -> BerufskollegOrganisationsformen.data().getWertByIDOrNull(id), BerufskollegOrganisationsformen::daten);
	}

	/**
	 * Setzt die Organisationsform der Klasse an einem Weiterbildungskolleg. Bei null wird die Organisationsform NICHT_ZUGEORDNET gesetzt.
	 *
	 * @param dto         die Klasse
	 * @param abschnitt   der Schuljahresabschnitt der Klasse
	 * @param schulform   die Schulform der Schule, die eine Schulform der Weiterbildung sein muss
	 * @param idOrgform   die ID der Organisationsform oder null
	 */
	default void applyWeiterbildungOrganisationsform(final DTOKlassen dto, final Schuljahresabschnitt abschnitt, final Schulform schulform,
			final Long idOrgform) {
		validateWeiterbildungSchulform(schulform);
		dto.OrgFormKrz = (idOrgform == null)
				? getKuerzelNichtZugeordnet(abschnitt.schuljahr)
				: getKuerzelOrganisationsform(idOrgform, abschnitt.schuljahr, ART_WEITERBILDUNG,
						id -> WeiterbildungskollegOrganisationsformen.data().getWertByIDOrNull(id), WeiterbildungskollegOrganisationsformen::daten);
	}

	/**
	 * Setzt die Schulgliederung der Klasse. Bei null oder -1 wird die Standard-Schulgliederung der Schulform gesetzt, bzw. keine,
	 * wenn die Schulform keine Standard-Schulgliederung hat.
	 *
	 * @param dto                die Klasse
	 * @param abschnitt          der Schuljahresabschnitt der Klasse
	 * @param schulform          die Schulform der Schule
	 * @param idSchulgliederung  die ID der Schulgliederung, null oder -1
	 */
	default void applySchulgliederung(final DTOKlassen dto, final Schuljahresabschnitt abschnitt, final Schulform schulform, final Long idSchulgliederung) {
		final boolean istStandard = (idSchulgliederung == null) || (idSchulgliederung == KEINE_SCHULGLIEDERUNG);
		final Schulgliederung standard = Schulgliederung.getDefault(schulform);
		if (istStandard && (standard == null)) {
			dto.ASDSchulformNr = null;
			return;
		}
		final Schulgliederung schulgliederung = istStandard
				? standard
				: Schulgliederung.data().getWertByIDOrNull(idSchulgliederung);
		if (schulgliederung == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, SCHULGLIEDERUNG_UNBEKANNT.formatted(idSchulgliederung));
		}
		if (!schulgliederung.hatSchulform(abschnitt.schuljahr, schulform)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, SCHULGLIEDERUNG_NICHT_UNTERSTUETZT);
		}
		dto.ASDSchulformNr = schulgliederung.daten(abschnitt.schuljahr).kuerzel;
	}

	/**
	 * Setzt die Klassenart der Klasse. Die Klassenart muss im Schuljahr der Klasse gültig sein.
	 *
	 * @param dto            die Klasse
	 * @param abschnitt      der Schuljahresabschnitt der Klasse
	 * @param idKlassenart   die ID der Klassenart oder null
	 */
	default void applyKlassenart(final DTOKlassen dto, final Schuljahresabschnitt abschnitt, final Long idKlassenart) {
		if (idKlassenart == null) {
			dto.Klassenart = null;
			return;
		}
		final Klassenart klassenart = Klassenart.data().getWertByIDOrNull(idKlassenart);
		if (klassenart == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KLASSENART_UNBEKANNT.formatted(idKlassenart));
		}
		final var eintrag = klassenart.daten(abschnitt.schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KLASSENART_UNGUELTIG_IM_SCHULJAHR.formatted(idKlassenart, abschnitt.schuljahr));
		}
		dto.Klassenart = eintrag.kuerzel;
	}

	/**
	 * Setzt die Fachklasse der Klasse. Die Fachklasse muss im Fachklassen-Katalog existieren.
	 *
	 * @param dto            die Klasse
	 * @param idFachklasse   die ID der Fachklasse oder null
	 */
	default void applyFachklasse(final DTOKlassen dto, final Long idFachklasse) {
		if (idFachklasse == null) {
			dto.Fachklasse_ID = null;
			return;
		}
		final var fachklasse = JsonDaten.fachklassenManager.getDatenByID(idFachklasse);
		if (fachklasse == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, FACHKLASSE_UNBEKANNT.formatted(idFachklasse));
		}
		dto.Fachklasse_ID = fachklasse.id;
	}

	private static void validateAllgemeinbildendeSchulform(final Schulform schulform) {
		if (!schulform.istAllgemeinbildend()) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KEINE_ALLGEMEINBILDENDE_SCHULFORM);
		}
	}

	private static void validateBerufsbildendeSchulform(final Schulform schulform) {
		if (!schulform.istBerufsbildend()) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KEINE_BERUFSBILDENDE_SCHULFORM);
		}
	}

	private static void validateWeiterbildungSchulform(final Schulform schulform) {
		if (!schulform.istWeiterbildung()) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, KEINE_WEITERBILDUNG_SCHULFORM);
		}
	}

	private static String toKlassenjahrgang(final String asdJahrgang) {
		if (asdJahrgang == null) {
			return "";
		}
		return switch (asdJahrgang) {
			case "E1" -> JAHRGANGSTEIL_E1;
			case "E2" -> JAHRGANGSTEIL_E2;
			default -> asdJahrgang;
		};
	}

	private static String getJahrgangsteil(final String asdKlasse) {
		if (asdKlasse == null) {
			return "";
		}
		return (asdKlasse.length() > LAENGE_JAHRGANGSTEIL)
				? asdKlasse.substring(0, LAENGE_JAHRGANGSTEIL)
				: asdKlasse;
	}

	private static String getParallelitaetAusAsdKlasse(final String asdKlasse) {
		return ((asdKlasse == null) || (asdKlasse.length() <= LAENGE_JAHRGANGSTEIL))
				? ""
				: asdKlasse.substring(LAENGE_JAHRGANGSTEIL, LAENGE_JAHRGANGSTEIL + 1);
	}

	private static String getKuerzelNichtZugeordnet(final int schuljahr) {
		final var eintrag = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, NICHT_ZUGEORDNET_UNGUELTIG_IM_SCHULJAHR.formatted(schuljahr));
		}
		return eintrag.kuerzel;
	}

	private static <T> String getKuerzelOrganisationsform(final long idOrgform, final int schuljahr, final String art,
			final LongFunction<T> getWertByID, final BiFunction<T, Integer, OrganisationsformKatalogEintrag> getDaten) {
		final T orgform = getWertByID.apply(idOrgform);
		if (orgform == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ORGANISATIONSFORM_UNGUELTIG.formatted(idOrgform, art));
		}
		final OrganisationsformKatalogEintrag eintrag = getDaten.apply(orgform, schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, ORGANISATIONSFORM_UNGUELTIG_IM_SCHULJAHR.formatted(idOrgform, art, schuljahr));
		}
		return eintrag.kuerzel;
	}

	private static String getDefaultOrganisationsform(final int schuljahr, final Schulform schulform) {
		final OrganisationsformKatalogEintrag eintrag;
		if (schulform.istAllgemeinbildend()) {
			eintrag = AllgemeinbildendOrganisationsformen.NICHT_ZUGEORDNET.daten(schuljahr);
		} else if (schulform.istBerufsbildend()) {
			eintrag = BerufskollegOrganisationsformen.VOLLZEIT.daten(schuljahr);
		} else if (schulform.istWeiterbildung()) {
			eintrag = WeiterbildungskollegOrganisationsformen.VOLLZEIT.daten(schuljahr);
		} else {
			eintrag = null;
		}
		if (eintrag == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, DEFAULT_ORGANISATIONSFORM_FEHLER);
		}
		return eintrag.kuerzel;
	}

	private static String getDefaultSchulgliederung(final int schuljahr, final Schulform schulform) {
		final var standard = Schulgliederung.getDefault(schulform);
		final var schulgliederung = (standard != null)
				? standard
				: getErsteSchulgliederung(schuljahr, schulform);
		final var eintrag = (schulgliederung == null)
				? null
				: schulgliederung.daten(schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, DEFAULT_SCHULGLIEDERUNG_FEHLER);
		}
		return eintrag.kuerzel;
	}

	private static Schulgliederung getErsteSchulgliederung(final int schuljahr, final Schulform schulform) {
		return Schulgliederung.getBySchuljahrAndSchulform(schuljahr, schulform).stream()
				.findFirst()
				.orElse(null);
	}

	private static String getDefaultKlassenart(final int schuljahr, final Schulform schulform) {
		final Klassenart klassenart = Klassenart.getDefault(schulform);
		if (klassenart == null) {
			return null;
		}
		final var eintrag = klassenart.daten(schuljahr);
		if (eintrag == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, DEFAULT_KLASSENART_FEHLER);
		}
		return eintrag.kuerzel;
	}

	private static <T> void ifNotNull(final T wert, final Consumer<T> aktion) {
		if (wert != null) {
			aktion.accept(wert);
		}
	}

	private static <T> void ifPresent(final JsonNullable<T> wert, final Consumer<T> aktion) {
		if (wert.isPresent()) {
			aktion.accept(wert.get());
		}
	}

}
