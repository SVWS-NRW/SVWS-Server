<template>
	<div class="page page-flex-row">
		<div v-if="!manager().auswahlIsKonfigurationLokal()" class="min-w-fit flex flex-col gap-8">
			<div class="min-w-148 max-w-196">
				<div class="text-headline-md mb-4">Verbindung zum Webnotenmanager einrichten</div>
				<svws-ui-input-wrapper>
					<div>Adresse: {{ manager().auswahl().url }}</div>
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


			<template v-if="manager().getConnectionResponse(manager().auswahl().id).success">
				<!-- Den Webnotenmanager konfigurieren -->
				<div class="max-w-164">
					<div class="text-headline-md mb-4">Webnotenmanager konfigurieren</div>
					<svws-ui-input-wrapper class="mt-8">
						<div class="text-headline-md">SMTP Einstellungen</div>
						<svws-ui-text-input v-model.trim="smtpConfig.host" placeholder="SMTP-Server" />
						<svws-ui-input-number v-model="smtpConfig.port" placeholder="Port" :min="1" />
						<svws-ui-text-input v-model.trim="smtpConfig.username" placeholder="Benutzername" />
						<svws-ui-text-input v-model.trim="smtpConfig.password" placeholder="Passwort" type="password" />
						<svws-ui-checkbox v-model="smtpConfig.useTLS">TLS verwenden</svws-ui-checkbox>
						<svws-ui-text-input v-model.trim="smtpConfig.fromEmail" placeholder="Absenderadresse der Email" />
						<svws-ui-text-input v-model.trim="smtpConfig.fromName" placeholder="Absendername der Email" />
						<svws-ui-button type="primary" @click="updateSMTP">
							Speichern
						</svws-ui-button>
					</svws-ui-input-wrapper>
				</div>
			</template>
			<div>
				<svws-ui-spinner :spinning />
			</div>
		</div>
		<div v-else>
			<div class="text-headline-md mb-4">Lokales Notenmodul, Anzeige wird noch entsprechend angepasst</div>
		</div>

		<!-- Die Ausgabe des Logs -->
		<div v-if="status !== null && manager().auswahl().id > 0" class="min-w-fit grow h-full overflow-hidden flex flex-col gap-4">
			<log-box :logs="status.log" :status="status.success" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { TLSCertificate } from '@core';
	import { ENMServerConfigElement, ENMServerConfigSMTP } from '@core';
	import type { NotenmodulKonfigurationProps } from './NotenmodulKonfigurationProps';

	const props = defineProps<NotenmodulKonfigurationProps>();

	const cert = computed<TLSCertificate | null>(() => {
		const connInfo = props.manager().auswahl();
		if ((connInfo.serverTLSCertChain.size() < 1))
			return null;
		return connInfo.serverTLSCertChain.getFirst();
	});

	const smtpConfig = computed(() => {
		const json = props.serverConfig().get('smtp');
		if (json !== null)
			return ENMServerConfigSMTP.transpilerFromJSON(json);
		return new ENMServerConfigSMTP();
	});

	const status = computed(() => props.manager().getAuswahlConnectionResponse());
	const spinning = ref<boolean>(false);

	async function updateSMTP() {
		spinning.value = true;
		const element = new ENMServerConfigElement();
		element.key = "smtp";
		element.value = ENMServerConfigSMTP.transpilerToJSON(smtpConfig.value);
		element.type = "server";
		await props.setServerConfigElement(element);
		spinning.value = false;
	}

</script>