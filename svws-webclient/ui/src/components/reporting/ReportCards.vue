<template>
	<ui-card v-for="vorlage in sichtbareVorlagen" :key="vorlage.name()" icon="i-ri-printer-line" :title="vorlage.getUiTitel()" :subtitle="vorlage.getUiBeschreibung()"
		:is-open="currentAction === actionKey(vorlage)" @update:is-open="isOpen => setCurrentAction(actionKey(vorlage), isOpen)">
		<div class="flex flex-col w-full">
			<ui-select v-if="istStundenplanVorlage(vorlage)" v-model="stundenplanModel" :manager="stundenplanSelectManager" label="Stundenplan" :removable="false" />
			<report-parameters :reportvorlage="vorlage" :id-hauptdaten-objekt="istStundenplanVorlage(vorlage) ? (stundenplanModel?.id ?? -1) : undefined"
				:ids-hauptdaten="idsHauptdaten(vorlage)" :ids-detaildaten="[]" />
		</div>
	</ui-card>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
	import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";
	import { ReportingReportvorlageDatenContext } from "@core/core/types/reporting/ReportingReportvorlageDatenContext";
	import type { ReportingReportvorlageUiNutzung } from "@core/core/types/reporting/ReportingReportvorlageUiNutzung";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useReportingState } from "@ui/states/ReportingState";
	import { useCurrentAction } from "@ui/ui/composables/useCurrentAction";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	/**
	 * Erzeugt je Report-Vorlage eine Card mit den Report-Parametern. Die Vorlagen kommen aus einem Bereich (uiNutzung), aus einer Liste oder aus beidem.
	 * Eine Card erscheint nur, wenn die Vorlage zulässig ist und der Bereich IDs für ihren Daten-Context liefert. So zeigt der Client keine Card,
	 * die der Server ablehnt oder die keine Daten hätte.
	 */
	const props = defineProps<{
		uiNutzung?: ReportingReportvorlageUiNutzung;
		vorlagen?: ReportingReportvorlage[];
		ids: Map<ReportingReportvorlageDatenContext, () => List<number>>;
		stundenplaene?: StundenplanListeEintrag[];
	}>();

	const reportingState = useReportingState();

	/** Die geöffnete Card. Der Bereich teilt den Wert mit seinen übrigen Cards, damit immer nur eine Card offen ist. */
	const currentAction = defineModel<string>("currentAction", { default: "" });

	/**
	 * Meldet, ob der Benutzer in diesem Bereich Vorlagen nutzen darf. Das gilt auch, wenn die Cards gerade fehlen, weil keine Auswahl oder kein
	 * Stundenplan vorliegt. Der Bereich erkennt daran, ob dem Benutzer Berichte zur Verfügung stehen.
	 */
	const emit = defineEmits<{
		"update:hatVorlagen": [hatVorlagen: boolean];
	}>();

	const { setCurrentAction } = useCurrentAction(currentAction);

	const stundenplanContexte = new Set<ReportingReportvorlageDatenContext>([
		ReportingReportvorlageDatenContext.STUNDENPLANUNG_FACH,
		ReportingReportvorlageDatenContext.STUNDENPLANUNG_KLASSEN,
		ReportingReportvorlageDatenContext.STUNDENPLANUNG_LEHRER,
		ReportingReportvorlageDatenContext.STUNDENPLANUNG_RAUM,
		ReportingReportvorlageDatenContext.STUNDENPLANUNG_SCHUELER,
	]);

	const wochentag = ["So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa.", "So."];

	/** Gespeichert wird nur die ID. Ein Objekt im ref wäre ein Proxy und würde in der Liste der Stundenpläne nicht wiedergefunden. */
	const idStundenplanAuswahl = ref<number>();

	const stundenplanListe = computed<StundenplanListeEintrag[]>(() => props.stundenplaene ?? []);

	const stundenplanModel = computed<StundenplanListeEintrag | undefined>({
		get: () => stundenplanListe.value.find(s => s.id === idStundenplanAuswahl.value) ?? stundenplanListe.value.at(0),
		set: value => idStundenplanAuswahl.value = value?.id,
	});

	const zulaessigeVorlagen = computed<ReportingReportvorlage[]>(() => {
		const vorlagen = new Set<ReportingReportvorlage>();
		if (props.uiNutzung !== undefined) {
			for (const vorlage of ReportingReportvorlage.getByUiNutzung(props.uiNutzung)) {
				vorlagen.add(vorlage);
			}
		}
		for (const vorlage of props.vorlagen ?? []) {
			vorlagen.add(vorlage);
		}
		return [...vorlagen].filter(vorlage => reportingState.istVorlageZulaessig(vorlage) && props.ids.has(vorlage.getReportingReportvorlageDatenContext()));
	});

	/** Ohne ausgewählte Daten oder ohne Stundenplan entstünde kein sinnvoller Bericht, daher fehlt die Card dann. */
	const sichtbareVorlagen = computed<ReportingReportvorlage[]>(() => zulaessigeVorlagen.value.filter(vorlage => hatIdsHauptdaten(vorlage)
		&& (!istStundenplanVorlage(vorlage) || (stundenplanListe.value.length > 0))));

	const stundenplanSelectManager = new SelectManager({
		options: stundenplanListe,
		optionDisplayText: stundenplanText,
		selectionDisplayText: stundenplanText,
	});

	watch(() => zulaessigeVorlagen.value.length > 0, hatVorlagen => emit("update:hatVorlagen", hatVorlagen), { immediate: true });

	function istStundenplanVorlage(vorlage: ReportingReportvorlage): boolean {
		return stundenplanContexte.has(vorlage.getReportingReportvorlageDatenContext());
	}

	function idsHauptdaten(vorlage: ReportingReportvorlage): List<number> {
		const ids = props.ids.get(vorlage.getReportingReportvorlageDatenContext());
		return (ids === undefined) ? new ArrayList<number>() : ids();
	}

	function hatIdsHauptdaten(vorlage: ReportingReportvorlage): boolean {
		return !idsHauptdaten(vorlage).isEmpty();
	}

	function actionKey(vorlage: ReportingReportvorlage): string {
		return "report:" + vorlage.name();
	}

	function stundenplanText(s: StundenplanListeEintrag): string {
		return s.bezeichnung.replace("Stundenplan ", "") + ": " + toDateStr(s.gueltigAb) + "—" + toDateStr(s.gueltigBis)
			+ " (KW " + toKW(s.gueltigAb) + "—" + toKW(s.gueltigBis) + ")";
	}

	function toDateStr(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toKW(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}

</script>
