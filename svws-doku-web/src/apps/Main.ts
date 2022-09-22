import { ApiServer } from "@svws-nrw/svws-api-ts";
import {
	DBSchemaListeEintrag,
	NationalitaetKatalogEintrag
} from "@svws-nrw/svws-api-ts";

import { App, Apps } from "./BaseApp";

import { Schueler } from "./schueler/Schueler";
import { reactive } from 'vue';

export type Kataloge = {
	nationalitaeten: NationalitaetKatalogEintrag[];
};


export class MainConfig {
	dbSchema : DBSchemaListeEintrag | undefined = undefined;
	dbSchemata : DBSchemaListeEintrag[] = [];
	pending  = true;
	isAuthenticated  = false;
	isConnected  = false;

	get schemaname() : string {
		if ((!this.dbSchema) || (!this.dbSchema.name))
			return '';
		return this.dbSchema.name.valueOf();
	}
};

/**
 * Diese Klasse wird als App in vue geladen Sie steht als `this.$app` zur
 * Verfügung und sollte nur die nötigsten Informationen enthalten, die für alle
 * anderen Apps notwendig sind. Beispielsweise der aktuelle Abschnitt.
 */
export class Main {
	/** Die OpenAPI-Schnittstelle für den Zugriff auf die Konfiguration des SVWS-Servers */
	private hostname = "localhost";
	private username = "Admin";
	private password = "";
	private api!: ApiServer;
	private _pending: Promise<unknown>[] = [];

	// public connection = new Connection();
	public config: MainConfig = reactive(new MainConfig());

	public kataloge: Kataloge = {
		nationalitaeten: [],
	};


	/**
	 * Versucht eine Verbindung zu dem SVWS-Server unter der angegebenen URL aufzubauen.
	 *
	 * @param {string} url Die URL unter der der SVWS-Server erreichbar sein soll
	 * @returns {Promise<void>}
	 */
	public async connectTo(url: string): Promise<void> {
		this.hostname = `https://${url || this.hostname}`;
		this.api = new ApiServer(this.hostname, this.username, this.password);
		console.log(`Connecting to ${this.hostname}`, url, this.api);
		try {
			const result = await this.api.getConfigDBSchemata();
			console.log(`DB-Revision: ${result}`);
			this.config.dbSchemata = result.toArray(new Array<DBSchemaListeEintrag>());
			if (result.size() <= 0) {
				this.config.dbSchema = undefined;
			} else {
				let tmp = undefined;
				for (const s of result) {
					if (s.isDefault)
						tmp = s;
				}
				if (!tmp)
					tmp = result.get(0);
				this.config.dbSchema = tmp;
			}
			this.config.isConnected = true;
			await this.start_apps(); //returns Promise<boolean>
		} catch (error) {
			{
				console.log(
					`Verbindung zum SVWS-Server unter https://${this.hostname} fehlgeschlagen.`
				);
			}
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
		console.log(this.config.schemaname)
		App.apps = {
			schueler: new Schueler(),
		};
		await App.apps.schueler.init();
		const o = App.schema;
		this._pending.push(
			App.api
				.getKatalogNationaelitaeten(o)
				.then(r => (this.kataloge.nationalitaeten = r.toArray(new Array<NationalitaetKatalogEintrag>())))
		);
		Promise.all(this._pending)
			.then(() => (this.config.pending = false))
			.catch(() => (this.config.pending = true))
	}
	/**
	 * Gibt alle Apps als Objekt zurück
	 *
	 * @returns {Apps}
	 */
	get apps(): Apps {
		return App.apps;
	}
}
