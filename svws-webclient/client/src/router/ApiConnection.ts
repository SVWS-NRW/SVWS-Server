import { ref } from "vue";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import { serverStateImpl } from "~/states/ServerStateImpl";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import { ApiExternal } from "@core/api/ApiExternal";
import { ApiServer } from "@core/api/ApiServer";
import { OpenApiError } from "@core/api/OpenApiError";
import { JsonCoreTypeReader } from "@core/asd/utils/JsonCoreTypeReader";
import type { DBSchemaListeEintrag } from "@core/core/data/db/DBSchemaListeEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { UserNotificationException } from "@core/core/exceptions/UserNotificationException";
import type { List } from "@core/java/util/List";
import type { ConfigState } from "@ui/states/ConfigState";

export class ApiConnection {

	// Der State der Konfiguration
	protected configState: ConfigState = configStateImpl;

	// Die URL mit welcher der Server verbunden ist
	protected _url: string = `https://${globalThis.location.hostname}:${globalThis.location.port}`;

	// Der Name des Schemas auf dem SVWS-Server, bei dem der Login stattfindet
	protected _schema: string | undefined;

	// Die Api selbst
	protected _api: ApiServer | undefined;

	// Die externe Api
	protected _apiExternal: ApiExternal | undefined;

	// Die Map mit den CoreTypeDaten
	protected _mapCoreTypeData = ref<Map<string, any> | undefined>(undefined);


	// Gibt die Server-API zurück.
	get api(): ApiServer {
		if (this._api === undefined) {
			throw new DeveloperNotificationException("Es wurde kein Api-Objekt angelegt - Verbindungen zum Server können nicht erfolgen");
		}
		return this._api;
	}

	// Gibt die External-API zurück.
	get apiExternal(): ApiExternal {
		if (this._apiExternal === undefined) {
			throw new DeveloperNotificationException("Es wurde kein Api-Objekt angelegt - Verbindungen zum Server können nicht erfolgen");
		}
		return this._apiExternal;
	}

	// Gibt das Datenbank-Schema zurück.
	get schema(): string {
		if (this._schema === undefined) {
			throw new DeveloperNotificationException("Es liegt kein DB-Schema für die Api vor");
		}
		return this._schema;
	}

	// gibt die Map mit den CoreType-Daten zurück
	get mapCoreTypeData(): Map<string, string> {
		if (this._mapCoreTypeData.value === undefined) {
			throw new DeveloperNotificationException("Eine Map mit den CoreType-Daten ist nicht vorhanden.");
		}
		return this._mapCoreTypeData.value;
	}

	/**
	 * Versucht eine Verbindung zu dem SVWS-Server mit dem angegebenen Hostnamen aufzubauen.
	 *
	 * @param {string} name Der Hostname unter der der SVWS-Server erreichbar sein soll
	 *
	 * @returns {Promise<List<DBSchemaListeEintrag>>}
	 */
	connectTo = async (): Promise<List<DBSchemaListeEintrag>> => {
		try {
			const api = new ApiServer(this._url, "", "");

			// Lese die Informationen zu den DB-Schemata ein
			const schemata = await api.getConfigDBSchemata();

			// Lese die Daten für die Initialisierung der Core-Types ein
			const reader = new JsonCoreTypeReader(this._url);
			await reader.loadAll();
			reader.readAll();
			this._mapCoreTypeData.value = reader.mapCoreTypeData;

			// ... und gib die Schemata zurück
			return schemata;
		} catch {
			console.log(`Verbindung zum SVWS-Server unter ${this._url} fehlgeschlagen`);
		}
		throw new UserNotificationException('Es konnte keine Verbindung hergestellt werden.');
	};


	/**
	 * Authentifiziert den angebenen Benutzer mit dem angegebenen Kennwort.
	 *
	 * @param {string} schema   Das Schema
	 * @param {string} username Der Benutzername
	 * @param {string} password Das Kennwort
	 *
	 * @returns {Promise<void>}
	 */
	login = async (schema: string, username: string, password: string): Promise<void> => {
		try {
			this._schema = schema;
			this._api = new ApiServer(this._url, username, password);
			this._apiExternal = new ApiExternal(this._url, username, password);
			await benutzerStateImpl.init(username, password);

			await this.configState.init();
		} catch (error) {
			// Wenn Status 404, dann ist das Schema noch nicht initialisiert
			if ((error instanceof OpenApiError) && (error.response?.status === 404)) {
				return;
			}
			if ((error instanceof OpenApiError) && (error.response?.status === 503)) {
				const res = await error.response.text();
				throw new UserNotificationException(res);
			}
			// TODO Anmelde-Fehler wird nur in der App angezeigt. Der konkreten Fehler könnte ggf. geloggt werden...
			this._api = undefined;
			this._apiExternal = undefined;
			benutzerStateImpl.reset();
			this.configState.clear();
			schuleStateImpl.reset();
			serverStateImpl.reset();
		}
	};


	/**
	 * Trennt die Verbindung für den aktuell angemeldeten Benutzer
	 */
	logout = async (): Promise<void> => {
		benutzerStateImpl.reset();
		schuleStateImpl.reset();
		serverStateImpl.reset();
		this._api = undefined;
		this._apiExternal = undefined;
		this.configState.clear();
	};

}
