import type { ApiFile, GostBlockungListeneintrag, GostBlockungsdatenManager, GostBlockungsergebnis, GostHalbjahr, GostJahrgang  } from "@core";

export interface SchuleDatenaustauschUntisBlockungenProps {
	mapAbiturjahrgaenge: () => Map<number, GostJahrgang>;
	abiturjahrgang: () => GostJahrgang | undefined;
	setAbiturjahrgang: (jahrgang: GostJahrgang) => Promise<void>;
	halbjahr: () => GostHalbjahr | undefined;
	setHalbjahr: (halbjahr: GostHalbjahr) => Promise<void>;
	mapBlockungen: () => Map<number, GostBlockungListeneintrag>;
	blockung: () => GostBlockungListeneintrag | undefined;
	setBlockung: (blockung: GostBlockungListeneintrag) => Promise<void>;
	ergebnis: () => GostBlockungsergebnis | undefined;
	setErgebnis: (ergebnis: GostBlockungsergebnis) => Promise<void>;
	getDatenmanager: () => GostBlockungsdatenManager;
	exportUntisBlockungenZIP: (formData: FormData) => Promise<ApiFile>;
}