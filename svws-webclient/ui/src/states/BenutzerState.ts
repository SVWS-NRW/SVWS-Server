import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import type { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { InjectionKey } from "vue";
import type { BenutzerDaten } from "@core/core/data/benutzer/BenutzerDaten";
import type { BenutzerEMailDaten } from "@core/core/data/benutzer/BenutzerEMailDaten";
import type { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";
import { AppContext } from "@ui/AppContext";
import type { AES } from "@ui/utils/crypto/aes";

/**
 * Die Schnittstelle für den Zustand der Schuljahresabschnitte und des aktuell ausgewählten Abschnitts
 */
export interface BenutzerState {

	/**
	 * Gibt an, sofern ein Login stattgefunden hat, ob es sich bei dem angemeldeten Benutzer um einen Administrator handelt oder nicht
	 *
	 * @returns true, wenn Benutzer Admin ist
	 */
	get istAdmin(): boolean

	/**
	 * Die Kompetenzen des angemeldeten Benutzers, sofern ein Login stattgefunden hat
	 *
	 * @returns die Liste der Benutzerkompetenzen
	 */
	get kompetenzen(): Set<BenutzerKompetenz>;

	/**
	 * Gibt den Status zurück, ob der Benutzer authentifiziert wurde
	 *
	 * @returns true, wenn der Benutzer angemeldet ist
	 */
	get authenticated(): boolean;

	/**
	 * Gibt die Benutzerdaten des angemeldeten Benutzers zurück, sofern ein Login stattgefunden hat
	 *
	 * @returns die Benutzerdaten des angemeldeten Benutzers
	 */
	get benutzerdaten(): BenutzerDaten;

	/**
	 * Die Klassen-IDs, auf denen der angemeldete Benutzer aufgrund einer Klassen- oder Abteilungsleitung funktionsbezogene Kompetenzen hat
	 *
	 * @returns das Set der IDs
	 */
	get kompetenzenKlasse(): Set<number>;

	/**
	 * Die Abiturjahrgänge, auf denen der angemeldete Benutzer als Beratungslehrer funktionsbezogene Kompetenzen hat
	 *
	 * @returns das Set der IDs
	 */
	get kompetenzenAbiturjahrgaenge(): Set<number>;

	/** Der AES-Schlüssel des Benutzers
	 *
	 * @returns der AES-Schlüssel des Benutzers
	 */
	get aes(): AES;

	/**
	 * Prüft, ob der angemeldete Benutzer die angegebene Kompetenz hat oder
	 * nicht.
	 *
	 * @param kompetenz   die zu prüfende Kompetenz.
	 *
	 * @returns true, falls der Benutzer die Kompetenz hat und ansonsten false
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	benutzerHatKompetenz(kompetenz: BenutzerKompetenz): boolean;

	/**
	 * Prüft, ob der angemeldete Benutzer eine der angegebenen Kompetenzen
	 * hat oder nicht.
	 *
	 * @param kompetenzen   die zu prüfenden Kompetenzen.
	 *
	 * @returns true, falls der Benutzer einer der Kompetenzen hat und ansonsten false
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist
	 */
	benutzerHatEineKompetenz(kompetenzen: Iterable<BenutzerKompetenz>): boolean;

	/**
	 * Gibt den Typ des Benutzers zurück.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist oder der Benutzer-Typ ungültig ist
	 */
	get benutzertyp(): BenutzerTyp;

	/**
	 * Gibt an, ob es sich bei dem Benutzer um einen Lehrer-Benutzer handelt.
	 *
	 * @throws {Error} falls kein Benutzer angemeldet ist oder der Benutzer-Typ ungültig oder kein Lehrer ist
	 */
	get benutzerIDLehrer(): number;

	/**
	 * Gibt die BenutzerEMailDaten zurück
	 *
	 * @returns die BenutzerEMailDaten des Benutzers
	 */
	get benutzerEMailDaten(): BenutzerEMailDaten;

	/**
	 * Holt die Daten des Initialkennworts vom Server und setzt es
	 */
	getWenomInitialkennwort(): Promise<void>;

	/**
	 * Das Initialkennwort des Benutzers für den WeNoM
	 *
	 * @returns das Initialkennwort des WeNoM
	 */
	get wenomInitialkennwort(): string | null;

	/**
	 * Methode zum Patchen von Benutzerdaten (zur Zeit ohne Api-Endpunkt)
	 *
	 * @param data   die zu patchenden Daten des Benutzers
	 */
	patchBenutzerdaten(data: Partial<BenutzerDaten>): Promise<void>;

	/**
	 * Eine Methode zum Ändern des Benutzerpassworts
	 *
	 * @param eins   das Passwort des Benutzers, alt
	 * @param zwei   das Passwort des Benutzers, neu
	 *
	 * @returns true, wenn erfolgreich das Passwort geändert wurde
	 */
	patchBenutzerpasswort(eins: string, zwei: string): Promise<boolean>;

	/**
	 * Methode, um das Passwort des WeNoM auf das Initialkennwortzurückzusetzen
	 */
	passwordResetWenom(): Promise<boolean>;

	/**
	 * Methode zum Patchen des BenutzerEMailDaten
	 *
	 * @param data   die Mail-Benutzerdaten des Benutzers, die geändert werden sollen
	 *
	 */
	patchBenutzerEMailDaten(data: Partial<BenutzerEMailDaten>): Promise<void>;

}

export const BenutzerStateKey: InjectionKey<BenutzerState> = Symbol('BenutzerState');

export function useBenutzerState(): BenutzerState {
	const state = AppContext.instance.inject(BenutzerStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des BenutzerState über provide in der main.ts eingebunden");
	}
	return state;
}
