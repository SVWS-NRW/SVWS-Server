import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { BetriebListeEintrag } from "@core";

export interface BetriebeAppProps {
    auswahl: BetriebListeEintrag | undefined;
    setTab: (value: AuswahlChildData) => Promise<void>;
    tab: AuswahlChildData;
    tabs: AuswahlChildData[];
    tabsHidden: boolean[];
}