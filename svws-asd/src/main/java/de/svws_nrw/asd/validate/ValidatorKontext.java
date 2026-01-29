package de.svws_nrw.asd.validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt den Kontext dar, in welchem die Validatoren
 * arbeiten. Alle Validatoren bekommen im Konstruktor ihren jeweiligen
 * Kontext übergeben.
 */
public class ValidatorKontext {

	/** Die Schulnummer der Schule */
	private final long _schulNr;

	/** Die Stammdaten der Schule */
	private final @NotNull Schulform _schulform;

	/** Die Laufeigenschaften der Validatoren */
	private final @NotNull ValidatorManager _validatorManager;

	/** Die ID des aktuellen Schuljahresabschnittes der Schule */
	private final long _idSchuljahresabsbschnittAktuell;

	/** Die Schuljahresabschnitte der Schule, welche ihrer ID zugeordnet werden */
	private final @NotNull Map<Long, Schuljahresabschnitt> _mapSchuljahresabschnitte = new HashMap<>();


	/**
	 * Erzeugt einen neuen Kontext für Validatoren. Prüfe auch, ob die Stammdaten der Schule eine Valiadierung möglich machen
	 * oder aufgrund gravierender Fehler eine Prüfungen unmöglich machen.
	 *
	 * @param schulNr                         die Schulnummer der Schule
	 * @param schulform                       die Schulform der Schule
	 * @param abschnitte                      die Liste der Schuljahresabschnitte der Schule
	 * @param idSchuljahresabsbschnittAktuell die ID des aktuellen Schuljahresabschnittes der Schule
	 * @param zebras                          die Umgebung, in der gerade validiert wird: true: ZeBrAS, false: SVWS
	 */
	public ValidatorKontext(final long schulNr, final @NotNull Schulform schulform, final @NotNull List<Schuljahresabschnitt> abschnitte,
			final long idSchuljahresabsbschnittAktuell, final boolean zebras) {

		_schulNr = schulNr;
		_schulform = schulform;
		_idSchuljahresabsbschnittAktuell = idSchuljahresabsbschnittAktuell;
		for (final Schuljahresabschnitt entry : abschnitte)
			_mapSchuljahresabschnitte.put(entry.id, entry);

		_validatorManager = ValidatorManager.getManager(schulform, zebras);
	}

	/**
	 * Gibt die Schulform der Schule anhand der Information aus den
	 * Stammdaten der Schule zurück.
	 *
	 * @return die Schulform als Core-Type
	 */
	public @NotNull Schulform getSchulform() {
		return _schulform;
	}

	/**
	 * Gibt das aktuelle Schuljahr der Schule zurück.
	 *
	 * @return das aktuelle Schuljahr
	 */
	public int getSchuljahr() {
		final Schuljahresabschnitt abschnitt = getSchuljahresabschnitt();
		if (abschnitt != null)
			return abschnitt.schuljahr;
		//sollte nie erreicht werden!
		throw new ValidatorException("Es ist kein gültiger Schuljahresabschnitt in den SchuleStammdaten gesetzt");
	}


	/**
	 * Gibt den aktuellen Schuljahresabschnitt der Schule zurück.
	 *
	 * @return der Schuljahresabschnitt oder null, wenn dieser nicht korrekt gesetzt ist
	 */
	public Schuljahresabschnitt getSchuljahresabschnitt() {
		return _mapSchuljahresabschnitte.get(_idSchuljahresabsbschnittAktuell);
	}


	/**
	 * Gibt den Schuljahresabschnitt der Schule für die übergebene ID zurück.
	 *
	 * @param id   die ID des Schuljahresabschnitts
	 *
	 * @return der Schuljahresabschnitt oder null, falls die id ungültig ist
	 */
	public Schuljahresabschnitt getSchuljahresabschnittByID(final long id) {
		return _mapSchuljahresabschnitte.get(id);
	}


	/**
	 * Gibt den Datums-Manager für den Beginn des aktuellen Schuljahres zurück.
	 *
	 * @return der Datums-Manager für den Beginn des aktuellen Schuljahres
	 */
	public @NotNull DateManager getSchuljahresbeginn() {
		try {
			// am 1.8. beginnt das Schuljahr...
			return DateManager.fromValues(getSchuljahr(), 8, 1);
		} catch (final InvalidDateException e) {
			// sollte nicht erreicht werden !
			throw new ValidatorException("Fehler beim Erstellen des Datums für den Beginn des Schuljahres", e);
		}
	}


	/**
	 * Gibt den Datums-Manager für das Ende des aktuellen Schuljahres zurück.
	 *
	 * @return der Datums-Manager für das Ende des aktuellen Schuljahres
	 */
	public @NotNull DateManager getSchuljahresende() {
		try {
			// am 31.7. des Folgejahres endet das Schuljahr...
			return DateManager.fromValues(getSchuljahr() + 1, 7, 31);
		} catch (final InvalidDateException e) {
			// sollte nicht erreicht werden !
			throw new ValidatorException("Fehler beim Erstellen des Datums für das Ende des Schuljahres", e);
		}
	}


	/**
	 * Gibt die Schulnummer der Schule zurück.
	 *
	 * @return die Schulnummer der Schule
	 */
	public long getSchulnummer() {
		return _schulNr;
	}

	/**
	 * Gibt den ValidatorManager zurück.
	 *
	 * @return der ValidatorManager
	 */
	public @NotNull ValidatorManager getValidatorManager() {
		return _validatorManager;
	}

}
