import type { ApiFile, GostBlockungListeneintrag, GostBlockungsergebnis, GostHalbjahr, GostJahrgang, List } from "@core";
import type { RoutingStatus } from "~/router/RoutingStatus";

export interface SchuleDatenaustauschUntisBlockungenProps {
	mapAbiturjahrgaenge: () => Map<number, GostJahrgang>;
	abiturjahrgang: () => GostJahrgang | undefined;
	gotoAbiturjahrgang: (value: number) => Promise<RoutingStatus>;
	halbjahr: () => GostHalbjahr | undefined;
	gotoHalbjahr: (value: number) => Promise<RoutingStatus|undefined>;
	listBlockungen: () => List<GostBlockungListeneintrag>;
	blockung: () => GostBlockungListeneintrag | undefined;
	gotoBlockung: (value: number) => Promise<RoutingStatus|undefined>;
	listErgebnisse: () => List<GostBlockungsergebnis>;
	ergebnis: () => GostBlockungsergebnis | undefined;
	gotoErgebnis: (value: number) => Promise<RoutingStatus | undefined>;
	exportUntisBlockungenZIP: (formData: FormData) => Promise<ApiFile>;
}