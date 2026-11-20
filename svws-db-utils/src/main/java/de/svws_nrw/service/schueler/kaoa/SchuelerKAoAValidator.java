package de.svws_nrw.service.schueler.kaoa;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.kaoa.KAOAZusatzmerkmalKatalogEintrag;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.types.kaoa.KAOAAnschlussoptionen;
import de.svws_nrw.asd.types.kaoa.KAOABerufsfeld;
import de.svws_nrw.asd.types.kaoa.KAOAEbene4;
import de.svws_nrw.asd.types.kaoa.KAOAKategorie;
import de.svws_nrw.asd.types.kaoa.KAOAMerkmal;
import de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.asd.types.kaoa.KAOAZusatzmerkmaleOptionsarten;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Dieser Validator prüft KAoA-Daten eines Schülers gegen die KAoA-Kataloge. Geprüft wird der vollständige Zustand der KAoA-Daten, bei
 * einem Patch also der Zustand nach dem Zusammenführen von bestehenden Daten und Patch. Der Validator greift nicht auf die Datenbank zu.
 */
public final class SchuelerKAoAValidator {

	/** Die maximale Länge der Bemerkung, sie ist durch die Datenbank begrenzt. */
	public static final int MAX_LAENGE_BEMERKUNG = 255;

	private static final String FELDER_NUR_EINES_GEFUELLT = "idAnschlussoption, idEbene4, idBerufsfeld, bemerkung";

	private static final String MELDUNG_NUR_EIN_FELD = "Es darf nur eines der folgenden Felder gefüllt sein: " + FELDER_NUR_EINES_GEFUELLT;

	private static final long MAX_ANZAHL_GEFUELLTE_FELDER = 1;

	private static final String MELDUNG_FELD_NICHT_NULL = "%s darf nicht null sein.";

	private SchuelerKAoAValidator() {
		// Utility-Klasse
	}

	/**
	 * Prüft die KAoA-Daten für das übergebene Schuljahr gegen die KAoA-Kataloge.
	 *
	 * @param daten      die zu prüfenden KAoA-Daten
	 * @param schuljahr  das Schuljahr, in dem die Katalogeinträge gültig sein müssen
	 */
	public static void validate(final SchuelerKAoADaten daten, final int schuljahr) {
		validateNurEinFeldGefuellt(daten);
		final var kategorie = getKategorie(daten.idKategorie, schuljahr);
		validateJahrgang(daten.idJahrgang, kategorie, schuljahr);
		final var merkmal = getMerkmal(daten.idMerkmal, kategorie, schuljahr);
		final var zusatzmerkmal = getZusatzmerkmal(daten.idZusatzmerkmal, merkmal, schuljahr);
		validateOptionsart(daten, zusatzmerkmal, schuljahr);
	}

	/**
	 * Prüft, ob sich der ASD-Schlüssel des Jahrgangs aus der Datenbank im übergebenen Schuljahr einem Jahrgang zuordnen lässt.
	 *
	 * @param asdJahrgang  der ASD-Schlüssel des Jahrgangs
	 * @param schuljahr    das Schuljahr, in dem der Jahrgang gültig sein muss
	 */
	public static void validateAsdJahrgang(final String asdJahrgang, final int schuljahr) {
		if (asdJahrgang == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Jahrgang der KAoA-Daten ist nicht gesetzt.");
		}
		if (Jahrgaenge.data().getEintragBySchuljahrUndSchluessel(schuljahr, asdJahrgang) == null) {
			throw new ApiOperationException(Status.NOT_FOUND,
					"Kein Jahrgang mit dem Schlüssel %s im Schuljahr %d gefunden.".formatted(asdJahrgang, schuljahr));
		}
	}

	private static void validateNurEinFeldGefuellt(final SchuelerKAoADaten daten) {
		final var anzahlGefuellt = Stream.of(
				daten.idAnschlussoption != null,
				daten.idEbene4 != null,
				daten.idBerufsfeld != null,
				StringUtils.isNotBlank(daten.bemerkung))
				.filter(istGefuellt -> istGefuellt)
				.count();
		if (anzahlGefuellt > MAX_ANZAHL_GEFUELLTE_FELDER) {
			throw new ApiOperationException(Status.BAD_REQUEST, MELDUNG_NUR_EIN_FELD);
		}
	}

	private static KAOAKategorie getKategorie(final long idKategorie, final int schuljahr) {
		final var kategorie = findOrThrowNotFound(() -> KAOAKategorie.data().getWertByID(idKategorie),
				"Keine KAoAKategorie mit der ID %d vorhanden.", idKategorie);
		requireValidInSchuljahr(kategorie.daten(schuljahr), "Die Kategorie mit der ID %d ist im Schuljahr %d nicht gültig.", idKategorie, schuljahr);
		return kategorie;
	}

	private static void validateJahrgang(final long idJahrgang, final KAOAKategorie kategorie, final int schuljahr) {
		final var jahrgang = findOrThrowNotFound(() -> Jahrgaenge.data().getWertByID(idJahrgang), "Kein Jahrgang mit der ID %d vorhanden.", idJahrgang);
		requireMatch(kategorie.hatJahrgang(schuljahr, jahrgang), "Der Jahrgang mit der ID %d ist für die Kategorie %s im Schuljahr %d nicht zulässig.",
				idJahrgang, kategorie.name(), schuljahr);
	}

	private static KAOAMerkmal getMerkmal(final long idMerkmal, final KAOAKategorie kategorie, final int schuljahr) {
		final var merkmal = findOrThrowNotFound(() -> KAOAMerkmal.data().getWertByID(idMerkmal), "Kein KAoAMerkmal mit der ID %d vorhanden.", idMerkmal);
		final var eintrag = requireValidInSchuljahr(merkmal.daten(schuljahr), "Das KAoAMerkmal mit der ID %d ist im Schuljahr %d nicht gültig.",
				idMerkmal, schuljahr);
		requireMatch(eintrag.kategorie.equals(kategorie.name()), "Das KAoAMerkmal mit der ID %d passt nicht zur Kategorie %s.", idMerkmal, kategorie.name());
		return merkmal;
	}

	private static KAOAZusatzmerkmal getZusatzmerkmal(final long idZusatzmerkmal, final KAOAMerkmal merkmal, final int schuljahr) {
		final var zusatzmerkmal = findOrThrowNotFound(() -> KAOAZusatzmerkmal.data().getWertByID(idZusatzmerkmal),
				"Kein KAoAZusatzmerkmal mit der ID %d vorhanden.", idZusatzmerkmal);
		final var eintrag = getEintragZusatzmerkmal(idZusatzmerkmal, zusatzmerkmal, schuljahr);
		requireMatch(eintrag.merkmal.equals(merkmal.name()), "Das KAoAZusatzmerkmal mit der ID %d passt nicht zum KAoAMerkmal %s.",
				idZusatzmerkmal, merkmal.name());
		return zusatzmerkmal;
	}

	private static KAOAZusatzmerkmalKatalogEintrag getEintragZusatzmerkmal(final long idZusatzmerkmal, final KAOAZusatzmerkmal zusatzmerkmal,
			final int schuljahr) {
		return requireValidInSchuljahr(zusatzmerkmal.daten(schuljahr), "Das KAoAZusatzmerkmal mit der ID %d ist im Schuljahr %d nicht gültig.",
				idZusatzmerkmal, schuljahr);
	}

	private static void validateOptionsart(final SchuelerKAoADaten daten, final KAOAZusatzmerkmal zusatzmerkmal, final int schuljahr) {
		final var optionsart = getOptionsart(getEintragZusatzmerkmal(daten.idZusatzmerkmal, zusatzmerkmal, schuljahr));
		switch (optionsart) {
			case SBO_EBENE_4 -> validateEbene4(daten.idEbene4, zusatzmerkmal, schuljahr);
			case ANSCHLUSSOPTION -> validateAnschlussoption(daten.idAnschlussoption, zusatzmerkmal, schuljahr);
			case BERUFSFELD -> validateBerufsfeld(daten.idBerufsfeld);
			default -> {
				// keine weitere Validierung notwendig, die Länge der Bemerkung prüft der BeanValidator am Request
			}
		}
	}

	private static KAOAZusatzmerkmaleOptionsarten getOptionsart(final KAOAZusatzmerkmalKatalogEintrag eintrag) {
		return findOrThrowNotFound(() -> KAOAZusatzmerkmaleOptionsarten.data().getWertByBezeichner(eintrag.optionsart),
				"Keine Optionsart für den Bezeichner %s vorhanden.", eintrag.optionsart);
	}

	private static void validateEbene4(final Long idEbene4, final KAOAZusatzmerkmal zusatzmerkmal, final int schuljahr) {
		requireNotNull(idEbene4, "idEbene4");
		final var ebene4 = findOrThrowNotFound(() -> KAOAEbene4.data().getWertByID(idEbene4), "Keine Ebene4 mit der ID %d vorhanden.", idEbene4);
		final var eintrag = requireValidInSchuljahr(ebene4.daten(schuljahr), "Die Ebene4 mit der ID %d ist im Schuljahr %d nicht gültig.", idEbene4, schuljahr);
		requireMatch(eintrag.zusatzmerkmal.equals(zusatzmerkmal.name()), "Die Ebene4 mit der ID %d passt nicht zum KAoAZusatzmerkmal %s.",
				idEbene4, zusatzmerkmal.name());
	}

	private static void validateAnschlussoption(final Long idAnschlussoption, final KAOAZusatzmerkmal zusatzmerkmal, final int schuljahr) {
		requireNotNull(idAnschlussoption, "idAnschlussoption");
		final var anschlussoption = findOrThrowNotFound(() -> KAOAAnschlussoptionen.data().getWertByID(idAnschlussoption),
				"Keine Anschlussoption mit der ID %d vorhanden.", idAnschlussoption);
		final var eintrag = requireValidInSchuljahr(anschlussoption.daten(schuljahr),
				"Die Anschlussoption mit der ID %d ist im Schuljahr %d nicht gültig.", idAnschlussoption, schuljahr);
		requireMatch(eintrag.anzeigeZusatzmerkmal.contains(zusatzmerkmal.name()), "Die Anschlussoption mit der ID %d passt nicht zum KAoAZusatzmerkmal %s.",
				idAnschlussoption, zusatzmerkmal.name());
	}

	private static void validateBerufsfeld(final Long idBerufsfeld) {
		requireNotNull(idBerufsfeld, "idBerufsfeld");
		findOrThrowNotFound(() -> KAOABerufsfeld.data().getWertByID(idBerufsfeld), "Kein Berufsfeld mit der ID %d vorhanden.", idBerufsfeld);
	}

	private static void requireNotNull(final Long id, final String feldname) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, MELDUNG_FELD_NICHT_NULL.formatted(feldname));
		}
	}

	private static <T> T findOrThrowNotFound(final Supplier<T> lookup, final String meldung, final Object... args) {
		try {
			return lookup.get();
		} catch (final CoreTypeException e) {
			throw new ApiOperationException(Status.NOT_FOUND, meldung.formatted(args));
		}
	}

	private static void requireMatch(final boolean matches, final String meldung, final Object... args) {
		if (!matches) {
			throw new ApiOperationException(Status.BAD_REQUEST, meldung.formatted(args));
		}
	}

	private static <T> T requireValidInSchuljahr(final T eintrag, final String meldung, final Object... args) {
		if (eintrag == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, meldung.formatted(args));
		}
		return eintrag;
	}

}
