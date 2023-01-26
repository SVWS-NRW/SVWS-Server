import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
import { RouteNodeListView } from "../RouteNodeListView";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { WritableComputedRef } from "vue";
import { RouteNode } from "~/router/RouteNode";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
import { RouteApp } from "~/router/RouteApp";


export class RouteDataLehrer {
	stammdaten: DataLehrerStammdaten = new DataLehrerStammdaten();
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
}


const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue")
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue")


export class RouteLehrer extends RouteNodeListView<ListLehrer, LehrerListeEintrag, RouteDataLehrer, RouteApp> {

	public constructor() {
		super("lehrer", "/lehrkraefte/:id(\\d+)?", SLehrerAuswahl, SLehrerApp, new ListLehrer(), 'id', new RouteDataLehrer());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrkräfte";
		super.setView("liste", SLehrerAuswahl, (route) => this.getProps(route));
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerUnterrichtsdaten
		];
		super.defaultChild = routeLehrerIndividualdaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.liste.update_list();
			return { name: redirect_name, params: { id: this.liste.liste.at(0)?.id }};  // TODO auswahl.gefiltert statt auswahl.liste nutzen
		}
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		await this.liste.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.liste.liste.find(l => l.id === id));
		}
	}

	protected async onSelect(item?: LehrerListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
			await this.data.stammdaten.unselect();
		} else {
			this.item = item;
			await this.data.stammdaten.select(this.item);
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<LehrerListeEintrag | undefined> {
		return this.getSelector();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to),
			stammdaten: this.data.stammdaten,
			schule: this.data.schule
		};
	}

}

export const routeLehrer = new RouteLehrer();