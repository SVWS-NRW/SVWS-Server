import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { DataReligion } from "~/apps/kataloge/religionen/DataReligion";
import { RouteNode } from "~/router/RouteNode";
import { routeKatalogReligion, RouteKatalogReligion } from "../RouteKatalogReligion";

export class RouteDataKlassenDaten {
	item: ReligionEintrag | undefined = undefined;
	daten: DataReligion = new DataReligion();
}

const SReligionDaten = () => import("~/components/kataloge/religionen/daten/SReligionDaten.vue");

export class RouteKatalogReligionDaten extends RouteNode<RouteDataKlassenDaten, RouteKatalogReligion> {

	public constructor() {
		super("religionen_daten", "daten", SReligionDaten, new RouteDataKlassenDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

    public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			this.onSelect(this.parent!.liste.liste.find(s => s.id === id));
		}
	}

	protected onSelect(item?: ReligionEintrag) {
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
		return {
			...routeKatalogReligion.getProps(to),
			data: this.data.daten
		};
	}

}

export const routeKatalogReligionDaten = new RouteKatalogReligionDaten();

