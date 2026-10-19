<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<svws-ui-input-wrapper class="flex flex-col gap-4">
			<report-cards :ui-nutzung="ReportingReportvorlageUiNutzung.SCHUELER" :ids="idsJeDatenContext" :stundenplaene
				v-model:current-action="currentAction" @update:hat-vorlagen="hatVorlagen => hatReportCards = hatVorlagen" />
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen"
				subtitle="Setze einen Löschvermerk bei den ausgewählten Schülern." :is-open="currentAction === 'delete'"
				@update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div v-if="isDeleteConditionSectionVisible">
					<span v-if="selectedAllowedToDelete">Bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheckErrors" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="isDeleteDisabled" title="Löschen" @click="entferneSchueler" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
		</svws-ui-input-wrapper>
		<log-box :logs :status="statusAction">
			<template #button>
				<svws-ui-button v-if="statusAction !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
			</template>
		</log-box>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ReportingReportvorlageDatenContext } from "@core/core/types/reporting/ReportingReportvorlageDatenContext";
	import { ReportingReportvorlageUiNutzung } from "@core/core/types/reporting/ReportingReportvorlageUiNutzung";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useCurrentAction } from "@ui/ui/composables/useCurrentAction";

	import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";

	import type { SSchuelerAllgemeinesGruppenprozesseProps } from "./SSchuelerAllgemeinesGruppenprozesseProps";

	const props = defineProps<SSchuelerAllgemeinesGruppenprozesseProps>();
	const benutzerState = useBenutzerState();
	const schuelerAuswahlState = useSchuelerAuswahlState();

	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LOESCHEN));

	const hatReportCards = ref<boolean>(false);
	const hatIrgendwelcheKompetenzen = computed(() => hatReportCards.value || hatKompetenzLoeschen.value);

	const idsJeDatenContext = new Map<ReportingReportvorlageDatenContext, () => List<number>>([
		[ReportingReportvorlageDatenContext.SCHUELER, () => schuelerAuswahlState.manager.liste.auswahlKeyList()],
		[ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER, () => schuelerAuswahlState.manager.liste.auswahlKeyList()],
	]);

	const stundenplaene = computed(() => [...schuelerAuswahlState.mapStundenplaene.values()]);

	const isDeleteDisabled = computed<boolean>(() => !hatKompetenzLoeschen.value || !schuelerAuswahlState.manager.liste.auswahlExists() || !selectedAllowedToDelete.value || loading.value);
	const deleteCheckErrors = computed<List<string>>(() => schuelerAuswahlState.deleteSchuelerCheck()[1]);
	const selectedAllowedToDelete = computed(() => schuelerAuswahlState.deleteSchuelerCheck()[0]);
	const isDeleteConditionSectionVisible = computed<boolean>(() => (schuelerAuswahlState.manager.liste.auswahlExists() || (statusAction.value === undefined)));

	const { currentAction, setCurrentAction } = useCurrentAction();
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const statusAction = ref<boolean | undefined>();

	async function entferneSchueler() {
		loading.value = true;
		[statusAction.value, logs.value] = await schuelerAuswahlState.delete();
		loading.value = false;
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		statusAction.value = undefined;
	}

</script>
