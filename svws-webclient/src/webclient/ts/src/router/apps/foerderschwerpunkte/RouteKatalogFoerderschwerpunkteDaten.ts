import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteKatalogFoerderschwerpunkte } from "~/router/apps/RouteKatalogFoerderschwerpunkte";
import { routeLogin } from "~/router/RouteLogin";

export class RouteDataKatalogFoerderschwerpunkteDaten {
	item: FoerderschwerpunktEintrag | undefined = undefined;
	private _daten: FoerderschwerpunktEintrag | undefined = undefined;

	get daten(): FoerderschwerpunktEintrag {
		if (this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: FoerderschwerpunktEintrag | undefined) {
		this._daten = value;
	}

	patch = async (data : Partial<FoerderschwerpunktEintrag>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchFoerderschwerpunktDaten", data);
		//await routeLogin.data.api.patchJahrgangDaten(data, routeLogin.data.schema, this.item.id);
	}
}

const SFoerderschwerpunktDaten = () => import("~/components/kataloge/foerderschwerpunkte/daten/SFoerderschwerpunktDaten.vue");

export class RouteKatalogFoerderschwerpunkteDaten extends RouteNode<RouteDataKatalogFoerderschwerpunkteDaten, RouteKatalogFoerderschwerpunkte> {

	public constructor() {
		super("foerderschwerpunkte_daten", "daten", SFoerderschwerpunktDaten, new RouteDataKatalogFoerderschwerpunkteDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: FoerderschwerpunktEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten = undefined;
		} else {
			this.data.item = item;
			this.data.daten = await routeLogin.data.api.getSchuelerFoerderschwerpunkt(routeLogin.data.schema, item.id);
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

export const routeKatalogFoerderschwerpunkteDaten = new RouteKatalogFoerderschwerpunkteDaten();

