import type { GostBlockungRegelUpdate, GostBlockungsdatenManager, GostBlockungsergebnisKursSchuelerZuordnungUpdate, GostBlockungsergebnisManager, SchuelerListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface GostKursplanungUmwahlansichtProps {
	hatBlockung: boolean,
	hatErgebnis: boolean,
	regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	updateKursSchuelerZuordnungen: (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate) => Promise<boolean>;
	autoKursSchuelerZuordnung: (idSchueler : number) => Promise<void>;
	gotoSchueler: (idSchueler: number) => Promise<void>;
	gotoLaufbahnplanung: (idSchueler: number) => Promise<void>;
	getDatenmanager: () => GostBlockungsdatenManager;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	schueler: SchuelerListeEintrag | undefined;
	apiStatus: ApiStatus;
}