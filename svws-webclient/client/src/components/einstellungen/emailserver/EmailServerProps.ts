import type { SMTPServerKonfiguration } from "@core";

export interface EmailServerProps {
	smptServerKonfiguration: () => SMTPServerKonfiguration;
	patch: (data: Partial<SMTPServerKonfiguration>) => Promise<void>;
}
