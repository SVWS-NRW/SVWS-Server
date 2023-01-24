import { RouteNode } from "~/router/RouteNode";
import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { DataKatalogFahrschuelerarten } from "~/apps/schueler/DataKatalogFahrschuelerarten";
import { DataKatalogFoerderschwerpunkte } from "~/apps/schueler/DataKatalogFoerderschwerpunkte";
import { RouteLocationNormalized, RouteParams } from "vue-router";
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

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const tmp = parseInt(to_params.id as string);
			await this.onSelect(this.parent!.liste.liste.find(s => s.id === tmp));
		}
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
		return {
			...routeSchueler.getProps(to),
			fachschuelerarten: this.data.fachschuelerarten,
			foerderschwerpunkte: this.data.foerderschwerpunkte
		};
	}

}

export const routeSchuelerIndividualdaten = new RouteSchuelerIndividualdaten();

