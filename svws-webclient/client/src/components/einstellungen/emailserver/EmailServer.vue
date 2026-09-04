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
					<svws-ui-text-input placeholder="Trust TLS Host"
						:model-value="smptServerKonfiguration().trustTLSHost"
						@change="trustTLSHost => patch({ trustTLSHost: trustTLSHost || null })" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { EmailServerProps } from "~/components/einstellungen/emailserver/EmailServerProps";
	import { computed } from "vue";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

	const props = defineProps<EmailServerProps>();
	const benutzerState = useBenutzerState();

	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));

</script>
