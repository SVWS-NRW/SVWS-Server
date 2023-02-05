import { Erzieherart, ErzieherStammdaten, List } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler } from "~/router/apps/RouteSchueler";
import { routeApp } from "~/router/RouteApp";
import { App } from "~/apps/BaseApp";
import { ref, Ref } from "vue";

const SSchuelerErziehungsberechtigte = () => import("~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigte.vue");

export class RouteDataSchuelerErziehungsberechtigte {

	idSchueler: number | undefined = undefined;
	_daten: Ref<List<ErzieherStammdaten> | undefined> = ref(undefined);
	_erzieherarten: Ref<List<Erzieherart> | undefined> = ref(undefined);

	public get erzieherarten() : List<Erzieherart> {
		if (this._erzieherarten.value === undefined)
			throw new Error("Zugriff auf den Katalog der Erzieherarten, bevor dieser geladen werden konnte. ");
		return this._erzieherarten.value;
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
		this._daten.value = (idSchueler === undefined) ? undefined : await App.api.getSchuelerErzieher(App.schema, idSchueler);
	}

	patch = async (data : Partial<ErzieherStammdaten>, id: number) => {
		if (this._daten.value === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await App.api.patchErzieherStammdaten(data, App.schema, id);
	}

}

export class RouteSchuelerErziehungsberechtigte extends RouteNode<RouteDataSchuelerErziehungsberechtigte, RouteSchueler> {

	public constructor() {
		super("schueler_erziehungsberechtigte", "erziehungsberechtigte", SSchuelerErziehungsberechtigte, new RouteDataSchuelerErziehungsberechtigte());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erziehungsberechtigte";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		this.data._erzieherarten.value = await App.api.getErzieherArten(App.schema);
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
			erzieherarten: this.data.erzieherarten,
			orte: routeApp.data.orte,
			ortsteile: routeApp.data.ortsteile
		};
	}

}

export const routeSchuelerErziehungsberechtigte = new RouteSchuelerErziehungsberechtigte();

