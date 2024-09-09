import type { ApiFile, GostBlockungListeneintrag, GostBlockungsergebnis, GostHalbjahr, GostJahrgang, List } from "@core";

export interface SchuleDatenaustauschUntisBlockungenProps {
	mapAbiturjahrgaenge: () => Map<number, GostJahrgang>;
	abiturjahrgang: () => GostJahrgang | undefined;
	gotoAbiturjahrgang: (value: number) => Promise<void>;
	halbjahr: () => GostHalbjahr | undefined;
	gotoHalbjahr: (value: number) => Promise<void>;
	listBlockungen: () => List<GostBlockungListeneintrag>;
	blockung: () => GostBlockungListeneintrag | undefined;
	gotoBlockung: (value: number) => Promise<void>;
	listErgebnisse: () => List<GostBlockungsergebnis>;
	ergebnis: () => GostBlockungsergebnis | undefined;
	gotoErgebnis: (value: number) => Promise<void>;
	exportUntisBlockungenZIP: (formData: FormData) => Promise<ApiFile>;
}