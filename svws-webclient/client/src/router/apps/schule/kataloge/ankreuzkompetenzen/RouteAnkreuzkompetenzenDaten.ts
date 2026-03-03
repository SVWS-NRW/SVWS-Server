import { RouteNode } from "~/router/RouteNode";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteLocationNormalized } from "vue-router";
import { api } from "~/router/Api";
import type { RouteAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import { routeAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import type { AnkreuzkompetenzenDatenProps } from "~/components/schule/kataloge/ankreuzkompetenzen/daten/AnkreuzkompetenzenDatenProps";

const AnkreuzkompetenzenDaten = () => import("~/components/schule/kataloge/ankreuzkompetenzen/daten/AnkreuzkompetenzenDaten.vue");

class RouteAnkreuzkompetenzenDaten extends RouteNode<any, RouteAnkreuzkompetenzen> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.ankreuzkompetenzen.daten", "daten", AnkreuzkompetenzenDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ankreuzkompetenzen";
	}

	public getProps(to: RouteLocationNormalized): AnkreuzkompetenzenDatenProps {
		return {
			patch: routeAnkreuzkompetenzen.data.patch,
			manager: () => routeAnkreuzkompetenzen.data.manager,
			addJahrgaengezuordnungen: routeAnkreuzkompetenzen.data.addJahrgaengezuordnungen,
			deleteJahrgaengezuordnungen: routeAnkreuzkompetenzen.data.deleteJahrgaengezuordnungen,
			schuljahr: api.abschnitt.schuljahr,
			schulform: api.schulform,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeAnkreuzkompetenzenDaten = new RouteAnkreuzkompetenzenDaten();
