<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDrucken" icon="i-ri-printer-line" title="Schülerliste drucken" subtitle="Drucke eine Liste mit den Daten der ausgewählten Schülerinnen und Schüler."
					 :is-open="currentAction === 'printListeSchuelerKontakteErzieher'" @update:is-open="isOpen => setCurrentAction('printListeSchuelerKontakteErzieher', isOpen)">
				<svws-ui-input-wrapper :grid="4" class="p-2">
					<div class="text-left">
						<svws-ui-checkbox v-model="option1" name="nurSchuelerKlasse">Klasse</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option2" name="nurSchuelerRufname">Nur Rufname</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4" name="mitSchuelerGeschlecht">Geschlecht</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option8" name="mitSchuelerGebDat">Geburtsdatum</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option16" name="mitSchuelerStaat">Staatsangehörigkeit</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option32" name="mitSchuelerAnschrift">Anschrift</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option64" name="mitSchuelerTelefonPrivat">Telefon</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option128" name="mitSchuelerEmailSchule">Schulische E-Mail</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option256" name="mitSchuelerEmailPrivat">Private E-Mail</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option512" name="mitSchuelerTelefonkontakte">Telefonische Kontakte</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option1024" name="mitErzieher">Erzieher</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option2048" name="mitErzieherAnschrift">Erzieher Anschrift</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4096" name="mitErzieherEmailPrivat">Erzieher E-Mail</svws-ui-checkbox><br>
					</div>
					<div class="text-left col-span-4">
						<br><p class="font-bold underline mb-2">Optionen zur Druckausgabe:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value=1 v-model="gruppeDruck" name="gesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value=3 v-model="gruppeDruck" name="gesamtausdruckDuplex" label="Gesamtausdruck duplex" />
						</svws-ui-radio-group>
					</div>
				</svws-ui-input-wrapper>
				<template #buttonFooterLeft>
					<svws-ui-button @click="selectAll" class="mt-4">
						<span class="icon i-ri-checkbox-line" />
						Alle auswählen
					</svws-ui-button>
					<svws-ui-button @click="deselectAll" class="mt-4">
						<span class="icon i-ri-checkbox-blank-line" />
						Alle abwählen
					</svws-ui-button>
					<svws-ui-button @click="downloadPDF" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Drucken
					</svws-ui-button>
				</template>
			</ui-card>
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
							<svws-ui-radio-option :value=1 v-model="gruppeDruck" name="gesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value=2 v-model="gruppeDruck" name="einzelausdruckEinseitig" label="Einzelausdruck einseitig" />
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
							<svws-ui-radio-option :value=1 v-model="gruppeDruck" name="gesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value=2 v-model="gruppeDruck" name="einzelausdruckEinseitig" label="Einzelausdruck einseitig" />
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

	type Action = 'printListeSchuelerKontakteErzieher' | 'printSchulbescheinigung' | 'printStundenplan' | 'delete' | '';

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

	const option1 = ref(false);
	const option2 = ref(false);
	const option4 = ref(false);
	const option8 = ref(false);
	const option16 = ref(false);
	const option32 = ref(false);
	const option64 = ref(false);
	const option128 = ref(false);
	const option256 = ref(false);
	const option512 = ref(false);
	const option1024 = ref(false);
	const option2048 = ref(false);
	const option4096 = ref(false);
	const gruppeDruck = ref(1);

	async function downloadPDF() {
		const reportingParameter = new ReportingParameter();
		const listeIdsSchueler = new ArrayList<number>();
		for (const schueler of props.schuelerListeManager().liste.auswahl())
			listeIdsSchueler.add(schueler.id);
		switch (currentAction.value) {
			case 'printListeSchuelerKontakteErzieher':
				reportingParameter.reportvorlage = ReportingReportvorlage.SCHUELER_v_LISTE_KONTAKTDATENERZIEHER.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsSchueler;
				reportingParameter.einzelausgabeHauptdaten = false;
				reportingParameter.einzelausgabeDetaildaten = false;
				break;
			case 'printSchulbescheinigung':
				reportingParameter.reportvorlage = ReportingReportvorlage.SCHUELER_v_SCHULBESCHEINIGUNG.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsSchueler;
				reportingParameter.einzelausgabeHauptdaten = (gruppeDruck.value === 2);
				reportingParameter.einzelausgabeDetaildaten = false;
				break;
			case 'printStundenplan':
				if (stundenplanAuswahl.value === undefined)
					return;
				reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN.getBezeichnung();
				reportingParameter.idsHauptdaten = ListUtils.create1(stundenplanAuswahl.value.id);
				reportingParameter.idsDetaildaten = listeIdsSchueler;
				reportingParameter.einzelausgabeHauptdaten = false;
				reportingParameter.einzelausgabeDetaildaten = (gruppeDruck.value === 2);
				break;
			default:
				return;
		}
		reportingParameter.duplexdruck = ((gruppeDruck.value === 3) || (gruppeDruck.value === 4));
		reportingParameter.detailLevel = ((option1.value ? 1 : 0) + (option2.value ? 2 : 0) + (option4.value ? 4 : 0)
				+ (option8.value ? 8 : 0) + (option16.value ? 16 : 0) + (option32.value ? 32 : 0) + (option64.value ? 64 : 0)
				+ (option128.value ? 128 : 0) + (option256.value ? 256 : 0) + (option512.value ? 512 : 0)
				+ (option1024.value ? 1024 : 0) + (option2048.value ? 2048 : 0) + (option4096.value ? 4096 : 0));
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

	async function selectAll() {
		option1.value = true;
		option2.value = true;
		option4.value = true;
		option8.value = true;
		option16.value = true;
		option32.value = true;
		option64.value = true;
		option128.value = true;
		option256.value = true;
		option512.value = true;
		option1024.value = true;
		option2048.value = true;
		option4096.value = true;
	}

	async function deselectAll() {
		option1.value = false;
		option2.value = false;
		option4.value = false;
		option8.value = false;
		option16.value = false;
		option32.value = false;
		option64.value = false;
		option128.value = false;
		option256.value = false;
		option512.value = false;
		option1024.value = false;
		option2048.value = false;
		option4096.value = false;
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
		option1.value = false;
		option2.value = false;
		option4.value = false;
		option8.value = false;
		option16.value = false;
		option32.value = false;
		option64.value = false;
		option128.value = false;
		option256.value = false;
		option512.value = false;
		option1024.value = false;
		option2048.value = false;
		option4096.value = false;
		gruppeDruck.value = 1;

		currentAction.value = open ? newAction : "";
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		statusAction.value = undefined;
	}

</script>
