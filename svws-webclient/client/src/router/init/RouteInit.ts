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
	protected source = ref<'init'|'restore'|'migrate' | undefined>(undefined);
	protected db = ref<'mysql' | 'mariadb' | 'mssql' | 'mdb'| undefined>(undefined);

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "init", "/init/:source?/:db?", SInit);
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

	migrateDB = async (formData: FormData): Promise<boolean> => {
		if (this.source.value === 'restore')
			return this.importSQLite(formData);
		const db = this.db.value;
		if (!db) return false;
		const schulnummer = parseInt(formData.get('schulnummer')?.toString() || '');
		const data = new DatenbankVerbindungsdaten();
		data.location = formData.get('location')?.toString() || null;
		data.schema = formData.get('schema')?.toString() || null;
		data.username = formData.get('username')?.toString() || null;
		data.password = formData.get('password')?.toString() || null;
		try {
			switch (db) {
				case 'mariadb':
					schulnummer
						? await api.server.migrateMariaDBSchulnummer(data, api.schema, schulnummer)
						: await api.server.migrateMariaDB(data, api.schema)
					break;
				case 'mysql':
					schulnummer
						? await api.server.migrateMySqlSchulnummer(data, api.schema, schulnummer)
						: await api.server.migrateMySql(data, api.schema)
					break;
				case 'mssql':
					schulnummer
						? await api.server.migrateMsSqlServerSchulnummer(data, api.schema, schulnummer)
						: await api.server.migrateMsSqlServer(data, api.schema)
					break;
				case 'mdb':
					await api.server.migrateMDB(formData, api.schema)
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

	setSource = async (source: 'init'|'restore'|'migrate') => {
		const db = (source === 'migrate') ? 'mdb' : undefined;
		await RouteManager.doRoute({name: this.name, params: { source, db } });
	}

	setDB = async (db: 'mysql'|'mariadb'|'mssql'|'mdb') => {
		await RouteManager.doRoute({name: this.name, params: { source: this.source.value, db }});
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.source instanceof Array || to_params.db instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const source = (to_params.source === 'init' || to_params.source === 'migrate' || to_params.source === 'restore') ? to_params.source : undefined;
		const db = (to_params.db === 'mysql' || to_params.db === 'mariadb' || to_params.db === 'mssql' || to_params.db === 'mdb') ? to_params.db : undefined;
		if (isEntering)
			this.listSchulkatalog.value = await api.server.getKatalogSchulen(api.schema);
		if (source === undefined)
			return { name: this.name, params: { source: 'init' }};
		if ((source === 'migrate') && (db === undefined))
			return { name: this.name, params: { source: to_params.source, db: 'mdb' }};
		if ((source !== 'migrate') && (db !== undefined))
			return { name: this.name, params: { source: to_params.source }};
		this.source.value = source;
		this.db.value = db;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name };
	}

	public getProps(): InitProps {
		return {
			setSource: this.setSource,
			setDB: this.setDB,
			listSchulkatalog: this.listSchulkatalog.value,
			initSchule: this.initSchule,
			migrateDB: this.migrateDB,
			importSQLite: this.importSQLite,
			source: this.source.value,
			db: this.db.value,
		}
	}

}

export const routeInit = new RouteInit();
