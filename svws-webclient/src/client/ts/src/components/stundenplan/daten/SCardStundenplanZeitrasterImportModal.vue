<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal" type="danger" class="hidden">
		<template #modalTitle>Zeitraster importieren</template>
		<template #modalContent>
			Beim Import der Katalogzeitraster werden alle bisher für den Stundenplan angelegten Zeitraster entfernt und durch Zeitraster aus dem Schulkatalog ersetzt.
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="danger" @click="importer()"> Importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { StundenplanManager} from "@core";
	import type { StundenplanZeitraster } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		importZeitraster: () => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

	const modal = ref();

	const openModal = () => {
		modal.value.openModal();
	}

	async function importer() {
		const list = props.stundenplanManager().getListZeitraster();
		await props.removeZeitraster([...list]);
		await props.importZeitraster();
		modal.value.closeModal();
	}

</script>
