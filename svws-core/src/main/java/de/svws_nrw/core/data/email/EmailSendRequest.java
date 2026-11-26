package de.svws_nrw.core.data.email;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Ein Auftrag für den E-Mail-Versand über die API. Jede Adresse der Empfängerliste erhält eine eigene E-Mail mit demselben Betreff, Text und
 * denselben Anhängen, sodass die Empfänger einander nicht sehen.
 */
@XmlRootElement
@Schema(description = "ein Auftrag für den E-Mail-Versand an eine Liste von Empfängern")
@TranspilerDTO
public class EmailSendRequest {

	/** Der Betreff der E-Mail. */
	@Schema(description = "der Betreff der E-Mail", example = "Elternabend")
	public @NotNull String subject = "";

	/** Der Text der E-Mail als reiner Text. */
	@Schema(description = "der Text der E-Mail als reiner Text", example = "Liebe Eltern, ...")
	public @NotNull String text = "";

	/** Die E-Mail-Adressen der Empfänger. */
	@ArraySchema(schema = @Schema(implementation = String.class, description = "die E-Mail-Adressen der Empfänger", example = "max.mustermann@schule.de"))
	public @NotNull List<String> recipients = new ArrayList<>();

	/** Die Dateianhänge, die jeder Empfänger erhält. */
	@ArraySchema(schema = @Schema(implementation = EmailAttachment.class, description = "die Dateianhänge, die jeder Empfänger erhält"))
	public @NotNull List<EmailAttachment> attachments = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public EmailSendRequest() {
		// leer
	}

}
