import { RouteNode } from "~/router/RouteNode";
import { BenutzergruppeListeEintrag } from "@svws-nrw/svws-core-ts";
import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";
import { ListBenutzer } from "~/apps/schule/benutzerverwaltung/ListBenutzer";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { RouteSchuleBenutzergruppe, routeSchuleBenutzergruppe } from "../RouteSchuleBenutzergruppe";

const SBenutzergruppe = () => import("~/components/schule/benutzergruppen/daten/SBenutzergruppe.vue");

export class RouteDataSchuleBenutzergruppeDaten {
	item: BenutzergruppeListeEintrag | undefined = undefined;
	daten: DataBenutzergruppe = new DataBenutzergruppe();
	listBenutzer: ListBenutzer = new ListBenutzer();
}

export class RouteSchuleBenutzergruppeDaten extends RouteNode<RouteDataSchuleBenutzergruppeDaten, RouteSchuleBenutzergruppe> {

	public constructor() {
		super("benutzergruppe_daten", "daten", SBenutzergruppe, new RouteDataSchuleBenutzergruppeDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.data.listBenutzer.update_list();
			await this.onSelect(this.parent!.liste.liste.find(f => f.id === id));
		}
	}

	protected async onSelect(item?: BenutzergruppeListeEintrag) {
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
			...routeSchuleBenutzergruppe.getProps(to),
			data: this.data.daten,
			benutzer: this.data.listBenutzer.liste
		};
	}

}

export const routeSchuleBenutzergruppeDaten = new RouteSchuleBenutzergruppeDaten();

