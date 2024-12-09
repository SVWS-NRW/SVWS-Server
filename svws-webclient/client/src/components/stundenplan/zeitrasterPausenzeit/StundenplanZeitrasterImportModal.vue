<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" type="danger" class="hidden">
		<template #modalTitle>Zeitraster importieren</template>
		<template #modalContent>
			Beim Import der Katalogzeitraster werden alle bisher für den Stundenplan angelegten Zeitraster entfernt und durch Zeitraster aus dem Schulkatalog ersetzt.
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="danger" @click="importer()"> Importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from "vue";
	import type { StundenplanManager} from "@core";
	import type { StundenplanZeitraster } from "@core";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		importZeitraster: () => Promise<void>;
		removeZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	const openModal = () => {
		show.value = true;
	}

	async function importer() {
		const list = props.stundenplanManager().getListZeitraster();
		await props.removeZeitraster(list);
		await props.importZeitraster();
		show.value = false;
	}

</script>
