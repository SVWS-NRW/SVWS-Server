import { ApiServer } from "../../svws-webclient/core/src/api/ApiServer";
import { OpenApiError } from "../../svws-webclient/core/src/api/OpenApiError";
import { loadConfig } from "./ConfigUtils";

export interface Result<T> {
	content?: T,
	error?: Error,
	response?: Response | null
}

const { localTestRunFrontendURL, localTestRunBackendURL, localTestRunDBSchema, localTestRunENMURL } = await loadConfig();

export const backendURL: string = process.env.VITE_targetHost ?? localTestRunBackendURL;

export const enmURL: string = process.env.VITE_ENM_targetHost ?? localTestRunENMURL;

export const frontendURL: string = process.env.VITE_targetHost ?? `${localTestRunFrontendURL}/#/${localTestRunDBSchema}`;

export const adminFrontendURL: string = process.env.VITE_targetHost !== undefined ? `${process.env.VITE_targetHost}/admin` : `${localTestRunFrontendURL}/admin`;

/**
 * Liefert eine neue Instanz vom Typ {@link ApiServer}, mit dem die API des SVWS-Servers genutzt werden kann.
 *
 * @return neue ApiServer Instanz
 */
export const privilegedApiServer: ApiServer = new ApiServer(backendURL, "Admin", "");

/**
 * Methode kümmert sich um das Aufbereiten der Response bzw. des Errors, der von der API zurückgegeben wird.
 *
 * @param apiCtx ApiServer Context
 * @param method API Methode aus {@link ApiServer}, die aufgerufen werden soll
 * @param methodArgs Argumente für die API Methode, die aufgerufen werden soll
 *
 * @return Liefert ein {@link Result} Objekt zurück.
 */
export async function handleRequest <A extends any[], R>(apiCtx: ApiServer, method: (...args: A) => Promise<R>, ...methodArgs: A): Promise<Result<R>> {
	try {
		return { content: (await method.call(apiCtx, ...methodArgs)) }
	} catch(e) {
		if (e instanceof Error)
			return { error: e, response: e instanceof OpenApiError ? e.response : null }
		else
			return { }
	}
}
