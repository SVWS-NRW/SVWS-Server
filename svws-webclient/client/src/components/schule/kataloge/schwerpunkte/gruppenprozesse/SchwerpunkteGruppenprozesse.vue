<template>
	<div class="page page-grid-cards">
		<div v-if="hatKeineErforderlicheKompetenz">
			Für die Nutzung der Gruppenprozesse fehlen die Benutzerkompetenzen.
		</div>
		<div v-if="true" class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzLoeschen"
				icon="i-ri-delete-bin-line"
				title="Löschen"
				subtitle="Ausgewählte Schwerpunkte werden gelöscht.">
				<div>
					<span v-if="selectedAllowedToDelete">Alle ausgewählten Schwerpunkte sind bereit zum Löschen.</span>
					<template v-else>
						<template v-for="message in deleteCheckErrors" :key="message">
							<span class="text-ui-danger whitespace-pre-line"> {{ message }} <br> </span>
						</template>
						Schwerpunkte
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen"
						class="mt-4"
						:disabled="!selectedAllowedToDelete || !props.manager().liste.auswahlExists()"
						:is-loading
						@click="deleteSelectedSchwerpunkte">
						<svws-ui-spinner v-if="isLoading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import type { List } from "@core";
	import { BenutzerKompetenz } from "@core";
	import type { SchwerpunkteGruppenprozesseProps } from './SchwerpunkteGruppenprozesseProps';

	const props = defineProps<SchwerpunkteGruppenprozesseProps>();

	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const hatKompetenzLoeschen = computed(() => {
		return props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	});
	const hatKeineErforderlicheKompetenz = computed(() => !hatKompetenzLoeschen.value);
	const selectedAllowedToDelete = computed<boolean>(() => props.deleteCheck().success);
	const deleteCheckErrors = computed<Iterable<string>>(() => props.deleteCheck().logs);

	async function deleteSelectedSchwerpunkte() {
		isLoading.value = true;
		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		isLoading.value = false;
	}

	function clearLog() {
		isLoading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}
</script>
