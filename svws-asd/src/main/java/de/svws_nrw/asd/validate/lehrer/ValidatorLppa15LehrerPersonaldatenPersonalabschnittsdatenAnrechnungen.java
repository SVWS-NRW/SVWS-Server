package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob bei Teilzeitbeschäftigung im Blockmodell (TS) in der Ermäßigungs-/Freistellungsphase (ID 290)
 * eine Stundenanzahl größer 0 eingetragen wurde.
 */
public final class ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Minderleistungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _minderleistung;

	/** Die Beschäftigungsart der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull Long> _idBeschaeftigungsart;

	/** Der Einsatzstatus der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull Long> _idEinsatzstatus;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param minderleistung         die Liste der Minderleistungsstunden
	 * @param idBeschaeftigungsart   die ID der Beschäftigungsart
	 * @param idEinsatzstatus        die ID des Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppa15LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistung,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_minderleistung = minderleistung;
		_idBeschaeftigungsart = idBeschaeftigungsart;
		_idEinsatzstatus = idEinsatzstatus;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> listeMinderleistung = _minderleistung.get();
		final Long idBeschaeftigung = _idBeschaeftigungsart.get();
		final Long idEinsatz = _idEinsatzstatus.get();

		if ((listeMinderleistung == null) || (idBeschaeftigung == null)) {
			return true;
		}

		final LehrerBeschaeftigungsart beschaeftigung = LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigung);
		if (beschaeftigung != LehrerBeschaeftigungsart.TS) {
			return true;
		}

		final LehrerEinsatzstatus einsatz = (idEinsatz == null) ? null : LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatz);
		if ((idEinsatz != null) && (einsatz != LehrerEinsatzstatus.A)) {
			return true;
		}

		final LehrerMinderleistungsarten grund290 = LehrerMinderleistungsarten.data().getWertByBezeichner("ID_290");
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : listeMinderleistung) {
			if (eintrag.idGrund == null) {
				continue;
			}
			final LehrerMinderleistungsarten art = LehrerMinderleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art == grund290) && (Math.abs(eintrag.anzahl) < 0.001)) {
				addFehler(0,
						"Anrechnungsgründe des Lehrers: Den Angaben nach befindet sich die ausgewählte Lehrkraft in der 'Ermäßigungs-' bzw. 'Freistellungsphase' der 'Teilzeitbeschäftigung im Blockmodell'. Bitte erfassen Sie bei der Minderleistung 'Ermäßigungs-/Freistellungsphase 'Teilzeitbeschäftigung im Blockmodell' im Feld 'Stunden' die Differenz aus dem Pflichtstundensoll und der tatsächlich geleisteten Arbeitszeit. Das Pflichtstundensoll ist in jeder Phase der Teilzeitbeschäftigung im Blockmodell gleich.");
				return false;
			}
		}

		return true;
	}

}
