import type { Schulform, Schuljahresabschnitt, ServerMode, SimpleOperationResponse } from "@core";

export interface SchuleDatenaustauschUntisImporteProps {
	serverMode: ServerMode;
	schulform: Schulform;
	schuljahresabschnitt: () => Schuljahresabschnitt;
	importUntisStundenplanGPU001: (formData: FormData, ignoreMissing: boolean) => Promise<SimpleOperationResponse>;
	importUntisRaeumeGPU005: (formData: FormData) => Promise<SimpleOperationResponse>;
}
