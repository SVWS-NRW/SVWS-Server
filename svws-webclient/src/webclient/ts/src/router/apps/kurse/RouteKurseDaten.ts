import { RouteNode } from "~/router/RouteNode";
import { RouteKurse, routeKurse } from "~/router/apps/RouteKurse";
import { KursDaten, KursListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeLogin } from "~/router/RouteLogin";

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
		//await routeLogin.data.api.patchKursDaten(data, routeLogin.data.schema, this.item.id);
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
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.id === id));
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
			this.data.daten = await routeLogin.data.api.getKurs(routeLogin.data.schema, item.id);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patch: this.data.patch,
			mapJahrgaenge: routeKurse.data.mapJahrgaenge,
			mapLehrer: routeKurse.data.mapLehrer,
			data: this.data.daten
		};
	}

}

export const routeKurseDaten = new RouteKurseDaten();

