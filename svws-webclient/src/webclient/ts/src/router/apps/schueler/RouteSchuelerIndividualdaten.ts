import { RouteNode } from "~/router/RouteNode";
import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { DataKatalogFahrschuelerarten } from "~/apps/schueler/DataKatalogFahrschuelerarten";
import { DataKatalogFoerderschwerpunkte } from "~/apps/schueler/DataKatalogFoerderschwerpunkte";
import { RouteLocationNormalized } from "vue-router";
import { RouteSchueler, routeSchueler } from "../RouteSchueler";

const SSchuelerIndividualdaten = () => import("~/components/schueler/individualdaten/SSchuelerIndividualdaten.vue");

export class RouteDataSchuelerIndividualdaten {
	item: SchuelerListeEintrag | undefined = undefined;
	fachschuelerarten: DataKatalogFahrschuelerarten = new DataKatalogFahrschuelerarten();
	foerderschwerpunkte: DataKatalogFoerderschwerpunkte = new DataKatalogFoerderschwerpunkte();
}

export class RouteSchuelerIndividualdaten extends RouteNode<RouteDataSchuelerIndividualdaten, RouteSchueler> {

	public constructor() {
		super("schueler_daten", "daten", SSchuelerIndividualdaten, new RouteDataSchuelerIndividualdaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Individualdaten";
	}

	protected async onSelect(item?: SchuelerListeEintrag) {
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
			await this.data.fachschuelerarten.unselect();
			await this.data.foerderschwerpunkte.unselect();
		} else {
			if (this.data.item === undefined) {
				await this.data.fachschuelerarten.select(undefined);
				await this.data.foerderschwerpunkte.select(undefined);
			}
			this.data.item = item;
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop: Record<string, any> = routeSchueler.getProps(to);
		this.onSelect(prop.item.value);
		prop.fachschuelerarten = this.data.fachschuelerarten;
		prop.foerderschwerpunkte = this.data.foerderschwerpunkte;
		return prop;
	}

}

export const routeSchuelerIndividualdaten = new RouteSchuelerIndividualdaten();

