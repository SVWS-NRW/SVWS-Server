<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Zeitraster bearbeiten</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input type="number" v-model="item.unterrichtstunde" required placeholder="Stunde" />
				<svws-ui-text-input v-model="item.stundenbeginn" required placeholder="Stundenbeginn" />
				<svws-ui-text-input v-model="item.stundenbeginn" placeholder="Stundenende" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.unterrichtstunde"> Zeitraster Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { StundenplanZeitraster } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		zeitraster?: StundenplanZeitraster;
		multi?: boolean;
		patchZeitraster: (item: StundenplanZeitraster, multi?: boolean) => Promise<void>;
		addZeitraster: (item: StundenplanZeitraster) => Promise<void>;
	}>();

	const modal = ref();
	const item = ref<StundenplanZeitraster>(props.zeitraster || new StundenplanZeitraster());

	const openModal = () => {
		modal.value.openModal();
	}

	async function importer() {
		props.zeitraster
			? await props.patchZeitraster(item.value, props.multi)
			: await props.addZeitraster(item.value);
		modal.value.closeModal();
	}
</script>
