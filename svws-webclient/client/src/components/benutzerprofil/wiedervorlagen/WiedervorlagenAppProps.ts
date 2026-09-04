import type { WiedervorlageEintrag } from "@core/core/data/schule/WiedervorlageEintrag";

export interface WiedervorlagenAppProps {
	goToPerson: (eintrag: WiedervorlageEintrag) => Promise<void>;
}
