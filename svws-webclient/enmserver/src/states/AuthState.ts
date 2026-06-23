import type { Schulform } from '@core/asd/types/schule/Schulform';
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
import type { ServerMode } from '@core/core/types/ServerMode';
import { inject, type InjectionKey } from 'vue';

/**
 *  Die Schnittstelle für den Zustand der Authentifizierung im Client
 */
export interface AuthState {

	/** Gibt die Version der Anwendung zurück */
	get version(): string;

	/** Gibt den Githash des aktuellen Commits der Anwendung zurück */
	get githash(): string;

	/** Gibt den Modus zurück, in welchem der Server betrieben wird. */
	get mode(): ServerMode;

	/** Gibt die Schulform der Schule zurück. */
	get schulform(): Schulform;

	/**
	 * Gibt die verbleibende Zeit in Sekunden für die Gültigkeit des Tokens zurück.
	 * Existiert kein Token so wird 0 zurückgegeben.
	 *
	 * @returns die verbleibenden Sekunden der Token-Gültigkeit
	 */
	get expirationSeconds(): number;

	/**
	 * Gibt den Ablaufzeitpunkt als Date-Objekt zurück. Liegt kein Token vor, so wird null zurückgegeben.
	 *
	 * @returns der Ablaufzeitpunkt als Date oder null
	 */
	get expiresAt(): Date | null;

	/** Gibt den Status zurück, ob aktuell ein Benutzer authentifiziert ist */
	get authenticated(): boolean;

	/** Gibt den Benutzernamen zurück, der aktuell authentifiziert ist oder sich aktuell im Login-Vorgang befindet **/
	get username(): string;

	/** Gibt an, ob gerade eine Passwort-Änderung stattfindet. */
	get pendingPasswordChange(): boolean;

	/** Gibt das generierte Passwort zurück, wenn gerade eine Passwort-Änderung stattfindet. */
	get generatedPassword(): string | null;

	/** Gibt an, ob der Passwort-Login erfolgreich war, aber der zweite Faktor noch geprüft werden muss */
	get pending2FA(): boolean;

	/** Liefert im Falle einer Erstanmeldung die Setup-Daten für den QR-Code bei TOTP */
	get totpSetup(): { secret: string, issuer: string, account: string } | null;

	/** Gibt den Anmeldezustand zurück */
	get message(): string | null;

	/**
	 * Gibt die Version und den Git-Hash des Servers zurück
	 *
	 * @returns die Version und der Git-Hash des Servers
	 */
	checkVersion(): Promise<{ version: string, githash: string | null } | null>;

	/**
	 * Startet den Login-Vorgang mit dem angebenen Benutzernamen und Kennwort. Im Rahmen einer
	 * Zwei-Faktor-Authentifzierung ist dies die Prüfung des ersten Faktors.
	 *
	 * @param username   der Benutzername
	 * @param password   das Kennwort
	 *
	 * @returns eine Promise bezüglich des Login-Erfolgs
	 */
	login(username: string, password: string): Promise<boolean>;

	/**
	 * Bestätigt die Änderung des Kennwortes, sofern ein neue Kennwort vom Server bereitsteht.
	 *
	 * @returns eine Promise bezüglich des Erfolges beim Bestätigen des kennwortes
	 */
	confirmPasswordChange(): Promise<boolean>;

	/**
	 * Prüft den übergebenen TOTP-Token, um den Login-Vorgang bei einer Zwei-Faktor-Authentifizierung abzuschließen.
	 *
	 * @param code   der TOTP-Token
	 *
	 * @returns true im Erfolgsfall
	 */
	verifyTotp(code: string): Promise<boolean>;

	/**
	 * Meldet den angemeldeten Benutzer bei der Api ab.
	 */
	logout(silent?: boolean): Promise<void>;
}

export const AuthStateKey: InjectionKey<AuthState> = Symbol('AuthState');

export function useAuthState(): AuthState {
	const auth = inject(AuthStateKey);
	if (auth === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des AuthState über provide in der main.ts eingebunden.");
	}
	return auth;
}
