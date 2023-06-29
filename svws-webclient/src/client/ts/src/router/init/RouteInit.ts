import type { Ref} from "vue";
import { ref} from "vue";
import type { RouteLocationRaw, RouteParams } from "vue-router";

import type { List, SchulenKatalogEintrag} from "@core";
import { DatenbankVerbindungsdaten, ServerMode} from "@core";
import { ArrayList, BenutzerKompetenz, Schulform } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { api } from "~/router/Api";
import { routeApp } from "~/router/apps/RouteApp";

import SInit from "~/components/init/SInit.vue";
import type { InitProps } from "~/components/init/SInitProps";


export class RouteInit extends RouteNode<unknown, any> {

	protected listSchulkatalog: Ref<List<SchulenKatalogEintrag>> = ref(new ArrayList<SchulenKatalogEintrag>());
	protected source: Ref<'schulkatalog'|'schild2'|'backup'|undefined> = ref(undefined);
	protected db: Ref<'mysql'|'mariadb'|'mssql'|'mdb'|undefined> = ref(undefined);

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
		return this.gotoApp();
	}

	importSQLite = async (formData: FormData): Promise<boolean> => {
		try {
			await api.server.importSQLite(formData, api.schema);
		} catch (error) {
			console.warn(`Das Initialiseren des Schemas mit einnem SQLite-Backup ist fehlgeschlagen.`);
			return false;
		}
		return this.gotoApp();
	}

	migrateDB = async (formData: FormData): Promise<boolean> => {
		if (this.source.value === 'backup')
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
		return this.gotoApp();
	}

	gotoApp = async (): Promise<true> => {
		await api.init();
		await RouteManager.doRoute(routeApp.getRoute());
		return true;
	}

	setSource = async (source: string) => await RouteManager.doRoute({name: this.name, params: { source } });
	setDB = async (db: string) => await RouteManager.doRoute({name: this.name, params: { source: this.source.value, db }});

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		this.listSchulkatalog.value = await api.server.getKatalogSchulen(api.schema);
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.source instanceof Array || to_params.db instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		this.source.value = ['schulkatalog','schild2','backup',undefined].includes(to_params.source) ? to_params.source as 'schulkatalog'|'schild2'|'backup'|undefined : undefined;
		this.db.value = ['mysql','mariadb','mssql','mdb',undefined].includes(to_params.db) ? to_params.db as 'mysql'|'mariadb'|'mssql'|'mdb'|undefined : undefined;
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
			source: this.source.value,
			db: this.db.value,
		}
	}

}

export const routeInit = new RouteInit();
