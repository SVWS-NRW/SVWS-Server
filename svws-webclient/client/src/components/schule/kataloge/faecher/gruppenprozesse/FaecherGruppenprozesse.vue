<template>
	<div class="page page-grid-cards">
		<div v-if="hatkeineErforderlicheKompetenz">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div class="flex flex-col gap-4" v-if="ServerMode.DEV.checkServerMode(serverMode)">
			<ui-card v-if="hatKompetenzDrucken && (stundenplaeneById.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken" subtitle="Drucke die Stundenpläne der ausgewählten Klassen."
				:is-open="currentAction === 'print'" @update:is-open="isOpen => setCurrentAction('print', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div>
						<svws-ui-select title="Stundenplan" v-model="stundenplanAuswahl" :items="stundenplaeneById.values()"
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
					<svws-ui-button class="mt-4"
						:disabled="(stundenplanAuswahl === undefined) || !hatKompetenzDrucken || manager().liste.auswahl().isEmpty()"
						@click="downloadPDF" :is-loading="loading">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Drucken
					</svws-ui-button>
				</template>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" title="Löschen" subtitle="Ausgewählte Fächer werden gelöscht" icon="i-ri-delete-bin-line"
				:is-open="currentAction === 'delete'"
				@update:is-open="isOpen => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="selectedAllowedToDelete">Alle ausgewählten Fächer sind bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheckErrors" :key="message">
						<span class="text-ui-danger whitespace-pre-line"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen" class="mt-4"
						@click="deleteFaecher"
						:disabled="!selectedAllowedToDelete || !props.manager().liste.auswahlExists()" :is-loading="loading">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
			<ui-card v-if="hatKompetenzUpdate && hatGymnasialeOberstufe"
				icon="icon i-ri-arrow-up-down-line" title="Standardsortierung Sekundarstufe II anwenden"
				subtitle="Die Sortierung wird auf alle Fächer angewendet, nicht nur auf die markierten."
				:is-open="currentAction === 'sort'" @update:is-open="(isOpen) => setCurrentAction('sort', isOpen)">
				<template #buttonFooterLeft>
					<svws-ui-button class="mt-4" @click="sortModalIsOpen = true">
						<span class="icon i-ri-play-line" />
						Standardsortierung Sek II anwenden
					</svws-ui-button>
					<svws-ui-modal v-model:show="sortModalIsOpen"
						:auto-close="false" :close-in-title="false"
						size="medium" type="danger">
						<template #modalTitle>Standardsortierung für die Sekundarstufe II anwenden</template>
						<template #modalContent>
							Sollen alle Fächer nach der Standardsortierung für die Sekundarstufe II sortiert werden? <br>
							Dabei geht die aktuell hinterlegte Sortierreihenfolge verloren.
						</template>
						<template #modalActions>
							<svws-ui-button type="secondary" @click="sortModalIsOpen = false"> Nein </svws-ui-button>
							<svws-ui-button type="danger" @click="sort"> Ja </svws-ui-button>
						</template>
					</svws-ui-modal>
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

	import { computed, ref } from "vue";
	import type { FaecherGruppenprozesseProps } from "./FaecherGruppenprozesseProps";
	import type { List, StundenplanListeEintrag } from "@core";
	import { ServerMode, BenutzerKompetenz, ReportingParameter, DateUtils, ReportingReportvorlage, ArrayList } from "@core";

	const props = defineProps<FaecherGruppenprozesseProps>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatKompetenzDrucken = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));
	const hatKompetenzUpdate = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const hatGymnasialeOberstufe = computed(() => props.manager().schulform().daten(props.schuljahr)?.hatGymOb ?? false);
	const stundenplaeneById = computed(() => props.manager().stundenplaeneById);
	const hatkeineErforderlicheKompetenz = computed<boolean>(() => !hatKompetenzLoeschen.value || !hatKompetenzDrucken.value || !hatKompetenzUpdate.value);
	const deleteCheckErrors = computed<List<string>>(() => props.deleteCheck()[1]);
	const selectedAllowedToDelete = computed<boolean>(() => props.deleteCheck()[0]);

	// --- delete ---

	async function deleteFaecher() {
		loading.value = true;
		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';
		loading.value = false;
	}

	// --- sort ---

	const sortModalIsOpen = ref<boolean>(false);

	async function sort() {
		await props.sortFaecher();
		const log = new ArrayList<string>();
		log.add("Standardsortierung für die Sekundarstufe II wurde erfolgreich angewendet.");
		logs.value = log;
		status.value = true;
		sortModalIsOpen.value = false;
	}

	// --- Stundenplan ---

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const option2 = ref(false);
	const gruppe2 = ref(false);

	async function downloadPDF() {
		if (stundenplanAuswahl.value === undefined) {
			return;
		}
		loading.value = true;
		const reportingParameter = new ReportingParameter();
		reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_V_FACH_STUNDENPLAN.getBezeichnung();
		reportingParameter.einzelausgabeDetaildaten = gruppe2.value;
		reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_V_FACH_STUNDENPLAN.getVorlageParameterList());
		for (const vp of reportingParameter.vorlageParameter) {
			if (vp.name === "mitPausenzeiten") {
				vp.wert = option2.value.toString();
			}
		}
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

	/// --- util ---

	const currentAction = ref<string>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	function setCurrentAction(newAction: string, open: boolean) {
		if (newAction === oldAction.value.name && !open) {
			return;
		}
		oldAction.value.name = currentAction.value;
		oldAction.value.open = (currentAction.value === "");
		if (open) {
			currentAction.value = newAction;
		} else {
			currentAction.value = "";
		}
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

</script>
