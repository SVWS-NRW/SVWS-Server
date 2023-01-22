import { RouteNode } from "~/router/RouteNode";
import { routeSchuleBenutzer, RouteSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
import { DataBenutzer } from "~/apps/schule/benutzerverwaltung/DataBenutzer";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { ListBenutzergruppe } from "~/apps/schule/benutzerverwaltung/ListBenutzergruppe";

const SBenutzer = () => import("~/components/schule/benutzer/daten/SBenutzer.vue");

export class RouteDataSchuleBenutzerDaten {
	item: BenutzerListeEintrag | undefined = undefined;
	daten: DataBenutzer = new DataBenutzer();
	listBenutzergruppen: ListBenutzergruppe = new ListBenutzergruppe();
}

export class RouteSchuleBenutzerDaten extends RouteNode<RouteDataSchuleBenutzerDaten, RouteSchuleBenutzer> {

	public constructor() {
		super("benutzer_daten", "daten", SBenutzer, new RouteDataSchuleBenutzerDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.data.listBenutzergruppen.update_list();
			await this.onSelect(this.parent!.liste.liste.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: BenutzerListeEintrag) {
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
			...routeSchuleBenutzer.getProps(to),
			data: this.data.daten,
			benutzergruppen: this.data.listBenutzergruppen.liste
		};
	}

}

export const routeSchuleBenutzerDaten = new RouteSchuleBenutzerDaten();

