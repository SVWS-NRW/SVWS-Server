import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteApp } from "~/router/RouteApp";
import { routeSchuleBenutzer } from "~/router/apps/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/RouteSchuleBenutzergruppe";
import { api } from "../Api";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core-ts";
import { RouteNode } from "../RouteNode";
import { SchuleAppProps } from "~/components/schule/SSchuleAppProps";

export class RouteDataSchule {

	setGostLupoImportMDBFuerJahrgang = async (formData: FormData) => {
		try {
			const res = await api.server.setGostLupoImportMDBFuerJahrgang( formData, api.schema);
			return res.success;
		} catch(e) {
			return false;
		}
	}
}
const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SSchuleApp = () => import("~/components/schule/SSchuleApp.vue")

export class RouteSchule extends RouteNode<RouteDataSchule, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule", "/schule", SSchuleApp, new RouteDataSchule());
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
		super.defaultChild = routeSchuleBenutzer;
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<undefined> {
		// TODO
		return computed({ get(): undefined { return undefined; }, set(value: undefined) { }});
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): SchuleAppProps {
		return {
			schuleStammdaten: api.schuleStammdaten,
			setGostLupoImportMDBFuerJahrgang: this.data.setGostLupoImportMDBFuerJahrgang
		};
	}

}

export const routeSchule = new RouteSchule();
