<template>
	<div class="page page-flex-row">
		<!-- Daten mit dem Webnotenmanager synchronisieren -->
		<div class="max-w-164">
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

		<!-- Den Webnotenmanager zurücksetzen -->
		<div class="max-w-164">
			<div class="text-headline-md mb-4">Entfernen der Daten</div>
			<svws-ui-input-wrapper>
				<div>
					<div> Entfernt alle Daten des Lernabschnittes vom Webnotenmanager. Die Benutzerdaten bleiben auf dem Server des Webnotenmanagers zwar erhalten, eine Anmeldung am Client des Webnotenmanagers ist danach aber nicht mehr möglich da keine Leistungsdaten vorhanden sind.</div>
					<svws-ui-button type="primary" @click="call(reset)">
						Daten entfernen
					</svws-ui-button>
				</div>
				<div>
					<div> Entfernt alle Daten des Lernabschnittes vom Webnotenmanager, die Konfigurationseinstellungen und auch die Benutzerdaten. Die dort gespeicherten Anmeldeinformationen gehen damit verloren.</div>
					<svws-ui-button type="danger" @click="call(truncate)">
						Daten, Konfiguration und Benutzer entfernen
					</svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { SimpleOperationResponse } from '@core';
	import type { NotenmodulSynchronisationProps } from './NotenmodulSynchronisationProps';

	const props = defineProps<NotenmodulSynchronisationProps>();

	const status = ref<SimpleOperationResponse | null>(null);
	const spinning = ref<boolean>(false);

	async function call(func: () => Promise<SimpleOperationResponse>) {
		status.value = null;
		spinning.value = true;
		status.value = await func();
		spinning.value = false;
	}

</script>