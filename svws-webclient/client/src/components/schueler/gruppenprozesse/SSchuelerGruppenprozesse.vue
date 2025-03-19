<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDrucken && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken" subtitle="Drucke die Stundenpläne der ausgewählten Schüler."
					 :is-open="currentAction === 'print'" @update:is-open="isOpen => setCurrentAction('print', isOpen)">
				<div>
					<svws-ui-input-wrapper :grid="2" class="p-2">
						<div>
							<svws-ui-select title="Stundenplan" v-model="stundenplanAuswahl" :items="mapStundenplaene.values()"
											:item-text="s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')'" />
						</div>
						<div></div>
						<div class="text-left">
							<svws-ui-checkbox v-model="option2">Pausenzeiten anzeigen</svws-ui-checkbox><br>
							<svws-ui-checkbox v-model="option4">Fach- statt Kursbezeichnung verwenden (nicht Sek-II)</svws-ui-checkbox><br>
							<svws-ui-checkbox v-model="option8">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox><br>
							<svws-ui-checkbox v-model="option16">Individuelle Kursart anzeigen</svws-ui-checkbox>
						</div>
						<div>
							<svws-ui-radio-group>
								<svws-ui-radio-option :value="false" v-model="gruppe2" name="Ausgabe" label="Gesamtausdruck" />
								<svws-ui-radio-option :value="true" v-model="gruppe2" name="Ausgabe" label="Einzelausdruck" />
							</svws-ui-radio-group>
						</div>
					</svws-ui-input-wrapper>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="stundenplanAuswahl === undefined" @click="downloadPDF" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Drucken
					</svws-ui-button>
				</template>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen"
				subtitle="Bei den ausgewählten Schülern wird ein Löschvermerk gesetzt." :is-open="currentAction === 'delete'"
				@update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="preConditionCheck[0]">Alle ausgewählten Schüler sind bereit zum Löschen.</span>
					<template v-else v-for="message in preConditionCheck[1]" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="!preConditionCheck[0] || loading" title="Löschen" @click="entferneSchueler" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>

			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SchuelerGruppenprozesseProps } from "./SSchuelerGruppenprozesseProps";
	import type { StundenplanListeEintrag, List } from "@core";
	import { ArrayList, BenutzerKompetenz, DateUtils, ReportingParameter, ReportingReportvorlage } from "@core";

	type Action = 'print' | 'delete' | '';

	const props = defineProps<SchuelerGruppenprozesseProps>();

	const currentAction = ref<Action>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN));
	const hatKompetenzDrucken = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete')
			return props.deleteSchuelerCheck();
		return [true, new ArrayList<string>()];
	})

	function setCurrentAction(newAction: Action, open: boolean) {
		if(newAction === oldAction.value.name && !open)
			return;
		oldAction.value.name = currentAction.value;
		oldAction.value.open = (currentAction.value === "") ? false : true;
		if(open === true)
			currentAction.value= newAction;
		else
			currentAction.value = "";
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneSchueler() {
		loading.value = true;

		[status.value, logs.value] = await props.deleteSchueler();
		currentAction.value = '';

		loading.value = false;
	}

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
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
		reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN.getBezeichnung();
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

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.' ];

	function toDateStr(iso: string) : string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toKW(iso: string) : string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}
</script>
