import { RouteData, type RouteStateInterface } from "~/router/RouteData";

interface RouteStateBenutzerprofil extends RouteStateInterface { }

const defaultState = <RouteStateBenutzerprofil> { };

export class RouteDataBenutzerprofilNutzereinstellungen extends RouteData<RouteStateBenutzerprofil> {

	public constructor() {
		super(defaultState);
	}

}
