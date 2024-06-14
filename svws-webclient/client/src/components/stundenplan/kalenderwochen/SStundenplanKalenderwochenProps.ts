import type { Stundenplan, StundenplanManager } from "@core";

export interface StundenplanKalenderwochenProps {
	stundenplanManager: () => StundenplanManager;
	patch: (daten: Partial<Stundenplan>) => Promise<void>;
}