import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { DataKatalogErzieherarten } from "~/apps/schueler/DataKatalogErzieherarten";
import { DataSchuelerErzieherStammdaten } from "~/apps/schueler/DataSchuelerErzieherStammdaten";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchueler, routeSchueler } from "~/router/apps/RouteSchueler";

const SSchuelerErziehungsberechtigte = () => import("~/components/schueler/erziehungsberechtigte/SSchuelerErziehungsberechtigte.vue");

export class RouteDataSchuelerErziehungsberechtigte {
	item: SchuelerListeEintrag | undefined = undefined;
	daten: DataSchuelerErzieherStammdaten = new DataSchuelerErzieherStammdaten();
	erzieherarten: DataKatalogErzieherarten = new DataKatalogErzieherarten();
}

export class RouteSchuelerErziehungsberechtigte extends RouteNode<RouteDataSchuelerErziehungsberechtigte, RouteSchueler> {

	public constructor() {
		super("schueler_erziehungsberechtigte", "erziehungsberechtigte", SSchuelerErziehungsberechtigte, new RouteDataSchuelerErziehungsberechtigte());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erziehungsberechtigte";
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
			await this.data.erzieherarten.unselect();
		} else {
			this.data.item = item;
			await this.data.daten.select(this.data.item);
			await this.data.erzieherarten.select(undefined);
		}
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...routeSchueler.getProps(to),
			data: this.data.daten,
			erzieherarten: this.data.erzieherarten
		};
	}

}

export const routeSchuelerErziehungsberechtigte = new RouteSchuelerErziehungsberechtigte();

