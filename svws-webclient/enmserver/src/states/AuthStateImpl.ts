import { ref, shallowRef } from "vue";
import type { AuthState } from "./AuthState";
import { ApiEnmServer } from "@wenom/ApiEnmServer";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { UserNotificationException } from "@core/core/exceptions/UserNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import type { Schulform } from "@core/asd/types/schule/Schulform";
import { version } from "@version";
import { githash } from "@githash";
import { OpenApiError } from "@core/api/OpenApiError";
import { activityStateImpl } from "./ActivityStateImpl";
import { RouteManager } from "@wenom/router/RouteManager";

/**
 * Der Paylod from JWT-Token
 */
interface JWTPayload {

	// Die Lehrer-ID
	sub: number;

	// Der Ablaufzeitpunkt als Unix-Timestamp
	exp: number;

	// Der Erstellungszeitpunkt als Unix-Timestamp
	iat: number;

}


/**
 * Die Implementierung für den Status der Authentifizierung des WeNoM-Clients
 */
class AuthStateImpl implements AuthState {

	// Gibt an, ob der Client beim Server authentifiziert ist
	private readonly _authenticated = ref<boolean>(false);

	// Gibt an, ob der Client sich momentan in der Phase der Abmeldung befindet
	private _isLoggingOut = false;

	// Gibt an, ob gerade eine Passwort-Änderung stattfindet
	private readonly _pendingPasswordChange = ref<boolean>(false);

	// Gibt das generierte Passwort zurück, wenn gerade eine Passwort-Änderung stattfindet
	private readonly _generatedPassword = ref<string | null>(null);

	// Gibt an, ob die Authentifizierung mit einem ersten Faktor erfolgreich war, aber noch auf einen zweiten Faktor gewartet wird.
	private readonly _pending2FA = ref<boolean>(false);

	// Der aktuell gültige Token
	private readonly _token = ref<string | null>(null);

	// Bei einer Erstanmeldung die Informationen für den QR-Code für TOTP und ansonsten null
	private readonly _totpSetup = shallowRef<{ secret: string, issuer: string, account: string } | null>(null);

	// Der Benutzername für den Login
	private _username = "";

	// Die Api selbst
	private _api: ApiEnmServer | undefined;

	// Der Modus, in welchem der Server betrieben wird
	private readonly _serverMode = shallowRef<ServerMode>(ServerMode.STABLE);

	// Die Schulform, für welche der Server Daten hat
	private readonly _schulform = shallowRef<Schulform | null>(null);

	// ein Timeout für Refreshes
	private _timerID: ReturnType<typeof setTimeout> | null = null;

	// eine Nachricht über den Anmeldezustand
	private readonly _message = ref<string | null>(null);


	/** Gibt die Version der Anwendung zurück */
	public get version(): string {
		return version;
	}

	/** Gibt den Githash des aktuellen Commits der Anwendung zurück */
	public get githash(): string {
		return githash;
	}

	/**
	 * Gibt das aktuelle Access-Token zurück
	 */
	public get token(): string | null {
		return this._token.value;
	}

	/** Gibt die Klassen für den Zugriff auf die Server-API zurück. */
	public get api(): ApiEnmServer {
		if (this._api === undefined) {
			throw new DeveloperNotificationException("Es wurde noch kein API-Objekt angelegt - Verbindungen zum Server sind (noch) nicht möglich");
		}
		return this._api;
	}

	/** Gibt den Modus zurück, in welchem der Server betrieben wird. */
	public get mode(): ServerMode {
		return this._serverMode.value;
	}

	/** Gibt die Schulform der Schule zurück, wo der Benutzer angemeldet ist. */
	public get schulform(): Schulform {
		if (this._schulform.value === null) {
			throw new UserNotificationException("Die Schulform des Servers konnte nicht bestimmt werden.");
		}
		return this._schulform.value;
	}

	/** Gibt den Status zurück, ob aktuell ein Benutzer authentifiziert ist */
	public get authenticated(): boolean {
		return this._authenticated.value;
	}

	/** Gibt an, ob gerade eine Passwort-Änderung stattfindet. */
	public get pendingPasswordChange(): boolean {
		return this._pendingPasswordChange.value;
	}

	/** Gibt das generierte Passwort zurück, wenn gerade eine Passwort-Änderung stattfindet. */
	public get generatedPassword(): string | null {
		return this._generatedPassword.value;
	}

	/** Gibt an, ob der Passwort-Login erfolgreich war, aber der zweite Faktor noch geprüft werden muss */
	public get pending2FA(): boolean {
		return this._pending2FA.value;
	}

	/** Liefert im Falle einer Erstanmeldung die Setup-Daten für den QR-Code bei TOTP */
	public get totpSetup(): { secret: string, issuer: string, account: string } | null {
		return this._totpSetup.value;
	}

	/** Gibt den Benutzernamen zurück, der aktuell authentifiziert ist oder sich aktuell im Login-Vorgang befindet **/
	public get username(): string {
		return this._username;
	}

	/**
	 * Gibt zurück, ob ein administrativer Benutzer angemeldet ist oder nicht.
	 */
	// eslint-disable-next-line @typescript-eslint/class-literal-property-style
	public get istAdmin(): boolean {
		return false;
	}

	/**
	 * Gibt den Anmeldezustand zurück
	 */
	public get message(): string | null {
		return this._message.value;
	}

	/**
	 * Bestimmt den Payload den JWT-Access-Tokens
	 *
	 * @returns die Payload des Access-Tokens
	 */
	private getTokenPayload(): JWTPayload | null {
		if (this._token.value === null) {
			return null;
		}
		try {
			const base64Url = this._token.value.split('.')[1];
			const base64 = base64Url.replaceAll('-', '+').replaceAll('_', '/');
			const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
				const cp = c.codePointAt(0);
				const hex = (cp === undefined) ? '00' : cp.toString(16).padStart(2, '0');
				return '%' + hex;
			}).join(''));
			return JSON.parse(jsonPayload) as JWTPayload;
		} catch {
			return null;
		}
	}


	/**
	 * Gibt die verbleibende Zeit in Sekunden für die Gültigkeit des Tokens zurück.
	 * Existiert kein Token so wird 0 zurückgegeben.
	 *
	 * @returns die verbleibenden Sekunden der Token-Gültigkeit
	 */
	public get expirationSeconds(): number {
		const payload = this.getTokenPayload();
		if (!payload) {
			return 0;
		}
		const now = Math.floor(Date.now() / 1000);
		return Math.max(0, payload.exp - now);
	}

	/**
	 * Gibt den Ablaufzeitpunkt als Date-Objekt zurück. Liegt kein Token vor, so wird null zurückgegeben.
	 *
	 * @returns der Ablaufzeitpunkt als Date oder null
	 */
	public get expiresAt(): Date | null {
		const payload = this.getTokenPayload();
		return payload ? new Date(payload.exp * 1000) : null;
	}

	/**
	 * Gibt die Version und den Git-Hash des Servers zurück
	 *
	 * @returns die Version und der Git-Hash des Servers
	 */
	public async checkVersion(): Promise<{ version: string, githash: string | null } | null> {
		try {
			const api = new ApiEnmServer();
			return await api.getVersion();
		} catch {
			return null;
		}
	}

	/**
	 * Authentifiziert den Benutzer mit dem angebenen Benutzernamen und Kennwort bei dem
	 * angebenen Schema.
	 *
	 * @param username   der Benutzername
	 * @param password   das Kennwort
	 *
	 * @returns eine Promise bezüglich des Login-Erfolgs
	 */
	public async login(username: string, password: string): Promise<boolean> {
		try {
			this._message.value = null;
			this._username = username;
			this._api = new ApiEnmServer(this._username, password);
			this._api.onUnauthorized = async () => {
				if (!this.authenticated) {
					return false;
				}
				await this.logout();
				return true;
			};
			const result = await this._api.login();
			await this.receivedAccessToken(result.token);

			// Wenn die Änderung eines Initial-Kennwortes gefordert wird ...
			if (result.isChangePassword) {
				this._pendingPasswordChange.value = true;
				const payload = this.getTokenPayload() as JWTPayload & { pwd: string };
				this._generatedPassword.value = payload.pwd;
				this._authenticated.value = false;
				return true;
			}

			// Wenn 2FA aktiv ist, dann muss dieser noch geprüft werden ...
			if (result.isTotp) {
				this._pending2FA.value = true;
				this._totpSetup.value = result.setup;
				this._authenticated.value = false;
				return true;
			}

			// ... und wenn nicht, dann kann der Login abgeschlossen werden
			await this.finalizeLogin();
			return true;
		} catch (e) {
			await this.logout();
			if ((e instanceof OpenApiError) && (e.response?.status !== 401)) {
				let message = await e.response?.text() ?? "";
				if ((message === "") && import.meta.env.DEV) {
					message = `Der Server ist nicht erreichbar. Haben Sie ihn gestartet und wenn ja, ist WeNoM die Adresse mit Port bekannt? `
					+ (import.meta.env.VITE_PHP_API_URL === undefined ? `Wenn der PHP-Server läuft, aber nicht unter der gleichen Adresse wie der Client erreichbar ist, müssen Sie eine .env.local-Datei anlegen mit dem Wert: VITE_PHP_API_URL=https://serveradresse:port`
						: `Es ist in einer .env-Datei die Server-Adresse ${import.meta.env.VITE_PHP_API_URL} angegeben. Bitte überprüfen Sie, ob der Server unter dieser Adresse erreichbar ist.`);
				}
				this._message.value = `Anmeldung fehlgeschlagen: ${message}`;
				return false;
			}
			this._message.value = "Benutzername oder Passwort falsch.";
			return false;
		}
	}


	public async confirmPasswordChange(): Promise<boolean> {
		try {
			this._message.value = null;
			const result = await this.api.changePassword();
			await this.receivedAccessToken(result.token);

			this._pendingPasswordChange.value = false;
			this._generatedPassword.value = null;

			// Wenn 2FA aktiv ist, dann muss dieser noch geprüft werden ...
			if (result.isTotp) {
				this._pending2FA.value = true;
				this._totpSetup.value = result.setup;
				this._authenticated.value = false;
				return true;
			}

			// ... und wenn nicht, dann kann der Login abgeschlossen werden
			await this.finalizeLogin();
			return true;
		} catch (e) {
			await this.logout();
			if ((e instanceof OpenApiError) && (e.response?.status === 403)) {
				this._message.value = "Passwortänderung fehlgeschlagen: Das Initialpasswort wurde bereits an anderer Stelle neu gesetzt.";
				return false;
			}
			this._message.value = "Passwortänderung fehlgeschlagen";
			return false;
		}
	}


	/**
	 * Prüft den übergebenen TOTP-Token, um den Login-Vorgang bei einer Zwei-Faktor-Authentifizierung abzuschließen.
	 *
	 * @param code   der TOTP-Token
	 *
	 * @returns true im Erfolgsfall
	 */
	public async verifyTotp(code: string): Promise<boolean> {
		try {
			this._message.value = null;
			const result = await this.api.loginTotp(code);
			await this.receivedAccessToken(result.token);

			await this.finalizeLogin();
			return true;
		} catch (e) {
			await this.logout();
			if ((e instanceof OpenApiError) && (e.response?.status !== 401)) {
				const message = await e.response?.text() ?? "Unbekannter Grund.";
				this._message.value = `Anmeldung fehlgeschlagen: ${message}`;
				return false;
			}
			this._message.value = "Code fehlerhaft";
			return false;
		}
	}


	/**
	 * Finalisiert den Login, sobald alle Faktoren beim Login geprüft wurden.
	 */
	private async finalizeLogin(): Promise<void> {
		this._serverMode.value = await this.api.getServerMode();
		this._schulform.value = await this.api.getSchulform();
		this._authenticated.value = true;
		this._pending2FA.value = false;
		this._totpSetup.value = null;
		await this.updateRefreshTokenTimer();
		activityStateImpl.start(() => this.logout());
	}

	/**
	 * Meldet den angemeldeten Benutzer bei der Api ab.
	 */
	public async logout(silent: boolean = false): Promise<void> {
		if (this._isLoggingOut) {
			return;
		}
		this._isLoggingOut = true;
		this._message.value = null;
		this.stopTimer();
		activityStateImpl.stop();
		if (this._authenticated.value) {
			try {
				await this.api.logout();
			} catch (e: unknown) {
				if (e instanceof UserNotificationException) {
					this._message.value = silent ? null : e.getMessage();
				}
			}
		}
		this._authenticated.value = false;
		this._pending2FA.value = false;
		this._pendingPasswordChange.value = false;
		this._token.value = null;
		this._totpSetup.value = null;
		this._username = "";
		this._api = undefined;
		this._serverMode.value = ServerMode.STABLE;
		this._schulform.value = null;
		await RouteManager.doRoute({ name: 'login' });
		RouteManager.resetRouteState();
		this._isLoggingOut = false;
	}

	private async updateRefreshTokenTimer() {
		const seconds = this.expirationSeconds - 14;
		if (seconds > 0) {
			this.startTimer(seconds);
		} else {
			this.stopTimer();
		}
	}

	private async receivedAccessToken(token: string) {
		this._token.value = token;
	}

	private async doRefreshToken(): Promise<void> {
		const result = await this.api.refreshToken();
		await this.receivedAccessToken(result.token);
		await this.updateRefreshTokenTimer();
	}

	private startTimer(seconds: number): void {
		this.stopTimer();
		this._timerID = setTimeout(() => {
			void this.doRefreshToken();
		}, seconds * 1_000);
	}

	private stopTimer(): void {
		if (this._timerID !== null) {
			clearTimeout(this._timerID);
			this._timerID = null;
		}
	}

}

export const authStateImpl = new AuthStateImpl();
