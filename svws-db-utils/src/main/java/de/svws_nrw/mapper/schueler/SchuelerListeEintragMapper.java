package de.svws_nrw.mapper.schueler;

import java.util.Map;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für die Erstellung eines {@link SchuelerListeEintrag} aus den Datenbank-DTOs {@link DTOSchueler},
 * {@link DTOSchuelerLernabschnittsdaten} und {@link DTOJahrgang}.
 * <p>
 * Die Standardfelder des Schülers werden über MapStruct gemappt. Die Felder, welche vom Lernabschnitt,
 * dem Jahrgang und der Schulgliederung abhängen, werden in
 * {@link #toSchuelerListeEintrag(DTOSchueler, int, DTOSchuelerLernabschnittsdaten, Map, Schulform)}
 * von Hand gesetzt, da hier Fachlogik zur Ermittlung der Schulgliederung nötig ist.
 * <p>
 * Ist {@link DTOSchueler#idStatus} oder {@link DTOSchueler#Duplikat} nicht gesetzt, so ist der Datensatz
 * fehlerhaft und das Mapping wird mit einer {@link ApiOperationException} mit dem
 * Status {@link Status#INTERNAL_SERVER_ERROR} abgebrochen. Ein fehlendes Geschlecht wird als leerer String abgebildet.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SchuelerListeEintragMapper {

	/** Instanz des Mappers */
	SchuelerListeEintragMapper INSTANCE = Mappers.getMapper(SchuelerListeEintragMapper.class);

	/**
	 * Erstellt einen {@link SchuelerListeEintrag} anhand der Schülerinformation aus den Datenbank-DTOs
	 * {@link DTOSchueler} und {@link DTOSchuelerLernabschnittsdaten}.
	 * <p>
	 * Ohne Lernabschnitt ({@code aktAbschnitt == null}) werden Klasse, Jahrgang und Schulgliederung auf -1,
	 * der Jahrgang auf einen leeren String und die EP-Jahre auf null gesetzt. Als Schuljahresabschnitt wird dann
	 * der Abschnitt des Schülers verwendet. In diesem Fall werden {@code mapJahrgaenge} und {@code schulform} nicht benötigt.
	 * <p>
	 * Mit Lernabschnitt müssen die Jahrgangsdefinitionen übergeben werden. Ist der Jahrgang nicht enthalten, so werden
	 * Jahrgang-ID und Jahrgang wie ohne Lernabschnitt gesetzt. Ist keine Schulgliederung im Lernabschnitt gesetzt, so wird
	 * die Standard-Schulgliederung der Schulform verwendet. Kann keine Schulgliederung ermittelt werden, so wird die ID -1 gesetzt.
	 *
	 * @param schueler       die DB-Informationen zum Schüler
	 * @param schuljahr      das Schuljahr
	 * @param aktAbschnitt   die DB-Informationen zu dem aktuellen Schülerlernabschnitt oder null
	 * @param mapJahrgaenge  die Jahrgangsdefinitionen, welche ihrer ID zugeordnet sind, wird bei einem Lernabschnitt benötigt
	 * @param schulform      die Schulform der Schule
	 *
	 * @return der Schülerlisteneintrag
	 *
	 * @throws ApiOperationException   mit dem Status INTERNAL_SERVER_ERROR, falls der Status oder die Duplikat-Angabe des Schülers nicht gesetzt sind
	 */
	default SchuelerListeEintrag toSchuelerListeEintrag(
			final DTOSchueler schueler,
			final int schuljahr,
			final DTOSchuelerLernabschnittsdaten aktAbschnitt,
			final Map<Long, DTOJahrgang> mapJahrgaenge,
			final Schulform schulform
	) {
		final var eintrag = toSchuelerListeEintrag(schueler);
		if (aktAbschnitt == null) {
			setzeAbschnittsfelderOhneLernabschnitt(eintrag, schueler);
		} else {
			setzeAbschnittsfelderMitLernabschnitt(eintrag, aktAbschnitt, mapJahrgaenge, schuljahr, schulform);
		}
		return eintrag;
	}

	/**
	 * Bildet die Standardfelder eines {@link DTOSchueler} auf einen {@link SchuelerListeEintrag} ab.
	 * Die Felder, welche vom Lernabschnitt abhängen, werden dabei nicht gesetzt.
	 *
	 * @param schueler   die DB-Informationen zum Schüler
	 *
	 * @return der Schülerlisteneintrag mit den Standardfeldern
	 */
	@Mapping(target = "id", source = "ID")
	@Mapping(target = "nachname", source = "Nachname", qualifiedByName = "mapNullToEmpty")
	@Mapping(target = "vorname", source = "Vorname", qualifiedByName = "mapNullToEmpty")
	@Mapping(target = "geschlecht", source = "Geschlecht", qualifiedByName = "mapGeschlechtKuerzel")
	@Mapping(target = "geburtsdatum", source = "Geburtsdatum")
	@Mapping(target = "idSchuljahresabschnittSchueler", source = "Schuljahresabschnitts_ID")
	@Mapping(target = "status", source = "idStatus")
	@Mapping(target = "istDuplikat", source = "Duplikat")
	@Mapping(target = "externeSchulNr", source = "ExterneSchulNr")
	SchuelerListeEintrag toSchuelerListeEintrag(DTOSchueler schueler);

	/**
	 * Prüft vor dem Mapping, ob die Pflichtfelder Status und Duplikat des Schüler-DTOs gesetzt sind.
	 *
	 * @param schueler   das Schüler-DTO, welches gemappt werden soll
	 *
	 * @throws ApiOperationException   mit dem Status INTERNAL_SERVER_ERROR, falls Status oder Duplikat nicht gesetzt sind
	 */
	@BeforeMapping
	default void pruefePflichtfelder(final DTOSchueler schueler) {
		if (schueler.idStatus == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Schüler mit der ID %d hat keinen Status.".formatted(schueler.ID));
		}
		if (schueler.Duplikat == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Der Schüler mit der ID %d hat keine Angabe, ob es sich um ein Duplikat handelt.".formatted(schueler.ID));
		}
	}

	/**
	 * Bildet einen String {@code null} auf einen leeren String ab.
	 *
	 * @param wert   der String oder null
	 *
	 * @return der String oder ein leerer String, falls dieser null ist
	 */
	@Named("mapNullToEmpty")
	default String mapNullToEmpty(final String wert) {
		return (wert == null) ? "" : wert;
	}

	/**
	 * Löst ein {@link Geschlecht} auf dessen Kürzel auf.
	 *
	 * @param geschlecht   das Geschlecht oder null
	 *
	 * @return das Kürzel des Geschlechts oder ein leerer String, falls das Geschlecht null ist
	 */
	@Named("mapGeschlechtKuerzel")
	default String mapGeschlechtKuerzel(final Geschlecht geschlecht) {
		return (geschlecht == null) ? "" : geschlecht.kuerzel;
	}

	/**
	 * Setzt die vom Lernabschnitt abhängigen Felder auf ihre Vorgabewerte, wenn kein Lernabschnitt vorliegt.
	 * Als Schuljahresabschnitt wird der Abschnitt des Schülers verwendet.
	 *
	 * @param eintrag    der Schülerlisteneintrag, dessen Felder gesetzt werden
	 * @param schueler   die DB-Informationen zum Schüler
	 */
	private void setzeAbschnittsfelderOhneLernabschnitt(final SchuelerListeEintrag eintrag, final DTOSchueler schueler) {
		eintrag.idSchuljahresabschnitt = schueler.Schuljahresabschnitts_ID;
		eintrag.idKlasse = -1L;
		eintrag.idJahrgang = -1L;
		eintrag.epJahre = null;
		eintrag.jahrgang = "";
		eintrag.idSchulgliederung = -1L;
	}

	/**
	 * Setzt die vom Lernabschnitt abhängigen Felder anhand des Lernabschnitts, der Jahrgangsdefinitionen
	 * und der Schulgliederung.
	 *
	 * @param eintrag         der Schülerlisteneintrag, dessen Felder gesetzt werden
	 * @param aktAbschnitt    die DB-Informationen zu dem aktuellen Schülerlernabschnitt
	 * @param mapJahrgaenge   die Jahrgangsdefinitionen, welche ihrer ID zugeordnet sind
	 * @param schuljahr       das Schuljahr, für welches die Schulgliederung bestimmt wird
	 * @param schulform       die Schulform der Schule
	 */
	private void setzeAbschnittsfelderMitLernabschnitt(
			final SchuelerListeEintrag eintrag,
			final DTOSchuelerLernabschnittsdaten aktAbschnitt,
			final Map<Long, DTOJahrgang> mapJahrgaenge,
			final int schuljahr,
			final Schulform schulform
	) {
		eintrag.idSchuljahresabschnitt = aktAbschnitt.Schuljahresabschnitts_ID;
		eintrag.idKlasse = (aktAbschnitt.Klassen_ID == null) ? -1L : aktAbschnitt.Klassen_ID;
		final var jahrgang = mapJahrgaenge.get(aktAbschnitt.Jahrgang_ID);
		eintrag.idJahrgang = (jahrgang == null) ? -1L : jahrgang.ID;
		eintrag.jahrgang = (jahrgang == null) ? "" : mapNullToEmpty(jahrgang.ASDJahrgang);
		eintrag.epJahre = aktAbschnitt.EPJahre;
		eintrag.idSchulgliederung = ermittleIdSchulgliederung(aktAbschnitt.Schulgliederung, schuljahr, schulform);
	}

	/**
	 * Ermittelt die ID der Schulgliederung. Ist kein Schlüssel gesetzt, so wird die Standard-Schulgliederung der
	 * Schulform verwendet.
	 *
	 * @param schluesselSchulgliederung   der Schlüssel der Schulgliederung aus dem Lernabschnitt oder null
	 * @param schuljahr                   das Schuljahr, für welches der Katalogeintrag bestimmt wird
	 * @param schulform                   die Schulform der Schule
	 *
	 * @return die ID der Schulgliederung oder -1, falls diese nicht ermittelt werden kann
	 */
	private Long ermittleIdSchulgliederung(final String schluesselSchulgliederung, final int schuljahr, final Schulform schulform) {
		final var schulgliederung = ermittleSchulgliederung(schluesselSchulgliederung, schulform);
		if (schulgliederung == null) {
			return -1L;
		}
		final var katalogEintrag = schulgliederung.daten(schuljahr);
		if (katalogEintrag == null) {
			return -1L;
		}
		return katalogEintrag.id;
	}

	/**
	 * Ermittelt die Schulgliederung anhand des Schlüssels. Ist kein Schlüssel gesetzt, so wird die
	 * Standard-Schulgliederung der Schulform zurückgegeben.
	 *
	 * @param schluesselSchulgliederung   der Schlüssel der Schulgliederung aus dem Lernabschnitt oder null
	 * @param schulform                   die Schulform der Schule
	 *
	 * @return die Schulgliederung oder null, falls diese nicht ermittelt werden kann
	 */
	private Schulgliederung ermittleSchulgliederung(final String schluesselSchulgliederung, final Schulform schulform) {
		if (schluesselSchulgliederung == null) {
			return Schulgliederung.getDefault(schulform);
		}
		return Schulgliederung.data().getWertBySchluessel(schluesselSchulgliederung);
	}

}
