import type { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import type { Schulleitung } from "@core/asd/data/schule/Schulleitung";
import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import type { Teilstandort } from "@core/core/data/schule/Teilstandort";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

import { api } from "~/router/Api";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";
import { benutzerStateImpl } from "~/states/BenutzerStateImpl";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

interface RouteStateSchuleAdressdaten extends RouteStateInterface {
	listTeilstandorte: List<Teilstandort>;
	listLeitungsfunktionen: List<Schulleitung>;
	listLehrer: List<LehrerListeEintrag>;
}

const defaultState = <RouteStateSchuleAdressdaten>{
	listTeilstandorte: new ArrayList(),
	listLeitungsfunktionen: new ArrayList(),
	listLehrer: new ArrayList(),
};

export class RouteDataSchuleAdressdaten extends RouteData<RouteStateSchuleAdressdaten> {

	public constructor() {
		super(defaultState);
	}

	/**
	 * Lädt die Teilstandorte, die Leitungsfunktionen der Schule und die Lehrerliste für Kürzel und Name der Lehrkräfte.
	 * Ohne die Kompetenz zum Ansehen von Lehrerdaten werden keine Leitungsfunktionen und Lehrer geladen.
	 */
	public async ladeDaten() {
		const hatKompetenzLehrerdaten = benutzerStateImpl.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
		const [listTeilstandorte, listLeitungsfunktionen, listLehrer] = await Promise.all([
			api.server.getTeilstandorte(api.schema),
			hatKompetenzLehrerdaten ? api.server.getSchulleitungsfunktionen(api.schema) : new ArrayList<Schulleitung>(),
			hatKompetenzLehrerdaten ? api.server.getLehrer(api.schema) : new ArrayList<LehrerListeEintrag>(),
		]);
		this.setPatchedState({ listTeilstandorte, listLeitungsfunktionen, listLehrer });
	}

	get listLeitungsfunktionen(): List<Schulleitung> {
		return this._state.value.listLeitungsfunktionen;
	}

	get listLehrer(): List<LehrerListeEintrag> {
		return this._state.value.listLehrer;
	}

	gotoLehrer = async (idLehrer: number): Promise<void> => {
		await RouteManager.doRoute(routeLehrer.getRoute({ id: idLehrer }));
	};

	patch = async (data: Partial<SchuleStammdaten>): Promise<boolean> => {
		await schuleStateImpl.patchStammdaten(data);
		this.commit();
		return true;
	};

	get getListTeilstandorte(): List<Teilstandort> {
		const list = new ArrayList<Teilstandort>();
		list.addAll(this._state.value.listTeilstandorte);
		return list;
	}

	addTeilstandorteintrag = async (data: Partial<Teilstandort>): Promise<void> => {
		const teilstandort = await api.server.addTeilstandort(data, api.schema);
		const listTeilstandorte = this.getListTeilstandorte;
		listTeilstandorte.add(teilstandort);
		this.setPatchedState({ listTeilstandorte });
	};

	patchTeilstandorteintrag = async (data: Partial<Teilstandort>, adrMerkmal: string): Promise<boolean> => {
		await api.server.patchTeilstandort(data, api.schema, adrMerkmal);
		const listTeilstandorte = this.getListTeilstandorte;
		for (const teilstandort of listTeilstandorte) {
			if (teilstandort.adrMerkmal === adrMerkmal) {
				Object.assign(teilstandort, data);
				break;
			}
		}
		this.setPatchedState({ listTeilstandorte });
		return true;
	};

	deleteTeilstandorteintraege = async (adrMermale: List<string>): Promise<void> => {
		await api.server.deleteTeilstandorte(adrMermale, api.schema);
		const listTeilstandorte = this.getListTeilstandorte;
		for (const adrMerkmal of adrMermale) {
			for (let i = 0; i < listTeilstandorte.size(); i++) {
				const eintrag = listTeilstandorte.get(i);
				if (eintrag.adrMerkmal === adrMerkmal) {
					listTeilstandorte.removeElementAt(i);
					break;
				}
			}
		}
		this.setPatchedState({ listTeilstandorte });
	};
}
