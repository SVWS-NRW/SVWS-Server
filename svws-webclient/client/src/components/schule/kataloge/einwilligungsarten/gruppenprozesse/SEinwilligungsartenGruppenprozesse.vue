<template>
	<div class="page page-grid-cards">
		<div v-if="ServerMode.DEV.checkServerMode(serverMode)" class="flex flex-col gap-y-16 lg:gap-y-16">
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Einwilligungsarten werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="alleEinwilligungsartenLeer">Alle ausgewählten Einwilligungsarten sind bereit zum Löschen.</span>
					<span v-if="leereEinwilligungsartenartenVorhanden">Einige Einwilligungsarten haben noch Schüler, leere Einwilligungsarten können gelöscht werden.</span>
					<div v-if="!alleEinwilligungsartenLeer">
						<span v-for="message in nichtAlleEinwilligungsartenLeer" :key="message" class="text-ui-danger"> {{ message }} <br> </span>
					</div>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="manager().getEinwilligungsartenIDsMitSchuelern().size() === manager().liste.auswahlSize() || loading"
						title="Löschen" @click="entferneEinwilligungsarten" :is-loading="loading" class="mt-4">
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
			<svws-ui-todo title="Einwilligungsarten - Gruppenprozesse">
				Dieser Bereich ist noch in Entwicklung. Hier werden später Gruppenprozesse zu den Einwilligungsarten vorhanden sein.
			</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import { ArrayList, BenutzerKompetenz, type List, ServerMode } from "@core";
	import type {
		SchuleEinwilligungsartenGruppenprozesseProps,
	} from "~/components/schule/kataloge/einwilligungsarten/gruppenprozesse/SEinwilligungsartenGruppenprozesseProps";

	const props = defineProps<SchuleEinwilligungsartenGruppenprozesseProps>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));

	const currentAction = ref<string>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const alleEinwilligungsartenLeer = computed(() => (currentAction.value === 'delete') && props.manager().getEinwilligungsartenIDsMitSchuelern().isEmpty());

	const nichtAlleEinwilligungsartenLeer = computed(() => {
		const errorLog: List<string> = new ArrayList<string>();
		if (alleEinwilligungsartenLeer.value)
			for (const einwilligungsart of props.manager().getEinwilligungsartenIDsMitSchuelern())
				errorLog.add(`Einwilligungsart ${props.manager().liste.get(einwilligungsart)?.bezeichnung ?? '???'} (ID: ${einwilligungsart}) kann nicht gelöscht werden, da ihr noch Schüler zugeordnet sind.`);
		return errorLog;
	})

	const leereEinwilligungsartenartenVorhanden = computed(() =>
		(alleEinwilligungsartenLeer.value) && (props.manager().getEinwilligungsartenIDsMitSchuelern().size() !== props.manager().liste.auswahlSize()));

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

	async function entferneEinwilligungsarten() {
		loading.value = true;
		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';
		loading.value = false;
	}

</script>
