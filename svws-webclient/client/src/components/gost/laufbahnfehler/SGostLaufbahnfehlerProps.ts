import type { ApiFile } from "@core/api/BaseApi";
import type { GostBelegpruefungsArt } from "@core/core/abschluss/gost/GostBelegpruefungsArt";
import type { GostBelegpruefungsErgebnisse } from "@core/core/data/gost/GostBelegpruefungsErgebnisse";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface GostLaufbahnfehlerProps {
	listBelegpruefungsErgebnisse: () => List<GostBelegpruefungsErgebnisse>;
	gostBelegpruefungsArt: () => GostBelegpruefungsArt;
	setGostBelegpruefungsArt: (value: GostBelegpruefungsArt) => Promise<void>;
	gotoLaufbahnplanung: (d: number) => Promise<RoutingStatus>;
	gotoSprachenfolge: (d: number) => Promise<RoutingStatus>;
	importLaufbahnplanung: (data: FormData) => Promise<void>;
	exportLaufbahnplanung: (schueler: List<number>) => Promise<ApiFile>;
	resetFachwahlenAlle: (ergebnisse: Iterable<GostBelegpruefungsErgebnisse>) => Promise<void>;
	jahrgangsdaten: () => GostJahrgangsdaten;
	apiStatus: ApiStatus;
	filterFehler: () => boolean;
	setFilterFehler: (value: boolean) => Promise<void>;
	filterExterne: () => boolean;
	setFilterExterne: (value: boolean) => Promise<void>;
	filterNurMitFachwahlen: () => boolean;
	setFilterNurMitFachwahlen: (value: boolean) => Promise<void>;
	filterNeuaufnahmen: () => boolean;
	setFilterNeuaufnahmen: (value: boolean) => Promise<void>;
	loeschenFachwahlenSelected: (value: Iterable<GostBelegpruefungsErgebnisse>) => Promise<void>;
}
