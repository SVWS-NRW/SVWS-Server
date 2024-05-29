<template>
	<svws-ui-modal :show="showModal" type="danger" size="medium">
		<template #modalTitle>Achtung</template>
		<template #modalContent>
			Die Umstellung des Wochentyp-Modells auf den Wert {{ wochenTypModell }} {{ wochenTypModell <= 4 ? `(${modelle[wochenTypModell]})` : '' }}
			führt dazu, dass Unterricht mit den Wochentypen größer als {{ stundenplanManager().getWochenTypModell() }} {{ stundenplanManager().getWochenTypModell() <= 4 ? `(${modelle[stundenplanManager().getWochenTypModell()]})` : '' }} zu Unterricht in allen Wochen umgewandelt wird.
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="emit('change', undefined)"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="emit('change', true)"> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { StundenplanManager } from "@core";

	const props = defineProps<{
		wochenTypModell: number;
		stundenplanManager: () => StundenplanManager;
	}>();

	const modelle = ['keins', null, 'AB-Wochen', 'ABC-Wochen', 'ABCD-Wochen', 'weitere'];

	const emit = defineEmits<{
		change: [val: boolean];
	}>()

	const _showModal = ref<boolean>(true);
	const showModal = () => _showModal;

</script>
