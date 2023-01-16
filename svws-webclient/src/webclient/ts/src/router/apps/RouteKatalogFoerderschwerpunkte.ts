import { FoerderschwerpunktEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams } from "vue-router";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKatalogFoerderschwerpunkteDaten } from "~/router/apps/foerderschwerpunkte/RouteKatalogFoerderschwerpunkteDaten";
import { ListFoerderschwerpunkte } from "~/apps/kataloge/foerderschwerpunkt/ListFoerderschwerpunkte";
import { RouteNode } from "~/router/RouteNode";
import { RouteApp } from "~/router/RouteApp";


const SFoerderschwerpunkteAuswahl = () => import("~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteAuswahl.vue")
const SFoerderschwerpunkteApp = () => import("~/components/kataloge/foerderschwerpunkte/SFoerderschwerpunkteApp.vue")

export class RouteKatalogFoerderschwerpunkte extends RouteNodeListView<ListFoerderschwerpunkte, FoerderschwerpunktEintrag, unknown, RouteApp> {

	public constructor() {
		super("foerderschwerpunkte", "/kataloge/foerderschwerpunkte/:id(\\d+)?", SFoerderschwerpunkteAuswahl, SFoerderschwerpunkteApp, new ListFoerderschwerpunkte(), 'id');
		super.propHandler = (route) => this.getProps(route);
		super.text = "Förderschwerpunkte";
		super.setView("liste", SFoerderschwerpunkteAuswahl, (route) => this.getProps(route));
		super.children = [
			routeKatalogFoerderschwerpunkteDaten
		];
		super.defaultChild = routeKatalogFoerderschwerpunkteDaten;
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
		await this.liste.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			this.onSelect(this.liste.liste.find(f => f.id === id));
		}
	}

	protected onSelect(item?: FoerderschwerpunktEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
		} else {
			this.item = item;
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<FoerderschwerpunktEintrag | undefined> {
		return this.getSelector();
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to)
		};
	}

}

export const routeKatalogFoerderschwerpunkte = new RouteKatalogFoerderschwerpunkte();
