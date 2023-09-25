<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="medium" class="hidden">
		<template #modalTitle>Religion Hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select v-model="religion" title="Statistikkürzel" :items="list" :item-text="i => i.kuerzel || 'ohne Kürzel'" required />
				<svws-ui-text-input v-model="religion.kuerzel" type="text" placeholder="Kürzel" />
				<svws-ui-text-input v-model="religion.text" type="text" placeholder="Bezeichnung" />
				<svws-ui-text-input v-model="religion.textZeugnis" type="text" placeholder="Zeugnisbezeichnung" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()" :disabled="!religion.kuerzel"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { Religion, ReligionEintrag } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		addEintrag: (religion: ReligionEintrag) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const religion = ref(new ReligionEintrag());

	const list: ReligionEintrag[] = [];
	for (const r of Religion.values()) {
		const re = new ReligionEintrag();
		re.kuerzel = r.daten.kuerzel;
		re.text = r.daten.bezeichnung;
		re.textZeugnis = r.daten.bezeichnung;
		list.push(re);
	}

	async function saveEntries() {
		await props.addEintrag(religion.value);
		showModal().value = false;
		religion.value = new ReligionEintrag();
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
