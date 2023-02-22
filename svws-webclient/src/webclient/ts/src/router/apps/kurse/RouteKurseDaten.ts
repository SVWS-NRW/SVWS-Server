import { KursDaten, KursListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { KursDatenProps } from "~/components/kurse/daten/SKursDatenProps";
import { api } from "~/router/Api";
import { RouteKurse, routeKurse } from "~/router/apps/RouteKurse";
import { RouteNode } from "~/router/RouteNode";

export class RouteDataKurseDaten {
	item: KursListeEintrag | undefined = undefined;
	private _daten: KursDaten | undefined = undefined;

	get daten(): KursDaten {
		if (this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Lehrerstammdaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: KursDaten | undefined) {
		this._daten = value;
	}

	patch = async (data : Partial<KursDaten>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchKursDaten", data);
		//await api.server.patchKursDaten(data, api.schema, this.item.id);
	}
}

const SKursDaten = () => import("~/components/kurse/daten/SKursDaten.vue");

export class RouteKurseDaten extends RouteNode<RouteDataKurseDaten, RouteKurse> {

	public constructor() {
		super("kurse_daten", "daten", SKursDaten, new RouteDataKurseDaten());
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
			await this.onSelect(this.parent.data.mapKurse.get(id));
		}
	}

	protected async onSelect(item?: KursListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten = undefined;
		} else {
			this.data.item = item;
			this.data.daten = await api.server.getKurs(api.schema, item.id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): KursDatenProps {
		return {
			patch: this.data.patch,
			mapJahrgaenge: routeKurse.data.mapJahrgaenge,
			mapLehrer: routeKurse.data.mapLehrer,
			data: this.data.daten
		};
	}

}

export const routeKurseDaten = new RouteKurseDaten();

