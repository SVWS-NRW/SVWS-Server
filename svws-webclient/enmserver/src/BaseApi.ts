import { OpenApiError } from "@core/api/OpenApiError";

export class BaseApi {

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
		if (this.username !== '') {
			this.headers.Authorization = "Basic " + btoa(String.fromCodePoint(...tmp));
		}
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

	protected async getTextBased(path: string, mimetype: string): Promise<string> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers.Accept = mimetype;
		requestInit.body = null;
		requestInit.method = 'GET';
		try {
			const response = await fetch(this.getURL(path), requestInit);
			if (!response.ok) {
				throw new OpenApiError(response, 'Fetch failed for GET: ' + path);
			}
			return await response.text();
		} catch (e) {
			if (e instanceof Error) {
				throw (e instanceof OpenApiError) ? e : new OpenApiError(e, 'Fetch failed for GET: ' + path);
			}
			throw new Error("Unexpected Error: " + String(e), { cause: e });
		}
	}

	protected async postTextBased(path: string, mimetype_send: string, mimetype_receive: string, body: string | null): Promise<string> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers["Content-Type"] = mimetype_send;
		requestInit.headers.Accept = mimetype_receive;
		requestInit.body = body;
		requestInit.method = 'POST';
		try {
			const response = await fetch(this.getURL(path), requestInit);
			if (!response.ok) {
				throw new OpenApiError(response, 'Fetch failed for POST: ' + path);
			}
			return await response.text();
		} catch (e) {
			if (e instanceof Error) {
				throw (e instanceof OpenApiError) ? e : new OpenApiError(e, 'Fetch failed for POST: ' + path);
			}
			throw new Error("Unexpected Error: " + String(e), { cause: e });
		}
	}


	protected async postJson(path: string, body: any): Promise<string> {
		return await this.postTextBased(path, 'application/json', 'application/json', JSON.stringify(body));
	}

	protected async putTextBased(path: string, mimetype: string, body: string): Promise<void> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers["Content-Type"] = mimetype;
		requestInit.body = body;
		requestInit.method = 'PUT';
		try {
			const response = await fetch(this.getURL(path), requestInit);
			if (!response.ok) {
				throw new OpenApiError(response, 'Fetch failed for PUT: ' + path);
			}
			return;
		} catch (e) {
			if (e instanceof Error) {
				throw (e instanceof OpenApiError) ? e : new OpenApiError(e, 'Fetch failed for PUT: ' + path);
			}
			throw new Error("Unexpected Error: " + String(e), { cause: e });
		}
	}

}
