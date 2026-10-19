<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div class="flex flex-col gap-4">
			<report-cards :ui-nutzung="ReportingReportvorlageUiNutzung.KLASSEN" :ids="idsJeDatenContext" :stundenplaene
				v-model:current-action="currentAction" @update:hat-vorlagen="hatVorlagen => hatReportCards = hatVorlagen" />
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Klassen werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div class="w-full">
					<span v-if="alleKlassenLeer">Alle ausgewählten Klassen sind bereit zum Löschen.</span>
					<span v-if="leereKlassenVorhanden">Einige Klassen haben noch Schüler, leere Klassen können gelöscht werden.</span>
					<div v-if="!alleKlassenLeer">
						<span v-for="message in nichtAlleKlassenLeer" :key="message" class="text-ui-danger"> {{ message }} <br> </span>
					</div>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="klassenState.manager.getKlassenIDsMitSchuelern().size() === klassenState.manager.liste.auswahlSize() || loading"
						:title="leereKlassenVorhanden ? 'Leere Klassen löschen' : 'Löschen'" @click="entferneKlassen" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						{{ leereKlassenVorhanden ? 'Leere Klassen löschen' : 'Löschen' }}
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
	import { useCurrentAction } from "@ui/ui/composables/useCurrentAction";

	import { useKlassenAuswahlState } from "~/states/klassen/KlassenAuswahlState";

	import type { KlassenGruppenprozesseProps } from "./KlassenGruppenprozesseProps";

	const props = defineProps<KlassenGruppenprozesseProps>();
	const klassenState = useKlassenAuswahlState();
	const benutzerState = useBenutzerState();

	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const hatReportCards = ref<boolean>(false);
	const hatIrgendwelcheKompetenzen = computed(() => hatReportCards.value || hatKompetenzLoeschen.value);

	/** Die Klassen liefern Klassen-IDs und für schülerbezogene Vorlagen die IDs der Schüler aus den gewählten Klassen. */
	const idsJeDatenContext = new Map<ReportingReportvorlageDatenContext, () => List<number>>([
		[ReportingReportvorlageDatenContext.KLASSEN, () => klassenState.manager.liste.auswahlKeyList()],
		[ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN, () => klassenState.manager.liste.auswahlKeyList()],
		[ReportingReportvorlageDatenContext.SCHUELER, () => klassenState.manager.getSchuelerIDsDerAuswahl()],
	]);

	const stundenplaene = computed(() => [...klassenState.mapStundenplaene.values()]);

	const { currentAction, setCurrentAction } = useCurrentAction();

	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const alleKlassenLeer = computed(() => (currentAction.value === 'delete') && klassenState.manager.getKlassenIDsMitSchuelern().isEmpty());

	const nichtAlleKlassenLeer = computed(() => {
		const errorLog: List<string> = new ArrayList<string>();
		if (!alleKlassenLeer.value) {
			for (const klasse of klassenState.manager.getKlassenIDsMitSchuelern()) {
				errorLog.add(`Klasse ${klassenState.manager.liste.get(klasse)?.kuerzel ?? '???'} (ID: ${klasse}) kann nicht gelöscht werden, da ihr noch Schüler zugeordnet sind.`);
			}
		}
		return errorLog;
	});

	const leereKlassenVorhanden = computed(() =>
		!alleKlassenLeer.value && (klassenState.manager.getKlassenIDsMitSchuelern().size() !== klassenState.manager.liste.auswahlSize()));

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneKlassen() {
		loading.value = true;

		const [delStatus, logMessages] = await klassenState.delete();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}
</script>
