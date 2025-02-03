// Deaktiviert die Überprüfung von TLS-Zertifikaten. Dies sollte nur in Entwicklungsumgebungen verwendet werden.
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

class ApiService {
	private baseUrl: string;
	private token: string;
	private xmlData: string;

	constructor(username: string, password: string) {
		// Setzt die Basis-URL für die API-Anfragen. Standardmäßig wird "https://localhost" verwendet, wenn keine Umgebungsvariable gesetzt ist.
		const targetHost : string = process.env.VITE_targetHost ?? "https://localhost";

		this.baseUrl = targetHost;
		// Kodiert den Benutzernamen und das Passwort in Base64 für die HTTP-Basic-Authentifizierung.
		const tmp = (new TextEncoder()).encode(username + ":" + password);
		this.token = btoa(String.fromCodePoint(...tmp));
		// XML-Daten für PROPFIND-Anfragen.
		this.xmlData = `<?xml version="1.0" encoding="utf-8" ?><propfind xmlns="DAV:"><prop></prop></propfind>`;
	}

	private async request(endpoint: string, options: RequestInit = {}) {
		// Berechnet die Länge des Inhalts, wenn ein Body vorhanden ist.
		let contentLength = 0
		if (options.body !== null) {
			contentLength = new TextEncoder().encode(options.body as string).length;
		}

		try {
			// Führt die HTTP-Anfrage durch und fügt die notwendigen Header hinzu.
			const response = await fetch(`${this.baseUrl}${endpoint}`, {
				...options,
				headers: {
					...options.headers,
					'accept': '*/*',
					'authorization': `Basic ${this.token}`,
					'content-type': 'application/xml',
					'content-length': `${contentLength}`
				},
				body: options.body,
			});
			return response
		} catch (e) {
			// Fehlerbehandlung für fehlgeschlagene Anfragen.
			console.log("Request fehlgeschlagen: ", e)
		}
	}

	// Methode für PROPFIND-Anfragen.
	public async propfind(endpoint: string, options: RequestInit = {}) {
		return this.request(endpoint, {method: 'PROPFIND', body: options.body ?? this.xmlData, headers: options.headers});
	}

	// Methode für REPORT-Anfragen.
	public async report(endpoint: string, options: RequestInit = {}) {
		return this.request(endpoint, {method: 'REPORT', body: options.body ?? this.xmlData});
	}

	// Methode für PUT-Anfragen.
	public async put(endpoint: string, options: RequestInit = {}) {
		return this.request(endpoint, {method: 'PUT', body: this.xmlData, headers: options.headers});
	}

	// Methode für DELETE-Anfragen.
	public async delete(endpoint: string, options: RequestInit = {}) {
		return this.request(endpoint, {method: 'DELETE', body: this.xmlData, headers: options.headers});
	}
}

// Funktion zum Erstellen einer neuen Instanz von ApiService.
export function getApiService(username: string, password: string) {
	return new ApiService(username, password)
}
