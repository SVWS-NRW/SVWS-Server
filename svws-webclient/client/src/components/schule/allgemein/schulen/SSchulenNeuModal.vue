<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden">
		<template #modalTitle>Schule hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="item.name" required placeholder="Name" />
				<svws-ui-text-input v-model="item.schulnummerStatistik" required placeholder="Schulnummer" :valid />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.name || !valid(item.schulnummerStatistik)"> Schule hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { SchulEintrag } from "@core";

	const props = defineProps<{
		addEintrag: (schule: Partial<SchulEintrag>) => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	const item = ref<SchulEintrag>(new SchulEintrag());

	const openModal = () => {
		show.value = true;
	}

	async function importer() {
		await props.addEintrag({name: item.value.name, schulnummerStatistik: item.value.schulnummerStatistik});
		item.value = new SchulEintrag();
		show.value = false;
	}

	function valid(schulnummer: string | null) {
		return typeof schulnummer === 'string' && schulnummer.length === 6;
	}
</script>
