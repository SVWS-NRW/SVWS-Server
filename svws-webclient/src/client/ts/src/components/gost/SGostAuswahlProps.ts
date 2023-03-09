import { GostJahrgang, JahrgangsListeEintrag, Schuljahresabschnitt } from "@svws-nrw/svws-core";
import { ApiStatus } from "../ApiStatus";

export interface GostAuswahlProps {
	auswahl: GostJahrgang | undefined;
	mapAbiturjahrgaenge: Map<number, GostJahrgang>;
	mapJahrgaengeOhneAbiJahrgang: Map<number, JahrgangsListeEintrag>;
	abschnitte: Map<number, Schuljahresabschnitt>;
	aktAbschnitt: Schuljahresabschnitt;
	aktSchulabschnitt: number;
	apiStatus: ApiStatus;
	setAbschnitt: (abschnitt: Schuljahresabschnitt) => void;
	addAbiturjahrgang: (idJahrgang: number) => Promise<void>;
	gotoAbiturjahrgang: (abiturjahrgang: GostJahrgang) => Promise<void>
}