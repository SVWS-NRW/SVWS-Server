<template>
	<div class="page page-grid-cards">
		<div v-if="hatkeineErforderlicheKompetenz">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzLoeschen" title="Löschen" subtitle="Ausgewählte Betriebe werden gelöscht" icon="i-ri-delete-bin-line">
				<div>
					<span v-if="selectedAllowedToDelete">Alle ausgewählten Betriebe sind bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheckErrors" :key="message">
						<span class="text-ui-danger whitespace-pre-line"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen" class="mt-4"
						@click="deleteSelectedBetriebe"
						:disabled="!selectedAllowedToDelete || !props.manager().liste.auswahlExists()" :is-loading>
						<svws-ui-spinner v-if="isLoading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent"
						@click="clearLog">
						Log verwerfen
					</svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import { BenutzerKompetenz, type List } from "@core";
	import type { BetriebeGruppenprozesseProps } from "~/components/schule/kataloge/betriebe/gruppenprozesse/BetriebeGruppenprozesseProps";

	const props = defineProps<BetriebeGruppenprozesseProps>();
	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();
	const hatKompetenzLoeschen = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatkeineErforderlicheKompetenz = computed<boolean>(() => !hatKompetenzLoeschen.value);
	const selectedAllowedToDelete = computed<boolean>(() => props.deleteCheck().success);
	const deleteCheckErrors = computed<Iterable<string>>(() => props.deleteCheck().logs);

	async function deleteSelectedBetriebe() {
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
