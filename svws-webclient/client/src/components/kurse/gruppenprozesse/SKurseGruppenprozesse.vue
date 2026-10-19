<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div class="flex flex-col gap-4">
			<report-cards :ui-nutzung="ReportingReportvorlageUiNutzung.KURSE" :ids="idsJeDatenContext"
				v-model:current-action="currentAction" @update:hat-vorlagen="hatVorlagen => hatReportCards = hatVorlagen" />
			<!-- Karte: Löschen (bestehende Funktionalität, DEV) -->
			<ui-card v-if="serverState.hasDev && hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Kurse werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="preConditionCheck.success">Alle ausgewählten Kurse sind bereit zum Löschen.</span>
					<template v-else v-for="(message, i) in preConditionCheck.logs" :key="i">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="!preConditionCheck.success || loading" title="Löschen" @click="entferneKurse" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
			<log-box v-if="hatIrgendwelcheKompetenzen" :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";

	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ReportingReportvorlageDatenContext } from "@core/core/types/reporting/ReportingReportvorlageDatenContext";
	import { ReportingReportvorlageUiNutzung } from "@core/core/types/reporting/ReportingReportvorlageUiNutzung";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useServerState } from "@ui/states/ServerState";
	import { useCurrentAction } from "@ui/ui/composables/useCurrentAction";

	import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";

	import type { KurseGruppenprozesseProps } from "./SKurseGruppenprozesseProps";

	const props = defineProps<KurseGruppenprozesseProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const kurseAuswahlState = useKurseAuswahlState();

	const { currentAction, setCurrentAction } = useCurrentAction();
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const hatReportCards = ref<boolean>(false);
	const hatIrgendwelcheKompetenzen = computed(() => hatReportCards.value || hatKompetenzLoeschen.value);

	const idsJeDatenContext = new Map<ReportingReportvorlageDatenContext, () => List<number>>([
		[ReportingReportvorlageDatenContext.KURSE, () => kurseAuswahlState.manager.liste.auswahlKeyList()],
	]);

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete') {
			return { success: true, logs: new ArrayList<string>() };
		}
		return { success: false, logs: new ArrayList<string>() };
	});

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneKurse() {
		loading.value = true;

		const [delStatus, logMessages] = await kurseAuswahlState.delete();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

</script>
