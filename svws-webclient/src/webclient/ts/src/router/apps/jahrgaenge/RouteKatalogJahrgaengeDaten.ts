import { JahrgangsDaten, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { JahrgangDatenProps } from "~/components/kataloge/jahrgaenge/daten/SJahrgangDatenProps";
import { api } from "~/router/Api";
import { routeKatalogJahrgaenge, RouteKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { RouteNode } from "~/router/RouteNode";

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
		//await api.server.patchJahrgangDaten(data, api.schema, this.item.id);
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
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.onSelect(this.parent.data.mapJahrgaenge.get(id));
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
			this.data.daten = await api.server.getJahrgang(api.schema, item.id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): JahrgangDatenProps {
		return {
			patch: this.data.patch,
			data: this.data.daten,
			mapJahrgaenge: routeKatalogJahrgaenge.data.mapJahrgaenge
		};
	}

}

export const routeKatalogJahrgaengeDaten = new RouteKatalogJahrgaengeDaten();

