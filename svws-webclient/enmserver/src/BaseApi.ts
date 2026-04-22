import { OpenApiError } from "@core/api/OpenApiError";
import { UserNotificationException } from "@core/core/exceptions/UserNotificationException";

export class BaseApi {

	/**
	 * Ein Handler für die Reaktion auf Antworten mit 401 - Unauthorized
	 *
	 * @returns true, wenn ein Auto-Logout stattgefunden hat
	 */
	private _onUnauthorized: (() => Promise<boolean>) | null = null;

	/**
	 * Setzt den globalen Handler für Antworten mit 401 - Unauthorized. Dieser kann z.B. für ein automatisches Logout
	 * verwendet werden.
	 *
	 * @param handler   der Handler zur Reaktion auf 401 - Unauthorized -Antworten
	 */
	public set onUnauthorized(handler: () => Promise<boolean>) {
		this._onUnauthorized = handler;
	}

	/** Die URL des Servers. Alle Pfadangaben sind relativ zu dieser URL. */
	protected url: string;

	/** Der Anmeldename beim Server */
	protected username: string;

	/** Der Default-RequestInit für einen Fetch */
	protected requestinit: RequestInit = {
		cache: 'no-cache',
		credentials: 'same-origin',
	};

	/** Die Default-Header-Einträge */
	protected headers: Record<string, string> = {};

	/**
	 * Erstellt eine neue API mit der übergebenen Konfiguration.
	 *
	 * @param {string} url - die URL des Servers: Alle Pfadangaben sind relativ zu dieser URL
	 * @param {string} username - der Benutzername für den API-Zugriff
	 * @param {string} password - das Kennwort des Benutzers für den API-Zugriff
	 */
	public constructor(url: string, username: string, password: string) {
		this.url = url;
		this.username = username;
		this.setBasicAuth(password);
	}


	/**
	 * Erlaubt das Umstellen der Authentifizierung auf Basic-Auth
	 *
	 * @param password   das Kennwort
	 */
	public setBasicAuth(password: string): void {
		const tmp = (new TextEncoder()).encode(this.username + ":" + password);
		this.headers.Authorization = "Basic " + btoa(String.fromCodePoint(...tmp));
	}


	/**
	 * Erlaubt das Umstellen der Authentifizierung auf Json-Web-Tokens
	 *
	 * @param token   das Token
	 */
	public setBearerToken(token: string): void {
		this.headers.Authorization = `Bearer ${token}`;
	}


	protected getURL(path: string): string {
		return this.url + path;
	}


	/**
	 * Hilfsmethode, um alle API-Aufrufe ähnlich zu handhaben.
	 *
	 * @param path   die URL für den API-Aufruf
	 * @param init   die Informaationen für den Request
	 *
	 * @returns die Response des Fetch
	 */
	private async doFetch(path: string, init: RequestInit): Promise<Response> {
		try {
			// Führe das Fetch aus
			const response = await fetch(this.getURL(path), init);

			// Bei einem 401 - Unauthorized wird der globaler Handler aufgerufen, sofern einer festgelegt wurde
			if ((response.status === 401) && (this._onUnauthorized !== null)) {
				const isAutoLogout = await this._onUnauthorized();
				if (isAutoLogout) {
					throw new UserNotificationException("Die Sitzung ist abgelaufen und der Benutzer wurde automatisch abgemeldet.");
				}
			}

			// Prüft, ob die Antwort nicht OK ist, d.h. der Status-Code nicht im Bereich 200 - 299 liegt.
			if (!response.ok) {
				throw new OpenApiError(response, `Fetch failed for ${init.method}: ${path}`);
			}

			return response;
		} catch (e) {
			if (e instanceof Error) {
				throw (e instanceof OpenApiError) || (e instanceof UserNotificationException) ? e : new OpenApiError(e, `Fetch failed for ${init.method}: ${path}`);
			}
			throw new Error("Unexpected Error: " + String(e), { cause: e });
		}
	}


	protected async getTextBased(path: string, mimetype: string): Promise<{ status: number, data: string }> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers.Accept = mimetype;
		requestInit.body = null;
		requestInit.method = 'GET';

		const response = await this.doFetch(path, requestInit);
		return { status: response.status, data: await response.text() };
	}

	protected async postTextBased(path: string, mimetype_send: string, mimetype_receive: string, body: string | null): Promise<{ status: number, data: string }> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers["Content-Type"] = mimetype_send;
		requestInit.headers.Accept = mimetype_receive;
		requestInit.body = body;
		requestInit.method = 'POST';
		const response = await this.doFetch(path, requestInit);
		return { status: response.status, data: await response.text() };
	}


	protected async postJson(path: string, body: any): Promise<{ status: number, data: string }> {
		return await this.postTextBased(path, 'application/json', 'application/json', JSON.stringify(body));
	}

	protected async putTextBased(path: string, mimetype: string, body: string): Promise<{ status: number }> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers["Content-Type"] = mimetype;
		requestInit.body = body;
		requestInit.method = 'PUT';
		const response = await this.doFetch(path, requestInit);
		return { status: response.status };
	}

}
