package de.svws_nrw.service.uv;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKlasseAnlageRequest;
import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvKursCreateRequest;
import de.svws_nrw.core.data.uv.UvKursunterrichtAnlageRequest;
import de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.uv.klassen.UvKlasseCreateRequest;
import de.svws_nrw.service.uv.klassen.UvKlassePatchRequest;
import de.svws_nrw.service.uv.klassen.UvKlasseService;
import de.svws_nrw.service.uv.kurse.UvKursPatchRequest;
import de.svws_nrw.service.uv.kurse.UvKursService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppeService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeSchuelerService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeService;
import jakarta.ws.rs.core.Response.Status;

/** Atomare Fachoperationen für Klassen, Kursunterricht und ihre Schülergruppen, unabhängig von Import oder GUI. */
public final class UvUnterrichtsstrukturService {

	private final UvKlasseService klassen;
	private final UvKursService kurse;
	private final UvLerngruppeService lerngruppen;
	private final UvSchuelergruppeService gruppen;
	private final UvSchuelergruppeSchuelerService mitglieder;

	/**
	 * Erstellt den Fachservice aus den vorhandenen CRUD-Services.
	 *
	 * @param klassen der Klassenservice
	 * @param kurse der Kursservice
	 * @param lerngruppen der Lerngruppenservice
	 * @param gruppen der Schülergruppenservice
	 * @param mitglieder der Service für Gruppenmitgliedschaften
	 */
	public UvUnterrichtsstrukturService(final UvKlasseService klassen, final UvKursService kurse,
			final UvLerngruppeService lerngruppen, final UvSchuelergruppeService gruppen, final UvSchuelergruppeSchuelerService mitglieder) {
		this.klassen = klassen;
		this.kurse = kurse;
		this.lerngruppen = lerngruppen;
		this.gruppen = gruppen;
		this.mitglieder = mitglieder;
	}

	/**
	 * Legt eine Klasse mit vorhandener oder neuer Schülergruppe atomar an.
	 *
	 * @param request die Anlageanfrage
	 * @return die angelegten Objekte
	 */
	public UvPlanungsabschnittsdatenBundle createKlasseMitSchuelergruppe(final UvKlasseAnlageRequest request) {
		return createKlassenMitSchuelergruppen(List.of(request));
	}

	/**
	 * Legt mehrere Klassen und ihre Schülergruppen gemeinsam an; tritt einer bestehenden Importtransaktion bei.
	 *
	 * @param requests die Anlageanfragen
	 * @return die angelegten Klassen und neuen Schülergruppen
	 */
	public UvPlanungsabschnittsdatenBundle createKlassenMitSchuelergruppen(final Collection<UvKlasseAnlageRequest> requests) {
		return transactional(() -> {
			final UvPlanungsabschnittsdatenBundle result = new UvPlanungsabschnittsdatenBundle();
			final List<UvSchuelergruppeCreateRequest> neueGruppen = new ArrayList<>();
			for (final UvKlasseAnlageRequest request : requests) {
				pruefeKlassendaten(request);
				if (request.schuelergruppe != null) {
					pruefePlan(request.klasse.idPlanungsabschnitt, request.schuelergruppe.idPlanungsabschnitt);
					neueGruppen.add(request.schuelergruppe);
				} else {
					pruefePlan(request.klasse.idPlanungsabschnitt, gruppen.get(request.klasse.idSchuelergruppe).idPlanungsabschnitt);
				}
			}
			result.schuelergruppen.addAll(gruppen.createMultiple(neueGruppen));
			// createMultiple vergibt aufsteigende IDs; die Repository-Lesereihenfolge ist hingegen nicht festgelegt.
			result.schuelergruppen.sort(Comparator.comparingLong(gruppe -> gruppe.id));
			final List<UvKlasseCreateRequest> klassenRequests = new ArrayList<>();
			int index = 0;
			for (final UvKlasseAnlageRequest request : requests) {
				final long idGruppe = request.schuelergruppe == null ? request.klasse.idSchuelergruppe : result.schuelergruppen.get(index++).id;
				klassenRequests.add(klasseRequest(request.klasse, idGruppe));
			}
			result.klassen.addAll(klassen.createMultiple(klassenRequests));
			return result;
		});
	}

	/**
	 * Legt Kurs, Lerngruppe und gegebenenfalls Schülergruppe samt zusätzlichen Mitgliedschaften atomar an.
	 *
	 * @param request die Anlageanfrage
	 * @return die angelegten Objekte und Mitgliedschaften
	 */
	public UvPlanungsabschnittsdatenBundle createKursMitLerngruppe(final UvKursunterrichtAnlageRequest request) {
		return transactional(() -> {
			pruefeKursdaten(request);
			final long idPlan = request.kurs.idPlanungsabschnitt;
			pruefePlan(idPlan, request.lerngruppe.idPlanungsabschnitt);
			pruefeKurslerngruppendaten(request);
			final UvPlanungsabschnittsdatenBundle result = new UvPlanungsabschnittsdatenBundle();
			final UvSchuelergruppe gruppe;
			if (request.schuelergruppe == null) {
				gruppe = gruppen.get(request.kurs.idSchuelergruppe);
				pruefePlan(idPlan, gruppe.idPlanungsabschnitt);
			} else {
				pruefePlan(idPlan, request.schuelergruppe.idPlanungsabschnitt);
				gruppe = gruppen.create(request.schuelergruppe);
				result.schuelergruppen.add(gruppe);
			}
			final UvKursCreateRequest kursRequest = new UvKursCreateRequest();
			kursRequest.idPlanungsabschnitt = idPlan;
			kursRequest.idSchuljahresabschnitt = request.kurs.idSchuljahresabschnitt;
			kursRequest.idFach = request.kurs.idFach;
			kursRequest.kursart = request.kurs.kursart;
			kursRequest.kursnummer = request.kurs.kursnummer;
			kursRequest.idSchuelergruppe = gruppe.id;
			final UvKurs kurs = kurse.create(kursRequest);
			result.kurse.add(kurs);
			final UvLerngruppeCreateRequest lerngruppeRequest = new UvLerngruppeCreateRequest();
			lerngruppeRequest.idPlanungsabschnitt = idPlan;
			lerngruppeRequest.idKurs = kurs.id;
			lerngruppeRequest.wochenstunden = request.lerngruppe.wochenstunden;
			lerngruppeRequest.wochenstundenUnterrichtet = request.lerngruppe.wochenstundenUnterrichtet;
			lerngruppeRequest.koopSchulNr = request.lerngruppe.koopSchulNr;
			lerngruppeRequest.koopAnzahlExterne = request.lerngruppe.koopAnzahlExterne;
			result.lerngruppen.add(lerngruppen.create(lerngruppeRequest));
			if (!request.idsSchueler.isEmpty()) {
				ergaenzeSchuelergruppenmitglieder(result, idPlan, gruppe.id, request.schuelergruppe == null, request.idsSchueler);
			}
			return result;
		});
	}

	/**
	 * Legt eine neue Schülergruppe an und ordnet sie atomar einer vorhandenen Klasse zu.
	 *
	 * @param idKlasse die Klassen-ID
	 * @param request die neue Schülergruppe
	 * @return die neue Schülergruppe und die geänderte Klasse
	 */
	public UvPlanungsabschnittsdatenBundle createSchuelergruppeFuerKlasse(final long idKlasse, final UvSchuelergruppeCreateRequest request) {
		return transactional(() -> {
			final UvKlasse klasse = klassen.get(idKlasse);
			pruefePlan(klasse.idPlanungsabschnitt, request.idPlanungsabschnitt);
			final UvSchuelergruppe gruppe = gruppen.create(request);
			final UvKlassePatchRequest patch = new UvKlassePatchRequest();
			patch.id = idKlasse;
			patch.idSchuelergruppe = JsonNullable.of(gruppe.id);
			final UvPlanungsabschnittsdatenBundle result = new UvPlanungsabschnittsdatenBundle();
			result.schuelergruppen.add(gruppe);
			result.klassen.add(klassen.patch(patch));
			return result;
		});
	}

	/**
	 * Legt eine neue Schülergruppe an und ordnet sie atomar einem vorhandenen Kurs zu.
	 *
	 * @param idKurs die Kurs-ID
	 * @param request die neue Schülergruppe
	 * @return die neue Schülergruppe und der geänderte Kurs
	 */
	public UvPlanungsabschnittsdatenBundle createSchuelergruppeFuerKurs(final long idKurs, final UvSchuelergruppeCreateRequest request) {
		return transactional(() -> {
			final UvKurs kurs = kurse.get(idKurs);
			pruefePlan(kurs.idPlanungsabschnitt, request.idPlanungsabschnitt);
			final UvSchuelergruppe gruppe = gruppen.create(request);
			final UvKursPatchRequest patch = new UvKursPatchRequest();
			patch.id = idKurs;
			patch.idSchuelergruppe = JsonNullable.of(gruppe.id);
			final UvPlanungsabschnittsdatenBundle result = new UvPlanungsabschnittsdatenBundle();
			result.schuelergruppen.add(gruppe);
			result.kurse.add(kurse.patch(patch));
			return result;
		});
	}

	/* Ergänzt nur Schüler, die der Zielgruppe noch nicht angehören. */
	private void ergaenzeSchuelergruppenmitglieder(final UvPlanungsabschnittsdatenBundle result, final long idPlan, final long idGruppe,
			final boolean bestehendeGruppe, final Iterable<Long> idsSchueler) {
		final Set<Long> vorhanden = new HashSet<>();
		if (bestehendeGruppe) {
			mitglieder.getListByPlanungsabschnitt(idPlan).stream().filter(eintrag -> eintrag.idSchuelergruppe == idGruppe)
					.forEach(eintrag -> vorhanden.add(eintrag.idSchueler));
		}
		final List<UvSchuelergruppeSchuelerCreateRequest> zuordnungen = new ArrayList<>();
		for (final Long idSchueler : idsSchueler) {
			if ((idSchueler == null) || (idSchueler <= 0)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Schüler-IDs sind ungültig.");
			}
			if (vorhanden.add(idSchueler)) {
				final UvSchuelergruppeSchuelerCreateRequest zuordnung = new UvSchuelergruppeSchuelerCreateRequest();
				zuordnung.idPlanungsabschnitt = idPlan;
				zuordnung.idSchuelergruppe = idGruppe;
				zuordnung.idSchueler = idSchueler;
				zuordnungen.add(zuordnung);
			}
		}
		result.schuelergruppenschueler.addAll(mitglieder.createMultiple(zuordnungen));
	}

	private static void pruefeKlassendaten(final UvKlasseAnlageRequest request) {
		if ((request.klasse == null) || (request.klasse.kuerzel == null) || request.klasse.kuerzel.isBlank() || (request.klasse.kuerzel.length() > 15)
				|| ((request.klasse.parallelitaet == null) || (request.klasse.parallelitaet.length() > 2)) || (request.klasse.idSchuljahresabschnitt <= 0)
				|| ((request.klasse.bezeichnung != null) && (request.klasse.bezeichnung.length() > 150))) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Klassendaten sind unvollständig oder ungültig.");
		}
	}

	private static void pruefeKursdaten(final UvKursunterrichtAnlageRequest request) {
		if ((request.kurs == null) || (request.lerngruppe == null) || (request.idsSchueler == null)
				|| (request.kurs.idPlanungsabschnitt == null) || (request.kurs.idSchuljahresabschnitt == null)
				|| (request.kurs.idFach == null) || (request.kurs.kursart == null) || request.kurs.kursart.isBlank()
				|| (request.kurs.kursnummer == null) || (request.kurs.kursnummer < 1)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Kursdaten sind unvollständig oder ungültig.");
		}
	}

	private static void pruefeKurslerngruppendaten(final UvKursunterrichtAnlageRequest request) {
		if ((request.lerngruppe.idKlasse != null) || (request.lerngruppe.idFach != null)
				|| (request.lerngruppe.wochenstunden == null) || !Double.isFinite(request.lerngruppe.wochenstunden)
				|| (request.lerngruppe.wochenstunden < 0) || (request.lerngruppe.wochenstundenUnterrichtet == null)
				|| !Double.isFinite(request.lerngruppe.wochenstundenUnterrichtet) || (request.lerngruppe.wochenstundenUnterrichtet < 0)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten der Kurslerngruppe sind ungültig.");
		}
	}

	private static void pruefePlan(final long erwartet, final Long tatsaechlich) {
		if ((erwartet <= 0) || (tatsaechlich == null) || (erwartet != tatsaechlich)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Alle Objekte müssen zum selben Planungsabschnitt gehören.");
		}
	}

	private static UvKlasseCreateRequest klasseRequest(final UvKlasse klasse, final long idGruppe) {
		final UvKlasseCreateRequest request = new UvKlasseCreateRequest();
		request.idPlanungsabschnitt = klasse.idPlanungsabschnitt;
		request.idSchuljahresabschnitt = klasse.idSchuljahresabschnitt;
		request.kuerzel = klasse.kuerzel;
		request.bezeichnung = klasse.bezeichnung;
		request.parallelitaet = klasse.parallelitaet;
		request.idSchuelergruppe = idGruppe;
		request.idStundentafel = klasse.idStundentafel;
		request.orgFormKrz = klasse.orgFormKrz;
		request.idFachklasse = klasse.idFachklasse;
		request.asdSchulformNr = klasse.asdSchulformNr;
		return request;
	}
}
