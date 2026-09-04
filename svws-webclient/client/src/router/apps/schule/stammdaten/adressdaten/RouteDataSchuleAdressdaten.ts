import type { RouteStateInterface } from "~/router/RouteData";
import { RouteData } from "~/router/RouteData";
import { api } from "~/router/Api";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import type { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import type { Teilstandort } from "@core/core/data/schule/Teilstandort";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

interface RouteStateSchuleAdressdaten extends RouteStateInterface {
	listTeilstandorte: List<Teilstandort>;
}

const defaultState = <RouteStateSchuleAdressdaten>{
	listTeilstandorte: new ArrayList(),
};

export class RouteDataSchuleAdressdaten extends RouteData<RouteStateSchuleAdressdaten> {

	public constructor() {
		super(defaultState);
	}

	public async ladeDaten() {
		const listTeilstandorte = await api.server.getTeilstandorte(api.schema);
		this._state.value.listTeilstandorte = listTeilstandorte;
		this.setPatchedState({ listTeilstandorte });
	}

	patch = async (data: Partial<SchuleStammdaten>) => {
		await schuleStateImpl.patchStammdaten(data);
		this.commit();
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

	patchTeilstandorteintrag = async (data: Partial<Teilstandort>, adrMerkmal: string): Promise<void> => {
		await api.server.patchTeilstandort(data, api.schema, adrMerkmal);
		const listTeilstandorte = this.getListTeilstandorte;
		for (const teilstandort of listTeilstandorte) {
			if (teilstandort.adrMerkmal === adrMerkmal) {
				Object.assign(teilstandort, data);
				break;
			}
		}
		this.setPatchedState({ listTeilstandorte });
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
