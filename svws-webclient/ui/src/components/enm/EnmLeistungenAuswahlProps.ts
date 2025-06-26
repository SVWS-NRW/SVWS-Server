import type { EnmLerngruppenAuswahlEintrag, EnmManager } from "./EnmManager";

export interface EnmLeistungenAuswahlProps {
	enmManager: () => EnmManager;
	setAuswahlEinzel: (value: EnmLerngruppenAuswahlEintrag | null) => void;
	auswahlEinzel: () => EnmLerngruppenAuswahlEintrag | null;
	setAuswahlMehrfach: (value: Array<EnmLerngruppenAuswahlEintrag>) => void;
	auswahlMehrfach: () => Array<EnmLerngruppenAuswahlEintrag>;
}
