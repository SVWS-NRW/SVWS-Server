package de.svws_nrw.mapper.klassen;

import java.util.Map;
import java.util.Optional;

import de.svws_nrw.asd.data.klassen.KlassenDaten;
import de.svws_nrw.asd.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für die Konvertierung einer {@link DTOKlassen} in das Core-DTO {@link KlassenDaten} (Lesen).
 * <p>
 * Entscheidung: Die Fachlogik des Mappings (Fallbacks bei Klassenart und Schulgliederung, Halbjahresbetrieb bei
 * Vorgänger- und Folgeklasse, Organisationsformen, Parallelität) liegt als benannte, einzeln testbare Methoden in
 * diesem Mapper. Der Mapper greift nicht auf Repositories, Services oder die Datenbank zu. Alles, was er benötigt
 * (Schulform, Schuljahresabschnitt, die Klassen der Nachbarabschnitte, Klassenleitungen und Schüler), bekommt er
 * über den {@link KlassenMappingContext}. Das Laden der Daten übernimmt der aufrufende Service.
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
	KlassenDaten toKlassenDaten(DTOKlassen dto, @Context KlassenMappingContext ctx);

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
		return ((asdKlasse == null) || (asdKlasse.length() < 3)) ? null : asdKlasse.substring(2);
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

}
