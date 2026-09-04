import type { Beschaeftigungsart } from "@core/core/data/schule/Beschaeftigungsart";
import type { BeschaeftigungsartenListeManager } from "@ui/ui/manager/kataloge/BeschaeftigungsartenListeManager";

export interface BeschaeftigungsartenDatenProps {
	manager: () => BeschaeftigungsartenListeManager;
	patch: (data: Partial<Beschaeftigungsart>) => Promise<boolean>;
}
