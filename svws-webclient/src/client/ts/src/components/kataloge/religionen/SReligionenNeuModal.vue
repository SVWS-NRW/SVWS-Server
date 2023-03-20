<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal" size="medium">
		<template #modalTitle>Religion Hinzufügen</template>
		<template #modalContent>
			<div class="input-wrapper">
				<svws-ui-multi-select v-model="reli_neu.kuerzel" title="Statistikkürzel" :items="inputKatalogReligionenStatistik"
					:item-text="(i: Religion) => i.daten.kuerzel" required />
				<svws-ui-text-input v-model="reli_neu.kuerzel" type="text" placeholder="Kürzel" />
				<svws-ui-text-input v-model="reli_neu.text" type="text" placeholder="Bezeichnung" />
				<svws-ui-text-input v-model="reli_neu.textZeugnis" type="text" placeholder="Zeugnisbezeichnung" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button v-if="reli_neu.kuerzel || reli_neu.textZeugnis || reli_neu.text" type="secondary" @click="deleteEntries()"> Felder Leeren </svws-ui-button>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>

</template>

<script setup lang="ts">
	import { Religion, ReligionEintrag } from '@svws-nrw/svws-core';
	import { computed, ComputedRef, reactive, ref } from 'vue';

	const props = defineProps<{

		addEintrag: (religion: ReligionEintrag) => Promise<void>;
	}>();

	const modal = ref();
	const reli_neu: ReligionEintrag = reactive(new ReligionEintrag());

	const inputKatalogReligionenStatistik: ComputedRef<Religion[] | undefined> = computed(() => Religion.values());

	async function saveEntries() {
		if (reli_neu.kuerzel) {
			modal.value.closeModal();
			await props.addEintrag(reli_neu);
		} else {
			alert("Kürzel darf nicht leer sein");
		}
		deleteEntries();
	}

	function deleteEntries() {
		reli_neu.kuerzel = null;
		reli_neu.text = null;
		reli_neu.textZeugnis = null;
	}

	const openModal = () => {
		modal.value.openModal();
	}
</script>