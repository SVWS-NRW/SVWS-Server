import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzergruppeProps } from "~/components/schule/benutzergruppen/daten/SBenutzergruppeProps";
import { RouteNode } from "~/router/RouteNode";
import { RouteSchuleBenutzergruppe, routeSchuleBenutzergruppe } from "../schule/RouteSchuleBenutzergruppe";
import { RouteDataSchuleBenutzergruppe } from "./RouteDataSchuleBenutzergruppe";



const SBenutzergruppe = () => import("~/components/schule/benutzergruppen/daten/SBenutzergruppe.vue");



export class RouteSchuleBenutzergruppeDaten extends RouteNode<unknown, RouteSchuleBenutzergruppe> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "benutzergruppe_daten", "daten", SBenutzergruppe, new RouteDataSchuleBenutzergruppe());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {

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
			removeBenutzerFromBenutzergruppe : routeSchuleBenutzergruppe.data.removeBenutzerFromBenutzergruppe
		};
	}

}

export const routeSchuleBenutzergruppeDaten = new RouteSchuleBenutzergruppeDaten();

