package de.svws_nrw.mapper.lehrer.personalabschnittsdaten;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.lehrer.LehrerRechtsverhaeltnis;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.mapper.JsonNullableMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LehrerPersonalabschnittsdatenMapper {

	/** Instanz */
	LehrerPersonalabschnittsdatenMapper INSTANCE = Mappers.getMapper(LehrerPersonalabschnittsdatenMapper.class);

	/**
	 * Konvertiert einen {@link DTOLehrerAbschnittsdaten} in {@link LehrerPersonalabschnittsdaten}.
	 * Komplexe Felder (ID-Lookups, Katalogauflösungen) werden über den {@link LehrerPersonalabschnittsdatenMappingContext} aufgelöst.
	 * Anrechnungen, Mehrleistungen, Minderleistungen und Funktionen werden via {@link #mapListen} nach dem Mapping gesetzt.
	 *
	 * @param entity die LehrerAbschnittsdaten-Entity
	 * @param schuljahr das Schuljahr
	 * @param ctx    der Mapping-Kontext
	 * @return die befüllten {@link LehrerPersonalabschnittsdaten}
	 */
	@Mapping(source = "entity.ID", 							target = "id")
	@Mapping(source = "entity.Lehrer_ID", 					target = "idLehrer")
	@Mapping(source = "entity.Schuljahresabschnitts_ID", 	target = "idSchuljahresabschnitt")
	@Mapping(source = "entity.PflichtstdSoll", 				target = "pflichtstundensoll")
	@Mapping(source = "entity.StammschulNr", 				target = "stammschulnummer")
	@Mapping(source = "entity.Rechtsverhaeltnis",      		target = "idRechtsverhaeltnis", 	qualifiedByName = "mapIdRechtsverhaeltnis")
	@Mapping(source = "entity.Beschaeftigungsart",      	target = "idBeschaeftigungsart", 	qualifiedByName = "mapIdBeschaeftigungsart")
	@Mapping(source = "entity.Einsatzstatus",      			target = "idEinsatzstatus", 		qualifiedByName = "mapIdEinsatzstatus")
	LehrerPersonalabschnittsdaten toApi(
			DTOLehrerAbschnittsdaten entity,
			@Context Integer schuljahr,
			@Context LehrerPersonalabschnittsdatenMappingContext ctx
	);

	/**
	 * Löst einen Rechtsverhältnis-Schlüssel auf die interne ID auf.
	 *
	 * @param schluessel der ASD-Schlüssel des Rechtverhältnisses
	 * @param schuljahr das Schuljahr
	 * @return die interne ID des Rechtsverhältnisses oder {@code null}
	 */
	@Named("mapIdRechtsverhaeltnis")
	default Long mapIdRechtsverhaeltnis(final String schluessel, @Context final Integer schuljahr) {
		if (schluessel == null) {
			return null;
		}

		final var schulform = (schuljahr != null)
				? LehrerRechtsverhaeltnis.data().getEintragBySchuljahrUndSchluessel(schuljahr, schluessel)
				: LehrerRechtsverhaeltnis.data().getLastEintragBySchluesselOrNull(schluessel);
		if (schulform == null) {
			return null;
		}

		return schulform.id;
	}

	/**
	 * Löst einen Beschäftigungsart-Schlüssel auf die interne ID auf.
	 *
	 * @param schluessel der ASD-Schlüssel der Beschäftigungsart
	 * @param schuljahr das Schuljahr
	 * @return die interne ID der Beschäftigungsart oder {@code null}
	 */
	@Named("mapIdBeschaeftigungsart")
	default Long mapIdBeschaeftigungsart(final String schluessel, @Context final Integer schuljahr) {
		if (schluessel == null) {
			return null;
		}

		final var schulform = (schuljahr != null)
				? LehrerBeschaeftigungsart.data().getEintragBySchuljahrUndSchluessel(schuljahr, schluessel)
				: LehrerBeschaeftigungsart.data().getLastEintragBySchluesselOrNull(schluessel);
		if (schulform == null) {
			return null;
		}

		return schulform.id;
	}

	/**
	 * Löst einen Einsatzstatus-Schlüssel auf die interne ID auf.
	 *
	 * @param schluessel der ASD-Schlüssel des Einsatzstatus
	 * @param schuljahr das Schuljahr
	 * @return die interne ID des Einsatzstatus oder {@code null}
	 */
	@Named("mapIdEinsatzstatus")
	default Long mapIdEinsatzstatus(final String schluessel, @Context final Integer schuljahr) {
		if (schluessel == null) {
			return null;
		}

		final var schulform = (schuljahr != null)
				? LehrerEinsatzstatus.data().getEintragBySchuljahrUndSchluessel(schuljahr, schluessel)
				: LehrerEinsatzstatus.data().getLastEintragBySchluesselOrNull(schluessel);
		if (schulform == null) {
			return null;
		}

		return schulform.id;
	}

	/**
	 * Setzt nach dem Mapping die Anrechnungen, Mehrleistungen, Minderleistungen und Funktionen aus dem {@link LehrerPersonalabschnittsdatenMappingContext}.
	 *
	 * @param ctx    der Kontext mit den Listen
	 * @param target das bereits gemappte Zielobjekt
	 */
	@AfterMapping
	default void mapListen(
			@Context final LehrerPersonalabschnittsdatenMappingContext ctx,
			@MappingTarget final LehrerPersonalabschnittsdaten target) {
		target.anrechnungen.addAll(ctx.anrechnungen());
		target.mehrleistung.addAll(ctx.mehrleistung());
		target.minderleistung.addAll(ctx.minderleistung());
		target.funktionen.addAll(ctx.funktionen());
	}

}
