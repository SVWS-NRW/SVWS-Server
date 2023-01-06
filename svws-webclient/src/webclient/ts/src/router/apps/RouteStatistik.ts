import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized } from "vue-router";
import { RouteApp } from "../RouteApp";
import { RouteNodeListView } from "../RouteNodeListView";

const SStatistikAuswahl = () => import("~/components/statistik/SStatistikAuswahl.vue")
const SStatistikApp = () => import("~/components/statistik/SStatistikApp.vue")

export class RouteStatistik extends RouteNodeListView<unknown, unknown, RouteApp> {

	protected defaultChildNode = undefined;

	public constructor() {
		super("statistik", "/statistik", SStatistikAuswahl, SStatistikApp);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Statistik";
		super.setView("liste", SStatistikAuswahl, (route) => this.getNoProps(route));
		super.children = [
		];
	}

    protected getAuswahlComputedProperty(): WritableComputedRef<undefined> {
		// TODO
		return computed({ get(): undefined { return undefined; }, set(value: undefined) { }});
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		// TODO
		return { };
	}

}

export const routeStatistik = new RouteStatistik();
