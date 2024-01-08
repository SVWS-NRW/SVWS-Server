import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleBenutzer, type RouteSchuleBenutzer } from "~/router/apps/schule/benutzer/RouteSchuleBenutzer";

import type { BenutzerProps } from "~/components/schule/benutzer/daten/SBenutzerProps";

const SBenutzer = () => import("~/components/schule/benutzer/daten/SBenutzer.vue");

export class RouteSchuleBenutzerDaten extends RouteNode<unknown, RouteSchuleBenutzer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "benutzer_daten", "daten", SBenutzer);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzer";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): BenutzerProps {
		return {
			listBenutzergruppen: routeSchuleBenutzer.data.listBenutzergruppen,
			getBenutzerManager : routeSchuleBenutzer.data.getBenutzerManager,
			setAnzeigename : routeSchuleBenutzer.data.setAnzeigename,
			setAnmeldename : routeSchuleBenutzer.data.setAnmeldename,
			setIstAdmin : routeSchuleBenutzer.data.setIstAdmin,
			setPassword : routeSchuleBenutzer.data.setPassword,
			addBenutzerToBenutzergruppe : routeSchuleBenutzer.data.addBenutzerToBenutzergruppe,
			removeBenutzerFromBenutzergruppe : routeSchuleBenutzer.data.removeBenutzerFromBenutzergruppe,
			addKompetenz : routeSchuleBenutzer.data.addKompetenz,
			removeKompetenz : routeSchuleBenutzer.data.removeKompetenz,
			addBenutzerKompetenzGruppe : routeSchuleBenutzer.data.addBenutzerKompetenzGruppe,
			removeBenutzerKompetenzGruppe : routeSchuleBenutzer.data.removeBenutzerKompetenzGruppe,
			gotoBenutzergruppe: routeSchuleBenutzer.data.gotoBenutzergruppe,
			benutzerKompetenzen: routeSchule.benutzerKompetenzen
		};
	}

}

export const routeSchuleBenutzerDaten = new RouteSchuleBenutzerDaten();

