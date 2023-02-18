import { FachDaten, FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeLogin } from "~/router/RouteLogin";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogFaecher, RouteKatalogFaecher } from "../RouteKatalogFaecher";

export class RouteDataFaecherDaten {
	item: FaecherListeEintrag | undefined = undefined;
	private _daten: FachDaten | undefined = undefined;

	get daten(): FachDaten {
		if (this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Fachdaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: FachDaten | undefined) {
		this._daten = value;
	}

	patch = async (data : Partial<FachDaten>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchFachdatenDaten", data);
		//await routeLogin.data.api.patchFachdatenDaten(data, routeLogin.data.schema, this.item.id);
	}
}

const SFachDaten = () => import("~/components/kataloge/faecher/daten/SFachDaten.vue");

export class RouteFaecherDaten extends RouteNode<RouteDataFaecherDaten, RouteKatalogFaecher> {

	public constructor() {
		super("faecher_daten", "daten", SFachDaten, new RouteDataFaecherDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.gefiltert.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: FaecherListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten = undefined;
		} else {
			this.data.item = item;
			this.data.daten = await routeLogin.data.api.getFach(routeLogin.data.schema, item.id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patch: this.data.patch,
			item: this.data.item,
			data: this.data.daten
		};
	}

}

export const routeFaecherDaten = new RouteFaecherDaten();

