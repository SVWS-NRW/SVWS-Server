package de.svws_nrw.service.gost;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.kataloge.jahrgaenge.JahrgaengeUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Record enthält die Daten eines Jahrgangs der Schule, welche für die Zuordnung von Abiturjahrgängen zu Jahrgängen benötigt werden.
 *
 * @param kuerzelASDJahrgang   das Statistik-Kürzel des Jahrgangs
 * @param gliederung           die Schulgliederung des Jahrgangs, null falls keine zugeordnet ist
 */
public record SchulJahrgang(@NotNull String kuerzelASDJahrgang, Schulgliederung gliederung) {

	/** Die Anzahl der Schuljahre, welche ein Abiturjahrgang in der gymnasialen Oberstufe verbringt */
	static final int ANZAHL_SCHULJAHRE_OBERSTUFE = 3;


	/**
	 * Prüft, ob der Jahrgang zur gymnasialen Oberstufe gehört.
	 *
	 * @return true, wenn der Jahrgang zur gymnasialen Oberstufe gehört
	 */
	public boolean istGymOb() {
		return JahrgaengeUtils.istGymOb(kuerzelASDJahrgang);
	}


	/**
	 * Prüft, ob sich ein Schüler in diesem Jahrgang noch die angegebene Anzahl von Jahren bis zum Abitur an der Schule befindet.
	 *
	 * @param schulform    die Schulform der Schule
	 * @param restjahre    die Anzahl der Jahre bis zum Abitur
	 *
	 * @return true, wenn die Restjahre des Jahrgangs bestimmt werden können und mit den angegebenen Restjahren übereinstimmen
	 */
	public boolean hatRestjahreBisAbitur(final Schulform schulform, final int restjahre) {
		final Integer jahrgangRestjahre = getRestjahreBisAbitur(schulform);
		return (jahrgangRestjahre != null) && (jahrgangRestjahre == restjahre);
	}


	/**
	 * Bestimmt die Anzahl der Jahre, die sich ein Schüler in diesem Jahrgang bis zum Abitur noch an der Schule befindet. Außerhalb
	 * von Gymnasien wird bei Jahrgängen der Sekundarstufe I die Oberstufenzeit hinzugerechnet.
	 *
	 * @param schulform   die Schulform der Schule
	 *
	 * @return die Anzahl der Restjahre oder null, falls diese für den Jahrgang nicht bestimmt werden kann
	 */
	private Integer getRestjahreBisAbitur(final Schulform schulform) {
		final Integer restjahre = JahrgaengeUtils.getRestlicheJahre(schulform, gliederung, kuerzelASDJahrgang);
		if ((restjahre != null) && istSekIAusserhalbGymnasium(schulform)) {
			return restjahre + ANZAHL_SCHULJAHRE_OBERSTUFE;
		}
		return restjahre;
	}


	/**
	 * Prüft, ob der Jahrgang zur Sekundarstufe I gehört und die Schulform kein Gymnasium ist. In diesem Fall folgt die Oberstufenzeit
	 * erst nach dem Jahrgang.
	 *
	 * @param schulform   die Schulform der Schule
	 *
	 * @return true, wenn der Jahrgang zur Sekundarstufe I gehört und die Schulform kein Gymnasium ist
	 */
	private boolean istSekIAusserhalbGymnasium(final Schulform schulform) {
		return (schulform != Schulform.GY) && JahrgaengeUtils.istSekI(kuerzelASDJahrgang);
	}

}
