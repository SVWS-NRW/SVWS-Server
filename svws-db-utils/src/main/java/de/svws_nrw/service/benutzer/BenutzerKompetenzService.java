package de.svws_nrw.service.benutzer;

import java.util.Collection;
import java.util.Set;

import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Zugriff und die Verwaltung der Benutzerkompetenzen des aktuellen Benutzers
 */
public class BenutzerKompetenzService {

	private final BenutzerRepository benutzerRepository;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository   das Repository für den Zugriff auf die Benutzerdaten des angemeldeten Benutzers
	 */
	public BenutzerKompetenzService(final BenutzerRepository benutzerRepository) {
		this.benutzerRepository = benutzerRepository;
	}


	/**
	 * Prüft, anhand der übergebenen Kompetenzen bzw. Kompetenzmengen, ob der Benutzer ausschließlich eine funktionsbezogene
	 * Kompetenz besitzt und nicht noch zusätzlich eine übergreifendere Kompetenz.
	 *
	 * @param kompetenzFunktionsbezogen   die zu prüfenden funktionsbezogenen Kompetenzen
	 * @param kompetenzenUebergreifend    die zu prüfenden übergreifenden Kompetenzen
	 *
	 * @return <code>true</code>, wenn der Benutzer ausschließlich die funktionsbezogene Kompetenz besitzt. Ansonsten <code>false</code> wenn der Benutzer
	 *   eine übergreifende Kompetenz besitzt, bei der eine weiterführende Prüfung nicht mehr notwendig ist. <br>
	 *
	 * @throws IllegalArgumentException wenn einer der Methodenparameter fehlt.
	 */
	private boolean hatBenutzerNurFunktionsbezogeneKompetenz(final @NotNull Collection<BenutzerKompetenz> kompetenzFunktionsbezogen,
			final @NotNull Set<BenutzerKompetenz> kompetenzenUebergreifend) throws IllegalArgumentException {
		if ((kompetenzFunktionsbezogen == null) || (kompetenzenUebergreifend == null) || kompetenzenUebergreifend.isEmpty()) {
			throw new IllegalArgumentException("Die Parameter kompetenzFunktionsbezogen und kompetenzenUebergreifend dürfen nicht null oder leer sein.");
		}

		final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
		final boolean hatUebergreifendeKompetenz = kompetenzenUebergreifend.stream().anyMatch(benutzer::hatVerwendeteKompetenz);
		if (hatUebergreifendeKompetenz) {
			return false;
		}

		for (final BenutzerKompetenz kompetenz : kompetenzFunktionsbezogen) {
			if (benutzer.hatVerwendeteKompetenz(kompetenz)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Prüft, ob der Benutzer bei dem angegeben Abiturjahrgang als Beratungslehrer funktionsbezogene Rechte hat oder nicht.
	 *
	 * @param abijahrgang   der zu prüfende Abiturjahrgang
	 *
	 * @throws ApiOperationException   wenn der Benutzer nicht die Kompetenz für den funktionsbezogenen Zugriff auf die Daten des Abiturjahrganges hat (403 - FORBIDDEN).
	 */
	private void pruefeBeratungslehrerKompetenz(final Integer abijahrgang) throws ApiOperationException {
		if (abijahrgang == null) {
			throw new ApiOperationException(Status.FORBIDDEN,
					"Der Benutzer kann keine funktionsbezogene Kompetenz nutzen, um auf Daten zuzugreifen, die keinem Abiturjahrgang zugeordnet sind.");
		}
		final boolean hatKompetenzFuerAbijahrgang = benutzerRepository.getAktuellerBenutzer().getAbiturjahrgaenge().contains(abijahrgang);
		if (!hatKompetenzFuerAbijahrgang) {
			throw new ApiOperationException(Status.FORBIDDEN,
					"Der Benutzer hat keine funktionsbezogene Kompetenz für den Zugriff als Beratungslehrer auf den Abiturjahrgang " + abijahrgang);
		}
	}


	/**
	 * Prüft, ob der Benutzer eine allgemeine oder funktionsbezogene Kompetenz für die Laufbahnplanung
	 * bei dem Abiturjahrgangs hat. Ist dies nicht der Fall, so ist der Zugriff verboten und es wird eine
	 * {@link ApiOperationException} mit dem Status FORBIDDEN erzeugt.
	 *
	 * @param abiturjahrgang   der Abiturjahrgang, für welchen die Kompetenzen geprüft werden sollen
	 *
	 * @throws ApiOperationException wenn der Benutzer keine Kompetenz für den Zugriff auf die Laufbahnplanung besitzt.
	 */
	public void pruefeKompetenzLaufbahnplanung(final Integer abiturjahrgang) throws ApiOperationException {
		final Set<BenutzerKompetenz> funktionsbezogeneKompetenzen = Set.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN);
		final Set<BenutzerKompetenz> allgemeineKompetenzen = Set.of(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN);
		if (hatBenutzerNurFunktionsbezogeneKompetenz(funktionsbezogeneKompetenzen, allgemeineKompetenzen)) {
			pruefeBeratungslehrerKompetenz(abiturjahrgang);
		}
	}

	/**
	 * Prüft, ob der Benutzer eine allgemeine oder funktionsbezogene Kompetenz für die Gymnasiale Oberstufe
	 * bei dem Abiturjahrgangs hat. Ist dies nicht der Fall, so ist der Zugriff verboten und es wird eine
	 * {@link ApiOperationException} mit dem Status FORBIDDEN erzeugt.
	 *
	 * @param abiturjahrgang   der Abiturjahrgang, für welchen die Kompetenzen geprüft werden sollen
	 *
	 * @throws ApiOperationException wenn der Benutzer keine Kompetenz für den Zugriff auf die Laufbahnplanung besitzt.
	 */
	public void pruefeKompetenzGost(final Integer abiturjahrgang) throws ApiOperationException {
		final Set<BenutzerKompetenz> funktionsbezogeneKompetenzen = Set.of(
				BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
				BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION
		);
		final Set<BenutzerKompetenz> allgemeineKompetenzen = Set.of(
				BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN
		);
		if (hatBenutzerNurFunktionsbezogeneKompetenz(funktionsbezogeneKompetenzen, allgemeineKompetenzen)) {
			pruefeBeratungslehrerKompetenz(abiturjahrgang);
		}
	}

}
