import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { computed, WritableComputedRef } from "vue";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { RouteManager } from "../RouteManager";
import { LehrerAppProps } from "~/components/lehrer/SLehrerAppProps";
import { LehrerAuswahlProps } from "~/components/lehrer/SLehrerAuswahlProps";
import { RouteDataLehrer } from "./lehrer/RouteDataLehrer";
import { api } from "../Api";


const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue")
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue")


export class RouteLehrer extends RouteNode<RouteDataLehrer, RouteApp> {

	public constructor() {
		super("lehrer", "/lehrkraefte/:id(\\d+)?", SLehrerApp, new RouteDataLehrer());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrkräfte";
		super.setView("liste", SLehrerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerUnterrichtsdaten
		];
		super.defaultChild = routeLehrerIndividualdaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.setSchuljahresabschnitt(routeApp.data.aktAbschnitt.value.id);
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idLehrer = !to_params.id ? undefined : parseInt(to_params.id);
		const eintrag = (idLehrer !== undefined) ? this.data.mapLehrer.get(idLehrer) : this.data.firstLehrer;
		await this.data.setLehrer(eintrag);
		if (!this.data.hatStammdaten) {
			if (to.name === this.name)
				return;
			return this.getRoute(undefined);
		}
		if (to.name === this.name)
			return this.getChildRoute(this.data.stammdaten.id);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getChildRoute(id: number | undefined) : RouteLocationRaw {
		const redirect_name: string = (routeLehrer.selectedChild === undefined) ? routeLehrerIndividualdaten.name : routeLehrer.selectedChild.name;
		return { name: redirect_name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): LehrerAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapLehrer: this.data.mapLehrer,
			setLehrer: this.data.gotoLehrer,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			setAbschnitt: routeApp.data.setAbschnitt
		};
	}
	public getProps(to: RouteLocationNormalized): LehrerAppProps {
		return {
			stammdaten:  this.data.auswahl === undefined ? undefined : this.data.stammdaten,
		};
	}

	public get childRouteSelector() : WritableComputedRef<RouteRecordRaw> {
		return computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				void RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
			}
		});
	}

}

export const routeLehrer = new RouteLehrer();