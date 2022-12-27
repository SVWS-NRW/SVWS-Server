import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized } from "vue-router";
import { RouteNodeListView } from "../RouteNodeListView";

const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SSchuleApp = () => import("~/components/schule/SSchuleApp.vue")

export class RouteSchule extends RouteNodeListView<unknown, unknown> {

	protected defaultChildNode = undefined;

	public constructor() {
		super("schule", "/schule", SSchuleAuswahl, SSchuleApp);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule";
		super.setView("liste", SSchuleAuswahl, (route) => this.getNoProps(route));
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

export const routeSchule = new RouteSchule();
