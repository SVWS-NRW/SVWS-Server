<template>
	<div class="page--flex">
		<svws-ui-header>
			<span class="inline-block mr-3">Datenaustausch mit dem Web-Notenmodul</span>
			<br>
			<span class="opacity-50 flex">
				<span class="i-ri-download-2-line icon-xl" />
				<span class="i-ri-upload-2-line icon-xl" />
			</span>
		</svws-ui-header>
	</div>

	<div class="page--content page--content--flex-row gap-2 h-full w-full overflow-hidden">
		<div class="page--content page--content--flex-row gap-2 h-full w-full overflow-hidden">
			<!-- Auswahl des Untis-Importes (linke Seite) -->
			<div class="h-full min-w-64 w-64 flex flex-col gap-2 m-2">
				<svws-ui-button id="contentFocusField" :type="(aktuell === 'setup') ? 'primary' : 'secondary'" @click="onSelect('setup')">
					<div class="flex flex-col gap-1">
						<p class="text-left font-bold ">Verbindungsdaten einrichten</p>
						<p class="text-left font-normal">{{ (clientSecret !== null) ? 'vorhanden' : 'keine vorhanden' }}{{ (clientSecret !== null) ? (connected ? ', verbunden' : ', nicht verbunden') : '' }}</p>
					</div>
				</svws-ui-button>
				<template v-if="connected">
					<svws-ui-button id="contentFocusField" :type="(aktuell === 'synchronize') ? 'primary' : 'secondary'" @click="onSelect('synchronize')">
						<div class="flex flex-col gap-1">
							<p class="text-left font-bold ">Synchronisieren</p>
							<p class="text-left font-normal">Abgleich der Daten mit dem Webnotenmanager</p>
						</div>
					</svws-ui-button>
					<svws-ui-button :type="(aktuell === 'reset') ? 'primary' : 'secondary'" @click="onSelect('reset')">
						<div class="flex flex-col gap-1">
							<p class="text-left font-bold ">Zurücksetzen</p>
							<p class="text-left font-normal">Daten auf dem Webnotenmanager entfernen</p>
						</div>
					</svws-ui-button>
				</template>
			</div>

			<!-- Weitere Eingabemöglichkeiten für den zuvor gewählten Untis-Import (rechte Seite - spezielle Ansicht nach Auswahl) -->
			<div class="flex flex-col gap-8">
				<div v-if="aktuell === 'setup'" class="max-w-196">
					<div class="text-headline-md mb-4">Verbindung zum Webnotenmanager einrichten</div>
					<template v-if="clientSecret === null">
						<svws-ui-input-wrapper>
							<svws-ui-text-input class="contentFocusField" v-model.trim="url" type="text" placeholder="URL" />
							<svws-ui-text-input v-model.trim="token" type="text" placeholder="Secret" />
							<svws-ui-button type="primary" @click="updateCredentials" :disabled="(url === '') || (token === '')">
								speichern
							</svws-ui-button>
						</svws-ui-input-wrapper>
					</template>
					<template v-else>
						<svws-ui-input-wrapper>
							<div>URL: {{ url }}</div>
							<svws-ui-button type="primary" @click="removeVerbindungsdaten">
								entfernen
							</svws-ui-button>
						</svws-ui-input-wrapper>
					</template>
				</div>
				<div v-if="aktuell === 'synchronize'" class="max-w-164">
					<div class="text-headline-md mb-4">Daten abgleichen</div>
					<svws-ui-input-wrapper>
						<div>
							<div> Führt einen Abgleich der Daten in beide Richtungen durch, indem zuerst die neuen lokalen Daten zum Webnotenmanager hochgeladen werden und anschließend neue Daten vom Webnotenmanager abgeholt werden. </div>
							<svws-ui-button type="primary" @click="call(synchronize)">
								<span class="i-ri-download-2-line icon" />
								<span class="i-ri-upload-2-line icon mr-2" />
								Synchronisieren
							</svws-ui-button>
						</div>
						<div>
							<div> Lädt die lokalen Daten zum Webnotenmanager hoch und aktualisiert diesen ggf. mit neueren Daten dort. </div>
							<svws-ui-button type="primary" @click="call(upload)">
								<span class="i-ri-upload-2-line icon mr-2" />
								Hochladen
							</svws-ui-button>
						</div>
						<div>
							<div> Lädt die Daten vom Webnotenmanager herunter und aktualisiert ggf. die lokalen Daten.</div>
							<svws-ui-button type="primary" @click="call(download)">
								<span class="i-ri-download-2-line icon mr-2" />
								Herunterladen
							</svws-ui-button>
						</div>
					</svws-ui-input-wrapper>
				</div>
				<div v-if="aktuell === 'reset'" class="max-w-164">
					<div class="text-headline-md mb-4">Zurücksetzen der Daten</div>
					<svws-ui-input-wrapper>
						<div>
							<div> Entfernt alle Daten des Lernabschnittes vom Webnotenmanager. Die Benutzerdaten bleiben erhalten.</div>
							<svws-ui-button type="primary" @click="call(reset)">
								Daten zurücksetzen
							</svws-ui-button>
						</div>
						<div>
							<div> Entfernt alle Daten des Lernabschnittes vom Webnotenmanager und auch die Benutzerdaten. Die dort gespeicherten Anmeldeinformationen gehen damit verloren.</div>
							<svws-ui-button type="danger" @click="call(truncate)">
								Daten und Benutzer zurücksetzen
							</svws-ui-button>
						</div>
					</svws-ui-input-wrapper>
				</div>
				<div>
					<svws-ui-spinner :spinning />
				</div>
			</div>

			<!-- Die Ausgabe des Logs -->
			<div v-if="status !== null" class="w-full h-full overflow-hidden flex flex-col gap-4">
				<log-box :logs="status.log" :status="status.success" />
			</div>
		</div>
	</div>


	<!-- <div class="svws-ui-page w-full">
		<div class="svws-ui-tab-content">
			<div class="page--content page--content--full">
				<div v-if="secretSet()">
					<svws-ui-content-card title="Aktuelle Abschnittsdaten sychronisieren">
						<div class="flex items-start gap-3">
							<svws-ui-button type="primary" @click="wenomSynchronize">
								synchronisieren
							</svws-ui-button>
						</div>
					</svws-ui-content-card>
					<svws-ui-content-card title="Aktuelle Abschnittsdaten löschen">
						<div class="flex items-start gap-3">
							<svws-ui-button type="primary" @click="wenomTruncate">
								Abschnittsdaten löschen
							</svws-ui-button>
						</div>
					</svws-ui-content-card>
					<svws-ui-content-card title="Zugangsdaten löschen">
						<div class="flex items-start gap-3">
							<svws-ui-button type="primary" @click="wenomRemoveCredentials">
								Zugangsdaten löschen
							</svws-ui-button>
						</div>
					</svws-ui-content-card>
				</div>
				<div v-else>
					<svws-ui-content-card title="Zugangsdaten zum Webnotenmanager">
						<div class="flex items-start gap-3">
							<svws-ui-text-input class="contentFocusField" v-model.trim="url" type="text" placeholder="URL" />
							<svws-ui-text-input v-model.trim="token" type="text" placeholder="Secret" />
							<svws-ui-button type="primary" @click="setWenomCredentials(url, token)" :disabled="!url || !token">
								speichern
							</svws-ui-button>
						</div>
					</svws-ui-content-card>
				</div>
			</div>
		</div>
	</div> -->
</template>

<script setup lang="ts">

	import { onMounted, ref, shallowRef } from "vue";
	import type { SchuleDatenaustauschWenomProps } from './SSchuleDatenaustauschWenomProps';
	import type { OAuth2ClientSecret, SimpleOperationResponse } from "@core";

	const props = defineProps<SchuleDatenaustauschWenomProps>();

	type WENOM = 'setup' | 'reset' | 'synchronize';
	const aktuell = ref<WENOM>('setup');

	const clientSecret = shallowRef<OAuth2ClientSecret | null>(null);
	const connected = ref<boolean>(false);

	onMounted(async () => await checkConnection(await props.getCredentials()));

	const url = ref<string>("");
	const token = ref<string>("");

	const status = ref<SimpleOperationResponse | null>(null);
	const spinning = ref<boolean>(false);


	function onSelect(value : WENOM): void {
		if (aktuell.value === value)
			return;
		aktuell.value = value;
		status.value = null;
	}

	async function checkConnection(secret: OAuth2ClientSecret | null) {
		clientSecret.value = secret;
		url.value = secret?.authServer ?? ''; // Die URL soll zu Beginn geladen werden
		token.value = ''; // das Token soll nach dem Einlesen oder Setzen nicht mehr sichtbar sein
		if (clientSecret.value !== null) {
			const res = await props.check();
			if (res.success)
				aktuell.value = 'synchronize';
			else
				status.value = res;
			connected.value = res.success;
		}
	}

	async function call(func: () => Promise<SimpleOperationResponse>) {
		status.value = null;
		spinning.value = true;
		status.value = await func();
		spinning.value = false;
	}

	async function updateCredentials() {
		status.value = null;
		spinning.value = true;
		const credentials = await props.setCredentials(url.value, token.value);
		await checkConnection(credentials);
		spinning.value = false;
	}

	async function removeVerbindungsdaten() {
		status.value = null;
		spinning.value = true;
		await props.removeCredentials();
		token.value = '';
		clientSecret.value = null;
		connected.value = false;
		spinning.value = false;
	}

</script>
