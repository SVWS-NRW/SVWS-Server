import type { Schulform } from '@core/asd/types/schule/Schulform';
import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
import type { ServerMode } from '@core/core/types/ServerMode';
import { inject, type InjectionKey } from 'vue';

export interface AuthState {

	/** Gibt die Version der Anwendung zurück */
	get version(): string;

	/** Gibt den Githash des aktuellen Commits der Anwendung zurück */
	get githash(): string;

	/** Gibt den Modus zurück, in welchem der Server betrieben wird. */
	get mode(): ServerMode;

	/** Gibt die Schulform der Schule zurück. */
	get schulform(): Schulform;

	/** Gibt den Hostnamen zurück, mit welchem die Server-Verbindung aufgebaut wurde */
	get hostname(): string;

	/**
	 * Setzt den Hostnamen, der für die Verbindung verwendet wird.
	 *
	 * @param hostname    der Hostname
	 */
	setHostname(hostname: string): void;

	/** Gibt den Status zurück, ob aktuell ein Benutzer authentifiziert ist */
	get authenticated(): boolean;

	/** Gibt den Benutzernamen zurück, der aktuell authentifiziert ist oder sich aktuell im Login-Vorgang befindet **/
	get username(): string;

	/** Gibt an, ob der Passwort-Login erfolgreich war, aber der zweite Faktor noch geprüft werden muss */
	get pending2FA(): boolean;

	/** Liefert im Falle einer Erstanmeldung die Setup-Daten für den QR-Code bei TOTP */
	get totpSetup(): { secret: string, issuer: string, account: string } | null;


	/**
	 * Versucht eine Verbindung zu einem Server bei der angegebenen Adresse herzustellen.
	 *
	 * @param adresse   die Adresse bestehend aus Hostnamen und ggf. Port des SVWS-Servers
	 */
	connectTo(adresse: string): Promise<void>;

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
	logout(): Promise<void>;
}

export const AuthStateKey: InjectionKey<AuthState> = Symbol('AuthState');

export function useAuthState(): AuthState {
	const auth = inject(AuthStateKey);
	if (auth === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des AuthState über provide in der main.ts eingebunden.");
	}
	return auth;
}
