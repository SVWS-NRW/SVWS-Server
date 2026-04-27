import { ref } from "vue";
import type { RouteLocationRaw, RouteParams } from "vue-router";

import type { InitProps } from "~/components/init/SInitProps";
import type { List, SchulenKatalogEintrag } from "@core";
import { DatenbankVerbindungsdaten, ServerMode, ArrayList, BenutzerKompetenz, Schulform, SimpleOperationResponse, OpenApiError } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { api } from "~/router/Api";
import { routeLogin } from "../login/RouteLogin";

import SInit from "~/components/init/SInit.vue";


export class RouteInit extends RouteNode<any, any> {

	protected listSchulkatalog = ref<List<SchulenKatalogEintrag>>(new ArrayList<SchulenKatalogEintrag>());

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "init", "/init", SInit);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Initialisierung";
	}

	private async createSimpleOperationResponse(e: unknown): Promise<SimpleOperationResponse> {
		const res = new SimpleOperationResponse();
		if (e instanceof OpenApiError) {
			if (e.response instanceof Response) {
				const text = await e.response.text();
				try {
					const res = JSON.parse(text);
					return res satisfies SimpleOperationResponse;
				} catch {
					res.log.add("Fehler beim Importieren der Daten");
				}
			}
			res.log.add(e.message);
		}
		return res;
	}

	initSchule = async (schule: SchulenKatalogEintrag): Promise<boolean> => {
		try {
			await api.server.initSchule(api.schema, Number(schule.SchulNr));
			return true;
		} catch (error) {
			console.warn(`Das Initialiseren des Schemas mit der Schulnummer ${schule.SchulNr} ist fehlgeschlagen.`, error);
			await this.logout();
			return false;
		}
	};

	importSQLite = async (formData: FormData): Promise<SimpleOperationResponse> => {
		try {
			return await api.server.importSQLite(formData, api.schema);
		} catch (e) {
			return this.createSimpleOperationResponse(e);
		}
	};

	migrateDB = async (formData: FormData, restore: boolean, db: string | undefined): Promise<SimpleOperationResponse> => {
		if (restore) {
			return this.importSQLite(formData);
		}
		if (db === undefined) {
			return new SimpleOperationResponse();
		}
		const schulnummer = Number.parseInt(formData.get('schulnummer') as string | null ?? "0");
		const data = new DatenbankVerbindungsdaten();
		data.location = formData.get('location') as string | null ?? "";
		data.schema = formData.get('schema') as string | null ?? "";
		data.username = formData.get('username') as string | null ?? "";
		data.password = formData.get('password') as string | null ?? "";
		try {
			switch (db) {
				case 'mariadb':
					if (schulnummer > 0) {
						return await api.server.migrateMariaDBSchulnummer(data, api.schema, schulnummer);
					} else {
						return await api.server.migrateMariaDB(data, api.schema);
					}
				case 'mysql':
					if (schulnummer > 0) {
						return await api.server.migrateMySqlSchulnummer(data, api.schema, schulnummer);
					} else {
						return await api.server.migrateMySql(data, api.schema);
					}
				case 'mssql':
					if (schulnummer > 0) {
						return await api.server.migrateMsSqlServerSchulnummer(data, api.schema, schulnummer);
					} else {
						return await api.server.migrateMsSqlServer(data, api.schema);
					}
				case 'mdb':
				default:
					return await api.server.migrateMDB(formData, api.schema);
			}
		} catch (e) {
			return this.createSimpleOperationResponse(e);
		}
	};

	logout = async (): Promise<true> => {
		await api.logout();
		await RouteManager.doRoute(routeLogin.getRoute());
		return true;
	};

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			this.listSchulkatalog.value = await api.server.getKatalogSchulen(api.schema);
		}
	}

	public getProps(): InitProps {
		return {
			listSchulkatalog: this.listSchulkatalog.value,
			initSchule: this.initSchule,
			migrateDB: this.migrateDB,
			importSQLite: this.importSQLite,
		};
	}

}

export const routeInit = new RouteInit();
