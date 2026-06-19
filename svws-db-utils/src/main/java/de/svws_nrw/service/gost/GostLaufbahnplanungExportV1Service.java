package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBeratungslehrer;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Beratungslehrer;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Fach;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Fachbelegung;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Fachkombination;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Schueler;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Sprachbelegung;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Sprachpruefung;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerRepository;
import de.svws_nrw.repo.gost.GostJahrgangFachkombinationenRepository;
import de.svws_nrw.repo.gost.GostJahrgangsdatenRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.service.crypto.SchuelerCredentialsService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Export von Laufbahnplanungsdaten in der Version 1
 */
public class GostLaufbahnplanungExportV1Service {

	private final BenutzerRepository benutzerRepository;
	private final SchuelerRepository schuelerRepository;
	private final GostJahrgangFachkombinationenRepository gostJahrgangFachkombinationenRepository;
	private final GostJahrgangsdatenRepository gostJahrgangsdatenRepository;

	private final SchuelerCredentialsService schuelerCredentialsService;
	private final GostAbiturdatenService gostAbiturdatenService;
	private final GostFaecherService gostFaecherService;
	private final GostBeratungslehrerService gostBeratungslehrerService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                        das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param schuelerRepository                        das Repository für den Zugriff auf die Schülerdaten
	 * @param gostJahrgangFachkombinationenRepository   das Repository für den Zugriff auf die Fachkombinationen der Abiturjahrgänge der gymnasialen Oberstufe
	 * @param gostJahrgangsdatenRepository              das Repository für den Zugriff auf die allgemeinen Daten der Abiturjahrgänge der gymnasialen Oberstufe
	 * @param schuelerCredentialsService                das Repository für den Zugriff auf die Schüler-Credentials
	 * @param gostAbiturdatenService                    der Service für den Zugriff auf die Abiturdaten der gymnasialen Oberstufe
	 * @param gostFaecherService                        der Service für den Zugruff auf die Fächerdaten eines Abiturjahrganges der gymnasialen Oberstufe
	 * @param gostBeratungslehrerService                der Service für den Zugruff auf die Beratungslehrer eines Abiturjahrganges der gymnasialen Oberstufe
	 */
	public GostLaufbahnplanungExportV1Service(final BenutzerRepository benutzerRepository,
			final SchuelerRepository schuelerRepository,
			final GostJahrgangFachkombinationenRepository gostJahrgangFachkombinationenRepository,
			final GostJahrgangsdatenRepository gostJahrgangsdatenRepository,
			final SchuelerCredentialsService schuelerCredentialsService,
			final GostAbiturdatenService gostAbiturdatenService,
			final GostFaecherService gostFaecherService,
			final GostBeratungslehrerService gostBeratungslehrerService) {
		this.benutzerRepository = benutzerRepository;
		this.schuelerCredentialsService = schuelerCredentialsService;
		this.schuelerRepository = schuelerRepository;
		this.gostJahrgangFachkombinationenRepository = gostJahrgangFachkombinationenRepository;
		this.gostJahrgangsdatenRepository = gostJahrgangsdatenRepository;
		this.gostAbiturdatenService = gostAbiturdatenService;
		this.gostFaecherService = gostFaecherService;
		this.gostBeratungslehrerService = gostBeratungslehrerService;
	}


	/**
	 * Erstellt das Export-Objekt mit den Laufbahnplanungsdaten des angegebenen Schülers.
	 *
	 * @param idSchueler   die ID des Schülers
	 *
	 * @return das Laufbahnplanungsdaten-Objekt
	 */
	public GostLaufbahnplanungExportV1 get(final long idSchueler) {
		final var list = getList(List.of(idSchueler));
		if (list.isEmpty()) {
			throw new ApiOperationException(Response.Status.NOT_FOUND,
					"Es wurden keine Daten für einen Schüler mit der ID %d gefunden.".formatted(idSchueler));
		}
		return list.getFirst();
	}

	/**
	 * Erstellt die Export-Objekte mit den Laufbahnplanungsdaten der angegebenen Schüler.
	 *
	 * @param idsSchueler   die IDs der Schüler
	 *
	 * @return die Laufbahnplanungsdaten-Objekte
	 */
	public List<GostLaufbahnplanungExportV1> getList(final Collection<Long> idsSchueler) {
		return transactional(() -> {
			// Bestimme die Daten aus der Datenbank
			final SchuleStammdaten schule = benutzerRepository.getAktuellerBenutzer().schuleGetStammdaten();

			final List<Abiturdaten> listAbiturdaten = gostAbiturdatenService.getList(idsSchueler);
			final List<Integer> abiturjahrgaenge = listAbiturdaten.stream().map(a -> a.abiturjahr).filter(a -> a != null).distinct().toList();
			final Map<Integer, GostFaecherManager> mapGostFaecher = gostFaecherService.getMapGostFaecherManager(abiturjahrgaenge, false);
			final Map<Integer, List<DTOGostJahrgangFachkombinationen>> mapFachkombinationen = gostJahrgangFachkombinationenRepository.getMapByAbiturjahrgaenge(abiturjahrgaenge);
			final Map<Integer, DTOGostJahrgangsdaten> mapJahrgangsdaten = gostJahrgangsdatenRepository.findMapByIds(abiturjahrgaenge);
			final Map<Integer, List<GostBeratungslehrer>> mapBeratungslehrer = gostBeratungslehrerService.getMap(abiturjahrgaenge);
			final Map<Long, DTOSchueler> mapSchueler = schuelerRepository.findMapByIds(idsSchueler);
			final Map<Long, AES> mapAES = schuelerCredentialsService.getOrCreateMap(idsSchueler);

			// Schreibe die Daten in die Export-DTOs
			final List<GostLaufbahnplanungExportV1> result = new ArrayList<>();
			for (final Abiturdaten abidaten : listAbiturdaten) {
				final DTOGostJahrgangsdaten jahrgangsdaten = mapJahrgangsdaten.get(abidaten.abiturjahr);
				if (jahrgangsdaten == null) {
					throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Jahrgangsdaten für den Abiturjahrgang gefunden.");
				}
				final List<GostBeratungslehrer> listBeratungslehrer = mapBeratungslehrer.getOrDefault(abidaten.abiturjahr, new ArrayList<>());
				final GostFaecherManager gostFaecher = mapGostFaecher.get(abidaten.abiturjahr);
				final List<DTOGostJahrgangFachkombinationen> kombis = mapFachkombinationen.getOrDefault(abidaten.abiturjahr, new ArrayList<>());
				final DTOSchueler schueler = mapSchueler.get(abidaten.schuelerID);
				final AES aes = mapAES.get(abidaten.schuelerID);
				result.add(getExportInternal(abidaten, schule, jahrgangsdaten, listBeratungslehrer, gostFaecher, kombis, schueler, aes));
			}
			return result;
		});
	}


	private static GostLaufbahnplanungExportV1 getExportInternal(final Abiturdaten abidaten, final SchuleStammdaten schule, final DTOGostJahrgangsdaten jahrgangsdaten,
			final List<GostBeratungslehrer> listBeratungslehrer, final GostFaecherManager gostFaecher, final List<DTOGostJahrgangFachkombinationen> kombis,
			final DTOSchueler schueler, final AES aes) {
		final GostLaufbahnplanungExportV1 daten = new GostLaufbahnplanungExportV1();
		daten.schulNr = schule.schulNr;
		daten.schulBezeichnung1 = (schule.bezeichnung1 == null) ? "" : schule.bezeichnung1;
		daten.schulBezeichnung2 = (schule.bezeichnung2 == null) ? "" : schule.bezeichnung2;
		daten.schulBezeichnung3 = (schule.bezeichnung3 == null) ? "" : schule.bezeichnung3;
		daten.anmerkungen = "Exportiert am " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		daten.abiturjahr = abidaten.abiturjahr;
		daten.jahrgang = abidaten.jahrgang;
		daten.textBeratungsbogen = jahrgangsdaten.TextBeratungsbogen;
		daten.hatZusatzkursGE = jahrgangsdaten.ZusatzkursGEVorhanden;
		daten.beginnZusatzkursGE = jahrgangsdaten.ZusatzkursGEErstesHalbjahr;
		daten.hatZusatzkursSW = jahrgangsdaten.ZusatzkursSWVorhanden;
		daten.beginnZusatzkursSW = jahrgangsdaten.ZusatzkursSWErstesHalbjahr;
		for (final GostBeratungslehrer beratungslehrer : listBeratungslehrer) {
			final GostLaufbahnplanungExportV1Beratungslehrer exportBeratungslehrer = new GostLaufbahnplanungExportV1Beratungslehrer();
			exportBeratungslehrer.id = beratungslehrer.id;
			exportBeratungslehrer.kuerzel = beratungslehrer.kuerzel;
			exportBeratungslehrer.nachname = beratungslehrer.nachname;
			exportBeratungslehrer.vorname = beratungslehrer.vorname;
			daten.beratungslehrer.add(exportBeratungslehrer);
		}
		for (final GostFach fach : gostFaecher.faecher()) {
			final GostLaufbahnplanungExportV1Fach exportFach = new GostLaufbahnplanungExportV1Fach();
			exportFach.id = fach.id;
			exportFach.kuerzel = fach.kuerzel;
			exportFach.kuerzelAnzeige = fach.kuerzelAnzeige;
			exportFach.bezeichnung = fach.bezeichnung;
			exportFach.sortierung = fach.sortierung;
			exportFach.istPruefungsordnungsRelevant = fach.istPruefungsordnungsRelevant;
			exportFach.istFremdsprache = fach.istFremdsprache;
			exportFach.istFremdSpracheNeuEinsetzend = fach.istFremdSpracheNeuEinsetzend;
			exportFach.biliSprache = fach.biliSprache;
			exportFach.istMoeglichAbiLK = fach.istMoeglichAbiLK;
			exportFach.istMoeglichAbiGK = fach.istMoeglichAbiGK;
			exportFach.istMoeglichEF1 = fach.istMoeglichEF1;
			exportFach.istMoeglichEF2 = fach.istMoeglichEF2;
			exportFach.istMoeglichQ11 = fach.istMoeglichQ11;
			exportFach.istMoeglichQ12 = fach.istMoeglichQ12;
			exportFach.istMoeglichQ21 = fach.istMoeglichQ21;
			exportFach.istMoeglichQ22 = fach.istMoeglichQ22;
			exportFach.wochenstundenQualifikationsphase = 3;
			exportFach.projektKursLeitfach1ID = null;
			exportFach.projektKursLeitfach1Kuerzel = null;
			exportFach.projektKursLeitfach2ID = null;
			exportFach.projektKursLeitfach2Kuerzel = null;
			daten.faecher.add(exportFach);
		}
		for (final DTOGostJahrgangFachkombinationen kombi : kombis) {
			final GostLaufbahnplanungExportV1Fachkombination exportKombi = new GostLaufbahnplanungExportV1Fachkombination();
			exportKombi.id = kombi.ID;
			exportKombi.abiturjahr = kombi.Abi_Jahrgang;
			exportKombi.fachID1 = kombi.Fach1_ID;
			exportKombi.kursart1 = kombi.Kursart1;
			exportKombi.fachID2 = kombi.Fach2_ID;
			exportKombi.kursart2 = kombi.Kursart2;
			exportKombi.gueltigInHalbjahr[0] = kombi.EF1;
			exportKombi.gueltigInHalbjahr[1] = kombi.EF2;
			exportKombi.gueltigInHalbjahr[2] = kombi.Q11;
			exportKombi.gueltigInHalbjahr[3] = kombi.Q12;
			exportKombi.gueltigInHalbjahr[4] = kombi.Q21;
			exportKombi.gueltigInHalbjahr[5] = kombi.Q22;
			exportKombi.typ = kombi.Typ.getValue();
			exportKombi.hinweistext = kombi.Hinweistext;
			daten.fachkombinationen.add(exportKombi);
		}
		final GostLaufbahnplanungExportV1Schueler schuelerDaten = new GostLaufbahnplanungExportV1Schueler();
		schuelerDaten.id = schueler.ID;
		try {
			schuelerDaten.idEnc = aes.encryptBase64(ByteBuffer.wrap(new byte[8]).putLong(schuelerDaten.id).array());
		} catch (final AESException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e);
		}
		schuelerDaten.vorname = schueler.Vorname;
		schuelerDaten.nachname = schueler.Nachname;
		schuelerDaten.geschlecht = schueler.Geschlecht.kuerzel;
		schuelerDaten.bilingualeSprache = abidaten.bilingualeSprache;
		for (int i = 0; i < GostHalbjahr.maxHalbjahre; i++) {
			System.arraycopy(abidaten.bewertetesHalbjahr, 0, schuelerDaten.bewertetesHalbjahr, 0, GostHalbjahr.maxHalbjahre);
		}
		for (final AbiturFachbelegung fbel : abidaten.fachbelegungen) {
			final GostLaufbahnplanungExportV1Fachbelegung fb = new GostLaufbahnplanungExportV1Fachbelegung();
			fb.fachID = fbel.fachID;
			fb.abiturFach = fbel.abiturFach;
			for (int i = 0; i < GostHalbjahr.maxHalbjahre; i++) {
				final String strKursart = (fbel.belegungen[i] == null) ? null : fbel.belegungen[i].kursartKuerzel;
				final GostKursart kursart = (strKursart == null) ? null : GostKursart.fromKuerzel(strKursart);
				fb.kursart[i] = (kursart == null) ? null : kursart.kuerzel;
				fb.schriftlich[i] = (kursart != null) && fbel.belegungen[i].schriftlich;
				if ("AT".equals(strKursart)) {
					fb.kursart[i] = "AT";
				}
			}
			schuelerDaten.fachbelegungen.add(fb);
		}
		for (final Sprachbelegung bel : abidaten.sprachendaten.belegungen) {
			final GostLaufbahnplanungExportV1Sprachbelegung exportBel = new GostLaufbahnplanungExportV1Sprachbelegung();
			exportBel.sprache = bel.sprache;
			exportBel.istNachweis = bel.istNachweis;
			exportBel.reihenfolge = bel.reihenfolge;
			exportBel.belegungVonJahrgang = bel.belegungVonJahrgang;
			exportBel.belegungVonAbschnitt = bel.belegungVonAbschnitt;
			exportBel.belegungBisJahrgang = bel.belegungBisJahrgang;
			exportBel.belegungBisAbschnitt = bel.belegungBisAbschnitt;
			exportBel.referenzniveau = bel.referenzniveau;
			exportBel.hatKleinesLatinum = bel.hatKleinesLatinum;
			exportBel.hatLatinum = bel.hatLatinum;
			exportBel.hatGraecum = bel.hatGraecum;
			exportBel.hatHebraicum = bel.hatHebraicum;
			schuelerDaten.sprachendaten.belegungen.add(exportBel);
		}
		for (final Sprachpruefung pruef : abidaten.sprachendaten.pruefungen) {
			final GostLaufbahnplanungExportV1Sprachpruefung exportPruef = new GostLaufbahnplanungExportV1Sprachpruefung();
			exportPruef.sprache = pruef.sprache;
			exportPruef.jahrgang = pruef.jahrgang;
			exportPruef.anspruchsniveauId = pruef.anspruchsniveauId;
			exportPruef.pruefungsdatum = pruef.pruefungsdatum;
			exportPruef.ersetzteSprache = pruef.ersetzteSprache;
			exportPruef.istHSUPruefung = pruef.istHSUPruefung;
			exportPruef.istFeststellungspruefung = pruef.istFeststellungspruefung;
			exportPruef.kannErstePflichtfremdspracheErsetzen = pruef.kannErstePflichtfremdspracheErsetzen;
			exportPruef.kannZweitePflichtfremdspracheErsetzen = pruef.kannZweitePflichtfremdspracheErsetzen;
			exportPruef.kannWahlpflichtfremdspracheErsetzen = pruef.kannWahlpflichtfremdspracheErsetzen;
			exportPruef.kannBelegungAlsFortgefuehrteSpracheErlauben = pruef.kannBelegungAlsFortgefuehrteSpracheErlauben;
			exportPruef.referenzniveau = pruef.referenzniveau;
			exportPruef.note = pruef.note;
			exportPruef.zeugnisbezeichnung = pruef.zeugnisbezeichnung;
			schuelerDaten.sprachendaten.pruefungen.add(exportPruef);
		}
		daten.schueler.add(schuelerDaten);
		return daten;
	}

}
