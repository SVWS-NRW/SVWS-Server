import { RouteNode } from "~/router/RouteNode";
import { routeKatalogJahrgaenge, RouteKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";
import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { DataJahrgang } from "~/apps/kataloge/jahrgaenge/DataJahrgang";
import { RouteLocationNormalized, RouteParams } from "vue-router";

export class RouteDataKatalogJahrgaengeDaten {
	item: JahrgangsListeEintrag | undefined = undefined;
	daten: DataJahrgang = new DataJahrgang();
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
			await this.data.daten.unselect();
		} else {
			this.data.item = item;
			await this.data.daten.select(this.data.item);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeKatalogJahrgaenge.getProps(to),
			data: this.data.daten,
			listJahrgaenge: routeKatalogJahrgaenge.liste.liste
		};
	}

}

export const routeKatalogJahrgaengeDaten = new RouteKatalogJahrgaengeDaten();

