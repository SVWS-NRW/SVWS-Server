<template>
	<svws-ui-modal :show="show" @update:show="value => emit('update:show', value)" size="small" class="hidden">
		<template #modalTitle>Hinweis</template>
		<template #modalDescription>
			{{ text }}
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="emit('update:show', false)">{{ abbrechen_text }}</svws-ui-button>
			<svws-ui-button v-if="jumpTo !== undefined" type="primary" @click="jump_to">{{ jumpTo_text }}</svws-ui-button>
			<svws-ui-button v-if="weiter !== undefined" type="primary" @click="weiter();emit('update:show', false)">{{ weiter_text }}</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	const props = withDefaults(defineProps<{
		show: boolean;
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

	const emit = defineEmits<{
		"update:show": [show: boolean];
	}>();

	async function jump_to() {
		emit('update:show', false);
		await props.jumpTo!();
	}

</script>
