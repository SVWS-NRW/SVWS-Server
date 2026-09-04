import type { Telefonart } from "@core/core/data/schule/Telefonart";
import type { TelefonartenListeManager } from "@ui/ui/manager/kataloge/TelefonartenListeManager";

export interface TelefonartenDatenProps {
	patch: (data: Partial<Telefonart>) => Promise<boolean>;
	manager: () => TelefonartenListeManager,
}
