import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { api } from "~/router/Api";
import { configStateImpl } from "~/states/ConfigStateImpl";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { SchuelerVermerke } from "@core/core/data/schueler/SchuelerVermerke";
import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { JavaLong } from "@core/java/lang/JavaLong";
import { JavaString } from "@core/java/lang/JavaString";
import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import type { List } from "@core/java/util/List";


interface RouteStateSchuelerVermerke extends RouteStateInterface {
	auswahl: SchuelerListeEintrag | undefined;
	schuelerVermerke: List<SchuelerVermerke>;
	mapVermerkArten: Map<number, VermerkartEintrag>;
}

const defaultState = <RouteStateSchuelerVermerke> {
	auswahl: undefined,
	schuelerVermerke: new ArrayList(),
	mapVermerkArten: new Map(),
};

export class RouteDataSchuelerVermerke extends RouteData<RouteStateSchuelerVermerke> {

	private static readonly comparatorDatumDesc: Comparator<SchuelerVermerke> = {
		compare: (a: SchuelerVermerke, b: SchuelerVermerke) => {
			let cmp;
			if ((a.datum !== null) && (b.datum !== null)) {
				cmp = JavaString.compareTo(b.datum, a.datum);
				if (cmp !== 0) {
					return cmp;
				}
			}
			return JavaLong.compare(b.id, a.id);
		},
	};

	public constructor() {
		super(defaultState);
	}

	get filterNurSichtbare(): boolean {
		return configStateImpl.config.getValue("schueler.vermerke.filterNurSichtbare") === 'true';
	}

	setFilterNurSichtbare = async (value: boolean) => {
		await configStateImpl.config.setValue('schueler.vermerke.filterNurSichtbare', value ? "true" : "false");
	};

	get auswahl(): SchuelerListeEintrag {
		if (this._state.value.auswahl === undefined) {
			throw new DeveloperNotificationException("Unerwarteter Fehler: Schülerauswahl nicht festgelegt, es können keine Informationen zu Vermerk-Daten abgerufen oder eingegeben werden.");
		}
		return this._state.value.auswahl;
	}

	get schuelerVermerke(): List<SchuelerVermerke> {
		return this._state.value.schuelerVermerke;
	}

	get mapVermerkArten(): Map<number, VermerkartEintrag> {
		return this._state.value.mapVermerkArten;
	}

	patch = async (data: Partial<SchuelerVermerke>, idVermerk: number) => {
		api.status.start();
		await api.server.patchSchuelerVermerke(data, api.schema, idVermerk);
		for (const vermerk of this.schuelerVermerke) {
			if (vermerk.id === idVermerk) {
				Object.assign(vermerk, data);
			}
		}
		this.commit();
		api.status.stop();
		return true;
	};

	add = async () => {
		const addCanditate: Partial<SchuelerVermerke> = { idSchueler: this.auswahl.id };
		api.status.start();
		const vermerk = await api.server.addVermerk(addCanditate, api.schema);
		this.schuelerVermerke.add(vermerk);
		this.schuelerVermerke.sort(RouteDataSchuelerVermerke.comparatorDatumDesc);
		this.commit();
		api.status.stop();
	};

	remove = async (idVermerk: number) => {
		api.status.start();
		await api.server.deleteSchuelerVermerk(api.schema, this.auswahl.id, idVermerk);
		for (let i = this.schuelerVermerke.size() - 1; i >= 0; i--) {
			const vermerk = this.schuelerVermerke.get(i);
			if (vermerk.id === idVermerk) {
				this.schuelerVermerke.removeElementAt(i);
				break;
			}
		}
		this.commit();
		api.status.stop();
	};

	public async ladeDaten(auswahl: SchuelerListeEintrag | null) {
		if (auswahl === null) {
			this.setPatchedDefaultState({});
		} else {
			const schuelerVermerke = await api.server.getVermerkdaten(api.schema, auswahl.id);
			schuelerVermerke.sort(RouteDataSchuelerVermerke.comparatorDatumDesc);
			const vermerkArten = await api.server.getVermerkarten(api.schema);
			const mapVermerkArten = new Map();
			for (const va of vermerkArten) {
				mapVermerkArten.set(va.id, va);
			}
			this.setPatchedDefaultState({ auswahl, schuelerVermerke, mapVermerkArten });
		}
	}

}
