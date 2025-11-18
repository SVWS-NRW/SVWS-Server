<template>
	<div class="page page-flex-row">
		<div class="min-w-fit flex flex-col gap-8">
			<div class="min-w-148 max-w-196">
				<div class="text-headline-md mb-4">Verbindung zum Webnotenmanager einrichten</div>
				<svws-ui-input-wrapper>
					<div>Adresse: {{ manager().auswahl().url }}</div>
					<svws-ui-text-input :model-value="manager().auswahl().clientSecret" type="password" placeholder="Secret" @change="clientSecret => (clientSecret !== null) && updateServerConnection({ clientSecret })" />
					<svws-ui-text-input :model-value="manager().auswahl().bezeichnung" type="text" placeholder="Bezeichnung" @change="bezeichnung => updateServerConnection({ bezeichnung: bezeichnung ?? null })" />
					<svws-ui-button type="primary" @click="connect(manager().auswahl().id)">
						Verbindungsdaten prüfen
					</svws-ui-button>
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper v-if="manager().auswahl().serverTLSCertIsKnown === false" class="mt-8">
					<div class="text-headline-md">TLS-Zertifikat des Servers </div>
					<div v-if="cert === null">Kein Zertifikat angegeben.</div>
					<div v-else>
						<div class="text-headline-sm">Inhaber</div>
						<div class="pl-4">{{ cert.subject }}</div>
						<div class="text-headline-sm">Aussteller</div>
						<div class="pl-4">{{ cert.issuer }}</div>
						<div class="text-headline-sm">Gültigkeit</div>
						<div class="pl-4">von: {{ cert.validSince }}</div>
						<div class="pl-4">bis: {{ cert.validUntil }}</div>
					</div>
					<svws-ui-checkbox :model-value="manager().auswahl().serverTLSCertIsTrusted" @update:model-value="value => trustCertificate(value)">
						Zertifikat vertrauen?
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</div>
		</div>

		<!-- Die Ausgabe des Logs -->
		<div v-if="status !== null && manager().auswahl().id > 0" class="min-w-fit grow h-full overflow-hidden flex flex-col gap-4">
			<log-box :logs="status.log" :status="status.success" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';
	import type { TLSCertificate } from '@core';
	import type { NotenmodulVerbindungProps } from './NotenmodulVerbindungProps';

	const props = defineProps<NotenmodulVerbindungProps>();

	const cert = computed<TLSCertificate | null>(() => {
		const connInfo = props.manager().auswahl();
		if ((connInfo.serverTLSCertChain.size() < 1))
			return null;
		return connInfo.serverTLSCertChain.getFirst();
	});

	const status = computed(() => props.manager().getAuswahlConnectionResponse());

</script>