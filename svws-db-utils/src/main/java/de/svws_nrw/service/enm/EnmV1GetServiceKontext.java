package de.svws_nrw.service.enm;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.db.dto.current.katalog.DTOAnkreuzkompetenzJahrgang;
import de.svws_nrw.db.dto.current.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.dto.current.katalog.DTOFloskeln;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzdaten;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsNotenmodulCredentials;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulCredentialsRepository;
import de.svws_nrw.repo.enm.NotenmodulCredentialsTimestampsRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzJahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzKonfigurationRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepository;
import de.svws_nrw.repo.schule.kataloge.floskel.FloskelJahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.floskel.FloskelRepository;
import de.svws_nrw.repo.schule.kataloge.floskelgruppe.FloskelgruppeRepository;
import de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt.FoerderschwerpunktRepository;
import de.svws_nrw.repo.schule.kataloge.teilleistungsart.TeilleistungsartRepository;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerAnkreuzkompetenzenRepository;
import de.svws_nrw.repo.schueler.SchuelerAnkreuzkompetenzenTimestampsRepository;
import de.svws_nrw.repo.schueler.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.SchuelerLeistungsdatenTimestampsRepository;
import de.svws_nrw.repo.schueler.SchuelerLernabschnittBemerkungenRepository;
import de.svws_nrw.repo.schueler.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.SchuelerLernabschnittTimestampsRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.SchuelerTeilleistungenRepository;
import de.svws_nrw.repo.schueler.SchuelerTeilleistungenTimestampsRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Stellt den Daten-Kontext für den Get-Service für ENM-Daten in Version 1 bereit.
 */
public final class EnmV1GetServiceKontext {

	/** Der Manager für das Zusammenstellen der ENM-Daten */
	public EnmV1DatenManager manager;

	/** Das Repository für den Zugriff auf die Schuldaten */
	private final EigeneSchuleRepository eigeneSchuleRepository;

	/** Das Repository für den Zugriff auf die Schuljahresabschnitte */
	private final SchuljahresabschnitteRepository schuljahresabschnitteRepository;

	/** Das Repository für den Zugriff auf die Lehrerdaten */
	private final LehrerRepository lehrerRepository;

	/** Das Repository für den Zugriff auf die Fachdaten */
	private final FachRepository fachRepository;

	/** Das Repository für den Zugriff auf die Schüler */
	private final SchuelerRepository schuelerRepository;

	/** Das Repository für den Zugriff auf die Schüler-Lernabschnitte */
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Schüler-Lernabschnitte */
	private final SchuelerLernabschnittTimestampsRepository schuelerLernabschnittTimestampsRepository;

	/** Das Repository für den Zugriff auf die Lernabschnittbezogenen Bemerkungen zu Schülern */
	private final SchuelerLernabschnittBemerkungenRepository schuelerLernabschnittBemerkungenRepository;

	/** Das Repository für den Zugriff auf die Schüler-Leistungsdaten */
	private final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Schüler-Leistungsdaten */
	private final SchuelerLeistungsdatenTimestampsRepository schuelerLeistungsdatenTimestampsRepository;

	/** Das Repository für den Zugriff auf die Schüler-Teilleistungen */
	private final SchuelerTeilleistungenRepository schuelerTeilleistungenRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Schüler-Teilleistungen */
	private final SchuelerTeilleistungenTimestampsRepository schuelerTeilleistungenTimestampsRepository;

	/** Das Repository für den Zugriff auf die Schüler-Ankreuzkompetenzen */
	private final SchuelerAnkreuzkompetenzenRepository schuelerAnkreuzkompetenzenRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Schüler-Ankreuzkompetenzen */
	private final SchuelerAnkreuzkompetenzenTimestampsRepository schuelerAnkreuzkompetenzenTimestampsRepository;

	/** Das Repository für den Zugriff auf die Kurse */
	private final KurseRepository kurseRepository;

	/** Das Repository für den Zugriff auf die Klassen */
	private final KlassenRepository klassenRepository;

	/** Das Repository für den Zugriff auf die Klassenleitungen */
	private final KlassenleitungenRepository klassenleitungenRepository;

	/** Das Repository für den Zugriff auf die Jahrgänge */
	private final JahrgangRepository jahrgangRepository;

	/** Das Repository für den Zugriff auf die Förderschwerpunkte */
	private final FoerderschwerpunktRepository foerderschwerpunktRepository;

	/** Das Repository für den Zugriff auf die Konfiguration für die Ankreuzkompetenzen */
	private final AnkreuzkompetenzKonfigurationRepository ankreuzkompetenzKonfigurationRepository;

	/** Das Repository für den Zugriff auf den Katalog der Ankreuzkompetenzen */
	private final AnkreuzkompetenzRepository ankreuzkompetenzRepository;

	/** Das Repository für den Zugriff auf die Jahrgangszuordnung für den Katalog der Ankreuzkompetenzen */
	private final AnkreuzkompetenzJahrgangRepository ankreuzkompetenzJahrgangRepository;

	/** Das Repository für den Zugriff auf den Katalog der Floskeln */
	private final FloskelRepository floskelRepository;

	/** Das Repository für den Zugriff auf den Katalog der Floskelgruppen */
	private final FloskelgruppeRepository floskelgruppeRepository;

	/** Das Repository für den Zugriff auf die Jahrgangszuordnungen der Floskeln */
	private final FloskelJahrgangRepository floskelJahrgangRepository;

	/** Das Repository für den Zugriff auf den Katalog der Teilleistungsarten */
	private final TeilleistungsartRepository TeilleistungsartRepository;

	/** Das Repository für den Zugriff auf die Credentials der Lehrer für das Notenmodul */
	private final NotenmodulCredentialsRepository notenmodulCredentialsRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Credentials der Lehrer für das Notenmodul */
	private final NotenmodulCredentialsTimestampsRepository notenmodulCredentialsTimestampsRepository;


	/** Die Informationen zur Schule */
	private DTOEigeneSchule schule;

	/** Der Schuljahresabschnitt, für welchen die ENM-Daten erzeugt wurden */
	private DTOSchuljahresabschnitte schuljahresabschnitt;

	/** Der Lehrer für welchen die ENM-Daten aggregiert werden - oder null, falls die Daten schulweit aggregiert werden */
	private DTOLehrer dtoLehrer;

	/** Eine Map mit der Zuordnung der Lehrer zu ihren Entitäten */
	private Map<Long, DTOLehrer> mapLehrer;

	/** Ein Map mit der Zuordnung der Notenmodul-Credentials zu den Passwort-Hashes */
	private Map<Long, DTONotenmodulCredentials> mapLehrerPWHash;

	/** Ein Map mit der Zuordnung der Notenmodul-Credentials zu Zeitstempel der Passwort-Hashes */
	private Map<Long, DTOTimestampsNotenmodulCredentials> mapLehrerPWHashTimestamps;

	/** Ggf. die Konfiguration für die Ankreuzkompetenzen. Ist diese nicht gesetzt, so werden Ankreuzkompetenzen nicht unterstützt */
	private Optional<DTOAnkreuzdaten> ankreuzkompetenzenKonfiguration = Optional.empty();

	/** Eine Map mit den Katalog-Einträgen zu den Ankreuzkompetenzen */
	private Map<Long, DTOAnkreuzfloskeln> mapKatalogAnkreuzkompetenzen;

	/** Eine Map mit den Jahrgangszuordnungen der Ankreuzkompetenzen zugeordnet zu den jeweiligen Ankreuzkompetenz-IDs */
	private Map<Long, List<DTOAnkreuzkompetenzJahrgang>> kompetenzZuordnungenByJahrgang;

	/** Eine Map mit den Förderschwerpunkten zugeordnet zu der Förderschwerpunkt-ID */
	private Map<Long, DTOFoerderschwerpunkt> mapFoerderschwerpunkte;

	/** Eine Map mit den Schülern zugeordnet zu der Schüler-IDs */
	private Map<Long, DTOSchueler> mapSchueler;

	/** Eine Map mit den Fächern zugeordnet zu der Fach-IDs */
	private Map<Long, DTOFach> mapFaecher;

	/** Eine Map mit den Jahrgangsdaten zugeordnet zu der Jahrgangs-ID */
	private Map<Long, DTOJahrgang> mapJahrgaenge;

	/** Eine Map mit den Klassendaten zugeordnet zu der Klassen-ID */
	private Map<Long, DTOKlassen> mapKlassen;

	/** Eine Map mit den IDs der Klassenleitungen (Lehrer) zugeordnet zu der Klassen-ID */
	private Map<Long, List<Long>> mapKlassenLeitung;

	/** Eine Map mit den Kursen zugeordnet zu der Kurs-ID */
	private Map<Long, DTOKurs> mapKurse;

	/** Eine Map mit den Teilleistungsarten zugeordnet zu der Teilleistungsart-ID */
	private Map<Long, DTOTeilleistungsarten> mapTeilleistungsarten;

	/** Eine Map mit den Schüler-Lernabschnittsdaten zugeordnet zu deren ID */
	private Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte;

	/** Eine Map mit den Bemerkungen der Schüler-Lernabschnittsdaten zugeordnet zu der ID der Lernabschnitte */
	private Map<Long, DTOSchuelerPSFachBemerkungen> mapBemerkungen;

	/** Eine Map mit den Zeitstempeln der Schüler-Lernabschnittsdaten zugeordnet zu der ID der Lernabschnitte */
	private Map<Long, DTOTimestampsSchuelerLernabschnittsdaten> mapTimestampsLernabschnitte;

	/** Eine Map mit den Schüler-Leistungsdaten zugeordnet zu deren ID */
	private Map<Long, List<DTOSchuelerLeistungsdaten>> mapLeistungen;

	/** Eine Map mit den Zeitstempeln der Schüler-Leistungsdaten zugeordnet zu der ID der Leistungsdaten */
	private Map<Long, DTOTimestampsSchuelerLeistungsdaten> mapTimestampsLeistungsdaten;

	/** Eine Map mit den Schüler-Teilleistungen zugeordnet zu deren ID */
	private Map<Long, List<DTOSchuelerTeilleistung>> mapTeilleistungen;

	/** Eine Map mit den Zeitstempeln der Schüler-Teilleistungen zugeordnet zu der ID der Teilleistungen */
	private Map<Long, DTOTimestampsSchuelerTeilleistungen> mapTeilleistungenTimestamps;

	/** Eine Map mit den Schüler-Ankreuzkompetenzen zugeordnet zu deren Lernabschnitts-ID */
	private Map<Long, List<DTOSchuelerAnkreuzfloskeln>> mapAnkreuzkompetenzen;

	/** Eine Map mit den Zeitstempeln der Schüler-Ankreuzkompetenzen zugeordnet zu der ID der Schüler-Ankreuzkompetenzen */
	private Map<Long, DTOTimestampsSchuelerAnkreuzkompetenzen> mapAnkreuzkompetenzenTimestamps;

	/** Die Liste der Floskelgruppen */
	private List<DTOFloskelgruppen> listFloskelgruppen;

	/** Eine Map mit der Zuordnung von Jahrgangs-IDs zu Floskel-IDs, sofern eine Einschränkung der Floskel auf Jahrgänge besteht */
	private Map<Long, List<Long>> mapJahrgangIdsByFloskelIds;

	/** Die Liste der Floskeln */
	private List<DTOFloskeln> listFloskeln;


	private EnmV1GetServiceKontext(final EigeneSchuleRepository eigeneSchuleRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final LehrerRepository lehrerRepository,
			final FachRepository fachRepository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLernabschnittTimestampsRepository schuelerLernabschnittTimestampsRepository,
			final SchuelerLernabschnittBemerkungenRepository schuelerLernabschnittBemerkungenRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final SchuelerLeistungsdatenTimestampsRepository schuelerLeistungsdatenTimestampsRepository,
			final SchuelerTeilleistungenRepository schuelerTeilleistungenRepository,
			final SchuelerTeilleistungenTimestampsRepository schuelerTeilleistungenTimestampsRepository,
			final SchuelerAnkreuzkompetenzenRepository schuelerAnkreuzkompetenzenRepository,
			final SchuelerAnkreuzkompetenzenTimestampsRepository schuelerAnkreuzkompetenzenTimestampsRepository,
			final KurseRepository kurseRepository,
			final KlassenRepository klassenRepository,
			final KlassenleitungenRepository klassenleitungenRepository,
			final JahrgangRepository jahrgangRepository,
			final FoerderschwerpunktRepository foerderschwerpunktRepository,
			final AnkreuzkompetenzKonfigurationRepository ankreuzkompetenzKonfigurationRepository,
			final AnkreuzkompetenzRepository ankreuzkompetenzRepository,
			final AnkreuzkompetenzJahrgangRepository ankreuzkompetenzJahrgangRepository,
			final FloskelRepository floskelRepository,
			final FloskelgruppeRepository floskelgruppeRepository,
			final FloskelJahrgangRepository floskelJahrgangRepository,
			final TeilleistungsartRepository TeilleistungsartRepository,
			final NotenmodulCredentialsRepository notenmodulCredentialsRepository,
			final NotenmodulCredentialsTimestampsRepository notenmodulCredentialsTimestampsRepository) {
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.schuljahresabschnitteRepository = schuljahresabschnitteRepository;
		this.lehrerRepository = lehrerRepository;
		this.fachRepository = fachRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
		this.schuelerLernabschnittTimestampsRepository = schuelerLernabschnittTimestampsRepository;
		this.schuelerLernabschnittBemerkungenRepository = schuelerLernabschnittBemerkungenRepository;
		this.schuelerLeistungsdatenRepository = schuelerLeistungsdatenRepository;
		this.schuelerLeistungsdatenTimestampsRepository = schuelerLeistungsdatenTimestampsRepository;
		this.schuelerTeilleistungenRepository = schuelerTeilleistungenRepository;
		this.schuelerTeilleistungenTimestampsRepository = schuelerTeilleistungenTimestampsRepository;
		this.schuelerAnkreuzkompetenzenRepository = schuelerAnkreuzkompetenzenRepository;
		this.schuelerAnkreuzkompetenzenTimestampsRepository = schuelerAnkreuzkompetenzenTimestampsRepository;
		this.kurseRepository = kurseRepository;
		this.klassenRepository = klassenRepository;
		this.klassenleitungenRepository = klassenleitungenRepository;
		this.jahrgangRepository = jahrgangRepository;
		this.foerderschwerpunktRepository = foerderschwerpunktRepository;
		this.ankreuzkompetenzKonfigurationRepository = ankreuzkompetenzKonfigurationRepository;
		this.ankreuzkompetenzRepository = ankreuzkompetenzRepository;
		this.ankreuzkompetenzJahrgangRepository = ankreuzkompetenzJahrgangRepository;
		this.floskelRepository = floskelRepository;
		this.floskelgruppeRepository = floskelgruppeRepository;
		this.floskelJahrgangRepository = floskelJahrgangRepository;
		this.TeilleistungsartRepository = TeilleistungsartRepository;
		this.notenmodulCredentialsRepository = notenmodulCredentialsRepository;
		this.notenmodulCredentialsTimestampsRepository = notenmodulCredentialsTimestampsRepository;
	}

	/**
	 * Erstellt einen neuen Service-Kontext.
	 *
	 * @param eigeneSchuleRepository                                 das Repository für den Zugriff auf die Schuldaten
	 * @param schuljahresabschnitteRepository                  das Repository für den Zugriff auf die Schuljahresabschnitte
	 * @param lehrerRepository                                 das Repository für den Zugriff auf die Lehrerdaten
	 * @param fachRepository                                   das Repository für den Zugriff auf die Fachdaten
	 * @param schuelerRepository                               das Repository für den Zugriff auf die Schüler
	 * @param schuelerLernabschnittRepository                  das Repository für den Zugriff auf die Schüler-Lernabschnitte
	 * @param schuelerLernabschnittTimestampsRepository        das Repository für den Zugriff auf die Zeitstempel für die Schüler-Lernabschnitte
	 * @param schuelerLernabschnittBemerkungenRepository       das Repository für den Zugriff auf die Lernabschnittbezogenen Bemerkungen zu Schülern
	 * @param schuelerLeistungsdatenRepository                 das Repository für den Zugriff auf die Schüler-Leistungsdaten
	 * @param schuelerLeistungsdatenTimestampsRepository       das Repository für den Zugriff auf die Zeitstempel für die Schüler-Leistungsdaten
	 * @param schuelerTeilleistungenRepository                 das Repository für den Zugriff auf die Schüler-Teilleistungen
	 * @param schuelerTeilleistungenTimestampsRepository       das Repository für den Zugriff auf die Zeitstempel für die Schüler-Teilleistungen
	 * @param schuelerAnkreuzkompetenzenRepository             das Repository für den Zugriff auf die Schüler-Ankreuzkompetenzen
	 * @param schuelerAnkreuzkompetenzenTimestampsRepository   das Repository für den Zugriff auf die Zeitstempel für die Schüler-Ankreuzkompetenzen
	 * @param kurseRepository                                  das Repository für den Zugriff auf die Kurse
	 * @param klassenRepository                                das Repository für den Zugriff auf die Klassen
	 * @param klassenleitungenRepository                       das Repository für den Zugriff auf die Klassenleitungen
	 * @param jahrgangRepository                             das Repository für den Zugriff auf die Jahrgänge
	 * @param foerderschwerpunktRepository                    das Repository für den Zugriff auf die Förderschwerpunkte
	 * @param ankreuzkompetenzKonfigurationRepository        das Repository für den Zugriff auf die Konfiguration für die Ankreuzkompetenzen
	 * @param ankreuzkompetenzRepository                     das Repository für den Zugriff auf den Katalog der Ankreuzkompetenzen
	 * @param ankreuzkompetenzJahrgangRepository           das Repository für den Zugriff auf die Jahrgangszuordnung für den Katalog der Ankreuzkompetenzen
	 * @param floskelRepository                                das Repository für den Zugriff auf den Katalog der Floskeln
	 * @param floskelgruppeRepository                         das Repository für den Zugriff auf den Katalog der Floskelgruppen
	 * @param floskelJahrgangRepository                      das Repository für den Zugriff auf die Jahrgangszuordnungen der Floskeln
	 * @param TeilleistungsartRepository                     das Repository für den Zugriff auf den Katalog der Teilleistungsarten
	 * @param notenmodulCredentialsRepository                  das Repository für den Zugriff auf die Credentials der Lehrer für das Notenmodul
	 * @param notenmodulCredentialsTimestampsRepository        das Repository für den Zugriff auf die Zeitstempel für die Credentials
	 *                                                         der Lehrer für das Notenmodul
	 *
	 * @return der Kontext
	 */
	public static EnmV1GetServiceKontext of(final EigeneSchuleRepository eigeneSchuleRepository,
			final SchuljahresabschnitteRepository schuljahresabschnitteRepository,
			final LehrerRepository lehrerRepository,
			final FachRepository fachRepository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLernabschnittTimestampsRepository schuelerLernabschnittTimestampsRepository,
			final SchuelerLernabschnittBemerkungenRepository schuelerLernabschnittBemerkungenRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final SchuelerLeistungsdatenTimestampsRepository schuelerLeistungsdatenTimestampsRepository,
			final SchuelerTeilleistungenRepository schuelerTeilleistungenRepository,
			final SchuelerTeilleistungenTimestampsRepository schuelerTeilleistungenTimestampsRepository,
			final SchuelerAnkreuzkompetenzenRepository schuelerAnkreuzkompetenzenRepository,
			final SchuelerAnkreuzkompetenzenTimestampsRepository schuelerAnkreuzkompetenzenTimestampsRepository,
			final KurseRepository kurseRepository,
			final KlassenRepository klassenRepository,
			final KlassenleitungenRepository klassenleitungenRepository,
			final JahrgangRepository jahrgangRepository,
			final FoerderschwerpunktRepository foerderschwerpunktRepository,
			final AnkreuzkompetenzKonfigurationRepository ankreuzkompetenzKonfigurationRepository,
			final AnkreuzkompetenzRepository ankreuzkompetenzRepository,
			final AnkreuzkompetenzJahrgangRepository ankreuzkompetenzJahrgangRepository,
			final FloskelRepository floskelRepository,
			final FloskelgruppeRepository floskelgruppeRepository,
			final FloskelJahrgangRepository floskelJahrgangRepository,
			final TeilleistungsartRepository TeilleistungsartRepository,
			final NotenmodulCredentialsRepository notenmodulCredentialsRepository,
			final NotenmodulCredentialsTimestampsRepository notenmodulCredentialsTimestampsRepository) {
		return new EnmV1GetServiceKontext(eigeneSchuleRepository, schuljahresabschnitteRepository, lehrerRepository, fachRepository, schuelerRepository,
				schuelerLernabschnittRepository, schuelerLernabschnittTimestampsRepository, schuelerLernabschnittBemerkungenRepository,
				schuelerLeistungsdatenRepository, schuelerLeistungsdatenTimestampsRepository,
				schuelerTeilleistungenRepository, schuelerTeilleistungenTimestampsRepository,
				schuelerAnkreuzkompetenzenRepository, schuelerAnkreuzkompetenzenTimestampsRepository,
				kurseRepository, klassenRepository, klassenleitungenRepository, jahrgangRepository, foerderschwerpunktRepository,
				ankreuzkompetenzKonfigurationRepository, ankreuzkompetenzRepository, ankreuzkompetenzJahrgangRepository,
				floskelRepository, floskelgruppeRepository, floskelJahrgangRepository, TeilleistungsartRepository,
				notenmodulCredentialsRepository, notenmodulCredentialsTimestampsRepository);
	}


	/**
	 * Führt für den Fall, dass Lehrer-spezifische ENM-Daten erzeugt werden eine Filterung
	 * der Lernabschnitte anhand der zuvor geladenenen Leistungsdaten durch. Dabei
	 * bleiben Lernabschnitte erhalten, für welche der Lehrer entweder Klassenlehrer ist
	 * oder in denen als Fachlehrer unterrichtet.
	 *
	 * @param dtoLehrer           der Lehrer, für den gefiltert wird oder null, falls keine Filterung erfolgen soll
	 * @param mapLernabschnitte   die zu filternden Lernabschnitte
	 * @param mapKlassen          die Map der Klassen
	 * @param mapKlassenLeitung   die Map der Klassenleitungen
	 * @param mapLeistungen       die Map der bereits geladenen lehrerspezifischen Leistungen
	 */
	private static void filterLernabschnitteByLehrer(final DTOLehrer dtoLehrer,
			final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte,
			final Map<Long, DTOKlassen> mapKlassen,
			final Map<Long, List<Long>> mapKlassenLeitung,
			final Map<Long, List<DTOSchuelerLeistungsdaten>> mapLeistungen) {
		if (dtoLehrer == null) {
			return;
		}
		mapLernabschnitte.entrySet().removeIf(entry -> {
			final var la = entry.getValue();

			// Prüfe, ob der Lehrer Fachlehrer ist
			if (mapLeistungen.get(la.ID) != null) {
				return false; // NICHT löschen
			}

			// Wenn die ENM-Datei für einen einzelnen Lehrer erstellt wird, so muss ein Lernabschnitt beachtet werden, wo er Klassenlehrer ist ...
			final DTOKlassen klasse = (la.Klassen_ID == null) ? null : mapKlassen.get(la.Klassen_ID);
			final List<Long> idsKlassenleitungen = (klasse == null) ? null : mapKlassenLeitung.get(la.Klassen_ID);
			if (idsKlassenleitungen == null) {
				// Er ist kein Fach- und es gibt keine zugeordnetene Klassenleitungen - also entfernen
				return true;
			}

			for (final Long idKlassenleitung : idsKlassenleitungen) {
				if (idKlassenleitung.equals(dtoLehrer.ID)) {
					return false; // NICHT löschen
				}
			}

			// Er ist kein Fach- und kein Klassenlehrer - also Löschen
			return true;
		});
	}


	/**
	 * Lädt die grundlegenden Daten für die Erstellung der ENM-Daten aus der Datenbank. Die Reihenfolge der
	 * Repository-Aufrufe ist so gestaltet, dass die Zugriffe möglichst gebündelt stattfinden.
	 *
	 * @param idLehrer   die Lehrer-ID, falls die Daten Lehrer-spezifisch erzeugt werden
	 */
	public void fetchData(final Long idLehrer) {
		// Erzeuge den Manager für das Hinzufügen von ENM-Daten
		this.manager = new EnmV1DatenManager(idLehrer);

		// Bestimme zunächst die Schulspezifischen Informationen, insbesondere zum Schuljahresabschnitt
		final DTOEigeneSchule schuleDto = eigeneSchuleRepository.getFirst();
		final long idSchuljahresabschnitt = schuleDto.Schuljahresabschnitts_ID;
		this.schuljahresabschnitt = schuljahresabschnitteRepository.getById(idSchuljahresabschnitt);

		// Bestimme dann die Klassen für den schuljahresabschnitt (wird für die Abfrage der Klassenleitungen gebraucht
		this.mapKlassen = klassenRepository.getMapBySchuljahresabschnitt(idSchuljahresabschnitt);
		this.mapKlassenLeitung = klassenleitungenRepository.getMapKlassenleitungen(mapKlassen.keySet());

		// Bestimmer die Lehrer ...
		this.mapLehrer = lehrerRepository.getMap();

		// ... ermittle den speziellen Lehrer, für welchen die ENM-Daten bestimmt werden, falls eine idLehrer übergeben wurde
		// ... wenn idLehrer = null ist, dann sollen die ENM-Daten schulweit bestimmt werden
		this.dtoLehrer = (idLehrer == null) ? null : mapLehrer.get(idLehrer);
		if ((idLehrer != null) && (dtoLehrer == null)) {
			throw new ApiOperationException(Status.NOT_FOUND);
		}

		this.mapSchueler = schuelerRepository.getMapByStatusAndSchuljahresabschnitt(idSchuljahresabschnitt,
				List.of(SchuelerStatus.AKTIV.daten(schuljahresabschnitt.Jahr).id, SchuelerStatus.EXTERN.daten(schuljahresabschnitt.Jahr).id));

		// Bestimme zunächst alle aktuellen Lernabschnitte des Schuljahresabschnittes
		this.mapLernabschnitte =
				schuelerLernabschnittRepository.getMapByLernabschnittIDAndSchuljahreabschnitt(mapSchueler.keySet(), idSchuljahresabschnitt);

		// Bestimme alle Leistungsdaten des Schuljahresabschnittes oder bei Lehrer-Spezifischen ENM-Daten die Liestungsdaten, wo der Lehrer Fachlehrer
		final List<DTOSchuelerLeistungsdaten> leistungsdaten = (dtoLehrer == null)
				? schuelerLeistungsdatenRepository.findListByLernabschnitt(mapLernabschnitte.keySet())
				: schuelerLeistungsdatenRepository.findListByLernabschnittAndFachlehrer(mapLernabschnitte.keySet(), List.of(dtoLehrer.ID));
		final List<Long> idsLeistungsdaten = leistungsdaten.stream().map(ld -> ld.ID).toList();
		this.mapLeistungen = leistungsdaten.stream().collect(Collectors.groupingBy(l -> l.Abschnitt_ID));

		// Filtere ggf. die Lernabschnitte anhand von nicht vorhandenen Leistungsdaten, falls der Lehrer nicht als Klassenlehrer eingetragen ist
		filterLernabschnitteByLehrer(dtoLehrer, mapLernabschnitte, mapKlassen, mapKlassenLeitung, mapLeistungen);

		// Bestimme nun die Teilleistungen und die Ankreuzkompetenzen des Schülers
		final List<DTOSchuelerTeilleistung> listTeilleistungen =
				schuelerTeilleistungenRepository.findListByLeistungsdaten(idsLeistungsdaten);
		final List<DTOSchuelerAnkreuzfloskeln> listAnkreuzkompetenzen =
				schuelerAnkreuzkompetenzenRepository.findListByLernabschnitt(mapLernabschnitte.keySet());

		// Lese nun die restlichen nicht schülerspezifischen ENM-Daten ein
		this.schule = eigeneSchuleRepository.getFirst();
		this.mapLehrerPWHash = notenmodulCredentialsRepository.getMap();
		this.mapLehrerPWHashTimestamps = notenmodulCredentialsTimestampsRepository.getMap();
		this.ankreuzkompetenzenKonfiguration = ankreuzkompetenzKonfigurationRepository.findFirst();
		this.mapKatalogAnkreuzkompetenzen = ankreuzkompetenzRepository.getMap();
		this.kompetenzZuordnungenByJahrgang = ankreuzkompetenzJahrgangRepository.getAll().stream().collect(Collectors.groupingBy(e -> e.idJahrgang));
		this.mapFoerderschwerpunkte = foerderschwerpunktRepository.getMap();
		this.mapFaecher = fachRepository.getMap();
		this.mapJahrgaenge = jahrgangRepository.getMap();
		this.mapKurse = kurseRepository.getMapBySchuljahresabschnitt(idSchuljahresabschnitt);
		this.mapTeilleistungsarten = TeilleistungsartRepository.getMap();
		this.mapBemerkungen = schuelerLernabschnittBemerkungenRepository.findMapByLernabschnittID(mapLernabschnitte.keySet());
		this.mapTimestampsLernabschnitte = schuelerLernabschnittTimestampsRepository.findMapByIds(mapLernabschnitte.keySet());
		this.mapTimestampsLeistungsdaten = schuelerLeistungsdatenTimestampsRepository.findMapByIds(idsLeistungsdaten);
		this.mapTeilleistungen = listTeilleistungen.stream().collect(Collectors.groupingBy(st -> st.Leistung_ID));
		this.mapTeilleistungenTimestamps = schuelerTeilleistungenTimestampsRepository.findMapByIds(listTeilleistungen.stream().map(t -> t.ID).toList());
		this.mapAnkreuzkompetenzen = listAnkreuzkompetenzen.stream().collect(Collectors.groupingBy(a -> a.Abschnitt_ID));
		this.mapAnkreuzkompetenzenTimestamps =
				schuelerAnkreuzkompetenzenTimestampsRepository.findMapByIds(listAnkreuzkompetenzen.stream().map(t -> t.ID).toList());

		this.listFloskelgruppen = floskelgruppeRepository.getAll();
		this.mapJahrgangIdsByFloskelIds = floskelJahrgangRepository.getAll().stream()
				.collect(Collectors.groupingBy(fj -> fj.Floskel_ID, Collectors.mapping(f -> f.Jahrgang_ID, Collectors.toList())));
		this.listFloskeln = floskelRepository.getAll();
	}


	/**
	 * Gibt die Daten zur Schule zurück.
	 *
	 * @return die Daten zur Schule
	 */
	public DTOEigeneSchule getSchuldaten() {
		return this.schule;
	}


	/**
	 * Gibt das Schuljahr zurück, für welches die ENM-Daten erstellt wurden.
	 *
	 * @return das Schuljahr
	 */
	public int getSchuljahr() {
		return this.schuljahresabschnitt.Jahr;
	}


	/**
	 * Gibt das Halbjahr zurück, für welches die ENM-Daten erstellt wurden.
	 *
	 * @return das Halbjahr
	 */
	public int getHalbjahr() {
		return this.schuljahresabschnitt.Abschnitt;
	}


	/**
	 * Gibt zurück, ob die ENM-Daten Lehrer-Spezifisch erzeugt werden oder nicht.
	 *
	 * @return true, wenn die ENM-Daten Lehrer-Spezifisch erzeugt werden, und ansonsten false
	 */
	public boolean istLehrerSpezifisch() {
		return (this.dtoLehrer != null);
	}


	/**
	 * Gibt den Lehrer zurück, für welchen die ENM-Daten erzeugt werden.
	 *
	 * @return die Lehrer-Entität oder null, falls die Daten nicht Lehrer-spezifisch erzeugt werden.
	 */
	public DTOLehrer getLehrerSpezfisch() {
		return dtoLehrer;
	}


	/**
	 * Gibt den Lehrer für die übergene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return die Lehrer-Entität
	 */
	public DTOLehrer getLehrer(final long id) {
		if (this.mapLehrer == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapLehrer.get(id);
	}

	/**
	 * Gibt die Notenmodul-Credentials für den Lehrer mit der angegebenen ID zurück.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return die Notenmodul-Credentials
	 */
	public DTONotenmodulCredentials getCredentials(final long idLehrer) {
		if (this.mapLehrerPWHash == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapLehrerPWHash.get(idLehrer);
	}

	/**
	 * Gibt den Zeitstempel für die Notenmodul-Credentials für den Lehrer mit der angegebenen ID zurück.
	 *
	 * @param idLehrer   die ID des Lehrers
	 *
	 * @return der Zeitstempel-Informationen zu den Credentials des Lehrers
	 */
	public DTOTimestampsNotenmodulCredentials getTimestampCredentials(final long idLehrer) {
		if (this.mapLehrerPWHashTimestamps == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapLehrerPWHashTimestamps.get(idLehrer);
	}


	/**
	 * Gibt das Fach für die übergene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return das Fach
	 */
	public DTOFach getFach(final long id) {
		if (this.mapFaecher == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapFaecher.get(id);
	}


	/**
	 * Gibt den Kurs für die übergene ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Kurs
	 */
	public DTOKurs getKurs(final long id) {
		if (this.mapKurse == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapKurse.get(id);
	}


	/**
	 * Gibt das Kürzel für den Förderschwerpunkt mit der angegebenen ID zurück.
	 *
	 * @param id   die ID des Förderschwerpunkts oder null für keinen
	 *
	 * @return das Statistik-Kürzel für den Förderschwerpunkt
	 */
	public String getFoerderschwerpunktKuerzel(final Long id) {
		if (id == null) {
			return null;
		}
		if (this.mapFoerderschwerpunkte == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		final var fs = this.mapFoerderschwerpunkte.get(id);
		if (fs == null) {
			return null;
		}
		return fs.StatistikKrz;
	}


	/**
	 * Gibt den Jahrgang für die angegebene ID zurück.
	 *
	 * @param id   die ID des Jahrgangs
	 *
	 * @return der Jahrgang
	 */
	public DTOJahrgang getJahrgang(final long id) {
		if (this.mapJahrgaenge == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapJahrgaenge.get(id);
	}


	/**
	 * Gibt die Teilleistungsart für die angegebene ID zurück.
	 *
	 * @param id   die ID der Teilleistungsart
	 *
	 * @return die Teilleistungsart
	 */
	public DTOTeilleistungsarten getTeilleistungsart(final long id) {
		if (this.mapTeilleistungsarten == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapTeilleistungsarten.get(id);
	}


	/**
	 * Gibt die Klasse für die angegebene ID zurück.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die Klasse
	 */
	public DTOKlassen getKlasse(final long id) {
		if (this.mapKlassen == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapKlassen.get(id);
	}


	/**
	 * Gibt die Klassenleitungen für die Klasse mit der angegebene ID zurück.
	 *
	 * @param id   die ID der Klasse
	 *
	 * @return die ID der Klassenleitungen (Lehrer)
	 */
	public List<Long> getKlassenleitungen(final long id) {
		if (this.mapKlassenLeitung == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapKlassenLeitung.get(id);
	}


	/**
	 * Gibt den Schüler für die angegebene ID zurück.
	 *
	 * @param id   die ID des Schülers
	 *
	 * @return der Schüler
	 */
	public DTOSchueler getSchueler(final long id) {
		if (this.mapSchueler == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapSchueler.get(id);
	}


	/**
	 * Gibt den Schüler-spezifischen Bemerkungen für den Lernabschnitt mit der angegebenen ID zurück.
	 *
	 * @param idLernabschnitt   die ID des Lernabschnittes
	 *
	 * @return die Bemerkungen
	 */
	public DTOSchuelerPSFachBemerkungen getBemerkungen(final long idLernabschnitt) {
		if (this.mapBemerkungen == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapBemerkungen.get(idLernabschnitt);
	}


	/**
	 * Gibt die Liste der Lernabschnitte zurück.
	 *
	 * @return die Liste der Lernabschnitte
	 */
	public Collection<DTOSchuelerLernabschnittsdaten> getLernabschnitte() {
		if (this.mapLernabschnitte == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapLernabschnitte.values();
	}


	/**
	 * Gibt die Zeitstempel für den Lernabschnitt mit der angegebenen ID zurück.
	 *
	 * @param idLernabschnitt   die ID des Lernabschnittes
	 *
	 * @return die Zeitstempel für den Lernabschnitt
	 */
	public DTOTimestampsSchuelerLernabschnittsdaten getLernabschnittTimestamps(final long idLernabschnitt) {
		if (this.mapTimestampsLernabschnitte == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapTimestampsLernabschnitte.get(idLernabschnitt);
	}


	/**
	 * Gibt die Leistungsdaten für den Lernabschnitt mit der angegebenen ID zurück.
	 *
	 * @param idLernabschnitt   die ID des Lernabschnittes
	 *
	 * @return die Leistungsdaten
	 */
	public List<DTOSchuelerLeistungsdaten> getLeistungsdaten(final long idLernabschnitt) {
		if (this.mapLeistungen == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapLeistungen.get(idLernabschnitt);
	}


	/**
	 * Gibt die Zeitstempel für die Leistungsdaten mit der angegebenen ID zurück.
	 *
	 * @param idLeistungsdaten   die ID der Leistungsdaten
	 *
	 * @return die Zeitstempel für die Leistungsdaten
	 */
	public DTOTimestampsSchuelerLeistungsdaten getLeistungsdatenTimestamps(final long idLeistungsdaten) {
		if (this.mapTimestampsLeistungsdaten == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapTimestampsLeistungsdaten.get(idLeistungsdaten);
	}


	/**
	 * Gibt die Teilleistungen für die Leistungsdaten mit der angegebenen ID zurück.
	 *
	 * @param idLeistung   die ID der Leistungsdaten
	 *
	 * @return die Teilleistungen
	 */
	public List<DTOSchuelerTeilleistung> getTeilleistungen(final long idLeistung) {
		if (this.mapTeilleistungen == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapTeilleistungen.get(idLeistung);
	}


	/**
	 * Gibt die Zeitstempel für die Teilleistung mit der angegebenen ID zurück.
	 *
	 * @param idTeilleistung   die ID der Teilleistung
	 *
	 * @return die Zeitstempel für die Teilleistung
	 */
	public DTOTimestampsSchuelerTeilleistungen getTeilleistungTimestamps(final long idTeilleistung) {
		if (this.mapTeilleistungenTimestamps == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapTeilleistungenTimestamps.get(idTeilleistung);
	}


	/**
	 * Gibt die Konfiguration für die Ankreuzkompetenzen der Schule zurück.
	 *
	 * @return die Konfiguration für die Ankreuzkompetenzen
	 */
	public Optional<DTOAnkreuzdaten> getKonfigurationAnkreuzkompetenzen() {
		return this.ankreuzkompetenzenKonfiguration;
	}


	/**
	 * Gibt die Ankreuzkompetenz für die angegebene ID zurück.
	 *
	 * @param id   die ID der Ankreuzkompetenz
	 *
	 * @return die Ankreuzkompetenz
	 */
	public DTOAnkreuzfloskeln getKatalogeintragAnkreuzkompetenz(final long id) {
		if (this.mapKatalogAnkreuzkompetenzen == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapKatalogAnkreuzkompetenzen.get(id);
	}


	/**
	 * Bestimmt die Liste der Ankreuzkompetenz-Jahrgans-Zuordnungen, die zu einem Jahrgang gehören.
	 *
	 * @param idJahrgang   die ID des Jahrgangs
	 *
	 * @return die Liste der Ankreuzkompetenz-Jahrgans-Zuordnungen
	 */
	public List<DTOAnkreuzkompetenzJahrgang> getAnkreuzkompetenzJahrgaenge(final long idJahrgang) {
		if (this.kompetenzZuordnungenByJahrgang == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.kompetenzZuordnungenByJahrgang.getOrDefault(idJahrgang, List.of());
	}


	/**
	 * Gibt die Ankreuzkompetenzen für einen Lernabschnitt mit der angegebenen ID zurück.
	 *
	 * @param idLernabschnitt   die ID des Lernabschnittes
	 *
	 * @return die Ankreuzkompetenzen
	 */
	public List<DTOSchuelerAnkreuzfloskeln> getSchuelerAnkreuzkompetenzen(final long idLernabschnitt) {
		if (this.mapAnkreuzkompetenzen == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapAnkreuzkompetenzen.get(idLernabschnitt);
	}


	/**
	 * Gibt die Zeitstempel für die Schüler-Ankreuzkompetenzen mit der angegebenen ID zurück.
	 *
	 * @param id   die ID der Schüler-Ankreuzkompetenzen
	 *
	 * @return die Zeitstempel für die Schüler-Ankreuzkompetenzen
	 */
	public DTOTimestampsSchuelerAnkreuzkompetenzen getSchuelerAnkreuzkompetenzenTimestamps(final long id) {
		if (this.mapAnkreuzkompetenzenTimestamps == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapAnkreuzkompetenzenTimestamps.get(id);
	}


	/**
	 * Gibt die Liste der Floskelgruppen zurück.
	 *
	 * @return die Liste der Floskelgruppen
	 */
	public List<DTOFloskelgruppen> getFloskelgruppen() {
		if (this.listFloskelgruppen == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.listFloskelgruppen;
	}

	/**
	 * Gibt die Liste der Floskeln zurück.
	 *
	 * @return die Liste der Floskeln
	 */
	public List<DTOFloskeln> getFloskeln() {
		if (this.listFloskelgruppen == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.listFloskeln;
	}


	/**
	 * Gibt die Liste der Jahrgangszuordnungen für die Floskel mit der angegebenen ID zurück.
	 *
	 * @param idFloskel   die ID der Floskel
	 *
	 * @return die Liste der Jahrgangs-IDs aus den Jahrgangszuordnungen - eine leere Liste, falls keine Einschränkung vorliegt
	 */
	public List<Long> getFloskelJahrgaenge(final long idFloskel) {
		if (this.mapJahrgangIdsByFloskelIds == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Der Daten-Kontext wurde nicht initialisiert.");
		}
		return this.mapJahrgangIdsByFloskelIds.getOrDefault(idFloskel, Collections.emptyList());
	}

}
