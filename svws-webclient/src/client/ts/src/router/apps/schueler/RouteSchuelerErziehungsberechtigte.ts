import type { Erzieherart, ErzieherStammdaten, List} from "@core";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { shallowRef } from "vue";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { SchuelerErziehungsberechtigteProps } from "~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigteProps";
import { api } from "~/router/Api";
import type { RouteSchueler } from "~/router/apps/RouteSchueler";
import { routeApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";

const SSchuelerErziehungsberechtigte = () => import("~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigte.vue");

interface RouteStateDataSchuelerErziehungsberechtigte {
	daten: List<ErzieherStammdaten> | undefined;
	idSchueler: number | undefined;
	mapErzieherarten: Map<number, Erzieherart>;
}

export class RouteDataSchuelerErziehungsberechtigte {

	private static _defaultState: RouteStateDataSchuelerErziehungsberechtigte = {
		daten: undefined,
		idSchueler: undefined,
		mapErzieherarten: new Map(),
	}

	private _state = shallowRef(RouteDataSchuelerErziehungsberechtigte._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataSchuelerErziehungsberechtigte>) {
		this._state.value = Object.assign({ ... RouteDataSchuelerErziehungsberechtigte._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataSchuelerErziehungsberechtigte>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get daten(): List<ErzieherStammdaten> {
		if (this._state.value.daten === undefined)
			throw new Error("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		return this._state.value.daten;
	}

	public async setEintrag(idSchueler?: number) {
		if (idSchueler === undefined || idSchueler === this._state.value.idSchueler)
			return;
		const daten = await api.server.getSchuelerErzieher(api.schema, idSchueler);
		this.setPatchedState({idSchueler, daten});
	}

	public get mapErzieherarten() : Map<number, Erzieherart> {
		if (this._state.value.mapErzieherarten.size === 0)
			throw new Error("Zugriff auf den Katalog der Erzieherarten, bevor dieser geladen werden konnte.");
		return this._state.value.mapErzieherarten;
	}

	public async ladeListe() {
		const listErzieherarten = await api.server.getErzieherArten(api.schema);
		const mapErzieherarten = new Map<number, Erzieherart>();
		for (const e of listErzieherarten)
			mapErzieherarten.set(e.id, e);
		this.setPatchedDefaultState({ mapErzieherarten })
	}

	patch = async (data : Partial<ErzieherStammdaten>, id: number) => {
		await api.server.patchErzieherStammdaten(data, api.schema, id);
	}

}

export class RouteSchuelerErziehungsberechtigte extends RouteNode<RouteDataSchuelerErziehungsberechtigte, RouteSchueler> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler.erziehungsberechtigte", "erziehungsberechtigte", SSchuelerErziehungsberechtigte, new RouteDataSchuelerErziehungsberechtigte());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erziehungsberechtigte";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = to_params.id === undefined ? undefined : parseInt(to_params.id);
		await this.data.setEintrag(id);
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchuelerErziehungsberechtigteProps {
		return {
			patch: this.data.patch,
			data: this.data.daten,
			mapErzieherarten: this.data.mapErzieherarten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile
		};
	}

}

export const routeSchuelerErziehungsberechtigte = new RouteSchuelerErziehungsberechtigte();

