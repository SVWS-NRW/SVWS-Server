import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchuleDatenaustauschUntisImporte } from "./RouteSchuleDatenaustauschUntisImporte";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { OpenApiError } from "@core/api/OpenApiError";
import type { GostBlockungListeneintrag } from "@core/core/data/gost/GostBlockungListeneintrag";
import { LongAndStringLists } from "@core/core/data/LongAndStringLists";
import { SimpleOperationResponse } from "@core/core/data/SimpleOperationResponse";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import type { List } from "@core/java/util/List";


type RouteStateDatenaustauschUntis = RouteStateInterface;

const defaultState = <RouteStateDatenaustauschUntis> {
	view: routeSchuleDatenaustauschUntisImporte,
};


export class RouteDataSchuleDatenaustauschUntis extends RouteData<RouteStateDatenaustauschUntis> {

	public constructor() {
		super(defaultState);
	}

	public async importUntisGPU(apimethod: () => Promise<SimpleOperationResponse>): Promise<SimpleOperationResponse> {
		try {
			return await apimethod();
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response !== null) && ((e.response.status === 400) || (e.response.status === 404) || (e.response.status === 409) || (e.response.status === 500))) {
				const json: string = await e.response.text();
				return SimpleOperationResponse.transpilerFromJSON(json);
			}
			const result = new SimpleOperationResponse();
			result.log.add("Fehler bei der Server-Anfrage. ");
			if (e instanceof Error) {
				result.log.add("  " + e.message);
			}
			return result;
		}
	}

	importUntisStundenplanGPU001 = async (formData: FormData, ignoreMissing: boolean): Promise<SimpleOperationResponse> => {
		return await this.importUntisGPU(() => api.server.importStundenplanUntisGPU001(formData, api.schema, ignoreMissing ? 1 : 0));
	};

	importUntisRaeumeGPU005 = async (formData: FormData): Promise<SimpleOperationResponse> => {
		return await this.importUntisGPU(() => api.server.importUntisRaeumeGPU005(formData, api.schema));
	};

	exportUntisKlassenGPU003 = async (): Promise<string[]> => {
		try {
			const apifile = await api.server.exportUntisKlassenGPU003(api.schema, abschnittStateImpl.auswahl.id);
			return [await apifile.data.text()];
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response) && (e.response.status === 404)) {
				return [];
			}
			throw e;
		}
	};

	exportUntisLehrerGPU004 = async (): Promise<string[]> => {
		try {
			const apifile = await api.server.exportUntisLehrerGPU004(api.schema, abschnittStateImpl.auswahl.id);
			return [await apifile.data.text()];
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response) && (e.response.status === 404)) {
				return [];
			}
			throw e;
		}
	};

	exportUntisFaecherGPU006 = async (): Promise<string[]> => {
		const apifile = await api.server.exportUntisFaecherGPU006(api.schema, abschnittStateImpl.auswahl.id);
		return [await apifile.data.text()];
	};

	exportUntisSchuelerGPU010 = async (sidvariante: number): Promise<string []> => {
		try {
			const apifile = await api.server.exportUntisSchuelerGPU010(api.schema, abschnittStateImpl.auswahl.id, sidvariante);
			return [await apifile.data.text()];
		} catch (e) {
			if ((e instanceof OpenApiError) && (e.response instanceof Response) && (e.response.status === 404)) {
				return [];
			}
			throw e;
		}
	};

	exportUntisFachwahlenGPU015 = async (sidvariante: number, gpu002: string): Promise<string[]> => {
		const apifile = await api.server.exportUntisFachwahlenGPU015(gpu002, api.schema, abschnittStateImpl.auswahl.id, sidvariante);
		return [await apifile.data.text()];
	};

	exportUntisKlausurenGPU017 = async (sidvariante: number, gpu002: string): Promise<string[]> => {
		const apifile = await api.server.exportUntisKlausurenGPU017(gpu002, api.schema, abschnittStateImpl.auswahl.id, sidvariante);
		return [await apifile.data.text()];
	};

	exportUntisSchienenGPU019 = async (gpu002: string): Promise<string[]> => {
		const apifile = await api.server.exportUntisSchienenGPU019(gpu002, api.schema, abschnittStateImpl.auswahl.id);
		return [await apifile.data.text()];
	};

	ladeBlockungslisten = async (abijahrgaenge: number[]): Promise<Array<List<GostBlockungListeneintrag>>> => {
		// Bestimme zunächst die Blockungslisten für die übergebenen Abiturjahrgänge
		const all = [];
		const schuljahr = abschnittStateImpl.auswahl.schuljahr;
		const abschnitt = abschnittStateImpl.auswahl.abschnitt;
		for (const abiturjahr of abijahrgaenge) {
			const idHalbjahr = GostHalbjahr.fromAbiturjahrSchuljahrUndHalbjahr(abiturjahr, schuljahr, abschnitt)?.id ?? null;
			if (idHalbjahr !== null) {
				all.push(api.server.getGostAbiturjahrgangBlockungsliste(api.schema, abiturjahr, idHalbjahr));
			}
		}
		return await Promise.all(all);
	};

	exportUntisBlockung = async (sidvariante: number, gpu002: string, blockungsergebnisse: number[]): Promise<string[]> => {
		const data = new LongAndStringLists();
		const tmp = JSON.stringify(gpu002);
		data.strings.add(tmp.substring(1, tmp.length - 1));
		for (const idBlockungsergegbnis of blockungsergebnisse) {
			data.numbers.add(idBlockungsergegbnis);
		}
		return [... await api.server.exportUntisBlockungGPU002GPU015GPU019(data, api.schema, abschnittStateImpl.auswahl.id, sidvariante)];
	};

}
