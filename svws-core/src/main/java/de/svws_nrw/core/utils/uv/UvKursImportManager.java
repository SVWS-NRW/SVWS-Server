package de.svws_nrw.core.utils.uv;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvKursCreateRequest;
import de.svws_nrw.core.data.uv.UvKursImportDaten;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest;
import de.svws_nrw.core.data.uv.UvLerngruppenSchieneCreateRequest;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.core.data.uv.UvSchieneCreateRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest;
import de.svws_nrw.core.types.gost.GostKursart;
import jakarta.validation.constraints.NotNull;

/**
 * Erstellt aus GOSt-Blockungen oder Kursdaten einen einheitlichen Kursimport für
 * einen UV-Planungsabschnitt. Die Klasse enthält keine Server- oder UI-Abhängigkeiten.
 */
public final class UvKursImportManager {

	private final @NotNull Comparator<JahrgangsDaten> compJahrgangsdaten = (final @NotNull JahrgangsDaten a, final @NotNull JahrgangsDaten b) -> {
		final int cmp = Integer.compare(a.sortierung, b.sortierung);
		return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
	};

	private final @NotNull UvPlanungsabschnitt planungsabschnitt;
	private final @NotNull UvManager uvManager;
	private final long idSchuljahresabschnitt;
	private final @NotNull List<UvKursImportQuellkurs> kurse = new ArrayList<>();
	private final @NotNull Map<Long, String> schienenBezeichnungById = new HashMap<>();

	private final @NotNull Set<String> fehlendeFaecher = new HashSet<>();
	private final @NotNull Set<String> fehlendeLehrer = new HashSet<>();
	private final @NotNull Set<String> fehlendeSchueler = new HashSet<>();
	private long nextTemporaereId;
	private int nextSchienenNummer;

	/**
	 * Erstellt einen Importmanager für eine GOSt-Blockung.
	 *
	 * @param blockung der Quellblockung
	 * @param ergebnis das zu importierende Blockungsergebnis
	 * @param planungsabschnitt der Zielplanungsabschnitt
	 * @param uvManager die Daten des Zielplanungsabschnitts
	 * @param idSchuljahresabschnitt die ID des Schuljahresabschnitts der anzulegenden Kurse
	 */
	public UvKursImportManager(final @NotNull GostBlockungsdaten blockung, final @NotNull GostBlockungsergebnis ergebnis,
			final @NotNull UvPlanungsabschnitt planungsabschnitt, final @NotNull UvManager uvManager, final long idSchuljahresabschnitt) {
		this.planungsabschnitt = planungsabschnitt;
		this.uvManager = uvManager;
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		for (final @NotNull GostBlockungSchiene schiene : blockung.schienen) {
			schienenBezeichnungById.put(schiene.id, schiene.bezeichnung);
		}
		final @NotNull Map<Long, GostBlockungsergebnisKurs> ergebnisKurse = new HashMap<>();
		for (final @NotNull GostBlockungsergebnisSchiene schiene : ergebnis.schienen) {
			for (final @NotNull GostBlockungsergebnisKurs kurs : schiene.kurse) {
				ergebnisKurse.put(kurs.id, kurs);
			}
		}
		for (final @NotNull GostBlockungKurs kurs : blockung.kurse) {
			final GostBlockungsergebnisKurs ergebnisKurs = ergebnisKurse.get(kurs.id);
			if (ergebnisKurs != null) {
				addGostKurs(kurs, ergebnisKurs);
			}
		}
		analyse();
	}

	/**
	 * Erstellt einen Importmanager für persistierte Kurse eines Schuljahresabschnitts.
	 *
	 * @param kurse die Quellkurse
	 * @param planungsabschnitt der Zielplanungsabschnitt
	 * @param uvManager die Daten des Zielplanungsabschnitts
	 */
	public UvKursImportManager(final @NotNull List<KursDaten> kurse, final @NotNull UvPlanungsabschnitt planungsabschnitt,
			final @NotNull UvManager uvManager) {
		this.planungsabschnitt = planungsabschnitt;
		this.uvManager = uvManager;
		this.idSchuljahresabschnitt = kurse.isEmpty() ? -1 : kurse.getFirst().idSchuljahresabschnitt;
		for (final @NotNull KursDaten kurs : kurse) {
			final @NotNull UvKursImportQuellkurs quellkurs = new UvKursImportQuellkurs(kurs.id, kurs.idFach, kurs.kursartAllg,
					getKursnummer(kurs.kuerzel), kurs.wochenstunden);
			for (final @NotNull Schueler schueler : kurs.schueler) {
				quellkurs.schuelerIds.add(schueler.id);
			}
			if (kurs.lehrer != null) {
				quellkurs.lehrerIds.add(kurs.lehrer);
			}
			for (final @NotNull KursLehrer lehrer : kurs.weitereLehrer) {
				quellkurs.lehrerIds.add(lehrer.idLehrer);
			}
			for (final @NotNull Integer schiene : kurs.schienen) {
				quellkurs.schienenIds.add(schiene.longValue());
				schienenBezeichnungById.put(schiene.longValue(), "Schiene " + schiene);
			}
			this.kurse.add(quellkurs);
		}
		analyse();
	}

	/** @return die fehlenden Fächer als lesbare Liste. */
	public @NotNull List<String> getFehlendeFaecher() {
		return new ArrayList<>(fehlendeFaecher);
	}

	/** @return die fehlenden Lehrer als lesbare Liste. */
	public @NotNull List<String> getFehlendeLehrer() {
		return new ArrayList<>(fehlendeLehrer);
	}

	/** @return die fehlenden Schüler als lesbare Liste. */
	public @NotNull List<String> getFehlendeSchueler() {
		return new ArrayList<>(fehlendeSchueler);
	}

	/**
	 * Erstellt die bestätigten Importdaten. Kurse ohne Ziel-Fach sowie nicht
	 * auflösbare Lehrer- und Schülerzuordnungen werden dabei ausgelassen.
	 * Schülergruppennamen enthalten die sortierten Jahrgänge der übernommenen Schüler.
	 * Bereits belegte Namen im Zielplanungsabschnitt erhalten einen freien Nummernsuffix ab -2.
	 *
	 * @return die anzulegenden UV-Daten
	 */
	public @NotNull UvKursImportDaten createImportDaten() {
		final @NotNull UvKursImportDaten result = new UvKursImportDaten();
		final @NotNull Map<String, Long> schieneByBezeichnung = new HashMap<>();
		final @NotNull Set<String> gruppenbezeichnungen = new HashSet<>();
		for (final @NotNull UvSchuelergruppe gruppe : uvManager.schuelergruppeGetMengeByPlanungsabschnitt(planungsabschnitt)) {
			gruppenbezeichnungen.add(gruppe.bezeichnung);
		}
		nextTemporaereId = -1;
		nextSchienenNummer = 1;
		for (final @NotNull UvSchiene schiene : uvManager.schieneGetMengeByPlanungsabschnitt(planungsabschnitt)) {
			nextSchienenNummer = Math.max(nextSchienenNummer, schiene.nummer + 1);
			if (schiene.bezeichnung != null) {
				schieneByBezeichnung.put(schiene.bezeichnung, schiene.id);
			}
		}
		for (final @NotNull UvKursImportQuellkurs quellkurs : kurse) {
			final UvFach fach = uvManager.fachGetByIdFachAndPlanungsabschnittOrNull(quellkurs.idFach, planungsabschnitt);
			if (fach == null) {
				continue;
			}
			addKurs(result, quellkurs, fach, schieneByBezeichnung, gruppenbezeichnungen);
		}
		return result;
	}

	private void addKurs(final @NotNull UvKursImportDaten daten, final @NotNull UvKursImportQuellkurs quellkurs,
			final @NotNull UvFach fach, final @NotNull Map<String, Long> schieneByBezeichnung,
			final @NotNull Set<String> gruppenbezeichnungen) {
		final @NotNull UvSchuelergruppeCreateRequest schuelergruppe = new UvSchuelergruppeCreateRequest();
		schuelergruppe.id = nextTemporaereId--;
		schuelergruppe.idPlanungsabschnitt = planungsabschnitt.id;
		daten.schuelergruppen.add(schuelergruppe);
		final @NotNull UvKursCreateRequest kurs = new UvKursCreateRequest();
		kurs.id = nextTemporaereId--;
		kurs.idPlanungsabschnitt = planungsabschnitt.id;
		kurs.idSchuljahresabschnitt = idSchuljahresabschnitt;
		kurs.idFach = fach.id;
		kurs.kursart = quellkurs.kursart;
		kurs.kursnummer = quellkurs.nummer;
		kurs.idSchuelergruppe = schuelergruppe.id;
		daten.kurse.add(kurs);
		final @NotNull UvLerngruppeCreateRequest lerngruppe = new UvLerngruppeCreateRequest();
		lerngruppe.id = nextTemporaereId--;
		lerngruppe.idPlanungsabschnitt = planungsabschnitt.id;
		lerngruppe.idKurs = kurs.id;
		lerngruppe.wochenstunden = quellkurs.wochenstunden;
		lerngruppe.wochenstundenUnterrichtet = quellkurs.wochenstunden;
		daten.lerngruppen.add(lerngruppe);
		addLehrer(daten, quellkurs, lerngruppe);
		addSchueler(daten, quellkurs, schuelergruppe);
		schuelergruppe.bezeichnung = createGruppenbezeichnung(quellkurs, schuelergruppe, gruppenbezeichnungen);
		addSchienen(daten, quellkurs, lerngruppe.id, schieneByBezeichnung);
	}

	private @NotNull String createGruppenbezeichnung(final @NotNull UvKursImportQuellkurs quellkurs,
			final @NotNull UvSchuelergruppeCreateRequest schuelergruppe, final @NotNull Set<String> gruppenbezeichnungen) {
		final @NotNull List<JahrgangsDaten> jahrgaenge = new ArrayList<>();
		for (final @NotNull Long idJahrgang : schuelergruppe.idsJahrgaengeErlaubt) {
			jahrgaenge.add(uvManager.jahrgangsdatenGetById(idJahrgang));
		}
		jahrgaenge.sort(compJahrgangsdaten);
		final @NotNull StringBuilder jahrgangsbezeichnung = new StringBuilder();
		for (final @NotNull JahrgangsDaten jahrgang : jahrgaenge) {
			if (!jahrgangsbezeichnung.isEmpty()) {
				jahrgangsbezeichnung.append("/");
			}
			jahrgangsbezeichnung.append((jahrgang.kuerzel == null) ? ("JG " + jahrgang.id) : jahrgang.kuerzel);
		}
		final @NotNull String basis = (jahrgangsbezeichnung.isEmpty() ? "" : (jahrgangsbezeichnung.toString() + " "))
				+ getFachKuerzel(quellkurs.idFach) + "-" + quellkurs.kursart + quellkurs.nummer;
		@NotNull String bezeichnung = basis;
		int nummer = 2;
		while (gruppenbezeichnungen.contains(bezeichnung)) {
			bezeichnung = basis + "-" + nummer++;
		}
		gruppenbezeichnungen.add(bezeichnung);
		return bezeichnung;
	}

	private void addSchueler(final @NotNull UvKursImportDaten daten, final @NotNull UvKursImportQuellkurs quellkurs,
			final @NotNull UvSchuelergruppeCreateRequest schuelergruppe) {
		for (final @NotNull Long idSchueler : quellkurs.schuelerIds) {
			final UvPlanungsabschnittSchueler schueler = uvManager.planungsabschnittSchuelerGetByIdOrNull(planungsabschnitt.id, idSchueler);
			if (schueler == null) {
				continue;
			}
			if (!schuelergruppe.idsJahrgaengeErlaubt.contains(schueler.idJahrgang)) {
				schuelergruppe.idsJahrgaengeErlaubt.add(schueler.idJahrgang);
			}
			final @NotNull UvSchuelergruppeSchuelerCreateRequest zuordnung = new UvSchuelergruppeSchuelerCreateRequest();
			zuordnung.idPlanungsabschnitt = planungsabschnitt.id;
			zuordnung.idSchuelergruppe = schuelergruppe.id;
			zuordnung.idSchueler = idSchueler;
			daten.schuelergruppenschueler.add(zuordnung);
		}
	}

	private void addSchienen(final @NotNull UvKursImportDaten daten, final @NotNull UvKursImportQuellkurs quellkurs,
			final long idLerngruppe, final @NotNull Map<String, Long> schieneByBezeichnung) {
		for (final @NotNull Long idQuellschiene : quellkurs.schienenIds) {
			final String bezeichnung = schienenBezeichnungById.get(idQuellschiene);
			if (bezeichnung == null) {
				continue;
			}
			Long idSchiene = schieneByBezeichnung.get(bezeichnung);
			if (idSchiene == null) {
				final @NotNull UvSchieneCreateRequest schiene = new UvSchieneCreateRequest();
				idSchiene = nextTemporaereId--;
				schiene.id = idSchiene;
				schiene.idPlanungsabschnitt = planungsabschnitt.id;
				schiene.nummer = nextSchienenNummer++;
				schiene.bezeichnung = bezeichnung;
				daten.schienen.add(schiene);
				schieneByBezeichnung.put(bezeichnung, idSchiene);
			}
			final @NotNull UvLerngruppenSchieneCreateRequest zuordnung = new UvLerngruppenSchieneCreateRequest();
			zuordnung.idPlanungsabschnitt = planungsabschnitt.id;
			zuordnung.idLerngruppe = idLerngruppe;
			zuordnung.idSchiene = idSchiene;
			daten.lerngruppenschienen.add(zuordnung);
		}
	}

	private void addGostKurs(final @NotNull GostBlockungKurs kurs, final @NotNull GostBlockungsergebnisKurs ergebnisKurs) {
		final @NotNull UvKursImportQuellkurs quellkurs = new UvKursImportQuellkurs(kurs.id, kurs.fach_id, GostKursart.fromID(kurs.kursart).kuerzel,
				kurs.nummer, kurs.wochenstunden);
		for (final @NotNull GostBlockungKursLehrer lehrer : kurs.lehrer) {
			quellkurs.lehrerIds.add(lehrer.id);
		}
		quellkurs.schuelerIds.addAll(ergebnisKurs.schueler);
		quellkurs.schienenIds.addAll(ergebnisKurs.schienen);
		kurse.add(quellkurs);
	}

	private void analyse() {
		for (final @NotNull UvKursImportQuellkurs kurs : kurse) {
			if (uvManager.fachGetByIdFachAndPlanungsabschnittOrNull(kurs.idFach, planungsabschnitt) == null) {
				fehlendeFaecher.add(getFachKuerzel(kurs.idFach));
			}
			for (final Long idLehrer : kurs.lehrerIds) {
				final UvLehrer uvLehrer = uvManager.lehrerGetByKLehrerIdOrNull(idLehrer);
				if ((uvLehrer == null) || !uvManager.lehrerIsInPlanungsabschnitt(uvLehrer, planungsabschnitt)) {
					fehlendeLehrer.add((uvLehrer == null) ? ("Lehrer-ID " + idLehrer) : uvLehrer.kuerzel);
				}
			}
			for (final Long idSchueler : kurs.schuelerIds) {
				if (uvManager.planungsabschnittSchuelerGetByIdOrNull(planungsabschnitt.id, idSchueler) == null) {
					fehlendeSchueler.add("Schüler-ID " + idSchueler);
				}
			}
		}
	}

	private void addLehrer(final @NotNull UvKursImportDaten daten, final @NotNull UvKursImportQuellkurs quellkurs,
			final @NotNull UvLerngruppeCreateRequest lerngruppe) {
		int reihenfolge = 1;
		for (final @NotNull Long idKLehrer : quellkurs.lehrerIds) {
			final UvLehrer lehrer = uvManager.lehrerGetByKLehrerIdOrNull(idKLehrer);
			if ((lehrer == null) || !uvManager.lehrerIsInPlanungsabschnitt(lehrer, planungsabschnitt)) {
				continue;
			}
			final @NotNull UvLerngruppenLehrerCreateRequest zuordnung = new UvLerngruppenLehrerCreateRequest();
			zuordnung.idPlanungsabschnitt = planungsabschnitt.id;
			zuordnung.idLerngruppe = lerngruppe.id;
			zuordnung.idLehrer = lehrer.id;
			zuordnung.reihenfolge = reihenfolge++;
			zuordnung.wochenstunden = quellkurs.wochenstunden;
			zuordnung.wochenstundenAngerechnet = quellkurs.wochenstunden;
			daten.lerngruppenlehrer.add(zuordnung);
		}
	}

	private @NotNull String getFachKuerzel(final long idFach) {
		for (final @NotNull FachDaten fach : uvManager.fachdatenGetMenge()) {
			if (fach.id == idFach) {
				return fach.kuerzel;
			}
		}
		return "Fach-ID " + idFach;
	}

	private static int getKursnummer(final @NotNull String kuerzel) {
		int index = kuerzel.length() - 1;
		while ((index >= 0) && ("0123456789".indexOf(kuerzel.charAt(index)) >= 0)) {
			index--;
		}
		if (index == (kuerzel.length() - 1)) {
			return 1;
		}
		return Integer.parseInt(kuerzel.substring(index + 1));
	}

}
