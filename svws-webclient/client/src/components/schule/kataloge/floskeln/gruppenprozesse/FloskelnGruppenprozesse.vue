<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Floskeln werden gelöscht">
				<div v-if="isPreConditionSectionVisible">
					<span v-if="manager().liste.auswahlExists()"> Alle ausgewählten Floskeln sind bereit zum Löschen.</span>
					<span v-else class="text-ui-danger whitespace-pre-line">Es wurde keine Floskel zum Löschen ausgewählt.</span>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen" class="mt-4"
						@click="deleteFloskeln"
						:disabled="!props.manager().liste.auswahlExists()" :is-loading>
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

	import { computed, ref } from "vue";
	import type { FloskelnGruppenprozesseProps } from "./FloskelnGruppenprozesseProps";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const props = defineProps<FloskelnGruppenprozesseProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatIrgendwelcheKompetenzen = computed(() => hatKompetenzLoeschen.value);
	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();
	const isPreConditionSectionVisible = computed<boolean>(() => (props.manager().liste.auswahlExists() || (status.value === undefined)));

	async function deleteFloskeln() {
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
