import type { StundenplanKalenderwochenzuordnung } from "@core/core/data/stundenplan/StundenplanKalenderwochenzuordnung";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import type { List } from "@core/java/util/List";

export interface StundenplanKalenderwochenProps {
	stundenplanManager: () => StundenplanManager;
	patchKalenderwochenzuordnungen: (daten: List<StundenplanKalenderwochenzuordnung>) => Promise<void>;
	deleteKalenderwochenzuordnungen: () => Promise<void>;
}