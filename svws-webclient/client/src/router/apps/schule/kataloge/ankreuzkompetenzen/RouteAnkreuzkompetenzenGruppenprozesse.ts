import { RouteNode } from "~/router/RouteNode";
import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { RouteAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import { routeAnkreuzkompetenzen } from "~/router/apps/schule/kataloge/ankreuzkompetenzen/RouteAnkreuzkompetenzen";
import type { AnkreuzkompetenzenGruppenprozesseProps } from "~/components/schule/kataloge/ankreuzkompetenzen/gruppenprozesse/AnkreuzkompetenzenGruppenprozesseProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const AnkreuzkompetenzenGruppenprozesse = () => import(
	"~/components/schule/kataloge/ankreuzkompetenzen/gruppenprozesse/AnkreuzkompetenzenGruppenprozesse.vue");

class RouteAnkreuzkompetenzenGruppenprozesse extends RouteNode<any, RouteAnkreuzkompetenzen> {
	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN], "schule.ankreuzkompetenzen.gruppenprozesse", "gruppenprozesse", AnkreuzkompetenzenGruppenprozesse);
		super.types = new Set([ViewType.GRUPPENPROZESSE]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Gruppenprozesse";
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): AnkreuzkompetenzenGruppenprozesseProps {
		return {
			manager: () => routeAnkreuzkompetenzen.data.manager,
			delete: routeAnkreuzkompetenzen.data.delete,
			deleteCheck: routeAnkreuzkompetenzen.data.deleteCheck,
			gotoDefaultView: routeAnkreuzkompetenzen.data.gotoDefaultView,
		};
	}
}

export const routeAnkreuzkompetenzenGruppenprozesse = new RouteAnkreuzkompetenzenGruppenprozesse();

