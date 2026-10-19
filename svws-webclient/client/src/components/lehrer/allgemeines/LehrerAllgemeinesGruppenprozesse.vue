<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper class="flex flex-col gap-4">
			<report-cards :ui-nutzung="ReportingReportvorlageUiNutzung.LEHRER" :ids="idsJeDatenContext" :stundenplaene v-model:current-action="currentAction" />
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen"
				subtitle="Setze einen Löschvermerk bei den ausgewählten Lehrkräften." :is-open="currentAction === 'delete'"
				@update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div v-if="isDeleteConditionSectionVisible">
					<span v-if="selectedAllowedToDelete">Bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheckErrors" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="isDeleteDisabled" title="Löschen" @click="entferneLehrer" :is-loading="loading" class="mt-4">
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

	import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

	import type { LehrerAllgemeinesGruppenprozesseProps } from "./LehrerAllgemeinesGruppenprozesseProps";

	const props = defineProps<LehrerAllgemeinesGruppenprozesseProps>();
	const benutzerState = useBenutzerState();
	const lehrerAuswahlState = useLehrerAuswahlState();

	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LOESCHEN));

	const isDeleteDisabled = computed<boolean>(() => !hatKompetenzLoeschen.value || !lehrerAuswahlState.manager.liste.auswahlExists() || !selectedAllowedToDelete.value || loading.value);
	const deleteCheckErrors = computed<Iterable<string>>(() => lehrerAuswahlState.deleteCheck()[1]);
	const selectedAllowedToDelete = computed<boolean>(() => lehrerAuswahlState.deleteCheck()[0]);
	const isDeleteConditionSectionVisible = computed<boolean>(() => (lehrerAuswahlState.manager.liste.auswahlExists() || (statusAction.value === undefined)));

	const idsJeDatenContext = new Map<ReportingReportvorlageDatenContext, () => List<number>>([
		[ReportingReportvorlageDatenContext.LEHRER, () => lehrerAuswahlState.manager.liste.auswahlKeyList()],
		[ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER, () => lehrerAuswahlState.manager.liste.auswahlKeyList()],
	]);

	const stundenplaene = computed(() => [...lehrerAuswahlState.mapStundenplaene.values()]);

	const { currentAction, setCurrentAction } = useCurrentAction();
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const statusAction = ref<boolean | undefined>();

	async function entferneLehrer() {
		loading.value = true;
		[statusAction.value, logs.value] = await lehrerAuswahlState.delete();
		loading.value = false;
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		statusAction.value = undefined;
	}

</script>
