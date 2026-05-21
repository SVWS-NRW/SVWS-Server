package de.svws_nrw.mapper.schueler.schulbesuch;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.Kindergartenbesuch;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchPatchRequest;
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

@Mapper(uses =  JsonNullableMapper.class,
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
	@Mapping(source = "entity.ID",                        		target = "id")
	@Mapping(source = "entity.Entlassart",   					target = "schluesselHoechsterSchulabschluss")
	@Mapping(source = "entity.HatBerufsausbildung",   			target = "berufsabschlussVorhanden")
	@Mapping(source = "entity.LSSchulform",              		target = "schulformVorherigeSchule")
	@Mapping(source = "entity.LSSchulEntlassDatum",				target = "entlassdatumVorherigeSchule")
	@Mapping(source = "entity.LSJahrgang",                		target = "kuerzelEntlassjahrgangVorherigeSchule")
	@Mapping(source = "entity.LSVersetzung",              		target = "idHerkunftsartVersetzungVorherigeSchule")
	@Mapping(source = "entity.LSBemerkung",               		target = "bemerkungVorherigeSchule")
	@Mapping(source = "entity.LSEntlassArt",              		target = "idAbschlussartVorherigeSchule")
	@Mapping(source = "entity.Entlassdatum",              		target = "entlassdatumDieseSchule")
	@Mapping(source = "entity.Entlassjahrgang_ID",        		target = "idEntlassjahrgangDieseSchule")
	@Mapping(source = "entity.Schulwechseldatum",         		target = "wechseldatumAufnehmendeSchule")
	@Mapping(source = "entity.WechselBestaetigt",         		target = "wechselBestaetigtAufnehmendeSchule")
	@Mapping(source = "entity.Einschulungsjahr",          		target = "einschulungsjahrGrundschule")
	@Mapping(source = "entity.JahrWechsel_SI",            		target = "wechseljahrSekI")
	@Mapping(source = "entity.ErsteSchulform_SI",         		target = "kuerzelErsteSchulformSek1")
	@Mapping(source = "entity.JahrWechsel_SII",           		target = "wechseljahrSekII")
	@Mapping(source = "entity.Kindergarten_ID",           		target = "idKindergarten")
	@Mapping(source = "entity.VerpflichtungSprachfoerderkurs",	target = "verpflichtungSprachfoerderkurs")
	@Mapping(source = "entity.TeilnahmeSprachfoerderkurs",     	target = "teilnahmeSprachfoerderkurs")
	@Mapping(source = "entity.LSSchulNr",                 		target = "idVorherigeSchule",							qualifiedByName = "mapIdSchule")
	@Mapping(source = "entity.LSEntlassgrund",            		target = "idEntlassgrundVorherigeSchule", 				qualifiedByName = "mapIdEntlassgrund")
	@Mapping(source = "entity.Entlassgrund",           			target = "idEntlassgrundDieseSchule", 					qualifiedByName = "mapIdEntlassgrund")
	@Mapping(source = "entity.SchulwechselNr",			        target = "idAufnehmendeSchule",							qualifiedByName = "mapIdSchule")
	@Mapping(source = "entity.EinschulungsartASD",      		target = "idEinschulungsartGrundschule", 				qualifiedByName = "mapIdEinschulungsart")
	@Mapping(source = "entity.EPJahre",                   		target = "idEingangsphaseGrundschule",					qualifiedByName = "mapIdEingangsphase")
	@Mapping(source = "entity.Uebergangsempfehlung_JG5",  		target = "idUebergangsempfehlungGrundschule",			qualifiedByName = "mapIdUebergangsempfehlung")
	@Mapping(source = "entity.DauerKindergartenbesuch",   		target = "idDauerKindergartenbesuch", 					qualifiedByName = "mapIdKindergartenbesuch")
	SchuelerSchulbesuchsdaten toApi(
			DTOSchueler entity,
			@Context SchulbesuchMappingContext ctx);

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
	 * Löst eine Schulnummer auf die interne Schul-ID auf.
	 *
	 * @param schulnummer die Schulnummer aus dem DTO
	 * @param ctx         der Mapping-Kontext mit der Schulen-Lookup-Map
	 * @return die interne ID der Schule oder {@code null}
	 */
	@Named("mapIdSchule")
	default Long mapIdSchule(final String schulnummer, @Context final SchulbesuchMappingContext ctx) {
		try {
			return ctx.schulenBySchulnummer().get(schulnummer).ID;
		} catch (final Exception ignored) {
			return null;
		}
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
		try {
			return ctx.entlassartenByBezeichnung().get(bezeichnung).ID;
		} catch (final Exception ignored) {
			return null;
		}
	}

	/**
	 * Löst einen Einschulungsart-Schlüssel auf die interne ID auf.
	 *
	 * @param schluessel der ASD-Schlüssel der Einschulungsart
	 * @return die interne ID der Einschulungsart oder {@code null}
	 */
	@Named("mapIdEinschulungsart")
	default Long mapIdEinschulungsart(final String schluessel) {
		try {
			return Einschulungsart.data().getWertBySchluessel(schluessel).getLetzterEintrag().id;
		} catch (final Exception ignored) {
			return null;
		}
	}

	/**
	 * Löst die Anzahl der Eingangsphase-Jahre auf die interne Katalog-ID auf.
	 *
	 * @param value der Integer-Wert aus dem DTO
	 * @return die interne ID des Katalogeintrags oder {@code null}
	 */
	@Named("mapIdEingangsphase")
	default Long mapIdEingangsphase(final Integer value) {
		try {
			return PrimarstufeSchuleingangsphaseBesuchsjahre.data().getEintragByID(value.longValue()).id;
		} catch (final Exception ignored) {
			return null;
		}
	}

	/**
	 * Löst einen Übergangsempfehlungs-Schlüssel auf die interne ID auf.
	 *
	 * @param schluessel der Schlüssel der Übergangsempfehlung
	 * @return die interne ID oder {@code null}
	 */
	@Named("mapIdUebergangsempfehlung")
	default Long mapIdUebergangsempfehlung(final String schluessel) {
		try {
			return Uebergangsempfehlung.data().getWertBySchluessel(schluessel).historie().getLast().id;
		} catch (final Exception ignored) {
			return null;
		}
	}

	/**
	 * Löst einen Kindergartenbesuch-Schlüssel auf die interne Katalog-ID auf.
	 *
	 * @param schluessel der Schlüssel des Kindergartenbesuchs
	 * @return die interne ID des Katalogeintrags oder {@code null}
	 */
	@Named("mapIdKindergartenbesuch")
	default Long mapIdKindergartenbesuch(final String schluessel) {
		try {
			return Kindergartenbesuch.data().getWertBySchluessel(schluessel).historie().getLast().id;
		} catch (final Exception ignored) {
			return null;
		}
	}


	/**
	 * Wendet die Änderungen eines {@link SchulbesuchPatchRequest} auf eine bestehende
	 * {@link DTOSchueler}-Entity an. Felder mit {@code null}-Wert werden nicht überschrieben.
	 *
	 * @param input                  der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch                die zu aktualisierende Entity
	 */
	@Mapping(source = "berufsabschlussVorhanden",			 		target = "HatBerufsausbildung")
	@Mapping(source = "entlassdatumVorherigeSchule", 				target = "LSSchulEntlassDatum")
	@Mapping(source = "kuerzelEntlassjahrgangVorherigeSchule", 		target = "LSJahrgang")
	@Mapping(source = "idHerkunftsartVersetzungVorherigeSchule", 	target = "LSVersetzung")
	@Mapping(source = "bemerkungVorherigeSchule", 					target = "LSBemerkung")
	@Mapping(source = "idAbschlussartVorherigeSchule", 				target = "LSEntlassArt")
	@Mapping(source = "entlassdatumDieseSchule", 					target = "Entlassdatum")
	@Mapping(source = "idEntlassjahrgangDieseSchule", 				target = "Entlassjahrgang_ID")
	@Mapping(source = "wechseldatumAufnehmendeSchule", 				target = "Schulwechseldatum")
	@Mapping(source = "wechselBestaetigtAufnehmendeSchule", 		target = "WechselBestaetigt")
	@Mapping(source = "einschulungsjahrGrundschule", 				target = "Einschulungsjahr")
	@Mapping(source = "wechseljahrSekI", 							target = "JahrWechsel_SI")
	@Mapping(source = "kuerzelErsteSchulformSek1", 					target = "ErsteSchulform_SI")
	@Mapping(source = "wechseljahrSekII", 							target = "JahrWechsel_SII")
	@Mapping(source = "idKindergarten", 							target = "Kindergarten_ID")
	@Mapping(source = "verpflichtungSprachfoerderkurs",	 			target = "VerpflichtungSprachfoerderkurs")
	@Mapping(source = "teilnahmeSprachfoerderkurs",			 		target = "TeilnahmeSprachfoerderkurs")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchulbesuchPatchRequest input, @MappingTarget DTOSchueler toPatch);

}
