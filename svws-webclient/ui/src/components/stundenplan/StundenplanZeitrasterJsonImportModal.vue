<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" type="danger" class="hidden">
		<template #modalTitle>Zeitraster importieren</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<div>Beim Import werden alle bisher für den Stundenplan angelegten Zeitraster entfernt und durch die importierten Zeitraster ersetzt.</div>
				<input type="file" accept=".json" @change="onFileChanged" :disabled="loading" class="w-full">
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="danger" @click="importFile" :disabled="loading"><svws-ui-spinner :spinning="loading" /> Importieren </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from "vue";
	import type { StundenplanManager } from "../../../../core/src/core/utils/stundenplan/StundenplanManager";
	import type { StundenplanZeitraster } from "../../../../core/src/core/data/stundenplan/StundenplanZeitraster";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		addZeitraster: (list: Iterable<StundenplanZeitraster>) => Promise<void>;
		removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const file = ref<File | null>(null);

	const openModal = () => {
		show.value = true;
	}

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target.files) {
			file.value = target.files[0];
		}
		loading.value = false;
		status.value = undefined;
	}

	async function importFile() {
		if (!file.value)
			return;
		status.value = undefined;
		loading.value = true;
		const json = await file.value.text();
		const zeitraster: StundenplanZeitraster[] = JSON.parse(json);
		if (zeitraster.length > 0) {
			await props.removeZeitraster(props.stundenplanManager().getListZeitraster());
			const list = new ArrayList<StundenplanZeitraster>();
			for (const e of zeitraster) {
				e.id = -1;
				list.add(e);
			}
			await props.addZeitraster(list);
		}
		loading.value = false;
		file.value = null;
		show.value = false;
	}
</script>
