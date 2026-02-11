<template>
	<div class="page page-grid-cards">
		<div v-if="hatkeineErforderlicheKompetenz">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Kindergärten werden gelöscht">
				<div>
					<span v-if="selectedAllowedToDelete">Alle ausgewählten Kindergärten sind bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheckErrors" :key="message">
						<span class="text-ui-danger whitespace-pre-line"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen" class="mt-4"
						@click="handleDeleteClick"
						:disabled="!props.manager().liste.auswahlExists()" :is-loading>
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
			<svws-ui-modal v-model:show="warningModalIsShown"
				:auto-close="false" :close-in-title="false"
				size="small" type="danger">
				<template #modalTitle>
					<slot name="title">Daten gehen verloren</slot>
				</template>
				<template #modalDescription>
					<div class="text-left">
						<slot name="description">
							Durch das Löschen der Kindergärten werden auch alle Referenzen auf diese Einträge endgültig gelöscht.<br>
							Wollen Sie das Löschen wirklich durchführen?
						</slot>
					</div>
				</template>
				<template #modalActions>
					<svws-ui-button type="secondary" @click="cancel">Nein</svws-ui-button>
					<svws-ui-button type="danger" @click="deleteSelectedKindergaerten">Ja</svws-ui-button>
				</template>
			</svws-ui-modal>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { KindergaertenGruppenprozesseProps } from "~/components/schule/kataloge/kindergaerten/gruppenprozesse/KindergaertenGruppenprozesseProps";
	import type { List } from "@core";
	import { BenutzerKompetenz } from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<KindergaertenGruppenprozesseProps>();
	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatkeineErforderlicheKompetenz = computed(() => !hatKompetenzLoeschen.value);
	const selectedAllowedToDelete = computed<boolean>(() => props.deleteCheck().success);
	const deleteCheckErrors = computed<Iterable<string>>(() => props.deleteCheck().logs);
	const warningModalIsShown = ref<boolean>(false);


	function openWarningModal() {
		warningModalIsShown.value = true;
	}

	function closeWarningModal() {
		warningModalIsShown.value = false;
	}

	function handleDeleteClick() {
		if (selectedAllowedToDelete.value) {
			void deleteSelectedKindergaerten();
		} else {
			openWarningModal();
		}
	}

	async function deleteSelectedKindergaerten() {
		isLoading.value = true;
		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		isLoading.value = false;
		closeWarningModal();
	}

	function clearLog() {
		isLoading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function cancel(): Promise<void> {
		closeWarningModal();
		await props.gotoDefaultView(null);
	}

</script>
