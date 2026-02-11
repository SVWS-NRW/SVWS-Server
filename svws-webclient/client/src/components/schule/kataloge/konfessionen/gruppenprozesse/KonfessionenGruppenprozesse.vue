<template>
	<div class="page page-grid-cards">
		<div v-if="hatkeineErforderlicheKompetenz">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div class="flex flex-col gap-4">
			<ui-card icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Konfessionen werden gelöscht." v-if="hatKompetenzLoeschen">
				<div>
					<span v-if="selectedAllowedToDelete">Alle ausgewählten Konfessionen sind bereit zum Löschen.</span>
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
							Durch das Löschen der Konfessionen werden auch alle referenzierenden Einträge bei den Schülern endgültig gelöscht.<br>
							Wollen Sie das Löschen wirklich durchführen?
						</slot>
					</div>
				</template>
				<template #modalActions>
					<svws-ui-button type="secondary" @click="cancel">Nein</svws-ui-button>
					<svws-ui-button type="danger" @click="confirmDelete">Ja</svws-ui-button>
				</template>
			</svws-ui-modal>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { KonfessionenGruppenprozesseProps } from "~/components/schule/kataloge/konfessionen/gruppenprozesse/KonfessionenGruppenprozesseProps";
	import type { List } from "@core";
	import { computed, ref } from "vue";
	import { BenutzerKompetenz } from "@core";

	const props = defineProps<KonfessionenGruppenprozesseProps>();
	const isLoading = ref<boolean>(false);
	const warningModalIsShown = ref<boolean>(false);
	const status = ref<boolean | undefined>();
	const logs = ref<List<string | null> | undefined>();
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatkeineErforderlicheKompetenz = computed(() => !hatKompetenzLoeschen.value);
	const selectedAllowedToDelete = computed<boolean>(() => props.deleteCheck().success);
	const deleteCheckErrors = computed<Iterable<string>>(() => props.deleteCheck().logs);
	const hatReferenzen = computed<boolean>(() => !props.manager().idsReferencedKonfessionen.isEmpty());

	function openWarningModal() {
		warningModalIsShown.value = true;
	}

	function closeWarningModal() {
		warningModalIsShown.value = false;
	}

	function handleDeleteClick() {
		if (hatReferenzen.value) {
			openWarningModal();
		} else {
			void deleteKonfessionen();
		}
	}
	async function deleteKonfessionen() {
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

	async function cancel(): Promise<void> {
		closeWarningModal();
		await props.gotoDefaultView(null);
	}

	async function confirmDelete(): Promise<void> {
		closeWarningModal();
		await deleteKonfessionen();
	}

</script>
