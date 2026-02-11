package de.svws_nrw.base.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beinhaltet die Informationen zu einem E-Mail-Enpfänger und den ihm zugeordneten Anhöngen.
 */
public final class EmailJobRecipient {

	/** Die Empfänger-E-Mail-Adresse */
	public final @NotNull String email;

	/** Die Liste der Attachments */
	public final List<EmailJobAttachment> attachments = new ArrayList<>();

	/**
	 * Erstellt einen neuen Anhang mit der Adresse des E-Mail-Empfängers
	 *
	 * @param recipient die Empfänger-E-Mail-Adresse
	 */
	public EmailJobRecipient(final @NotNull String recipient) {
		if ((recipient == null) || recipient.isBlank())
			throw new IllegalArgumentException("Notwendiger Parameter E-Mail-Adresse für die Erzeugung eines E-Mail-Recipients ist null oder leer.");
		this.email = recipient;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		final EmailJobRecipient other = (EmailJobRecipient) obj;
		return Objects.equals(this.email, other.email);
	}

}
