import { JahrgangsListeEintrag, KursListeEintrag, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { ListKurse } from "~/apps/kurse/ListKurse";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { routeKurseDaten } from "~/router/apps/kurse/RouteKurseDaten";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "../RouteApp";
import { routeLogin } from "../RouteLogin";

export class RouteDataKurse {
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();
}

const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue")
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue")

export class RouteKurse extends RouteNodeListView<ListKurse, KursListeEintrag, RouteDataKurse, RouteApp> {

	public constructor() {
		super("kurse", "/kurse/:id(\\d+)?", SKurseAuswahl, SKurseApp, new ListKurse(), 'id', new RouteDataKurse());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Kurse";
		super.setView("liste", SKurseAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKurseDaten
		];
		super.defaultChild = routeKurseDaten;
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
		const listJahrgaenge = await routeLogin.data.api.getJahrgaenge(routeLogin.data.schema);
		const mapJahrgaenge = new Map<number, JahrgangsListeEintrag>();
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j);
		this.data.mapJahrgaenge = mapJahrgaenge;
		// Laden des Lehrer-Katalogs
		const listLehrer = await routeLogin.data.api.getLehrer(routeLogin.data.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		this.data.mapLehrer = mapLehrer;
		// Die Auswahlliste wird als letztes geladen
		await this.liste.update_list();
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.liste.liste.find(k => k.id === id));
		}
	}

	protected async onSelect(item?: KursListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
		} else {
			this.item = item;
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<KursListeEintrag | undefined> {
		return this.getSelector();
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			item: this._item,
			mapJahrgaenge: this.data.mapJahrgaenge,
			mapLehrer: this.data.mapLehrer,
			abschnitte: routeApp.data.schuleStammdaten.abschnitte,
			aktAbschnitt: routeApp.data.aktAbschnitt,
			setAbschnitt: routeApp.data.setAbschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			item: this._item,
			mapLehrer: this.data.mapLehrer
		};
	}

}

export const routeKurse = new RouteKurse();
