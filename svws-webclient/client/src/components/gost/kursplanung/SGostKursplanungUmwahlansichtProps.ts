import type { Schueler } from "@core/asd/data/schueler/Schueler";
import type { GostBlockungRegelUpdate } from "@core/core/data/gost/GostBlockungRegelUpdate";
import type { GostBlockungsergebnisKursSchuelerZuordnungUpdate } from "@core/core/data/gost/GostBlockungsergebnisKursSchuelerZuordnungUpdate";
import type { GostBlockungsdatenManager } from "@core/core/utils/gost/GostBlockungsdatenManager";
import type { GostBlockungsergebnisManager } from "@core/core/utils/gost/GostBlockungsergebnisManager";
import type { ApiStatus } from "~/components/ApiStatus";

export interface GostKursplanungUmwahlansichtProps {
	hatBlockung: boolean,
	hatErgebnis: boolean,
	regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	updateKursSchuelerZuordnungen: (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate) => Promise<boolean>;
	autoKursSchuelerZuordnung: (idSchueler: number) => Promise<void>;
	gotoSchueler: (idSchueler: number) => Promise<void>;
	gotoLaufbahnplanung: (idSchueler: number) => Promise<void>;
	getDatenmanager: () => GostBlockungsdatenManager;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	schueler: Schueler | undefined;
	apiStatus: ApiStatus;
}