import { JahrgangsListeEintrag, KlassenDaten, KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";
import { routeLogin } from "~/router/RouteLogin";
import { RouteNode } from "~/router/RouteNode";
import { RouteKlassen, routeKlassen } from "../RouteKlassen";

export class RouteDataKlassenDaten {
	item: KlassenListeEintrag | undefined = undefined;
	listJahrgaenge: ListJahrgaenge = new ListJahrgaenge();
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
	private _daten: KlassenDaten | undefined = undefined;

	get daten(): KlassenDaten {
		if (this._daten === undefined)
			throw new Error("Unerwarteter Fehler: Klassendaten nicht initialisiert");
		return this._daten;
	}

	set daten(value: KlassenDaten | undefined) {
		this._daten = value;
	}

	patch = async (data : Partial<KlassenDaten>) => {
		if (this.item === undefined)
			throw new Error("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		console.log("TODO: Implementierung patchKlassenDaten", data);
		//await routeLogin.data.api.patchKursDaten(data, routeLogin.data.schema, this.item.id);
	}
}

const SKlassenDaten = () => import("~/components/klassen/daten/SKlassenDaten.vue");

export class RouteKlassenDaten extends RouteNode<RouteDataKlassenDaten, RouteKlassen> {

	public constructor() {
		super("klassen_daten", "daten", SKlassenDaten, new RouteDataKlassenDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.listJahrgaenge.update_list();
		this.data.mapJahrgaenge.clear();
		this.data.listJahrgaenge.liste.forEach(j => this.data.mapJahrgaenge.set(j.id, j));
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.id === id));
		}
	}

	protected async onSelect(item?: KlassenListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten = undefined;
		} else {
			this.data.item = item;
			this.data.daten = await routeLogin.data.api.getKlasse(routeLogin.data.schema, item.id)
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			patch: this.data.patch,
			data: this.data.daten,
			mapLehrer: routeKlassen.data.mapLehrer,
			mapJahrgaenge: this.data.mapJahrgaenge
		};
	}

}

export const routeKlassenDaten = new RouteKlassenDaten();

