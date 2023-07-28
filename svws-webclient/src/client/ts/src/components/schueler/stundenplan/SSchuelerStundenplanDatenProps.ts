import type { StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";

export interface SchuelerStundenplanDatenProps {
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
}