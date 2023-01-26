import { JahrgangsListeEintrag, KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";
import { DataKlasse } from "~/apps/klassen/DataKlasse";
import { RouteNode } from "~/router/RouteNode";
import { RouteKlassen, routeKlassen } from "../RouteKlassen";

export class RouteDataKlassenDaten {
	item: KlassenListeEintrag | undefined = undefined;
	daten: DataKlasse = new DataKlasse();
	listJahrgaenge: ListJahrgaenge = new ListJahrgaenge();
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
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
			await this.data.daten.unselect();
		} else {
			this.data.item = item;
			await this.data.daten.select(this.data.item);
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeKlassen.getProps(to),
			data: this.data.daten,
			listJahrgaenge: this.data.listJahrgaenge,
			mapJahrgaenge: this.data.mapJahrgaenge
		};
	}

}

export const routeKlassenDaten = new RouteKlassenDaten();

