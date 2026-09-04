import type { StundenplanUnterricht } from "@core/core/data/stundenplan/StundenplanUnterricht";
import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
import type { StundenplanUnterrichtListeManager } from "@ui/ui/manager/stundenplan/StundenplanUnterrichtListeManager";

export interface StundenplanUnterrichteProps {
	stundenplanManager: () => StundenplanManager;
	stundenplanUnterrichtListeManager: () => StundenplanUnterrichtListeManager;
	setFilter: () => Promise<void>;
	patchUnterricht: (daten: Iterable<StundenplanUnterricht>) => Promise<void>;
}
