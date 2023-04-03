package de.svws_nrw.core.types.schule;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.core.data.schule.ReformpaedagogikKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung ist der Core-Type für die Reformpaedagogik an
 * unterschiedlichen Schulformen.
 */
public enum Reformpaedagogik {

	/** Reformpaedagogik KEIN_EINTRAG - Es ist kein Eintrag zur Reformpädagogik vorhanden */
    KEIN_EINTRAG(new ReformpaedagogikKatalogEintrag[]{
		new ReformpaedagogikKatalogEintrag(0, "*", "ohne Eintrag", Arrays.asList(
				Schulform.FW, Schulform.HI, Schulform.WF,
				Schulform.G,
				Schulform.GE,
				Schulform.GY,
				Schulform.H,
				Schulform.PS,
				Schulform.R,
				Schulform.SG,
				Schulform.SK,
				Schulform.SR,
				Schulform.V
			), null, null)
	}),

    /** Reformpaedagogik Celestin Freinet */
    FREINET(new ReformpaedagogikKatalogEintrag[]{
        new ReformpaedagogikKatalogEintrag(1000, "C", "Celestin Freinet", Arrays.asList(
                Schulform.G,
                Schulform.GY
            ), null, null)
    }),

    /** Reformpaedagogik Janusz Korczak (Pädagogik der Achtung) */
    KORCZAK(new ReformpaedagogikKatalogEintrag[]{
        new ReformpaedagogikKatalogEintrag(2000, "J", "Janusz Korczak (Pädagogik der Achtung)", Arrays.asList(
                Schulform.G,
                Schulform.GE,
                Schulform.GY,
                Schulform.H,
                Schulform.PS,
                Schulform.R,
                Schulform.S, Schulform.KS,
                Schulform.SG,
                Schulform.SK,
                Schulform.SR,
                Schulform.V
            ), null, null)
    }),

    /** Reformpaedagogik Montessori */
    MONTESSORI(new ReformpaedagogikKatalogEintrag[]{
        new ReformpaedagogikKatalogEintrag(3000, "M", "Montessori", Arrays.asList(
                Schulform.G,
                Schulform.GE,
                Schulform.GY,
                Schulform.H,
                Schulform.PS,
                Schulform.R,
                Schulform.S, Schulform.KS,
                Schulform.SG,
                Schulform.SK,
                Schulform.SR,
                Schulform.V
            ), null, null)
    }),

    /** Reformpaedagogik Peter Petersen/Jena-Plan */
    PETERSEN(new ReformpaedagogikKatalogEintrag[]{
        new ReformpaedagogikKatalogEintrag(4000, "P", "Peter Petersen/Jena-Plan", Arrays.asList(
                Schulform.G,
                Schulform.GY,
                Schulform.S, Schulform.KS,
                Schulform.SG,
                Schulform.SR
            ), null, null)
    }),

	/** Sonstige Reformpaedagogik */
	SONSTIGE(new ReformpaedagogikKatalogEintrag[]{
		new ReformpaedagogikKatalogEintrag(5000, "S", "sonstige", Arrays.asList(
                Schulform.FW, Schulform.HI, Schulform.WF,
                Schulform.G,
                Schulform.GE,
                Schulform.GY,
                Schulform.H,
                Schulform.PS,
                Schulform.R,
                Schulform.SG,
                Schulform.SK,
                Schulform.SR,
                Schulform.V
			), null, null)
	});



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Reformpädagogik, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull ReformpaedagogikKatalogEintrag daten;

	/** Die Historie mit den Einträgen zu der Reformpädagogik */
	public final @NotNull ReformpaedagogikKatalogEintrag@NotNull[] historie;

	/** Eine Map mit der Zuordnung der Reformpädagogik zu dem Kürzel der Reformpädagogik */
	private static final @NotNull HashMap<@NotNull String, @NotNull Reformpaedagogik> _schulgliederungenKuerzel = new HashMap<>();

	/** Eine Map mit der Zuordnung der Reformpädagogik zu der ID der Reformpädagogik */
	private static final @NotNull HashMap<@NotNull Long, @NotNull Reformpaedagogik> _schulgliederungenID = new HashMap<>();

	/** Die Schulformen, bei welchen die Reformpädagogik vorkommt */
	private @NotNull Vector<@NotNull Schulform>@NotNull[] schulformen;


	/**
	 * Erzeugt eine Reformpädagogik in der Aufzählung.
	 *
	 * @param historie   die Historie der Reformpädagogik, welches ein Array von {@link ReformpaedagogikKatalogEintrag} ist
	 */
	@SuppressWarnings("unchecked")
	Reformpaedagogik(final @NotNull ReformpaedagogikKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
		// Erzeuge ein zweites Array mit der Schulformzuordnung für dei Historie
		this.schulformen = (@NotNull Vector<@NotNull Schulform>@NotNull[])Array.newInstance(Vector.class, historie.length);
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
	 * Gibt eine Map von den Kürzeln der Reformpädagogik auf die zugehörige Reformpädagogik
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den Kürzeln der Reformpädagogik auf die zugehörige Reformpädagogik
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Reformpaedagogik> getMapSchulgliederungByKuerzel() {
		if (_schulgliederungenKuerzel.size() == 0)
			for (final Reformpaedagogik r : Reformpaedagogik.values())
				_schulgliederungenKuerzel.put(r.daten.kuerzel, r);
		return _schulgliederungenKuerzel;
	}


	/**
	 * Gibt eine Map von den IDs der Reformpädagogik auf die zugehörige Reformpädagogik
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den IDs der Reformpädagogik auf die zugehörige Reformpädagogik
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull Reformpaedagogik> getMapSchulgliederungByID() {
		if (_schulgliederungenID.size() == 0)
			for (final Reformpaedagogik r : Reformpaedagogik.values()) {
				for (final ReformpaedagogikKatalogEintrag k : r.historie)
					_schulgliederungenID.put(k.id, r);
			}
		return _schulgliederungenID;
	}


	/**
	 * Liefert die Reformpädagogik anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Reformpädagogik
	 *
	 * @return die Reformpädagogik oder null, falls das Kürzel ungültig ist
	 */
	public static Reformpaedagogik getByKuerzel(final String kuerzel) {
		if ((kuerzel == null) || "".equals(kuerzel))
			return Reformpaedagogik.KEIN_EINTRAG;
		return getMapSchulgliederungByKuerzel().get(kuerzel);
	}


	/**
	 * Liefert die Reformpädagogik anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID der Reformpädagogik
	 *
	 * @return die Reformpädagogik oder null, falls die ID ungültig ist
	 */
	public static Reformpaedagogik getByID(final Long id) {
		return getMapSchulgliederungByID().get(id);
	}


	/**
	 * Liefert alle Schulformen zurück, bei welchen die Reformpädagogik vorkommen kann.
	 *
	 * @return eine Liste der Schulformen
	 */
	@JsonIgnore
	public @NotNull List<@NotNull Schulform> getSchulformen() {
		return schulformen[historie.length - 1];
	}


	/**
	 * Liefert alle möglichen Reformpädagogik-Einträge für die angegeben Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform zulässigen Reformpädagogik-Einträge
	 */
	public static @NotNull List<@NotNull Reformpaedagogik> get(final Schulform schulform) {
		final @NotNull Vector<@NotNull Reformpaedagogik> result = new Vector<>();
		if (schulform == null)
			return result;
		final @NotNull Reformpaedagogik@NotNull[] gliederungen = Reformpaedagogik.values();
		for (int i = 0; i < gliederungen.length; i++) {
			final @NotNull Reformpaedagogik gliederung = gliederungen[i];
			if (gliederung.hasSchulform(schulform))
				result.add(gliederung);
		}
		return result;
	}


	/**
	 * Prüft anhand des Schulform-Kürzels, ob bei der Schulform diese Reformpädagogik
	 * vorkommen kann oder nicht.
	 *
	 * @param kuerzel   das Kürzel der Schulform
	 *
	 * @return true, falls die Reformpädagogik bei der Schulform vorkommen kann und ansonsten false
	 */
	@JsonIgnore
	public boolean hasSchulformByKuerzel(final String kuerzel) {
		if ((kuerzel == null) || "".equals(kuerzel))
			return false;
		if (daten.schulformen != null) {
			for (int i = 0; i < daten.schulformen.size(); i++) {
				final String sfKuerzel = daten.schulformen.get(i);
				if (sfKuerzel.equals(kuerzel))
					return true;
			}
		}
		return false;
	}


	/**
	 * Prüft, ob bei der Schulform diese Reformpädagogik vorkommen kann oder nicht.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return true, falls die Reformpädagogik bei der Schulform vorkommen kann und ansonsten false
	 */
	@JsonIgnore
	public boolean hasSchulform(final Schulform schulform) {
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

}
