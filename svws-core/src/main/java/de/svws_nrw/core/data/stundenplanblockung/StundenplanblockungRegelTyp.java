package de.svws_nrw.core.data.stundenplanblockung;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert die unterschiedlichen Regel-Typen, die im Rahmen 
 * der Stundenplanblockung eingesetzt werden bei {@link StundenplanblockungRegel}. 
 */
public enum StundenplanblockungRegelTyp {

	// Start der Enums ...
	/**
	 * Eine Regel ist nicht definiert.
	 */
	UNDEFINIERT(0, "Undefiniert", Collections.emptyList()),

	/**
	 * Definiert, wie viele Tage in der Woche Unterricht stattfindet.  
	 */
	SCHULE_TAGE_PRO_WOCHE(1, "Schule: Unterrichtstage pro Woche:",
			Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER)
	),

	/**
	 * Definiert, wie viele Stunden es pro Tag maximal gibt.  
	 */
	SCHULE_LETZTE_STUNDE(2, "Schule: Letzte mögliche Stunde:",
			Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER)
	),

	/**
	 * Definiert, wie viele Springstunden eine Lehrkraft pro Woche maximal haben darf. 
	 * Dieser Wert kann pro Lehrkraft mit einer Regel individuell überschrieben werden.  
	 */
	SCHULE_LEHRKRAFT_MAX_SPRING_PRO_WOCHE(3, "Schule: Pro Lehrkraft pro Woche max. Springstunden:",
			Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER)
	),

	/**
	 * Definiert, wie viele Springstunden eine Lehrkraft pro Tag maximal haben darf. 
	 * Dieser Wert kann pro Lehrkraft mit einer Regel individuell überschrieben werden.  
	 */
	SCHULE_LEHRKRAFT_MAX_SPRING_PRO_TAG(4, "Schule: Pro Lehrkraft pro Tag max. Springstunden:",
			Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER)
	),

	/**
	 * Definiert, wie viel Präsenz (Unterricht + Springstunden) eine Lehrkraft pro Tag maximal haben darf. 
	 * Dieser Wert kann pro Lehrkraft mit einer Regel individuell überschrieben werden.  
	 */
	SCHULE_LEHRKRAFT_MAX_PRAESENZ_PRO_TAG(5, "Schule: Pro Lehrkraft pro Tag max. Präsenz:",
			Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER)
	),

	/**
	 * Definiert, ob in bestimmten Stunden Einzelstunden verboten sind.
	 */
	SCHULE_LERNGRUPPEN_KEINE_EINZELSTUNDE_IN_STUNDE(6, "Schule: Lerngruppen haben keine Enzelstunde in Stunde:",
			Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER)
	),

	/**
	 * Definiert, ob eine Doppelstunde (und mehr) diese und die nächste Stunde überschreiten darf.  
	 */
	SCHULE_LERNGRUPPEN_KEINE_STUNDENUEBERGAENGE_IN_STUNDE_UND_DARAUFFOLGEND(7, "Schule: Lerngruppen haben keinen Übergang von dieser zur darauffolgenden Stunde:",
			Arrays.asList(StundenplanblockungRegelParameterTyp.WERT_INTEGER)
	),

	;
	// ... Ende der Enums.

	/**Der ID des Typs der Regel. */
	public final int id;

	/**Die Bezeichnung des Regel-Typs. */
	public final String bezeichnung;

	/**Die Typen der Regel-Parameter */
	private final @NotNull List<@NotNull StundenplanblockungRegelParameterTyp> paramTypes;

	/**Mapping von "Typ --> GostKursblockungRegelTyp". */
	private static final @NotNull HashMap<@NotNull Integer, StundenplanblockungRegelTyp> _map_id_regel = new HashMap<>();

	/**
	 * Liefert die Map. Falls diese leer ist, wird sie vorher gefüllt.
	 *
	 * @return Liefert die Map. Falls diese leer ist, wird sie vorher gefüllt.
	 */
	private static @NotNull HashMap<@NotNull Integer, StundenplanblockungRegelTyp> getMap() {
		if (_map_id_regel.isEmpty())
			for (final @NotNull StundenplanblockungRegelTyp typ : StundenplanblockungRegelTyp.values())
				if (_map_id_regel.put(typ.id, typ) != null)
					throw new NullPointerException("StundenplanblockungRegelTyp.id=" + typ.id + " doppelt!");
		return _map_id_regel;
	}

	/**
	 * Liefert die Menge aller existierender Regeln.
	 *
	 * @return Die Menge aller existierender Regeln.
	 */
	public static @NotNull Collection<StundenplanblockungRegelTyp> getCollection() {
		return getMap().values();
	}

	/**
	 * Erstellt einen neuen Regel-Typ mit der angegeben ID.
	 *
	 * @param id            die ID des Regel-Typs
	 * @param paramCount    die Anzahl der Parameter für diesen Regel-Typ
	 * @param bezeichnung   die textuelle Bezeichnung für diesen Regel-Typ 
	 */
	private StundenplanblockungRegelTyp(final int id, final @NotNull String bezeichnung,
			final @NotNull List<@NotNull StundenplanblockungRegelParameterTyp> paramTypes) throws IllegalArgumentException {
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.paramTypes = paramTypes;
	}

	/**
	 * Ermittelt den Regel-Typ anhand seiner ID und gibt diesen zurück.
	 *
	 * @param id   die ID des Regel-Typs
	 * @return der Regel-Typ 
	 */
	public static @NotNull StundenplanblockungRegelTyp fromTyp(final Integer id) {
		if (id == null)
			return StundenplanblockungRegelTyp.UNDEFINIERT;
		final StundenplanblockungRegelTyp gostTyp = getMap().get(id);
		if (gostTyp == null)
			return StundenplanblockungRegelTyp.UNDEFINIERT;
		return gostTyp;
	}

	/**
	 * Ermittelt den Regel-Typ anhand des Regel-Objektes.
	 *
	 * @param pRegel Das Regel-Objekt.
	 * @return der Regel-Typ 
	 */
	public static @NotNull StundenplanblockungRegelTyp fromRegel(final @NotNull StundenplanblockungRegel pRegel) {
		return fromTyp(pRegel.typ);
	}

	/**
	 * Gibt die Anzahl der Parameter für diesen Regel-Type zurück.
	 *
	 * @return die Anzahl der Parameter für diesen Regel-Type zurück. 
	 */
	public int getParamCount() {
		return this.paramTypes.size();
	}

	/**
	 * Gibt den i-ten Parameter-Typ der Regel zurück.
	 *
	 * @param i   der Index des Parameters
	 *
	 * @return der Parameter-Typ
	 *
	 * @throws IllegalArgumentException falls der angegebene Index ungültig ist 
	 */
	public @NotNull StundenplanblockungRegelParameterTyp getParamType(final int i) throws IllegalArgumentException {
		if ((i < 0) || (i >= paramTypes.size()))
			throw new IllegalArgumentException(
					"Ein Parameter mit dem Index i existiert nicht für den Regel-Typ " + this.name());
		return paramTypes.get(i);
	}

	/**
	 * Prüft, ob der Regeltyp einen Parameter von dem angegebenen
	 * Parametertyp hat.
	 *  
	 * @param paramType   der Parametertyp
	 *
	 * @return true, falls die Regel einen solchen Parametertyp hat und ansonsten false 
	 */
	public boolean hasParamType(final StundenplanblockungRegelParameterTyp paramType) {
		for (final StundenplanblockungRegelParameterTyp cur : paramTypes)
			if (paramType == cur)
				return true;
		return false;
	}

}
