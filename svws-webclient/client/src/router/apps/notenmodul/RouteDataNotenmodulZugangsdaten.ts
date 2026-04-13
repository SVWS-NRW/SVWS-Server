import { HashMap, type JavaMap } from "@core";
import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeLehrer } from "../lehrer/RouteLehrer";


interface RouteStateNotenmodulZugangsdaten extends RouteStateInterface {
	mapInitialKennwoerter: JavaMap<number, string>;
}


export class RouteDataNotenmodulZugangsdaten extends RouteData<RouteStateNotenmodulZugangsdaten> {

	public constructor() {
		super(<RouteStateNotenmodulZugangsdaten>{
			idSchuljahresabschnitt: -1,
			mapInitialKennwoerter: new HashMap<number, string>(),
		});
	}

	public async entferneDaten() {
		this.setPatchedState({
			mapInitialKennwoerter: new HashMap<number, string>(),
		});
	}


	/**
	 * Initialisiert die Daten der Route. Wird beim Betreten der Ansicht ausgeführt.
	 */
	public async init() {
		// ... lese die Liste mit den Initialkennwörtern der Lehrer aus den ENM-Daten ein ...
		const mapInitialKennwoerter = await this.getEnmLehrerInitialKennwoerter();
		// ... und aktualisiere den State
		this.setPatchedState({ mapInitialKennwoerter });
	}

	private async getEnmLehrerInitialKennwoerter(): Promise<JavaMap<number, string>> {
		const mapInitialKennwoerter = new HashMap<number, string>();
		try {
			const daten = await api.server.getENMLehrerInitialKennwoerter(api.schema);
			for (const eintrag of daten) {
				if (eintrag.initialKennwort !== null) {
					mapInitialKennwoerter.put(eintrag.id, eintrag.initialKennwort);
				}
			}
		} catch (e) {
			console.log(e);
			mapInitialKennwoerter.clear();
		}
		return mapInitialKennwoerter;
	}

	get mapEnmInitialKennwoerter(): JavaMap<number, string> {
		return this._state.value.mapInitialKennwoerter;
	}

	public open = async (id: number): Promise<void> => {
		await RouteManager.doRoute(routeLehrer.getRoute({ id }));
	};

	public resetTotp = async (id: number): Promise<boolean> => {
		await api.server.resetENMLehrerTotpSecret(api.schema, id);
		return true;
	};

	public updatePassword = async (value: string | null, id: number): Promise<boolean> => {
		if (value === null) {
			await api.server.resetENMLehrerPasswordToInitial(api.schema, id);
		} else {
			await api.server.setENMLehrerPassword(value, api.schema, id);
		}
		return true;
	};
}
