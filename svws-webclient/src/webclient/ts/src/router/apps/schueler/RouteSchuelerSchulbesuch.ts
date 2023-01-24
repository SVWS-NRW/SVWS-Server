import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { DataSchuelerSchulbesuchsdaten } from "~/apps/schueler/DataSchuelerSchulbesuchsdaten";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";

export class RouteDataSchuelerSchulbesuch {
	item: SchuelerListeEintrag | undefined = undefined;
	daten: DataSchuelerSchulbesuchsdaten = new DataSchuelerSchulbesuchsdaten();
}

const SSchuelerSchulbesuch = () => import("~/components/schueler/schulbesuch/SSchuelerSchulbesuch.vue");

export class RouteSchuelerSchulbesuch extends RouteNode<RouteDataSchuelerSchulbesuch, RouteSchueler> {

	public constructor() {
		super("schueler_schulbesuch", "schulbesuch", SSchuelerSchulbesuch, new RouteDataSchuelerSchulbesuch());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schulbesuch";
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
			await this.data.daten.unselect();
		} else {
			this.data.item = item;
			await this.data.daten.select(this.data.item);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeSchueler.getProps(to),
			data: this.data.daten
		};
	}

}

export const routeSchuelerSchulbesuch = new RouteSchuelerSchulbesuch();

