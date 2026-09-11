package de.svws_nrw.core.utils.uv;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.adt.map.ListMap1DLongKeys;
import de.svws_nrw.core.adt.map.ListMap2DLongKeys;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.adt.map.ListMap4DLongKeys;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvGrunddatenBundle;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden;
import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppenSchiene;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle;
import de.svws_nrw.core.data.uv.UvRaum;
import de.svws_nrw.core.data.uv.UvRaumgruppe;
import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.core.data.uv.UvStundentafel;
import de.svws_nrw.core.data.uv.UvStundentafelFach;
import de.svws_nrw.core.data.uv.UvUnterricht;
import de.svws_nrw.core.data.uv.UvUnterrichtLerngruppenlehrer;
import de.svws_nrw.core.data.uv.UvUnterrichtRaum;
import de.svws_nrw.core.data.uv.UvZeitraster;
import de.svws_nrw.core.data.uv.UvZeitrasterEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.DateUtils;
import de.svws_nrw.core.utils.ListUtils;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Verwaltet die Daten der Unterrichtsverteilung und stellt darauf aufbauende
 * Zugriffs-, Prüf- und Hilfsmethoden für die verschiedenen UV-Datentypen bereit.
 * <p>Die Löschkaskade für Klassen und Kurse führt über deren Lerngruppen zu den Unterrichten
 * und weiter zu deren Raum- und Lehrerzuordnungen. Lerngruppen entfernen außerdem ihre
 * Schienen- und Lehrerzuordnungen; letztere entfernen ebenfalls ihre Unterrichtszuordnungen.
 * Jede Stufe delegiert dafür an die jeweilige interne Remove-Methode ohne Indexneuaufbau.
 * Erst der öffentliche Aufruf baut die abgeleiteten Indizes neu auf. Ein reiner Indexaustausch
 * beim Patchen darf diese fachlichen Löschkaskaden nicht auslösen.</p>
 * <p>Patch-Methoden erwarten vollständige neue DTOs mit unveränderter Identität. Die bisher im
 * Manager gespeicherten DTOs dürfen vor dem Aufruf nicht verändert werden, damit ihre alten
 * Indexschlüssel für den Austausch verfügbar bleiben.</p>
 */
@SuppressWarnings({ "unused", "java:S1192" })
public class UvManager {

	// Schuldaten
	private Map<Long, JahrgangsDaten> jahrgangById = null;
	private Map<Long, FachDaten> fachdatenById = null;

	// Planungsabschnitte
	private final @NotNull Map<Long, UvPlanungsabschnitt> planungsabschnittById = new HashMap<>();
	private final @NotNull List<UvPlanungsabschnitt> planungsabschnittMenge = new ArrayList<>();

	// Lehrer
	private final @NotNull Map<Long, UvLehrer> lehrerById = new HashMap<>();
	private final @NotNull List<UvLehrer> lehrerMenge = new ArrayList<>();
	private final @NotNull Map<Long, UvLehrer> lehrerByIdKLehrer = new HashMap<>();

	// Lehrer-Lehrämter
	private final @NotNull HashMap2D<Boolean, Long, LehrerUnterrichtsfach> lehrerUnterrichtsfachByIstKLehrerAndId = new HashMap2D<>();
	private final @NotNull List<LehrerUnterrichtsfach> lehrerUnterrichtsfachMenge = new ArrayList<>();
	private @NotNull ListMap2DLongKeys<LehrerUnterrichtsfach> lehrerUnterrichtsfachByIdKLehrerAndIdFach = new ListMap2DLongKeys<>();
	private @NotNull ListMap2DLongKeys<LehrerUnterrichtsfach> lehrerUnterrichtsfachByIdLehrerAndIdFach = new ListMap2DLongKeys<>();
	private @NotNull ListMap2DLongKeys<UvLehrer> lehrerMengeByIdFachAndSek = new ListMap2DLongKeys<>();

	// Planungsabschnitt-Lehrer Zuordnungen
	private @NotNull ListMap2DLongKeys<UvPlanungsabschnittLehrer> planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer =
			new ListMap2DLongKeys<>();
	private final @NotNull List<UvPlanungsabschnittLehrer> planungsabschnittLehrerMenge = new ArrayList<>();

	// Anrechnungsstunden und Pflichtstundensoll
	private final @NotNull Map<Long, UvLehrerAnrechnungsstunden> lehrerAnrechnungsstundenById = new HashMap<>();
	private final @NotNull List<UvLehrerAnrechnungsstunden> lehrerAnrechnungsstundenMenge = new ArrayList<>();
	private final @NotNull Map<Long, List<UvLehrerAnrechnungsstunden>> lehrerAnrechnungsstundenMengeByLehrerId = new HashMap<>();
	private final @NotNull Map<Long, UvLehrerPflichtstundensoll> lehrerPflichtstundensollById = new HashMap<>();
	private final @NotNull List<UvLehrerPflichtstundensoll> lehrerPflichtstundensollMenge = new ArrayList<>();
	private final @NotNull Map<Long, List<UvLehrerPflichtstundensoll>> lehrerPflichtstundensollMengeByLehrerId = new HashMap<>();

	//Räume und Raumgruppen
	private final @NotNull List<UvRaum> raumMenge = new ArrayList<>();
	private final @NotNull Map<Long, UvRaumgruppe> raumgruppeById = new HashMap<>();
	private final @NotNull List<UvRaumgruppe> raumgruppeMenge = new ArrayList<>();
	private @NotNull ListMap2DLongKeys<UvRaum> raumByIdRaumgruppeAndIdRaum = new ListMap2DLongKeys<>();
	private final @NotNull Map<String, UvRaum> raumByKuerzel = new HashMap<>();

	// Stundentafeln
	private final @NotNull Map<Long, UvStundentafel> stundentafelById = new HashMap<>();
	private final @NotNull List<UvStundentafel> stundentafelMenge = new ArrayList<>();

	// Stundentafel-Fächer
	private @NotNull ListMap4DLongKeys<UvStundentafelFach> stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach =
			new ListMap4DLongKeys<>();
	private final @NotNull List<UvStundentafelFach> stundentafelFachMenge = new ArrayList<>();

	// Fächer
	private final @NotNull Map<Long, UvFach> fachById = new HashMap<>();
	private final @NotNull List<UvFach> fachMenge = new ArrayList<>();
	private @NotNull ListMap1DLongKeys<UvFach> fachMengeByIdFach = new ListMap1DLongKeys<>();

	// Zeitraster
	private final @NotNull Map<Long, UvZeitraster> zeitrasterById = new HashMap<>();
	private final @NotNull List<UvZeitraster> zeitrasterMenge = new ArrayList<>();

	// Zeitraster-Einträge
	private @NotNull ListMap2DLongKeys<UvZeitrasterEintrag> zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag =
			new ListMap2DLongKeys<>();
	private final @NotNull List<UvZeitrasterEintrag> zeitrasterEintragMenge = new ArrayList<>();

	// Planungsabschnitt-Zeitraster Zuordnungen
	private @NotNull ListMap2DLongKeys<UvPlanungsabschnittZeitraster> planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster =
			new ListMap2DLongKeys<>();
	private final @NotNull List<UvPlanungsabschnittZeitraster> planungsabschnittZeitrasterMenge = new ArrayList<>();

	// Planungsabschnitt-Schüler Zuordnungen
	private @NotNull ListMap4DLongKeys<UvPlanungsabschnittSchueler> planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler =
			new ListMap4DLongKeys<>();
	private final @NotNull List<UvPlanungsabschnittSchueler> planungsabschnittSchuelerMenge = new ArrayList<>();
	private @NotNull ListMap2DLongKeys<UvSchuelergruppe> schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe =
			new ListMap2DLongKeys<>();
	private final @NotNull List<UvSchuelergruppe> schuelergruppeMenge = new ArrayList<>();
	private @NotNull ListMap3DLongKeys<UvSchuelergruppeSchueler> schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler =
			new ListMap3DLongKeys<>();
	private final @NotNull List<UvSchuelergruppeSchueler> schuelergruppeSchuelerMenge = new ArrayList<>();
	private @NotNull ListMap2DLongKeys<UvPlanungsabschnittSchueler> planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe =
			new ListMap2DLongKeys<>();

	// Klassen und Kurse
	private @NotNull ListMap3DLongKeys<UvKlasse> klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse =
			new ListMap3DLongKeys<>();
	private final @NotNull List<UvKlasse> klasseMenge = new ArrayList<>();

	// Klassenlehrer
	private @NotNull ListMap3DLongKeys<UvKlassenLehrer> klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer =
			new ListMap3DLongKeys<>();
	private final @NotNull List<UvKlassenLehrer> klassenLehrerMenge = new ArrayList<>();

	private @NotNull ListMap3DLongKeys<UvKurs> kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs =
			new ListMap3DLongKeys<>();
	private final @NotNull List<UvKurs> kursMenge = new ArrayList<>();

	// Schienen
	private @NotNull ListMap3DLongKeys<UvSchiene> schieneByIdPlanungsabschnittAndNummerAndIdSchiene =
			new ListMap3DLongKeys<>();
	private final @NotNull List<UvSchiene> schieneMenge = new ArrayList<>();

	// Lerngruppen
	private @NotNull ListMap2DLongKeys<UvLerngruppe> lerngruppeByIdPlanungsabschnittAndIdLerngruppe =
			new ListMap2DLongKeys<>();
	private final @NotNull List<UvLerngruppe> lerngruppeMenge = new ArrayList<>();

	private @NotNull ListMap1DLongKeys<UvLerngruppe> lerngruppeMengeByIdSchuelergruppe =
			new ListMap1DLongKeys<>();
	private @NotNull ListMap3DLongKeys<UvLerngruppe> lerngruppeByIdKursAndIdKlasseAndIdFach =
			new ListMap3DLongKeys<>();
	private @NotNull ListMap3DLongKeys<UvLerngruppenSchiene> lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene =
			new ListMap3DLongKeys<>();
	private final @NotNull List<UvLerngruppenSchiene> lerngruppenSchieneMenge = new ArrayList<>();
	private @NotNull ListMap3DLongKeys<UvLerngruppenLehrer> lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer =
			new ListMap3DLongKeys<>();
	private final @NotNull List<UvLerngruppenLehrer> lerngruppenLehrerMenge = new ArrayList<>();

	// Unterrichte
	private @NotNull ListMap4DLongKeys<UvUnterricht> unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht =
			new ListMap4DLongKeys<>();
	private final @NotNull List<UvUnterricht> unterrichtMenge = new ArrayList<>();
	private @NotNull ListMap3DLongKeys<UvUnterrichtRaum> unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum =
			new ListMap3DLongKeys<>();
	private final @NotNull List<UvUnterrichtRaum> unterrichtRaumMenge = new ArrayList<>();
	private @NotNull ListMap3DLongKeys<UvUnterrichtLerngruppenlehrer> unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer =
			new ListMap3DLongKeys<>();
	private final @NotNull List<UvUnterrichtLerngruppenlehrer> unterrichtLerngruppenlehrerMenge = new ArrayList<>();

	// Comparators
	private final @NotNull Comparator<JahrgangsDaten> compJahrgangsdaten =
			(final @NotNull JahrgangsDaten a, final @NotNull JahrgangsDaten b) -> {
				final int cmp = Integer.compare(a.sortierung, b.sortierung);
				return (cmp == 0) ? Long.compare(a.id, b.id) : cmp;
			};

	private final @NotNull Comparator<UvLehrer> compLehrer =
			(final @NotNull UvLehrer a, final @NotNull UvLehrer b) -> {
				final int result = a.kuerzel.compareTo(b.kuerzel);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<UvPlanungsabschnittLehrer> compPlanungsabschnittLehrer =
			(final @NotNull UvPlanungsabschnittLehrer a, final @NotNull UvPlanungsabschnittLehrer b) -> {
				final @NotNull UvLehrer aL = lehrerGetByPlanungsabschnittLehrer(a);
				final @NotNull UvLehrer bL = lehrerGetByPlanungsabschnittLehrer(b);
				return compLehrer.compare(aL, bL);
			};

	private final @NotNull Comparator<UvLehrerPflichtstundensoll> compLehrerPflichtstundensoll =
			(final @NotNull UvLehrerPflichtstundensoll a, final @NotNull UvLehrerPflichtstundensoll b) -> {
				final @NotNull UvLehrer aL = lehrerGetByLehrerPflichtstundensoll(a);
				final @NotNull UvLehrer bL = lehrerGetByLehrerPflichtstundensoll(b);
				int result = compLehrer.compare(aL, bL);
				if (result != 0) {
					return result;
				}
				result = b.gueltigVon.compareTo(a.gueltigVon);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<UvLehrerAnrechnungsstunden> compLehrerAnrechnungsstunden =
			(final @NotNull UvLehrerAnrechnungsstunden a, final @NotNull UvLehrerAnrechnungsstunden b) -> {
				final @NotNull UvLehrer aL = lehrerGetByLehrerAnrechnungsstunden(a);
				final @NotNull UvLehrer bL = lehrerGetByLehrerAnrechnungsstunden(b);
				int result = compLehrer.compare(aL, bL);
				if (result != 0) {
					return result;
				}
				result = b.gueltigVon.compareTo(a.gueltigVon);
				if (result != 0) {
					return result;
				}
				if ((a.gueltigBis == null) && (b.gueltigBis != null)) {
					return -1;
				}
				if ((a.gueltigBis != null) && (b.gueltigBis == null)) {
					return 1;
				}
				if ((a.gueltigBis != null) && (b.gueltigBis != null)) {
					result = a.gueltigBis.compareTo(b.gueltigBis);
				}
				if (result != 0) {
					return result;
				}
				result = a.anrechnungsgrundKrz.compareTo(b.anrechnungsgrundKrz);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<UvRaumgruppe> compRaumgruppe =
			(final @NotNull UvRaumgruppe a, final @NotNull UvRaumgruppe b) -> a.bezeichnung.compareTo(b.bezeichnung);

	private final @NotNull Comparator<UvRaum> compRaum =
			(final @NotNull UvRaum a, final @NotNull UvRaum b) -> a.kuerzel.compareTo(b.kuerzel);

	private final @NotNull Comparator<UvFach> compFach =
			(final @NotNull UvFach a, final @NotNull UvFach b) -> {
				final @NotNull FachDaten fdA = fachdatenGetByFach(a);
				final @NotNull FachDaten fdB = fachdatenGetByFach(b);
				int result = Integer.compare(fdA.sortierung, fdB.sortierung);
				if (result != 0) {
					return result;
				}
				result = fdA.bezeichnung.compareTo(fdB.bezeichnung);
				if (result != 0) {
					return result;
				}
				result = a.gueltigVon.compareTo(b.gueltigVon);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<UvStundentafelFach> compStundentafelFach =
			(final @NotNull UvStundentafelFach a, final @NotNull UvStundentafelFach b) -> {
				final @NotNull UvFach fachA = fachGetByStundentafelFach(a);
				final @NotNull UvFach fachB = fachGetByStundentafelFach(b);
				return compFach.compare(fachA, fachB);
			};

	private final @NotNull Comparator<UvZeitraster> compZeitraster =
			(final @NotNull UvZeitraster a, final @NotNull UvZeitraster b) -> {
				final int result = a.gueltigVon.compareTo(b.gueltigVon);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<UvZeitrasterEintrag> compZeitrasterEintrag =
			(final @NotNull UvZeitrasterEintrag a, final @NotNull UvZeitrasterEintrag b) -> {
				int result = Long.compare(a.idZeitraster, b.idZeitraster);
				if (result != 0) {
					return result;
				}
				result = Integer.compare(a.wochentag, b.wochentag);
				if (result != 0) {
					return result;
				}
				result = Integer.compare(a.stunde, b.stunde);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<UvPlanungsabschnittZeitraster> compPlanungsabschnittZeitraster =
			(final @NotNull UvPlanungsabschnittZeitraster a, final @NotNull UvPlanungsabschnittZeitraster b) -> {
				final int result = Long.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.idZeitraster, b.idZeitraster);
			};

	private final @NotNull Comparator<UvPlanungsabschnittSchueler> compPlanungsabschnittSchueler =
			(final @NotNull UvPlanungsabschnittSchueler a, final @NotNull UvPlanungsabschnittSchueler b) -> {
				int result = a.daten.nachname.compareTo(b.daten.nachname);
				if (result != 0) {
					return result;
				}
				result = a.daten.vorname.compareTo(b.daten.vorname);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.idSchueler, b.idSchueler);
			};

	private final @NotNull Comparator<UvSchuelergruppe> compSchuelergruppe =
			(final @NotNull UvSchuelergruppe a, final @NotNull UvSchuelergruppe b) -> {
				final int result = a.bezeichnung.compareTo(b.bezeichnung);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<UvSchuelergruppeSchueler> compSchuelergruppeSchueler =
			(final @NotNull UvSchuelergruppeSchueler a, final @NotNull UvSchuelergruppeSchueler b) -> {
				final @NotNull UvPlanungsabschnittSchueler schuelerA = planungsabschnittSchuelerGetBySchuelergruppeSchueler(a);
				final @NotNull UvPlanungsabschnittSchueler schuelerB = planungsabschnittSchuelerGetBySchuelergruppeSchueler(b);
				return compPlanungsabschnittSchueler.compare(schuelerA, schuelerB);
			};

	private final @NotNull Comparator<UvKlasse> compKlasse =
			(final @NotNull UvKlasse a, final @NotNull UvKlasse b) -> {
				final int result = Long.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
				if (result != 0) {
					return result;
				}
				return a.kuerzel.compareTo(b.kuerzel);
			};

	private final @NotNull Comparator<UvKurs> compKurs =
			(final @NotNull UvKurs a, final @NotNull UvKurs b) -> {
				int result = Long.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
				if (result != 0) {
					return result;
				}
				result = a.kursart.compareTo(b.kursart);
				if (result != 0) {
					return result;
				}
				return Integer.compare(a.kursnummer, b.kursnummer);
			};

	private final @NotNull Comparator<UvSchiene> compSchiene =
			(final @NotNull UvSchiene a, final @NotNull UvSchiene b) -> {
				final int result = Long.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
				if (result != 0) {
					return result;
				}
				return Integer.compare(a.nummer, b.nummer);
			};

	private final @NotNull Comparator<UvLerngruppe> compLerngruppe =
			(final @NotNull UvLerngruppe a, final @NotNull UvLerngruppe b) -> {
				final int result = Long.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
				if (result != 0) {
					return result;
				}
				final UvKlasse aKlasse = klasseGetByLerngruppe(a);
				final UvKlasse bKlasse = klasseGetByLerngruppe(b);
				if ((aKlasse != null) && (bKlasse != null)) {
					return compKlasse.compare(aKlasse, bKlasse);
				}
				final UvKurs aKurs = kursGetByLerngruppe(a);
				final UvKurs bKurs = kursGetByLerngruppe(b);
				if ((aKurs != null) && (bKurs != null)) {
					return compKurs.compare(aKurs, bKurs);
				}
				if ((aKlasse != null) && (bKurs != null)) {
					return -1;
				}
				if ((aKurs != null) && (bKlasse != null)) {
					return 1;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<UvLerngruppenLehrer> compLerngruppenLehrer =
			(final @NotNull UvLerngruppenLehrer a, final @NotNull UvLerngruppenLehrer b) -> {
				final int result = Integer.compare(a.reihenfolge, b.reihenfolge);
				if (result != 0) {
					return result;
				}
				return compLehrer.compare(lehrerGetByLerngruppenLehrer(a), lehrerGetByLerngruppenLehrer(b));
			};

	private final @NotNull Comparator<UvKlassenLehrer> compKlassenLehrer =
			(final @NotNull UvKlassenLehrer a, final @NotNull UvKlassenLehrer b) -> {
				final int result = Integer.compare(a.reihenfolge, b.reihenfolge);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	private final @NotNull Comparator<UvUnterricht> compUnterricht =
			(final @NotNull UvUnterricht a, final @NotNull UvUnterricht b) -> {
				int result = Long.compare(a.idPlanungsabschnitt, b.idPlanungsabschnitt);
				if (result != 0) {
					return result;
				}
				result = Long.compare(a.idLerngruppe, b.idLerngruppe);
				if (result != 0) {
					return result;
				}
				return Long.compare(a.id, b.id);
			};

	/**
	 * Konstruktor
	 */
	public UvManager() {
		super();
	}

	/**
	 * Konstruktor
	 * @param jahrgaenge die {@link JahrgangsDaten}-Objekte, die hinzugefügt werden sollen
	 * @param fachdaten die {@link FachDaten}-Objekte, die hinzugefügt werden sollen
	 */
	public UvManager(final @NotNull Collection<JahrgangsDaten> jahrgaenge, final @NotNull Collection<FachDaten> fachdaten) {
		jahrgangsdatenAddAll(jahrgaenge);
		fachdatenAddAll(fachdaten);
	}

	/**
	 * Füge alle Grunddaten zum Manager hinzu.
	 * @param gData die {@link UvGrunddatenBundle}, die hinzugefügt werden sollen
	 */
	public void grunddatenBundleAdd(final @NotNull UvGrunddatenBundle gData) {
		grunddatenBundleAddOhneUpdate(gData);
		updateAll();
	}

	private void grunddatenBundleAddOhneUpdate(final @NotNull UvGrunddatenBundle gData) {
		lehrerAddAllOhneUpdate(gData.lehrer);
		lehrerPflichtstundensollAddAllOhneUpdate(gData.lehrerPflichtstundensoll);
		lehrerAnrechnungsstundenAddAllOhneUpdate(gData.lehrerAnrechnungsstunden);
		raumgruppeAddAllOhneUpdate(gData.raumgruppen);
		raumAddAllOhneUpdate(gData.raeume);
		stundentafelAddAllOhneUpdate(gData.stundentafeln);
		fachAddAllOhneUpdate(gData.faecher);
		stundentafelFachAddAllOhneUpdate(gData.stundentafelfaecher);
		lehrerUnterrichtsfachAddAllOhneUpdate(gData.lehrerUnterrichtsfaecher);
		zeitrasterAddAllOhneUpdate(gData.zeitraster);
		zeitrasterEintragAddAllOhneUpdate(gData.zeitrastereintraege);
	}

	/**
	 * Füge alle Planungsabschnitts-Daten zum Manager hinzu.
	 * @param pData die Datensammlung
	 */
	public void planungsabschnittsdatenBundleAdd(final @NotNull UvPlanungsabschnittsdatenBundle pData) {
		planungsabschnittsdatenBundleAddOhneUpdate(pData);
		updateAll();
	}

	private void planungsabschnittsdatenBundleAddOhneUpdate(final @NotNull UvPlanungsabschnittsdatenBundle pData) {
		if (pData.planungsabschnitt != null) {
			planungsabschnittAddOhneUpdate(pData.planungsabschnitt);
		}
		planungsabschnittLehrerAddAllOhneUpdate(pData.planungsabschnittlehrer);
		planungsabschnittSchuelerAddAllOhneUpdate(pData.planungsabschnittschueler);
		planungsabschnittZeitrasterAddAllOhneUpdate(pData.planungsabschnittzeitraster);
		klasseAddAllOhneUpdate(pData.klassen);
		klassenLehrerAddAllOhneUpdate(pData.klassenlehrer);
		kursAddAllOhneUpdate(pData.kurse);
		schuelergruppeAddAllOhneUpdate(pData.schuelergruppen);
		schuelergruppeSchuelerAddAllOhneUpdate(pData.schuelergruppenschueler);
		schieneAddAllOhneUpdate(pData.schienen);
		lerngruppeAddAllOhneUpdate(pData.lerngruppen);
		lerngruppenLehrerAddAllOhneUpdate(pData.lerngruppenlehrer);
		lerngruppenSchieneAddAllOhneUpdate(pData.lerngruppenschienen);
		unterrichtAddAllOhneUpdate(pData.unterrichte);
		unterrichtRaumAddAllOhneUpdate(pData.unterrichtraeume);
		unterrichtLerngruppenlehrerAddAllOhneUpdate(pData.unterrichtlerngruppenlehrer);
	}


	// #####################################################################
	// #################### Update-Methoden ################################
	// #####################################################################

	private void updateAll() {
		updateLehrerMenge();
		updateLehrerByIdKLehrer();
		updateLehrerUnterrichtsfachMenge();
		updateLehrerUnterrichtsfachByIdKLehrerAndIdFach();
		updateLehrerUnterrichtsfachByIdLehrerAndIdFach();
		updatePlanungsabschnittLehrerMenge();
		updateLehrerAnrechnungsstundenMenge();
		updateLehrerPflichtstundensollMenge();
		updateRaumgruppeMenge();
		updateRaumMenge();
		updateFachMenge();
		updateStundentafelMenge();
		updateStundentafelFachMenge();
		updateZeitrasterMenge();
		updateZeitrasterEintragMenge();
		updatePlanungsabschnittZeitrasterMenge();
		updatePlanungsabschnittSchuelerMenge();
		updateSchuelergruppeMenge();
		updateSchuelergruppeSchuelerMenge();
		updateKlasseMenge();
		updateKlassenLehrerMenge();
		updateKursMenge();
		updateSchieneMenge();
		updateLerngruppeMenge();
		updateLerngruppenLehrerMenge();
		updateLerngruppenSchieneMenge();
		updateUnterrichtMenge();
		updateUnterrichtRaumMenge();
		updateUnterrichtLerngruppenlehrerMenge();

		updateLehrerMengeByIdFachAndSek();
		updateLehrerAnrechnungsstundenMengeByLehrerId();
		updateZeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag();
		updateLehrerPflichtstundensollMengeByLehrerId();
		updatePlanungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer();
		updatePlanungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster();
		updatePlanungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler();
		updatePlanungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe(); // benötigt _planungsabschnittSchueler_by_idPlanungsabschnitt_and_idSchueler
		updateRaumByIdRaumgruppeAndIdRaum();
		updateRaumByKuerzel();
		updateStundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach();
		updateFachMengeByIdFach();
		updateSchuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe();
		updateKlasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse();
		updateKlassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer();
		updateKursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs();
		updateSchieneByIdPlanungsabschnittAndNummerAndIdSchiene();
		updateLerngruppeByIdPlanungsabschnittAndIdLerngruppe();
		updateLerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer();
		updateLerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene();
		updateLerngruppeMengeByIdSchuelergruppe();
		updateLerngruppeByIdKursAndIdKlasseAndIdFach();
		updateUnterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht();
		updateUnterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum();
		updateUnterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer();
	}

	private void updateLehrerByIdKLehrer() {
		lehrerByIdKLehrer.clear();
		for (final UvLehrer lehrer : lehrerMenge) {
			if (lehrer.idKLehrer == null) {
				continue;
			}
			lehrerByIdKLehrer.put(lehrer.idKLehrer, lehrer);
		}
	}

	private void updateLehrerPflichtstundensollMengeByLehrerId() {
		lehrerPflichtstundensollMengeByLehrerId.clear();
		for (final @NotNull UvLehrerPflichtstundensoll p : lehrerPflichtstundensollMenge) {
			MapUtils.getOrCreateArrayList(lehrerPflichtstundensollMengeByLehrerId, p.idLehrer).add(p);
		}
	}

	private void updateLehrerAnrechnungsstundenMengeByLehrerId() {
		lehrerAnrechnungsstundenMengeByLehrerId.clear();
		for (final @NotNull UvLehrerAnrechnungsstunden anr : lehrerAnrechnungsstundenMenge) {
			MapUtils.getOrCreateArrayList(lehrerAnrechnungsstundenMengeByLehrerId, anr.idLehrer).add(anr);
		}
	}

	private void updatePlanungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer() {
		planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer = new ListMap2DLongKeys<>();
		for (final @NotNull UvPlanungsabschnittLehrer anr : planungsabschnittLehrerMenge) {
			planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.addSingle(anr.idPlanungsabschnitt, anr.idLehrer, anr);
		}
	}

	private void updateRaumByIdRaumgruppeAndIdRaum() {
		raumByIdRaumgruppeAndIdRaum = new ListMap2DLongKeys<>();
		for (final @NotNull UvRaum raum : raumMenge) {
			raumByIdRaumgruppeAndIdRaum.addSingle((raum.idRaumgruppe == null) ? -1L : raum.idRaumgruppe, raum.id, raum);
		}
	}

	private void updateRaumByKuerzel() {
		raumByKuerzel.clear();
		for (final @NotNull UvRaum raum : raumMenge) {
			raumByKuerzel.put(raum.kuerzel, raum);
		}
	}

	private void updateStundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach() {
		stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach = new ListMap4DLongKeys<>();
		for (final @NotNull UvStundentafelFach fach : stundentafelFachMenge) {
			stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.addSingle(fach.idStundentafel, fach.abschnitt, fach.idFach, fach.id,
					fach);
		}
	}

	private void updateFachMengeByIdFach() {
		fachMengeByIdFach = new ListMap1DLongKeys<>();
		for (final @NotNull UvFach fach : fachMenge) {
			fachMengeByIdFach.add(fach.idFach, fach);
		}
	}

	private void updateZeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag() {
		zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag = new ListMap2DLongKeys<>();
		for (final @NotNull UvZeitrasterEintrag eintrag : zeitrasterEintragMenge) {
			zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.addSingle(eintrag.idZeitraster, eintrag.id, eintrag);
		}
	}

	private void updatePlanungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster() {
		planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster = new ListMap2DLongKeys<>();
		for (final @NotNull UvPlanungsabschnittZeitraster zuordnung : planungsabschnittZeitrasterMenge) {
			planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster, zuordnung);
		}
	}

	private void updatePlanungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler() {
		planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler = new ListMap4DLongKeys<>();
		for (final @NotNull UvPlanungsabschnittSchueler zuordnung : planungsabschnittSchuelerMenge) {
			planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.addSingle(zuordnung.idPlanungsabschnitt,
					zuordnung.idJahrgang, (zuordnung.idKlasse != null) ? zuordnung.idKlasse : -1L, zuordnung.idSchueler, zuordnung);
		}
	}

	private void updateSchuelergruppeMenge() {
		schuelergruppeMenge.clear();
		schuelergruppeMenge.addAll(schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.getAllValues());
		schuelergruppeMenge.sort(compSchuelergruppe);
	}

	private void updateSchuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe() {
		schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe = new ListMap2DLongKeys<>();
		for (final @NotNull UvSchuelergruppe schuelergruppe : schuelergruppeMenge) {
			schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.addSingle(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id, schuelergruppe);
		}
	}

	private void updateSchuelergruppeSchuelerMenge() {
		schuelergruppeSchuelerMenge.clear();
		schuelergruppeSchuelerMenge.addAll(schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.getAllValues());
		schuelergruppeSchuelerMenge.sort(compSchuelergruppeSchueler);
	}

	private void updateSchuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler() {
		schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler = new ListMap3DLongKeys<>();
		for (final @NotNull UvSchuelergruppeSchueler zuordnung : schuelergruppeSchuelerMenge) {
			schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.addSingle(zuordnung.idPlanungsabschnitt,
					zuordnung.idSchuelergruppe, zuordnung.idSchueler, zuordnung);
		}
	}

	private void updateKlasseMenge() {
		klasseMenge.clear();
		klasseMenge.addAll(klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.getAllValues());
		klasseMenge.sort(compKlasse);
	}

	private void updateKlasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse() {
		klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse = new ListMap3DLongKeys<>();
		for (final @NotNull UvKlasse klasse : klasseMenge) {
			klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.addSingle(klasse.idPlanungsabschnitt, klasse.idSchuelergruppe, klasse.id, klasse);
		}
	}

	private void updateKlassenLehrerMenge() {
		klassenLehrerMenge.clear();
		klassenLehrerMenge.addAll(klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.getAllValues());
		klassenLehrerMenge.sort(compKlassenLehrer);
	}

	private void updateKlassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer() {
		klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer = new ListMap3DLongKeys<>();
		for (final @NotNull UvKlassenLehrer kl : klassenLehrerMenge) {
			klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.addSingle(kl.idPlanungsabschnitt, kl.idKlasse, kl.idLehrer, kl);
		}
	}

	private void updateKursMenge() {
		kursMenge.clear();
		kursMenge.addAll(kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.getAllValues());
		kursMenge.sort(compKurs);
	}

	private void updateKursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs() {
		kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs = new ListMap3DLongKeys<>();
		for (final @NotNull UvKurs kurs : kursMenge) {
			kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.addSingle(kurs.idPlanungsabschnitt, kurs.idSchuelergruppe, kurs.id, kurs);
		}
	}

	private void updateSchieneMenge() {
		schieneMenge.clear();
		schieneMenge.addAll(schieneByIdPlanungsabschnittAndNummerAndIdSchiene.getAllValues());
		schieneMenge.sort(compSchiene);
	}

	private void updateSchieneByIdPlanungsabschnittAndNummerAndIdSchiene() {
		schieneByIdPlanungsabschnittAndNummerAndIdSchiene = new ListMap3DLongKeys<>();
		for (final @NotNull UvSchiene schiene : schieneMenge) {
			schieneByIdPlanungsabschnittAndNummerAndIdSchiene.addSingle(schiene.idPlanungsabschnitt, schiene.nummer, schiene.id, schiene);
		}
	}

	private void updateLerngruppeMenge() {
		lerngruppeMenge.clear();
		lerngruppeMenge.addAll(lerngruppeByIdPlanungsabschnittAndIdLerngruppe.getAllValues());
		lerngruppeMenge.sort(compLerngruppe);
	}

	private void updateLerngruppeByIdPlanungsabschnittAndIdLerngruppe() {
		lerngruppeByIdPlanungsabschnittAndIdLerngruppe = new ListMap2DLongKeys<>();
		for (final @NotNull UvLerngruppe lerngruppe : lerngruppeMenge) {
			lerngruppeByIdPlanungsabschnittAndIdLerngruppe.addSingle(lerngruppe.idPlanungsabschnitt, lerngruppe.id, lerngruppe);
		}
	}

	private void updateLerngruppenLehrerMenge() {
		lerngruppenLehrerMenge.clear();
		lerngruppenLehrerMenge.addAll(lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.getAllValues());
		lerngruppenLehrerMenge.sort(compLerngruppenLehrer);
	}

	private void updateLerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer() {
		lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer = new ListMap3DLongKeys<>();
		for (final @NotNull UvLerngruppenLehrer lgl : lerngruppenLehrerMenge) {
			lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.addSingle(lgl.idPlanungsabschnitt, lgl.idLerngruppe, lgl.idLehrer, lgl);
		}
	}

	private void updateLerngruppenSchieneMenge() {
		lerngruppenSchieneMenge.clear();
		lerngruppenSchieneMenge.addAll(lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.getAllValues());
	}

	private void updateLerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene() {
		lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene = new ListMap3DLongKeys<>();
		for (final @NotNull UvLerngruppenSchiene lgs : lerngruppenSchieneMenge) {
			lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.addSingle(lgs.idPlanungsabschnitt, lgs.idLerngruppe, lgs.idSchiene, lgs);
		}
	}

	private void updatePlanungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe() {
		planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe = new ListMap2DLongKeys<>();
		for (final @NotNull UvSchuelergruppeSchueler grSchue : schuelergruppeSchuelerMenge) {
			planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe.add(grSchue.idPlanungsabschnitt, grSchue.idSchuelergruppe,
					planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler
							.getSingle14OrException(grSchue.idPlanungsabschnitt, grSchue.idSchueler));
		}
	}

	private void updateLerngruppeMengeByIdSchuelergruppe() {
		lerngruppeMengeByIdSchuelergruppe = new ListMap1DLongKeys<>();
		for (final @NotNull UvLerngruppe lerngruppe : lerngruppeMenge) {
			final UvKurs kurs = kursGetByLerngruppe(lerngruppe);
			if (kurs != null) {
				lerngruppeMengeByIdSchuelergruppe.add(kurs.idSchuelergruppe, lerngruppe);
			} else {
				lerngruppeMengeByIdSchuelergruppe.add(
						DeveloperNotificationException.ifNull("Die Lerngruppe %d ist weder einer Klasse noch einem Kurs zugeordnet.".formatted(lerngruppe.id),
								klasseGetByLerngruppe(lerngruppe)).idSchuelergruppe,
						lerngruppe);
			}
		}
	}

	private void updateLerngruppeByIdKursAndIdKlasseAndIdFach() {
		lerngruppeByIdKursAndIdKlasseAndIdFach = new ListMap3DLongKeys<>();
		for (final @NotNull UvLerngruppe lerngruppe : lerngruppeMenge) {
			lerngruppeByIdKursAndIdKlasseAndIdFach.addSingle((lerngruppe.idKurs == null) ? -1 : lerngruppe.idKurs,
					(lerngruppe.idKlasse == null) ? -1 : lerngruppe.idKlasse,
					(lerngruppe.idFach == null) ? -1 : lerngruppe.idFach,
					lerngruppe);
		}
	}

	private void updateUnterrichtMenge() {
		unterrichtMenge.clear();
		unterrichtMenge.addAll(unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.getAllValues());
		unterrichtMenge.sort(compUnterricht);
	}

	private void updateUnterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht() {
		unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht = new ListMap4DLongKeys<>();
		for (final @NotNull UvUnterricht unterricht : unterrichtMenge) {
			unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.addSingle(
					unterricht.idPlanungsabschnitt, unterricht.idLerngruppe,
					(unterricht.idZeitrasterEintrag == null) ? -1 : unterricht.idZeitrasterEintrag, unterricht.id,
					unterricht);
		}
	}

	private void updateUnterrichtRaumMenge() {
		unterrichtRaumMenge.clear();
		unterrichtRaumMenge.addAll(unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.getAllValues());
	}

	private void updateUnterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum() {
		unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum = new ListMap3DLongKeys<>();
		for (final @NotNull UvUnterrichtRaum zuordnung : unterrichtRaumMenge) {
			unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.addSingle(
					zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idRaum, zuordnung);
		}
	}

	private void updateUnterrichtLerngruppenlehrerMenge() {
		unterrichtLerngruppenlehrerMenge.clear();
		unterrichtLerngruppenlehrerMenge.addAll(unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.getAllValues());
	}

	private void updateUnterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer() {
		unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer = new ListMap3DLongKeys<>();
		for (final @NotNull UvUnterrichtLerngruppenlehrer zuordnung : unterrichtLerngruppenlehrerMenge) {
			unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.addSingle(
					zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idLerngruppenLehrer, zuordnung);
		}
	}

	private void updateLehrerMengeByIdFachAndSek() {
		lehrerMengeByIdFachAndSek = new ListMap2DLongKeys<>();
		for (final @NotNull LehrerUnterrichtsfach lehramt : lehrerUnterrichtsfachMenge) {
			final @NotNull UvLehrer lehrer = lehrerGetByLehrerUnterrichtsfachOrException(lehramt);
			if (lehramt.istSek1) {
				lehrerMengeByIdFachAndSek.add(lehramt.idFach, 1, lehrer);
			}
			if (lehramt.istSek2) {
				lehrerMengeByIdFachAndSek.add(lehramt.idFach, 2, lehrer);
			}
			if (lehramt.istSek1 && lehramt.istSek2) {
				lehrerMengeByIdFachAndSek.add(lehramt.idFach, 3, lehrer);
			}
		}
	}


	// #####################################################################
	// ############################ Schuldaten #############################
	// #####################################################################

	/**
	 * Fügt alle {@link JahrgangsDaten}-Objekte hinzu.
	 * @param jahrgaenge eine Sammlung der hinzuzufügenden {@link JahrgangsDaten}-Objekte
	 */
	private void jahrgangsdatenAddAll(final @NotNull Collection<JahrgangsDaten> jahrgaenge) {
		jahrgangById = new HashMap<>();
		for (final @NotNull JahrgangsDaten jd : jahrgaenge) {
			DeveloperNotificationException.ifMapPutOverwrites(jahrgangById, jd.id, jd);
		}
	}

	private @NotNull Map<Long, JahrgangsDaten> jahrgangsdatenByIdMapGet() {
		return DeveloperNotificationException.ifNull("Keine Jahrgangsdaten im UvManager enthalten.", jahrgangById);
	}

	/**
	 * Liefert die Jahrgangsdaten zu einer ID
	 * @param id die ID, zu der die Jahrgangsdaten gesucht werden
	 * @return die {@link JahrgangsDaten}
	 */
	public @NotNull JahrgangsDaten jahrgangsdatenGetById(final long id) {
		return DeveloperNotificationException.ifNull("Jahrgang mit ID %d nicht im UvManager enthalten.".formatted(id), jahrgangsdatenByIdMapGet().get(id));
	}

	/**
	 * Gibt alle {@link JahrgangsDaten}-Objekte zurück.
	 * Hinweis: Wenn es keine {@link JahrgangsDaten}-Objekte gibt, ist die Liste leer.
	 * @return eine List aller {@link JahrgangsDaten}-Objekte
	 */
	public @NotNull List<JahrgangsDaten> jahrgangsdatenGetMenge() {
		if (jahrgangById == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(jahrgangById.values());
	}

	/**
	 * Liefert das zur Stundentafel zugehörige {@link JahrgangsDaten}-Objekt. <br>
	 * @param st die {@link UvStundentafel}
	 * @return das zur Stundentafel zugehörige {@link JahrgangsDaten}-Objekt.
	 */
	public @NotNull JahrgangsDaten jahrgangsdatenGetByStundentafel(final @NotNull UvStundentafel st) {
		return DeveloperNotificationException.ifMapGetIsNull(jahrgangsdatenByIdMapGet(), st.idJahrgang);
	}

	/**
	 * Fügt alle {@link FachDaten}-Objekte hinzu.
	 * @param fachdaten eine Sammlung der hinzuzufügenden {@link FachDaten}-Objekte
	 */
	private void fachdatenAddAll(final @NotNull Collection<FachDaten> fachdaten) {
		fachdatenById = new HashMap<>();
		for (final @NotNull FachDaten fd : fachdaten) {
			DeveloperNotificationException.ifMapPutOverwrites(fachdatenById, fd.id, fd);
		}
	}

	/**
	 * Gibt alle {@link FachDaten}-Objekte zurück.
	 * @return eine List aller {@link FachDaten}-Objekte
	 */
	public @NotNull List<FachDaten> fachdatenGetMenge() {
		return new ArrayList<>(DeveloperNotificationException.ifNull("Fachdaten nicht im UvManager enthalten.", fachdatenById).values());
	}

	/**
	 * Liefert das zum {@link UvStundentafelFach} zugehörige {@link FachDaten}-Objekt. <br>
	 * @param stf das {@link UvStundentafelFach}
	 * @return das zum {@link UvStundentafelFach} zugehörige {@link FachDaten}-Objekt.
	 */
	public @NotNull FachDaten fachdatenGetByStundentafelFach(final @NotNull UvStundentafelFach stf) {
		return fachdatenGetByFach(fachGetByStundentafelFach(stf));
	}

	/**
	 * Liefert das zum {@link UvFach} zugehörige {@link FachDaten}-Objekt. <br>
	 * @param fach das {@link UvFach}
	 * @return das zum {@link UvFach} zugehörige {@link FachDaten}-Objekt.
	 */
	public @NotNull FachDaten fachdatenGetByFach(final @NotNull UvFach fach) {
		return DeveloperNotificationException.ifMapGetIsNull(DeveloperNotificationException.ifNull("FachDaten nicht im UvManager enthalten.", fachdatenById),
				fach.idFach);
	}

	/**
	 * Liefert das zum {@link LehrerUnterrichtsfach} zugehörige {@link FachDaten}-Objekt. <br>
	 * @param fach das {@link LehrerUnterrichtsfach}
	 * @return das zum {@link LehrerUnterrichtsfach} zugehörige {@link FachDaten}-Objekt.
	 */
	public @NotNull FachDaten fachdatenGetByLehrerUnterrichtsfach(final @NotNull LehrerUnterrichtsfach fach) {
		return DeveloperNotificationException.ifMapGetIsNull(DeveloperNotificationException.ifNull("FachDaten nicht im UvManager enthalten.", fachdatenById),
				fach.idFach);
	}



	// #####################################################################
	// #################### Planungsabschnitte #############################
	// #####################################################################

	/**
	 * Fügt ein {@link UvPlanungsabschnitt}-Objekt hinzu.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}, der hinzugefügt werden soll
	 */
	public void planungsabschnittAdd(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		planungsabschnittAddOhneUpdate(planungsabschnitt);
		updateAll();
	}

	private void planungsabschnittAddOhneUpdate(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		planungsabschnittById.put(planungsabschnitt.id, planungsabschnitt);
	}

	/**
	 * Ersetzt das vorhandene {@link UvPlanungsabschnitt}-Objekt durch das neue DTO ohne Löschkaskade
	 * und baut die Indizes neu auf. Das bisherige Objekt wird nicht verändert.
	 *
	 * @param planungsabschnitt die vollständigen neuen Daten mit unveränderter ID
	 */
	public void planungsabschnittPatchAttributes(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		planungsabschnittGetByIdOrException(planungsabschnitt.id);
		planungsabschnittById.put(planungsabschnitt.id, planungsabschnitt);
		updateAll();
	}


	/**
	 * Liefert das zur ID zugehörige {@link UvPlanungsabschnitt}-Objekt. <br>
	 * @param idPlanungsabschnitt   Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvPlanungsabschnitt}-Objekt.
	 */
	public @NotNull UvPlanungsabschnitt planungsabschnittGetByIdOrException(final long idPlanungsabschnitt) {
		return DeveloperNotificationException.ifMapGetIsNull(planungsabschnittById, idPlanungsabschnitt);
	}

	/**
	 * Prüft, ob ein {@link UvPlanungsabschnitt}-Objekt im Manager existiert.
	 * @param planungsabschnitt das zu prüfende {@link UvPlanungsabschnitt}-Objekt
	 * @return {@code true}, wenn das Objekt im Manager existiert, sonst {@code false}
	 */
	public boolean planungsabschnittIsGeladen(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return planungsabschnittById.containsKey(planungsabschnitt.id);
	}



	// #####################################################################
	// ########################### UvLehrer ################################
	// #####################################################################

	private void updateLehrerMenge() {
		lehrerMenge.clear();
		lehrerMenge.addAll(lehrerById.values());
		lehrerMenge.sort(compLehrer);
	}

	/**
	 * Fügt ein {@link UvLehrer}-Objekt hinzu.
	 * @param lehrer Das {@link UvLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lehrerAdd(final @NotNull UvLehrer lehrer) {
		lehrerAddAll(ListUtils.create1(lehrer));
	}

	private void lehrerAddOhneUpdate(final @NotNull UvLehrer lehrer) {
		lehrerAddAllOhneUpdate(ListUtils.create1(lehrer));
	}

	/**
	 * Fügt alle {@link UvLehrer}-Objekte hinzu.
	 * @param listLehrer Die Menge der {@link UvLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lehrerAddAll(final @NotNull Collection<UvLehrer> listLehrer) {
		lehrerAddAllOhneUpdate(listLehrer);
		updateAll();
	}

	private void lehrerAddAllOhneUpdate(final @NotNull Collection<UvLehrer> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvLehrer lehrer : list) {
			lehrerCheck(lehrer);
			DeveloperNotificationException.ifTrue("lehrerAddAllOhneUpdate: ID=" + lehrer.id + " existiert bereits!",
					lehrerById.containsKey(lehrer.id));
			DeveloperNotificationException.ifTrue("lehrerAddAllOhneUpdate: ID=" + lehrer.id + " doppelt in der Liste!",
					!setOfIDs.add(lehrer.id));
		}
		// add all
		for (final @NotNull UvLehrer lehrer : list) {
			DeveloperNotificationException.ifMapPutOverwrites(lehrerById, lehrer.id, lehrer);
		}
	}

	private static void lehrerCheck(final @NotNull UvLehrer lehrer) {
		DeveloperNotificationException.ifInvalidID("lehrer.id", lehrer.id);
		DeveloperNotificationException.ifFalse("Zugangsdatum von Lehrer mit ID %d nicht gültig.".formatted(lehrer.id),
				((lehrer.datumZugang == null) || lehrer.datumZugang.trim().isEmpty()) || DateUtils.isValidDate(lehrer.datumZugang));
		DeveloperNotificationException.ifFalse("Abgangsdatum von Lehrer mit ID %d nicht gültig.".formatted(lehrer.id),
				(lehrer.datumAbgang == null) || DateUtils.isValidDate(lehrer.datumAbgang));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLehrer}-Objekt. <br>
	 * @param idLehrer Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvLehrer}-Objekt.
	 */
	public @NotNull UvLehrer lehrerGetByIdOrException(final long idLehrer) {
		return DeveloperNotificationException.ifMapGetIsNull(lehrerById, idLehrer);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvLehrer}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idLehrer die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvLehrer lehrerGetByIdOrNull(final long idLehrer) {
		return lehrerById.get(idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrer}-Objekte. <br>
	 * @return eine Liste aller {@link UvLehrer}-Objekte.
	 */
	public @NotNull List<UvLehrer> lehrerGetMengeAsList() {
		return new ArrayList<>(lehrerMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvLehrer}-Objekt durch das neue Objekt.
	 * @param lehrer Das neue {@link UvLehrer}-Objekt.
	 */
	public void lehrerAllPatchAttributes(final @NotNull Collection<UvLehrer> lehrer) {
		lehrerAllPatchAttributesOhneUpdate(lehrer);
		updateAll();
	}

	private void lehrerAllPatchAttributesOhneUpdate(final @NotNull Collection<UvLehrer> lehrer) {
		// Nur die Datensätze austauschen; abhängige Zuordnungen bleiben erhalten.
		for (final @NotNull UvLehrer neu : new HashSet<>(lehrer)) {
			DeveloperNotificationException.ifMapRemoveFailes(lehrerById, neu.id);
		}
		lehrerAddAllOhneUpdate(lehrer);
	}

	private void lehrerRemoveOhneUpdateById(final long idLehrer) {
		final @NotNull UvLehrer lehrer = lehrerGetByIdOrException(idLehrer);
		planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.removeAllByKey2(idLehrer);
		lehrerAnrechnungsstundenRemoveAllOhneUpdate(lehrerAnrechnungsstundenGetMengeByLehrer(lehrer));
		lehrerPflichtstundensollRemoveAllOhneUpdate(lehrerPflichtstundensollGetMengeByLehrer(lehrer));
		klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.removeAllByKey3(idLehrer);
		final @NotNull List<LehrerUnterrichtsfach> unterrichtsfaecher = new ArrayList<>();
		for (final @NotNull LehrerUnterrichtsfach unterrichtsfach : lehrerUnterrichtsfachMenge) {
			if ((!unterrichtsfach.istKLehrer && (unterrichtsfach.idLehrer == idLehrer))
					|| (unterrichtsfach.istKLehrer && (lehrer.idKLehrer != null) && (unterrichtsfach.idLehrer == lehrer.idKLehrer))) {
				unterrichtsfaecher.add(unterrichtsfach);
			}
		}
		lehrerUnterrichtsfachRemoveAllOhneUpdate(unterrichtsfaecher);
		DeveloperNotificationException.ifMapRemoveFailes(lehrerById, idLehrer);
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrer}-Objekt.
	 * @param idLehrer Die ID des {@link UvLehrer}-Objekts.
	 */
	public void lehrerRemoveById(final long idLehrer) {
		lehrerRemoveOhneUpdateById(idLehrer);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrer}-Objekt.
	 * @param lehrer das zu entfernende {@link UvLehrer}-Objekt.
	 */
	public void lehrerRemove(final @NotNull UvLehrer lehrer) {
		lehrerRemoveOhneUpdate(lehrer);
		updateAll();
	}

	private void lehrerRemoveOhneUpdate(final @NotNull UvLehrer lehrer) {
		lehrerRemoveOhneUpdateById(lehrer.id);
	}

	/**
	 * Entfernt alle {@link UvLehrer}-Objekte.
	 * @param listLehrer Die Liste der zu entfernenden {@link UvLehrer}-Objekte.
	 */
	public void lehrerRemoveAll(final @NotNull Collection<UvLehrer> listLehrer) {
		lehrerRemoveAllOhneUpdate(listLehrer);
		updateAll();
	}

	private void lehrerRemoveAllOhneUpdate(final @NotNull Collection<UvLehrer> listLehrer) {
		final Set<UvLehrer> setLehrer = new HashSet<>(listLehrer);
		for (final @NotNull UvLehrer lehrer : setLehrer) {
			lehrerRemoveOhneUpdateById(lehrer.id);
		}
	}

	/**
	 * Entfernt alle {@link UvLehrer}-Objekte.
	 * @param listLehrerIds Die Liste der zu entfernenden {@link UvLehrer}-Objekte.
	 */
	public void lehrerRemoveAllById(final @NotNull List<Long> listLehrerIds) {
		for (final long idLehrer : listLehrerIds) {
			lehrerRemoveOhneUpdateById(idLehrer);
		}
		updateAll();
	}


	// #####################################################################
	// ######################## LehrerUnterrichtsfach ######################
	// #####################################################################

	private void updateLehrerUnterrichtsfachMenge() {
		lehrerUnterrichtsfachMenge.clear();
		lehrerUnterrichtsfachMenge.addAll(lehrerUnterrichtsfachByIstKLehrerAndId.getNonNullValuesAsList());
	}

	private void updateLehrerUnterrichtsfachByIdKLehrerAndIdFach() {
		lehrerUnterrichtsfachByIdKLehrerAndIdFach = new ListMap2DLongKeys<>();
		for (final @NotNull LehrerUnterrichtsfach unterrichtsfach : lehrerUnterrichtsfachMenge) {
			if (!unterrichtsfach.istKLehrer) {
				continue;
			}
			lehrerUnterrichtsfachByIdKLehrerAndIdFach.addSingle(unterrichtsfach.idLehrer,
					unterrichtsfach.idFach, unterrichtsfach);
		}
	}

	private void updateLehrerUnterrichtsfachByIdLehrerAndIdFach() {
		lehrerUnterrichtsfachByIdLehrerAndIdFach = new ListMap2DLongKeys<>();
		for (final @NotNull LehrerUnterrichtsfach unterrichtsfach : lehrerUnterrichtsfachMenge) {
			lehrerUnterrichtsfachByIdLehrerAndIdFach.addSingle(lehrerGetByLehrerUnterrichtsfachOrException(unterrichtsfach).id,
					unterrichtsfach.idFach, unterrichtsfach);
		}
	}

	/**
	 * Fügt ein {@link LehrerUnterrichtsfach}-Objekt hinzu.
	 * @param unterrichtsfach Das {@link LehrerUnterrichtsfach}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lehrerUnterrichtsfachAdd(final @NotNull LehrerUnterrichtsfach unterrichtsfach) {
		lehrerUnterrichtsfachAddAll(ListUtils.create1(unterrichtsfach));
	}

	/**
	 * Fügt alle {@link LehrerUnterrichtsfach}-Objekte hinzu.
	 * @param list Die Menge der {@link LehrerUnterrichtsfach}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lehrerUnterrichtsfachAddAll(final @NotNull Collection<LehrerUnterrichtsfach> list) {
		lehrerUnterrichtsfachAddAllOhneUpdate(list);
		updateAll();
	}

	private void lehrerUnterrichtsfachAddAllOhneUpdate(final @NotNull Collection<LehrerUnterrichtsfach> list) {
		for (final @NotNull LehrerUnterrichtsfach la : list) {
			lehrerUnterrichtsfachCheck(la);
			DeveloperNotificationException.ifTrue(
					"lehrerUnterrichtsfachAddAllOhneUpdate: istKLehrer=" + la.istKLehrer + ", ID=" + la.id + " existiert bereits!",
					lehrerUnterrichtsfachByIstKLehrerAndId.contains(la.istKLehrer, la.id));
			lehrerUnterrichtsfachByIstKLehrerAndId.put(la.istKLehrer, la.id, la);
		}
	}

	private static void lehrerUnterrichtsfachCheck(final @NotNull LehrerUnterrichtsfach unterrichtsfach) {
		DeveloperNotificationException.ifInvalidID("unterrichtsfach.id", unterrichtsfach.id);
		DeveloperNotificationException.ifInvalidID("unterrichtsfach.idLehrer", unterrichtsfach.idLehrer);
		DeveloperNotificationException.ifInvalidID("unterrichtsfach.idFach", unterrichtsfach.idFach);
	}

	/**
	 * Liefert das zugehörige {@link LehrerUnterrichtsfach}-Objekt. <br>
	 * @param istKLehrer gibt an, ob der Eintrag zu einem K-Lehrer gehört.
	 * @param idUnterrichtsfach Die ID des angefragten Objektes.
	 * @return das zugehörige {@link LehrerUnterrichtsfach}-Objekt.
	 */
	public @NotNull LehrerUnterrichtsfach lehrerUnterrichtsfachGetByIdOrException(final boolean istKLehrer, final long idUnterrichtsfach) {
		return lehrerUnterrichtsfachByIstKLehrerAndId.getOrException(istKLehrer, idUnterrichtsfach);
	}

	/**
	 * Liefert eine Liste aller {@link LehrerUnterrichtsfach}-Objekte. <br>
	 * @return eine Liste aller {@link LehrerUnterrichtsfach}-Objekte.
	 */
	public @NotNull List<LehrerUnterrichtsfach> lehrerUnterrichtsfachGetMengeAsList() {
		return new ArrayList<>(lehrerUnterrichtsfachMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link LehrerUnterrichtsfach}-Objekt durch das neue Objekt.
	 * @param unterrichtsfaecher Die neuen {@link LehrerUnterrichtsfach}-Objekte.
	 */
	public void lehrerUnterrichtsfachAllPatchAttributes(final @NotNull Collection<LehrerUnterrichtsfach> unterrichtsfaecher) {
		lehrerUnterrichtsfachRemoveAllOhneUpdate(unterrichtsfaecher);
		lehrerUnterrichtsfachAddAllOhneUpdate(unterrichtsfaecher);
		updateAll();
	}

	private void lehrerUnterrichtsfachRemoveOhneUpdate(final @NotNull LehrerUnterrichtsfach unterrichtsfach) {
		lehrerUnterrichtsfachByIstKLehrerAndId.removeOrException(unterrichtsfach.istKLehrer, unterrichtsfach.id);
	}

	/**
	 * Entfernt ein existierendes {@link LehrerUnterrichtsfach}-Objekt.
	 * @param istKLehrer gibt an, ob der Eintrag zu einem K-Lehrer gehört.
	 * @param idUnterrichtsfach Die ID des {@link LehrerUnterrichtsfach}-Objekts.
	 */
	public void lehrerUnterrichtsfachRemoveById(final boolean istKLehrer, final long idUnterrichtsfach) {
		lehrerUnterrichtsfachByIstKLehrerAndId.removeOrException(istKLehrer, idUnterrichtsfach);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link LehrerUnterrichtsfach}-Objekt.
	 * @param unterrichtsfach das zu entfernende {@link LehrerUnterrichtsfach}-Objekt.
	 */
	public void lehrerUnterrichtsfachRemove(final @NotNull LehrerUnterrichtsfach unterrichtsfach) {
		lehrerUnterrichtsfachRemoveOhneUpdate(unterrichtsfach);
		updateAll();
	}

	/**
	 * Entfernt alle {@link LehrerUnterrichtsfach}-Objekte.
	 * @param list Die Liste der zu entfernenden {@link LehrerUnterrichtsfach}-Objekte.
	 */
	public void lehrerUnterrichtsfachRemoveAll(final @NotNull Collection<LehrerUnterrichtsfach> list) {
		lehrerUnterrichtsfachRemoveAllOhneUpdate(list);
		updateAll();
	}

	private void lehrerUnterrichtsfachRemoveAllOhneUpdate(final @NotNull Collection<LehrerUnterrichtsfach> list) {
		final Set<LehrerUnterrichtsfach> set = new HashSet<>(list);
		for (final @NotNull LehrerUnterrichtsfach la : set) {
			lehrerUnterrichtsfachRemoveOhneUpdate(la);
		}
	}

	/**
	 * Liefert die Liste der {@link LehrerUnterrichtsfach}-Objekte zu einem {@link UvLehrer}.
	 * @param lehrer der {@link UvLehrer}
	 * @return die Liste der {@link LehrerUnterrichtsfach}-Objekte, oder eine leere Liste
	 */
	public @NotNull List<LehrerUnterrichtsfach> lehrerUnterrichtsfachGetMengeByLehrer(final @NotNull UvLehrer lehrer) {
		return lehrerUnterrichtsfachByIdLehrerAndIdFach.get1(lehrer.id);
	}


	// #####################################################################
	// #################### UvPlanungsabschnittLehrer ######################
	// #####################################################################

	private void updatePlanungsabschnittLehrerMenge() {
		planungsabschnittLehrerMenge.clear();
		planungsabschnittLehrerMenge.addAll(planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.getAllValues());
		planungsabschnittLehrerMenge.sort(compPlanungsabschnittLehrer);
	}

	/**
	 * Fügt ein {@link UvPlanungsabschnittLehrer}-Objekt hinzu.
	 * @param lehrer Das {@link UvPlanungsabschnittLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public void planungsabschnittLehrerAdd(final @NotNull UvPlanungsabschnittLehrer lehrer) {
		planungsabschnittLehrerAddAll(ListUtils.create1(lehrer));
	}

	private void planungsabschnittLehrerAddOhneUpdate(final @NotNull UvPlanungsabschnittLehrer lehrer) {
		planungsabschnittLehrerAddAllOhneUpdate(ListUtils.create1(lehrer));
	}

	/**
	 * Fügt alle {@link UvPlanungsabschnittLehrer}-Objekte hinzu.
	 * @param listLehrer Die Menge der {@link UvPlanungsabschnittLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public void planungsabschnittLehrerAddAll(final @NotNull Collection<UvPlanungsabschnittLehrer> listLehrer) {
		planungsabschnittLehrerAddAllOhneUpdate(listLehrer);
		updateAll();
	}

	private void planungsabschnittLehrerAddAllOhneUpdate(final @NotNull Collection<UvPlanungsabschnittLehrer> list) {
		// check all
		final @NotNull HashSet<UvPlanungsabschnittLehrer> setOfIDs = new HashSet<>();
		for (final @NotNull UvPlanungsabschnittLehrer lehrer : list) {
			planungsabschnittLehrerCheck(lehrer);
			DeveloperNotificationException.ifTrue("planungsabschnittLehrerAddAllOhneUpdate: ID=" + lehrer.idPlanungsabschnitt + " existiert bereits!",
					planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.containsKey12(lehrer.idPlanungsabschnitt, lehrer.idLehrer));
			DeveloperNotificationException.ifTrue("planungsabschnittLehrerAddAllOhneUpdate: ID=" + lehrer.idPlanungsabschnitt + " doppelt in der Liste!",
					!setOfIDs.add(lehrer));
		}
		// add all
		for (final @NotNull UvPlanungsabschnittLehrer lehrer : list) {
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer,
					lehrer.idPlanungsabschnitt, lehrer.idLehrer, lehrer);
		}
	}

	private void planungsabschnittLehrerCheck(final @NotNull UvPlanungsabschnittLehrer lehrer) {
		DeveloperNotificationException.ifInvalidID("lehrer.idPlanungsabschnitt", lehrer.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("lehrer.idLehrer", lehrer.idLehrer);
		if (!planungsabschnittById.containsKey(lehrer.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"planungsabschnittLehrerCheck: Der UvPlanungsabschnitt mit der ID " + lehrer.idPlanungsabschnitt + " existiert nicht.");
		}
		if (!lehrerById.containsKey(lehrer.idLehrer)) {
			throw new DeveloperNotificationException("planungsabschnittLehrerCheck: Der UvLehrer mit der ID " + lehrer.idLehrer + " existiert nicht.");
		}
	}

	private void planungsabschnittLehrerRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idLehrer) {
		planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.removeOrException(idPlanungsabschnitt, idLehrer);
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittLehrer}-Objekt.
	 * @param idPlanungsabschnitt die ID des {@link UvPlanungsabschnitt}-Objekts.
	 * @param idLehrer Die ID des {@link UvLehrer}-Objekts.
	 */
	public void planungsabschnittLehrerRemoveById(final long idPlanungsabschnitt, final long idLehrer) {
		planungsabschnittLehrerRemoveOhneUpdateById(idPlanungsabschnitt, idLehrer);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittLehrer}-Objekt.
	 * @param lehrer das zu entfernende {@link UvPlanungsabschnittLehrer}-Objekt.
	 */
	public void planungsabschnittLehrerRemove(final @NotNull UvPlanungsabschnittLehrer lehrer) {
		planungsabschnittLehrerRemoveOhneUpdate(lehrer);
		updateAll();
	}

	private void planungsabschnittLehrerRemoveOhneUpdate(final @NotNull UvPlanungsabschnittLehrer lehrer) {
		planungsabschnittLehrerRemoveOhneUpdateById(lehrer.idPlanungsabschnitt, lehrer.idLehrer);
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittLehrer}-Objekte.
	 * @param listLehrer Die Liste der zu entfernenden {@link UvPlanungsabschnittLehrer}-Objekte.
	 */
	public void planungsabschnittLehrerRemoveAll(final @NotNull Collection<UvPlanungsabschnittLehrer> listLehrer) {
		planungsabschnittLehrerRemoveAllOhneUpdate(listLehrer);
		updateAll();
	}

	private void planungsabschnittLehrerRemoveAllOhneUpdate(final @NotNull Collection<UvPlanungsabschnittLehrer> listLehrer) {
		final Set<UvPlanungsabschnittLehrer> setLehrer = new HashSet<>(listLehrer);
		for (final @NotNull UvPlanungsabschnittLehrer lehrer : setLehrer) {
			planungsabschnittLehrerRemoveOhneUpdateById(lehrer.idPlanungsabschnitt, lehrer.idLehrer);
		}
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittLehrer}-Objekte.
	 * @param idPlanungsabschnitt die ID des {@link UvPlanungsabschnitt}-Objekts.
	 * @param listLehrerIds Die Liste der zu entfernenden {@link UvPlanungsabschnittLehrer}-Objekte.
	 */
	public void planungsabschnittLehrerRemoveAllById(final long idPlanungsabschnitt, final @NotNull List<Long> listLehrerIds) {
		for (final long idLehrer : listLehrerIds) {
			planungsabschnittLehrerRemoveById(idPlanungsabschnitt, idLehrer);
		}
		updateAll();
	}

	/**
	 * Liefert die Liste aller {@link UvPlanungsabschnittLehrer}-Objekte zu einem {@link UvPlanungsabschnitt}.
	 *
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return die Liste der {@link UvPlanungsabschnittLehrer}-Objekte
	 */
	public @NotNull List<UvPlanungsabschnittLehrer> planungsabschnittLehrerGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.get1(planungsabschnitt.id);
	}


	// #####################################################################
	// #################### UvLehrerAnrechnungsstunden #####################
	// #####################################################################

	private void updateLehrerAnrechnungsstundenMenge() {
		lehrerAnrechnungsstundenMenge.clear();
		lehrerAnrechnungsstundenMenge.addAll(lehrerAnrechnungsstundenById.values());
		lehrerAnrechnungsstundenMenge.sort(compLehrerAnrechnungsstunden);
	}

	/**
	 * Fügt ein {@link UvLehrerAnrechnungsstunden}-Objekt hinzu.
	 * @param anrechnungsstunde Das {@link UvLehrerAnrechnungsstunden}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lehrerAnrechnungsstundenAdd(final @NotNull UvLehrerAnrechnungsstunden anrechnungsstunde) {
		lehrerAnrechnungsstundenAddAll(ListUtils.create1(anrechnungsstunde));
	}

	private void lehrerAnrechnungsstundenAddOhneUpdate(final @NotNull UvLehrerAnrechnungsstunden anrechnungsstunde) {
		lehrerAnrechnungsstundenAddAllOhneUpdate(ListUtils.create1(anrechnungsstunde));
	}

	/**
	 * Fügt alle {@link UvLehrerAnrechnungsstunden}-Objekte hinzu.
	 * @param listAnrechnungsstunden Die Menge der {@link UvLehrerAnrechnungsstunden}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lehrerAnrechnungsstundenAddAll(final @NotNull Collection<UvLehrerAnrechnungsstunden> listAnrechnungsstunden) {
		lehrerAnrechnungsstundenAddAllOhneUpdate(listAnrechnungsstunden);
		updateAll();
	}

	private void lehrerAnrechnungsstundenAddAllOhneUpdate(final @NotNull Collection<UvLehrerAnrechnungsstunden> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();

		for (final @NotNull UvLehrerAnrechnungsstunden anrechnungsstunde : list) {
			lehrerAnrechnungsstundenCheck(anrechnungsstunde);
			DeveloperNotificationException.ifTrue("lehrerAnrechnungsstundenAddAllOhneUpdate: ID=" + anrechnungsstunde.id + " existiert bereits!",
					lehrerAnrechnungsstundenById.containsKey(anrechnungsstunde.id));
			DeveloperNotificationException.ifTrue("lehrerAnrechnungsstundenAddAllOhneUpdate: ID=" + anrechnungsstunde.id + " doppelt in der Liste!",
					!setOfIDs.add(anrechnungsstunde.id));
		}
		// add all
		for (final @NotNull UvLehrerAnrechnungsstunden anrechnungsstunde : list) {
			DeveloperNotificationException.ifMapPutOverwrites(lehrerAnrechnungsstundenById, anrechnungsstunde.id, anrechnungsstunde);
		}
	}

	private void lehrerAnrechnungsstundenCheck(final @NotNull UvLehrerAnrechnungsstunden anrechnungsstunde) {
		DeveloperNotificationException.ifInvalidID("anrechnungsstunde.id", anrechnungsstunde.id);
		DeveloperNotificationException.ifInvalidID("anrechnungsstunde.idLehrer", anrechnungsstunde.idLehrer);
		if (!lehrerById.containsKey(anrechnungsstunde.idLehrer)) {
			throw new DeveloperNotificationException(
					"lehrerAnrechnungsstundenCheck: Der UvLehrer mit der ID " + anrechnungsstunde.idLehrer + " existiert nicht.");
		}
		DeveloperNotificationException.ifFalse("Gültigkeitsbeginn von UvLehrerAnrechnungsstunden mit ID %d nicht gültig.".formatted(anrechnungsstunde.id),
				DateUtils.isValidDate(anrechnungsstunde.gueltigVon));
		DeveloperNotificationException.ifFalse("Gültigkeitsende von UvLehrerAnrechnungsstunden mit ID %d nicht gültig.".formatted(anrechnungsstunde.id),
				(anrechnungsstunde.gueltigBis == null) || DateUtils.isValidDate(anrechnungsstunde.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLehrerAnrechnungsstunden}-Objekt. <br>
	 * @param idAnrechnungsstunde Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvLehrerAnrechnungsstunden}-Objekt.
	 */
	public @NotNull UvLehrerAnrechnungsstunden lehrerAnrechnungsstundenGetByIdOrException(final long idAnrechnungsstunde) {
		return DeveloperNotificationException.ifMapGetIsNull(lehrerAnrechnungsstundenById, idAnrechnungsstunde);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrerAnrechnungsstunden}-Objekte. <br>
	 * @return eine Liste aller {@link UvLehrerAnrechnungsstunden}-Objekte.
	 */
	public @NotNull List<UvLehrerAnrechnungsstunden> lehrerAnrechnungsstundenGetMengeAsList() {
		return new ArrayList<>(lehrerAnrechnungsstundenMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvLehrerAnrechnungsstunden}-Objekt durch das neue Objekt.
	 * @param anrechnungsstunden Die neuen {@link UvLehrerAnrechnungsstunden}-Objekte.
	 */
	public void lehrerAnrechnungsstundenAllPatchAttributes(final @NotNull Collection<UvLehrerAnrechnungsstunden> anrechnungsstunden) {
		lehrerAnrechnungsstundenAllPatchAttributesOhneUpdate(anrechnungsstunden);
		updateAll();
	}

	private void lehrerAnrechnungsstundenAllPatchAttributesOhneUpdate(final @NotNull Collection<UvLehrerAnrechnungsstunden> anrechnungsstunden) {
		lehrerAnrechnungsstundenRemoveAllOhneUpdate(anrechnungsstunden);
		lehrerAnrechnungsstundenAddAllOhneUpdate(anrechnungsstunden);
	}

	private void lehrerAnrechnungsstundenRemoveOhneUpdateById(final long idAnrechnungsstunde) {
		DeveloperNotificationException.ifMapRemoveFailes(lehrerAnrechnungsstundenById, idAnrechnungsstunde);
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrerAnrechnungsstunden}-Objekt.
	 * @param idAnrechnungsstunde Die ID des {@link UvLehrerAnrechnungsstunden}-Objekts.
	 */
	public void lehrerAnrechnungsstundenRemoveById(final long idAnrechnungsstunde) {
		lehrerAnrechnungsstundenRemoveOhneUpdateById(idAnrechnungsstunde);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrerAnrechnungsstunden}-Objekt.
	 * @param anrechnungsstunde das zu entfernende {@link UvLehrerAnrechnungsstunden}-Objekt.
	 */
	public void lehrerAnrechnungsstundenRemove(final @NotNull UvLehrerAnrechnungsstunden anrechnungsstunde) {
		lehrerAnrechnungsstundenRemoveOhneUpdate(anrechnungsstunde);
		updateAll();
	}

	private void lehrerAnrechnungsstundenRemoveOhneUpdate(final @NotNull UvLehrerAnrechnungsstunden anrechnungsstunde) {
		lehrerAnrechnungsstundenRemoveOhneUpdateById(anrechnungsstunde.id);
	}

	/**
	 * Entfernt alle {@link UvLehrerAnrechnungsstunden}-Objekte.
	 * @param listAnrechnungsstunden Die Liste der zu entfernenden {@link UvLehrerAnrechnungsstunden}-Objekte.
	 */
	public void lehrerAnrechnungsstundenRemoveAll(final @NotNull Collection<UvLehrerAnrechnungsstunden> listAnrechnungsstunden) {
		lehrerAnrechnungsstundenRemoveAllOhneUpdate(listAnrechnungsstunden);
		updateAll();
	}

	private void lehrerAnrechnungsstundenRemoveAllOhneUpdate(final @NotNull Collection<UvLehrerAnrechnungsstunden> listAnrechnungsstunden) {
		final Set<UvLehrerAnrechnungsstunden> setAnrechnungsstunden = new HashSet<>(listAnrechnungsstunden);
		for (final @NotNull UvLehrerAnrechnungsstunden anrechnungsstunde : setAnrechnungsstunden) {
			lehrerAnrechnungsstundenRemoveOhneUpdateById(anrechnungsstunde.id);
		}
	}


	// #####################################################################
	// #################### UvLehrerPflichtstundensoll #####################
	// #####################################################################

	private void updateLehrerPflichtstundensollMenge() {
		lehrerPflichtstundensollMenge.clear();
		lehrerPflichtstundensollMenge.addAll(lehrerPflichtstundensollById.values());
		lehrerPflichtstundensollMenge.sort(compLehrerPflichtstundensoll);
	}

	/**
	 * Fügt ein {@link UvLehrerPflichtstundensoll}-Objekt hinzu.
	 * @param pflichtstundensoll Das {@link UvLehrerPflichtstundensoll}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lehrerPflichtstundensollAdd(final @NotNull UvLehrerPflichtstundensoll pflichtstundensoll) {
		lehrerPflichtstundensollAddAll(ListUtils.create1(pflichtstundensoll));
	}

	private void lehrerPflichtstundensollAddOhneUpdate(final @NotNull UvLehrerPflichtstundensoll pflichtstundensoll) {
		lehrerPflichtstundensollAddAllOhneUpdate(ListUtils.create1(pflichtstundensoll));
	}

	/**
	 * Fügt alle {@link UvLehrerPflichtstundensoll}-Objekte hinzu.
	 * @param listPflichtstundensoll Die Menge der {@link UvLehrerPflichtstundensoll}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lehrerPflichtstundensollAddAll(final @NotNull Collection<UvLehrerPflichtstundensoll> listPflichtstundensoll) {
		lehrerPflichtstundensollAddAllOhneUpdate(listPflichtstundensoll);
		updateAll();
	}

	private void lehrerPflichtstundensollAddAllOhneUpdate(final @NotNull Collection<UvLehrerPflichtstundensoll> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();

		for (final @NotNull UvLehrerPflichtstundensoll pflichtstundensoll : list) {
			lehrerPflichtstundensollCheck(pflichtstundensoll);
			DeveloperNotificationException.ifTrue("lehrerPflichtstundensollAddAllOhneUpdate: ID=" + pflichtstundensoll.id + " existiert bereits!",
					lehrerPflichtstundensollById.containsKey(pflichtstundensoll.id));
			DeveloperNotificationException.ifTrue("lehrerPflichtstundensollAddAllOhneUpdate: ID=" + pflichtstundensoll.id + " doppelt in der Liste!",
					!setOfIDs.add(pflichtstundensoll.id));
		}
		// add all
		for (final @NotNull UvLehrerPflichtstundensoll pflichtstundensoll : list) {
			DeveloperNotificationException.ifMapPutOverwrites(lehrerPflichtstundensollById, pflichtstundensoll.id, pflichtstundensoll);
		}
	}

	private void lehrerPflichtstundensollCheck(final @NotNull UvLehrerPflichtstundensoll pflichtstundensoll) {
		DeveloperNotificationException.ifInvalidID("pflichtstundensoll.id", pflichtstundensoll.id);
		DeveloperNotificationException.ifInvalidID("pflichtstundensoll.idLehrer", pflichtstundensoll.idLehrer);
		if (!lehrerById.containsKey(pflichtstundensoll.idLehrer)) {
			throw new DeveloperNotificationException(
					"lehrerPflichtstundensollCheck: Der UvLehrer mit der ID " + pflichtstundensoll.idLehrer + " existiert nicht.");
		}
		DeveloperNotificationException.ifFalse("Gültigkeitsbeginn von UvLehrerPflichtstundensoll mit ID %d nicht gültig.".formatted(pflichtstundensoll.id),
				DateUtils.isValidDate(pflichtstundensoll.gueltigVon));
		DeveloperNotificationException.ifFalse("Gültigkeitsende von UvLehrerPflichtstundensoll mit ID %d nicht gültig.".formatted(pflichtstundensoll.id),
				(pflichtstundensoll.gueltigBis == null) || DateUtils.isValidDate(pflichtstundensoll.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLehrerPflichtstundensoll}-Objekt. <br>
	 * @param idPflichtstundensoll Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvLehrerPflichtstundensoll}-Objekt.
	 */
	public @NotNull UvLehrerPflichtstundensoll lehrerPflichtstundensollGetByIdOrException(final long idPflichtstundensoll) {
		return DeveloperNotificationException.ifMapGetIsNull(lehrerPflichtstundensollById, idPflichtstundensoll);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrerPflichtstundensoll}-Objekte. <br>
	 * @return eine Liste aller {@link UvLehrerPflichtstundensoll}-Objekte.
	 */
	public @NotNull List<UvLehrerPflichtstundensoll> lehrerPflichtstundensollGetMengeAsList() {
		return new ArrayList<>(lehrerPflichtstundensollMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvLehrerPflichtstundensoll}-Objekt durch das neue Objekt.
	 * @param pflichtstundensoll Die neuen {@link UvLehrerPflichtstundensoll}-Objekte.
	 */
	public void lehrerPflichtstundensollAllPatchAttributes(final @NotNull Collection<UvLehrerPflichtstundensoll> pflichtstundensoll) {
		lehrerPflichtstundensollAllPatchAttributesOhneUpdate(pflichtstundensoll);
		updateAll();
	}

	private void lehrerPflichtstundensollAllPatchAttributesOhneUpdate(final @NotNull Collection<UvLehrerPflichtstundensoll> pflichtstundensoll) {
		lehrerPflichtstundensollRemoveAllOhneUpdate(pflichtstundensoll);
		lehrerPflichtstundensollAddAllOhneUpdate(pflichtstundensoll);
	}

	private void lehrerPflichtstundensollRemoveOhneUpdateById(final long idPflichtstundensoll) {
		DeveloperNotificationException.ifMapRemoveFailes(lehrerPflichtstundensollById, idPflichtstundensoll);
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrerPflichtstundensoll}-Objekt.
	 * @param idPflichtstundensoll Die ID des {@link UvLehrerPflichtstundensoll}-Objekts.
	 */
	public void lehrerPflichtstundensollRemoveById(final long idPflichtstundensoll) {
		lehrerPflichtstundensollRemoveOhneUpdateById(idPflichtstundensoll);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLehrerPflichtstundensoll}-Objekt.
	 * @param pflichtstundensoll das zu entfernende {@link UvLehrerPflichtstundensoll}-Objekt.
	 */
	public void lehrerPflichtstundensollRemove(final @NotNull UvLehrerPflichtstundensoll pflichtstundensoll) {
		lehrerPflichtstundensollRemoveOhneUpdate(pflichtstundensoll);
		updateAll();
	}

	private void lehrerPflichtstundensollRemoveOhneUpdate(final @NotNull UvLehrerPflichtstundensoll pflichtstundensoll) {
		lehrerPflichtstundensollRemoveOhneUpdateById(pflichtstundensoll.id);
	}

	/**
	 * Entfernt alle {@link UvLehrerPflichtstundensoll}-Objekte.
	 * @param listPflichtstundensoll Die Liste der zu entfernenden {@link UvLehrerPflichtstundensoll}-Objekte.
	 */
	public void lehrerPflichtstundensollRemoveAll(final @NotNull Collection<UvLehrerPflichtstundensoll> listPflichtstundensoll) {
		lehrerPflichtstundensollRemoveAllOhneUpdate(listPflichtstundensoll);
		updateAll();
	}

	private void lehrerPflichtstundensollRemoveAllOhneUpdate(final @NotNull Collection<UvLehrerPflichtstundensoll> listPflichtstundensoll) {
		final Set<UvLehrerPflichtstundensoll> setPflichtstundensoll = new HashSet<>(listPflichtstundensoll);
		for (final @NotNull UvLehrerPflichtstundensoll pflichtstundensoll : setPflichtstundensoll) {
			lehrerPflichtstundensollRemoveOhneUpdateById(pflichtstundensoll.id);
		}
	}


	// #####################################################################
	// ############################# UvRaum ################################
	// #####################################################################

	private void updateRaumMenge() {
		raumMenge.clear();
		raumMenge.addAll(raumByIdRaumgruppeAndIdRaum.getAllValues());
		raumMenge.sort(compRaum);
	}

	/**
	 * Fügt ein {@link UvRaum}-Objekt hinzu.
	 * @param raum Das {@link UvRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public void raumAdd(final @NotNull UvRaum raum) {
		raumAddAll(ListUtils.create1(raum));
	}

	private void raumAddOhneUpdate(final @NotNull UvRaum raum) {
		raumAddAllOhneUpdate(ListUtils.create1(raum));
	}

	/**
	 * Fügt alle {@link UvRaum}-Objekte hinzu.
	 * @param listRaeume Die Menge der {@link UvRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public void raumAddAll(final @NotNull Collection<UvRaum> listRaeume) {
		raumAddAllOhneUpdate(listRaeume);
		updateAll();
	}

	private void raumAddAllOhneUpdate(final @NotNull Collection<UvRaum> list) {
		final Set<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvRaum raum : list) {
			raumCheck(raum);
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " existiert bereits!",
					raumByIdRaumgruppeAndIdRaum.containsKey2(raum.id));
			DeveloperNotificationException.ifTrue("raumAddAllOhneUpdate: ID=" + raum.id + " doppelt in der Liste!", !setOfIDs.add(raum.id));
		}
		for (final @NotNull UvRaum raum : list) {
			DeveloperNotificationException.ifListMap2DLongKeysPutOverwrites(raumByIdRaumgruppeAndIdRaum,
					(raum.idRaumgruppe == null) ? -1L : raum.idRaumgruppe, raum.id, raum);
		}
	}

	private void raumCheck(final @NotNull UvRaum raum) {
		DeveloperNotificationException.ifInvalidID("raum.id", raum.id);
		if (raum.idRaumgruppe != null) {
			DeveloperNotificationException.ifInvalidID("raum.idRaumgruppe", raum.idRaumgruppe);
			if (!raumgruppeById.containsKey(raum.idRaumgruppe)) {
				throw new DeveloperNotificationException("raumCheck: Die UvRaumgruppe mit der ID " + raum.idRaumgruppe + " existiert nicht.");
			}
		}
		DeveloperNotificationException.ifFalse("Gültigkeitsbeginn von UvRaum mit ID %d nicht gültig.".formatted(raum.id),
				DateUtils.isValidDate(raum.gueltigVon));
		DeveloperNotificationException.ifFalse("Gültigkeitsende von UvRaum mit ID %d nicht gültig.".formatted(raum.id),
				(raum.gueltigBis == null) || DateUtils.isValidDate(raum.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvRaum}-Objekt. <br>
	 * @param idRaum Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvRaum}-Objekt.
	 */
	public @NotNull UvRaum raumGetByIdOrException(final long idRaum) {
		return raumByIdRaumgruppeAndIdRaum.getSingle2OrException(idRaum);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvRaum}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idRaum die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvRaum raumGetByIdOrNull(final long idRaum) {
		return raumByIdRaumgruppeAndIdRaum.getSingle2OrNull(idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link UvRaum}-Objekte. <br>
	 * @return eine Liste aller {@link UvRaum}-Objekte.
	 */
	public @NotNull List<UvRaum> raumGetMengeAsList() {
		return new ArrayList<>(raumMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvRaum}-Objekt durch das neue Objekt.
	 * @param raeume Die neuen {@link UvRaum}-Objekte.
	 */
	public void raumAllPatchAttributes(final @NotNull Collection<UvRaum> raeume) {
		raumAllPatchAttributesOhneUpdate(raeume);
		updateAll();
	}

	private void raumAllPatchAttributesOhneUpdate(final @NotNull Collection<UvRaum> raeume) {
		raumRemoveAllOhneUpdate(raeume);
		raumAddAllOhneUpdate(raeume);
	}

	private void raumRemoveOhneUpdateById(final long idRaum) {
		raumByIdRaumgruppeAndIdRaum.removeAllByKey2(idRaum);
	}

	/**
	 * Entfernt ein existierendes {@link UvRaum}-Objekt.
	 * @param idRaum Die ID des {@link UvRaum}-Objekts.
	 */
	public void raumRemoveById(final long idRaum) {
		raumRemoveOhneUpdateById(idRaum);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvRaum}-Objekt.
	 * @param raum das zu entfernende {@link UvRaum}-Objekt.
	 */
	public void raumRemove(final @NotNull UvRaum raum) {
		raumRemoveOhneUpdate(raum);
		updateAll();
	}

	private void raumRemoveOhneUpdate(final @NotNull UvRaum raum) {
		raumRemoveOhneUpdateById(raum.id);
	}

	/**
	 * Entfernt alle {@link UvRaum}-Objekte.
	 * @param listRaeume Die Liste der zu entfernenden {@link UvRaum}-Objekte.
	 */
	public void raumRemoveAll(final @NotNull Collection<UvRaum> listRaeume) {
		raumRemoveAllOhneUpdate(listRaeume);
		updateAll();
	}

	private void raumRemoveAllOhneUpdate(final @NotNull Collection<UvRaum> listRaeume) {
		final Set<UvRaum> setRaeume = new HashSet<>(listRaeume);
		for (final @NotNull UvRaum raum : setRaeume) {
			raumRemoveOhneUpdateById(raum.id);
		}
	}

	/**
	 * Entfernt alle {@link UvRaum}-Objekte.
	 * @param listRaumIds Die Liste der zu entfernenden {@link UvRaum}-Objekte.
	 */
	public void raumRemoveAllById(final @NotNull List<Long> listRaumIds) {
		for (final long idRaum : listRaumIds) {
			raumRemoveOhneUpdateById(idRaum);
		}
		updateAll();
	}


	// #####################################################################
	// ########################## UvRaumgruppe #############################
	// #####################################################################

	private void updateRaumgruppeMenge() {
		raumgruppeMenge.clear();
		raumgruppeMenge.addAll(raumgruppeById.values());
		raumgruppeMenge.sort(compRaumgruppe);
	}

	/**
	 * Fügt eine {@link UvRaumgruppe} hinzu.
	 * @param raumgruppe Die {@link UvRaumgruppe}, die hinzugefügt werden soll.
	 */
	public void raumgruppeAdd(final @NotNull UvRaumgruppe raumgruppe) {
		raumgruppeAddAll(ListUtils.create1(raumgruppe));
	}

	private void raumgruppeAddOhneUpdate(final @NotNull UvRaumgruppe raumgruppe) {
		raumgruppeAddAllOhneUpdate(ListUtils.create1(raumgruppe));
	}

	/**
	 * Fügt alle {@link UvRaumgruppe}-Objekte hinzu.
	 * @param listRaumgruppen Die Menge der {@link UvRaumgruppe}-Objekte, welche hinzugefügt werden soll.
	 */
	public void raumgruppeAddAll(final @NotNull Collection<UvRaumgruppe> listRaumgruppen) {
		raumgruppeAddAllOhneUpdate(listRaumgruppen);
		updateAll();
	}

	private void raumgruppeAddAllOhneUpdate(final @NotNull Collection<UvRaumgruppe> list) {
		final Set<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvRaumgruppe raumgruppe : list) {
			raumgruppeCheck(raumgruppe);
			DeveloperNotificationException.ifTrue("raumgruppeAddAllOhneUpdate: ID=" + raumgruppe.id + " existiert bereits!",
					raumgruppeById.containsKey(raumgruppe.id));
			DeveloperNotificationException.ifTrue("raumgruppeAddAllOhneUpdate: ID=" + raumgruppe.id + " doppelt in der Liste!", !setOfIDs.add(raumgruppe.id));
		}
		for (final @NotNull UvRaumgruppe raumgruppe : list) {
			DeveloperNotificationException.ifMapPutOverwrites(raumgruppeById, raumgruppe.id, raumgruppe);
		}
	}

	private static void raumgruppeCheck(final @NotNull UvRaumgruppe raumgruppe) {
		DeveloperNotificationException.ifInvalidID("raumgruppe.id", raumgruppe.id);
		DeveloperNotificationException.ifFalse("Gültigkeitsbeginn von UvRaumgruppe mit ID %d nicht gültig.".formatted(raumgruppe.id),
				DateUtils.isValidDate(raumgruppe.gueltigVon));
		DeveloperNotificationException.ifFalse("Gültigkeitsende von UvRaumgruppe mit ID %d nicht gültig.".formatted(raumgruppe.id),
				(raumgruppe.gueltigBis == null) || DateUtils.isValidDate(raumgruppe.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvRaumgruppe}-Objekt. <br>
	 * @param idRaumgruppe Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvRaumgruppe}-Objekt.
	 */
	public @NotNull UvRaumgruppe raumgruppeGetByIdOrException(final long idRaumgruppe) {
		return DeveloperNotificationException.ifMapGetIsNull(raumgruppeById, idRaumgruppe);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvRaumgruppe}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idRaumgruppe die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvRaumgruppe raumgruppeGetByIdOrNull(final long idRaumgruppe) {
		return raumgruppeById.get(idRaumgruppe);
	}

	/**
	 * Liefert eine Liste aller {@link UvRaumgruppe}-Objekte. <br>
	 * @return eine Liste aller {@link UvRaumgruppe}-Objekte.
	 */
	public @NotNull List<UvRaumgruppe> raumgruppeGetMengeAsList() {
		return new ArrayList<>(raumgruppeMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvRaumgruppe}-Objekt durch das neue Objekt.
	 * @param raumgruppen Die neuen {@link UvRaumgruppe}-Objekte.
	 */
	public void raumgruppeAllPatchAttributes(final @NotNull Collection<UvRaumgruppe> raumgruppen) {
		raumgruppeAllPatchAttributesOhneUpdate(raumgruppen);
		updateAll();
	}

	private void raumgruppeAllPatchAttributesOhneUpdate(final @NotNull Collection<UvRaumgruppe> raumgruppen) {
		for (final @NotNull UvRaumgruppe neu : new HashSet<>(raumgruppen)) {
			DeveloperNotificationException.ifMapRemoveFailes(raumgruppeById, neu.id);
		}
		raumgruppeAddAllOhneUpdate(raumgruppen);
	}

	private void raumgruppeRemoveOhneUpdateById(final long idRaumgruppe) {
		DeveloperNotificationException.ifMapRemoveFailes(raumgruppeById, idRaumgruppe);
		for (final @NotNull UvRaum raum : raumByIdRaumgruppeAndIdRaum.get1(idRaumgruppe)) {
			raum.idRaumgruppe = null;
		}
	}

	/**
	 * Entfernt ein existierendes {@link UvRaumgruppe}-Objekt.
	 * @param idRaumgruppe Die ID des {@link UvRaumgruppe}-Objekts.
	 */
	public void raumgruppeRemoveById(final long idRaumgruppe) {
		raumgruppeRemoveOhneUpdateById(idRaumgruppe);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvRaumgruppe}-Objekt.
	 * @param raumgruppe das zu entfernende {@link UvRaumgruppe}-Objekt.
	 */
	public void raumgruppeRemove(final @NotNull UvRaumgruppe raumgruppe) {
		raumgruppeRemoveOhneUpdate(raumgruppe);
		updateAll();
	}

	private void raumgruppeRemoveOhneUpdate(final @NotNull UvRaumgruppe raumgruppe) {
		raumgruppeRemoveOhneUpdateById(raumgruppe.id);
	}

	/**
	 * Entfernt alle {@link UvRaumgruppe}-Objekte.
	 * @param listRaumgruppen Die Liste der zu entfernenden {@link UvRaumgruppe}-Objekte.
	 */
	public void raumgruppeRemoveAll(final @NotNull Collection<UvRaumgruppe> listRaumgruppen) {
		raumgruppeRemoveAllOhneUpdate(listRaumgruppen);
		updateAll();
	}

	private void raumgruppeRemoveAllOhneUpdate(final @NotNull Collection<UvRaumgruppe> listRaumgruppen) {
		final Set<UvRaumgruppe> setRaumgruppen = new HashSet<>(listRaumgruppen);
		for (final @NotNull UvRaumgruppe raumgruppe : setRaumgruppen) {
			raumgruppeRemoveOhneUpdateById(raumgruppe.id);
		}
	}


	// #####################################################################
	// ######################### UvStundentafel ############################
	// #####################################################################

	private void updateStundentafelMenge() {
		stundentafelMenge.clear();
		stundentafelMenge.addAll(stundentafelById.values());
	}

	/**
	 * Fügt ein {@link UvStundentafel}-Objekt hinzu.
	 * @param stundentafel Das {@link UvStundentafel}-Objekt, welches hinzugefügt werden soll.
	 */
	public void stundentafelAdd(final @NotNull UvStundentafel stundentafel) {
		stundentafelAddAll(ListUtils.create1(stundentafel));
	}

	private void stundentafelAddOhneUpdate(final @NotNull UvStundentafel stundentafel) {
		stundentafelAddAllOhneUpdate(ListUtils.create1(stundentafel));
	}

	/**
	 * Fügt alle {@link UvStundentafel}-Objekte hinzu.
	 * @param listStundentafeln Die Menge der {@link UvStundentafel}-Objekte, welche hinzugefügt werden soll.
	 */
	public void stundentafelAddAll(final @NotNull Collection<UvStundentafel> listStundentafeln) {
		stundentafelAddAllOhneUpdate(listStundentafeln);
		updateAll();
	}

	private void stundentafelAddAllOhneUpdate(final @NotNull Collection<UvStundentafel> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvStundentafel stundentafel : list) {
			stundentafelCheck(stundentafel);
			DeveloperNotificationException.ifTrue("stundentafelAddAllOhneUpdate: ID=" + stundentafel.id + " existiert bereits!",
					stundentafelById.containsKey(stundentafel.id));
			DeveloperNotificationException.ifTrue("stundentafelAddAllOhneUpdate: ID=" + stundentafel.id + " doppelt in der Liste!",
					!setOfIDs.add(stundentafel.id));
		}
		// add all
		for (final @NotNull UvStundentafel stundentafel : list) {
			DeveloperNotificationException.ifMapPutOverwrites(stundentafelById, stundentafel.id, stundentafel);
		}
	}

	private static void stundentafelCheck(final @NotNull UvStundentafel stundentafel) {
		DeveloperNotificationException.ifInvalidID("stundentafel.id", stundentafel.id);
		DeveloperNotificationException.ifInvalidID("stundentafel.idJahrgang", stundentafel.idJahrgang);
		DeveloperNotificationException.ifFalse("Gültigkeitsbeginn von UvStundentafel mit ID %d nicht gültig.".formatted(stundentafel.id),
				DateUtils.isValidDate(stundentafel.gueltigVon));
		DeveloperNotificationException.ifFalse("Gültigkeitsende von UvStundentafel mit ID %d nicht gültig.".formatted(stundentafel.id),
				(stundentafel.gueltigBis == null) || DateUtils.isValidDate(stundentafel.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvStundentafel}-Objekt. <br>
	 * @param idStundentafel Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvStundentafel}-Objekt.
	 */
	public @NotNull UvStundentafel stundentafelGetByIdOrException(final long idStundentafel) {
		return DeveloperNotificationException.ifMapGetIsNull(stundentafelById, idStundentafel);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvStundentafel}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idStundentafel die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvStundentafel stundentafelGetByIdOrNull(final long idStundentafel) {
		return stundentafelById.get(idStundentafel);
	}

	/**
	 * Liefert eine Liste aller {@link UvStundentafel}-Objekte. <br>
	 * @return eine Liste aller {@link UvStundentafel}-Objekte.
	 */
	public @NotNull List<UvStundentafel> stundentafelGetMengeAsList() {
		return new ArrayList<>(stundentafelMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvStundentafel}-Objekt durch das neue Objekt.
	 * @param stundentafeln Die neuen {@link UvStundentafel}-Objekte.
	 */
	public void stundentafelAllPatchAttributes(final @NotNull Collection<UvStundentafel> stundentafeln) {
		stundentafelAllPatchAttributesOhneUpdate(stundentafeln);
		updateAll();
	}

	private void stundentafelAllPatchAttributesOhneUpdate(final @NotNull Collection<UvStundentafel> stundentafeln) {
		for (final @NotNull UvStundentafel neu : new HashSet<>(stundentafeln)) {
			DeveloperNotificationException.ifMapRemoveFailes(stundentafelById, neu.id);
		}
		stundentafelAddAllOhneUpdate(stundentafeln);
	}

	private void stundentafelRemoveOhneUpdateById(final long idStundentafel) {
		DeveloperNotificationException.ifMapRemoveFailes(stundentafelById, idStundentafel);
		final @NotNull List<UvStundentafelFach> listFaecher =
				stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.get1(idStundentafel);
		stundentafelFachRemoveAllOhneUpdate(listFaecher);
	}

	/**
	 * Entfernt ein existierendes {@link UvStundentafel}-Objekt.
	 * @param idStundentafel Die ID des {@link UvStundentafel}-Objekts.
	 */
	public void stundentafelRemoveById(final long idStundentafel) {
		stundentafelRemoveOhneUpdateById(idStundentafel);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvStundentafel}-Objekt.
	 * @param stundentafel das zu entfernende {@link UvStundentafel}-Objekt.
	 */
	public void stundentafelRemove(final @NotNull UvStundentafel stundentafel) {
		stundentafelRemoveOhneUpdate(stundentafel);
		updateAll();
	}

	private void stundentafelRemoveOhneUpdate(final @NotNull UvStundentafel stundentafel) {
		stundentafelRemoveOhneUpdateById(stundentafel.id);
	}

	/**
	 * Entfernt alle {@link UvStundentafel}-Objekte.
	 * @param listStundentafeln Die Liste der zu entfernenden {@link UvStundentafel}-Objekte.
	 */
	public void stundentafelRemoveAll(final @NotNull Collection<UvStundentafel> listStundentafeln) {
		stundentafelRemoveAllOhneUpdate(listStundentafeln);
		updateAll();
	}

	private void stundentafelRemoveAllOhneUpdate(final @NotNull Collection<UvStundentafel> listStundentafeln) {
		final Set<UvStundentafel> setStundentafeln = new HashSet<>(listStundentafeln);
		for (final @NotNull UvStundentafel stundentafel : setStundentafeln) {
			stundentafelRemoveOhneUpdateById(stundentafel.id);
		}
	}

	/**
	 * Entfernt alle {@link UvStundentafel}-Objekte.
	 * @param listStundentafelIds Die Liste der IDs der zu entfernenden {@link UvStundentafel}-Objekte.
	 */
	public void stundentafelRemoveAllById(final @NotNull List<Long> listStundentafelIds) {
		for (final long idStundentafel : listStundentafelIds) {
			stundentafelRemoveOhneUpdateById(idStundentafel);
		}
		updateAll();
	}


	// #####################################################################
	// ##################### UvStundentafelFach ############################
	// #####################################################################

	private void updateStundentafelFachMenge() {
		stundentafelFachMenge.clear();
		for (final long key1 : stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.keySet1()) {
			stundentafelFachMenge.addAll(stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.get1(key1));
		}
		stundentafelFachMenge.sort(compStundentafelFach);
	}

	/**
	 * Fügt ein {@link UvStundentafelFach}-Objekt hinzu.
	 * @param fach Das {@link UvStundentafelFach}-Objekt, welches hinzugefügt werden soll.
	 */
	public void stundentafelFachAdd(final @NotNull UvStundentafelFach fach) {
		stundentafelFachAddAll(ListUtils.create1(fach));
	}

	private void stundentafelFachAddOhneUpdate(final @NotNull UvStundentafelFach fach) {
		stundentafelFachAddAllOhneUpdate(ListUtils.create1(fach));
	}

	/**
	 * Fügt alle {@link UvStundentafelFach}-Objekte hinzu.
	 * @param listFaecher Die Menge der {@link UvStundentafelFach}-Objekte, welche hinzugefügt werden soll.
	 */
	public void stundentafelFachAddAll(final @NotNull Collection<UvStundentafelFach> listFaecher) {
		stundentafelFachAddAllOhneUpdate(listFaecher);
		updateAll();
	}

	private void stundentafelFachAddAllOhneUpdate(final @NotNull Collection<UvStundentafelFach> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvStundentafelFach fach : list) {
			stundentafelFachCheck(fach);
			DeveloperNotificationException.ifTrue("stundentafelFachAddAllOhneUpdate: ID=" + fach.id + " existiert bereits!",
					stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.containsKey123(fach.idStundentafel, fach.abschnitt,
							fach.idFach));
			DeveloperNotificationException.ifTrue("stundentafelFachAddAllOhneUpdate: ID=" + fach.id + " doppelt in der Liste!",
					!setOfIDs.add(fach.id));
		}
		// add all
		for (final @NotNull UvStundentafelFach fach : list) {
			stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.addSingle(fach.idStundentafel, fach.abschnitt, fach.idFach, fach.id,
					fach);
		}
	}

	private void stundentafelFachCheck(final @NotNull UvStundentafelFach fach) {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifInvalidID("fach.idStundentafel", fach.idStundentafel);
		DeveloperNotificationException.ifInvalidID("fach.idFach", fach.idFach);
		if (!stundentafelById.containsKey(fach.idStundentafel)) {
			throw new DeveloperNotificationException("stundentafelFachCheck: Die UvStundentafel mit der ID " + fach.idStundentafel + " existiert nicht.");
		}
		if (!fachById.containsKey(fach.idFach)) {
			throw new DeveloperNotificationException("stundentafelFachCheck: Das UvFach mit der ID " + fach.idFach + " existiert nicht.");
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvStundentafelFach}-Objekte. <br>
	 * @return eine Liste aller {@link UvStundentafelFach}-Objekte.
	 */
	public @NotNull List<UvStundentafelFach> stundentafelFachGetMengeAsList() {
		return new ArrayList<>(stundentafelFachMenge);
	}

	/**
	 * Liefert das zur ID gehörige {@link UvStundentafelFach}-Objekt zurück.
	 * @param idStundentafelfach die ID des {@link UvStundentafelFach}-Objekts.
	 * @return das {@link UvStundentafelFach}-Objekt
	 */
	public @NotNull UvStundentafelFach stundentafelFachGetByIdOrException(final long idStundentafelfach) {
		return stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.getSingle4OrException(idStundentafelfach);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvStundentafelFach}-Objekt durch das neue Objekt.
	 * @param faecher Die neuen {@link UvStundentafelFach}-Objekte.
	 */
	public void stundentafelFachAllPatchAttributes(final @NotNull Collection<UvStundentafelFach> faecher) {
		stundentafelFachAllPatchAttributesOhneUpdate(faecher);
		updateAll();
	}

	private void stundentafelFachAllPatchAttributesOhneUpdate(final @NotNull Collection<UvStundentafelFach> faecher) {
		for (final @NotNull UvStundentafelFach neu : new HashSet<>(faecher)) {
			stundentafelFachRemoveOhneUpdate(stundentafelFachGetByIdOrException(neu.id));
		}
		stundentafelFachAddAllOhneUpdate(faecher);
	}

	private void stundentafelFachRemoveOhneUpdateById(final long idStundentafelfach) {
		stundentafelFachRemoveOhneUpdate(stundentafelFachGetByIdOrException(idStundentafelfach));
	}

	/**
	 * Entfernt ein existierendes {@link UvStundentafelFach}-Objekt.
	 * @param idFach Die ID des {@link UvStundentafelFach}-Objekts.
	 */
	public void stundentafelFachRemoveById(final long idFach) {
		stundentafelFachRemoveOhneUpdateById(idFach);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvStundentafelFach}-Objekt.
	 * @param fach das zu entfernende {@link UvStundentafelFach}-Objekt.
	 */
	public void stundentafelFachRemove(final @NotNull UvStundentafelFach fach) {
		stundentafelFachRemoveOhneUpdate(fach);
		updateAll();
	}

	private void stundentafelFachRemoveOhneUpdate(final @NotNull UvStundentafelFach fach) {
		stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.removeOrException(fach.idStundentafel, fach.abschnitt,
				fach.idFach, fach.id);
	}

	/**
	 * Entfernt alle {@link UvStundentafelFach}-Objekte.
	 * @param listFaecher Die Liste der zu entfernenden {@link UvStundentafelFach}-Objekte.
	 */
	public void stundentafelFachRemoveAll(final @NotNull Collection<UvStundentafelFach> listFaecher) {
		stundentafelFachRemoveAllOhneUpdate(listFaecher);
		updateAll();
	}

	private void stundentafelFachRemoveAllOhneUpdate(final @NotNull Collection<UvStundentafelFach> listFaecher) {
		final Set<UvStundentafelFach> setFaecher = new HashSet<>(listFaecher);
		for (final @NotNull UvStundentafelFach fach : setFaecher) {
			stundentafelFachRemoveOhneUpdate(fach);
		}
	}

	/**
	 * Entfernt alle {@link UvStundentafelFach}-Objekte mit den angegebenen IDs.
	 * @param listIds Die Liste der IDs der zu entfernenden {@link UvStundentafelFach}-Objekte.
	 */
	public void stundentafelFachRemoveAllById(final @NotNull List<Long> listIds) {
		for (final long id : listIds) {
			stundentafelFachRemoveOhneUpdateById(id);
		}
		updateAll();
	}


	// #####################################################################
	// ############################# UvFach ################################
	// #####################################################################

	private void updateFachMenge() {
		fachMenge.clear();
		fachMenge.addAll(fachById.values());
		fachMenge.sort(compFach);
	}

	/**
	 * Fügt ein {@link UvFach}-Objekt hinzu.
	 * @param fach Das {@link UvFach}-Objekt, welches hinzugefügt werden soll.
	 */
	public void fachAdd(final @NotNull UvFach fach) {
		fachAddAll(ListUtils.create1(fach));
	}

	private void fachAddOhneUpdate(final @NotNull UvFach fach) {
		fachAddAllOhneUpdate(ListUtils.create1(fach));
	}

	/**
	 * Fügt alle {@link UvFach}-Objekte hinzu.
	 * @param listFaecher Die Menge der {@link UvFach}-Objekte, welche hinzugefügt werden soll.
	 */
	public void fachAddAll(final @NotNull Collection<UvFach> listFaecher) {
		fachAddAllOhneUpdate(listFaecher);
		updateAll();
	}

	private void fachAddAllOhneUpdate(final @NotNull Collection<UvFach> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvFach fach : list) {
			fachCheck(fach);
			DeveloperNotificationException.ifTrue("fachAddAllOhneUpdate: ID=" + fach.id + " existiert bereits!",
					fachById.containsKey(fach.id));
			DeveloperNotificationException.ifTrue("fachAddAllOhneUpdate: ID=" + fach.id + " doppelt in der Liste!",
					!setOfIDs.add(fach.id));
		}
		// add all
		for (final @NotNull UvFach fach : list) {
			DeveloperNotificationException.ifMapPutOverwrites(fachById, fach.id, fach);
		}
	}

	private static void fachCheck(final @NotNull UvFach fach) {
		DeveloperNotificationException.ifInvalidID("fach.id", fach.id);
		DeveloperNotificationException.ifInvalidID("fach.idFach", fach.idFach);
		DeveloperNotificationException.ifFalse("Gültigkeitsbeginn von UvFach mit ID %d nicht gültig.".formatted(fach.id),
				DateUtils.isValidDate(fach.gueltigVon));
		DeveloperNotificationException.ifFalse("Gültigkeitsende von UvFach mit ID %d nicht gültig.".formatted(fach.id),
				(fach.gueltigBis == null) || DateUtils.isValidDate(fach.gueltigBis));
	}

	/**
	 * Liefert das {@link UvFach}-Objekt zur angegebenen ID
	 * @param idFach die ID des Fachs
	 * @return das {@link UvFach}
	 */
	public @NotNull UvFach fachGetByIdOrException(final long idFach) {
		return DeveloperNotificationException.ifMapGetIsNull(fachById, idFach);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvFach}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idFach die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvFach fachGetByIdOrNull(final long idFach) {
		return fachById.get(idFach);
	}

	/**
	 * Liefert eine Liste aller {@link UvFach}-Objekte. <br>
	 * @return eine Liste aller {@link UvFach}-Objekte.
	 */
	public @NotNull List<UvFach> fachGetMengeAsList() {
		return new ArrayList<>(fachMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvFach}-Objekt durch das neue Objekt.
	 * @param faecher Die neuen {@link UvFach}-Objekte.
	 */
	public void fachAllPatchAttributes(final @NotNull Collection<UvFach> faecher) {
		fachAllPatchAttributesOhneUpdate(faecher);
		updateAll();
	}

	/**
	 * Aktualisiert das vorhandene {@link UvFach}-Objekt durch das neue Objekt.
	 * @param fach das neue {@link UvFach}-Objekt.
	 */
	public void fachPatchAttributes(final @NotNull UvFach fach) {
		fachRemoveOhneUpdate(fach);
		fachAddOhneUpdate(fach);
		updateAll();
	}

	private void fachAllPatchAttributesOhneUpdate(final @NotNull Collection<UvFach> faecher) {
		fachRemoveAllOhneUpdate(faecher);
		fachAddAllOhneUpdate(faecher);
	}

	private void fachRemoveOhneUpdateById(final long idFach) {
		DeveloperNotificationException.ifMapRemoveFailes(fachById, idFach);
	}

	/**
	 * Entfernt ein existierendes {@link UvFach}-Objekt.
	 * @param idFach Die ID des {@link UvFach}-Objekts.
	 */
	public void fachRemoveById(final long idFach) {
		fachRemoveOhneUpdateById(idFach);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvFach}-Objekt.
	 * @param fach das zu entfernende {@link UvFach}-Objekt.
	 */
	public void fachRemove(final @NotNull UvFach fach) {
		fachRemoveOhneUpdate(fach);
		updateAll();
	}

	private void fachRemoveOhneUpdate(final @NotNull UvFach fach) {
		fachRemoveOhneUpdateById(fach.id);
	}

	/**
	 * Entfernt alle {@link UvFach}-Objekte.
	 * @param listFaecher Die Liste der zu entfernenden {@link UvFach}-Objekte.
	 */
	public void fachRemoveAll(final @NotNull Collection<UvFach> listFaecher) {
		fachRemoveAllOhneUpdate(listFaecher);
		updateAll();
	}

	private void fachRemoveAllOhneUpdate(final @NotNull Collection<UvFach> listFaecher) {
		final Set<UvFach> setFaecher = new HashSet<>(listFaecher);
		for (final @NotNull UvFach fach : setFaecher) {
			fachRemoveOhneUpdateById(fach.id);
		}
	}

	/**
	 * Entfernt alle {@link UvFach}-Objekte mit den angegebenen IDs.
	 * @param listIds Die Liste der IDs der zu entfernenden {@link UvFach}-Objekte.
	 */
	public void fachRemoveAllById(final @NotNull List<Long> listIds) {
		for (final long id : listIds) {
			fachRemoveOhneUpdateById(id);
		}
		updateAll();
	}

	// #####################################################################
	// ########################### UvZeitraster ############################
	// #####################################################################

	private void updateZeitrasterMenge() {
		zeitrasterMenge.clear();
		zeitrasterMenge.addAll(zeitrasterById.values());
		zeitrasterMenge.sort(compZeitraster);
	}

	/**
	 * Fügt ein {@link UvZeitraster}-Objekt hinzu.
	 * @param zeitraster Das {@link UvZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public void zeitrasterAdd(final @NotNull UvZeitraster zeitraster) {
		zeitrasterAddAll(ListUtils.create1(zeitraster));
	}

	private void zeitrasterAddOhneUpdate(final @NotNull UvZeitraster zeitraster) {
		zeitrasterAddAllOhneUpdate(ListUtils.create1(zeitraster));
	}

	/**
	 * Fügt alle {@link UvZeitraster}-Objekte hinzu.
	 * @param listZeitraster Die Menge der {@link UvZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public void zeitrasterAddAll(final @NotNull Collection<UvZeitraster> listZeitraster) {
		zeitrasterAddAllOhneUpdate(listZeitraster);
		updateAll();
	}

	private void zeitrasterAddAllOhneUpdate(final @NotNull Collection<UvZeitraster> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvZeitraster zeitraster : list) {
			zeitrasterCheck(zeitraster);
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: ID=" + zeitraster.id + " existiert bereits!",
					zeitrasterById.containsKey(zeitraster.id));
			DeveloperNotificationException.ifTrue("zeitrasterAddAllOhneUpdate: ID=" + zeitraster.id + " doppelt in der Liste!",
					!setOfIDs.add(zeitraster.id));
		}
		// add all
		for (final @NotNull UvZeitraster zeitraster : list) {
			DeveloperNotificationException.ifMapPutOverwrites(zeitrasterById, zeitraster.id, zeitraster);
		}
	}

	private static void zeitrasterCheck(final @NotNull UvZeitraster zeitraster) {
		DeveloperNotificationException.ifInvalidID("zeitraster.id", zeitraster.id);
		DeveloperNotificationException.ifTrue("Bezeichnung von UvZeitraster mit ID %d darf nicht leer sein.".formatted(zeitraster.id),
				(zeitraster.bezeichnung == null) || zeitraster.bezeichnung.isBlank());
		DeveloperNotificationException.ifFalse("Gültigkeitsbeginn von UvZeitraster mit ID %d nicht gültig.".formatted(zeitraster.id),
				DateUtils.isValidDate(zeitraster.gueltigVon));
		DeveloperNotificationException.ifFalse("Gültigkeitsende von UvZeitraster mit ID %d nicht gültig.".formatted(zeitraster.id),
				(zeitraster.gueltigBis == null) || DateUtils.isValidDate(zeitraster.gueltigBis));
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvZeitraster}-Objekt. <br>
	 * @param idZeitraster Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvZeitraster}-Objekt.
	 */
	public @NotNull UvZeitraster zeitrasterGetByIdOrException(final long idZeitraster) {
		return DeveloperNotificationException.ifMapGetIsNull(zeitrasterById, idZeitraster);
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvZeitraster}-Objekt oder {@code null}. <br>
	 * @param idZeitraster Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvZeitraster}-Objekt oder {@code null}.
	 */
	public UvZeitraster zeitrasterGetByIdOrNull(final long idZeitraster) {
		return zeitrasterById.get(idZeitraster);
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitraster}-Objekte. <br>
	 * @return eine Liste aller {@link UvZeitraster}-Objekte.
	 */
	public @NotNull List<UvZeitraster> zeitrasterGetMengeAsList() {
		return new ArrayList<>(zeitrasterMenge);
	}

	/**
	 * Aktualisiert das vorhandene {@link UvZeitraster}-Objekt durch das neue Objekt.
	 * @param listZeitraster Die neuen {@link UvZeitraster}-Objekte.
	 */
	public void zeitrasterAllPatchAttributes(final @NotNull Collection<UvZeitraster> listZeitraster) {
		zeitrasterAllPatchAttributesOhneUpdate(listZeitraster);
		updateAll();
	}

	private void zeitrasterAllPatchAttributesOhneUpdate(final @NotNull Collection<UvZeitraster> listZeitraster) {
		zeitrasterRemoveAllOhneUpdate(listZeitraster);
		zeitrasterAddAllOhneUpdate(listZeitraster);
	}

	private void zeitrasterRemoveOhneUpdateById(final long idZeitraster) {
		DeveloperNotificationException.ifMapRemoveFailes(zeitrasterById, idZeitraster);
	}

	/**
	 * Entfernt ein existierendes {@link UvZeitraster}-Objekt.
	 * @param idZeitraster Die ID des {@link UvZeitraster}-Objekts.
	 */
	public void zeitrasterRemoveById(final long idZeitraster) {
		zeitrasterRemoveOhneUpdateById(idZeitraster);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvZeitraster}-Objekt.
	 * @param zeitraster das zu entfernende {@link UvZeitraster}-Objekt.
	 */
	public void zeitrasterRemove(final @NotNull UvZeitraster zeitraster) {
		zeitrasterRemoveOhneUpdate(zeitraster);
		updateAll();
	}

	private void zeitrasterRemoveOhneUpdate(final @NotNull UvZeitraster zeitraster) {
		zeitrasterRemoveOhneUpdateById(zeitraster.id);
	}

	/**
	 * Entfernt alle {@link UvZeitraster}-Objekte.
	 * @param listZeitraster Die Liste der zu entfernenden {@link UvZeitraster}-Objekte.
	 */
	public void zeitrasterRemoveAll(final @NotNull Collection<UvZeitraster> listZeitraster) {
		zeitrasterRemoveAllOhneUpdate(listZeitraster);
		updateAll();
	}

	private void zeitrasterRemoveAllOhneUpdate(final @NotNull Collection<UvZeitraster> listZeitraster) {
		final Set<UvZeitraster> setZeitraster = new HashSet<>(listZeitraster);
		for (final @NotNull UvZeitraster zeitraster : setZeitraster) {
			zeitrasterRemoveOhneUpdateById(zeitraster.id);
		}
	}

	/**
	 * Entfernt alle {@link UvZeitraster}-Objekte mit den angegebenen IDs.
	 * @param listIds Die Liste der IDs der zu entfernenden {@link UvZeitraster}-Objekte.
	 */
	public void zeitrasterRemoveAllById(final @NotNull List<Long> listIds) {
		for (final long id : listIds) {
			zeitrasterRemoveOhneUpdateById(id);
		}
		updateAll();
	}

	// #####################################################################
	// ####################### UvZeitrastereintrag #########################
	// #####################################################################

	private void updateZeitrasterEintragMenge() {
		zeitrasterEintragMenge.clear();
		zeitrasterEintragMenge.addAll(zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.getAllValues());
		zeitrasterEintragMenge.sort(compZeitrasterEintrag);
	}

	/**
	 * Fügt ein {@link UvZeitrasterEintrag}-Objekt hinzu.
	 * @param eintrag Das {@link UvZeitrasterEintrag}-Objekt, welches hinzugefügt werden soll.
	 */
	public void zeitrasterEintragAdd(final @NotNull UvZeitrasterEintrag eintrag) {
		zeitrasterEintragAddAll(ListUtils.create1(eintrag));
	}

	private void zeitrasterEintragAddOhneUpdate(final @NotNull UvZeitrasterEintrag eintrag) {
		zeitrasterEintragAddAllOhneUpdate(ListUtils.create1(eintrag));
	}

	/**
	 * Fügt alle {@link UvZeitrasterEintrag}-Objekte hinzu.
	 * @param listEintraege Die Menge der {@link UvZeitrasterEintrag}-Objekte, welche hinzugefügt werden soll.
	 */
	public void zeitrasterEintragAddAll(final @NotNull Collection<UvZeitrasterEintrag> listEintraege) {
		zeitrasterEintragAddAllOhneUpdate(listEintraege);
		updateAll();
	}

	private void zeitrasterEintragAddAllOhneUpdate(final @NotNull Collection<UvZeitrasterEintrag> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvZeitrasterEintrag eintrag : list) {
			zeitrasterEintragCheck(eintrag);
			DeveloperNotificationException.ifTrue("zeitrasterEintragAddAllOhneUpdate: ID=" + eintrag.id + " existiert bereits!",
					zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.containsKey12(eintrag.idZeitraster, eintrag.id));
			DeveloperNotificationException.ifTrue("zeitrasterEintragAddAllOhneUpdate: ID=" + eintrag.id + " doppelt in der Liste!",
					!setOfIDs.add(eintrag.id));
		}
		// add all
		for (final @NotNull UvZeitrasterEintrag eintrag : list) {
			zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.addSingle(eintrag.idZeitraster, eintrag.id, eintrag);
			zeitrasterEintragMenge.add(eintrag);
		}
	}

	private void zeitrasterEintragCheck(final @NotNull UvZeitrasterEintrag eintrag) {
		DeveloperNotificationException.ifInvalidID("eintrag.id", eintrag.id);
		DeveloperNotificationException.ifInvalidID("eintrag.idZeitraster", eintrag.idZeitraster);
		if (!zeitrasterById.containsKey(eintrag.idZeitraster)) {
			throw new DeveloperNotificationException("zeitrasterEintragCheck: Das UvZeitraster mit der ID " + eintrag.idZeitraster + " existiert nicht.");
		}
		DeveloperNotificationException.ifTrue("Wochentag von UvZeitrasterEintrag mit ID %d muss zwischen 1 und 7 liegen.".formatted(eintrag.id),
				(eintrag.wochentag < 1) || (eintrag.wochentag > 7));
		DeveloperNotificationException.ifTrue("Stunde von UvZeitrasterEintrag mit ID %d muss größer als 0 sein.".formatted(eintrag.id),
				eintrag.stunde < 1);
		DeveloperNotificationException.ifTrue("Beginn von UvZeitrasterEintrag mit ID %d muss >= 0 sein.".formatted(eintrag.id),
				eintrag.beginn < 0);
		DeveloperNotificationException.ifTrue("Ende von UvZeitrasterEintrag mit ID %d muss > Beginn sein.".formatted(eintrag.id),
				eintrag.ende <= eintrag.beginn);
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvZeitrasterEintrag}-Objekt. <br>
	 * @param idZeitraster Die ID des Zeitrasters.
	 * @param idEintrag Die ID des angefragten Eintrags.
	 * @return das zur ID zugehörige {@link UvZeitrasterEintrag}-Objekt.
	 */
	public @NotNull UvZeitrasterEintrag zeitrasterEintragGetByIdOrException(final long idZeitraster, final long idEintrag) {
		return zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.getSingle12OrException(idZeitraster, idEintrag);
	}

	/**
	 * Liefert den Zeitrastereintrag direkt anhand seiner Datensatz-ID oder {@code null}.
	 *
	 * @param idEintrag die ID des Zeitrastereintrags
	 * @return der gespeicherte Eintrag oder {@code null}, falls er nicht vorhanden ist
	 */
	public UvZeitrasterEintrag zeitrasterEintragGetByIdOrNull(final long idEintrag) {
		return zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.getSingle2OrNull(idEintrag);
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitrasterEintrag}-Objekte. <br>
	 * @return eine Liste aller {@link UvZeitrasterEintrag}-Objekte.
	 */
	public @NotNull List<UvZeitrasterEintrag> zeitrasterEintragGetMengeAsList() {
		return new ArrayList<>(zeitrasterEintragMenge);
	}



	/**
	 * Aktualisiert das vorhandene {@link UvZeitrasterEintrag}-Objekt durch das neue Objekt.
	 * @param listEintraege Die neuen {@link UvZeitrasterEintrag}-Objekte.
	 */
	public void zeitrasterEintragAllPatchAttributes(final @NotNull Collection<UvZeitrasterEintrag> listEintraege) {
		zeitrasterEintragAllPatchAttributesOhneUpdate(listEintraege);
		updateAll();
	}

	private void zeitrasterEintragAllPatchAttributesOhneUpdate(final @NotNull Collection<UvZeitrasterEintrag> listEintraege) {
		for (final @NotNull UvZeitrasterEintrag neu : new HashSet<>(listEintraege)) {
			final @NotNull UvZeitrasterEintrag alt = zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.getSingle2OrException(neu.id);
			zeitrasterEintragRemoveOhneUpdate(alt);
		}
		zeitrasterEintragAddAllOhneUpdate(listEintraege);
	}

	private void zeitrasterEintragRemoveOhneUpdateById(final long idZeitraster, final long idEintrag) {
		final @NotNull UvZeitrasterEintrag eintrag = zeitrasterEintragGetByIdOrException(idZeitraster, idEintrag);
		zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.removeSingleOrException(idZeitraster, idEintrag);
		zeitrasterEintragMenge.remove(eintrag);
	}

	/**
	 * Entfernt ein existierendes {@link UvZeitrasterEintrag}-Objekt.
	 * @param idZeitraster Die ID des Zeitrasters.
	 * @param idEintrag Die ID des {@link UvZeitrasterEintrag}-Objekts.
	 */
	public void zeitrasterEintragRemoveById(final long idZeitraster, final long idEintrag) {
		zeitrasterEintragRemoveOhneUpdateById(idZeitraster, idEintrag);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvZeitrasterEintrag}-Objekt.
	 * @param eintrag das zu entfernende {@link UvZeitrasterEintrag}-Objekt.
	 */
	public void zeitrasterEintragRemove(final @NotNull UvZeitrasterEintrag eintrag) {
		zeitrasterEintragRemoveOhneUpdate(eintrag);
		updateAll();
	}

	private void zeitrasterEintragRemoveOhneUpdate(final @NotNull UvZeitrasterEintrag eintrag) {
		zeitrasterEintragRemoveOhneUpdateById(eintrag.idZeitraster, eintrag.id);
	}

	/**
	 * Entfernt alle {@link UvZeitrasterEintrag}-Objekte.
	 * @param listEintraege Die Liste der zu entfernenden {@link UvZeitrasterEintrag}-Objekte.
	 */
	public void zeitrasterEintragRemoveAll(final @NotNull Collection<UvZeitrasterEintrag> listEintraege) {
		zeitrasterEintragRemoveAllOhneUpdate(listEintraege);
		updateAll();
	}

	private void zeitrasterEintragRemoveAllOhneUpdate(final @NotNull Collection<UvZeitrasterEintrag> listEintraege) {
		final Set<UvZeitrasterEintrag> setEintraege = new HashSet<>(listEintraege);
		for (final @NotNull UvZeitrasterEintrag eintrag : setEintraege) {
			zeitrasterEintragRemoveOhneUpdateById(eintrag.idZeitraster, eintrag.id);
		}
	}

	/**
	 * Entfernt alle {@link UvZeitrasterEintrag}-Objekte für ein bestimmtes Zeitraster.
	 * @param idZeitraster Die ID des Zeitrasters.
	 */
	public void zeitrasterEintragRemoveAllByZeitraster(final long idZeitraster) {
		final List<UvZeitrasterEintrag> list = new ArrayList<>(zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.get1(idZeitraster));
		zeitrasterEintragRemoveAllOhneUpdate(list);
		updateAll();
	}

	// #####################################################################
	// ################## UvPlanungsabschnittZeitraster ####################
	// #####################################################################

	private void updatePlanungsabschnittZeitrasterMenge() {
		planungsabschnittZeitrasterMenge.clear();
		planungsabschnittZeitrasterMenge.addAll(planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.getAllValues());
		planungsabschnittZeitrasterMenge.sort(compPlanungsabschnittZeitraster);
	}

	/**
	 * Fügt ein {@link UvPlanungsabschnittZeitraster}-Objekt hinzu.
	 * @param zuordnung Das {@link UvPlanungsabschnittZeitraster}-Objekt, welches hinzugefügt werden soll.
	 */
	public void planungsabschnittZeitrasterAdd(final @NotNull UvPlanungsabschnittZeitraster zuordnung) {
		planungsabschnittZeitrasterAddAll(ListUtils.create1(zuordnung));
	}

	private void planungsabschnittZeitrasterAddOhneUpdate(final @NotNull UvPlanungsabschnittZeitraster zuordnung) {
		planungsabschnittZeitrasterAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvPlanungsabschnittZeitraster}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvPlanungsabschnittZeitraster}-Objekte, welche hinzugefügt werden soll.
	 */
	public void planungsabschnittZeitrasterAddAll(final @NotNull Collection<UvPlanungsabschnittZeitraster> listZuordnungen) {
		planungsabschnittZeitrasterAddAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void planungsabschnittZeitrasterAddAllOhneUpdate(final @NotNull Collection<UvPlanungsabschnittZeitraster> list) {
		// check all
		final @NotNull HashSet<UvPlanungsabschnittZeitraster> setOfIDs = new HashSet<>();
		for (final @NotNull UvPlanungsabschnittZeitraster zuordnung : list) {
			planungsabschnittZeitrasterCheck(zuordnung);
			DeveloperNotificationException.ifTrue("planungsabschnittZeitrasterAddAllOhneUpdate: Zuordnung existiert bereits!",
					planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.containsKey12(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster));
			DeveloperNotificationException.ifTrue("planungsabschnittZeitrasterAddAllOhneUpdate: Zuordnung doppelt in der Liste!",
					!setOfIDs.add(zuordnung));
		}
		// add all
		for (final @NotNull UvPlanungsabschnittZeitraster zuordnung : list) {
			planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.addSingle(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster, zuordnung);
			planungsabschnittZeitrasterMenge.add(zuordnung);
		}
	}

	private void planungsabschnittZeitrasterCheck(final @NotNull UvPlanungsabschnittZeitraster zuordnung) {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idZeitraster", zuordnung.idZeitraster);
		if (!planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"planungsabschnittZeitrasterCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
		if (!zeitrasterById.containsKey(zuordnung.idZeitraster)) {
			throw new DeveloperNotificationException(
					"planungsabschnittZeitrasterCheck: Das UvZeitraster mit der ID " + zuordnung.idZeitraster + " existiert nicht.");
		}
	}

	/**
	 * Aktualisiert das vorhandene {@link UvPlanungsabschnittZeitraster}-Objekt durch das neue Objekt.
	 * @param pazr Die neuen {@link UvPlanungsabschnittZeitraster}-Objekte.
	 */
	public void planungsabschnittZeitrasterAllPatchAttributes(final @NotNull Collection<UvPlanungsabschnittZeitraster> pazr) {
		planungsabschnittZeitrasterAllPatchAttributesOhneUpdate(pazr);
		updateAll();
	}

	private void planungsabschnittZeitrasterAllPatchAttributesOhneUpdate(final @NotNull Collection<UvPlanungsabschnittZeitraster> pazr) {
		planungsabschnittZeitrasterRemoveAllOhneUpdate(pazr);
		planungsabschnittZeitrasterAddAllOhneUpdate(pazr);
	}

	/**
	 * Liefert eine Liste aller {@link UvPlanungsabschnittZeitraster}-Objekte. <br>
	 * @return eine Liste aller {@link UvPlanungsabschnittZeitraster}-Objekte.
	 */
	public @NotNull List<UvPlanungsabschnittZeitraster> planungsabschnittZeitrasterGetMengeAsList() {
		return new ArrayList<>(planungsabschnittZeitrasterMenge);
	}

	private void planungsabschnittZeitrasterRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idZeitraster) {
		final @NotNull UvPlanungsabschnittZeitraster zuordnung =
				planungsabschnittZeitrasterGetByIdOrException(idPlanungsabschnitt, idZeitraster);
		planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.removeOrException(idPlanungsabschnitt, idZeitraster);
		planungsabschnittZeitrasterMenge.remove(zuordnung);
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittZeitraster}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idZeitraster Die ID des Zeitrasters.
	 */
	public void planungsabschnittZeitrasterRemoveById(final long idPlanungsabschnitt, final long idZeitraster) {
		planungsabschnittZeitrasterRemoveOhneUpdateById(idPlanungsabschnitt, idZeitraster);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittZeitraster}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvPlanungsabschnittZeitraster}-Objekt.
	 */
	public void planungsabschnittZeitrasterRemove(final @NotNull UvPlanungsabschnittZeitraster zuordnung) {
		planungsabschnittZeitrasterRemoveOhneUpdate(zuordnung);
		updateAll();
	}

	private void planungsabschnittZeitrasterRemoveOhneUpdate(final @NotNull UvPlanungsabschnittZeitraster zuordnung) {
		planungsabschnittZeitrasterRemoveOhneUpdateById(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster);
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittZeitraster}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvPlanungsabschnittZeitraster}-Objekte.
	 */
	public void planungsabschnittZeitrasterRemoveAll(final @NotNull Collection<UvPlanungsabschnittZeitraster> listZuordnungen) {
		planungsabschnittZeitrasterRemoveAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void planungsabschnittZeitrasterRemoveAllOhneUpdate(final @NotNull Collection<UvPlanungsabschnittZeitraster> listZuordnungen) {
		final Set<UvPlanungsabschnittZeitraster> setZuordnungen = new HashSet<>(listZuordnungen);
		for (final @NotNull UvPlanungsabschnittZeitraster zuordnung : setZuordnungen) {
			planungsabschnittZeitrasterRemoveOhneUpdateById(zuordnung.idPlanungsabschnitt, zuordnung.idZeitraster);
		}
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittZeitraster}-Objekte für einen Planungsabschnitt.
	 * @param idPlanungsabschnitt die ID des {@link UvPlanungsabschnitt}-Objekts.
	 * @param listZeitrasterIds Die Liste der IDs der zu entfernenden Zeitraster.
	 */
	public void planungsabschnittZeitrasterRemoveAllById(final long idPlanungsabschnitt, final @NotNull List<Long> listZeitrasterIds) {
		for (final long idZeitraster : listZeitrasterIds) {
			planungsabschnittZeitrasterRemoveOhneUpdateById(idPlanungsabschnitt, idZeitraster);
		}
		updateAll();
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittZeitraster}-Objekte für einen Planungsabschnitt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 */
	public void planungsabschnittZeitrasterRemoveAllByPlanungsabschnitt(final long idPlanungsabschnitt) {
		final List<UvPlanungsabschnittZeitraster> list =
				new ArrayList<>(planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.get1(idPlanungsabschnitt));
		planungsabschnittZeitrasterRemoveAllOhneUpdate(list);
		updateAll();
	}

	// #####################################################################
	// ################## UvPlanungsabschnittSchueler ######################
	// #####################################################################

	private void updatePlanungsabschnittSchuelerMenge() {
		planungsabschnittSchuelerMenge.clear();
		planungsabschnittSchuelerMenge.addAll(planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.getAllValues());
		planungsabschnittSchuelerMenge.sort(compPlanungsabschnittSchueler);
	}

	/**
	 * Fügt ein {@link UvPlanungsabschnittSchueler}-Objekt hinzu.
	 * @param zuordnung Das {@link UvPlanungsabschnittSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public void planungsabschnittSchuelerAdd(final @NotNull UvPlanungsabschnittSchueler zuordnung) {
		planungsabschnittSchuelerAddAll(ListUtils.create1(zuordnung));
	}

	private void planungsabschnittSchuelerAddOhneUpdate(final @NotNull UvPlanungsabschnittSchueler zuordnung) {
		planungsabschnittSchuelerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvPlanungsabschnittSchueler}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvPlanungsabschnittSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public void planungsabschnittSchuelerAddAll(final @NotNull Collection<UvPlanungsabschnittSchueler> listZuordnungen) {
		planungsabschnittSchuelerAddAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void planungsabschnittSchuelerAddAllOhneUpdate(final @NotNull Collection<UvPlanungsabschnittSchueler> list) {
		// check all
		final @NotNull HashSet<UvPlanungsabschnittSchueler> setOfIDs = new HashSet<>();
		for (final @NotNull UvPlanungsabschnittSchueler zuordnung : list) {
			planungsabschnittSchuelerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("planungsabschnittSchuelerAddAllOhneUpdate: Zuordnung existiert bereits!",
					planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.containsKey14(zuordnung.idPlanungsabschnitt,
							zuordnung.idSchueler));
			DeveloperNotificationException.ifTrue("planungsabschnittSchuelerAddAllOhneUpdate: Zuordnung doppelt in der Liste!",
					!setOfIDs.add(zuordnung));
		}
		// add all
		for (final @NotNull UvPlanungsabschnittSchueler zuordnung : list) {
			planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.addSingle(zuordnung.idPlanungsabschnitt,
					zuordnung.idJahrgang, (zuordnung.idKlasse != null) ? zuordnung.idKlasse : -1L, zuordnung.idSchueler, zuordnung);
			planungsabschnittSchuelerMenge.add(zuordnung);
		}
	}

	private void planungsabschnittSchuelerCheck(final @NotNull UvPlanungsabschnittSchueler zuordnung) {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idSchueler", zuordnung.idSchueler);
		if (!planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"planungsabschnittSchuelerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Aktualisiert die vorhandenen {@link UvPlanungsabschnittSchueler}-Objekte durch die neuen Objekte.
	 * @param pas Die neuen {@link UvPlanungsabschnittSchueler}-Objekte.
	 */
	public void planungsabschnittSchuelerAllPatchAttributes(final @NotNull Collection<UvPlanungsabschnittSchueler> pas) {
		planungsabschnittSchuelerAllPatchAttributesOhneUpdate(pas);
		updateAll();
	}

	private void planungsabschnittSchuelerAllPatchAttributesOhneUpdate(final @NotNull Collection<UvPlanungsabschnittSchueler> pas) {
		for (final @NotNull UvPlanungsabschnittSchueler neu : new HashSet<>(pas)) {
			final @NotNull UvPlanungsabschnittSchueler alt = planungsabschnittSchuelerGetByIdOrException(neu.idPlanungsabschnitt, neu.idSchueler);
			planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.removeOrException(
					alt.idPlanungsabschnitt, alt.idJahrgang, (alt.idKlasse == null) ? -1L : alt.idKlasse, alt.idSchueler);
			planungsabschnittSchuelerMenge.remove(alt);
		}
		planungsabschnittSchuelerAddAllOhneUpdate(pas);
	}

	/**
	 * Liefert eine Liste aller {@link UvPlanungsabschnittSchueler}-Objekte. <br>
	 * @return eine Liste aller {@link UvPlanungsabschnittSchueler}-Objekte.
	 */
	public @NotNull List<UvPlanungsabschnittSchueler> planungsabschnittSchuelerGetMengeAsList() {
		return new ArrayList<>(planungsabschnittSchuelerMenge);
	}

	private void planungsabschnittSchuelerRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idSchueler) {
		final @NotNull UvPlanungsabschnittSchueler pas = planungsabschnittSchuelerGetByIdOrException(idPlanungsabschnitt, idSchueler);
		planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.removeOrException(idPlanungsabschnitt, pas.idJahrgang,
				(pas.idKlasse != null) ? pas.idKlasse : -1L, idSchueler);
		planungsabschnittSchuelerMenge.remove(pas);
		final @NotNull List<UvSchuelergruppeSchueler> sgs =
				schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.get3(pas.idSchueler);
		schuelergruppeSchuelerRemoveAllOhneUpdate(sgs);
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittSchueler}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idSchueler Die ID des Schülers.
	 */
	public void planungsabschnittSchuelerRemoveById(final long idPlanungsabschnitt, final long idSchueler) {
		planungsabschnittSchuelerRemoveOhneUpdateById(idPlanungsabschnitt, idSchueler);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvPlanungsabschnittSchueler}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvPlanungsabschnittSchueler}-Objekt.
	 */
	public void planungsabschnittSchuelerRemove(final @NotNull UvPlanungsabschnittSchueler zuordnung) {
		planungsabschnittSchuelerRemoveOhneUpdate(zuordnung);
		updateAll();
	}

	private void planungsabschnittSchuelerRemoveOhneUpdate(final @NotNull UvPlanungsabschnittSchueler zuordnung) {
		planungsabschnittSchuelerRemoveOhneUpdateById(zuordnung.idPlanungsabschnitt, zuordnung.idSchueler);
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittSchueler}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvPlanungsabschnittSchueler}-Objekte.
	 */
	public void planungsabschnittSchuelerRemoveAll(final @NotNull Collection<UvPlanungsabschnittSchueler> listZuordnungen) {
		planungsabschnittSchuelerRemoveAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void planungsabschnittSchuelerRemoveAllOhneUpdate(final @NotNull Collection<UvPlanungsabschnittSchueler> listZuordnungen) {
		final Set<UvPlanungsabschnittSchueler> setZuordnungen = new HashSet<>(listZuordnungen);
		for (final @NotNull UvPlanungsabschnittSchueler zuordnung : setZuordnungen) {
			planungsabschnittSchuelerRemoveOhneUpdateById(zuordnung.idPlanungsabschnitt, zuordnung.idSchueler);
		}
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittSchueler}-Objekte für einen Planungsabschnitt.
	 * @param idPlanungsabschnitt die ID des {@link UvPlanungsabschnitt}-Objekts.
	 * @param listSchuelerIds Die Liste der IDs der zu entfernenden Schüler.
	 */
	public void planungsabschnittSchuelerRemoveAllById(final long idPlanungsabschnitt, final @NotNull List<Long> listSchuelerIds) {
		for (final long idSchueler : listSchuelerIds) {
			planungsabschnittSchuelerRemoveOhneUpdateById(idPlanungsabschnitt, idSchueler);
		}
		updateAll();
	}

	/**
	 * Entfernt alle {@link UvPlanungsabschnittSchueler}-Objekte für einen Planungsabschnitt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 */
	public void planungsabschnittSchuelerRemoveAllByPlanungsabschnitt(final long idPlanungsabschnitt) {
		final List<UvPlanungsabschnittSchueler> list =
				new ArrayList<>(planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.get1(idPlanungsabschnitt));
		planungsabschnittSchuelerRemoveAllOhneUpdate(list);
		updateAll();
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvPlanungsabschnittSchueler}-Objekt. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idSchueler die ID des Schülers.
	 * @return das zur ID zugehörige {@link UvPlanungsabschnittSchueler}-Objekt.
	 */
	public @NotNull UvPlanungsabschnittSchueler planungsabschnittSchuelerGetByIdOrException(final long idPlanungsabschnitt, final long idSchueler) {
		return planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.getSingle14OrException(idPlanungsabschnitt,
				idSchueler);
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvPlanungsabschnittSchueler}-Objekt oder {@code null}.
	 *
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts
	 * @param idSchueler die ID des Schülers
	 * @return das zugehörige {@link UvPlanungsabschnittSchueler}-Objekt oder {@code null}
	 */
	public UvPlanungsabschnittSchueler planungsabschnittSchuelerGetByIdOrNull(final long idPlanungsabschnitt, final long idSchueler) {
		return planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.getSingle14OrNull(idPlanungsabschnitt,
				idSchueler);
	}


	// #####################################################################
	// ######################### UvSchuelergruppe ##########################
	// #####################################################################

	/**
	 * Fügt ein {@link UvSchuelergruppe}-Objekt hinzu.
	 * @param schuelergruppe Das {@link UvSchuelergruppe}-Objekt, welches hinzugefügt werden soll.
	 */
	public void schuelergruppeAdd(final @NotNull UvSchuelergruppe schuelergruppe) {
		schuelergruppeAddAll(ListUtils.create1(schuelergruppe));
	}

	private void schuelergruppeAddOhneUpdate(final @NotNull UvSchuelergruppe schuelergruppe) {
		schuelergruppeAddAllOhneUpdate(ListUtils.create1(schuelergruppe));
	}

	/**
	 * Fügt alle {@link UvSchuelergruppe}-Objekte hinzu.
	 * @param listSchuelergruppen Die Menge der {@link UvSchuelergruppe}-Objekte, welche hinzugefügt werden soll.
	 */
	public void schuelergruppeAddAll(final @NotNull Collection<UvSchuelergruppe> listSchuelergruppen) {
		schuelergruppeAddAllOhneUpdate(listSchuelergruppen);
		updateAll();
	}

	private void schuelergruppeAddAllOhneUpdate(final @NotNull Collection<UvSchuelergruppe> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvSchuelergruppe schuelergruppe : list) {
			schuelergruppeCheck(schuelergruppe);
			DeveloperNotificationException.ifTrue("schuelergruppeAddAllOhneUpdate: Schuelergruppe existiert bereits!",
					schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.containsKey12(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id));
			DeveloperNotificationException.ifTrue("schuelergruppeAddAllOhneUpdate: ID=" + schuelergruppe.id + " doppelt in der Liste!",
					!setOfIDs.add(schuelergruppe.id));
		}
		// add all
		for (final @NotNull UvSchuelergruppe schuelergruppe : list) {
			schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.addSingle(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id, schuelergruppe);
			schuelergruppeMenge.add(schuelergruppe);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvSchuelergruppe}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param schuelergruppe die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public void schuelergruppePatchAttributes(final @NotNull UvSchuelergruppe schuelergruppe) {
		final @NotNull UvSchuelergruppe alt = schuelergruppeGetByIdOrException(schuelergruppe.id);
		schuelergruppeCheck(schuelergruppe);
		DeveloperNotificationException.ifTrue("schuelergruppePatchAttributes: Der Planungsabschnitt darf nicht geändert werden.",
				alt.idPlanungsabschnitt != schuelergruppe.idPlanungsabschnitt);
		schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.removeSingleOrException(alt.idPlanungsabschnitt, alt.id);
		schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.addSingle(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id, schuelergruppe);
		updateAll();
	}

	private void schuelergruppeCheck(final @NotNull UvSchuelergruppe schuelergruppe) {
		DeveloperNotificationException.ifInvalidID("schuelergruppe.id", schuelergruppe.id);
		DeveloperNotificationException.ifInvalidID("schuelergruppe.idPlanungsabschnitt", schuelergruppe.idPlanungsabschnitt);
		if (!planungsabschnittById.containsKey(schuelergruppe.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"schuelergruppeCheck: Der UvPlanungsabschnitt mit der ID " + schuelergruppe.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvSchuelergruppe}-Objekt. <br>
	 * @param idSchuelergruppe Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvSchuelergruppe}-Objekt.
	 */
	public @NotNull UvSchuelergruppe schuelergruppeGetByIdOrException(final long idSchuelergruppe) {
		return schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.getSingle2OrException(idSchuelergruppe);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvSchuelergruppe}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idSchuelergruppe die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvSchuelergruppe schuelergruppeGetByIdOrNull(final long idSchuelergruppe) {
		return schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.getSingle2OrNull(idSchuelergruppe);
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppe}-Objekte. <br>
	 * @return eine Liste aller {@link UvSchuelergruppe}-Objekte.
	 */
	public @NotNull List<UvSchuelergruppe> schuelergruppeGetMengeAsList() {
		return new ArrayList<>(schuelergruppeMenge);
	}

	private void schuelergruppeRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idSchuelergruppe) {
		final @NotNull UvSchuelergruppe schuelergruppe =
				schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.getSingle12OrException(idPlanungsabschnitt, idSchuelergruppe);
		schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.removeSingleOrException(idPlanungsabschnitt, idSchuelergruppe);
		schuelergruppeMenge.remove(schuelergruppe);
	}

	/**
	 * Entfernt ein existierendes {@link UvSchuelergruppe}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idSchuelergruppe Die ID der Schülergruppe.
	 */
	public void schuelergruppeRemoveById(final long idPlanungsabschnitt, final long idSchuelergruppe) {
		schuelergruppeRemoveOhneUpdateById(idPlanungsabschnitt, idSchuelergruppe);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvSchuelergruppe}-Objekt.
	 * @param schuelergruppe das zu entfernende {@link UvSchuelergruppe}-Objekt.
	 */
	public void schuelergruppeRemove(final @NotNull UvSchuelergruppe schuelergruppe) {
		schuelergruppeRemoveOhneUpdate(schuelergruppe);
		updateAll();
	}

	private void schuelergruppeRemoveOhneUpdate(final @NotNull UvSchuelergruppe schuelergruppe) {
		schuelergruppeRemoveOhneUpdateById(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id);
	}

	/**
	 * Entfernt alle {@link UvSchuelergruppe}-Objekte.
	 * @param listSchuelergruppen Die Liste der zu entfernenden {@link UvSchuelergruppe}-Objekte.
	 */
	public void schuelergruppeRemoveAll(final @NotNull Collection<UvSchuelergruppe> listSchuelergruppen) {
		schuelergruppeRemoveAllOhneUpdate(listSchuelergruppen);
		updateAll();
	}

	private void schuelergruppeRemoveAllOhneUpdate(final @NotNull Collection<UvSchuelergruppe> listSchuelergruppen) {
		final Set<UvSchuelergruppe> setSchuelergruppen = new HashSet<>(listSchuelergruppen);
		for (final @NotNull UvSchuelergruppe schuelergruppe : setSchuelergruppen) {
			schuelergruppeRemoveOhneUpdateById(schuelergruppe.idPlanungsabschnitt, schuelergruppe.id);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppe}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvSchuelergruppe}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvSchuelergruppe> schuelergruppeGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.get1(planungsabschnitt.id);
	}

	// #####################################################################
	// ##################### UvSchuelergruppeSchueler ######################
	// #####################################################################

	/**
	 * Fügt ein {@link UvSchuelergruppeSchueler}-Objekt hinzu.
	 * @param zuordnung Das {@link UvSchuelergruppeSchueler}-Objekt, welches hinzugefügt werden soll.
	 */
	public void schuelergruppeSchuelerAdd(final @NotNull UvSchuelergruppeSchueler zuordnung) {
		schuelergruppeSchuelerAddAll(ListUtils.create1(zuordnung));
	}

	private void schuelergruppeSchuelerAddOhneUpdate(final @NotNull UvSchuelergruppeSchueler zuordnung) {
		schuelergruppeSchuelerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvSchuelergruppeSchueler}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvSchuelergruppeSchueler}-Objekte, welche hinzugefügt werden soll.
	 */
	public void schuelergruppeSchuelerAddAll(final @NotNull Collection<UvSchuelergruppeSchueler> listZuordnungen) {
		schuelergruppeSchuelerAddAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void schuelergruppeSchuelerAddAllOhneUpdate(final @NotNull Collection<UvSchuelergruppeSchueler> list) {
		// check all
		for (final @NotNull UvSchuelergruppeSchueler zuordnung : list) {
			schuelergruppeSchuelerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("schuelergruppeSchuelerAddAllOhneUpdate: Zuordnung existiert bereits!",
					schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.containsKey123(
							zuordnung.idPlanungsabschnitt, zuordnung.idSchuelergruppe, zuordnung.idSchueler));
		}
		// add all
		for (final @NotNull UvSchuelergruppeSchueler zuordnung : list) {
			schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.addSingle(
					zuordnung.idPlanungsabschnitt, zuordnung.idSchuelergruppe, zuordnung.idSchueler, zuordnung);
			schuelergruppeSchuelerMenge.add(zuordnung);
		}
	}

	private void schuelergruppeSchuelerCheck(final @NotNull UvSchuelergruppeSchueler zuordnung) {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idSchuelergruppe", zuordnung.idSchuelergruppe);
		DeveloperNotificationException.ifInvalidID("zuordnung.idSchueler", zuordnung.idSchueler);
		if (!planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"schuelergruppeSchuelerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
		if (!schuelergruppeByIdPlanungsabschnittAndIdSchuelergruppe.containsKey12(zuordnung.idPlanungsabschnitt, zuordnung.idSchuelergruppe)) {
			throw new DeveloperNotificationException(
					"schuelergruppeSchuelerCheck: Die UvSchuelergruppe mit der ID " + zuordnung.idSchuelergruppe + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvSchuelergruppeSchueler}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idSchuelergruppe Die ID der Schülergruppe.
	 * @param idSchueler Die ID des Schülers.
	 * @return das zur ID zugehörige {@link UvSchuelergruppeSchueler}-Objekt.
	 */
	public @NotNull UvSchuelergruppeSchueler schuelergruppeSchuelerGetByIdOrException(final long idPlanungsabschnitt, final long idSchuelergruppe,
			final long idSchueler) {
		return schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.getSingle123OrException(idPlanungsabschnitt,
				idSchuelergruppe, idSchueler);
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte. <br>
	 * @return eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte.
	 */
	public @NotNull List<UvSchuelergruppeSchueler> schuelergruppeSchuelerGetMengeAsList() {
		return new ArrayList<>(schuelergruppeSchuelerMenge);
	}

	private void schuelergruppeSchuelerRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idSchuelergruppe, final long idSchueler) {
		schuelergruppeSchuelerRemoveOhneUpdate(schuelergruppeSchuelerGetByIdOrException(idPlanungsabschnitt, idSchuelergruppe, idSchueler));
	}

	/**
	 * Entfernt ein existierendes {@link UvSchuelergruppeSchueler}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idSchuelergruppe Die ID der Schülergruppe.
	 * @param idSchueler Die ID des Schülers.
	 */
	public void schuelergruppeSchuelerRemoveById(final long idPlanungsabschnitt, final long idSchuelergruppe, final long idSchueler) {
		schuelergruppeSchuelerRemoveOhneUpdateById(idPlanungsabschnitt, idSchuelergruppe, idSchueler);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvSchuelergruppeSchueler}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvSchuelergruppeSchueler}-Objekt.
	 */
	public void schuelergruppeSchuelerRemove(final @NotNull UvSchuelergruppeSchueler zuordnung) {
		schuelergruppeSchuelerRemoveOhneUpdate(zuordnung);
		updateAll();
	}

	private void schuelergruppeSchuelerRemoveOhneUpdate(final @NotNull UvSchuelergruppeSchueler zuordnung) {
		schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.removeSingleOrException(
				zuordnung.idPlanungsabschnitt, zuordnung.idSchuelergruppe, zuordnung.idSchueler);
		schuelergruppeSchuelerMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvSchuelergruppeSchueler}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvSchuelergruppeSchueler}-Objekte.
	 */
	public void schuelergruppeSchuelerRemoveAll(final @NotNull Collection<UvSchuelergruppeSchueler> listZuordnungen) {
		schuelergruppeSchuelerRemoveAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void schuelergruppeSchuelerRemoveAllOhneUpdate(final @NotNull Collection<UvSchuelergruppeSchueler> listZuordnungen) {
		final Set<UvSchuelergruppeSchueler> setZuordnungen = new HashSet<>(listZuordnungen);
		for (final @NotNull UvSchuelergruppeSchueler zuordnung : setZuordnungen) {
			schuelergruppeSchuelerRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte für einen Planungsabschnitt. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @return eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvSchuelergruppeSchueler> schuelergruppeSchuelerGetMengeByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.get1(idPlanungsabschnitt);
	}

	/**
	 * Liefert eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte für eine Schülergruppe. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idSchuelergruppe die ID der Schülergruppe.
	 * @return eine Liste aller {@link UvSchuelergruppeSchueler}-Objekte für die Schülergruppe.
	 */
	public @NotNull List<UvSchuelergruppeSchueler> schuelergruppeSchuelerGetMengeBySchuelergruppe(final long idPlanungsabschnitt, final long idSchuelergruppe) {
		return new ArrayList<>(
				schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.get12(idPlanungsabschnitt, idSchuelergruppe));
	}

	// #####################################################################
	// ############################## UvKlasse #############################
	// #####################################################################

	/**
	 * Fügt ein {@link UvKlasse}-Objekt hinzu.
	 * @param klasse Das {@link UvKlasse}-Objekt, welches hinzugefügt werden soll.
	 */
	public void klasseAdd(final @NotNull UvKlasse klasse) {
		klasseAddAll(ListUtils.create1(klasse));
	}

	private void klasseAddOhneUpdate(final @NotNull UvKlasse klasse) {
		klasseAddAllOhneUpdate(ListUtils.create1(klasse));
	}

	/**
	 * Fügt alle {@link UvKlasse}-Objekte hinzu.
	 * @param listKlassen Die Menge der {@link UvKlasse}-Objekte, welche hinzugefügt werden soll.
	 */
	public void klasseAddAll(final @NotNull Collection<UvKlasse> listKlassen) {
		klasseAddAllOhneUpdate(listKlassen);
		updateAll();
	}

	private void klasseAddAllOhneUpdate(final @NotNull Collection<UvKlasse> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvKlasse klasse : list) {
			klasseCheck(klasse);
			DeveloperNotificationException.ifTrue("klasseAddAllOhneUpdate: Klasse existiert bereits!",
					klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.containsKey13(klasse.idPlanungsabschnitt, klasse.id));
			DeveloperNotificationException.ifTrue("klasseAddAllOhneUpdate: ID=" + klasse.id + " doppelt in der Liste!",
					!setOfIDs.add(klasse.id));
		}
		// add all
		for (final @NotNull UvKlasse klasse : list) {
			klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.addSingle(klasse.idPlanungsabschnitt, klasse.idSchuelergruppe, klasse.id, klasse);
			klasseMenge.add(klasse);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvKlasse}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param klasse die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public void klassePatchAttributes(final @NotNull UvKlasse klasse) {
		final @NotNull UvKlasse alt = klasseGetByIdOrException(klasse.id);
		klasseCheck(klasse);
		DeveloperNotificationException.ifTrue("klassePatchAttributes: Der Planungsabschnitt darf nicht geändert werden.",
				alt.idPlanungsabschnitt != klasse.idPlanungsabschnitt);
		klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.removeSingleOrException(alt.idPlanungsabschnitt, alt.idSchuelergruppe, alt.id);
		klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.addSingle(klasse.idPlanungsabschnitt, klasse.idSchuelergruppe, klasse.id, klasse);
		updateAll();
	}

	private void klasseCheck(final @NotNull UvKlasse klasse) {
		DeveloperNotificationException.ifInvalidID("klasse.id", klasse.id);
		DeveloperNotificationException.ifInvalidID("klasse.idPlanungsabschnitt", klasse.idPlanungsabschnitt);
		if (!planungsabschnittById.containsKey(klasse.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("klasseCheck: Der UvPlanungsabschnitt mit der ID " + klasse.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvKlasse}-Objekt. <br>
	 * @param idKlasse Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvKlasse}-Objekt.
	 */
	public @NotNull UvKlasse klasseGetByIdOrException(final long idKlasse) {
		return klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.getSingle3OrException(idKlasse);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvKlasse}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idKlasse die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvKlasse klasseGetByIdOrNull(final long idKlasse) {
		return klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.getSingle3OrNull(idKlasse);
	}

	/**
	 * Liefert eine Liste aller {@link UvKlasse}-Objekte. <br>
	 * @return eine Liste aller {@link UvKlasse}-Objekte.
	 */
	public @NotNull List<UvKlasse> klasseGetMengeAsList() {
		return new ArrayList<>(klasseMenge);
	}

	private void klasseRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idKlasse) {
		final @NotNull UvKlasse klasse = klasseGetByIdOrException(idKlasse);
		klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.removeSingleOrException(idPlanungsabschnitt, klasse.idSchuelergruppe, idKlasse);
		for (final @NotNull UvLerngruppe lerngruppe : new ArrayList<>(lerngruppeByIdKursAndIdKlasseAndIdFach.get2(idKlasse))) {
			lerngruppeRemoveOhneUpdateById(idPlanungsabschnitt, lerngruppe.id);
		}
		klassenLehrerRemoveByKlasseOhneUpdate(klasse);
		klasseMenge.remove(klasse);
	}

	/**
	 * Entfernt ein existierendes {@link UvKlasse}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idKlasse Die ID der Klasse.
	 */
	public void klasseRemoveById(final long idPlanungsabschnitt, final long idKlasse) {
		klasseRemoveOhneUpdateById(idPlanungsabschnitt, idKlasse);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvKlasse}-Objekt.
	 * @param klasse das zu entfernende {@link UvKlasse}-Objekt.
	 */
	public void klasseRemove(final @NotNull UvKlasse klasse) {
		klasseRemoveOhneUpdate(klasse);
		updateAll();
	}

	private void klasseRemoveOhneUpdate(final @NotNull UvKlasse klasse) {
		klasseRemoveOhneUpdateById(klasse.idPlanungsabschnitt, klasse.id);
	}

	/**
	 * Entfernt alle {@link UvKlasse}-Objekte.
	 * @param listKlassen Die Liste der zu entfernenden {@link UvKlasse}-Objekte.
	 */
	public void klasseRemoveAll(final @NotNull Collection<UvKlasse> listKlassen) {
		klasseRemoveAllOhneUpdate(listKlassen);
		updateAll();
	}

	private void klasseRemoveAllOhneUpdate(final @NotNull Collection<UvKlasse> listKlassen) {
		final Set<UvKlasse> setKlassen = new HashSet<>(listKlassen);
		for (final @NotNull UvKlasse klasse : setKlassen) {
			klasseRemoveOhneUpdateById(klasse.idPlanungsabschnitt, klasse.id);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvKlasse}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvKlasse}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvKlasse> klasseGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.get1(planungsabschnitt.id);
	}

	// #####################################################################
	// ######################### UvKlassenLehrer ##########################
	// #####################################################################

	/**
	 * Fügt ein {@link UvKlassenLehrer}-Objekt hinzu.
	 * @param zuordnung Das {@link UvKlassenLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public void klassenLehrerAdd(final @NotNull UvKlassenLehrer zuordnung) {
		klassenLehrerAddAll(ListUtils.create1(zuordnung));
	}

	private void klassenLehrerAddOhneUpdate(final @NotNull UvKlassenLehrer zuordnung) {
		klassenLehrerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvKlassenLehrer}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvKlassenLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public void klassenLehrerAddAll(final @NotNull Collection<UvKlassenLehrer> listZuordnungen) {
		klassenLehrerAddAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void klassenLehrerAddAllOhneUpdate(final @NotNull Collection<UvKlassenLehrer> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvKlassenLehrer zuordnung : list) {
			klassenLehrerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("klassenLehrerAddAllOhneUpdate: Zuordnung existiert bereits!",
					klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.containsKey123(
							zuordnung.idPlanungsabschnitt, zuordnung.idKlasse, zuordnung.idLehrer));
			DeveloperNotificationException.ifTrue("klassenLehrerAddAllOhneUpdate: ID=" + zuordnung.id + " doppelt in der Liste!",
					!setOfIDs.add(zuordnung.id));
		}
		// add all
		for (final @NotNull UvKlassenLehrer zuordnung : list) {
			klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.addSingle(
					zuordnung.idPlanungsabschnitt, zuordnung.idKlasse, zuordnung.idLehrer, zuordnung);
			klassenLehrerMenge.add(zuordnung);
		}
	}

	private void klassenLehrerCheck(final @NotNull UvKlassenLehrer zuordnung) {
		DeveloperNotificationException.ifInvalidID("zuordnung.id", zuordnung.id);
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idKlasse", zuordnung.idKlasse);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLehrer", zuordnung.idLehrer);
		if (!planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"klassenLehrerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
		if (!lehrerById.containsKey(zuordnung.idLehrer)) {
			throw new DeveloperNotificationException("klassenLehrerCheck: Der UvLehrer mit der ID " + zuordnung.idLehrer + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvKlassenLehrer}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idKlasse Die ID der Klasse.
	 * @param idLehrer Die ID des Lehrers.
	 * @return das zur ID zugehörige {@link UvKlassenLehrer}-Objekt.
	 */
	public @NotNull UvKlassenLehrer klassenLehrerGetByIdOrException(final long idPlanungsabschnitt, final long idKlasse, final long idLehrer) {
		return klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.getSingle123OrException(idPlanungsabschnitt, idKlasse, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link UvKlassenLehrer}-Objekte. <br>
	 * @return eine Liste aller {@link UvKlassenLehrer}-Objekte.
	 */
	public @NotNull List<UvKlassenLehrer> klassenLehrerGetMengeAsList() {
		return new ArrayList<>(klassenLehrerMenge);
	}

	private void klassenLehrerRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idKlasse, final long idLehrer) {
		klassenLehrerRemoveOhneUpdate(klassenLehrerGetByIdOrException(idPlanungsabschnitt, idKlasse, idLehrer));
	}

	/**
	 * Entfernt ein existierendes {@link UvKlassenLehrer}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idKlasse Die ID der Klasse.
	 * @param idLehrer Die ID des Lehrers.
	 */
	public void klassenLehrerRemoveById(final long idPlanungsabschnitt, final long idKlasse, final long idLehrer) {
		klassenLehrerRemoveOhneUpdateById(idPlanungsabschnitt, idKlasse, idLehrer);
		updateAll();
	}

	/**
	 * Entfernt alle {@link UvKlassenLehrer}-Objekte für die angegebene {@link UvKlasse}.
	 * @param klasse die {@link UvKlasse}, deren Klassenlehrer entfernt werden sollen.
	 */
	private void klassenLehrerRemoveByKlasseOhneUpdate(final @NotNull UvKlasse klasse) {
		klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.removeAllByKey2(klasse.id);
	}

	/**
	 * Entfernt ein existierendes {@link UvKlassenLehrer}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvKlassenLehrer}-Objekt.
	 */
	public void klassenLehrerRemove(final @NotNull UvKlassenLehrer zuordnung) {
		klassenLehrerRemoveOhneUpdate(zuordnung);
		updateAll();
	}

	private void klassenLehrerRemoveOhneUpdate(final @NotNull UvKlassenLehrer zuordnung) {
		klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.removeSingleOrException(
				zuordnung.idPlanungsabschnitt, zuordnung.idKlasse, zuordnung.idLehrer);
		klassenLehrerMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvKlassenLehrer}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvKlassenLehrer}-Objekte.
	 */
	public void klassenLehrerRemoveAll(final @NotNull Collection<UvKlassenLehrer> listZuordnungen) {
		klassenLehrerRemoveAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void klassenLehrerRemoveAllOhneUpdate(final @NotNull Collection<UvKlassenLehrer> listZuordnungen) {
		final Set<UvKlassenLehrer> setZuordnungen = new HashSet<>(listZuordnungen);
		for (final @NotNull UvKlassenLehrer zuordnung : setZuordnungen) {
			klassenLehrerRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Aktualisiert die vorhandenen {@link UvKlassenLehrer}-Objekte anhand der übergebenen neuen Werte.
	 * @param listZuordnungen die Sammlung der aktualisierten {@link UvKlassenLehrer}-Objekte
	 */
	public void klassenLehrerAllPatchAttributes(final @NotNull Collection<UvKlassenLehrer> listZuordnungen) {
		klassenLehrerAllPatchAttributesOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void klassenLehrerAllPatchAttributesOhneUpdate(final @NotNull Collection<UvKlassenLehrer> listZuordnungen) {
		for (final @NotNull UvKlassenLehrer neu : new HashSet<>(listZuordnungen)) {
			klassenLehrerRemoveOhneUpdate(klassenLehrerGetByZuordnungsIdOrException(neu.id));
		}
		klassenLehrerAddAllOhneUpdate(listZuordnungen);
	}

	/**
	 * Liefert die aktuelle Klassenlehrerzuordnung anhand ihrer unveränderlichen Datensatz-ID.
	 *
	 * @param id die ID der Zuordnung
	 * @return die gespeicherte Zuordnung
	 */
	public @NotNull UvKlassenLehrer klassenLehrerGetByZuordnungsIdOrException(final long id) {
		for (final @NotNull UvKlassenLehrer zuordnung : klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.getAllValues()) {
			if (zuordnung.id == id) {
				return zuordnung;
			}
		}
		throw new DeveloperNotificationException("Klassenlehrer-Zuordnung mit ID " + id + " existiert nicht.");
	}

	/**
	 * Liefert eine Liste aller {@link UvKlassenLehrer}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvKlassenLehrer}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvKlassenLehrer> klassenLehrerGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvKlassenLehrer}-Objekte für eine {@link UvKlasse}. <br>
	 * @param klasse die {@link UvKlasse}.
	 * @return eine Liste aller {@link UvKlassenLehrer}-Objekte für die {@link UvKlasse}.
	 */
	public @NotNull List<UvKlassenLehrer> klassenLehrerGetMengeByKlasse(final @NotNull UvKlasse klasse) {
		return klassenLehrerByIdPlanungsabschnittAndIdKlasseAndIdLehrer.get12(klasse.idPlanungsabschnitt, klasse.id);
	}

	// #####################################################################
	// ############################### UvKurs ##############################
	// #####################################################################

	/**
	 * Fügt ein {@link UvKurs}-Objekt hinzu.
	 * @param kurs Das {@link UvKurs}-Objekt, welches hinzugefügt werden soll.
	 */
	public void kursAdd(final @NotNull UvKurs kurs) {
		kursAddAll(ListUtils.create1(kurs));
	}

	private void kursAddOhneUpdate(final @NotNull UvKurs kurs) {
		kursAddAllOhneUpdate(ListUtils.create1(kurs));
	}

	/**
	 * Fügt alle {@link UvKurs}-Objekte hinzu.
	 * @param listKurse Die Menge der {@link UvKurs}-Objekte, welche hinzugefügt werden soll.
	 */
	public void kursAddAll(final @NotNull Collection<UvKurs> listKurse) {
		kursAddAllOhneUpdate(listKurse);
		updateAll();
	}

	private void kursAddAllOhneUpdate(final @NotNull Collection<UvKurs> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvKurs kurs : list) {
			kursCheck(kurs);
			DeveloperNotificationException.ifTrue("kursAddAllOhneUpdate: Kurs existiert bereits!",
					kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.containsKey13(kurs.idPlanungsabschnitt, kurs.id));
			DeveloperNotificationException.ifTrue("kursAddAllOhneUpdate: ID=" + kurs.id + " doppelt in der Liste!",
					!setOfIDs.add(kurs.id));
		}
		// add all
		for (final @NotNull UvKurs kurs : list) {
			kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.addSingle(kurs.idPlanungsabschnitt, kurs.idSchuelergruppe, kurs.id, kurs);
			kursMenge.add(kurs);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvKurs}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param kurs die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public void kursPatchAttributes(final @NotNull UvKurs kurs) {
		final @NotNull UvKurs alt = kursGetByIdOrException(kurs.id);
		kursCheck(kurs);
		DeveloperNotificationException.ifTrue("kursPatchAttributes: Der Planungsabschnitt darf nicht geändert werden.",
				alt.idPlanungsabschnitt != kurs.idPlanungsabschnitt);
		kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.removeSingleOrException(alt.idPlanungsabschnitt, alt.idSchuelergruppe, alt.id);
		kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.addSingle(kurs.idPlanungsabschnitt, kurs.idSchuelergruppe, kurs.id, kurs);
		updateAll();
	}

	private void kursCheck(final @NotNull UvKurs kurs) {
		DeveloperNotificationException.ifInvalidID("kurs.id", kurs.id);
		DeveloperNotificationException.ifInvalidID("kurs.idPlanungsabschnitt", kurs.idPlanungsabschnitt);
		if (!planungsabschnittById.containsKey(kurs.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("kursCheck: Der UvPlanungsabschnitt mit der ID " + kurs.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvKurs}-Objekt. <br>
	 * @param idKurs Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvKurs}-Objekt.
	 */
	public @NotNull UvKurs kursGetByIdOrException(final long idKurs) {
		return kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.getSingle3OrException(idKurs);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvKurs}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idKurs die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvKurs kursGetByIdOrNull(final long idKurs) {
		return kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.getSingle3OrNull(idKurs);
	}

	/**
	 * Liefert eine Liste aller {@link UvKurs}-Objekte. <br>
	 * @return eine Liste aller {@link UvKurs}-Objekte.
	 */
	public @NotNull List<UvKurs> kursGetMengeAsList() {
		return new ArrayList<>(kursMenge);
	}

	private void kursRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idKurs) {
		final @NotNull UvKurs kurs = kursGetByIdOrException(idKurs);
		kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.removeSingleOrException(idPlanungsabschnitt, kurs.idSchuelergruppe, idKurs);
		final UvLerngruppe lerngruppe = lerngruppeGetByKursIdOrNull(idKurs);
		if (lerngruppe != null) {
			lerngruppeRemoveOhneUpdateById(lerngruppe.idPlanungsabschnitt, lerngruppe.id);
		}
	}

	/**
	 * Entfernt ein existierendes {@link UvKurs}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idKurs Die ID des Kurses.
	 */
	public void kursRemoveById(final long idPlanungsabschnitt, final long idKurs) {
		kursRemoveOhneUpdateById(idPlanungsabschnitt, idKurs);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvKurs}-Objekt.
	 * @param kurs das zu entfernende {@link UvKurs}-Objekt.
	 */
	public void kursRemove(final @NotNull UvKurs kurs) {
		kursRemoveOhneUpdate(kurs);
		updateAll();
	}

	private void kursRemoveOhneUpdate(final @NotNull UvKurs kurs) {
		kursRemoveOhneUpdateById(kurs.idPlanungsabschnitt, kurs.id);
	}

	/**
	 * Entfernt alle {@link UvKurs}-Objekte.
	 * @param listKurse Die Liste der zu entfernenden {@link UvKurs}-Objekte.
	 */
	public void kursRemoveAll(final @NotNull Collection<UvKurs> listKurse) {
		kursRemoveAllOhneUpdate(listKurse);
		updateAll();
	}

	private void kursRemoveAllOhneUpdate(final @NotNull Collection<UvKurs> listKurse) {
		final Set<UvKurs> setKurse = new HashSet<>(listKurse);
		for (final @NotNull UvKurs kurs : setKurse) {
			kursRemoveOhneUpdateById(kurs.idPlanungsabschnitt, kurs.id);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvKurs}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvKurs}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvKurs> kursGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.get1(planungsabschnitt.id);
	}

	// #####################################################################
	// ############################# UvSchiene #############################
	// #####################################################################

	/**
	 * Fügt ein {@link UvSchiene}-Objekt hinzu.
	 * @param schiene Das {@link UvSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public void schieneAdd(final @NotNull UvSchiene schiene) {
		schieneAddAll(ListUtils.create1(schiene));
	}

	private void schieneAddOhneUpdate(final @NotNull UvSchiene schiene) {
		schieneAddAllOhneUpdate(ListUtils.create1(schiene));
	}

	/**
	 * Fügt alle {@link UvSchiene}-Objekte hinzu.
	 * @param listSchienen Die Menge der {@link UvSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public void schieneAddAll(final @NotNull Collection<UvSchiene> listSchienen) {
		schieneAddAllOhneUpdate(listSchienen);
		updateAll();
	}

	private void schieneAddAllOhneUpdate(final @NotNull Collection<UvSchiene> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvSchiene schiene : list) {
			schieneCheck(schiene);
			DeveloperNotificationException.ifTrue("schieneAddAllOhneUpdate: Schiene existiert bereits!",
					schieneByIdPlanungsabschnittAndNummerAndIdSchiene.containsKey13(schiene.idPlanungsabschnitt, schiene.id));
			DeveloperNotificationException.ifTrue("schieneAddAllOhneUpdate: ID=" + schiene.id + " doppelt in der Liste!",
					!setOfIDs.add(schiene.id));
		}
		// add all
		for (final @NotNull UvSchiene schiene : list) {
			schieneByIdPlanungsabschnittAndNummerAndIdSchiene.addSingle(schiene.idPlanungsabschnitt, schiene.nummer, schiene.id, schiene);
			schieneMenge.add(schiene);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvSchiene}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param schiene die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public void schienePatchAttributes(final @NotNull UvSchiene schiene) {
		final @NotNull UvSchiene alt = schieneGetByIdOrException(schiene.id);
		schieneCheck(schiene);
		DeveloperNotificationException.ifTrue("schienePatchAttributes: Der Planungsabschnitt darf nicht geändert werden.",
				alt.idPlanungsabschnitt != schiene.idPlanungsabschnitt);
		schieneByIdPlanungsabschnittAndNummerAndIdSchiene.removeSingleOrException(alt.idPlanungsabschnitt, alt.nummer, alt.id);
		schieneByIdPlanungsabschnittAndNummerAndIdSchiene.addSingle(schiene.idPlanungsabschnitt, schiene.nummer, schiene.id, schiene);
		updateAll();
	}

	private void schieneCheck(final @NotNull UvSchiene schiene) {
		DeveloperNotificationException.ifInvalidID("schiene.id", schiene.id);
		DeveloperNotificationException.ifInvalidID("schiene.idPlanungsabschnitt", schiene.idPlanungsabschnitt);
		if (!planungsabschnittById.containsKey(schiene.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException("schieneCheck: Der UvPlanungsabschnitt mit der ID " + schiene.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvSchiene}-Objekt. <br>
	 * @param idSchiene Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvSchiene}-Objekt.
	 */
	public @NotNull UvSchiene schieneGetByIdOrException(final long idSchiene) {
		return schieneByIdPlanungsabschnittAndNummerAndIdSchiene.getSingle3OrException(idSchiene);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvSchiene}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idSchiene die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvSchiene schieneGetByIdOrNull(final long idSchiene) {
		return schieneByIdPlanungsabschnittAndNummerAndIdSchiene.getSingle3OrNull(idSchiene);
	}

	/**
	 * Liefert eine Liste aller {@link UvSchiene}-Objekte. <br>
	 * @return eine Liste aller {@link UvSchiene}-Objekte.
	 */
	public @NotNull List<UvSchiene> schieneGetMengeAsList() {
		return new ArrayList<>(schieneMenge);
	}

	private void schieneRemoveOhneUpdateById(final long idSchiene) {
		schieneRemoveOhneUpdate(schieneGetByIdOrException(idSchiene));
	}

	/**
	 * Entfernt ein existierendes {@link UvSchiene}-Objekt.
	 * @param idSchiene Die ID der Schiene.
	 */
	public void schieneRemoveById(final long idSchiene) {
		schieneRemoveOhneUpdateById(idSchiene);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvSchiene}-Objekt.
	 * @param schiene das zu entfernende {@link UvSchiene}-Objekt.
	 */
	public void schieneRemove(final @NotNull UvSchiene schiene) {
		schieneRemoveOhneUpdate(schiene);
		updateAll();
	}

	private void schieneRemoveOhneUpdate(final @NotNull UvSchiene schiene) {
		schieneByIdPlanungsabschnittAndNummerAndIdSchiene.removeSingleOrException(schiene.idPlanungsabschnitt, schiene.nummer, schiene.id);
		schieneMenge.remove(schiene);
	}

	/**
	 * Entfernt alle {@link UvSchiene}-Objekte.
	 * @param listSchienen Die Liste der zu entfernenden {@link UvSchiene}-Objekte.
	 */
	public void schieneRemoveAll(final @NotNull Collection<UvSchiene> listSchienen) {
		schieneRemoveAllOhneUpdate(listSchienen);
		updateAll();
	}

	private void schieneRemoveAllOhneUpdate(final @NotNull Collection<UvSchiene> listSchienen) {
		final Set<UvSchiene> setSchienen = new HashSet<>(listSchienen);
		for (final @NotNull UvSchiene schiene : setSchienen) {
			schieneRemoveOhneUpdate(schiene);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvSchiene}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvSchiene}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvSchiene> schieneGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return schieneByIdPlanungsabschnittAndNummerAndIdSchiene.get1(planungsabschnitt.id);
	}

	// #####################################################################
	// ########################### UvLerngruppe ############################
	// #####################################################################

	/**
	 * Fügt ein {@link UvLerngruppe}-Objekt hinzu.
	 * @param lerngruppe Das {@link UvLerngruppe}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lerngruppeAdd(final @NotNull UvLerngruppe lerngruppe) {
		lerngruppeAddAll(ListUtils.create1(lerngruppe));
	}

	private void lerngruppeAddOhneUpdate(final @NotNull UvLerngruppe lerngruppe) {
		lerngruppeAddAllOhneUpdate(ListUtils.create1(lerngruppe));
	}

	/**
	 * Fügt alle {@link UvLerngruppe}-Objekte hinzu.
	 * @param listLerngruppen Die Menge der {@link UvLerngruppe}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lerngruppeAddAll(final @NotNull Collection<UvLerngruppe> listLerngruppen) {
		lerngruppeAddAllOhneUpdate(listLerngruppen);
		updateAll();
	}

	private void lerngruppeAddAllOhneUpdate(final @NotNull Collection<UvLerngruppe> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvLerngruppe lerngruppe : list) {
			lerngruppeCheck(lerngruppe);
			DeveloperNotificationException.ifTrue("lerngruppeAddAllOhneUpdate: Lerngruppe existiert bereits!",
					lerngruppeByIdPlanungsabschnittAndIdLerngruppe.containsKey12(lerngruppe.idPlanungsabschnitt, lerngruppe.id));
			DeveloperNotificationException.ifTrue("lerngruppeAddAllOhneUpdate: ID=" + lerngruppe.id + " doppelt in der Liste!",
					!setOfIDs.add(lerngruppe.id));
		}
		// add all
		for (final @NotNull UvLerngruppe lerngruppe : list) {
			lerngruppeByIdPlanungsabschnittAndIdLerngruppe.addSingle(lerngruppe.idPlanungsabschnitt, lerngruppe.id, lerngruppe);
			lerngruppeMenge.add(lerngruppe);
		}
	}

	/**
	 * Ersetzt das vorhandene {@link UvLerngruppe}-Objekt anhand seiner ID ohne Löschkaskade
	 * und baut die Indizes neu auf. Das übergebene Objekt muss die vollständigen neuen Daten
	 * enthalten; das bisherige Manager-Objekt darf zuvor nicht verändert werden.
	 *
	 * @param lerngruppe die neuen Daten mit unveränderter ID und unverändertem Planungsabschnitt
	 */
	public void lerngruppePatchAttributes(final @NotNull UvLerngruppe lerngruppe) {
		final @NotNull UvLerngruppe alt = lerngruppeGetByIdOrException(lerngruppe.id);
		lerngruppeCheck(lerngruppe);
		DeveloperNotificationException.ifTrue("lerngruppePatchAttributes: Der Planungsabschnitt darf nicht geändert werden.",
				alt.idPlanungsabschnitt != lerngruppe.idPlanungsabschnitt);
		lerngruppeByIdPlanungsabschnittAndIdLerngruppe.removeSingleOrException(alt.idPlanungsabschnitt, alt.id);
		lerngruppeByIdPlanungsabschnittAndIdLerngruppe.addSingle(lerngruppe.idPlanungsabschnitt, lerngruppe.id, lerngruppe);
		updateAll();
	}

	private void lerngruppeCheck(final @NotNull UvLerngruppe lerngruppe) {
		DeveloperNotificationException.ifInvalidID("lerngruppe.id", lerngruppe.id);
		DeveloperNotificationException.ifInvalidID("lerngruppe.idPlanungsabschnitt", lerngruppe.idPlanungsabschnitt);
		if (!planungsabschnittById.containsKey(lerngruppe.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"lerngruppeCheck: Der UvPlanungsabschnitt mit der ID " + lerngruppe.idPlanungsabschnitt + " existiert nicht.");
		}
		lerngruppeCheckZuordnung(lerngruppe);
	}

	/**
	 * Prüft ausschließlich die Zuordnung einer Lerngruppe: entweder Kurs ohne Klasse und Fach
	 * oder Klasse und Fach ohne Kurs. Die Existenz der referenzierten Objekte wird nicht geprüft.
	 * Bei einem Patch muss der vollständige resultierende Datensatz übergeben werden.
	 *
	 * @param lerngruppe die zu prüfende Lerngruppe
	 * @throws DeveloperNotificationException falls die Zuordnung unvollständig oder widersprüchlich ist
	 */
	public static void lerngruppeCheckZuordnung(final @NotNull UvLerngruppe lerngruppe) {
		final boolean kursunterricht = (lerngruppe.idKurs != null) && (lerngruppe.idKlasse == null) && (lerngruppe.idFach == null);
		final boolean klassenunterricht = (lerngruppe.idKurs == null) && (lerngruppe.idKlasse != null) && (lerngruppe.idFach != null);
		if (!kursunterricht && !klassenunterricht) {
			throw new DeveloperNotificationException(
					"Eine Lerngruppe muss entweder einem Kurs ohne Klasse und Fach oder einer Klasse und einem Fach ohne Kurs zugeordnet sein.");
		}
	}

	/**
	 * Prüft, ob die angegebene Lerngruppe ein Korrekturfach ist.
	 *
	 * Eine Lerngruppe ist ein Korrekturfach, wenn
	 * - sie einem Leistungskurs zugeordnet ist oder
	 * - das zugehörige Fach Mathematik, Deutsch, Englisch, Latein oder Französisch ist.
	 *
	 * @param lerngruppe   Die zu prüfende {@link UvLerngruppe}.
	 *
	 * @return {@code true}, wenn die Lerngruppe ein Korrekturfach ist, sonst {@code false}.
	 */
	public boolean lerngruppeIstKorrekturfach(final @NotNull UvLerngruppe lerngruppe) {
		// Es muss ein Fach vorliegen, sonst kann es kein Korrekturfach sein.
		final UvFach fach = fachGetByLerngruppe(lerngruppe);
		if (fach == null) {
			return false;
		}

		// Ist es Mathematik, Deutsch, Englisch, Latein oder Französisch?
		final String kuerzelStatistik = fachdatenGetByFach(fach).kuerzelStatistik;
		if ("M".equals(kuerzelStatistik)
				|| "D".equals(kuerzelStatistik)
				|| "E".equals(kuerzelStatistik)
				|| "L".equals(kuerzelStatistik)
				|| "F".equals(kuerzelStatistik)) {
			return true;
		}

		// Ist es ein Kurs?
		final UvKurs kurs = kursGetByLerngruppe(lerngruppe);
		if (kurs == null) {
			return false;
		}

		// Hat der Kurs eine Kursart?
		final GostKursart kursart = GostKursart.fromKuerzel(kurs.kursart);
		if (kursart == null) {
			return false;
		}

		// Ist es ein LK?
		return kursart == GostKursart.LK;
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLerngruppe}-Objekt. <br>
	 * @param idLerngruppe Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvLerngruppe}-Objekt.
	 */
	public @NotNull UvLerngruppe lerngruppeGetByIdOrException(final long idLerngruppe) {
		return lerngruppeByIdPlanungsabschnittAndIdLerngruppe.getSingle2OrException(idLerngruppe);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvLerngruppe}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idLerngruppe die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvLerngruppe lerngruppeGetByIdOrNull(final long idLerngruppe) {
		return lerngruppeByIdPlanungsabschnittAndIdLerngruppe.getSingle2OrNull(idLerngruppe);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppe}-Objekte. <br>
	 * @return eine Liste aller {@link UvLerngruppe}-Objekte.
	 */
	public @NotNull List<UvLerngruppe> lerngruppeGetMengeAsList() {
		return new ArrayList<>(lerngruppeMenge);
	}

	/**
	 * Liefert die einem Kurs zugeordnete {@link UvLerngruppe} oder {@code null}.
	 *
	 * @param idKurs die ID des Kurses
	 * @return die zugeordnete {@link UvLerngruppe} oder {@code null}
	 */
	public UvLerngruppe lerngruppeGetByKursIdOrNull(final long idKurs) {
		return lerngruppeByIdKursAndIdKlasseAndIdFach.getSingle1OrNull(idKurs);
	}

	private void lerngruppeRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idLerngruppe) {
		final @NotNull UvLerngruppe lerngruppe = lerngruppeGetByIdOrException(idLerngruppe);
		lerngruppeByIdPlanungsabschnittAndIdLerngruppe.removeSingleOrException(idPlanungsabschnitt, idLerngruppe);
		unterrichtRemoveAllOhneUpdate(unterrichtGetMengeByLerngruppe(idPlanungsabschnitt, idLerngruppe));
		final @NotNull List<UvLerngruppenSchiene> schienen = lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get2(idLerngruppe);
		lerngruppenSchieneRemoveAllOhneUpdate(schienen);
		lerngruppenLehrerRemoveAllOhneUpdate(lerngruppenLehrerGetMengeByLerngruppe(lerngruppe));
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppe}-Objekt.
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 */
	public void lerngruppeRemoveById(final long idPlanungsabschnitt, final long idLerngruppe) {
		lerngruppeRemoveOhneUpdateById(idPlanungsabschnitt, idLerngruppe);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppe}-Objekt.
	 * @param lerngruppe das zu entfernende {@link UvLerngruppe}-Objekt.
	 */
	public void lerngruppeRemove(final @NotNull UvLerngruppe lerngruppe) {
		lerngruppeRemoveOhneUpdate(lerngruppe);
		updateAll();
	}

	private void lerngruppeRemoveOhneUpdate(final @NotNull UvLerngruppe lerngruppe) {
		if (lerngruppe.idKurs != null) {
			kursRemoveOhneUpdateById(lerngruppe.idPlanungsabschnitt, lerngruppe.idKurs);
			return;
		}
		lerngruppeRemoveOhneUpdateById(lerngruppe.idPlanungsabschnitt, lerngruppe.id);
	}

	/**
	 * Entfernt alle {@link UvLerngruppe}-Objekte.
	 * @param listLerngruppen Die Liste der zu entfernenden {@link UvLerngruppe}-Objekte.
	 */
	public void lerngruppeRemoveAll(final @NotNull Collection<UvLerngruppe> listLerngruppen) {
		lerngruppeRemoveAllOhneUpdate(listLerngruppen);
		updateAll();
	}

	private void lerngruppeRemoveAllOhneUpdate(final @NotNull Collection<UvLerngruppe> listLerngruppen) {
		final Set<UvLerngruppe> setLerngruppen = new HashSet<>(listLerngruppen);
		for (final @NotNull UvLerngruppe lerngruppe : setLerngruppen) {
			lerngruppeRemoveOhneUpdate(lerngruppe);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppe}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvLerngruppe}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvLerngruppe> lerngruppeGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return lerngruppeByIdPlanungsabschnittAndIdLerngruppe.get1(planungsabschnitt.id);
	}

	// #####################################################################
	// ######################## UvLerngruppenLehrer #########################
	// #####################################################################

	/**
	 * Fügt ein {@link UvLerngruppenLehrer}-Objekt hinzu.
	 * @param zuordnung Das {@link UvLerngruppenLehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lerngruppenLehrerAdd(final @NotNull UvLerngruppenLehrer zuordnung) {
		lerngruppenLehrerAddAll(ListUtils.create1(zuordnung));
	}

	private void lerngruppenLehrerAddOhneUpdate(final @NotNull UvLerngruppenLehrer zuordnung) {
		lerngruppenLehrerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvLerngruppenLehrer}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvLerngruppenLehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lerngruppenLehrerAddAll(final @NotNull Collection<UvLerngruppenLehrer> listZuordnungen) {
		lerngruppenLehrerAddAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void lerngruppenLehrerAddAllOhneUpdate(final @NotNull Collection<UvLerngruppenLehrer> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvLerngruppenLehrer zuordnung : list) {
			lerngruppenLehrerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("lerngruppenLehrerAddAllOhneUpdate: Zuordnung existiert bereits!",
					lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.containsKey123(
							zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idLehrer));
			DeveloperNotificationException.ifTrue("lerngruppenLehrerAddAllOhneUpdate: ID=" + zuordnung.id + " doppelt in der Liste!",
					!setOfIDs.add(zuordnung.id));
		}
		// add all
		for (final @NotNull UvLerngruppenLehrer zuordnung : list) {
			lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.addSingle(
					zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idLehrer, zuordnung);
			lerngruppenLehrerMenge.add(zuordnung);
		}
	}

	private void lerngruppenLehrerCheck(final @NotNull UvLerngruppenLehrer zuordnung) {
		DeveloperNotificationException.ifInvalidID("zuordnung.id", zuordnung.id);
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLerngruppe", zuordnung.idLerngruppe);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLehrer", zuordnung.idLehrer);
		if (!planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"lerngruppenLehrerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLerngruppenLehrer}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 * @param idLehrer Die ID des Lehrers.
	 * @return das zur ID zugehörige {@link UvLerngruppenLehrer}-Objekt.
	 */
	public @NotNull UvLerngruppenLehrer lerngruppenLehrerGetByIdOrException(final long idPlanungsabschnitt, final long idLerngruppe, final long idLehrer) {
		return lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.getSingle123OrException(idPlanungsabschnitt, idLerngruppe, idLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenLehrer}-Objekte. <br>
	 * @return eine Liste aller {@link UvLerngruppenLehrer}-Objekte.
	 */
	public @NotNull List<UvLerngruppenLehrer> lerngruppenLehrerGetMengeAsList() {
		return new ArrayList<>(lerngruppenLehrerMenge);
	}

	private void lerngruppenLehrerRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idLerngruppe, final long idLehrer) {
		lerngruppenLehrerRemoveOhneUpdate(lerngruppenLehrerGetByIdOrException(idPlanungsabschnitt, idLerngruppe, idLehrer));
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppenLehrer}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 * @param idLehrer Die ID des Lehrers.
	 */
	public void lerngruppenLehrerRemoveById(final long idPlanungsabschnitt, final long idLerngruppe, final long idLehrer) {
		lerngruppenLehrerRemoveOhneUpdateById(idPlanungsabschnitt, idLerngruppe, idLehrer);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppenLehrer}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvLerngruppenLehrer}-Objekt.
	 */
	public void lerngruppenLehrerRemove(final @NotNull UvLerngruppenLehrer zuordnung) {
		lerngruppenLehrerRemoveOhneUpdate(zuordnung);
		updateAll();
	}

	private void lerngruppenLehrerRemoveOhneUpdate(final @NotNull UvLerngruppenLehrer zuordnung) {
		lerngruppenLehrerRemoveOhneKaskadeOhneUpdate(zuordnung);
		unterrichtLerngruppenlehrerRemoveAllOhneUpdate(new ArrayList<>(
				unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.get13(zuordnung.idPlanungsabschnitt, zuordnung.id)));
	}

	private void lerngruppenLehrerRemoveOhneKaskadeOhneUpdate(final @NotNull UvLerngruppenLehrer zuordnung) {
		lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.removeSingleOrException(
				zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idLehrer);
		lerngruppenLehrerMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvLerngruppenLehrer}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvLerngruppenLehrer}-Objekte.
	 */
	public void lerngruppenLehrerRemoveAll(final @NotNull Collection<UvLerngruppenLehrer> listZuordnungen) {
		lerngruppenLehrerRemoveAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void lerngruppenLehrerRemoveAllOhneUpdate(final @NotNull Collection<UvLerngruppenLehrer> listZuordnungen) {
		final Set<UvLerngruppenLehrer> setZuordnungen = new HashSet<>(listZuordnungen);
		for (final @NotNull UvLerngruppenLehrer zuordnung : setZuordnungen) {
			lerngruppenLehrerRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Aktualisiert die vorhandenen {@link UvLerngruppenLehrer}-Objekte anhand der übergebenen neuen Werte.
	 * @param listZuordnungen die Sammlung der aktualisierten {@link UvLerngruppenLehrer}-Objekte
	 */
	public void lerngruppenLehrerAllPatchAttributes(final @NotNull Collection<UvLerngruppenLehrer> listZuordnungen) {
		lerngruppenLehrerAllPatchAttributesOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void lerngruppenLehrerAllPatchAttributesOhneUpdate(final @NotNull Collection<UvLerngruppenLehrer> listZuordnungen) {
		// Ein Austausch im Index ist kein fachliches Löschen: Unterrichtszuordnungen bleiben erhalten.
		for (final @NotNull UvLerngruppenLehrer zuordnung : new HashSet<>(listZuordnungen)) {
			lerngruppenLehrerRemoveOhneKaskadeOhneUpdate(lerngruppenLehrerGetByZuordnungsIdOrException(zuordnung.id));
		}
		lerngruppenLehrerAddAllOhneUpdate(listZuordnungen);
	}

	/**
	 * Liefert die aktuelle Lerngruppenlehrerzuordnung anhand ihrer unveränderlichen Datensatz-ID.
	 *
	 * @param id die ID der Zuordnung
	 * @return die gespeicherte Zuordnung
	 */
	public @NotNull UvLerngruppenLehrer lerngruppenLehrerGetByZuordnungsIdOrException(final long id) {
		for (final @NotNull UvLerngruppenLehrer zuordnung : lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.getAllValues()) {
			if (zuordnung.id == id) {
				return zuordnung;
			}
		}
		throw new DeveloperNotificationException("Lerngruppenlehrer-Zuordnung mit ID " + id + " existiert nicht.");
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenLehrer}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvLerngruppenLehrer}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvLerngruppenLehrer> lerngruppenLehrerGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenLehrer}-Objekte für eine {@link UvLerngruppe}. <br>
	 * @param lerngruppe die {@link UvLerngruppe}.
	 * @return eine Liste aller {@link UvLerngruppenLehrer}-Objekte für die {@link UvLerngruppe}.
	 */
	public @NotNull List<UvLerngruppenLehrer> lerngruppenLehrerGetMengeByLerngruppe(final @NotNull UvLerngruppe lerngruppe) {
		return lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.get12(lerngruppe.idPlanungsabschnitt, lerngruppe.id);
	}

	// #####################################################################
	// ####################### UvLerngruppenSchiene #########################
	// #####################################################################

	/**
	 * Fügt ein {@link UvLerngruppenSchiene}-Objekt hinzu.
	 * @param zuordnung Das {@link UvLerngruppenSchiene}-Objekt, welches hinzugefügt werden soll.
	 */
	public void lerngruppenSchieneAdd(final @NotNull UvLerngruppenSchiene zuordnung) {
		lerngruppenSchieneAddAll(ListUtils.create1(zuordnung));
	}

	private void lerngruppenSchieneAddOhneUpdate(final @NotNull UvLerngruppenSchiene zuordnung) {
		lerngruppenSchieneAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvLerngruppenSchiene}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvLerngruppenSchiene}-Objekte, welche hinzugefügt werden soll.
	 */
	public void lerngruppenSchieneAddAll(final @NotNull Collection<UvLerngruppenSchiene> listZuordnungen) {
		lerngruppenSchieneAddAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void lerngruppenSchieneAddAllOhneUpdate(final @NotNull Collection<UvLerngruppenSchiene> list) {
		// check all
		for (final @NotNull UvLerngruppenSchiene zuordnung : list) {
			lerngruppenSchieneCheck(zuordnung);
			DeveloperNotificationException.ifTrue("lerngruppenSchieneAddAllOhneUpdate: Zuordnung existiert bereits!",
					lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.containsKey123(
							zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idSchiene));
		}
		// add all
		for (final @NotNull UvLerngruppenSchiene zuordnung : list) {
			lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.addSingle(
					zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idSchiene, zuordnung);
			lerngruppenSchieneMenge.add(zuordnung);
		}
	}

	private void lerngruppenSchieneCheck(final @NotNull UvLerngruppenSchiene zuordnung) {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLerngruppe", zuordnung.idLerngruppe);
		DeveloperNotificationException.ifInvalidID("zuordnung.idSchiene", zuordnung.idSchiene);
		if (!planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"lerngruppenSchieneCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvLerngruppenSchiene}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 * @param idSchiene Die ID der Schiene.
	 * @return das zur ID zugehörige {@link UvLerngruppenSchiene}-Objekt.
	 */
	public @NotNull UvLerngruppenSchiene lerngruppenSchieneGetByIdOrException(final long idPlanungsabschnitt, final long idLerngruppe, final long idSchiene) {
		return lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.getSingle123OrException(idPlanungsabschnitt, idLerngruppe, idSchiene);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenSchiene}-Objekte. <br>
	 * @return eine Liste aller {@link UvLerngruppenSchiene}-Objekte.
	 */
	public @NotNull List<UvLerngruppenSchiene> lerngruppenSchieneGetMengeAsList() {
		return new ArrayList<>(lerngruppenSchieneMenge);
	}

	private void lerngruppenSchieneRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idLerngruppe, final long idSchiene) {
		lerngruppenSchieneRemoveOhneUpdate(lerngruppenSchieneGetByIdOrException(idPlanungsabschnitt, idLerngruppe, idSchiene));
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppenSchiene}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idLerngruppe Die ID der Lerngruppe.
	 * @param idSchiene Die ID der Schiene.
	 */
	public void lerngruppenSchieneRemoveById(final long idPlanungsabschnitt, final long idLerngruppe, final long idSchiene) {
		lerngruppenSchieneRemoveOhneUpdateById(idPlanungsabschnitt, idLerngruppe, idSchiene);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvLerngruppenSchiene}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvLerngruppenSchiene}-Objekt.
	 */
	public void lerngruppenSchieneRemove(final @NotNull UvLerngruppenSchiene zuordnung) {
		lerngruppenSchieneRemoveOhneUpdate(zuordnung);
		updateAll();
	}

	private void lerngruppenSchieneRemoveOhneUpdate(final @NotNull UvLerngruppenSchiene zuordnung) {
		lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.removeSingleOrException(
				zuordnung.idPlanungsabschnitt, zuordnung.idLerngruppe, zuordnung.idSchiene);
		lerngruppenSchieneMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvLerngruppenSchiene}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvLerngruppenSchiene}-Objekte.
	 */
	public void lerngruppenSchieneRemoveAll(final @NotNull Collection<UvLerngruppenSchiene> listZuordnungen) {
		lerngruppenSchieneRemoveAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void lerngruppenSchieneRemoveAllOhneUpdate(final @NotNull Collection<UvLerngruppenSchiene> listZuordnungen) {
		final Set<UvLerngruppenSchiene> setZuordnungen = new HashSet<>(listZuordnungen);
		for (final @NotNull UvLerngruppenSchiene zuordnung : setZuordnungen) {
			lerngruppenSchieneRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenSchiene}-Objekte für einen Planungsabschnitt. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @return eine Liste aller {@link UvLerngruppenSchiene}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvLerngruppenSchiene> lerngruppenSchieneGetMengeByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get1(idPlanungsabschnitt);
	}

	/**
	 * Liefert eine Liste aller {@link UvLerngruppenSchiene}-Objekte für eine Lerngruppe. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idLerngruppe die ID der Lerngruppe.
	 * @return eine Liste aller {@link UvLerngruppenSchiene}-Objekte für die Lerngruppe.
	 */
	public @NotNull List<UvLerngruppenSchiene> lerngruppenSchieneGetMengeByLerngruppe(final long idPlanungsabschnitt, final long idLerngruppe) {
		return new ArrayList<>(lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get12(idPlanungsabschnitt, idLerngruppe));
	}

	// #####################################################################
	// ########################## UvUnterricht #############################
	// #####################################################################

	/**
	 * Fügt ein {@link UvUnterricht}-Objekt hinzu.
	 * @param unterricht Das {@link UvUnterricht}-Objekt, welches hinzugefügt werden soll.
	 */
	public void unterrichtAdd(final @NotNull UvUnterricht unterricht) {
		unterrichtAddAll(ListUtils.create1(unterricht));
	}

	private void unterrichtAddOhneUpdate(final @NotNull UvUnterricht unterricht) {
		unterrichtAddAllOhneUpdate(ListUtils.create1(unterricht));
	}

	/**
	 * Fügt alle {@link UvUnterricht}-Objekte hinzu.
	 * @param listUnterricht Die Menge der {@link UvUnterricht}-Objekte, welche hinzugefügt werden soll.
	 */
	public void unterrichtAddAll(final @NotNull Collection<UvUnterricht> listUnterricht) {
		unterrichtAddAllOhneUpdate(listUnterricht);
		updateAll();
	}

	private void unterrichtAddAllOhneUpdate(final @NotNull Collection<UvUnterricht> list) {
		// check all
		final @NotNull HashSet<Long> setOfIDs = new HashSet<>();
		for (final @NotNull UvUnterricht unterricht : list) {
			unterrichtCheck(unterricht);
			DeveloperNotificationException.ifTrue("unterrichtAddAllOhneUpdate: Unterricht existiert bereits!",
					unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht
							.containsKey1234(unterricht.idPlanungsabschnitt, unterricht.idLerngruppe,
									(unterricht.idZeitrasterEintrag == null) ? -1 : unterricht.idZeitrasterEintrag, unterricht.id));
			DeveloperNotificationException.ifTrue("unterrichtAddAllOhneUpdate: ID=" + unterricht.id + " doppelt in der Liste!",
					!setOfIDs.add(unterricht.id));
		}
		// add all
		for (final @NotNull UvUnterricht unterricht : list) {
			unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.addSingle(
					unterricht.idPlanungsabschnitt, unterricht.idLerngruppe,
					(unterricht.idZeitrasterEintrag == null) ? -1 : unterricht.idZeitrasterEintrag, unterricht.id,
					unterricht);
			unterrichtMenge.add(unterricht);
		}
	}

	private void unterrichtCheck(final @NotNull UvUnterricht unterricht) {
		DeveloperNotificationException.ifInvalidID("unterricht.id", unterricht.id);
		DeveloperNotificationException.ifInvalidID("unterricht.idPlanungsabschnitt", unterricht.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("unterricht.idLerngruppe", unterricht.idLerngruppe);
		if (!planungsabschnittById.containsKey(unterricht.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"unterrichtCheck: Der UvPlanungsabschnitt mit der ID " + unterricht.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvUnterricht}-Objekt. <br>
	 * @param idUnterricht Die ID des angefragten-Objektes.
	 * @return das zur ID zugehörige {@link UvUnterricht}-Objekt.
	 */
	public @NotNull UvUnterricht unterrichtGetByIdOrException(final long idUnterricht) {
		return unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.getSingle4OrException(idUnterricht);
	}

	/**
	 * Liefert das zur ID gehörende {@link UvUnterricht}-Objekt direkt aus dem Index oder {@code null}.
	 *
	 * @param idUnterricht die ID des gesuchten Objekts
	 * @return das gespeicherte Objekt oder {@code null}, falls es nicht vorhanden ist
	 */
	public UvUnterricht unterrichtGetByIdOrNull(final long idUnterricht) {
		return unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.getSingle4OrNull(idUnterricht);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterricht}-Objekte. <br>
	 * @return eine Liste aller {@link UvUnterricht}-Objekte.
	 */
	public @NotNull List<UvUnterricht> unterrichtGetMengeAsList() {
		return new ArrayList<>(unterrichtMenge);
	}

	private void unterrichtRemoveOhneUpdateById(final long idUnterricht) {
		unterrichtRemoveOhneUpdate(unterrichtGetByIdOrException(idUnterricht));
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterricht}-Objekt anhand seiner ID.
	 * @param idUnterricht Die ID des Unterrichts.
	 */
	public void unterrichtRemoveById(final long idUnterricht) {
		unterrichtRemoveOhneUpdateById(idUnterricht);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterricht}-Objekt.
	 * @param unterricht das zu entfernende {@link UvUnterricht}-Objekt.
	 */
	public void unterrichtRemove(final @NotNull UvUnterricht unterricht) {
		unterrichtRemoveOhneUpdate(unterricht);
		updateAll();
	}

	private void unterrichtRemoveOhneUpdate(final @NotNull UvUnterricht unterricht) {
		unterrichtRemoveOhneKaskadeOhneUpdate(unterricht);
		unterrichtRaumRemoveAllOhneUpdate(new ArrayList<>(unterrichtRaumGetMengeByUnterricht(unterricht)));
		unterrichtLerngruppenlehrerRemoveAllOhneUpdate(new ArrayList<>(unterrichtLerngruppenlehrerGetMengeByUnterricht(unterricht)));
	}

	private void unterrichtRemoveOhneKaskadeOhneUpdate(final @NotNull UvUnterricht unterricht) {
		unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.removeOrException(
				unterricht.idPlanungsabschnitt, unterricht.idLerngruppe,
				(unterricht.idZeitrasterEintrag == null) ? -1 : unterricht.idZeitrasterEintrag, unterricht.id);
		unterrichtMenge.remove(unterricht);
	}

	/**
	 * Entfernt alle {@link UvUnterricht}-Objekte.
	 * @param listUnterricht Die Liste der zu entfernenden {@link UvUnterricht}-Objekte.
	 */
	public void unterrichtRemoveAll(final @NotNull Collection<UvUnterricht> listUnterricht) {
		unterrichtRemoveAllOhneUpdate(listUnterricht);
		updateAll();
	}

	private void unterrichtRemoveAllOhneUpdate(final @NotNull Collection<UvUnterricht> listUnterricht) {
		final Set<UvUnterricht> setUnterricht = new HashSet<>(listUnterricht);
		for (final @NotNull UvUnterricht unterricht : setUnterricht) {
			unterrichtRemoveOhneUpdate(unterricht);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterricht}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvUnterricht}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvUnterricht> unterrichtGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterricht}-Objekte für eine Lerngruppe. <br>
	 * @param idPlanungsabschnitt die ID des Planungsabschnitts.
	 * @param idLerngruppe die ID der Lerngruppe.
	 * @return eine Liste aller {@link UvUnterricht}-Objekte für die Lerngruppe.
	 */
	public @NotNull List<UvUnterricht> unterrichtGetMengeByLerngruppe(final long idPlanungsabschnitt, final long idLerngruppe) {
		return new ArrayList<>(
				unterrichtByIdPlanungsabschnittAndIdLerngruppeAndIdZeitrasterEintragAndIdUnterricht.get12(idPlanungsabschnitt, idLerngruppe));
	}

	/**
	 * Aktualisiert die vorhandenen {@link UvUnterricht}-Objekte durch die neuen Objekte.
	 * @param listUnterricht Die neuen {@link UvUnterricht}-Objekte.
	 */
	public void unterrichtAllPatchAttributes(final @NotNull Collection<UvUnterricht> listUnterricht) {
		unterrichtAllPatchAttributesOhneUpdate(listUnterricht);
		updateAll();
	}

	private void unterrichtAllPatchAttributesOhneUpdate(final @NotNull Collection<UvUnterricht> listUnterricht) {
		// Ein Austausch im Index ist kein fachliches Löschen: Raum- und Lehrerzuordnungen bleiben erhalten.
		for (final @NotNull UvUnterricht unterricht : new HashSet<>(listUnterricht)) {
			unterrichtRemoveOhneKaskadeOhneUpdate(unterrichtGetByIdOrException(unterricht.id));
		}
		unterrichtAddAllOhneUpdate(listUnterricht);
	}

	// #####################################################################
	// ######################### UvUnterrichtRaum ##########################
	// #####################################################################

	/**
	 * Fügt ein {@link UvUnterrichtRaum}-Objekt hinzu.
	 * @param zuordnung Das {@link UvUnterrichtRaum}-Objekt, welches hinzugefügt werden soll.
	 */
	public void unterrichtRaumAdd(final @NotNull UvUnterrichtRaum zuordnung) {
		unterrichtRaumAddAll(ListUtils.create1(zuordnung));
	}

	private void unterrichtRaumAddOhneUpdate(final @NotNull UvUnterrichtRaum zuordnung) {
		unterrichtRaumAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvUnterrichtRaum}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvUnterrichtRaum}-Objekte, welche hinzugefügt werden soll.
	 */
	public void unterrichtRaumAddAll(final @NotNull Collection<UvUnterrichtRaum> listZuordnungen) {
		unterrichtRaumAddAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void unterrichtRaumAddAllOhneUpdate(final @NotNull Collection<UvUnterrichtRaum> list) {
		// check all
		for (final @NotNull UvUnterrichtRaum zuordnung : list) {
			unterrichtRaumCheck(zuordnung);
			DeveloperNotificationException.ifTrue("unterrichtRaumAddAllOhneUpdate: Zuordnung existiert bereits!",
					unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.containsKey123(
							zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idRaum));
		}
		// add all
		for (final @NotNull UvUnterrichtRaum zuordnung : list) {
			unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.addSingle(
					zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idRaum, zuordnung);
			unterrichtRaumMenge.add(zuordnung);
		}
	}

	private void unterrichtRaumCheck(final @NotNull UvUnterrichtRaum zuordnung) {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idUnterricht", zuordnung.idUnterricht);
		DeveloperNotificationException.ifInvalidID("zuordnung.idRaum", zuordnung.idRaum);
		if (!planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"unterrichtRaumCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvUnterrichtRaum}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idUnterricht Die ID des Unterrichts.
	 * @param idRaum Die ID des Raums.
	 * @return das zur ID zugehörige {@link UvUnterrichtRaum}-Objekt.
	 */
	public @NotNull UvUnterrichtRaum unterrichtRaumGetByIdOrException(final long idPlanungsabschnitt, final long idUnterricht, final long idRaum) {
		return unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.getSingle123OrException(idPlanungsabschnitt, idUnterricht, idRaum);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtRaum}-Objekte. <br>
	 * @return eine Liste aller {@link UvUnterrichtRaum}-Objekte.
	 */
	public @NotNull List<UvUnterrichtRaum> unterrichtRaumGetMengeAsList() {
		return new ArrayList<>(unterrichtRaumMenge);
	}

	private void unterrichtRaumRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idUnterricht, final long idRaum) {
		unterrichtRaumRemoveOhneUpdate(unterrichtRaumGetByIdOrException(idPlanungsabschnitt, idUnterricht, idRaum));
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterrichtRaum}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idUnterricht Die ID des Unterrichts.
	 * @param idRaum Die ID des Raums.
	 */
	public void unterrichtRaumRemoveById(final long idPlanungsabschnitt, final long idUnterricht, final long idRaum) {
		unterrichtRaumRemoveOhneUpdateById(idPlanungsabschnitt, idUnterricht, idRaum);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterrichtRaum}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvUnterrichtRaum}-Objekt.
	 */
	public void unterrichtRaumRemove(final @NotNull UvUnterrichtRaum zuordnung) {
		unterrichtRaumRemoveOhneUpdate(zuordnung);
		updateAll();
	}

	private void unterrichtRaumRemoveOhneUpdate(final @NotNull UvUnterrichtRaum zuordnung) {
		unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.removeSingleOrException(
				zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idRaum);
		unterrichtRaumMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvUnterrichtRaum}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvUnterrichtRaum}-Objekte.
	 */
	public void unterrichtRaumRemoveAll(final @NotNull Collection<UvUnterrichtRaum> listZuordnungen) {
		unterrichtRaumRemoveAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void unterrichtRaumRemoveAllOhneUpdate(final @NotNull Collection<UvUnterrichtRaum> listZuordnungen) {
		final Set<UvUnterrichtRaum> setZuordnungen = new HashSet<>(listZuordnungen);
		for (final @NotNull UvUnterrichtRaum zuordnung : setZuordnungen) {
			unterrichtRaumRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtRaum}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvUnterrichtRaum}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvUnterrichtRaum> unterrichtRaumGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtRaum}-Objekte für einen {@link UvUnterricht}. <br>
	 * @param unterricht der {@link UvUnterricht}.
	 * @return eine Liste aller {@link UvUnterrichtRaum}-Objekte für den {@link UvUnterricht}.
	 */
	public @NotNull List<UvUnterrichtRaum> unterrichtRaumGetMengeByUnterricht(final @NotNull UvUnterricht unterricht) {
		return unterrichtRaumByIdPlanungsabschnittAndIdUnterrichtAndIdRaum.get12(unterricht.idPlanungsabschnitt, unterricht.id);
	}

	// #####################################################################
	// ################### UvUnterrichtLerngruppenlehrer ####################
	// #####################################################################

	/**
	 * Fügt ein {@link UvUnterrichtLerngruppenlehrer}-Objekt hinzu.
	 * @param zuordnung Das {@link UvUnterrichtLerngruppenlehrer}-Objekt, welches hinzugefügt werden soll.
	 */
	public void unterrichtLerngruppenlehrerAdd(final @NotNull UvUnterrichtLerngruppenlehrer zuordnung) {
		unterrichtLerngruppenlehrerAddAll(ListUtils.create1(zuordnung));
	}

	private void unterrichtLerngruppenlehrerAddOhneUpdate(final @NotNull UvUnterrichtLerngruppenlehrer zuordnung) {
		unterrichtLerngruppenlehrerAddAllOhneUpdate(ListUtils.create1(zuordnung));
	}

	/**
	 * Fügt alle {@link UvUnterrichtLerngruppenlehrer}-Objekte hinzu.
	 * @param listZuordnungen Die Menge der {@link UvUnterrichtLerngruppenlehrer}-Objekte, welche hinzugefügt werden soll.
	 */
	public void unterrichtLerngruppenlehrerAddAll(final @NotNull Collection<UvUnterrichtLerngruppenlehrer> listZuordnungen) {
		unterrichtLerngruppenlehrerAddAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void unterrichtLerngruppenlehrerAddAllOhneUpdate(final @NotNull Collection<UvUnterrichtLerngruppenlehrer> list) {
		// check all
		for (final @NotNull UvUnterrichtLerngruppenlehrer zuordnung : list) {
			unterrichtLerngruppenlehrerCheck(zuordnung);
			DeveloperNotificationException.ifTrue("unterrichtLerngruppenlehrerAddAllOhneUpdate: Zuordnung existiert bereits!",
					unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.containsKey123(
							zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idLerngruppenLehrer));
		}
		// add all
		for (final @NotNull UvUnterrichtLerngruppenlehrer zuordnung : list) {
			unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.addSingle(
					zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idLerngruppenLehrer, zuordnung);
			unterrichtLerngruppenlehrerMenge.add(zuordnung);
		}
	}

	private void unterrichtLerngruppenlehrerCheck(final @NotNull UvUnterrichtLerngruppenlehrer zuordnung) {
		DeveloperNotificationException.ifInvalidID("zuordnung.idPlanungsabschnitt", zuordnung.idPlanungsabschnitt);
		DeveloperNotificationException.ifInvalidID("zuordnung.idUnterricht", zuordnung.idUnterricht);
		DeveloperNotificationException.ifInvalidID("zuordnung.idLerngruppenLehrer", zuordnung.idLerngruppenLehrer);
		if (!planungsabschnittById.containsKey(zuordnung.idPlanungsabschnitt)) {
			throw new DeveloperNotificationException(
					"unterrichtLerngruppenlehrerCheck: Der UvPlanungsabschnitt mit der ID " + zuordnung.idPlanungsabschnitt + " existiert nicht.");
		}
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvUnterrichtLerngruppenlehrer}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idUnterricht Die ID des Unterrichts.
	 * @param idLerngruppenLehrer Die ID des Lerngruppenlehrers.
	 * @return das zur ID zugehörige {@link UvUnterrichtLerngruppenlehrer}-Objekt.
	 */
	public @NotNull UvUnterrichtLerngruppenlehrer unterrichtLerngruppenlehrerGetByIdOrException(final long idPlanungsabschnitt, final long idUnterricht,
			final long idLerngruppenLehrer) {
		return unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.getSingle123OrException(
				idPlanungsabschnitt, idUnterricht, idLerngruppenLehrer);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte. <br>
	 * @return eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte.
	 */
	public @NotNull List<UvUnterrichtLerngruppenlehrer> unterrichtLerngruppenlehrerGetMengeAsList() {
		return new ArrayList<>(unterrichtLerngruppenlehrerMenge);
	}

	private void unterrichtLerngruppenlehrerRemoveOhneUpdateById(final long idPlanungsabschnitt, final long idUnterricht, final long idLerngruppenLehrer) {
		unterrichtLerngruppenlehrerRemoveOhneUpdate(unterrichtLerngruppenlehrerGetByIdOrException(idPlanungsabschnitt, idUnterricht, idLerngruppenLehrer));
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterrichtLerngruppenlehrer}-Objekt.
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idUnterricht Die ID des Unterrichts.
	 * @param idLerngruppenLehrer Die ID des Lerngruppenlehrers.
	 */
	public void unterrichtLerngruppenlehrerRemoveById(final long idPlanungsabschnitt, final long idUnterricht, final long idLerngruppenLehrer) {
		unterrichtLerngruppenlehrerRemoveOhneUpdateById(idPlanungsabschnitt, idUnterricht, idLerngruppenLehrer);
		updateAll();
	}

	/**
	 * Entfernt ein existierendes {@link UvUnterrichtLerngruppenlehrer}-Objekt.
	 * @param zuordnung das zu entfernende {@link UvUnterrichtLerngruppenlehrer}-Objekt.
	 */
	public void unterrichtLerngruppenlehrerRemove(final @NotNull UvUnterrichtLerngruppenlehrer zuordnung) {
		unterrichtLerngruppenlehrerRemoveOhneUpdate(zuordnung);
		updateAll();
	}

	private void unterrichtLerngruppenlehrerRemoveOhneUpdate(final @NotNull UvUnterrichtLerngruppenlehrer zuordnung) {
		unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.removeSingleOrException(
				zuordnung.idPlanungsabschnitt, zuordnung.idUnterricht, zuordnung.idLerngruppenLehrer);
		unterrichtLerngruppenlehrerMenge.remove(zuordnung);
	}

	/**
	 * Entfernt alle {@link UvUnterrichtLerngruppenlehrer}-Objekte.
	 * @param listZuordnungen Die Liste der zu entfernenden {@link UvUnterrichtLerngruppenlehrer}-Objekte.
	 */
	public void unterrichtLerngruppenlehrerRemoveAll(final @NotNull Collection<UvUnterrichtLerngruppenlehrer> listZuordnungen) {
		unterrichtLerngruppenlehrerRemoveAllOhneUpdate(listZuordnungen);
		updateAll();
	}

	private void unterrichtLerngruppenlehrerRemoveAllOhneUpdate(final @NotNull Collection<UvUnterrichtLerngruppenlehrer> listZuordnungen) {
		final Set<UvUnterrichtLerngruppenlehrer> setZuordnungen = new HashSet<>(listZuordnungen);
		for (final @NotNull UvUnterrichtLerngruppenlehrer zuordnung : setZuordnungen) {
			unterrichtLerngruppenlehrerRemoveOhneUpdate(zuordnung);
		}
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte für einen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvUnterrichtLerngruppenlehrer> unterrichtLerngruppenlehrerGetMengeByPlanungsabschnitt(
			final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte für einen Unterricht. <br>
	 * @param unterricht der {@link UvUnterricht}.
	 * @return eine Liste aller {@link UvUnterrichtLerngruppenlehrer}-Objekte für den Unterricht.
	 */
	public @NotNull List<UvUnterrichtLerngruppenlehrer> unterrichtLerngruppenlehrerGetMengeByUnterricht(final @NotNull UvUnterricht unterricht) {
		return unterrichtLerngruppenlehrerByIdPlanungsabschnittAndIdUnterrichtAndIdLerngruppenlehrer.get12(unterricht.idPlanungsabschnitt,
				unterricht.id);
	}

	// #####################################################################
	// ######################### Manager-Methoden ##########################
	// #####################################################################

	/**
	 * Liefert eine Liste aller {@link UvLehrerPflichtstundensoll}-Objekte für einen Lehrer. <br>
	 * @param lehrer der {@link UvLehrer}
	 * @return eine Liste aller {@link UvLehrerPflichtstundensoll}-Objekte für den Lehrer.
	 */
	public @NotNull List<UvLehrerPflichtstundensoll> lehrerPflichtstundensollGetMengeByLehrer(final @NotNull UvLehrer lehrer) {
		final List<UvLehrerPflichtstundensoll> list = lehrerPflichtstundensollMengeByLehrerId.get(lehrer.id);
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrer}-Objekte für den aktuellen Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return eine Liste aller {@link UvLehrer}-Objekte für den aktuellen Planungsabschnitt.
	 */
	public @NotNull List<UvLehrer> lehrerGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		final @NotNull List<UvLehrer> list = new ArrayList<>();
		for (final @NotNull UvPlanungsabschnittLehrer planungsabschnittLehrer : planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer
				.get1(planungsabschnitt.id)) {
			list.add(lehrerGetByPlanungsabschnittLehrer(planungsabschnittLehrer));
		}
		list.sort(compLehrer);
		return list;
	}

	/**
	 * Liefert einen {@link UvLehrer}, der dem angegebenen {@link UvPlanungsabschnittLehrer} zugeordnet ist.
	 *
	 * @param planungsabschnittLehrer Der {@link UvPlanungsabschnittLehrer}, für den der zugehörige {@link UvLehrer} abgerufen werden soll.
	 * @return Der {@link UvLehrer}, der dem angegebenen {@link UvPlanungsabschnittLehrer} entspricht.
	 * @throws DeveloperNotificationException Wenn kein {@link UvLehrer} mit der angegebenen ID gefunden wird.
	 */
	public @NotNull UvLehrer lehrerGetByPlanungsabschnittLehrer(final @NotNull UvPlanungsabschnittLehrer planungsabschnittLehrer) {
		return DeveloperNotificationException.ifMapGetIsNull(lehrerById, planungsabschnittLehrer.idLehrer);
	}

	/**
	 * Liefert den {@link UvPlanungsabschnittSchueler}, der durch die übergebene {@link UvSchuelergruppeSchueler}
	 * identifiziert wird.
	 *
	 * @param schuelergruppeSchueler Das {@link UvSchuelergruppeSchueler}-Objekt, welches die Identifikationsinformationen
	 *                               für den Planungsabschnittsschüler beinhaltet.
	 * @return Der gefundene {@link UvPlanungsabschnittSchueler}.
	 */
	public @NotNull UvPlanungsabschnittSchueler planungsabschnittSchuelerGetBySchuelergruppeSchueler(
			final @NotNull UvSchuelergruppeSchueler schuelergruppeSchueler) {
		return planungsabschnittSchuelerGetByIdOrException(schuelergruppeSchueler.idPlanungsabschnitt, schuelergruppeSchueler.idSchueler);
	}

	/**
	 * Liefert eine Liste aller {@link UvLehrerAnrechnungsstunden}-Objekte für einen Lehrer. <br>
	 * @param lehrer der {@link UvLehrer}
	 * @return eine Liste aller {@link UvLehrerAnrechnungsstunden}-Objekte für den Lehrer.
	 */
	public @NotNull List<UvLehrerAnrechnungsstunden> lehrerAnrechnungsstundenGetMengeByLehrer(final @NotNull UvLehrer lehrer) {
		final List<UvLehrerAnrechnungsstunden> list = lehrerAnrechnungsstundenMengeByLehrerId.get(lehrer.id);
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert {@code true}, wenn der Lehrer für diesen Planungsabschnitt angelegt ist.
	 * @param lehrer der {@link UvLehrer}
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return {@code true}, wenn der Lehrer für diesen Planungsabschnitt angelegt ist.
	 */
	public boolean lehrerIsInPlanungsabschnitt(final @NotNull UvLehrer lehrer, final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.containsKey12(planungsabschnitt.id, lehrer.id);
	}

	/**
	 * Liefert eine Liste aller fehlenden {@link UvLehrer}-Objekte, die zwar im Zeitraum des angegebenen Planungsabschnitts an der Schule tätig waren, aber
	 * nicht für den Planungsabschnitt angelegt sind.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return eine Liste aller fehlenden {@link UvLehrer}-Objekte für den aktuellen Planungsabschnitt.
	 */
	public @NotNull List<UvLehrer> lehrerGetMengeTaetigAberNichtInPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		final @NotNull List<UvLehrer> result = new ArrayList<>();
		for (final UvLehrer l : lehrerMenge) {
			if (lehrerIsInPlanungsabschnitt(l, planungsabschnitt)) {
				continue;
			}
			final boolean taetigImZeitraum = ((l.datumZugang == null) || (l.datumZugang.compareTo(planungsabschnitt.gueltigBis) <= 0))
					&& ((l.datumAbgang == null) || (l.datumAbgang.compareTo(planungsabschnitt.gueltigVon) >= 0));
			if (taetigImZeitraum) {
				result.add(l);
			}
		}
		return result;
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvLehrer} referenziert werden und gibt diese als Menge zurück.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @param lehrer die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvLehrer}
	 */
	public @NotNull Set<UvLehrer> lehrerGetMengeVerwendetInPlanungsabschnittByLehrerMenge(final @NotNull UvPlanungsabschnitt planungsabschnitt,
			final @NotNull Collection<UvLehrer> lehrer) {
		final @NotNull Set<UvLehrer> result = new HashSet<>();
		for (final UvLehrer l : new HashSet<>(lehrer)) {
			if (lerngruppenLehrerByIdPlanungsabschnittAndIdLerngruppeAndIdLehrer.containsKey13(planungsabschnitt.id, l.id)) {
				result.add(l);
			}
		}
		return result;
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvLehrer} referenziert werden und gibt diese als Menge zurück.
	 * @param lehrer die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvLehrer}
	 */
	public @NotNull Set<UvLehrer> lehrerGetMengeVerwendetByLehrerMenge(final @NotNull Collection<UvLehrer> lehrer) {
		final @NotNull Set<UvLehrer> result = new HashSet<>();
		for (final UvLehrer l : new HashSet<>(lehrer)) {
			if (planungsabschnittLehrerByIdPlanungsabschnittAndIdLehrer.containsKey2(l.id)) {
				result.add(l);
			}
		}
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link UvRaum}-Objekte für eine {@link UvRaumgruppe}. <br>
	 * @param raumgruppe die {@link UvRaumgruppe}
	 * @return eine Liste aller {@link UvRaum}-Objekte für die {@link UvRaumgruppe}.
	 */
	public @NotNull List<UvRaum> raumGetMengeByRaumgruppe(final @NotNull UvRaumgruppe raumgruppe) {
		return raumByIdRaumgruppeAndIdRaum.get1(raumgruppe.id);
	}

	/**
	 * Liefert den {@link UvRaum} zum übergebenen Kürzel oder {@code null}, falls kein Raum mit diesem Kürzel existiert.
	 *
	 * @param kuerzel das Kürzel des gesuchten {@link UvRaum}s
	 * @return den zugehörigen {@link UvRaum} oder {@code null}, falls kein passender Raum existiert
	 */
	public UvRaum raumGetByKuerzelOrNull(final @NotNull String kuerzel) {
		return raumByKuerzel.get(kuerzel);
	}

	/**
	 * Liefert eine Liste aller {@link UvStundentafelFach}-Objekte für eine Stundentafel. <br>
	 * @param stundentafel die {@link UvStundentafel}
	 * @param abschnitt der Abschnitt des Schuljahres
	 * @return eine Liste aller {@link UvStundentafelFach}-Objekte für die Stundentafel.
	 */
	public @NotNull List<UvStundentafelFach> stundentafelFachGetMengeByStundentafelAndAbschnitt(final @NotNull UvStundentafel stundentafel,
			final int abschnitt) {
		final List<UvStundentafelFach> list =
				stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.get12(stundentafel.id, abschnitt);
		return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
	}

	/**
	 * Liefert das {@link UvFach}-Objekt für ein {@link UvStundentafelFach}-Objekt.
	 * @param stundentafelFach das {@link UvStundentafelFach}-Objekt
	 * @return das zugehörige {@link UvFach}-Objekt
	 */
	public @NotNull UvFach fachGetByStundentafelFach(final @NotNull UvStundentafelFach stundentafelFach) {
		return fachGetByIdOrException(stundentafelFach.idFach);
	}

	/**
	 * Liefert eine Liste aller {@link UvFach}-Objekte, die im Gültigkeitsintervall
	 * der übergebenen {@link UvStundentafel} gültig sind.
	 *
	 * @param stundentafel   die {@link UvStundentafel}, deren Gültigkeitsintervall geprüft wird
	 * @param abschnitt      das Halbjahr
	 * @return die Liste der Fächer, die im Gültigkeitsintervall der Stundentafel gültig sind
	 */
	public @NotNull List<UvFach> fachGetMengeGueltigUndFehlendByStundentafel(final @NotNull UvStundentafel stundentafel, final int abschnitt) {
		final @NotNull List<UvFach> list = new ArrayList<>();
		for (final @NotNull UvFach fach : fachMenge) {
			if (stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach.containsKey123(stundentafel.id, abschnitt, fach.id)) {
				continue;
			}
			if (DateUtils.intervallUeberlappt(stundentafel.gueltigVon, stundentafel.gueltigBis, fach.gueltigVon, fach.gueltigBis)) {
				list.add(fach);
			}
		}
		return list;
	}

	/**
	 * Prüft, ob das übergebene {@link UvFach}-Objekt einen Gültigkeitskonflikt mit einem anderen
	 * {@link UvFach}-Objekt derselben Fachart hat.
	 *
	 * @param fach   das zu prüfende Fach
	 * @return das andere Fach mit Gültigkeitskonflikt oder {@code null}, wenn kein Konflikt besteht
	 */
	public UvFach fachGetByGueltigkeitskonfliktMitFach(final @NotNull UvFach fach) {
		return fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(fach, fach.gueltigVon, fach.gueltigBis);
	}

	/**
	 * Prüft, ob das übergebene {@link UvFach}-Objekt einen Gültigkeitskonflikt mit einem anderen
	 * {@link UvFach}-Objekt derselben Fachart hat.
	 *
	 * @param fach   das zu prüfende Fach
	 * @param gueltigVon der neue Beginn des Gültigkeitsintervalls
	 * @param gueltigBis das neue Ende des Gültigkeitsintervalls
	 * @return das andere Fach mit Gültigkeitskonflikt oder {@code null}, wenn kein Konflikt besteht
	 */
	public UvFach fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(final @NotNull UvFach fach, final @NotNull String gueltigVon, final String gueltigBis) {
		for (final @NotNull UvFach otherFach : fachMenge) {
			if ((otherFach.idFach != fach.idFach) || (otherFach.id == fach.id)) {
				continue;
			}
			if (DateUtils.intervallUeberlappt(gueltigVon, gueltigBis, otherFach.gueltigVon, otherFach.gueltigBis)) {
				return otherFach;
			}
		}
		return null;
	}

	/**
	 * Liefert eine Liste aller {@link UvRaumgruppe}-Objekte, die im Gültigkeitsintervall
	 * des übergebenen {@link UvRaum} gültig sind.
	 *
	 * @param raum   der {@link UvRaum}, dessen Gültigkeitsintervall geprüft wird
	 * @return die Liste der Raumgruppen, die im Gültigkeitsintervall des Raums gültig sind
	 */
	public @NotNull List<UvRaumgruppe> raumgruppeGetMengeGueltigByRaum(final @NotNull UvRaum raum) {
		return raumgruppeGetMengeGueltigByZeitraum(raum.gueltigVon, raum.gueltigBis);
	}

	/**
	 * Liefert eine Liste aller {@link UvRaumgruppe}-Objekte, die im übergebenen Gültigkeitsintervall gültig sind.
	 *
	 * @param gueltigVon Beginn des Gültigkeitsintervalls
	 * @param gueltigBis Ende des Gültigkeitsintervalls
	 * @return die Liste der Raumgruppen, die im Gültigkeitsintervall des Raums gültig sind
	 */
	public @NotNull List<UvRaumgruppe> raumgruppeGetMengeGueltigByZeitraum(final @NotNull String gueltigVon, final String gueltigBis) {
		final @NotNull List<UvRaumgruppe> list = new ArrayList<>();
		for (final @NotNull UvRaumgruppe raumgruppe : raumgruppeMenge) {
			if (DateUtils.intervallUeberlappt(gueltigVon, gueltigBis, raumgruppe.gueltigVon, raumgruppe.gueltigBis)) {
				list.add(raumgruppe);
			}
		}
		return list;
	}

	/**
	 * Gibt eine Liste aller {@link UvFach}-Objekte zurück, die zu den gegebenen {@link FachDaten} gehören.
	 *
	 * @param fachdaten   Die {@link FachDaten} (Schulfach).
	 *
	 * @return Eine Liste der {@link UvFach}-Objekte zu den {@link FachDaten}.
	 */
	public @NotNull List<UvFach> fachGetMengeByFachdaten(final @NotNull FachDaten fachdaten) {
		return fachMengeByIdFach.get(fachdaten.id);
	}

	/**
	 * Liefert das zum Fach und Planungsabschnitt gehörige {@link UvFach}-Objekt oder {@code null}.
	 *
	 * @param idFach die ID des Schulfachs
	 * @param planungsabschnitt der Planungsabschnitt
	 * @return das passende {@link UvFach}-Objekt oder {@code null}
	 */
	public UvFach fachGetByIdFachAndPlanungsabschnittOrNull(final long idFach, final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		for (final @NotNull UvFach fach : fachMengeByIdFach.get(idFach)) {
			if (DateUtils.intervallUeberlappt(planungsabschnitt.gueltigVon, planungsabschnitt.gueltigBis, fach.gueltigVon, fach.gueltigBis)) {
				return fach;
			}
		}
		return null;
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitrasterEintrag}-Objekte für ein bestimmtes Zeitraster. <br>
	 * @param idZeitraster Die ID des Zeitrasters.
	 * @return eine Liste aller {@link UvZeitrasterEintrag}-Objekte für das Zeitraster.
	 */
	public @NotNull List<UvZeitrasterEintrag> zeitrasterEintragGetMengeByZeitraster(final long idZeitraster) {
		return zeitrasterEintragByIdZeitrasterAndIdZeitrasterEintrag.get1(idZeitraster);
	}

	/**
	 * Liefert das zur ID zugehörige {@link UvPlanungsabschnittZeitraster}-Objekt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @param idZeitraster Die ID des Zeitrasters.
	 * @return das zur ID zugehörige {@link UvPlanungsabschnittZeitraster}-Objekt.
	 */
	public @NotNull UvPlanungsabschnittZeitraster planungsabschnittZeitrasterGetByIdOrException(final long idPlanungsabschnitt, final long idZeitraster) {
		return planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.getSingle12OrException(idPlanungsabschnitt, idZeitraster);
	}

	/**
	 * Liefert eine Liste aller {@link UvPlanungsabschnittZeitraster}-Objekte für einen bestimmten Planungsabschnitt. <br>
	 * @param idPlanungsabschnitt Die ID des Planungsabschnitts.
	 * @return eine Liste aller {@link UvPlanungsabschnittZeitraster}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvPlanungsabschnittZeitraster> planungsabschnittZeitrasterGetMengeByPlanungsabschnitt(final long idPlanungsabschnitt) {
		return planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.get1(idPlanungsabschnitt);
	}

	/**
	 * Prüft, ob ein {@link UvZeitraster} einem {@link UvPlanungsabschnitt} zugeordnet ist.
	 * @param zeitraster das {@link UvZeitraster}
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @return {@code true}, wenn das Zeitraster dem Planungsabschnitt zugeordnet ist, sonst {@code false}.
	 */
	public boolean zeitrasterIsInPlanungsabschnitt(final @NotNull UvZeitraster zeitraster, final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.containsKey12(planungsabschnitt.id,
				zeitraster.id);
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitraster}-Objekte, die einem Planungsabschnitt zugeordnet sind. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvZeitraster}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvZeitraster> zeitrasterGetMengeByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		final @NotNull List<UvZeitraster> result = new ArrayList<>();
		for (final @NotNull UvPlanungsabschnittZeitraster zuordnung : planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster
				.get1(planungsabschnitt.id)) {
			result.add(zeitrasterGetByIdOrException(zuordnung.idZeitraster));
		}
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link UvZeitraster}-Objekte, die im Gültigkeitsintervall des übergebenen
	 * {@link UvPlanungsabschnitt} gültig sind.
	 *
	 * @param planungsabschnitt   der {@link UvPlanungsabschnitt}, dessen Gültigkeitsintervall geprüft wird
	 * @return die Liste der {@link UvZeitraster}, die im Gültigkeitsintervall gültig sind
	 */
	public @NotNull List<UvZeitraster> zeitrasterGetMengeGueltigByPlanungsabschnitt(final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		final @NotNull Set<UvZeitraster> set = new HashSet<>();
		for (final @NotNull UvZeitraster zeitraster : zeitrasterMenge) {
			if (DateUtils.intervallUeberlappt(planungsabschnitt.gueltigVon, planungsabschnitt.gueltigBis, zeitraster.gueltigVon, zeitraster.gueltigBis)) {
				set.add(zeitraster);
			}
		}
		set.addAll(zeitrasterGetMengeByPlanungsabschnitt(planungsabschnitt));
		final @NotNull List<UvZeitraster> list = new ArrayList<>(set);
		list.sort(compZeitraster);
		return list;
	}

	/**
	 * Liefert die zu einem {@link UvPlanungsabschnittZeitraster} gehörigen Jahrgänge.
	 * @param zeitraster das {@link UvPlanungsabschnittZeitraster}-Objekt
	 * @return die Liste der zugeordneten {@link JahrgangsDaten}
	 */
	public @NotNull List<JahrgangsDaten> jahrgangsdatenGetMengeByPlanungsabschnittZeitraster(final @NotNull UvPlanungsabschnittZeitraster zeitraster) {
		final @NotNull List<JahrgangsDaten> result = new ArrayList<>();
		for (final @NotNull Long jahrgangId : zeitraster.idsJahrgaenge) {
			result.add(DeveloperNotificationException.ifMapGetIsNull(jahrgangsdatenByIdMapGet(), jahrgangId));
		}
		return result;
	}

	/**
	 * Liefert das {@link UvPlanungsabschnittZeitraster}-Objekt für den übergebenen {@link UvPlanungsabschnitt} und das {@link UvZeitraster}.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @param zeitraster das {@link UvZeitraster}
	 * @return das zugehörige {@link UvPlanungsabschnittZeitraster}-Objekt
	 */
	public @NotNull UvPlanungsabschnittZeitraster planungsabschnittZeitrasterGetByPlanungsabschnittAndZeitraster(
			final @NotNull UvPlanungsabschnitt planungsabschnitt,
			final @NotNull UvZeitraster zeitraster) {
		return planungsabschnittZeitrasterByIdPlanungsabschnittAndIdZeitraster.getSingle12OrException(planungsabschnitt.id,
				zeitraster.id);
	}

	/**
	 * Liefert die zu einem {@link UvPlanungsabschnittZeitraster} gehörigen Jahrgänge.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @param zeitraster das {@link UvZeitraster}
	 * @return die Liste der zugeordneten {@link JahrgangsDaten}
	 */
	public @NotNull List<JahrgangsDaten> jahrgangsdatenGetMengeByPlanungsabschnittAndZeitraster(final @NotNull UvPlanungsabschnitt planungsabschnitt,
			final @NotNull UvZeitraster zeitraster) {
		return jahrgangsdatenGetMengeByPlanungsabschnittZeitraster(
				planungsabschnittZeitrasterGetByPlanungsabschnittAndZeitraster(planungsabschnitt, zeitraster));
	}

	/**
	 * Liefert die Liste aller in Stundentafeln verwendeter {@link UvFach}-Objekte.
	 * @return die Liste aller in Stundentafeln verwendeter {@link UvFach}-Objekte.
	 */
	public @NotNull List<UvFach> fachGetMengeVerwendetInStundentafel() {
		final @NotNull List<UvFach> result = new ArrayList<>(fachGetMengeAsList());
		final var it = result.iterator();
		while (it.hasNext()) {
			final UvFach fach = it.next();
			if (!stundentafelFachByIdStundentafelAndAbschnittAndIdFachAndIdStundentafelFach
					.containsKey3(fach.id)) {
				it.remove();
			}
		}
		return result;
	}

	/**
	 * Liefert eine Liste aller {@link UvPlanungsabschnittSchueler}-Objekte für einen bestimmten Planungsabschnitt. <br>
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}.
	 * @return eine Liste aller {@link UvPlanungsabschnittSchueler}-Objekte für den Planungsabschnitt.
	 */
	public @NotNull List<UvPlanungsabschnittSchueler> planungsabschnittSchuelerGetMengeByPlanungsabschnitt(
			final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		return planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.get1(planungsabschnitt.id);
	}

	/**
	 * Liefert zum Planungsabschnitt und der Nummer der Schiene das {@link UvSchiene}-Objekt, falls es existiert, sonst {@code null}.
	 * @param planungsabschnitt der {@link UvPlanungsabschnitt}
	 * @param nummer die Nummer der Schiene
	 * @return die Schiene, falls sie existiert
	 */
	public UvSchiene schieneGetByPlanungsabschnittAndNummer(final @NotNull UvPlanungsabschnitt planungsabschnitt, final int nummer) {
		return schieneByIdPlanungsabschnittAndNummerAndIdSchiene.getSingle12OrNull(planungsabschnitt.id, nummer);
	}

	/**
	 * Liefert alle Schüler einer Gruppe
	 * @param gruppe die {@link UvSchuelergruppe}
	 * @return die Liste von {@link UvPlanungsabschnittSchueler}-Objekten
	 */
	public @NotNull List<UvPlanungsabschnittSchueler> planungsabschnittSchuelerGetMengeBySchuelergruppe(final @NotNull UvSchuelergruppe gruppe) {
		return planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe.get2(gruppe.id);
	}

	/**
	 * Liefert alle Schüler einer {@link UvKlasse}
	 * @param klasse die {@link UvKlasse}
	 * @return die Liste von {@link UvPlanungsabschnittSchueler}-Objekten
	 */
	public @NotNull List<UvPlanungsabschnittSchueler> planungsabschnittSchuelerGetMengeByKlasse(final @NotNull UvKlasse klasse) {
		return planungsabschnittSchuelerMengeByIdPlanungsabschnittAndIdSchuelergruppe.get2(klasse.idSchuelergruppe);
	}

	/**
	 * Gibt zurück, ob der Schülerjahrgang in die Schülergruppe passt.
	 * @param schueler der zu prüfende {@link UvSchuelergruppeSchueler}
	 * @return {@code true}, falls der Jahrgang in der Gruppe zugelassen ist, sonst {@code false}
	 */
	public boolean schuelergruppeSchuelerHatZurGruppePassendenJahrgang(final @NotNull UvSchuelergruppeSchueler schueler) {
		final @NotNull UvPlanungsabschnittSchueler s = planungsabschnittSchuelerGetBySchuelergruppeSchueler(schueler);
		final @NotNull UvSchuelergruppe g = schuelergruppeGetByIdOrException(schueler.idSchuelergruppe);
		return g.idsJahrgaengeErlaubt.contains(s.idJahrgang);
	}

	/**
	 * Gibt zurück, ob die {@link UvSchuelergruppe} Schüler mit falscher Jahrgangszugehörigkeit beinhaltet
	 * @param gruppe die zu prüfende {@link UvSchuelergruppe}
	 * @return {@code true}, falls es widersprüchliche Daten gibt, sonst {@code false}
	 */
	public boolean schuelergruppeHatSchuelerMitFalschemJahrgang(final @NotNull UvSchuelergruppe gruppe) {
		for (final @NotNull UvSchuelergruppeSchueler schueler : schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler
				.get2(gruppe.id)) {
			if (!schuelergruppeSchuelerHatZurGruppePassendenJahrgang(schueler)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gibt zurück, ob der Schülerjahrgang in die Schülergruppe passt.
	 * @param schueler der zu prüfende {@link UvPlanungsabschnittSchueler}
	 * @param gruppe die zu prüfende {@link UvSchuelergruppe}
	 * @return {@code true}, falls der Jahrgang in der Gruppe zugelassen ist, sonst {@code false}
	 */
	public boolean planungsabschnittSchuelerHatZurGruppePassendenJahrgang(final @NotNull UvPlanungsabschnittSchueler schueler,
			final @NotNull UvSchuelergruppe gruppe) {
		final @NotNull UvSchuelergruppeSchueler s =
				schuelergruppeSchuelerByIdPlanungsabschnittAndIdSchuelergruppeAndIdSchueler.getSingle123OrException(schueler.idPlanungsabschnitt,
						gruppe.id,
						schueler.idSchueler);
		return schuelergruppeSchuelerHatZurGruppePassendenJahrgang(s);
	}

	/**
	 * Liefert die {@link UvSchuelergruppe} zu einer {@link UvKlasse}
	 * @param klasse die {@link UvKlasse}, zu der die {@link UvSchuelergruppe} gesucht wird
	 * @return die {@link UvSchuelergruppe}
	 */
	public @NotNull UvSchuelergruppe schuelergruppeGetByKlasse(final @NotNull UvKlasse klasse) {
		return schuelergruppeGetByIdOrException(klasse.idSchuelergruppe);
	}

	/**
	 * Liefert die {@link UvSchuelergruppe} zu einem {@link UvKurs}
	 * @param kurs der {@link UvKurs}, zu dem die {@link UvSchuelergruppe} gesucht wird
	 * @return die {@link UvSchuelergruppe}
	 */
	public @NotNull UvSchuelergruppe schuelergruppeGetByKurs(final @NotNull UvKurs kurs) {
		return schuelergruppeGetByIdOrException(kurs.idSchuelergruppe);
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvKlasse}n referenziert werden und gibt diese als Menge zurück.
	 * @param klassen die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvKlasse}n
	 */
	public @NotNull Set<UvKlasse> klasseGetMengeVerwendetByKlasseMenge(final @NotNull Collection<UvKlasse> klassen) {
		final @NotNull Set<UvKlasse> result = new HashSet<>();
		for (final UvKlasse klasse : new HashSet<>(klassen)) {
			if (planungsabschnittSchuelerByIdPlanungsabschnittAndIdJahrgangAndIdKlasseAndIdSchueler.containsKey3(klasse.id)
					|| lerngruppeByIdKursAndIdKlasseAndIdFach.containsKey2(klasse.id)) {
				result.add(klasse);
			}
		}
		return result;
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvSchiene}n referenziert werden und gibt diese als Menge zurück.
	 * @param schienen die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvSchiene}n
	 */
	public @NotNull Set<UvSchiene> schieneGetMengeVerwendetBySchieneMenge(final @NotNull Collection<UvSchiene> schienen) {
		final @NotNull Set<UvSchiene> result = new HashSet<>();
		for (final UvSchiene schiene : new HashSet<>(schienen)) {
			if (lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.containsKey3(schiene.id)) {
				result.add(schiene);
			}
		}
		return result;
	}

	/**
	 * Prüft für die übergebene Menge, welche {@link UvSchuelergruppe}n referenziert werden und gibt diese als Menge zurück.
	 * @param schuelergruppen die zu prüfende Menge
	 * @return die Menge der verwendeten {@link UvSchuelergruppe}n
	 */
	public @NotNull Set<UvSchuelergruppe> schuelergruppeGetMengeVerwendetBySchuelergruppeMenge(final @NotNull Collection<UvSchuelergruppe> schuelergruppen) {
		final @NotNull Set<UvSchuelergruppe> result = new HashSet<>();
		for (final UvSchuelergruppe schuelergruppe : new HashSet<>(schuelergruppen)) {
			if (klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.containsKey2(schuelergruppe.id)
					|| kursByIdPlanungsabschnittAndIdSchuelergruppeAndIdKurs.containsKey2(schuelergruppe.id)) {
				result.add(schuelergruppe);
			}
		}
		return result;
	}

	/**
	 * Gibt die Menge der {@link UvLerngruppenSchiene}n zur übergebenen {@link UvLerngruppe} zurück.
	 * @param lerngruppe die {@link UvLerngruppe}, zu der die Schienen gesucht werden
	 * @return die Menge der zugeordneten {@link UvLerngruppenSchiene}-Objekte
	 */
	public @NotNull List<UvLerngruppenSchiene> lerngruppenSchieneGetMengeByLerngruppe(final @NotNull UvLerngruppe lerngruppe) {
		return lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get12(lerngruppe.idPlanungsabschnitt, lerngruppe.id);
	}

	/**
	 * Gibt die Menge der {@link UvLerngruppenSchiene}n zur übergebenen {@link UvSchiene} zurück.
	 * @param schiene die {@link UvSchiene}, zu der die {@link UvLerngruppe}n gesucht werden
	 * @return die Menge der zugeordneten {@link UvLerngruppenSchiene}-Objekte
	 */
	public @NotNull List<UvLerngruppenSchiene> lerngruppenSchieneGetMengeBySchiene(final @NotNull UvSchiene schiene) {
		return lerngruppenSchieneByIdPlanungsabschnittAndIdLerngruppeAndIdSchiene.get13(schiene.idPlanungsabschnitt, schiene.id);
	}

	/**
	 * Liefert das zur {@link UvLerngruppenSchiene} zugehörige {@link UvLerngruppe}-Objekt. <br>
	 * @param schiene die {@link UvLerngruppenSchiene}, zu der die {@link UvLerngruppe} gesucht wird.
	 * @return das zugehörige {@link UvLerngruppe}-Objekt.
	 */
	public @NotNull UvLerngruppe lerngruppeGetByLerngruppenSchiene(final @NotNull UvLerngruppenSchiene schiene) {
		return lerngruppeByIdPlanungsabschnittAndIdLerngruppe.getSingle12OrException(schiene.idPlanungsabschnitt, schiene.idLerngruppe);
	}

	/**
	 * Liefert die Fachdaten zum übergebenen Kurs
	 * @param kurs der {@link UvKurs}
	 * @return die {@link FachDaten}
	 */
	public @NotNull FachDaten fachdatenGetByKurs(final @NotNull UvKurs kurs) {
		return fachdatenGetByFach(fachGetByKurs(kurs));
	}

	/**
	 * Liefert das Fach zum übergebenen Kurs
	 * @param kurs der {@link UvKurs}
	 * @return das {@link UvFach}
	 */
	public @NotNull UvFach fachGetByKurs(final @NotNull UvKurs kurs) {
		return fachGetByIdOrException(kurs.idFach);
	}

	/**
	 * Liefert den Kurs zur Lerngruppe oder {@code null}, falls es sich um Klassenunterricht handelt
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return der zugeordnete {@link UvKurs} oder {@code null}, falls es sich um Klassenunterricht handelt
	 */
	public UvKurs kursGetByLerngruppe(final @NotNull UvLerngruppe lerngruppe) {
		if (lerngruppe.idKurs == null) {
			return null;
		}
		return kursGetByIdOrException(lerngruppe.idKurs);
	}

	/**
	 * Liefert die Klasse zur Lerngruppe oder {@code null}, falls es sich um Kursunterricht handelt
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return die zugeordnete {@link UvKlasse} oder {@code null}, falls es sich um Kursunterricht handelt
	 */
	public UvKlasse klasseGetByLerngruppe(final @NotNull UvLerngruppe lerngruppe) {
		if (lerngruppe.idKlasse == null) {
			return null;
		}
		return klasseGetByIdOrException(lerngruppe.idKlasse);
	}

	/**
	 * Liefert alle {@link UvKlasse}-Objekte zur {@link UvLerngruppe}.
	 * Bei Klassenunterricht enthält die Liste genau die zugeordnete Klasse.
	 * Bei Kursunterricht enthält die Liste alle Klassen der zugehörigen Schülergruppe.
	 *
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return die zugeordneten {@link UvKlasse}-Objekte
	 */
	public @NotNull List<UvKlasse> klasseGetMengeByLerngruppe(final @NotNull UvLerngruppe lerngruppe) {
		if (lerngruppe.idKlasse != null) {
			return ListUtils.create1(klasseGetByIdOrException(lerngruppe.idKlasse));
		}
		final UvKurs kurs = kursGetByLerngruppe(lerngruppe);
		if (kurs == null) {
			return new ArrayList<>();
		}
		return klasseByIdPlanungsabschnittAndIdSchuelergruppeAndIdKlasse.get12(lerngruppe.idPlanungsabschnitt, kurs.idSchuelergruppe);
	}

	/**
	 * Liefert alle {@link JahrgangsDaten} zur {@link UvLerngruppe}.
	 * In der Regel enthält die Liste genau einen Jahrgang.
	 *
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return die zugeordneten {@link JahrgangsDaten}
	 */
	public @NotNull List<JahrgangsDaten> jahrgangsdatenGetMengeByLerngruppe(final @NotNull UvLerngruppe lerngruppe) {
		final UvSchuelergruppe schuelergruppe = schuelergruppeGetByLerngruppe(lerngruppe);
		if (schuelergruppe == null) {
			return new ArrayList<>();
		}
		return jahrgangsdatenGetMengeBySchuelergruppe(schuelergruppe);
	}

	/**
	 * Liefert die tatsächlich vertretenen Jahrgänge der Schüler einer Schülergruppe ohne Duplikate.
	 * Die Liste ist nach Jahrgangssortierung und bei gleicher Sortierung nach ID sortiert.
	 * Erlaubte Jahrgänge ohne zugeordnete Schüler werden nicht berücksichtigt.
	 *
	 * @param schuelergruppe die auszuwertende Schülergruppe
	 * @return die sortierten Jahrgangsdaten der zugeordneten Schüler; bei leerer Gruppe eine leere Liste
	 */
	public @NotNull List<JahrgangsDaten> jahrgangsdatenGetMengeBySchuelergruppe(final @NotNull UvSchuelergruppe schuelergruppe) {
		final @NotNull List<JahrgangsDaten> result = new ArrayList<>();
		final @NotNull Set<Long> idsJahrgaenge = new HashSet<>();
		for (final @NotNull UvPlanungsabschnittSchueler schueler : planungsabschnittSchuelerGetMengeBySchuelergruppe(schuelergruppe)) {
			if (idsJahrgaenge.add(schueler.idJahrgang)) {
				result.add(jahrgangsdatenGetById(schueler.idJahrgang));
			}
		}
		result.sort(compJahrgangsdaten);
		return result;
	}

	/**
	 * Liefert die {@link UvSchuelergruppe} zur übergebenen {@link UvLerngruppe}.
	 * Bei Klassenunterricht wird die Schülergruppe über die Klasse ermittelt,
	 * bei Kursunterricht direkt über den {@link UvKurs}.
	 *
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return die zugehörige {@link UvSchuelergruppe} oder {@code null}, falls weder eine Klasse noch ein Kurs zugeordnet ist
	 */
	public UvSchuelergruppe schuelergruppeGetByLerngruppe(final @NotNull UvLerngruppe lerngruppe) {
		final UvKlasse klasse = klasseGetByLerngruppe(lerngruppe);
		if (klasse != null) {
			return schuelergruppeGetByKlasse(klasse);
		}
		final UvKurs kurs = kursGetByLerngruppe(lerngruppe);
		return (kurs == null) ? null : schuelergruppeGetByKurs(kurs);
	}

	/**
	 * Liefert das {@link UvFach} zur {@link UvLerngruppe} oder {@code null}, falls kein Fach zugeordnet ist.
	 * Bei Kursunterricht wird das Fach aus dem zugeordneten {@link UvKurs} ermittelt,
	 * bei Klassenunterricht direkt aus der {@link UvLerngruppe}.
	 *
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return das zugeordnete {@link UvFach} oder {@code null}
	 */
	public UvFach fachGetByLerngruppe(final @NotNull UvLerngruppe lerngruppe) {
		final UvKurs kurs = kursGetByLerngruppe(lerngruppe);
		if (kurs != null) {
			return fachGetByIdOrException(kurs.idFach);
		}
		if (lerngruppe.idFach == null) {
			return null;
		}
		return fachGetByIdOrException(lerngruppe.idFach);
	}

	/**
	 * Liefert zu einer {@link UvSchuelergruppe} alle zugeordneten {@link UvLerngruppe}n
	 * @param schuelergruppe die {@link UvSchuelergruppe}
	 * @return die zugeordneten {@link UvLerngruppe}n
	 */
	public @NotNull List<UvLerngruppe> lerngruppeGetMengeBySchuelergruppe(final @NotNull UvSchuelergruppe schuelergruppe) {
		return lerngruppeMengeByIdSchuelergruppe.get(schuelergruppe.id);
	}

	/**
	 * Liefert das {@link UvLehrer}-Objekt zu einem {@link UvLerngruppenLehrer}.
	 * @param lehrer der {@link UvLerngruppenLehrer}
	 * @return der {@link UvLehrer}
	 */
	public @NotNull UvLehrer lehrerGetByLerngruppenLehrer(final @NotNull UvLerngruppenLehrer lehrer) {
		return lehrerGetByIdOrException(lehrer.idLehrer);
	}

	/**
	 * Liefert das {@link UvLehrer}-Objekt zu einem {@link UvKlassenLehrer}.
	 * @param klassenLehrer der {@link UvKlassenLehrer}
	 * @return der {@link UvLehrer}
	 */
	public @NotNull UvLehrer lehrerGetByKlassenLehrer(final @NotNull UvKlassenLehrer klassenLehrer) {
		return lehrerGetByIdOrException(klassenLehrer.idLehrer);
	}

	/**
	 * Liefert einen {@link UvLehrer} basierend auf dem angegebenen {@link UvLehrerPflichtstundensoll}.
	 *
	 * @param pflichtstundensoll das {@link UvLehrerPflichtstundensoll}, das die Pflichtstunden und die Lehrer-ID enthält
	 * @return der {@link UvLehrer}, der der angegebenen Lehrer-ID im {@link UvLehrerPflichtstundensoll} entspricht
	 */
	public @NotNull UvLehrer lehrerGetByLehrerPflichtstundensoll(final @NotNull UvLehrerPflichtstundensoll pflichtstundensoll) {
		return lehrerGetByIdOrException(pflichtstundensoll.idLehrer);
	}

	/**
	 * Ruft einen Lehrer basierend auf den Anrechnungsstunden ab.
	 *
	 * @param anrechnungsstunden die {@link UvLehrerAnrechnungsstunden}, die die relevanten Informationen enthalten,
	 *                           um den Lehrer zu identifizieren.
	 * @return der {@link UvLehrer}, der den angegebenen Anrechnungsstunden zugeordnet ist.
	 */
	public @NotNull UvLehrer lehrerGetByLehrerAnrechnungsstunden(final @NotNull UvLehrerAnrechnungsstunden anrechnungsstunden) {
		return lehrerGetByIdOrException(anrechnungsstunden.idLehrer);
	}

	/**
	 * Liefert das {@link UvSchiene}-Objekt zu einer {@link UvLerngruppenSchiene}.
	 * @param schiene die {@link UvLerngruppenSchiene}
	 * @return die {@link UvSchiene}
	 */
	public @NotNull UvSchiene schieneGetByLerngruppenSchiene(final @NotNull UvLerngruppenSchiene schiene) {
		return schieneGetByIdOrException(schiene.idSchiene);
	}

	/**
	 * Prüft, ob der {@link UvLehrer} eine Lehrbefähigung für das angegebene {@link UvFach} besitzt.
	 *
	 * TODO: Die Prüfung berücksichtigt bislang nur den Fach-Eintrag. Die Erweiterung um Sekundarstufe
	 * und Gültigkeitszeitraum sowie den dafür benötigten Kontext noch fachlich festlegen.
	 *
	 * @param lehrer der {@link UvLehrer}
	 * @param fach   das {@link UvFach}
	 * @return {@code true}, falls der Lehrer eine Lehrbefähigung für das UV-Fach besitzt, sonst {@code false}
	 */
	public boolean lehrerHatLehrbefaehigungFach(final @NotNull UvLehrer lehrer, final @NotNull UvFach fach) {
		return lehrerUnterrichtsfachByIdLehrerAndIdFach.getSingle12OrNull(lehrer.id, fach.idFach) != null;
	}

	/**
	 * Prüft, ob der {@link UvLehrer} eine Lehrbefähigung für das Fach der angegebenen {@link UvLerngruppe} besitzt.
	 * Bei Klassenunterricht wird das Fach direkt aus der {@link UvLerngruppe} ermittelt,
	 * bei Kursunterricht aus dem zugeordneten {@link UvKurs}.
	 *
	 * TODO: Die Prüfung um Sekundarstufe und Gültigkeitszeitraum anhand der Jahrgänge und des
	 * Planungsabschnitts der Lerngruppe erweitern. Zuvor die zeitliche Gültigkeitsregel und den Umgang
	 * mit gemischten oder unbekannten Jahrgangsstufen sowie fehlenden Berechtigungsangaben festlegen.
	 *
	 * @param lehrer     der {@link UvLehrer}
	 * @param lerngruppe die {@link UvLerngruppe}
	 * @return {@code true}, falls der Lehrer eine Lehrbefähigung für das Fach der Lerngruppe besitzt, sonst {@code false}
	 */
	public boolean lehrerHatLehrbefaehigungLerngruppe(final @NotNull UvLehrer lehrer, final @NotNull UvLerngruppe lerngruppe) {
		final UvFach fach = fachGetByLerngruppe(lerngruppe);
		if (fach == null) {
			return false;
		}
		return lehrerHatLehrbefaehigungFach(lehrer, fach);
	}

	/**
	 * Prüft, ob der {@link UvPlanungsabschnittLehrer} eine Lehrbefähigung für das angegebene {@link UvFach} besitzt.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @param fach                    das {@link UvFach}
	 * @return {@code true}, falls der Lehrer eine Lehrbefähigung für das Fach besitzt, sonst {@code false}
	 */
	public boolean planungsabschnittLehrerHatLehrbefaehigungFach(final @NotNull UvPlanungsabschnittLehrer planungsabschnittLehrer,
			final @NotNull UvFach fach) {
		return lehrerHatLehrbefaehigungFach(lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer), fach);
	}

	/**
	 * Prüft, ob der {@link UvPlanungsabschnittLehrer} eine Lehrbefähigung für das Fach der angegebenen {@link UvLerngruppe} besitzt.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @param lerngruppe              die {@link UvLerngruppe}
	 * @return {@code true}, falls der Lehrer eine Lehrbefähigung für das Fach der Lerngruppe besitzt, sonst {@code false}
	 */
	public boolean planungsabschnittLehrerHatLehrbefaehigungLerngruppe(final @NotNull UvPlanungsabschnittLehrer planungsabschnittLehrer,
			final @NotNull UvLerngruppe lerngruppe) {
		return lehrerHatLehrbefaehigungLerngruppe(lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer), lerngruppe);
	}

	/**
	 * Liefert das {@link UvLehrerPflichtstundensoll}, das am Beginn des {@link UvPlanungsabschnitt}s gültig ist,
	 * oder {@code null}, falls kein passendes Pflichtstundensoll existiert.
	 *
	 * @param lehrer             der {@link UvLehrer}
	 * @param planungsabschnitt  der {@link UvPlanungsabschnitt}
	 * @return das am Beginn des Planungsabschnitts gültige {@link UvLehrerPflichtstundensoll} oder {@code null}
	 */
	public UvLehrerPflichtstundensoll lehrerPflichtstundensollGetByLehrerAndPlanungsabschnitt(final @NotNull UvLehrer lehrer,
			final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		final List<UvLehrerPflichtstundensoll> liste = lehrerPflichtstundensollMengeByLehrerId.get(lehrer.id);
		if (liste == null) {
			return null;
		}
		final @NotNull String stichtag = planungsabschnitt.gueltigVon;
		for (final @NotNull UvLehrerPflichtstundensoll p : liste) {
			if ((p.gueltigVon.compareTo(stichtag) <= 0) && ((p.gueltigBis == null) || (p.gueltigBis.compareTo(stichtag) >= 0))) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Liefert das {@link UvLehrerPflichtstundensoll}, das am Beginn des Planungsabschnitts des
	 * {@link UvPlanungsabschnittLehrer} gültig ist, oder {@code null}, falls kein passendes Pflichtstundensoll existiert.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @return das am Beginn des Planungsabschnitts gültige {@link UvLehrerPflichtstundensoll} oder {@code null}
	 */
	public UvLehrerPflichtstundensoll lehrerPflichtstundensollGetByPlanungsabschnittLehrer(final @NotNull UvPlanungsabschnittLehrer planungsabschnittLehrer) {
		final @NotNull UvPlanungsabschnitt pa =
				DeveloperNotificationException.ifMapGetIsNull(planungsabschnittById, planungsabschnittLehrer.idPlanungsabschnitt);
		return lehrerPflichtstundensollGetByLehrerAndPlanungsabschnitt(lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer), pa);
	}

	/**
	 * Liefert den Pflichtstundensoll-Wert des {@link UvLehrer}s am Beginn des {@link UvPlanungsabschnitt}s,
	 * oder {@code null}, falls kein passendes Pflichtstundensoll existiert.
	 *
	 * @param lehrer             der {@link UvLehrer}
	 * @param planungsabschnitt  der {@link UvPlanungsabschnitt}
	 * @return der Pflichtstundensoll-Wert oder {@code null}
	 */
	public Double lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(final @NotNull UvLehrer lehrer,
			final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		final UvLehrerPflichtstundensoll p = lehrerPflichtstundensollGetByLehrerAndPlanungsabschnitt(lehrer, planungsabschnitt);
		return (p != null) ? p.pflichtstdSoll : null;
	}

	/**
	 * Liefert den Pflichtstundensoll-Wert des {@link UvPlanungsabschnittLehrer}s am Beginn seines Planungsabschnitts,
	 * oder {@code null}, falls kein passendes Pflichtstundensoll existiert.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @return der Pflichtstundensoll-Wert oder {@code null}
	 */
	public Double lehrerPflichtstundensollGetDoubleByPlanungsabschnittLehrer(final @NotNull UvPlanungsabschnittLehrer planungsabschnittLehrer) {
		final UvLehrerPflichtstundensoll p = lehrerPflichtstundensollGetByPlanungsabschnittLehrer(planungsabschnittLehrer);
		return (p != null) ? p.pflichtstdSoll : null;
	}

	/**
	 * Liefert die Liste der {@link UvLehrerAnrechnungsstunden}, die am Beginn des {@link UvPlanungsabschnitt}s gültig sind.
	 *
	 * @param lehrer             der {@link UvLehrer}
	 * @param planungsabschnitt  der {@link UvPlanungsabschnitt}
	 * @return die Liste der am Beginn des Planungsabschnitts gültigen {@link UvLehrerAnrechnungsstunden}
	 */
	public @NotNull List<UvLehrerAnrechnungsstunden> lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(final @NotNull UvLehrer lehrer,
			final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		final List<UvLehrerAnrechnungsstunden> liste = lehrerAnrechnungsstundenMengeByLehrerId.get(lehrer.id);
		if (liste == null) {
			return new ArrayList<>();
		}
		final @NotNull String stichtag = planungsabschnitt.gueltigVon;
		final @NotNull List<UvLehrerAnrechnungsstunden> result = new ArrayList<>();
		for (final @NotNull UvLehrerAnrechnungsstunden a : liste) {
			if ((a.gueltigVon.compareTo(stichtag) <= 0) && ((a.gueltigBis == null) || (a.gueltigBis.compareTo(stichtag) >= 0))) {
				result.add(a);
			}
		}
		return result;
	}

	/**
	 * Liefert die Liste der {@link UvLehrerAnrechnungsstunden}, die am Beginn des Planungsabschnitts des
	 * {@link UvPlanungsabschnittLehrer} gültig sind.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @return die Liste der am Beginn des Planungsabschnitts gültigen {@link UvLehrerAnrechnungsstunden}
	 */
	public @NotNull List<UvLehrerAnrechnungsstunden> lehrerAnrechnungsstundenGetMengeByPlanungsabschnittLehrer(
			final @NotNull UvPlanungsabschnittLehrer planungsabschnittLehrer) {
		final @NotNull UvPlanungsabschnitt pa =
				DeveloperNotificationException.ifMapGetIsNull(planungsabschnittById, planungsabschnittLehrer.idPlanungsabschnitt);
		return lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer), pa);
	}

	/**
	 * Liefert die Summe der Anrechnungsstunden des {@link UvLehrer}s, die am Beginn des {@link UvPlanungsabschnitt}s gültig sind.
	 *
	 * @param lehrer             der {@link UvLehrer}
	 * @param planungsabschnitt  der {@link UvPlanungsabschnitt}
	 * @return die Summe der Anrechnungsstunden
	 */
	public double lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(final @NotNull UvLehrer lehrer,
			final @NotNull UvPlanungsabschnitt planungsabschnitt) {
		double summe = 0.0;
		for (final @NotNull UvLehrerAnrechnungsstunden a : lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(lehrer, planungsabschnitt)) {
			summe += a.anzahlStunden;
		}
		return summe;
	}

	/**
	 * Liefert die Summe der Anrechnungsstunden des {@link UvPlanungsabschnittLehrer}s, die am Beginn seines Planungsabschnitts gültig sind.
	 *
	 * @param planungsabschnittLehrer der {@link UvPlanungsabschnittLehrer}
	 * @return die Summe der Anrechnungsstunden
	 */
	public double lehrerAnrechnungsstundenGetDoubleByPlanungsabschnittLehrer(final @NotNull UvPlanungsabschnittLehrer planungsabschnittLehrer) {
		return lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(
				lehrerGetByIdOrException(planungsabschnittLehrer.idLehrer),
				DeveloperNotificationException.ifMapGetIsNull(planungsabschnittById, planungsabschnittLehrer.idPlanungsabschnitt));
	}

	/**
	 * Liefert alle {@link LehrerUnterrichtsfach}-Einträge zum übergebenen {@link UvFach}.
	 *
	 * @param fach das {@link UvFach}
	 * @return die zugeordneten {@link LehrerUnterrichtsfach}-Einträge
	 */
	public @NotNull List<LehrerUnterrichtsfach> lehrerUnterrichtsfachGetMengeByFach(final @NotNull UvFach fach) {
		return lehrerUnterrichtsfachByIdLehrerAndIdFach.get2(fach.idFach);
	}

	/**
	 * Liefert den {@link UvLehrer} zur übergebenen K-Lehrer-ID.
	 *
	 * @param idKLehrer die K-Lehrer-ID des gesuchten {@link UvLehrer}s
	 * @return den zugehörigen {@link UvLehrer}
	 * @throws DeveloperNotificationException falls kein {@link UvLehrer} mit dieser K-Lehrer-ID vorhanden ist
	 */
	public @NotNull UvLehrer lehrerGetByKLehrerIdOrException(final long idKLehrer) {
		return DeveloperNotificationException.ifMapGetIsNull(lehrerByIdKLehrer, idKLehrer);
	}

	/**
	 * Liefert den {@link UvLehrer} zur übergebenen K-Lehrer-ID oder {@code null}.
	 *
	 * @param idKLehrer die K-Lehrer-ID des gesuchten {@link UvLehrer}s
	 * @return den zugehörigen {@link UvLehrer} oder {@code null}
	 */
	public UvLehrer lehrerGetByKLehrerIdOrNull(final long idKLehrer) {
		return lehrerByIdKLehrer.get(idKLehrer);
	}

	/**
	 * Liefert den {@link UvLehrer} zu einem {@link LehrerUnterrichtsfach}.
	 *
	 * @param unterrichtsfach das {@link LehrerUnterrichtsfach}
	 * @return den zugehörigen {@link UvLehrer}
	 */
	public @NotNull UvLehrer lehrerGetByLehrerUnterrichtsfachOrException(final @NotNull LehrerUnterrichtsfach unterrichtsfach) {
		return unterrichtsfach.istKLehrer ? lehrerGetByKLehrerIdOrException(unterrichtsfach.idLehrer) : lehrerGetByIdOrException(unterrichtsfach.idLehrer);
	}

	/**
	 * Liefert den Wert des größten Wochentages.
	 *
	 * @return den Wert des größten Wochentages.
	 */
	public int zeitrasterGetMaxWochentag() {
		return Wochentag.SONNTAG.id;
	}

	/**
	 * Liefert den Wert des größtmöglichen letzten Stunde.
	 *
	 * @return den Wert des größtmöglichen letzten Stunde.
	 */
	public int zeitrasterGetMaxStunde() {
		return 16; // TODO BAR, ESR: Gibt es mehr als 16 Stunden? Oder können wir irgendwelche Daten scannen?
	}

	/**
	 * Liefert den Wert des größtmöglichen Wochentyps oder 0, falls jede Woche gleich ist.
	 * <br>Hinweis: 0 = Jede Woche gleich, 1 = ungültiger Wert, 2 = A-B-Wochen, 3 = A-B-C-Wochen, ...
	 *
	 * @return den Wert des größtmöglichen Wochentyps oder 0, falls jede Woche gleich ist.
	 */
	public int zeitrasterGetWochentypmodell() {
		return 3; // TODO BAR, ESR: Gibt es mehr als A-B-C-Wochen? Oder können wir irgendwelche Daten scannen?
	}


}
