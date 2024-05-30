import type { SimpleOperationResponse } from "@core";
import type { AbschnittAuswahlDaten } from "@comp";

export interface SchuleDatenaustauschUntisStundenplanProps {
	importUntisStundenplanGPU001: (formData: FormData, ignoreMissing: boolean) => Promise<SimpleOperationResponse>;
	schuljahresabschnittsauswahl: () => AbschnittAuswahlDaten;
}