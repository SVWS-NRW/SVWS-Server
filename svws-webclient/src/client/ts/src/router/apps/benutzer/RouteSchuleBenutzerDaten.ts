import { BenutzerKompetenz, BenutzerListeEintrag, Schulform } from "@svws-nrw/svws-core";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerProps } from "~/components/schule/benutzer/daten/SBenutzerProps";
import { routeSchuleBenutzer, RouteSchuleBenutzer } from "~/router/apps/schule/RouteSchuleBenutzer";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

const SBenutzer = () => import("~/components/schule/benutzer/daten/SBenutzer.vue");

export class RouteDataSchuleBenutzerDaten {
	auswahl: BenutzerListeEintrag | undefined = undefined;
}

export class RouteSchuleBenutzerDaten extends RouteNode<unknown, RouteSchuleBenutzer> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "benutzer_daten", "daten", SBenutzer, new RouteDataSchuleBenutzerDaten());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Daten";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		console.log("benutzer_daten->update");
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
			getGruppen4Kompetenz : routeSchuleBenutzer.data.getGruppen4Kompetenz,
			goToBenutzergruppe: this.gotToBenutzergruppe
		};
	}

	public gotToBenutzergruppe = async (b_id: number) => await RouteManager.doRoute({ name: "benutzergruppe_daten", params: { id: b_id} });
}

export const routeSchuleBenutzerDaten = new RouteSchuleBenutzerDaten();

