<template>
	<svws-ui-modal :show="show" @update:show="value => emit('update:show', value)" size="small" class="hidden">
		<template #modalTitle>Hinweis</template>
		<template #modalDescription>
			{{ text }}
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="emit('update:show', false)">{{ abbrechenText }}</svws-ui-button>
			<svws-ui-button v-if="jumpTo !== undefined" type="primary" @click="jump_to">{{ jumpToText }}</svws-ui-button>
			<svws-ui-button v-if="weiter !== undefined" type="primary" @click="weiter();emit('update:show', false)">{{ weiterText }}</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	const props = withDefaults(defineProps<{
		show: boolean;
		text: string | undefined;
		jumpTo?: () => Promise<void>;
		weiter?: () => void;
		jumpToText?: string;
		weiterText?: string;
		abbrechenText?: string;
	}>(), {
		jumpTo: undefined,
		weiter: undefined,
		weiterText: "weiter",
		jumpToText: "springen",
		abbrechenText: "abbrechen",
	});

	const emit = defineEmits<{
		"update:show": [show: boolean];
	}>();

	async function jump_to() {
		emit('update:show', false);
		await props.jumpTo!();
	}

</script>
