import type { GostBlockungListeneintrag } from "@core/core/data/gost/GostBlockungListeneintrag";
import type { GostBlockungsdaten } from "@core/core/data/gost/GostBlockungsdaten";
import type { GostBlockungsergebnis } from "@core/core/data/gost/GostBlockungsergebnis";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import type { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
import type { GostBlockungsdatenManager } from "@core/core/utils/gost/GostBlockungsdatenManager";
import type { GostBlockungsergebnisManager } from "@core/core/utils/gost/GostBlockungsergebnisManager";
import type { List } from "@core/java/util/List";
import type { ApiStatus } from "~/components/ApiStatus";

export interface GostKursplanungAuswahlProps {
	setHalbjahr: (value: GostHalbjahr) => Promise<void>;
	halbjahr: GostHalbjahr;
	jahrgangsdaten: () => GostJahrgangsdaten | undefined;
	// ... zusätzlich für die Blockungsauswahl
	patchBlockung: (data: Partial<GostBlockungsdaten>, idBlockung: number) => Promise<boolean>;
	addBlockung: () => Promise<void>;
	removeBlockung: () => Promise<void>;
	gotoBlockung: (auswahl: GostBlockungListeneintrag | undefined) => Promise<void>;
	auswahlBlockung: GostBlockungListeneintrag | undefined;
	mapBlockungen: () => Map<number, GostBlockungListeneintrag>;
	addErgebnisse: (ergebnisse: List<GostBlockungsergebnis>) => Promise<void>;
	apiStatus: ApiStatus;
	// ... zusätzlich für die Ergebnisauswahl
	getDatenmanager: () => GostBlockungsdatenManager;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	patchErgebnis: (data: Partial<GostBlockungsergebnis>, idErgebnis: number) => Promise<boolean>;
	removeErgebnisse: (ergebnisse: GostBlockungsergebnis[]) => Promise<void>;
	gotoErgebnis: (value: GostBlockungsergebnis | undefined) => Promise<void>;
	hatBlockung: boolean;
	auswahlErgebnis: GostBlockungsergebnis | undefined;
	restoreBlockung: () => Promise<void>;
	revertBlockung: () => Promise<void>;
	ausfuehrlicheDarstellungKursdifferenz: () => boolean;
	setAusfuehrlicheDarstellungKursdifferenz: (value: boolean) => Promise<void>;
	mapCoreTypeData: () => Map<string, any>;
}