import type { List } from "@core/java/util/List";

export interface GostGruppenprozesseProps {
	removeAbiturjahrgaenge: () => Promise<[boolean, List<string | null>]>;
	removeAbiturjahrgaengeCheck: () => [boolean, List<string>];
}
