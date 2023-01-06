import { JahrgangsListeEintrag, KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
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
			this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			this.onSelect(this.parent!.data.auswahl.liste.find(s => s.id === id));
		}
	}

	protected onSelect(item?: KlassenListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			this.data.daten.unselect();
		} else {
			this.data.item = item;
			this.data.daten.select(this.data.item);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		let prop: Record<string, any> = routeKlassen.getProps(to);
		prop.data = this.data.daten;
		prop.listJahrgaenge = this.data.listJahrgaenge;
		prop.mapJahrgaenge = this.data.mapJahrgaenge;
		return prop;
	}

}

export const routeKlassenDaten = new RouteKlassenDaten();

