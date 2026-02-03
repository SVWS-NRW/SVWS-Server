package de.svws_nrw.core.abschluss.bk.d.markieren;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import de.svws_nrw.core.abschluss.bk.d.BKGymAbiturdatenManager;
import de.svws_nrw.core.data.bk.abi.BKGymAbiturMarkierungsalgorithmusErgebnis;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse enthält die verschiedenen Markierungsvarianten, von denen die beste
 * abschließend ermittelt wird/werden kann.
 * Das ist notwendig, da die beste Markierung bei der Fremdsprachenbelegung nicht nur auf Grundlagedirekt durch eine mögliche Markierungsoption entsprechend den Regel der Prüfungsordnung
 * der Fremdsprachenkurse ermittelt werden kann. Auch für die Berücksichtigung der
 * Facharbeit wird eine zusätzliche Variante durchgerechnet.
 */
public class BKGymAbiturMarkierungsVarianten {

	/** Der Manager für die Fächer des beruflichen Gymnasiums */
	public final @NotNull BKGymAbiturdatenManager abiturdatenManager;

	/** Die verschiedenen Markierungsergebnisse, aus denen das beste Ergebnis gewählt wird. */
	private final @NotNull List<BKGymAbiturMarkierungsVariante> ergebnisse = new ArrayList<>();

	/** Eine Map, welche von der Nummer des Abiturfaches auf die FachID verweist.*/
	private final @NotNull HashMap<Integer, Long> mapAbiturfachbelegungen = new HashMap<>();


	/**
	 * Konstruktor der die Root-Variante der Markierungen aus den Abiturdaten erzeugt.
	 *
	 * @param manager   der Manager für die Abiturdaten
	 */
	public BKGymAbiturMarkierungsVarianten(final @NotNull BKGymAbiturdatenManager manager) {
		this.abiturdatenManager = manager;
		init();
	}


	/**
	 * Erzeugt die root-Variante und trägt sie als einzige in die Liste der Varianten ein.
	 * Über Markierungsregeln können weitere Varianten später hinzugefügt werden.
	 */
	private void init() {
		final @NotNull BKGymAbiturMarkierungsVariante root = new BKGymAbiturMarkierungsVariante(this);
		ergebnisse.add(root);
		reportDoppelteFaecher(root);
		reportFehlerFacharbeit(root);
	}


	/**
	 * gibt die im Fächermanager ermittelten doppelten Fächer ins Log aus.
	 *
	 * @param root   die Markierungsvariante
	 */
	private void reportDoppelteFaecher(@NotNull final BKGymAbiturMarkierungsVariante root) {
		for (final @NotNull String fach : abiturdatenManager.getFaecherManager().getDoppelteFaecher())
			root.addLogEintrag(0, "Hinweis: Das Fach " + fach + " ist im Fächerkatalog nicht eindeutig bestimmbar. Bitte die Bezeichnungen der Fächer eindeutig festlegen.");
	}


	/**
	 * gibt den Hinweis aus, wenn das der Facharbeit zugeordnete Fach kein Leistungskurs ist
	 *
	 * @param root   die Markierungsvariante
	 */
	private void reportFehlerFacharbeit(@NotNull final BKGymAbiturMarkierungsVariante root) {
		if (!abiturdatenManager.getFachbelegungManager().getIstFacharbeitLK())
			root.addLogEintrag(0, "Hinweis: Die Facharbeit ist nicht einem der beiden Leistungkursfächer zugeordnet.");
	}


	/**
	 * Getter auf die Ergebnisse
	 *
	 * @return die Liste mit den Markierungsvarianten
	 */
	public @NotNull List<BKGymAbiturMarkierungsVariante> getErgebnisse() {
		return ergebnisse;
	}


	/**
	 * Fügt eine neue Markierungsvariante hinzu
	 *
	 * @param variante   die Variante, die hinzugefügt wird
	 */
	public void addVariante(final @NotNull BKGymAbiturMarkierungsVariante variante) {
		ergebnisse.add(variante);
	}


	/**
	 * Liefert das beste Ergebnis als DTO zurück
	 *
	 * @return das Ergebnis
	 */
	public BKGymAbiturMarkierungsalgorithmusErgebnis getBestesErgebnis() {
		if (ergebnisse.isEmpty())
			return null;
		ergebnisse.sort(BKGymAbiturMarkierungsVariante.comparator);
		return ergebnisse.getFirst().getErgebnis();
	}
}
