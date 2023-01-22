import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { ListFaecher } from "~/apps/faecher/ListFaecher";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeFaecherDaten } from "~/router/apps/faecher/RouteFaecherDaten";
import { RouteNode } from "~/router/RouteNode";
import { RouteApp } from "~/router/RouteApp";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";

export class RouteDataKatalogFaecher {
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
	item: FaecherListeEintrag | undefined = undefined;
}

const SFaecherAuswahl = () => import("~/components/faecher/SFaecherAuswahl.vue")
const SFaecherApp = () => import("~/components/faecher/SFaecherApp.vue")

export class RouteKatalogFaecher extends RouteNodeListView<ListFaecher, FaecherListeEintrag, RouteDataKatalogFaecher, RouteApp> {

	public constructor() {
		super("faecher", "/kataloge/faecher/:id(\\d+)?", SFaecherAuswahl, SFaecherApp, new ListFaecher(), 'id', new RouteDataKatalogFaecher());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
		super.setView("liste", SFaecherAuswahl, (route) => this.getProps(route));
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
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
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
		if (item === this.data.item)
			return;
		if (item === undefined) {
			this.data.item = undefined;
		} else {
			this.data.item = item;
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<FaecherListeEintrag | undefined> {
		return this.getSelector();
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to),
			schule: this.data.schule
		};
	}

}

export const routeKatalogFaecher = new RouteKatalogFaecher();
