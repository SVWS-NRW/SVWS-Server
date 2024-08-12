<template>
	<svws-ui-modal :show="show" size="small" class="hidden">
		<template #modalTitle>Hinweis</template>
		<template #modalDescription>
			{{ text }}
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="props.show().value = false">{{ abbrechen_text }}</svws-ui-button>
			<svws-ui-button v-if="jumpTo !== undefined" type="primary" @click="jump_to">{{ jumpTo_text }}</svws-ui-button>
			<svws-ui-button v-if="weiter !== undefined" type="primary" @click="weiter();props.show().value = false;">{{ weiter_text }}</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { Ref} from 'vue';

	const props = withDefaults(defineProps<{
		show: () => Ref<boolean>;
		text: string | undefined;
		jumpTo?: () => Promise<void>;
		weiter?: () => void;
		jumpTo_text?: string;
		weiter_text?: string;
		abbrechen_text?: string;
	}>(), {
		jumpTo: undefined,
		weiter: undefined,
		weiter_text: "weiter",
		jumpTo_text: "springen",
		abbrechen_text: "abbrechen",
	});

	async function jump_to() {
		props.show().value = false;
		await props.jumpTo!();
	}


</script>
