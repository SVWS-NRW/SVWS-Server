import type { GostBlockungRegel, GostBlockungsdatenManager, GostBlockungsergebnisManager, SchuelerListeEintrag } from "@core";
import type { ApiStatus } from "~/components/ApiStatus";

export interface GostUmwahlansichtProps {
	addRegel: (regel: GostBlockungRegel) => Promise<GostBlockungRegel | undefined>;
	removeRegel: (id: number) => Promise<GostBlockungRegel | undefined>;
	updateKursSchuelerZuordnung: (idSchueler: number, idKursNeu: number, idKursAlt: number | undefined) => Promise<boolean>;
	removeKursSchuelerZuordnung: (idSchueler: number, idKurs: number) => Promise<boolean>;
	autoKursSchuelerZuordnung: (idSchueler : number) => Promise<void>;
	gotoSchueler: (idSchueler: number) => Promise<void>;
	gotoLaufbahnplanung: (idSchueler: number) => Promise<void>;
	getDatenmanager: () => GostBlockungsdatenManager;
	getErgebnismanager: () => GostBlockungsergebnisManager;
	schueler: SchuelerListeEintrag | undefined;
	apiStatus: ApiStatus;
}