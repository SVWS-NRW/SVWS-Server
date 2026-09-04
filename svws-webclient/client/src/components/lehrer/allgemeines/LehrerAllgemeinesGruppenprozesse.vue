<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDruckenStundenplan && (stundenplanModel !== undefined)" icon="i-ri-printer-line" title="Stundenplan drucken oder versenden" subtitle="Drucke oder versende die Stundenpläne der ausgewählten Lehrkräfte."
				:is-open="currentAction === 'druckLehrerStundenplan'" @update:is-open="isOpen => setCurrentAction('druckLehrerStundenplan', isOpen)">
				<div>
					<div class="flex flex-col">
						<ui-select v-model="stundenplanModel" :manager="stundenplanSelectManager" label="Stundenplan" />
					</div>
					<report-parameters :reportvorlage="ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN"
						:id-hauptdaten-objekt="stundenplanModel?.id ?? -1" :ids-hauptdaten="[...lehrerListeManager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
				</div>
			</ui-card>
			<ui-card v-if="hatKompetenzDruckenStundenplan && (stundenplanModel !== undefined)" icon="i-ri-printer-line" title="Kombinierten Stundenplan drucken" subtitle="Drucke die Stundenpläne der ausgewählten Lehrkräfte in einer kombinierten Ansicht."
				:is-open="currentAction === 'druckLehrerStundenplanKombiniert'" @update:is-open="isOpen => setCurrentAction('druckLehrerStundenplanKombiniert', isOpen)">
				<div>
					<div class="flex flex-col">
						<ui-select v-model="stundenplanModel" :manager="stundenplanSelectManager" label="Stundenplan" />
					</div>
					<report-parameters :reportvorlage="ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT"
						:id-hauptdaten-objekt="stundenplanModel?.id ?? -1" :ids-hauptdaten="[...lehrerListeManager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
				</div>
			</ui-card>
			<ui-card v-if="hatKompetenzDruckenSchuelerLeistungsdaten" icon="i-ri-printer-line" title="Leistungsübersicht drucken" subtitle="Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der ausgewählten Lehrkräfte drucken"
				:is-open="currentAction === 'druckLehrerListeSchuelerLeistungsdaten'" @update:is-open="isOpen => setCurrentAction('druckLehrerListeSchuelerLeistungsdaten', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN"
					:ids-hauptdaten="[...lehrerListeManager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen"
				subtitle="Setze einen Löschvermerk bei den ausgewählten Lehrkräften." :is-open="currentAction === 'delete'"
				@update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div v-if="isDeleteConditionSectionVisible">
					<span v-if="selectedAllowedToDelete">Bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheckErrors" :key="message">
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
	import type { LehrerAllgemeinesGruppenprozesseProps } from "./LehrerAllgemeinesGruppenprozesseProps";
	import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";

	type Action = 'druckLehrerStundenplan' | 'druckLehrerStundenplanKombiniert' | 'druckLehrerListeSchuelerLeistungsdaten' | 'delete' | '';

	const props = defineProps<LehrerAllgemeinesGruppenprozesseProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzDrucken = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN) || benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)));
	const hatKompetenzDruckenStundenplan = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerLeistungsdaten = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LOESCHEN));

	const isDeleteDisabled = computed<boolean>(() => !hatKompetenzLoeschen.value || !props.lehrerListeManager().liste.auswahlExists() || !selectedAllowedToDelete.value || loading.value);
	const deleteCheckErrors = computed<Iterable<string>>(() => props.deleteCheck().logs);
	const selectedAllowedToDelete = computed<boolean>(() => props.deleteCheck().success);
	const isDeleteConditionSectionVisible = computed<boolean>(() => (props.lehrerListeManager().liste.auswahlExists() || (statusAction.value === undefined)));

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();

	const stundenplanOptions = computed(() => props.mapStundenplaene.values());
	const stundenplanModel = computed({
		get: () => {
			if (stundenplanAuswahl.value === undefined) {
				if (props.mapStundenplaene.size > 0) {
					const [first] = props.mapStundenplaene.values();
					return first;
				}
				return undefined;
			}
			return stundenplanAuswahl.value;
		},
		set: value => stundenplanAuswahl.value = value,
	});

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

	async function entferneLehrer() {
		loading.value = true;
		[statusAction.value, logs.value] = await props.deleteLehrer();
		loading.value = false;
	}

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

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.'];

</script>
