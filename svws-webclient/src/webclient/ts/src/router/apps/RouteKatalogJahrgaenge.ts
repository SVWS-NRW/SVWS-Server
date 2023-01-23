import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKatalogJahrgaengeDaten } from "~/router/apps/jahrgaenge/RouteKatalogJahrgaengeDaten";
import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
import { RouteNode } from "~/router/RouteNode";
import { RouteApp } from "~/router/RouteApp";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

export class RouteDataKatalogJahrgaenge {
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
}

const SJahrgaengeAuswahl = () => import("~/components/jahrgaenge/SJahrgaengeAuswahl.vue")
const SJahrgaengeApp = () => import("~/components/jahrgaenge/SJahrgaengeApp.vue")

export class RouteKatalogJahrgaenge extends RouteNodeListView<ListJahrgaenge, JahrgangsListeEintrag, RouteDataKatalogJahrgaenge, RouteApp> {

	public constructor() {
		super("jahrgaenge", "/kataloge/jahrgaenge/:id(\\d+)?", SJahrgaengeAuswahl, SJahrgaengeApp, new ListJahrgaenge(), 'id', new RouteDataKatalogJahrgaenge());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgänge";
		super.setView("liste", SJahrgaengeAuswahl, (route) => this.getProps(route));
		super.children = [
			routeKatalogJahrgaengeDaten
		];
		super.defaultChild = routeKatalogJahrgaengeDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.liste.update_list();
			return { name: redirect_name, params: { id: this.liste.liste.at(0)?.id }};
		}
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		await this.liste.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.liste.liste.find(k => k.id === id));
		}
	}

	protected async onSelect(item?: JahrgangsListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
		} else {
			this.item = item;
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<JahrgangsListeEintrag | undefined> {
		return this.getSelector();
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to),
			schule: this.data.schule,
		};
	}

}

export const routeKatalogJahrgaenge = new RouteKatalogJahrgaenge();
