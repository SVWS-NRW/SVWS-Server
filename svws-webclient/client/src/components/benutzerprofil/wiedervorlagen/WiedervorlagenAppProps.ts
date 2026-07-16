import type { WiedervorlageEintrag } from "@core";

export interface WiedervorlagenAppProps {
	goToPerson: (eintrag: WiedervorlageEintrag) => Promise<void>;
}
