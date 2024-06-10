package de.svws_nrw.core.utils.schueler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.kaoa.KAOAAnschlussoption;
import de.svws_nrw.core.types.kaoa.KAOABerufsfeld;
import de.svws_nrw.core.types.kaoa.KAOAEbene4;
import de.svws_nrw.core.types.kaoa.KAOAKategorie;
import de.svws_nrw.core.types.kaoa.KAOAMerkmal;
import de.svws_nrw.core.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.utils.AttributMitAuswahl;
import de.svws_nrw.core.utils.AuswahlManager;
import de.svws_nrw.core.utils.MapUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Alternative der KAoA-Daten.
 */
public class SchuelerKAoAManager
		extends AuswahlManager<@NotNull Long, @NotNull SchuelerKAoADaten, @NotNull SchuelerKAoADaten> {

	/**
	 * Funktionen zum Mappen von Auswahl- bzw. Daten-Objekten auf deren ID-Typ
	 */
	private static final @NotNull Function<@NotNull SchuelerKAoADaten, @NotNull Long> _kaoaToId =
			(final @NotNull SchuelerKAoADaten kaoa) -> kaoa.id;
	private static final @NotNull Function<@NotNull KAOAKategorie, @NotNull Long> _kategorieToId =
			(final @NotNull KAOAKategorie kategorie) -> kategorie.daten.id;
	private static final @NotNull Comparator<@NotNull KAOAKategorie> _comparatorKategorie =
			(final @NotNull KAOAKategorie a, final @NotNull KAOAKategorie b) -> a.ordinal() - b.ordinal();
	private static final @NotNull Function<@NotNull KAOAMerkmal, @NotNull Long> _merkmalToId =
			(final @NotNull KAOAMerkmal merkmal) -> merkmal.daten.id;
	private static final @NotNull Comparator<@NotNull KAOAMerkmal> _comparatorMerkmal =
			(final @NotNull KAOAMerkmal a, final @NotNull KAOAMerkmal b) -> a.ordinal() - b.ordinal();
	private static final @NotNull Function<@NotNull KAOAZusatzmerkmal, @NotNull Long> _zusatzmerkmalToId =
			(final @NotNull KAOAZusatzmerkmal zusatzmerkmal) -> zusatzmerkmal.daten.id;
	private static final @NotNull Comparator<@NotNull KAOAZusatzmerkmal> _comparatorZusatzmerkmal =
			(final @NotNull KAOAZusatzmerkmal a, final @NotNull KAOAZusatzmerkmal b) -> a.ordinal() - b.ordinal();
	private static final @NotNull Function<@NotNull KAOAAnschlussoption, @NotNull Long> _anschlussoptionToId =
			(final @NotNull KAOAAnschlussoption anschlussoption) -> anschlussoption.daten.id;
	private static final @NotNull Comparator<@NotNull KAOAAnschlussoption> _comparatorAnschlussoptionen =
			(final @NotNull KAOAAnschlussoption a, final @NotNull KAOAAnschlussoption b) -> a.ordinal() - b.ordinal();
	private static final @NotNull Function<@NotNull KAOAEbene4, @NotNull Long> _ebene4ToId =
			(final @NotNull KAOAEbene4 ebene4) -> ebene4.daten.id;
	private static final @NotNull Comparator<@NotNull KAOAEbene4> _comparatorEbene4 =
			(final @NotNull KAOAEbene4 a, final @NotNull KAOAEbene4 b) -> a.ordinal() - b.ordinal();
	private static final @NotNull Comparator<@NotNull KAOABerufsfeld> _comparatorBerufsfelder =
			(final @NotNull KAOABerufsfeld a, final @NotNull KAOABerufsfeld b) -> a.ordinal() - b.ordinal();

	/** Das Filter-Attribut für die Kategorien */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull KAOAKategorie> _kategorien;

	/** Das Filter-Attribut für die Merkmale */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull KAOAMerkmal> _merkmale;

	/** Das Filter-Attribut für die Zusatzmerkmale */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull KAOAZusatzmerkmal> _zusatzmerkmale;

	/** Das Filter-Attribut für die Anschlussoptionen */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull KAOAAnschlussoption> _anschlussoptionen;

	/** Das Filter-Attribut für die Ebene4 */
	public final @NotNull AttributMitAuswahl<@NotNull Long, @NotNull KAOAEbene4> _ebene4;
	private final @NotNull List<@NotNull SchuelerLernabschnittsdaten> _lernabschnittsdaten;

	/** Das Filter-Attribut für die Berufsfelder */
	private final @NotNull List<@NotNull KAOABerufsfeld> _berufsfelder;

	/**
	 * Zusätzliche Maps, welche zum schnellen Zugriff auf Teilmengen der Liste
	 * verwendet werden können
	 */
	private final @NotNull Map<@NotNull Long, @NotNull List<@NotNull SchuelerKAoADaten>> _mapKAoABySchueler = new HashMap<>();
	private final @NotNull Map<@NotNull String, @NotNull List<@NotNull KAOAMerkmal>> _mapMerkmalByKategorie = new HashMap<>();
	private final @NotNull Map<@NotNull String, @NotNull List<@NotNull KAOAZusatzmerkmal>> _mapZusatzmerkmalByMerkmal = new HashMap<>();
	private final @NotNull Map<@NotNull String, @NotNull List<@NotNull KAOAAnschlussoption>> _mapAnschlussoptionByZusatzmerkmal = new HashMap<>();
	private final @NotNull Map<@NotNull String, @NotNull List<@NotNull KAOAEbene4>> _mapEbene4ByZusatzmerkmal = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager mit den übergebenen KAoA Daten
	 *
	 * @param schuljahresabschnitt       Der Schuljahresabschnitt, auf den sich dien KAoA-Daten bezieht
	 * @param schuljahresabschnittSchule Die Liste der Schuljahresabschnitte
	 * @param schuljahresabschnitte      Der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param schulform                  Die Schulform der Schule
	 * @param schuelerKAoA               KAoA Daten des Schülers
	 * @param schuelerLernabschnitt      the schueler lernabschnitt
	 */
	public SchuelerKAoAManager(final long schuljahresabschnitt, final long schuljahresabschnittSchule,
			final @NotNull List<@NotNull Schuljahresabschnitt> schuljahresabschnitte, final Schulform schulform,
			final @NotNull List<@NotNull SchuelerKAoADaten> schuelerKAoA,
			final @NotNull List<@NotNull SchuelerLernabschnittsdaten> schuelerLernabschnitt) {
		super(schuljahresabschnitt, schuljahresabschnittSchule, schuljahresabschnitte, schulform, schuelerKAoA,
				SchuelerKAoAUtils.comparator, _kaoaToId, _kaoaToId,
				Arrays.asList(new Pair<>("schuljahr", true), new Pair<>("kategorie", true)));
		this._lernabschnittsdaten = schuelerLernabschnitt;
		this._kategorien = new AttributMitAuswahl<>(Arrays.asList(KAOAKategorie.values()), _kategorieToId, _comparatorKategorie,
				_eventHandlerFilterChanged);
		this._merkmale = new AttributMitAuswahl<>(Arrays.asList(KAOAMerkmal.values()), _merkmalToId, _comparatorMerkmal,
				_eventHandlerFilterChanged);
		this._zusatzmerkmale = new AttributMitAuswahl<>(Arrays.asList(KAOAZusatzmerkmal.values()), _zusatzmerkmalToId, _comparatorZusatzmerkmal,
				_eventHandlerFilterChanged);
		this._anschlussoptionen = new AttributMitAuswahl<>(Arrays.asList(KAOAAnschlussoption.values()), _anschlussoptionToId,
				_comparatorAnschlussoptionen, _eventHandlerFilterChanged);
		this._berufsfelder = Arrays.asList(KAOABerufsfeld.values());
		this._berufsfelder.sort(_comparatorBerufsfelder);
		this._ebene4 = new AttributMitAuswahl<>(Arrays.asList(KAOAEbene4.values()), _ebene4ToId, _comparatorEbene4, _eventHandlerFilterChanged);
		initKAoA();
		initSchuelerKAoA();
	}

	private void initKAoA() {
		for (final @NotNull KAOAKategorie kategorie : this._kategorien.list()) {
			final @NotNull ArrayList<@NotNull KAOAMerkmal> merkmaleOfKategorie = new ArrayList<>();
			for (final @NotNull KAOAMerkmal merkmal : this._merkmale.list()) {
				if (merkmal.daten.kategorie.equals(kategorie.daten.kuerzel))
					merkmaleOfKategorie.add(merkmal);

				final @NotNull ArrayList<@NotNull KAOAZusatzmerkmal> zusatzmerkmaleOfMerkmal = new ArrayList<>();
				for (final @NotNull KAOAZusatzmerkmal zusatzmerkmal : this._zusatzmerkmale.list()) {
					if (zusatzmerkmal.daten.merkmal.equals(merkmal.daten.kuerzel))
						zusatzmerkmaleOfMerkmal.add(zusatzmerkmal);

					processZusatzmerkmal(zusatzmerkmal);
				}
				this._mapZusatzmerkmalByMerkmal.put(merkmal.daten.kuerzel, zusatzmerkmaleOfMerkmal);
			}
			this._mapMerkmalByKategorie.put(kategorie.daten.kuerzel, merkmaleOfKategorie);
		}
	}

	private void processZusatzmerkmal(final @NotNull KAOAZusatzmerkmal zusatzmerkmal) {
		final @NotNull ArrayList<@NotNull KAOAAnschlussoption> anschlussoptionOfZusatzmerkmal = new ArrayList<>();
		for (final @NotNull KAOAAnschlussoption anschlussoption : this._anschlussoptionen.list())
			for (final @NotNull String anzeigeMerkmal : anschlussoption.daten.anzeigeZusatzmerkmal)
				if (anzeigeMerkmal.equals(zusatzmerkmal.daten.kuerzel))
					anschlussoptionOfZusatzmerkmal.add(anschlussoption);
		this._mapAnschlussoptionByZusatzmerkmal.put(zusatzmerkmal.daten.kuerzel, anschlussoptionOfZusatzmerkmal);

		final @NotNull ArrayList<@NotNull KAOAEbene4> ebene4OfZusatzmerkmal = new ArrayList<>();
		for (final @NotNull KAOAEbene4 ebene4 : this._ebene4.list())
			if (ebene4.daten.zusatzmerkmal.equals(zusatzmerkmal.daten.kuerzel))
				ebene4OfZusatzmerkmal.add(ebene4);
		this._mapEbene4ByZusatzmerkmal.put(zusatzmerkmal.daten.kuerzel, ebene4OfZusatzmerkmal);
	}

	private void initSchuelerKAoA() {
		for (final @NotNull SchuelerLernabschnittsdaten lernabschnitt : this._lernabschnittsdaten) {
			final @NotNull ArrayList<@NotNull SchuelerKAoADaten> schuelerKAoA = new ArrayList<>();
			for (final @NotNull SchuelerKAoADaten kaoa : this.liste.list())
				if (lernabschnitt.id == kaoa.abschnitt)
					schuelerKAoA.add(kaoa);
			this._mapKAoABySchueler.put(lernabschnitt.schuelerID, schuelerKAoA);
		}
	}

	/**
	 * Prüft, ob der angegebene KAoA-Eintrag durch den Filter durchgelassen wird oder nicht.
	 *
	 * @param eintrag Der zu prüfende KAoA-Eintrag
	 *
	 * @return true, wenn der Eintrag den Filter passiert, und ansonsten false
	 */
	@Override
	protected boolean checkFilter(final @NotNull SchuelerKAoADaten eintrag) {
		if (this._kategorien.auswahlExists() && !this._kategorien.auswahlHasKey(eintrag.kategorie))
			return false;
		if (this._merkmale.auswahlExists() && !this._merkmale.auswahlHasKey(eintrag.merkmal))
			return false;
		if ((eintrag.zusatzmerkmal != null) && this._zusatzmerkmale.auswahlExists()
				&& !this._zusatzmerkmale.auswahlHasKey(eintrag.zusatzmerkmal))
			return false;
		if ((eintrag.anschlussoption != null) && this._anschlussoptionen.auswahlExists()
				&& !this._anschlussoptionen.auswahlHasKey(eintrag.anschlussoption))
			return false;
		return ((eintrag.ebene4 != null) && this._ebene4.auswahlExists() && !this._ebene4.auswahlHasKey(eintrag.ebene4));
	}

	/**
	 * Vergleicht zwei KAoA-Einträge anhand der spezifizierten Ordnung.
	 *
	 * @param a Der erste Eintrag
	 * @param b Der zweite Eintrag
	 *
	 * @return Das Ergebnis des Vergleichs (-1 kleiner, 0 gleich und 1 größer)
	 */
	@Override
	protected int compareAuswahl(final @NotNull SchuelerKAoADaten a, final @NotNull SchuelerKAoADaten b) {
		for (final Pair<@NotNull String, @NotNull Boolean> criteria : _order) {
			final String field = criteria.a;
			final boolean asc = (criteria.b == null) || criteria.b;
			int cmp;
			if ("schuljahr".equals(field) || "kategorie".equals(field))
				cmp = SchuelerKAoAUtils.comparator.compare(a, b);
			else
				throw new DeveloperNotificationException(
						"Fehler bei der Sortierung. Das Sortierkriterium wird vom SchuelerKAoAManager nicht "
								+ "unterstützt" + ".");
			if (cmp == 0)
				continue;
			return asc ? cmp : -cmp;
		}
		return Long.compare(a.id, b.id);
	}

	/**
	 * Passt bei Änderungen an den Daten ggf. das Auswahl-Objekt an.
	 *
	 * @param eintrag           Der Auswahl-Eintrag
	 * @param schuelerKAoADaten Das neue KAoA-Daten-Objekt zu der Auswahl
	 */
	@Override
	protected boolean onSetDaten(final @NotNull SchuelerKAoADaten eintrag,
			final @NotNull SchuelerKAoADaten schuelerKAoADaten) {
		boolean updateEintrag = false;
		// Passe ggf. die Daten an ... (beim Patchen der Daten)
		if (schuelerKAoADaten.id != eintrag.id) {
			eintrag.id = schuelerKAoADaten.id;
			updateEintrag = true;
		}
		return updateEintrag;
	}

	/**
	 * Gibt die KAoA Daten des Schülers zurück. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param schueler Der Schüler
	 *
	 * @return Die KAoA Daten des Schülers
	 */
	public @NotNull List<@NotNull SchuelerKAoADaten> getKAoABySchuelerID(final @NotNull SchuelerListeEintrag schueler) {
		return MapUtils.getOrCreateArrayList(_mapKAoABySchueler, schueler.id);
	}

	/**
	 * Gibt alle KAoA Kategorien zurück.
	 *
	 * @return Die KAoA Kategorien
	 */
	public @NotNull List<@NotNull KAOAKategorie> getKAOAKategorien() {
		return this._kategorien.list();
	}

	/**
	 * Gibt alle KAoA Merkmale zurück, die der angegebenen Kategorie zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param kategorie Die Kategorie
	 *
	 * @return Die KAoA Merkmale
	 */
	public @NotNull List<@NotNull KAOAMerkmal> getKAOAMerkmaleByKategorie(final @NotNull KAOAKategorie kategorie) {
		return MapUtils.getOrCreateArrayList(_mapMerkmalByKategorie, kategorie.daten.kuerzel);
	}

	/**
	 * Gibt alle Zusatzmerkmale zurück, die dem angegeben Merkmal zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param merkmal Das Merkmal
	 *
	 * @return Die Zusatzmerkmale
	 */
	public @NotNull List<@NotNull KAOAZusatzmerkmal> getKAOAZusatzmerkmaleByMerkmal(final @NotNull KAOAMerkmal merkmal) {
		return MapUtils.getOrCreateArrayList(_mapZusatzmerkmalByMerkmal, merkmal.daten.kuerzel);
	}

	/**
	 * Gibt alle KAoA Anschlussoptionen zurück, die dem angegebenen Zusatzmerkmal zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste
	 * zurückgegeben.
	 *
	 * @param zusatzmerkmal Das Zusatzmerkmal
	 *
	 * @return Die Anschlussoptionen
	 */
	public @NotNull List<@NotNull KAOAAnschlussoption> getKAOAAnschlussoptionByZusatzmerkmal(final @NotNull KAOAZusatzmerkmal zusatzmerkmal) {
		return MapUtils.getOrCreateArrayList(_mapAnschlussoptionByZusatzmerkmal, zusatzmerkmal.daten.kuerzel);
	}

	/**
	 * Gibt alle Ebene4 Optionen zurück, die dem angegebenen Zusatzmerkmal zugeordnet sind. Falls keine gefunden wurden, wird eine leere Liste zurückgegeben.
	 *
	 * @param zusatzmerkmal Das Zusatzmerkmal
	 *
	 * @return Die Ebene4 Optionen
	 */
	public @NotNull List<@NotNull KAOAEbene4> getKAOAEbene4ByZusatzmerkmal(final @NotNull KAOAZusatzmerkmal zusatzmerkmal) {
		return MapUtils.getOrCreateArrayList(_mapEbene4ByZusatzmerkmal, zusatzmerkmal.daten.kuerzel);
	}

	/**
	 * Gibt alle Berufsfelder zurück.
	 *
	 * @return Die Berufsfelder
	 */
	public @NotNull List<@NotNull KAOABerufsfeld> getKAOABerufsfelder() {
		return this._berufsfelder;
	}

}
