<template>
	<Teleport defer to=".svws-ui-header--actions">
		<svws-ui-modal-hilfe> <hilfe-lehrer-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-16" v-if="ServerMode.DEV.checkServerMode(serverMode)">
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
					<svws-ui-button :disabled="(lehrerListeManager().getIdsReferenzierterLehrer().size() === lehrerListeManager().liste.auswahlSize()) || loading"
						title="Löschen" @click="entferneLehrer" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
			<ui-card v-if="hatKompetenzDrucken && mapStundenplaene.size > 0" icon="i-ri-printer-line" title="Stundenplan drucken" subtitle="Drucke die Stundenpläne der ausgewählten Lehrer."
				:is-open="currentAction === 'print'" @update:is-open="isOpen => setCurrentAction('print', isOpen)">
				<div>
					<svws-ui-input-wrapper :grid="2" class="p-2">
						<svws-ui-select title="Stundenplan" v-model="stundenplanAuswahl" :items="mapStundenplaene.values()" :disabled="mapStundenplaene.size === 1"
							:item-text="s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')'" />
						<svws-ui-button-select type="secondary" :dropdown-actions :disabled="stundenplanAuswahl === undefined">
							<template #icon> <svws-ui-spinner spinning v-if="apiStatus.pending" /> <span class="icon i-ri-printer-line" v-else /> </template>
						</svws-ui-button-select>
					</svws-ui-input-wrapper>
				</div>
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
	import { BenutzerKompetenz, DateUtils } from "@core";
	import { ServerMode } from "@core";
	import type { LehrerGruppenprozesseProps, DownloadPDFTypen } from "~/components/lehrer/gruppenprozesse/SLehrerGruppenprozesseProps";

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

	const dropdownActions = [
		{ text: "Stundenplan", action: () => downloadPDF("Stundenplan"), default: true },
		{ text: "Stundenplan mit Pausenaufsichten", action: () => downloadPDF("Stundenplan mit Pausenaufsichten") },
		{ text: "Stundenplan mit Pausenzeiten", action: () => downloadPDF("Stundenplan mit Pausenzeiten") },
	];

	async function downloadPDF(title: DownloadPDFTypen) {
		if (stundenplanAuswahl.value === undefined)
			return;
		const { data, name } = await props.getPDF(title, stundenplanAuswahl.value.id);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
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
