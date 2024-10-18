import type { GostHalbjahr, GostJahrgangsdaten, GostBlockungsdaten, GostBlockungListeneintrag, GostBlockungsdatenManager, List, Schuljahresabschnitt, ServerMode, GostBlockungsergebnis, GostBlockungsergebnisManager, BenutzerKompetenz, Schulform } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface GostKursplanungAuswahlProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenAbiturjahrgaenge: Set<number>;
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
	rechneGostBlockung: () => Promise<List<number>>;
	removeErgebnisse: (ergebnisse: GostBlockungsergebnis[]) => Promise<void>;
	gotoErgebnis: (value: GostBlockungsergebnis | undefined) => Promise<void>;
	hatBlockung: boolean;
	auswahlErgebnis: GostBlockungsergebnis | undefined;
	restoreBlockung: () => Promise<void>;
	revertBlockung: () => Promise<void>;
	aktAbschnitt: Schuljahresabschnitt;
	mode: ServerMode;
	ausfuehrlicheDarstellungKursdifferenz: () => boolean;
	setAusfuehrlicheDarstellungKursdifferenz: (value: boolean) => Promise<void>;
	mapCoreTypeNameJsonData: () => Map<string, string>;
}