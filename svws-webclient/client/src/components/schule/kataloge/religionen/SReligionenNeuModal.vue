<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium" class="hidden">
		<template #modalTitle>Religion Hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-wrapper>
					<svws-ui-select v-model="religion" title="Statistikkürzel" :items="list" :item-text="i => i.kuerzel || '--- bitte auswählen ---'" required />
				</svws-ui-input-wrapper>
				<svws-ui-text-input v-model="religion.text" type="text" placeholder="Bezeichnung" />
				<svws-ui-text-input v-model="religion.textZeugnis" type="text" placeholder="Zeugnisbezeichnung" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()" :disabled="!religion.kuerzel"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import { Religion, ReligionEintrag } from '@core';

	const props = defineProps<{
		addEintrag: (religion: ReligionEintrag) => Promise<void>;
		schuljahr: number;
	}>();

	const show = ref<boolean>(false);

	const religion = ref(new ReligionEintrag());

	const list: ReligionEintrag[] = [];
	for (const r of Religion.values()) {
		const re = new ReligionEintrag();
		re.kuerzel = r.daten(props.schuljahr)?.kuerzel ?? '—';
		re.text = r.daten(props.schuljahr)?.text ?? '—';
		re.textZeugnis = r.daten(props.schuljahr)?.text ?? '—';
		list.push(re);
	}

	async function saveEntries() {
		await props.addEintrag(religion.value);
		show.value = false;
	}

	const openModal = () => {
		religion.value = new ReligionEintrag();
		show.value = true;
	}

</script>
