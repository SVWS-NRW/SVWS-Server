import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeEinstellungen } from "~/router/apps/einstellungen/RouteEinstellungen";
import { routeEinstellungenBenutzergruppe, type RouteEinstellungenBenutzergruppe } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppe";

import type { BenutzergruppeProps } from "~/components/einstellungen/benutzergruppen/daten/SBenutzergruppeProps";

const SBenutzergruppe = () => import("~/components/einstellungen/benutzergruppen/daten/SBenutzergruppe.vue");

export class RouteEinstellungenBenutzergruppeDaten extends RouteNode<any, RouteEinstellungenBenutzergruppe> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "einstellungen.benutzergruppe.daten", "daten", SBenutzergruppe);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzergruppe";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (routeEinstellungenBenutzergruppe.data.auswahl === undefined )
			return routeEinstellungenBenutzergruppe.getRoute(undefined);
	}

	public getProps(to: RouteLocationNormalized): BenutzergruppeProps {
		return {
			auswahl: () => routeEinstellungenBenutzergruppe.data.auswahl,
			listBenutzerAlle: () => routeEinstellungenBenutzergruppe.data.listBenutzerAlle,
			listBenutzergruppenBenutzer: () => routeEinstellungenBenutzergruppe.data.listBenutzergruppenBenutzer,
			aktualisiereListeBenutzerGruppenBenutzer : routeEinstellungenBenutzergruppe.data.aktualisiereListeBenutzerGruppenBenutzer,
			getBenutzergruppenManager : routeEinstellungenBenutzergruppe.data.getBenutzergruppenManager,
			setBezeichnung : routeEinstellungenBenutzergruppe.data.setBezeichnung,
			setIstAdmin : routeEinstellungenBenutzergruppe.data.setIstAdmin,
			addKompetenz : routeEinstellungenBenutzergruppe.data.addKompetenz,
			removeKompetenz : routeEinstellungenBenutzergruppe.data.removeKompetenz,
			addBenutzerKompetenzGruppe : routeEinstellungenBenutzergruppe.data.addBenutzerKompetenzGruppe,
			removeBenutzerKompetenzGruppe : routeEinstellungenBenutzergruppe.data.removeBenutzerKompetenzGruppe,
			create : routeEinstellungenBenutzergruppe.data.create,
			addBenutzerToBenutzergruppe : routeEinstellungenBenutzergruppe.data.addBenutzerToBenutzergruppe,
			removeBenutzerFromBenutzergruppe : routeEinstellungenBenutzergruppe.data.removeBenutzerFromBenutzergruppe,
			gotoBenutzer: routeEinstellungenBenutzergruppe.data.gotoBenutzer,
			benutzerKompetenzen: routeEinstellungen.benutzerKompetenzen
		};
	}

}

export const routeEinstellungenBenutzergruppeDaten = new RouteEinstellungenBenutzergruppeDaten();

