import { RouteNode } from "~/router/RouteNode";
import { routeKatalogJahrgaenge, RouteKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { JahrgangsDaten, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeLogin } from "~/router/RouteLogin";

export class RouteDataKatalogJahrgaengeDaten {
	item: JahrgangsListeEintrag | undefined = undefined;
	private _daten: JahrgangsDaten | undefined = undefined;

	get daten(): JahrgangsDaten {
		if (this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: JahrgangsDaten | undefined) {
		this._daten = value;
	}

	patch = async (data : Partial<JahrgangsDaten>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchJahrgangDaten", data);
		//await routeLogin.data.api.patchJahrgangDaten(data, routeLogin.data.schema, this.item.id);
	}
}

const SJahrgangDaten = () => import("~/components/kataloge/jahrgaenge/daten/SJahrgangDaten.vue");

export class RouteKatalogJahrgaengeDaten extends RouteNode<RouteDataKatalogJahrgaengeDaten, RouteKatalogJahrgaenge> {

	public constructor() {
		super("jahrgaenge_daten", "daten", SJahrgangDaten, new RouteDataKatalogJahrgaengeDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.id === id));
		}
	}

	protected async onSelect(item?: JahrgangsListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten = undefined;
		} else {
			this.data.item = item;
			this.data.daten = await routeLogin.data.api.getJahrgang(routeLogin.data.schema, item.id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patch: this.data.patch,
			item: this.data.item,
			data: this.data.daten,
			listJahrgaenge: routeKatalogJahrgaenge.liste.liste
		};
	}

}

export const routeKatalogJahrgaengeDaten = new RouteKatalogJahrgaengeDaten();

