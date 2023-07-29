import type { StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";

export interface StundenplanAnsichtProps {
	mode: 'schueler' | 'lehrer' | 'klasse';
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
}