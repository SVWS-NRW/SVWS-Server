<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4" v-if="ServerMode.DEV.checkServerMode(serverMode)">
			<ui-card v-if="hatKompetenzDrucken && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken" subtitle="Drucke die Stundenpläne der ausgewählten Klassen."
				:is-open="currentAction === 'print'" @update:is-open="isOpen => setCurrentAction('print', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div>
						<svws-ui-select title="Stundenplan" v-model="stundenplanAuswahl" :items="mapStundenplaene.values()"
							:item-text="s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')'" />
					</div>
					<div />
					<div class="text-left">
						<svws-ui-checkbox v-model="option2" name="Pausenzeiten">Pausenzeiten anzeigen</svws-ui-checkbox><br>
					</div>
					<div>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="false" v-model="gruppe2" name="Ausgabe" label="Gesamtausdruck" />
							<svws-ui-radio-option :value="true" v-model="gruppe2" name="Ausgabe" label="Einzelausdruck" />
						</svws-ui-radio-group>
					</div>
				</svws-ui-input-wrapper>
				<template #buttonFooterLeft>
					<svws-ui-button class="mt-4" :disabled="(stundenplanAuswahl === undefined) || !hatKompetenzDrucken" @click="downloadPDF" :is-loading="loading">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Drucken
					</svws-ui-button>
				</template>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Fächer werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="preConditionCheck[0]">Alle ausgewählten Fächer sind bereit zum Löschen.</span>
					<template v-else v-for="message in preConditionCheck[1]" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
					<span v-if="loeschbareFaecherVorhanden">Einige Fächer sind noch an anderer Stelle referenziert, die Übrigen können gelöscht werden.</span>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button class="mt-4" title="Löschen" @click="entferneFaecher" :is-loading="loading"
						:disabled="manager().getIdsReferenzierterFaecher().size() === manager().liste.auswahlSize() || loading || !hatKompetenzLoeschen">
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
			<svws-ui-todo title="Fächer - Gruppenprozesse">
				Dieser Bereich ist noch in Entwicklung. Hier werden später Gruppenprozesse zu den Fächern vorhanden sein.
			</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { SchuleFachGruppenprozesseProps } from "./SSchuleFachGruppenprozesseProps";
	import type { List, StundenplanListeEintrag } from "@core";
	import { ServerMode, BenutzerKompetenz, ReportingParameter, DateUtils, ReportingReportvorlage } from "@core";

	const props = defineProps<SchuleFachGruppenprozesseProps>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatKompetenzDrucken = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));

	const currentAction = ref<string>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const alleFaecherLoeschbar = computed(() => (currentAction.value === 'delete') && props.manager().getIdsReferenzierterFaecher().isEmpty());
	const loeschbareFaecherVorhanden = computed(() =>
		!alleFaecherLoeschbar.value && (props.manager().getIdsReferenzierterFaecher().size() !== props.manager().liste.auswahlSize()));

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete')
			return props.deleteFaecherCheck();
		return [true, []];
	})

	function setCurrentAction(newAction: string, open: boolean) {
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

	async function entferneFaecher() {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteFaecher();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const option2 = ref(false);
	const gruppe2 = ref(false);

	async function downloadPDF() {
		if (stundenplanAuswahl.value === undefined)
			return;
		loading.value = true;
		const reportingParameter = new ReportingParameter();
		reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_FACH_STUNDENPLAN.getBezeichnung();
		reportingParameter.einzelausgabeDetaildaten = gruppe2.value;
		reportingParameter.detailLevel = (option2.value ? 2 : 0);
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
