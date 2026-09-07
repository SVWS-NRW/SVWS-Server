<!-- eslint-disable vue/no-v-html -->
<template>
	<div v-html="html" class="prose" />
</template>

<script setup lang="ts">
	import { createMarkdownExit } from 'markdown-exit';
	import { computed } from 'vue';

	const md = createMarkdownExit();

	const html = computed(() => md.render(props.text));

	const props = defineProps<{
		text: string;
	}>();

</script>