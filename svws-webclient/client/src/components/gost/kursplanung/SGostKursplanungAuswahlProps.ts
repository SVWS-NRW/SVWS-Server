import type { GostHalbjahr, GostJahrgangsdaten, GostBlockungsdaten, GostBlockungListeneintrag, GostBlockungsdatenManager, GostBlockungsergebnisListeneintrag, List, Schuljahresabschnitt, ServerMode, GostBlockungsergebnis, GostBlockungsergebnisManager } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface GostKursplanungAuswahlProps {
	setHalbjahr: (value: GostHalbjahr) => Promise<void>;
	halbjahr: GostHalbjahr;
	jahrgangsdaten: () => GostJahrgangsdaten | undefined;
	// ... zusätzlich für die Blockungsauswahl
	patchBlockung: (data: Partial<GostBlockungsdaten>, idBlockung: number) => Promise<boolean>;
	addBlockung: () => Promise<void>;
	removeBlockung: () => Promise<void>;
	setAuswahlBlockung: (auswahl: GostBlockungListeneintrag | undefined) => Promise<void>;
	auswahlBlockung: GostBlockungListeneintrag | undefined;
	mapBlockungen: () => Map<number, GostBlockungListeneintrag>;
	addErgebnisse: (ergebnisse: List<GostBlockungsergebnis>) => Promise<void>;
	apiStatus: ApiStatus;
	// ... zusätzlich für die Ergebnisauswahl
	getDatenmanager: () => GostBlockungsdatenManager;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	patchErgebnis: (data: Partial<GostBlockungsergebnisListeneintrag>, idErgebnis: number) => Promise<boolean>;
	rechneGostBlockung: () => Promise<List<number>>;
	removeErgebnisse: (ergebnisse: GostBlockungsergebnisListeneintrag[]) => Promise<void>;
	setAuswahlErgebnis: (value: GostBlockungsergebnisListeneintrag | undefined) => Promise<void>;
	hatBlockung: boolean;
	auswahlErgebnis: GostBlockungsergebnisListeneintrag | undefined;
	restoreBlockung: () => Promise<void>;
	aktAbschnitt: Schuljahresabschnitt;
	mode: ServerMode;
}