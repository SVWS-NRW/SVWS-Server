import { OpenApiError } from '../api/OpenApiError';

export interface ApiFile {
	name: string,
	data: Blob,
}

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
		const tmp = (new TextEncoder()).encode(username + ":" + password);
		if (username !== '') {
			this.headers.Authorization = "Basic " + btoa(String.fromCodePoint(...tmp));
		}
	}


	protected getURL(path: string): string {
		return this.url + path;
	}

	protected decodeFilename(header: string): string {
		// prüfe, ob filename vorhanden ist im Header und ermittel `filename`. Ebenso `filenameUTF8`
		const nameRegex = /(.*filename="(?<filename>.*)")?(.*filename\*=UTF-8''(?<filenameUTF8>.*))?/;
		const match = nameRegex.exec(header);
		if (match !== null) {
			const { filename, filenameUTF8 } = match.groups as { filename?: string; filenameUTF8?: string };
			if (filenameUTF8 !== undefined) {
				return decodeURIComponent(filenameUTF8);
			}
			if (filename !== undefined) {
				return decodeURIComponent(filename);
			}
		}
		throw new Error('Failed to extract file name from Header');
	}

	protected async getBinary(path: string, mimetype: string): Promise<ApiFile> {
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
			const file: ApiFile = { name: "", data: await response.blob() };
			const header = response.headers.get('content-disposition');
			if (header !== null) {
				file.name = this.decodeFilename(header);
			}
			return file;
		} catch (e) {
			if (e instanceof Error) {
				throw (e instanceof OpenApiError) ? e : new OpenApiError(e, 'Fetch failed for GET: ' + path);
			}
			throw new Error("GET failed for: " + path, { cause: e });
		}
	}


	public getPDF(path: string): Promise<ApiFile> {
		return this.getBinary(path, 'application/pdf');
	}


	public getSQLite(path: string): Promise<ApiFile> {
		return this.getBinary(path, 'application/vnd.sqlite3');
	}


	public getZip(path: string): Promise<ApiFile> {
		return this.getBinary(path, 'application/zip');
	}


	public getOctetStream(path: string): Promise<ApiFile> {
		return this.getBinary(path, 'application/octet-stream');
	}


	protected async postMultipartBased(path: string, body: FormData | null): Promise<string> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
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
			throw new Error("POST failed for: " + path, { cause: e });
		}
	}


	public postMultipart(path: string, body: FormData | null): Promise<string> {
		return this.postMultipartBased(path, body);
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
			throw new Error("POST failed for: " + path, { cause: e });
		}
	}

	public getText(path: string): Promise<string> {
		return this.getTextBased(path, 'text/plain');
	}

	public getJSON(path: string): Promise<string> {
		return this.getTextBased(path, 'application/json');
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
			throw new Error("POST failed for: " + path, { cause: e });
		}
	}

	public postText(path: string, body: string | null): Promise<string> {
		return this.postTextBased(path, 'text/plain', 'text/plain', body);
	}

	public postJSON(path: string, body: string | null): Promise<string> {
		return this.postTextBased(path, 'application/json', 'application/json', body);
	}

	protected async postBinaryToTextBased(path: string, mimetype_send: string, mimetype_receive: string, body: ApiFile): Promise<string> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers["Content-Type"] = mimetype_send;
		requestInit.headers.Accept = mimetype_receive;
		requestInit.body = body.data;
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
			throw new Error("POST failed for: " + path, { cause: e });
		}
	}

	public async postOctetStreamToJSON(path: string, body: ApiFile): Promise<string> {
		return this.postBinaryToTextBased(path, 'application/octet-stream', 'application/json', body);
	}

	protected async postTextBasedToBinary(path: string, mimetype_send: string, mimetype_receive: string, body: string | null): Promise<ApiFile> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers["Content-Type"] = mimetype_send;
		requestInit.headers.Accept = mimetype_receive;
		requestInit.body = body;
		requestInit.method = 'POST';
		try {
			const response = await fetch(this.getURL(path), requestInit);
			if (!response.ok) {
				throw new OpenApiError(response, 'Fetch failed for GET: ' + path);
			}
			const file: ApiFile = { name: "", data: await response.blob() };
			const header = response.headers.get('content-disposition');
			if (header !== null) {
				file.name = this.decodeFilename(header);
			}
			return file;
		} catch (e) {
			if (e instanceof Error) {
				throw (e instanceof OpenApiError) ? e : new OpenApiError(e, 'Fetch failed for POST: ' + path);
			}
			throw new Error("POST failed for: " + path, { cause: e });
		}
	}

	public async postJSONtoOctetStream(path: string, body: string | null): Promise<ApiFile> {
		return this.postTextBasedToBinary(path, 'application/json', 'application/octet-stream', body);
	}

	public async postJSONtoPDF(path: string, body: string | null): Promise<ApiFile> {
		return this.postTextBasedToBinary(path, 'application/json', 'application/pdf', body);
	}

	public async postJSONtoZIP(path: string, body: string | null): Promise<ApiFile> {
		return this.postTextBasedToBinary(path, 'application/json', 'application/zip', body);
	}

	protected async patchTextBased(path: string, mimetype_send: string, mimetype_receive: string | null, body: string): Promise<string> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers["Content-Type"] = mimetype_send;
		if (mimetype_receive !== null) {
			requestInit.headers.Accept = mimetype_receive;
		}
		requestInit.body = body;
		requestInit.method = 'PATCH';
		try {
			const response = await fetch(this.getURL(path), requestInit);
			if (!response.ok) {
				throw new OpenApiError(response, 'Fetch failed for PATCH: ' + path);
			}
			return (mimetype_receive === null) ? "" : await response.text();
		} catch (e) {
			if (e instanceof Error) {
				throw (e instanceof OpenApiError) ? e : new OpenApiError(e, 'Fetch failed for PATCH: ' + path);
			}
			throw new Error("PATCH failed for: " + path, { cause: e });
		}
	}

	public async patchText(path: string, body: string): Promise<void> {
		await this.patchTextBased(path, 'text/plain', null, body);
	}

	public async patchJSON(path: string, body: string): Promise<void> {
		await this.patchTextBased(path, 'application/json', null, body);
	}

	public async patchJSONWithResponse(path: string, body: string): Promise<string> {
		return await this.patchTextBased(path, 'application/json', 'application/json', body);
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
			throw new Error("PUT failed for: " + path, { cause: e });
		}
	}

	public putText(path: string, body: string): Promise<void> {
		return this.putTextBased(path, 'text/plain', body);
	}

	public putJSON(path: string, body: string): Promise<void> {
		return this.putTextBased(path, 'application/json', body);
	}

	protected async deleteTextBased(path: string, mimetype_send: string | null, mimetype_receive: string, body: string | null): Promise<string> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		if (mimetype_send !== null) {
			requestInit.headers["Content-Type"] = mimetype_send;
		}
		requestInit.headers.Accept = mimetype_receive;
		requestInit.body = body;
		requestInit.method = 'DELETE';
		try {
			const response = await fetch(this.getURL(path), requestInit);
			if (!response.ok) {
				throw new OpenApiError(response, 'Fetch failed for DELETE: ' + path);
			}
			return await response.text();
		} catch (e) {
			if (e instanceof Error) {
				throw (e instanceof OpenApiError) ? e : new OpenApiError(e, 'Fetch failed for DELETE: ' + path);
			}
			throw new Error("DELETE failed for: " + path, { cause: e });
		}
	}

	public deleteText(path: string, body: string | null): Promise<string> {
		return this.deleteTextBased(path, (body === null) ? null : 'text/plain', 'text/plain', body);
	}

	public deleteJSON(path: string, body: string | null): Promise<string> {
		return this.deleteTextBased(path, (body === null) ? null : 'application/json', 'application/json', body);
	}

}
