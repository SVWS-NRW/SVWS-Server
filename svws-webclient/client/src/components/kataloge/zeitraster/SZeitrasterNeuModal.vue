<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal">
		<template #modalTitle>Zeitraster hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-select :model-value="Wochentag.fromIDorException(item.wochentag)" @update:model-value="item.wochentag=$event.id" :items="Wochentag.values()" :item-text="i=>i.beschreibung" required placeholder="Wochentag" />
				<svws-ui-text-input type="number" v-model="item.unterrichtstunde" required placeholder="Stunde" />
				<svws-ui-text-input type="number" v-model="item.stundenbeginn" placeholder="Beginn" />
				<svws-ui-text-input type="number" v-model="item.stundenende" placeholder="Ende" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.wochentag || !item.unterrichtstunde"> Zeitraster Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { StundenplanZeitraster, Wochentag } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addZeitraster: (raum: StundenplanZeitraster) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const item = ref<StundenplanZeitraster>(new StundenplanZeitraster());

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addZeitraster(item.value);
		item.value = new StundenplanZeitraster();
		showModal().value = false;
	}

</script>
