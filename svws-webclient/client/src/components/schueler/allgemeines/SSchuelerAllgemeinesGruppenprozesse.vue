<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDrucken" icon="i-ri-printer-line" title="Schulbescheinigung drucken" subtitle="Drucke eine Schulbescheinigung für die ausgewählten Schülerinnen und Schüler."
					 :is-open="currentAction === 'printSchulbescheinigung'" @update:is-open="isOpen => setCurrentAction('printSchulbescheinigung', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div class="text-left">
						<svws-ui-checkbox v-model="option2" name="anErzieher">Erzieher als Adressat</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4" name="mitBildBriefkopf">Bild des Briefkopfes verwenden</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option8" name="mitSchullogo">Schullogo verwenden</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option16" name="keineAnschrift">Keine Anschrift</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option32" name="keinInfoblock">Kein Infoblock</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option64" name="keineUnterschrift">Keine Unterschrift</svws-ui-checkbox><br>
					</div>
					<div class="text-left col-span-2">
						<br><p class="font-bold underline mb-2">Optionen zur Druckausgabe:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="gruppeDruck" name="gesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="gruppeDruck" name="einzelausdruckEinseitig" label="Einzelausdruck einseitig" />
						</svws-ui-radio-group>
					</div>
				</svws-ui-input-wrapper>
				<template #buttonFooterLeft>
					<svws-ui-button @click="downloadPDF" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Drucken
					</svws-ui-button>
				</template>
			</ui-card>
			<ui-card v-if="hatKompetenzDrucken && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken" subtitle="Drucke die Stundenpläne der ausgewählten Schüler."
				:is-open="currentAction === 'printStundenplan'" @update:is-open="isOpen => setCurrentAction('printStundenplan', isOpen)">
				<div class="w-full flex flex-col gap-6">
					<ui-select :disabled="!schuelerListeManager().liste.auswahlExists()" label="Stundenplan" v-model="stundenplanAuswahl"
						:manager="stundenplanSelectManager" />
					<div class="w-full flex gap-6">
						<div class="grow">
							<svws-ui-checkbox v-model="option2">Pausenzeiten anzeigen</svws-ui-checkbox><br>
							<svws-ui-checkbox v-model="option4">Fach- statt Kursbezeichnung verwenden (nicht Sek-II)</svws-ui-checkbox><br>
							<svws-ui-checkbox v-model="option8">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox><br>
							<svws-ui-checkbox v-model="option16">Individuelle Kursart anzeigen</svws-ui-checkbox>
						</div>
						<svws-ui-radio-group class="grow">
							<svws-ui-radio-option :value="1" v-model="gruppeDruck" name="Ausgabe" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="gruppeDruck" name="Ausgabe" label="Einzelausdruck einseitig" />
						</svws-ui-radio-group>
					</div>
					<div v-if="!schuelerListeManager().liste.auswahlExists()">
						<span class="text-ui-danger">Es ist kein Schüler ausgewählt.</span>
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
				subtitle="Setze einen Löschvermerk bei den ausgewählten Schülern." :is-open="currentAction === 'delete'"
				@update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="deleteSchuelerCheck()[0]">Bereit zum Löschen.</span>
					<template v-else v-for="message in deleteSchuelerCheck()[1]" :key="message">
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
		</div>
		<log-box :logs :status="statusAction">
			<template #button>
				<svws-ui-button v-if="statusAction !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
			</template>
		</log-box>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed, watch } from "vue";
	import type { SSchuelerAllgemeinesGruppenprozesseProps } from "./SSchuelerAllgemeinesGruppenprozesseProps";
	import {type StundenplanListeEintrag, type List, BenutzerKompetenz, ListUtils, ArrayList} from "@core";
	import { DateUtils, ReportingParameter, ReportingReportvorlage } from "@core";
	import { BaseSelectManager } from "@ui";

	type Action = 'printSchulbescheinigung' | 'printStundenplan' | 'delete' | '';

	const props = defineProps<SSchuelerAllgemeinesGruppenprozesseProps>();

	const hatKompetenzDrucken = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN));

	const isPrintDisabled = computed<boolean>(() => !hatKompetenzDrucken.value || !props.schuelerListeManager().liste.auswahlExists() || stundenplanAuswahl.value === undefined || loading.value)
	const isDeleteDisabled = computed<boolean>(() => !hatKompetenzLoeschen.value || !props.schuelerListeManager().liste.auswahlExists() || !props.deleteSchuelerCheck()[0] || loading.value)

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const currentAction = ref<Action>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const statusAction = ref<boolean | undefined>();

	const stundenplanDisplayText = (eintrag: StundenplanListeEintrag) => {
		return eintrag.bezeichnung.replace('Stundenplan ', '') + ': '
			+ toDateStr(eintrag.gueltigAb) + '—'
			+ toDateStr(eintrag.gueltigBis)
			+ ' (KW ' + toKW(eintrag.gueltigAb) + '—'
			+ toKW(eintrag.gueltigBis) + ')'
	}


	const stundenplaene = computed<Array<StundenplanListeEintrag>>(() => [...props.mapStundenplaene.values()])
	watch(
		() => stundenplaene.value,
		(newValue) => {
			stundenplanSelectManager.options = newValue;
		}
	);
	const stundenplanSelectManager = new BaseSelectManager({
		options: stundenplaene.value, optionDisplayText: stundenplanDisplayText, selectionDisplayText: stundenplanDisplayText,
	})

	async function entferneSchueler() {
		loading.value = true;
		[statusAction.value, logs.value] = await props.deleteSchueler();
		loading.value = false;
	}

	const option2 = ref(false);
	const option4 = ref(false);
	const option8 = ref(false);
	const option16 = ref(false);
	const option32 = ref(false);
	const option64 = ref(false);
	const gruppeDruck = ref(1);

	async function downloadPDF() {
		const reportingParameter = new ReportingParameter();
		const listeIdsSchueler = new ArrayList<number>();
		for (const schueler of props.schuelerListeManager().liste.auswahl())
			listeIdsSchueler.add(schueler.id);
		switch (currentAction.value) {
			case 'printSchulbescheinigung':
				reportingParameter.reportvorlage = ReportingReportvorlage.SCHUELER_v_SCHULBESCHEINIGUNG.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsSchueler;
				reportingParameter.einzelausgabeHauptdaten = ((gruppeDruck.value === 2) ? true : false);
				reportingParameter.einzelausgabeDetaildaten = false;
				break;
			case 'printStundenplan':
				if (stundenplanAuswahl.value === undefined)
					return;
				reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN.getBezeichnung();
				reportingParameter.idsHauptdaten = ListUtils.create1(stundenplanAuswahl.value.id);
				reportingParameter.idsDetaildaten = listeIdsSchueler;
				reportingParameter.einzelausgabeHauptdaten = false;
				reportingParameter.einzelausgabeDetaildaten = ((gruppeDruck.value === 2) ? true : false);
				break;
			default:
				return;
		}
		reportingParameter.detailLevel = (option2.value ? 2 : 0) + (option4.value ? 4 : 0) + (option8.value ? 8 : 0)
				+ (option16.value ? 16 : 0)	+ (option32.value ? 32 : 0) + (option64.value ? 64 : 0);
		loading.value = true;
		const { data, name } = await props.getPDF(reportingParameter);
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

	function setCurrentAction(newAction: Action, open: boolean) {
		if ((newAction !== currentAction.value) && !open)
			return;
		option2.value = false;
		option4.value = false;
		option8.value = false;
		option16.value = false;
		option32.value = false;
		option64.value = false;
		gruppeDruck.value = 1;

		currentAction.value = open ? newAction : '';
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		statusAction.value = undefined;
	}

</script>
