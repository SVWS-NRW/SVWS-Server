import {
	ApiSchema,
	ApiServer,
	List,
	ReligionEintrag,
	Vector,
	DBSchemaListeEintrag,
	Schuljahresabschnitt,
	KatalogEintrag,
	OrtKatalogEintrag,
	OrtsteilKatalogEintrag,
} from "@svws-nrw/svws-core-ts";

import { type Apps, App } from "./BaseApp";
import { Schule } from "./schule/Schule";

import { BaseList } from "./BaseList";
import { ApiLoadingStatus } from "./core/ApiLoadingStatus.class";
import { MAIN_LOADING_SYMBOL } from "./core/LoadingSymbols";
import { ComputedRef, inject, InjectionKey, provide, reactive } from "vue";

export type Kataloge = {
	orte: List<OrtKatalogEintrag>;
	ortsteile: List<OrtsteilKatalogEintrag>;
	religionen: List<ReligionEintrag>;
	haltestellen: List<KatalogEintrag>;
	beschaeftigungsarten: List<KatalogEintrag>;
};

export class MainConfig {
	selected_app = "Schueler";
	dbSchema: DBSchemaListeEintrag | undefined = undefined;
	dbSchemata: DBSchemaListeEintrag[] = [];
	apiLoadingStatus: ApiLoadingStatus = new ApiLoadingStatus();
	pending: ComputedRef<boolean> = this.apiLoadingStatus.api_pending;
	isAuthenticated: boolean | undefined = undefined;
	isConnected = false;
	blockung_aktiv = false;
	hasGost = false;
	user_config: Map<keyof UserConfigKeys, UserConfigKeys[keyof UserConfigKeys]> = new Map();

	/** Das aktuelle Drag & Drop - Objekt */
	drag_and_drop_data: any;

	get schemaname(): string {
		if (!this.dbSchema || !this.dbSchema.name) return "";
		return this.dbSchema.name.valueOf();
	}
	/**
	 * Der aktuell ausgewählte Abschnitt
	 *
	 * @returns {Schuljahresabschnitt}
	 */
	get akt_abschnitt(): Schuljahresabschnitt {
		return App.akt_abschnitt;
	}
	/**
	 * Setzt den aktuellen Schuljahresabschnitt
	 *
	 * @param {Schuljahresabschnitt} abschnitt
	 */
	set akt_abschnitt(abschnitt: Schuljahresabschnitt) {
		App.akt_abschnitt = abschnitt;
		const lists = BaseList.all
		for (const l of lists) {
			l.update_list()
		}
	}
}

/**
 * Diese Klasse wird als App in vue geladen Sie steht als `this.$app` zur
 * Verfügung und sollte nur die nötigsten Informationen enthalten, die für alle
 * anderen Apps notwendig sind. Beispielsweise der aktuelle Abschnitt.
 */
export class Main {
	/** Die OpenAPI-Schnittstelle für den Zugriff auf die Konfiguration des SVWS-Servers */
	private hostname = "localhost";
	private username = "";
	private password = "";
	private api!: ApiServer;
	private api_schema!: ApiSchema;
	private _pending: Promise<unknown>[] = [];

	// public connection = new Connection();
	public config = reactive(new MainConfig());

	public kataloge: Kataloge = {
		ortsteile: new Vector<OrtsteilKatalogEintrag>(),
		haltestellen: new Vector<KatalogEintrag>(),
		religionen: new Vector<ReligionEintrag>(),
		orte: new Vector<OrtKatalogEintrag>(),
		beschaeftigungsarten: new Vector<KatalogEintrag>(),
	};

	/**
	 * Diese Methode überprüft, ob die aktuelle Verbindung zum SVWS-Server vorhanden ist.
	 *
	 * @returns {boolean}
	 */
	public get connected(): boolean {
		return this.config.isConnected;
	}
	/**
	 * Diese Methode prüft, ob der Benutzer angemeldet ist
	 *
	 * @returns {boolean}
	 */
	public get authenticated(): boolean | undefined {
		return this.config.isAuthenticated;
	}

	/**
	 * Versucht eine Verbindung zu dem SVWS-Server unter der angegebenen URL aufzubauen.
	 *
	 * @param {string} url Die URL unter der der SVWS-Server erreichbar sein soll
	 * @returns {Promise<void>}
	 */
	public async connectTo(url: string): Promise<boolean> {
		this.hostname = `https://${url || this.hostname}`;
		this.api = new ApiServer(this.hostname, this.username, this.password);
		console.log(`Connecting to ${this.hostname}`, url, this.api);
		// this.connection.server = url;
		// this.connection.initApi();
		// this.api_config = new ApiServer(this.connection.config);
		try {
			const result = await this.api.getConfigDBSchemata();
			console.log(`DB-Revision: ${result}`);
			//this.connection.setDBSchemata(true, result);
			this.config.dbSchemata = result.toArray(
				new Array<DBSchemaListeEintrag>()
			);
			if (result.size() <= 0) {
				this.config.dbSchema = undefined;
			} else {
				let tmp = undefined;
				for (const s of result) {
					if (s.isDefault) tmp = s;
				}
				if (!tmp) tmp = result.get(0);
				this.config.dbSchema = tmp;
			}
			console.log(`Verbunden mit DB-Schema: ${this.config.dbSchema?.name}`);
			this.config.isConnected = true;
			return true
		} catch (error) {
			{
				//this.connection.setDBSchemata(false, []);
				console.log(
					`Verbindung zum SVWS-Server unter https://${this.hostname} fehlgeschlagen.`
				);
				return false
				// switch (this.connection.connectionStep) {
				// 	case 1:
				// 		this.connection.connectionStep++;
				// 		this.connectTo(hostname);
				// 		break;
				// }
			}
		}
	}

	/**
	 * Authentifiziert den angebenen Benutzer mit dem angegebenen Kennwort.
	 *
	 * @param {string} username Der Benutzername
	 * @param {string} password Das Kennwort
	 * @returns {Promise<void>}
	 */
	public async authenticate(
		username: string,
		password: string
	): Promise<void> {
		// this.connection.username = username;
		// this.connection.password = password;
		// this.connection.initApi();
		try {
			this.api_schema = new ApiSchema(this.hostname, username, password);
			// const result = await this.api.isAlive();
			if (!this.config.dbSchema?.name) return
			const result = await this.api_schema.revision(this.config.dbSchema?.name.toString())
			// TODO verwende revision für Client Check
			console.log(`DB-Revision: ${result}`);
			this.username = username;
			this.password = password;
			await this.start_apps(); //returns Promise<boolean>
			this.config.isAuthenticated = true;
		} catch (error) {
			// TODO error z.B. loggen
			console.log(error)
			this.config.isAuthenticated = false;
			//this.schemaRevision = -1;
		}
	}

	/**
	 * Diese Methode startet alle Apps, die in dieser App enthalten sind. Holt mit
	 * einem Worker die größeren Kataloge ab.
	 *
	 * @returns {Promise<void>}
	 */
	private async start_apps(): Promise<void> {
		App.setup({
			url: this.hostname,
			username: this.username,
			password: this.password,
			schema: this.config.schemaname
		});
		App.apps = {
			schule: new Schule(),
		};
		await App.apps.schule.init();
		this.config.hasGost = !!App.apps.schule.schuleStammdaten.schulform.value?.daten.hatGymOb;
		const o = App.schema;
		this._pending.push(App.api.getOrte(o).then(r => this.kataloge.orte = r));
		this._pending.push(App.api.getOrtsteile(o).then(r => this.kataloge.ortsteile = r));
		this._pending.push(App.api.getReligionen(o).then(r => this.kataloge.religionen = r));
		this._pending.push(App.api.getHaltestellen(o).then(r => this.kataloge.haltestellen = r));
		this._pending.push(App.api.getKatalogBeschaeftigungsart(o).then(r => this.kataloge.beschaeftigungsarten = r));

		const prom = Promise.all(this._pending).finally(() => (this.config.selected_app = "Schueler"));
		this.config.apiLoadingStatus.addStatusByPromise(prom, {
			caller: 'Main',
			message: 'Anwendung wird aktualisiert.',
			categories: [MAIN_LOADING_SYMBOL]
		});
	}

	/**
	 * Gibt alle Apps als Objekt zurück
	 *
	 * @returns {Apps}
	 */
	get apps(): Apps {
		return App.apps;
	}

	/**
	 * Diese Methode loggt den aktuellen Benutzer aus und beendet die aktuell
	 * authentifizierte Verbindung zum SVWS-Server.
	 *
	 * @returns {Promise<void>}
	 */
	public logout(): void {
		this.config.selected_app = "Schueler";
		this.connectTo(this.hostname);
	}
}

export const mainApp = new Main();

function requireInjection<T>(key: InjectionKey<T>, defaultValue?: T) {
	const resolved = inject(key, defaultValue);
	if (!resolved) throw new Error(`${key} was not provided.`);
	return resolved;
}

export const mainInjectKey = Symbol("MainInjectKey") as InjectionKey<Main>;
export function provideMainApp() {
	provide(mainInjectKey, mainApp);
}
export function injectMainApp() {
	return requireInjection(mainInjectKey);
}
