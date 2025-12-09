<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div v-if="ServerMode.DEV.checkServerMode(serverMode)" class="flex flex-col gap-4">
			<ui-card icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Konfessionen werden gelöscht." v-if="hatKompetenzLoeschen">
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="!checkBeforeDelete().isEmpty() || isLoading" title="Löschen" @click="deleteKonfessionen" :is-loading class="mt-4">
						<svws-ui-spinner v-if="isLoading" spinning />
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
	</div>
</template>

<script setup lang="ts">

	import type { KonfessionenGruppenprozesseProps } from "~/components/schule/kataloge/konfessionen/gruppenprozesse/KonfessionenGruppenprozesseProps";
	import type { List } from "@core";
	import { computed, ref } from "vue";
	import { BenutzerKompetenz, ServerMode } from "@core";

	const props = defineProps<KonfessionenGruppenprozesseProps>();
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const hatIrgendwelcheKompetenzen = computed(() => hatKompetenzLoeschen.value);

	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	function clearLog() {
		isLoading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function deleteKonfessionen() {
		isLoading.value = true;
		const [delStatus, logMessages] = await props.delete();
		logs.value = logMessages;
		status.value = delStatus;
		isLoading.value = false;
	}

</script>
