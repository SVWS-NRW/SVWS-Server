<template>
	<div class="page--content">
		<div v-if="ServerMode.DEV.checkServerMode(serverMode)" class="flex flex-col gap-y-16 lg:gap-y-16">
			<svws-ui-action-button v-if="hatKompetenzLoeschen" title="Löschen" description="Ausgewählte Einwilligungsarten werden gelöscht." icon="i-ri-delete-bin-line"
				:action-function="entferneEinwilligungsarten" :action-label="leereEinwilligungsartenartenVorhanden ? 'Leere Einwilligungsarten löschen' : 'Löschen'" :is-loading="loading" :is-active="currentAction === 'delete'"
				:action-disabled="manager().getEinwilligungsartenIDsMitSchuelern().size() === manager().liste.auswahlSize()" @click="toggleDeleteEinwilligungsarten">
				<span v-if="alleEinwilligungsartenLeer">Alle ausgewählten Einwilligungsarten sind bereit zum Löschen.</span>
				<span v-if="leereEinwilligungsartenartenVorhanden">Einige Einwilligungsarten haben noch Schüler, leere Einwilligungsarten können gelöscht werden.</span>
				<div v-if="!alleEinwilligungsartenLeer">
					<span v-for="message in nichtAlleEinwilligungsartenLeer" :key="message" class="text-error"> {{ message }} <br> </span>
				</div>
			</svws-ui-action-button>
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

	function toggleDeleteEinwilligungsarten() {
		currentAction.value = (currentAction.value === 'delete') ? '' : 'delete';
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
