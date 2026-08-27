package de.svws_nrw.mapper.schueler.stammdaten;

import java.util.Optional;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schueler.stammdaten.SchuelerImportData;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenPatchRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct-Mapper für die Konvertierung zwischen {@link DTOSchueler} und den zugehörigen
 * API- bzw. Service-Modellen.
 * <p>
 * <b>Vorbedingung für alle Mapping-Methoden:</b> Die fachliche Validierung aller Eingabedaten
 * (CoreType-IDs, Pflichtfelder, Referenzintegrität) obliegt ausschließlich dem Service vor
 * dem Aufruf des Mappers. Der Mapper setzt valide Eingaben voraus und führt selbst
 * keine Validierung durch.
 */
@Mapper(uses = JsonNullableMapper.class,
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SchuelerStammdatenMapper {

	/** Instanz des Mappers */
	SchuelerStammdatenMapper INSTANCE = Mappers.getMapper(SchuelerStammdatenMapper.class);

	/**
	 * Konvertiert einen {@link DTOSchueler} in {@link SchuelerStammdaten}.
	 * Felder mit Typ-Konvertierung (Nationalitaeten, Verkehrssprache, Geschlecht) werden
	 * über {@code @Named}-Methoden aufgelöst. Das Foto wird bewusst ignoriert und
	 * muss nach dem Mapping separat gesetzt werden.
	 *
	 * @param entity die Schüler-Entity
	 * @return die befüllten {@link SchuelerStammdaten}
	 */
	@Mapping(target = "id", source = "ID")
	@Mapping(target = "foto", ignore = true)
	@Mapping(target = "nachname", source = "Nachname")
	@Mapping(target = "vorname", source = "Vorname")
	@Mapping(target = "alleVornamen", source = "AlleVornamen")
	@Mapping(target = "geschlecht", source = "Geschlecht", qualifiedByName = "mapGeschlecht")
	@Mapping(target = "geburtsdatum", source = "Geburtsdatum")
	@Mapping(target = "geburtsort", source = "Geburtsort")
	@Mapping(target = "geburtsname", source = "Geburtsname")
	@Mapping(target = "strassenname", source = "Strassenname")
	@Mapping(target = "hausnummer", source = "HausNr")
	@Mapping(target = "hausnummerZusatz", source = "HausNrZusatz")
	@Mapping(target = "wohnortID", source = "Ort_ID")
	@Mapping(target = "ortsteilID", source = "Ortsteil_ID")
	@Mapping(target = "telefon", source = "Telefon")
	@Mapping(target = "telefonMobil", source = "Fax")
	@Mapping(target = "emailPrivat", source = "Email")
	@Mapping(target = "emailSchule", source = "SchulEmail")
	@Mapping(target = "idStaatsangehoerigkeit", source = "StaatKrz", qualifiedByName = "mapNationalitaet")
	@Mapping(target = "idStaatsangehoerigkeit2", source = "StaatKrz2", qualifiedByName = "mapNationalitaet")
	@Mapping(target = "religionID", source = "Religion_ID")
	@Mapping(target = "druckeKonfessionAufZeugnisse", source = "KonfDruck")
	@Mapping(target = "religionabmeldung", source = "Religionsabmeldung")
	@Mapping(target = "religionanmeldung", source = "Religionsanmeldung")
	@Mapping(target = "hatMigrationshintergrund", source = "Migrationshintergrund")
	@Mapping(target = "zuzugsjahr", source = "JahrZuzug")
	@Mapping(target = "idGeburtsland", source = "GeburtslandSchueler", qualifiedByName = "mapNationalitaet")
	@Mapping(target = "idVerkehrspracheFamilie", source = "VerkehrsspracheFamilie", qualifiedByName = "mapVerkehrssprache")
	@Mapping(target = "idGeburtslandVater", source = "GeburtslandVater", qualifiedByName = "mapNationalitaet")
	@Mapping(target = "idGeburtslandMutter", source = "GeburtslandMutter", qualifiedByName = "mapNationalitaet")
	@Mapping(target = "status", source = "idStatus")
	@Mapping(target = "istDuplikat", source = "Duplikat")
	@Mapping(target = "externeSchulNr", source = "ExterneSchulNr")
	@Mapping(target = "idSchuelerausweis", source = "Ausweisnummer")
	@Mapping(target = "fahrschuelerArtID", source = "Fahrschueler_ID")
	@Mapping(target = "haltestelleID", source = "Haltestelle_ID")
	@Mapping(target = "anmeldedatum", source = "AnmeldeDatum")
	@Mapping(target = "aufnahmedatum", source = "Aufnahmedatum")
	@Mapping(target = "istVolljaehrig", source = "Volljaehrig")
	@Mapping(target = "istSchulpflichtErfuellt", source = "SchulpflichtErf")
	@Mapping(target = "istBerufsschulpflichtErfuellt", source = "BerufsschulpflErf")
	@Mapping(target = "hatMasernimpfnachweis", source = "MasernImpfnachweis")
	@Mapping(target = "keineAuskunftAnDritte", source = "KeineAuskunft")
	@Mapping(target = "erhaeltSchuelerBAFOEG", source = "Bafoeg")
	@Mapping(target = "erhaeltMeisterBAFOEG", source = "MeisterBafoeg")
	@Mapping(target = "beginnBildungsgang", source = "BeginnBildungsgang")
	@Mapping(target = "dauerBildungsgang", source = "DauerBildungsgang")
	@Mapping(target = "beruf", source = "Beruf")
	SchuelerStammdaten toApi(DTOSchueler entity);

	/**
	 * Mappt einen {@link SchuelerImportData} auf eine neue {@link DTOSchueler}-Entity.
	 * Nur die für den Import relevanten Felder werden übertragen.
	 * Alle weiteren Felder (ID, Schuljahresabschnitts_ID, Boolean-Defaults,
	 * Staatsangehörigkeit, Wohnort etc.) obliegen dem Service via {@code initDTO}.
	 *
	 * @param request die Import-Daten mit den zu übernehmenden Feldern
	 * @param guId    die bereits generierte GU_ID des Schülers
	 * @return die befüllte {@link DTOSchueler}-Entity
	 */
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "GU_ID", source = "guId")
	@Mapping(target = "Nachname", source = "request.nachname")
	@Mapping(target = "Vorname", source = "request.vorname")
	@Mapping(target = "AlleVornamen", source = "request.alleVornamen")
	@Mapping(target = "Geschlecht", source = "request.idGeschlecht", qualifiedByName = "mapIdGeschlecht")
	@Mapping(target = "idStatus", source = "request.idSchuelerStatus")
	@Mapping(target = "Geburtsdatum", source = "request.geburtsdatum")
	@Mapping(target = "Religion_ID", source = "request.idReligion")
	@Mapping(target = "AnmeldeDatum", source = "request.anmeldedatum")
	@Mapping(target = "Aufnahmedatum", source = "request.aufnahmedatum")
	@Mapping(target = "BeginnBildungsgang", source = "request.beginnBildungsgang")
	@Mapping(target = "DauerBildungsgang", source = "request.dauerBildungsgang")
	@Mapping(target = "Schuljahresabschnitts_ID", source = "request.idSchuljahresabschnitt")
	DTOSchueler toDomain(SchuelerImportData request, String guId);

	/**
	 * Wendet die Änderungen eines {@link SchuelerStammdatenPatchRequest} auf eine bestehende
	 * {@link DTOSchueler}-Entity an. Felder mit {@code null}-Wert werden nicht überschrieben.
	 * Felder mit komplexer Validierungslogik (Wohnort/Ortsteil, Staatsangehörigkeit, Status usw.)
	 * werden hier nicht gemappt – sie verbleiben in der Service-Schicht.
	 *
	 * @param input   der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch die zu aktualisierende Entity
	 */
	@Mapping(target = "Nachname", source = "nachname")
	@Mapping(target = "Vorname", source = "vorname")
	@Mapping(target = "AlleVornamen", source = "alleVornamen")
	@Mapping(target = "Geschlecht", source = "geschlecht", qualifiedByName = "mapIdGeschlecht")
	@Mapping(target = "Geburtsdatum", source = "geburtsdatum")
	@Mapping(target = "Geburtsort", source = "geburtsort")
	@Mapping(target = "Geburtsname", source = "geburtsname")
	@Mapping(target = "Strassenname", source = "strassenname")
	@Mapping(target = "HausNr", source = "hausnummer")
	@Mapping(target = "HausNrZusatz", source = "hausnummerZusatz")
	@Mapping(target = "Ort_ID", source = "wohnortID")
	@Mapping(target = "Ortsteil_ID", source = "ortsteilID")
	@Mapping(target = "Telefon", source = "telefon")
	@Mapping(target = "Fax", source = "telefonMobil")
	@Mapping(target = "Email", source = "emailPrivat")
	@Mapping(target = "SchulEmail", source = "emailSchule")
	@Mapping(target = "StaatKrz", source = "idStaatsangehoerigkeit", qualifiedByName = "mapIdNationalitaet")
	@Mapping(target = "StaatKrz2", source = "idStaatsangehoerigkeit2", qualifiedByName = "mapIdNationalitaet")
	@Mapping(target = "Religion_ID", source = "religionID")
	@Mapping(target = "KonfDruck", source = "druckeKonfessionAufZeugnisse")
	@Mapping(target = "Religionsabmeldung", source = "religionabmeldung")
	@Mapping(target = "Religionsanmeldung", source = "religionanmeldung")
	@Mapping(target = "Migrationshintergrund", source = "hatMigrationshintergrund")
	@Mapping(target = "JahrZuzug", source = "zuzugsjahr")
	@Mapping(target = "VerkehrsspracheFamilie", source = "idVerkehrspracheFamilie", qualifiedByName = "mapIdVerkehrssprache")
	@Mapping(target = "GeburtslandSchueler", source = "idGeburtsland", qualifiedByName = "mapIdNationalitaet")
	@Mapping(target = "GeburtslandVater", source = "idGeburtslandVater", qualifiedByName = "mapIdNationalitaet")
	@Mapping(target = "GeburtslandMutter", source = "idGeburtslandMutter", qualifiedByName = "mapIdNationalitaet")
	@Mapping(target = "idStatus", source = "status")
	@Mapping(target = "ExterneSchulNr", source = "externeSchulNr")
	@Mapping(target = "Ausweisnummer", source = "idSchuelerausweis")
	@Mapping(target = "Fahrschueler_ID", source = "fahrschuelerArtID")
	@Mapping(target = "Haltestelle_ID", source = "haltestelleID")
	@Mapping(target = "AnmeldeDatum", source = "anmeldedatum")
	@Mapping(target = "Aufnahmedatum", source = "aufnahmedatum")
	@Mapping(target = "Volljaehrig", source = "istVolljaehrig")
	@Mapping(target = "SchulpflichtErf", source = "istSchulpflichtErfuellt")
	@Mapping(target = "BerufsschulpflErf", source = "istBerufsschulpflichtErfuellt")
	@Mapping(target = "MasernImpfnachweis", source = "hatMasernimpfnachweis")
	@Mapping(target = "KeineAuskunft", source = "keineAuskunftAnDritte")
	@Mapping(target = "Bafoeg", source = "erhaeltSchuelerBAFOEG")
	@Mapping(target = "MeisterBafoeg", source = "erhaeltMeisterBAFOEG")
	@Mapping(target = "Duplikat", source = "istDuplikat")
	@Mapping(target = "BeginnBildungsgang", source = "beginnBildungsgang")
	@Mapping(target = "DauerBildungsgang", source = "dauerBildungsgang")
	@Mapping(target = "Beruf", source = "beruf")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchuelerStammdatenPatchRequest input, @MappingTarget DTOSchueler toPatch);

	/**
	 * Liest die interne ID aus einem {@link Nationalitaeten}-CoreType.
	 * Gibt {@code null} zurück, wenn der Wert nicht gesetzt ist.
	 *
	 * @param nationalitaet der CoreType-Wert aus dem DTO
	 * @return die interne ID des letzten Historieneintrags oder {@code null}
	 */
	@Named("mapNationalitaet")
	default Long mapNationalitaet(final Nationalitaeten nationalitaet) {
		return Optional.ofNullable(nationalitaet)
				.map(n -> n.historie().getLast().id)
				.orElse(null);
	}

	/**
	 * Liest die interne ID aus einem {@link Verkehrssprache}-CoreType.
	 * Gibt {@code null} zurück, wenn der Wert nicht gesetzt ist.
	 *
	 * @param verkehrssprache der CoreType-Wert aus dem DTO
	 * @return die interne ID des letzten Historieneintrags oder {@code null}
	 */
	@Named("mapVerkehrssprache")
	default Long mapVerkehrssprache(final Verkehrssprache verkehrssprache) {
		return Optional.ofNullable(verkehrssprache)
				.map(v -> v.historie().getLast().id)
				.orElse(null);
	}

	/**
	 * Liest die numerische ID aus dem {@link Geschlecht}-Enum.
	 *
	 * @param geschlecht der Geschlecht-Wert aus dem DTO
	 * @return die ID des Geschlechts
	 */
	@Named("mapGeschlecht")
	default int mapGeschlecht(final Geschlecht geschlecht) {
		return geschlecht.id;
	}

	/**
	 * Löst eine Geschlecht-ID auf den zugehörigen {@link Geschlecht}-Enum auf.
	 *
	 * @param idGeschlecht die numerische ID des Geschlechts aus dem Request
	 * @return der zugehörige {@link Geschlecht}-Wert
	 */
	@Named("mapIdGeschlecht")
	default Geschlecht mapIdGeschlecht(final Integer idGeschlecht) {
		return Geschlecht.fromValue(idGeschlecht);
	}

	/**
	 * Löst eine Nationalität-ID auf den zugehörigen {@link Nationalitaeten}-Enum auf.
	 *
	 * @param idNationalitaet die numerische ID der Nationalitä aus dem Request
	 * @return der zugehörige {@link Nationalitaeten}-Wert
	 */
	@Named("mapIdNationalitaet")
	default Nationalitaeten mapIdNationalitaet(final Long idNationalitaet) {
		return Nationalitaeten.data().getWertByIDOrNull(idNationalitaet);
	}

	/**
	 * Löst eine Verkehrssprache-ID auf den zugehörigen {@link Verkehrssprache}-Enum auf.
	 *
	 * @param idVerkehrssprache die numerische ID der Verkehrssprache aus dem Request
	 * @return der zugehörige {@link Verkehrssprache}-Wert
	 */
	@Named("mapIdVerkehrssprache")
	default Verkehrssprache mapIdVerkehrssprache(final Long idVerkehrssprache) {
		return Verkehrssprache.data().getWertByIDOrNull(idVerkehrssprache);
	}

}
