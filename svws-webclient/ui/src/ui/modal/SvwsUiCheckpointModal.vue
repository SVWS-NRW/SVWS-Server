<template>
	<svws-ui-modal v-model:show="show" :auto-close="false" :close-in-title="false" size="small" type="danger">
		<template #modalTitle><slot name="title">Daten gehen verloren</slot></template>
		<template #modalDescription>
			<div class="text-left">
				<slot name="description">
					Beim Verlassen dieser Seite gehen alle eingetragenen Informationen verloren.<br>
					Wollen Sie die Seite wirklich verlassen?
				</slot>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
			<svws-ui-button type="danger" @click="confirm">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { onMounted, ref } from 'vue';
	import type { Checkpoint } from "./Checkpoint";

	const props = withDefaults(defineProps<{
		checkpoint: Checkpoint;
		continueRouting: () => Promise<unknown>;
	}>(),{ });

	const show = ref<boolean>(false);

	onMounted(() => {
		props.checkpoint.callback = async () => { show.value = true };
	})

	function cancel () {
		show.value = false;
		props.checkpoint.active = true;
	}

	async function confirm() {
		show.value = false;
		await props.continueRouting();
	}

</script>
