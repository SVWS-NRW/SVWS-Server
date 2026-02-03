<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div v-if="ServerMode.DEV.checkServerMode(serverMode)" class="flex flex-col">
			<ui-card v-if="hatKompetenzLoeschen" title="Löschen" subtitle="Ausgewählte Lernplattformen werden gelöscht." icon="i-ri-delete-bin-line">
				<div>
					<span v-if="preConditionCheck.success">Alle ausgewählten Lernplattformen sind bereit zum Löschen.</span>
					<template v-else v-for="message in preConditionCheck.logs" :key="message">
						<span class="text-ui-danger whitespace-pre-line"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen" class="mt-4"
						@click="toggleWarningModal"
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
			<svws-ui-modal v-model:show="warningModalIsShown"
				:auto-close="false" :close-in-title="false"
				size="small" type="danger">
				<template #modalTitle>
					<slot name="title">Daten gehen verloren</slot>
				</template>
				<template #modalDescription>
					<div class="text-left">
						<slot name="description">
							Durch das Löschen der Lernplattformen werden auch alle referenzierenden Einträge endgültig gelöscht.<br>
							Wollen Sie das Löschen wirklich durchführen?
						</slot>
					</div>
				</template>
				<template #modalActions>
					<svws-ui-button type="secondary" @click="cancel">Nein</svws-ui-button>
					<svws-ui-button type="danger" @click="deleteSelectedLernplattformen">Ja</svws-ui-button>
				</template>
			</svws-ui-modal>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import { BenutzerKompetenz, type List, ServerMode } from "@core";
	import type { LernplattformenGruppenprozesseProps } from "~/components/schule/kataloge/lernplattformen/gruppenprozesse/LernplattformenGruppenprozesseProps";

	const props = defineProps<LernplattformenGruppenprozesseProps>();
	const isLoading = ref<boolean>(false);
	const warningModalIsShown = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatIrgendwelcheKompetenzen = computed<boolean>(() => hatKompetenzLoeschen.value);
	const preConditionCheck = computed(() => props.deleteCheck());

	function toggleWarningModal() {
		warningModalIsShown.value = !warningModalIsShown.value;
	}

	function clearLog() {
		isLoading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function deleteSelectedLernplattformen() {
		isLoading.value = true;
		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		isLoading.value = false;
		toggleWarningModal();
	}

	async function cancel(): Promise<void> {
		await props.gotoDefaultView(null);
	}

</script>
