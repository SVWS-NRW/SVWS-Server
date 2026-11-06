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

/**
 * MapStruct-Mapper für die Konvertierung eines {@link DTOKurs} in das Core-DTO {@link KursDaten} und eines
 * {@link DTOKursLehrer} in das Core-DTO {@link KursLehrer} (Lesen).
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
	Pattern PATTERN_ZAHL = Pattern.compile("^\\d+$");

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
	KursDaten toKursDaten(DTOKurs dto);

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
	KursLehrer toKursLehrer(DTOKursLehrer dto);

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
				.filter(eintrag -> PATTERN_ZAHL.matcher(eintrag).matches())
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
