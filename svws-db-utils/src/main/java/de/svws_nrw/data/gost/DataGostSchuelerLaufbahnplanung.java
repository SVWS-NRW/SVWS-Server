package de.svws_nrw.data.gost;

import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostBelegpruefungsErgebnisse;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.gost.GostLaufbahnplanungGKLKlausurvorgabe;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.gost.GostServiceFactoryBuilder;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactoryBuilder;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link Abiturdaten}.
 */
public final class DataGostSchuelerLaufbahnplanung {

	/** Die Datenbank-Verbindung */
	private final DBEntityManager conn;

	/** Das Abitur, sofern sich die Anfrage auf ein konkretes Abiturjahr bezieht. Ansonsten null */
	private final Integer abiturjahr;


	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Abiturdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr   das Abiturjahr, auf welches sich die Anfrage bezieht oder null, wenn kein konkretes angegeben wurde
	 */
	public DataGostSchuelerLaufbahnplanung(final DBEntityManager conn, final Integer abiturjahr) {
		this.conn = conn;
		this.abiturjahr = abiturjahr;
	}

	/**
	 * Führt eine Belegprüfung für alles Schüler des angebenen Abitur-Jahrgangs durch
	 * und gibt die Belegprüfungsergebnisse für die Schüler zurück.
	 *
	 * @param pruefungsArt   die Art der Belegprüfung
	 *
	 * @return die Belegprüfungsergebnisse
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response pruefeBelegungAbiturjahrgang(final GostBelegpruefungsArt pruefungsArt) throws ApiOperationException {
		if (abiturjahr == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Es muss ein gültiges Abiturjahr angegeben werden. Der Wert null ist nicht zulässig.");
		}
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat und ob der Schüler überhaupt existiert.
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final int schuljahr = abiturjahr - 1;
		final List<DTOSchueler> listSchuelerDTOs = (new DataGostJahrgangSchuelerliste(conn, abiturjahr)).getSchuelerDTOs();
		final List<Long> listSchuelerIDs = listSchuelerDTOs.stream().map(s -> s.ID).toList();
		final Map<Long, DTOGostSchueler> mapGostSchuelerDTOs = conn.queryByKeyList(DTOGostSchueler.class, listSchuelerIDs)
				.stream().collect(Collectors.toMap(s -> s.Schueler_ID, s -> s));

		final Map<Long, Abiturdaten> mapAbiturdaten = GostServiceFactoryBuilder.getGostServiceFactory().getGostAbiturdatenService().getMapByAbiturjahrgang(abiturjahr);

		// Erstelle das DTO für die Eregbnisrückmeldung
		final List<GostBelegpruefungsErgebnisse> daten = new ArrayList<>();

		// Bestimme die Jahrgangsdaten des Abiturjahrgangs
		final @NotNull GostJahrgangsdaten jahrgangsdaten = DataGostJahrgangsdaten.getJahrgangsdaten(conn, abiturjahr);

		// Bestimme die Fächer, welche in dem Abiturjahrgang vorhanden sind.
		final @NotNull GostFaecherManager faecherManager = DBUtilsFaecherGost.getFaecherManager(schuljahr, conn, abiturjahr);

		// Bestimme die nicht erlaubten und die geforderten Fächerkombinationen des Abiturjahrgangs
		faecherManager.addFachkombinationenAll(DataGostJahrgangFachkombinationen.getFachkombinationen(conn, abiturjahr));

		// Bestimme ab dem Abiturjahrgang 2030 die GKL-Wahlen der Schüler und die Klausurvorgaben des Jahrgangs für die GKL-Prüfung (nur bei der Gesamtprüfung)
		final boolean pruefeGKL = AbiturdatenManager.istAbitur2030(abiturjahr) && (pruefungsArt == GostBelegpruefungsArt.GESAMT);
		final Map<Long, GostSchuelerGKLWahl> mapGKLWahlen = pruefeGKL
				? GostServiceFactoryBuilder.getGostServiceFactory().getGostSchuelerService().getMapGKLWahlen(listSchuelerIDs, abiturjahr)
				: Map.of();
		final Map<Long, GostLaufbahnplanungGKLKlausurvorgabe> mapKlausurvorgaben = pruefeGKL
				? getMapKlausurvorgaben(faecherManager)
				: Map.of();

		// Führe für alle Schüler nacheinander die Belegprüfung durch
		for (final DTOSchueler dtoSchueler : listSchuelerDTOs) {
			final SchuelerStatus status = SchuelerStatus.data().getWertByID(dtoSchueler.idStatus == null ? null : dtoSchueler.idStatus.longValue());
			if ((status != SchuelerStatus.AKTIV) && (status != SchuelerStatus.EXTERN) && (status != SchuelerStatus.NEUAUFNAHME)
					&& (status != SchuelerStatus.WARTELISTE)) {
				continue;
			}

			// Bestimme die Laufbahndaten des Schülers
			final Abiturdaten abidaten = mapAbiturdaten.get(dtoSchueler.ID);

			// Bestimme die Beratungsdaten des Schülers
			final DTOGostSchueler gostSchueler = mapGostSchuelerDTOs.get(dtoSchueler.ID);

			// Erzeuge das Ergebnis-DTO für die Rückgabe
			final GostBelegpruefungsErgebnisse ergebnisse = new GostBelegpruefungsErgebnisse();

			// Führe die Belegprüfung für den Schüler durch
			final AbiturdatenManager abiManager =
					new AbiturdatenManager(abidaten, jahrgangsdaten, faecherManager, pruefungsArt);
			if (pruefeGKL) {
				final GostSchuelerGKLWahl gklWahl = mapGKLWahlen.get(dtoSchueler.ID);
				abiManager.pruefeGKL((gklWahl == null) ? new GostSchuelerGKLWahl() : gklWahl, mapKlausurvorgaben);
			}
			ergebnisse.ergebnis = abiManager.getBelegpruefungErgebnis();
			ergebnisse.hatFachwahlen = abiManager.existsFachbelegung();
			ergebnisse.beratungsDatum = (gostSchueler == null) ? null : gostSchueler.DatumBeratung;
			ergebnisse.ruecklaufDatum = (gostSchueler == null) ? null : gostSchueler.DatumRuecklauf;

			// F+lle das zugehörige Schüler-DTO
			ergebnisse.schueler.id = dtoSchueler.ID;
			ergebnisse.schueler.vorname = dtoSchueler.Vorname;
			ergebnisse.schueler.nachname = dtoSchueler.Nachname;
			ergebnisse.schueler.status = dtoSchueler.idStatus;
			ergebnisse.schueler.geschlecht = dtoSchueler.Geschlecht.id;

			// Schreibe das Ergebnis in die Rückmeldung
			daten.add(ergebnisse);
		}

		daten.sort((a, b) -> {
			final Collator collator = Collator.getInstance(Locale.GERMAN);
			if ((a.schueler.nachname == null) && (b.schueler.nachname != null)) {
				return -1;
			} else if ((a.schueler.nachname != null) && (b.schueler.nachname == null)) {
				return 1;
			} else if ((a.schueler.nachname == null) && (b.schueler.nachname == null)) {
				return 0;
			}
			int result = collator.compare(a.schueler.nachname, b.schueler.nachname);
			if (result == 0) {
				if ((a.schueler.vorname == null) && (b.schueler.vorname != null)) {
					return -1;
				} else if ((a.schueler.vorname != null) && (b.schueler.vorname == null)) {
					return 1;
				} else if ((a.schueler.vorname == null) && (b.schueler.vorname == null)) {
					return 0;
				}
				result = collator.compare(a.schueler.vorname, b.schueler.vorname);
			}
			return result;
		});

		// Erzeuge die Response mit den Belegprüfungsergebnissen
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Bestimmt die Klausurvorgaben des Abiturjahrgangs für die GKL-Prüfung. Vorgaben zu Fächern, die der Fächer-Manager
	 * nicht kennt, entfallen, wie bei der Laufbahnplanung im Client.
	 *
	 * @param faecherManager   der Fächer-Manager des Abiturjahrgangs
	 *
	 * @return die Map mit den Klausurvorgaben zu ihrer ID
	 */
	private Map<Long, GostLaufbahnplanungGKLKlausurvorgabe> getMapKlausurvorgaben(final @NotNull GostFaecherManager faecherManager) {
		final Map<Long, GostLaufbahnplanungGKLKlausurvorgabe> result = new HashMap<>();
		final List<GostKlausurvorgabe> vorgaben = GostKlausurenServiceFactoryBuilder.getGostKlausurenServiceFactory()
				.getGostKlausurenVorgabeService().getListByAbiturjahr(abiturjahr, -1, false);
		for (final GostKlausurvorgabe vorgabe : vorgaben) {
			final GostFach fach = faecherManager.get(vorgabe.idFach);
			final GostHalbjahr halbjahr = GostHalbjahr.fromID(vorgabe.halbjahr);
			if ((fach != null) && (halbjahr != null)) {
				result.put(vorgabe.id, new GostLaufbahnplanungGKLKlausurvorgabe(fach, halbjahr, vorgabe));
			}
		}
		return result;
	}

}
