import { shallowRef } from "vue";

import {type Comparator, JavaString, type List, SchemaListeEintrag } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";

import { routeSchema } from "~/router/apps/schema/RouteSchema";
import { routeSchemaUebersicht } from "~/router/apps/schema/uebersicht/RouteSchemaUebersicht";

interface RouteStateSchema {
	auswahl: SchemaListeEintrag | undefined;
	auswahlGruppe: SchemaListeEintrag[];
	mapSchema: Map<string, SchemaListeEintrag>;
	view: RouteNode<any, any>;
}

export class RouteDataSchema {

	private static _defaultState : RouteStateSchema = {
		auswahl: undefined,
		auswahlGruppe: [],
		mapSchema: new Map(),
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
		const view = routeSchemaUebersicht;
		this.setPatchedDefaultState({
			mapSchema,
			auswahl,
			view
		});
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
		this.setPatchedState({ auswahl });
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

	gotoSchema = async (value: SchemaListeEintrag | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeSchema.name, params: { } });
			return;
		}
		const redirect_name: string = (routeSchema.selectedChild === undefined) ? routeSchemaUebersicht.name : routeSchema.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { schema: value.name } });
	}

	setAuswahlGruppe = (auswahlGruppe: SchemaListeEintrag[]) =>	this.setPatchedState({ auswahlGruppe });

}
