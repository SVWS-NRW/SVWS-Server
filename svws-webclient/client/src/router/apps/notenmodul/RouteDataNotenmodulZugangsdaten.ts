import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { routeLehrer } from "../lehrer/RouteLehrer";
import { HashMap } from "@core/java/util/HashMap";
import type { JavaMap } from "@core/java/util/JavaMap";
import type { List } from "@core/java/util/List";


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
	public async init(idsLehrer: List<number>) {
		// ... lese die Liste mit den Initialkennwörtern der Lehrer aus den ENM-Daten ein ...
		const mapInitialKennwoerter = await this.getEnmLehrerInitialKennwoerter(idsLehrer);
		// ... und aktualisiere den State
		this.setPatchedState({ mapInitialKennwoerter });
	}

	private async getEnmLehrerInitialKennwoerter(idsLehrer: List<number>): Promise<JavaMap<number, string>> {
		const mapInitialKennwoerter = new HashMap<number, string>();
		try {
			const daten = await api.server.getENMLehrerInitialKennwoerterByIds(idsLehrer, api.schema);
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

	public resetPassword = async (id: number): Promise<void> => {
		await api.server.resetENMLehrerPasswordToInitial(api.schema, id);
	};

	public generateInitialPassword = async (id: number): Promise<string> => {
		return await api.server.generateENMLehrerInitialPassword(api.schema, id);
	};

	public set2fa = async (value: number, id: number): Promise<boolean> => {
		await api.server.setENMLehrerArt2FA(value, api.schema, id);
		return true;
	};
}
