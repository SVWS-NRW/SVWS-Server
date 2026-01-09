<template>
	<div class="page page-flex-col">
		<div class="overflow-y-scroll">
			<div class="min-w-fit flex flex-col gap-8">
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
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import { ENMServerConfigElement, ENMServerConfigSMTP } from '@core';
	import type { NotenmodulMailProps } from './NotenmodulMailProps';

	const props = defineProps<NotenmodulMailProps>();

	const smtpConfig = computed(() => {
		const json = props.serverConfig().get('smtp');
		if (json !== null) {
			return ENMServerConfigSMTP.transpilerFromJSON(json);
		}
		return new ENMServerConfigSMTP();
	});

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
