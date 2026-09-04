<template>
	<div class="page page-grid-cards">
		<div v-if="hatKeineErforderlicheKompetenz">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzLoeschen" title="Löschen" subtitle="Ausgewählte Ankreuzkompetenzen werden gelöscht." icon="i-ri-delete-bin-line">
				<div v-if="isPreConditionSectionVisible">
					<span v-if="selectedAllowedToDelete">Alle ausgewählten Ankreuzkompetenzen sind bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheckErrors" :key="message">
						<span class="text-ui-danger whitespace-pre-line"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen" class="mt-4"
						@click="deleteSelectedAnkreuzkompetenzen"
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
						@click="clearLog" title="Log verwerfen">
						Log verwerfen
					</svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { computed, ref } from "vue";
	import type { AnkreuzkompetenzenGruppenprozesseProps } from "~/components/schule/kataloge/ankreuzkompetenzen/gruppenprozesse/AnkreuzkompetenzenGruppenprozesseProps";

	const props = defineProps<AnkreuzkompetenzenGruppenprozesseProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatKeineErforderlicheKompetenz = computed<boolean>(() => !hatKompetenzLoeschen.value);
	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();
	const deleteCheckErrors = computed<List<string>>(() => props.deleteCheck()[1]);
	const selectedAllowedToDelete = computed<boolean>(() => props.deleteCheck()[0]);
	const isPreConditionSectionVisible = computed<boolean>(() => (props.manager().liste.auswahlExists() || (status.value === undefined)));

	async function deleteSelectedAnkreuzkompetenzen() {
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
