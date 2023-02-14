import { Erzieherart, ErzieherStammdaten, List } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler } from "~/router/apps/RouteSchueler";
import { routeApp } from "~/router/RouteApp";
import { routeLogin } from "~/router/RouteLogin";
import { ref, Ref } from "vue";

const SSchuelerErziehungsberechtigte = () => import("~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigte.vue");

export class RouteDataSchuelerErziehungsberechtigte {

	idSchueler: number | undefined = undefined;
	_daten: Ref<List<ErzieherStammdaten> | undefined> = ref(undefined);
	_mapErzieherarten: Ref<Map<number, Erzieherart>> = ref(new Map());

	public get mapErzieherarten() : Map<number, Erzieherart> {
		if (this._mapErzieherarten.value.size === 0)
			throw new Error("Zugriff auf den Katalog der Erzieherarten, bevor dieser geladen werden konnte. ");
		return this._mapErzieherarten.value;
	}

	public get daten(): List<ErzieherStammdaten> {
		if (this._daten.value === undefined)
			throw new Error("Beim Zugriff auf die Daten sind noch keine gültigen Daten geladen.");
		return this._daten.value;
	}

	public get visible(): boolean {
		return !(routeSchuelerErziehungsberechtigte.hidden()) && (this._daten.value !== undefined);
	}

	public async onSelect(idSchueler?: number) {
		if (((idSchueler === undefined) && (this.idSchueler === undefined)) || ((this.idSchueler !== undefined) && (this.idSchueler === idSchueler)))
			return;
		this.idSchueler = idSchueler;
		this._daten.value = (idSchueler === undefined) ? undefined : await routeLogin.data.api.getSchuelerErzieher(routeLogin.data.schema, idSchueler);
	}

	patch = async (data : Partial<ErzieherStammdaten>, id: number) => {
		if (this._daten.value === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await routeLogin.data.api.patchErzieherStammdaten(data, routeLogin.data.schema, id);
	}

}

export class RouteSchuelerErziehungsberechtigte extends RouteNode<RouteDataSchuelerErziehungsberechtigte, RouteSchueler> {

	public constructor() {
		super("schueler_erziehungsberechtigte", "erziehungsberechtigte", SSchuelerErziehungsberechtigte, new RouteDataSchuelerErziehungsberechtigte());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erziehungsberechtigte";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		const listErzieherarten = await routeLogin.data.api.getErzieherArten(routeLogin.data.schema);
		const mapErzieherarten = new Map<number, Erzieherart>();
		for (const e of listErzieherarten)
			mapErzieherarten.set(e.id, e);
		this.data._mapErzieherarten.value = mapErzieherarten;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.data.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.id as string);
			await this.data.onSelect(this.parent!.liste.liste.find(s => s.id === tmp)?.id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
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

