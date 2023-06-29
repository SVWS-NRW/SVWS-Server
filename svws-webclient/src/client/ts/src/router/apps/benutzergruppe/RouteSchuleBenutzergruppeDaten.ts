import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuleBenutzergruppe, type RouteSchuleBenutzergruppe } from "~/router/apps/schule/RouteSchuleBenutzergruppe";
import { routeSchule } from "~/router/apps/RouteSchule";

import type { BenutzergruppeProps } from "~/components/schule/benutzergruppen/daten/SBenutzergruppeProps";

const SBenutzergruppe = () => import("~/components/schule/benutzergruppen/daten/SBenutzergruppe.vue");

export class RouteSchuleBenutzergruppeDaten extends RouteNode<unknown, RouteSchuleBenutzergruppe> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "benutzergruppe_daten", "daten", SBenutzergruppe);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzergruppe";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if(routeSchuleBenutzergruppe.data.auswahl === undefined )
			return routeSchuleBenutzergruppe.getRoute(undefined);
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id: id }};
	}

	public getProps(to: RouteLocationNormalized): BenutzergruppeProps {
		return {
			auswahl: () => routeSchuleBenutzergruppe.data.auswahl,
			listBenutzerAlle: () => routeSchuleBenutzergruppe.data.listBenutzerAlle,
			listBenutzergruppenBenutzer: () => routeSchuleBenutzergruppe.data.listBenutzergruppenBenutzer,
			aktualisiereListeBenutzerGruppenBenutzer : routeSchuleBenutzergruppe.data.aktualisiereListeBenutzerGruppenBenutzer,
			getBenutzergruppenManager : routeSchuleBenutzergruppe.data.getBenutzergruppenManager,
			setBezeichnung : routeSchuleBenutzergruppe.data.setBezeichnung,
			setIstAdmin : routeSchuleBenutzergruppe.data.setIstAdmin,
			addKompetenz : routeSchuleBenutzergruppe.data.addKompetenz,
			removeKompetenz : routeSchuleBenutzergruppe.data.removeKompetenz,
			addBenutzerKompetenzGruppe : routeSchuleBenutzergruppe.data.addBenutzerKompetenzGruppe,
			removeBenutzerKompetenzGruppe : routeSchuleBenutzergruppe.data.removeBenutzerKompetenzGruppe,
			create : routeSchuleBenutzergruppe.data.create,
			addBenutzerToBenutzergruppe : routeSchuleBenutzergruppe.data.addBenutzerToBenutzergruppe,
			removeBenutzerFromBenutzergruppe : routeSchuleBenutzergruppe.data.removeBenutzerFromBenutzergruppe,
			goToBenutzer: this.gotToBenutzer,
			benutzerKompetenzen: routeSchule.benutzerKompetenzen
		};
	}

	public gotToBenutzer = async (b_id: number) => await RouteManager.doRoute({ name: "benutzer_daten", params: { id: b_id} });

}

export const routeSchuleBenutzergruppeDaten = new RouteSchuleBenutzergruppeDaten();

