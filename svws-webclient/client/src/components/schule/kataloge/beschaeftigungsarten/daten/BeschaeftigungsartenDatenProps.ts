import type { Beschaeftigungsart } from "@core";
import type { BeschaeftigungsartenListeManager } from "@ui";

export interface BeschaeftigungsartenDatenProps {
	manager: () => BeschaeftigungsartenListeManager;
	patch: (data: Partial<Beschaeftigungsart>) => Promise<boolean>;
}
