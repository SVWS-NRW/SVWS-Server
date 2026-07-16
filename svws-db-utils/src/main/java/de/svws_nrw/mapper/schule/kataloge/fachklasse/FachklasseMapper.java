package de.svws_nrw.mapper.schule.kataloge.fachklasse;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.svws_nrw.asd.data.schule.FachklasseKatalogEintrag;
import de.svws_nrw.asd.types.schule.DQRNiveau;
import de.svws_nrw.asd.types.schule.Fachklasse;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.schule.FachklasseEintrag;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOFachklassen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schule.kataloge.fachklasse.FachklasseEintragCreateRequest;
import de.svws_nrw.service.schule.kataloge.fachklasse.FachklasseEintragPatchRequest;
import jakarta.ws.rs.core.Response;
import org.apache.poi.util.StringUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class)
public interface FachklasseMapper {

	/** Instanz des Mappers */
	FachklasseMapper INSTANCE = Mappers.getMapper(FachklasseMapper.class);

	/**
	 * Mappt eine {@link DTOFachklassen}-Entity auf das API-Modell {@link FachklasseEintrag}.
	 *
	 * @param entity die Quell-Entity
	 * @param schuljahr das aktuelle Schuljahr für CoreType-Lookups
	 * @return das befüllte DTO
	 */
	@Mapping(target = "idFachklasse", ignore = true)
	@Mapping(target = "schluesselSchulgliederung", ignore = true)
	@Mapping(target = "referenziertInAnderenTabellen", ignore = true)
	FachklasseEintrag toApi(DTOFachklassen entity, @Context int schuljahr);

	/**
	 * Rekonstruiert nach dem Basis-Mapping den Fachklassen-Schlüssel aus dem Feld
	 * {@link DTOFachklassen#Kennung} (z.B. {@code "10-179-02"}) durch Entfernen des letzten
	 * Bindestrichs (z.B. {@code "10-17902"}) und löst damit den zum Schuljahr passenden
	 * {@link Fachklasse}-Katalogeintrag auf.
	 * <p>
	 * Bei Erfolg werden folgende Felder am {@link FachklasseEintrag} gesetzt:
	 * <ul>
	 *   <li>{@link FachklasseEintrag#idFachklasse} – die ID des zum Schuljahr gültigen Katalogeintrags</li>
	 *   <li>{@link FachklasseEintrag#schluesselSchulgliederung} – der Schlüssel der letzten
	 *       {@link Schulgliederung}, die zum {@code bkIndex} der Fachklasse im angegebenen
	 *       Schuljahr gefunden wird; {@code null} wenn keine gefunden wird</li>
	 * </ul>
	 * Ist {@link DTOFachklassen#Kennung} {@code null}, leer oder der rekonstruierte Schlüssel
	 * im angegebenen Schuljahr unbekannt, werden die Felder nicht gesetzt.
	 *
	 * @param entity    die Quell-Entity mit dem zu rekonstruierenden {@link DTOFachklassen#Kennung}-Feld
	 * @param schuljahr das aktuelle Schuljahr für den {@link Fachklasse}- und {@link Schulgliederung}-Lookup
	 * @param dto       das Ziel-DTO, in das {@code idFachklasse} und {@code schluesselSchulgliederung} geschrieben werden
	 */
	@AfterMapping
	default void mapKennungToApi(
			final DTOFachklassen entity,
			@Context final int schuljahr,
			@MappingTarget final FachklasseEintrag dto) {
		if (StringUtil.isBlank(entity.Kennung)) {
			return;
		}
		// "10-179-02" -> letzten Bindestrich entfernen -> "10-17902"
		final int lastDash = entity.Kennung.lastIndexOf("-");
		final var schluesselFachklasse = entity.Kennung.substring(0, lastDash) + entity.Kennung.substring(lastDash + 1);

		final var fachklasse = Fachklasse.data().getEintragBySchuljahrUndSchluessel(schuljahr, schluesselFachklasse);
		if (fachklasse == null) {
			return;
		}
		dto.idFachklasse = fachklasse.id;
		if (fachklasse.bkIndex == null) {
			return;
		}
		final var schulgliederungen = Schulgliederung.getBySchuljahrAndBKIndex(schuljahr, fachklasse.bkIndex);
		if (schulgliederungen.isEmpty()) {
			return;
		}
		dto.schluesselSchulgliederung = schulgliederungen.getFirst().schluessel;
	}

	/**
	 * Mappt einen {@link FachklasseEintragCreateRequest} auf eine neue {@link DTOFachklassen}-Entity.
	 * Einfache Felder werden direkt übertragen. Die CoreType-abhängigen Felder
	 * ({@link DTOFachklassen#BKIndex}, {@link DTOFachklassen#FKS}, {@link DTOFachklassen#AP},
	 * {@link DTOFachklassen#Kennung}, {@link DTOFachklassen#FKS_AP_SIM},
	 * {@link DTOFachklassen#BKIndexTyp}, {@link DTOFachklassen#DQR_Niveau})
	 * werden via {@link #mapIdFachklasseFromCreate} nach dem Mapping gesetzt.
	 *
	 * @param dto       der Create-Request mit den zu übernehmenden Feldern
	 * @param schuljahr das aktuelle Schuljahr für CoreType-Lookups
	 * @return die befüllte {@link DTOFachklassen}-Entity
	 */
	@BeanMapping(ignoreByDefault = true)
	@Mapping(source = "bezeichnung", target = "bezeichnung")
	@Mapping(source = "kuerzel",     target = "kuerzel")
	@Mapping(source = "istSichtbar", target = "istSichtbar")
	@Mapping(source = "sortierung",  target = "sortierung")
	DTOFachklassen toDomain(FachklasseEintragCreateRequest dto, @Context Integer schuljahr);

	/**
	 * Wendet die Änderungen eines {@link FachklasseEintragPatchRequest} auf eine bestehende
	 * {@link DTOFachklassen}-Entity an. Felder mit {@code undefined}-Wert werden nicht überschrieben.
	 * Ist {@code idFachklasse} gesetzt, werden die CoreType-abhängigen Felder
	 * via {@link #mapIdFachklasseFromPatch} neu aufgelöst.
	 *
	 * @param dto       der Patch-Request mit den zu ändernden Feldern
	 * @param schuljahr das aktuelle Schuljahr für CoreType-Lookups
	 * @param entity    die zu aktualisierende {@link DTOFachklassen}-Entity
	 */
	@BeanMapping(ignoreByDefault = true,
			nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(source = "bezeichnung", target = "bezeichnung")
	@Mapping(source = "kuerzel",     target = "kuerzel")
	@Mapping(source = "istSichtbar", target = "istSichtbar")
	@Mapping(source = "sortierung",  target = "sortierung")
	void patch(FachklasseEintragPatchRequest dto, @Context Integer schuljahr, @MappingTarget DTOFachklassen entity);

	/**
	 * Löst nach dem Mapping eines {@link FachklasseEintragCreateRequest} die {@code idFachklasse}
	 * auf den zugehörigen {@link de.svws_nrw.asd.types.schule.Fachklasse}-CoreType auf
	 * und befüllt die abhängigen Felder der {@link DTOFachklassen}-Entity.
	 *
	 * @param dto       der Create-Request mit der aufzulösenden {@code idFachklasse}
	 * @param schuljahr das aktuelle Schuljahr für CoreType-Lookups
	 * @param entity    die zu befüllende {@link DTOFachklassen}-Entity
	 * @throws ApiOperationException wenn {@code idFachklasse} null oder unbekannt ist,
	 *                               kein DQR-Niveau oder keine Schulgliederung gefunden wird
	 */
	@AfterMapping
	default void mapIdFachklasseFromCreate(
			final FachklasseEintragCreateRequest dto,
			@Context final Integer schuljahr,
			@MappingTarget final DTOFachklassen entity) {
		resolveAndMapFachklasse(dto.idFachklasse, schuljahr, entity);
	}

	/**
	 * Löst nach dem Mapping eines {@link FachklasseEintragPatchRequest} die {@code idFachklasse}
	 * auf den zugehörigen {@link de.svws_nrw.asd.types.schule.Fachklasse}-CoreType auf
	 * und befüllt die abhängigen Felder der {@link DTOFachklassen}-Entity,
	 * sofern {@code idFachklasse} im Request explizit gesetzt wurde.
	 * Bei {@code undefined} wird kein Lookup durchgeführt.
	 *
	 * @param dto       der Patch-Request mit der aufzulösenden {@code idFachklasse}
	 * @param schuljahr das aktuelle Schuljahr für CoreType-Lookups
	 * @param entity    die zu aktualisierende {@link DTOFachklassen}-Entity
	 * @throws ApiOperationException wenn {@code idFachklasse} gesetzt, aber unbekannt ist,
	 *                               kein DQR-Niveau oder keine Schulgliederung gefunden wird
	 */
	@AfterMapping
	default void mapIdFachklasseFromPatch(
			final FachklasseEintragPatchRequest dto,
			@Context final Integer schuljahr,
			@MappingTarget final DTOFachklassen entity) {
		dto.idFachklasse.ifPresent(kuerzel -> resolveAndMapFachklasse(kuerzel, schuljahr, entity));
	}

	private static void resolveAndMapFachklasse(final Long idFachklasse, final Integer schuljahr, final DTOFachklassen entity) {
		final var fachklasse = Fachklasse.data().getEintragByID(idFachklasse);
		if (fachklasse == null) {
			return;
		}
		mapFachklasse(fachklasse, schuljahr, entity);
	}

	private static void mapFachklasse(final FachklasseKatalogEintrag fachklasse, final Integer schuljahr, final DTOFachklassen entity) {
		entity.BKIndex = fachklasse.bkIndex;
		entity.FKS = fachklasse.fkSchluessel;
		entity.AP = fachklasse.fkSchluessel2;
		entity.Kennung = getKennung(fachklasse);
		entity.FKS_AP_SIM = getFksApSim(fachklasse);
		entity.BKIndexTyp = getSchluesselSchulgliederung(fachklasse, schuljahr);
		entity.DQR_Niveau = getIdDQRNiveau(fachklasse, schuljahr);
	}

	private static String getFksApSim(final FachklasseKatalogEintrag fachklasse) {
		return Objects.toString(fachklasse.fkSchluessel, "") + Objects.toString(fachklasse.fkSchluessel2, "");
	}

	private static String getKennung(final FachklasseKatalogEintrag fachklasse) {
		return String.join("-",
				Objects.toString(fachklasse.bkIndex, ""),
				Objects.toString(fachklasse.fkSchluessel, ""),
				Objects.toString(fachklasse.fkSchluessel2, ""));
	}

	private static Integer getIdDQRNiveau(final FachklasseKatalogEintrag fachklasse, final int schuljahr) {
		if (fachklasse.dqrNiveau == null) {
			return null;
		}
		return Optional.ofNullable(DQRNiveau.data().getEintragBySchuljahrUndSchluessel(schuljahr, fachklasse.dqrNiveau))
				.map(d -> d.id)
				.map(Long::intValue)
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST,
						"Kein DQRNiveau zum Schlüssel %s zur Fachklasse mit der id %d gefunden".formatted(fachklasse.dqrNiveau, fachklasse.id)));
	}

	private static String getSchluesselSchulgliederung(final FachklasseKatalogEintrag fachklasse, final int schuljahr) {
		return Optional.of(Schulgliederung.getBySchuljahrAndBKIndex(schuljahr, fachklasse.bkIndex))
				.filter(list -> !list.isEmpty())
				.map(List::getFirst)
				.map(s -> s.schluessel)
				.orElseThrow(() -> new ApiOperationException(Response.Status.BAD_REQUEST,
						"keine Schulgliederung zum BKIndex %s im Schuljahr %d zur Fachklasse mit der id %d gefunden".formatted(fachklasse.bkIndex, schuljahr,
								fachklasse.id)));
	}

}
