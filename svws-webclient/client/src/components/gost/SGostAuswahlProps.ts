import type { BenutzerKompetenz, GostJahrgang, GostJahrgangsdaten, JahrgangsDaten, Schulform, ServerMode } from "@core";
import type { ApiStatus } from "../ApiStatus";
import type { AbschnittAuswahlDaten } from "@comp";

export interface GostAuswahlProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	auswahl: GostJahrgang | undefined;
	jahrgangsdaten: () => GostJahrgangsdaten | undefined;
	mapAbiturjahrgaenge: () => Map<number, GostJahrgang>;
	mapJahrgaengeOhneAbiJahrgang: () => Map<number, JahrgangsDaten>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
	apiStatus: ApiStatus;
	addAbiturjahrgang: (idJahrgang: number) => Promise<void>;
	gotoAbiturjahrgang: (abiturjahrgang: GostJahrgang) => Promise<void>;
	getAbiturjahrFuerJahrgang: (idJahrgang: number) => number;
	removeAbiturjahrgang: () => Promise<void>;
	// Config
	filterNurAktuelle: () => boolean;
	setFilterNurAktuelle: (value: boolean) => Promise<void>;
}
