package de.svws_nrw.mapper.schueler.lernabschnittsdaten;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.asd.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefung;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.VersetzungsvermerkKatalogEintrag;
import de.svws_nrw.asd.data.schule.OrganisationsformKatalogEintrag;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schueler.Versetzungsvermerk;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.mapper.JsonNullableMapper;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittCreateRequest;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittsdatenPatchRequest;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * Mapper für die Konvertierung zwischen der Entity {@link DTOSchuelerLernabschnittsdaten} und dem
 * Core-DTO {@link SchuelerLernabschnittsdaten}.
 */
@Mapper(uses = JsonNullableMapper.class)
public interface SchuelerLernabschnittsdatenMapper {

	/** schuelerLernabschnittsdatenMapper */
	SchuelerLernabschnittsdatenMapper INSTANCE = Mappers.getMapper(SchuelerLernabschnittsdatenMapper.class);

	/**
	 * Konvertiert eine {@link DTOSchuelerLernabschnittsdaten}-Entity in das Core-DTO
	 * {@link SchuelerLernabschnittsdaten}. Die Auflösung der Kataloge erfolgt abhängig von Schuljahr und
	 * Schulform, die Bemerkungen und Leistungsdaten werden über den
	 * {@link SchuelerLernabschnittsdatenMappingContext} ergänzt.
	 *
	 * @param entity     die Entity mit den Lernabschnittsdaten
	 * @param schuljahr  das Schuljahr des zugehörigen Schuljahresabschnitts
	 * @param schulform  die Schulform der eigenen Schule
	 * @param ctx        der Mapping-Kontext mit Bemerkungen und Leistungsdaten
	 *
	 * @return das befüllte Core-DTO {@link SchuelerLernabschnittsdaten}
	 */
	@Mapping(target = "id", source = "entity.ID")
	@Mapping(target = "schuelerID", source = "entity.Schueler_ID")
	@Mapping(target = "schuljahresabschnitt", source = "entity.Schuljahresabschnitts_ID")
	@Mapping(target = "datumAnfang", source = "entity.DatumVon")
	@Mapping(target = "datumEnde", source = "entity.DatumBis")
	@Mapping(target = "datumKonferenz", source = "entity.Konferenzdatum")
	@Mapping(target = "datumZeugnis", source = "entity.ZeugnisDatum")
	@Mapping(target = "pruefungsOrdnung", source = "entity.PruefOrdnung")
	@Mapping(target = "tutorID", source = "entity.Tutor_ID")
	@Mapping(target = "klassenID", source = "entity.Klassen_ID")
	@Mapping(target = "folgeklassenID", source = "entity.Folgeklasse_ID")
	@Mapping(target = "jahrgangID", source = "entity.Jahrgang_ID")
	@Mapping(target = "fachklasseID", source = "entity.Fachklasse_ID")
	@Mapping(target = "schwerpunktID", source = "entity.Schwerpunkt_ID")
	@Mapping(target = "foerderschwerpunkt1ID", source = "entity.Foerderschwerpunkt_ID")
	@Mapping(target = "foerderschwerpunkt2ID", source = "entity.Foerderschwerpunkt2_ID")
	@Mapping(target = "sonderpaedagogeID", source = "entity.Sonderpaedagoge_ID")
	@Mapping(target = "bilingualerZweig", source = "entity.BilingualerZweig")
	@Mapping(target = "noteDurchschnitt", source = "entity.DSNote")
	@Mapping(target = "abschlussart", source = "entity.AbschlussArt")
	@Mapping(target = "abschluss", source = "entity.Abschluss")
	@Mapping(target = "abschlussBerufsbildend", source = "entity.Abschluss_B")
	@Mapping(target = "zeugnisart", source = "entity.Zeugnisart")
	@Mapping(target = "textErgebnisPruefungsalgorithmus", source = "entity.PruefAlgoErgebnis")
	@Mapping(target = "fehlstundenGrenzwert", source = "entity.FehlstundenGrenzwert")
	@Mapping(target = "wechselNr", source = "entity.WechselNr")
	@Mapping(target = "istGewertet", source = "entity.SemesterWertung", defaultValue = "true")
	@Mapping(target = "istWiederholung", source = "entity.Wiederholung")
	@Mapping(target = "fehlstundenGesamt", source = "entity.SumFehlStd")
	@Mapping(target = "fehlstundenUnentschuldigt", source = "entity.SumFehlStdU")
	@Mapping(target = "hatSchwerbehinderungsNachweis", source = "entity.Schwerbehinderung")
	@Mapping(target = "hatAOSF", source = "entity.AOSF")
	@Mapping(target = "hatAutismus", source = "entity.Autist")
	@Mapping(target = "hatZieldifferentenUnterricht", source = "entity.ZieldifferentesLernen")
	@Mapping(target = "istAbschlussPrognose", source = "entity.AbschlIstPrognose")
	@Mapping(target = "istFachpraktischerAnteilAusreichend", source = "entity.FachPraktAnteilAusr")
	@Mapping(target = "idKlassenart", source = "entity.Klassenart", qualifiedByName = "mapIdKlassenart")
	@Mapping(target = "idSchulgliederung", source = "entity.Schulgliederung", qualifiedByName = "mapIdSchulgliederung")
	@Mapping(target = "idOrganisationsform", source = "entity.OrgFormKrz", qualifiedByName = "mapIdOrganisationsform")
	@Mapping(target = "idVersetzungsvermerk", source = "entity.VersetzungKrz", qualifiedByName = "mapIdVersetzungsvermerk")
	@Mapping(target = "idEpJahre", source = "entity.EPJahre", qualifiedByName = "mapIdEpJahre")
	@Mapping(target = "noteLernbereichGSbzwAL", source = "entity.Gesamtnote_GS", qualifiedByName = "mapNoteLernbereich")
	@Mapping(target = "noteLernbereichNW", source = "entity.Gesamtnote_NW", qualifiedByName = "mapNoteLernbereich")
	@Mapping(target = "nachpruefungen", ignore = true)
	@Mapping(target = "bemerkungen", ignore = true)
	@Mapping(target = "leistungsdaten", ignore = true)
	SchuelerLernabschnittsdaten toApi(
			DTOSchuelerLernabschnittsdaten entity,
			@Context int schuljahr,
			@Context Schulform schulform,
			@Context SchuelerLernabschnittsdatenMappingContext ctx
	);

	/**
	 * Befüllt die Nachprüfungsdaten des Lernabschnitts. Nachprüfungen werden nur gesetzt,
	 * wenn mögliche Nachprüfungsfächer eingetragen sind, sonst bleibt das Feld null.
	 *
	 * @param entity   das Datenbank-DTO des Lernabschnitts
	 * @param target   das zu befüllende Core-DTO
	 */
	@AfterMapping
	default void mapNachpruefungen(
			final DTOSchuelerLernabschnittsdaten entity,
			@MappingTarget final SchuelerLernabschnittsdaten target) {
		if (entity.MoeglNPFaecher == null) {
			return;
		}
		final String[] moeglicheNPFaecher = entity.MoeglNPFaecher.split(",");
		if ((moeglicheNPFaecher.length == 0) || moeglicheNPFaecher[0].trim().isBlank()) {
			return;
		}
		final SchuelerLernabschnittNachpruefungsdaten nachpruefungen = new SchuelerLernabschnittNachpruefungsdaten();
		Collections.addAll(nachpruefungen.moegliche, moeglicheNPFaecher);
		addNachpruefung(nachpruefungen.pruefungen, "V", entity.NPV_Fach_ID, entity.NPV_Datum, entity.NPV_NoteKrz);
		addNachpruefung(nachpruefungen.pruefungen, "A", entity.NPAA_Fach_ID, entity.NPAA_Datum, entity.NPAA_NoteKrz);
		addNachpruefung(nachpruefungen.pruefungen, "B", entity.NPBQ_Fach_ID, entity.NPBQ_Datum, entity.NPBQ_NoteKrz);
		target.nachpruefungen = nachpruefungen;
	}

	/**
	 * Fügt eine Nachprüfung zur Liste hinzu, sofern ein Fach eingetragen ist.
	 *
	 * @param pruefungen   die Liste, in die die Nachprüfung eingefügt wird
	 * @param grund        der Grund der Nachprüfung (V, A oder B)
	 * @param fachID       die ID des Faches, oder null, wenn es keine Nachprüfung gibt
	 * @param datum        das Datum der Nachprüfung
	 * @param note         das Kürzel der Note in der Nachprüfung
	 */
	private static void addNachpruefung(final List<SchuelerLernabschnittNachpruefung> pruefungen, final String grund,
			final Long fachID, final String datum, final String note) {
		if (fachID == null) {
			return;
		}
		final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
		np.grund = grund;
		np.fachID = fachID;
		np.datum = datum;
		np.note = note;
		pruefungen.add(np);
	}

	/**
	 * Löst das Kürzel der Klassenart auf die ID des zugehörigen Katalog-Eintrags auf.
	 *
	 * @param kuerzel     das Kürzel der Klassenart
	 * @param schuljahr   das Schuljahr, für das der Katalog-Eintrag gesucht wird
	 *
	 * @return die ID des Katalog-Eintrags oder {@code null}, wenn kein Eintrag gefunden wurde
	 */
	@Named("mapIdKlassenart")
	default Long mapIdKlassenart(final String kuerzel, @Context final int schuljahr) {
		if (kuerzel == null) {
			return null;
		}
		final KlassenartKatalogEintrag eintrag = Klassenart.data().getEintragBySchuljahrUndSchluessel(schuljahr, kuerzel);
		if (eintrag == null) {
			return null;
		}
		return eintrag.id;
	}

	/**
	 * Löst das ASD-Kürzel der Schulgliederung auf die ID des zugehörigen Katalog-Eintrags auf. Ist das Kürzel
	 * nicht gesetzt oder kann es nicht aufgelöst werden, so wird die Standard-Schulgliederung der Schulform verwendet.
	 *
	 * @param kuerzel     das ASD-Kürzel der Schulgliederung
	 * @param schuljahr   das Schuljahr, für das der Katalog-Eintrag gesucht wird
	 * @param schulform   die Schulform der eigenen Schule
	 *
	 * @return die ID des Katalog-Eintrags oder {@code null}, wenn auch keine Standard-Schulgliederung ermittelt werden kann
	 */
	@Named("mapIdSchulgliederung")
	default Long mapIdSchulgliederung(final String kuerzel, @Context final int schuljahr, @Context final Schulform schulform) {
		if (kuerzel != null) {
			final SchulgliederungKatalogEintrag eintrag = Schulgliederung.data().getEintragBySchuljahrUndSchluessel(schuljahr, kuerzel);
			if (eintrag != null) {
				return eintrag.id;
			}
		}
		final Schulgliederung standard = Schulgliederung.getDefault(schulform);
		if (standard == null) {
			return null;
		}
		final SchulgliederungKatalogEintrag eintrag = standard.daten(schuljahr);
		return (eintrag == null) ? null : eintrag.id;
	}

	/**
	 * Löst das Kürzel der Organisationsform auf die ID des zugehörigen Katalog-Eintrags auf. Welcher Katalog
	 * dabei verwendet wird, hängt von der Schulform ab.
	 *
	 * @param kuerzel     das Kürzel der Organisationsform
	 * @param schuljahr   das Schuljahr, für das der Katalog-Eintrag gesucht wird
	 * @param schulform   die Schulform der eigenen Schule
	 *
	 * @return die ID des Katalog-Eintrags oder {@code null}, wenn kein Eintrag gefunden wurde
	 */
	@Named("mapIdOrganisationsform")
	default Long mapIdOrganisationsform(final String kuerzel, @Context final int schuljahr, @Context final Schulform schulform) {
		if (kuerzel == null) {
			return null;
		}
		final OrganisationsformKatalogEintrag eintrag;
		if (Schulform.WB == schulform) {
			eintrag = WeiterbildungskollegOrganisationsformen.data().getEintragBySchuljahrUndSchluessel(schuljahr, kuerzel);
		} else if ((Schulform.BK == schulform) || (Schulform.SB == schulform)) {
			eintrag = BerufskollegOrganisationsformen.data().getEintragBySchuljahrUndSchluessel(schuljahr, kuerzel);
		} else {
			eintrag = AllgemeinbildendOrganisationsformen.data().getEintragBySchuljahrUndSchluessel(schuljahr, kuerzel);
		}
		return (eintrag == null) ? null : eintrag.id;
	}

	/**
	 * Löst das Kürzel des Versetzungsvermerks auf die ID des zugehörigen Katalog-Eintrags auf.
	 * Kann das Kürzel nicht aufgelöst werden, so wird {@code -1} zurückgegeben.
	 *
	 * @param kuerzel     das Kürzel des Versetzungsvermerks
	 * @param schuljahr   das Schuljahr, für das der Katalog-Eintrag gesucht wird
	 *
	 * @return die ID des Katalog-Eintrags oder {@code -1}, wenn kein Eintrag gefunden wurde
	 */
	@Named("mapIdVersetzungsvermerk")
	default Long mapIdVersetzungsvermerk(final String kuerzel, @Context final int schuljahr) {
		if (kuerzel == null) {
			return -1L;
		}
		final Versetzungsvermerk vermerk = Versetzungsvermerk.data().getWertByKuerzel(kuerzel);
		if (vermerk == null) {
			return -1L;
		}
		final VersetzungsvermerkKatalogEintrag eintrag = vermerk.daten(schuljahr);
		return (eintrag == null) ? -1L : eintrag.id;
	}

	/**
	 * Konvertiert die Anzahl der Jahre in der Eingangsphase in den Datentyp des Core-DTO.
	 *
	 * @param epJahre   die Anzahl der Jahre in der Eingangsphase
	 *
	 * @return die Anzahl der Jahre oder {@code null}, wenn diese nicht gesetzt ist
	 */
	@Named("mapIdEpJahre")
	default Long mapIdEpJahre(final Integer epJahre) {
		return (epJahre == null) ? null : epJahre.longValue();
	}

	/**
	 * Prüft, ob es sich bei der übergebenen Zahl um eine gültige Note der Sekundarstufe I handelt und
	 * gibt diese in dem Fall unverändert zurück.
	 *
	 * @param note   die Note des Lernbereichs
	 *
	 * @return die Note oder {@code null}, wenn es sich um keine gültige Note handelt
	 */
	@Named("mapNoteLernbereich")
	default Integer mapNoteLernbereich(final Integer note) {
		return (Note.fromNoteSekI(note) == null) ? null : note;
	}

	/**
	 * Befüllt die Bemerkungen des Lernabschnitts aus dem Mapping-Kontext. Die allgemeine
	 * Zeugnisbemerkung ist nicht Teil des Kontextes, da sie in der Tabelle der Lernabschnittsdaten
	 * selbst gespeichert ist, und wird daher aus der Entity ergänzt.
	 *
	 * @param entity   das Datenbank-DTO des Lernabschnitts
	 * @param ctx      der Mapping-Kontext mit Bemerkungen und Leistungsdaten
	 * @param target   das zu befüllende Core-DTO
	 */
	@AfterMapping
	default void mapListen(
			final DTOSchuelerLernabschnittsdaten entity,
			@Context final SchuelerLernabschnittsdatenMappingContext ctx,
			@MappingTarget final SchuelerLernabschnittsdaten target) {
		target.bemerkungen = ctx.bemerkung();
		target.bemerkungen.zeugnisAllgemein = entity.ZeugnisBem;
		target.leistungsdaten = ctx.leistungsdaten();
	}

	/**
	 * Mappt einen {@link SchuelerLernabschnittCreateRequest} auf eine neue {@link DTOSchuelerLernabschnittsdaten}-Entity.
	 * @param dto {@link SchuelerLernabschnittCreateRequest}
	 * @return {@link DTOSchuelerLernabschnittsdaten}
	 */
	@Mapping(target = "ID", ignore = true)
	@Mapping(target = "Schueler_ID", source = "idSchueler")
	@Mapping(target = "Schuljahresabschnitts_ID", source = "idSchuljahresabschnitt")
	@Mapping(target = "FachPraktAnteilAusr", constant = "true")
	@Mapping(target = "meldungBAN", constant = "false")
	@Mapping(target = "WechselNr", constant = "0")
	@Mapping(target = "SemesterWertung", constant = "true")
	@Mapping(target = "Schwerbehinderung", constant = "false")
	@Mapping(target = "AOSF", constant = "false")
	@Mapping(target = "Autist", constant = "false")
	@Mapping(target = "ZieldifferentesLernen", constant = "false")
	@Mapping(target = "Wiederholung", constant = "false")
	@Mapping(target = "Klassen_ID", source = "idKlasse")
	@Mapping(target = "Jahrgang_ID", source = "idJahrgang")
	@BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	DTOSchuelerLernabschnittsdaten toDomain(SchuelerLernabschnittCreateRequest dto);

	/**
	 * Wendet die Änderungen eines {@link SchuelerLernabschnittsdatenPatchRequest} auf eine bestehende
	 * {@link DTOSchuelerLernabschnittsdaten}-Entity an. Es werden nur die Felder übernommen, die im Request
	 * enthalten sind. Ein enthaltener Wert {@code null} entfernt den Wert in der Entity.
	 * Referenzen auf andere Tabellen und Kataloge werden vorher in der Service-Schicht geprüft
	 * und hier nur übernommen.
	 *
	 * @param input       der Patch-Request mit den zu ändernden Feldern
	 * @param toPatch     die zu aktualisierende Entity
	 * @param schuljahr   das Schuljahr des Lernabschnitts, für die Auflösung des Versetzungsvermerk-Kürzels
	 * @param schulform   die Schulform der eigenen Schule, für die Auswahl des Organisationsform-Katalogs
	 */
	@Mapping(target = "DatumVon", source = "datumAnfang", qualifiedByName = "mapDatum")
	@Mapping(target = "DatumBis", source = "datumEnde", qualifiedByName = "mapDatum")
	@Mapping(target = "Konferenzdatum", source = "datumKonferenz", qualifiedByName = "mapDatum")
	@Mapping(target = "ZeugnisDatum", source = "datumZeugnis", qualifiedByName = "mapDatum")
	@Mapping(target = "SemesterWertung", source = "istGewertet")
	@Mapping(target = "Wiederholung", source = "istWiederholung")
	@Mapping(target = "PruefOrdnung", source = "pruefungsOrdnung")
	@Mapping(target = "SumFehlStd", source = "fehlstundenGesamt")
	@Mapping(target = "SumFehlStdU", source = "fehlstundenUnentschuldigt")
	@Mapping(target = "FehlstundenGrenzwert", source = "fehlstundenGrenzwert")
	@Mapping(target = "Schwerbehinderung", source = "hatSchwerbehinderungsNachweis")
	@Mapping(target = "AOSF", source = "hatAOSF")
	@Mapping(target = "Autist", source = "hatAutismus")
	@Mapping(target = "ZieldifferentesLernen", source = "hatZieldifferentenUnterricht")
	@Mapping(target = "FachPraktAnteilAusr", source = "istFachpraktischerAnteilAusreichend")
	@Mapping(target = "DSNote", source = "noteDurchschnitt")
	@Mapping(target = "Gesamtnote_GS", source = "noteLernbereichGSbzwAL")
	@Mapping(target = "Gesamtnote_NW", source = "noteLernbereichNW")
	@Mapping(target = "AbschlussArt", source = "abschlussart")
	@Mapping(target = "AbschlIstPrognose", source = "istAbschlussPrognose")
	@Mapping(target = "PruefAlgoErgebnis", source = "textErgebnisPruefungsalgorithmus")
	@Mapping(target = "Zeugnisart", source = "zeugnisart")
	@Mapping(target = "Tutor_ID", source = "tutorID")
	@Mapping(target = "Sonderpaedagoge_ID", source = "sonderpaedagogeID")
	@Mapping(target = "Folgeklasse_ID", source = "folgeklassenID")
	@Mapping(target = "Klassen_ID", source = "klassenID")
	@Mapping(target = "Foerderschwerpunkt_ID", source = "foerderschwerpunkt1ID")
	@Mapping(target = "Foerderschwerpunkt2_ID", source = "foerderschwerpunkt2ID")
	@Mapping(target = "Schwerpunkt_ID", source = "schwerpunktID")
	@Mapping(target = "BilingualerZweig", source = "bilingualerZweig")
	@Mapping(target = "Jahrgang_ID", source = "jahrgangID")
	@Mapping(target = "Klassenart", source = "idKlassenart", qualifiedByName = "mapKlassenartKuerzel")
	@Mapping(target = "Schulgliederung", source = "idSchulgliederung", qualifiedByName = "mapSchulgliederungKuerzel")
	@Mapping(target = "OrgFormKrz", source = "idOrganisationsform", qualifiedByName = "mapOrganisationsformKuerzel")
	@Mapping(target = "EPJahre", source = "idEpJahre", qualifiedByName = "mapEpJahre")
	@Mapping(target = "Fachklasse_ID", source = "fachklasseID")
	@Mapping(target = "VersetzungKrz", source = "idVersetzungsvermerk", qualifiedByName = "mapVersetzungsvermerkKuerzel")
	@BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void patch(SchuelerLernabschnittsdatenPatchRequest input, @MappingTarget DTOSchuelerLernabschnittsdaten toPatch,
			@Context int schuljahr, @Context Schulform schulform);

	/**
	 * Wandelt ein leeres Datum in {@code null} um, damit ein geleertes Datumsfeld den Wert entfernt.
	 *
	 * @param datum   das Datum im Format yyyy-MM-dd, ein leerer oder nur aus Leerzeichen bestehender String oder {@code null}
	 *
	 * @return das Datum oder {@code null}, wenn es leer, nur aus Leerzeichen bestehend oder {@code null} ist
	 */
	@Named("mapDatum")
	default String mapDatum(final String datum) {
		return ((datum == null) || datum.isBlank()) ? null : datum;
	}

	/**
	 * Löst die ID einer Klassenart auf das Kürzel des zugehörigen Katalog-Eintrags auf.
	 * Die Gültigkeit der ID wurde zuvor im Service geprüft.
	 *
	 * @param idKlassenart   die ID der Klassenart oder {@code null}
	 *
	 * @return das Kürzel der Klassenart oder {@code null}, wenn die ID {@code null} ist
	 */
	@Named("mapKlassenartKuerzel")
	default String mapKlassenartKuerzel(final Long idKlassenart) {
		if (idKlassenart == null) {
			return null;
		}
		return Optional.ofNullable(Klassenart.data().getEintragByID(idKlassenart))
				.map(eintrag -> eintrag.kuerzel)
				.orElse(null);
	}

	/**
	 * Löst die ID einer Schulgliederung auf das Kürzel des zugehörigen Katalog-Eintrags auf.
	 * Die Gültigkeit der ID wurde zuvor im Service geprüft.
	 *
	 * @param idSchulgliederung   die ID der Schulgliederung oder {@code null}
	 *
	 * @return das Kürzel der Schulgliederung oder {@code null}, wenn die ID {@code null} ist
	 */
	@Named("mapSchulgliederungKuerzel")
	default String mapSchulgliederungKuerzel(final Long idSchulgliederung) {
		if (idSchulgliederung == null) {
			return null;
		}
		return Optional.ofNullable(Schulgliederung.data().getEintragByID(idSchulgliederung))
				.map(eintrag -> eintrag.kuerzel)
				.orElse(null);
	}

	/**
	 * Liefert den Katalog der Organisationsformen, der für die übergebene Schulform gilt
	 * (Weiterbildungskolleg, Berufskolleg inkl. SB oder allgemeinbildend).
	 *
	 * @param schulform   die Schulform der eigenen Schule
	 *
	 * @return der zugehörige Katalog der Organisationsformen
	 */
	static CoreTypeDataManager<OrganisationsformKatalogEintrag, ?> getOrganisationsformKatalog(final Schulform schulform) {
		if (Schulform.WB == schulform) {
			return WeiterbildungskollegOrganisationsformen.data();
		}
		if ((Schulform.BK == schulform) || (Schulform.SB == schulform)) {
			return BerufskollegOrganisationsformen.data();
		}
		return AllgemeinbildendOrganisationsformen.data();
	}

	/**
	 * Löst die ID einer Organisationsform auf das Kürzel des zugehörigen Katalog-Eintrags auf.
	 * Der Katalog wird anhand der Schulform gewählt. Die Gültigkeit der ID wurde zuvor im Service geprüft.
	 *
	 * @param idOrganisationsform   die ID der Organisationsform oder {@code null}
	 * @param schulform             die Schulform der eigenen Schule
	 *
	 * @return das Kürzel der Organisationsform oder {@code null}, wenn die ID {@code null} ist
	 */
	@Named("mapOrganisationsformKuerzel")
	default String mapOrganisationsformKuerzel(final Long idOrganisationsform, @Context final Schulform schulform) {
		if (idOrganisationsform == null) {
			return null;
		}
		return Optional.ofNullable(getOrganisationsformKatalog(schulform).getEintragByID(idOrganisationsform))
				.map(eintrag -> eintrag.kuerzel)
				.orElse(null);
	}

	/**
	 * Konvertiert die ID der Besuchsjahre in der Schuleingangsphase in die Anzahl der Jahre für die Entity.
	 * Die ID des Katalog-Eintrags entspricht dabei der Anzahl der Jahre.
	 *
	 * @param idEpJahre   die ID des Katalog-Eintrags oder {@code null}
	 *
	 * @return die Anzahl der Jahre oder {@code null}, wenn die ID {@code null} ist
	 */
	@Named("mapEpJahre")
	default Integer mapEpJahre(final Long idEpJahre) {
		return (idEpJahre == null) ? null : idEpJahre.intValue();
	}

	/**
	 * Löst die ID eines Versetzungsvermerks auf das Kürzel des Katalog-Eintrags im übergebenen Schuljahr auf.
	 * Die Gültigkeit der ID wurde zuvor im Service geprüft.
	 *
	 * @param idVersetzungsvermerk   die ID des Versetzungsvermerks oder {@code null}
	 * @param schuljahr              das Schuljahr des Lernabschnitts
	 *
	 * @return das Kürzel des Versetzungsvermerks oder {@code null}, wenn die ID {@code null} ist
	 */
	@Named("mapVersetzungsvermerkKuerzel")
	default String mapVersetzungsvermerkKuerzel(final Long idVersetzungsvermerk, @Context final int schuljahr) {
		if (idVersetzungsvermerk == null) {
			return null;
		}
		return Versetzungsvermerk.data().getWertByIDOrNull(idVersetzungsvermerk).daten(schuljahr).kuerzel;
	}
}
