import { ApiServer, OpenApiError } from "@core";

export interface Result<T> {
	content?: T,
	error?: Error,
	response?: Response | null
}


/**
 * Methode liefert eine neue Instanz vom Typ {@link ApiServer}, mit dem die API des SVWS-Servers genutzt werden kann.
 *
 * @param schema Schema der Testdatenbank
 *
 * @return neue ApiServer Instanz
 */
export function getApiServer(schema: string): ApiServer {

	const targetHost : string = process.env.VITE_targetHost ?? "https://localhost";

	console.log("Target Host for API Server " + targetHost)

	return new ApiServer(targetHost, "Admin", "");
}

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
