class ApiService {
	private baseUrl: string;
	private token: string;

	/**
	 * Erstellt eine neue Instanz von ApiService.
	 *
	 * @param {string} username - Der Benutzername für die Authentifizierung.
	 * @param {string} password - Das Passwort für die Authentifizierung.
	 * @param {string} baseUrl - Die Basis-URL für die API.
	 */
	constructor(username: string, password: string, baseUrl: string) {
		// Kodiert den Benutzernamen und das Passwort in Base64 für die HTTP-Basic-Authentifizierung.
		const tmp = new TextEncoder().encode(`${username}:${password}`);
		this.token = btoa(String.fromCodePoint(...tmp));
		this.baseUrl = baseUrl;

		process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

	}

	/**
	 * Führt eine HTTP-Anfrage an den angegebenen Endpunkt durch.
	 *
	 * @param {string} endpoint - Der API-Endpunkt.
	 * @param {RequestInit} [options={}] - Die Optionen für die Anfrage.
	 * @returns {Promise<Response>} - Die Antwort der Anfrage.
	 */
	private async request(endpoint: string, options: RequestInit = {}): Promise<Response> {
		// Berechnet die Länge des Inhalts, wenn ein Body vorhanden ist.
		let contentLength = 0
		if (options.body !== null) {
			contentLength = new TextEncoder().encode(options.body as string).length;
		}

		const response = await fetch(`${this.baseUrl}${endpoint}`, {
			...options,
			headers: {
				...options.headers,
				...(contentLength > 0) && {'content-length': String(contentLength)},
				'accept': '*/*',
				'authorization': `Basic ${this.token}`,
			},
			...(options.body !== undefined) && {body: options.body},
		})
		return response
	}

	/**
	 * Führt eine GET-Anfrage an den angegebenen Endpunkt durch.
	 *
	 * @param {string} endpoint - Der API-Endpunkt.
	 * @param {RequestInit} [options={}] - Die Optionen für die Anfrage.
	 * @returns {Promise<Response>} - Die Antwort der Anfrage.
	 */
	public async get(endpoint: string, options: RequestInit = {}): Promise<Response> {
		return this.request(endpoint, {method: 'GET', headers: options.headers});
	}

	/**
	 * Führt eine POST-Anfrage an den angegebenen Endpunkt durch.
	 *
	 * @param {string} endpoint - Der API-Endpunkt.
	 * @param {RequestInit} [options={}] - Die Optionen für die Anfrage.
	 * @returns {Promise<Response>} - Die Antwort der Anfrage.
	 */
	public async post(endpoint: string, options: RequestInit = {}): Promise<Response> {
		return this.request(endpoint, {method: 'POST', headers: options.headers, body: options.body});
	}

	/**
	 * Führt eine PATCH-Anfrage an den angegebenen Endpunkt durch.
	 *
	 * @param {string} endpoint - Der API-Endpunkt.
	 * @param {RequestInit} [options={}] - Die Optionen für die Anfrage, inklusive dem Patch Object
	 * @returns {Promise<Response>} - Die Antwort der Anfrage.
	 */
	public async patch(endpoint: string, options: RequestInit = {}): Promise<Response> {
		return this.request(endpoint, {method: 'PATCH', headers: options.headers, body: options.body});
	}

	/**
	 * Führt eine PUT-Anfrage an den angegebenen Endpunkt durch.
	 *
	 * @param {string} endpoint - Der API-Endpunkt.
	 * @param {RequestInit} [options={}] - Die Optionen für die Anfrage.
	 * @returns {Promise<Response>} - Die Antwort der Anfrage.
	 */
	public async put(endpoint: string, options: RequestInit = {}): Promise<Response> {
		return this.request(endpoint, {method: 'PUT', headers: options.headers, body: options.body});
	}

	/**
	 * Führt eine DELETE-Anfrage an den angegebenen Endpunkt durch.
	 *
	 * @param {string} endpoint - Der API-Endpunkt.
	 * @param {RequestInit} [options={}] - Die Optionen für die Anfrage.
	 * @returns {Promise<Response>} - Die Antwort der Anfrage.
	 */
	public async delete(endpoint: string, options: RequestInit = {}): Promise<Response> {
		return this.request(endpoint, {method: 'DELETE', headers: options.headers});
	}
}

/**
 * Funktion zum Erstellen einer neuen Instanz von ApiService.
 *
 * @param {string} username - Der Benutzername für die Authentifizierung.
 * @param {string} password - Das Passwort für die Authentifizierung.
 * @param {string} baseUrl - Die Basis-URL für die API.
 * @returns {ApiService} - Eine neue Instanz von ApiService.
 */
export function getApiService(username: string, password: string, baseUrl: string): ApiService {
	return new ApiService(username, password, baseUrl);
}
