import { ref } from "vue";
import type { RouteLocationRaw, RouteParams } from "vue-router";

import type { InitProps } from "~/components/init/SInitProps";
import type { List, SchulenKatalogEintrag} from "@core";
import { DatenbankVerbindungsdaten, DeveloperNotificationException, ServerMode, ArrayList, BenutzerKompetenz, Schulform } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { api } from "~/router/Api";
import { routeLogin } from "../login/RouteLogin";

import SInit from "~/components/init/SInit.vue";


export class RouteInit extends RouteNode<any, any> {

	protected listSchulkatalog = ref<List<SchulenKatalogEintrag>>(new ArrayList<SchulenKatalogEintrag>());

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "init", "/init", SInit);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "Initialisierung";
	}

	initSchule = async (schule: SchulenKatalogEintrag): Promise<boolean> => {
		try {
			await api.server.initSchule(api.schema, Number(schule.SchulNr));
		} catch(error) {
			console.warn(`Das Initialiseren des Schemas mit der Schulnummer ${schule.SchulNr} ist fehlgeschlagen.`, error);
			return false;
		}
		return this.logout();
	}

	importSQLite = async (formData: FormData): Promise<boolean> => {
		try {
			await api.server.importSQLite(formData, api.schema);
		} catch (error) {
			console.warn(`Das Initialiseren des Schemas mit einnem SQLite-Backup ist fehlgeschlagen.`);
			return false;
		}
		return this.logout();
	}

	migrateDB = async (formData: FormData, restore: boolean, db: string | undefined): Promise<boolean> => {
		if (restore)
			return this.importSQLite(formData);
		if (db === undefined) return false;
		const schulnummer = parseInt(formData.get('schulnummer')?.toString() ?? '');
		const data = new DatenbankVerbindungsdaten();
		data.location = formData.get('location')?.toString() ?? null;
		data.schema = formData.get('schema')?.toString() ?? null;
		data.username = formData.get('username')?.toString() ?? null;
		data.password = formData.get('password')?.toString() ?? null;
		try {
			switch (db) {
				case 'mariadb':
					if (schulnummer > 0)
						await api.server.migrateMariaDBSchulnummer(data, api.schema, schulnummer);
					else
						await api.server.migrateMariaDB(data, api.schema);
					break;
				case 'mysql':
					if (schulnummer > 0)
						await api.server.migrateMySqlSchulnummer(data, api.schema, schulnummer);
					else
						await api.server.migrateMySql(data, api.schema);
					break;
				case 'mssql':
					if (schulnummer > 0)
						await api.server.migrateMsSqlServerSchulnummer(data, api.schema, schulnummer);
					else
						await api.server.migrateMsSqlServer(data, api.schema);
					break;
				case 'mdb':
					await api.server.migrateMDB(formData, api.schema);
					break;
			}
		} catch(error) {
			console.warn(`Das Initialiseren des Schemas mit der Schild 2-Datenbank ist fehlgeschlagen.`);
			return false;
		}
		return this.logout();
	}

	logout = async (): Promise<true> => {
		await api.logout();
		await RouteManager.doRoute(routeLogin.getRoute());
		return true;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			this.listSchulkatalog.value = await api.server.getKatalogSchulen(api.schema);
	}

	public getProps(): InitProps {
		return {
			listSchulkatalog: this.listSchulkatalog.value,
			initSchule: this.initSchule,
			migrateDB: this.migrateDB,
			importSQLite: this.importSQLite,
		}
	}

}

export const routeInit = new RouteInit();
