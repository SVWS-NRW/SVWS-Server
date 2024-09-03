import type { BenutzerKompetenz, GostBlockungRegelUpdate, GostBlockungsdatenManager, GostBlockungsergebnisKursSchuelerZuordnungUpdate, GostBlockungsergebnisManager, Schueler, Schulform, ServerMode } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface GostKursplanungUmwahlansichtProps {
	schulform: Schulform;
	serverMode: ServerMode;
	benutzerKompetenzen: Set<BenutzerKompetenz>,
	benutzerKompetenzenAbiturjahrgaenge: Set<number>;
	hatBlockung: boolean,
	hatErgebnis: boolean,
	regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	updateKursSchuelerZuordnungen: (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate) => Promise<boolean>;
	autoKursSchuelerZuordnung: (idSchueler : number) => Promise<void>;
	gotoSchueler: (idSchueler: number) => Promise<void>;
	gotoLaufbahnplanung: (idSchueler: number) => Promise<void>;
	getDatenmanager: () => GostBlockungsdatenManager;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	schueler: Schueler | undefined;
	apiStatus: ApiStatus;
}