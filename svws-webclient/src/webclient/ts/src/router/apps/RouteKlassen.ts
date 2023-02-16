import { KlassenListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import { ListKlassen } from "~/apps/klassen/ListKlassen";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "~/router/RouteApp";

export class RouteDataKlassen {
	listLehrer: ListLehrer = new ListLehrer();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();
}

const SKlassenAuswahl = () => import("~/components/klassen/SKlassenAuswahl.vue")
const SKlassenApp = () => import("~/components/klassen/SKlassenApp.vue")

export class RouteKlassen extends RouteNodeListView<ListKlassen, KlassenListeEintrag, RouteDataKlassen, RouteApp> {

	public constructor() {
		super("klassen", "/klassen/:id(\\d+)?", SKlassenAuswahl, SKlassenApp, new ListKlassen(), 'id', new RouteDataKlassen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klassen";
		super.setView("liste", SKlassenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKlassenDaten
		];
		super.defaultChild = routeKlassenDaten;
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
		await this.data.listLehrer.update_list();
		this.data.mapLehrer.clear();
		this.data.listLehrer.liste.forEach(l => this.data.mapLehrer.set(l.id, l));
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

	protected async onSelect(item?: KlassenListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
		} else {
			this.item = item;
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<KlassenListeEintrag | undefined> {
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
			mapLehrer: this.data.mapLehrer
		};
	}

}

export const routeKlassen = new RouteKlassen();
