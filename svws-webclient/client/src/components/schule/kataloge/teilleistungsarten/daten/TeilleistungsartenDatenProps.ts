import type { Teilleistungsart } from "@core/core/data/kataloge/Teilleistungsart";
import type { TeilleistungsartenListeManager } from "~/states/teilleistungsarten/TeilleistungsartenListeManager";

export interface TeilleistungsartenDatenProps {
	patch: (data: Partial<Teilleistungsart>) => Promise<boolean>;
	manager: () => TeilleistungsartenListeManager,
}
