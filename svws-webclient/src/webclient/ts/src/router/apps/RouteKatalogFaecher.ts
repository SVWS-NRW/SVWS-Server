import { FaecherListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { ListFaecher } from "~/apps/kataloge/faecher/ListFaecher";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeFaecherDaten } from "~/router/apps/faecher/RouteKatalogFaecherDaten";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "~/router/RouteApp";

export class RouteDataKatalogFaecher {
	item: FaecherListeEintrag | undefined = undefined;
}

const SFaecherAuswahl = () => import("~/components/kataloge/faecher/SFaecherAuswahl.vue")
const SFaecherApp = () => import("~/components/kataloge/faecher/SFaecherApp.vue")

export class RouteKatalogFaecher extends RouteNodeListView<ListFaecher, FaecherListeEintrag, RouteDataKatalogFaecher, RouteApp> {

	public constructor() {
		super("faecher", "/kataloge/faecher/:id(\\d+)?", SFaecherAuswahl, SFaecherApp, new ListFaecher(), 'id', new RouteDataKatalogFaecher());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
		super.setView("liste", SFaecherAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeFaecherDaten
		];
		super.defaultChild = routeFaecherDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.liste.update_list();
			return { name: redirect_name, params: { id: this.liste.gefiltert.at(0)?.id }};
		}
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.liste.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.liste.gefiltert.find(k => k.id === id));
		}
	}

	protected async onSelect(item?: FaecherListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
		} else {
			this.item = item;
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<FaecherListeEintrag | undefined> {
		return this.getSelector();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			item: this.item,
			abschnitte: routeApp.data.schuleStammdaten.abschnitte,
			aktAbschnitt: routeApp.data.aktAbschnitt,
			setAbschnitt: routeApp.data.setAbschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			item: this.item,
		};
	}

}

export const routeKatalogFaecher = new RouteKatalogFaecher();
