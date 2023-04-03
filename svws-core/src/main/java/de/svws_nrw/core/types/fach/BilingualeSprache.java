package de.svws_nrw.core.types.fach;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.data.fach.BilingualeSpracheKatalogEintrag;
import de.svws_nrw.core.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Fremdsprachen, welche an Schulen bilingual
 * unterrichtet werden können.
 */
public enum BilingualeSprache {

	/** Bilinguale Sprache Englisch */
	ENGLISCH(new BilingualeSpracheKatalogEintrag[] {
			new BilingualeSpracheKatalogEintrag(1000L, ZulaessigesFach.E, Arrays.asList(
					Schulform.BK, Schulform.SB,
					Schulform.G,
					Schulform.GE,
					Schulform.GY,
					Schulform.GM,
					Schulform.R,
					Schulform.SG,
					Schulform.SK,
					Schulform.SR
					), null, null)
	}),

	/** Bilinguale Sprache Französisch */
	FRANZOESISCH(new BilingualeSpracheKatalogEintrag[] {
			new BilingualeSpracheKatalogEintrag(2000L, ZulaessigesFach.F, Arrays.asList(
					Schulform.BK, Schulform.SB,
					Schulform.G,
					Schulform.GE,
					Schulform.GY,
					Schulform.GM,
					Schulform.R,
					Schulform.SG,
					Schulform.SK,
					Schulform.SR
					), null, null)
	}),

	/** Bilinguale Sprache Italienisch */
	ITALIENISCH(new BilingualeSpracheKatalogEintrag[] {
			new BilingualeSpracheKatalogEintrag(3000L, ZulaessigesFach.I, Arrays.asList(
					Schulform.BK, Schulform.SB,
					Schulform.G,
					Schulform.GE,
					Schulform.GY,
					Schulform.GM,
					Schulform.R,
					Schulform.SG,
					Schulform.SK,
					Schulform.SR
					), null, null)
	}),

	/** Bilinguale Sprache Niederländisch */
	NIEDERLAENDISCH(new BilingualeSpracheKatalogEintrag[] {
			new BilingualeSpracheKatalogEintrag(4000L, ZulaessigesFach.N, Arrays.asList(
					Schulform.BK, Schulform.SB,
					Schulform.G,
					Schulform.GE,
					Schulform.GY,
					Schulform.GM,
					Schulform.R,
					Schulform.SG,
					Schulform.SK,
					Schulform.SR
					), null, null)
	}),

	/** Bilinguale Sprache Spanisch */
	SPANISCH(new BilingualeSpracheKatalogEintrag[] {
			new BilingualeSpracheKatalogEintrag(5000L, ZulaessigesFach.S, Arrays.asList(
					Schulform.BK, Schulform.SB,
					Schulform.G,
					Schulform.GE,
					Schulform.GY,
					Schulform.GM,
					Schulform.R,
					Schulform.SG,
					Schulform.SK,
					Schulform.SR
					), null, null)
	}),

	/** Bilinguale Sprache Türkisch */
	TUERKISCH(new BilingualeSpracheKatalogEintrag[] {
			new BilingualeSpracheKatalogEintrag(6000L, ZulaessigesFach.T, Arrays.asList(
					Schulform.BK, Schulform.SB,
					Schulform.G,
					Schulform.GE,
					Schulform.GY,
					Schulform.GM,
					Schulform.R,
					Schulform.SG,
					Schulform.SK,
					Schulform.SR
					), null, null)
	}),

	/** Bilinguale Sprache Neugriechisch */
	NEUGRIECHIESCH(new BilingualeSpracheKatalogEintrag[] {
			new BilingualeSpracheKatalogEintrag(7000L, ZulaessigesFach.Z, Arrays.asList(
					Schulform.BK, Schulform.SB,
					Schulform.G,
					Schulform.GE,
					Schulform.GY,
					Schulform.GM,
					Schulform.R,
					Schulform.SG,
					Schulform.SK,
					Schulform.SR
					), null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der bilingualen Sprache */
	public final @NotNull BilingualeSpracheKatalogEintrag daten;

	/** Die Historie mit den Einträgen der bilingualen Sprache */
	public final @NotNull BilingualeSpracheKatalogEintrag@NotNull[] historie;

	/** Eine Map, welche der ID der bilingualen Sprache die Instanz dieser Aufzählung zuordnet. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull BilingualeSpracheKatalogEintrag> _mapEintragByID = new HashMap<>();

	/** Eine Map, welche der ID der bilingualen Sprache die Instanz dieser Aufzählung zuordnet. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull BilingualeSprache> _mapByID = new HashMap<>();

	/** Eine Map, welche dem Kürzel der bilingualen Sprache die Instanz dieser Aufzählung zuordnet. */
	private static final @NotNull HashMap<@NotNull String, @NotNull BilingualeSprache> _mapByKuerzel = new HashMap<>();

	/** Die Schulformen, bei welchen die bilingualen Sprache vorkommt */
	private @NotNull Vector<@NotNull Schulform> @NotNull[] schulformen;


	/**
	 * Erzeugt eine bilingualen Sprache in der Aufzählung.
	 *
	 * @param historie   die Historie der bilingualen Sprache, welche ein Array von
	 *                   {@link BilingualeSpracheKatalogEintrag} ist
	 */
	@SuppressWarnings("unchecked")
	BilingualeSprache(final @NotNull BilingualeSpracheKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		// Erzeuge ein zweites Array mit der Schulformzuordnung für die Historie
		this.schulformen = (@NotNull Vector<@NotNull Schulform> @NotNull[]) Array.newInstance(Vector.class, historie.length);
		for (int i = 0; i < historie.length; i++) {
			this.schulformen[i] = new Vector<>();
			for (final @NotNull String kuerzel : historie[i].schulformen) {
				final Schulform sf = Schulform.getByKuerzel(kuerzel);
				if (sf != null)
					this.schulformen[i].add(sf);
			}
		}
	}


	/**
	 * Gibt eine Map von den IDs der bilingualen Sprachen auf die zugehörigen Katalog-Einträge
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der bilingualen Sprachen auf die zugehörigen Katalog-Einträge
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull BilingualeSpracheKatalogEintrag> getMapEintragByID() {
		if (_mapEintragByID.size() == 0)
			for (final BilingualeSprache s : BilingualeSprache.values())
				for (final BilingualeSpracheKatalogEintrag k : s.historie)
					_mapEintragByID.put(k.id, k);
		return _mapEintragByID;
	}


	/**
	 * Gibt eine Map von den IDs der bilingualen Sprachen auf die zugehörigen bilingualen Sprachen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der bilingualen Sprachen auf die zugehörigen bilingualen Sprachen
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull BilingualeSprache> getMapByID() {
		if (_mapByID.size() == 0)
			for (final BilingualeSprache s : BilingualeSprache.values())
				_mapByID.put(s.daten.id, s);
		return _mapByID;
	}


	/**
	 * Gibt eine Map von den Kürzeln der bilingualen Sprachen auf die zugehörigen bilingualen Sprachen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der bilingualen Sprachen auf die zugehörigen bilingualen Sprachen
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull BilingualeSprache> getMapByKuerzel() {
		if (_mapByKuerzel.size() == 0)
			for (final BilingualeSprache s : BilingualeSprache.values())
				_mapByKuerzel.put(s.daten.kuerzel, s);
		return _mapByKuerzel;
	}


	/**
	 * Liefert alle Schulformen zurück, bei welchen die bilingualen Sprache vorkommt.
	 *
	 * @return eine Liste der Schulformen
	 */
	public @NotNull List<@NotNull Schulform> getSchulformen() {
		return schulformen[historie.length - 1];
	}


	/**
	 * Prüft, ob die Schulform bei dieser bilingualen Sprache zulässig ist.
	 *
	 * @param schulform    die Schulform
	 *
	 * @return true, falls die bilingualen Sprache in der Schulform zulässig ist, ansonsten false.
	 */
	private boolean hasSchulform(final Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return false;
		if (daten.schulformen != null) {
			for (int i = 0; i < daten.schulformen.size(); i++) {
				final String sfKuerzel = daten.schulformen.get(i);
				if (sfKuerzel.equals(schulform.daten.kuerzel))
					return true;
			}
		}
		return false;
	}


	/**
	 * Liefert den Katalog-Eintrag der bilingualen Sprache zu der übergebenen ID zurück.
	 *
	 * @param id   die ID des Katalog-Eintrags
	 *
	 * @return der Katalog-Eintrag der bilingualen Sprache oder null, falls die ID ungültig ist
	 */
	public static BilingualeSpracheKatalogEintrag getKatalogEintragByID(final long id) {
		return getMapEintragByID().get(id);
	}


	/**
	 * Liefert die bilingualen Sprache zu der übergebenen ID der bilingualen Sprache zurück.
	 *
	 * @param id   die ID der bilingualen Sprache
	 *
	 * @return die bilingualen Sprache oder null, falls die ID ungültig ist
	 */
	public static BilingualeSprache getByID(final long id) {
		return getMapByID().get(id);
	}


	/**
	 * Liefert die bilingualen Sprache zu der übergebenen ID der bilingualen Sprache zurück.
	 *
	 * @param kuerzel   das Kürzel der bilingualen Sprache
	 *
	 * @return die bilingualen Sprache oder null, falls das Kürzel ungültig ist
	 */
	public static BilingualeSprache getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}


	/**
	 * Bestimmt alle bilingualen Sprachen, die in der angegebenen Schulform zulässig sind.
	 *
	 * @param schulform    die Schulform
	 *
	 * @return die bilingualen Sprache in der angegebenen Schulform
	 */
	public static @NotNull List<@NotNull BilingualeSprache> get(final Schulform schulform) {
		final @NotNull Vector<@NotNull BilingualeSprache> faecher = new Vector<>();
		if (schulform == null)
			return faecher;
		final @NotNull BilingualeSprache@NotNull[] fachgruppen = BilingualeSprache.values();
		for (int i = 0; i < fachgruppen.length; i++) {
			final BilingualeSprache fg = fachgruppen[i];
			if (fg.hasSchulform(schulform))
				faecher.add(fg);
		}
		return faecher;
	}

}
