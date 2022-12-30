import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized } from "vue-router";
import { RouteNodeListView } from "../RouteNodeListView";
import { routeSchuleBenutzer } from "./RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "./RouteSchuleBenutzergruppe";

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
		super.menu = [
			// TODO { title: "Schule bearbeiten", value: "schule_bearbeiten" },
			// TODO { title: "Einstellungen", value: "einstellungen" },
			// TODO { title: "Datenaustausch", value: "datenaustausch" },
			// TODO { title: "Datensicherung", value: "datensicherung" },
			// TODO { title: "Schuljahreswechsel", value: "schuljahreswechsel" },
			// TODO { title: "Werkzeuge", value: "werkzeuge" },
			routeSchuleBenutzer,
			routeSchuleBenutzergruppe
			// TODO { title: "Hilfe", value: "hilfe" }
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
