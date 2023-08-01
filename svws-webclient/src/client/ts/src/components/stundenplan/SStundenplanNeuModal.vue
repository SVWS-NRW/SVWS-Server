<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Stundenplan hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<SvwsUiTextInput type="text" v-model="item.bezeichnung" required placeholder="Bezeichnung" />
				<SvwsUiTextInput type="date" v-model="item.gueltigAb" required placeholder="Beginn" />
				<SvwsUiTextInput type="date" v-model="item.gueltigBis" required placeholder="Ende" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.bezeichnung || !item.gueltigAb || !item.gueltigBis"> Stundenplan Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { StundenplanListeEintrag } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addStundenplan: (eintrag: StundenplanListeEintrag) => Promise<void>;
	}>();

	const modal = ref();
	const item = ref<StundenplanListeEintrag>(new StundenplanListeEintrag());

	const openModal = () => {
		modal.value.openModal();
	}
	async function importer() {
		await props.addStundenplan(item.value);
		item.value = new StundenplanListeEintrag();
		modal.value.closeModal();
	}
</script>
