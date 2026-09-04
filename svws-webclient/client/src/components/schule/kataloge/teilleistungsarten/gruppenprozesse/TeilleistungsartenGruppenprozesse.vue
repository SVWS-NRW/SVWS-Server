<template>
	<div class="page page-grid-cards">
		<div v-if="hatKeineErforderlicheKompetenz">
			Für die Nutzung der Gruppenprozesse fehlen die Benutzerkompetenzen.
		</div>
		<div v-if="true" class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzLoeschen"
				icon="i-ri-delete-bin-line"
				title="Löschen"
				subtitle="Ausgewählte Teilleistungsarten werden gelöscht.">
				<div v-if="isPreConditionSectionVisible">
					<span v-if="selectedAllowedToDelete">Alle ausgewählten Teilleistungsarten sind bereit zum Löschen.</span>
					<template v-else>
						<template v-for="message in deleteCheckErrors" :key="message">
							<span class="text-ui-danger whitespace-pre-line"> {{ message }} <br> </span>
						</template>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen"
						class="mt-4"
						:disabled="!selectedAllowedToDelete || !props.manager().liste.auswahlExists()"
						:is-loading
						@click="deleteSelectedTeilleistungsarten">
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
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { computed, ref } from "vue";
	import type { TeilleistungsartenGruppenprozesseProps } from "./TeilleistungsartenGruppenprozesseProps";

	const props = defineProps<TeilleistungsartenGruppenprozesseProps>();
	const benutzerState = useBenutzerState();

	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const hatKompetenzLoeschen = computed(() => {
		return benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	});
	const hatKeineErforderlicheKompetenz = computed(() => !hatKompetenzLoeschen.value);
	const selectedAllowedToDelete = computed<boolean>(() => props.deleteCheck().success);
	const deleteCheckErrors = computed<Iterable<string>>(() => props.deleteCheck().logs);
	const isPreConditionSectionVisible = computed<boolean>(() => (props.manager().liste.auswahlExists() || (status.value === undefined)));

	async function deleteSelectedTeilleistungsarten() {
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
