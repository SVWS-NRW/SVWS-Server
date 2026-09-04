import type { SMTPServerKonfiguration } from "@core/core/data/email/SMTPServerKonfiguration";

export interface EmailServerProps {
	smptServerKonfiguration: () => SMTPServerKonfiguration;
	patch: (data: Partial<SMTPServerKonfiguration>) => Promise<void>;
}
