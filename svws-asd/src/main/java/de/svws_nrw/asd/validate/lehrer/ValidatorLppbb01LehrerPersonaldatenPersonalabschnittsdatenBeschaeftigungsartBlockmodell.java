package de.svws_nrw.asd.validate.lehrer;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator für Beschäftigungsart = "TS" (Teilzeit im Blockmodell).
 * <p>
 * Prüft: Bei Pflichtstundensoll &gt; 0 und Einsatzstatus = " " (Leerzeichen) oder "A"
 * muss <b>entweder</b> der Mehrleistungsgrund 100 <b>oder</b> einer der
 * Minderleistungsgründe 240/290 gesetzt sein.
 * </p>
 *
 * <p><b>Fehlerhärte:</b> Default</p>
 * <p><b>Fehlertext:</b> Bei BA="TS" muss Grund 100 (Mehrleistung) oder 240/290 (Minderleistung) gesetzt sein.</p>
 */
public final class ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell extends Validator {

	/** Die Lehrer-Personalabschnittsdaten, die geprüft werden. */
	private final @NotNull LehrerPersonalabschnittsdaten daten;

	/**
	 * Erstellt einen neuen Validator.
	 *
	 * @param daten    die Abschnittsdaten der Lehrkraft
	 * @param kontext  der Kontext der Validierung
	 */
	public ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(
			final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
	}

	/**
	 * Prüft, ob eine Liste von Anrechnungsstunden einen bestimmten Grund enthält.
	 *
	 * @param liste    die zu prüfende Liste (kann {@code null} sein)
	 * @param idGrund  die gesuchte Grund-ID
	 *
	 * @return {@code true}, wenn ein Eintrag mit {@code idGrund} enthalten ist; sonst {@code false}
	 */
	private static boolean hatGrund(final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste, final long idGrund) {
		if (liste == null || liste.isEmpty())
			return false;
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden lpa : liste)
			if (lpa != null && lpa.idGrund == idGrund)
				return true;
		return false;
	}

	/**
	 * Prüft die Regel für Teilzeit im Blockmodell (Beschäftigungsart = "TS"):
	 * <p>
	 * Gilt nur, wenn das Pflichtstundensoll > 0 ist und der Einsatzstatus
	 * " " (Leerzeichen) oder "A" lautet. Die Prüfung ist erfüllt, wenn
	 * mindestens einer der folgenden Gründe gesetzt ist:
	 * <ul>
	 *   <li>Mehrleistung: 100</li>
	 *   <li>Minderleistung: 240 oder 290</li>
	 * </ul>
	 *
	 * @return {@code true}, wenn die Regel erfüllt oder nicht anwendbar ist, sonst {@code false}
	 */
	@Override
	protected boolean pruefe() {
		final Double pss = daten.pflichtstundensoll;
		if (pss == null || pss <= 0.0)
			return true;

		final String ba = daten.beschaeftigungsart == null ? "" : daten.beschaeftigungsart.trim();
		if (LehrerBeschaeftigungsart.data().getWertBySchluessel(ba) != LehrerBeschaeftigungsart.TS)
			return true;

		final String es = daten.einsatzstatus == null ? "" : daten.einsatzstatus;
		if (!"".equals(es.trim()) && LehrerEinsatzstatus.data().getWertBySchluessel(es) != LehrerEinsatzstatus.A)
			return true;

		final boolean hatMehr100 = hatGrund(daten.mehrleistung, 100L);
		final boolean hatMinder240 = hatGrund(daten.minderleistung, 240L);
		final boolean hatMinder290 = hatGrund(daten.minderleistung, 290L);

		final boolean hatMehrMinderGrund = hatMehr100 || hatMinder240 || hatMinder290;

		final String fehlertext =
				"\"Bei einer Lehrkraft mit 'Beschäftigungsart' = TS (Teilzeitbeschäftigung im Blockmodell) muss entweder der Mehrleistungsgrund '100' Ansparphase, Phase mit erhöhter Arbeitszeit \"Teilzeitbeschäftigung im Blockmodell\" (§ 65 LBG) (vormals Sabbatjahr) oder der Minderleistungsgrund '290' (Ermäßigungs-/Freistellungsphase 'Teilzeitbeschäftigung im Blockmodell') eingetragen sein.\"))";

		if (!hatMehrMinderGrund) {
			this.addFehler(1, fehlertext);
			return false;
		}

		return true;
	}
}
