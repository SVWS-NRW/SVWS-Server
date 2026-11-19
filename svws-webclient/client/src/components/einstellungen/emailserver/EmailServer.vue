<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>E-Mail-Server</span>
					</h2>
				</div>
			</div>
		</header>
		<div class="page page-grid-cards">
			<svws-ui-content-card v-if="!readonly" title="E-Mail-Server">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="SMTP-Host" class="contentFocusField"
						:model-value="smptServerKonfiguration().host"
						@change="host => host && patch({ host })" />
					<svws-ui-input-number placeholder="Port"
						:model-value="smptServerKonfiguration().port"
						@change="port => (port !== null) && (port !== undefined) && patch({ port })" />
					<svws-ui-checkbox type="toggle"
						:model-value="smptServerKonfiguration().useStartTLS"
						@update:model-value="value => patch({ useStartTLS : value })">
						Nutze StartTLS
					</svws-ui-checkbox>
					<svws-ui-checkbox type="toggle"
						:model-value="smptServerKonfiguration().useTLS"
						@update:model-value="value => patch({ useTLS : value })">
						Nutze TLS
					</svws-ui-checkbox>
					<span v-if="unverschluesselt" class="col-span-full text-ui-caution">
						<span class="icon icon-ui-caution i-ri-alert-line" /> Ohne TLS und StartTLS sendet der Server E-Mails und Zugangsdaten unverschlüsselt. Der Fingerabdruck wirkt dann nicht.
					</span>
					<svws-ui-text-input placeholder="Fingerabdruck des Zertifikats (SHA-256)" span="full"
						:model-value="smptServerKonfiguration().zertifikatFingerabdruck"
						:valid="fingerabdruckGueltig"
						@change="patchFingerabdruck" />
					<p class="col-span-full">
						Ist ein Fingerabdruck eingetragen, vertraut der Server nur noch diesem einen Zertifikat, zum Beispiel auch einem selbst erstellten.
						Name und Laufzeit des Zertifikats prüft er dann nicht. Nach einem Zertifikatswechsel muss der neue Fingerabdruck eingetragen werden,
						sonst scheitert der Versand.
					</p>
					<p class="col-span-full mt-4">
						Die folgende Angabe nutzen ausschließlich externe Programme. Der SVWS-Server wertet sie beim Versand nicht aus.
					</p>
					<svws-ui-text-input placeholder="Trust TLS Host"
						:model-value="smptServerKonfiguration().trustTLSHost"
						@change="trustTLSHost => patch({ trustTLSHost: trustTLSHost || null })" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import { useBenutzerState } from "@ui/states/BenutzerState";

	import type { EmailServerProps } from "~/components/einstellungen/emailserver/EmailServerProps";

	const props = defineProps<EmailServerProps>();
	const benutzerState = useBenutzerState();

	const readonly = computed<boolean>(() => !benutzerState.istAdmin);

	const unverschluesselt = computed<boolean>(() => !props.smptServerKonfiguration().useTLS && !props.smptServerKonfiguration().useStartTLS);

	/** Entfernt Doppelpunkte und Leerzeichen, damit Fingerabdrücke aus verschiedenen Werkzeugen gleich behandelt werden. */
	function hexZiffern(value: string | null): string {
		return (value ?? "").replace(/[:\s]/g, "").toUpperCase();
	}

	function fingerabdruckGueltig(value: string | null): boolean {
		const hex = hexZiffern(value);
		return (hex.length === 0) || /^[0-9A-F]{64}$/.test(hex);
	}

	/** Speichert den Fingerabdruck in der Form des Servers, damit die Anzeige ohne Neuladen zum gespeicherten Wert passt. */
	async function patchFingerabdruck(value: string | null) {
		if (!fingerabdruckGueltig(value)) {
			return;
		}
		const hex = hexZiffern(value);
		await props.patch({ zertifikatFingerabdruck: (hex.length === 0) ? null : hex.replace(/(..)(?!$)/g, "$1:") });
	}

</script>
