<template>
	<Teleport defer to=".svws-ui-header--actions">
		<svws-ui-modal-hilfe> <hilfe-lehrer-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4" v-if="ServerMode.DEV.checkServerMode(serverMode)">
			<ui-card v-if="hatKompetenzDrucken && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken" subtitle="Drucke die Stundenpläne der ausgewählten Lehrer."
				:is-open="currentAction === 'print'" @update:is-open="isOpen => setCurrentAction('print', isOpen)">
				<div>
					<svws-ui-input-wrapper :grid="2" class="p-2">
						<div>
							<svws-ui-select title="Stundenplan" v-model="stundenplanAuswahl" :items="mapStundenplaene.values()"
								:item-text="s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')'" />
						</div>
						<div></div>
						<div>
							<svws-ui-radio-group>
								<svws-ui-radio-option :value="0" v-model="gruppe1" name="Unterrichte" label="Unterrichte" />
								<svws-ui-radio-option :value="1" v-model="gruppe1" name="Unterrichte" label="Unterrichte mit Pausenaufsichten" />
								<svws-ui-radio-option :value="2" v-model="gruppe1" name="Unterrichte" label="Unterrichte mit Pausenzeiten" />
							</svws-ui-radio-group>
						</div>
						<div class="text-left">
							<svws-ui-checkbox v-model="option4">Fach- statt Kursbezeichnung verwenden (nicht Sek-II)</svws-ui-checkbox><br>
							<svws-ui-checkbox v-model="option8">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox>
						</div>
						<div>
							<svws-ui-radio-group>
								<svws-ui-radio-option :value="0" v-model="gruppe2" name="Ausgabe" label="Gesamtausdruck" />
								<svws-ui-radio-option :value="1" v-model="gruppe2" name="Ausgabe" label="Einzelausdruck" />
								<svws-ui-radio-option :value="2" v-model="gruppe2" name="Ausgabe" label="Kombinierter Ausdruck" />
							</svws-ui-radio-group>
						</div>
					</svws-ui-input-wrapper>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button class="mt-4" :disabled="(stundenplanAuswahl === undefined) || !hatKompetenzDrucken" @click="downloadPDF" :is-loading="loading">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Drucken
					</svws-ui-button>
				</template>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Lehrer werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="isOpen => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="preConditionCheck[0]">Alle ausgewählten Lehrer sind bereit zum Löschen.</span>
					<template v-else v-for="message in preConditionCheck[1]" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
					<span v-if="loeschbareLehrerVorhanden">Einige Lehrer sind noch an anderer Stelle referenziert, die Übrigen können gelöscht werden.</span>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button class="mt-4" title="Löschen" @click="entferneLehrer" :is-loading="loading"
						:disabled="(lehrerListeManager().getIdsReferenzierterLehrer().size() === lehrerListeManager().liste.auswahlSize()) || loading || !hatKompetenzLoeschen">
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
		<div v-else>
			<svws-ui-todo title="Lehrer - Gruppenprozesse">
				Dieser Bereich ist noch in Entwicklung. Hier werden später Gruppenprozesse zu den Lehrern vorhanden sein.
			</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { List, StundenplanListeEintrag } from "@core";
	import { BenutzerKompetenz, DateUtils, ReportingParameter, ReportingReportvorlage, ServerMode } from "@core";
	import type { LehrerGruppenprozesseProps } from "~/components/lehrer/gruppenprozesse/SLehrerGruppenprozesseProps";

	type Action = 'print' | 'delete' | '';

	const props = defineProps<LehrerGruppenprozesseProps>();

	const currentAction = ref<Action>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const gruppe1 = ref<0|1|2>(0);
	const gruppe2 = ref<0|1|2>(0);

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_LOESCHEN));
	const hatKompetenzDrucken = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_ANSEHEN));

	const alleLehrerLoeschbar = computed(() => (currentAction.value === 'delete') && props.lehrerListeManager().getIdsReferenzierterLehrer().isEmpty());
	const loeschbareLehrerVorhanden = computed(() =>
		!alleLehrerLoeschbar.value && (props.lehrerListeManager().getIdsReferenzierterLehrer().size() !== props.lehrerListeManager().liste.auswahlSize()));

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete')
			return props.deleteLehrerCheck();
		return [true, []];
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

	async function entferneLehrer() {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteLehrer();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const option4 = ref(false);
	const option8 = ref(false);

	async function downloadPDF() {
		if (stundenplanAuswahl.value === undefined)
			return;
		loading.value = true;
		const reportingParameter = new ReportingParameter();
		if (gruppe2.value === 2)
			reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_LEHRER_STUNDENPLAN_KOMBINIERT.getBezeichnung();
		else
			reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_LEHRER_STUNDENPLAN.getBezeichnung();
		if (gruppe2.value === 1)
			reportingParameter.einzelausgabeDetaildaten = true;
		reportingParameter.detailLevel = gruppe1.value + (option4.value ? 4 : 0) + (option8.value ? 8 : 0);
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
