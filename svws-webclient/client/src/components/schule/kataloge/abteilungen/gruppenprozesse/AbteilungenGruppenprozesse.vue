<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Abteilungen werden gelöscht">
				<div v-if="isPreConditionSectionVisible" class="w-full">
					<svws-ui-checkbox v-model="deleteAbteilungenInFolgeAbschnitt">
						Zusätzlich im Folgeabschnitt ({{ getTextFolgeAbschnitt() }}) löschen.
					</svws-ui-checkbox>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button	title="Löschen" class="mt-4"
						@click="deleteSelectedAbteilungen"
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
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { AbteilungenGruppenprozesseProps } from "./AbteilungenGruppenprozesseProps";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import type { List } from "@core/java/util/List";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";

	const abschnittState = useAbschnittState();
	const props = defineProps<AbteilungenGruppenprozesseProps>();
	const benutzerState = useBenutzerState();

	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();
	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const isPreConditionSectionVisible = computed<boolean>(() => (props.manager().liste.auswahlExists() || (status.value === undefined)));
	const deleteAbteilungenInFolgeAbschnitt = computed({
		get: () => props.manager().deleteAbteilungenInFolgeAbschnitt,
		set: (value: boolean) => props.manager().deleteAbteilungenInFolgeAbschnitt = value,
	});

	async function deleteSelectedAbteilungen() {
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

	function getTextFolgeAbschnitt() {
		const folgeAbschnitt = props.manager().schuljahresabschnitte.get(abschnittState.auswahl.idFolgeAbschnitt ?? -1);
		if ((folgeAbschnitt === null) || (folgeAbschnitt.schuljahr <= 0)) {
			return '';
		}
		return `${folgeAbschnitt.schuljahr}/${(folgeAbschnitt.schuljahr + 1) % 100}.${folgeAbschnitt.abschnitt}`;
	}

</script>
