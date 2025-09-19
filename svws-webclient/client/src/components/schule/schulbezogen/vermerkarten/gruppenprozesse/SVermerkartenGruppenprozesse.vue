<template>
	<div class="page page-grid-cards">
		<div v-if="ServerMode.DEV.checkServerMode(serverMode)" class="flex flex-col gap-y-16 lg:gap-y-16">
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Vermerkarten werden gelöscht"
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="alleVermerkartenLeer">Alle ausgewählten Vermerkarten sind bereit zum Löschen.</span>
					<span v-if="leereVermerkartenVorhanden">Einige Vermerkarten haben noch Schüler, leere Vermerkarten können gelöscht werden.</span>
					<div v-if="!alleVermerkartenLeer">
						<span v-for="message in nichtAlleVermerkartenLeer" :key="message" class="text-ui-danger"> {{ message }} <br> </span>
					</div>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="manager().getVermerkartenIDsMitSchuelern().size() === manager().liste.auswahlSize() || loading"
						title="Löschen" @click="entferneVermerkarten" :is-loading="loading" class="mt-4">
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
			<svws-ui-todo title="Vermerkarten - Gruppenprozesse">
				Dieser Bereich ist noch in Entwicklung. Hier werden später Gruppenprozesse zu den Vermerkartenn vorhanden sein.
			</svws-ui-todo>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SchuleVermerkartenGruppenprozesseProps } from "./SVermerkartenGruppenprozesseProps";
	import { ArrayList, BenutzerKompetenz, type List, ServerMode } from "@core";

	const props = defineProps<SchuleVermerkartenGruppenprozesseProps>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));

	const currentAction = ref<string>('');
	const oldAction = ref<{ name: string | undefined; open: boolean }>({
		name: undefined,
		open: false,
	});
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const alleVermerkartenLeer = computed(() => (currentAction.value === 'delete') && props.manager().getVermerkartenIDsMitSchuelern().isEmpty());

	const nichtAlleVermerkartenLeer = computed(() => {
		const errorLog: List<string> = new ArrayList<string>();
		if (alleVermerkartenLeer.value === false)
			for (const vermerkart of props.manager().getVermerkartenIDsMitSchuelern())
				errorLog.add(`Vermerkart ${props.manager().liste.get(vermerkart)?.bezeichnung ?? '???'} (ID: ${vermerkart}) kann nicht gelöscht werden, da ihr noch Schüler zugeordnet sind.`);
		return errorLog;
	})

	const leereVermerkartenVorhanden = computed(() =>
		(alleVermerkartenLeer.value === false) && (props.manager().getVermerkartenIDsMitSchuelern().size() !== props.manager().liste.auswahlSize()));

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

	async function entferneVermerkarten() {
		loading.value = true;
		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';
		loading.value = false;
	}

</script>
