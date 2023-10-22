package de.svws_nrw.core.utils.schueler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.GostJahrgang;
import de.svws_nrw.core.data.jahrgang.JahrgangsListeEintrag;
import de.svws_nrw.core.data.klassen.KlassenListeEintrag;
import de.svws_nrw.core.data.kurse.KursListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.utils.gost.GostAbiturjahrUtils;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.core.utils.klassen.KlassenUtils;
import de.svws_nrw.core.utils.kurse.KursUtils;
import de.svws_nrw.core.utils.schule.SchuljahresabschnittsUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Manager zum Verwalten der Schüler-Listen.
 */
public class SchuelerListeManager {

	/** Die Liste, welche als Grundlage in den Manager geladen wurde */
	private final @NotNull List<@NotNull SchuelerListeEintrag> _schueler = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerByID = new HashMap<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerMitStatus = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInJahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInKlasse = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInKurs = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Long, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInSchuljahresabschnitt = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull Integer, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInAbiturjahrgang = new HashMap2D<>();
	private final @NotNull HashMap2D<@NotNull String, @NotNull Long, @NotNull SchuelerListeEintrag> _mapSchuelerInSchulgliederung = new HashMap2D<>();

	private final @NotNull List<@NotNull JahrgangsListeEintrag> _jahrgaenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull JahrgangsListeEintrag> _mapJahrgangByID = new HashMap<>();

	private final @NotNull List<@NotNull KlassenListeEintrag> _klassen = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull KlassenListeEintrag> _mapKlasseByID = new HashMap<>();

	private final @NotNull List<@NotNull KursListeEintrag> _kurse = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull KursListeEintrag> _mapKursByID = new HashMap<>();

	private final @NotNull List<@NotNull Schuljahresabschnitt> _schuljahresabschnitte = new ArrayList<>();
	private final @NotNull Map<@NotNull Long, @NotNull Schuljahresabschnitt> _mapSchuljahresabschnittByID = new HashMap<>();

	private final @NotNull List<@NotNull GostJahrgang> _abiturjahrgaenge = new ArrayList<>();
	private final @NotNull Map<@NotNull Integer, @NotNull GostJahrgang> _mapAbiturjahrgangByID = new HashMap<>();


	/**
	 * Erstellt einen neuen Manager und initialisiert diesen mit den übergebenen Daten
	 *
	 * @param schueler                die Liste der Schüler
	 * @param jahrgaenge              die Liste des Jahrgangskatalogs
	 * @param klassen                 die Liste des Klassenkatalogs
	 * @param kurse                   die Liste des Kurskatalogs
	 * @param schuljahresabschnitte   die Liste der Schuljahresabschnitte
	 * @param abiturjahrgaenge        die Liste der Abiturjahrgänge
	 */
	public SchuelerListeManager(final @NotNull List<@NotNull SchuelerListeEintrag> schueler,
			final @NotNull List<@NotNull JahrgangsListeEintrag> jahrgaenge,
			final @NotNull List<@NotNull KlassenListeEintrag> klassen,
			final @NotNull List<@NotNull KursListeEintrag> kurse,
			final @NotNull List<@NotNull Schuljahresabschnitt> schuljahresabschnitte,
			final @NotNull List<@NotNull GostJahrgang> abiturjahrgaenge) {
		initSchueler(schueler);
		initJahrgaenge(jahrgaenge);
		initKlassen(klassen);
		initKurse(kurse);
		initSchuljahresabschnitte(schuljahresabschnitte);
		initAbiturjahrgaenge(abiturjahrgaenge);
	}


	private void initSchueler(final @NotNull List<@NotNull SchuelerListeEintrag> schueler) {
		this._schueler.clear();
		this._schueler.addAll(schueler);
		this._schueler.sort(SchuelerUtils.comparator);
		this._mapSchuelerByID.clear();
		for (final @NotNull SchuelerListeEintrag s : schueler) {
			this._mapSchuelerByID.put(s.id, s);
			this._mapSchuelerMitStatus.put(s.status, s.id, s);
			if (s.idJahrgang >= 0)
				this._mapSchuelerInJahrgang.put(s.idJahrgang, s.id, s);
			if (s.idKlasse >= 0)
				this._mapSchuelerInKlasse.put(s.idKlasse, s.id, s);
			for (final long idKurs : s.kurse)
				this._mapSchuelerInKurs.put(idKurs, s.id, s);
			if (s.idSchuljahresabschnitt >= 0)
				this._mapSchuelerInSchuljahresabschnitt.put(s.idSchuljahresabschnitt, s.id, s);
			if (s.abiturjahrgang != null)
				this._mapSchuelerInAbiturjahrgang.put(s.abiturjahrgang, s.id, s);
			if (!s.schulgliederung.isBlank())
				this._mapSchuelerInSchulgliederung.put(s.schulgliederung, s.id, s);
		}
	}

	private void initJahrgaenge(final @NotNull List<@NotNull JahrgangsListeEintrag> jahrgaenge) {
		this._jahrgaenge.clear();
		this._jahrgaenge.addAll(jahrgaenge);
		this._jahrgaenge.sort(JahrgangsUtils.comparator);
		this._mapJahrgangByID.clear();
		for (final @NotNull JahrgangsListeEintrag j : jahrgaenge)
			this._mapJahrgangByID.put(j.id, j);
	}

	private void initKlassen(final @NotNull List<@NotNull KlassenListeEintrag> klassen) {
		this._klassen.clear();
		this._klassen.addAll(klassen);
		this._klassen.sort(KlassenUtils.comparator);
		this._mapKlasseByID.clear();
		for (final @NotNull KlassenListeEintrag k : klassen)
			this._mapKlasseByID.put(k.id, k);
	}

	private void initKurse(final @NotNull List<@NotNull KursListeEintrag> kurse) {
		this._kurse.clear();
		this._kurse.addAll(kurse);
		this._kurse.sort(KursUtils.comparator);
		this._mapKursByID.clear();
		for (final @NotNull KursListeEintrag k : kurse)
			this._mapKursByID.put(k.id, k);
	}

	private void initSchuljahresabschnitte(final @NotNull List<@NotNull Schuljahresabschnitt> schuljahresabschnitte) {
		this._schuljahresabschnitte.clear();
		this._schuljahresabschnitte.addAll(schuljahresabschnitte);
		this._schuljahresabschnitte.sort(SchuljahresabschnittsUtils.comparator);
		this._mapSchuljahresabschnittByID.clear();
		for (final @NotNull Schuljahresabschnitt sja : schuljahresabschnitte)
			this._mapSchuljahresabschnittByID.put(sja.id, sja);
	}

	private void initAbiturjahrgaenge(final @NotNull List<@NotNull GostJahrgang> abiturjahrgaenge) {
		this._abiturjahrgaenge.clear();
		this._abiturjahrgaenge.addAll(abiturjahrgaenge);
		this._abiturjahrgaenge.sort(GostAbiturjahrUtils.comparator);
		this._mapAbiturjahrgangByID.clear();
		for (final @NotNull GostJahrgang j : abiturjahrgaenge)
			this._mapAbiturjahrgangByID.put(j.abiturjahr, j);
	}

}
