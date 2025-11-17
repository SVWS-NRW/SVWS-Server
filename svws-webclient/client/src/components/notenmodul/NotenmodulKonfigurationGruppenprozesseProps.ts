import type { List } from "@core";
import type { WenomAuswahlListeManager } from "@ui";

export interface NotenmodulKonfigurationGruppenprozesseProps {
	manager: () => WenomAuswahlListeManager;
	deleteKonfiguration: () => Promise<[boolean, List<string | null>]>;
}
