package de.svws_nrw.mapper.schueler.schulbesuch;

import java.util.List;
import java.util.Optional;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.Kindergartenbesuch;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchPatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchResolver;
import org.apache.poi.util.StringUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = JsonNullableMapper.class,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SchulbesuchMapper {

	/** Instanz des Mapper */
	SchulbesuchMapper INSTANCE = Mappers.getMapper(SchulbesuchMapper.class);

	/**
	 * Konvertiert einen {@link DTOSchueler} in {@link SchuelerSchulbesuchsdaten}.
	 * Komplexe Felder (ID-Lookups, Katalogauflösungen) werden über den {@link SchulbesuchMappingContext} aufgelöst.
	 * Merkmale und bisherige Schulen werden via {@link #mapListen} nach dem Mapping gesetzt.
	 *
	 * @param entity die Schüler-Entity
	 * @param ctx    der Mapping-Kontext mit Lookup-Maps sowie Merkmalen und bisherigen Schulen
	 * @return die befüllten {@link SchuelerSchulbesuchsdaten}
	 */
	@Mapping(source = "entity.ID", target = "id")
	@Mapping(source = "entity.Entlassart", target = "schluesselHoechsterSchulabschluss")
	@Mapping(source = "entity.HatBerufsausbildung", target = "berufsabschlussVorhanden")
	@Mapping(source = "entity.LSSchulEntlassDatum", target = "entlassdatumVorherigeSchule")
	@Mapping(source = "entity.LSJahrgang", target = "kuerzelEntlassjahrgangVorherigeSchule")
	@Mapping(source = "entity.LSVersetzung", target = "idHerkunftsartVersetzungVorherigeSchule")
	@Mapping(source = "entity.LSBemerkung", target = "bemerkungVorherigeSchule")
	@Mapping(source = "entity.Entlassdatum", target = "entlassdatumDieseSchule")
	@Mapping(source = "entity.Entlassjahrgang_ID", target = "idEntlassjahrgangDieseSchule")
	@Mapping(source = "entity.Schulwechseldatum", target = "wechseldatumAufnehmendeSchule")
	@Mapping(source = "entity.WechselBestaetigt", target = "wechselBestaetigtAufnehmendeSchule")
	@Mapping(source = "entity.Einschulungsjahr", target = "einschulungsjahrGrundschule")
	@Mapping(source = "entity.JahrWechsel_SI", target = "wechseljahrSekI")
	@Mapping(source = "entity.ErsteSchulform_SI", target = "kuerzelErsteSchulformSek1")
	@Mapping(source = "entity.JahrWechsel_SII", target = "wechseljahrSekII")
	@Mapping(source = "entity.Kindergarten_ID", target = "idKindergarten")
	@Mapping(source = "entity.VerpflichtungSprachfoerderkurs", target = "verpflichtungSprachfoerderkurs")
	@Mapping(source = "entity.TeilnahmeSprachfoerderkurs", target = "teilnahmeSprachfoerderkurs")
	@Mapping(source = "entity.LSSchulNr", target = "idVorherigeSchule", qualifiedByName = "mapIdSchule")
	@Mapping(source = "entity.LSEntlassgrund", target = "idEntlassgrundVorherigeSchule", qualifiedByName = "mapIdEntlassgrund")
	@Mapping(source = "entity.Entlassgrund", target = "idEntlassgrundDieseSchule", qualifiedByName = "mapIdEntlassgrund")
	@Mapping(source = "entity.SchulwechselNr", target = "idAufnehmendeSchule", qualifiedByName = "mapIdSchule")
	@Mapping(source = "entity.EinschulungsartASD", target = "idEinschulungsartGrundschule", qualifiedByName = "mapIdEinschulungsart")
	@Mapping(source = "entity.EPJahre", target = "idEingangsphaseGrundschule", qualifiedByName = "mapIdEingangsphase")
	@Mapping(source = "entity.Uebergangsempfehlung_JG5", target = "idUebergangsempfehlungGrundschule", qualifiedByName = "mapIdUebergangsempfehlung")
	@Mapping(source = "entity.DauerKindergartenbesuch", target = "idDauerKindergartenbesuch", qualifiedByName = "mapIdKindergartenbesuch")
	@Mapping(source = "entity.LSFachklKennung", target = "schluesselCoreTypeFachklasseVorherigeSchule", qualifiedByName = "mapSchluesselFachklasse")
	SchuelerSchulbesuchsdaten toApi(
			DTOSchueler entity,
			@Context SchulbesuchMappingContext ctx);

	/**
	 * Splittet den kombinierten Abschlussart-Schlüssel aus {@link DTOSchueler#LSEntlassArt}
	 * auf die getrennten Felder für allgemeinbildenden und berufsbildenden Abschluss auf.
	 * Bei einstelligem Wert wird nur der allgemeinbildende Schlüssel gesetzt.
	 * Bei zweistelligem Wert enthält die erste Stelle den berufsbildenden (Ziffer)
	 * und die zweite Stelle den allgemeinbildenden Schlüssel (Buchstabe).
	 *
	 * @param entity die Schüler-Entity mit dem Quellfeld
	 * @param target das Zielobjekt der Mapping-Operation
	 */
	@AfterMapping
	default void mapAbschlussartVorherigeSchule(
			final DTOSchueler entity,
			@MappingTarget final SchuelerSchulbesuchsdaten target) {
		final var abschlussart = entity.LSEntlassArt;
		if ((abschlussart == null) || abschlussart.isBlank()) {
			return;
		}
		if (abschlussart.length() == 1) {
			target.schluesselAbschlussartAllgemeinbildendVorherigeSchule = abschlussart;
		} else if (abschlussart.length() == 2) {
			target.schluesselAbschlussartBerufsbildendVorherigeSchule = abschlussart.substring(0, 1);
			target.schluesselAbschlussartAllgemeinbildendVorherigeSchule = abschlussart.substring(1, 2);
		}
	}

	/**
	 * Falls es sich bei der Auswahl der vorherigen Schule um "Kein Schulbesuch" handelt (Keine Schulnummer in der entity hinterlegt)
	 * wird die id des CoreTypes HerkunftSonstige.json gemapped
	 *
	 * @param entity die Schüler-Entity mit dem Quellfeld
	 * @param ctx    der Kontext
	 * @param target das bereits gemappte Zielobjekt
	 */
	@AfterMapping
	default void mapHerkunftSonstige(
			final DTOSchueler entity,
			@Context final SchulbesuchMappingContext ctx,
			@MappingTarget final SchuelerSchulbesuchsdaten target) {
		SchulbesuchResolver.mapHerkunftSonstige(entity, target, ctx.jahrEntlassungVorherigeSchule());
	}

	/**
	 * Falls es sich bei der Auswahl der vorherigen Schule um BK, SB oder WB handelt und eine Schulgliederung hinterlegt ist,
	 * wird die id des CoreTypes HerkunftBildungsgang.json gemapped
	 *
	 * @param entity die Schüler-Entity mit dem Quellfeld
	 * @param ctx    der Kontext
	 * @param target das bereits gemappte Zielobjekt
	 */
	@AfterMapping
	default void mapSchulgliederung(
			final DTOSchueler entity,
			@Context final SchulbesuchMappingContext ctx,
			@MappingTarget final SchuelerSchulbesuchsdaten target) {
		SchulbesuchResolver.mapSchulgliederung(entity, target, ctx.jahrEntlassungVorherigeSchule());
	}



	/**
	 * Setzt nach dem Mapping die Merkmale und bisherigen Schulen aus dem {@link SchulbesuchMappingContext}.
	 *
	 * @param ctx    der Kontext mit den Listen
	 * @param target das bereits gemappte Zielobjekt
	 */
	@AfterMapping
	default void mapListen(
			@Context final SchulbesuchMappingContext ctx,
			@MappingTarget final SchuelerSchulbesuchsdaten target) {
		target.merkmale = ctx.merkmale();
		target.bisherBesuchteSchulen = ctx.bisherigeSchulen();
	}

	/**
	 * Rekonstruiert den ursprünglichen Fachklassen-Schlüssel (z.B. '10-17902')
	 * aus dem gespeicherten Feld {@link DTOSchueler#LSFachklKennung} (z.B. '10-179-02')
	 *
	 * @param kennung die Kennung der Fachklasse
	 * @return der schluessel der Fachklasse
	 */
	@Named("mapSchluesselFachklasse")
	default String mapSchluesselFachklasse(final String kennung) {
		if (StringUtil.isBlank(kennung)) {
			return null;
		}
		// "10-179-02" -> letzten Bindestrich entfernen -> "10-17902"
		final int lastDash = kennung.lastIndexOf("-");
		return kennung.substring(0, lastDash) + kennung.substring(lastDash + 1);
	}

	/**
	 * Löst eine Schulnummer auf die interne Schul-ID auf.
	 *
	 * @param schulnummer die Schulnummer aus dem DTO
	 * @param ctx         der Mapping-Kontext mit der Schulen-Lookup-Map
	 * @return die interne ID der Schule oder {@code null}
	 */
	@Named("mapIdSchule")
	default Long mapIdSchule(final String schulnummer, @Context final SchulbesuchMappingContext ctx) {
		if (schulnummer == null) {
			return null;
		}
		return Optional.ofNullable(ctx.schulenBySchulnummer().get(schulnummer))
				.map(s -> s.ID)
				.orElse(null);
	}

	/**
	 * Löst einen Entlassgrund (Bezeichnung) auf die interne ID auf.
	 *
	 * @param bezeichnung die Bezeichnung des Entlassgrundes aus dem DTO
	 * @param ctx         der Mapping-Kontext mit der Entlassarten-Lookup-Map
	 * @return die interne ID des Entlassgrundes oder {@code null}
	 */
	@Named("mapIdEntlassgrund")
	default Long mapIdEntlassgrund(final String bezeichnung, @Context final SchulbesuchMappingContext ctx) {
		if (bezeichnung == null) {
			return null;
		}
		return Optional.ofNullable(ctx.entlassartenByBezeichnung().get(bezeichnung))
				.map(s -> s.ID)
				.orElse(null);
	}

	/**
	 * Löst einen Einschulungsart-Schlüssel auf die interne ID auf.
	 *
	 * @param schluessel der ASD-Schlüssel der Einschulungsart
	 * @return die interne ID der Einschulungsart oder {@code null}
	 */
	@Named("mapIdEinschulungsart")
	default Long mapIdEinschulungsart(final String schluessel) {
		if (schluessel == null) {
			return null;
		}
		return Optional.ofNullable(Einschulungsart.data().getWertBySchluessel(schluessel))
				.map(CoreType::historie)
				.filter(list -> !list.isEmpty())
				.map(List::getLast)
				.map(s -> s.id)
				.orElse(null);
	}

	/**
	 * Löst die Anzahl der Eingangsphase-Jahre auf die interne Katalog-ID auf.
	 *
	 * @param value der Integer-Wert aus dem DTO
	 * @return die interne ID des Katalogeintrags oder {@code null}
	 */
	@Named("mapIdEingangsphase")
	default Long mapIdEingangsphase(final Integer value) {
		return Optional.ofNullable(value)
				.map(Integer::longValue)
				.orElse(null);
	}

	/**
	 * Löst einen Übergangsempfehlungs-Schlüssel auf die interne ID auf.
	 *
	 * @param schluessel der Schlüssel der Übergangsempfehlung
	 * @return die interne ID oder {@code null}
	 */
	@Named("mapIdUebergangsempfehlung")
	default Long mapIdUebergangsempfehlung(final String schluessel) {
		if (schluessel == null) {
			return null;
		}
		return Optional.ofNullable(Uebergangsempfehlung.data().getWertBySchluessel(schluessel))
				.map(CoreType::historie)
				.filter(list -> !list.isEmpty())
				.map(List::getLast)
				.map(s -> s.id)
				.orElse(null);
	}

	/**
	 * Löst einen Kindergartenbesuch-Schlüssel auf die interne Katalog-ID auf.
	 *
	 * @param schluessel der Schlüssel des Kindergartenbesuchs
	 * @return die interne ID des Katalogeintrags oder {@code null}
	 */
	@Named("mapIdKindergartenbesuch")
	default Long mapIdKindergartenbesuch(final String schluessel) {
		if (schluessel == null) {
			return null;
		}
		return Optional.ofNullable(Kindergartenbesuch.data().getWertBySchluessel(schluessel))
				.map(CoreType::historie)
				.filter(list -> !list.isEmpty())
				.map(List::getLast)
				.map(s -> s.id)
				.orElse(null);
	}

	/**
	 * Wendet die Änderungen eines {@link SchulbesuchPatchRequest} auf eine bestehende
	 * {@link DTOSchueler}-Entity an. Felder mit {@code null}-Wert werden nicht überschrieben.
	 *
	 * @param input                  der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch                die zu aktualisierende Entity
	 */
	@Mapping(source = "berufsabschlussVorhanden", target = "HatBerufsausbildung")
	@Mapping(source = "entlassdatumVorherigeSchule", target = "LSSchulEntlassDatum")
	@Mapping(source = "kuerzelEntlassjahrgangVorherigeSchule", target = "LSJahrgang")
	@Mapping(source = "idHerkunftsartVersetzungVorherigeSchule", target = "LSVersetzung")
	@Mapping(source = "bemerkungVorherigeSchule", target = "LSBemerkung")
	@Mapping(source = "entlassdatumDieseSchule", target = "Entlassdatum")
	@Mapping(source = "idEntlassjahrgangDieseSchule", target = "Entlassjahrgang_ID")
	@Mapping(source = "wechseldatumAufnehmendeSchule", target = "Schulwechseldatum")
	@Mapping(source = "wechselBestaetigtAufnehmendeSchule", target = "WechselBestaetigt")
	@Mapping(source = "einschulungsjahrGrundschule", target = "Einschulungsjahr")
	@Mapping(source = "wechseljahrSekI", target = "JahrWechsel_SI")
	@Mapping(source = "kuerzelErsteSchulformSek1", target = "ErsteSchulform_SI")
	@Mapping(source = "wechseljahrSekII", target = "JahrWechsel_SII")
	@Mapping(source = "idKindergarten", target = "Kindergarten_ID")
	@Mapping(source = "verpflichtungSprachfoerderkurs", target = "VerpflichtungSprachfoerderkurs")
	@Mapping(source = "teilnahmeSprachfoerderkurs", target = "TeilnahmeSprachfoerderkurs")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchulbesuchPatchRequest input, @MappingTarget DTOSchueler toPatch);

}
