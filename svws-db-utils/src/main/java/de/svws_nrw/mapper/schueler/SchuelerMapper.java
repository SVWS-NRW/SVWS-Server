package de.svws_nrw.mapper.schueler;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für die Konvertierung eines {@link DTOSchueler} in das Core-DTO {@link Schueler}.
 * <p>
 * Ist {@link DTOSchueler#Geschlecht} oder {@link DTOSchueler#idStatus} nicht gesetzt, so ist der
 * Datensatz fehlerhaft und das Mapping wird mit einer {@link ApiOperationException} mit dem
 * Status {@link Status#INTERNAL_SERVER_ERROR} abgebrochen.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SchuelerMapper {

	/** Instanz des Mappers */
	SchuelerMapper INSTANCE = Mappers.getMapper(SchuelerMapper.class);

	/**
	 * Konvertiert einen {@link DTOSchueler} in ein {@link Schueler} ohne Abschlussjahrgang.
	 * Der Abschlussjahrgang wird dabei auf -1 gesetzt.
	 *
	 * @param dto   das Schüler-DTO mit den zu mappenden Daten
	 *
	 * @return das Core-DTO mit den gemappten Daten
	 */
	default Schueler toSchueler(final DTOSchueler dto) {
		return toSchueler(dto, null);
	}

	/**
	 * Konvertiert einen {@link DTOSchueler} in ein {@link Schueler}. Ein Vorname {@code null} wird zu einem
	 * leeren String, ein Abschlussjahrgang {@code null} wird zu -1.
	 *
	 * @param dto                 das Schüler-DTO mit den zu mappenden Daten
	 * @param abschlussjahrgang   der Abschlussjahrgang des Schülers oder null, falls dieser nicht gesetzt werden soll
	 *
	 * @return das Core-DTO mit den gemappten Daten
	 */
	@Mapping(target = "id", source = "dto.ID")
	@Mapping(target = "nachname", source = "dto.Nachname")
	@Mapping(target = "vorname", source = "dto.Vorname", qualifiedByName = "mapVorname")
	@Mapping(target = "geschlecht", source = "dto.Geschlecht", qualifiedByName = "mapGeschlecht")
	@Mapping(target = "abschlussjahrgang", source = "abschlussjahrgang", qualifiedByName = "mapAbschlussjahrgang")
	@Mapping(target = "status", source = "dto.idStatus")
	@Mapping(target = "externeSchulNr", source = "dto.ExterneSchulNr")
	Schueler toSchueler(DTOSchueler dto, Integer abschlussjahrgang);

	/**
	 * Prüft vor dem Mapping, ob die Pflichtfelder Geschlecht und Status des Schüler-DTOs gesetzt sind.
	 *
	 * @param dto   das Schüler-DTO, welches gemappt werden soll
	 *
	 * @throws ApiOperationException   mit dem Status INTERNAL_SERVER_ERROR, falls Geschlecht oder Status nicht gesetzt sind
	 */
	@BeforeMapping
	default void pruefePflichtfelder(final DTOSchueler dto) {
		if (dto.Geschlecht == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Schüler mit der ID %d hat kein gültiges Geschlecht.".formatted(dto.ID));
		}
		if (dto.idStatus == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Schüler mit der ID %d hat keinen Status.".formatted(dto.ID));
		}
	}

	/**
	 * Löst ein {@link Geschlecht} auf dessen numerische ID auf.
	 *
	 * @param geschlecht   das Geschlecht, welches nicht null sein darf
	 *
	 * @return die ID des Geschlechts
	 */
	@Named("mapGeschlecht")
	default int mapGeschlecht(final Geschlecht geschlecht) {
		return geschlecht.id;
	}

	/**
	 * Bildet einen Vornamen {@code null} auf einen leeren String ab.
	 *
	 * @param vorname   der Vorname oder null
	 *
	 * @return der Vorname oder ein leerer String, falls dieser null ist
	 */
	@Named("mapVorname")
	default String mapVorname(final String vorname) {
		return (vorname == null) ? "" : vorname;
	}

	/**
	 * Bildet einen Abschlussjahrgang {@code null} auf -1 ab.
	 *
	 * @param abschlussjahrgang   der Abschlussjahrgang oder null
	 *
	 * @return der Abschlussjahrgang oder -1, falls dieser null ist
	 */
	@Named("mapAbschlussjahrgang")
	default int mapAbschlussjahrgang(final Integer abschlussjahrgang) {
		return (abschlussjahrgang == null) ? -1 : abschlussjahrgang;
	}

}
