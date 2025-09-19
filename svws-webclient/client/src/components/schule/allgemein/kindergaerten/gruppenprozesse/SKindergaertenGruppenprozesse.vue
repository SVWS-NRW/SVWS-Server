<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-4" v-if="ServerMode.DEV.checkServerMode(serverMode)">
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Kindergärten werden gelöscht">
				<template #buttonFooterLeft>
					<svws-ui-button title="Löschen" @click="entferneKindergaerten" :is-loading class="mt-4">
						<svws-ui-spinner v-if="isLoading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { KindergaertenGruppenprozesseProps } from "~/components/schule/allgemein/kindergaerten/gruppenprozesse/SKindergaertenGruppenprozesseProps";
	import type { List} from "@core";
	import { BenutzerKompetenz, ServerMode } from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<KindergaertenGruppenprozesseProps>();

	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN));
	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	async function entferneKindergaerten() {
		isLoading.value = true;
		const [delStatus, logMessages] = await props.deleteKindergaerten();
		logs.value = logMessages;
		status.value = delStatus;
		isLoading.value = false;
	}

	function clearLog() {
		isLoading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

</script>
