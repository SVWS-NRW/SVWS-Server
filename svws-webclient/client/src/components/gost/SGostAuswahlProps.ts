import type { GostJahrgang, GostJahrgangsdaten, JahrgangsDaten, Schuljahresabschnitt } from "@core";
import type { ApiStatus } from "../ApiStatus";

export interface GostAuswahlProps {
	auswahl: GostJahrgang | undefined;
	jahrgangsdaten: () => GostJahrgangsdaten | undefined;
	mapAbiturjahrgaenge: () => Map<number, GostJahrgang>;
	mapJahrgaengeOhneAbiJahrgang: () => Map<number, JahrgangsDaten>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	apiStatus: ApiStatus;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	addAbiturjahrgang: (idJahrgang: number) => Promise<void>;
	gotoAbiturjahrgang: (abiturjahrgang: GostJahrgang) => Promise<void>;
	getAbiturjahrFuerJahrgang: (idJahrgang: number) => number;
	removeAbiturjahrgang: () => Promise<void>;
}