<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Zeitraster hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-multi-select :model-value="Wochentag.fromIDorException(item.wochentag)" @update:model-value="item.wochentag=$event.id" :items="Wochentag.values()" :item-text="i=>i.beschreibung" required placeholder="Wochentag" />
				<SvwsUiTextInput type="number" v-model="item.unterrichtstunde" required placeholder="Stunde" />
				<SvwsUiTextInput type="number" v-model="item.stundenbeginn" placeholder="Beginn" />
				<SvwsUiTextInput type="number" v-model="item.stundenende" placeholder="Ende" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.wochentag || !item.unterrichtstunde"> Zeitraster Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { StundenplanZeitraster, Wochentag } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		addZeitraster: (raum: StundenplanZeitraster) => Promise<void>;
	}>();

	const modal = ref();
	const item = ref<StundenplanZeitraster>(new StundenplanZeitraster());

	const openModal = () => {
		modal.value.openModal();
	}

	async function importer() {
		await props.addZeitraster(item.value);
		item.value = new StundenplanZeitraster();
		modal.value.closeModal();
	}
</script>
