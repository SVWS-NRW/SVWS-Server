import type { GostJahrgang } from "@core/core/data/gost/GostJahrgang";
import type { GostJahrgangsdaten } from "@core/core/data/gost/GostJahrgangsdaten";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { ApiStatus } from "../ApiStatus";

export interface GostAuswahlProps {
	auswahl: GostJahrgang | undefined;
	jahrgangsdaten: () => GostJahrgangsdaten | undefined;
	mapAbiturjahrgaenge: () => Map<number, GostJahrgang>;
	mapJahrgaengeOhneAbiJahrgang: () => Map<number, JahrgangsDaten>;
	apiStatus: ApiStatus;
	addAbiturjahrgang: (idJahrgang: number) => Promise<void>;
	gotoAbiturjahrgang: (abiturjahrgang: GostJahrgang) => Promise<void>;
	getAbiturjahrFuerJahrgang: (idJahrgang: number) => number;
	selected: () => GostJahrgang[];
	setSelected: (value: GostJahrgang[]) => void;
	gotoCreationMode: (navigate: boolean) => Promise<void>;
	gotoGruppenprozess: (navigate: boolean) => Promise<void>;
	// Config
	filterNurAktuelle: () => boolean;
	setFilterNurAktuelle: (value: boolean) => Promise<void>;
}
