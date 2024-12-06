import type { Schulform, Schuljahresabschnitt, SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschUntisImporteProps {
	schulform: Schulform;
	schuljahresabschnitt: () => Schuljahresabschnitt;
	importUntisStundenplanGPU001: (formData: FormData, ignoreMissing: boolean) => Promise<SimpleOperationResponse>;
	importUntisRaeumeGPU005: (formData: FormData) => Promise<SimpleOperationResponse>;
}
