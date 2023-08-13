import type { StundenplanKalenderwochenzuordnung, StundenplanManager } from "@core";

export interface StundenplanAnsichtProps {
	mode: 'schueler' | 'lehrer' | 'klasse';
	ignoreEmpty: boolean;
	manager: () => StundenplanManager;
	wochentyp: () => number;
	kalenderwoche: () => StundenplanKalenderwochenzuordnung | undefined;
}
