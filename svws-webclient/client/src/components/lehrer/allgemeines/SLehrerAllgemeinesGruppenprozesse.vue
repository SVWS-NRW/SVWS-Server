<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDrucken && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken"
				subtitle="Drucke die Stundenpläne der ausgewählten Lehrer." :is-open="currentAction === 'print'"
				@update:is-open="isOpen => setCurrentAction('print', isOpen)">
				<div class="w-full flex flex-col gap-6">
					<ui-select :disabled="!lehrerListeManager().liste.auswahlExists()" label="Stundenplan" v-model="stundenplanAuswahl"
						:manager="stundenplanSelectManager" />
					<div class="w-full flex gap-6">
						<div class="grow">
							<svws-ui-checkbox v-model="option2">Pausenzeiten anzeigen</svws-ui-checkbox>
							<br>
							<svws-ui-checkbox v-model="option4">Fach- statt Kursbezeichnung verwenden (nicht Sek-II)</svws-ui-checkbox>
							<br>
							<svws-ui-checkbox v-model="option8">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox>
							<br>
							<svws-ui-checkbox v-model="option16">Individuelle Kursart anzeigen</svws-ui-checkbox>
						</div>
						<svws-ui-radio-group class="grow">
							<svws-ui-radio-option :value="false" v-model="gruppe2" name="Ausgabe" label="Gesamtausdruck" />
							<svws-ui-radio-option :value="true" v-model="gruppe2" name="Ausgabe" label="Einzelausdruck" />
						</svws-ui-radio-group>
					</div>
					<div v-if="!lehrerListeManager().liste.auswahlExists()">
						<span class="text-ui-danger">Es ist kein Lehrer ausgewählt.</span>
					</div>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="isPrintDisabled" @click="downloadPDF" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Drucken
					</svws-ui-button>
				</template>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen"
				subtitle="Ausgewählte Lehrer werden gelöscht." :is-open="currentAction === 'delete'"
				@update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="deleteCheck.success">Bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheck.logs" :key="message">
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
		</div>
		<log-box :logs :status="statusAction">
			<template #button>
				<svws-ui-button v-if="statusAction !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
			</template>
		</log-box>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SLehrerAllgemeinesGruppenprozesseProps } from "./SLehrerAllgemeinesGruppenprozesseProps";
	import type { StundenplanListeEintrag, List } from "@core";
	import { DateUtils, ReportingParameter, ReportingReportvorlage, BenutzerKompetenz } from "@core";
	import { SelectManager } from "@ui";

	type Action = 'print' | 'delete' | '';

	const props = defineProps<SLehrerAllgemeinesGruppenprozesseProps>();

	const hatKompetenzDrucken = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_LOESCHEN));

	const isPrintDisabled = computed<boolean>(
		() => !hatKompetenzDrucken.value || !props.lehrerListeManager().liste.auswahlExists() || stundenplanAuswahl.value === undefined || loading.value)
	const isDeleteDisabled = computed<boolean>(
		() => !hatKompetenzLoeschen.value || !props.lehrerListeManager().liste.auswahlExists() || !deleteCheck.value.success || loading.value)

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const currentAction = ref<Action>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const statusAction = ref<boolean | undefined>();

	const deleteCheck = computed(() => props.deleteLehrerCheck());

	const stundenplanDisplayText = (eintrag: StundenplanListeEintrag) => {
		return eintrag.bezeichnung.replace('Stundenplan ', '') + ': '
			+ toDateStr(eintrag.gueltigAb) + '—'
			+ toDateStr(eintrag.gueltigBis)
			+ ' (KW ' + toKW(eintrag.gueltigAb) + '—'
			+ toKW(eintrag.gueltigBis) + ')'
	}


	const stundenplaene = computed<Array<StundenplanListeEintrag>>(() => [...props.mapStundenplaene.values()])

	const stundenplanSelectManager = new SelectManager({ options: stundenplaene, optionDisplayText: stundenplanDisplayText,
		selectionDisplayText: stundenplanDisplayText,
	})

	async function entferneLehrer() {
		loading.value = true;
		[statusAction.value, logs.value] = await props.deleteLehrer();
		loading.value = false;
	}

	const option2 = ref(false);
	const option4 = ref(false);
	const option8 = ref(false);
	const option16 = ref(false);
	const gruppe2 = ref(false);

	async function downloadPDF() {
		if (stundenplanAuswahl.value === undefined)
			return;
		loading.value = true;
		const reportingParameter = new ReportingParameter();
		reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_LEHRER_STUNDENPLAN.getBezeichnung();
		reportingParameter.einzelausgabeDetaildaten = gruppe2.value;
		reportingParameter.detailLevel = (option2.value ? 2 : 0) + (option4.value ? 4 : 0) + (option8.value ? 8 : 0) + (option16.value ? 16 : 0);
		const { data, name } = await props.getPDF(reportingParameter, stundenplanAuswahl.value.id);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
		loading.value = false;
	}

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.'];

	function toDateStr(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toKW(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}

	function setCurrentAction(newAction: Action, open: boolean) {
		if ((newAction !== currentAction.value) && !open)
			return;

		currentAction.value = open ? newAction : '';
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		statusAction.value = undefined;
	}

</script>
