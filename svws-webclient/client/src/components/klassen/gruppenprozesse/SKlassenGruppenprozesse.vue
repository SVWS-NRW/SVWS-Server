<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDrucken" icon="i-ri-printer-line" title="Klassenliste drucken" subtitle="Drucke Listen mit den Daten der Schülerinnen und Schüler der ausgewählten Klassen."
				:is-open="currentAction === 'printListe'" @update:is-open="isOpen => setCurrentAction('printListe', isOpen)">
				<svws-ui-input-wrapper :grid="4" class="p-2">
					<div class="text-left">
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
							<svws-ui-radio-option :value="1" v-model="gruppeDruck" name="gesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="gruppeDruck" name="einzelausdruckEinseitig" label="Einzelausdruck einseitig" />
							<svws-ui-radio-option :value="3" v-model="gruppeDruck" name="gesamtausdruckDuplex" label="Gesamtausdruck duplex" />
							<svws-ui-radio-option :value="4" v-model="gruppeDruck" name="einzelausdruckDuplex" label="Einzelausdruck duplex" />
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
			<ui-card v-if="hatKompetenzDrucken && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken" subtitle="Drucke die Stundenpläne der ausgewählten Klassen."
					 :is-open="currentAction === 'printStundenplan'" @update:is-open="isOpen => setCurrentAction('printStundenplan', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div>
						<svws-ui-select title="Stundenplan" v-model="stundenplanAuswahl" :items="mapStundenplaene.values()"
										:item-text="s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')'" />
					</div>
					<div />
					<div class="text-left">
						<svws-ui-checkbox v-model="option2" name="Pausenzeiten">Pausenzeiten anzeigen</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4" name="Fachbezeichnungen">Fach- statt Kursbezeichnung verwenden (nicht Sek-II)</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option8" name="Fachkuerzel">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox>
					</div>
					<div>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="gruppeDruck" name="Ausgabe" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="gruppeDruck" name="Ausgabe" label="Einzelausdruck einseitig" />
						</svws-ui-radio-group>
					</div>
				</svws-ui-input-wrapper>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="stundenplanAuswahl === undefined" @click="downloadPDF" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Drucken
					</svws-ui-button>
				</template>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Klassen werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div class="w-full">
					<span v-if="alleKlassenLeer">Alle ausgewählten Klassen sind bereit zum Löschen.</span>
					<span v-if="leereKlassenVorhanden">Einige Klassen haben noch Schüler, leere Klassen können gelöscht werden.</span>
					<div v-if="!alleKlassenLeer">
						<span v-for="message in nichtAlleKlassenLeer" :key="message" class="text-ui-danger"> {{ message }} <br> </span>
					</div>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="manager().getKlassenIDsMitSchuelern().size() === manager().liste.auswahlSize() || loading"
						:title="leereKlassenVorhanden ? 'Leere Klassen löschen' : 'Löschen'" @click="entferneKlassen" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						{{ leereKlassenVorhanden ? 'Leere Klassen löschen' : 'Löschen' }}
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
	import type { KlassenGruppenprozesseProps } from "./SKlassenGruppenprozesseProps";
	import {ListUtils, StundenplanListeEintrag} from "@core";
	import { ArrayList, BenutzerKompetenz, DateUtils, ReportingParameter, ReportingReportvorlage, type List } from "@core";

	type Action = 'printListe' | 'printStundenplan' | 'delete' | '';

	const props = defineProps<KlassenGruppenprozesseProps>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));
	const hatKompetenzDrucken = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));

	const currentAction = ref<Action>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const alleKlassenLeer = computed(() => (currentAction.value === 'delete') && props.manager().getKlassenIDsMitSchuelern().isEmpty());

	const nichtAlleKlassenLeer = computed(() => {
		const errorLog: List<string> = new ArrayList<string>();
		if (!alleKlassenLeer.value)
			for (const klasse of props.manager().getKlassenIDsMitSchuelern())
				errorLog.add(`Klasse ${props.manager().liste.get(klasse)?.kuerzel ?? '???'} (ID: ${klasse}) kann nicht gelöscht werden, da ihr noch Schüler zugeordnet sind.`);
		return errorLog;
	})

	const leereKlassenVorhanden = computed(() =>
		!alleKlassenLeer.value && (props.manager().getKlassenIDsMitSchuelern().size() !== props.manager().liste.auswahlSize()));

	function setCurrentAction(newAction: Action, open: boolean) {
		if(newAction === oldAction.value.name && !open)
			return;
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

	async function entferneKlassen() {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteKlassen();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
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
		const listeIdsKlassen = new ArrayList<number>();
		for (const klasse of props.manager().liste.auswahl())
			listeIdsKlassen.add(klasse.id);
		switch (currentAction.value) {
			case 'printListe':
				reportingParameter.reportvorlage = ReportingReportvorlage.KLASSEN_v_KLASSE_SCHUELER_STAMMDATENLISTE.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsKlassen;
				reportingParameter.einzelausgabeHauptdaten = (((gruppeDruck.value === 2) || (gruppeDruck.value === 4)) ? true : false);
				reportingParameter.einzelausgabeDetaildaten = false;
				break;
			case 'printStundenplan':
				if (stundenplanAuswahl.value === undefined)
					return;
				reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_KLASSEN_STUNDENPLAN.getBezeichnung();
				reportingParameter.idsHauptdaten = ListUtils.create1(stundenplanAuswahl.value.id);
				reportingParameter.idsDetaildaten = listeIdsKlassen;
				reportingParameter.einzelausgabeHauptdaten = false;
				reportingParameter.einzelausgabeDetaildaten = ((gruppeDruck.value === 2) ? true : false);
				break;
			default:
				return;
		}
		reportingParameter.detailLevel = ((((gruppeDruck.value === 3) || (gruppeDruck.value === 4)) ? 1 : 0)
				+ (option2.value ? 2 : 0) + (option4.value ? 4 : 0) + (option8.value ? 8 : 0)
				+ (option16.value ? 16 : 0) + (option32.value ? 32 : 0) + (option64.value ? 64 : 0)
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
</script>
