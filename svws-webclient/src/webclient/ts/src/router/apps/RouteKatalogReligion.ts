import { ReligionEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKatalogReligionDaten } from "~/router/apps/religion/RouteKatalogReligionDaten";
import { ListReligionen } from "~/apps/kataloge/religionen/ListReligionen";
import { RouteNode } from "~/router/RouteNode";
import { RouteApp } from "~/router/RouteApp";


const SReligionenAuswahl = () => import("~/components/kataloge/religionen/SReligionenAuswahl.vue")
const SReligionenApp = () => import("~/components/kataloge/religionen/SReligionenApp.vue")

export class RouteKatalogReligion extends RouteNodeListView<ListReligionen, ReligionEintrag, unknown, RouteApp> {

	public constructor() {
		super("religionen", "/kataloge/religion/:id(\\d+)?", SReligionenAuswahl, SReligionenApp, new ListReligionen(), 'id');
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religion";
		super.setView("liste", SReligionenAuswahl, (route) => this.getProps(route));
		super.children = [
			routeKatalogReligionDaten
		];
		super.defaultChild = routeKatalogReligionDaten;
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
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.liste.liste.find(k => k.id === id));
		}
	}

	protected async onSelect(item?: ReligionEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
		} else {
			this.item = item;
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<ReligionEintrag | undefined> {
		return this.getSelector();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to)
		};
	}

}

export const routeKatalogReligion = new RouteKatalogReligion();
