import type { ApiFile, GostBlockungListeneintrag, GostBlockungsdatenManager, GostHalbjahr, GostJahrgang  } from "@core";

export interface SchuleDatenaustauschUntisBlockungenProps {
	mapAbiturjahrgaenge: () => Map<number, GostJahrgang>;
	abiturjahrgang: () => GostJahrgang | undefined;
	setAbiturjahrgang: (jahrgang: GostJahrgang) => Promise<void>;
	halbjahr: () => GostHalbjahr | undefined;
	setHalbjahr: (halbjahr: GostHalbjahr) => Promise<void>;
	mapBlockungen: () => Map<number, GostBlockungListeneintrag>;
	setBlockung: (blockung: GostBlockungListeneintrag) => Promise<void>
	getDatenmanager: () => GostBlockungsdatenManager;
	exportUntisBlockungenZIP: (formData: FormData) => Promise<ApiFile>;
}