<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<svws-ui-input-wrapper v-else class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" title="Schülerliste drucken" subtitle="Drucke eine Liste mit den Daten der ausgewählten Schülerinnen und Schüler."
				:is-open="currentAction === 'druckSchuelerListeKontaktdatenErzieher'" @update:is-open="isOpen => setCurrentAction('druckSchuelerListeKontaktdatenErzieher', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.SCHUELER_V_LISTE_KONTAKTDATENERZIEHER"
					:ids-hauptdaten="[...schuelerListeManager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>
			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" title="Schulbescheinigung drucken" subtitle="Drucke eine Schulbescheinigung für die ausgewählten Schülerinnen und Schüler."
				:is-open="currentAction === 'druckSchuelerSchulbescheinigung'" @update:is-open="isOpen => setCurrentAction('druckSchuelerSchulbescheinigung', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG"
					:ids-hauptdaten="[...schuelerListeManager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>
			<ui-card v-if="hatKompetenzDruckenStundenplan && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken oder versenden" subtitle="Drucke oder versende die Stundenpläne der ausgewählten Schüler."
				:is-open="currentAction === 'druckSchuelerStundenplan'" @update:is-open="isOpen => setCurrentAction('druckSchuelerStundenplan', isOpen)">
				<div>
					<div class="flex flex-col">
						<ui-select v-model="stundenplanModel" :manager="stundenplanSelectManager" label="Stundenplan" />
					</div>
					<report-parameters :reportvorlage="ReportingReportvorlage.STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN"
						:id-hauptdaten-objekt="stundenplanModel?.id ?? -1" :ids-hauptdaten="[...schuelerListeManager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
				</div>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen"
				subtitle="Setze einen Löschvermerk bei den ausgewählten Schülern." :is-open="currentAction === 'delete'"
				@update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div v-if="isDeleteConditionSectionVisible">
					<span v-if="selectedAllowedToDelete">Bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheckErrors" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="isDeleteDisabled" title="Löschen" @click="entferneSchueler" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
		</svws-ui-input-wrapper>
		<log-box :logs :status="statusAction">
			<template #button>
				<svws-ui-button v-if="statusAction !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
			</template>
		</log-box>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SSchuelerAllgemeinesGruppenprozesseProps } from "./SSchuelerAllgemeinesGruppenprozesseProps";
	import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";

	type Action = 'druckSchuelerListeKontaktdatenErzieher' | 'druckSchuelerSchulbescheinigung' | 'druckSchuelerStundenplan' | 'delete' | '';

	const props = defineProps<SSchuelerAllgemeinesGruppenprozesseProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzDrucken = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN) || benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)));
	const hatKompetenzDruckenStundenplan = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerIndividualdaten = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LOESCHEN));
	const hatIrgendwelcheKompetenzen = computed(() => hatKompetenzDrucken.value || hatKompetenzLoeschen.value || hatKompetenzDruckenStundenplan.value || hatKompetenzDruckenSchuelerIndividualdaten.value);

	const isDeleteDisabled = computed<boolean>(() => !hatKompetenzLoeschen.value || !props.schuelerListeManager().liste.auswahlExists() || !selectedAllowedToDelete.value || loading.value);
	const deleteCheckErrors = computed<List<string>>(() => props.deleteSchuelerCheck()[1]);
	const selectedAllowedToDelete = computed(() => props.deleteSchuelerCheck()[0]);
	const isDeleteConditionSectionVisible = computed<boolean>(() => (props.schuelerListeManager().liste.auswahlExists() || (statusAction.value === undefined)));

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const stundenplanModel = computed({
		get: () => {
			if (stundenplanAuswahl.value === undefined) {
				if (props.mapStundenplaene.size > 0) {
					const [first] = props.mapStundenplaene.values();
					return first;
				}
			}
			return stundenplanAuswahl.value;
		},
		set: value => stundenplanAuswahl.value = value,
	});

	const stundenplanOptions = computed(() => props.mapStundenplaene.values());
	const stundenplanSelectManager = new SelectManager({
		options: stundenplanOptions.value,
		optionDisplayText: s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')',
		selectionDisplayText: s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')',
	});

	function toDateStr(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toKW(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}

	const currentAction = ref<Action>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const statusAction = ref<boolean | undefined>();

	async function entferneSchueler() {
		loading.value = true;
		[statusAction.value, logs.value] = await props.deleteSchueler();
		loading.value = false;
	}

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.'];

	function setCurrentAction(newAction: Action, open: boolean) {
		if ((newAction !== currentAction.value) && !open) {
			return;
		}
		currentAction.value = open ? newAction : "";
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		statusAction.value = undefined;
	}

</script>
