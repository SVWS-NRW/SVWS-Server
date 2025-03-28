import { type Ref, ref, shallowRef } from "vue";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";

import { routeApp } from "../RouteApp";
import { routeSchema } from "~/router/apps/schema/RouteSchema";
import { routeSchemaUebersicht } from "~/router/apps/schema/uebersicht/RouteSchemaUebersicht";
import type { SchemaMigrationQuelle } from "~/components/schema/SchemaMigrationQuelle";
import { OpenApiError } from "@core/api/OpenApiError";
import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
import type { BenutzerKennwort } from "@core/core/data/BenutzerKennwort";
import { MigrateBody } from "@core/core/data/db/MigrateBody";
import { SchemaListeEintrag } from "@core/core/data/db/SchemaListeEintrag";
import { DatenbankVerbindungsdaten } from "@core/core/data/schema/DatenbankVerbindungsdaten";
import type { SchuleInfo } from "@core/core/data/schule/SchuleInfo";
import type { SchulenKatalogEintrag } from "@core/core/data/schule/SchulenKatalogEintrag";
import { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { JavaString } from "@core/java/lang/JavaString";
import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import type { List } from "@core/java/util/List";


interface RouteStateSchema {
	auswahl: SchemaListeEintrag | undefined;
	auswahlGruppe: SchemaListeEintrag[];
	mapSchema: Map<string, SchemaListeEintrag>;
	revision: number | null;
	schuleInfo: SchuleInfo | undefined;
	schulen: List<SchulenKatalogEintrag>;
	admins: List<BenutzerListeEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataSchema {

	private static _defaultState : RouteStateSchema = {
		auswahl: undefined,
		auswahlGruppe: [],
		mapSchema: new Map(),
		revision: null,
		schuleInfo: undefined,
		schulen: new ArrayList<SchulenKatalogEintrag>(),
		admins: new ArrayList<BenutzerListeEintrag>(),
		view: routeSchemaUebersicht,
	};

	private _state = shallowRef(RouteDataSchema._defaultState);

	private _migrationQuellinformationen = ref<SchemaMigrationQuelle>({
		dbms: 'mdb',
		schildzentral: false,
		schulnummer: "",
		location: "",
		schema: "",
		user: "",
		password: "",
	});

	private setPatchedDefaultState(patch: Partial<RouteStateSchema>) {
		this._state.value = Object.assign({ ... RouteDataSchema._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchema>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public get migrationQuellinformationen() : Ref<SchemaMigrationQuelle> {
		return this._migrationQuellinformationen;
	}

	public resetMigrationQuellinformationen() {
		this._migrationQuellinformationen.value.dbms = 'mdb';
		this._migrationQuellinformationen.value.schildzentral = false;
		this._migrationQuellinformationen.value.schulnummer = "";
		this._migrationQuellinformationen.value.location = "";
		this._migrationQuellinformationen.value.schema = "";
		this._migrationQuellinformationen.value.user = "";
		this._migrationQuellinformationen.value.password = "";
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): SchemaListeEintrag | undefined {
		return this._state.value.auswahl;
	}

	get admins(): List<BenutzerListeEintrag> {
		return new ArrayList<BenutzerListeEintrag>(this._state.value.admins);
	}

	get auswahlGruppe(): SchemaListeEintrag[] {
		return this._state.value.auswahlGruppe;
	}

	get hatAuswahlSVWSSchema(): boolean {
		if (this.auswahl === undefined)
			return false;
		return (this.auswahl.revision >= 0);
	}

	get auswahlSVWSSchema(): SchemaListeEintrag | null {
		if ((this.auswahl === undefined) || (this.auswahl.revision < 0))
			return null;
		return this.auswahl;
	}

	get mapSchema(): Map<string, SchemaListeEintrag> {
		return this._state.value.mapSchema;
	}

	get revision(): number | null {
		return this._state.value.revision;
	}

	get schuleInfo(): SchuleInfo | undefined {
		return this._state.value.schuleInfo;
	}

	get schulen(): List<SchulenKatalogEintrag> {
		return this._state.value.schulen;
	}

	private getEmpty(name: string) : SchemaListeEintrag {
		const entry = new SchemaListeEintrag();
		entry.name = name;
		entry.revision = -1;
		entry.isTainted = false;
		return entry;
	}

	/**
	 * Initialisiert die Schema-Liste
	 */
	public async init(schemaname : string | undefined, refreshOnly? : boolean) {
		api.status.start();
		const mapSchema = new Map<string, SchemaListeEintrag>();
		const listSchema : List<SchemaListeEintrag> = await api.privileged.getSchemaListe();
		listSchema.sort(<Comparator<SchemaListeEintrag>>{ compare(s1 : SchemaListeEintrag, s2 : SchemaListeEintrag) { return JavaString.compareToIgnoreCase(s1.name, s2.name); } });
		for (const s of listSchema)
			mapSchema.set(s.name.toLocaleLowerCase(), s);
		this._state.value.mapSchema = mapSchema;
		let currSchema : SchemaListeEintrag | undefined = undefined;
		if (mapSchema.size > 0) {
			currSchema = schemaname === undefined ? listSchema.get(0) : mapSchema.get(schemaname.toLocaleLowerCase());
			if (currSchema === undefined)
				currSchema = mapSchema.values().next().value;
		}
		const revision = (refreshOnly === true) ? this._state.value.revision : await api.server.getServerDBRevision();
		const schulen: List<SchulenKatalogEintrag> = (refreshOnly === true) ? this._state.value.schulen : await api.privileged.getAllgemeinenKatalogSchulen();
		const view = routeSchemaUebersicht;
		const { auswahl, schuleInfo, admins } = await this.getSchemaInformation(currSchema);
		this.setPatchedDefaultState({ mapSchema, auswahl, schuleInfo, admins, revision, schulen, view });
		api.status.stop();
	}

	/**
	 * Lädt die Informationen zu dem angegebenen Schema vom SVWS-Server.
	 *
	 * @param schema   das Schema
	 */
	private async getSchemaInformation(schema: SchemaListeEintrag | undefined) : Promise<{ auswahl : SchemaListeEintrag | undefined, schuleInfo : SchuleInfo | undefined, admins : List<BenutzerListeEintrag> }> {
		if ((schema === undefined) || (this.mapSchema.size === 0))
			return { auswahl: undefined, schuleInfo: undefined, admins: new ArrayList()};
		const auswahl = this.mapSchema.has(schema.name.toLocaleLowerCase()) ? schema : undefined;
		let schuleInfo = undefined;
		let admins: List<BenutzerListeEintrag> = new ArrayList();
		if (auswahl !== undefined && auswahl.revision > 0) {
			// Es liegt ein SVWS-Schema vor ...
			try {
				// ... versuche die Informationen zur Schule zu laden
				schuleInfo = await api.privileged.getSchuleInfo(auswahl.name);
			} catch (e) {
				console.log("Die Information zur Schule konnten für das Schema " + auswahl.name + " nicht gefunden werden.")
			}
			// Wenn die Revision des Schemas aktuell ist, dann lade auch die Informationen zu den Admin-Benutzern
			if (auswahl.revision === this.revision)
				admins = await api.privileged.getSchemaAdmins(auswahl.name);
		}
		return { auswahl, schuleInfo, admins };
	}

	/**
	 * Setzt das ausgewählte Schema.
	 *
	 * @param schema   das ausgewählte Schema
	 */
	public async setSchema(schema: SchemaListeEintrag | undefined) {
		const { auswahl, schuleInfo, admins } = await this.getSchemaInformation(schema);
		this.setPatchedState({ auswahl, schuleInfo, admins });
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeSchema.children.includes(view))
			this.setPatchedState({ view });
		else
			throw new Error("Diese für das Schema gewählte Ansicht wird nicht unterstützt.");
	}

	gotoSchema = async (auswahl: SchemaListeEintrag | undefined) => {
		if (auswahl === undefined) {
			await RouteManager.doRoute({ name: routeSchema.name });
			return;
		}
		const redirect_name: string = (routeSchema.selectedChild === undefined) ? routeSchemaUebersicht.name : routeSchema.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { schema: auswahl.name } });
		this.setPatchedState({auswahl});
	}

	gotoSchemaNeu = async () => {
		await RouteManager.doRoute('/schemaneu');
	}

	setAuswahlGruppe = async (auswahlGruppe: SchemaListeEintrag[]) =>	{
		this.setPatchedState({ auswahlGruppe });
		if ((auswahlGruppe.length > 0) && ((routeApp.selectedChild?.name === 'schema') || (routeApp.selectedChild?.name === 'schemaneu')))
			await RouteManager.doRoute('/schemagruppe');
		else if ((auswahlGruppe.length === 0) && (routeApp.selectedChild?.name === 'schemagruppe'))
			await RouteManager.doRoute('schema');
	}

	upgradeSchema = async () => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Es soll ein Backup angelegt werden, aber es ist kein Schema ausgewählt.");
		api.status.start();
		const result = await api.privileged.updateSchemaToCurrent(this.auswahl.name);
		api.status.stop();
		await this.init(this.auswahl.name, true);
		return result;
	}

	removeSchemata = async () => {
		api.status.start();
		for (const schema of this.auswahlGruppe) {
			await api.privileged.destroySchema(schema.name);
			this.mapSchema.delete(schema.name.toLocaleLowerCase());
		}
		api.status.stop();
		if (this.auswahl && this.auswahlGruppe.includes(this.auswahl)) {
			this._state.value.auswahl = undefined;
			await this.gotoSchema(undefined);
		}
		await this.setAuswahlGruppe([]);
	}

	addSchema = async (data: BenutzerKennwort, schema: string) => {
		api.status.start();
		const result = await api.privileged.createSchemaCurrent(data, schema);
		api.status.stop();
		await this.init(schema, true);
		return result;
	}

	importSchema = async (data: FormData, schema: string) => {
		api.status.start();
		const result = await api.privileged.importSQLite2Schema(data, schema);
		api.status.stop();
		await this.init(schema, true);
		api.status.start();
		await this.setSchema(this.auswahl);
		api.status.stop();
		return result;
	}

	backupSchema = async () => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Es soll ein Backup angelegt werden, aber es ist kein Schema ausgewählt.");
		api.status.start();
		const data = await api.privileged.exportSQLiteFrom(this.auswahl.name);
		api.status.stop();
		return data;
	}

	restoreSchema = async (data: FormData) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Es soll ein Backup wiederhergestellt werden, aber es ist kein Schema ausgewählt.");
		api.status.start();
		let result = new SimpleOperationResponse();
		try {
			result = await api.privileged.importSQLiteInto(data, this.auswahl.name);
		} catch (error: unknown) {
			if ((error instanceof OpenApiError) && (error.response instanceof Response)) {
				try {
					const json = await error.response.text();
					result = SimpleOperationResponse.transpilerFromJSON(json);
				} catch(e) {
					result = new SimpleOperationResponse();
					result.success = false;
					result.log.add("Fehler beim Aufruf der API-Methode " + error.response.statusText + " (" + error.response.status + ")");
				}
			} else {
				result = new SimpleOperationResponse();
				result.success = false;
				const out = error instanceof Error ? error.message : 'unbekannter Fehler';
				result.log.add("Es soll ein Backup wiederhergestellt werden, aber es gab einen unerwarteten Fehler: " + out);
			}
		}
		api.status.stop();
		await this.init(this.auswahl.name, true);
		api.status.start();
		await this.setSchema(this.auswahl);
		api.status.stop();
		return result;
	}

	duplicateSchema = async (formData: FormData, duplikat: string) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Es soll ein Duplikat angelegt werden, aber es ist kein Schema ausgewählt.");
		api.status.start();
		const { data } = await api.privileged.exportSQLiteFrom(this.auswahl.name);
		formData.append("database", data);
		const result = await this.importSchema(formData, duplikat);
		api.status.stop();
		await this.init(duplikat, true);
		return result;
	}

	migrateSchema = async (formData: FormData) => {
		const currSchema = this.auswahl?.name;
		const migrateBody = new MigrateBody();
		const datenbankVerbindungsdaten = new DatenbankVerbindungsdaten();
		const db = formData.get('db')?.toString() ?? null;
		const schulnummer = parseInt(formData.get('schulnummer')?.toString() ?? '0');
		let schema = formData.get('schema')?.toString() ?? null;
		if (schema === currSchema) {
			datenbankVerbindungsdaten.location = formData.get('srcLocation')?.toString() ?? null;
			datenbankVerbindungsdaten.schema = formData.get('srcSchema')?.toString() ?? null;
			datenbankVerbindungsdaten.username = formData.get('srcUsername')?.toString() ?? null;
			datenbankVerbindungsdaten.password = formData.get('srcPassword')?.toString() ?? null;
		} else {
			migrateBody.srcLocation = formData.get('srcLocation')?.toString() ?? null;
			migrateBody.srcSchema = formData.get('srcSchema')?.toString() ?? null;
			migrateBody.srcUsername = formData.get('srcUsername')?.toString() ?? null;
			migrateBody.srcPassword = formData.get('srcPassword')?.toString() ?? null;
			migrateBody.schemaUsername = formData.get('schemaUsername')?.toString() ?? api.username;
			migrateBody.schemaUserPassword = formData.get('schemaUserPassword')?.toString() ?? null;
		}
		if (schema === null || db === null)
			throw new DeveloperNotificationException("Es muss ein Schema und eine Datenbank für eine Migration angegeben werden.");
		api.status.start();
		let result = new SimpleOperationResponse();
		try {
			switch (db) {
				case 'mariadb':
					if (schulnummer > 0)
						if (schema === currSchema)
							result = await api.privileged.migrateMariaDBSchulnummerInto(datenbankVerbindungsdaten, schema, schulnummer);
						else
							result = await api.privileged.migrateMariaDB2SchemaSchulnummer(migrateBody, schema, schulnummer);
					else
						if (schema === currSchema)
							result = await api.privileged.migrateMariaDBInto(datenbankVerbindungsdaten, schema);
						else
							result = await api.privileged.migrateMariaDB2Schema(migrateBody, schema);
					break;
				case 'mysql':
					if (schulnummer > 0)
						if (schema === currSchema)
							result = await api.privileged.migrateMySqlSchulnummerInto(datenbankVerbindungsdaten, schema, schulnummer);
						else
							result = await api.privileged.migrateMySQL2SchemaSchulnummer(migrateBody, schema, schulnummer);
					else
						if (schema === currSchema)
							result = await api.privileged.migrateMySqlInto(datenbankVerbindungsdaten, schema);
						else
							result = await api.privileged.migrateMySQL2Schema(migrateBody, schema);
					break;
				case 'mssql':
					if (schulnummer > 0)
						if (schema === currSchema)
							result = await api.privileged.migrateMsSqlServerSchulnummerInto(datenbankVerbindungsdaten, schema, schulnummer);
						else
							result = await api.privileged.migrateMSSQL2SchemaSchulnummer(migrateBody, schema, schulnummer);
					else
						if (schema === currSchema)
							result = await api.privileged.migrateMsSqlServerInto(datenbankVerbindungsdaten, schema);
						else
							result = await api.privileged.migrateMSSQL2Schema(migrateBody, schema);
					break;
				case 'mdb':
					if (schema === currSchema)
						result = await api.privileged.migrateMDBInto(formData, schema);
					else
						result = await api.privileged.migrateMDB2Schema(formData, schema);
					break;
				default:
					throw new DeveloperNotificationException("Es ist ein Fehler aufgetreten bei der Migration");
			}
		} catch(error) {
			schema = currSchema ?? null;
			if ((error instanceof OpenApiError) && (error.response instanceof Response)) {
				try {
					const json = await error.response.text();
					result = SimpleOperationResponse.transpilerFromJSON(json);
				} catch(e) {
					result = new SimpleOperationResponse();
					result.success = false;
					result.log.add("Fehler beim Aufruf der API-Methode " + error.response.statusText + " (" + error.response.status + ")");
				}
			} else {
				result = new SimpleOperationResponse();
				result.success = false;
				const out = error instanceof Error ? error.message : 'unbekannter Fehler';
				result.log.add("Beim Migrieren gab es einen unterwarteten Fehler: " + out);
			}
		}
		api.status.stop();
		await this.init(schema ?? undefined, true);
		api.status.start();
		await this.setSchema(this.auswahl);
		api.status.stop();
		return result;
	}

	initSchema = async (schulnummer: number) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Es soll ein Schema initialisiert werden, aber es ist kein Schema ausgewählt.");
		api.status.start();
		const result = await api.privileged.initSchemaMitSchule(this.auswahl.name, schulnummer);
		api.status.stop();
		await this.init(this.auswahl.name, true);
		return result;
	}

	createEmptySchema = async () => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Es soll ein leeres SVWS-Schema in einem Schema erstellt werden, aber es ist kein Schema ausgewählt.");
		api.status.start();
		const result = await api.privileged.createSchemaCurrentInto(this.auswahl.name);
		api.status.stop();
		await this.init(this.auswahl.name, true);
		return result;
	}

	addExistingSchemaToConfig = async(data: BenutzerKennwort, schema: string) => {
		if (schema === "")
			throw new DeveloperNotificationException("Es soll ein Schema zur Konfiguration hinzugefügt werden, aber es ist kein Schemaname angegeben.");
		api.status.start();
		await api.privileged.importExistingSchema(data, schema);
		const eintrag = this.mapSchema.get(schema);
		if (eintrag !== undefined)
			eintrag.isInConfig = true;
		api.status.stop();
		this.commit();
	}

	refresh = async () => await this.init(undefined, true);

}
