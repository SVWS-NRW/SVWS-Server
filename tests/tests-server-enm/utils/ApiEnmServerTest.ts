import { OpenApiError } from "@core/api/OpenApiError";
import type { ENMv2Schueler } from "@core/core/data/enm/v2/ENMv2Schueler";
import { ApiEnmServer, type ApiLoginData } from "@enm/ApiEnmServer";
import { assert, expect } from "vitest";

export class ApiEnmServerTest extends ApiEnmServer {

	async testLogin(): Promise<ApiLoginData> {
		return await this.login();
	}

	static async testErrorStatus(apiMethod: () => Promise<any>, status: number) {
		try {
			await apiMethod();
		} catch (e) {
			assert.instanceOf(e, OpenApiError, 'e is an instance of OpenApiError');
			expect(e.response?.status).toBe(status);
			return;
		}
		assert.fail("api method should fail");
	}

	async testLadeSchueler(id: number): Promise<ENMv2Schueler> {
		const daten = await this.getLehrerENMDaten();
		for (const schueler of daten.schueler) {
			if (schueler.id === id) {
				return schueler;
			}
		}
		throw new Error("Schüler nicht gefunden");
	}

	async testEmptyPost(path: string): Promise<Response> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers["Content-Type"] = "*/*";
		requestInit.headers.Accept = "*/*";
		requestInit.body = null;
		requestInit.method = 'POST';
		return await fetch(this.getURL(path), requestInit);
	}

	async testGet(path: string): Promise<Response> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers.Accept = "*/*";
		requestInit.body = null;
		requestInit.method = 'GET';
		return await fetch(this.getURL(path), requestInit);
	}

	async testEmptyPut(path: string): Promise<Response> {
		const requestInit: RequestInit = { ...this.requestinit };
		requestInit.headers = { ...this.headers };
		requestInit.headers["Content-Type"] = "*/*";
		requestInit.body = "";
		requestInit.method = 'PUT';
		return await fetch(this.getURL(path), requestInit);
	}

}
