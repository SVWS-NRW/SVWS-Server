import { shallowRef } from "vue";

import type { BenutzerKennwort , Comparator,  List, SchuleInfo } from "@core";
import { DatenbankVerbindungsdaten, DeveloperNotificationException, JavaString, MigrateBody, SchemaListeEintrag, SimpleOperationResponse } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";

import { routeSchema } from "~/router/apps/schema/RouteSchema";
import { routeSchemaUebersicht } from "~/router/apps/schema/uebersicht/RouteSchemaUebersicht";

interface RouteStateSchema {
	auswahl: SchemaListeEintrag | undefined;
	auswahlGruppe: SchemaListeEintrag[];
	mapSchema: Map<string, SchemaListeEintrag>;
	revision: number | null;
	schuleInfo: SchuleInfo | undefined;
	view: RouteNode<any, any>;
}

export class RouteDataSchema {

	private static _defaultState : RouteStateSchema = {
		auswahl: undefined,
		auswahlGruppe: [],
		mapSchema: new Map(),
		revision: null,
		schuleInfo: undefined,
		view: routeSchemaUebersicht,
	};

	private _state = shallowRef(RouteDataSchema._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchema>) {
		this._state.value = Object.assign({ ... RouteDataSchema._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchema>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
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
	public async init(schemaname : string | undefined) {
		api.status.start();
		const mapSchema = new Map<string, SchemaListeEintrag>();
		const listSchema : List<string> = await api.privileged.getSchemaListe();
		listSchema.sort(<Comparator<string>>{ compare(s1 : string, s2 : string) { return JavaString.compareToIgnoreCase(s1, s2); } });
		for (const s of listSchema)
			mapSchema.set(s, this.getEmpty(s));
		const listSVWSSchema : List<SchemaListeEintrag> = await api.privileged.getSVWSSchemaListe();
		for (const s of listSVWSSchema)
			mapSchema.set(s.name, s);
		let auswahl : SchemaListeEintrag | undefined = undefined;
		if (mapSchema.size > 0) {
			auswahl = schemaname === undefined ? undefined : mapSchema.get(schemaname);
			if (auswahl === undefined)
				auswahl = mapSchema.values().next().value;
		}
		const revision = await api.server.getServerDBRevision();
		const view = routeSchemaUebersicht;
		this.setPatchedDefaultState({
			mapSchema,
			auswahl,
			revision,
			view
		});
		api.status.stop();
	}

	/**
	 * Setzt das ausgewählte Schema.
	 *
	 * @param schema   das ausgewählte Schema
	 */
	public async setSchema(schema: SchemaListeEintrag | undefined) {
		if (schema === this._state.value.auswahl)
			return;
		if ((schema === undefined) || (this.mapSchema.size === 0))
			return;
		const auswahl = this.mapSchema.has(schema.name) ? schema : undefined;
		let schuleInfo = undefined;
		if (auswahl !== undefined)
			try {
				schuleInfo = await api.privileged.getSchuleInfo(auswahl.name);
			} catch(e) {
				schuleInfo = undefined;
			}
		this.setPatchedState({ auswahl, schuleInfo });
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeSchema.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für das Schema gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get auswahl(): SchemaListeEintrag | undefined {
		return this._state.value.auswahl;
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

	gotoSchema = async (value: SchemaListeEintrag | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeSchema.name, params: { } });
			return;
		}
		const redirect_name: string = (routeSchema.selectedChild === undefined) ? routeSchemaUebersicht.name : routeSchema.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { schema: value.name } });
	}

	setAuswahlGruppe = (auswahlGruppe: SchemaListeEintrag[]) =>	this.setPatchedState({ auswahlGruppe });

	upgradeSchema = async () => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Es soll ein Backup angelegt werden, aber es ist kein Schema ausgewählt.");
		api.status.start();
		const result = await api.privileged.updateSchemaToCurrent(this.auswahl.name);
		api.status.stop();
		return result;
	}

	removeSchemata = async () => {
		api.status.start();
		for (const schema of this.auswahlGruppe) {
			await api.privileged.destroySchema(schema.name);
			this.mapSchema.delete(schema.name);
		}
		api.status.stop();
		if (this.auswahl && this.auswahlGruppe.includes(this.auswahl))
			await this.gotoSchema(undefined);
		this.setAuswahlGruppe([]);
	}

	addSchema = async (data: BenutzerKennwort, schema: string) => {
		api.status.start();
		const result = await api.privileged.createSchemaCurrent(data, schema);
		const list = await api.privileged.getSVWSSchemaListe();
		for (const item of list)
			if (item.name === schema) {
				this.mapSchema.set(item.name, item);
				this.setPatchedState({mapSchema: this.mapSchema});
				await this.gotoSchema(item);
				break;
			}
		api.status.stop();
		return result;
	}

	importSchema = async (data: FormData, schema: string) => {
		api.status.start();
		const result = await api.privileged.importSQLite2Schema(data, schema);
		const list = await api.privileged.getSVWSSchemaListe();
		for (const item of list)
			if (item.name === schema) {
				this.mapSchema.set(item.name, item);
				this.setPatchedState({mapSchema: this.mapSchema});
				await this.gotoSchema(item);
				break;
			}
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
		try {
			const result = await api.privileged.importSQLiteInto(data, this.auswahl.name);
			api.status.stop();
			return result;
		} catch (error) {
			api.status.stop();
			if (error instanceof SimpleOperationResponse)
				return error;
			else throw new DeveloperNotificationException("Es soll ein Backup wiederhergestellt werden, aber es gabe einen unterwarteten Fehler.");
		}
	}

	duplicateSchema = async (formData: FormData, duplikat: string) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Es soll ein Duplikat angelegt werden, aber es ist kein Schema ausgewählt.");
		api.status.start();
		const { data } = await api.privileged.exportSQLiteFrom(this.auswahl.name);
		formData.append("database", data);
		const result = await this.importSchema(formData, duplikat);
		api.status.stop();
		return result;
	}

	migrateSchema = async (formData: FormData) => {
		const currSchema = this.auswahl?.name;
		const migrateBody = new MigrateBody();
		const datenbankVerbindungsdaten = new DatenbankVerbindungsdaten();
		const db = formData.get('db')?.toString() || null;
		const schulnummer = parseInt(formData.get('schulnummer')?.toString() || '');
		const schema = formData.get('schema')?.toString() || null;
		if (schema === currSchema) {
			datenbankVerbindungsdaten.location = formData.get('srcLocation')?.toString() || null;
			datenbankVerbindungsdaten.username = formData.get('srcUsername')?.toString() || null;
			datenbankVerbindungsdaten.password = formData.get('srcPassword')?.toString() || null;
		} else {
			migrateBody.srcLocation = formData.get('srcLocation')?.toString() || null;
			migrateBody.srcSchema = formData.get('srcSchema')?.toString() || null;
			migrateBody.srcUsername = formData.get('srcUsername')?.toString() || null;
			migrateBody.srcPassword = formData.get('srcPassword')?.toString() || null;
			migrateBody.schemaUsername = formData.get('schemaUsername')?.toString() || api.username;
			migrateBody.schemaUserPassword = formData.get('schemaUserPassword')?.toString() || null;
		}
		if (schema === null || db === null)
			throw new DeveloperNotificationException("Es muss ein Schema und eine Datenbank für eine Migration angegeben werden.");
		api.status.start();
		let result = new SimpleOperationResponse();
		try {
			switch (db) {
				case 'mariadb':
					if (schulnummer)
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
					if (schulnummer)
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
					if (schulnummer)
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
			await this.init(schema);
		} catch(error) {
			console.warn(`Das Initialiseren des Schemas mit der Schild 2-Datenbank ist fehlgeschlagen.`);
		}
		api.status.stop();
		return result;
	}

	refresh = async () => await this.init(undefined);
}
