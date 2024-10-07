import type { List } from "@core";

export interface GostGruppenprozesseProps {
	removeAbiturjahrgaenge: () => Promise<[boolean, List<string | null>]>;
	removeAbiturjahrgaengeCheck: () => [boolean, List<string>];
}
