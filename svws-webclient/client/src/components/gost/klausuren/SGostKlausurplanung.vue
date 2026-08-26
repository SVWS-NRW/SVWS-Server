<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-button-select v-if="!state.manager.terminGetMengeAsList().isEmpty()" type="secondary" :dropdown-actions="dropdownList">
			<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon i-ri-printer-line" v-else /> </template>
		</svws-ui-button-select>
	</Teleport>
	<Teleport to=".svws-sub-nav-target" defer>
		<nav class="svws-ui-secondary-tabs">
			<svws-ui-tab-bar :tab-manager secondary :focus-switching-enabled :focus-help-visible>
				<template #badge="{ tab }">
					<template v-if="(tab.name === 'gost.klausurplanung.probleme') && state.manager.hasFehlenddatenZuAbijahrUndHalbjahr(state.jahrgangsdaten.abiturjahr, state.halbjahr)">
						<div class="font-bold text-ui-ondanger bg-ui-danger rounded-full shadow-sm h-5 ml-1 -mt-2 px-1.5 pt-0.5" v-if="numErrors">{{ numErrors }}</div>
						<div class="font-bold text-ui-oncaution bg-ui-caution rounded-full shadow-sm h-5 ml-1 -mt-2 px-1.5 pt-0.5" v-if="numWarnings">{{ numWarnings }}</div>
					</template>
				</template>
			</svws-ui-tab-bar>
		</nav>
		<svws-ui-sub-nav />
	</Teleport>
	<router-view />
</template>

<script setup lang="ts">

	import { RouterView } from "vue-router";
	import type { DownloadPDFTypen } from "./DownloadPDFTypen";
	import type { GostKlausurplanungProps } from "./SGostKlausurplanungProps";
	import { computed, onMounted, ref } from "vue";
	import { useAbschnittState, useConfigState, useGostKlausurplanungState, useRegionSwitch, useReportingState } from "@ui";
	import { SGostKlausurplanungVorgabenIgnoreManager } from "~/components/gost/klausuren/SGostKlausurplanungVorgabenIgnoreManager";
	import { ArrayList, ReportingReportvorlage } from "@core";

	const props = defineProps<GostKlausurplanungProps>();
	const state = useGostKlausurplanungState();
	const abschnittState = useAbschnittState();
	const reportingState = useReportingState();
	const configState = useConfigState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const vorgabenIgnoreManager = new SGostKlausurplanungVorgabenIgnoreManager(
		(key, fromJSON) => configState.config.getObjectValue(key, fromJSON),
		undefined
	);

	const numErrors = computed<number>(() => {
		if (state.jahrgangsdaten.abiturjahr === -1) {
			return 0;
		}
		return state.manager.planungsfehlerGetAnzahlByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, state.kwErrorLimit, vorgabenIgnoreManager.getAll());
	});
	const numWarnings = computed<number>(() => {
		if (state.jahrgangsdaten.abiturjahr === -1) {
			return 0;
		}
		return state.manager.planungshinweiseGetAnzahlByHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal, state.kwWarnLimit, state.kwErrorLimit);
	});

	const dropdownList = [
		{ text: "Klausurplan (Kurse)", action: () => downloadPDF("Klausurplan (Kurse)") },
		{ text: "Klausurplan (Nachschreiber)", action: () => downloadPDF("Klausurplan (Nachschreiber)") },
		{ text: "Klausurplan (Kurse und Nachschreiber)", action: () => downloadPDF("Klausurplan (Kurse und Nachschreiber)") },
		{ text: "Klausurplan (detailliert)", action: () => downloadPDF("Klausurplan (detailliert)"), default: true },
		{ text: "Schüler-Klausurplan (gesamt)", action: () => downloadPDF("Schüler-Klausurplan (gesamt)") },
		{ text: "Schüler-Klausurplan (einzeln)", action: () => downloadPDF("Schüler-Klausurplan (einzeln)") },
		{ text: "Klausurplan alle Jgst. (Kurse)", action: () => downloadPDF("Klausurplan alle Jgst. (Kurse)") },
		{ text: "Klausurplan alle Jgst. (Nachschreiber)", action: () => downloadPDF("Klausurplan alle Jgst. (Nachschreiber)") },
		{ text: "Klausurplan alle Jgst. (Kurse und Nachschreiber)", action: () => downloadPDF("Klausurplan alle Jgst. (Kurse und Nachschreiber)") },
		{ text: "Klausurplan alle Jgst. (detailliert)", action: () => downloadPDF("Klausurplan alle Jgst. (detailliert)") },
		{ text: "Schüler-Klausurplan alle Jgst. (gesamt)", action: () => downloadPDF("Schüler-Klausurplan alle Jgst. (gesamt)") },
		{ text: "Schüler-Klausurplan alle Jgst. (einzeln)", action: () => downloadPDF("Schüler-Klausurplan alle Jgst. (einzeln)") },
	];

	async function downloadPDF(title: DownloadPDFTypen) {
		const istKlausurplan = title.startsWith("Klausurplan", 0);

		const reportvorlage = istKlausurplan
			? ReportingReportvorlage.GOST_KLAUSURPLANUNG_V_KLAUSURTERMINE_MIT_KURSEN
			: ReportingReportvorlage.GOST_KLAUSURPLANUNG_V_SCHUELER_MIT_KLAUSUREN;

		const reportingParameter = reportvorlage.getReportingParameter();

		if (istKlausurplan) {
			const istDetailliert = title.indexOf("detailliert") > 0;
			reportvorlage.setReportingParameterVorlageparameter(reportingParameter, "mitKursklausuren", ((title.indexOf("Kurse") > 0) || istDetailliert).toString());
			reportvorlage.setReportingParameterVorlageparameter(reportingParameter, "mitNachschreibern", ((title.indexOf("Nachschreiber") > 0) || istDetailliert).toString());
			reportvorlage.setReportingParameterVorlageparameter(reportingParameter, "mitKlausurschreiberNamen", istDetailliert.toString());
		}

		reportvorlage.setReportingParameterVorlageparameter(reportingParameter, "einzelausgabeDaten", (title.indexOf("einzeln") > 0).toString());

		reportingParameter.idSchuljahresabschnitt = abschnittState.auswahl.id;
		reportingParameter.idsHauptdaten = new ArrayList<number>();

		if (title.indexOf(" alle ") <= 0) {
			// Die ID für ein bestimmtes Gost-Halbjahr eines Abiturjahrgangs wird als fünfstellige Zahl codiert.
			reportingParameter.idsHauptdaten.add(((state.jahrgangsdaten.abiturjahr * 10) + state.halbjahr.id));
		}
		await reportingState.createPDFReport(reportingParameter);
	}

</script>
