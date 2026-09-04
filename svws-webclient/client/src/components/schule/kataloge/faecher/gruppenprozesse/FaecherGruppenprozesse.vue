<template>
	<div class="page page-grid-cards">
		<div v-if="hatkeineErforderlicheKompetenz">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDrucken && (stundenplaeneById.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken" subtitle="Drucke die Stundenpläne der ausgewählten Fächer."
				:is-open="currentAction === 'print'" @update:is-open="isOpen => setCurrentAction('print', isOpen)">
				<div class="flex flex-col w-full">
					<ui-select label="Stundenplan"
						v-model="stundenplanModel"
						:manager="stundenplanSelectManager"
						:removable="false" :disabled="!hasSelection" />
					<report-parameters v-if="hasSelection"
						:reportvorlage="ReportingReportvorlage.STUNDENPLANUNG_V_FACH_STUNDENPLAN"
						:id-hauptdaten-objekt="stundenplanModel?.id ?? -1"
						:ids-hauptdaten="[...manager().liste.auswahl()].map(i=>i.id)"
						:ids-detaildaten="[]" />
				</div>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" title="Löschen" subtitle="Ausgewählte Fächer werden gelöscht" icon="i-ri-delete-bin-line">
				<div v-if="isPreConditionSectionVisible">
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
					<svws-ui-button class="mt-4"
						@click="sortModalIsOpen = true"
						:disabled="!hasSelection">
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
	import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";

	const props = defineProps<FaecherGruppenprozesseProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatKompetenzUpdate = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const hatKompetenzDrucken = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));
	const hatGymnasialeOberstufe = computed<boolean>(() => props.manager().schulform().daten(schuleState.abschnitt.schuljahr)?.hatGymOb ?? false);
	const hasSelection = computed<boolean>(() => props.manager().liste.auswahlExists());
	const stundenplaeneById = computed<Map<number, StundenplanListeEintrag>>(() => props.manager().stundenplaeneById);
	const hatkeineErforderlicheKompetenz = computed<boolean>(() => !hatKompetenzLoeschen.value || !hatKompetenzDrucken.value || !hatKompetenzUpdate.value);
	const deleteCheckErrors = computed<List<string>>(() => props.deleteCheck()[1]);
	const selectedAllowedToDelete = computed<boolean>(() => props.deleteCheck()[0]);
	const isPreConditionSectionVisible = computed<boolean>(() => (hasSelection.value || (status.value === undefined)));

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
	const stundenplanModel = computed({
		get: () => {
			if (stundenplanAuswahl.value === undefined) {
				if (stundenplaeneById.value.size > 0) {
					const [first] = stundenplaeneById.value.values();
					return first;
				}
			}
			return stundenplanAuswahl.value;
		},
		set: value => stundenplanAuswahl.value = value,
	});

	const stundenplanOptions = computed(() => stundenplaeneById.value.values());
	const stundenplanSelectManager = new SelectManager({
		options: stundenplanOptions,
		optionDisplayText: s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')',
		selectionDisplayText: s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')',
	});

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
