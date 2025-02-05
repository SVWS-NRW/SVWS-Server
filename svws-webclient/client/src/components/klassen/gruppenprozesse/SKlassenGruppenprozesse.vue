<template>
	<div class="page--content">
		<div class="flex flex-col gap-y-16 lg:gap-y-16">
			<svws-ui-action-button v-if="hatKompetenzLoeschen" title="Löschen" description="Ausgewählte Klassen werden gelöscht." icon="i-ri-delete-bin-line"
				:action-function="entferneKlassen" :action-label="leereKlassenVorhanden ? 'Leere Klassen löschen' : 'Löschen'" :is-loading="loading" :is-active="currentAction === 'delete'"
				:action-disabled="manager().getKlassenIDsMitSchuelern().size() === manager().liste.auswahlSize()" @click="toggleDeleteKlassen">
				<span v-if="alleKlassenLeer">Alle ausgewählten Klassen sind bereit zum Löschen.</span>
				<span v-if="leereKlassenVorhanden">Einige Klassen haben noch Schüler, leere Klassen können gelöscht werden.</span>
				<div v-if="!alleKlassenLeer">
					<span v-for="message in nichtAlleKlassenLeer" :key="message" class="text-ui-danger"> {{ message }} <br> </span>
				</div>
			</svws-ui-action-button>
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
	import { ArrayList, BenutzerKompetenz, type List } from "@core";

	const props = defineProps<KlassenGruppenprozesseProps>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const currentAction = ref<string>('');
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

	function toggleDeleteKlassen() {
		currentAction.value = currentAction.value === 'delete' ? '' : 'delete';
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

</script>
