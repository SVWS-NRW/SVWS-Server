import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
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
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.id === id));
		}
	}

	protected async onSelect(item?: ReligionEintrag) {
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
			item: this.data.item,
			data: this.data.daten
		};
	}

}

export const routeKatalogReligionDaten = new RouteKatalogReligionDaten();

