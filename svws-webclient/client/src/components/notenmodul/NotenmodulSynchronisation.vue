<template>
	<div class="page page-flex-row">
		<div class="min-w-fit flex flex-col gap-8">
			<!-- Daten mit dem WebNotenManager synchronisieren -->
			<div class="max-w-164">
				<div class="text-headline-md mb-4">Daten abgleichen</div>
				<svws-ui-input-wrapper>
					<div>
						<div> Lädt die lokalen Daten zum WebNotenManager hoch und aktualisiert diesen ggf. zeitstempelbasiert mit neueren Daten. </div>
						<svws-ui-button type="primary" @click="call(upload)">
							<span class="i-ri-upload-2-line icon mr-2" />
							Hochladen
						</svws-ui-button>
					</div>
					<div>
						<div> Lädt die Daten vom WebNotenManager herunter und aktualisiert ggf. zeitstempelbasiert die lokalen Daten.</div>
						<svws-ui-button type="primary" @click="call(download)">
							<span class="i-ri-download-2-line icon mr-2" />
							Herunterladen
						</svws-ui-button>
					</div>
					<div>
						<div>
							Führt einen Abgleich der Daten in beide Richtungen durch, indem zuerst neue Daten vom WebNotenManager abgeholt werden und anschließend neue lokale Daten in den WebNotenManager hochgeladen werden.
						</div>
						<svws-ui-button type="primary" @click="call(synchronize)">
							<span class="i-ri-download-2-line icon" />
							<span class="i-ri-upload-2-line icon mr-2" />
							Synchronisieren
						</svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
			</div>
			<!-- Den WebNotenManager zurücksetzen -->
			<div class="max-w-164">
				<div class="text-headline-md mb-4">Entfernen der Daten</div>
				<div>Achtung, wenn Daten auf dem WebNotenManager entfernt werden, gehen alle dort eingegebenen Daten unwiderruflich verloren. Dies sollte nur durchgeführt werden, wenn alle Daten synchronisiert worden sind oder der WebNotenManager für den aktuellen Lernabschnitt nicht weiter verwendet wird.</div>
				<svws-ui-button type="danger" @click="show = true">
					Den Löschdialog öffnen …
				</svws-ui-button>
				<svws-ui-modal v-model:show="show" size="medium" class="hidden" type="danger">
					<template #modalTitle>Daten entfernen</template>
					<template #modalDescription>
						<div class="text-justify space-y-4">
							<div>Es wird unterschieden zwischen den <span class="font-bold">Lernabschnittsdaten</span> und <span class="font-bold">Allen Daten</span>.</div>
							<div>Ersteres entfernt alle Daten des Lernabschnittes vom WebNotenManager. Die Benutzerdaten bleiben auf dem Server des WebNotenManagers zwar erhalten, eine Anmeldung am Client des WebNotenManagers ist danach aber nicht mehr möglich da keine Leistungsdaten vorhanden sind.</div>
							<div>Letzteres entfernt alle Daten des Lernabschnittes vom WebNotenManager, die Konfigurationseinstellungen und auch die Benutzerdaten. Die dort gespeicherten Anmeldeinformationen gehen damit verloren.</div>
						</div>
					</template>
					<template #modalActions>
						<div class="w-full flex justify-between">
							<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
							<div class="flex flex-row">
								<svws-ui-button type="danger" @click="call(reset).then(() => show = false)">
									Lernabschnittsdaten entfernen
								</svws-ui-button>
								<svws-ui-button class="justify-self-end" type="danger" @click="call(truncate).then(() => show = false)">Alle Daten entfernen</svws-ui-button>
							</div>
						</div>
					</template>
				</svws-ui-modal>
			</div>
		</div>

		<!-- Die Ausgabe des Logs -->
		<div v-if="spinning" class="flex items-center h-12 gap-2"><svws-ui-spinner spinning /> Führe Aktion aus…</div>
		<div v-else-if="status !== null && manager().auswahl().id > 0" class="min-w-fit grow h-full overflow-hidden flex flex-col gap-4">
			<log-box :logs="status.log" :status="status.success" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { NotenmodulSynchronisationProps } from './NotenmodulSynchronisationProps';
	import type { SimpleOperationResponse } from '@core/core/data/SimpleOperationResponse';

	const props = defineProps<NotenmodulSynchronisationProps>();

	const status = ref<SimpleOperationResponse | null>(null);
	const spinning = ref<boolean>(false);
	const show = ref(false);

	async function call(func: () => Promise<SimpleOperationResponse>) {
		status.value = null;
		spinning.value = true;
		status.value = await func();
		spinning.value = false;
	}

</script>
