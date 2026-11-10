package de.svws_nrw.mapper.kurse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.service.kurse.KursCreateRequest;
import de.svws_nrw.service.kurse.KursPatchRequest;

/**
 * MapStruct-Mapper für die Konvertierung eines {@link DTOKurs} in das Core-DTO {@link KursDaten} und eines
 * {@link DTOKursLehrer} in das Core-DTO {@link KursLehrer} (Lesen). Außerdem überträgt er die Daten eines
 * {@link KursCreateRequest} bzw. {@link KursPatchRequest} auf ein {@link DTOKurs}, ohne selbst auf die Datenbank zuzugreifen.
 * <p>
 * Die Schüler und die weiteren Lehrer eines Kurses werden nicht von diesem Mapper bestimmt. Der aufrufende
 * Service lädt diese Daten gebündelt und ergänzt sie im {@link KursDaten}.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KursMapper {

	/** Der Wert für die Sortierung, wenn beim Kurs keine Sortierung gesetzt ist. */
	int SORTIERUNG_DEFAULT = 32000;

	/** Der Wert für die Wochenstunden, wenn beim Kurs keine Wochenstunden gesetzt sind. */
	int WOCHENSTUNDEN_DEFAULT = 0;

	/** Das Muster, dem ein Eintrag in der kommagetrennten Jahrgangsliste entsprechen muss, um übernommen zu werden. */
	Pattern PATTERN_JAHRGANG_ZAHL = Pattern.compile("^\\d+$");

	/** Die Wochenstunden eines neu angelegten Kurses, wenn keine angegeben sind. */
	int WOCHENSTUNDEN_NEUER_KURS = 3;

	/** Die Wochenstunden des Lehrers, wenn sie im Patch auf null gesetzt werden. */
	double WOCHENSTUNDEN_LEHRER_DEFAULT = 0.0;

	/** Die Wochenstunden einer Zusatzkraft, wenn kein Anteil gesetzt ist. */
	double ANTEIL_DEFAULT = 0.0;

	/** Das Trennzeichen der kommagetrennten Listen für Jahrgänge und Schienen. */
	String TRENNZEICHEN = ",";

	/** Instanz des Mappers */
	KursMapper INSTANCE = Mappers.getMapper(KursMapper.class);

	/**
	 * Konvertiert ein {@link DTOKurs} in ein {@link KursDaten} ohne Schüler und ohne weitere Lehrer.
	 *
	 * @param dto   der Kurs, welcher gemappt werden soll
	 *
	 * @return die Kursdaten
	 */
	@Mapping(source = "ID", target = "id")
	@Mapping(source = "Schuljahresabschnitts_ID", target = "idSchuljahresabschnitt")
	@Mapping(source = "KurzBez", target = "kuerzel")
	@Mapping(source = "dto", target = "idJahrgaenge", qualifiedByName = "mapIdJahrgaenge")
	@Mapping(source = "Fach_ID", target = "idFach")
	@Mapping(source = "Lehrer_ID", target = "idLehrer")
	@Mapping(source = "KursartAllg", target = "kursartAllg", defaultValue = "")
	@Mapping(source = "Sortierung", target = "sortierung", qualifiedByName = "mapSortierung")
	@Mapping(source = "Sichtbar", target = "istSichtbar", qualifiedByName = "mapIstSichtbar")
	@Mapping(source = "Schienen", target = "schienen", qualifiedByName = "mapSchienen")
	@Mapping(source = "WochenStd", target = "wochenstunden", qualifiedByName = "mapWochenstunden")
	@Mapping(source = "dto", target = "wochenstundenLehrer", qualifiedByName = "mapWochenstundenLehrer")
	@Mapping(source = "Fortschreibungsart", target = "idKursFortschreibungsart", qualifiedByName = "mapIdKursFortschreibungsart")
	@Mapping(source = "SchulNr", target = "schulnummer")
	@Mapping(source = "EpochU", target = "istEpochalunterricht")
	@Mapping(source = "ZeugnisBez", target = "bezeichnungZeugnis", qualifiedByName = "mapBezeichnungZeugnis")
	@Mapping(target = "schueler", ignore = true)
	@Mapping(target = "weitereLehrer", ignore = true)
	KursDaten toApi(DTOKurs dto);

	/**
	 * Konvertiert ein {@link DTOKursLehrer} in ein {@link KursLehrer}. Ist kein Anteil gesetzt,
	 * so werden 0 Wochenstunden verwendet.
	 *
	 * @param dto   die Zuordnung der Zusatzkraft zum Kurs, welche gemappt werden soll
	 *
	 * @return die Zusatzkraft des Kurses
	 */
	@Mapping(source = "Kurs_ID", target = "idKurs")
	@Mapping(source = "Lehrer_ID", target = "idLehrer")
	@Mapping(source = "Anteil", target = "wochenstundenLehrer", qualifiedByName = "mapAnteil")
	KursLehrer toApi(DTOKursLehrer dto);

	/**
	 * Bestimmt die IDs der Jahrgänge eines Kurses aus der ID des Jahrgangs und der kommagetrennten Liste
	 * der Jahrgänge. Einträge der Liste, welche keine Zahlen sind, werden ignoriert.
	 *
	 * @param dto   der Kurs
	 *
	 * @return die Liste der Jahrgangs-IDs
	 */
	@Named("mapIdJahrgaenge")
	default List<Long> mapIdJahrgaenge(final DTOKurs dto) {
		final var idJahrgang = Stream.ofNullable(dto.Jahrgang_ID);
		final var idsJahrgaenge = splitListe(dto.Jahrgaenge)
				.filter(eintrag -> PATTERN_JAHRGANG_ZAHL.matcher(eintrag).matches())
				.map(Long::parseLong);
		return Stream.concat(idJahrgang, idsJahrgaenge).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Bestimmt die Nummern der Schienen aus einem kommagetrennten Text. Einträge, welche keine Zahlen sind,
	 * werden ignoriert.
	 *
	 * @param schienen   der kommagetrennte Text mit den Schienen, kann null sein
	 *
	 * @return die Liste der Schienennummern
	 */
	@Named("mapSchienen")
	default List<Integer> mapSchienen(final String schienen) {
		return splitListe(schienen)
				.map(String::trim)
				.flatMap(eintrag -> parseInteger(eintrag).stream())
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Erstellt aus den Daten zum Anlegen einen neuen Kurs. Nicht angegebene optionale Felder erhalten die
	 * Standardwerte. Die ID wird erst beim Speichern vergeben.
	 *
	 * @param request       die Daten des neuen Kurses
	 * @param jahrgaenge    die Jahrgänge des Kurses in der gewünschten Reihenfolge, leer wenn keine angegeben sind
	 *
	 * @return der neue Kurs
	 */
	default DTOKurs toDomain(final KursCreateRequest request, final List<DTOJahrgang> jahrgaenge) {
		final var kurs = new DTOKurs(0L, request.idSchuljahresabschnitt, request.kuerzel, request.idFach);
		kurs.KursartAllg = request.kursartAllg;
		kurs.Lehrer_ID = request.idLehrer;
		kurs.Sortierung = Optional.ofNullable(request.sortierung).orElse(SORTIERUNG_DEFAULT);
		kurs.Sichtbar = Optional.ofNullable(request.istSichtbar).orElse(true);
		kurs.WochenStd = Optional.ofNullable(request.wochenstunden).orElse(WOCHENSTUNDEN_NEUER_KURS);
		kurs.WochenstdKL = request.wochenstundenLehrer;
		kurs.Fortschreibungsart = Optional.ofNullable(request.idKursFortschreibungsart)
				.map(KursFortschreibungsart::fromID)
				.orElse(KursFortschreibungsart.KEINE);
		kurs.SchulNr = request.schulnummer;
		kurs.EpochU = Optional.ofNullable(request.istEpochalunterricht).orElse(false);
		kurs.ZeugnisBez = request.bezeichnungZeugnis;
		Optional.ofNullable(request.schienen).ifPresent(schienen -> applySchienen(kurs, schienen));
		applyJahrgaenge(kurs, jahrgaenge);
		return kurs;
	}

	/**
	 * Überträgt die im Patch gesetzten Attribute auf den Kurs. Jahrgänge und Schienen werden nur geändert,
	 * wenn sich die Liste unterscheidet. Ein null bei den Wochenstunden des Lehrers wird zu 0.0.
	 *
	 * @param request       die zu ändernden Attribute
	 * @param dto           der Kurs, der geändert wird
	 * @param jahrgaenge    die Jahrgänge zu den IDs im Patch in der gewünschten Reihenfolge, wird nur bei gesetzten IDs verwendet
	 */
	default void patch(final KursPatchRequest request, final DTOKurs dto, final List<DTOJahrgang> jahrgaenge) {
		request.kuerzel.ifPresent(kuerzel -> dto.KurzBez = kuerzel);
		request.kursartAllg.ifPresent(kursart -> dto.KursartAllg = kursart);
		request.idFach.ifPresent(idFach -> dto.Fach_ID = idFach);
		request.idLehrer.ifPresent(idLehrer -> dto.Lehrer_ID = idLehrer);
		request.sortierung.ifPresent(sortierung -> dto.Sortierung = sortierung);
		request.istSichtbar.ifPresent(sichtbar -> dto.Sichtbar = sichtbar);
		request.wochenstunden.ifPresent(wochenstunden -> dto.WochenStd = wochenstunden);
		request.wochenstundenLehrer.ifPresent(wochenstunden -> dto.WochenstdKL = Optional.ofNullable(wochenstunden).orElse(WOCHENSTUNDEN_LEHRER_DEFAULT));
		request.istEpochalunterricht.ifPresent(epochal -> dto.EpochU = epochal);
		request.bezeichnungZeugnis.ifPresent(bezeichnung -> dto.ZeugnisBez = bezeichnung);
		request.idKursFortschreibungsart.ifPresent(id -> dto.Fortschreibungsart = KursFortschreibungsart.fromID(id));
		request.schulnummer.ifPresent(schulnummer -> dto.SchulNr = schulnummer);
		request.schienen.ifPresent(schienen -> applySchienenIfChanged(dto, schienen));
		if (request.idJahrgaenge.isPresent()) {
			applyJahrgaengeIfChanged(dto, jahrgaenge);
		}
	}

	private void applySchienenIfChanged(final DTOKurs dto, final List<Integer> schienen) {
		if (!hasSameElements(mapSchienen(dto.Schienen), schienen)) {
			applySchienen(dto, schienen);
		}
	}

	private void applyJahrgaengeIfChanged(final DTOKurs dto, final List<DTOJahrgang> jahrgaenge) {
		final var idsNeu = jahrgaenge.stream()
				.map(j -> j.ID)
				.toList();
		if (!hasSameElements(mapIdJahrgaenge(dto), idsNeu)) {
			applyJahrgaenge(dto, jahrgaenge);
		}
	}

	private <T> boolean hasSameElements(final List<T> vorher, final List<T> neu) {
		return (vorher.size() == neu.size()) && vorher.containsAll(neu);
	}

	/**
	 * Überträgt die Jahrgänge auf den Kurs. Ein Jahrgang wird in Jahrgang_ID und ASDJahrgang gespeichert, mehrere
	 * Jahrgänge nur in der kommagetrennten Spalte Jahrgaenge. Bei einer leeren Liste werden alle drei Spalten geleert.
	 *
	 * @param dto           der Kurs, der geändert wird
	 * @param jahrgaenge    die Jahrgänge in der gewünschten Reihenfolge
	 */
	default void applyJahrgaenge(final DTOKurs dto, final List<DTOJahrgang> jahrgaenge) {
		final var einJahrgang = (jahrgaenge.size() == 1);
		dto.Jahrgang_ID = einJahrgang ? jahrgaenge.getFirst().ID : null;
		dto.ASDJahrgang = einJahrgang ? jahrgaenge.getFirst().ASDJahrgang : null;
		dto.Jahrgaenge = (jahrgaenge.size() > 1)
				? jahrgaenge.stream()
						.map(j -> String.valueOf(j.ID))
						.collect(Collectors.joining(TRENNZEICHEN))
				: null;
	}

	/**
	 * Überträgt die Schienen als kommagetrennten Text auf den Kurs.
	 *
	 * @param dto        der Kurs, der geändert wird
	 * @param schienen   die Nummern der Schienen
	 */
	default void applySchienen(final DTOKurs dto, final List<Integer> schienen) {
		dto.Schienen = schienen.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(TRENNZEICHEN));
	}

	/**
	 * Zerlegt einen kommagetrennten Text in seine nicht leeren Einträge.
	 *
	 * @param liste   der kommagetrennte Text, kann null sein
	 *
	 * @return der Stream mit den Einträgen
	 */
	private Stream<String> splitListe(final String liste) {
		if ((liste == null) || liste.isBlank()) {
			return Stream.empty();
		}
		return Arrays.stream(liste.split(TRENNZEICHEN))
				.filter(eintrag -> !eintrag.isBlank());
	}

	/**
	 * Wandelt einen Text in eine Zahl um.
	 *
	 * @param text   der Text
	 *
	 * @return die Zahl oder ein leeres Optional, wenn der Text keine Zahl ist
	 */
	private Optional<Integer> parseInteger(final String text) {
		try {
			return Optional.of(Integer.parseInt(text));
		} catch (final NumberFormatException e) {
			return Optional.empty();
		}
	}

	/**
	 * Bildet eine nicht gesetzte Sortierung auf den Default-Wert {@link #SORTIERUNG_DEFAULT} ab.
	 *
	 * @param sortierung   die Sortierung, kann null sein
	 *
	 * @return die Sortierung
	 */
	@Named("mapSortierung")
	default int mapSortierung(final Integer sortierung) {
		return (sortierung == null) ? SORTIERUNG_DEFAULT : sortierung;
	}

	/**
	 * Bildet eine nicht gesetzte Sichtbarkeit auf {@code true} ab.
	 *
	 * @param sichtbar   die Sichtbarkeit, kann null sein
	 *
	 * @return die Sichtbarkeit
	 */
	@Named("mapIstSichtbar")
	default boolean mapIstSichtbar(final Boolean sichtbar) {
		return (sichtbar == null) || sichtbar;
	}

	/**
	 * Bildet nicht gesetzte Wochenstunden auf den Default-Wert {@link #WOCHENSTUNDEN_DEFAULT} ab.
	 *
	 * @param wochenstunden   die Wochenstunden, können null sein
	 *
	 * @return die Wochenstunden
	 */
	@Named("mapWochenstunden")
	default int mapWochenstunden(final Integer wochenstunden) {
		return (wochenstunden == null) ? WOCHENSTUNDEN_DEFAULT : wochenstunden;
	}

	/**
	 * Bestimmt die Wochenstunden des Lehrers. Sind diese nicht gesetzt, so werden die Wochenstunden
	 * des Kurses verwendet.
	 *
	 * @param dto   der Kurs
	 *
	 * @return die Wochenstunden des Lehrers
	 */
	@Named("mapWochenstundenLehrer")
	default double mapWochenstundenLehrer(final DTOKurs dto) {
		return (dto.WochenstdKL == null) ? mapWochenstunden(dto.WochenStd) : dto.WochenstdKL;
	}

	/**
	 * Bestimmt die ID der Fortschreibungsart. Ist keine Fortschreibungsart gesetzt, so wird die ID von
	 * {@link KursFortschreibungsart#KEINE} verwendet.
	 *
	 * @param fortschreibungsart   die Fortschreibungsart, kann null sein
	 *
	 * @return die ID der Fortschreibungsart
	 */
	@Named("mapIdKursFortschreibungsart")
	default int mapIdKursFortschreibungsart(final KursFortschreibungsart fortschreibungsart) {
		return Objects.requireNonNullElse(fortschreibungsart, KursFortschreibungsart.KEINE).id;
	}

	/**
	 * Bildet eine leere oder nur aus Leerzeichen bestehende Zeugnisbezeichnung auf null ab.
	 *
	 * @param bezeichnung   die Zeugnisbezeichnung, kann null sein
	 *
	 * @return die Zeugnisbezeichnung oder null
	 */
	@Named("mapBezeichnungZeugnis")
	default String mapBezeichnungZeugnis(final String bezeichnung) {
		return ((bezeichnung == null) || bezeichnung.isBlank()) ? null : bezeichnung;
	}

	/**
	 * Bildet einen nicht gesetzten Anteil einer Zusatzkraft auf 0 Wochenstunden ab.
	 *
	 * @param anteil   der Anteil, kann null sein
	 *
	 * @return die Wochenstunden der Zusatzkraft
	 */
	@Named("mapAnteil")
	default double mapAnteil(final Double anteil) {
		return (anteil == null) ? ANTEIL_DEFAULT : anteil;
	}

}
